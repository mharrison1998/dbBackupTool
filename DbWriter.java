import java.sql.*;
import java.io.*;
import java.util.Objects;

/**
  * writer that connects to a database and creates a printer writer to write the backup statements to
  * @author Matt Harrison
  */
public class DbWriter extends DbConnector {

	private ResultSet rs = null;
	static private final int STR_SIZE = 25;
	private PrintWriter writer;
	private MetaDataCollector mdc;

	// this method takes a String, converts it into an array of bytes;
	// copies those bytes into a bigger byte array (STR_SIZE worth), and
	// pads any remaining bytes with spaces. Finally, it converts the bigger
	// byte array back into a String, which it then returns.
	// e.g. if the String was "s_name", the new string returned is
	// "s_name                    " (the six characters followed by 18 spaces).
	private String pad( String in )
	{
		byte [] org_bytes = in.getBytes( );
		byte [] new_bytes = new byte[STR_SIZE];
		int upb = in.length( );

		if ( upb > STR_SIZE )
			upb = STR_SIZE;

		for ( int i = 0; i < upb; i++ )
			new_bytes[i] = org_bytes[i];

		for ( int i = upb; i < STR_SIZE; i++ )
			new_bytes[i] = ' ';

		return new String( new_bytes );
	}

	/*
	 * Creates a connection to the named database
	 */
	public DbWriter ( String dbName)
	{
		super( dbName );
		
		try
		{
			writer = new PrintWriter("backup.sql", "UTF-8");
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		mdc = new MetaDataCollector(con);
	}
	
	/**
	  * closes the writer after all the statements have been written
	  */
	public void writeEnd()
	{
		writer.close();
	}
	
	/**
	  * goes through all the table and writes their CREATE statement
	  */
	public void writeCreateTables()
	{
		for(Table table: mdc.getTables())
			{
				writer.println(table.getQuery());
			}
	}
	
	/**
	  * goes through the database, querying using SELECT statements and then using the returned data to write insert statements
	  * will check wether the returned value needs to have speech marks or if it is /N
	  * also checks when their needs to be commas
	  */
	public void writeInserts()
	{
		try
		{
			
			for(Table table: mdc.getTables())
			{
				
				Statement sqlStmt = con.createStatement();
				String query =	"SELECT *\n" +
								"FROM " +
								table.getName() + ";";
				PreparedStatement ps = con.prepareStatement(query);
				ResultSet rSet = ps.executeQuery( );
				
				
				while(rSet.next())
				{
					StringBuilder sb = new StringBuilder();
					sb.append("INSERT INTO " + table.getName() +" VALUES (");
					for(int i = 0; i < table.getColumnsList().size(); i++)
					{
						if(i != 0)
							sb.append(", ");
						if(table.getColumnsList().get(i).needSpeechmarks())
						{
							sb.append('"' + rSet.getString(table.getColumnsList().get(i).getName()) + '"');
						}
						else
						{
							if(Objects.equals(rSet.getString(table.getColumnsList().get(i).getName()),"\\N"))
							{
								sb.append("'\\N'");
							}
							else
							{
								sb.append(rSet.getString(table.getColumnsList().get(i).getName()));
							}
						}
					}
					sb.append(");");
					writer.println(sb);
				}
				
			
			}
		}
		catch(SQLException se)
		{
			System.out.println(se);
		}
	}
}
