package me.invertmc.configs;

import me.invertmc.Feudal;
import me.invertmc.sql.SQLControl;

import java.io.File;
import java.util.ArrayList;

public class Configs {

    /**
     * Loads configs
     */
    public static boolean CreateConfigs() throws Exception {
        boolean success = true;

        Feudal.log("Loading configs...");

        Feudal.setPluginFolder(Feudal.getPlugin(Feudal.class).getDataFolder());
        if (!Feudal.getPluginFolder().exists()) {
            try {
                Feudal.getPluginFolder().mkdirs();
            } catch (Exception e) {
                success = false;

                Feudal.error("Failed to create main plugin folder.  All config loading and/or creating has been canceled.");
                throw e;
            }
        }

        Feudal.setDataFolderFeudal(new File(Feudal.getPluginFolder().getAbsolutePath() + "/DATA"));
        if (!Feudal.getDataFolderFeudal().exists()) {
            try {
                Feudal.getDataFolderFeudal().mkdirs();
            } catch (Exception e) {
                success = false;

                Feudal.error("Failed to create data folder.  All config loading and/or creating has been canceled.");
                throw e;
            }
        }


        if (MainConfig.config()) {

            Feudal.log("\u00a7aMain config successfully loaded!");
        } else {

            Feudal.warn("Main config had problems while loading.");
            success = false;
        }

        /*if(CommandConfig.config()){
            Feudal.log("\u00a7aCommand config successfully loaded!");
        }else{
            Feudal.warn("Command config had problems while loading.");
            success = false;
        }

       /* if(Language.language()){

            Feudal.log("\u00a7aLanguage config successfully loaded!");
        }else{

            Feudal.warn("Language config had problems while loading.");
            success = false;
        }

        /*if(Market.marketConfig()){

            Feudal.log("\u00a7aMarket config successfully loaded!");
        }else{

            Feudal.warn("Market config had problems while loading.");
            success = false;
        }*/

       /* if(challengesConfig()){

            Feudal.log("\u00a7aChallenges config successfully loaded!");
        }else{

            Feudal.warn("Challenges config had problems while loading.");
            success = false;
        }

       /* if(MarketData.marketData()){

            Feudal.log("\u00a7aMarket data file successfully loaded!");
        }else{

            Feudal.warn("Market data file had problems while loading.");
            success = false;
        }*/

       /* if(playerData()){

            Feudal.log("\u00a7aPlayer data successfully loaded!");
        }else{

            Feudal.warn("Player data had problems while loading.");
            success = false;
        }

        if(professionData()){

            Feudal.log("\u00a7aProfession data successfully loaded!");
        }else{

            Feudal.warn("Profession data had problems while loading.");
            success = false;
        }

        if(playerFolder()){

            Feudal.log("\u00a7aPlayer folder and configs successfully loaded!");
        }else{

            Feudal.warn("There was a problem while loading the player folder and/or configs.");
            success = false;
        }

		/*if(ncpFolder()){
			Main.getPlugin().log("\u00a7aNCP folder and configs successfully loaded!");
		}else{
			Main.getPlugin().warn("There was a problem while loading the NCP folder and/or configs.");
			success = false;
		}*/

        /*if(kingdomsFolder()){

            Feudal.log("\u00a7aKingdoms folder and configs successfully loaded!");
        }else{

            Feudal.warn("There was a problem while loading the kingdoms folder and/or configs.");
            success = false;
        }

        Feudal.getPlugin(Feudal.class).setSQL(new SQLControl());
        if(Feudal.getConfiguration().getConfig().getBoolean("sql.enable")) {
            if(Feudal.getPlugin(Feudal.class).doesUseSql()) {
                Feudal.log("\u00a7aSQL successfully loaded!");
            }else {
                Feudal.warn("There was a problem while loading the SQL.");
                success = false;
            }
        }

        return success;
    }

    /*private static boolean challengesConfig() throws Exception{
        boolean load = true;
        Feudal.setChallengesConfig(new Configuration(new File(Feudal.getDataFolderFeudal(), "challenges.yml")));
        try {
            if(Feudal.getChallengesConfig().loadConfig()){
                createChallengesConfigDefaults();
            }
        } catch (Exception e) {
            load = false;

            Feudal.warn("Failed to load config: " + Feudal.getChallengesConfig().getFile().getAbsolutePath() + ".  Please check to make sure the config does not have any syntax errors.");
            try {
                Feudal.getChallengesConfig().broke();
            } catch (Exception e1) {

                Feudal.error("FAILED TO SAVE BROKEN BACKUP OF CONFIG: " + Feudal.getChallengesConfig().getFile().getAbsolutePath());
                throw e1;
            }
            try{
                Feudal.getChallengesConfig().loadConfig();
                createChallengesConfigDefaults();
            }catch(Exception e1){
                throw e1;
            }

            throw e;
        }
        try {
            Feudal.getChallengesConfig().save();
        } catch (Exception e) {
            load = false;

            Feudal.error("Failed to save config: " + Feudal.getChallengesConfig().getFile().getAbsolutePath());
            throw e;
        }
        return load;
    }

    private static void createChallengesConfigDefaults() {
        ArrayList<String> str = new ArrayList<String>();
        Feudal.getChallengesConfig().getConfig().set("challenges", str);
    }

    private static boolean kingdomsFolder() throws Exception{
        boolean load = true;
        Feudal.setKingdomsFolder(new File(Feudal.getPluginFolder().getAbsolutePath() + "/Kingdoms"));
        if (!Feudal.getKingdomsFolder().exists()) {
            try{
                Feudal.getKingdomsFolder().mkdir();
            }catch(Exception e){
                load = false;

                Feudal.error("Failed to create 'Kingdoms' folder.  All config loading and/or creating for Kingdoms has been canceled.");
                throw e;
            }
        }

        for(File f : Feudal.getKingdomsFolder().listFiles()){
            Configuration config = new Configuration(f);
            try {
                config.loadConfig();
                Feudal.getKingdomConfigs().add(config);
            } catch (Exception e) {
                load = false;

                Feudal.warn("Failed to load config: " + config.getFile().getAbsolutePath() + ".  Please check to make sure the config does not have any syntax errors.");
                try {
                    config.broke();
                } catch (Exception e1) {

                    Feudal.error("FAILED TO SAVE BROKEN BACKUP OF CONFIG: " + config.getFile().getAbsolutePath());
                    throw e1;
                }
                throw e;
            }
        }

        return load;
    }
*/
	/*private static boolean ncpFolder() {
		boolean load = true;
		Main.setNcpFolder(new File(Main.getPluginFolder().getAbsolutePath() + "/NCPs"));
		if (!Main.getNcpFolder().exists()) {
			try{
				Main.getNcpFolder().mkdir();
			}catch(Exception e){
				load = false;
				e.printStackTrace();
				Main.getPlugin().error("Failed to create 'NCPs' folder.  All config loading and/or creating for ncps has been canceled.");
				return load;
			}
		}

		for(File f : Main.getNcpFolder().listFiles()){
			Configuration config = new Configuration(f);
			try {
				config.loadConfig();
				Main.getNcpConfigs().add(config);
			} catch (Exception e) {
				load = false;
				e.printStackTrace();
				Main.getPlugin().warn("Failed to load config: " + config.getFile().getAbsolutePath() + ".  Please check to make sure the config does not have any syntax errors.");
				try {
					config.broke();
				} catch (Exception e1) {
					e1.printStackTrace();
					Main.getPlugin().error("FAILED TO SAVE BROKEN BACKUP OF CONFIG: " + config.getFile().getAbsolutePath());
				}
			}
		}
		return load;
	}*/

    /*public static boolean playerFolder() throws Exception{
        boolean load = true;
        Feudal.setPlayerFolder(new File(Feudal.getPluginFolder().getAbsolutePath() + "/players"));
        if (!Feudal.getPlayerFolder().exists()) {
            try{
                Feudal.getPlayerFolder().mkdir();
            }catch(Exception e){
                load = false;

                Feudal.error("Failed to create 'players' folder.  All config loading and/or creating for players has been canceled.");
                throw e;
            }
        }
        return load;
    }

    public static boolean playerData() throws Exception{
        boolean load = true;
        Feudal.setPlayerData(new Configuration(new File(Feudal.getDataFolderFeudal(), "pd.yml")));
        try {
            Feudal.getPlayerData().loadConfig();
        } catch (Exception e) {
            load = false;

            Feudal.warn("Failed to load config: " + Feudal.getPlayerData().getFile().getAbsolutePath() + ".  Please check to make sure the config does not have any syntax errors.");
            try {
                Feudal.getPlayerData().broke();
            } catch (Exception e1) {


                Feudal.error("FAILED TO SAVE BROKEN BACKUP OF CONFIG: " + Feudal.getPlayerData().getFile().getAbsolutePath());
                throw e1;
            }
            try{
                Feudal.getPlayerData().loadConfig();
            }catch(Exception e1){
                throw e1;
            }

            throw e;
        }
        try {
            Feudal.getPlayerData().save();
        } catch (Exception e) {
            load = false;

            Feudal.error("Failed to save config: " + Feudal.getPlayerData().getFile().getAbsolutePath());
            throw e;
        }
        return load;
    }

    private static boolean professionData() throws Exception{
        boolean load = true;
        Feudal.setProfessionData(new Configuration(new File(Feudal.getDataFolderFeudal(), "professionData.yml")));
        try {
            if(Feudal.getProfessionData().loadConfig()){
                setProfessionDataDefaults();
            }
        } catch (Exception e) {
            load = false;

            Feudal.warn("Failed to load config: " + Feudal.getProfessionData().getFile().getAbsolutePath() + ".  Please check to make sure the config does not have any syntax errors.");
            try {
                Feudal.getProfessionData().broke();
            } catch (Exception e1) {

                Feudal.error("FAILED TO SAVE BROKEN BACKUP OF CONFIG: " + Feudal.getProfessionData().getFile().getAbsolutePath());
                throw e1;
            }
            try{
                if(Feudal.getProfessionData().loadConfig()){
                    setProfessionDataDefaults();
                }
            }catch(Exception e1){
                throw e1;			}

            throw e;
        }
        try {
            Feudal.getProfessionData().save();
        } catch (Exception e) {
            load = false;

            Feudal.error("Failed to save config: " + Feudal.getProfessionData().getFile().getAbsolutePath());
            throw e;
        }
        return load;
    }

    private static void setProfessionDataDefaults(){
        //FARMER, LOGGER, HUNTER, MINER, COOK, FISHER, BUILDER, SHEPERD, SCRIBE, GUARD, ASSASSIN, ALCHEMIST,
        //BLACKSMITH, HEALER, MERCHANT, SQUIRE, KNIGHT, BARON, KING
        Feudal.getProfessionData().getConfig().set("FARMER", new ArrayList<String>());
        Feudal.getProfessionData().getConfig().set("LOGGER", new ArrayList<String>());
        Feudal.getProfessionData().getConfig().set("HUNTER", new ArrayList<String>());
        Feudal.getProfessionData().getConfig().set("MINER", new ArrayList<String>());
        Feudal.getProfessionData().getConfig().set("COOK", new ArrayList<String>());
        Feudal.getProfessionData().getConfig().set("FISHER", new ArrayList<String>());
        Feudal.getProfessionData().getConfig().set("BUILDER", new ArrayList<String>());
        Feudal.getProfessionData().getConfig().set("SHEPHERD", new ArrayList<String>());
        Feudal.getProfessionData().getConfig().set("SCRIBE", new ArrayList<String>());
        Feudal.getProfessionData().getConfig().set("GUARD", new ArrayList<String>());
        Feudal.getProfessionData().getConfig().set("ASSASSIN", new ArrayList<String>());
        Feudal.getProfessionData().getConfig().set("ALCHEMIST", new ArrayList<String>());
        Feudal.getProfessionData().getConfig().set("BLACKSMITH", new ArrayList<String>());
        Feudal.getProfessionData().getConfig().set("HEALER", new ArrayList<String>());
        Feudal.getProfessionData().getConfig().set("MERCHANT", new ArrayList<String>());
        Feudal.getProfessionData().getConfig().set("SQUIRE", new ArrayList<String>());
        Feudal.getProfessionData().getConfig().set("KNIGHT", new ArrayList<String>());
        Feudal.getProfessionData().getConfig().set("BARON", new ArrayList<String>());
        Feudal.getProfessionData().getConfig().set("KING", new ArrayList<String>());
        Feudal.getProfessionData().getConfig().set("preSchedule", System.currentTimeMillis() - 3600000);
    }

     */

    /*public static boolean reload() {
        boolean ret = true;
        try{
            if(MainConfig.config()){
                Feudal.log("\u00a7aMain config successfully loaded!");
            }else{
                Feudal.warn("Main config had problems while loading.");
                ret = false;
            }
        }catch(Exception e){
            ret = false;
            Feudal.warn("Main config had problems while loading.");
            ErrorManager.error(26, e);
        }

        try {
            if(CommandConfig.config()){
                Feudal.log("\u00a7aCommand config successfully loaded!");
            }else{
                Feudal.warn("Command config had problems while loading.");
                ret = false;
            }
        }catch(Exception e){
            ret = false;
            Feudal.warn("Command config had problems while loading.");
            ErrorManager.error(26, e);
        }

        try{
            if(Language.language()){
                Feudal.log("\u00a7aLanguage config successfully loaded!");
            }else{
                Feudal.warn("Language config had problems while loading.");
                ret = false;
            }
        }catch(Exception e){
            ret = false;
            Feudal.warn("Language config had problems while loading.");
            ErrorManager.error(27, e);
        }

        try{
            if(Market.marketConfig()){
                Feudal.log("\u00a7aMarket config successfully loaded!");
            }else{
                Feudal.warn("Market config had problems while loading.");
                ret = false;
            }
        }catch(Exception e){
            Feudal.warn("Market config had problems while loading.");
            ret = false;
            ErrorManager.error(28, e);
        }
        return ret;*/
        return false;
    }
}