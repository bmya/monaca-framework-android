#!/bin/bash

show_error() {
  echo >&2 "Usage: $0 [-vn] [-d project_dir] ..."
  exit 1
}

VERBOSE="-q"
while getopts "vnd:" flag; do
  case $flag in
    \?) OPT_ERROR=1; break;;
    v) VERBOSE="-v";;
    n) DRY_RUN="-n";;
    d) PROJECT_ROOT="$OPTARG";;
  esac
done

shift $(( $OPTIND - 1 ))

if [ $OPT_ERROR ]; then
  show_error
fi

if [ ! -d $PROJECT_ROOT ]; then
  show_error
fi

if [ -z "$PROJECT_ROOT" ]; then
  show_error ""
fi

COPY_TO=`dirname $0`/../MonacaSandbox

#
# Copy files
#
RSYNC_OPT="-rptgoD $VERBOSE $DRY_RUN"
rsync $RSYNC_OPT --delete "$PROJECT_ROOT/assets/" "$COPY_TO/assets/"
rsync $RSYNC_OPT "$PROJECT_ROOT/android/" "$COPY_TO/"

#
# Copy certificate
#
if [ -d "$PROJECT_ROOT/etc/" ]; then
  rsync $RSYNC_OPT "$PROJECT_ROOT/etc/" "$COPY_TO/../etc/"
  
  # add keystore path
  echo "key.store=$PROJECT_ROOT/etc/keystore.private" >> "$COPY_TO/local.properties"
fi

