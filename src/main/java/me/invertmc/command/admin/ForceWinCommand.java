package me.invertmc.command.admin;

import me.invertmc.Feudal;
import me.invertmc.command.Arg;
import me.invertmc.command.Command;
import me.invertmc.command.HelpMessage;
import me.invertmc.kingdoms.Challenge;
import me.invertmc.kingdoms.Kingdom;
import org.bukkit.entity.Player;

public class ForceWinCommand extends Command {

    @Override
    public CommandType getType() {
        return CommandType.PLAYER;
    }

    @Override
    public boolean run(String[] args, Player p) {
        if(Arg.isArgument("forcewin", args[0])){
            if(!p.hasPermission("feudal.commands.admin.kingdoms.forcewin")){
                p.sendMessage(Feudal.getMessage("a.noperm"));
                return true;
            }
            if(args.length == 3){
                Kingdom attacker = null;
                for(Kingdom k : Feudal.getKingdoms()){
                    if(k.getName().equalsIgnoreCase(args[1])){
                        attacker = k;
                    }
                }
                if(attacker == null){
                    //Gets kingdom by players name
                    String uuid = getPlayerByUUID(args[1]);
                    if(uuid != null){
                        attacker = Feudal.getPlayerKingdom(uuid);
                    }
                }
                if(attacker == null){
                    p.sendMessage(Feudal.getMessage("commands.2755"));
                    return true;
                }

                Kingdom defender = null;
                for(Kingdom k : Feudal.getKingdoms()){
                    if(k.getName().equalsIgnoreCase(args[2])){
                        defender = k;
                    }
                }
                if(defender == null){
                    //Gets kingdom by players name
                    String uuid = getPlayerByUUID(args[2]);
                    if(uuid != null){
                        defender = Feudal.getPlayerKingdom(uuid);
                    }
                }
                if(defender == null){
                    p.sendMessage(Feudal.getMessage("commands.2773"));
                    return true;
                }

                Challenge challenge = Feudal.getChallenge(attacker, defender);
                if(challenge == null){
                    challenge = Feudal.getChallenge(defender, attacker);
                }
                if(challenge != null){
                    challenge.win(attacker, "\u00a74\u00a7lForce win by: \u00a77" + p.getName(), false);
                    p.sendMessage(Feudal.getMessage("commands.2783").replace("%b%", defender.getName() + "").replace("%a%", attacker.getName() + ""));
                }else{
                    p.sendMessage(Feudal.getMessage("commands.2785").replace("%b%", defender.getName() + "").replace("%a%", attacker.getName() + ""));
                }
            }else{
                p.sendMessage(Feudal.getMessage("commands.2788"));
            }
            return true;
        }
        return false;
    }

    @Override
    public HelpMessage[] getHelpMessage() {
        return new HelpMessage[]{new HelpMessage("feudal.commands.admin.kingdoms.forcewin",
                "commandHelp.140")};
    }

}
