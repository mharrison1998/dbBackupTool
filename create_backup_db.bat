javac *.java
java -cp "sqlite-jdbc-3.7.2.jar;." Main
sqlite3.exe backup.db < backup.sql > aoutput.txt