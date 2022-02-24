package me.invertmc.api;

import java.util.List;
import java.util.UUID;

import me.invertmc.Feudal;
import me.invertmc.kingdoms.Kingdom;
import me.invertmc.kingdoms.Land;
import me.invertmc.user.User;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

/**
 *
 * This class is purposed to be used by other Bukkit plugins to hook into Feudal and use it as an API.
 * <br>
 * For help using this <a href="http://feudal.coremod.com/API_Implementation">API click here.</a>
 * <br>
 * <br>
 * Michael Forseth holds the copyright for Feudal. Feudal can not be modified, shared, or changed unless permission is obtained.
 * <br>
 * However, Feudal can be used as an API by other plugins as long as the Feudal binary form is not changed or modified internally.
 * <br>
 * Feudal does not have any warranty and Paul Perschilli is not responsible for any damages or loss of data resulting from Feudal.
 *
 *
 */
public interface FeudalAPI {

    /**
     * Get direct instance of the main @Feudal class which extends JavaPlugin.
     *
     * @return main @Feudal instance
     */
    public Feudal getFeudal();

    /**
     * Get a Feudal @User via a player's UUID.
     *
     * @param uuid of minecraft Player
     * @return @User May return null.
     */
    public User getUser(UUID uuid);

    /**
     * Get a Feudal @User from a player's in game name.
     *
     * @param playerName
     * @return @User May return null.
     */
    public User getUser(String playerName);

    /**
     * Gets a Feudal player's uuid from their name. This can be used to check if they have Feudal user data.
     * @param playerName
     * @return Bukkit Player UUID - May return null
     */
    public UUID getOfflineUUID(String playerName);

    /**
     * Get a Feudal @Kingdom by name.
     *
     * @param kingdomName Feudal kingdom name
     * @return @Kingdom May be null
     * @deprecated Kingdoms can change names, so this method is not suggested.
     */
    @Deprecated
    public Kingdom getKingdomByName(String kingdomName);

    /**
     * Get a Feudal @Kingdom by a kingdom's uuid.
     *
     * @param uuid Feudal Kingdom uuid as string
     * @return @Kingdom May return null
     */
    public Kingdom getKingdomByUUID(String uuid);

    /**
     * Get a Feudal @Kingdom by a kingdom's uuid.
     *
     * @param uuid Feudal Kingdom uuid
     * @return @Kingdom May return null
     */
    public Kingdom getKingdomByUUID(UUID uuid);

    /**
     * Get a Feudal @Kingdom from a Feudal User.
     * @param user Feudal User
     * @return @Kingdom May return null
     */
    public Kingdom getKingdom(User user);

    /**
     * Get a Feudal @Kingdom from a Bukkit OfflinePlayer.
     * <br>
     * This can also be used with a Bukkit Player.
     * @param player Bukkit OfflinePlayer
     * @return @Kingdom May return null
     */
    public Kingdom getKingdom(OfflinePlayer player);

    /**
     * Get a Feudal @Kingdom from Feudal @Land.
     * @param land Feudal @Land which is just a from of a Bukkit Chunk
     * @return @Kingdom May return null
     */
    public Kingdom getKingdom(Land land);

    /**
     * Get a Feudal @Kingdom from a Bukkit Chunk
     * @param chunk Bukkit Chunk
     * @return @Kingdom May return null
     */
    public Kingdom getKingdom(Chunk chunk);

    /**
     * Get a Feudal @Kingdom from a Bukkit Location
     * @param location Bukkit Location
     * @return @Kingdom May return null
     */
    public Kingdom getKingdom(Location location);

    /**
     * Add a Feudal sub-command by using the Feudal @Command
     * <br>
     * If you want to change the location your command is displayed in '/f help', implement @CustomCommand,
     * and return the position you want your command to show up atfor '/f help'.
     *
     * @param feudalCommand Feudal @Command
     */
    public void addCommand(Command feudalCommand);

    /**
     * Remove a Feudal sub-command by using the instance of that Feudal @Command
     * @param feudalCommand Feudal @Command
     * @return true if command was remove, false if not in the command list
     */
    public boolean removeCommand(Command feudalCommand);

    /**
     * Get a list of Feudal @Command which are being used.
     *
     * @return List of @Command
     */
    public List<Command> getCommands();

    /**
     * Get the version of minecraft Feudal has loaded for.
     * <br><br>
     * The versions contain, but are not limited to:
     * <ul>
     * <li>1.14</li>
     * <li>1.13</li>
     * 	<li>1.12</li>
     * <li>1.11</li>
     * <li>1.10</li>
     * <li>1.9.4</li>
     * <li>1.9</li>
     * <li>1.8</li>
     * </ul>
     *
     * @return Minecraft version Feudal loaded for.
     */
    public String getMinecraftVersion();

    /**
     * This will get a String from the Feudal language config file based on the path entered as languageField.
     * @param languageField YAML path in language.yml
     * @return language message
     */
    public String getMessage(String languageField);

    /**
     * This gets a list of all active Challenges.
     * @see @Challenge
     *
     * @return List of @Challenge
     */
    public List<Challenge> getChallenges();

    /**
     * Get a @Challenge from a @Kingdom attacker and defender.
     *
     * @param attacker @Kingdom which is attacking in this challenge
     * @param defender @Kingdom which is defending in this challenge
     * @return @Challenge May be null
     */
    public Challenge getChallenge(Kingdom attacker, Kingdom defender);

    /**
     * Get all active challenges for a specific @Kingdom.
     * @param kingdom @Kingdom
     * @return List of @Challenge
     */
    public List<Challenge> getChallenges(Kingdom kingdom);

}