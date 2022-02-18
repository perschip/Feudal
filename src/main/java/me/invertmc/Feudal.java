package me.invertmc;

import java.io.File;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import me.invertmc.command.CommandManager;
import me.invertmc.files.Messages;
import net.md_5.bungee.api.ChatColor;

public class Feudal extends JavaPlugin {
	
	  public static Feudal instance;
	   public Messages messagescfg;
	  PluginDescriptionFile pdf = this.getDescription();

	   public void onEnable() {
	      this.instance = this;
	      this.getLogger().info("=======================");
	      this.getLogger().info("Feudal Has Been Enabled!");
	      this.getLogger().info("Verison: " + this.pdf.getVersion());
	      this.createConfig();
	      this.loadConfigManager();
	      this.getLogger().info("=======================");
	      this.getCommand("feudal").setExecutor(new CommandManager());
	   }

	   public void onDisable() {
	   }

	   private void createConfig() {
	      try {
	         if (!this.getDataFolder().exists()) {
	            this.getDataFolder().mkdirs();
	         }

	         File file = new File(this.getDataFolder(), "config.yml");
	         if (!file.exists()) {
	            this.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "[Feudal]: &bConfig&8: &aGenerating config."));
	            this.getConfig().options().copyDefaults(true);
	            this.saveDefaultConfig();
	         } else {
	            this.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "[Feudal]: &bConfig&8: &aLoading config."));
	         }
	      } catch (Exception var2) {
	         var2.printStackTrace();
	      }

	   }

	   public void loadConfigManager() {
	      this.messagescfg = new Messages();
	      this.messagescfg.setup();
	   }

	   public static Feudal getInstance() {
	      return instance;
	   }
	}
