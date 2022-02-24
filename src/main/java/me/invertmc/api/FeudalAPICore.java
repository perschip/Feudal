package me.invertmc.api;

import java.util.List;
import java.util.UUID;

import me.invertmc.Feudal;
import me.invertmc.kingdoms.Kingdom;
import me.invertmc.kingdoms.Land;
import me.invertmc.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;

public class FeudalAPICore implements FeudalAPI {

    public Feudal getFeudal(){
        return Feudal.getPlugin(Feudal.class);
    }

    public User getUser(UUID uuid){
        return Feudal.getUser(uuid.toString());
    }

    public User getUser(String playerName){
        @SuppressWarnings("deprecation")
        OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
        if(player != null && player.hasPlayedBefore()){
            return Feudal.getUser(player.getUniqueId().toString());
        }else{
            return null;
        }
    }

    public UUID getOfflineUUID(String playerName){
        try{
            String uuid = Feudal.getPlayerData().getConfig().getString(playerName);
            if(uuid != null && !uuid.isEmpty()){
                return UUID.fromString(uuid);
            }else{
                return null;
            }
        }catch(Exception e){
            return null;
        }
    }

    @Deprecated
    public Kingdom getKingdomByName(String kingdomName){
        for(Kingdom kingdom : Feudal.getKingdoms()){
            if(kingdom.getName().equalsIgnoreCase(kingdomName)){
                return kingdom;
            }
        }
        return null;
    }

    public Kingdom getKingdomByUUID(String uuid){
        for(Kingdom kingdom : Feudal.getKingdoms()){
            if(kingdom.getUUID().equals(uuid)){
                return kingdom;
            }
        }
        return null;
    }

    public Kingdom getKingdomByUUID(UUID uuid){
        return getKingdomByUUID(uuid.toString());
    }

    public Kingdom getKingdom(User user){
        if(user.getKingdomUUID() != null && !user.getKingdomUUID().isEmpty()){
            return getKingdomByUUID(user.getKingdomUUID());
        }else{
            return null;
        }
    }

    @Override
    public Kingdom getKingdom(OfflinePlayer player) {
        for(Kingdom kingdom : Feudal.getKingdoms()){
            for(String uuid : kingdom.getMembers().keySet()){
                if(uuid.equals(player.getUniqueId().toString())){
                    return kingdom;
                }
            }
        }
        return null;
    }

    @Override
    public Kingdom getKingdom(Land land) {
        for(Kingdom kingdom : Feudal.getKingdoms()){
            for(Land l : kingdom.getLand()){
                if(l.equals(land)){
                    return kingdom;
                }
            }
        }
        return null;
    }

    @Override
    public Kingdom getKingdom(Chunk chunk) {
        Block block = chunk.getBlock(0, 0, 0);
        if(block != null){
            return getKingdom(block.getLocation());
        }
        return null;
    }

    @Override
    public Kingdom getKingdom(Location location) {
        Land land = new Land(location);
        for(Kingdom kingdom : Feudal.getKingdoms()){
            if(kingdom.getLand().contains(land)){
                return kingdom;
            }
        }
        return null;
    }

    @Override
    public void addCommand(Command feudalCommand) {
        if(feudalCommand instanceof CustomCommand){
            Feudal.getPlugin().getCommands().getCommands().add(((CustomCommand) feudalCommand).getHelpIndex(), feudalCommand);
        }else{
            Feudal.getPlugin().getCommands().getCommands().add(feudalCommand);
        }
    }

    @Override
    public boolean removeCommand(Command feudalCommand) {
        return Feudal.getPlugin().getCommands().getCommands().remove(feudalCommand);
    }

    @Override
    public List<Command> getCommands() {
        return Feudal.getPlugin().getCommands().getCommands();
    }

    @Override
    public String getMinecraftVersion() {
        return Feudal.getVersion();
    }

    @Override
    public String getMessage(String languageField) {
        return Feudal.getMessage(languageField);
    }

    @Override
    public List<Challenge> getChallenges() {
        return Feudal.getChallenges();
    }

    @Override
    public Challenge getChallenge(Kingdom attacker, Kingdom defender) {
        return Feudal.getChallenge(attacker, defender);
    }

    @Override
    public List<Challenge> getChallenges(Kingdom kingdom) {
        return Feudal.getChallenges(kingdom);
    }

}