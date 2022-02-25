package me.invertmc.command.admin;

import me.invertmc.Feudal;
import me.invertmc.command.Arg;
import me.invertmc.command.Command;
import me.invertmc.command.HelpMessage;
import me.invertmc.user.User;
import org.bukkit.entity.Player;


public class SetSpeedCommand extends Command{

    @Override
    public CommandType getType() {
        return CommandType.PLAYER;
    }

    @Override
    public boolean run(String[] args, Player p) {
        if(Arg.isArgument("setSpeedLevel", args[0])){
            if(!p.hasPermission("feudal.commands.admin.character.setspeed")){
                p.sendMessage(Feudal.getMessage("a.noperm"));
                return true;
            }
            if(args.length == 2 || args.length == 3){
                int level = 0;
                try{
                    level = Integer.parseInt(args[1]);
                }catch(Exception e){
                    p.sendMessage(Feudal.getMessage("commands.572"));
                    return true;
                }
                User u = null;
                if(args.length == 3){
                    String uuid = getPlayerByUUID(args[2]);
                    if(uuid == null){
                        p.sendMessage(Feudal.getMessage("a.12").replace("%p%", args[1]));
                        return true;
                    }else{
                        u = Feudal.getUser(uuid);
                        if(u == null){
                            p.sendMessage(Feudal.getMessage("commands.584").replace("%a%", args[1] + ""));
                            return true;
                        }
                    }
                }else{
                    u = Feudal.getUser(p.getUniqueId().toString());
                    if(u == null){
                        p.sendMessage(Feudal.getMessage("a.ufail"));
                        return true;
                    }
                }

                if(u.getSpeed() != null){
                    if(level < 0){
                        p.sendMessage(Feudal.getMessage("commands.598"));
                    }else if(u.getSpeed().getMaxLevel() < level){
                        p.sendMessage(Feudal.getMessage("commands.600").replace("%a%", u.getSpeed().getMaxLevel() + ""));
                    }else{
                        u.getSpeed().setLevel(level);
                        u.effectAttributes();
                        u.save(true);
                        p.sendMessage(Feudal.getMessage("commands.605").replace("%b%", level + "").replace("%a%", u.getName() + ""));
                    }
                }else{
                    p.sendMessage(Feudal.getMessage("commands.608").replace("%a%", u.getName() + ""));
                }
            }else{
                p.sendMessage(Feudal.getMessage("commands.611"));
            }
            return true;
        }
        return false;
    }

    @Override
    public HelpMessage[] getHelpMessage() {
        return new HelpMessage[]{new HelpMessage("feudal.commands.admin.character.setspeed",
                "commandHelp.126")};
    }

}