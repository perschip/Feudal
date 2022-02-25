package me.invertmc.kingdoms;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import me.invertmc.Feudal;
import me.invertmc.user.User;
import me.invertmc.utils.ErrorManager;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class Challenge {
    private Kingdom attacker;
    private Kingdom defender;
    private Kingdom winner = null;
    private Location bannerLoc;
    private long start = 0;
    private float tax;
    private boolean accepted;
    private short attackerDeathsRemaining = 0;
    private short defenderDeathsRemaining = 0;
    private long attackerOnlineTime = 0;
    private long defenderOnlineTime = 0;
    private long joinTimeAttacker = -1;
    private long joinTimeDefender = -1;

    private boolean finish = false;

    private ArrayList<String> attackerReady = new ArrayList<String>();
    private ArrayList<String> defenderReady = new ArrayList<String>();
    private boolean fighting = false;
    private ArrayList<Land> connected;
    private short timerAttack = -1;
    private short timerDefence = -1;
    private short timerMinuteTax = -1;
    private short attackerTimeRemaining = 0;
    private short defenderTimeRemaining = 0;
    private long lastCountDefence = 0;
    private long lastCountAttack = 0;

    ArrayList<String> attackerDeath = new ArrayList<String>();
    ArrayList<String> defenderDeath = new ArrayList<String>();

    private boolean aTimer = false;
    private boolean dTimer = false;
    private boolean taxTimer = false;

    private Scoreboard board = null;
    private Objective obj = null;

    /**
     * Create a new challenge.  There is no real way to use this for API functions.
     * @param attacker
     * @param defender
     * @param bannerLocation
     * @param accepted
     * @param tax
     */
    public Challenge(Kingdom attacker, Kingdom defender, Location bannerLocation, boolean accepted, float tax){
        this.attacker = attacker;
        this.defender = defender;
        this.bannerLoc = bannerLocation;
        this.accepted = accepted;
        this.tax = tax;

        if(fightersOnline()){
            startTimerMinuteTax();
        }
    }

    /**
     * Load challenge from a config string. Parse the string to a Challenge.
     * @param encoded
     */
    public Challenge(String encoded){
        String split[] = encoded.split("/");//size 11
        attacker = Feudal.getKingdom(split[0]);
        defender = Feudal.getKingdom(split[1]);
        bannerLoc = new Location(Bukkit.getWorld(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4]), Integer.parseInt(split[5]));
        start = Long.parseLong(split[6]);
        tax = (float) Double.parseDouble(split[7]);
        accepted = Boolean.parseBoolean(split[8]);
        attackerDeathsRemaining = (short) Integer.parseInt(split[9]);
        defenderDeathsRemaining = (short) Integer.parseInt(split[10]);
        attackerOnlineTime = Long.parseLong(split[11]);
        defenderOnlineTime = Long.parseLong(split[12]);

        if(fightersOnline()){
            startTimerMinuteTax();
        }
    }

    /**
     * Convert Challenge to the encode string for the config.
     * @return
     */
    public String getSaveString(){
        return attacker.getUUID() + "/" + defender.getUUID() + "/" + bannerLoc.getWorld().getName() + "/" + bannerLoc.getBlockX() + "/" +
                bannerLoc.getBlockY() + "/" + bannerLoc.getBlockZ() + "/" + start + "/" + tax + "/" + accepted + "/" + attackerDeathsRemaining + "/" +
                defenderDeathsRemaining + "/" + attackerOnlineTime + "/" + defenderOnlineTime;
        //attacker/defender/bannerWorld/bannerX/bannerY/bannerZ/start/tax/accepted/attackerDeaths/defenderDeaths/attackerOnlineTime/defenderOnlineTime
    }

    /**
     * Run the Challenge hourly update.
     */
    public void updateHourly(){
        if(checkChallenge()){
            if(!accepted){
                tax += Feudal.getConfiguration().getConfig().getDouble("kingdom.challenges.taxAdd");
                if(defender.getTreasury() > tax){
                    defender.setTreasury(defender.getTreasury() - tax);
                    defender.messageMembers(Feudal.getMessage("c.tax.hour.tax").replace("%tax%", tax+"").replace("%attacker%", attacker.getName()), false);
                }else{
                    defender.setTreasury(0);
                    defender.messageMembers(Feudal.getMessage("c.tax.hour.tax").replace("%attacker%", attacker.getName()), true);
                    win(attacker, Feudal.getMessage("c.tax.hour.win").replace("%defender%", defender.getName()), false);
                }
                try {
                    defender.save();
                } catch (Exception e) {
                    ErrorManager.error(79, e);
                }
                for(String s : defender.getMembers().keySet()){
                    User u = Feudal.getUser(s);
                    if(u != null){
                        u.effectReputation(Feudal.getConfiguration().getConfig().getInt("reputation.ignoreChallengePerHour"), Feudal.getMessage("reputation.ignoreChallengePerHour"));
                    }
                }
            }else{
                checkDefaultWin();
            }
        }
    }

    private void startTimerMinuteTax() {
        if(!taxTimer){
            taxTimer = true;
            if(Feudal.getConfiguration().getConfig().getBoolean("kingdom.challenges.taxPerMinuteWhileFightersOnline")){
                timerMinuteTax = (short) Bukkit.getScheduler().scheduleSyncRepeatingTask(Feudal.getPlugin(), new Runnable(){

                    @Override
                    public void run() {
                        if(taxTimer == true){
                            if(fightersOnline()){
                                if(checkChallenge() && !accepted){
                                    boolean members = false;
                                    for(String s : defender.getMembers().keySet()){
                                        if(!defender.getMembers().get(s).equals(Rank.GUEST)){
                                            Player checkP = Bukkit.getPlayer(UUID.fromString(s));
                                            if(checkP != null && checkP.isOnline()){
                                                members = true;
                                                break;
                                            }
                                        }
                                    }
                                    if(members){
                                        double h = Feudal.getConfiguration().getConfig().getDouble("kingdom.challenges.taxAdd");
                                        tax += h;
                                        double newTax = (defender.getTreasury() * ((tax/h)/Feudal.getConfiguration().getConfig().getDouble("kingdom.challenges.minutesToWinWhileFightersOnlineBeforeAccepting"))) + tax;
                                        if(defender.getTreasury() > newTax){
                                            defender.setTreasury(defender.getTreasury() - newTax);
                                            defender.messageMembers(Feudal.getMessage("c.tax.minute").replace("%tax%", newTax+"").replace("%attacker%", attacker.getName()), false);
                                            defender.messageMembers(Feudal.getMessage("c.tax.command").replace("%attacker%", attacker.getName()), false);
                                            defender.messageMembers(Feudal.getMessage("c.tax.t").replace("%money%", defender.getTreasury()+""), false);
                                            try {
                                                defender.save();
                                            } catch (Exception e) {
                                                ErrorManager.error(79, e);
                                            }
                                        }else{
                                            defender.setTreasury(0);
                                            defender.messageMembers(Feudal.getMessage("c.tax.hour.fail").replace("%attacker%", attacker.getName()), true);
                                            win(attacker, Feudal.getMessage("c.tax.hour.win").replace("%defender%", defender.getName()), false);
                                            Bukkit.getScheduler().cancelTask(timerMinuteTax);
                                            taxTimer = false;
                                        }
                                    }
                                }

                            }else{
                                Bukkit.getScheduler().cancelTask(timerMinuteTax);
                                taxTimer = false;
                            }
                        }else{
                            if(timerMinuteTax != -1){
                                Bukkit.getScheduler().cancelTask(timerMinuteTax);
                            }
                        }
                    }

                }, 1200, 1200);
            }
        }
    }

    private boolean checkChallenge() {
        if(defender == null || attacker == null || defender.getMembers().size() == 0 || attacker.getMembers().size() == 0){
            if(Feudal.getChallenges().contains(this)){
                Feudal.getChallenges().remove(this);
            }
            return false;
        }else{
            return true;
        }
    }

    private void checkDefaultWin() {
        if(System.currentTimeMillis() - start >= (3600000 * Feudal.getConfiguration().getConfig().getInt("kingdom.challenges.lengthInHours"))){
            double percent = ( ((double)attackerOnlineTime) / ((double)(attackerOnlineTime+defenderOnlineTime)) ) * 100;
            double percentNeeded = Feudal.getConfiguration().getConfig().getDouble("kingdom.challenges.onlinePercentNeededForAttackersToWinByDefault");
            if(percent >= percentNeeded){
                win(attacker, "\u00a77" + attacker.getName() + " \u00a76was online \u00a77" + Feudal.round(percent) + "% \u00a76more than \u00a77" + defender.getName() + ".", true);
            }else{
                win(defender, "\u00a77" + attacker.getName() + " \u00a76was online \u00a77" + Feudal.round(percent) + "% \u00a76more than \u00a77" + defender.getName() + "\u00a76, but they needed \u00a77" + percentNeeded + "%", true);
            }
        }
    }

    private void checkForWin() {
        if(checkChallenge()){
            if(this.defenderDeathsRemaining <= 0){
                String reason = "\u00a77" + defender.getName() + Feudal.getMessage("landUpdate.diedTooMany");
                if(this.defenderDeathsRemaining == -1){
                    reason = "\u00a77" + defender.getName() + Feudal.getMessage("landUpdate.abandoned");
                }
                win(attacker, reason, false);
            }else if(this.attackerDeathsRemaining <= 0){
                String reason = "\u00a77" + attacker.getName() + Feudal.getMessage("landUpdate.diedTooMany");
                if(this.attackerDeathsRemaining == -1){
                    reason = "\u00a77" + attacker.getName() + Feudal.getMessage("landUpdate.abandoned");
                }
                win(defender, reason, false);
            }
        }
    }

    /**
     * Set challenge as accepted
     * @return
     */
    public int accept(){
        if(checkChallenge()){
            if(accepted){
                return 1;
            }else{
                accepted = true;
                attacker.messageMembers(Feudal.getMessage("c.a").replace("%defender%", defender.getName()), true);
                defender.effectReputation(Feudal.getConfiguration().getConfig().getInt("reputation.acceptChallenge"), Feudal.getMessage("reputation.acceptChallenge"));
                start = System.currentTimeMillis();
                if(fightersOnline() && !fighting){
                    startFighting();
                }
                Feudal.saveChallengesRough();
                return 0;
            }
        }else{
            return 1;
        }
    }

    private boolean fightersOnline() {
        if(attacker.getFightersOnline() > 0 && defender.getFightersOnline() > 0){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Have a challenge surrender with kingdom being the kingdom surrendering.
     * @param k
     * @return
     */
    public int surrender(Kingdom k){
        if(checkChallenge()){
            if(k.equals(attacker)){
                win(defender, "\u00a77" + attacker.getName() + Feudal.getMessage("landUpdate.surrender"), false);
                return 0;
            }else if(k.equals(defender)){
                win(attacker, "\u00a77" + defender.getName() + Feudal.getMessage("landUpdate.surrender"), false);
                return 0;
            }else{
                return -1;
            }
        }else{
            return -1;
        }
    }

    /**
     * Get a random location near the challenge to teleport to.
     * @return
     */
    public Location getTeleportLocation(){
        Random rand = new Random();
        double x = ((rand.nextInt(32))-16) + bannerLoc.getX();
        double z = ((rand.nextInt(32))-16) + bannerLoc.getZ();
        double y = bannerLoc.getWorld().getMaxHeight();
        Location loc = new Location(bannerLoc.getWorld(), x, y, z);
        while(loc.getBlock() == null || (loc.getBlock() != null && loc.getBlock().getType().equals(Material.AIR))){
            if(loc.getBlockY() <= 5){
                loc = loc.add(0, rand.nextInt(35)+40, 0);
                break;
            }
            loc = loc.add(0, -1, 0);
        }
        return loc.add(0, 1, 0);
    }

    /**
     * Start the fight if it is ready to start.
     */
    public void updateCheckFight(){
        if(!fighting && accepted && fightersOnline()){
            this.startFighting();
        }
    }

    public Kingdom getWinner(){
        return winner;
    }

    /**
     * Bring an end to the challenge.
     * @param king
     * @param reason
     * @param _default
     */
    public void win(Kingdom king, String reason, boolean _default) {
        winner = king;
        this.taxTimer = false;
        this.aTimer = false;
        this.dTimer = false;
        removeLeaderBoard();
        if(checkChallenge()){

            if(_default){
                if(defender.equals(king)){//Defender wins default
                    defender.messageMembers(Feudal.getMessage("c.win.defender.default.d").replace("%attacker%", attacker.getName()), true);
                    defender.messageMembers(reason, true);
                    attacker.messageMembers(Feudal.getMessage("c.win.defender.default.a").replace("%defender%", defender.getName()), true);
                    attacker.messageMembers(reason, true);
                    defender.effectReputation(Feudal.getConfiguration().getConfig().getInt("reputation.winDefault"), Feudal.getMessage("reputation.winDefault"));
                    attacker.effectReputation(Feudal.getConfiguration().getConfig().getInt("reputation.loseDefault"), Feudal.getMessage("reputation.loseDefault"));
                    defender.setChallengesWon(defender.getChallengesWon() + 1);
                    attacker.setChallengesLost(attacker.getChallengesLost() + 1);
                    defender.giveWinXP(false);

                    attacker.addBan(defender.getUUID(), System.currentTimeMillis() + (3600000 * Feudal.getConfiguration().getConfig().getInt("kingdom.challenges.attackerCounterAttackBanInHours")));
                    attacker.checkBans();

                    try{
                        defender.save();
                        attacker.save();
                    }catch(Exception e){
                        ErrorManager.error(80, e);
                    }
                    if(Feudal.getChallenges().contains(this)){
                        Feudal.getChallenges().remove(this);
                    }
                }else if(attacker.equals(king)){//Attacker wins default
                    double percent = Feudal.getConfiguration().getConfig().getDouble("kingdom.challenges.percentOfTreasuryWonByDefault") / 100;
                    double money = defender.getTreasury() * percent;
                    defender.setTreasury(defender.getTreasury() - money);
                    attacker.setTreasury(attacker.getTreasury() + money);
                    attacker.messageMembers(Feudal.getMessage("c.win.attacker.default.a").replace("%defender%", defender.getName()).replace("%money%", Feudal.round(money)+""), true);
                    attacker.messageMembers(reason, true);
                    defender.messageMembers(Feudal.getMessage("c.win.attacker.default.d").replace("%attacker%", attacker.getName()).replace("%money%", Feudal.round(money)+""), true);
                    defender.messageMembers(reason, true);
                    attacker.giveWinXP(false);

                    if(connected == null || connected.size() == 0){
                        this.connected = defender.getConnectedLand(new Land(bannerLoc));
                    }

                    //Lose land only if enabled
                    if(Feudal.getConfiguration().getConfig().getBoolean("kingdom.challenges.loseLand", true)){
                        for(Land l : connected){
                            if(defender.getLand().contains(l)){
                                defender.getLand().remove(l);
                            }
                            ArrayList<Land> remove = new ArrayList<Land>();
                            for(Land l2 : Feudal.getLands().keySet()){
                                if(l2.equals(l)){
                                    remove.add(l2);
                                }
                            }
                            for(Land l2 : remove){
                                Feudal.getLands().remove(l2);
                            }
                        }
                        defender.updateLand();
                    }

                    attacker.setChallengesWon(attacker.getChallengesWon() + 1);
                    defender.setChallengesLost(defender.getChallengesLost() + 1);

                    attacker.effectReputation(Feudal.getConfiguration().getConfig().getInt("reputation.winDefault"), Feudal.getMessage("reputation.winDefault"));
                    defender.effectReputation(Feudal.getConfiguration().getConfig().getInt("reputation.loseDefault"), Feudal.getMessage("reputation.loseDefault"));

                    if(Feudal.getConfiguration().getConfig().getBoolean("kingdom.challenges.winban")){
                        attacker.addBan(defender.getUUID(), System.currentTimeMillis() + (3600000 * Feudal.getConfiguration().getConfig().getInt("kingdom.challenges.attackerCounterAttackBanInHours")));
                    }

                    defender.addBan(attacker.getUUID(), System.currentTimeMillis() + (3600000 * Feudal.getConfiguration().getConfig().getInt("kingdom.challenges.defenderCounterAttackBanInHours")));
                    defender.checkBans();

                    try{
                        defender.save();
                        attacker.save();
                    }catch(Exception e){
                        ErrorManager.error(80, e);
                    }
                    if(Feudal.getChallenges().contains(this)){
                        Feudal.getChallenges().remove(this);
                    }
                }else{
                    defender.messageMembers(Feudal.getMessage("c.err.1"), false);
                    attacker.messageMembers(Feudal.getMessage("c.err.1"), false);
                    Bukkit.getConsoleSender().sendMessage("\u00a74[FEUDAL] ERROR : Unable to calculate the winner for a challenge. Challenge: " + attacker.getName() + " vs " + defender.getName());
                }
            }else{
                if(defender.equals(king)){//Defender wins kills / time

                    if(defender.getOnline() > 0){
                        ArrayList<ItemStack> items = new ArrayList<ItemStack>();
                        for(String id : this.attackerDeath){
                            User u = Feudal.getUser(id);
                            if(u != null && u.getPlayer() != null && u.getPlayer().isOnline()){
                                for(ItemStack item : u.getPlayer().getInventory().getContents()){
                                    items.add(item);
                                }
                                u.getPlayer().getInventory().clear();
                            }
                        }

                        while(items.size() > 0 && defender.getOnline() > 0){
                            for(String id : defender.getMembers().keySet()){
                                User u = Feudal.getUser(id);
                                if(u != null && u.getPlayer() != null && u.getPlayer().isOnline()){
                                    if(items.size() > 0){
                                        if(items.get(0) != null){
                                            u.getPlayer().getWorld().dropItem(u.getPlayer().getLocation(), items.get(0));
                                        }
                                        items.remove(0);
                                    }
                                }
                            }
                        }
                    }

                    Bukkit.broadcastMessage(Feudal.getMessage("c.b.d").replace("%defender%", defender.getName()).replace("%attacker%", attacker.getName()));

                    Location loc = null;
                    if(attacker.getLand().size() > 0){
                        if(attacker.getHome() != null){
                            loc = attacker.getHome();
                        }
                        Random rand = new Random();
                        int land = rand.nextInt(attacker.getLand().size());
                        Land l = attacker.getLand().get(land);
                        Chunk c = l.getWorld().getChunkAt(l.getX(), l.getZ());
                        loc = c.getBlock(rand.nextInt(15)+1, 0, rand.nextInt(15)+1).getLocation();
                    }

                    attacker.messageMembers(Feudal.getMessage("c.win.defender.d").replace("%defender%", defender.getName()), true);
                    attacker.messageMembers(reason, true);
                    if(loc != null)
                        defender.messageMembers(Feudal.getMessage("c.win.defender.a").replace("%attacker%", attacker.getName()).replace("%loc%", "World=" + loc.getWorld().getName() + ", X=" + loc.getBlockX() + ", Z=" + loc.getBlockZ()), true);
                    defender.messageMembers(reason, true);

                    defender.setChallengesWon(defender.getChallengesWon() + 1);
                    attacker.setChallengesLost(attacker.getChallengesLost() + 1);
                    defender.effectReputation(Feudal.getConfiguration().getConfig().getInt("reputation.winChallenge"), Feudal.getMessage("reputation.win"));
                    attacker.effectReputation(Feudal.getConfiguration().getConfig().getInt("reputation.loseChallenge"), Feudal.getMessage("reputation.lose"));
                    defender.giveWinXP(true);

                    attacker.addBan(defender.getUUID(), System.currentTimeMillis() + (3600000 * Feudal.getConfiguration().getConfig().getInt("kingdom.challenges.attackerCounterAttackBanInHours")));
                    attacker.checkBans();

                    try{
                        defender.save();
                        attacker.save();
                    }catch(Exception e){
                        ErrorManager.error(80, e);
                    }
                    if(Feudal.getChallenges().contains(this)){
                        Feudal.getChallenges().remove(this);
                    }
                }else if(attacker.equals(king)){//Attackers win kills / time

                    if(attacker.getOnline() > 0){
                        ArrayList<ItemStack> items = new ArrayList<ItemStack>();
                        for(String id : this.defenderDeath){
                            User u = Feudal.getUser(id);
                            if(u != null && u.getPlayer() != null && u.getPlayer().isOnline()){
                                for(ItemStack item : u.getPlayer().getInventory().getContents()){
                                    items.add(item);
                                }
                                u.getPlayer().getInventory().clear();
                            }
                        }

                        while(items.size() > 0 && attacker.getOnline() > 0){
                            for(String id : attacker.getMembers().keySet()){
                                User u = Feudal.getUser(id);
                                if(u != null && u.getPlayer() != null && u.getPlayer().isOnline()){
                                    if(items.size() > 0){
                                        if(items.get(0) != null){
                                            u.getPlayer().getWorld().dropItem(u.getPlayer().getLocation(), items.get(0));
                                        }
                                        items.remove(0);
                                    }
                                }
                            }
                        }
                    }

                    Bukkit.broadcastMessage(Feudal.getMessage("c.b.a").replace("%defender%", defender.getName()).replace("%attacker%", attacker.getName()));

                    double percent = Feudal.getConfiguration().getConfig().getDouble("kingdom.challenges.percentOfTreasuryWon") / 100;
                    double money = defender.getTreasury() * percent;

                    defender.setTreasury(defender.getTreasury() - money);
                    attacker.setTreasury(attacker.getTreasury() + money);
                    attacker.messageMembers(Feudal.getMessage("c.win.attacker.a").replace("%defender%", defender.getName()).replace("%money%", Feudal.round(money)+""), true);
                    attacker.messageMembers(reason, true);
                    defender.messageMembers(Feudal.getMessage("c.win.attacker.d").replace("%attacker%", attacker.getName()).replace("%money%", Feudal.round(money)+""), true);
                    defender.messageMembers(reason, true);

                    attacker.setChallengesWon(attacker.getChallengesWon() + 1);
                    defender.setChallengesLost(defender.getChallengesLost() + 1);
                    attacker.effectReputation(Feudal.getConfiguration().getConfig().getInt("reputation.winChallenge"), Feudal.getMessage("reputation.win"));
                    defender.effectReputation(Feudal.getConfiguration().getConfig().getInt("reputation.loseChallenge"), Feudal.getMessage("reputation.lose"));
                    attacker.giveWinXP(true);

                    if(connected == null || connected.size() == 0){
                        this.connected = defender.getConnectedLand(new Land(bannerLoc));
                    }
                    long requiredClaimAge = (long) (Feudal.getConfiguration().getConfig().getDouble("kingdom.challenges.claimAgeRequiredHours") * 3600000);

                    if(Feudal.getConfiguration().getConfig().getBoolean("kingdom.challenges.loseLand", true)){
                        //Change land ownership.
                        for(Land l : connected){
                            if(defender.getLand().contains(l)){
                                defender.getLand().remove(l);
                            }
                            ArrayList<Land> remove = new ArrayList<Land>();
                            for(Land l2 : Feudal.getLands().keySet()){
                                if(l2.equals(l)){
                                    remove.add(l2);
                                }
                            }
                            for(Land l2 : remove){
                                Feudal.getLands().remove(l2);
                            }

                            //Setting for winning land
                            if(Feudal.getConfiguration().getConfig().getBoolean("kingdom.challenges.winland")){
                                //If kingdom is brand new, land will not be taken. So kingdoms can make fake kingdoms to expand.
                                if(System.currentTimeMillis() >= requiredClaimAge + defender.getFirstClaim() && !defender.hasSpecialShield()){
                                    Feudal.getLands().put(l, attacker);
                                    attacker.addLand(l);
                                }
                            }
                        }
                        defender.updateLand();
                        attacker.updateLand();
                    }

                    if(Feudal.getConfiguration().getConfig().getBoolean("kingdom.challenges.winban")){
                        attacker.addBan(defender.getUUID(), System.currentTimeMillis() + (3600000 * Feudal.getConfiguration().getConfig().getInt("kingdom.challenges.attackerCounterAttackBanInHours")));
                    }

                    defender.addBan(attacker.getUUID(), System.currentTimeMillis() + (3600000 * Feudal.getConfiguration().getConfig().getInt("kingdom.challenges.defenderCounterAttackBanInHours")));
                    defender.checkBans();

                    try{
                        defender.save();
                        attacker.save();
                    }catch(Exception e){
                        ErrorManager.error(80, e);
                    }
                    if(Feudal.getChallenges().contains(this)){
                        Feudal.getChallenges().remove(this);
                    }
                }else{
                    defender.messageMembers(Feudal.getMessage("c.err.1"), false);
                    attacker.messageMembers(Feudal.getMessage("c.err.1"), false);
                    Bukkit.getConsoleSender().sendMessage("\u00a74[FEUDAL] ERROR : Unable to calculate the winner for a challenge. Challenge: " + attacker.getName() + " vs " + defender.getName());
                }
            }
            attacker.checkFight();
            defender.checkFight();
        }

        finish = true;
        Feudal.saveChallengesRough();
    }

    private void startFighting(){
        if(attacker.isInFight()){
            defender.messageMembers(Feudal.getMessage("c.infight.d").replace("%attacker%", attacker.getName()), false);
        }else if(defender.isInFight()){
            attacker.messageMembers(Feudal.getMessage("c.infight.a").replace("%defender%", defender.getName()), false);
        }else{
            this.connected = defender.getConnectedLand(new Land(bannerLoc));

            fighting = true;
            attacker.messageMembers(Feudal.getMessage("c.s.sep"), false);
            attacker.messageMembers(Feudal.getMessage("c.s.a1").replace("%defender%", defender.getName()), false);
            attacker.messageMembers(Feudal.getMessage("c.s.a2").replace("%defender%", defender.getName()), false);
            attacker.messageMembers(Feudal.getMessage("c.s.a3").replace("%time%", getFancyTime(Feudal.getConfiguration().getConfig().getInt("kingdom.challenges.secondsToBeginAttack"))), false);
            attacker.messageMembers(Feudal.getMessage("c.s.sep"), false);

            defender.messageMembers(Feudal.getMessage("c.s.sep"), false);
            defender.messageMembers(Feudal.getMessage("c.s.d1").replace("%attacker%", attacker.getName()), false);
            defender.messageMembers(Feudal.getMessage("c.s.d2").replace("%attacker%", attacker.getName()), false);
            defender.messageMembers(Feudal.getMessage("c.s.d3").replace("%attacker%", attacker.getName()).replace("%time%", getFancyTime(Feudal.getConfiguration().getConfig().getInt("kingdom.challenges.secondsToBeginAttack"))), false);
            defender.messageMembers(Feudal.getMessage("c.s.sep"), false);

            this.attackerDeathsRemaining = (short) attacker.getMembers().size();
            this.defenderDeathsRemaining = (short) defender.getMembers().size();

            int sepration = Feudal.getConfiguration().getConfig().getInt("kingdom.challenges.fighterSeparation");
            if(this.attackerDeathsRemaining > this.defenderDeathsRemaining){
                if(this.attackerDeathsRemaining - this.defenderDeathsRemaining > sepration){
                    this.defenderDeathsRemaining = (short) (this.attackerDeathsRemaining - sepration);
                }
            }else if(this.attackerDeathsRemaining < this.defenderDeathsRemaining){
                if(this.defenderDeathsRemaining - this.attackerDeathsRemaining > sepration){
                    this.attackerDeathsRemaining = (short) (this.defenderDeathsRemaining - sepration);
                }
            }

            attackerTimeRemaining = (short) Feudal.getConfiguration().getConfig().getInt("kingdom.challenges.secondsToBeginAttack");
            startAttackTimer();
            defenderTimeRemaining = (short) (Feudal.getConfiguration().getConfig().getInt("kingdom.challenges.secondsToDefendPerChunk") * this.connected.size());

            for(String m : attacker.getMembers().keySet()){
                User u = Feudal.getUser(m);
                if(u != null && u.getPlayer() != null && u.getPlayer().isOnline()){
                    this.checkUser(u, true, u.getPlayer().getLocation());
                }
            }
            for(String m : defender.getMembers().keySet()){
                User u = Feudal.getUser(m);
                if(u != null && u.getPlayer() != null && u.getPlayer().isOnline()){
                    this.checkUser(u, false, u.getPlayer().getLocation());
                }
            }

            updateLeaderBoard();
        }
    }

    private void startAttackTimer() {
        if(this.timerAttack != -1){
            Bukkit.getScheduler().cancelTask(timerAttack);
            aTimer = false;
        }
        aTimer = true;
        timerAttack = (short) Bukkit.getScheduler().scheduleSyncRepeatingTask(Feudal.getPlugin(), new Runnable(){
            @Override
            public void run() {
                if(aTimer == true){
                    if(System.currentTimeMillis() - lastCountAttack > 900){
                        lastCountAttack = System.currentTimeMillis();
                        updateLeaderBoard();
                        attackerTimeRemaining--;
                        if(attackerTimeRemaining <= 0){
                            Challenge.this.attackerDeathsRemaining = -1;
                            checkForWin();
                            Bukkit.getScheduler().cancelTask(timerAttack);
                            aTimer = false;
                        }else{
                            if(attackerTimeRemaining <= 10 || attackerTimeRemaining % 60 == 0){
                                attacker.messageMembers(Feudal.getMessage("c.timer.attack").replace("%time%", getFancyTime(attackerTimeRemaining)).replace("%defender%", defender.getName()), false);
                            }
                        }
                    }
                }else{
                    if(timerAttack != -1)
                        Bukkit.getScheduler().cancelTask(timerAttack);
                }
            }

        }, 20, 20);
    }

    private void startDefenceTimer() {
        if(this.timerDefence != -1){
            Bukkit.getScheduler().cancelTask(timerDefence);
            dTimer = false;
        }
        dTimer = true;
        timerDefence = (short) Bukkit.getScheduler().scheduleSyncRepeatingTask(Feudal.getPlugin(), new Runnable(){
            @Override
            public void run() {
                if(dTimer){
                    if(System.currentTimeMillis() - lastCountDefence > 900){
                        lastCountDefence = System.currentTimeMillis();
                        updateLeaderBoard();
                        defenderTimeRemaining--;
                        if(defenderTimeRemaining <= 0){
                            Challenge.this.defenderDeathsRemaining = -1;
                            checkForWin();
                            Bukkit.getScheduler().cancelTask(timerDefence);
                            dTimer = false;
                        }else{
                            if(defenderTimeRemaining <= 10 || defenderTimeRemaining % 60 == 0){
                                defender.messageMembers(Feudal.getMessage("c.timer.defence").replace("%time%", getFancyTime(defenderTimeRemaining)).replace("%attacker%", attacker.getName()), false);
                            }
                        }
                    }
                }else{
                    if(timerDefence != -1){
                        Bukkit.getScheduler().cancelTask(timerDefence);
                    }
                }
            }

        }, 20, 20);
    }

    /**
     * Get defneding kingdom
     * @return
     */
    public Kingdom getDefender() {
        return defender;
    }

    /**
     * Get attacking kingdom
     * @return
     */
    public Kingdom getAttacker() {
        return attacker;
    }

    /**
     * Check if challenge is accepted
     * @return
     */
    public boolean isAccepted() {
        return accepted;
    }

    /**
     * Get the location of the banner in raw-string form
     * @return
     */
    public String getBannerLocationStr() {
        return bannerLoc.getBlockX() + ", " + bannerLoc.getBlockY() + ", " + bannerLoc.getBlockZ();
    }

    /**
     * Get how much longer the challenge has in ms.  This will only work if the challenge has been accepted.
     * @return
     */
    private long getTimeRemaining(){
        long end = start + (3600000 * Feudal.getConfiguration().getConfig().getInt("kingdom.challenges.lengthInHours"));
        if(end - System.currentTimeMillis() < 0){
            return -1;
        }
        return (end - System.currentTimeMillis());
    }

    /**
     * Get how much longer a challenge has (if already accepted) in a nice string format.
     * @return
     */
    public String getTimeRemainingStr() {
        long rem = getTimeRemaining();
        if(rem == -1){
            return "Less than an hour.";
        }else{
            long days = rem / 86400000;
            long hours = ((rem - (days * 86400000)) / 3600000);
            long minutes = (rem - (days * 86400000) - (hours * 3600000)) / 60000;
            long seconds = (rem - (days * 86400000) - (hours * 3600000) - (minutes * 60000)) / 1000;
            return days + (days == 1 ? "day" : "days") + " " + hours + ":" + minutes + ":" + seconds;
        }
    }

    /**
     * Update challenge when a fighter joins
     * @param attacker
     */
    public void fighterJoin(boolean attacker) {
        if(attacker){
            if(this.joinTimeAttacker == -1){
                this.joinTimeAttacker = System.currentTimeMillis();
            }
        }else{
            if(this.joinTimeDefender == -1){
                this.joinTimeDefender = System.currentTimeMillis();
            }
        }
        if(fightersOnline() && !fighting){
            if(this.isAccepted()){
                startFighting();
            }else{
                startTimerMinuteTax();
            }
        }
    }

    /**
     * Update challenge when fighter leaves.
     * @param attacker
     */
    public void fighterLeave(boolean attacker) {
        if(attacker){
            if(this.joinTimeAttacker != -1){
                this.attackerOnlineTime += System.currentTimeMillis() - this.joinTimeAttacker;
                this.joinTimeAttacker = -1;
            }
        }else{
            if(this.joinTimeDefender != -1){
                this.defenderOnlineTime += System.currentTimeMillis() - this.joinTimeDefender;
                this.joinTimeDefender = -1;
            }
        }
    }

    /**
     * Check if challenge is in fighting stage
     * @return
     */
    public boolean isFighting() {
        return fighting;
    }

    /**
     * Check if a user is in the fight and update challenge and leaderboard accordingly.
     * @param u
     * @param attacker2
     * @param to
     */
    public void checkUser(User u, boolean attacker2, Location to) {
        if(finish){
            return;
        }
        if(u.getPlayer() != null && u.getPlayer().isOnline()){
            boolean found = false;
            if(u.getPlayer().getLocation().getWorld().equals(bannerLoc.getWorld())){
                if(to != null){
                    Land l = new Land(to);
                    Land up = new Land(l.getX() + 1, l.getZ(), l.getWorld(), l.getWorldName());
                    Land down = new Land(l.getX() - 1, l.getZ(), l.getWorld(), l.getWorldName());
                    Land left = new Land(l.getX(), l.getZ() -1, l.getWorld(), l.getWorldName());
                    Land right = new Land(l.getX(), l.getZ() +1, l.getWorld(), l.getWorldName());
                    Land[] land = new Land[]{l, up, down, left, right};

                    for(Land la : land){
                        if(connected.contains(la)){
                            found = true;
                        }
                    }
                }
            }
            if(attacker2){
                if(!found && this.attackerReady.size() > 0){
                    if(this.attackerReady.contains(u.getUUID())){
                        this.attackerReady.remove(u.getUUID());
                    }
                    if(this.attackerReady.size() == 0){
                        attacker.messageMembers(Feudal.getMessage("c.attacker.l.a").replace("%defender%", defender.getName()).replace("%time%", getFancyTime(this.attackerTimeRemaining)), false);
                        defender.messageMembers(Feudal.getMessage("c.attacker.l.d").replace("%attacker%", attacker.getName()).replace("%time%", getFancyTime(this.attackerTimeRemaining)), false);
                        startAttackTimer();
                        Bukkit.getScheduler().cancelTask(this.timerDefence);
                        dTimer = false;
                    }
                }else if(found){
                    if(this.attackerReady.size() == 0){
                        attacker.messageMembers(Feudal.getMessage("c.attacker.e.a"), false);
                        defender.messageMembers(Feudal.getMessage("c.attacker.e.d").replace("%attacker%", attacker.getName()), false);
                        if(timerAttack != -1){
                            Bukkit.getScheduler().cancelTask(this.timerAttack);
                            aTimer = false;
                        }
                    }
                    if(!this.attackerReady.contains(u.getUUID())){
                        this.attackerReady.add(u.getUUID());
                    }
                    if(this.defenderReady.size() == 0){
                        this.startDefenceTimer();
                    }
                }
            }else{
                if(!found && this.defenderReady.size() > 0){
                    if(defenderReady.contains(u.getUUID())){
                        defenderReady.remove(u.getUUID());
                    }
                    if(this.defenderReady.size() == 0 && this.attackerReady.size() != 0){
                        this.startDefenceTimer();
                        attacker.messageMembers(Feudal.getMessage("c.defender.l.a").replace("%defender%", defender.getName()).replace("%time%", getFancyTime(this.defenderTimeRemaining)), false);
                        defender.messageMembers(Feudal.getMessage("c.defender.l.d").replace("%attacker%", attacker.getName()).replace("%time%", getFancyTime(this.defenderTimeRemaining)), false);
                    }
                }else if(found){
                    if(this.defenderReady.size() == 0){
                        defender.messageMembers(Feudal.getMessage("c.defender.e.a"), false);
                        attacker.messageMembers(Feudal.getMessage("c.defender.e.d").replace("%defender%", defender.getName()), false);
                    }
                    if(!defenderReady.contains(u.getUUID())){
                        defenderReady.add(u.getUUID());
                    }
                    Bukkit.getScheduler().cancelTask(this.timerDefence);
                    dTimer=false;
                }
            }
            updateLeaderBoard();
        }
    }

    /**
     * Get time for chat.
     * @param sec
     * @return
     */
    private String getFancyTime(int sec) {
        int minutes = sec / 60;
        int seconds = sec - (minutes*60);
        return minutes + "m " + seconds + "s";
    }

    /**
     * Get time for leaderboard.
     * @param sec
     * @return
     */
    private String getFancyTime2(int sec) {
        int minutes = sec / 60;
        int seconds = sec - (minutes*60);
        return minutes + ":" + seconds;
    }

    /**
     * Set a user as dead according to the challenge and check for user and for win.
     * @param u
     * @param attacker
     */
    public void death(User u, boolean attacker) {
        updateLeaderBoard();
        attackerTimeRemaining = (short) Feudal.getConfiguration().getConfig().getInt("kingdom.challenges.secondsToBeginAttack");
        if(attacker){
            this.attackerDeathsRemaining--;
        }else{
            this.defenderDeathsRemaining--;
        }
        this.checkForWin();
        checkUser(u, attacker, null);
    }

    /**
     * Get the Location of the banner.
     * @return
     */
    public Location getBannerLocation() {
        return bannerLoc;
    }

    /**
     * Check if fighters are online to fight.
     */
    public void checkFighters() {
        if(fightersOnline() && !fighting){
            if(this.isAccepted()){
                startFighting();
            }
        }
    }

    private void updateLeaderBoard(){
        if(!Feudal.getConfiguration().getConfig().getBoolean("kingdom.challenges.enableScoreboards")){
            return;
        }
        if(board == null || obj == null){
            board = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
            obj = board.registerNewObjective("FChallenge", "dummy");
            String ch = Feudal.getMessage("leader.ch");
            obj.setDisplayName((ch.length() > 17 ? "\u00a7b\u00a7lChallenge" : ch));
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        }

        if(board != null && obj != null){
            String objName = "";
            try{
                objName = obj.getName();
            }catch(Exception e){
                return;
            }

            String att = Feudal.getMessage("leader.attacker");
            String def = Feudal.getMessage("leader.defender");
            if(att.length() > 17){
                att = "\u00a76Attacker";
            }
            if(def.length() > 17){
                def = "\u00a76Defender";
            }
            if(attacker.getName().length() <= 14){
                att = "\u00a76" + attacker.getName();
            }else if(attacker.getName().length() <= 16){
                att = attacker.getName();
            }
            if(defender.getName().length() <= 14){
                def = "\u00a76" + defender.getName();
            }else if(defender.getName().length() <= 16){
                def = defender.getName();
            }
            String inFight = Feudal.getMessage("leader.inFight");

            String aTime = (inFight.length() > 17 ? "\u00a7aIn Fight" : inFight);
            String dTime = (inFight.length() > 17 ? "\u00a7aIn Fight" : inFight);
            if(aTimer){
                aTime = "\u00a7c" + this.getFancyTime2(this.attackerTimeRemaining);
            }
            if(dTimer){
                dTime = "\u00a7c" + this.getFancyTime2(this.defenderTimeRemaining);
            }

            board = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
            obj = board.registerNewObjective("FChallenge", "dummy");
            String ch = Feudal.getMessage("leader.ch");
            obj.setDisplayName((ch.length() > 17 ? "\u00a7b\u00a7lChallenge" : ch));
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);

            obj.getScore(att).setScore(7);
            obj.getScore(Feudal.getMessage("landUpdate.lives") + this.attackerDeathsRemaining + "\u00a7a").setScore(6);
            obj.getScore(aTime + "\u00a7a").setScore(5);

            obj.getScore("\u00a75").setScore(4);

            obj.getScore(def).setScore(3);
            obj.getScore(Feudal.getMessage("landUpdate.lives") + this.defenderDeathsRemaining + "\u00a7b").setScore(2);
            obj.getScore(dTime + "\u00a7b").setScore(1);

            for(String s : attacker.getMembers().keySet()){
                Player p = Bukkit.getPlayer(UUID.fromString(s));
                if(p != null && p.isOnline()){
                    if(Feudal.getConfiguration().getConfig().getBoolean("kingdom.challenges.forceScoreboards") || isSidebarObjective(objName, p)){
                        p.setScoreboard(board);
                    }
                }
            }
            for(String s : defender.getMembers().keySet()){
                Player p = Bukkit.getPlayer(UUID.fromString(s));
                if(p != null && p.isOnline()){
                    if(Feudal.getConfiguration().getConfig().getBoolean("kingdom.challenges.forceScoreboards") || isSidebarObjective(objName, p)){
                        p.setScoreboard(board);
                    }
                }
            }
        }
    }

    public boolean isSidebarObjective(String objName, Player player) {
        if(player.getScoreboard() != null && player.getScoreboard().getObjective(DisplaySlot.SIDEBAR) != null && player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getName() != null) {
            return player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getName().equals(objName);
        }
        return false;
    }

    /**
     * Remove the leaderboard for the attackers and defenders
     */
    public void removeLeaderBoard(){
        for(String s : attacker.getMembers().keySet()){
            Player p = Bukkit.getPlayer(UUID.fromString(s));
            if(p != null && p.isOnline() && p.getScoreboard() != null){
                removeLeaderBoard(p);
            }
        }
        for(String s : defender.getMembers().keySet()){
            Player p = Bukkit.getPlayer(UUID.fromString(s));
            if(p != null && p.isOnline() && p.getScoreboard() != null){
                removeLeaderBoard(p);
            }
        }
    }

    /**
     * Remove leaderboard from a player.
     * @param p
     */
    public void removeLeaderBoard(Player p){
        try{
            Scoreboard s = p.getScoreboard();
            if(obj != null && s.getObjective(obj.getName()) != null){
                s.clearSlot(DisplaySlot.SIDEBAR);
                p.setScoreboard(s);
            }else{
                if(p.getScoreboard() == board){
                    s.clearSlot(DisplaySlot.SIDEBAR);
                    p.setScoreboard(s);
                }
            }
        }catch(Exception e){
            return;
        }
    }

    /**
     * Remove leaderboard from a player if no longer have information for the object saved for the leaderboard.
     * @param player
     */
    public static void resetLeaderBoard(Player player) {
        boolean remove = false;
        String ch = Feudal.getMessage("leader.ch");
        if(ch.length() > 17){
            ch = "\u00a7b\u00a7lChallenge";
        }
        for(Objective o : player.getScoreboard().getObjectives()){
            if(o.getDisplayName() != null && o.getDisplayName().equals("\u00a7b\u00a7lChallenge")){
                remove = true;
            }
        }
        if(remove){
            Scoreboard s = player.getScoreboard();
            s.clearSlot(DisplaySlot.SIDEBAR);
            player.setScoreboard(s);
        }
    }

    /**
     * Check if a player killed someone they were in that kingdom with and remove feudal xp.
     * @param u
     * @param killer
     * @param uIsAttacker
     */
    public void killCheck(User u, User killer, boolean uIsAttacker) {//Lose xp if you kill an enemy kingdom member when you were apart of that kingdom once.
        if((!uIsAttacker && attacker.isMember(killer.getUUID()))){
            for(KingdomLog l : killer.getPastKingdoms()){
                if(l.getKingdomUUID().equals(defender.getUUID())){
                    killer.effectReputation(Feudal.getConfiguration().getConfig().getInt("reputation.attackPrevKingdom"), Feudal.getMessage("reputation.attackPreKingdom"));
                }
            }
        }
    }

    /**
     * Check if enough members are on to start the fight if the fighers are not on.
     * @param equals
     */
    public void memberJoin(boolean equals) {
        if(accepted && !fighting){
            double at = attacker.getPercentMembersOnline();
            if(at >= Feudal.getConfiguration().getConfig().getDouble("kingdom.challenges.percentAttackMembersNeededToFight")){
                double de = defender.getPercentMembersOnline();
                if(de >= Feudal.getConfiguration().getConfig().getDouble("kingdom.challenges.percentDefenderMembersNeededToFight")){
                    startFighting();
                }
            }
        }
    }

    /**
     * Add an attacker death to the challenge count.
     * @param player
     */
    public void addAttackerDeath(Player player) {
        String u = player.getUniqueId().toString();
        if(!attackerDeath.contains(u)){
            attackerDeath.add(u);
        }
    }

    /**
     * Add a defender death to the challenge count.
     * @param player
     */
    public void addDefenderDeath(Player player) {
        String u = player.getUniqueId().toString();
        if(!defenderDeath.contains(u)){
            defenderDeath.add(u);
        }
    }
}
