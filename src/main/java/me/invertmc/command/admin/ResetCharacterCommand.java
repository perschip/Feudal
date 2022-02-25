package me.invertmc.command.admin;

import me.invertmc.Feudal;
import me.invertmc.command.Arg;
import me.invertmc.command.Command;
import me.invertmc.command.HelpMessage;
import me.invertmc.kingdoms.Kingdom;
import me.invertmc.kingdoms.Rank;
import me.invertmc.sql.UserSave;
import me.invertmc.user.User;
import me.invertmc.user.classes.Profession;
import org.bukkit.entity.Player;

public class ResetCharacterCommand extends Command {

    @Override
    public CommandType getType() {
        return CommandType.PLAYER;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean run(String[] args, Player p) {
        if(Arg.isArgument("resetCharacter", args[0])){
            if(!p.hasPermission("feudal.commands.admin.character.resetcharacter")){
                p.sendMessage(Feudal.getMessage("a.noperm"));
                return true;
            }
            if(args.length == 2){
                String uuid = getPlayerByUUID(args[1]);
                if(uuid == null){
                    p.sendMessage(Feudal.getMessage("a.12").replace("%p%", args[1]));
                }else{
                    User u = Feudal.getUser(uuid);
                    if(u != null){
                        boolean online = (u.getPlayer() != null && u.getPlayer().isOnline());
                        if(online){
                            Feudal.unloadPlayer(u.getPlayer());
                        }
                        if(!u.getKingdomUUID().equals("")){
                            Kingdom king = Feudal.getKingdom(u.getKingdomUUID());
                            Rank rank = king.getRank(u.getUUID());
                            if(rank != null && rank.equals(Rank.LEADER)){
                                Feudal.getKingdoms().remove(king);
                                king.delete(p.getName());
                                king.removeAllMembers();
                            }else{
                                king.removeMember(u.getUUID());
                            }
                            u.setKingdomUUID("");
                        }
                        if(u.getConfig() != null) {
                            u.getConfig().getFile().delete();
                        }else if(Feudal.getPlugin().doesUseSql()) {
                            UserSave.delete(u.getUUID());
                        }
                        u.unloadPermissions();
                        u.setProfession(new Profession(u, Profession.Type.NONE, 0, 0));
                        u.effectAttributes();
                        if(online){
                            Feudal.logPlayer(u.getPlayer().getName(), u.getPlayer().getUniqueId());
                            Feudal.loadPlayer(u.getPlayer());
                        }
                        p.sendMessage(Feudal.getMessage("commands.852").replace("%a%", u.getName() + ""));
                    }else{
                        p.sendMessage(Feudal.getMessage("commands.854").replace("%a%", args[1] + ""));
                    }
                }
            }else{
                p.sendMessage(Feudal.getMessage("commands.858"));
            }
            return true;
        }
        return false;
    }

    @Override
    public HelpMessage[] getHelpMessage() {
        return new HelpMessage[]{new HelpMessage("feudal.commands.admin.character.resetcharacter",
                "commandHelp.136")};
    }

}
