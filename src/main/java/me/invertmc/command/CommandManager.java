package me.invertmc.command;

import java.util.ArrayList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor {
   private ArrayList<SubCommand> subcommands = new ArrayList();

   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      if (sender instanceof Player) {
         Player p = (Player)sender;
         int i;
         if (args.length > 0) {
            for(i = 0; i < this.getSubcommands().size(); ++i) {
               if (args[0].equalsIgnoreCase(((SubCommand)this.getSubcommands().get(i)).getName())) {
                  ((SubCommand)this.getSubcommands().get(i)).perform(p, args);
               }
            }
         } else if (args.length == 0) {
            p.sendMessage("--------------------------------");

            for(i = 0; i < this.getSubcommands().size(); ++i) {
               p.sendMessage(((SubCommand)this.getSubcommands().get(i)).getSyntax() + " - " + ((SubCommand)this.getSubcommands().get(i)).getDescription());
            }

            p.sendMessage("--------------------------------");
         }
      }

      return true;
   }

   public ArrayList<SubCommand> getSubcommands() {
      return this.subcommands;
   }
}