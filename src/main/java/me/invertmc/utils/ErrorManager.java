package me.invertmc.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import me.invertmc.Feudal;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

/**
 * Manages errors for feudal.
 * 0 = General error (No specific location)
 * 1 = Plugin Metrics
 * 2 = Check Market errors 1
 * 3 = Check Market errors 2
 * 4 = General on enable error
 * 5 = General on disable error
 * 6 = Load kingdom error
 * 7 = Load kingdom error 2
 * 8 = Load challenges error
 * 9 = Metrics error
 * 10 Save challenge rough
 * 11 Save challenges
 * 12 log player
 * 13 - 20 Load player
 * 21 - 22 get user
 * 23 unload player
 * 24 save market error
 * 25 load kingdom 3
 * 26 reload main config
 * 27 reload language config
 * 28 reload market config
 * 29 command help
 * 30 save kingdom
 * 31 new kingdom file
 * 32 load new kingdom config
 * 33 load new kingdom object
 * 34 save new kingdom object
 * 35 save kingdom ALL
 * 36 save user
 * 37 command is land protected
 * 38 Player command error
 * 39 Console command error
 * 40 event chat error
 * 41 event join error
 * 42 event leave error
 * 43 event interact error
 * 44 event piston retract error
 * 45 event piston extend error
 * 46 event explode error
 * 47 event fire spread error
 * 48 event block burn error
 * 49 event entity change block error
 * 50 event move error
 * 51 event teleport error
 * 52 event command error
 * 53 event inventory click error
 * 54 event entity damage by entity event
 * 55 event brew error
 * 56 event block grow error
 * 57 event block break error
 * 58 event block place error
 * 59 event regen error
 * 60 event craft error
 * 61 event fish error
 * 62 event sheer error
 * 63 event book write error
 * 64 event item consume error
 * 65 event inventory close error
 * 66 event entity death error
 * 67 event player death error
 * 68 event entity damage error
 * 69 event xp gain error
 * 70 event enchant error
 * 71 event potion splash error
 * 72 event item damage error
 * 73 general main schedule error
 * 74 stamina timer
 * 75 update user online time
 * 76 save profession data
 * 77 save user in task
 * 78 place holder error
 * 79 challenge defender save
 * 80 challenge save kingdom
 * 81 - 83 kingdom split fail
 * 84 kingdom save kingdom
 * 85 save kingdom user
 * 86 market kingdom save
 * 87 kingdom save change profession
 * 88 user failed load current profession
 * 89 user failed load past professions
 * 90 user failed load kingdon log
 * 91 user save user
 * 92 user save profession data
 * 93 xp kingdom save
 * 94 xp user save
 * 95 spar timeout
 * 96 block break monitor event
 * 97 failed to load player folder.
 * 98 no player data config
 */
@SuppressWarnings("unused")
public class ErrorManager {

    private static boolean currentVersion = false;
    private static boolean loaded = false;
    private static long lastSent = 0;
    private static int port = 0;
    private static String spigot = "";
    private static Plugin plugin = null;
    private static String feudalVersion = "";

    public static void load() {
		reload();
		if(plugin != null){
			new Thread(){

				public void run(){
					try{
						String result = "";
						URL url = new URL("http://download.feudal.coremod.com/feudal_version.php");
						HttpURLConnection con = (HttpURLConnection) url.openConnection();
						con.setRequestMethod("GET");
						BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
						String line;
						while((line = rd.readLine()) != null){
							result += line;
						}
						rd.close();

						if(result.equalsIgnoreCase(feudalVersion)){
							currentVersion = true;
							Feudal.log("You are using the latest version of feudal!");
							Feudal.log("Feudal error manager activated. Any Feudal errors will be sent to the creator of Feudal.");
							if(spigot.equals("")){
								Feudal.warn("Feudal errors will still be shown in console since you have not provided your spigot username in the main config (spigotUsername).");
							}
						}else{
							Feudal.warn("Feudal version outdated. Get the latest version of Feudal here: http://download.feudal.coremod.com/");
						}
						loaded = true;
					}catch(Exception e){
						Feudal.error("Failed to load feudal error manager. Errors will not be sent to the creator of feudal and they will be printed in console.");
						e.printStackTrace();
					}
				}

			}.start();
		}else{
			Feudal.log("Failed to load feudal error manager. Errors will not be sent to the creator of feudal and they will be printed in console.");
		}
    }

    public static void reload(){
        port = Bukkit.getServer().getPort();
        spigot = Feudal.getConfiguration().getConfig().getString("spigotUsername");
        plugin = Feudal.getPlugin(Feudal.class);
        feudalVersion = plugin.getDescription().getVersion();
        if(spigot == null){
            spigot = "";
        }
    }

    public static boolean isLatest() {
        if(loaded){
            return currentVersion;
        }else{
            return true;
        }
    }

    public static boolean isLoaded(){
        return loaded;
    }

    public static boolean hasSpigot() {
        if(spigot.equals("")){
            return false;
        }else{
            return true;
        }
    }

    public static void error(int id, Exception e){
		/*if(doesSend()){
			reload();
			String trace = e.toString() + Arrays.toString(e.getStackTrace());

			sendError(id, trace, e);
		}else{
			Feudal.error("This error has not been sent to the Feudal creator.  The error is printed below.  Please send it to Forseth11 on spigot. ERROR ID: " + id);
			e.printStackTrace();
		}*/
        e.printStackTrace();
    }

}