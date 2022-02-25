package me.invertmc.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import me.invertmc.Feudal;
import me.invertmc.api.events.LeaveKingdomEvent;
import me.invertmc.api.events.ReputationChangeEvent;
import me.invertmc.api.events.custom.MoneyAddEvent;
import me.invertmc.api.events.custom.MoneyGetEvent;
import me.invertmc.api.events.custom.MoneyRemoveEvent;
import me.invertmc.configs.Configuration;
import me.invertmc.kingdoms.Kingdom;
import me.invertmc.kingdoms.KingdomLog;
import me.invertmc.sql.UserSave;
import me.invertmc.user.attributes.Attribute;
import me.invertmc.user.attributes.Attributes;
import me.invertmc.user.attributes.Effect;
import me.invertmc.user.classes.Profession;
import me.invertmc.user.classes.SocialClass;
import me.invertmc.utils.ErrorManager;
import me.invertmc.utils.OnlineTime;
import me.invertmc.utils.VaultUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;


public class User {
    private volatile String uuid, name;
    private volatile Player player; //Can be null if the player is offline.
    private volatile double balance; //balance will not be used here if vault is installed. NOTE: If vault is uninstalled then it will be set on player join and leave incase vault in uninstalled.  If it is newly installed then the balance will change the vault balance if the balance is greater than vaults.
    private volatile Attributes strength, toughness, speed, stamina, luck;
    private volatile Profession profession;
    private volatile ArrayList<Profession> pastProfessions = new ArrayList<Profession>();
    private volatile short reputation; //Default reputation = 1000. MAX REPUTATION BEFORE ERROR 32000
    private volatile ArrayList<KingdomLog> kingdomLogs = new ArrayList<KingdomLog>();//Stores kingdom join and leave log.
    private volatile Location location;
    private volatile long firstJoin, lastJoin;
    private volatile Configuration config;
    private volatile String kingdomUUID = "";
    private volatile List<String> offlineMessages = new ArrayList<String>();
    private volatile Kingdom currentKingdom = null;
    private volatile long lastLandMessage;
    private volatile String lastTrack = null;
    private volatile long cooldownShield = -1;
    private volatile boolean home = true;

    private double staminaDamage = Feudal.getConfiguration().getConfig().getDouble("Stamina.defaultDamage");

    private static double maxStaminaDamage = -1;
    private static double minStaminaDamage = -1;
    private static double staminaRegen = -1;

    //SQL stuff
    private volatile boolean changeOfflineMessage = false;//Used for sql saving
    private volatile boolean change = false;
    private volatile boolean changeKingdomLog = false;
    //

    private byte chat = 0;
    private long instanceCreateTime = System.currentTimeMillis();

    private OnlineTime onlineTime = new OnlineTime();

    /**
     * Creates a user instance.  This will load a player from a config.
     * @param UUID
     * @param config
     */
    public User(String UUID, Configuration config) {
        this.uuid = UUID;
        this.config = config;
        loadFromConfig();
        loadPlayer();

        if(player == null || !player.isOnline()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(Feudal.getPlugin(), new Runnable() {
                public void run() {
                    if(player == null || !player.isOnline()) {
                        if(change || changeOfflineMessage || changeKingdomLog) {
                            Feudal.asyncSaveUser(User.this);
                        }
                    }
                }
            }, 20);
        }
    }

    private void loadPlayer() {
        Player p = Bukkit.getPlayer(UUID.fromString(uuid));
        if(p != null && p.isOnline()){
            player = p;
            name = p.getName();
            effectAttributes();
            if(offlineMessages.size() > 0){
                Bukkit.getScheduler().scheduleSyncDelayedTask(Feudal.getPlugin(), new Runnable(){

                    @Override
                    public void run() {
                        player.sendMessage(Feudal.getMessage("offlineMsg.start"));
                        for(String message : offlineMessages){
                            player.sendMessage(Feudal.getMessage("offlineMsg.list").replace("%msg%", message));
                        }
                        offlineMessages = new ArrayList<String>();
                        changeOfflineMessage = true;
                    }

                }, 70);
            }
        }
    }

