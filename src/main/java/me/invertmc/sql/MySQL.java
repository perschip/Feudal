package me.invertmc.sql;
import java.sql.SQLException;

import me.invertmc.Feudal;
import me.invertmc.utils.StackUtil;
import org.bukkit.Bukkit;


public class MySQL extends SQLite implements SQLManager {
	
	public MySQL() {
		open();
	}

	public synchronized void open() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			Bukkit.getLogger().severe(
					"[Feudal] Failed to init MySQL driver:");
			StackUtil.dumpStack(e);
			return;
		}

		String host = Feudal.getConfiguration().getConfig().getString("sql.host_ip", "localhost");// HOST
		String port = Feudal.getConfiguration().getConfig().getString("sql.port", "3306");// PORT
		String database = Feudal.getConfiguration().getConfig().getString("sql.database", "feudal");// DATABASE
		String user = Feudal.getConfiguration().getConfig().getString("sql.username", "root");// USERNAME
		String pass = Feudal.getConfiguration().getConfig().getString("sql.password", "password");// PASSWORD
		try {
			this.setConnection(java.sql.DriverManager.getConnection("jdbc:mysql://"
					+ host + ':' + port + '/' + database + '?' + "user=" + user
					+ "&password=" + pass));

			this.setStatement(this.getConnection().createStatement());

			this.getStatement().setQueryTimeout(30);
		} catch (SQLException e) {
			Bukkit.getLogger().severe(
					"[Feudal] Failed to init MySQL connection:");
			StackUtil.dumpStack(e);
		}
	}
	
}