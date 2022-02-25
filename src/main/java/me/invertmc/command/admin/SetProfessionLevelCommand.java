package me.invertmc.command.admin;

import me.invertmc.Feudal;
import me.invertmc.command.Arg;
import me.invertmc.command.Command;
import me.invertmc.command.HelpMessage;
import me.invertmc.user.User;
import me.invertmc.user.classes.Profession;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class SetProfessionLevelCommand extends Command {

    @Override
    public CommandType getType() {
        return CommandType.PLAYER;
    }

    @Override
    public boolean run(String[] args, Player p) {
        if(Arg.isArgument("setProfessionLevel", args[0])){
            if(!p.hasPermission("feudal.commands.admin.character.setprofessionlevel")){
                p.sendMessage(Feudal.getMessage("a.noperm"));
                return true;
            }
            if(args.length == 2 || args.length == 3){
                int level = 0;
                try{
                    level = Integer.parseInt(args[1]);
                }catch(Exception e){
                    p.sendMessage(Feudal.getMessage("commands.522"));
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
                            p.sendMessage(Feudal.getMessage("commands.534").replace("%a%", args[1] + ""));
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

                if(u.getProfession() != null){
                    if(level < 0){
                        p.sendMessage(Feudal.getMessage("commands.548"));
                    }else if(u.getProfession().getMaxLevel() < level){
                        p.sendMessage(Feudal.getMessage("commands.550").replace("%a%", u.getProfession().getMaxLevel() + ""));
                    }else{
                        u.getProfession().setLevel(level);
                        if(level >= u.getProfession().getMaxLevel()){
                            u.getProfession().setMax(true);
                        }else{
                            u.getProfession().setMax(false);
                            ArrayList<Profession> remove = new ArrayList<Profession>();
                            for(Profession pro : u.getPastProfessions()){
                                if(pro.getType().equals(u.getProfession().getType())){
                                    remove.add(pro);
                                }
                            }
                            for(Profession pro : remove){
                                u.getPastProfessions().remove(pro);
                            }
                        }
                        u.save(true);
                        p.sendMessage(Feudal.getMessage("commands.554").replace("%b%", level + "").replace("%a%", u.getName() + ""));
                    }
                }else{
                    p.sendMessage(Feudal.getMessage("commands.557").replace("%a%", u.getName() + ""));
                }
            }else{
                p.sendMessage(Feudal.getMessage("commands.560"));
            }
            return true;
        }
        return false;
    }

    @Override
    public HelpMessage[] getHelpMessage() {
        return new HelpMessage[]{new HelpMessage("feudal.commands.admin.character.setprofessionlevel",
                "commandHelp.124")};
    }

}