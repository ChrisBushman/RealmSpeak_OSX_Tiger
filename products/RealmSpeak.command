#!/bin/sh
DIR=$(cd "$(dirname "$0")"; pwd)
java -Xmx256m -Xdock:name="RealmSpeak for OS X" -cp "$DIR/RealmSpeakFull.jar" com.robin.magic_realm.RealmSpeak.RealmSpeakFrame
