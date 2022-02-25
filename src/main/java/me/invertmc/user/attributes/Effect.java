package me.invertmc.user.attributes;

import me.invertmc.Feudal;
import me.invertmc.kingdoms.Kingdom;
import me.invertmc.kingdoms.Land;
import me.invertmc.kingdoms.Rank;
import me.invertmc.user.User;
import me.invertmc.user.classes.Profession;
import me.invertmc.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Effect {
    /**
     * Sets the player's movement speed.
     * @param user
     */
    public static void speedMethod(User user){
        try{
            if(user.getProfession() != null && !user.getProfession().getType().equals(Profession.Type.NONE)){
                user.getPlayer().setWalkSpeed(Attributes.getSpeedEffectActual(user.getSpeed().getLevel()));
            }else{
                user.getPlayer().setWalkSpeed(0.2F);
            }
        }catch(IllegalStateException e){
            //Do nothing
        }
    }
    /**
     * Set's the player's damage amount based on their current strength level.
     * @param event
     */
    public static void damageByEntity(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player){
            Player player = (Player) event.getDamager();
            if(Feudal.getOnlinePlayers().containsKey(player.getUniqueId().toString())){
                User u = Feudal.getUser(player.getUniqueId().toString());
                if(u != null && u.getStrength() != null){
                    event.setDamage(event.getDamage() * Attributes.getStrengthDamageEffect(u.getStrength().getLevel()) * u.getStaminaDamage());
                    u.regenStamina();
                }
            }
        }
        if(event.getEntity() instanceof Player){
            User u = Feudal.getUser(((Player) event.getEntity()).getUniqueId().toString());
            if(u != null){
                Profession p = u.getProfession();
                if(p != null && !p.getType().equals(Profession.Type.NONE)){
                    if(p.getType().equals(Profession.Type.GUARD)){
                        if(!u.getKingdomUUID().equals("")){
                            Kingdom k = Feudal.getLandKingdom(new Land(u.getPlayer().getLocation()));
                            if(k != null && k.getUUID().equals(u.getKingdomUUID())){
                                event.setDamage(event.getDamage() * (100.0 - Feudal.getConfiguration().getConfig().getDouble("Guard.damageResistancePercent")) / 100.0);
                            }
                        }
                    }else if(p.getType().equals(Profession.Type.SQUIRE)){
                        if(event.getDamager() instanceof Player){
                            event.setDamage(event.getDamage() * (100.0 - Feudal.getConfiguration().getConfig().getDouble("Squire.damageResistancePercent")) / 100.0);
                        }
                    }else if(p.getType().equals(Profession.Type.KNIGHT)){
                        event.setDamage(event.getDamage() * (100.0 - Feudal.getConfiguration().getConfig().getDouble("Knight.damageResistancePercent")) / 100.0);
                    }else if(p.getType().equals(Profession.Type.KING)){
                        boolean leader = false;
                        Kingdom king = null;
                        if(!u.getKingdomUUID().equals("")){
                            king = Feudal.getKingdom(u.getKingdomUUID());
                            if(king != null){
                                if(king.getMembers().get(u.getUUID()).equals(Rank.LEADER)){
                                    leader = true;
                                }
                            }
                        }

                        if(leader){
                            Kingdom k = Feudal.getLandKingdom(new Land(u.getPlayer().getLocation()));
                            if(k != null && (k.getUUID().equals(u.getKingdomUUID()) || (k.isEnemied(king) && Feudal.getChallenge(king, k) != null))){
                                event.setDamage(event.getDamage() * (100.0 - Feudal.getConfiguration().getConfig().getDouble("King.damageResistancePercent")) / 100.0);
                            }
                        }else{
                            event.setDamage(event.getDamage() * (100.0 - Feudal.getConfiguration().getConfig().getDouble("Knight.damageResistancePercent")) / 100.0);
                        }
                    }else if(p.getType().equals(Profession.Type.ARCHBISHOP)){
                        boolean leader = false;
                        Kingdom king = null;
                        if(!u.getKingdomUUID().equals("")){
                            king = Feudal.getKingdom(u.getKingdomUUID());
                            if(king != null){
                                if(king.getMembers().get(u.getUUID()).equals(Rank.LEADER)){
                                    leader = true;
                                }
                            }
                        }

                        if(leader){
                            Kingdom k = Feudal.getLandKingdom(new Land(u.getPlayer().getLocation()));
                            if(k != null && (k.getUUID().equals(u.getKingdomUUID()) || (k.isEnemied(king) && Feudal.getChallenge(king, k) != null))){
                                event.setDamage(event.getDamage() * (100.0 - Feudal.getConfiguration().getConfig().getDouble("ArchBishop.damageResistancePercent")) / 100.0);
                            }
                        }else{
                            event.setDamage(event.getDamage() * (100.0 - Feudal.getConfiguration().getConfig().getDouble("King.damageResistancePercent")) / 100.0);
                        }
                    }
                }
            }
        }

    }
    /**
     * Makes the player dig faster or slower based on their current strength level. The stronger, the faster.
     * @param event
     */
    public static void blockBreak(BlockBreakEvent event) {
        if(Feudal.getOnlinePlayers().containsKey(event.getPlayer().getUniqueId().toString())){
            int playerHasteLevel = Attributes.getStrengthHasteEffect(Feudal.getOnlinePlayers().get(event.getPlayer().getUniqueId().toString()).getStrength().getLevel());
            if(playerHasteLevel > 0){
                playerHasteLevel--;
                int duration = 60;
                if(event.getBlock() != null && event.getBlock().getType().equals(Material.OBSIDIAN)){
                    duration = 600;
                }
                if(event.getPlayer().hasPotionEffect(PotionEffectType.FAST_DIGGING)){
                    for(PotionEffect pt : event.getPlayer().getActivePotionEffects()){
                        if(pt.getType().equals(PotionEffectType.FAST_DIGGING) && pt.getAmplifier() > playerHasteLevel){
                            return;
                        }
                    }
                    event.getPlayer().removePotionEffect(PotionEffectType.FAST_DIGGING);
                }

                event.getPlayer().addPotionEffect(Utils.getHiddenPotionEffect(PotionEffectType.FAST_DIGGING, duration, playerHasteLevel));
            }
        }
    }
    /**
     * Adds/subtracts to the player's max health based on the player's toughness level.
     * @param user
     */
    @SuppressWarnings("deprecation")
    public static void health(User user){
        if(Feudal.getConfiguration().getConfig().getBoolean("Toughness.effectHealth", true)) {
            user.getPlayer().setMaxHealth(Math.round(
                    Attributes.getToughnessHealthEffect(
                            user.getToughness().getLevel()
                    ) / 2) * 2
            );
        }
    }
    /**
     * Sets the regeneration rate of a player's health based on their toughness level.
     * @param event
     */
    public static void regenerateHealth(EntityRegainHealthEvent event) {
        if(event.getRegainReason().equals(RegainReason.SATIATED)){
            if(event.getEntity() instanceof Player){
                Player player = (Player) event.getEntity();
                if(Feudal.getOnlinePlayers().containsKey(player.getUniqueId().toString())){
                    User user = Feudal.getOnlinePlayers().get(player.getUniqueId().toString());
                    int playerToughness = user.getToughness().getLevel();
                    float playerRegenRate = Attributes.getToughnessRegenRate(playerToughness);
                    event.setAmount(event.getAmount()*playerRegenRate);
                }
            }
        }
    }
    /**
     * Adds to the player's saturation rate based on their current stamina level.
     */
    public static void saturationRegain(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Feudal.getPlugin(), new Runnable()
        {
            public void run()
            {
                for(String UUID : Feudal.getOnlinePlayers().keySet()){
                    //Get user through UUID
                    User user = Feudal.getOnlinePlayers().get(UUID);
                    //Pass in the stamina level to getStaminaEffect method.
                    float stamRegen = Attributes.getStaminaEffect(user.getStamina().getLevel());
                    if(user.getPlayer() != null){
                        if(user.getPlayer().getSaturation() + stamRegen > user.getPlayer().getFoodLevel()){
                            user.getPlayer().setSaturation(user.getPlayer().getFoodLevel());
                        }else{
                            user.getPlayer().setSaturation(user.getPlayer().getSaturation()+stamRegen);
                        }
                    }
                }
            }
        }, Feudal.getConfiguration().getConfig().getInt("Stamina.Tick"), Feudal.getConfiguration().getConfig().getInt("Stamina.Tick"));
    }
}
