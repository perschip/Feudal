package me.invertmc.utils;

import me.invertmc.kingdoms.Kingdom;
import me.invertmc.kingdoms.LandManagement;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class UtilsAbove1_7 {

    public static void pistonRetract(BlockPistonRetractEvent event) {
        try{
            if(LandManagement.blockIsOnLand(event.getBlock().getLocation())){
                return;
            }
            for(final Block b : event.getBlocks()){
                if(LandManagement.piston(b.getLocation())){
                    event.setCancelled(true);
                    return;
                }
            }
        }catch(final Exception e){
            ErrorManager.error(44, e);
        }
    }

    public static double getHealth(LivingEntity entity) {
        return entity.getHealth();
    }

    public static double getMaxHealth(LivingEntity entity) {
        return entity.getMaxHealth();
    }

    public static PotionEffect getHiddenPotionEffect(PotionEffectType type, int duration, int level) {
        return new PotionEffect(type, duration, level, true, false);
    }

    public static int countMatches(String shape, String string) {
        return StringUtils.countMatches(shape, string);
    }

    public static void setItemProtector(Item item, String string) {
        item.setCustomName(string);
        item.setCustomNameVisible(true);
    }

    public static boolean isArmorStand(Entity entity) {
        return (entity instanceof ArmorStand);
    }

    public static void keepInventory(PlayerDeathEvent event) {
        event.setKeepInventory(true);
    }

}
