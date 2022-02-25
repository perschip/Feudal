package me.invertmc.user.classes;

import me.invertmc.Feudal;
import me.invertmc.user.TrackPlayer;
import me.invertmc.user.User;
import me.invertmc.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class Effect {
    public static void eat(PlayerItemConsumeEvent event){
        if(!event.isCancelled()){
            if(event.getItem() != null && event.getItem().getType().equals(Material.GOLDEN_CARROT)){
                if(event.getItem().getItemMeta() != null && event.getItem().getItemMeta().getDisplayName() != null && event.getItem().getItemMeta().getDisplayName().equals("\u00a7bHealer Carrot")){

                    if(Feudal.getConfiguration().getConfig().getBoolean("goldcarrot.goldappleeffects")){
                        if(!event.getPlayer().hasPotionEffect(PotionEffectType.ABSORPTION) || (getPotionEffect(event.getPlayer(), PotionEffectType.ABSORPTION).getAmplifier() <= 1 &&getPotionEffect(event.getPlayer(), PotionEffectType.ABSORPTION).getDuration() < 2400)){
                            if(event.getPlayer().hasPotionEffect(PotionEffectType.ABSORPTION)){
                                event.getPlayer().removePotionEffect(PotionEffectType.ABSORPTION);
                            }
                            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 1));
                        }
                        if(!event.getPlayer().hasPotionEffect(PotionEffectType.REGENERATION) || (getPotionEffect(event.getPlayer(), PotionEffectType.REGENERATION).getAmplifier() <= 1 && getPotionEffect(event.getPlayer(), PotionEffectType.REGENERATION).getDuration() < 100)){
                            if(event.getPlayer().hasPotionEffect(PotionEffectType.REGENERATION)){
                                event.getPlayer().removePotionEffect(PotionEffectType.REGENERATION);
                            }
                            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1));
                        }
                    }
                    if(!event.getPlayer().hasPotionEffect(PotionEffectType.NIGHT_VISION)){
                        if(Feudal.getConfiguration().getConfig().getBoolean("goldcarrot.nightvision")){
                            if(!(event.getPlayer().getWorld().getTime() < 12300 || event.getPlayer().getWorld().getTime() > 23850)){
                                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Feudal.getConfiguration().getConfig().getInt("goldcarrot.nightvisionticks"), 0));
                            }
                        }
                    }
                    if(!event.getPlayer().hasPotionEffect(PotionEffectType.SPEED)){
                        if(Feudal.getConfiguration().getConfig().getBoolean("goldcarrot.speed"))
                            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Feudal.getConfiguration().getConfig().getInt("goldcarrot.speedticks"), Feudal.getConfiguration().getConfig().getInt("goldcarrot.speedlevel")));
                    }

                    if(Feudal.getConfiguration().getConfig().getBoolean("goldcarrot.increasehealth")){
                        if(Utils.getHealth(event.getPlayer()) < Utils.getMaxHealth(event.getPlayer())){
                            double h = Utils.getHealth(event.getPlayer()) + ((Utils.getMaxHealth(event.getPlayer()) - Utils.getHealth(event.getPlayer())) * 0.10);
                            if(Utils.getHealth(event.getPlayer()) > 0 && h <= Utils.getMaxHealth(event.getPlayer())){
                                event.getPlayer().setHealth(h);
                            }
                        }
                    }

                }
            }else if(event.getItem() != null){
                ItemMeta meta = event.getItem().getItemMeta();
                if(meta != null && meta.getLore() != null) {
                    for(String s : meta.getLore()) {
                        if(s != null && s.startsWith(Feudal.getMessage("cookedby").replace("%n%", ""))) {
                            if(!event.getPlayer().hasPotionEffect(PotionEffectType.SPEED)) {
                                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1200, 0));
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    private static PotionEffect getPotionEffect(Player player, PotionEffectType type) {
        for(PotionEffect effect : player.getActivePotionEffects()) {
            if(effect.getType().equals(type)) {
                return effect;
            }
        }
        return null;
    }

    public static void use(PlayerInteractEvent event){
        if((event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) && event.getItem() != null && event.getItem().getType().equals(Material.COMPASS)){
            if(Feudal.getOnlinePlayers().containsKey(event.getPlayer().getUniqueId().toString())){
                User u = Feudal.getOnlinePlayers().get(event.getPlayer().getUniqueId().toString());
                if(u.getProfession() != null && Feudal.getOnlinePlayers().size() > 1){
                    Feudal.getTrackPlayers().add(new TrackPlayer(event.getPlayer(), (u.getProfession().getType().equals(Profession.Type.ASSASSIN))));
                }else{
                    event.getPlayer().sendMessage(Feudal.getMessage("noPlayersToTrack"));
                }
            }
        }
    }
}