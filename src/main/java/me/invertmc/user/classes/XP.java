package me.invertmc.user.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cryptomorin.xseries.XMaterial;
import me.invertmc.Feudal;
import me.invertmc.api.events.XPGainEvent;
import me.invertmc.kingdoms.Kingdom;
import me.invertmc.kingdoms.Land;
import me.invertmc.kingdoms.Rank;
import me.invertmc.user.User;
import me.invertmc.user.attributes.Attributes;
import me.invertmc.user.attributes.Luck;
import me.invertmc.utils.Utils;
import me.invertmc.utils.WGUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;

public class XP {
    private static HashMap<Location, String> brewing = new HashMap<>();

    /**
     * Add xp to a user.
     * @param u The user
     * @param xp Xp being added
     */
    public static void addXP(User u, int xp) {
        if(u != null && u.getPlayer() != null){
            final String world = u.getPlayer().getWorld().getName();
            for(final String s : Feudal.getConfiguration().getConfig().getStringList("bannedWorlds")){
                if(world.equalsIgnoreCase(s)){
                    return;
                }
            }
        }

        final XPGainEvent event = new XPGainEvent(u, xp);
        Bukkit.getPluginManager().callEvent(event);
        if(event.isCancelled()){
            return;
        }else{
            xp = event.getXp();
        }

        if(u.getProfession() != null && u.getLuck() != null && u.getSpeed() != null && u.getStamina() != null && u.getStrength() != null && u.getToughness() != null){
            if(!u.getProfession().isMax()){
                final int level = u.getProfession().getLevel();
                final int i = u.getProfession().addXp(xp, u);
                if(i == 0){
                    if(u.getPlayer() != null && u.getPlayer().isOnline() && ((u.getProfession().getLevel() % Feudal.getConfiguration().getConfig().getInt("Profession.NotifyLevelUpPer")) == 0 || (u.getProfession().getLevel() - level >= Feudal.getConfiguration().getConfig().getInt("Profession.NotifyLevelUpPer")))){
                        u.getPlayer().sendMessage(Feudal.getMessage("xp.p.level").replace("%profession%", u.getProfession().getType().getNameLang()).replace("%level%", u.getProfession().getLevel() + "").replace("%max-level%", u.getProfession().getMaxLevel() + ""));
                    }
                }else if(i == 1){
                    if(u.getPlayer() != null && u.getPlayer().isOnline()){
                        u.getPlayer().sendMessage(Feudal.getMessage("xp.p.max").replace("%profession%", u.getProfession().getType().getNameLang()).replace("%level%", u.getProfession().getLevel() + ""));
                        u.getPlayer().sendMessage(Feudal.getMessage("xp.p.change"));
                        u.effectReputation(Feudal.getConfiguration().getConfig().getInt("reputation.maxProfession"), Feudal.getMessage("reputation.maxP"));
                    }
                    u.getProfession().setMax(true);
                }
            }

            final Profession.Type type = u.getProfession().getType();

            double money = type.getMoneyXP();
            if(money > 0) {
                money *= xp;
                u.addMoney(money);
            }

            if(type.equals(Profession.Type.MINER) || type.equals(Profession.Type.COOK) || type.equals(Profession.Type.FISHER) || type.equals(Profession.Type.SCRIBE) || type.equals(Profession.Type.ASSASSIN) ||
                    type.equals(Profession.Type.HEALER) || type.equals(Profession.Type.MERCHANT) || type.equals(Profession.Type.BARON) || type.equals(Profession.Type.KING) || type.equals(Profession.Type.ARCHBISHOP)){
                if(!u.getLuck().isMax()){
                    final int level = u.getLuck().getLevel();
                    final int i = u.getLuck().addXp(xp);
                    if(i == 0){
                        if(u.getPlayer() != null && u.getPlayer().isOnline() &&
                                (((u.getLuck().getLevel() % Feudal.getConfiguration().getConfig().getInt("Luck.NotifyLevelUpPer")) == 0 || u.getLuck().getLevel() == 1) || (u.getLuck().getLevel() - level >= Feudal.getConfiguration().getConfig().getInt("Luck.NotifyLevelUpPer")))){

                            u.getPlayer().sendMessage(Feudal.getMessage("xp.a.level").replace("%attribute%", u.getLuck().getName()).replace("%level%", u.getLuck().getLevel() + "").replace("%max-level%", u.getLuck().getMaxLevel() + ""));
                        }
                    }else if(i == 1){
                        if(u.getPlayer() != null && u.getPlayer().isOnline()){
                            u.getPlayer().sendMessage(Feudal.getMessage("xp.a.max").replace("%attribute%", u.getLuck().getName()).replace("%level%", u.getLuck().getLevel() + ""));
                        }
                        u.getLuck().setMax(true);
                    }
                }
            }

            if(type.equals(Profession.Type.FARMER) || type.equals(Profession.Type.HUNTER) || type.equals(Profession.Type.COOK) || type.equals(Profession.Type.BUILDER) || type.equals(Profession.Type.SHEPHERD) ||
                    type.equals(Profession.Type.ALCHEMIST) || type.equals(Profession.Type.BLACKSMITH) || type.equals(Profession.Type.HEALER) || type.equals(Profession.Type.MERCHANT) ||
                    type.equals(Profession.Type.BARON) || type.equals(Profession.Type.KING) || type.equals(Profession.Type.ARCHBISHOP)){
                if(!u.getStamina().isMax()){
                    final int level = u.getStamina().getLevel();
                    final int i = u.getStamina().addXp(xp);
                    if(i == 0){
                        if(u.getPlayer() != null && u.getPlayer().isOnline() &&
                                (((u.getStamina().getLevel() % Feudal.getConfiguration().getConfig().getInt("Stamina.NotifyLevelUpPer")) == 0 || u.getStamina().getLevel() == 1) || (u.getStamina().getLevel() - level >= Feudal.getConfiguration().getConfig().getInt("Stamina.NotifyLevelUpPer")))){
                            u.getPlayer().sendMessage(Feudal.getMessage("xp.a.level").replace("%attribute%", u.getStamina().getName()).replace("%level%", u.getStamina().getLevel() + "").replace("%max-level%", u.getStamina().getMaxLevel() + ""));
                        }
                    }else if(i == 1){
                        if(u.getPlayer() != null && u.getPlayer().isOnline()){
                            u.getPlayer().sendMessage(Feudal.getMessage("xp.a.max").replace("%attribute%", u.getStamina().getName()).replace("%level%", u.getStamina().getLevel() + ""));
                        }
                        u.getStamina().setMax(true);
                    }
                }
            }

            if(type.equals(Profession.Type.HUNTER) || type.equals(Profession.Type.ASSASSIN) || type.equals(Profession.Type.ALCHEMIST) || type.equals(Profession.Type.SQUIRE) || type.equals(Profession.Type.BARON) ||
                    type.equals(Profession.Type.KING) || type.equals(Profession.Type.ARCHBISHOP)){
                if(!u.getSpeed().isMax()){
                    final int level = u.getSpeed().getLevel();
                    final int i = u.getSpeed().addXp(xp);
                    if(i == 0){
                        if(u.getPlayer() != null && u.getPlayer().isOnline() &&
                                (((u.getSpeed().getLevel() % Feudal.getConfiguration().getConfig().getInt("Speed.NotifyLevelUpPer")) == 0 || u.getSpeed().getLevel() == 1) || (u.getSpeed().getLevel() - level >= Feudal.getConfiguration().getConfig().getInt("Speed.NotifyLevelUpPer")))){
                            u.getPlayer().sendMessage(Feudal.getMessage("xp.a.level").replace("%attribute%", u.getSpeed().getName()).replace("%level%", u.getSpeed().getLevel() + "").replace("%max-level%", u.getSpeed().getMaxLevel() + ""));
                        }
                    }else if(i == 1){
                        if(u.getPlayer() != null && u.getPlayer().isOnline()){
                            u.getPlayer().sendMessage(Feudal.getMessage("xp.a.max").replace("%attribute%", u.getSpeed().getName()).replace("%level%", u.getSpeed().getLevel() + ""));
                        }
                        u.getSpeed().setMax(true);
                    }
                }
            }

            if(type.equals(Profession.Type.LOGGER) || type.equals(Profession.Type.FISHER) || type.equals(Profession.Type.SHEPHERD) || type.equals(Profession.Type.SCRIBE) || type.equals(Profession.Type.GUARD) ||
                    type.equals(Profession.Type.SQUIRE) || type.equals(Profession.Type.KNIGHT) || type.equals(Profession.Type.KING) || type.equals(Profession.Type.BARON) || type.equals(Profession.Type.ARCHBISHOP)){
                if(!u.getToughness().isMax()){
                    final int level = u.getToughness().getLevel();
                    final int i = u.getToughness().addXp(xp);
                    if(i == 0){
                        if(u.getPlayer() != null && u.getPlayer().isOnline() &&
                                (((u.getToughness().getLevel() % Feudal.getConfiguration().getConfig().getInt("Toughness.NotifyLevelUpPer")) == 0 || u.getToughness().getLevel() == 1) || (u.getToughness().getLevel() - level >= Feudal.getConfiguration().getConfig().getInt("Toughness.NotifyLevelUpPer")))){
                            u.getPlayer().sendMessage(Feudal.getMessage("xp.a.level").replace("%attribute%", u.getToughness().getName()).replace("%level%", u.getToughness().getLevel() + "").replace("%max-level%", u.getToughness().getMaxLevel() + ""));
                        }
                    }else if(i == 1){
                        if(u.getPlayer() != null && u.getPlayer().isOnline()){
                            u.getPlayer().sendMessage(Feudal.getMessage("xp.a.max").replace("%attribute%", u.getToughness().getName()).replace("%level%", u.getToughness().getLevel() + ""));
                        }
                        u.getToughness().setMax(true);
                    }
                }
            }

            if(type.equals(Profession.Type.FARMER) || type.equals(Profession.Type.LOGGER) || type.equals(Profession.Type.MINER) || type.equals(Profession.Type.BUILDER) || type.equals(Profession.Type.GUARD) ||
                    type.equals(Profession.Type.BLACKSMITH) || type.equals(Profession.Type.KNIGHT) || type.equals(Profession.Type.KING) || type.equals(Profession.Type.BARON) || type.equals(Profession.Type.ARCHBISHOP)){
                if(!u.getStrength().isMax()){
                    final int level = u.getStrength().getLevel();
                    final int i = u.getStrength().addXp(xp);
                    if(i == 0){
                        if(u.getPlayer() != null && u.getPlayer().isOnline() &&
                                (((u.getStrength().getLevel() % Feudal.getConfiguration().getConfig().getInt("Strength.NotifyLevelUpPer")) == 0 || u.getStrength().getLevel() == 1) || (u.getStrength().getLevel() - level >= Feudal.getConfiguration().getConfig().getInt("Strength.NotifyLevelUpPer")))){
                            u.getPlayer().sendMessage(Feudal.getMessage("xp.a.level").replace("%attribute%", u.getStrength().getName()).replace("%level%", u.getStrength().getLevel() + "").replace("%max-level%", u.getStrength().getMaxLevel() + ""));
                        }
                    }else if(i == 1){
                        if(u.getPlayer() != null && u.getPlayer().isOnline()){
                            u.getPlayer().sendMessage(Feudal.getMessage("xp.a.max").replace("%attribute%", u.getStrength().getName()).replace("%level%", u.getStrength().getLevel() + ""));
                        }
                        u.getStrength().setMax(true);
                    }
                }
            }

            u.effectAttributes();
        }
    }

