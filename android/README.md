monaca-framework-android
========================


This framework is part of [Monaca platform](http://monaca.mobi "monaca.mobi") for Android.
<br>
It includes following features.

* Building Hybrid HTML5 application with [PhoneGap](http://phonegap.com/ "PhoneGap").
* Use json file to compose Native Component
* Native Transition with [monaca.js](https://github.com/monaca/monaca.js "monaca.js")


Requirement
-----------
Installed Android SDK 2.1, 3.0 or later.

How To Build
------------

### Clone the project
* git clone git@github.com:monaca/monaca-framework-android.git monaca
* cd monaca && git submodule init && git submodule update

### Import Projects to Eclipse
  1. Import all projects (or at least Cordova, BarcodeScannerLibrary, MonacaUtils, MonacaFramework, and MonacaSandbox)
  4. Run MonacaSandbox project.

Troubleshooting
---------------

### If project build fail
* Make sure you have Google API Level 17 SDK installed (for Cordova project)
* Make sure each project have set the Project Build Target (Right click the project -> Properties -> Android). If there is a tick sign, you are at the right track!
* Try cleaning all projects.
* 
