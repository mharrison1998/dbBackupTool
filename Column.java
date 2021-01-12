import java.sql.*;

/**
  * Creates a class with all the metadata about the column
  * @author Matt Harrison
  */

public class Column
{
	private String columnName;
    private String datatype;
    private String columnsize;
    private String decimaldigits;
    private String isNullable;
	
	private boolean speechmarks = false;
	
	private String query;
	
	/**
	  * constructor gets all the metadata and builds the query
	  * @param columnSet
	  */
	public Column(ResultSet columnSet)
	{
		try
		{
				this.columnName = columnSet.getString("COLUMN_NAME");
				this.datatype = columnSet.getString("DATA_TYPE");
				this.columnsize = columnSet.getString("COLUMN_SIZE");
				this.decimaldigits = columnSet.getString("DECIMAL_DIGITS");
				this.isNullable = columnSet.getString("IS_NULLABLE");
		}
		catch(SQLException se)
		{
			System.out.println(se);
		}
		
		query = buildQuery();
	}
	
	/**
	  * @return String
	  */
	public String getQuery()
	{
		return query;
	}
	
	/**
	  * @return String
	  */
	public String getName()
	{
		return columnName;
	}
	
	/**
	  * @return boolean if the column needs speechmarks or not when inserting a value
	  */
	public boolean needSpeechmarks()
	{
		return speechmarks;
	}
	
	/**
	  * writes the statement needed for part of the CREATE TABLE, optionaly writes NOT NULL and the columnsize
	  * @return
	  */
	private String buildQuery()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(columnName + " " +convertDataType(datatype));
		if(convertDataType(datatype) == "VARCHAR")
		{
			sb.append("(" + columnsize + ")");
		}
		
		
		switch(isNullable)
		{
			case "NO":
				sb.append(" NOT NULL");
			default:
		}
		
		return sb.toString();
	}
	
	/**
	  * convers the number into the SQL name
	  * @return String
	  */
	private String convertDataType(String datatype)
	{ 
	//in sql lite, most of these get converted to the same dataType
		switch(datatype)
		{
			case "-7":
				return "BIT";
			case "-6":
				return "TINYINT";
			case "-5":
				return "BIGINT";
			case "-4":
				return "LONGVARBINARY";
			case "-3":
				return "VARBINARY";
			case "-2":
				return "BINARY";
			case "-1":
				speechmarks = true;
				return "LONGVARCHAR";
			case "0":
				return "NULL";
			case "1":
				return "CHAR";
			case "2":
				return "NUMERIC";
			case "3":
				return "DECIMAL";
			case "4":
				return "INTEGER";
			case "5":
				return "SMALLINT";
			case "6":
				return "FLOAT";
			case "7":
				return "REAL";
			case "8":
				return "DOUBLE";
			case "12":
				speechmarks = true;
				return "VARCHAR";
			case "91":
				return "DATE";
			case "92":
				return "TIME";
			case "93":
				return "TIMESTAMP";
			case "1111":
				return "OTHER";
			default:
				return "TEXT"; //then its still readable
		}
	}
}