    private void loadFromConfig() {

        if(Feudal.getPlugin().doesUseSql()) {
            UserSave.load(this);
        }else {

            name = config.getConfig().getString("name");
            reputation = (short) config.getConfig().getInt("reputation");
            try{
                location = new Location(Bukkit.getWorld(config.getConfig().getString("location.world")), config.getConfig().getDouble("location.x"), config.getConfig().getDouble("location.y"), config.getConfig().getDouble("location.z"), (float) config.getConfig().getDouble("location.yaw"), (float) config.getConfig().getDouble("location.pitch"));
            }catch(IllegalArgumentException e){
                location = null;
                try{
                    location = new Location(Bukkit.getWorlds().get(0), config.getConfig().getDouble("location.x"), config.getConfig().getDouble("location.y"), config.getConfig().getDouble("location.z"), (float) config.getConfig().getDouble("location.yaw"), (float) config.getConfig().getDouble("location.pitch"));
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
            firstJoin = config.getConfig().getLong("firstJoin");
            lastJoin = config.getConfig().getLong("lastJoin");
            kingdomUUID = config.getConfig().getString("kingdomUUID");
            offlineMessages = config.getConfig().getStringList("offlineMessages");

            if(config.getConfig().contains("cooldownShield")){
                cooldownShield = config.getConfig().getLong("cooldownShield");
            }

            try {
                profession = new Profession(config.getConfig().getString("currentProfession"), this);
            } catch (Exception e1) {
                ErrorManager.error(88, e1);

                Feudal.error("Failed to load current profession for player by the uuid: " + uuid);
            }

            for(String str : config.getConfig().getStringList("pastProfessions")){
                try {
                    pastProfessions.add(new Profession(str, this));
                } catch (Exception e) {
                    ErrorManager.error(89, e);

                    Feudal.error("Failed to load past profession for play by the uuid: " + uuid + ". Profession string: " + str);
                }
            }

            for(String str : config.getConfig().getStringList("kingdomlog")){
                try {
                    kingdomLogs.add(new KingdomLog(str));
                } catch (Exception e) {
                    ErrorManager.error(90, e);

                    Feudal.error("Failed to load kingdom log for player by the uuid: " + uuid);
                }
            }

            for(int i = 1; i <= 24; i++){
                onlineTime.setHour(i, config.getConfig().getInt("online." + i));
            }

            strength = new Attributes(Attribute.STRENGTH, config.getConfig().getInt("strength.xp"), config.getConfig().getInt("strength.level"), this);
            toughness = new Attributes(Attribute.TOUGHNESS, config.getConfig().getInt("toughness.xp"), config.getConfig().getInt("toughness.level"), this);
            speed = new Attributes(Attribute.SPEED, config.getConfig().getInt("speed.xp"), config.getConfig().getInt("speed.level"), this);
            stamina = new Attributes(Attribute.STAMINA, config.getConfig().getInt("stamina.xp"), config.getConfig().getInt("stamina.level"), this);
            luck = new Attributes(Attribute.LUCK, config.getConfig().getInt("luck.xp"), config.getConfig().getInt("luck.level"), this);

            //Load vault or balance
            balance = config.getConfig().getDouble("balance");
        }
        syncBalance();
        //

    }

    /**
     * Syncs balance with vault.
     *
     */
    public void syncBalance(){
        double bal = 0;
        if(Bukkit.getPluginManager().isPluginEnabled("Vault") && VaultUtils.hasEconomy() && Feudal.getEco() == 1){
            try{
                OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
                if(player != null){
                    if(player.getName() != null){
                        if(VaultUtils.hasAccount(player)){
                            bal = VaultUtils.balance(player);
                        }
                    }
                }
            }catch(Exception e){
                if(Feudal.getEco() == 1 && Bukkit.getPluginManager().isPluginEnabled("Vault") && VaultUtils.hasEconomy()){
                    //e.printStackTrace();

                    Feudal.error("Failed to get vault balance for player by uuid: " + uuid);
                }
            }
        }

        if(Feudal.getEco() == 1){
            if(balance != bal) {
                change = true;
            }
            balance = bal;
        }
    }

    /**
     * Adds money to a user's balance.  The money must be greater than 0. This will effect vault if vault is enabled.
     * @param money
     */
    public void addMoney(double money){
        if(money <= 0){
            return;
        }
        MoneyAddEvent event = new MoneyAddEvent(this, money);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if(!event.isOverrided()){
            if(Feudal.getEco() == 1){
                VaultUtils.deposit(Bukkit.getOfflinePlayer(UUID.fromString(uuid)), event.getMoney());
                syncBalance();
            }else{
                change = true;
                balance += event.getMoney();
            }
        }
    }

    /**
     * Removes money from a user's balance.  The money must be greater than 0. This will effect vault if vault is enabled. Check if player has enough money first by snycing balance and getting the balance.
     * @param money
     */
    public void removeMoney(double money){
        if(money <= 0){
            return;
        }
        MoneyRemoveEvent event = new MoneyRemoveEvent(this, money);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if(!event.isOverrided()){
            if(Feudal.getEco() == 1){
                VaultUtils.withdraw(Bukkit.getOfflinePlayer(UUID.fromString(uuid)), event.getMoney());
                syncBalance();
            }else{
                change = true;
                balance -= event.getMoney();
            }
        }
    }

    /**
     * Saves a user.
     */
    public void save(boolean async){

        if(Feudal.getPlugin().doesUseSql()) {
            UserSave.save(this, async);
            return;
        }

        config.getConfig().set("name", name);
        config.getConfig().set("reputation", reputation);
        config.getConfig().set("firstJoin", firstJoin);
        if(player != null && player.isOnline()){
            lastJoin = System.currentTimeMillis();
        }
        config.getConfig().set("lastJoin", lastJoin);
        config.getConfig().set("kingdomUUID", kingdomUUID);

        location = new Location(Bukkit.getWorld(config.getConfig().getString("location.world")), config.getConfig().getDouble("location.x"), config.getConfig().getDouble("location.y"), config.getConfig().getDouble("location.z"), (float) config.getConfig().getDouble("location.yaw"), (float) config.getConfig().getDouble("location.pitch"));
        if(player != null && player.isOnline()){
            location = player.getLocation();
        }

        if(location != null){
            if(location.getWorld() != null){
                config.getConfig().set("location.world", location.getWorld().getName());
            }
            config.getConfig().set("location.x", location.getX());
            config.getConfig().set("location.y", location.getY());
            config.getConfig().set("location.z", location.getZ());
            config.getConfig().set("location.yaw", location.getYaw());
            config.getConfig().set("location.pitch", location.getPitch());
        }

        config.getConfig().set("offlineMessages", offlineMessages);

        if(cooldownShield > 0){
            config.getConfig().set("cooldownShield", cooldownShield);
        }else{
            config.getConfig().set("cooldownShield", null);
        }

        config.getConfig().set("currentProfession", profession.toString());

        ArrayList<String> newPastProfessions = new ArrayList<String>();
        for(Profession pro : this.pastProfessions){
            newPastProfessions.add(pro.toString());
        }
        config.getConfig().set("pastProfessions", newPastProfessions);

        ArrayList<String> newKingdomLogs = new ArrayList<String>();
        for(KingdomLog log : this.kingdomLogs){
            newKingdomLogs.add(log.toString());
        }
        config.getConfig().set("kingdomlog", newKingdomLogs);

        syncBalance();
        config.getConfig().set("balance", balance);

        //Attributes
        config.getConfig().set("strength.xp", strength.getXp());
        config.getConfig().set("strength.level", strength.getLevel());

        config.getConfig().set("toughness.xp", toughness.getXp());
        config.getConfig().set("toughness.level", toughness.getLevel());

        config.getConfig().set("speed.xp", speed.getXp());
        config.getConfig().set("speed.level", speed.getLevel());

        config.getConfig().set("stamina.xp", stamina.getXp());
        config.getConfig().set("stamina.level", stamina.getLevel());

        config.getConfig().set("luck.xp", luck.getXp());
        config.getConfig().set("luck.level", luck.getLevel());

        config.getConfig().set("online.1", onlineTime.getHour(1));
        config.getConfig().set("online.2", onlineTime.getHour(2));
        config.getConfig().set("online.3", onlineTime.getHour(3));
        config.getConfig().set("online.4", onlineTime.getHour(4));
        config.getConfig().set("online.5", onlineTime.getHour(5));
        config.getConfig().set("online.6", onlineTime.getHour(6));
        config.getConfig().set("online.7", onlineTime.getHour(7));
        config.getConfig().set("online.8", onlineTime.getHour(8));
        config.getConfig().set("online.9", onlineTime.getHour(9));
        config.getConfig().set("online.10", onlineTime.getHour(10));
        config.getConfig().set("online.11", onlineTime.getHour(11));
        config.getConfig().set("online.12", onlineTime.getHour(12));
        config.getConfig().set("online.13", onlineTime.getHour(13));
        config.getConfig().set("online.14", onlineTime.getHour(14));
        config.getConfig().set("online.15", onlineTime.getHour(15));
        config.getConfig().set("online.16", onlineTime.getHour(16));
        config.getConfig().set("online.17", onlineTime.getHour(17));
        config.getConfig().set("online.18", onlineTime.getHour(18));
        config.getConfig().set("online.19", onlineTime.getHour(19));
        config.getConfig().set("online.20", onlineTime.getHour(20));
        config.getConfig().set("online.21", onlineTime.getHour(21));
        config.getConfig().set("online.22", onlineTime.getHour(22));
        config.getConfig().set("online.23", onlineTime.getHour(23));
        config.getConfig().set("online.24", onlineTime.getHour(24));

        try {
            config.save();
        } catch (Exception e) {
            ErrorManager.error(91, e);

            Feudal.error("Failed to save player config: " + config.getFile().getAbsolutePath());
        }
    }

    /**
     * Sets a players's config as default.
     * Will not function properly if the config is not empty or new.
     * @param player
     * @param config
     */
    public static void setDefaults(Player player, Configuration config) {

        if(Feudal.getPlugin().doesUseSql()) {
            UserSave.setDefaults(player);
            return;
        }

        boolean autoSetup = (Feudal.getConfiguration().getConfig().getBoolean("setup.default.enable"));

        config.getConfig().set("name", player.getName());
        config.getConfig().set("reputation", Feudal.getConfiguration().getConfig().getInt("reputation.start"));
        config.getConfig().set("firstJoin", System.currentTimeMillis());
        config.getConfig().set("lastJoin", System.currentTimeMillis());
        config.getConfig().set("kingdomUUID", "");
        for(int i = 1; i <= 24; i++){
            config.getConfig().set("online." + i, 0);
        }

        if(Feudal.getEco() == 0){
            config.getConfig().set("balance", Feudal.getConfiguration().getConfig().getInt("startBalance"));
        }else{
            config.getConfig().set("balance", 0);
        }
        config.getConfig().set("location.world", player.getLocation().getWorld().getName());
        config.getConfig().set("location.x", player.getLocation().getX());
        config.getConfig().set("location.y", player.getLocation().getY());
        config.getConfig().set("location.z", player.getLocation().getZ());
        config.getConfig().set("location.yaw", player.getLocation().getYaw());
        config.getConfig().set("location.pitch", player.getLocation().getPitch());

        if(autoSetup) {
            String professionStr = Feudal.getConfiguration().getConfig().getString("setup.default.profession");
            Profession.Type prof = Profession.Type.NONE;
            if(professionStr != null && !professionStr.isEmpty()) {
                if(professionStr.toLowerCase().startsWith("random")) {
                    //random profession
                    Random rand = new Random();
                    if(professionStr.contains(":")) {
                        String split[] = professionStr.split(":");
                        if(split.length == 2) {
                            List<Profession.Type> types = new ArrayList<Profession.Type>();
                            if(split[1].equalsIgnoreCase("PEASANT") || split[1].equalsIgnoreCase("P")) {
                                for(Profession.Type type : Profession.Type.values()) {
                                    if(type.CLASS.equals(SocialClass.PEASANT)) {
                                        types.add(type);
                                    }
                                }
                                prof = types.get(rand.nextInt(types.size()));
                            }else if(split[1].equalsIgnoreCase("COMMONER") || split[1].equalsIgnoreCase("C")) {
                                for(Profession.Type type : Profession.Type.values()) {
                                    if(type.CLASS.equals(SocialClass.COMMONER)) {
                                        types.add(type);
                                    }
                                }
                                prof = types.get(rand.nextInt(types.size()));
                            }else if(split[1].equalsIgnoreCase("NOBLE") || split[1].equalsIgnoreCase("N")) {
                                for(Profession.Type type : Profession.Type.values()) {
                                    if(type.CLASS.equals(SocialClass.NOBLE)) {
                                        types.add(type);
                                    }
                                }
                                prof = types.get(rand.nextInt(types.size()));
                            }else {
                                prof = Profession.Type.values()[rand.nextInt(Profession.Type.values().length)];
                            }
                        }else {
                            prof = Profession.Type.values()[rand.nextInt(Profession.Type.values().length)];
                        }
                    }else {
                        prof = Profession.Type.values()[rand.nextInt(Profession.Type.values().length)];
                    }
                }else {
                    prof = Profession.Type.fromString(professionStr);
                }
            }

            config.getConfig().set("currentProfession", prof.toString() + "/" +
                    Feudal.getConfiguration().getConfig().getInt("setup.default.level") + "/0");
        }else {
            config.getConfig().set("currentProfession", "NONE/0/0");
        }
        config.getConfig().set("pastProfessions", new ArrayList<String>());
        config.getConfig().set("timelog", new ArrayList<String>());
        config.getConfig().set("kingdomlog", new ArrayList<String>());
        config.getConfig().set("offlineMessages", new ArrayList<String>());

        config.getConfig().set("strength.xp", 0);
        if(autoSetup) {
            config.getConfig().set("strength.level", Feudal.getConfiguration().getConfig().getInt("setup.default.strength"));
        }else {
            config.getConfig().set("strength.level", 0);
        }

        config.getConfig().set("toughness.xp", 0);
        if(autoSetup) {
            config.getConfig().set("toughness.level", Feudal.getConfiguration().getConfig().getInt("setup.default.toughness"));
        }else {
            config.getConfig().set("toughness.level", 0);
        }

        config.getConfig().set("speed.xp", 0);
        if(autoSetup) {
            config.getConfig().set("speed.level", Feudal.getConfiguration().getConfig().getInt("setup.default.speed"));
        }else {
            config.getConfig().set("speed.level", 0);
        }

        config.getConfig().set("stamina.xp", 0);
        if(autoSetup) {
            config.getConfig().set("stamina.level", Feudal.getConfiguration().getConfig().getInt("setup.default.stamina"));
        }else {
            config.getConfig().set("stamina.level", 0);
        }

        config.getConfig().set("luck.xp", 0);
        if(autoSetup) {
            config.getConfig().set("luck.level", Feudal.getConfiguration().getConfig().getInt("setup.default.luck"));
        }else {
            config.getConfig().set("luck.level", 0);
        }
    }

    /**
     * Get a user's profession.
     * @return
     */
    public Profession getProfession() {
        return profession;
    }

    /**
     * Gets a user's strength.
     * @return
     */
    public Attributes getStrength() {
        return strength;
    }

    /**
     * Gets a user's toughness.
     * @return
     */
    public Attributes getToughness() {
        return toughness;
    }

    /**
     * Get a user's speed.
     * @return
     */
    public Attributes getSpeed() {
        return speed;
    }

    /**
     * Get a user's stamina
     * @return
     */
    public Attributes getStamina() {
        return stamina;
    }

    /**
     * Get a user's luck
     * @return
     */
    public Attributes getLuck() {
        return luck;
    }

    /**
     * Set a user's profession
     * @param profession2
     */
    public void setProfession(Profession profession2) {
        this.profession = profession2;
    }

    /**
     * Set a user's strength
     * @param strength
     */
    public void setStrength(Attributes strength) {
        this.strength = strength;
    }

    /**
     * Set a user's toughness
     * @param toughness
     */
    public void setToughness(Attributes toughness) {
        this.toughness = toughness;
    }

    /**
     * Set a user's speed
     * @param speed
     */
    public void setSpeed(Attributes speed) {
        this.speed = speed;
    }

    /**
     * Set a user's stamina
     * @param stamina
     */
    public void setStamina(Attributes stamina) {
        this.stamina = stamina;
    }

    /**
     * Set a user's luck
     * @param luck
     */
    public void setLuck(Attributes luck) {
        this.luck = luck;
    }

    /**
     * Get the user's player. CAN BE NULL
     * @return
     */
    public Player getPlayer(){
        return player;
    }

    /**
     * Get a user's uuid
     * @return
     */
    public String getUUID() {
        return uuid;
    }

    /**
     * Get a user's balance.
     * @return
     */
    public double getBalance() {
        syncBalance();

        MoneyGetEvent event = new MoneyGetEvent(this);
        Bukkit.getServer().getPluginManager().callEvent(event);

        if(event.isOverrided()){
            return event.getMoney();
        }else{
            return balance;
        }
    }

    public double getSaveBalance() {
        return balance;
    }

    /**
     * Makes attributes for the user take effect if they are online.  This is called when a user instance is created.
     * This will call effectProfessionData within this method.
     */
    public void effectAttributes() {
        if(this.player != null){
            Effect.health(this);
            Effect.speedMethod(this);
        }
        effectProfessionData();
    }

    /**
     * Get a list of a user's past professions.
     * @return
     */
    public ArrayList<Profession> getPastProfessions(){
        return pastProfessions;
    }

    /**
     * Add a profession to a user's past professions.
     * @param p
     */
    public void addPastProfession(Profession p){
        this.setChange(true);
        for(Profession past : pastProfessions){
            if(past.getType().equals(p.getType())){
                return;
            }
        }
        pastProfessions.add(p);
    }

    /**
     * Updates the profession data config with this user's new profession.
     */
    public void effectProfessionData() {
        //FARMER, LOGGER, HUNTER, MINER, COOK, FISHER, BUILDER, SHEPERD, SCRIBE, GUARD, ASSASSIN, ALCHEMIST,
        //BLACKSMITH, HEALER, MERCHANT, SQUIRE, KNIGHT, BARON, KING
        if(this.profession != null){
            if(!this.profession.getType().equals(Profession.Type.FARMER) && Feudal.getProfessionData().getConfig().getStringList("FARMER").contains(uuid)){
                List<String> list = Feudal.getProfessionData().getConfig().getStringList("FARMER");
                list.remove(uuid);
                Feudal.getProfessionData().getConfig().set("FARMER", list);

            }else if(!this.profession.getType().equals(Profession.Type.HUNTER) && Feudal.getProfessionData().getConfig().getStringList("LOGGER").contains(uuid)){
                List<String> list = Feudal.getProfessionData().getConfig().getStringList("LOGGER");
                list.remove(uuid);
                Feudal.getProfessionData().getConfig().set("LOGGER", list);

            }else if(!this.profession.getType().equals(Profession.Type.HUNTER) && Feudal.getProfessionData().getConfig().getStringList("HUNTER").contains(uuid)){
                List<String> list = Feudal.getProfessionData().getConfig().getStringList("HUNTER");
                list.remove(uuid);
                Feudal.getProfessionData().getConfig().set("HUNTER", list);

            }else if(!this.profession.getType().equals(Profession.Type.MINER) && Feudal.getProfessionData().getConfig().getStringList("MINER").contains(uuid)){
                List<String> list = Feudal.getProfessionData().getConfig().getStringList("MINER");
                list.remove(uuid);
                Feudal.getProfessionData().getConfig().set("MINER", list);

            }else if(!this.profession.getType().equals(Profession.Type.COOK) && Feudal.getProfessionData().getConfig().getStringList("COOK").contains(uuid)){
                List<String> list = Feudal.getProfessionData().getConfig().getStringList("COOK");
                list.remove(uuid);
                Feudal.getProfessionData().getConfig().set("COOK", list);

            }else if(!this.profession.getType().equals(Profession.Type.FISHER) && Feudal.getProfessionData().getConfig().getStringList("FISHER").contains(uuid)){
                List<String> list = Feudal.getProfessionData().getConfig().getStringList("FISHER");
                list.remove(uuid);
                Feudal.getProfessionData().getConfig().set("FISHER", list);

            }else if(!this.profession.getType().equals(Profession.Type.BUILDER) && Feudal.getProfessionData().getConfig().getStringList("BUILDER").contains(uuid)){
                List<String> list = Feudal.getProfessionData().getConfig().getStringList("BUILDER");
                list.remove(uuid);
                Feudal.getProfessionData().getConfig().set("BUILDER", list);

            }else if(!this.profession.getType().equals(Profession.Type.SHEPHERD) && Feudal.getProfessionData().getConfig().getStringList("SHEPHERD").contains(uuid)){
                List<String> list = Feudal.getProfessionData().getConfig().getStringList("SHEPHERD");
                list.remove(uuid);
                Feudal.getProfessionData().getConfig().set("SHEPHERD", list);

            }else if(!this.profession.getType().equals(Profession.Type.SCRIBE) && Feudal.getProfessionData().getConfig().getStringList("SCRIBE").contains(uuid)){
                List<String> list = Feudal.getProfessionData().getConfig().getStringList("SCRIBE");
                list.remove(uuid);
                Feudal.getProfessionData().getConfig().set("SCRIBE", list);

            }else if(!this.profession.getType().equals(Profession.Type.GUARD) && Feudal.getProfessionData().getConfig().getStringList("GUARD").contains(uuid)){
                List<String> list = Feudal.getProfessionData().getConfig().getStringList("GUARD");
                list.remove(uuid);
                Feudal.getProfessionData().getConfig().set("GUARD", list);

            }else if(!this.profession.getType().equals(Profession.Type.ASSASSIN) && Feudal.getProfessionData().getConfig().getStringList("ASSASSIN").contains(uuid)){
                List<String> list = Feudal.getProfessionData().getConfig().getStringList("ASSASSIN");
                list.remove(uuid);
                Feudal.getProfessionData().getConfig().set("ASSASSIN", list);

            }else if(!this.profession.getType().equals(Profession.Type.ALCHEMIST) && Feudal.getProfessionData().getConfig().getStringList("ALCHEMIST").contains(uuid)){
                List<String> list = Feudal.getProfessionData().getConfig().getStringList("ALCHEMIST");
                list.remove(uuid);
                Feudal.getProfessionData().getConfig().set("ALCHEMIST", list);

            }else if(!this.profession.getType().equals(Profession.Type.BLACKSMITH) && Feudal.getProfessionData().getConfig().getStringList("BLACKSMITH").contains(uuid)){
                List<String> list = Feudal.getProfessionData().getConfig().getStringList("BLACKSMITH");
                list.remove(uuid);
                Feudal.getProfessionData().getConfig().set("BLACKSMITH", list);

            }else if(!this.profession.getType().equals(Profession.Type.HEALER) && Feudal.getProfessionData().getConfig().getStringList("HEALER").contains(uuid)){
                List<String> list = Feudal.getProfessionData().getConfig().getStringList("HEALER");
                list.remove(uuid);
                Feudal.getProfessionData().getConfig().set("HEALER", list);

            }else if(!this.profession.getType().equals(Profession.Type.MERCHANT) && Feudal.getProfessionData().getConfig().getStringList("MERCHANT").contains(uuid)){
                List<String> list = Feudal.getProfessionData().getConfig().getStringList("MERCHANT");
                list.remove(uuid);
                Feudal.getProfessionData().getConfig().set("MERCHANT", list);

            }else if(!this.profession.getType().equals(Profession.Type.SQUIRE) && Feudal.getProfessionData().getConfig().getStringList("SQUIRE").contains(uuid)){
                List<String> list = Feudal.getProfessionData().getConfig().getStringList("SQUIRE");
                list.remove(uuid);
                Feudal.getProfessionData().getConfig().set("SQUIRE", list);

            }else if(!this.profession.getType().equals(Profession.Type.KNIGHT) && Feudal.getProfessionData().getConfig().getStringList("KNIGHT").contains(uuid)){
                List<String> list = Feudal.getProfessionData().getConfig().getStringList("KNIGHT");
                list.remove(uuid);
                Feudal.getProfessionData().getConfig().set("KNIGHT", list);

            }else if(!this.profession.getType().equals(Profession.Type.BARON) && Feudal.getProfessionData().getConfig().getStringList("BARON").contains(uuid)){
                List<String> list = Feudal.getProfessionData().getConfig().getStringList("BARON");
                list.remove(uuid);
                Feudal.getProfessionData().getConfig().set("BARON", list);

            }else if(!this.profession.getType().equals(Profession.Type.KING) && Feudal.getProfessionData().getConfig().getStringList("KING").contains(uuid)){
                List<String> list = Feudal.getProfessionData().getConfig().getStringList("KING");
                list.remove(uuid);
                Feudal.getProfessionData().getConfig().set("KING", list);

            }

            if(!this.profession.getType().equals(Profession.Type.NONE) && !Feudal.getProfessionData().getConfig().getStringList(this.profession.getType().toString()).contains(uuid)){
                List<String> list = Feudal.getProfessionData().getConfig().getStringList(this.profession.getType().toString());
                list.add(uuid);
                Feudal.getProfessionData().getConfig().set(this.profession.getType().toString(), list);
            }

            try {
                Feudal.getProfessionData().save();
            } catch (Exception e) {
                ErrorManager.error(92, e);

                Feudal.error("Failed to save config: " + Feudal.getProfessionData().getFile().getAbsolutePath());
            }
        }
    }

    /**
     * Gets user's name
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Gets reputation
     * @return
     */
    public int getReputation() {
        return reputation;
    }

    /**
     * Get the config for this user
     * @return
     */
    public Configuration getConfig(){
        return config;
    }

    /**
     * Get the player's kingdom uuid.  This SHOULD be empty but not null if they are not in a kingdom.
     * @return
     */
    public String getKingdomUUID() {
        return kingdomUUID;
    }

    /**
     * Sets a player's kingdom uuid. (What kingdom they are in)
     * @param uuid
     */
    public void setKingdomUUID(String uuid) {
        this.setChange(true);
        if(kingdomUUID != null && !kingdomUUID.isEmpty() && (uuid == null || uuid.isEmpty())){
            Kingdom kingdom = Feudal.getAPI().getKingdom(this);
            if(kingdom != null){
                Bukkit.getPluginManager().callEvent(new LeaveKingdomEvent(kingdom, this));
            }
        }
        kingdomUUID = uuid;
    }

    /**
     * Add past kingdom to this user's kingdom log
     * @param kingdomLog
     */
    public void addPastKingdom(KingdomLog kingdomLog) {
        this.kingdomLogs.add(kingdomLog);
        changeKingdomLog = true;
    }

    /**
     * Get this user's kingdom log. (Joining and leaving of kingdoms)
     * @return
     */
    public ArrayList<KingdomLog> getPastKingdoms(){
        return this.kingdomLogs;
    }

    /**
     * Gets this users @OnlineTime
     * @return
     */
    public OnlineTime getOnlineTime() {
        return onlineTime;
    }

    /**
     * Get when this user was last online.  Last online is the last time they joined.
     * @return
     */
    public long getLastOnline() {
        return this.lastJoin;
    }

    /**
     * Get the time when a user first joined.
     * @return
     */
    public long getFirstJoinTime() {
        return this.firstJoin;
    }

    /**
     * Change a user's reputation. int1 is the change.
     * @param int1
     * @param reason
     */
    public void effectReputation(int int1, String reason) {
        if(int1 != 0){
            this.setChange(true);

            ReputationChangeEvent event = new ReputationChangeEvent(this, int1, reason);
            Bukkit.getPluginManager().callEvent(event);
            if(event.isCancelled()){
                return;
            }else{
                int1 = event.getReputationChange();
            }

            reputation += int1;
            if(reputation > Feudal.getConfiguration().getConfig().getInt("reputation.max")){
                reputation = (short) Feudal.getConfiguration().getConfig().getInt("reputation.max");
            }else if(reputation < Feudal.getConfiguration().getConfig().getInt("reputation.min")){
                reputation = (short) Feudal.getConfiguration().getConfig().getInt("reputation.min");
            }
            if(player != null && player.isOnline()){
                player.sendMessage(Feudal.getMessage("reputation.c").replace("%change%", int1+"").replace("%reason%", reason));
                player.sendMessage(Feudal.getMessage("reputation.n").replace("%rep%", reputation+""));
            }
        }
    }

    /**
     * Sends this user a message which they will get the next time they join.
     * @param string
     */
    public void sendOfflineMessage(String string) {
        offlineMessages.add(string);
        changeOfflineMessage = true;

        try {
            this.save(true);
        }catch(Exception e) {
            ErrorManager.error(630322218, e);
        }
    }

    public void sendOfflineMessages(List<String> messages) {
        offlineMessages.addAll(messages);
        changeOfflineMessage = true;

        try {
            this.save(true);
        }catch(Exception e) {
            ErrorManager.error(631322218, e);
        }
    }

    /**
     * Get this user's current kingdom.  This kingdom is the kingdom which owns the land this user is on.  Can be null.
     * @return
     */
    public Kingdom getCurrentKingdom() {
        return this.currentKingdom;
    }

    /**
     * Sets a player's current kingdom. This kingdom is the kingdom which owns the land this user is on.
     * @param k
     */
    public void setCurrentKingdom(Kingdom k) {
        this.currentKingdom = k;
    }

    /**
     * Get this user's last land message.  The last time they were notified of changing land claims. This is used to avoid spaming the player.
     * @return
     */
    public long getLastLandMessage() {
        return lastLandMessage;
    }

    /**
     * Sets this user's last land message.
     * @param currentTimeMillis
     */
    public void setLastLandMessage(long currentTimeMillis) {
        lastLandMessage = currentTimeMillis;
    }

    /**
     * Check if this user is using kingdom chat, ally chat, or normal chat
     * @return
     */
    public byte getChat() {
        return chat;
    }

    /**
     * Sets this user to using kingdom chat or ally chat.
     * Normal = 0
     * Kingdom = 1
     * Allies and kingdom = 2
     * @param chat
     */
    public void setChat(byte chat){
        this.chat = chat;
    }

    /**
     * Get all attribute levels added together.
     * @return
     */
    public int getTotalAtt() {
        int t = 0;
        if(strength != null){
            t += strength.getLevel();
        }
        if(toughness != null){
            t += toughness.getLevel();
        }
        if(speed != null){
            t += speed.getLevel();
        }
        if(stamina != null){
            t += stamina.getLevel();
        }
        if(luck != null){
            t += luck.getLevel();
        }
        return t;
    }

    /**
     * Decrease damage % accordingly based on stamina level.  This is called when a player swings their arm and hits their enemy.
     */
    public void staminaHit(){
        if(maxStaminaDamage == -1){
            maxStaminaDamage = Feudal.getConfiguration().getConfig().getDouble("Stamina.defaultDamage");
        }
        if(minStaminaDamage == -1){
            minStaminaDamage = Feudal.getConfiguration().getConfig().getDouble("Stamina.minDamage");
        }

        if(this.stamina != null){
            float f = Attributes.getStaminaRemove(stamina.getLevel());
            if(f == 0){
                f = 1;
            }
            staminaDamage -= (maxStaminaDamage - minStaminaDamage) / f;
            if(staminaDamage < minStaminaDamage){
                staminaDamage = minStaminaDamage;
            }
        }
    }

    /**
     * This is called to regen the damage multiplier effected by stamina.
     */
    public void regenStamina() {
        if(maxStaminaDamage == -1){
            maxStaminaDamage = Feudal.getConfiguration().getConfig().getDouble("Stamina.defaultDamage");
        }
        if(staminaRegen == -1){
            staminaRegen = Feudal.getConfiguration().getConfig().getDouble("Stamina.damageRegen");
        }

        staminaDamage += staminaRegen;
        if(staminaDamage > maxStaminaDamage){
            staminaDamage = maxStaminaDamage;
        }
    }

    /**
     * Gets this user's stamina damage multiplier.
     * @return
     */
    public double getStaminaDamage() {
        return staminaDamage;
    }

    /**
     * Gets the uuid of the user which this user previously tracked with a tracker.
     * @return
     */
    public String getLastTrack() {
        return lastTrack;
    }

    /**
     * Sets the uuid of the user which this user previously tracked with a tracker.
     * @param uuid
     */
    public void setLastTrack(String uuid) {
        lastTrack = uuid;
    }

    public void setShield(int i) {
        this.setChange(true);
        int days = Feudal.getConfiguration().getConfig().getInt("kingdom.shield."+(i == 0 ? "vacation" : i)+".coolDownDays");
        this.cooldownShield = System.currentTimeMillis() + (days * 86400000);
    }

    public boolean canShield() {
        if(this.cooldownShield > 0){
            if(System.currentTimeMillis() > cooldownShield){
                return true;
            }else{
                return false;
            }
        }else{
            return true;
        }
    }

    public String shieldCooldown() {
        long rem = System.currentTimeMillis() - cooldownShield;
        long weeks = (rem) / 604800000L;
        long days = (rem-(weeks * 604800000L)) / 86400000;
        long hours = ((rem - (days * 86400000) -(weeks * 604800000L)) / 3600000);
        long minutes = (rem - (days * 86400000) - (hours * 3600000)-(weeks * 604800000L)) / 60000;
        long seconds = (rem - (days * 86400000) - (hours * 3600000) - (minutes * 60000)-(weeks * 604800000L)) / 1000;
        if(weeks == 0){
            return days + (days == 1 ? " day" : " days") + " " + hours + ":" + minutes + ":" + seconds + " ago";
        }else{
            return weeks + (weeks == 1 ? " week" : " weeks") + ", " + days + (days == 1 ? " day" : " days") + " " + hours + ":" + minutes + ":" + seconds + " ago";
        }
    }

    public void setHome(boolean b) {
        home = b;
    }

    public boolean canHome(){
        return home;
    }

    public void unloadPermissions() {
        if(player != null && player.isOnline() && Bukkit.getPluginManager().isPluginEnabled("Vault") && VaultUtils.hasPerms()){
            if(profession != null && !profession.getType().equals(Profession.Type.NONE)){
                List<String> perms = Feudal.getConfiguration().getConfig().getStringList(profession.getType().getName() + ".permissions");
                for(String perm : perms){
                    VaultUtils.removePermission(player, perm);
                }
            }
        }
    }

    public void loadPermissions() {
        if(player != null && player.isOnline() && Bukkit.getPluginManager().isPluginEnabled("Vault") && VaultUtils.hasPerms()){
            if(profession != null && !profession.getType().equals(Profession.Type.NONE)){
                List<String> perms = Feudal.getConfiguration().getConfig().getStringList(profession.getType().getName() + ".permissions");
                for(String perm : perms){
                    VaultUtils.addPermission(player, perm);
                }
            }
        }
    }

    public void setLastJoin() {
        this.setChange(true);
        if(player != null && player.isOnline()){
            lastJoin = System.currentTimeMillis();
        }
    }

    public List<String> getOfflineMessages(){
        return offlineMessages;
    }

    public long getCooldownShield() {
        return cooldownShield;
    }

    public void setName(String name) {
        this.setChange(true);
        this.name = name;
    }

    public void setReputation(short reputation) {
        this.setChange(true);
        this.reputation = reputation;
    }

    public void setFirstJoin(long firstJoin) {
        this.setChange(true);
        this.firstJoin = firstJoin;
    }

    public void setBalance(double balance) {
        this.setChange(true);
        this.balance = balance;
    }

    public void setPastProfessions(ArrayList<Profession> pastProfessions) {
        this.setChange(true);
        this.pastProfessions = pastProfessions;
    }

    public void setKingdomLogs(ArrayList<KingdomLog> kingdomLogs) {
        this.setChangeKingdomLog(true);
        this.kingdomLogs = kingdomLogs;
    }

    public void setLocation(Location location) {
        this.setChange(true);
        this.location = location;
    }

    public void setLastJoin(long lastJoin) {
        this.setChange(true);
        this.lastJoin = lastJoin;
    }

    public void setCooldownShield(long cooldown) {
        this.setChange(true);
        this.cooldownShield = cooldown;
    }

    public boolean isChangeOfflineMessage() {
        return changeOfflineMessage;
    }

    public boolean isChange() {
        return change;
    }

    public boolean isChangeKingdomLog() {
        return changeKingdomLog;
    }

    public synchronized void setChangeOfflineMessage(boolean b) {
        this.changeOfflineMessage = b;
    }

    public synchronized void setChangeKingdomLog(boolean b) {
        this.changeKingdomLog = b;
    }

    public void setChange(boolean b) {
        this.change = b;
    }

    public long getInstanceCreateTime() {
        return instanceCreateTime;
    }
}