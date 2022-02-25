package me.invertmc.command;

import java.util.ArrayList;
import java.util.List;

import me.invertmc.Feudal;
import org.bukkit.entity.Player;

public class CommandHelp {
    private static ArrayList<HelpMessage> helpMessages = new ArrayList<HelpMessage>();

    public static ArrayList<HelpMessage> getHelpMessages(){
        return helpMessages;
    }

    public static void help(int page, Player p) {
        try{
            List<String> sendableMessages = getSendable(p);

            int maxPage = sendableMessages.size() / 6;
            if((((double)sendableMessages.size()) / 6.0) > maxPage){
                maxPage++;
            }
            if(page < 1){
                page = 1;
            }
            if(page > maxPage){
                page = maxPage;
            }
            p.sendMessage(Feudal.getMessage("commands.3337").replace("%b%", maxPage + "").replace("%a%", page + ""));

            for(int i = (page-1)*6; i < (page*6) && i < sendableMessages.size(); i++){
                if(i < sendableMessages.size())
                    p.sendMessage(sendableMessages.get(i));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static List<String> getSendable(Player p) {
        List<String> str = new ArrayList<String>();
        for(HelpMessage msg : helpMessages){
            if(msg.hasPermission(p)){
                str.add(msg.getMessage());
            }
        }
        return str;
    }
}
