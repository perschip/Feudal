package me.invertmc;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.cryptomorin.xseries.XMaterial;
import me.invertmc.api.FeudalAPI;
import me.invertmc.api.FeudalAPICore;
import me.invertmc.command.Commands;
import me.invertmc.configs.Configs;
import me.invertmc.configs.Configuration;
import me.invertmc.kingdoms.*;
import me.invertmc.managergui.InventoryGui2;
import me.invertmc.market.Category;
import me.invertmc.market.CheckForMarketErrors;
import me.invertmc.market.ItemProtector;
import me.invertmc.market.MarketItem;
import me.invertmc.sql.KingdomSave;
import me.invertmc.sql.SQLControl;
import me.invertmc.sql.UserSave;
import me.invertmc.user.*;
import me.invertmc.user.attributes.Effect;
import me.invertmc.user.classes.Profession;
import me.invertmc.utils.ErrorManager;
import me.invertmc.utils.VaultUtils;
import me.invertmc.utils.WGUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class Feudal extends JavaPlugin {

	public static final Random RANDOM = new Random();
	private static Feudal plugin;
	private static File pluginFolder;

	private static Configuration config;
	private static Configuration commandConfig;
	private static Configuration playerData;
	private static Configuration professionData;
	private static Configuration challengesConfig;
	private static Configuration marketConfig;
	private static Configuration marketData;
	private static Configuration language;
	private static FeudalAPI api;
	private static File kingdomsFolder;
	private static File dataFolder;
	private static File playerFolder;
	private static HashMap<Land, Kingdom> lands = new HashMap<>();
	private static HashMap<String, User> onlinePlayers = new HashMap<>(); // String
	// =
	// UUID
	private volatile static ArrayList<Challenge> challenges = new ArrayList<>();
	private static ArrayList<Configuration> kingdomConfigs = new ArrayList<>();
	private static ArrayList<Configuration> ncpConfigs = new ArrayList<>();
	private static ArrayList<Selection> selections = new ArrayList<>();
	private static ArrayList<AttributeFixer> attFixer = new ArrayList<>();
	private static ArrayList<AttributeRedistribute> attRedistribute = new ArrayList<>();
	private static ArrayList<MarketItem> marketItems = new ArrayList<>();
	private static ArrayList<Category> categories = new ArrayList<>();
	private static ArrayList<TrackPlayer> trackPlayers = new ArrayList<>();
	private static ArrayList<Kingdom> kingdoms = new ArrayList<>();
	private static int eco = 0;//0 none ; 1 vault ; 2 moltres
	private static String minecraftVersion = "1.18";
	private static Commands commands;

	private static volatile List<String> saving = Collections.synchronizedList(new ArrayList<String>());

	private static final String pluginName = "Feudal";

	private SQLControl sqlSetup;

	PluginDescriptionFile pdf = this.getDescription();

	public void onEnable() {
		plugin = this;
		minecraftVersion = Bukkit.getVersion();

		this.getLogger().info( "\u00a7e=======================");
		this.getLogger().info("\u00a7eFeudal Has Been Enabled!");
		this.getLogger().info( "\u00a7eVerison: " + this.pdf.getVersion());
		this.getLogger().info("\u00a7e=======================");

		try {
			if (Configs.CreateConfigs()) {
				Feudal.log("Feudal configs have been successfully loaded!");
			}
		} catch (Exception e) {
			Feudal.error("There was a problem while loading the Feudal configs.");
			e.printStackTrace();
		}

		ErrorManager.load();

		if (Bukkit.getServer().getPluginManager().getPlugin("Vault") != null) {
			if (!Bukkit.getServer().getPluginManager().isPluginEnabled("Vault")) {
				Bukkit.getPluginManager().enablePlugin(
						Bukkit.getPluginManager().getPlugin("Vault"));
			}
			VaultUtils.setupEconomy();
			eco = 1;
		}

		if (Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") != null) {
			if (!Bukkit.getServer().getPluginManager()
					.isPluginEnabled("WorldGuard")) {
				Bukkit.getPluginManager().enablePlugin(
						Bukkit.getPluginManager().getPlugin("WorldGuard"));
			}
			WGUtils.setupWG();
		}

		try {
			final PluginManager m = this.getServer().getPluginManager();
			m.registerEvents(new EventManager(), this);
			/*if(!getVersion().contains("1.7") && !getVersion().contains("1.8")) {
				m.registerEvents(new EventManager1_8Plus(), this);
			}*/

			// register commands
			commands = new Commands();
			getCommand("feudal").setExecutor(commands);

			// Load all kingdoms
			if(getSql().doesUse()) {
				KingdomSave.loadKingdoms();
			}else {
				for (final File kingdomFile : kingdomsFolder.listFiles()) {
					final Configuration config = new Configuration(kingdomFile);
					try {
						config.loadConfig();
					} catch (final Exception e) {
						try {
							config.broke();
						} catch (final Exception e1) {
							ErrorManager.error(6, e);
							System.out
									.println("[Feudal] FAILED TO SAVE DATA FOR BROKEN FILE: "
											+ kingdomFile.getName());
						}
						ErrorManager.error(25, e);
						continue;
					}
					Kingdom king = null;
					try {
						king = new Kingdom(config);
					} catch (final Exception e) {
						ErrorManager.error(7, e);
						System.out
								.println("[Feudal] Failed to load kingdom from config: "
										+ kingdomFile.getName());
						continue;
					}

					// Does not load kingdom if the leader is missing. Sorry..
					boolean leader = false;
					for (final String s : king.getMembers().keySet()) {
						if (king.getMembers().get(s).equals(Rank.LEADER)) {
							leader = true;
						}
					}
					if (leader) {
						kingdoms.add(king);
						king.updateLand();
					}
					//
				}
			}
			//

			// Load challenges
			for (final String challengeStr : Feudal.getChallengesConfig().getConfig()
					.getStringList("challenges")) {
				Challenge c = null;
				try {
					c = new Challenge(challengeStr);
				} catch (final Exception e) {
					ErrorManager.error(8, e);
					continue;
				}
				if (c.getAttacker() != null && c.getDefender() != null) {
					challenges.add(c);
				}
			}
			//

			for (final Player p : Bukkit.getOnlinePlayers()) {
				Feudal.loadPlayer(p);
			}

			Effect.saturationRegain();

			ScheduledTasks.startScheduler();

			/*try {
				final Metrics metrics = new Metrics(this);
				metrics.start();
			} catch (final IOException e) {
				ErrorManager.error(9, e);
			}*/

			InventoryGui2.enable(this);


			/*
			if (Feudal.getConfiguration().getConfig().getBoolean("Healer.canCraftGodApple")) {
				final NamespacedKey key = new NamespacedKey(this.plugin, "godApple");
				final ShapedRecipe godApple = new ShapedRecipe(key, new ItemStack(Material.GOLDEN_APPLE, 1, (short) 1));
			}

		    public void createRecipePouch() {
		        ItemStack item = recipeItems("pouch");
		        NamespacedKey key = new NamespacedKey(this.plugin, "pouch");
		        ShapedRecipe craftItem = new ShapedRecipe(key, item);
		        craftItem.shape(this.plugin.getConfig().getString("Recipes.Pouch.Line1"),this.plugin.getConfig().getString("Recipes.Pouch.Line2"),this.plugin.getConfig().getString("Recipes.Pouch.Line3"));

		        int Count = 0;
		        while (plugin.getConfig().getString("Recipes.Pouch.Ingredients." + Count) != null) {
		          Count++;
		        }
		        Count--;
		        while (Count > -1) {
		            craftItem.setIngredient(this.plugin.getConfig().getString("Recipes.Pouch.Ingredients." + Count + ".Key").charAt(0), Material.matchMaterial(this.plugin.getConfig().getString("Recipes.Pouch.Ingredients." + Count + ".Material")));
		            Count--;
		        }

		        this.plugin.getServer().addRecipe(craftItem);
		    } */
			// Make a recipe for the op gold apple (This is restricted to be
			// crafted by healers only) (Configurable)
			if (Feudal.getConfiguration().getConfig()
					.getBoolean("Healer.canCraftGodApple")) {
				final ShapedRecipe godApple = new ShapedRecipe(new ItemStack(
						XMaterial.GOLDEN_APPLE.parseMaterial(), 1, (short) 1));
				godApple.shape("***", "*%*", "***");
				godApple.setIngredient('*', XMaterial.GOLD_BLOCK.parseMaterial());
				godApple.setIngredient('%', XMaterial.APPLE.parseMaterial());
				Bukkit.addRecipe(godApple);
			}
			//

			new CheckForMarketErrors();

			if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
				//new FeudalPlaceholder(this).hook();
				Feudal.log("Placeholders loaded for PlaceholderAPI");
			}

		} catch (final Exception e) {
			ErrorManager.error(4, e);
		}

		String commandOri = null;
		String[] aliases = null;
		for(final String s : Bukkit.getCommandAliases().keySet()) {
			if(s.equalsIgnoreCase("feudal")) {
				aliases = Bukkit.getCommandAliases().get(s);
				commandOri = s;
			}
		}
		String[] newAliases = null;
		final List<String> customCommands = Feudal.getConfiguration().getConfig().getStringList("customCommands");
		try {
			if(aliases != null) {
				newAliases = new String[aliases.length+customCommands.size()];
				for(int i = 0; i < aliases.length; i++) {
					newAliases[i] = aliases[i];
				}
				for(int i = aliases.length; i < newAliases.length; i++) {
					newAliases[i] = customCommands.remove(0);
				}
			}else {
				newAliases = customCommands.toArray(new String[customCommands.size()]);
			}
		}catch(final Exception e) {
			ErrorManager.error(1413222018, e);
		}
		if(commandOri != null && newAliases != null) {
			Bukkit.getCommandAliases().put(commandOri, newAliases);
		}

		ItemProtector.start();
		ScheduledTasks.startKingdomSaveTimer();

		api = new FeudalAPICore();
	}

	public void onDisable() {
		this.getLogger().info(ChatColor.RED + "=======================");
		this.getLogger().info(ChatColor.RED + "Feudal Has Been Disabled!");
		this.getLogger().info(ChatColor.RED + "Verison: " + this.pdf.getVersion());
		this.getLogger().info(ChatColor.RED + "=======================");

		try {
			for (final Block b : Land.getMarks()) {
				b.setType(Material.AIR);
			}

			for (final Selection sel : selections) {
				sel.getPlayer().closeInventory();
			}
				/*for (final Market1_12 mar : markets1_12) {
					mar.getPlayer().closeInventory();
				}*/

			for (final Player p : Bukkit.getOnlinePlayers()) {
				p.closeInventory();
			}
			saveUsers(false);
			saveKingdoms(false);
			saveChallenges();
			saveMarketItems();

			InventoryGui2.disable();

			onlinePlayers.clear();
		} catch (final Exception e) {
			ErrorManager.error(5, e);
		}
	}


	/**
	 *
	 * Get the running instance of FeudalAPI which can be used for API implementation.
	 *
	 * @return Current instance for FeudalAPI
	 */
	public static FeudalAPI getAPI(){
		return api;
	}

	/**
	 * Saves challenges but does no remove active leaderboards
	 */
	public static void saveChallengesRough() {
		final ArrayList<String> save = new ArrayList<>();
		for (final Challenge c : challenges) {
			save.add(c.getSaveString());
		}
		Feudal.getChallengesConfig().getConfig().set("challenges", save);
		try {
			Feudal.getChallengesConfig().save();
		} catch (final Exception e) {
			ErrorManager.error(10, e);
		}
	}

	/**
	 * Saves challenges
	 */
	public static void saveChallenges() {
		final ArrayList<String> save = new ArrayList<>();
		for (final Challenge c : challenges) {
			c.removeLeaderBoard();
			save.add(c.getSaveString());
		}
		Feudal.getChallengesConfig().getConfig().set("challenges", save);
		try {
			Feudal.getChallengesConfig().save();
		} catch (final Exception e) {
			ErrorManager.error(11, e);
		}
	}

	/**
	 * Send console error message
	 *
	 * @param error
	 *            The message sent to the console
	 */
	public static void error(String error) {
		Bukkit.getConsoleSender().sendMessage(
				"\u00a74[" + pluginName + "] Error: " + error);
	}

	/**
	 * Log information to console
	 *
	 * @param msg
	 *            Message logged to console
	 */
	public static void log(String msg) {
		Bukkit.getConsoleSender().sendMessage(
				"\u00a7e[" + pluginName + "] " + msg);
	}

	/**
	 * Send warning information to console
	 *
	 * @param warning
	 *            Message sent to console
	 */
	public static void warn(String warning) {
		Bukkit.getConsoleSender().sendMessage(
				"\u00a76[" + pluginName + "] Warning: " + warning);
	}

	/**
	 * Get the main plugin folder
	 *
	 * @return The plugin folder
	 */
	public static File getPluginFolder() {
		return pluginFolder;
	}

	/**
	 * Set the plugin folder
	 *
	 * @param pluginFolder
	 *            The plugin folder
	 */
	public static void setPluginFolder(File pluginFolder) {
		Feudal.pluginFolder = pluginFolder;
	}

	public static Configuration getCommandConfig() {
		return commandConfig;
	}

	public static void setCommandConfig(Configuration config) {
		commandConfig = config;
	}

	/**
	 * Get the main config Configuration
	 *
	 * @return The main configuration
	 */
	public static Configuration getConfiguration() {
		return config;
	}

	/**
	 * Set the main config file Configuration
	 *
	 * @param config
	 *            The main configuration
	 */
	public static void setConfiguration(Configuration config) {
		Feudal.config = config;
	}

	/**
	 * Get the playerData config
	 *
	 * @return Player data configuration
	 */
	public static Configuration getPlayerData() {
		return playerData;
	}

	/**
	 * Set the player data config
	 *
	 * @param playerData
	 *            Player data configuration
	 */
	public static void setPlayerData(Configuration playerData) {
		Feudal.playerData = playerData;
	}

	/**
	 * Get the kingdoms folder
	 *
	 * @return Kingdoms folder
	 */
	public static File getKingdomsFolder() {
		return kingdomsFolder;
	}

	/**
	 * Set the kingdoms folder
	 *
	 * @param kingdomsFolder
	 *            Kingdoms folder
	 */
	public static void setKingdomsFolder(File kingdomsFolder) {
		Feudal.kingdomsFolder = kingdomsFolder;
	}

	/**
	 * Get the players folder
	 *
	 * @return Player Folder
	 */
	public static File getPlayerFolder() {
		return playerFolder;
	}

	public static void setPlayerFolder(File playerFolder) {
		Feudal.playerFolder = playerFolder;
	}

	/*
	 * public static File getNcpFolder() { return ncpFolder; }
	 *
	 * public static void setNcpFolder(File ncpFolder) { Main.ncpFolder =
	 * ncpFolder; }
	 */

	/**
	 * Get online players in hash map (uuid, User)
	 *
	 * @return HashMap Map of player uuids and their user.
	 */
	public static HashMap<String, User> getOnlinePlayers() {
		return onlinePlayers;
	}

	/**
	 * Set the onlinePlayers hashmap.
	 *
	 * @param onlinePlayers
	 *            Map of online players with uuid and their user
	 */
	public static void setOnlinePlayers(HashMap<String, User> onlinePlayers) {
		Feudal.onlinePlayers = onlinePlayers;
	}

	/**
	 * Get array list of kingdom configs
	 *
	 * @return List of kingdom configs
	 */
	public static ArrayList<Configuration> getKingdomConfigs() {
		return kingdomConfigs;
	}

	/**
	 * Set the kingdom configs arraylist
	 *
	 * @param kingdomConfigs
	 *            List of kingdom configs
	 */
	public static void setKingdomConfigs(ArrayList<Configuration> kingdomConfigs) {
		Feudal.kingdomConfigs = kingdomConfigs;
	}

	/**
	 * Get ncm configs (Currently have no use until NCP Feudal addon)
	 *
	 * @return list of ncp configs
	 * @deprecated
	 */
	@Deprecated
	public static ArrayList<Configuration> getNcpConfigs() {
		return ncpConfigs;
	}

	/**
	 * Set ncm configs (Currently have no use until NCP Feudal addon)
	 *
	 * @deprecated
	 */
	@Deprecated
	public static void setNcpConfigs(ArrayList<Configuration> ncpConfigs) {
		Feudal.ncpConfigs = ncpConfigs;
	}

	/**
	 * Get the Feudal plugin. (Non-static form of this class)
	 *
	 * @return Feudal
	 */
	public static Feudal getPlugin() {
		return plugin;
	}

	/**
	 * Log a player. Save the player's name with their uuid in the player data
	 * config
	 *
	 *
	 *            The player
	 */
	public static void logPlayer(String name, UUID uuid) {
		if (Feudal.getPlayerData() == null) {
			try {
				Configs.playerData();
			} catch (final Exception e) {
				ErrorManager.error(98, e);
			}
		}
		if (Feudal.getPlayerData() != null) {
			Feudal.getPlayerData()
					.getConfig()
					.set(name.toLowerCase(),
							uuid.toString());
		}
		try {
			Feudal.getPlayerData().save();
		} catch (final Exception e) {
			ErrorManager.error(12, e);
			Feudal.error("Failed to save player data when updating player: "
					+ name);
		}
	}

	/*
	 * public static void loadPlayer(Player player, File f) {
	 * if(!onlinePlayers.containsKey(player.getUniqueId().toString())){
	 * Configuration config = new Configuration(f); try { config.loadConfig(); }
	 * catch (Exception e) { e.printStackTrace();
	 * Main.getPlugin().warn("Failed to load config: " +
	 * config.getFile().getAbsolutePath() +
	 * ".  Please check to make sure the config does not have any syntax errors."
	 * ); try { config.broke(); Main.loadPlayer(player); return; } catch
	 * (Exception e1) { e1.printStackTrace();
	 * Main.getPlugin().error("FAILED TO SAVE BROKEN BACKUP OF CONFIG: " +
	 * config.getFile().getAbsolutePath()); return; } } User u = new
	 * User(player.getUniqueId().toString(), config);
	 * onlinePlayers.put(player.getUniqueId().toString(), u);
	 * if(u.getProfession().getType().equals(Profession.Type.NONE)){
	 * selections.add(new Selection(player)); } } }
	 */

	/**
	 * Load a player. Creates a new config if they do not have a player data.
	 *
	 * @param player
	 *            The player
	 */
	public static void loadPlayer(Player player) {
		if(getPlugin().getSql().doesUse()) {
			UserSave.loadPlayer(player);
			return;
		}

		if (!onlinePlayers.containsKey(player.getUniqueId().toString())) {
			for (final File f : Feudal.playerFolder.listFiles()) {
				if (f.getName().equalsIgnoreCase(
						player.getUniqueId().toString() + ".yml")) {
					final Configuration config = new Configuration(f);
					try {
						config.loadConfig();
					} catch (final Exception e) {
						ErrorManager.error(13, e);
						Feudal.getPlugin();
						Feudal.warn("Failed to load config: "
								+ config.getFile().getAbsolutePath()
								+ ".  Please check to make sure the config does not have any syntax errors.");
						try {
							config.broke();
							Feudal.loadPlayer(player);
							return;
						} catch (final Exception e1) {
							ErrorManager.error(14, e1);
							Feudal.getPlugin();
							Feudal.error("FAILED TO SAVE BROKEN BACKUP OF CONFIG: "
									+ config.getFile().getAbsolutePath());
							return;
						}
					}
					User u = null;
					try {
						u = new User(player.getUniqueId().toString(), config);
					} catch (final Exception e) {
						ErrorManager.error(15, e);
						try {
							config.broke();
						} catch (final Exception e1) {
							ErrorManager.error(16, e);
							Feudal.getPlugin();
							Feudal.error("FAILED TO SAVE BROKEN BACKUP OF CONFIG: "
									+ config.getFile().getAbsolutePath());
						}
						Feudal.getPlugin();
						Feudal.error("There was a problem loading player: "
								+ player.getName()
								+ "(UUID: "
								+ player.getUniqueId().toString()
								+ "). The player file existed but was missing information. A new file has been made and the old file is now: "
								+ player.getUniqueId().toString()
								+ ".yml.broken");
						break;

					}

					u.loadPermissions();

					onlinePlayers.put(player.getUniqueId().toString(), u);
					if (u.getProfession().getType()
							.equals(Profession.Type.NONE)) {
						if (Feudal.getConfiguration().getConfig()
								.getBoolean("setup.require")) {
							selections.add(new Selection(player));
						}
					}

					// If a kingdom glitches a player will not glitch upon
					// kingdom load failure.
					final Kingdom king = Feudal.getPlayerKingdom(u.getUUID());
					if (king == null) {
						u.setKingdomUUID("");
					} else {
						u.setKingdomUUID(king.getUUID());
						//long kingLastOn = king.getLastOnline();
						king.checkOnline();
						/*if (kingLastOn != king.getLastOnline()) {
							try {
								king.save();
							} catch (Exception e) {
								ErrorManager.error(17, e);
							}
						}*/
					}

					if (!ScheduledTasks.getUpdated().contains(u.getUUID())) {// Updates
						// player's
						// online
						// time
						ScheduledTasks.updateUserOnlineTime(u);
					}

					final User finalU = u;
					Bukkit.getScheduler().scheduleSyncDelayedTask(
							Feudal.getPlugin(), () -> {
								for (final AttributeFixer fix : Feudal.attFixer) {
									if (finalU.getPlayer() != null) {
										if (fix.getPlayer().getUniqueId().toString().equals(finalU.getUUID())) {
											return;
										}
									}
								}
								finalU.effectAttributes();
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
								Feudal.attFixer.add(new AttributeFixer(player));
							}
						}
					}

					return;
				}
			}

			// Create new player config
			final File f = new File(Feudal.playerFolder.getAbsolutePath() + "/"
					+ player.getUniqueId().toString() + ".yml");
			try {
				f.createNewFile();
			} catch (final IOException e) {
				ErrorManager.error(18, e);
				Feudal.error("Failed to create new file: "
						+ Feudal.playerFolder.getAbsolutePath() + "/"
						+ player.getUniqueId().toString() + ".yml");
				return;
			}
			final Configuration config = new Configuration(f);
			try {
				config.loadConfig();
			} catch (final Exception e) {
				ErrorManager.error(19, e);
				Feudal.error("Failed to load config: " + f.getAbsolutePath());
			}

			User.setDefaults(player, config);

			try {
				config.save();
			} catch (final Exception e) {
				ErrorManager.error(20, e);
				Feudal.error("Failed to save config: " + f.getAbsolutePath());
			}

			final User user = new User(player.getUniqueId().toString(), config);

			onlinePlayers.put(player.getUniqueId().toString(), user);

			if (Feudal.getConfiguration().getConfig().getBoolean("setup.require")
					&& user.getProfession().getType().equals(Profession.Type.NONE))
				selections.add(new Selection(player));
		}
	}

	/**
	 * Get a user from their uuid string
	 *
	 * @param uuid
	 *            Player uuid
	 * @return
	 */
	public static User getUser(String uuid) {
		if (uuid == null) {
			return null;
		}
		if (onlinePlayers == null) {
			onlinePlayers = new HashMap<>();
		}
		if (!onlinePlayers.containsKey(uuid)) {

			if(getPlugin().doesUseSql()) {
				return UserSave.getUser(uuid);
			}

			if (playerFolder == null) {
				try {
					Configs.playerFolder();
				} catch (final Exception e) {
					ErrorManager.error(97, e);
				}
			}
			final File f = new File(Feudal.getPlayerFolder().getAbsolutePath() + "/"
					+ uuid + ".yml");
			if (f.exists()) {
				final Configuration config = new Configuration(f);
				try {
					if (config.loadConfig()) {
						return null;
					}
				} catch (final Exception e) {
					ErrorManager.error(21, e);
					Feudal.getPlugin();
					Feudal.warn("Failed to load config: "
							+ config.getFile().getAbsolutePath()
							+ ".  Please check to make sure the config does not have any syntax errors.");
					try {
						config.broke();
						return null;
					} catch (final Exception e1) {
						ErrorManager.error(22, e1);
						Feudal.getPlugin();
						Feudal.error("FAILED TO SAVE BROKEN BACKUP OF CONFIG: "
								+ config.getFile().getAbsolutePath());
						return null;
					}
				}
				try {
					return new User(uuid, config);
				}catch(final Exception e) {
					return null;
				}
			} else {
				return null;
			}
		} else {
			return onlinePlayers.get(uuid);
		}
	}

	/**
	 * Unload a player's data (Normally when they disconnect)
	 *
	 * @param player
	 *            The player
	 */
	public static void unloadPlayer(Player player) {
		if (Feudal.onlinePlayers.containsKey(player.getUniqueId().toString())) {
			final User u = Feudal.getUser(player.getUniqueId().toString());

			asyncSaveUser(u);

			Feudal.onlinePlayers.remove(player.getUniqueId().toString());
			u.unloadPermissions();
			if (u != null) {
				if (!u.getKingdomUUID().equals("")) {
					final Kingdom king = Feudal.getKingdom(u.getKingdomUUID());
					king.checkOnline();
					try {
						king.save();
					} catch (final Exception e) {
						ErrorManager.error(23, e);
					}
				}
			}
		}
		if (selections.size() > 0) {
			Selection remove = null;
			for (final Selection select : selections) {
				if (select.getPlayer().equals(player)) {
					remove = select;
				}
			}
			if (remove != null) {
				selections.remove(remove);
				player.closeInventory();
			}
		}
		if (Feudal.attFixer.size() > 0) {
			AttributeFixer remove = null;
			for (final AttributeFixer select : attFixer) {
				if (select.getPlayer().equals(player)) {
					remove = select;
				}
			}
			if (remove != null) {
				attFixer.remove(remove);
				player.closeInventory();
			}
		}
		if (Feudal.attRedistribute.size() > 0) {
			AttributeRedistribute remove = null;
			for (final AttributeRedistribute select : attRedistribute) {
				if (select.getPlayer().equals(player)) {
					remove = select;
				}
			}
			if (remove != null) {
				attRedistribute.remove(remove);
				player.closeInventory();
			}
		}
	}

	public boolean isSaving(String uuid) {
		synchronized(saving) {
			if(saving.contains(uuid)) {
				return true;
			}
		}
		return false;
	}

	public static void asyncSaveUser(User u) {
		final Thread thread = new Thread(() -> {
			synchronized(saving){
				saving.add(u.getUUID());
			}
			try{
				u.save(false);
			}catch(final Exception e) {
				ErrorManager.error(1056352018, e);
			}
			synchronized(saving){
				saving.remove(u.getUUID());
			}
		});
		thread.setName("asyncSaveUserLeave");
		if(Feudal.getPlugin().doesUseSql()) {
			thread.start();
		}else {
			thread.run();
		}
		//u.save(false);
	}

	/**
	 * Check if Vault is being used
	 *
	 * @return if vault is being used
	 */
	public static int getEco() {
		return eco;
	}

	/**
	 * Set using Vault
	 *
	 * @param eco
	 *            set vault as being used
	 */
	public static void setEco(int eco) {
		Feudal.eco = eco;
	}

	/**
	 * Get array list of all active selections (Player setup menus)
	 *
	 * @return list of slection menus
	 */
	public static ArrayList<Selection> getSelections() {
		return selections;
	}

	/**
	 * Get array list of all active AttributeFixers (Fix attributes menu)
	 *
	 * @return list of AttributeFixer menus
	 */
	public static ArrayList<AttributeFixer> getAttributeFixers() {
		return attFixer;
	}

	/**
	 * Get array list of all active AttributeRedistribute (AttributeRedistribute
	 * menu)
	 *
	 * @return list of AttributeRedistribute menus
	 */
	public static ArrayList<AttributeRedistribute> getAttributeRedistributers() {
		return attRedistribute;
	}

	/**
	 * Get the main market config.
	 *
	 * @return Market config
	 */
	public static Configuration getMarketConfig() {
		return marketConfig;
	}

	public static void setMarketConfig(Configuration marketConfig) {
		Feudal.marketConfig = marketConfig;
	}

	/**
	 * Get market data. Where items are stored.
	 *
	 * @return Market data config
	 */
	public static Configuration getMarketData() {
		return marketData;
	}

	public static void setMarketData(Configuration marketData) {
		Feudal.marketData = marketData;
	}

	/**
	 * Get language config.
	 *
	 * @return language config
	 */
	public static Configuration getLanguage() {
		return language;
	}

	public static void setLanguage(Configuration language) {
		Feudal.language = language;
	}

	/**
	 * Get a list of the MarketItems
	 *
	 * @return list of market items
	 */
	public static ArrayList<MarketItem> getMarketItems() {
		return marketItems;
	}

	/**
	 * Get array list of the market Categories
	 *
	 * @return list of market categories
	 */
	public static ArrayList<Category> getCategories() {
		return categories;
	}

	/**
	 * Save all market items
	 */
	public static void saveMarketItems() {
		for (final MarketItem item : marketItems) {
			item.setMarketData();
		}
		try {
			Feudal.getMarketData().save();
		} catch (final Exception e) {
			ErrorManager.error(24, e);
			Feudal.getPlugin();
			Feudal.error("Failed to save market items! TRYING AGAIN!");
			try {
				Feudal.getMarketData().save();
			} catch (final Exception e1) {
				Feudal.getPlugin();
				Feudal.error("Failed to save market items! TRYING AGAIN!");
				try {
					Feudal.getMarketData().save();
				} catch (final Exception e2) {
					Feudal.getPlugin();
					Feudal.error("Failed to save market items! TRYING AGAIN!");
					try {
						Feudal.getMarketData().save();
					} catch (final Exception e3) {
						Feudal.getPlugin();
						Feudal.error("FAILED TO SAVE MARKET ITEMS!!!");
					}
				}
			}
		}
	}

	/**
	 * Get profession data config. (Who has what profession)
	 *
	 * @return profession data config
	 */
	public static Configuration getProfessionData() {
		return professionData;
	}

	public static void setProfessionData(Configuration professionData) {
		Feudal.professionData = professionData;
	}

	/**
	 * Get all TrackPlayers in a list. There are the menus for the compass
	 * tracker.
	 *
	 * @return list of open track player menus
	 */
	public static ArrayList<TrackPlayer> getTrackPlayers() {
		return trackPlayers;
	}

	/**
	 * Get a list of all kingdoms
	 *
	 * @return list of all kingdoms
	 */
	public static ArrayList<Kingdom> getKingdoms() {
		return kingdoms;
	}

	/**
	 * Get a kingdom based on a kingdom member's uuid
	 *
	 * @param uuid
	 *            Player uuid
	 * @return Kingdom
	 */
	public static Kingdom getPlayerKingdom(String uuid) {
		for (final Kingdom king : kingdoms) {
			if (king.isMember(uuid)) {
				return king;
			}
		}
		return null;
	}

	/**
	 * Get land that is the same as land given. If two objects are different but
	 * have the same location set in the land.
	 *
	 * @param land
	 *            Land object
	 * @return Land object from the exact object for a kingdom
	 */
	public static Land getLand(Land land) {
		for (final Land l : lands.keySet()) {
			if (l.equals(land)) {
				return l;
			}
		}
		return null;
	}

	/**
	 * Get all land in a hashmap with the kingdom which owns it. Land, Kingdom
	 *
	 * @return map of land and kingdoms
	 */
	public static HashMap<Land, Kingdom> getLands() {
		return lands;
	}

	/**
	 * Get the kingdom from a piece of land. This can return null
	 *
	 * @param land
	 *            Piece of land
	 * @return Kingdom which owns that land
	 */
	public static Kingdom getLandKingdom(Land land) {
		for (final Land l : lands.keySet()) {
			if (l.equals(land)) {
				return lands.get(l);
			}
		}
		return null;
	}

	/**
	 * Converts long to: MM.dd.yy hh:mm a
	 *
	 * @param playerTime
	 *            Time in ms
	 * @return Time as string MM.dd.yy hh:mm a
	 */
	public static String niceTime(long playerTime) {
		return new SimpleDateFormat("MM.dd.yy hh:mm a").format(new Date(
				playerTime));
	}

	/**
	 * Get how many pages there will be for /f list.
	 *
	 * @return Number of pages for /f list
	 */
	public static int getMaxKingdomPage() {
		final double divide = ((double) kingdoms.size()) / 8;
		if (((int) divide) == divide) {
			return ((int) divide);
		} else {
			return ((int) divide) + 1;
		}
	}

	/**
	 * Get a kingdom based on its uuid.
	 *
	 * @param kingdomUUID
	 * @return
	 */
	public static Kingdom getKingdom(String kingdomUUID) {
		for (final Kingdom k : kingdoms) {
			if (k.getUUID().equals(kingdomUUID)) {
				return k;
			}
		}
		return null;
	}

	public static void setChallengesConfig(Configuration configuration) {
		challengesConfig = configuration;
	}

	/**
	 * Get the challenges config
	 *
	 * @return
	 */
	public static Configuration getChallengesConfig() {
		return challengesConfig;
	}

	/**
	 * Get a challenge from the attacker kingdom and defender kingdom
	 *
	 * @param attacker
	 * @param defender
	 * @return
	 */
	public static Challenge getChallenge(Kingdom attacker, Kingdom defender) {
		for (final Challenge c : challenges) {
			if (c.getAttacker().equals(attacker)
					&& c.getDefender().equals(defender)) {
				return c;
			}
		}
		return null;
	}

	/**
	 * Get all challenges associated with a kingdom.
	 *
	 * @param kingdom
	 * @return
	 */
	public static ArrayList<Challenge> getChallenges(Kingdom kingdom) {
		final ArrayList<Challenge> ch = new ArrayList<>();
		for (final Challenge c : challenges) {// Having two for loops make it possible
			// to put accepted before non-accepted
			// challenges.
			if ((c.getAttacker().equals(kingdom) || c.getDefender().equals(
					kingdom))
					&& c.isAccepted()) {
				ch.add(c);
			}
		}
		for (final Challenge c : challenges) {
			if ((c.getAttacker().equals(kingdom) || c.getDefender().equals(
					kingdom))
					&& !c.isAccepted()) {
				ch.add(c);
			}
		}
		return ch;
	}

	/**
	 * Round double to hundredth place
	 *
	 * @param price
	 * @return
	 */
	public static float round(double price) {
		return ((float) Math.round(price * 100F)) / 100;
	}

	/**
	 * Get all challenges
	 *
	 * @return
	 */
	public static ArrayList<Challenge> getChallenges() {
		return challenges;
	}

	/**
	 * Get a message from the language config. Will convert and to color code
	 * and andand to and.
	 *
	 * @param string
	 * @return
	 */
	public static String getMessage(String string) {
		String s = Feudal.getLanguage().getConfig().getString(string);
		if (s == null) {
			Feudal.error("LANGUAGE FIELD MISSING: " + string);
			return "\u00a74\u00a7lLANGUAGE INFORMATION MISSING";
		}
		s = s.replace("&", "\u00a7").replace("\u00a7\u00a7", "&");
		return s;
	}

	/**
	 * Get the minecraft version. EXAMPLE: 1.8, 1.9, 1.9.4, 1.9.10
	 *
	 * @return
	 */
	public static String getVersion() {
		return minecraftVersion;
	}

	/**
	 * Get the data folder for feudal.
	 *
	 * @return
	 */
	public static File getDataFolderFeudal() {
		return dataFolder;
	}

	public static void setDataFolderFeudal(File dataFolder) {
		Feudal.dataFolder = dataFolder;
	}

	/**
	 * Get item in main hand for 1.9 and 1.8 mc
	 *
	 * @param p
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static ItemStack getItemInHand(Player p) {
			return p.getItemInHand();
	}

	/**
	 * Set item in main hand for 1.9 and 1.8 mc
	 *
	 * @param p
	 * @param item
	 */
	@SuppressWarnings("deprecation")
	public static void setItemInHand(Player p, ItemStack item) {
			p.setItemInHand(item);
	}

	/**
	 * Check if a kingdom name is available
	 *
	 * @param kingdom
	 * @param string
	 * @return
	 */
	public static boolean canUseKingdomName(Kingdom kingdom, String string) {
		string = string.toLowerCase();
		for (final Kingdom king : Feudal.kingdoms) {
			if (!king.equals(kingdom)) {
				if (king.getName().toLowerCase().equals(string)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Get all open markets for mc version 1.9.4
	 *
	 * @return list of open 1.9.4 markets
	 *
	public static ArrayList<Market1_9_4> getMarkets1_9_4() {
		return markets1_9_4;
	}

	public static ArrayList<Market1_10> getMarkets1_10() {
		return markets1_10;
	}

	public static ArrayList<Market1_11> getMarkets1_11() {
		return markets1_11;
	}

	public static ArrayList<Market1_12> getMarkets1_12() {
		return markets1_12;
	}

	public static ArrayList<Market1_13> getMarkets1_13() {
		return markets1_13;
	}

	*
	 * public static boolean playerFileExists(String string) { File f = new
	 * File(Main.playerFolder.getAbsolutePath() + "/" + string + ".yml");
	 * if(f.exists()){ return true; }else{ return false; } }
	 */

	/*
	 * public static void loadWorld(String w) { World world =
	 * Bukkit.getWorld(w); if(world == null){ WorldCreator creator = new
	 * WorldCreator(w); creator.environment(World.Environment.NORMAL);
	 * creator.generateStructures(true); world = creator.createWorld(); } }
	 */

	public void saveKingdoms(boolean async) {
		for(final Kingdom king : getKingdoms()) {
			if(king.isChange() || king.isChangeBans() || king.isChangeLand() || king.isChangeMembers() ||  king.isChangeRelations()) {
				try {
					king.save(async);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void saveUsers(boolean async) {
		for(final User user : onlinePlayers.values()) {
			try {
				user.save(async);
			}catch(final Exception e) {
				e.printStackTrace();
			}
		}
	}

	public Commands getCommands() {
		return commands;
	}

	public boolean doesUseSql() {
		if(getSql() != null) {
			return getSql().doesUse();
		}else {
			return false;
		}
	}

	public SQLControl getSql() {
		return sqlSetup;
	}

	public void setSQL(SQLControl sqlControl) {
		this.sqlSetup = sqlControl;
	}
}
