#!/bin/bash

export PATH=$PATH:/android-sdk/tools/

show_error() {
  echo >&2 "Usage: $0 -t debug/release -i ..."
  echo >&2 "  -t : Build type (debug or release)"
  echo >&2 "  -i : Do init (android update project)"
  echo >&2 "  -c : Do clean before build"
  exit 1
}

while getopts "ict:" flag; do
  case $flag in
    \?) OPT_ERROR=1; break;;
    i) INIT=true;;
    c) CLEAN=true;;
    t) TYPE="$OPTARG";;
  esac
done

shift $(( $OPTIND - 1 ))

if [ $OPT_ERROR ]; then
  show_error
fi

if [ -z "$TYPE" ]; then
  show_error
fi

# Move to project root dir
DEST_DIR=`dirname $0`/../
cd $DEST_DIR
DEST_DIR=`pwd`

# Update project properties
if [ $INIT ]; then
  which android > /dev/null
  
  if [ $? -ne 0 ]; then
    echo "ERROR: Android command not specified in path"
    exit 1
  fi

  android update project -p MonacaUtils
  android update project -p cordova-android/framework
  android update project -p BarcodeScannerLibrary
  android update project -p MonacaFramework
  android update project -p MonacaSandbox
fi

# Do build
cd MonacaSandbox

if [ $CLEAN ]; then
  ant clean
fi

case "$TYPE" in
  "debug" )
    ant debug
    OUTPUT="bin/MonacaActivity-debug.apk"
    ;;
  "release" )
    ant release 
    OUTPUT="bin/MonacaActivity-release.apk"
    ;;
  * ) 
    echo "Please specify either debug or release." ;;
esac

if [ -f $OUTPUT ]; then
  echo "#SUCCESS# $DEST_DIR/MonacaSandbox/$OUTPUT"
fi

