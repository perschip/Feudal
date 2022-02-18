package me.invertmc.utils;

import me.invertmc.Feudal;
import net.md_5.bungee.api.ChatColor;

public class MessageUtils {
	
	private Feudal instance = Feudal.getInstance();
	
	public String colorize(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
		
	}

}
