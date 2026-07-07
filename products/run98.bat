@ECHO OFF
REM RealmSpeak launcher for Windows 98 / Java 1.4.2

REM Add Java 1.4.2 to PATH if found in the standard install locations.
REM The JRE installer normally adds itself to PATH, but this is a safety net.
IF EXIST C:\j2re1.4.2_19\bin\javaw.exe SET PATH=C:\j2re1.4.2_19\bin;%PATH%
IF EXIST C:\j2sdk1.4.2_19\jre\bin\javaw.exe SET PATH=C:\j2sdk1.4.2_19\jre\bin;%PATH%

REM Verify javaw is reachable before attempting launch
javaw -version > NUL 2>&1
IF ERRORLEVEL 1 GOTO NOJAVA

javaw -mx192m -Dswing.defaultlaf=com.sun.java.swing.plaf.windows.WindowsLookAndFeel -cp RealmSpeakFull.jar com.robin.magic_realm.RealmSpeak.RealmSpeakFrame %1
GOTO END

:NOJAVA
ECHO.
ECHO Java 1.4.2 was not found.
ECHO.
ECHO Please install the J2SE 1.4.2 Runtime Environment and try again.
ECHO The installer is named: j2re-1_4_2_19-windows-i586-p.exe
ECHO.
PAUSE

:END
