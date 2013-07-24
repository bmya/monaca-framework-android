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

// Get configuration
$config = json_decode(file_get_contents("etc/ios.config"), true);

//
// Unique build id
//
$BUILD_ID = uniqid("");
$CERTIFICATE = current(glob("etc/*.cer"));
$PRIVATE_KEY = current(glob("etc/*.key"));
$PROVISIONING = current(glob("etc/*.mobileprovision"));
$CODE_SIGN = "";
$VERSION = $config["versionName_ios"];
$TARGETED_DEVICE_FAMILY = $config["device_family"];

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
  execute("xcodebuild -sdk iphoneos -configuration $configuration TARGETED_DEVICE_FAMILY=\"$TARGETED_DEVICE_FAMILY\" VERSION=\"$VERSION\" CODE_SIGN_IDENTITY=\"$CODE_SIGN\" clean build");
} catch (Exception $e) {
  echo "Build failed\n";
  goto finalize;
}

if ($configuration == 'Debug') {
  $OUTPUT_FILE = $ROOT_DIR . "/build/debug.ipa";
  $cmd = sprintf('/usr/bin/xcrun -sdk iphoneos PackageApplication -v "%s/build/Debug-iphoneos/MonacaApp.app" -o "%s" --sign "%s" --embed "%s"', $ROOT_DIR, $OUTPUT_FILE, $CODE_SIGN, $PROVISIONING);
} elseif ($configuration == 'Adhoc') {
  $OUTPUT_FILE = $ROOT_DIR . "/build/release.ipa";
  $cmd = sprintf('/usr/bin/xcrun -sdk iphoneos PackageApplication -v "%s/build/Release-iphoneos/MonacaApp.app" -o "%s" --sign "%s" --embed "%s"', $ROOT_DIR, $OUTPUT_FILE, $CODE_SIGN, $PROVISIONING);
} else {
  $OUTPUT_FILE = $ROOT_DIR . "/build/release.zip";
  chdir("$ROOT_DIR/build/Release-iphoneos/");
  echo `zip -r "$OUTPUT_FILE" "MonacaApp.app"`;
}

execute('security unlock-keychain -p "" monaca');
execute($cmd);

echo "#SUCCESS# " . $OUTPUT_FILE . "\n";
$success = true;

finalize: 

// Delete provisioning
execute('rm -f ~/Library/MobileDevice/Provisioning\ Profiles/' . $BUILD_ID . '.mobileprovision');

// Delete certificate
try {
  execute('security delete-certificate -c "' . $CODE_SIGN . '"');
} catch (Exception $e) { }

if ($success) {
  exit(0);
} else {
  exit(1);
}

function execute($command, $do_output = true) {
  $output = array();
  echo $command . "\n\n";
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
