package me.invertmc.sql;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import me.invertmc.utils.StackUtil;
import org.bukkit.Bukkit;

public class SQLite implements SQLManager {
	private volatile Connection con;
	private volatile String fileDir;
	private volatile String fileName;
	private volatile Statement st;

	public SQLite(String fileDir, String fileName) {
		this.fileDir = fileDir;
		this.fileName = fileName;

		File dir = new File(fileDir);
		dir.mkdirs();

		File file = new File(fileDir + File.separator + fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				StackUtil.dumpStack(e);
			}
		}

		open();
	}

	protected SQLite() {
	}
	
	public synchronized boolean isOpen() {
		return (con != null) || (this.st != null);
	}

	public synchronized void close() {
		try {
			if (this.st != null)
				this.st.close();
			if (con != null)
				con.close();
		} catch (SQLException e) {
			Bukkit.getLogger().severe("[Feudal] Failed to close SQL:");
			StackUtil.dumpStack(e);
		}
	}
	
	public synchronized void open() {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			Bukkit.getLogger().severe(
					"[Feudal] Failed to init SQLite driver:");
			StackUtil.dumpStack(e);
			return;
		}
		try {
			con = java.sql.DriverManager.getConnection("jdbc:sqlite:"
					+ this.fileDir + File.separator + this.fileName);
			this.st = con.createStatement();

			this.st.setQueryTimeout(30);
		} catch (SQLException e) {
			Bukkit.getLogger().severe(
					"[Feudal] Failed to init SQLite connection:");
			StackUtil.dumpStack(e);
			return;
		}
	}
	
	public synchronized boolean isConnected() {
		try {
			if (this.con.isClosed()) {
				return false;
			}
			return true;
		} catch (Exception e) {
		}
		return false;
	}
	
	public synchronized ResultSet executeQuery(String statement, boolean next) {
		if (isConnected()) {
			SQLCount.count();
			try {
				Statement s = this.con.createStatement();
				ResultSet res = s.executeQuery(statement);
				if (next) {
					res.next();
				}
				return res;
			} catch (SQLException e) {
				if(e.getMessage().toLowerCase().contains("query does not return resultset")){
					return null;
				}
				StackUtil.dumpStack(e);
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
	
	public synchronized boolean executeUpdate(String statement) {
		if (isConnected()) {
			SQLCount.count();
			try {
				Statement s = this.con.createStatement();
				s.executeUpdate(statement);
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	public synchronized Connection getConnection() {
		return con;
	}

	public synchronized Statement getStatement() {
		return st;
	}

	public synchronized void setConnection(Connection con) {
		this.con = con;
	}
	
	public synchronized void setStatement(Statement s) {
		this.st = s;
	}
}