package me.invertmc.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import com.cryptomorin.xseries.XMaterial;
import me.invertmc.Feudal;
import me.invertmc.kingdoms.Kingdom;
import me.invertmc.utils.InventoryGui;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class TrackPlayer {
    private InventoryGui inventory;
    short page = 0;
    short maxPage = 0;
    private Player player;
    private ArrayList<User> onlinePlayers = new ArrayList<User>();
    private ArrayList<User> currentPlayers = new ArrayList<User>();
    private boolean assassin;
    private boolean complete = false;
    private User track = null;
    private double price = 0;

    public TrackPlayer(Player p, boolean assassin){
        player = p;
        this.assassin = assassin;
        if((Feudal.getConfiguration().getConfig().getBoolean("Assassin.randomTracker") && assassin) || (Feudal.getConfiguration().getConfig().getBoolean("Assassin.randomNonassassin") && !assassin)){
            inventory = createRandomTracker();
            if(inventory == null){
                return;
            }
        }else{
            inventory = createTrackInventory();
            updateTrackInventory(1);
        }
        p.closeInventory();
        p.openInventory(inventory.getInventory());
    }

    private InventoryGui createRandomTracker() {
        InventoryGui inv = new InventoryGui(Bukkit.createInventory(player, 9, InventoryGui.lim(Feudal.getMessage("track.title"))));

        Kingdom king = null;
        User us = Feudal.getUser(player.getUniqueId().toString());
        if(us != null && !us.getKingdomUUID().equals("")){
            king = Feudal.getKingdom(us.getKingdomUUID());
        }

        User track = Feudal.getUser(us.getLastTrack());

        price = Feudal.getConfiguration().getConfig().getDouble("Assassin.trackRange.price");
        if(!assassin){
            price *= Feudal.getConfiguration().getConfig().getDouble("Assassin.trackRange.priceMultiplier");
        }

        if(track == null || track.getPlayer() == null || !track.getPlayer().isOnline()){

            for(String uuid : Feudal.getOnlinePlayers().keySet()){
                if(!uuid.equals(player.getUniqueId().toString())){
                    User u = Feudal.getOnlinePlayers().get(uuid);
                    if(u.getPlayer() != null && u.getPlayer().isOnline() && u.getPlayer().getLocation().getWorld().equals(player.getLocation().getWorld())){
                        onlinePlayers.add(u);
                    }
                }
            }
            for(int i = 0; i < onlinePlayers.size(); i++){
                User u = onlinePlayers.get(i);
                if(u != null && u.getPlayer() != null && u.getPlayer().isOnline() && player.isOnline() && u.getPlayer().getWorld().equals(player.getWorld())){
                    boolean isFriend = false;
                    if(king != null){
                        if(!u.getKingdomUUID().equals("")){
                            if(u.getKingdomUUID().equals(king.getUUID())){
                                isFriend = true;
                            }else{
                                Kingdom checkK = Feudal.getKingdom(u.getKingdomUUID());
                                if(checkK != null && checkK.isAllied(king)){
                                    isFriend = true;
                                }
                            }
                        }
                    }

                    if(!isFriend){
                        if(isTrackable(u)){
                            currentPlayers.add(u);
                        }
                    }
                }
            }

            if(currentPlayers.size() > 1){
                track = currentPlayers.get(new Random().nextInt(currentPlayers.size()));
            }else{
                if(currentPlayers.size() > 0){
                    track = currentPlayers.get(0);
                }
            }

        }else{
            boolean isFriend = false;
            if(king != null){
                if(!track.getKingdomUUID().equals("")){
                    if(track.getKingdomUUID().equals(king.getUUID())){
                        isFriend = true;
                    }else{
                        Kingdom checkK = Feudal.getKingdom(track.getKingdomUUID());
                        if(checkK != null && checkK.isAllied(king)){
                            isFriend = true;
                        }
                    }
                }
            }

            if(isFriend){
                track = null;
                return createRandomTracker();
            }else if(!isTrackable(track)){
                this.player.sendMessage(Feudal.getMessage("track.tooClose"));
                return null;
            }
        }

        this.track = track;

        if(this.track == null){
            this.player.sendMessage(Feudal.getMessage("newTracker.noPlayers"));
            return null;
        }

        inv.setItem("RANDOM", 4, Selection.createItem(Material.EMERALD, 1, (short) 0, Feudal.getMessage("newTracker.trackRandom"), Arrays.asList(new String[]{Feudal.getMessage("track.price") + ((int)price)})));
        us.setLastTrack(track.getUUID());
        return inv;
    }

    private boolean isTrackable(User u) {
        if(u != null && u.getPlayer() != null && u.getPlayer().isOnline()){
            double min = Feudal.getConfiguration().getConfig().getDouble("Assassin.trackRange.minNonAssassin");
            if(assassin){
                min = Feudal.getConfiguration().getConfig().getDouble("Assassin.trackRange.min");
            }
            if(u.getPlayer().getLocation().getBlockY() < (u.getPlayer().getLocation().getWorld().getSeaLevel()-3)){
                if(assassin){
                    min = Feudal.getConfiguration().getConfig().getDouble("Assassin.trackRange.minUnderGround");
                }else{
                    min = Feudal.getConfiguration().getConfig().getDouble("Assassin.trackRange.minNonAssassinUnderGround");
                }
            }

            if(u.getPlayer().getLocation().getWorld().equals(player.getWorld()) && u.getPlayer().getLocation().distance(player.getLocation()) > min){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    private InventoryGui createTrackInventory() {
        int size = Feudal.getOnlinePlayers().size()-1;
        if(size > 36){
            size = 54;
        }else if(size > 27){
            size = 36;
        }else if(size > 18){
            size = 27;
        }else if(size > 9){
            size = 18;
        }else{
            size = 9;
        }
        InventoryGui i = new InventoryGui(Bukkit.createInventory(player, size, InventoryGui.lim(Feudal.getMessage("track.title"))));
        for(String uuid : Feudal.getOnlinePlayers().keySet()){
            if(!uuid.equals(player.getUniqueId().toString())){
                User u = Feudal.getOnlinePlayers().get(uuid);
                if(u.getPlayer() != null && u.getPlayer().isOnline() && u.getPlayer().getLocation().getWorld().equals(player.getLocation().getWorld())){
                    onlinePlayers.add(u);
                }
            }
        }
        maxPage = (short) (onlinePlayers.size() / 45);
        if(onlinePlayers.size() % 45 != 0){
            maxPage++;
        }
        return i;
    }

    private void updateTrackInventory(int page){
        this.page = (short) page;

        inventory.clear();
        inventory.getInventory().clear();

        if(page > 1){
            inventory.setItem("BACK", inventory.getInventory().getSize()-9, Selection.createItem(Material.WHITE_WOOL, 1, (short) 14, Feudal.getMessage("track.back"), null));
        }
        if(page != maxPage){
            inventory.setItem("NEXT", inventory.getInventory().getSize()-1, Selection.createItem(Material.WHITE_WOOL, 1, (short) 5, Feudal.getMessage("track.next"), null));
        }

        currentPlayers.clear();

        Kingdom king = null;
        User us = Feudal.getUser(player.getUniqueId().toString());
        if(us != null && !us.getKingdomUUID().equals("")){
            king = Feudal.getKingdom(us.getKingdomUUID());
        }

        for(int i = ((page-1)*45); i < (page * 45); i++){
            if(i < onlinePlayers.size()){
                User u = onlinePlayers.get(i);
                if(u != null && u.getPlayer() != null && u.getPlayer().isOnline() && player.isOnline() && u.getPlayer().getWorld().equals(player.getWorld())){
                    boolean isFriend = false;
                    if(king != null){
                        if(!u.getKingdomUUID().equals("")){
                            if(u.getKingdomUUID().equals(king.getUUID())){
                                isFriend = true;
                            }else{
                                Kingdom checkK = Feudal.getKingdom(u.getKingdomUUID());
                                if(checkK != null && checkK.isAllied(king)){
                                    isFriend = true;
                                }
                            }
                        }
                    }

                    if(!isFriend){
                        //double distance = ((((int)(player.getLocation().distance(u.getPlayer().getLocation()))) + 99) / 100 ) * 100;
                        double price = Feudal.getConfiguration().getConfig().getDouble("Assassin.trackRange.price");
                        if(!assassin){
                            price *= Feudal.getConfiguration().getConfig().getDouble("Assassin.trackRange.priceMultiplier");
                        }
                        ItemStack stack = null;
                        //if(assassin){
                        //stack = Selection.createItem(Material.SKULL_ITEM, 1, (short) 3, "\u00a7e\u00a7l" + u.getPlayer().getName(), Arrays.asList(new String[]{Main.getMessage("track.price") + ((int)price), Main.getMessage("track.dist") + ((int)distance)}));
                        //}else{
                        stack = Selection.createItem(XMaterial.PLAYER_HEAD.parseMaterial(), 1, (short) 3, "\u00a7e\u00a7l" + u.getPlayer().getName(), Arrays.asList(new String[]{Feudal.getMessage("track.price") + ((int)price)}));
                        //}
                        SkullMeta skull = (SkullMeta) stack.getItemMeta();
                        skull.setOwner(u.getPlayer().getName());
                        stack.setItemMeta(skull);
                        inventory.setItem(u.getUUID(), i, stack);
                        currentPlayers.add(u);
                    }
                }
            }
        }
    }

    private void soundDeny(){
        if(!Feudal.getVersion().contains("1.8") && !Feudal.getVersion().equals("1.7")){
            player.playSound(player.getLocation(), Sound.valueOf("BLOCK_ANVIL_LAND"), 1, 0.5F);
        }else{
            player.playSound(player.getLocation(), Sound.valueOf("ANVIL_LAND"), 1, 0.5F);
        }
    }

    private void soundClick(){
        if(!Feudal.getVersion().contains("1.8") && !Feudal.getVersion().equals("1.7")){
            player.playSound(player.getLocation(), Sound.valueOf("UI_BUTTON_CLICK"), 1, 1);
        }else{
            player.playSound(player.getLocation(), Sound.valueOf("CLICK"), 1, 1);
        }
    }

    private void soundComplete(){
        if(!Feudal.getVersion().contains("1.8") && !Feudal.getVersion().equals("1.7")){
            player.playSound(player.getLocation(), Sound.valueOf("ENTITY_PLAYER_LEVELUP"), 1, 0.5F);
        }else{
            player.playSound(player.getLocation(), Sound.valueOf("LEVEL_UP"), 1, 0.5F);
        }
    }

    private void click(String itemString){
        if(itemString != null){
            if(itemString.equals("RANDOM")){
                User user = Feudal.getUser(player.getUniqueId().toString());
                if(user != null){
                    user.syncBalance();
                    User u = track;
                    if(user.getBalance() >= price){
                        if(u.getPlayer() != null && u.getPlayer().isOnline()){
                            if(u.getPlayer().getLocation().getWorld().equals(player.getLocation().getWorld())){
                                user.removeMoney(price);
                                player.setCompassTarget(compassLocation(u.getPlayer().getLocation(), player.getLocation()));
                                soundComplete();

                                player.sendMessage(Feudal.getMessage("track.pay").replace("%price%", Feudal.round(price) + "").replace("%player%", Feudal.getMessage("newTracker.random")));

                                player.sendMessage(Feudal.getMessage("track.point").replace("%player%", Feudal.getMessage("newTracker.random")));
                                complete = true;
                                player.closeInventory();
                            }else{
                                soundDeny();
                                complete = true;
                                player.closeInventory();
                                player.sendMessage(Feudal.getMessage("track.wrongWorld"));
                            }
                        }else{
                            soundDeny();
                            complete = true;
                            player.closeInventory();
                            player.sendMessage(Feudal.getMessage("track.offline"));
                        }
                    }else{
                        soundDeny();
                        complete = true;
                        player.closeInventory();
                        player.sendMessage(Feudal.getMessage("track.noMoney"));
                    }
                    return;
                }

            }else if(price != 0){
                soundDeny();
                complete = true;
                player.closeInventory();
            }

            if(itemString.equals("BACK")){
                if(page - 1 > 0){
                    updateTrackInventory(page - 1);
                    soundClick();
                }else{
                    soundDeny();
                }
            }else if(itemString.equals("NEXT")){
                if(page + 1 < maxPage){
                    updateTrackInventory(page + 1);
                    soundClick();
                }else{
                    soundDeny();
                }
            }else{
                User user = Feudal.getUser(player.getUniqueId().toString());
                if(user != null)
                    for(User u : currentPlayers){
                        if(u.getUUID().equals(itemString)){
                            if(u.getPlayer() != null && user.getPlayer() != null && u.getPlayer().getLocation().getWorld().equals(user.getPlayer().getWorld())){
                                double price = Feudal.getConfiguration().getConfig().getDouble("Assassin.trackRange.price");
                                if(!assassin){
                                    price *= Feudal.getConfiguration().getConfig().getDouble("Assassin.trackRange.priceMultiplier");
                                }
                                user.syncBalance();
                                if(user.getBalance() >= price){
                                    if(u.getPlayer() != null && u.getPlayer().isOnline()){
                                        if(u.getPlayer().getLocation().getWorld().equals(player.getLocation().getWorld())){

                                            double min = Feudal.getConfiguration().getConfig().getDouble("Assassin.trackRange.minNonAssassin");
                                            if(assassin){
                                                min = Feudal.getConfiguration().getConfig().getDouble("Assassin.trackRange.min");
                                            }
                                            if(u.getPlayer().getLocation().getBlockY() < (u.getPlayer().getLocation().getWorld().getSeaLevel()-3)){
                                                if(assassin){
                                                    min = Feudal.getConfiguration().getConfig().getDouble("Assassin.trackRange.minUnderGround");
                                                }else{
                                                    min = Feudal.getConfiguration().getConfig().getDouble("Assassin.trackRange.minNonAssassinUnderGround");
                                                }
                                            }

                                            if(u.getPlayer().getLocation().distance(player.getLocation()) > min){
                                                user.removeMoney(price);
                                                player.setCompassTarget(compassLocation(u.getPlayer().getLocation(), player.getLocation()));
                                                soundComplete();

                                                player.sendMessage(Feudal.getMessage("track.pay").replace("%price%", Feudal.round(price) + "").replace("%player%", u.getPlayer().getName()));

                                                player.sendMessage(Feudal.getMessage("track.point").replace("%player%", u.getPlayer().getName()));
                                                complete = true;
                                                player.closeInventory();
                                            }else{
                                                soundDeny();
                                                complete = true;
                                                player.closeInventory();
                                                player.sendMessage(Feudal.getMessage("track.tooClose"));
                                            }
                                        }else{
                                            soundDeny();
                                            complete = true;
                                            player.closeInventory();
                                            player.sendMessage(Feudal.getMessage("track.wrongWorld"));
                                        }
                                    }else{
                                        soundDeny();
                                        complete = true;
                                        player.closeInventory();
                                        player.sendMessage(Feudal.getMessage("track.offline"));
                                    }
                                }else{
                                    soundDeny();
                                    complete = true;
                                    player.closeInventory();
                                    player.sendMessage(Feudal.getMessage("track.noMoney"));
                                }
                                return;
                            }else{
                                soundDeny();
                                complete = true;
                                player.closeInventory();
                                player.sendMessage(Feudal.getMessage("track.wrongWorld"));
                            }
                        }
                    }
                soundDeny();
            }
        }
    }

    private Location compassLocation(Location location, Location location2) {
        if(location.getWorld().equals(location2.getWorld())){
            double distance = location.distance(location2) * .5;
            double distanceAway = new Random().nextInt((int)distance+2);
            double ne = new Random().nextInt((int)distance+1) + (distance * .25);
            if(ne > distanceAway){
                distanceAway = ne;
            }
            int direction = new Random().nextInt(360);
            double rad = Math.toRadians(direction);
            double x = Math.cos(rad) * distanceAway;
            double z = Math.sin(rad) * distanceAway;
            if(direction >= 90 && direction < 180){
                x *= -1;
            }else if(direction >= 180 && direction < 270){
                x *= -1;
                z *= -1;
            }else if(direction >= 270 && direction < 360){
                z *= -1;
            }
            return new Location(location.getWorld(), location.getX() + x, location.getY(), location.getZ() + z);
        }else{
            return location;
        }
    }

    public static void inventoryClickEvent(InventoryClickEvent event) {
        if(Feudal.getTrackPlayers().size() > 0){
            ArrayList<TrackPlayer> remove = new ArrayList<TrackPlayer>();
            for(TrackPlayer track : Feudal.getTrackPlayers()){
                if(track.complete){
                    remove.add(track);
                }else{
                    if(track.player.equals(event.getWhoClicked())){
                        if(event.getInventory() != null && track.inventory != null)
                            if(event.getInventory().equals(track.inventory.getInventory())){
                                track.click(track.inventory.getItemString(event.getCurrentItem()));
                                if(track.complete){
                                    remove.add(track);
                                }
                            }
                        event.setCancelled(true);
                    }
                }
            }
            for(TrackPlayer mar : remove){
                Feudal.getTrackPlayers().remove(mar);
            }
        }
    }

    public static void close(InventoryCloseEvent event) {
        if(Feudal.getTrackPlayers().size() > 0){
            ArrayList<TrackPlayer> remove = new ArrayList<TrackPlayer>();
            for(TrackPlayer mar : Feudal.getTrackPlayers()){
                if(mar.player.equals(event.getPlayer())){
                    if(event.getInventory() != null && mar.inventory != null){
                        if(event.getInventory().equals(mar.inventory.getInventory())){
                            if(!mar.complete){
                                remove.add(mar);
                            }
                        }
                    }
                }
            }
            for(TrackPlayer mar : remove){
                Feudal.getTrackPlayers().remove(mar);
            }
        }
    }
}
