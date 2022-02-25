package me.invertmc.user.classes;

import com.cryptomorin.xseries.XMaterial;
import me.invertmc.Feudal;
import me.invertmc.kingdoms.Kingdom;
import me.invertmc.user.User;
import me.invertmc.user.classes.Profession;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Extra {

    @SuppressWarnings("deprecation")
    public static void interact(PlayerInteractEvent event) {
        if(!event.isCancelled()) {
            if(event.getClickedBlock() != null) {
                if (event.getClickedBlock().getType() == XMaterial.FARMLAND.parseMaterial() && event.getAction() == Action.PHYSICAL) {
                    final User user = Feudal.getAPI().getUser(event.getPlayer().getUniqueId());
                    if(user != null && user.getProfession() != null && user.getProfession().getType() == Profession.Type.FARMER) {
                        event.setCancelled(true);
                    }
                }else if (event.getAction() == Action.RIGHT_CLICK_BLOCK &&
                        (event.getClickedBlock().getType() == XMaterial.WHEAT.parseMaterial() || event.getClickedBlock().getType() == XMaterial.CARROT.parseMaterial() ||
                                event.getClickedBlock().getType() == XMaterial.POTATO.parseMaterial()) || event.getClickedBlock().getType() == XMaterial.BEETROOTS.parseMaterial()) {
                    ItemStack hand = Feudal.getItemInHand(event.getPlayer());
                    if(hand != null && hand.getType() == XMaterial.INK_SAC.parseMaterial() && hand.getDurability() == 15) {//bone meal
                        final User user = Feudal.getAPI().getUser(event.getPlayer().getUniqueId());
                        if(user != null && user.getProfession() != null && user.getProfession().getType() == Profession.Type.FARMER) {
                            boolean canMeal = false;
                            int nextDur = 0;
                            final Material material = event.getClickedBlock().getType();
                            final int data = event.getClickedBlock().getData();

                            if(material == XMaterial.BEETROOTS.parseMaterial() && data < 3) {
                                canMeal = true;
                                nextDur = 3;
                            }else if(data < 7) {
                                canMeal = true;
                                nextDur = 7;
                            }

                            if(canMeal) {
                                if(hand.getAmount() > 1) {
                                    hand.setAmount(hand.getAmount() - 1);
                                }else {
                                    hand = XMaterial.AIR.parseItem();
                                }
                                event.getClickedBlock().setData((byte) nextDur);
                                Feudal.setItemInHand(event.getPlayer(), hand);
                            }
                        }
                    }
                }else if(event.getClickedBlock().getType() == XMaterial.ANVIL.parseMaterial()) {
                    final User user = Feudal.getAPI().getUser(event.getPlayer().getUniqueId());
                    if(user != null && user.getProfession() != null && user.getProfession().getType() == Profession.Type.BLACKSMITH) {
                        final Kingdom king = Feudal.getAPI().getKingdom(event.getClickedBlock().getLocation());
                        if(king != null && king.isMember(user.getUUID())) {
                            byte b = event.getClickedBlock().getData();
                            if(b > 3) {
                                if(b == 4 || b == 8) {
                                    b = 0;
                                }else if(b == 5 || b == 9) {
                                    b = 1;
                                }else if(b == 6 || b == 10) {
                                    b = 2;
                                }else if(b == 7 || b == 11) {
                                    b = 3;
                                }
                                event.getClickedBlock().setData(b);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void damage(EntityDamageByEntityEvent event) {
        if(!event.isCancelled()) {
            Player player = null;
            if(event.getDamager() instanceof Player) {
                player = (Player) event.getDamager();
            }else if(event.getDamager() instanceof Projectile) {
                final Projectile pro = (Projectile) event.getDamager();
                if(pro.getShooter() instanceof Player) {
                    player = (Player) pro.getShooter();
                }
            }

            if(player != null) {
                final User u = Feudal.getAPI().getUser(player.getUniqueId());
                if(u != null && u.getProfession() != null) {
                    final EntityType type = event.getEntityType();

                    boolean speed = false;
                    if(u.getProfession().getType().equals(Profession.Type.HUNTER)){
                        if(type.equals(EntityType.CHICKEN) || type.equals(EntityType.COW) || type.equals(EntityType.HORSE)
                                || type.equals(EntityType.MUSHROOM_COW) || type.equals(EntityType.PIG) || type.equals(EntityType.SHEEP) || Utils.isType(type, "LLAMA")){
                            speed = true;
                        }else if(type.equals(EntityType.BAT) || type.equals(EntityType.OCELOT) || type.equals(EntityType.SQUID) || type.equals(EntityType.WOLF) || (!Feudal.getVersion().equals("1.7") && type.equals(EntityType.valueOf("RABBIT"))) ||
                                (Feudal.getVersion().contains("1.12") ? type.equals(EntityType.valueOf("PARROT")) : false)
                        ){
                            speed = true;
                        }
                    }

                    if(speed) {
                        if(!player.hasPotionEffect(PotionEffectType.SPEED)) {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 140, 0));
                        }
                    }
                }
            }
        }
    }
}