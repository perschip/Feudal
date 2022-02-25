package me.invertmc.command;

import me.invertmc.Feudal;

public class Arg {

    public static boolean isArgument(String key, String arg) {
        if(Feudal.getCommandConfig() != null && key != null) {
            String[] args = Feudal.getCommandConfig().getConfig().getString(key, key).split(" ");
            for(String s : args) {
                if(s.equalsIgnoreCase(arg)) {
                    return true;
                }
            }
        }
        return false;
    }

}
