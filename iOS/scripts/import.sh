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

if [ -z "$PROJECT_ROOT" ]; then
  show_error ""
fi

if [ ! -d $PROJECT_ROOT ]; then
  show_error
fi

COPY_TO=`dirname $0`/../MonacaSkeleton

#
# Copy files
#
RSYNC_OPT="-rptgoD $VERBOSE $DRY_RUN"
rsync $RSYNC_OPT --delete "$PROJECT_ROOT/assets/www/" "$COPY_TO/www/"
rsync $RSYNC_OPT "$PROJECT_ROOT/assets/app.json" "$COPY_TO/app.json"
rsync $RSYNC_OPT "$PROJECT_ROOT/iOS/MonacaSkeleton/" "$COPY_TO/"

#
# Copy certificate
#
if [ -d "$PROJECT_ROOT/etc" ]; then
  rsync $RSYNC_OPT "$PROJECT_ROOT/etc/" "$COPY_TO/../etc/"
fi
