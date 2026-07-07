#!/bin/sh
# Run RealmSpeak on SGI IRIX (Java 1.4)
cd `dirname $0`
/usr/java2/bin/java -mx256m -Duser.home=. -jar RealmSpeakFull.jar
