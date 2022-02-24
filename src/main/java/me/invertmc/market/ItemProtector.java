package me.invertmc.market;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import me.invertmc.Feudal;
import me.invertmc.utils.ErrorManager;
import me.invertmc.utils.UtilsAbove1_7;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class ItemProtector {

    private static Set<ItemProtector> droppedItems = new HashSet<ItemProtector>();

    private Item item;
    private Player owner;
    private long createTime;

    public ItemProtector(Item entityItem, Player player) {
        item = entityItem;
        owner = player;
        createTime = System.currentTimeMillis();

        item.setPickupDelay(0);
        try {
            if(!Feudal.getVersion().equals("1.7")) {
                UtilsAbove1_7.setItemProtector(item, owner.getName().toUpperCase());
            }
        }catch(Exception e) {
            ErrorManager.error(5531114, e);
        }
    }

    public static void start() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Feudal.getPlugin(), new Runnable() {

            @Override
            public void run() {
                long time = System.currentTimeMillis();
                Iterator<ItemProtector> it = droppedItems.iterator();
                while(it.hasNext()) {
                    ItemProtector item = it.next();
                    if(time - item.createTime > 600000 ||
                            !item.item.isValid()) {
                        it.remove();
                    }
                }
            }

        }, 1200, 1200);
    }

    public static void itemPickup(PlayerPickupItemEvent event) {
        for(ItemProtector item : droppedItems) {
            if(item.item.equals(event.getItem())) {
                if(!item.owner.equals(event.getPlayer())) {
                    event.setCancelled(true);
                }
            }
        }
    }

    public static void itemDespawn(ItemDespawnEvent event) {
        for(ItemProtector item : droppedItems) {
            if(item.item.equals(event.getEntity())) {
                event.setCancelled(true);
            }
        }
    }

    public static void protect(ItemProtector item) {
        droppedItems.add(item);
    }

}