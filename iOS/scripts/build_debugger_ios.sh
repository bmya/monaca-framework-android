#!/bin/sh

source config.sh

setup()
{
  APP_NAME="MonacaDebugger"
  APP_ID="mobi.monaca.debugger"
  TARGET="Debugger"
  PROVISIONING="${DEBUGGER_PROVISIONING}"
  SCRIPT_DIR=`pwd`
  CONFIGURATION=`./get_codesign.php prov ${PROVISIONING}`
  APP_CONFIGURATION=Release

  ./import_keychain.sh $CONFIGURATION

  if [ "$CONFIGURATION" = "Debug" ]; then
    CODE_SIGN=`./get_codesign.php cert ${DEV_CERT}`
  else
    CODE_SIGN=`./get_codesign.php cert ${DIST_CERT}`
  fi
}

teardown()
{
  cd ${SCRIPT_DIR}
  ./delete_keychain.sh
}


#
# Build and code sign.
#

setup

cd ${DEBUGGER_BUILD_DIR}

cp "${PROVISIONING}" "${PROVISIONING_DIR}"
if [ $? != 0 ]; then
  teardown
  exit 1
fi 

security unlock-keychain -p "" ${KEYCHAIN}
xcodebuild -project MonacaDebugger.xcodeproj -target ${TARGET} -sdk ${SDK} -configuration ${APP_CONFIGURATION} APP_ID="${APP_ID}" CODE_SIGN_IDENTITY="${CODE_SIGN}" PRODUCT_NAME="${APP_NAME}" VERISON="${VERSION}" clean build

if [ $? != 0 ]; then
  teardown
  exit 1
fi

DATE=`date '+%Y%m%d'`

# Generates ipa.file.
/usr/bin/xcrun -sdk ${SDK} PackageApplication -v "${DEBUGGER_BUILD_DIR}/build/${APP_CONFIGURATION}-iphoneos/${APP_NAME}.app" -o "${DEBUGGER_OUTPUT_DIR}/debugger.ipa" --sign ${CODE_SIGN} --embed ${PROVISIONING}

if [ $? != 0 ]; then
  teardown
  exit 1
fi

teardown

