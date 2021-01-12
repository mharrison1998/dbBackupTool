import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
  * Table class to store all the data about a table, including columns, Primary and foreign keys
  * @author Matt Harrison
  */

public class Table
{
	private DatabaseMetaData databaseMetaData;
	private String tableName;
	private List<Column> columns = new ArrayList<Column>();
	private List<String> primaryKeys = new ArrayList<String>();
	private List<ForeignKey> foreignKeys = new ArrayList<ForeignKey>();
	private String query;
	
	/**
	  * constructor that creates all the data needed in the table.
	  * @param tableName the name of the table
	  * @param databaseMetaData the metadata of the connection
	  */
	public Table(String tableName, DatabaseMetaData databaseMetaData)
	{
		this.tableName = tableName;
		this.databaseMetaData = databaseMetaData;
		try
		{
			ResultSet columnSet = databaseMetaData.getColumns(null,null, tableName, null);
			createColumns(columnSet);
			ResultSet priKeys = databaseMetaData.getPrimaryKeys(null, null, tableName);
			while(priKeys.next())
			{
				primaryKeys.add(priKeys.getString("COLUMN_NAME"));
			}
			ResultSet forKeys = databaseMetaData.getImportedKeys(null, null, tableName);
			while(forKeys.next())
			{
				foreignKeys.add(new ForeignKey(forKeys.getString("FKCOLUMN_NAME"), forKeys.getString("PKTABLE_NAME"), forKeys.getString("PKCOLUMN_NAME")));
			}
		}
		catch(SQLException se)
		{
			System.out.println(se);
		}
		query = buildQuery();
	}
	
	/**
	  * creates the columns
	  */
	private void createColumns(ResultSet columnSet)
	{
		try
		{
			while(columnSet.next())
				{
					columns.add(new Column(columnSet));
				}
		}
		catch(SQLException se)
		{
			System.out.println(se);
		}
	}
	
	/**
	  * @return String
	  */
	public String getName()
	{
		return tableName;
	}
	
	/**
	  * @return String
	  */
	public String getQuery()
	{
		return query;
	}
	
	/**
	  * @return List<Column>
	  */
	public List<Column> getColumnsList()
	{
		return columns;
	}
	
	/**
	  * creates the query needed to recreate the table
	  * @return String the query
	  */
	private String buildQuery()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE " + tableName + "(");
		for(int i = 0; i < columns.size(); i++)
		{
			if(i != 0)
				sb.append(", ");
			sb.append(columns.get(i).getQuery());
		}
		
		sb.append(", ");
		sb.append("PRIMARY KEY(");
		for(int i = 0; i < primaryKeys.size(); i++)
		{
			if(i != 0)
				sb.append(", ");
			sb.append(primaryKeys.get(i));
		}
		if(foreignKeys.size() != 0)
			sb.append("), ");
		else
			sb.append(")");
		
		for(int i = 0; i < foreignKeys.size(); i++)
		{
			if(i != 0)
				sb.append(", ");
			sb.append("FOREIGN KEY (" + foreignKeys.get(i).getpkColumnName() + ") REFERENCES " + foreignKeys.get(i).getfkTableName() + "(" + foreignKeys.get(i).getfkColumnName() + ")");
		}
		
		sb.append(");");
		return sb.toString();
	}
}