    public static void blockBreak(BlockBreakEvent event){//Farmer, logger, miner
        if(!event.isCancelled()){
            if(event.getBlock() != null){
                if(Feudal.getOnlinePlayers().containsKey(event.getPlayer().getUniqueId().toString())){
                    final User u = Feudal.getOnlinePlayers().get(event.getPlayer().getUniqueId().toString());
                    if(u.getProfession().getType().equals(Profession.Type.FARMER)){
                        final Material m = event.getBlock().getType();
                        final Block block = event.getBlock();
                        final short dmg = event.getBlock().getData();
                        if((m.equals(Material.WHEAT) && dmg > 6) || (m.equals(Material.CARROT) && dmg > 1) ||
                                (m.equals(Material.POTATO) && dmg > 1) || (m.equals(Material.BEETROOT) && dmg > 2)){
                            XP.addXP(u, Feudal.getConfiguration().getConfig().getInt("Farmer.xpPerCrop"));

                            Bukkit.getScheduler().scheduleSyncDelayedTask(Feudal.getPlugin(), () -> {
                                if(block.getType() == XMaterial.AIR.parseMaterial()) {
                                    block.setType(m);
                                    //	block.setData((byte) 0);
                                }
                            }, 60);
                        }
                    }else if(u.getProfession().getType().equals(Profession.Type.LOGGER)){
                        final Material m = event.getBlock().getType();
                        if(m.equals(XMaterial.OAK_LOG.parseMaterial()) || m.equals(XMaterial.SPRUCE_LOG.parseMaterial()) || m.equals(XMaterial.BIRCH_LOG.parseMaterial()) || m.equals(XMaterial.DARK_OAK_LOG.parseMaterial())
                                || m.equals(XMaterial.ACACIA_LOG.parseMaterial()) || m.equals(XMaterial.JUNGLE_LOG.parseMaterial())){
                            XP.addXP(u, Feudal.getConfiguration().getConfig().getInt("Logger.xpPerLog"));

                            final double luckDecimal = Attributes.getLuck(u.getLuck().getLevel());
                            if(Luck.chance(luckDecimal * (Feudal.getConfiguration().getConfig().getDouble("Luck.Effect.doubleLogs")/100))) {
                                event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), new ItemStack(m, 1, (short) (event.getBlock().getData()%4)));
                                event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), new ItemStack(m, 1, (short) (event.getBlock().getData()%4)));
                            }


                            if(System.currentTimeMillis() % 4 == 0) {
                                final ItemStack hand = Feudal.getItemInHand(event.getPlayer());
                                if(hand != null && hand.getType() != null && hand.getDurability() > 0) {
                                    final Material type = hand.getType();
                                    if(type == XMaterial.DIAMOND_AXE.parseMaterial() || type == XMaterial.GOLDEN_AXE.parseMaterial() ||
                                            type == XMaterial.IRON_AXE.parseMaterial() || type == XMaterial.STONE_AXE.parseMaterial() ||
                                            type == XMaterial.WOODEN_AXE.parseMaterial()) {
                                        hand.setDurability((short) (hand.getDurability() - 1));
                                        Feudal.setItemInHand(event.getPlayer(), hand);
                                    }
                                }
                            }

                        }
                    }else if(u.getProfession().getType().equals(Profession.Type.MINER)){
                        boolean silk = false;
                        if(Feudal.getItemInHand(event.getPlayer()) != null){
                            if(Feudal.getItemInHand(event.getPlayer()).containsEnchantment(Enchantment.SILK_TOUCH)){
                                silk = true;
                            }
                        }
                        final Material m = event.getBlock().getType();
                        if(m.equals(Material.COAL_ORE) && !silk){
                            XP.addXP(u, Feudal.getConfiguration().getConfig().getInt("Miner.xpPerCoal"));
                        }else if(m.equals(Material.IRON_ORE)){
                            XP.addXP(u, Feudal.getConfiguration().getConfig().getInt("Miner.xpPerIron"));
                            event.setCancelled(true);
                            event.getBlock().setType(Material.AIR);
                            final Item item = event.getBlock().getWorld().dropItem(event.getPlayer().getLocation(), new ItemStack(Material.IRON_INGOT, 1));
                            item.setPickupDelay(0);
                        }else if(m.equals(Material.GOLD_ORE)){
                            XP.addXP(u, Feudal.getConfiguration().getConfig().getInt("Miner.xpPerGold"));
                            event.setCancelled(true);
                            event.getBlock().setType(Material.AIR);
                            final Item item = event.getBlock().getWorld().dropItem(event.getPlayer().getLocation(), new ItemStack(Material.GOLD_INGOT, 1));
                            item.setPickupDelay(0);
                        }else if(m.equals(Material.EMERALD_ORE) && !silk){
                            XP.addXP(u, Feudal.getConfiguration().getConfig().getInt("Miner.xpPerEmerald"));
                        }else if(m.equals(Material.DIAMOND_ORE) && !silk){
                            XP.addXP(u, Feudal.getConfiguration().getConfig().getInt("Miner.xpPerDiamond"));
                        }else if(m.equals(Material.LAPIS_ORE) && !silk){
                            XP.addXP(u, Feudal.getConfiguration().getConfig().getInt("Miner.xpPerLapis"));
                        }else if((m.equals(Material.REDSTONE_ORE) || m.equals(Material.REDSTONE_ORE)) && !silk){
                            XP.addXP(u, Feudal.getConfiguration().getConfig().getInt("Miner.xpPerRedstone"));
                        }else if(m.equals(XMaterial.NETHER_QUARTZ_ORE.parseItem()) && !silk){
                            XP.addXP(u, Feudal.getConfiguration().getConfig().getInt("Miner.xpPerQuartz"));
                        }
                    }
                }
            }
        }
    }

    public static void entityDeathEvent(EntityDeathEvent event){//hunter, sheperd, guard, assassin, king, knight, squire
        if(Utils.getHealth(event.getEntity()) > 1){
            return;
        }
        if(event.getEntity().getKiller() != null && event.getEntity().getKiller().isOnline()){
            final Player p = event.getEntity().getKiller();

            if(Feudal.getOnlinePlayers().containsKey(p.getUniqueId().toString())){
                final User u = Feudal.getOnlinePlayers().get(p.getUniqueId().toString());
                final EntityType type = event.getEntityType();
                if(u.getProfession().getType().equals(Profession.Type.HUNTER)){
                    if(type.equals(EntityType.CHICKEN) || type.equals(EntityType.COW) || type.equals(EntityType.HORSE)
                            || type.equals(EntityType.MUSHROOM_COW) || type.equals(EntityType.PIG) || type.equals(EntityType.SHEEP) || isType(type, "LLAMA")){
                        if(XP.can(p,event.getEntity().getLocation()))XP.addXP(u, Feudal.getConfiguration().getConfig().getInt("Hunter.xpPerAnimal"));
                    }else if(type.equals(EntityType.BAT) || type.equals(EntityType.OCELOT) || type.equals(EntityType.SQUID) || type.equals(EntityType.WOLF) || (!Feudal.getVersion().equals("1.7") && type.equals(EntityType.valueOf("RABBIT"))) ||
                            (Feudal.getVersion().contains("1.12") ? type.equals(EntityType.valueOf("PARROT")) : false)
                    ){
                        if(XP.can(p,event.getEntity().getLocation()))XP.addXP(u, Feudal.getConfiguration().getConfig().getInt("Hunter.xpPerAnimalPlus"));
                    }
                }else if(u.getProfession().getType().equals(Profession.Type.SHEPHERD)){
                    if(type.equals(EntityType.CHICKEN) || type.equals(EntityType.COW) || type.equals(EntityType.HORSE)
                            || type.equals(EntityType.MUSHROOM_COW) || type.equals(EntityType.PIG) ||
                            (!Feudal.getVersion().equals("1.7") && type.equals(EntityType.valueOf("RABBIT"))) || type.equals(EntityType.SHEEP) || isType(type, "LLAMA")){
                        if(XP.can(p,event.getEntity().getLocation()))XP.addXP(u, Feudal.getConfiguration().getConfig().getInt("Shepherd.xpPerAnimalKill"));
                    }
                }else if(u.getProfession().getType().equals(Profession.Type.GUARD) || u.getProfession().getType().equals(Profession.Type.SQUIRE)
                        || u.getProfession().getType().equals(Profession.Type.KNIGHT) || u.getProfession().getType().equals(Profession.Type.BARON) ||
                        u.getProfession().getType().equals(Profession.Type.KING) || type.equals(Profession.Type.ARCHBISHOP)){
                    if(type.equals(EntityType.PLAYER)){
                        int xp = 0;

                        final Profession.Type t = u.getProfession().getType();
                        if(t.equals(Profession.Type.GUARD) || t.equals(Profession.Type.SQUIRE)){
                            if(!u.getKingdomUUID().equals("")){
                                final Kingdom k = Feudal.getLandKingdom(new Land(u.getPlayer().getLocation()));
                                final Kingdom pk = Feudal.getKingdom(u.getKingdomUUID());
                                if(k != null && k.getUUID().equals(u.getKingdomUUID())){
                                    if(t.equals(Profession.Type.GUARD)){
                                        xp = Feudal.getConfiguration().getConfig().getInt("Guard.xpPerPlayerKill");
                                    }else{
                                        xp = Feudal.getConfiguration().getConfig().getInt("Squire.xpPerPlayerKill");
                                    }
                                }else if(pk != null && k != null && pk.isEnemied(k)){
                                    if(t.equals(Profession.Type.SQUIRE)){
                                        xp = Feudal.getConfiguration().getConfig().getInt("Squire.xpPerPlayerKillOnEnemyLand");
                                    }
                                }
                            }
                        }else if(t.equals(Profession.Type.KNIGHT)){
                            xp = Feudal.getConfiguration().getConfig().getInt("Knight.xpPerPlayerKill");
                        }else if(t.equals(Profession.Type.KING)){
                            xp = Feudal.getConfiguration().getConfig().getInt("King.xpPerPlayerKill");
                        }else if(t.equals(Profession.Type.ARCHBISHOP)){
                            xp = Feudal.getConfiguration().getConfig().getInt("ArchBishop.xpPerPlayerKill");
                        }

                        if(xp != 0){
                            if(XP.can(p,event.getEntity().getLocation()))XP.addXP(u, xp);
                        }
                    }else if(type.equals(EntityType.BLAZE) || type.equals(EntityType.CAVE_SPIDER) || type.equals(EntityType.CREEPER) ||
                            type.equals(EntityType.ENDER_DRAGON) || type.equals(EntityType.ENDERMAN) || (!Feudal.getVersion().equals("1.7") && type.equals(EntityType.valueOf("ENDERMITE"))) ||
                            type.equals(EntityType.GHAST) || type.equals(EntityType.GIANT) || (!Feudal.getVersion().equals("1.7") && type.equals(EntityType.valueOf("GUARDIAN"))) ||
                            type.equals(EntityType.MAGMA_CUBE) || type.equals(EntityType.ZOMBIFIED_PIGLIN) || type.equals(EntityType.SILVERFISH) ||
                            type.equals(EntityType.SKELETON) || type.equals(EntityType.ZOMBIE) || type.equals(EntityType.SLIME) ||
                            type.equals(EntityType.SPIDER) || type.equals(EntityType.WITHER) ||
                            (Feudal.getVersion().contains("1.11") || Feudal.getVersion().contains("1.12") ? (type.equals(EntityType.valueOf("VEX")) || type.equals(EntityType.valueOf("EVOKER")) || type.equals(EntityType.valueOf("VINDICATOR"))) : false) ||
                            (Feudal.getVersion().contains("1.12") ? isType(type, "ILLUSIONER") : false)
                    ){
                        int xp = 0;

                        if(u.getProfession().getType().equals(Profession.Type.GUARD) || u.getProfession().getType().equals(Profession.Type.SQUIRE) || u.getProfession().getType().equals(Profession.Type.KNIGHT) || u.getProfession().getType().equals(Profession.Type.BARON)){
                            if(!u.getKingdomUUID().equals("")){
                                final Kingdom k = Feudal.getLandKingdom(new Land(u.getPlayer().getLocation()));
                                boolean go = true;
                                if(u.getProfession().getType().equals(Profession.Type.BARON) && k != null){
                                    User leader = null;
                                    for(final String s : k.getMembers().keySet()){
                                        if(k.getMembers().get(s).equals(Rank.LEADER)){
                                            leader = Feudal.getUser(s);
                                        }
                                    }
                                    if(leader != null && leader.getUUID().equals(u.getUUID())){
                                        go = false;
                                    }
                                }
                                if(k != null && k.getUUID().equals(u.getKingdomUUID()) && go){
                                    xp = Feudal.getConfiguration().getConfig().getInt(u.getProfession().getType().getName() + ".xpPerMobKill");
                                }
                            }
                        }

                        if(xp != 0){

                            try{
                                if(type.equals(EntityType.WITHER)|| type.equals(EntityType.ENDER_DRAGON)){
                                    xp*=8;
                                }else if(isType(type, "VEX") || isType(type, "EVOKER") || isType(type, "VINDICATOR")){
                                    xp*=3;
                                }
                            }catch(final Exception e){
                                //do nothing
                            }

                            if(XP.can(p,event.getEntity().getLocation()))XP.addXP(u, xp);
                        }
                    }
                }else if(u.getProfession().getType().equals(Profession.Type.ASSASSIN)){
                    if(type.equals(EntityType.PLAYER)){
                        int xp = 0;

                        final Player target = (Player) event.getEntity();
                        boolean onLand = false;
                        final User tar = Feudal.getUser(target.getUniqueId().toString());
                        if(tar != null && !tar.getKingdomUUID().equals("")){
                            final Kingdom k = Feudal.getLandKingdom(new Land(target.getLocation()));
                            if(k != null && k.getUUID().equals(tar.getKingdomUUID())){
                                onLand = true;
                            }
                        }

                        if(onLand){
                            xp = Feudal.getConfiguration().getConfig().getInt("Assassin.xpPerPlayerKillOnEnemyLand");
                        }else{
                            xp = Feudal.getConfiguration().getConfig().getInt("Assassin.xpPerPlayerKill");
                        }

                        if(xp != 0){
                            if(XP.can(p,event.getEntity().getLocation()))XP.addXP(u, xp);
                        }
                    }
                }
            }
        }

        if(event.getEntity() instanceof Player){
            final Player p = (Player) event.getEntity();
            final User u = Feudal.getUser(p.getUniqueId().toString());
            if(u != null){
                if((!Bukkit.getPluginManager().isPluginEnabled("WorldGuard") || !WGUtils.hasWG()) || WGUtils.canBuild(p, p.getLocation())){
                    if(u.getProfession() != null){
                        u.getProfession().resetXP();
                    }
                    if(u.getStrength() != null){
                        u.getStrength().resetXP();
                    }
                    if(u.getToughness() != null){
                        u.getToughness().resetXP();
                    }
                    if(u.getSpeed() != null){
                        u.getSpeed().resetXP();
                    }
                    if(u.getLuck() != null){
                        u.getLuck().resetXP();
                    }
                    if(u.getStamina() != null){
                        u.getStamina().resetXP();
                    }
                    if(p.getKiller() != null && p.getKiller() instanceof Player){
                        double percent = Feudal.getConfiguration().getConfig().getDouble("moneyPercentLostOnDeath") / 100;
                        final User killer = Feudal.getUser(p.getKiller().getUniqueId().toString());
                        if(killer != null){
                            if(killer.getProfession() != null && killer.getProfession().getType().equals(Profession.Type.ASSASSIN)){
                                percent = Feudal.getConfiguration().getConfig().getDouble("moneyPercentLostOnDeathByAssassinKiller") / 100;
                            }
                            if(percent > 0){
                                double remove = u.getBalance() * percent;
                                double toKingdom = 0;
                                u.removeMoney(remove);
                                Kingdom k = null;
                                if(!killer.getKingdomUUID().equals("")){
                                    k = Feudal.getKingdom(killer.getKingdomUUID());
                                    if(k != null){
                                        toKingdom = remove * k.getIncomeTax(killer.getUUID());
                                        remove-=toKingdom;
                                    }
                                }
                                if(toKingdom > 0 && k != null){
                                    k.setTreasury(toKingdom + k.getTreasury());
									/*try {
										k.save();
									} catch (Exception e) {
										ErrorManager.error(93, e);
									}*/
                                }
                                killer.addMoney(remove);
                                p.sendMessage(Feudal.getMessage("xp.death.loseMoney").replace("%money%", Feudal.round(remove) + ""));
                                if(killer.getPlayer() != null && killer.getPlayer().isOnline()){
                                    if(toKingdom > 0){
                                        killer.getPlayer().sendMessage(Feudal.getMessage("xp.death.earnMoney").replace("%player%", p.getName()).replace("%money%", Feudal.round(remove) + "").replace("%kingdom-money%", Feudal.round(toKingdom) + ""));
                                    }else{
                                        killer.getPlayer().sendMessage(Feudal.getMessage("xp.death.earnMoney").replace("%player%", p.getName()).replace("%money%", Feudal.round(remove) + ""));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean isType(EntityType type, String name) {
        try {
            if(type.equals(EntityType.valueOf(name.toUpperCase()))) {
                return true;
            }else {
                return false;
            }
        }catch(final Exception e) {
            return false;
        }
    }

    public static void inventoryClick(InventoryClickEvent event){//Cook, alchemist, healer
        if(event.isCancelled() || !(event.getWhoClicked() instanceof Player)){
            return;
        }
        if(event.getInventory().getType().equals(InventoryType.WORKBENCH) && event.getSlotType().equals(SlotType.RESULT)){
            if(event.getCursor() != null && !event.getCursor().getType().equals(Material.AIR)){
                if(event.getCurrentItem() != null && event.getCurrentItem().getType().equals(event.getCursor().getType())){
                    if(event.getCursor().getAmount() + event.getCurrentItem().getAmount() > event.getCursor().getMaxStackSize()){
                        event.setCancelled(true);
                    }
                }else{
                    if(!event.isShiftClick()){
                        event.setCancelled(true);
                    }
                }
            }
        }else if(event.getInventory().getType().equals(InventoryType.FURNACE) && event.getSlotType().equals(SlotType.RESULT) && event.getSlot() == 2){
            if(event.getCurrentItem() != null){
                final Material m = event.getCurrentItem().getType();
                if(event.getCursor() != null && !event.getCursor().getType().equals(Material.AIR)){
                    if(event.getCurrentItem() != null && event.getCurrentItem().getType().equals(event.getCursor().getType())){
                        if(event.getCursor().getAmount() + event.getCurrentItem().getAmount() >= event.getCursor().getMaxStackSize()){
                            event.setCancelled(true);
                            return;
                        }
                    }else{
                        event.setCancelled(true);
                        return;
                    }
                }
                if(event.getWhoClicked().getInventory().firstEmpty() == -1){
                    event.setCancelled(true);
                    return;
                }
                if(m.equals(XMaterial.COOKED_BEEF.parseItem()) || m.equals(XMaterial.COOKED_CHICKEN.parseItem()) || m.equals(XMaterial.COOKED_COD.parseItem())
                        || (!Feudal.getVersion().equals("1.7") && m.equals(XMaterial.COOKED_MUTTON.parseItem())) || (!Feudal.getVersion().equals("1.7") && m.equals(XMaterial.COOKED_RABBIT.parseItem())) || m.equals(XMaterial.BAKED_POTATO.parseItem())
                        || m.equals(XMaterial.COOKED_PORKCHOP.parseItem())){
                    if(Feudal.getOnlinePlayers().containsKey(event.getWhoClicked().getUniqueId().toString())){
                        final User u = Feudal.getOnlinePlayers().get(event.getWhoClicked().getUniqueId().toString());
                        if(u.getProfession().getType().equals(Profession.Type.COOK)){
                            if(event.getAction().equals(InventoryAction.PICKUP_ALL) || event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)){
                                transformCookItem(event, (Player) event.getWhoClicked());
                                XP.addXP(u, (Feudal.getConfiguration().getConfig().getInt("Cook.xpPerFood")*event.getCurrentItem().getAmount()));
                                if(u.getPlayer() != null) {
                                    u.getPlayer().giveExp(event.getCurrentItem().getAmount() * 6);
                                }
                            }else if(event.getAction().equals(InventoryAction.PICKUP_ONE)){
                                transformCookItem(event, (Player) event.getWhoClicked());
                                XP.addXP(u, (Feudal.getConfiguration().getConfig().getInt("Cook.xpPerFood")));
                                if(u.getPlayer() != null) {
                                    u.getPlayer().giveExp(6);
                                }
                            }else{
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }else if(event.getInventory().getType().equals(InventoryType.BREWING)){
            if(event.getWhoClicked().getInventory().firstEmpty() == -1){
                event.setCancelled(true);
                return;
            }
            final User u = Feudal.getUser(event.getWhoClicked().getUniqueId().toString());
            if(u != null && u.getProfession() != null && (u.getProfession().getType().equals(Profession.Type.ALCHEMIST) || u.getProfession().getType().equals(Profession.Type.HEALER))){
                if(event.getSlotType().equals(SlotType.FUEL) && (event.getSlot() == 3)){
                    if(event.getAction().equals(InventoryAction.PLACE_ALL) || event.getAction().equals(InventoryAction.PLACE_ONE) || event.getAction().equals(InventoryAction.PLACE_SOME)){
                        Location loc = null;
                        if(event.getInventory().getHolder() != null && event.getInventory().getHolder() instanceof BrewingStand){
                            loc = ((BrewingStand)event.getInventory().getHolder()).getLocation();
                        }
                        if(loc != null){
                            XP.brewing.put(loc, event.getWhoClicked().getUniqueId().toString());
                        }
                    }
                }else if(event.getSlotType().equals(SlotType.CRAFTING) && (event.getSlot() == 0 || event.getSlot() == 1 || event.getSlot() == 2)){
                    final ItemStack ing = ((BrewerInventory)event.getInventory()).getIngredient();
                    if(ing != null && !ing.getType().equals(Material.AIR)){
                        Location loc = null;
                       if(event.getInventory().getHolder() != null && event.getInventory().getHolder() instanceof BrewingStand){
                            loc = ((BrewingStand)event.getInventory().getHolder()).getLocation();
                        }
                        if(loc != null){
                            XP.brewing.put(loc, event.getWhoClicked().getUniqueId().toString());
                        }
                    }
                }else if((event.getSlotType().equals(SlotType.CONTAINER) || event.getSlotType().equals(SlotType.QUICKBAR)) && event.isShiftClick()){
                    final BrewerInventory inventory = (BrewerInventory) event.getInventory();
                    if(inventory.getIngredient() == null || inventory.getIngredient().getType().equals(Material.AIR)){
                        if(event.getCurrentItem() != null && !event.getCurrentItem().getType().equals(Material.AIR)){
                            final Material t = event.getCurrentItem().getType();
                            if(t.equals(Material.FERMENTED_SPIDER_EYE) || t.equals(Material.NETHER_WART) || t.equals(Material.SUGAR) ||
                                    t.equals(Material.REDSTONE) || t.equals(Material.GLOWSTONE_DUST) || t.equals(Material.SPIDER_EYE) ||
                                    t.equals(Material.GLISTERING_MELON_SLICE) || t.equals(Material.GOLDEN_CARROT) || t.equals(Material.MAGMA_CREAM) ||
                                    (!Feudal.getVersion().equals("1.7") && t.equals(Material.valueOf("RABBIT_FOOT"))) || t.equals(Material.GUNPOWDER) || t.equals(Material.GHAST_TEAR) ||
                                    t.equals(Material.BLAZE_POWDER) || t.equals(Material.getMaterial("DRAGONS_BREATH")) || (t.equals(Material.COD) && event.getCurrentItem().getDurability() == 3)){
                                Location loc = null;
                                if(event.getInventory().getHolder() != null && event.getInventory().getHolder() instanceof BrewingStand){
                                    loc = ((BrewingStand)event.getInventory().getHolder()).getLocation();
                                }
                                if(loc != null){
                                    XP.brewing.put(loc, event.getWhoClicked().getUniqueId().toString());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static void transformCookItem(InventoryClickEvent event, final Player player) {
        final ItemStack item = event.getCurrentItem();
        if(item != null) {
            if(item.getItemMeta() != null) {
                final ItemMeta meta = item.getItemMeta();
                List<String> lore = meta.getLore();
                if(lore == null) {
                    lore = new ArrayList<>();
                }

                lore.add(Feudal.getMessage("cookedby").replace("%n%", player.getName()));
                meta.setLore(lore);
                item.setItemMeta(meta);
                event.setCurrentItem(item);
            }
        }
        player.updateInventory();
    }

    public static void craftItemEvent(CraftItemEvent event) {//cook, blacksmith, healer
        if(event.isCancelled()){
            return;
        }
        final Player p = (Player) event.getWhoClicked();
        if(event.getCurrentItem() == null){
            return;
        }
        if(Feudal.getOnlinePlayers().containsKey(p.getUniqueId().toString())){
            final User u = Feudal.getOnlinePlayers().get(p.getUniqueId().toString());
            int count = 1;
            if(event.isShiftClick()){
                count = getCount(event.getInventory(), p, event.getCurrentItem());
            }

            //1.4.9 update to stop drop bug
            if(event.getClick().equals(ClickType.DROP) || event.getClick().equals(ClickType.CONTROL_DROP)){
                event.setCancelled(true);
                return;
            }

            if(event.getClick().equals(ClickType.NUMBER_KEY)) {
                return;
            }

            if(event.getWhoClicked().getInventory().firstEmpty() == -1){
                event.setCancelled(true);
                return;
            }
            if(u.getProfession().getType().equals(Profession.Type.COOK)){
                final Material m = event.getCurrentItem().getType();
                final int amnt = event.getCurrentItem().getAmount();
                if(m.equals(Material.COOKIE) || m.equals(Material.BREAD)){
                    if((event.isShiftClick() && count == -1) || event.getAction().equals(InventoryAction.PICKUP_HALF) || event.getAction().equals(InventoryAction.PICKUP_SOME) || event.getAction().equals(InventoryAction.PICKUP_ONE)){
                        event.setCancelled(true);
                        return;
                    }
                    XP.addXP(u, (Feudal.getConfiguration().getConfig().getInt("Cook.xpPerCookie")*amnt*(count > 0 ? count : 1)));
                }else if((!Feudal.getVersion().equals("1.7") && m.equals(Material.valueOf("RABBIT_STEW"))) || m.equals(Material.MUSHROOM_STEW) || m.equals(Material.PUMPKIN_PIE) || m.equals(Material.getMaterial("BEETROOT_SOUP"))){
                    if((event.isShiftClick() && count == -1) || event.getAction().equals(InventoryAction.PICKUP_HALF) || event.getAction().equals(InventoryAction.PICKUP_SOME) || event.getAction().equals(InventoryAction.PICKUP_ONE)){
                        event.setCancelled(true);
                        return;
                    }
                    XP.addXP(u, (Feudal.getConfiguration().getConfig().getInt("Cook.xpPerSoup")*amnt*(count > 0 ? count : 1)));
                }else if(m.equals(Material.CAKE)){
                    if((event.isShiftClick() && count == -1) || event.getAction().equals(InventoryAction.PICKUP_HALF) || event.getAction().equals(InventoryAction.PICKUP_SOME) || event.getAction().equals(InventoryAction.PICKUP_ONE)){
                        event.setCancelled(true);
                        return;
                    }
                    XP.addXP(u, (Feudal.getConfiguration().getConfig().getInt("Cook.xpPerCake")*amnt*(count > 0 ? count : 1)));
                }
            }else if(u.getProfession().getType().equals(Profession.Type.BLACKSMITH)){
                final Material m = event.getCurrentItem().getType();
                final int amt = event.getCurrentItem().getAmount();
                int xp = 0;

                Material returnType = null;
                int returnAmt = 0;

                if(m.equals(Material.WOODEN_SHOVEL)){
                    xp = Feudal.getConfiguration().getConfig().getInt("Blacksmith.xp1Wood");
                }else if(m.equals(Material.WOODEN_HOE) || m.equals(Material.WOODEN_SWORD)){
                    xp = Feudal.getConfiguration().getConfig().getInt("Blacksmith.xp2Wood");
                    returnType = Material.OAK_WOOD;
                    returnAmt = 1;
                }else if(m.equals(Material.WOODEN_AXE) || m.equals(Material.WOODEN_PICKAXE)){
                    xp = Feudal.getConfiguration().getConfig().getInt("Blacksmith.xp3Wood");
                    returnType = Material.OAK_WOOD;
                    returnAmt = 1;
                }else if(m.equals(Material.GOLDEN_SHOVEL)){
                    xp = Feudal.getConfiguration().getConfig().getInt("Blacksmith.xp1Gold");
                }else if(m.equals(Material.GOLDEN_HOE) || m.equals(Material.GOLDEN_SWORD)){
                    xp = Feudal.getConfiguration().getConfig().getInt("Blacksmith.xp2Gold");
                    returnType = Material.GOLD_INGOT;
                    returnAmt = 1;
                }else if(m.equals(Material.GOLDEN_AXE) || m.equals(Material.GOLDEN_PICKAXE)){
                    xp = Feudal.getConfiguration().getConfig().getInt("Blacksmith.xp3Gold");
                    returnType = Material.GOLD_INGOT;
                    returnAmt = 1;
                }else if(m.equals(Material.IRON_SHOVEL)){
                    xp = Feudal.getConfiguration().getConfig().getInt("Blacksmith.xp1Iron");
                }else if(m.equals(Material.IRON_HOE) || m.equals(Material.IRON_SWORD)){
                    xp = Feudal.getConfiguration().getConfig().getInt("Blacksmith.xp2Iron");
                    returnType = Material.IRON_INGOT;
                    returnAmt = 1;
                }else if(m.equals(Material.IRON_AXE) || m.equals(Material.IRON_PICKAXE)){
                    xp = Feudal.getConfiguration().getConfig().getInt("Blacksmith.xp3Iron");
                    returnType = Material.IRON_INGOT;
                    returnAmt = 1;
                }else if(m.equals(Material.DIAMOND_SHOVEL)){
                    xp = Feudal.getConfiguration().getConfig().getInt("Blacksmith.xp1Diamond");
                }else if(m.equals(Material.DIAMOND_HOE) || m.equals(Material.DIAMOND_SWORD)){
                    xp = Feudal.getConfiguration().getConfig().getInt("Blacksmith.xp2Diamond");
                    returnType = Material.DIAMOND;
                    returnAmt = 1;
                }else if(m.equals(Material.DIAMOND_AXE) || m.equals(Material.DIAMOND_PICKAXE)){
                    xp = Feudal.getConfiguration().getConfig().getInt("Blacksmith.xp3Diamond");
                    returnType = Material.DIAMOND;
                    returnAmt = 1;
                }else if(m.equals(Material.BOW)){
                    xp = Feudal.getConfiguration().getConfig().getInt("Blacksmith.xpBow");
                    returnType = Material.STRING;
                    returnAmt = 1;
                }else if(m.equals(Material.ARROW)){
                    if((event.isShiftClick() && count == -1) || event.getAction().equals(InventoryAction.PICKUP_HALF) || event.getAction().equals(InventoryAction.PICKUP_SOME) || event.getAction().equals(InventoryAction.PICKUP_ONE)){
                        event.setCancelled(true);
                        return;
                    }
                    xp = Feudal.getConfiguration().getConfig().getInt("Blacksmith.xpArrow");
                }else if(m.equals(Material.LEATHER_BOOTS)){
                    xp = Feudal.getConfiguration().getConfig().getInt("Blacksmith.xp4Leather");
                    returnType = Material.LEATHER;
                    returnAmt = 2;
                }else if(m.equals(Material.LEATHER_HELMET)){
                    xp = Feudal.getConfiguration().getConfig().getInt("Blacksmith.xp5Leather");
                    returnType = Material.LEATHER;
                    returnAmt = 2;
                }else if(m.equals(Material.LEATHER_LEGGINGS)){
                    xp = Feudal.getConfiguration().getConfig().getInt("Blacksmith.xp7Leather");
                    returnType = Material.LEATHER;
                    returnAmt = 3;
                }else if(m.equals(Material.LEATHER_CHESTPLATE)){
                    xp = Feudal.getConfiguration().getConfig().getInt("Blacksmith.xp8Leather");
                    returnType = Material.LEATHER;
                    returnAmt = 4;
                }else if(m.equals(Material.GOLDEN_BOOTS)){
                    xp = Feudal.getConfiguration().getConfig().getInt("Blacksmith.xp4Gold");
                    returnType = Material.GOLD_INGOT;
                    returnAmt = 2;
                }else if(m.equals(Material.GOLDEN_HELMET)){
                    xp = Feudal.getConfiguration().getConfig().getInt("Blacksmith.xp5Gold");
                    returnType = Material.GOLD_INGOT;
                    returnAmt = 2;
                }else if(m.equals(Material.GOLDEN_LEGGINGS)){
                    xp = Feudal.getConfiguration().getConfig().getInt("Blacksmith.xp7Gold");
                    returnType = Material.GOLD_INGOT;
                    returnAmt = 3;
                }else if(m.equals(Material.GOLDEN_CHESTPLATE)){
                    xp = Feudal.getConfiguration().getConfig().getInt("Blacksmith.xp8Gold");
                    returnType = Material.GOLD_INGOT;
                    returnAmt = 4;
                }else if(m.equals(Material.CHAINMAIL_BOOTS)){
                    xp = Feudal.getConfiguration().getConfig().getInt("Blacksmith.xpChainBoots");
                }else if(m.equals(Material.CHAINMAIL_HELMET)){
                    xp = Feudal.getConfiguration().getConfig().getInt("Blacksmith.xpChainHat");
                }else if(m.equals(Material.CHAINMAIL_LEGGINGS)){
                    xp = Feudal.getConfiguration().getConfig().getInt("Blacksmith.xpChainLegs");
                }else if(m.equals(Material.CHAINMAIL_CHESTPLATE)){
                    xp = Feudal.getConfiguration().getConfig().getInt("Blacksmith.xpChainChest");
                }else if(m.equals(Material.IRON_BOOTS)){
                    xp = Feudal.getConfiguration().getConfig().getInt("Blacksmith.xp4Iron");
                    returnType = Material.IRON_INGOT;
                    returnAmt = 2;
                }else if(m.equals(Material.IRON_HELMET)){
                    xp = Feudal.getConfiguration().getConfig().getInt("Blacksmith.xp5Iron");
                    returnType = Material.IRON_INGOT;
                    returnAmt = 2;
                }else if(m.equals(Material.IRON_LEGGINGS)){
                    xp = Feudal.getConfiguration().getConfig().getInt("Blacksmith.xp7Iron");
                    returnType = Material.IRON_INGOT;
                    returnAmt = 3;
                }else if(m.equals(Material.IRON_CHESTPLATE)){
                    xp = Feudal.getConfiguration().getConfig().getInt("Blacksmith.xp8Iron");
                    returnType = Material.IRON_INGOT;
                    returnAmt = 4;
                }else if(m.equals(Material.DIAMOND_BOOTS)){
                    xp = Feudal.getConfiguration().getConfig().getInt("Blacksmith.xp4Diamond");
                    returnType = Material.DIAMOND;
                    returnAmt = 2;
                }else if(m.equals(Material.DIAMOND_HELMET)){
                    xp = Feudal.getConfiguration().getConfig().getInt("Blacksmith.xp5Diamond");
                    returnType = Material.DIAMOND;
                    returnAmt = 2;
                }else if(m.equals(Material.DIAMOND_LEGGINGS)){
                    xp = Feudal.getConfiguration().getConfig().getInt("Blacksmith.xp7Diamond");
                    returnType = Material.DIAMOND;
                    returnAmt = 3;
                }else if(m.equals(Material.DIAMOND_CHESTPLATE)){
                    xp = Feudal.getConfiguration().getConfig().getInt("Blacksmith.xp8Diamond");
                    returnType = Material.DIAMOND;
                    returnAmt = 4;
                }else if(m.equals(Material.SADDLE)){
                    xp = Feudal.getConfiguration().getConfig().getInt("Blacksmith.xpSaddle");
                }
                if(event.isShiftClick() && count == -1 && xp > 0){
                    event.setCancelled(true);
                    return;
                }
                if(xp > 0){

                    if(returnType != null && returnAmt > 0) {
                        final ItemStack returnItem = new ItemStack(returnType, returnAmt);
                        p.getLocation().getWorld().dropItem(p.getLocation(), returnItem);
                    }

                    xp *= amt;
                    xp *= (count > 0 ? count : 1);
                    XP.addXP(u, xp);
                }
            }else if(u.getProfession().getType().equals(Profession.Type.HEALER)){
                final Material m = event.getCurrentItem().getType();
                final int amt = event.getCurrentItem().getAmount();
                int xp = 0;

                if(m.equals(Material.GOLDEN_APPLE)){

                    if(event.getAction().equals(InventoryAction.PICKUP_HALF) || event.getAction().equals(InventoryAction.PICKUP_SOME) || event.getAction().equals(InventoryAction.PICKUP_ONE)){
                        event.setCancelled(true);
                        return;
                    }

                    final short data = event.getCurrentItem().getDurability();
                    if(data == 0){
                        xp = Feudal.getConfiguration().getConfig().getInt("Healer.xpPerGoldApple");
                        if(!event.isShiftClick() || count != -1){
                            final Item itemDrop = u.getPlayer().getWorld().dropItem(u.getPlayer().getEyeLocation(), (new ItemStack(Material.GOLD_INGOT, 4*count)));
                            itemDrop.setPickupDelay(0);
                        }
                    }else if(data == 1){
                        xp = Feudal.getConfiguration().getConfig().getInt("Healer.xpPerGodApple");
                        if(!event.isShiftClick() || count != -1){
                            final Item itemDrop = u.getPlayer().getWorld().dropItem(u.getPlayer().getEyeLocation(), (new ItemStack(Material.GOLD_BLOCK, 4*count)));
                            itemDrop.setPickupDelay(0);
                        }
                    }
                }else if(m.equals(Material.GOLDEN_CARROT)){

                    if((event.isShiftClick() && count == -1) || event.getAction().equals(InventoryAction.PICKUP_HALF) || event.getAction().equals(InventoryAction.PICKUP_SOME) || event.getAction().equals(InventoryAction.PICKUP_ONE)){
                        event.setCancelled(true);
                        return;
                    }

                    xp = Feudal.getConfiguration().getConfig().getInt("Healer.xpPerGoldCarrot");
                    if(count != -1){

                        final ItemStack stack = event.getCurrentItem();
                        final ItemMeta meta = stack.getItemMeta();
                        meta.setDisplayName("\u00a7bHealer Carrot");
                        stack.setItemMeta(meta);
                        event.setCurrentItem(stack);

                        if(event.isShiftClick()){//Used to fix shift click
                            int amount = Integer.MAX_VALUE;
                            for(int i = 1; i<=9; i++){
                                if(event.getInventory().getItem(i).getAmount() < amount){
                                    amount = event.getInventory().getItem(i).getAmount();
                                }
                            }
                            if(amount == Integer.MAX_VALUE){
                                amount = 0;
                            }
                            if(amount > 0){
                                stack.setAmount(amount);
                                u.getPlayer().getInventory().addItem(stack);
                                u.getPlayer().getInventory().addItem(new ItemStack(Material.GOLD_NUGGET, 4*amount));
                                event.getInventory().clear();
                                u.getPlayer().updateInventory();
                                XP.addXP(u, xp * amount);
                                event.setCancelled(true);
                            }
                            return;
                        }else{
                            u.getPlayer().getInventory().addItem(new ItemStack(Material.GOLD_NUGGET, 4));
                        }
                    }
                }

                if(xp > 0){
                    if(event.isShiftClick() && count == -1){
                        event.setCancelled(true);
                        return;
                    }
                    XP.addXP(u, xp * amt * count);
                }
            }

            if(!u.getProfession().getType().equals(Profession.Type.HEALER) && !Feudal.getConfiguration().getConfig().getBoolean("Healer.anyoneCanCraftGodApple") && event.getCurrentItem() != null && event.getCurrentItem().getType().equals(Material.GOLDEN_APPLE) && event.getCurrentItem().getDurability() == 1){
                event.setCancelled(true);
            }
        }else if(!Feudal.getConfiguration().getConfig().getBoolean("Healer.anyoneCanCraftGodApple")){
            if(event.getCurrentItem() != null && event.getCurrentItem().getType().equals(Material.GOLDEN_APPLE) && event.getCurrentItem().getDurability() == 1){
                event.setCancelled(true);
            }
        }
    }

    private static int getCount(CraftingInventory inventory, Player p, ItemStack itemStack) {
        if(itemStack != null && p != null && inventory != null){
            int canCraft = -1;
            final ItemStack[] stacks = inventory.getMatrix();
            if(stacks != null && stacks.length > 0){
                for(final ItemStack item : stacks){
                    if(item != null && !item.getType().equals(Material.AIR) && (item.getAmount() < canCraft || canCraft == -1)){
                        canCraft = item.getAmount();
                    }
                }

                final ItemStack result = inventory.getResult();
                if(result != null){
                    final int max = result.getMaxStackSize();
                    int empty = 0;
                    int allowedEmpty = 0;
                    ItemStack[] items;
                    items = p.getInventory().getContents();

                    for(final ItemStack item : items){
                        if(item == null){
                            empty++;
                        }else if(item.getType().equals(Material.AIR)){
                            empty++;
                        }else if(item.isSimilar(result)){
                            allowedEmpty+= item.getMaxStackSize() - item.getAmount();
                        }
                    }
                    final int allowed = ((max * empty) + allowedEmpty) / result.getAmount();
                    if(allowed < canCraft){
                        canCraft = allowed;
                    }
                    if(canCraft == 0){
                        canCraft = -1;
                    }
                    return canCraft;
                }else{
                    return -1;
                }
            }else{
                return -1;
            }
        }else{
            return -1;
        }
    }

    public static void fish(PlayerFishEvent event) {//fisher
        if(event.getCaught() != null && !(event.getCaught() instanceof LivingEntity)){
            if(Feudal.getOnlinePlayers().containsKey(event.getPlayer().getUniqueId().toString())){
                final User u = Feudal.getOnlinePlayers().get(event.getPlayer().getUniqueId().toString());
                if(u.getProfession().getType().equals(Profession.Type.FISHER)){
                    XP.addXP(u, Feudal.getConfiguration().getConfig().getInt("Fisher.xpPerCatch"));
                }
            }
        }
    }

    public static void blockPlace(BlockPlaceEvent event){//Builder
        if(event.isCancelled()){
            return;
        }
        if(event.getBlock() != null){
            if(Feudal.getOnlinePlayers().containsKey(event.getPlayer().getUniqueId().toString())){
                final User u = Feudal.getOnlinePlayers().get(event.getPlayer().getUniqueId().toString());
                if(u.getProfession().getType().equals(Profession.Type.BUILDER)){
                    final List<String> blocks = Feudal.getConfiguration().getConfig().getStringList("Builder.blocks");
                    blocks.replaceAll(String::toUpperCase);
                    if(event.getBlock().getType() != null && blocks.contains(event.getBlock().getType().name().toUpperCase())){
                        int xp = Feudal.getConfiguration().getConfig().getInt("Builder.xpPerBlock");
                        if(!u.getKingdomUUID().equals("")){
                            final Kingdom k = Feudal.getLandKingdom(new Land(event.getPlayer().getLocation()));
                            if(k != null && k.getUUID().equals(u.getKingdomUUID())){
                                xp += Feudal.getConfiguration().getConfig().getInt("Builder.xpOnKingdomBonus");
                            }
                        }

                        //random chance for food
                        final double luckDecimal = Attributes.getLuck(u.getLuck().getLevel());
                        if(Luck.chance(luckDecimal * (Feudal.getConfiguration().getConfig().getDouble("Luck.Effect.builderFood")/100))) {
                            int newFood = event.getPlayer().getFoodLevel()+1;
                            if(newFood > 20) {
                                newFood = 20;
                            }
                            event.getPlayer().setFoodLevel(newFood);
                        }

                        XP.addXP(u, xp);
                    }
                }
            }
        }
    }

    public static void shear(PlayerShearEntityEvent event) {//sheperd
        if(event.getEntity() instanceof Sheep){
            if(Feudal.getOnlinePlayers().containsKey(event.getPlayer().getUniqueId().toString())){
                final User u = Feudal.getOnlinePlayers().get(event.getPlayer().getUniqueId().toString());
                if(u.getProfession().getType().equals(Profession.Type.SHEPHERD)){
                    if(XP.can(event.getPlayer(), event.getEntity().getLocation())) {
                        XP.addXP(u, Feudal.getConfiguration().getConfig().getInt("Shepherd.xpPerShear"));
                    }

                    //Bonus items
                    final double luckDecimal = Attributes.getLuck(u.getLuck().getLevel());
                    if(Luck.chance(luckDecimal * (Feudal.getConfiguration().getConfig().getDouble("Luck.Effect.shearBonus")/100))) {
                        final ItemStack randomItem = Utils.randomItem();
                        event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), randomItem);
                    }
                    //
                }
            }
        }
    }

    public static boolean can(Player p, Location location) {
        if(Bukkit.getPluginManager().isPluginEnabled("WorldGuard") && WGUtils.hasWG()){
            if(!WGUtils.canBuild(p, location)){
                return false;
            }
        }
        return true;
    }

    public static void bookWrite(PlayerEditBookEvent event) {//scribe
        if(event.isSigning()){
            if(Feudal.getOnlinePlayers().containsKey(event.getPlayer().getUniqueId().toString())){
                final User u = Feudal.getOnlinePlayers().get(event.getPlayer().getUniqueId().toString());
                if(u.getProfession().getType().equals(Profession.Type.SCRIBE)){
                    String stringBuilder = "";
                    for(final String str : event.getNewBookMeta().getPages()){
                        if(stringBuilder.length() < 1000){
                            stringBuilder += str;
                        }else{
                            break;
                        }
                    }
                    stringBuilder.toLowerCase();
                    stringBuilder = stringBuilder.replace("!", "").replace(".", "").replace("-", "").replace("_", "").replace("+", "").replace("=", "").replace("?", "").replace("#", "").replace("$", "").replace("%", "").replace(",", "").replace("\n", " ").replace("'", "");
                    int xp = Feudal.getConfiguration().getConfig().getInt("Scribe.xpStart");;
                    int loop = 0;
                    final ArrayList<String> words = new ArrayList<>();
                    for(final String s : stringBuilder.split(" ")){
                        if(loop >= 100){
                            break;
                        }
                        if(s.length() > 3 && s.length() < 15 && !words.contains(s)){
                            if(isWord(s)){
                                xp += Feudal.getConfiguration().getConfig().getInt("Scribe.xpPerWord");
                            }else{
                                xp += Feudal.getConfiguration().getConfig().getInt("Scribe.xpPerNonWord");
                            }
                        }
                        loop++;
                    }
                    if(xp < 1){
                        xp = 1;
                    }
                    XP.addXP(u, xp);
                }
            }
        }
    }

    private static boolean isWord(String s) {//to check scribe book.
        final ArrayList<Integer> l = new ArrayList<>();//0 = cons; 1 = voul, -1 = other
        for(final char c : s.toCharArray()){
            switch(c){
                case 'a': l.add(1); break;
                case 'b': l.add(0); break;
                case 'c': l.add(0); break;
                case 'd': l.add(0); break;
                case 'e': l.add(1); break;
                case 'f': l.add(0); break;
                case 'g': l.add(0); break;
                case 'h': l.add(0); break;
                case 'i': l.add(1); break;
                case 'j': l.add(0); break;
                case 'k': l.add(0); break;
                case 'l': l.add(0); break;
                case 'm': l.add(0); break;
                case 'n': l.add(0); break;
                case 'o': l.add(1); break;
                case 'p': l.add(0); break;
                case 'q': l.add(0); break;
                case 'r': l.add(0); break;
                case 's': l.add(0); break;
                case 't': l.add(0); break;
                case 'u': l.add(1); break;
                case 'v': l.add(0); break;
                case 'w': l.add(0); break;
                case 'x': l.add(0); break;
                case 'y': l.add(1); break;
                case 'z': l.add(0); break;
                default: l.add(-1); break;
            }
        }
        if(!l.contains(1)){//No voul
            return false;
        }
        for(int i = 0; i < l.size(); i++){
            if(i != (l.size()-1) && l.get(i).intValue() == -1){
                return false;
            }else if(i > 2 && i < (l.size()-1)){
                if(l.get(i).intValue() == 1 && l.get(i-1).intValue() == 1 && l.get(i-2).intValue() == 1 && l.get(i-3).intValue() == 1){
                    return false;
                }else if(l.get(i).intValue() == 0 && l.get(i-1).intValue() == 0 && l.get(i-2).intValue() == 0 &&  l.get(i-3).intValue() == 0){
                    return false;
                }
            }
        }
        return true;
    }

    public static HashMap<Location, String> getBrewing() {
        return brewing;
    }

}