package me.invertmc.sql;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import me.invertmc.sql.async.AsyncSQLThread;
import me.invertmc.sql.async.SQLAsync;
import me.invertmc.sql.async.SQLWait;
import me.invertmc.utils.StackUtil;
import org.bukkit.Bukkit;



public class SQLTable implements DataManager {

	private Table table;
	private SQLManager database;
	
	public SQLTable(SQLManager database) throws SQLException{
		this.database = database;
	}
	
	public void setTable(Table table) {
		this.table = table;
		try {
			database.getStatement().executeUpdate("CREATE TABLE IF NOT EXISTS "
					+ table.getName() + table.getUsage());
		} catch (SQLException e) {
			Bukkit.getLogger().severe("[Feudal] Failed creating SQL task:");
			StackUtil.dumpStack(e);
			return;
		}
	}
	
	public Table getTable(){
		return table;
	}
	
	public void set(Object... values) {
		try {
			SQLCount.count();
			String valueCount = "";
			for (int i = 0; i < values.length; i++) {
				valueCount = valueCount + "?";
				if (i < values.length - 1) {
					valueCount = valueCount + ",";
				}
			}
			PreparedStatement ps = this.database.getConnection().prepareStatement("INSERT INTO "
					+ this.table.getName() + this.table.getValuesNonPrimary()
					+ " VALUES(" + valueCount + ");");
			for (int i = 0; i < values.length; i++) {
				ps.setObject(i + 1, values[i]);
			}
			ps.executeUpdate();
		} catch (SQLException e) {
			Bukkit.getLogger().severe("[Feudal] Failed SQL task:");
			StackUtil.dumpStack(e);
			return;
		}
	}
	
	public Object get(String index, String toGet, Object value) throws SQLException {
		try {
			SQLCount.count();
			PreparedStatement ps = this.database.getConnection().prepareStatement("SELECT * FROM "
					+ this.table.getName() + " WHERE " + index + "=?;");
			ps.setObject(1, value);
			ResultSet result = ps.executeQuery();
			if (result.next()) {
				return result.getObject(toGet);
			}
		} catch (SQLException e) {
			Bukkit.getLogger().severe("[Feudal] Failed SQL task:");
			StackUtil.dumpStack(e);
			throw e;
		}

		return null;
	}

	public boolean contains(String index, Object value) throws SQLException {
		try {
			SQLCount.count();
			PreparedStatement ps = this.database.getConnection().prepareStatement("SELECT * FROM "
					+ this.table.getName() + " WHERE " + index + "=?;");
			ps.setObject(1, value);
			ResultSet result = ps.executeQuery();
			return result.next();
		} catch (SQLException e) {
			Bukkit.getLogger().severe("[Feudal] Failed SQL task:");
			StackUtil.dumpStack(e);
			throw e;
		}
	}

	public boolean containsCaseInsensitiveString(String index, String value) throws SQLException {
		try {
			SQLCount.count();
			PreparedStatement ps = this.database.getConnection().prepareStatement("SELECT * FROM "
					+ this.table.getName() + " WHERE lower(" + index + ")=?;");
			value = value.toLowerCase();
			ps.setString(1, value);
			ResultSet result = ps.executeQuery();
			return result.next();
		} catch (SQLException e) {
			Bukkit.getLogger().severe("[Feudal] Failed SQL task:");
			StackUtil.dumpStack(e);
			throw e;
		}
	}
	
