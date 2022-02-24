package me.invertmc.sql;

import me.invertmc.sql.async.SQLAsync;
import me.invertmc.sql.async.SQLWait;

import java.sql.ResultSet;
import java.sql.SQLException;



public interface DataManager {
	public abstract void setTable(Table paramTable);

	public abstract void set(Object... paramVarArgs);

	public abstract Object get(String selectColumn, String columnReturn,
			Object selectValue) throws Exception;

	public abstract boolean contains(String index, Object value) throws SQLException;

	public abstract void update(String selectColumn, String changeColumn,
			Object whereIndex, Object changeValue);

	public abstract void remove(String paramString, Object paramObject);

	public abstract boolean isOpen();

	public abstract void open();

	public abstract void close();

	public abstract boolean isConnected();

	public abstract ResultSet executeQuery(String statement, boolean next);

	public abstract boolean executeUpdate(String statement);

	public abstract boolean columnExists(String string);

	public abstract Table getTable();

	public abstract boolean containsCaseInsensitiveString(String index, String value) throws SQLException;
	
	/*
	 * Async methods
	 */
	public abstract SQLWait setAsync(Object... values) throws SQLException;
	
	public abstract SQLWait getAsync(String selectColumn, String columnReturn,
			Object selectValue) throws Exception;

	public abstract SQLWait containsAsync(String index, Object value) throws SQLException;

	public abstract SQLWait updateAsync(String selectColumn, String changeColumn,
			Object whereIndex, Object changeValue) throws SQLException;
	
	public abstract SQLWait removeAsync(String index, Object value) throws SQLException;
	
	public abstract SQLWait executeQueryAsync(String statement, boolean next);

	public abstract SQLWait executeUpdateAsync(String statement);
		
	/*
	 * Embedded Async methods
	 */
	public abstract SQLWait setAsync(SQLAsync.SQLEventHandler handler, Object... values) throws SQLException;
	
	public abstract SQLWait getAsync(String selectColumn, String columnReturn,
			Object selectValue, SQLAsync.SQLEventHandler handler) throws Exception;

	public abstract SQLWait containsAsync(String index, Object value, SQLAsync.SQLEventHandler handler) throws SQLException;

	public abstract SQLWait updateAsync(String selectColumn, String changeColumn,
			Object whereIndex, Object changeValue, SQLAsync.SQLEventHandler handler) throws SQLException;
	
	public abstract SQLWait removeAsync(String index, Object value, SQLAsync.SQLEventHandler handler) throws SQLException;
	
	public abstract SQLWait executeQueryAsync(String statement, boolean next, SQLAsync.SQLEventHandler handler);

	public abstract SQLWait executeUpdateAsync(String statement, SQLAsync.SQLEventHandler handler);
}