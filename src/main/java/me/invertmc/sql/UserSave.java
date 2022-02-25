package me.invertmc.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.invertmc.Feudal;
import me.invertmc.ScheduledTasks;
import me.invertmc.api.events.NewUserEvent;
import me.invertmc.kingdoms.ChallengeManager;
import me.invertmc.kingdoms.Kingdom;
import me.invertmc.kingdoms.KingdomLog;
import me.invertmc.user.AttributeFixer;
import me.invertmc.user.Selection;
import me.invertmc.user.User;
import me.invertmc.user.attributes.Attribute;
import me.invertmc.user.attributes.Attributes;
import me.invertmc.user.classes.Profession;
import me.invertmc.user.classes.SocialClass;
import me.invertmc.utils.Base64;
import me.invertmc.utils.ErrorManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class UserSave {

	private static final String ONLINE_DEFAULT = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	/*
	 * Online data format:
	 * BASE 64 units:
	 * 
	 * AA-AA-AA-AA-AA-AA-AA ... 24 times (minus dashes)
	 */
	
	/*
	 * Saves a user via MySQL
	 */
	public static void save(final User user, boolean async) {
		SQLControl sql = Feudal.getPlugin().getSql();
		
		String world = "world";
		double x=0, y=0, z=0, pitch=0, yaw=0;
		
		try {
			ResultSet set = sql.getUsersSQL().executeQuery("SELECT * FROM " + sql.getUsersSQL().getTable().getName() + 
					" WHERE uuid='" + user.getUUID() + "'", false);
			if(set != null && set.next()) {
				world = set.getString("locationWorld");
				x = set.getDouble("locationX");
				y = set.getDouble("locationY");
				z = set.getDouble("locationZ");
				pitch = set.getDouble("locationPitch");
				yaw = set.getDouble("locationYaw");
			}
		}catch(Exception e) {
			e.printStackTrace();
			Feudal.error("Failed to get user location from sql while saving: " + user.getUUID() + " - " + user.getName());
		}
		
		user.setLastJoin();
		Location location = new Location(Bukkit.getWorld(world), x, y, z, (float)yaw, (float)pitch);
		if(user.getPlayer() != null && user.getPlayer().isOnline()) {
			location = user.getPlayer().getLocation();
		}
		final Location loc = location;
		
		String pastProfessions = "";
		for(Profession pro : user.getPastProfessions()) {
			pastProfessions += pro.toString() + "~";
		}
		
		String onlineString = "";
		for(int i = 1; i <= 24; i++) {
			try {
				onlineString += Base64.encode(user.getOnlineTime().getHour(i), 2);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		if(onlineString.length() != 48) {
			onlineString = ONLINE_DEFAULT;//data became corrupt
		}
		
		user.syncBalance();
		
		final String sqlString = "UPDATE " + sql.getUsersSQL().getTable().getName() + 
				" SET name='" + SQLUtil.filter(user.getName(), 32) + "'," +
				" reputation='" + user.getReputation() + "'," +
				" firstJoin='" + user.getFirstJoinTime() + "'," +
				" lastJoin='" + user.getLastOnline() + "'," +
				" kingdomUUID='" + user.getKingdomUUID() + "'," +
				" locationWorld='" + loc.getWorld().getName() + "'," +
				" locationX='" + loc.getX() + "'," +
				" locationY='" + loc.getY() + "'," +
				" locationZ='" + loc.getZ() + "'," +
				" locationPitch='" + loc.getPitch() + "'," +
				" locationYaw='" + loc.getYaw() + "'," +
				" cooldownShield='" + user.getCooldownShield() + "'," +
				" currentProfession='" + user.getProfession().toString() + "'," +
				" pastProfessions='" + pastProfessions + "'," +
				" balance='" + user.getSaveBalance() + "'," +
				" strength_xp='" + user.getStrength().getXp() + "'," +
				" strength_level='" + user.getStrength().getLevel() + "'," +
				" toughness_xp='" + user.getToughness().getXp() + "'," +
				" toughness_level='" + user.getToughness().getLevel() + "'," +
				" speed_xp='" + user.getSpeed().getXp() + "'," +
				" speed_level='" + user.getSpeed().getLevel() + "'," +
				" stamina_xp='" + user.getStamina().getXp() + "'," +
				" stamina_level='" + user.getStamina().getLevel() + "'," +
				" luck_xp='" + user.getStamina().getXp() + "'," +
				" luck_level='" + user.getLuck().getLevel() + "'," +
				" online='" + onlineString + "'" +
				" WHERE uuid='" + user.getUUID() + "'";
		
		final int offlineMessages = user.getOfflineMessages().size();
		String offlineValues = "";
		for(String message : limit(user.getOfflineMessages(), 50)) {
			message = SQLUtil.filterBasic(message, 500, false);
			message = message.replace(";", ",");
			if(offlineValues.isEmpty()) {
				offlineValues += "('"+user.getUUID()+"','"+message+"')";
			}else {
				offlineValues += ",('"+user.getUUID()+"','"+message+"')";
			}
		}
		final String sqlOfflineSet = "INSERT INTO `"+sql.getOfflineMessagesSQL().getTable().getName()+"` VALUES " + offlineValues + ";";
		
		
		final int kingdomLogs = user.getPastKingdoms().size();
		String kingdomLogValues = "";
		List<String> list = new ArrayList<String>();
		for(KingdomLog log : user.getPastKingdoms()) {
			list.add(log.toString());
		}
		for(String log : limit(list, 100)) {
			log = SQLUtil.filterBasic(log, 100, false);
			log = log.replace(";", ",");
			if(kingdomLogValues.isEmpty()) {
				kingdomLogValues += "('"+user.getUUID()+"','"+log+"')";
			}else {
				kingdomLogValues += ",('"+user.getUUID()+"','"+log+"')";
			}
		}
		final String sqlSetKingdomLog = "INSERT INTO `"+sql.getKingdomLogSQL().getTable().getName()+"` VALUES " + kingdomLogs + ";";
		
		if(user.isChange() || user.isChangeKingdomLog() || user.isChangeOfflineMessage()) {
			Runnable runnable = new Runnable() {
				public void run() {
					try {
					
						//TODO remove
						System.out.println("SAVE user: " + user.getName());
						
						if(user.isChange()) {
							user.setChange(false);
							
							sql.getUsersSQL().executeUpdate(sqlString);
						}
						
						if(user.isChangeOfflineMessage()) {
							user.setChangeOfflineMessage(false);
							
							//remove offline messages
							sql.getOfflineMessagesSQL().remove("uuid", user.getUUID());
							
							if(offlineMessages > 0) {
								sql.getOfflineMessagesSQL().executeUpdate(sqlOfflineSet);
							}
						}
						
						if(user.isChangeKingdomLog()) {
							user.setChangeKingdomLog(false);
							
							//remove kingdom logs
							sql.getKingdomLogSQL().remove("uuid", user.getUUID());
						
							if(kingdomLogs > 0) {
								sql.getKingdomLogSQL().executeUpdate(sqlSetKingdomLog);
							}
						}
					}catch(Exception e) {
						e.printStackTrace();
						Feudal.error("SQL failed while saving: " + user.getUUID() + " - " + user.getName());
					}
				}
			};
			
			if(async) {
				new Thread(runnable).start();
			}else {
				runnable.run();
			}
		}
	}

	private static List<String> limit(List<String> array, int maxSize) {
		List<String> newArray = new ArrayList<String>();
		for(int i = array.size()-1; i >= 0; i--) {
			newArray.add(0, array.get(i));
			if(newArray.size() >= maxSize) {
				break;
			}
		}
		return newArray;
	}

	/*
	 * Loads a user via MySQL
	 */
	public static void load(User user) {
		SQLControl sql = Feudal.getPlugin().getSql();
		
		//if not exit, loadPlayer()
		try {
			if(sql.getUsersSQL().contains("uuid", user.getUUID())) {
								
				try {
					ResultSet set = sql.getUsersSQL().executeQuery("SELECT * FROM " + sql.getUsersSQL().getTable().getName() + " WHERE uuid='" + user.getUUID() + "'", false);
					if(set.next()) {
						user.setName(set.getString("name"));
						user.setReputation((short) set.getInt("reputation"));
						user.setFirstJoin(set.getLong("firstJoin"));
						user.setLastJoin(set.getLong("lastJoin"));
						user.setKingdomUUID(set.getString("kingdomUUID"));
						user.setCooldownShield(set.getLong("cooldownShield"));
						user.setBalance(set.getDouble("balance"));
						
						user.setStrength(new Attributes(Attribute.STRENGTH, set.getInt("strength_xp"), set.getInt("strength_level"), user));
						user.setToughness(new Attributes(Attribute.TOUGHNESS, set.getInt("toughness_xp"), set.getInt("toughness_level"), user));
						user.setSpeed(new Attributes(Attribute.SPEED, set.getInt("speed_xp"), set.getInt("speed_level"), user));
						user.setStamina(new Attributes(Attribute.STAMINA, set.getInt("stamina_xp"), set.getInt("stamina_level"), user));
						user.setLuck(new Attributes(Attribute.LUCK, set.getInt("luck_xp"), set.getInt("luck_level"), user));
						
						try {
							user.setProfession(
									new Profession(set.getString("currentProfession"), user)
									);
						} catch (Exception e1) {
							ErrorManager.error(88888, e1);
							
							Feudal.error("Failed to load current profession for player by the uuid: " + user.getUUID());
						}
						
						String pastProfessions = set.getString("pastProfessions");
						if(!pastProfessions.isEmpty()) {
							for(String str : pastProfessions.split("~")){
								try {
									user.getPastProfessions().add(new Profession(str, user));
								} catch (Exception e) {
									ErrorManager.error(89, e);
									
									Feudal.error("Failed to load past profession for play by the uuid: " + user.getUUID() + ". Profession string: " + str);
								}
							}
						}
						
						String onlineTime = set.getString("online");
						if(onlineTime.length() == 48) {
							for(int i = 0; i < 24; i++) {
								int i2 = i*2;
								try {
									user.getOnlineTime().setHour(i, Base64.decode(onlineTime.substring(i2, i2+2)));
								}catch(Exception e) {
									e.printStackTrace();
									Feudal.error("Failed to load user online times: " + user.getUUID());
								}
							}
						}else {
							for(int i = 1; i <= 24; i++){
								user.getOnlineTime().setHour(i, 0);
							}
						}
						
						String world = set.getString("locationWorld");
						double x = set.getDouble("locationX");
						double y = set.getDouble("locationY");
						double z = set.getDouble("locationZ");
						double yaw = set.getDouble("locationYaw");
						double pitch = set.getDouble("locationPitch");
						Location location = null;
						try {
							location = new Location(Bukkit.getWorld(world), x, y, z, (float) yaw, (float) pitch);
						}catch(Exception e) {
							try{
								location = new Location(Bukkit.getWorlds().get(0), x, y, z, (float) yaw, (float) pitch);
							}catch(Exception ex){
								ex.printStackTrace();
							}
						}
						
						user.setLocation(location);
					}else {
						Feudal.error("Failed to load user: " + user.getUUID());
					}
					
					set = sql.getOfflineMessagesSQL().executeQuery("SELECT * FROM " + sql.getOfflineMessagesSQL().getTable().getName() + " WHERE uuid='" + user.getUUID() + "'", false);
					while(set != null && set.next()) {
						user.getOfflineMessages().add(set.getString("message"));
					}
					
					set = sql.getKingdomLogSQL().executeQuery("SELECT * FROM " + sql.getKingdomLogSQL().getTable().getName() + " WHERE uuid='" + user.getUUID() + "'", false);
					while(set != null && set.next()) {
						user.getPastKingdoms().add(new KingdomLog(set.getString("log")));
					}
				}catch(Exception e) {
					e.printStackTrace();
					Feudal.error("Failed to load user (2): " + user.getUUID());
				}
			}else {
				if(user.getPlayer() != null) {
					loadPlayer(user.getPlayer());
				}
			}
		} catch (SQLException e) {
			Feudal.error("Failed to check if user exists on SQL: " + user.getUUID());
			e.printStackTrace();
		}
	}
	
	/*
	 * Setup defaults in sql
	 */
	public static void setDefaults(Player player) {
		boolean autoSetup = (Feudal.getConfiguration().getConfig().getBoolean("setup.default.enable"));
		String currentProfession = "NONE/0/0";
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
			currentProfession = prof.toString() + "/" + Feudal.getConfiguration().getConfig().getInt("setup.default.level") + "/0";
		}
		
		double balance = 0;
		if(Feudal.getEco() == 0){
			balance = Feudal.getConfiguration().getConfig().getInt("startBalance");
		}
		
		int strength=0, toughness=0, speed=0, stamina=0, luck=0;
		if(autoSetup) {
			strength = Feudal.getConfiguration().getConfig().getInt("setup.default.strength");
			toughness = Feudal.getConfiguration().getConfig().getInt("setup.default.toughness");
			speed = Feudal.getConfiguration().getConfig().getInt("setup.default.speed");
			stamina = Feudal.getConfiguration().getConfig().getInt("setup.default.stamina");
			luck = Feudal.getConfiguration().getConfig().getInt("setup.default.luck");
		}
		
		SQLControl sql = Feudal.getPlugin().getSql();
		sql.getUsersSQL().set(
				player.getUniqueId().toString(),
				player.getName(),
				Integer.valueOf(Feudal.getConfiguration().getConfig().getInt("reputation.start", 1000)),
				Long.valueOf(System.currentTimeMillis()),
				Long.valueOf(System.currentTimeMillis()),
				new String(""),
				player.getLocation().getWorld().getName(),
				Double.valueOf(player.getLocation().getX()),
				Double.valueOf(player.getLocation().getY()),
				Double.valueOf(player.getLocation().getZ()),
				Double.valueOf(player.getLocation().getYaw()),
				Double.valueOf(player.getLocation().getPitch()),
				Long.valueOf(0),
				currentProfession,
				new String(""),
				Double.valueOf(balance),
				new Integer(0),
				new Integer(strength),
				new Integer(0),
				new Integer(toughness),
				new Integer(0),
				new Integer(speed),
				new Integer(0),
				new Integer(stamina),
				new Integer(0),
				new Integer(luck),
				ONLINE_DEFAULT
				);
	}

	/*
	 * Loads player. Creates new one if they do not exist.
	 */
	public static void loadPlayer(Player player) {
		SQLControl sql = Feudal.getPlugin().getSql();
		try {
			if(sql.getUsersSQL().contains("uuid", player.getUniqueId().toString())) {
				//load from sql
				User u = new User(player.getUniqueId().toString(), null);
				u.loadPermissions();
				Feudal.getOnlinePlayers().put(player.getUniqueId().toString(), u);
				if (u.getProfession().getType().equals(Profession.Type.NONE)) {
					if (Feudal.getConfiguration().getConfig().getBoolean("setup.require")) {
						Feudal.getSelections().add(new Selection(player));
					}
				}
				
				// If a kingdom glitches a player will not glitch upon
				// kingdom load failure.
				Kingdom king = Feudal.getPlayerKingdom(u.getUUID());
				if (king == null) {
					u.setKingdomUUID("");
				} else {
					u.setKingdomUUID(king.getUUID());
					long kingLastOn = king.getLastOnline();
					king.checkOnline();
					if (kingLastOn != king.getLastOnline()) {
						try {
							king.save();
						} catch (Exception e) {
							ErrorManager.error(17, e);
						}
					}
				}

				if (!ScheduledTasks.getUpdated().contains(u.getUUID())) {// Updates												// time
					ScheduledTasks.updateUserOnlineTime(u);
				}

				final User finalU = u;
				Bukkit.getScheduler().scheduleSyncDelayedTask(
						Feudal.getPlugin(), new Runnable() {

							@Override
							public void run() {
								for (AttributeFixer fix : Feudal.getAttributeFixers()) {
									if (finalU.getPlayer() != null) {
										if (fix.getPlayer().getUniqueId().toString().equals(finalU.getUUID())) {
											return;
										}
									}
								}
								finalU.effectAttributes();
							}

						}, 20);

				//

				ChallengeManager.join(player);

				if (Feudal.getConfiguration().getConfig()
						.getBoolean("attributeCap.enable")
						&& Feudal.getConfiguration().getConfig()
								.getBoolean("attributeCap.enableFixer")) {
					if (u.getTotalAtt() > Feudal.getConfiguration()
							.getConfig().getInt("attributeCap.cap")) {
						if (!player
								.hasPermission("feudal.admin.passAttributeFix")) {
							Feudal.getAttributeFixers().add(new AttributeFixer(player));
						}
					}
				}

				return;
			}else {
				//create in sql
				User.setDefaults(player, null);
				
				User user = new User(player.getUniqueId().toString(), null);

				Feudal.getOnlinePlayers().put(player.getUniqueId().toString(), user);

				if (Feudal.getConfiguration().getConfig().getBoolean("setup.require") 
						&& user.getProfession().getType().equals(Profession.Type.NONE))
					Feudal.getSelections().add(new Selection(player));
				
				NewUserEvent event = new NewUserEvent(user);
				Bukkit.getPluginManager().callEvent(event);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Returns user
	 */
	public static User getUser(String uuid) {
		try {
			if (Feudal.getPlugin().getSql().getUsersSQL().contains("uuid", uuid)) {
				return new User(uuid, null);
			} else {
				return null;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void delete(String uuid) {
		SQLControl sql = Feudal.getPlugin().getSql();
		sql.getUsersSQL().remove("uuid", uuid);
	}
}
