package me.invertmc.command.admin;

import me.invertmc.Feudal;
import me.invertmc.command.Arg;
import me.invertmc.command.Command;
import me.invertmc.command.HelpMessage;
import me.invertmc.configs.Configs;
import org.bukkit.entity.Player;

public class ReloadCommand extends Command {

    @Override
    public HelpMessage[] getHelpMessage() {
        return new HelpMessage[]{new HelpMessage("feudal.commands.admin.reload",
                "commandHelp.141")};
    }

    @Override
    public CommandType getType() {
        return CommandType.PLAYER;
    }

    @Override
    public boolean run(String[] args, Player p) {
        if(Arg.isArgument("reload", args[0])){
            if(!p.hasPermission("feudal.commands.admin.reload")){
                p.sendMessage(Feudal.getMessage("a.noperm"));
                return true;
            }

            if(Configs.reload()){
                p.sendMessage(Feudal.getMessage("reloadconfig.done"));
            }else{
                Feudal.error("Failed to reload configs.  See the stack trace above.  Send the stack trace to the Feudal managers to get this resolved.");
                p.sendMessage(Feudal.getMessage("reloadconfig.error"));
            }
            return true;
        }
        return false;
    }

}