package me.invertmc.kingdoms;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.invertmc.Feudal;
import me.invertmc.sql.KingdomSave;
import me.invertmc.user.User;
import me.invertmc.utils.OnlineTime;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;


import me.invertmc.configs.Configuration;

public class Kingdom implements Comparable<Kingdom> {
    private Configuration config;
    private volatile String name;
    private volatile HashMap<String, Rank> members = new HashMap<String, Rank>(); //UUID, rank
    private volatile ArrayList<Land> land = new ArrayList<Land>();
    private volatile float treasury;
    private long createDate;
    private volatile long lastOnline;
    private boolean online = false;
    private boolean open = false;
    private boolean first = false;
    private long saftyEnd = 0;
    private short challengesWon = 0;
    private short challengesLost = 0;

    private long firstClaim = 0;

    private Shield shield = null;
    private long coolDownEnd = -1;

    private float incomeTax = 0;
    private float landTaxPercent = 1.0f;

    private String uuid;
    private int maxLand;
    private float taxRate;
    private Location home = null;
    private String homeWorld = null;
    private List<String> allies = new ArrayList<String>();//Kingdom UUID
    private List<String> enemies = new ArrayList<String>();//Kingdom UUID
    private List<String> fighters = new ArrayList<String>(); //UUID of members who can fight.
    private String description;
    private HashMap<String, Long> bans = new HashMap<String, Long>();//UUID, time to end (Bans are used to prevent an imediate counter attack) uuids of kingdoms
    private HashMap<String, Integer> wishes = new HashMap<String, Integer>();// KingdomUUID, WISH - Wishes are requests for ally, truce, or neutral. Wishes last until plugin reset.
    //0 for neutral, 1 for allies.

    private volatile boolean change = false;
    private volatile boolean changeMembers = false;
    private volatile boolean changeBans = false;
    private volatile boolean changeLand = false;
    private volatile boolean changeRelations = false;

    private HashMap<String, String> invites = new HashMap<String, String>(); //UUID : Player name - Invites last until plugin reset.

    /**
     * Used for creating a new kingdom. Call save directly after calling this.
     *
     *
     * @param config
     * @throws Exception
     */
    public Kingdom(Configuration config, String name, String uuid, String descrption, double treasury, double taxRate, long createDate, long lastOnline,
                   int maxLand, boolean alliesCanFight, HashMap<String, Rank> members, List<String> fighters, boolean open, boolean first) throws Exception{
        this.config = config;
        if(config != null && config.getConfig() != null && config.getFile().exists() || Feudal.getPlugin(Feudal.class).doesUseSql()){
            this.name = name;
            this.uuid = uuid;
            this.description = descrption;
            this.treasury = (float) treasury;
            this.taxRate = (float) taxRate;
            this.createDate = createDate;
            this.lastOnline = lastOnline;
            this.maxLand = maxLand;
            this.members = members;
            this.fighters = fighters;
            this.open = open;
            this.first = first;

            if(config == null) {
                KingdomSave.saveDefault(this);
            }
        }else{
            throw new Exception("Config not loaded.");
        }
    }

    /**
     * Load kingdom from config
     * @param config
     * @throws Exception
     */
    public Kingdom(Configuration config) throws Exception{
        this.config = config;
        if(config != null && config.getConfig() != null && config.getFile().exists()){
            try{
                name = config.getConfig().getString("name");
                treasury = (float) config.getConfig().getDouble("treasury");
                createDate = config.getConfig().getLong("createDate");
                lastOnline = config.getConfig().getLong("lastOnline");
                uuid = config.getConfig().getString("uuid");
                maxLand = config.getConfig().getInt("maxLand");
                taxRate = (float) config.getConfig().getDouble("taxRate");
                description = config.getConfig().getString("description");
                open = config.getConfig().getBoolean("open");
                challengesWon  = (short) config.getConfig().getInt("challengesWon");
                challengesLost  = (short) config.getConfig().getInt("challengesLost");
                if(config.getConfig().contains("first")){
                    first = config.getConfig().getBoolean("first");
                }
                if(config.getConfig().contains("saftyEnd")){
                    saftyEnd = config.getConfig().getLong("saftyEnd");
                }
                if(config.getConfig().contains("firstClaim")){
                    firstClaim = config.getConfig().getLong("firstClaim");
                }
                if(config.getConfig().getString("shield") != null &&
                        !config.getConfig().getString("shield").equals("")){
                    shield = new Shield(config.getConfig().getString("shield"));
                }
                coolDownEnd = config.getConfig().getLong("cooldown");
                incomeTax = (float) config.getConfig().getDouble("incomeTax");
                landTaxPercent = (float) config.getConfig().getDouble("landTaxPercent");

                for(String str : config.getConfig().getStringList("members")){
                    String split[] = str.split("/");
                    if(split.length == 2){
                        members.put(split[0], Rank.fromString(split[1]));
                    }
                }

                allies = config.getConfig().getStringList("allies");
                enemies = config.getConfig().getStringList("enemies");
                fighters = config.getConfig().getStringList("fighters");

                for(String str : config.getConfig().getStringList("bans")){
                    try{
                        String split[] = str.split("/");
                        if(split.length == 2){
                            bans.put(split[0], Long.parseLong(split[1]));
                        }
                    }catch(Exception e){
                        ErrorManager.error(81, e);
                        Feudal.error("Failed to load bans for kingdom: " + name + "[" + uuid + "]");
                    }
                }

                for(String str : config.getConfig().getStringList("land")){
                    try{
                        String split[] = str.split("~");
                        if(split.length == 3){
                            if(!Utils.isWorldLoaded(split[2])){
                                Utils.loadWorld(split[2]);
                            }
                            World w = Bukkit.getWorld(split[2].replace("%2", "/").replace("%1", "~").replace("%0", "%"));
                            if(w != null){
                                land.add(new Land(Integer.parseInt(split[0]), Integer.parseInt(split[1]), w, split[2].replace("%2", "/").replace("%1", "~").replace("%0", "%")));
                            }
                        }
                    }catch(Exception e){
                        ErrorManager.error(82, e);
                        Feudal.error("Failed to load some land for kingdom: " + name + "[" + uuid + "]");
                    }
                }

                if(config.getConfig().getBoolean("home.active")){
                    homeWorld = config.getConfig().getString("home.world");
                    if(!isWorldLoaded(homeWorld)){
                        loadWorld(homeWorld);
                    }
                    home = new Location(Bukkit.getWorld(homeWorld), config.getConfig().getDouble("home.x"), config.getConfig().getDouble("home.y"), config.getConfig().getDouble("home.z"), (float) config.getConfig().getDouble("home.yaw"), (float)config.getConfig().getDouble("home.pitch"));
                }
                if(config.getConfig().getString("bannerBackground") != null && !config.getConfig().getString("bannerBackground").equals("")){
                    try{
                        bannerBaseColor = DyeColor.valueOf(config.getConfig().getString("bannerBackground"));
                    }catch(Exception e){
                        bannerBaseColor = DyeColor.WHITE;
                    }
                }else{
                    bannerBaseColor = DyeColor.WHITE;
                }
                if(!Feudal.getVersion().equals("1.7")) {
                    banner = new BannerPattern();
                    for(String str : config.getConfig().getStringList("banner")){
                        try{
                            String split[] = str.split("/");
                            if(split.length == 2){
                                banner.add(split[0], split[1]);
                            }
                        }catch(Exception e){
                            ErrorManager.error(83, e);

                            Feudal.error("Failed to load banner pattern for kingdom: " + name + "[" + uuid + "]");
                        }
                    }
                }

                updateLand();

                if(checkBans()){
                    try{
                        this.save();
                    }catch(Exception e){
                        ErrorManager.error(84, e);
                    }
                }
            }catch(Exception e){
                throw e;
            }
        }else{
            throw new Exception("Failed to load kingdom from config. [1]");
        }
    }

