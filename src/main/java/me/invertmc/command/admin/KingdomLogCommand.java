package me.invertmc.command.admin;

import me.invertmc.Feudal;
import me.invertmc.command.Arg;
import me.invertmc.command.Command;
import me.invertmc.command.HelpMessage;
import me.invertmc.kingdoms.Kingdom;
import me.invertmc.kingdoms.KingdomLog;
import me.invertmc.user.User;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class KingdomLogCommand extends Command {

    @Override
    public CommandType getType() {
        return CommandType.PLAYER;
    }

    @Override
    public boolean run(String[] args, Player p) {
        if(Arg.isArgument("kingdomlog", args[0])){
            if(!p.hasPermission("feudal.commands.admin.kingdomlog")){
                p.sendMessage(Feudal.getMessage("a.noperm"));
                return true;
            }

            if(args.length == 2 || args.length == 3){
                String uuid = getPlayerByUUID(args[1]);
                if(uuid != null && !uuid.equals("")){
                    User u = Feudal.getUser(uuid);
                    if(u != null){
                        ArrayList<KingdomLog> logs = new ArrayList<KingdomLog>();
                        ArrayList<KingdomLog> logsD = u.getPastKingdoms();
                        long lastTime = Long.MAX_VALUE;

                        if(logsD != null && logsD.size() > 0){

                            while(logs.size() < logsD.size()){
                                KingdomLog l = null;
                                for(KingdomLog log : logsD){
                                    if(log.getTime() <= lastTime && !logs.contains(log)){
                                        l = log;
                                    }
                                }
                                if(l != null){
                                    lastTime = l.getTime();
                                    logs.add(l);
                                }else{
                                    break;
                                }
                            }
                            int page = 1;
                            if(args.length == 3){
                                try{
                                    page = Integer.parseInt(args[2]);
                                }catch(Exception e){
                                    p.sendMessage(Feudal.getMessage("kingdomlog.noInt"));
                                    return true;
                                }
                            }
                            if(page < 1){
                                page = 1;
                            }
                            int maxPage = (logs.size() / 7) + 1;
                            if(page > maxPage){
                                page = maxPage;
                            }


                            p.sendMessage(Feudal.getMessage("kingdomlog.title").replace("%name%", u.getName()) + " " + page + "/" + maxPage);// &6&lKingdom logs (%name%)
                            for(int i = ((page - 1)*7); i < ((page) * 7); i++){
                                if(i < logs.size()){
                                    KingdomLog log = logs.get(i);
                                    String kingdom = log.getKingdomUUID();
                                    Kingdom k = Feudal.getKingdom(kingdom);
                                    if(k != null){
                                        kingdom = k.getName();
                                    }else{
                                        kingdom = "UNKNOWN / DELETED (" + kingdom + ")";
                                    }

                                    String time = log.getTime() + "";
                                    long rem = System.currentTimeMillis() - log.getTime();
                                    long years = rem / 31557600000L;
                                    long weeks = (rem - (years * 31557600000L)) / 604800000L;
                                    long days = (rem-(years * 31557600000L)-(weeks * 604800000L)) / 86400000;
                                    long hours = ((rem - (days * 86400000) - (years * 31557600000L)-(weeks * 604800000L)) / 3600000);
                                    long minutes = (rem - (days * 86400000) - (hours * 3600000)-(years * 31557600000L)-(weeks * 604800000L)) / 60000;
                                    long seconds = (rem - (days * 86400000) - (hours * 3600000) - (minutes * 60000)-(years * 31557600000L)-(weeks * 604800000L)) / 1000;
                                    if(years == 0){
                                        if(weeks == 0){
                                            time = days + (days == 1 ? " day" : " days") + " " + hours + ":" + minutes + ":" + seconds + " ago";
                                        }else{
                                            time =  weeks + (weeks == 1 ? " week" : " weeks") + ", " + days + (days == 1 ? " day" : " days") + " " + hours + ":" + minutes + ":" + seconds + " ago";
                                        }
                                    }else{
                                        time = years + (years == 1 ? " year" : " years") + ", " + weeks + (weeks == 1 ? " week" : " weeks") + ", " + days + (days == 1 ? " day" : " days") + " " + hours + ":" + minutes + ":" + seconds + " ago";
                                    }

                                    p.sendMessage(Feudal.getMessage("kingdomlog.log").replace("%time%", time).replace("%kingdom%", kingdom).replace("%join%", log.isJoin() ? "JOINED" : "LEFT"));// &d%join% %kingdom% %time%
                                }
                            }
                        }else{
                            p.sendMessage(Feudal.getMessage("kingdomlog.noLog"));// This player has no kingdom log
                        }
                    }else{
                        p.sendMessage(Feudal.getMessage("commands.1219"));
                    }
                }else{
                    p.sendMessage(Feudal.getMessage("commands.1219"));
                }
            }else{
                p.sendMessage(Feudal.getMessage("commands.2930"));
            }
            return true;
        }
        return false;
    }

    @Override
    public HelpMessage[] getHelpMessage() {
        return new HelpMessage[]{new HelpMessage("feudal.commands.admin.kingdomlog",
                "kingdomlog.cmd")};
    }

}
