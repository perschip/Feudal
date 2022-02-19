package me.invertmc.command;

import org.bukkit.entity.Player;

public abstract class SubCommand {
   public abstract String getName();

   public abstract String getDescription();

   public abstract String getSyntax();

   public abstract void perform(Player var1, String[] var2);
}
   