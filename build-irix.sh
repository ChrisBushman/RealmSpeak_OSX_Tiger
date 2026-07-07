#!/bin/bash
# Build RealmSpeakFull targeting Java 1.4 (SGI IRIX) using ECJ.
#
# Prerequisites:
#   build/irix/ecj.jar  — included in repo
#   build/irix/rt.jar   — NOT included; extract from JDK 1.4.2 (see build/irix/README)
#
# Usage:
#   ./build-irix.sh              # full build
#   ./build-irix.sh clean build  # clean then build

set -e

DIR=$(cd "$(dirname "$0")"; pwd)
ECJ="$DIR/build/irix/ecj.jar"
RT="$DIR/build/irix/rt.jar"

if [ ! -f "$RT" ]; then
  echo "ERROR: $RT not found."
  echo "See build/irix/README for instructions on obtaining rt.jar."
  exit 1
fi

export JAVA_HOME=$(/usr/libexec/java_home -v 1.8)
export PATH="$JAVA_HOME/bin:$PATH"

ant -f "$DIR/build/build.xml" \
    -lib "$ECJ" \
    -Dcompile.source.version=1.4 \
    -Dcompile.target.version=1.4 \
    -Dcompile.bootclasspath="$RT" \
    -Dcompile.compiler=org.eclipse.jdt.core.JDTCompilerAdapter \
    -Dcompile.memoryMaximumSize=512m \
    "${@:-build}"
