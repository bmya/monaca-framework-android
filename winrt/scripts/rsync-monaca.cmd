@ECHO OFF
REM *****************************************************************
REM
REM CWRSYNC.CMD - Batch file template to start your rsync command (s).
REM
REM By Tevfik K. (http://itefix.no)
REM *****************************************************************

REM Make environment variable changes local to this batch file
SETLOCAL

REM ** CUSTOMIZE ** Specify where to find rsync and related files (C:\CWRSYNC)
SET CWRSYNCHOME=C:\CWRSYNC

REM Set CYGWIN variable to 'nontsec'. That makes sure that permissions
REM on your windows machine are not updated as a side effect of cygwin
REM operations.
SET CYGWIN=nontsec

REM Set HOME variable to your windows home directory. That makes sure 
REM that ssh command creates known_hosts in a directory you have access.
SET HOME=%HOMEDRIVE%%HOMEPATH%

REM Make cwRsync home as a part of system PATH to find required DLLs
SET CWOLDPATH=%PATH%
SET PATH=%CWRSYNCHOME%\BIN;%PATH%

rsync -e 'ssh -i /cygdrive/c/data/remote_builder/scripts/winrt/id_rsa' -r %1 %2

