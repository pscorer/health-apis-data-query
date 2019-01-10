#!/usr/bin/env bash

[ -z "$SENTINEL_BASE_DIR" ] && SENTINEL_BASE_DIR=/sentinel
cd $SENTINEL_BASE_DIR
SENTINEL_JAR=$(find -maxdepth 1 -name "sentinel-*.jar" -a -not -name "sentinel-*-tests.jar")
SENTINEL_TEST_JAR=$(find -maxdepth 1 -name "sentinel-*-tests.jar")
SYSTEM_PROPERTIES="-Dwebdriver.chrome.driver=/usr/local/bin/chromedriver -Dwebdriver.chrome.headless=true"
CATEGORY=

usage() {
cat <<EOF
Commands
  list-tests
  list-categories
  test [--category <category>] [-Dkey=value] <name> [name] [...]

Example
  test \
    --category gov.va.health.api.sentinel.categories.Lab \
    -Dlab.client-id=12345 \
    -Dlab.client-secret=ABCDEF \
    -Dlab.user-password=secret \
    gov.va.health.api.sentinel.LabTest

$1
EOF
exit 1
}

doTest() {
  [ -z "$@" ] && usage "No tests specified"
  local filter
  [ -n "$CATEGORY" ] && filter="--filter=org.junit.experimental.categories.IncludeCategories=$CATEGORY"
  java -cp "$(pwd)/*" $SYSTEM_PROPERTIES org.junit.runner.JUnitCore $filter $@
  exit $?
}

doListTests() {
  jar -tf $SENTINEL_TEST_JAR \
    | grep -E '(IT|Test)\.class' \
    | sed 's/\.class//' \
    | tr / . \
    | sort
}

doListCategories() {
  jar -tf $SENTINEL_JAR \
    | grep -E 'gov/va/health/api/sentinel/categories/.*\.class' \
    | sed 's/\.class//' \
    | tr / . \
    | sort
}


ARGS=$(getopt -n $(basename ${0}) \
    -l "category:,debug,help" \
    -o "c:D:h" -- "$@")
[ $? != 0 ] && usage
eval set -- "$ARGS"
while true
do
  case "$1" in
    -c|--category) CATEGORY=$2;;
    -D) SYSTEM_PROPERTIES+=" -D$2";;
    --debug) set -x;;
    -h|--help) usage "halp! what this do?";;
    --) shift;break;;
  esac
  shift;
done

[ $# == 0 ] && usage "No command specified"
COMMAND=$1
shift

case "$COMMAND" in
  t|test) doTest $@;;
  lc|list-categories) doListCategories;;
  lt|list-tests) doListTests;;
  
  *) usage "Unknown command: $COMMAND";;
esac

exit 0