    /**
     * Load kingdom from config
     * @param config
     * @throws Exception
     */
    public Kingdom(ResultSet set) throws Exception{
        try{
            name = set.getString("name");
            treasury = (float)set.getDouble("treasury");
            createDate = set.getLong("createData");
            lastOnline = set.getLong("lastOnline");
            uuid = set.getString("uuid");
            maxLand = set.getInt("maxLand");
            taxRate = (float) set.getDouble("taxRate");
            description = set.getString("description");
            open = set.getInt("open")==1;
            challengesWon  = (short) set.getInt("challengesWon");
            challengesLost  = (short) set.getInt("challengesLost");
            first = set.getInt("first") == 1;
            saftyEnd = set.getLong("saftyEnd");
            firstClaim = set.getLong("firstClaim");
            if(!set.getString("shield").isEmpty()) {
                shield = new Shield(set.getString("shield"));
            }
            coolDownEnd = set.getLong("cooldown");
            incomeTax = (float) set.getDouble("incomeTax");
            landTaxPercent = (float) set.getDouble("landTaxPercent");

            if(set.getInt("homeActive") == 1){
                homeWorld = set.getString("homeWorld");
                if(!isWorldLoaded(homeWorld)){
                    loadWorld(homeWorld);
                }
                home = new Location(Bukkit.getWorld(homeWorld),
                        set.getDouble("homeX"),
                        set.getDouble("homeY"),
                        set.getDouble("homeZ"),
                        (float) set.getDouble("homeYaw"),
                        (float) set.getDouble("homePitch"));
            }
            if(!set.getString("bannerBackground").equals("")){
                try{
                    bannerBaseColor = DyeColor.valueOf(set.getString("bannerBackground"));
                }catch(Exception e){
                    bannerBaseColor = DyeColor.WHITE;
                }
            }else{
                bannerBaseColor = DyeColor.WHITE;
            }
            if(!Feudal.getVersion().equals("1.7")) {
                banner = new BannerPattern();
                if(set.getString("banner").contains("~")) {
                    for(String str : set.getString("banner").split("~")){
                        try{
                            String split[] = str.split("/");
                            if(split.length == 2){
                                banner.add(split[0], split[1]);
                            }
                        }catch(Exception e){
                            ErrorManager.error(83, e);

                            Feudal.error("Failed to load banner pattern for kingdom: " + name + "[" + uuid + "]");
                        }
                    }
                }
            }

            SQLControl sql = Feudal.getPlugin().getSql();

            fighters = new ArrayList<String>();
            set = sql.getMembersSQL().executeQuery("SELECT * FROM " + sql.getMembersSQL().getTable().getName() + " WHERE kingdom_uuid='" + uuid + "'", false);
            while(set != null && set.next()) {
                try {
                    String split[] = set.getString("player").split("/");
                    if(split.length == 2) {
                        members.put(split[0], Rank.fromString(split[1]));
                        if(set.getInt("fighter") == 1) {
                            fighters.add(split[0]);
                        }
                    }
                }catch(Exception e) {
                    e.printStackTrace();
                    Feudal.error("Failed to load kingdom member for " + uuid + " data: " + set.getString("player"));
                }
            }

            allies = new ArrayList<String>();
            enemies = new ArrayList<String>();
            set = sql.getRelationsSQL().executeQuery("SELECT * FROM " + sql.getRelationsSQL().getTable().getName() + " WHERE kingdom_uuid='" + uuid + "'", false);
            while(set != null && set.next()) {
                int relation = set.getInt("relation");
                if(relation == 1) {
                    allies.add(set.getString("other_uuid"));
                }else if(relation == 2) {
                    enemies.add(set.getString("other_uuid"));
                }
            }

            bans = new HashMap<String, Long>();
            set = sql.getBansSQL().executeQuery("SELECT * FROM " + sql.getBansSQL().getTable().getName() + " WHERE kingdom_uuid='" + uuid + "'", false);
            while(set != null && set.next()) {
                String split[] = set.getString("ban").split("/");
                if(split.length == 2) {
                    bans.put(split[0], Long.parseLong(split[1]));
                }
            }

            land = new ArrayList<Land>();
            set = sql.getLandSQL().executeQuery("SELECT * FROM " + sql.getLandSQL().getTable().getName() + " WHERE kingdom_uuid='" + uuid + "'", false);
            while(set != null && set.next()) {
                try {
                    String split[] = set.getString("land").split("~");
                    if(split.length == 3){
                        if(!Utils.isWorldLoaded(split[2])){
                            Utils.loadWorld(split[2]);
                        }
                        World w = Bukkit.getWorld(split[2].replace("%2", "/").replace("%1", "~").replace("%0", "%"));
                        if(w != null){
                            land.add(new Land(Integer.parseInt(split[0]), Integer.parseInt(split[1]), w, split[2].replace("%2", "/").replace("%1", "~").replace("%0", "%")));
                        }
                    }
                }catch(Exception e) {
                    ErrorManager.error(82222, e);
                    Feudal.error("Failed to load some land for kingdom: " + name + "[" + uuid + "]");
                }
            }

            updateLand();

            if(checkBans()){
                try{
                    this.save();
                }catch(Exception e){
                    ErrorManager.error(84, e);
                }
            }
        }catch(Exception e){
            throw e;
        }
    }

    public void save() throws Exception{
        save(true);
    }

