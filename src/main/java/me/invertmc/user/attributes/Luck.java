package me.invertmc.user.attributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import me.invertmc.Feudal;
import me.invertmc.user.User;
import me.invertmc.user.classes.Profession;
import me.invertmc.utils.Randomizer;
import me.invertmc.utils.Utils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Luck {
    private static Random rand = new Random();

    public static void entityDeath(EntityDeathEvent event){
        if(event.getEntity() != null && event.getEntity().getKiller() != null && event.getEntity().getKiller() instanceof Player && !event.getEntity().getType().equals(EntityType.PLAYER) && event.getEntity() instanceof LivingEntity){
            if(event.getDrops() != null && event.getDrops().size() > 0){
                User u = Feudal.getUser(((Player)event.getEntity().getKiller()).getUniqueId().toString());
                if(u != null){
                    double luckDecimal = Attributes.getLuck(u.getLuck().getLevel());
                    if(luckDecimal != 0){
                        ArrayList<ItemStack> add = new ArrayList<ItemStack>();
                        for(ItemStack stack : event.getDrops()){
                            if(stack != null && stack.getType() != null){
                                if(Luck.chance(luckDecimal * (Feudal.getConfiguration().getConfig().getDouble("Luck.Effect.dropDoublePercent")/100))){
                                    add.add(stack);
                                }
                            }
                        }
                        for(ItemStack s : add){
                            event.getDrops().add(s);
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    public static void playerDamage(EntityDamageEvent event){
        if(!event.isCancelled()){
            if(event.getEntity() != null){
                if(event.getEntity() instanceof Player){
                    if(!event.getCause().equals(DamageCause.VOID) && !event.getCause().equals(DamageCause.SUICIDE) && !event.getCause().equals(DamageCause.CUSTOM)){
                        Player p = (Player)event.getEntity();
                        if(Utils.getHealth(p) - event.getDamage() <= 0.5){
                            User u = Feudal.getUser(p.getUniqueId().toString());
                            if(u != null){
                                double luckDecimal = Attributes.getLuck(u.getLuck().getLevel());
                                if(Luck.chance(luckDecimal * (Feudal.getConfiguration().getConfig().getDouble("Luck.Effect.cheatDeathPercent")/100))){
                                    event.setCancelled(true);
                                    p.setHealth(Utils.getMaxHealth(p) / 2);
                                    p.sendMessage(Feudal.getMessage("luck.heal"));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void xpGain(PlayerExpChangeEvent event){
        if(event.getAmount() > 0){
            if(event.getPlayer() != null){
                User u = Feudal.getUser(event.getPlayer().getUniqueId().toString());
                if(u != null){
                    double luckDecimal = Attributes.getLuck(u.getLuck().getLevel());
                    if(Luck.chance(luckDecimal * (Feudal.getConfiguration().getConfig().getDouble("Luck.Effect.doubleXPPercent")/100))){
                        event.setAmount(event.getAmount()*2);
                    }
                }
            }
        }
    }

    public static boolean chance(double d) {
        d = d*10000;
        if(d >= rand.nextInt(10000)){
            return true;
        }else{
            return false;
        }
    }

    public static void enchantItem(EnchantItemEvent event) {
        if(event.getEnchanter() != null){
            User u = Feudal.getUser(event.getEnchanter().getUniqueId().toString());
            if(u != null){
                HashMap<Enchantment, Integer> updated = new HashMap<Enchantment, Integer>();
                for(Enchantment e : event.getEnchantsToAdd().keySet()){
                    int level = event.getEnchantsToAdd().get(e);
                    if(level < e.getMaxLevel()){
                        if(Luck.chance(Attributes.getLuck(u.getLuck().getLevel()) * (Feudal.getConfiguration().getConfig().getDouble("Luck.Effect.higherEnchantmentsPercent")/100))){
                            updated.put(e, level+1);
                        }
                    }
                }
                for(Enchantment e : updated.keySet()){
                    event.getEnchantsToAdd().put(e, updated.get(e));
                }
                if(updated.size() > 0){
                    event.getEnchanter().sendMessage(Feudal.getMessage("luck.enchant"));
                }
            }
        }
    }

    public static void potionConsume(PlayerItemConsumeEvent event) {
        User u = Feudal.getUser(event.getPlayer().getUniqueId().toString());
        if(u != null){
            if(Luck.chance(Attributes.getLuck(u.getLuck().getLevel()) * (Feudal.getConfiguration().getConfig().getDouble("Luck.Effect.betterPotionEffectsPercent")/100))){
                if(event.getPlayer().hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)){
                    event.getPlayer().removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                }
                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 800, 0));
            }
        }
    }

    public static void potionSplash(PotionSplashEvent event) {
        long start = System.currentTimeMillis();
        for(LivingEntity ent : event.getAffectedEntities()){
            if(System.currentTimeMillis() - start > 1000){
                break;
            }
            if(ent instanceof Player){
                User u = Feudal.getUser(((Player)ent).getUniqueId().toString());
                if(u != null){
                    for(PotionEffect e : event.getPotion().getEffects()){
                        if(e.getType().equals(PotionEffectType.BLINDNESS) || e.getType().equals(PotionEffectType.CONFUSION) ||
                                e.getType().equals(PotionEffectType.HARM) || e.getType().equals(PotionEffectType.HUNGER) ||
                                e.getType().equals(PotionEffectType.POISON) || e.getType().equals(PotionEffectType.SLOW) ||
                                e.getType().equals(PotionEffectType.SLOW_DIGGING) || e.getType().equals(PotionEffectType.WEAKNESS) ||
                                e.getType().equals(PotionEffectType.WITHER)){
                            //Stamina effects negative
                            if(u.getStamina() != null){
                                event.setIntensity(ent, event.getIntensity(ent) * Attributes.getStaminaBadPotionMultiplier(u.getStamina().getLevel()));
                            }

                            continue;
                        }
                    }
                    if(Luck.chance(Attributes.getLuck(u.getLuck().getLevel()) * (Feudal.getConfiguration().getConfig().getDouble("Luck.Effect.betterPotionEffectsPercent")/100))){
                        event.setIntensity(ent, event.getIntensity(ent)+1);
                    }

                    //Stamina effects positive
                    if(u.getStamina() != null){
                        event.setIntensity(ent, event.getIntensity(ent) * Attributes.getStaminaGoodPotionMultiplier(u.getStamina().getLevel()));
                    }
                }
            }
        }
    }

    public static void damageEntity(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player){
            User u = Feudal.getUser(((Player)event.getDamager()).getUniqueId().toString());
            if(u != null){
                if(Luck.chance(Attributes.getLuck(u.getLuck().getLevel()) * (Feudal.getConfiguration().getConfig().getDouble("Luck.Effect.criticalHitsPercent")/100))){
                    event.setDamage(event.getDamage()*1.25);
                }
            }
        }
        if(event.getEntity() instanceof Player){
            User u = Feudal.getUser(((Player)event.getEntity()).getUniqueId().toString());
            if(u != null){
                if(Luck.chance(Attributes.getLuck(u.getLuck().getLevel()) * (Feudal.getConfiguration().getConfig().getDouble("Luck.Effect.reducedDamagePercent")/100))){
                    event.setDamage(event.getDamage()*0.5);
                }
            }
        }
    }

    public static void damageItem(PlayerItemDamageEvent event) {
        User u = Feudal.getUser(event.getPlayer().getUniqueId().toString());
        if(u != null){
            if(Luck.chance(Attributes.getLuck(u.getLuck().getLevel()) * (Feudal.getConfiguration().getConfig().getDouble("Luck.Effect.reducedDamagePercent")/100))){
                event.setCancelled(true);
            }
        }
    }

    public static void fish(PlayerFishEvent event) {
        if(event.getCaught() != null && event.getCaught() instanceof Item){
            User u = Feudal.getUser(event.getPlayer().getUniqueId().toString());
            if(u != null){
                if(Luck.chance(Attributes.getLuck(u.getLuck().getLevel()) * (Feudal.getConfiguration().getConfig().getDouble("Luck.Effect.fishCatchPercent")/100))){
                    u.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), ((Item) event.getCaught()).getItemStack());
                }
                if(u.getProfession() != null && u.getProfession().getType() == Profession.Type.FISHER) {
                    event.setExpToDrop(event.getExpToDrop()*7);
                    if(Luck.chance(Attributes.getLuck(u.getLuck().getLevel()) * (Feudal.getConfiguration().getConfig().getDouble("Luck.Effect.fishCatchPercent")/100))){
                        u.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), randomFishItem());
                    }
                }
            }
        }
    }

    private static ItemStack randomFishItem() {
        Randomizer<ItemStack> random = new Randomizer<ItemStack>(rand);

        //out of 1000
        random.add(new ItemStack(Material.REDSTONE, 1), 102);
        random.add(new ItemStack(Material.GOLD_NUGGET, 1), 103);
        random.add(new ItemStack(Material.APPLE, 1), 102);
        random.add(new ItemStack(Material.BLAZE_ROD, 1), 102);
        random.add(new ItemStack(Material.COAL, 1), 103);
        random.add(new ItemStack(Material.SLIME_BALL, 1), 103);
        random.add(new ItemStack(Material.PUMPKIN_SEEDS, 1), 102);
        random.add(new ItemStack(Material.MELON_SEEDS, 1), 102);
        random.add(new ItemStack(Material.GOLD_INGOT, 1), 25);
        random.add(new ItemStack(Material.GOLD_ORE, 1), 25);
        random.add(new ItemStack(Material.IRON_INGOT, 1), 25);
        random.add(new ItemStack(Material.IRON_ORE, 1), 25);
        random.add(new ItemStack(Material.GUNPOWDER, 1), 25);
        random.add(new ItemStack(Material.GHAST_TEAR, 1), 25);
        random.add(new ItemStack(Material.GOLDEN_APPLE, 1), 15);
        random.add(new ItemStack(Material.DIAMOND, 1), 10);
        random.add(new ItemStack(Material.EMERALD, 1), 5);
        random.add(new ItemStack(Material.GOLDEN_APPLE, 1, (byte) 1), 1);

        return random.randomize();
    }


}