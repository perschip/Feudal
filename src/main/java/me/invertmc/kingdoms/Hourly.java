package me.invertmc.kingdoms;

import java.util.ArrayList;
import java.util.List;

import me.invertmc.Feudal;
import me.invertmc.user.User;
import me.invertmc.user.classes.Profession;
import me.invertmc.user.classes.XP;
import me.invertmc.utils.ErrorManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.IllegalPluginAccessException;

public class Hourly {

    public static void hourlyUpdateAsync(final List<Kingdom> kingdoms) {

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    int wait = 250;
                    if(kingdoms.size() > 0) {
                        wait = 3600000 / kingdoms.size();
                    }
                    if(wait > 250) {
                        wait = 250;
                    }

                    long inactive = (long) (Feudal.getConfiguration().getConfig().getDouble("kingdom.inactiveDeleteDays") * 86400000);
                    float landTax = (float) Feudal.getConfiguration().getConfig().getDouble("kingdom.land.tax");
                    int taxReputation = Feudal.getConfiguration().getConfig().getInt("reputation.failToPayLandTax");
                    String taxReputationMsg = Feudal.getMessage("reputation.failToPayLandTax");
                    String taxFailMsg = Feudal.getMessage("kingdom.tax.fail");
                    String taxRemove = Feudal.getMessage("kingdom.tax.remove");

                    for(Kingdom king : kingdoms) {

                        //DELETE INACTIVE KINGDOMS
                        if(inactive > 0){
                            if(king.getLastOnline() > 0 && System.currentTimeMillis() - king.getLastOnline() > inactive){
                                Bukkit.getScheduler().scheduleSyncDelayedTask(Feudal.getPlugin(), new Runnable() {

                                    @SuppressWarnings("deprecation")
                                    @Override
                                    public void run() {
                                        Feudal.getKingdoms().remove(king);
                                        Feudal.log("Deleting kingdom for [Inactive]: " + king.getName());
                                        king.delete("SERVER - (KINGDOM INACTIVE)");
                                        king.removeAllMembers();
                                    }

                                });
                                continue;
                            }
                        }

                        //Calc tax AND select land to remove
                        boolean failed = false;
                        ArrayList<Land> remove = new ArrayList<Land>();
                        ArrayList<Land> currentLand = new ArrayList<Land>(king.getLand());
                        double tax = 0;
                        for(Land l : king.getLand()){
                            if(tax + landTax > king.getTreasury()){
                                failed = true;
                                boolean rem = true;
                                for(Challenge c : Feudal.getChallenges(king)){//Makes so if in defence you cant lose land from taxes.
                                    if(c.getDefender().equals(king)){
                                        rem = false;
                                        break;
                                    }
                                }
                                if(rem){
                                    remove.add(l);
                                }
                            }else{
                                tax += (landTax*king.getLandTaxPercent());
                            }
                        }

                        List<String> messageMembers = new ArrayList<String>();

                        //Offline messages
                        for(Land l : remove){
                            if(!(currentLand.size() == 1 && Feudal.getChallenges(king).size() > 0)){//If in challenge, must maintain 1 chunk
                                messageMembers.add(taxFailMsg.replace("%x%", l.getX()+"").replace("%z%", l.getZ()+""));
                                currentLand.remove(l);
                            }
                        }
                        remove.removeAll(currentLand);

                        double treasury = king.getTreasury() - tax;
                        if(tax > 0){
                            king.messageMembers(((taxRemove.replace("%tax%", Feudal.round(tax)+"").replace("%money%", Feudal.round(treasury)+""))), false);
                        }

                        //Offline messages and reputation
                        User leader = offlineMessages(king, messageMembers, failed, taxReputation, taxReputationMsg);

                        finilizeKingdom(king, tax, remove, leader);

                        try {
                            Thread.sleep(wait);
                        }catch(Exception e) {
                            ErrorManager.error(959352018, e);
                        }
                    }
                }catch(Exception e) {
                    ErrorManager.error(1041352018, e);
                }
            }

        });
        thread.setName("FeudalHourlyUpdate");
        thread.start();

    }

    protected static User offlineMessages(Kingdom king, List<String> messageMembers, boolean failed, int taxReputation, String taxReputatioMessage) {
        //Select leader
        User leader = null;

        for(String uuid : king.getMembers().keySet()) {
            try {
                User user = Feudal.getUser(uuid);
                if(user != null) {
                    if(user.getPlayer() != null && user.getPlayer().isOnline()){
                        for(String msg : messageMembers) {
                            user.getPlayer().sendMessage(msg);
                        }
                    }else{
                        user.sendOfflineMessages(messageMembers);
                    }
                    if(failed) {
                        user.effectReputation(taxReputation, taxReputatioMessage);
                    }
                    user.save(true);

                    if(king.getMembers().get(uuid).equals(Rank.LEADER)) {
                        leader = user;
                    }
                }
            }catch(Exception e) {
                ErrorManager.error(1026352018, e);
            }
        }

        return leader;
    }


    //SYNC
    protected static void finilizeKingdom(final Kingdom king, final double tax, final ArrayList<Land> remove, final User leader) {
        try {
            Bukkit.getScheduler().scheduleSyncDelayedTask(Feudal.getPlugin(), new Runnable() {
                public void run() {
                    try {
                        //Set treasury
                        king.setTreasury(king.getTreasury() - tax);

                        //Remove land
                        for(Land land : remove) {
                            king.getLand().remove(land);
                            Land checkLand = Feudal.getLand(land);
                            if(checkLand != null){
                                Feudal.getLands().remove(checkLand);
                            }
                        }

                        king.updateLand();

                        //Update shield
                        king.updateShield();

                        //Check online
                        king.checkOnline();

                        //Leader XP
                        if(leader != null){
                            if(leader.getProfession() != null && !leader.getProfession().getType().equals(Profession.Type.NONE)){
                                int xp = 0;
                                if(leader.getProfession().getType().equals(Profession.Type.SQUIRE) || leader.getProfession().getType().equals(Profession.Type.KNIGHT)){
                                    if(king.getMembers().size() > 1){
                                        xp = Feudal.getConfiguration().getConfig().getInt(leader.getProfession().getType().getName() + ".xpPerKingdomMember") * (king.getMembers().size() - 1);
                                    }
                                }else if(leader.getProfession().getType().equals(Profession.Type.BARON) || leader.getProfession().getType().equals(Profession.Type.KING) || leader.getProfession().getType().equals(Profession.Type.ARCHBISHOP)){
                                    if(king.getMembers().size() > 1){
                                        xp = Feudal.getConfiguration().getConfig().getInt(leader.getProfession().getType().getName() + ".xpPerKingdomMember") * (king.getMembers().size() - 1);
                                    }
                                    if(king.getLandAmount() > 1){
                                        xp += (king.getLandAmount() - 1) * Feudal.getConfiguration().getConfig().getInt(leader.getProfession().getType().getName() + ".xpPerClaimedLand");
                                    }
                                }
                                if(xp > 0){
                                    XP.addXP(leader, xp);
                                }
                            }
                        }

                        //Save kingdom
                        try {
                            king.save();//Saves async if SQL
                        } catch (Exception e) {
                            ErrorManager.error(35, e);
                        }
                    }catch(IllegalPluginAccessException e) {
                        //Nothing
                    }catch(Exception e) {
                        ErrorManager.error(1040352018, e);
                    }
                }
            });
        }catch(IllegalPluginAccessException e) {
            //Nothing
        }
    }

}