    /**
     * Save kingdom to config
     * @throws Exception
     */
    public void save(boolean async) throws Exception{
        checkBans();

        if(Feudal.getPlugin().doesUseSql()) {
            KingdomSave.save(this, async);
            return;
        }

        this.setChange(false);
        this.setChangeBans(false);
        this.setChangeMembers(false);
        this.setChangeLand(false);
        this.setChangeRelations(false);

        config.getConfig().set("name", name);
        config.getConfig().set("treasury", treasury);
        config.getConfig().set("createDate", createDate);
        config.getConfig().set("lastOnline", lastOnline);
        config.getConfig().set("uuid", uuid);
        config.getConfig().set("maxLand", maxLand);
        config.getConfig().set("taxRate", taxRate);
        config.getConfig().set("description", description);
        config.getConfig().set("open", open);
        config.getConfig().set("challengesWon", challengesWon);
        config.getConfig().set("challengesLost", challengesLost);
        config.getConfig().set("firstClaim", firstClaim);
        config.getConfig().set("cooldown", coolDownEnd);
        config.getConfig().set("first", first);
        config.getConfig().set("saftyEnd", saftyEnd);
        if(shield != null){
            config.getConfig().set("shield", shield.getSaveString());
        }else{
            config.getConfig().set("shield", "");
        }
        config.getConfig().set("incomeTax", incomeTax);
        config.getConfig().set("landTaxPercent", landTaxPercent);

        ArrayList<String> mem = new ArrayList<String>();
        for(String str : members.keySet()){
            mem.add(str + "/" + members.get(str).toString());
        }
        config.getConfig().set("members", mem);

        config.getConfig().set("allies", allies);
        config.getConfig().set("enemies", enemies);
        config.getConfig().set("fighters", fighters);

        ArrayList<String> b = new ArrayList<String>();
        if(bans != null){
            for(String str : bans.keySet()){
                b.add(str + "/" + bans.get(str));
            }
        }else{
            bans = new HashMap<String, Long>();
        }
        config.getConfig().set("bans", b);

        ArrayList<String> landSave = new ArrayList<String>();
        for(Land l : land){
            if(l != null){
                String str = l.getX() + "~" + l.getZ() + "~" + l.getWorldName().replace("%", "%0").replace("~", "%1").replace("/", "%2");
                landSave.add(str);
            }
        }
        config.getConfig().set("land", landSave);

        if(home != null){
            config.getConfig().set("home.active", true);
            config.getConfig().set("home.world", homeWorld);
            config.getConfig().set("home.x", home.getX());
            config.getConfig().set("home.y", home.getY());
            config.getConfig().set("home.z", home.getZ());
            config.getConfig().set("home.yaw", home.getYaw());
            config.getConfig().set("home.pitch", home.getPitch());
        }else{
            config.getConfig().set("home.active", false);
        }

        config.getConfig().set("bannerBackground", bannerBaseColor.toString());
        if(!Feudal.getVersion().equals("1.7") && banner != null) {
            config.getConfig().set("banner", banner.getBannerList());
        }
        config.save();
    }

    /**
     * Refresh claimed land
     */
    public void updateLand() {
        ArrayList<Land> updated = new ArrayList<Land>();
        for(Land l : land){
            if(!updated.contains(l)){
                updated.add(l);
            }
        }
        land = updated;
        for(Land l : land){
            for(Land land : Feudal.getLands().keySet()){
                if(l.equals(land)){
                    continue;
                }
            }
            Feudal.getLands().put(l, this);
        }
        changeLand = true;
    }

    /**
     * Get land total
     * @return
     */
    public int getLandAmount(){
        return land.size();
    }

