REM VNC password: 123
@echo off
cmd /c java -cp .\javavnc-1.0-SNAPSHOT.jar;target\dependency\javavnc-1.0-SNAPSHOT.jar org.javavnc.tcp2xmpp.TCP2XMPPMain
IF NOT ERRORLEVEL 1 GOTO end
echo Please install Java Runtime Environment (JRE) version 5 or later from http://java.sun.com/javase/downloads/index.jsp
:end
