<?php

/** 
 * Windows 8 Build Script
 * @author Masahiro Tanaka
 */

$ROOT_DIR = realpath(dirname(__FILE__) . "/../");

if (!function_exists('glob_recursive')) {
  function glob_recursive($pattern, $flags = 0)
  {
    $files = glob($pattern, $flags);
    foreach (glob(dirname($pattern) . DIRECTORY_SEPARATOR . '*', GLOB_ONLYDIR|GLOB_NOSORT) as $dir) {
      $files = array_merge($files, glob_recursive($dir . DIRECTORY_SEPARATOR . basename($pattern), $flags));
    }
    return $files;
  }
}

// Locate www dir
$www_dir = realpath(dirname(__FILE__) . "/../../src/assets/www");

// Import files
mkdir($www_dir . "/_monaca_winrt");
$files = glob_recursive($www_dir . "/../../winrt/*");
foreach ($files as $file) {
  $filename = basename($file);
  copy($file, $www_dir . "/_monaca_winrt/$filename");
}

// Copy other file
copy($www_dir . "/../../etc/debug.pfx", $www_dir . "/_monaca_winrt/app_debug.pfx");
$winrt_config = json_decode($www_dir . "/../../etc/winrt.config");

// List files
$files = glob_recursive($www_dir . DIRECTORY_SEPARATOR . "*.*");

$content_xml = "";
foreach ($files as $file) {
  
  if (strstr($file, "\\AppPackages\\")) continue;

  $FILENAME = $file;
  $FILENAME = str_replace($www_dir . "\\", "", $FILENAME);
  $FILENAME = str_replace('"', '\"', $FILENAME);
  
  switch (strrchr(strtolower($FILENAME), ".")) {
  case ".jsproj":
  case ".appxmanifest":
    break;
  case ".pfx":
    $content_xml .= "<None Include=\"$FILENAME\" />\r\n";
    break;
  default:
    $content_xml .= "<Content Include=\"$FILENAME\" />\r\n";
  }
}

// Copy WinRT Build Configuration Files
// Generate jsproj file
$JSPROJ_FILE = dirname(__FILE__) . "/MonacaApp.jsproj";
$jsproj = file_get_contents($JSPROJ_FILE);

$replace = array();
$replace["PROJECT_GUID"] = $winrt_config["identity_name_winrt"];
$replace["DEFAULT_LANGUAGE"] = "ja-JP";
$replace["PACKAGE_CERTIFICATE_KEY_FILE"] = "_monaca_winrt\\app_debug.pfx";
$replace["MANIFEST_FILE"] = "_monaca_winrt\\package.appxmanifest";
$replace["CONTENT_XML"] = $content_xml;

foreach ($replace as $key => $value) {
  $jsproj = str_replace("%%%" . $key . "%%%", $value, $jsproj);
}
file_put_contents($www_dir . DIRECTORY_SEPARATOR . basename($JSPROJ_FILE), $jsproj);

// Wait for a second
clearstatcache();
sleep(1);

// Do build
$MSBUILD_PATH = "C:\\Windows\\Microsoft.NET\\Framework64\\v4.0.30319\\MSBuild";
chdir($www_dir);
passthru($MSBUILD_PATH, $return_var);

// Check if build is successful
if ($return_var != 0) {
  echo "MSBuild Failed\n";
  exit(1);
}
$files = glob($www_dir . "\\AppPackages\\*.appxupload");
if (count($file) != 1) {
  echo "Build failed\n";
  exit(1);
}

mkdir($ROOT_DIR . "/build");
$OUTPUT_FILE = $ROOT_DIR . "/build/release.zip";
chdir("$www_dir/AppPackages/");
$command = "zip -rq $OUTPUT_FILE *'";

passthru($command);

if (!file_exists($OUTPUT_FILE)) {
  exit(1);
}

echo "#SUCCESS# $OUTPUT_FILE\n";

exit(0);