	public boolean columnExists(String column) {
		try {
			SQLCount.count();
			DatabaseMetaData md = database.getConnection().getMetaData();
			ResultSet rs = md.getColumns(null, null, table.getName(), column);
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public void update(String index, String toUpdate, Object indexValue,
			Object updateValue) {
		try {
			SQLCount.count();
			PreparedStatement ps = this.database.getConnection().prepareStatement("UPDATE "
					+ this.table.getName() + " SET " + toUpdate + "=? WHERE "
					+ index + "=?;");
			ps.setObject(1, updateValue);
			ps.setObject(2, indexValue);
			ps.executeUpdate();
		} catch (SQLException e) {
			Bukkit.getLogger().severe("[Feudal] Failed SQL task:");
			StackUtil.dumpStack(e);
			return;
		}
	}

	public void remove(String index, Object value) {
		try {
			SQLCount.count();
			PreparedStatement ps = this.database.getConnection().prepareStatement("DELETE FROM "
					+ this.table.getName() + " WHERE " + index + "=?;");
			ps.setObject(1, value);
			ps.executeUpdate();
		} catch (SQLException e) {
			Bukkit.getLogger().severe("[Feudal] Failed SQL task:");
			StackUtil.dumpStack(e);
			return;
		}
	}

	@Override
	public void open() {
		database.open();
	}

	@Override
	public boolean isConnected() {
		return database.isConnected();
	}

	@Override
	public ResultSet executeQuery(String statement, boolean next) {
		return database.executeQuery(statement, next);
	}

	@Override
	public boolean executeUpdate(String statement) {
		return database.executeUpdate(statement);
	}

	@Override
	public boolean isOpen() {
		return database.isOpen();
	}

	@Override
	public void close() {
		database.close();
	}

	@Override
	public SQLWait setAsync(Object... values) throws SQLException {
		SQLCount.count();
		SQLWait id = new SQLWait(SQLWait.SQLFunction.SET);
		try {
			String valueCount = "";
			for (int i = 0; i < values.length; i++) {
				valueCount = valueCount + "?";
				if (i < values.length - 1) {
					valueCount = valueCount + ",";
				}
			}
			PreparedStatement ps = this.database.getConnection().prepareStatement("INSERT INTO "
					+ this.table.getName() + this.table.getValues()
					+ " VALUES(" + valueCount + ");");
			for (int i = 0; i < values.length; i++) {
				ps.setObject(i + 1, values[i]);
			}
			AsyncSQLThread.set(id, ps);
		} catch (SQLException e) {
			Bukkit.getLogger().severe("[Feudal] Failed SQL task:");
			StackUtil.dumpStack(e);
			throw e;
		}
		return id;
	}

	@Override
	public SQLWait getAsync(String index, String toGet,
			Object value) throws Exception {
		SQLCount.count();
		SQLWait id = new SQLWait(SQLWait.SQLFunction.GET);
		try {
			PreparedStatement ps = this.database.getConnection().prepareStatement("SELECT * FROM "
					+ this.table.getName() + " WHERE " + index + "=?;");
			ps.setObject(1, value);
			AsyncSQLThread.get(id, ps, toGet);
		} catch (SQLException e) {
			Bukkit.getLogger().severe("[Feudal] Failed SQL task:");
			StackUtil.dumpStack(e);
			throw e;
		}
		return id;
	}

	@Override
	public SQLWait containsAsync(String index, Object value)
			throws SQLException {
		SQLCount.count();
		SQLWait id = new SQLWait(SQLWait.SQLFunction.CONTAINS);
		try {
			PreparedStatement ps = this.database.getConnection().prepareStatement("SELECT * FROM "
					+ this.table.getName() + " WHERE " + index + "=?;");
			ps.setObject(1, value);
			AsyncSQLThread.contains(id, ps);
		} catch (SQLException e) {
			Bukkit.getLogger().severe("[Feudal] Failed SQL task:");
			StackUtil.dumpStack(e);
			throw e;
		}
		return id;
	}

	@Override
	public SQLWait updateAsync(String index, String toUpdate, Object indexValue,
			Object updateValue) throws SQLException {
		SQLCount.count();
		SQLWait id = new SQLWait(SQLWait.SQLFunction.UPDATE);
		try {
			PreparedStatement ps = this.database.getConnection().prepareStatement("UPDATE "
					+ this.table.getName() + " SET " + toUpdate + "=? WHERE "
					+ index + "=?;");
			ps.setObject(1, updateValue);
			ps.setObject(2, indexValue);
			AsyncSQLThread.set(id, ps);
		} catch (SQLException e) {
			Bukkit.getLogger().severe("[Feudal] Failed SQL task:");
			StackUtil.dumpStack(e);
			throw e;
		}
		return id;
	}

	@Override
	public SQLWait removeAsync(String index, Object value) throws SQLException {
		SQLWait id = new SQLWait(SQLWait.SQLFunction.REMOVE);
		SQLCount.count();
		try {
			PreparedStatement ps = this.database.getConnection().prepareStatement("DELTE FROM "
					+ this.table.getName() + " WHERE " + index + "=?;");
			ps.setObject(1, value);
			AsyncSQLThread.set(id, ps);
		} catch (SQLException e) {
			Bukkit.getLogger().severe("[Feudal] Failed SQL task:");
			StackUtil.dumpStack(e);
			throw e;
		}
		return id;
	}

	@Override
	public SQLWait executeQueryAsync(String statement, boolean next) {
		SQLWait id = new SQLWait(SQLWait.SQLFunction.EXECUTE_QUERY);
		AsyncSQLThread.executeQuery(id, statement, next);
		return id;
	}

	@Override
	public SQLWait executeUpdateAsync(String statement) {
		SQLWait id = new SQLWait(SQLWait.SQLFunction.EXECUTE_UPDATE);
		AsyncSQLThread.executeUpdate(id, statement);
		return id;
	}
		
	@Override
	public SQLWait setAsync(SQLAsync.SQLEventHandler handler, Object... values) throws SQLException {
		SQLCount.count();
		SQLWait id = new SQLWait(SQLWait.SQLFunction.SET);
		try {
			String valueCount = "";
			for (int i = 0; i < values.length; i++) {
				valueCount = valueCount + "?";
				if (i < values.length - 1) {
					valueCount = valueCount + ",";
				}
			}
			PreparedStatement ps = this.database.getConnection().prepareStatement("INSERT INTO "
					+ this.table.getName() + this.table.getValues()
					+ " VALUES(" + valueCount + ");");
			for (int i = 0; i < values.length; i++) {
				ps.setObject(i + 1, values[i]);
			}
			new SQLAsync(id, true, handler);
			AsyncSQLThread.set(id, ps);
		} catch (SQLException e) {
			Bukkit.getLogger().severe("[Feudal] Failed SQL task:");
			StackUtil.dumpStack(e);
			throw e;
		}
		return id;
	}

	@Override
	public SQLWait getAsync(String index, String toGet,
			Object value, SQLAsync.SQLEventHandler handler) throws Exception {
		SQLCount.count();
		SQLWait id = new SQLWait(SQLWait.SQLFunction.GET);
		try {
			PreparedStatement ps = this.database.getConnection().prepareStatement("SELECT * FROM "
					+ this.table.getName() + " WHERE " + index + "=?;");
			ps.setObject(1, value);
			new SQLAsync(id, true, handler);
			AsyncSQLThread.get(id, ps, toGet);
		} catch (SQLException e) {
			Bukkit.getLogger().severe("[Feudal] Failed SQL task:");
			StackUtil.dumpStack(e);
			throw e;
		}
		return id;
	}

	@Override
	public SQLWait containsAsync(String index, Object value, SQLAsync.SQLEventHandler handler)
			throws SQLException {
		SQLCount.count();
		SQLWait id = new SQLWait(SQLWait.SQLFunction.CONTAINS);
		try {
			PreparedStatement ps = this.database.getConnection().prepareStatement("SELECT * FROM "
					+ this.table.getName() + " WHERE " + index + "=?;");
			ps.setObject(1, value);
			new SQLAsync(id, true, handler);
			AsyncSQLThread.contains(id, ps);
		} catch (SQLException e) {
			Bukkit.getLogger().severe("[Feudal] Failed SQL task:");
			StackUtil.dumpStack(e);
			throw e;
		}
		return id;
	}

	@Override
	public SQLWait updateAsync(String index, String toUpdate, Object indexValue,
			Object updateValue, SQLAsync.SQLEventHandler handler) throws SQLException {
		SQLCount.count();
		SQLWait id = new SQLWait(SQLWait.SQLFunction.UPDATE);
		try {
			PreparedStatement ps = this.database.getConnection().prepareStatement("UPDATE "
					+ this.table.getName() + " SET " + toUpdate + "=? WHERE "
					+ index + "=?;");
			ps.setObject(1, updateValue);
			ps.setObject(2, indexValue);
			new SQLAsync(id, true, handler);
			AsyncSQLThread.set(id, ps);
		} catch (SQLException e) {
			Bukkit.getLogger().severe("[Feudal] Failed SQL task:");
			StackUtil.dumpStack(e);
			throw e;
		}
		return id;
	}

	@Override
	public SQLWait removeAsync(String index, Object value, SQLAsync.SQLEventHandler handler) throws SQLException {
		SQLWait id = new SQLWait(SQLWait.SQLFunction.REMOVE);
		SQLCount.count();
		try {
			PreparedStatement ps = this.database.getConnection().prepareStatement("DELTE FROM "
					+ this.table.getName() + " WHERE " + index + "=?;");
			ps.setObject(1, value);
			new SQLAsync(id, true, handler);
			AsyncSQLThread.set(id, ps);
		} catch (SQLException e) {
			Bukkit.getLogger().severe("[Feudal] Failed SQL task:");
			StackUtil.dumpStack(e);
			throw e;
		}
		return id;
	}
	
	@Override
	public SQLWait executeQueryAsync(String statement, boolean next, SQLAsync.SQLEventHandler handler) {
		SQLWait id = new SQLWait(SQLWait.SQLFunction.EXECUTE_QUERY);
		new SQLAsync(id, true, handler);
		AsyncSQLThread.executeQuery(id, statement, next);
		return id;
	}
	
	@Override
	public SQLWait executeUpdateAsync(String statement, SQLAsync.SQLEventHandler handler) {
		SQLWait id = new SQLWait(SQLWait.SQLFunction.EXECUTE_UPDATE);
		new SQLAsync(id, true, handler);
		AsyncSQLThread.executeUpdate(id, statement);
		return id;
	}
	
}
