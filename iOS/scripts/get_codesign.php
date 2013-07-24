#!/usr/bin/php
<?php

/**
 * Usage: get_codesign.php [cert,prov] "/path/to/cert,prov"
 * @return Codesign embedded in the certificate
 *          or
 *         Whether ther provision is Development / Distribution
 * @author Masahiro Tanaka
 */

if ($argc != 3) {
  // If no arguments are passed
  echo "Usage: get_codesign.php [cert,prov] /path/to/[cert,prov]\n";
  exit(1);
}

if ($argv[1] == "cert") {
  echo parseCert($argv[2]);
} elseif ($argv[1] == "prov") {
  $prov = iOSFileReader::parseMobileProvision($argv[2]);
  switch ($prov["type"]) {
  case "development":
    echo "Debug";
    break;
  case "distribution":
    echo "Release";
    break;
  }
}

exit;

function parseCert($file) {
  $s_command = "openssl x509 -inform DER -in %s -text";
  $command = sprintf($s_command, $file);
  $cert = shell_exec($command);
  $INFORM = "DER";

  if (!trim($cert)) {
    // Try PEM format if DER fails
    $s_command = "openssl x509 -inform PEM -in %s -text";
    $command = sprintf($s_command, $file);
    $cert = shell_exec($command);
    $INFORM = "PEM";
  }


  if (!preg_match("/Subject: (.+)$/im", $cert, $match)) {
    echo "This is not a valid certificate";
    exit(1);
  }
  $subjects = explode(",", $match[1]);
  foreach ($subjects as $subject) {
    $subject = trim($subject);
    if (substr($subject, 0, 3) == "CN=") {
      $codesign = substr($subject, 3);
      break;
    }
  }

  return $codesign;
}

/**
 * Parse and reads iOS specific files
 */
class iOSFileReader {
  
  /**
   * Parse .mobileprovision and returns some meta data
   * @params $file File to parse
   * @return array Array including type and name
   */
  static public function parseMobileProvision($file) {
    
    $data = file_get_contents($file);
    if (!$data) {
      throw new Exception("Cannot load file");
    }
    
    // Extract XML part
    $xml_data = strstr($data, '<' . '?xml version="1.0" encoding="UTF-8"' . '?' . '>');
    $xml_data = substr($xml_data, 0, strpos($xml_data, "</plist>") + strlen("</plist>"));
    
    if (!$xml_data) {
      throw new Exception("Invalid format");
    }
    
    $structure = array();
    
    // Execute XMLReader
    $reader = new XMLReader();
    $reader->XML($xml_data);
    
    while ($reader->read()) {
      if ($reader->nodeType == XMLReader::ELEMENT) {
        switch ($reader->name) {
          case "key":
            $reader->read();
            switch ($reader->value) {
              case "get-task-allow":
                $reader->read();
                $reader->read();
                $reader->read();
                if ($reader->name == "false") {
                  $structure["type"] = "distribution";
                } elseif ($reader->name == "true") {
                  $structure["type"] = "development";
                }
                break;
              case "Name":
                $reader->read();
                $reader->read();
                $reader->read();
                $reader->read();
                $val = $reader->value;
                $structure["name"] = $val;
                break;
            }
            break;
        }
      }
    }
    
    return $structure;
  }
}



