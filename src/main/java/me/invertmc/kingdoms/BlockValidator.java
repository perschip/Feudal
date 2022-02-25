package me.invertmc.kingdoms;

import me.invertmc.Feudal;
import me.invertmc.user.User;
import org.bukkit.Material;
import org.bukkit.entity.Boat;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockValidator {

    public static void blockPlace(BlockPlaceEvent event){
        Land land = Feudal.getLand(new Land(event.getBlockPlaced().getLocation()));
        Kingdom king = Feudal.getLandKingdom(land);
        User u = Feudal.getUser(event.getPlayer().getUniqueId().toString());

        if (king == null || u == null){
            return;
        }

        // false means that user does not own this land
        if (!(u.getKingdomUUID().equals(king.getUUID()))){
            if (!king.isInFight()){
                // stop players from placing boats to prevent glitch
                if (event.getBlockPlaced().getState() instanceof Boat) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    public static void onInteract(PlayerInteractEvent event){
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            Land land = Feudal.getLand(new Land(event.getClickedBlock().getLocation()));
            Kingdom king = Feudal.getLandKingdom(land);
            User u = Feudal.getUser(event.getPlayer().getUniqueId().toString());

            if (king == null || u == null){
                return;
            }

            // false means that user does not own this land
            if (!(u.getKingdomUUID().equals(king.getUUID()))){
                if (!king.isInFight()){
                    // stop players from placing boats to prevent glitch
                    if (Feudal.getItemInHand(event.getPlayer()).getType() == Material.OAK_BOAT || Feudal.getItemInHand(event.getPlayer()).getType() == Material.SPRUCE_BOAT
                    || Feudal.getItemInHand(event.getPlayer()).getType() == Material.BIRCH_BOAT || Feudal.getItemInHand(event.getPlayer()).getType() == Material.JUNGLE_BOAT
                    || Feudal.getItemInHand(event.getPlayer()).getType() == Material.DARK_OAK_BOAT || Feudal.getItemInHand(event.getPlayer()).getType() == Material.ACACIA_BOAT) {
                        event.setCancelled(true);

                        // add debug message here (if needed)

                        return;
                    }
                }
            }
        }
    }

}