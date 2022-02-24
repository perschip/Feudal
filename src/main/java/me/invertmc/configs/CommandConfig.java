package me.invertmc.configs;

import java.io.File;

import me.invertmc.Feudal;
import org.bukkit.configuration.file.FileConfiguration;


public class CommandConfig {
    public static boolean config() throws Exception{
        boolean load = true;
        Feudal.setCommandConfig(new Configuration(new File(Feudal.getPluginFolder(), "command_settings.yml")));
        try {
            if(Feudal.getCommandConfig().loadConfig()){
                createConfigDefaults();
            }else{
                updateConfigDefaults();
            }
        } catch (Exception e) {
            load = false;

            Feudal.warn("Failed to load config: " + Feudal.getCommandConfig().getFile().getAbsolutePath() + ".  Please check to make sure the config does not have any syntax errors.");
            try {
                Feudal.getCommandConfig().broke();
            } catch (Exception e1) {

                Feudal.error("FAILED TO SAVE BROKEN BACKUP OF CONFIG: " + Feudal.getCommandConfig().getFile().getAbsolutePath());
                throw e1;			}
            try{
                Feudal.getCommandConfig().loadConfig();
                createConfigDefaults();
            }catch(Exception e1){
                throw e1;			}

            throw e;
        }
        try {
            Feudal.getCommandConfig().save();
        } catch (Exception e) {
            load = false;

            Feudal.error("Failed to save config: " + Feudal.getCommandConfig().getFile().getAbsolutePath());
            throw e;
        }
        return load;
    }

    private static void updateConfigDefaults() {
        //FileConfiguration c = Feudal.getCommandConfig().getConfig();
        //Stuff
    }

    private static void createConfigDefaults() {
        FileConfiguration c = Feudal.getCommandConfig().getConfig();

        c.set("help", "help");
        c.set("tutorial", "tutorial");
        c.set("about", "about");
        c.set("market", "market m");
        c.set("sell", "sell s");
        c.set("price", "price");
        c.set("character", "character c");
        c.set("changeProfession", "changeProfession cp");
        c.set("create", "create");
        c.set("manage", "manager manage");
        c.set("join", "join j");
        c.set("invite", "invite");
        c.set("list", "list");
        c.set("treasury", "treasury t");
        c.set("treasuryAdd", "add a");
        c.set("treasuryRemove", "remove r");
        c.set("info", "info who");
        c.set("banner", "banner");
        c.set("setBanner", "setBanner");
        c.set("kingdomChat", "kingdomchat kc chat");
        c.set("spar", "spar");
        c.set("enemy", "enemy");
        c.set("neutral", "neutral");
        c.set("ally", "ally");
        c.set("setOpen", "setOpen");
        c.set("setOpenTrue", "true t");
        c.set("setOpenFalse", "false f");

        c.set("banInfo", "banInfo");
        c.set("kingdomOnline", "kingdomOnline");
        c.set("online", "online");
        c.set("shield", "shield");
        c.set("shieldStop", "stop");
        c.set("vacation", "vacation");

        c.set("challenge", "challenge war");
        c.set("challengeAccept", "accept");
        c.set("challengeSurrender", "surrender");
        c.set("challengeInfo", "info");
        c.set("challengeTP", "tp");
        c.set("challengeList", "list");

        c.set("delete", "delete");
        c.set("leader", "leader");
        c.set("leave", "leave");
        c.set("kick", "kick");
        c.set("claim", "claim");
        c.set("unclaim", "unclaim");
        c.set("unclaimAll", "all");

        c.set("setHome", "setHome");
        c.set("home", "home");
        c.set("view", "view map");
        c.set("viewLarge", "me local large kingdom l");
        c.set("promote", "promote");
        c.set("demote", "demote");
        c.set("fighter", "fighter");

        c.set("reload", "reload");
        c.set("attributes", "attributes");
        c.set("setProfessionLevel", "setProfessionLevel profession");
        c.set("setSpeedLevel", "setSpeedLevel speed");
        c.set("setStrengthLevel", "setStrengthLevel strength");
        c.set("setToughnessLevel", "setToughnessLevel toughness");
        c.set("setStaminaLevel", "setStaminaLevel stamina");
        c.set("setLuckLevel", "setLuckLevel luck");

        c.set("resetCharacter", "resetCharacter");
        c.set("setReputation", "setReputation reputation");
        c.set("forcewin", "forcewin");
        c.set("kingdomlog", "kingdomlog");

        c.set("unenchant", "unenchant");

        c.set("name", "name");
        c.set("description", "description d");
        c.set("findclaims", "findclaims claims");
    }
}
