#!/bin/sh
DIR=$(cd "$(dirname "$0")"; pwd)
java -Xmx256m -cp "$DIR/RealmSpeakFull.jar" com.robin.magic_realm.RealmSpeak.RealmSpeakFrame
