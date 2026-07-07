# RealmSpeak

RealmSpeak is a java application that allows you to play Avalon Hill's Magic Realm the boardgame online with friends,
or as a solitaire game.

# How to install

Contained herein is all the code and resources you need to build RealmSpeak from scratch.  If you follow these instructions, it should be pretty easy to accomplish.

1.  Install [ant](http://ant.apache.org/) - At the time of this writing, I'm using ant 1.8.0.

2.  Install [Java JDK](https://www.oracle.com/java/technologies/downloads/) - You probably already have the JRE installed (at least), but you really should have the JDK.  On the website, look for Downloads, and choose Java SE.  From there, find the latest JDK.  At the time of this writing, the latest is called "JDK 6 Update 13".  You don't need JavaFX, JavaEE, or NetBeans, so don't download those unless you have a good reason to.

WARNING:  BE VERY CAREFUL EDITING THE PATH VARIABLE!!  IF YOU DELETE SOMETHING IMPORTANT, YOU MAY KILL INSTALLED APPS!!!  MAKE A COPY OF THE PATH BEFORE YOU START EDITING!!!!  YOU HAVE BEEN WARNED!!!!!!!!!!!!!!!!!!

3.  Add the ant/bin and java/bin directories to your path variable (Windows). To append (globally and permanently) a path to your current Windows PATH environmental variable one should follow the instructions [here](https://www.java.com/en/download/help/path.html).

  My path variable looks like this: `%SystemRoot%\system32;%SystemRoot%;C:\apache-ant-1.10.8\bin;C:\Program Files\AdoptOpenJDK\jdk-8.0.265.01-hotspot\bin;`

WARNING:  BE VERY CAREFUL EDITING THE PATH VARIABLE!!  IF YOU DELETE SOMETHING IMPORTANT, YOU MAY KILL INSTALLED APPS!!!  MAKE A COPY OF THE PATH BEFORE YOU START EDITING!!!!  YOU HAVE BEEN WARNED!!!!!!!!!!!!!!!!!!

4.  Open a console window (Start->Run...->Type "cmd" and press OK), and navigate to the "build" directory.

5.  Create a build file by typing the following:

	`ant -buildfile generate-build.xml` (this should take less than 5 seconds)

6.  Do you see the words "BUILD SUCCESSFUL"?  If no, then something's wrong.  e-mail me.  If yes, then continue to 7

7.  Do a full build of all projects by typing the following:

	`ant`

  Yes, you just type ant. This will take a bit longer (could be minutes, depending on your computer). [To build RealmSpeak by itself, `ant clean-build-RealmSpeakFull`].

8. Do you see the words "BUILD SUCCESSFUL"?  If yes, then all worked as expected.

9. Navigate to the "products" directory, and you should see a bunch of jar files, including RealmSpeakFull.jar.

10. Double-click the run.bat file, and RealmSpeak should launch.  If it doesn't, e-mail me.


Robin (aka DewKid)


# Hints (by sch4fchen):

First time to run the game:
1. You need to copy the .jar files from "libraries" directory, to "products" directory.

2. Quests (*.rsqst) must be copied into the folder .../products/quests" (without subdirectory).

3. Custom characters (*.rschar) must be copied into the folder .../products/characters".

Developer hint: I used Eclipse as IDE, building with ant 1.10.8 and OpenJDK 8

Look in the Documents subfolder for instructions about getting it all to build through "ant".
Once you have ant and a java JDK installed, it's a simple two commands to get the RealmSpeakFull.jar.
Copy the libraries (only once required). Then you just click run.bat.
Not a good way to modify RealmSpeak, but a great way to get the final product when you are done debugging.

To understand all the dependencies, take a look at the file build/project-list.xml. Scroll to the very bottom,
and note the project "RealmSpeakFull". This is RealmSpeak in all it's glory! You should be able to work backward
from that to figure out how to setup projects in your favorite IDE.

sch4fchen


# Building for OS X Tiger (Java 1.5)

This fork targets **Java 1.5 (Tiger)** for compatibility with OS X Tiger (Mac OS X 10.4).
The build compiles with `-source 1.5 -target 1.5` and a bootclasspath pointing to the
Tiger runtime jars bundled in `libraries/classes.jar` and `libraries/ui.jar`.

Because modern JDKs have dropped support for `-source 1.5`, you must build with
**Temurin 8** (the last JDK that still accepts `-source 1.5`).

### Prerequisites

1. Install [Apache Ant](http://ant.apache.org/) (1.8 or later).

2. Install Temurin 8 JDK. On macOS with Homebrew:

   ```
   brew install --cask temurin@8
   ```

   Or download it from [Adoptium](https://adoptium.net/temurin/releases/?version=8).

### Build

Point `JAVA_HOME` at Temurin 8 and run Ant:

```sh
export JAVA_HOME=$(/usr/libexec/java_home -v 1.8)
export PATH="$JAVA_HOME/bin:$PATH"
ant -f build/build.xml build-all-projects
```

A successful build prints `BUILD SUCCESSFUL` and produces jar files in the `products/` directory.

### Run

```sh
cd products
java -jar RealmSpeakFull.jar
```

Or on Tiger, double-click `RealmSpeak.command` / launch directly via `java -jar`.

# Building for Java 1.4 (Windows 98 / IRIX)

This branch (`irix-java14`) targets **Java 1.4** for compatibility with Windows 98 and
SGI IRIX workstations (which top out at Java 1.4.2).

The build uses the [Eclipse Compiler for Java (ECJ)](https://wiki.eclipse.org/JDT_Core_Programmer_Guide/ECJ)
with `-source 1.4 -target 1.4` and a bootclasspath pointing to a Java 1.4.2 `rt.jar`
so that any Java 5+ API call is caught at compile time rather than failing at runtime.

### Prerequisites

1. Install [Apache Ant](http://ant.apache.org/) (1.8 or later).

2. Install Temurin 8 JDK (the last JDK that accepts `-source 1.4`).  On macOS with Homebrew:

   ```
   brew install --cask temurin@8
   ```

   Or download from [Adoptium](https://adoptium.net/temurin/releases/?version=8).

3. Obtain a Java 1.4.2 `rt.jar` from the Oracle JDK 1.4.2 archive and place it at:

   ```
   build/irix/rt.jar
   ```

   This file is not bundled in the repository. You can extract it from the
   `j2sdk-1_4_2_19-linux-i586.bin` or Windows installer available from the
   [Oracle Java Archive](https://www.oracle.com/java/technologies/java-archive-javase1.4.2-downloads.html).
   The `ecj.jar` compiler adapter is already bundled at `build/irix/ecj.jar`.

### Build

Point `JAVA_HOME` at Temurin 8 and run Ant with the ECJ adapter and 1.4 bootclasspath:

```sh
export JAVA_HOME=$(/usr/libexec/java_home -v 1.8)
export PATH="$JAVA_HOME/bin:$PATH"
ant -f build/build.xml \
    -lib build/irix/ecj.jar \
    -Dcompile.source.version=1.4 \
    -Dcompile.target.version=1.4 \
    -Dcompile.bootclasspath=/path/to/build/irix/rt.jar \
    clean-build-RealmSpeakFull
```

A successful build prints `BUILD SUCCESSFUL` and produces `products/RealmSpeakFull.jar`.
All class files will be at version **48.0** (Java 1.4).

### Run on Windows 98

1. Extract the release zip into a folder.
2. Install the J2SE 1.4.2 JRE (`j2re-1_4_2_19-windows-i586-p.exe`).
3. Double-click `run98.bat`.

**Recommended screen resolution: 1024×768 or higher.**

### Run on SGI IRIX

1. Enable FTP on the O2 if not already running:

   ```sh
   chkconfig ftp on
   killall -HUP inetd
   ```

2. From the Mac, FTP the following files into a directory on the O2 (e.g. `/usr/RealmSpeak`):

   ```
   products/RealmSpeakFull.jar
   products/run-irix.sh
   products/characters/      (directory)
   products/gameData/        (directory)
   products/quests/          (directory)
   ```

3. On the O2, make the script executable and launch:

   ```sh
   chmod +x /usr/RealmSpeak/run-irix.sh
   /usr/RealmSpeak/run-irix.sh
   ```

   The `-Duser.home=.` flag keeps save files in the same directory as the jar.
   Adjust `-mx256m` if your O2 has less than 512 MB RAM (try `-mx128m`).

   Java on IRIX is typically installed at `/usr/java2/bin/java` — the script uses
   the full path so it works regardless of PATH settings.

### Run under Wine (for testing)

```sh
cd products
WINEPREFIX=~/.wine-java14 wine /path/to/j2sdk1.4.2_19/jre/bin/javaw.exe \
    -mx192m \
    -Dswing.defaultlaf=com.sun.java.swing.plaf.windows.WindowsLookAndFeel \
    -cp RealmSpeakFull.jar \
    com.robin.magic_realm.RealmSpeak.RealmSpeakFrame
```

To simulate a specific screen resolution (e.g. 1024×768), use Wine's virtual desktop:

```sh
wine explorer /desktop=RealmSpeak,1024x768 javaw.exe ...
```

---

# License

RealmSpeak is the Java application for playing the board game Magic Realm.
Copyright (c) 2005-2015 Robin Warren
E-mail: robin@dewkid.com
Further development (since 2020-08-20): sch4fchen

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program. If not, see
http://www.gnu.org/licenses/

For other licenses (e.g. graphics) please check credits and the corresponding folders.
For graphics taken from Battle for Wesnoth: https://wiki.wesnoth.org/Wesnoth:Copyrights
