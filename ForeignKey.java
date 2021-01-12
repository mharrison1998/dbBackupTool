import java.sql.*;

/**
  * Class to store metadata about a foreign key
  * @author Matt Harrison
  */

public class ForeignKey
{
	private String pkColumnName;
	private String fkTableName;
	private String fkColumnName;
	
	public ForeignKey(String pkColumnName, String fkTableName, String fkColumnName)
	{
		this.pkColumnName = pkColumnName;
		this.fkTableName = fkTableName;
		this.fkColumnName = fkColumnName;
	}
	
	public String getpkColumnName()
	{
		return	pkColumnName;
	}
	
	public String getfkTableName()
	{
		return	fkTableName;
	}
	
	public String getfkColumnName()
	{
		return	fkColumnName;
	}
}