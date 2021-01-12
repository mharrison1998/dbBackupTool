import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
  * class to get all of the required metadata to backup the database
  * @author Matt Harrison
  */

public class MetaDataCollector
{
	private DatabaseMetaData databaseMetaData;
	List<Table> tables = new ArrayList<Table>();
	
	/**
	  * Takes in a connection and gets a result set, then creates tables
	  * @param con the connection to the database that is being backed up
	  */
	public MetaDataCollector(Connection con)
	{
		try
		{
			databaseMetaData = con.getMetaData();
			ResultSet tableSet = databaseMetaData.getTables(null, null, null, new String[]{"TABLE"});
			createTables(tableSet);
		}
		catch(SQLException se)
		{
			System.out.println(se);
		}
	}
	
	/**
	  * uses the metadata resultset to get the names of tables and create tables to store metadata
	  * @param tableSet the result that has all the metadata
	  */
	private void createTables(ResultSet tableSet)
	{
		try
		{
			while(tableSet.next())
			{
					tables.add(new Table(tableSet.getString("TABLE_NAME"), databaseMetaData));
			}
		}
		catch(SQLException se)
		{
			System.out.println(se);
		}
	}
	
	/**
	  * @return tables the list of tables
	  */
	public List<Table> getTables()
	{
		return tables;
	}
}