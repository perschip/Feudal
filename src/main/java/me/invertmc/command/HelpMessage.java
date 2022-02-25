package me.invertmc.command;

import me.invertmc.Feudal;
import org.bukkit.entity.Player;

public class HelpMessage {
    private String[] permissions;
    private String message;
    private CustomHelpPermissions permsCustom;

    public HelpMessage(String permission, String message){
        if(Feudal.getLanguage().getConfig().contains(message)){
            this.message = Feudal.getMessage(message).replace("%a%", "\u00a72");
        }else{
            this.message = message.replace("%a%", "\u00a72");
        }
        this.permissions = new String[]{permission};
    }

    public HelpMessage(String message, String... permissions){
        if(Feudal.getLanguage().getConfig().contains(message)){
            this.message = Feudal.getMessage(message).replace("%a%", "\u00a72");
        }else{
            this.message = message.replace("%a%", "\u00a72");
        }
        this.permissions = permissions;
    }

    public String getMessage(){
        return message;
    }

    public boolean hasPermission(Player p){
        if(permsCustom != null) {
            return permsCustom.hasPermission(p);
        }
        for(String perm : permissions){
            if(p.hasPermission(perm)){
                return true;
            }
        }
        return false;
    }

    public void setCustomHelpPerms(CustomHelpPermissions customHelpPermissions) {
        permsCustom = customHelpPermissions;
    }
}
