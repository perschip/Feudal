package me.invertmc;

import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class Feudal extends JavaPlugin {
	
	
	public static  Feudal instance;
	
	
	@Override
	public void onEnable() {
		// Initialize Instance
		instance = this;
		
		// onEnable Message
		getLogger().info(ChatColor.GREEN + "==================");
		getLogger().info(ChatColor.GREEN + "Feudal has been enabled!");
		getLogger().info(ChatColor.GREEN + "==================");
	}
	
	
	@Override
	public void onDisable() {
		
		// onDisable Message
			getLogger().info(ChatColor.RED + "==================");
			getLogger().info(ChatColor.RED + "Feudal has been disabled!");
			getLogger().info(ChatColor.RED + "==================");
	}
	
	
	public void registerCommands() {
		
	}
	
	public static Feudal getInstance() {
		return instance;
	}

}
