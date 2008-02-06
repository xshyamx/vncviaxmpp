@echo off

cmd /c java -server -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000 -cp .\javavnc-1.0-SNAPSHOT.jar;target\dependency\javavnc-1.0-SNAPSHOT.jar org.javavnc.xmpp2tcp.XMPP2TCPMain
rem cmd /c  java -cp .\javavnc-1.0-SNAPSHOT.jar;target\dependency\javavnc-1.0-SNAPSHOT.jar org.javavnc.xmpp2tcp.XMPP2TCPMain

IF NOT ERRORLEVEL 1 GOTO end
echo Please install Java Runtime Environment (JRE) version 5 or later from http://java.sun.com/javase/downloads/index.jsp
:end