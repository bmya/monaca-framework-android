#!/usr/bin/php
<?php

$success = false;

//
// Change directory
//
$ROOT_DIR = realpath(dirname(__FILE__) . "/../");
chdir($ROOT_DIR);

//
// Command line
//
switch (@$_SERVER["argv"][1]) {
case "debug":
  $configuration = "Debug";
  break;
case "release":
  $configuration = "Release";
  break;
case "adhoc":
  $configuration = "Release";
  break;
default:
  echo "build.php debug/release/adhoc\n";
  exit;
}

//
// Unique build id
//
$BUILD_ID = uniqid("");
$CERTIFICATE = current(glob("etc/*.cer"));
$PRIVATE_KEY = current(glob("etc/*.key"));
$PROVISIONING = current(glob("etc/*.mobileprovision"));
$CODE_SIGN = "";

//
// Keychain Tool
//
echo "$ROOT_DIR\n";
try {
  execute('security create-keychain -p "" monaca', false);
} catch (Exception $e) {};
try {
  execute('security default-keychain -s monaca');
} catch (Exception $e) {};
try {
  execute('security unlock-keychain -p "" monaca');
} catch (Exception $e) {};

try {
  #execute('security import ' . $PRIVATE_KEY . ' -k monaca -t priv -f openssl -T /usr/bin/codesign -P ""', false);
  execute('security import ' . $PRIVATE_KEY . ' -k monaca -t priv -T /usr/bin/codesign -P ""', false);
} catch (Exception $e) {
  echo "Import failed: $PRIVATE_KEY\n";
};

try {
  execute('security add-certificate -k monaca ' . $CERTIFICATE);
  execute('cp ' . $PROVISIONING . ' ~/Library/MobileDevice/Provisioning\ Profiles/' . $BUILD_ID . '.mobileprovision');
  execute('security unlock-keychain -p "" monaca');
} catch (Exception $e) {};

try {
  $CODE_SIGN = execute("scripts/get_codesign.php cert $CERTIFICATE", false);
  execute("xcodebuild -sdk iphoneos -configuration $configuration CODE_SIGN_IDENTITY=\"$CODE_SIGN\" clean build");
  exit;
} catch (Exception $e) {
  echo "Build failed\n";
  goto finalize;
}

if ($configuration == 'Debug') {
  $cmd = sprintf('/usr/bin/xcrun -sdk iphoneos PackageApplication -v "%s/build/Debug-iphoneos/MonacaApp.app" -o "%s/build/debug.ipa" --sign "%s" --embed "%s"', $ROOT_DIR, $ROOT_DIR, $CODE_SIGN, $PROVISIONING);
} elseif ($configuration == 'Adhoc') {
  $cmd = sprintf('/usr/bin/xcrun -sdk %s PackageApplication -v "build/Release-iphoneos/MonacaApp.app" -o "%s/release.ipa" --sign "%s" --embed "%s"', $config->sdk, $config->build_dir, $product_name, $config->output_dir, $version, $date, $CODE_SIGN, $PROVISIONING);
} else {
  chdir($config->build_dir . '/build/Release-iphoneos/');
  echo `zip -r "$config->output_dir/app-$version-$date.zip" "$product_name.app"`;
}

execute('security unlock-keychain -p "" monaca');
execute($cmd);

$success = true;

finalize: 

// Delete provisioning
execute('rm -f ~/Library/MobileDevice/Provisioning\ Profiles/' . $BUILD_ID . '.mobileprovision');

// Delete certificate
try {
  //execute('security delete-certificate -c "' . $CODE_SIGN . '"');
} catch (Exception $e) { }

if ($success) {
  exit(0);
} else {
  exit(1);
}

function execute($command, $do_output = true) {
  $output = array();
  if ($do_output) {
    passthru($command . " 2>&1", $return_var);
    echo "\n";
  } else {
    exec($command . " 2>&1", $output, $return_var);
  }

  if ($return_var != 0) {
    throw new Exception("Command execution failed: $command");
  }
  return join("\n", $output);
}
