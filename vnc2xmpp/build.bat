set JAVA_HOME=D:\programs\jdk1.6.0
set PATH=%PATH%;%JAVA_HOME%\bin
set CLASSPATH=%CLASSPATH%;bin;lib\smack.jar;;lib\smackx.jar;lib\smackx-debug.jar

mkdir bin

javac -source 1.4 -target 1.4 -d bin src\com\fcg\xmpptcp\common\ConnectionUtil.java
javac -source 1.4 -target 1.4 -d bin src\com\fcg\xmpptcp\common\TCP2XMPPPumpThread.java 
javac -source 1.4 -target 1.4 -d bin src\com\fcg\xmpptcp\common\VNCUtils.java 
javac -source 1.4 -target 1.4 -d bin src\com\fcg\xmpptcp\common\XMPP2TCPPumpThread.java
javac -source 1.4 -target 1.4 -d bin src\com\fcg\xmpptcp\tcp2xmpp\TCPOverXMPPThread.java
javac -source 1.4 -target 1.4 -d bin src\com\fcg\xmpptcp\tcp2xmpp\TCP2XMPPMain.java
javac -source 1.4 -target 1.4 -d bin src\com\fcg\xmpptcp\xmpp2tcp\XMPPOverTCPThread.java
javac -source 1.4 -target 1.4 -d bin src\com\fcg\xmpptcp\xmpp2tcp\XMPP2TCPMain.java