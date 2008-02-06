call mvn package
call mvn dependency:copy-dependencies
copy target\*.jar target\dependency\
copy client.bat target\dependency\
copy server.bat target\dependency\