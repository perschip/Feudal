package me.invertmc.command;

import me.invertmc.Feudal;
import me.invertmc.kingdoms.Land;
import me.invertmc.utils.ErrorManager;
import me.invertmc.utils.WGUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;


public abstract class Command {

    public Command(){
        setHelpMessage();
    }

    protected static String getCardinalDirection(Player player) {
        float yaw = player.getLocation().getYaw();
        while(yaw < 0){
            yaw += 360;
        }
        while(yaw > 360){
            yaw -= 360;
        }
        if((315 < yaw && yaw <= 360) || (0 <= yaw && yaw <= 45)){
            return "S";
        }else if(45 < yaw && yaw <= 135){
            return "W";
        }else if(135 < yaw && yaw <= 225){
            return "N";
        }else if(225 < yaw && yaw <= 315){
            return "E";
        }else{
            return "+";
        }
    }

    private void setHelpMessage(){
        final HelpMessage[] msgs = getHelpMessage();
        int count = 0;
        for(final HelpMessage msg : msgs){
            if(msg != null){
                final boolean customIsPerms = this instanceof CustomHelpPermissions;
                if(customIsPerms) {
                    msg.setCustomHelpPerms((CustomHelpPermissions) this);
                }
                if(this.getType() == CommandType.SERVER) {
                    continue;
                }
                if(this instanceof CustomCommand){
                    final int index = ((CustomCommand) this).getHelpIndex();
                    if(index >= 0){
                        CommandHelp.getHelpMessages().add(index + count, msg);
                        count++;
                    }
                }else{
                    CommandHelp.getHelpMessages().add(msg);
                }
            }
        }
    }

    protected boolean isAlpha(String name) {
        final char[] chars = name.toLowerCase().toCharArray();
        for (final char c : chars) {
            if(c < 97 || c > 122) {
                return false;
            }
        }

        return true;
    }

    protected boolean isLandProtected(Player p, Land l) {
        if(Bukkit.getPluginManager().isPluginEnabled("WorldGuard") && WGUtils.hasWG()){
            final Chunk c = l.getWorld().getChunkAt(l.getX(), l.getZ());
            final int y = p.getLocation().getBlockY();
            if(c != null){
                try{
                    final Block blocks[] = new Block[]{c.getBlock(0, y, 0)
                            , c.getBlock(15, y, 0)
                            , c.getBlock(15, y, 15)
                            , c.getBlock(0, y, 15)
                            , c.getBlock(8, c.getWorld().getSeaLevel(), 8)
                            , c.getBlock(0, c.getWorld().getSeaLevel(), 0)
                            , c.getBlock(15, c.getWorld().getSeaLevel(), 0)
                            , c.getBlock(15, c.getWorld().getSeaLevel(), 15)
                            , c.getBlock(0, c.getWorld().getSeaLevel(), 15)};
                    for(final Block b : blocks){
                        if(b != null){
                            if(!WGUtils.canBuild(p, b)){
                                return true;
                            }
                        }
                    }
                }catch(final Exception e){
                    ErrorManager.error(37, e);
                    return false;
                }
            }
            return false;
        }else{
            return false;
        }
    }

    protected String getLastOnlineTime(boolean online, long lastOnline) {
        if(online){
            return "ONLINE NOW";
        }
        final long rem = System.currentTimeMillis() - lastOnline;
        final long years = rem / 31557600000L;
        final long weeks = (rem - (years * 31557600000L)) / 604800000L;
        final long days = (rem-(years * 31557600000L)-(weeks * 604800000L)) / 86400000;
        final long hours = ((rem - (days * 86400000) - (years * 31557600000L)-(weeks * 604800000L)) / 3600000);
        final long minutes = (rem - (days * 86400000) - (hours * 3600000)-(years * 31557600000L)-(weeks * 604800000L)) / 60000;
        final long seconds = (rem - (days * 86400000) - (hours * 3600000) - (minutes * 60000)-(years * 31557600000L)-(weeks * 604800000L)) / 1000;
        if(years == 0){
            if(weeks == 0){
                return days + (days == 1 ? " day" : " days") + " " + hours + ":" + minutes + ":" + seconds + " ago";
            }else{
                return weeks + (weeks == 1 ? " week" : " weeks") + ", " + days + (days == 1 ? " day" : " days") + " " + hours + ":" + minutes + ":" + seconds + " ago";
            }
        }else{
            return years + (years == 1 ? " year" : " years") + ", " + weeks + (weeks == 1 ? " week" : " weeks") + ", " + days + (days == 1 ? " day" : " days") + " " + hours + ":" + minutes + ":" + seconds + " ago";
        }
    }

    protected String getPlayerByUUID(String player) {//Gets players uuid from name
        String uuid = null;
        for(final Player pla : Bukkit.getOnlinePlayers()){
            if(pla.getName().equalsIgnoreCase(player)){
                uuid = pla.getUniqueId().toString();
                break;
            }
        }
        if(uuid == null){
            uuid = Feudal.getPlayerData().getConfig().getString(player.toLowerCase());
        }
        return uuid;
    }

    public abstract CommandType getType();
    public abstract boolean run(String[] args, Player p);
    public abstract HelpMessage[] getHelpMessage();

    public enum CommandType{
        PLAYER, SERVER;
    }

}
