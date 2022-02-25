package me.invertmc.command.admin;

import me.invertmc.Feudal;
import me.invertmc.command.Arg;
import me.invertmc.command.Command;
import me.invertmc.command.HelpMessage;
import me.invertmc.user.User;
import me.invertmc.utils.ErrorManager;
import org.bukkit.entity.Player;


public class SetReputationCommand extends Command{

    @Override
    public CommandType getType() {
        return CommandType.PLAYER;
    }

    @Override
    public boolean run(String[] args, Player p) {
        if(Arg.isArgument("setReputation", args[0])){
            if(!p.hasPermission("feudal.commands.admin.character.setreputation")){
                p.sendMessage(Feudal.getMessage("a.noperm"));
                return true;
            }
            if(args.length == 3){
                int reputation = 0;
                try{
                    reputation = Integer.parseInt(args[2]);
                }catch(Exception e){
                    p.sendMessage(Feudal.getMessage("commands.2989"));
                    return true;
                }
                if(reputation < Feudal.getConfiguration().getConfig().getInt("reputation.min")){
                    p.sendMessage(Feudal.getMessage("commands.2993").replace("%a%", Feudal.getConfiguration().getConfig().getInt("reputation.min") + ""));
                }else if(reputation > Feudal.getConfiguration().getConfig().getInt("reputation.max")){
                    p.sendMessage(Feudal.getMessage("commands.2995").replace("%a%", Feudal.getConfiguration().getConfig().getInt("reputation.max") + ""));
                }else{
                    String uuid = getPlayerByUUID(args[1]);
                    if(uuid == null){
                        p.sendMessage(Feudal.getMessage("a.12").replace("%p%", args[1]));
                    }else{
                        User u = Feudal.getUser(uuid);
                        if(u != null){
                            int change = reputation - u.getReputation();
                            u.effectReputation(change, Feudal.getMessage("reputation.changed").replace("%player%", p.getName()));
                            try{
                                u.save(true);
                            }catch(Exception e){
                                ErrorManager.error(35, e);
                                p.sendMessage(Feudal.getMessage("commands.3009"));
                                return true;
                            }
                            p.sendMessage(Feudal.getMessage("commands.3012").replace("%b%", reputation + "").replace("%a%", u.getName() + ""));
                        }else{
                            p.sendMessage(Feudal.getMessage("a.12").replace("%p%", args[1]));
                        }
                    }
                }
            }else{
                p.sendMessage(Feudal.getMessage("commands.3019"));
            }
            return true;
        }
        return false;
    }

    @Override
    public HelpMessage[] getHelpMessage() {
        return new HelpMessage[]{new HelpMessage("feudal.commands.admin.character.setReputation",
                "commandHelp.138")};
    }

}
