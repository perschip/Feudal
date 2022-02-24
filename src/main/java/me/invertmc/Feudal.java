package me.invertmc;

import java.io.File;
import java.util.ArrayList;

import me.invertmc.configs.Configs;
import me.invertmc.configs.Configuration;
import me.invertmc.kingdoms.Kingdom;
import me.invertmc.sql.SQLControl;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import me.invertmc.command.CommandManager;
import me.invertmc.files.Messages;
import net.md_5.bungee.api.ChatColor;

public class Feudal extends JavaPlugin {
	
	  public static Feudal instance;
	  private static File pluginFolder;
	  private static File dataFolder;
	  private static File kingdomsFolder;

	  private static Configuration config;
	  private static Configuration language;


	 private static ArrayList<Kingdom> kingdoms = new ArrayList<>();

	  private SQLControl sqlSetup;

	  PluginDescriptionFile pdf = this.getDescription();

	  private static final String pluginName = "Feudal";

	   public void onEnable() {
	      this.instance = this;

	      this.getLogger().info(ChatColor.GREEN + "=======================");
	      this.getLogger().info(ChatColor.GREEN + "Feudal Has Been Enabled!");
	      this.getLogger().info(ChatColor.GREEN + "Verison: " + this.pdf.getVersion());
	      this.getLogger().info(ChatColor.GREEN + "=======================");
	      this.getCommand("feudal").setExecutor(new CommandManager());

		   try {
			   if (Configs.CreateConfigs()) {
				   Feudal.log("Feudal configs have been successfully loaded!");
			   }
		   } catch (Exception e) {
			   e.printStackTrace();
		   }

	   }

	   public void onDisable() {
		   this.getLogger().info(ChatColor.RED + "=======================");
		   this.getLogger().info(ChatColor.RED + "Feudal Has Been Disabled!");
		   this.getLogger().info(ChatColor.RED + "Verison: " + this.pdf.getVersion());
		   this.getLogger().info(ChatColor.RED + "=======================");
	   }


	public boolean doesUseSql() {
		if(getSql() != null) {
			return getSql().doesUse();
		}else {
			return false;
		}
	}
	public SQLControl getSql() {
		return sqlSetup;
	}

	public void setSQL(SQLControl sqlControl) {
		this.sqlSetup = sqlControl;
	}


	public static void setPluginFolder(File pluginFolder) {
		Feudal.pluginFolder = pluginFolder;
	}

	public static File getPluginFolder() {
		return pluginFolder;
	}

	/**
	 * Get the main config Configuration
	 *
	 * @return The main configuration
	 */
	public static Configuration getConfiguration() {
		return config;
	}

	/**
	 * Set the main config file Configuration
	 *
	 * @param config
	 *            The main configuration
	 */
	public static void setConfiguration(Configuration config) {
		Feudal.config = config;
	}

	/**
	 * Send console error message
	 *
	 * @param error
	 *            The message sent to the console
	 */
	public static void error(String error) {
		Bukkit.getConsoleSender().sendMessage(
				"\u00a74[" + pluginName + "] Error: " + error);
	}

	/**
	 * Log information to console
	 *
	 * @param msg
	 *            Message logged to console
	 */
	public static void log(String msg) {
		Bukkit.getConsoleSender().sendMessage(
				"\u00a7e[" + pluginName + "] " + msg);
	}

	/**
	 * Send warning information to console
	 *
	 * @param warning
	 *            Message sent to console
	 */
	public static void warn(String warning) {
		Bukkit.getConsoleSender().sendMessage(
				"\u00a76[" + pluginName + "] Warning: " + warning);
	}

	/**
	 * Get the data folder for feudal.
	 *
	 * @return
	 */
	public static File getDataFolderFeudal() {
		return dataFolder;
	}

	public static void setDataFolderFeudal(File dataFolder) {
		Feudal.dataFolder = dataFolder;
	}

	/**
	 * Get a message from the language config. Will convert and to color code
	 * and andand to and.
	 *
	 * @param string
	 * @return
	 */
	public static String getMessage(String string) {
		String s = Feudal.getLanguage().getConfig().getString(string);
		if (s == null) {
			Feudal.error("LANGUAGE FIELD MISSING: " + string);
			return "\u00a74\u00a7lLANGUAGE INFORMATION MISSING";
		}
		s = s.replace("&", "\u00a7").replace("\u00a7\u00a7", "&");
		return s;
	}

	/**
	 * Get language config.
	 *
	 * @return language config
	 */
	public static Configuration getLanguage() {
		return language;
	}

	public static void setLanguage(Configuration language) {
		Feudal.language = language;
	}

	/**
	 * Get the kingdoms folder
	 *
	 * @return Kingdoms folder
	 */
	public static File getKingdomsFolder() {
		return kingdomsFolder;
	}

	/**
	 * Set the kingdoms folder
	 *
	 * @param kingdomsFolder
	 *            Kingdoms folder
	 */
	public static void setKingdomsFolder(File kingdomsFolder) {
		Feudal.kingdomsFolder = kingdomsFolder;
	}

	/**
	 * Get a list of all kingdoms
	 *
	 * @return list of all kingdoms
	 */
	public static ArrayList<Kingdom> getKingdoms() {
		return kingdoms;
	}

	public static Feudal getInstance() {
	      return instance;
	   }
	}
