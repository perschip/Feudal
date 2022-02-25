package me.invertmc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import me.invertmc.market.Market;
import me.invertmc.user.User;
import me.invertmc.utils.ErrorManager;
import org.bukkit.Bukkit;


public class ScheduledTasks {
    private static ArrayList<String> updated = new ArrayList<String>();

    public static void updateUserOnlineTime(User u){
        byte hourUTC = (byte) Calendar.getInstance(TimeZone.getTimeZone("GMT")).get(Calendar.HOUR_OF_DAY);
        u.getOnlineTime().setHour(hourUTC, u.getOnlineTime().getHour(hourUTC)+1);
        updated.add(u.getUUID());
    }

    public static ArrayList<String> getUpdated(){
        return updated;
    }

    protected static void startScheduler(){
        startStaminaTimer();
        long wait = (72000 - ((System.currentTimeMillis() - Feudal.getProfessionData().getConfig().getLong("preSchedule")) / 50));
        if(wait < 0){
            wait = 0;
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(Feudal.getPlugin(), new Runnable(){

            @Override
            public void run() {
                try{

                    Feudal.getProfessionData().getConfig().set("preSchedule", System.currentTimeMillis());
                    try {
                        Feudal.getProfessionData().save();
                    } catch (Exception e1) {
                        ErrorManager.error(76, e1);
                    }
                    byte hourUTC = (byte) Calendar.getInstance(TimeZone.getTimeZone("GMT")).get(Calendar.HOUR_OF_DAY);

                    Market.updateSAD();

                    for(String uuid : Feudal.getOnlinePlayers().keySet()){
                        if(!updated.contains(uuid)){
                            User u = Feudal.getOnlinePlayers().get(uuid);
                            u.getOnlineTime().setHour(hourUTC, u.getOnlineTime().getHour(hourUTC)+1);
                        }
                    }
                    updated = new ArrayList<String>();

                    Hourly.hourlyUpdateAsync(Feudal.getKingdoms());

				/*final long inactive = (long) (Feudal.getConfiguration().getConfig().getDouble("kingdom.inactiveDeleteDays") * 86400000);
				final float landTax = (float) Feudal.getConfiguration().getConfig().getDouble("kingdom.land.tax");
				if(landTax > 0){
					int ksize = Feudal.getKingdoms().size();
					if(ksize < 0){
						ksize = 1;
					}
					int sleeper = 2400 / 1;
					if(sleeper > 10){
						sleeper = 10;
					}
					int next = 0;
					ArrayList<Kingdom> loopKing = new ArrayList<Kingdom>();
					for(Kingdom king : Feudal.getKingdoms()){
						loopKing.add(king);
					}
					final int sleep = sleeper;
					for(final Kingdom king : loopKing){
						next += sleep;
						Bukkit.getScheduler().scheduleSyncDelayedTask(Feudal.getPlugin(), new Runnable(){

							@Override
							public void run() {
								boolean failed = false;
								ArrayList<Land> remove = new ArrayList<Land>();
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
								for(Land l : remove){
									if(!(king.getLand().size() == 1 && Feudal.getChallenges(king).size() > 0)){//If in challenge, must maintain 1 chunk
										king.messageMembers(Feudal.getMessage("kingdom.tax.fail").replace("%x%", l.getX()+"").replace("%z%", l.getZ()+""), true);
										king.getLand().remove(l);
										Land checkLand = Feudal.getLand(l);
										if(checkLand != null){
											Feudal.getLands().remove(checkLand);
										}
									}
								}
								king.checkOnline();
								king.setTreasury(king.getTreasury() - tax);
								if(tax > 0){
									king.messageMembers(Feudal.getMessage("kingdom.tax.remove").replace("%tax%", Feudal.round(tax)+"").replace("%money%", Feudal.round(king.getTreasury())+""), false);
								}
								if(failed){
									king.effectReputation(Feudal.getConfiguration().getConfig().getInt("reputation.failToPayLandTax"), Feudal.getMessage("reputation.failToPayLandTax"));
								}

								User leader = null;
								for(String s : king.getMembers().keySet()){
									if(king.getMembers().get(s).equals(Rank.LEADER)){
										leader = Feudal.getUser(s);
									}
								}
								if(leader != null){
									if(leader.getProfession() != null && !leader.getProfession().getType().equals(Type.NONE)){
										int xp = 0;
										if(leader.getProfession().getType().equals(Type.SQUIRE) || leader.getProfession().getType().equals(Type.KNIGHT)){
											if(king.getMembers().size() > 1){
												xp = Feudal.getConfiguration().getConfig().getInt(leader.getProfession().getType().getName() + ".xpPerKingdomMember") * (king.getMembers().size() - 1);
											}
										}else if(leader.getProfession().getType().equals(Type.BARON) || leader.getProfession().getType().equals(Type.KING) || leader.getProfession().getType().equals(Type.ARCHBISHOP)){
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

								king.updateShield();

								try {
									king.save();
								} catch (Exception e) {
									ErrorManager.error(35, e);
								}


								if(inactive > 0){
									if(king.getLastOnline() > 0 && System.currentTimeMillis() - king.getLastOnline() > inactive){
										Feudal.getKingdoms().remove(king);
										Feudal.log("Deleting kingdom for [Inactive]: " + king.getName());
										king.delete("SERVER - (KINGDOM INACTIVE)");
										king.removeAllMembers();
									}
								}
							}

						}, next);

					}
				}*/

                    ArrayList<Challenge> challenges = new ArrayList<Challenge>();
                    for(Challenge c : Feudal.getChallenges()){
                        challenges.add(c);
                    }
                    for(Challenge c : challenges){
                        c.updateHourly();
                    }
                    Feudal.saveChallengesRough();
                }catch(Exception e){
                    ErrorManager.error(73, e);
                }
            }

        }, wait, 72000);//72000 = 1 hour
    }

    private static void startStaminaTimer() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Feudal.getPlugin(), new Runnable(){

            @Override
            public void run() {
                try{
                    for(User u : Feudal.getOnlinePlayers().values()){
                        u.regenStamina();
                    }
                }catch(Exception e){
                    ErrorManager.error(74, e);
                }
            }

        }, Feudal.getConfiguration().getConfig().getInt("Stamina.damageRegenTimerTicks"), Feudal.getConfiguration().getConfig().getInt("Stamina.damageRegenTimerTicks"));
    }

    public static void startKingdomSaveTimer() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Feudal.getPlugin(), new Runnable() {
            public void run() {
                Feudal.getPlugin().saveKingdoms(false);
            }
        }, 1200, 1200);
    }

}