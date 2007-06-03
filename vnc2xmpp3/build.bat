set JAVA_HOME=D:\programs\jdk1.6.0
set PATH=%PATH%;%JAVA_HOME%\bin
set CLASSPATH=%CLASSPATH%;bin;lib\smack.jar;;lib\smackx.jar;lib\smackx-debug.jar;lib\smackx-jingle.jar

mkdir bin
javac -source 1.5 -target 1.5 -d bin src\org\jivesoftware\smack\*.java
javac -source 1.5 -target 1.5 -d bin src\com\fcg\xmpptcp\common\*.java
javac -source 1.5 -target 1.5 -d bin src\com\fcg\xmpptcp\tcp2xmpp\*.java
javac -source 1.5 -target 1.5 -d bin src\com\fcg\xmpptcp\xmpp2tcp\*.java
