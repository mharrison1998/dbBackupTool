import java.io.*;
/**
  * Main driver of program, creates a writer and asks for a database to back up
  * @author Matt Harrison
  */

public class Main {

DbWriter myDbWriter = null;
String dbName;


private void go()
{
	BufferedReader brin = new BufferedReader(new InputStreamReader(System.in));
    System.out.print("Name of database you want to backup: ");
    try
	{
		dbName = brin.readLine();
	}
	catch (IOException e)
	{
		System.out.println("Main.go() : Failure in I/O"+e);
	}
	myDbWriter = new DbWriter(dbName);
	myDbWriter.writeCreateTables();
	myDbWriter.writeInserts();
	myDbWriter.writeEnd();
	
	myDbWriter.close();
}; // end of method "go"

public static void main(String [ ] args)
{
	Main myMain = new Main();
	myMain.go();
} // end of method "main"

} // end of class "Main"