    /**
     * Check if player is a member with their uuid string.
     * @param uuid
     * @return
     */
    public boolean isMember(String uuid) {
        if(members.containsKey(uuid)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Get kingdom name
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Check if player was invited or is allowed to join.
     * @param uuid
     * @return
     */
    public boolean canJoin(String uuid) {
        if(open){
            return true;
        }else if(invites.containsKey(uuid)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Message all members
     * @param string
     * @param save
     */
    public void messageMembers(final String string, boolean save) {
        for(final String mem : members.keySet()){
            try {
                OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(mem));
                if(player != null) {
                    if(player.isOnline()){
                        player.getPlayer().sendMessage(string);
                    }else if(save){
                        new Thread(new Runnable() {
                            public void run() {
                                User user = Feudal.getUser(mem);
                                if(user != null) {
                                    user.sendOfflineMessage(string);
                                    try{
                                        user.save(true);
                                    }catch(Exception e){
                                        ErrorManager.error(85, e);
                                    }
                                }
                            }
                        }).start();
                    }
                }
            }catch(Exception e) {
                ErrorManager.error(146392018, e);
            }
        }
    }

    /**
     * Add a member with user and set their initial rank.
     * @param user
     * @param rank
     */
    public void addMember(User user, Rank rank) {
        if(this.invites.containsKey(user.getUUID())){
            this.invites.remove(user.getUUID());
        }
        members.put(user.getUUID(), rank);
        changeMembers = true;
		/*if(user.getProfession() != null && (user.getProfession().getType().equals(Type.GUARD) || user.getProfession().getType().equals(Type.ASSASSIN) ||
				user.getProfession().getType().equals(Type.SQUIRE) || user.getProfession().getType().equals(Type.KNIGHT) || user.getProfession().getType().equals(Type.BARON) ||
				user.getProfession().getType().equals(Type.KING))){
			fighters.add(user.getUUID());
		}*/
    }

    /**
     * Get kingdom's last online time in ms.
     * @return
     */
    public long getLastOnline() {
        return lastOnline;
    }

    /**
     * Check if the kingdom is currently online.
     */
    public void checkOnline() {
        boolean o = false;
        for(String s : members.keySet()){
            Player p = Bukkit.getPlayer(UUID.fromString(s));
            if(p != null && p.isOnline()){
                o = true;
                break;
            }
        }
        online = o;
        if(o){
            lastOnline = System.currentTimeMillis();
        }
        change = true;
    }

    /**
     * Returns if the kingdom is online.  Different from checkOnline() because this can only be used after checkOnline() checks.
     * @return
     */
    public boolean isOnline(){
        return online;
    }

    /**
     * Remove a member via their uuid.
     * @param uuid
     */
    public void removeMember(String uuid) {
        members.remove(uuid);
        if(fighters.contains(uuid)){
            fighters.remove(uuid);
        }
        changeMembers = true;
    }

    /**
     * Get kingdom uuid.
     * @return
     */
    public String getUUID() {
        return uuid;
    }

    /**
     * Add user to invite list (RAM only).
     * @param uuid
     * @param name
     */
    public void invite(String uuid, String name) {
        invites.put(uuid, name);
    }

    /**
     * Removed invited person
     * @param uuid2
     */
    public void unInvite(String uuid2) {
        invites.remove(uuid2);
    }

    /**
     * Check if a player has been invited with their uuid.
     * @param uuid
     * @return
     */
    public boolean hasBeenInvited(String uuid) {
        return invites.containsKey(uuid);
    }

    /**
     * Get the kingdom rank of a player with their uuid.
     * @param uuid
     * @return
     */
    public Rank getRank(String uuid) {
        if(members.containsKey(uuid)){
            return members.get(uuid);
        }else{
            return null;
        }
    }

    /**
     * Get if open joining is allowed.
     * @return
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * Toggle kingdom for open joining.
     * @param open2
     */
    public void setOpen(boolean open2) {
        open = open2;
        change = true;
    }

    /**
     * Delete this kingdom with name being the person who caused the deletion.
     * @param name
     */
    public void delete(String name) {

        Bukkit.getPluginManager().callEvent(new KingdomDeleteEvent(this, name));

        for(Challenge c : Feudal.getChallenges(this)){
            //if(c.getDefender().getUUID().equals(this.uuid)){
            c.surrender(this);
            //}
        }

        messageMembers(Feudal.getMessage("kingdom.delete").replace("%kingdom%", this.name).replace("%name%", name), true);
        ArrayList<Land> remove = new ArrayList<Land>();
        for(Land l : Feudal.getLands().keySet()){
            if(Feudal.getLands().get(l).equals(this)){
                remove.add(l);
            }
        }
        change = true;
        for(Land l : remove){
            Feudal.getLands().remove(l);
        }
        if(Feudal.getPlugin().doesUseSql()) {
            KingdomSave.delete(this);
        }else {
            config.getFile().delete();
        }
    }

    /**
     * Change leaders
     * @param promote
     */
    public void setLeader(User promote) {
        String leader = null;
        for(String mem : members.keySet()){
            if(members.get(mem) != null){
                if(members.get(mem).equals(Rank.LEADER)){
                    leader = mem;
                }
            }
        }
        members.put(leader, Rank.EXECUTIVE);
        members.put(promote.getUUID(), Rank.LEADER);

        if(promote.getPlayer() != null && promote.getPlayer().isOnline()){
            promote.getPlayer().sendMessage(Feudal.getMessage("kingdom.leader"));
        }

        messageMembers(Feudal.getMessage("kingdom.leaderall").replace("%leader%", promote.getName()), false);
        changeMembers = true;
        change = true;
    }

    /**
     * Get hashmap of members. uuid, rank
     * @return
     */
    public HashMap<String, Rank> getMembers() {
        return members;
    }

    /**
     * Get members ordered by online then by rank
     * @return List of members
     */
    public List<Member> getMembersOrdered() {
        List<Member> m = new ArrayList<Member>();
        for(String uuid : members.keySet()) {
            try {
                m.add(new Member(uuid, members.get(uuid), fighters.contains(uuid)));
            }catch(Exception e) {
                ErrorManager.error(209392018, e);
            }
        }
        Collections.sort(m);
        return m;
    }

    /**
     * Set kingdom as an enemy.
     * @param kingdom
     * @param b
     */
    public void enemy(Kingdom kingdom, boolean b) {
        this.enemies.add(kingdom.getUUID());
        this.allies.remove(kingdom.getUUID());
        if(b){
            messageMembers(Feudal.getMessage("kingdom.set.enemy").replace("%kingdom%", kingdom.getName()), false);
        }else{
            messageMembers(Feudal.getMessage("kingdom.enemy").replace("%kingdom%", kingdom.getName()), false);
        }
        changeRelations = true;
    }

    /**
     * Check if allied
     * @param other
     * @return
     */
    public boolean isAllied(Kingdom other) {
        return this.allies.contains(other.getUUID());
    }

    /**
     * Check if enemied
     * @param other
     * @return
     */
    public boolean isEnemied(Kingdom other) {
        return this.enemies.contains(other.getUUID());
    }

    /**
     * Set kingdom as neutral
     * @param kingdom
     */
    public void neutral(Kingdom kingdom) {
        if(enemies.contains(kingdom.getUUID())){
            enemies.remove(kingdom.getUUID());
        }
        if(allies.contains(kingdom.getUUID())){
            allies.remove(kingdom.getUUID());
        }
        messageMembers(Feudal.getMessage("kingdom.set.neutral").replace("%kingdom%", kingdom.getName()), false);
        changeRelations = true;
    }

    /**
     * Request kingdom to be neutral
     * @param other
     * @return
     */
    public boolean requestNeutral(Kingdom other) {
        if(wishes.containsKey(other.getUUID())){
            if(wishes.get(other.getUUID()).intValue() == 0){
                return false;
            }
        }
        wishes.put(other.getUUID(), 0);
        return true;
    }

    /**
     * Check if neutral request has already been sent.
     * @param kingdom
     * @return
     */
    public boolean hasRequestedNeutral(Kingdom kingdom) {
        if(wishes.containsKey(kingdom.getUUID())){
            if(wishes.get(kingdom.getUUID()).intValue() == 0){
                return true;
            }
        }
        return false;
    }

    /**
     * Request ally
     * @param other
     * @return
     */
    public boolean requestAlly(Kingdom other) {
        if(wishes.containsKey(other.getUUID())){
            if(wishes.get(other.getUUID()).intValue() == 1){
                return false;
            }
        }
        wishes.put(other.getUUID(), 1);
        return true;
    }

    /**
     * Add kingdom as ally
     * @param kingdom
     */
    public void ally(Kingdom kingdom) {
        if(enemies.contains(kingdom.getUUID())){
            enemies.remove(kingdom.getUUID());
        }
        this.allies.add(kingdom.getUUID());
        messageMembers(Feudal.getMessage("kingdom.set.ally").replace("%kingdom%", kingdom.getName()), false);
        changeRelations = true;
    }

    /**
     * Check if ally request has been sent.
     * @param kingdom
     * @return
     */
    public boolean hasRequestedAlly(Kingdom kingdom) {
        if(wishes.containsKey(kingdom.getUUID())){
            if(wishes.get(kingdom.getUUID()).intValue() == 1){
                return true;
            }
        }
        return false;
    }

    /**
     * Get max amount of land which can be claimed.
     * @return
     */
    public int getMaxLand() {
        return maxLand;
    }

    /**
     * Check if land is connected to another piece of land.
     * @param land2
     * @return
     */
    public boolean isLandConnected(Land land2) {
        if(land.size() == 0){
            return true;
        }
        for(Land l : land){
            if(l.isConnected(land2)){
                return true;
            }
        }
        return false;
    }

    /**
     * Update the claim time.  Used for attack delays.
     */
    public void updateClaimTime(){
        if(first){
            first = false;
            saftyEnd = System.currentTimeMillis() + (long)(Feudal.getConfiguration().getConfig().getDouble("kingdom.startShieldHours") * 3600000);
        }else{
            this.firstClaim = System.currentTimeMillis();
        }
        change = true;
    }

    public boolean isFirst() {
        return first;
    }

    /**
     * Add land to this kingdom
     * @param land2
     */
    public void addLand(Land land2) {
        land.add(land2);
        changeLand = true;
    }

    /**
     * Get current land claimed
     * @return
     */
    public ArrayList<Land> getLand() {
        return land;
    }

    /**
     * Set kingdom home
     * @param location
     */
    public void setHome(Location location) {
        home = location;
        homeWorld = location.getWorld().getName();
        change = true;
    }

    /**
     * Get kingdom home
     * Can be null
     * @return
     */
    public Location getHome() {
        if(home != null){
            if(!isWorldLoaded(homeWorld)){
                loadWorld(homeWorld);
            }
            Land l = new Land(home);
            if(!this.land.contains(l)){
                home = null;
            }
        }
        return home;
    }

    /**
     * Remove all members
     * @deprecated
     */
    @Deprecated
    public void removeAllMembers() {
        for(String uuid : members.keySet()){
            User u = Feudal.getUser(uuid);
            if(u == null){
                Bukkit.getConsoleSender().sendMessage("[Feudal] Failed to remove member: " + uuid + " from kingdom: " + this.name);
                continue;
            }
            u.setKingdomUUID("");
            u.addPastKingdom(new KingdomLog(this.uuid, System.currentTimeMillis(), false));
            changeMembers = true;
            u.save(true);
        }
    }

    /**
     * Get treasury value.  Has no relation with Vault
     * @return
     */
    public double getTreasury() {
        return this.treasury;
    }

    /**
     * Set treasury value.
     * @param d
     */
    public void setTreasury(double d) {
        treasury = (float) d;
        change = true;
    }

    /**
     * Get kingdom description
     * @return
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Set the description
     * @param description2
     */
    public void setDescription(String description2) {
        description = description2;
        change = true;
    }

    /**
     * Get the banner dye color.
     * @return
     */
    public DyeColor getBannerBaseColor() {
        return this.bannerBaseColor;
    }

    /**
     * Get the banner patterns
     * @return
     */
    public BannerPattern getBannerPatterns() {
        return this.banner;
    }

    /**
     * Set banner color.
     * @param baseColor
     */
    public void setBannerBaseColor(DyeColor baseColor) {
        if(baseColor == null){
            this.bannerBaseColor = Banner.randomBaseColor();
        }else{
            this.bannerBaseColor = baseColor;
        }
        change = true;
    }

    /**
     * Set banner pattern
     * @param patterns
     */
    public void setBannerPattern(BannerPattern patterns) {
        this.banner = patterns;
        change = true;
    }

    /**
     * Set kingdom name
     * @param string
     */
    public void setName(String string) {
        name = string;
        change = true;
    }

    /**
     * Get all member's online times.
     * @return
     */
    public ArrayList<OnlineTime> getMembersOnlineTime() {
        ArrayList<OnlineTime> time = new ArrayList<OnlineTime>();
        for(String uuid : members.keySet()){
            if(uuid != null){
                User u = Feudal.getUser(uuid);
                if(u != null){
                    time.add(u.getOnlineTime());
                }
            }
        }
        return time;
    }

    /**
     * Promote a member
     * @param uuid2
     */
    public void promote(String uuid2) {
        if(members.containsKey(uuid2)){
            Rank rank = members.get(uuid2);
            if(rank.equals(Rank.GUEST)){
                rank = Rank.MEMBER;
            }else if(rank.equals(Rank.MEMBER)){
                rank = Rank.EXECUTIVE;
            }
            members.put(uuid2, rank);
            changeMembers = true;
        }
    }

    public void setRank(String uuid, Rank rank) {
        if(members.containsKey(uuid)){
            members.put(uuid, rank);
        }
        changeMembers = true;
    }

    /**
     * Demote a member
     * @param uuid2
     */
    public void demote(String uuid2) {
        if(members.containsKey(uuid2)){
            Rank rank = members.get(uuid2);
            if(rank.equals(Rank.EXECUTIVE)){
                rank = Rank.MEMBER;
            }else if(rank.equals(Rank.MEMBER)){
                rank = Rank.GUEST;
            }
            members.put(uuid2, rank);
        }
        changeMembers = true;
    }

    /**
     * Get number of members online
     * @return
     */
    public Integer getOnline() {
        int total = 0;
        for(String s : members.keySet()){
            User u = Feudal.getUser(s);
            if(u != null && u.getPlayer() != null && u.getPlayer().isOnline()){
                total++;
            }
        }
        return total;
    }

    /**
     * Check if a location is on the kingdom land.
     * @param location
     * @return
     */
    public boolean isOnLand(Location location) {
        Land l = new Land(location);
        if(this.land.contains(l)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Send a player the kingdom's information.
     * @param p
     */
    public void kingdomInfo(Player p) {
        float landTax = Feudal.round(this.land.size() * Feudal.getConfiguration().getConfig().getDouble("kingdom.land.tax")*this.landTaxPercent);

        p.sendMessage(Feudal.getMessage("kingdom.info.title").replace("%kingdom%", name).replace("%online%", this.getOnline()+"").replace("%total%", members.size()+""));
        p.sendMessage(Feudal.getMessage("kingdom.info.desc").replace("%desc%", description));
        p.sendMessage(Feudal.getMessage("kingdom.info.ally").replace("%allies%", getAllies()));
        p.sendMessage(Feudal.getMessage("kingdom.info.enemy").replace("%enemies%", getEnemies()));
        p.sendMessage(Feudal.getMessage("kingdom.info.mem").replace("%members%", getMembersString()));
        p.sendMessage(Feudal.getMessage("kingdom.info.age").replace("%age%", getAge(System.currentTimeMillis() - this.createDate)));
        p.sendMessage(Feudal.getMessage("kingdom.info.open").replace("%open%", Feudal.getMessage("landUpdate." + this.open) + ""));
        p.sendMessage(Feudal.getMessage("kingdom.info.land").replace("%land%", this.land.size()+"").replace("%total%", this.maxLand+""));
        p.sendMessage(Feudal.getMessage("kingdom.info.landtax").replace("%tax%", landTax+""));
        p.sendMessage(Feudal.getMessage("kingdom.info.treasury").replace("%money%", Feudal.round(treasury)+""));
        p.sendMessage(Feudal.getMessage("kingdom.info.incometax").replace("%tax%", incomeTax+""));
        p.sendMessage(Feudal.getMessage("kingdom.info.challenges").replace("%won%", challengesWon+"").replace("%total%", (challengesWon + challengesLost)+""));
        p.sendMessage(Feudal.getMessage("kingdom.info.online").replace("%online%", getLastOnlineTime()));

        if(shield != null && shield.getEnd() > System.currentTimeMillis()){
            p.sendMessage(Feudal.getMessage("kingdom.info.shield").replace("%time%", shield.getDisplayString()));
        }
    }

    private String getLastOnlineTime() {
        if(online){
            return Feudal.getMessage("landUpdate.onlineNow");
        }
        long rem = System.currentTimeMillis() - lastOnline;
        long years = rem / 31557600000L;
        long weeks = (rem - (years * 31557600000L)) / 604800000L;
        long days = (rem-(years * 31557600000L)-(weeks * 604800000L)) / 86400000;
        long hours = ((rem - (days * 86400000) - (years * 31557600000L)-(weeks * 604800000L)) / 3600000);
        long minutes = (rem - (days * 86400000) - (hours * 3600000)-(years * 31557600000L)-(weeks * 604800000L)) / 60000;
        long seconds = (rem - (days * 86400000) - (hours * 3600000) - (minutes * 60000)-(years * 31557600000L)-(weeks * 604800000L)) / 1000;
        if(years == 0){
            if(weeks == 0){
                return days + (days == 1 ? Feudal.getMessage("landUpdate.day") : Feudal.getMessage("landUpdate.days")) + " " + hours + ":" + minutes + ":" + seconds;
            }else{
                return weeks + (weeks == 1 ? Feudal.getMessage("landUpdate.week") : Feudal.getMessage("landUpdate.weeks")) + ", " + days + (days == 1 ? Feudal.getMessage("landUpdate.day") : Feudal.getMessage("landUpdate.days")) + " " + hours + ":" + minutes + ":" + seconds;
            }
        }else{
            return years + (years == 1 ? Feudal.getMessage("landUpdate.year") : Feudal.getMessage("landUpdate.years")) + ", " + weeks + (weeks == 1 ? Feudal.getMessage("landUpdate.week") : Feudal.getMessage("landUpdate.weeks")) + ", " + days + (days == 1 ? Feudal.getMessage("landUpdate.day") : Feudal.getMessage("landUpdate.days")) + " " + hours + ":" + minutes + ":" + seconds;
        }
    }

    /**
     * Get the age of the kingdom is a nice string.
     * @param rem
     * @return
     */
    public static String getAge(long rem) {
        long years = rem / 31557600000L;
        long weeks = (rem - (years * 31557600000L)) / 604800000L;
        long days = (rem-(years * 31557600000L)-(weeks * 604800000L)) / 86400000;
        long hours = ((rem - (days * 86400000) - (years * 31557600000L)-(weeks * 604800000L)) / 3600000);
        long minutes = (rem - (days * 86400000) - (hours * 3600000)-(years * 31557600000L)-(weeks * 604800000L)) / 60000;
        long seconds = (rem - (days * 86400000) - (hours * 3600000) - (minutes * 60000)-(years * 31557600000L)-(weeks * 604800000L)) / 1000;
        if(years == 0){
            if(weeks == 0){
                return days + (days == 1 ? Feudal.getMessage("landUpdate.day") : Feudal.getMessage("landUpdate.days")) + " " + hours + ":" + minutes + ":" + seconds;
            }else{
                return weeks + (weeks == 1 ? Feudal.getMessage("landUpdate.week") : Feudal.getMessage("landUpdate.weeks")) + ", " + days + (days == 1 ? Feudal.getMessage("landUpdate.day") : Feudal.getMessage("landUpdate.days")) + " " + hours + ":" + minutes + ":" + seconds;
            }
        }else{
            return years + (years == 1 ? Feudal.getMessage("landUpdate.year") : Feudal.getMessage("landUpdate.years")) + ", " + weeks + (weeks == 1 ? Feudal.getMessage("landUpdate.week") : Feudal.getMessage("landUpdate.weeks")) + ", " + days + (days == 1 ? Feudal.getMessage("landUpdate.day") : Feudal.getMessage("landUpdate.days")) + " " + hours + ":" + minutes + ":" + seconds;
        }
    }

    public String getMembersString() {
        String mems = "";
        for(String mem : members.keySet()){
            String b = "";
            Rank rank = members.get(mem);
            if(rank.equals(Rank.LEADER)){
                b += "\u00a7bL:";
            }else if(rank.equals(Rank.EXECUTIVE)){
                b += "\u00a7aE:";
            }else if(rank.equals(Rank.MEMBER)){
                b += "\u00a72M:";
            }else{
                b += "\u00a7eG:";
            }
            b+="\u00a7c";
            //User u = Feudal.getUser(mem);
            try {
                OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(mem));
                if(player != null){
                    if(player.isOnline()){
                        b+="\u00a7a";
                    }
                    b+=player.getName();
                    if(this.fighters.contains(mem)){
                        b+="\u00a74(F)";
                    }
                    if(mems.equals("")){
                        mems = b;
                    }else{
                        mems += "\u00a77, " + b;
                    }
                }
            }catch(Exception e) {
                ErrorManager.error(142392018, e);
            }
        }
        return mems;
    }

    private String getEnemies() {
        if(enemies.size() > 0){
            String enemies = "";
            for(String enemy : this.enemies){
                Kingdom k = Feudal.getKingdom(enemy);
                if(k != null){
                    if(enemies.equals("")){
                        enemies += k.getName();
                    }else{
                        enemies += ", " + k.getName();
                    }
                }
            }
            return enemies;
        }else{
            return Feudal.getMessage("landUpdate.noEnemies");
        }
    }

    private String getAllies() {
        if(allies.size() > 0){
            String allies = "";
            for(String ally : this.allies){
                Kingdom k = Feudal.getKingdom(ally);
                if(k != null){
                    if(allies.equals("")){
                        allies += k.getName();
                    }else{
                        allies += ", " + k.getName();
                    }
                }
            }
            return allies;
        }else{
            return Feudal.getMessage("landUpdate.noAllies");
        }
    }

    public List<String> getAlliesList() {
        return allies;
    }

    public List<String> getEnemyList() {
        return enemies;
    }

    /**
     * Send a player information about this kingdom's challenge bans.
     * @param p
     */
    public void sendBanInfo(Player p) {
        if(checkBans()){
            try {
                this.save();
            } catch (Exception e) {
                ErrorManager.error(84, e);
            }
        }
        if(this.bans.size() > 0){
            ArrayList<String> messages = new ArrayList<String>();
            for(String ban : bans.keySet()){
                Kingdom k = Feudal.getKingdom(ban);
                if(k != null){
                    messages.add(Feudal.getMessage("kingdom.ban.info").replace("%kingdom%", k.name).replace("%age%", getAge(bans.get(ban) - System.currentTimeMillis())));
                }
            }

            if(messages.size() > 0){
                p.sendMessage(Feudal.getMessage("kingdom.ban.title").replace("%kingdom%", name));
                for(String msg : messages){
                    p.sendMessage(msg);
                }
            }else{
                p.sendMessage(Feudal.getMessage("kingdom.ban.none"));
            }
        }else{
            p.sendMessage(Feudal.getMessage("kingdom.ban.none"));
        }
    }

    /**
     * Get shield
     * Can be null
     * @return
     */
    public Shield getShield(){
        return shield;
    }

    /**
     * Set the shield
     * @param s
     */
    public void setShield(Shield s){
        shield = s;
        change = true;
    }

    /**
     * Check for bans and remove inactive ones.
     * @return
     */
    public boolean checkBans() {
        ArrayList<String> remove = new ArrayList<String>();
        for(String ban : bans.keySet()){
            if(bans.get(ban) - System.currentTimeMillis() < 1){
                remove.add(ban);
            }
        }
        boolean c = false;
        for(String ban : remove){
            bans.remove(ban);
            c = true;
        }
        if(remove.size() > 0) {
            changeBans = true;
        }

        return c;
    }

    /**
     * Disable the kingdom shield
     */
    public void disableShield() {
        long days = Feudal.getConfiguration().getConfig().getInt("kingdom.shield." + (shield.getType() == 0 ? "vacation" : shield.getType()) + ".coolDownDays");
        coolDownEnd = System.currentTimeMillis() + (days * 86400000);
        shield = null;
        change = true;
    }

    /**
     * Get when the shield cooldown ends in ms as time.
     * @return
     */
    public long getCooldownEnd() {
        return this.coolDownEnd;
    }

    /**
     * Check if kingdom has vacation shield.
     * @return
     */
    public boolean isVacation() {
        if(shield != null && shield.getType() == 0){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Check if kingdom is shielded.
     * @return
     */
    public boolean isShielded() {
        if(shield != null){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Update the income tax based on the leader.
     */
    public void updateIncomeTax(){
        for(String m : members.keySet()){
            if(members.get(m).equals(Rank.LEADER)){
                User u = Feudal.getUser(m);
                if(u != null){
                    if(u.getProfession() != null && !u.getProfession().getType().equals(Type.NONE)){
                        incomeTax = (float) Feudal.getConfiguration().getConfig().getDouble(u.getProfession().getType().getName() + ".taxPercent");
                        landTaxPercent = (float) (Feudal.getConfiguration().getConfig().getDouble(u.getProfession().getType().getName() + ".rentPercent")/100);
                    }
                }
            }
        }
    }

    /**
     * Get the income tax of this kingdom for a user from their uuid.
     * @param userUUID
     * @return
     */
    public double getIncomeTax(String userUUID) {
        if(members.containsKey(userUUID) && members.get(userUUID).equals(Rank.LEADER)){
            return 0;
        }else{
            return incomeTax / 100;
        }
    }

    /**
     * Get land tax percent change
     * @return
     */
    public double getLandTaxPercent() {
        return landTaxPercent;
    }

    /**
     * Check if player is a figher from their uuid.
     * @param uuid2
     * @return
     */
    public boolean isFighter(String uuid2) {
        for(String s : this.fighters){
            if(uuid2.equals(s)){
                return true;
            }
        }
        return false;
    }

    /**
     * Get number of online fighters
     * @return
     */
    public int getFightersOnline() {
        int online = 0;
        for(String s : fighters){
            Player p = Bukkit.getPlayer(UUID.fromString(s));
            if(p != null){
                if(p.isOnline()){
                    online++;
                }
            }
        }
        return online;
    }

    /**
     * List fighter uuids.
     * @return
     */
    public List<String> getFighters() {
        return fighters;
    }

    /**
     * Get all kingdom land claims connected to a piece of land.
     * @param landStart
     * @return
     */
    public ArrayList<Land> getConnectedLand(Land landStart) {
        ArrayList<Land> check = new ArrayList<Land>();
        ArrayList<Land> land = new ArrayList<Land>();
        ArrayList<Land> dontCheck = new ArrayList<Land>();
        land.add(landStart);
        check.add(landStart);

        while(check.size() != 0){
            ArrayList<Land> checkAdd = new ArrayList<Land>();
            for(Land l : check){
                if(!dontCheck.contains(l)){
                    ArrayList<Land> adding = getConnected(l);
                    for(Land l2 : adding){
                        if(!land.contains(l2)){
                            land.add(l2);
                            checkAdd.add(l2);
                        }
                    }
                    dontCheck.add(l);
                }
            }
            check.clear();
            for(Land l : checkAdd){
                check.add(l);
            }
        }

        return land;
    }

    private ArrayList<Land> getConnected(Land l) {
        ArrayList<Land> land = new ArrayList<Land>();
        Land up = new Land(l.getX() + 1, l.getZ(), l.getWorld(), l.getWorldName());
        Land down = new Land(l.getX() - 1, l.getZ(), l.getWorld(),l.getWorldName());
        Land left = new Land(l.getX(), l.getZ() -1, l.getWorld(),l.getWorldName());
        Land right = new Land(l.getX(), l.getZ() +1, l.getWorld(),l.getWorldName());

        Kingdom k = Feudal.getLandKingdom(up);
        if(k != null && k.equals(this)){
            land.add(up);
        }
        k = Feudal.getLandKingdom(down);
        if(k != null && k.equals(this)){
            land.add(down);
        }
        k = Feudal.getLandKingdom(left);
        if(k != null && k.equals(this)){
            land.add(left);
        }
        k = Feudal.getLandKingdom(right);
        if(k != null && k.equals(this)){
            land.add(right);
        }

        return land;
    }

    /**
     * Effect all member's reputation.
     * @param change
     * @param reason
     */
    public void effectReputation(int change, String reason){
        for(String s : members.keySet()){
            User u = Feudal.getUser(s);
            if(u != null){
                u.effectReputation(change, reason);
            }
        }
    }

    /**
     * Check if kingdom is protected or in a challenge fight.
     * @return
     */
    public boolean hasProtection() {
        for(Challenge c : Feudal.getChallenges(this)){
            if(c.isFighting() && c.getDefender().equals(this)){
                return false;
            }
        }
        return true;
    }

    public int getChallengesWon() {
        return challengesWon;
    }

    protected void setChallengesWon(int challengesWon) {
        this.challengesWon = (short) challengesWon;
        change = true;
    }

    public int getChallengesLost() {
        return challengesLost;
    }

    protected void setChallengesLost(int challengesLost) {
        this.challengesLost = (short) challengesLost;
        change = true;
    }

    protected boolean isBanned(String uuid2) {
        if(bans.containsKey(uuid2)){
            return true;
        }else{
            return false;
        }
    }

    protected String getBanInfo(Kingdom k){
        if(bans.containsKey(k.getUUID())){
            return("\u00a76" + k.name + " - "+Feudal.getMessage("landUpdate.endin")+": \u00a77" + getAge(bans.get(k.getUUID()) - System.currentTimeMillis()));
        }else{
            return "\u00a7cNo challenge ban information.";
        }
    }

    protected void addBan(String uuid2, long l) {
        this.bans.put(uuid2, l);
        changeBans = true;
    }

    protected void giveWinXP(boolean b) {
        int xp = Feudal.getConfiguration().getConfig().getInt("kingdom.challenges.xpWin");
        if(!b){
            xp = Feudal.getConfiguration().getConfig().getInt("kingdom.challenges.xpWinDefault");
        }
        for(String s : members.keySet()){
            User u = Feudal.getUser(s);
            if(u != null){
                if(u.getProfession() != null && !u.getProfession().getType().equals(Type.NONE)){
                    if(u.getProfession().getType().equals(Type.SQUIRE) || u.getProfession().getType().equals(Type.KNIGHT) || u.getProfession().getType().equals(Type.BARON) || u.getProfession().getType().equals(Type.KING) || u.getProfession().getType().equals(Type.ARCHBISHOP)){
                        xp = (int) (((double) xp) * Feudal.getConfiguration().getConfig().getDouble("kingdom.challenges.nobleWinXPMultiplier"));
                    }
                }
                XP.addXP(u, xp);
            }
        }
    }

    /**
     * Update fighters and land count based on members professions.
     */
    public void updateFightersAndLand() {
        maxLand = 0;
        for(String s : members.keySet()){
            User u = Feudal.getUser(s);
            if(u != null){
                if(u.getProfession() != null && !u.getProfession().getType().equals(Type.NONE)){
                    Type t = u.getProfession().getType();
                    if(members.get(s).equals(Rank.LEADER)){
                        if(!fighters.contains(s)){
                            fighters.add(s);
                            changeMembers = true;
                        }
                    }/*else if(t.equals(Type.GUARD) || t.equals(Type.ASSASSIN) || t.equals(Type.SQUIRE) || t.equals(Type.KNIGHT) || t.equals(Type.BARON) || t.equals(Type.KING)){
						fighters.add(s);
					}*/

                    maxLand += Profession.getLand(t);
                }
            }
        }
    }

    /**
     * Check if kingdom is fighting in a challenge
     * @return
     */
    public boolean isInFight() {
        for(Challenge c : Feudal.getChallenges(this)){
            if(c.isFighting()){
                return true;
            }
        }
        return false;
    }

    /**
     * Update all challenge updateCheckFight() related to this kingdom.
     */
    public void checkFight() {
        for(Challenge c : Feudal.getChallenges(this)){
            c.updateCheckFight();
        }
    }

    protected double getPercentMembersOnline() {
        double x = 0;
        for(String s : members.keySet()){
            Player p = Bukkit.getPlayer(UUID.fromString(s));
            if(p != null && p.isOnline()){
                x++;
            }
        }
        return x/members.size()*100;
    }

    /**
     * Get time first land was claimed since last unclaim all in ms.
     * @return
     */
    public long getFirstClaim() {
        return firstClaim;
    }

    /**
     * Check if kingdom has a special shield.  A special shield is the shield given for creating their first kingdom and claiming land for the first time.
     * @return
     */
    public boolean hasSpecialShield() {
        if(System.currentTimeMillis() < saftyEnd){
            return true;
        }else{
            return false;
        }
    }

    public long getSaftyEnd() {
        return saftyEnd;
    }

    /**
     * Disable the special shield.
     */
    public void disableSpecialShield() {
        saftyEnd = 0;
        change = true;
    }

    /**
     * Remove a fighter with their uuid.
     * @param uuid2
     */
    public void removeFigher(String uuid2) {
        if(fighters.contains(uuid2)){
            fighters.remove(uuid2);
        }
        changeMembers = true;
    }

    /**
     * Add a fighter with their uuid.
     * @param uuid2
     */
    public void addFigher(String uuid2) {
        if(!fighters.contains(uuid2)){
            fighters.add(uuid2);
        }
        changeMembers = true;
    }

    public boolean canShield() {
        if(this.coolDownEnd > 0){
            if(System.currentTimeMillis() > coolDownEnd){
                return true;
            }else{
                return false;
            }
        }else{
            return true;
        }
    }

    public void updateShield() {
        if(shield != null){
            if(System.currentTimeMillis() > shield.getEnd()){
                this.disableShield();
            }
        }
    }

    public String shieldCooldown() {
        long rem = coolDownEnd - System.currentTimeMillis();
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

    public void loadWorld(String wName) {
        if(wName != null && !wName.equals("")){
            Bukkit.getServer().createWorld(new WorldCreator(wName));
        }
    }

    public long getCreateData(){
        return createDate;
    }

    public boolean isWorldLoaded(String wName) {
        for(World w : Bukkit.getWorlds()){
            if(w.getName().equalsIgnoreCase(wName)){
                return true;
            }
        }
        return false;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public double getCurrentIncomeTax() {
        return incomeTax;
    }

    public HashMap<String, Long> getBans() {
        return bans;
    }

    public void setChange(boolean b) {
        change = b;
    }

    public boolean isChange() {
        return change;
    }

    public boolean isChangeMembers() {
        return changeMembers;
    }

    public boolean isChangeBans() {
        return changeBans;
    }

    public boolean isChangeLand() {
        return changeLand;
    }

    public boolean isChangeRelations() {
        return changeRelations;
    }

    public void setChangeMembers(boolean changeMembers) {
        this.changeMembers = changeMembers;
    }

    public void setChangeBans(boolean changeBans) {
        this.changeBans = changeBans;
    }

    public void setChangeLand(boolean changeLand) {
        this.changeLand = changeLand;
    }

    public void setChangeRelations(boolean changeRelations) {
        this.changeRelations = changeRelations;
    }

    @Override
    public int compareTo(Kingdom that) {//negative means this goes first
        if(that == null) {
            return -1;
        }

        /*
         * Order:
         * Online
         * Members
         * Max land
         * Land
         * Treasury
         */

        if(this.getOnline() > that.getOnline()) {
            return that.getOnline() - this.getOnline();
        }else {
            if(this.getMaxLand() > that.getMaxLand()) {
                return that.getMaxLand() - this.getMaxLand();
            }else {
                if(this.getLandAmount() > that.getLandAmount()) {
                    return that.getLandAmount() - this.getLandAmount();
                }else {
                    return (int) ((that.getTreasury() - this.getTreasury())*100);
                }
            }
        }
    }

}