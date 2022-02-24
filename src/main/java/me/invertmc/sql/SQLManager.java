package me.invertmc.sql;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Statement;

public interface SQLManager {
	  public abstract boolean isOpen();
	  
	  public abstract void open();
	  
	  public abstract void close();
	  
	  public abstract boolean isConnected();
	  
	  public abstract ResultSet executeQuery(String statement, boolean next);
	  
	  public abstract boolean executeUpdate(String statement);
	  
	  public abstract Connection getConnection();
	  
	  public abstract void setConnection(Connection con);
	  
	  public abstract void setStatement(Statement s);

	public abstract Statement getStatement();

}
