# dbBackupTool.java
A Java program to take an existing SQL Database, read the database into objects then recreate the database in full as a backup file, including tables, primary keys and foreign keys.

To run the program, execute the batch file. This will compile each of the classes, run the main class using sqlite3. This will produce a .sql file with all the statements needed to recreate the database.
The batch file then uses this .sql file to recreate the database and produce a database file named backup.db
