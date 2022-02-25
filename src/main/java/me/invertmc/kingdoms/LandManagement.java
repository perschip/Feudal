package me.invertmc.kingdoms;

import java.util.ArrayList;

import com.cryptomorin.xseries.XMaterial;
import me.invertmc.Feudal;
import me.invertmc.spar.Sparing;
import me.invertmc.user.User;
import me.invertmc.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;


public class LandManagement {

    public static void moveChunk(Player player, Location to) {
        final Land on = new Land(to.getChunk().getX(), to.getChunk().getZ(), player.getWorld(), player.getWorld().getName());
        final User u = Feudal.getUser(player.getUniqueId().toString());
        if(u != null){
            final Kingdom playerKing = Feudal.getPlayerKingdom(player.getUniqueId().toString());
            String color = "\u00a7e";
            final Kingdom onKing = Feudal.getLandKingdom(on);
            String name = Feudal.getMessage("land.unconquered");//"Unconquered Land";
            String des = "";
            if(onKing != null){
                name = onKing.getName();
                des = onKing.getDescription();
            }
            if(playerKing != null && onKing != null){
                if(playerKing.equals(onKing)){
                    color = "\u00a7b";
                }else if(playerKing.isEnemied(onKing)){
                    color = "\u00a7c";
                }else if(playerKing.isAllied(onKing)){
                    color = "\u00a7a";
                }
            }
            if((u.getCurrentKingdom() != null || onKing != null) && ((u.getCurrentKingdom() != null && onKing == null) || (u.getCurrentKingdom() == null && onKing != null) || (u.getCurrentKingdom() != null && onKing != null && !onKing.equals(u.getCurrentKingdom())))){
                if(System.currentTimeMillis() - u.getLastLandMessage() > 1000){
                        player.sendMessage(Feudal.getMessage("land.enter").replace("%color%", color).replace("%kingdom%", name).replace("%desc%", des));

                    u.setLastLandMessage(System.currentTimeMillis());
                }
            }
            u.setCurrentKingdom(onKing);
        }
    }

    public static void blockBreak(BlockBreakEvent event) {
        final Land l = new Land(event.getBlock().getLocation());
        final Kingdom king = Feudal.getLandKingdom(l);
        if(king == null){
            return;
        }
        if(!Feudal.getConfiguration().getConfig().getBoolean("protectSpawners") && event.getBlock() != null && event.getBlock().getType().equals(XMaterial.SPAWNER.parseItem())){
            return;
        }
        if(!king.hasProtection() && (event.getBlock() == null || !(event.getBlock() != null && (event.getBlock().getType().equals(XMaterial.CHEST.parseItem()) || Utils.isShulkerBox(event.getBlock().getType()))))){//Pre-1.2.3 update
            return;
        }else if(!king.hasProtection() && event.getBlock() != null){
            final Block inside = event.getPlayer().getLocation().getBlock();
            if(inside != null && (inside.getType().equals(XMaterial.CHEST.parseItem()) || Utils.isShulkerBox(event.getBlock().getType()))){
                final Location teleport = event.getPlayer().getLocation().add(event.getPlayer().getLocation().getDirection().multiply(3));
                event.getPlayer().teleport(teleport);
            }else{
                final Block b = event.getBlock().getRelative(BlockFace.DOWN);
                if(b != null && b.getType().equals(XMaterial.AIR.parseItem()) || b.getType().equals(XMaterial.WATER.parseItem())){
                    event.getPlayer().teleport(b.getLocation());
                }
            }
        }
        if(king.isShielded()){
            if(Feudal.getConfiguration().getConfig().getBoolean("kingdom.shieldProtection.blockBreak")){
                if(!LandManagement.hasPermission(king, event.getPlayer())){
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(Feudal.getMessage("land.shielded"));
                }
            }
        }else if(Feudal.getConfiguration().getConfig().getBoolean("kingdom.landProtection.blockBreak")){
            if(!LandManagement.hasPermission(king, event.getPlayer())){
                event.setCancelled(true);
                event.getPlayer().sendMessage(Feudal.getMessage("land.deny.break"));
            }
        }
    }

    private static boolean hasPermission(Kingdom king, Player player) {
        if(player.hasPermission("feudal.admin.passProtection")){
            return true;
        }
        if(king != null){
            final User u = Feudal.getUser(player.getUniqueId().toString());
            if(u == null){
                return false;
            }else{
                if(u.getKingdomUUID().equals(king.getUUID())){
                    if(king.isVacation()){
                        return false;
                    }else{
                        return true;
                    }
                }else{
                    return false;
                }
            }
        }else{
            return true;
        }
    }

    public static void blockPlace(BlockPlaceEvent event) {
        if(!event.isCancelled()){
            final Land l = new Land(event.getBlock().getLocation());
            Kingdom king = Feudal.getLandKingdom(l);
            if(king == null){
                if(event.getItemInHand() != null &&
                        (
                                (!Feudal.getVersion().equals("1.7") && event.getItemInHand().getType().equals(XMaterial.WHITE_BANNER.parseItem())
                                        ||
                                        (Feudal.getVersion().equals("1.7") && event.getItemInHand().getType().equals(XMaterial.NOTE_BLOCK.parseItem()))//NOTE BLOCK USED FOR CHALLENGES!
                                ))){
                    final Land land[] = new Land[]{new Land(l.getX() + 1, l.getZ(), l.getWorld(), l.getWorldName())
                            , new Land(l.getX() - 1, l.getZ(), l.getWorld(), l.getWorldName())
                            , new Land(l.getX(), l.getZ() + 1, l.getWorld(), l.getWorldName())
                            , new Land(l.getX(), l.getZ() - 1, l.getWorld(), l.getWorldName())};
                    for(final Land la : land){
                        if(la != null){
                            king = Feudal.getLandKingdom(la);
                            if(king != null && king.hasProtection() && !king.isShielded()){
                                if(checkChallengePlace(event, king)){
                                    return;
                                }
                            }
                        }
                    }
                }
                return;
            }
            if(!king.hasProtection()){
                if((!Feudal.getConfiguration().getConfig().getBoolean("kingdom.landProtection.otherExplosion")) || !(event.getBlock() != null && event.getBlock().getType().equals(XMaterial.TNT.parseItem()))){//Cant place tnt while in challenge if enabled.
                    return;
                }
            }
            if(king.isShielded()){
                if(Feudal.getConfiguration().getConfig().getBoolean("kingdom.shieldProtection.blockPlace")){
                    if(!LandManagement.hasPermission(king, event.getPlayer())){
                        event.setCancelled(true);
                        event.getPlayer().sendMessage(Feudal.getMessage("land.shielded"));
                    }
                }
            }else{
                if(checkChallengePlace(event, king)){
                    return;
                }
                if(!LandManagement.hasPermission(king, event.getPlayer())){
                    if(Feudal.getConfiguration().getConfig().getBoolean("kingdom.landProtection.blockPlace")){
                        event.setCancelled(true);
                        event.getPlayer().sendMessage(Feudal.getMessage("land.deny.place"));
                    }
                }
            }
        }
    }

    private static boolean checkChallengePlace(BlockPlaceEvent event, Kingdom king){
        if(event.getItemInHand() != null &&
                (
                        (!Feudal.getVersion().equals("1.7") && event.getItemInHand().getType().equals(XMaterial.WHITE_BANNER.parseItem()))
                                ||
                                (Feudal.getVersion().equals("1.7") && event.getItemInHand().getType().equals(XMaterial.NOTE_BLOCK.parseItem()))//NOTE BLOCK USED FOR CHALLENGES!
                )){
            final User u = Feudal.getUser(event.getPlayer().getUniqueId().toString());
            if(u != null && !u.getKingdomUUID().equals("")){
                final Kingdom challenger = Feudal.getKingdom(u.getKingdomUUID());
                if(challenger != null && !challenger.equals(king) ){
                    if(!challenger.isAllied(king)){
                        if(challenger.isEnemied(king)){
                            if(!king.isShielded() && !king.hasSpecialShield()){
                                if(ChallengeManager.placeChallenge(event, u, challenger, king)){
                                    return true;
                                }
                            }else{
                                event.getPlayer().sendMessage(Feudal.getMessage("land.shielded"));
                            }
                        }else{
                            event.getPlayer().sendMessage(Feudal.getMessage("land.tochallenge").replace("%kingdom%", king.getName()));
                        }
                    }else{
                        event.getPlayer().sendMessage(Feudal.getMessage("land.deny.ally"));
                    }
                }
            }
        }
        return false;
    }

    public static void interact(PlayerInteractEvent event) {
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)){

			/*if(event.getPlayer() != null && event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().getType().equals(Material.BANNER)){
				User u = Main.getUser(event.getPlayer().getUniqueId().toString());
				if(u != null && !u.getKingdomUUID().equals("")){
					Kingdom k = Main.getKingdom(u.getKingdomUUID());
					if(k != null){
						ItemStack item = u.getPlayer().getItemInHand();
						BannerMeta meta = (BannerMeta) item.getItemMeta();

						meta.setDisplayName("\u00a7a\u00a7l" + k.getName() + "'s banner");
						meta.setBaseColor(k.getBannerBaseColor());
						meta.setPatterns(k.getBannerPatterns());

						item.setItemMeta(meta);
						event.getPlayer().setItemInHand(item);
						event.getPlayer().updateInventory();
					}
				}
			}*/

            if(event.getClickedBlock() != null){
                final Material m = event.getClickedBlock().getType();
                final ItemStack stack = Feudal.getItemInHand(event.getPlayer());
                if(m.equals(XMaterial.OAK_BUTTON.parseItem()) || m.equals(XMaterial.STONE_BUTTON.parseItem()) || m.equals(XMaterial.OAK_DOOR.parseItem()) || m.equals(XMaterial.OAK_DOOR.parseItem()) ||
                        (!Feudal.getVersion().equals("1.7") && m.equals(XMaterial.SPRUCE_DOOR.parseItem()) ||
                                (!Feudal.getVersion().equals("1.7") && m.equals(XMaterial.BIRCH_DOOR.parseItem()) ||
                                        (!Feudal.getVersion().equals("1.7") && m.equals(XMaterial.ACACIA_DOOR.parseItem()) ||
                                                (!Feudal.getVersion().equals("1.7") && m.equals(XMaterial.DARK_OAK_DOOR.parseItem()) ||
                                                        (!Feudal.getVersion().equals("1.7") && m.equals(XMaterial.JUNGLE_DOOR.parseItem()) ||
                                                                (!Feudal.getVersion().equals("1.7") && m.equals(XMaterial.ARMOR_STAND.parseItem()) ||
                                                                        (!Feudal.getVersion().equals("1.7") && m.equals(XMaterial.SPRUCE_FENCE_GATE.parseItem()) ||
                                                                                (!Feudal.getVersion().equals("1.7") && m.equals(XMaterial.BIRCH_FENCE_GATE.parseItem()) ||
                                                                                        (!Feudal.getVersion().equals("1.7") && m.equals(XMaterial.ACACIA_FENCE_GATE.parseItem()) ||
                                                                                                (!Feudal.getVersion().equals("1.7") && m.equals(XMaterial.DARK_OAK_FENCE_GATE.parseItem()) ||
                                                                                                        (!Feudal.getVersion().equals("1.7") && m.equals(XMaterial.JUNGLE_FENCE_GATE.parseItem()) ||
                                                                                                                m.equals(XMaterial.LEVER.parseItem()) || m.equals(XMaterial.FURNACE.parseItem()) || m.equals(XMaterial.FURNACE.parseItem()) || m.equals(XMaterial.HOPPER.parseItem()) ||
                                                                                                                m.equals(XMaterial.ANVIL.parseItem()) || m.equals(XMaterial.OAK_FENCE_GATE.parseItem()) ||
                                                                                                                m.equals(XMaterial.OAK_TRAPDOOR.parseItem()) || m.equals(XMaterial.CHEST.parseItem())  || Utils.isShulkerBox(m) ||  m.equals(XMaterial.TRAPPED_CHEST.parseItem()) ||
                                                                                                                m.equals(XMaterial.PAINTING.parseItem()) ||
                                                                                                                m.equals(XMaterial.ITEM_FRAME.parseItem()) || m.equals(XMaterial.BOOKSHELF.parseItem()) || (stack != null && (stack.getType().equals(XMaterial.WATER_BUCKET.parseItem()) || stack.getType().equals(XMaterial.BUCKET.parseItem()) || stack.getType().equals(XMaterial.LAVA_BUCKET.parseItem()) ||
                                                                                                                m.equals(XMaterial.DROPPER.parseItem()) || m.equals(XMaterial.DISPENSER.parseItem()) || m.equals(XMaterial.BREWING_STAND.parseItem())
                                                                                                        )))))))))))))){
                    final Land l = new Land(event.getClickedBlock().getLocation());
                    final Kingdom king = Feudal.getLandKingdom(l);
                    if(king == null){
                        return;
                    }
                    if(event.getClickedBlock().getType() != null && (event.getClickedBlock().getType().equals(XMaterial.CHEST.parseItem()) || Utils.isShulkerBox(event.getClickedBlock().getType()) || event.getClickedBlock().getType().equals(XMaterial.TRAPPED_CHEST.parseItem())) && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
                        if(king.isShielded()){
                            if(Feudal.getConfiguration().getConfig().getBoolean("kingdom.shieldProtection.chestAccess")){
                                if(!LandManagement.hasPermission(king, event.getPlayer())){
                                    event.setCancelled(true);
                                    event.getPlayer().sendMessage(Feudal.getMessage("land.vacation"));
                                }
                            }
                        }else if(Feudal.getConfiguration().getConfig().getBoolean("kingdom.landProtection.chestAccess")){
                            if(!LandManagement.hasPermission(king, event.getPlayer())){
                                event.setCancelled(true);
                                event.getPlayer().sendMessage(Feudal.getMessage("land.deny.chest"));
                            }
                        }
                    }else if(event.getItem() != null && event.getItem().getType() != null && (event.getItem().getType().equals(XMaterial.FIRE_CHARGE.parseItem()) || event.getItem().getType().equals(XMaterial.FLINT_AND_STEEL.parseItem()))){
                        if(!king.hasProtection()){
                            return;
                        }
                        if(king.isShielded()){
                            if(Feudal.getConfiguration().getConfig().getBoolean("kingdom.shieldProtection.lighter")){
                                if(!LandManagement.hasPermission(king, event.getPlayer())){
                                    event.setCancelled(true);
                                    event.getPlayer().sendMessage(Feudal.getMessage("land.vacation"));
                                }
                            }
                        }else if(Feudal.getConfiguration().getConfig().getBoolean("kingdom.landProtection.lighter")){
                            if(!LandManagement.hasPermission(king, event.getPlayer())){
                                event.setCancelled(true);
                                event.getPlayer().sendMessage(Feudal.getMessage("land.deny.fire"));
                            }
                        }
                    }else if(event.getItem() != null && event.getItem().getType() != null && (

                            (!Feudal.getVersion().equals("1.7") && (event.getItem().getType().equals(XMaterial.WHITE_BANNER.parseItem())))
                                    ||
                                    (Feudal.getVersion().equals("1.7") && (event.getItem().getType().equals(XMaterial.NOTE_BLOCK.parseItem())))

                    ) && (event.getClickedBlock() != null && !event.getClickedBlock().getType().equals(XMaterial.OAK_BUTTON.parseItem()) && !event.getClickedBlock().getType().equals(XMaterial.STONE_BUTTON.parseItem()) && !event.getClickedBlock().getType().equals(XMaterial.OAK_DOOR.parseItem()) && !event.getClickedBlock().getType().equals(XMaterial.LEVER.parseItem()) &&
                            (!Feudal.getVersion().equals("1.7") && !event.getClickedBlock().getType().equals(XMaterial.ACACIA_DOOR.parseItem())) &&
                            (!Feudal.getVersion().equals("1.7") && !event.getClickedBlock().getType().equals(XMaterial.BIRCH_DOOR.parseItem())) &&
                            (!Feudal.getVersion().equals("1.7") && !event.getClickedBlock().getType().equals(XMaterial.SPRUCE_DOOR.parseItem())) &&
                            (!Feudal.getVersion().equals("1.7") && !event.getClickedBlock().getType().equals(XMaterial.JUNGLE_DOOR.parseItem())) &&
                            (!Feudal.getVersion().equals("1.7") && !event.getClickedBlock().getType().equals(XMaterial.DARK_OAK_DOOR.parseItem()))
                    )){
                        if(!king.hasProtection()){
                            return;
                        }
                        return;
                    }else{
                        if(!king.hasProtection()){
                            return;
                        }
                        if(king.isShielded()){
                            if(Feudal.getConfiguration().getConfig().getBoolean("kingdom.shieldProtection.interact")){
                                if(!LandManagement.hasPermission(king, event.getPlayer())){
                                    event.setCancelled(true);
                                    event.getPlayer().sendMessage(Feudal.getMessage("land.vacation"));
                                }
                            }
                        }else if(Feudal.getConfiguration().getConfig().getBoolean("kingdom.landProtection.interact")){
                            if(!LandManagement.hasPermission(king, event.getPlayer())){
                                event.setCancelled(true);
                                event.getPlayer().sendMessage(Feudal.getMessage("land.deny.interact"));
                            }
                        }
                    }
                }
            }
        }
    }

    public static boolean piston(Location location) {
        final Land l = new Land(location);
        final Kingdom king = Feudal.getLandKingdom(l);
        if(king != null){
            if(!king.hasProtection()){
                return false;
            }
            if(king.isShielded()){
                if(Feudal.getConfiguration().getConfig().getBoolean("kingdom.shieldProtection.piston")){
                    return true;
                }else{
                    return false;
                }
            }else if(Feudal.getConfiguration().getConfig().getBoolean("kingdom.landProtection.piston")){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    public static boolean blockIsOnLand(Location location) {
        final Land l = new Land(location);
        final Kingdom king = Feudal.getLandKingdom(l);
        if(king != null){
            return true;
        }else{
            return false;
        }
    }

    public static void explode(EntityExplodeEvent event) {
        boolean cancel = false;
        boolean cancelVac = false;
        if(event.getEntityType() != null && event.getEntityType().equals(EntityType.CREEPER)){
            if(Feudal.getConfiguration().getConfig().getBoolean("kingdom.landProtection.creeperExplosion")){
                cancel = true;
            }
            if(Feudal.getConfiguration().getConfig().getBoolean("kingdom.shieldProtection.creeperExplosion")){
                cancelVac = true;
            }
        }else{
            if(Feudal.getConfiguration().getConfig().getBoolean("kingdom.landProtection.otherExplosion")){
                cancel = true;
            }
            if(Feudal.getConfiguration().getConfig().getBoolean("kingdom.shieldProtection.otherExplosion")){
                cancelVac = true;
            }
        }

        final ArrayList<Block> remove = new ArrayList<>();
        for(final Block b : event.blockList()){
            final Land l = new Land(b.getLocation());
            final Kingdom king = Feudal.getLandKingdom(l);
            if(king != null){
                if(!king.hasProtection()){
                    continue;
                }
                if(king.isShielded()){
                    if(cancelVac){
                        remove.add(b);
                    }
                }else if(cancel){
                    remove.add(b);
                }
            }
        }

        for(final Block b : remove){
            event.blockList().remove(b);
        }
    }

    public static void fireSpread(BlockIgniteEvent event) {
        if(event.getCause().equals(IgniteCause.FIREBALL) || event.getCause().equals(IgniteCause.FLINT_AND_STEEL)){
            return;
        }
        if(event.getBlock() != null){
            final Land l = new Land(event.getBlock().getLocation());
            final Kingdom king = Feudal.getLandKingdom(l);
            if(king != null){
                if(!king.hasProtection()){
                    return;
                }
                if(king.isShielded()){
                    if(Feudal.getConfiguration().getConfig().getBoolean("kingdom.shieldProtection.fireSpread")){
                        event.setCancelled(true);
                        if(event.getIgnitingBlock() != null && event.getIgnitingBlock().getType().equals(XMaterial.FIRE.parseItem())){
                            event.getIgnitingBlock().setType(XMaterial.AIR.parseMaterial());
                        }
                    }
                }else{
                    if(Feudal.getConfiguration().getConfig().getBoolean("kingdom.landProtection.fireSpread")){
                        event.setCancelled(true);
                        if(event.getIgnitingBlock() != null && event.getIgnitingBlock().getType().equals(XMaterial.FIRE.parseItem())){
                            event.getIgnitingBlock().setType(XMaterial.AIR.parseMaterial());
                        }
                    }
                }
            }
        }
    }

    public static void burn(BlockBurnEvent event) {
        if(event.getBlock() != null){
            final Land l = new Land(event.getBlock().getLocation());
            final Kingdom king = Feudal.getLandKingdom(l);
            if(king != null){
                if(!king.hasProtection()){
                    return;
                }
                if(king.isShielded()){
                    if(Feudal.getConfiguration().getConfig().getBoolean("kingdom.shieldProtection.fireSpread")){
                        event.setCancelled(true);
                    }
                }else{
                    if(Feudal.getConfiguration().getConfig().getBoolean("kingdom.landProtection.fireSpread")){
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    public static void entityChangeBlock(EntityChangeBlockEvent event) {
        if(event.getEntityType() != null && event.getBlock() != null){
            if(event.getEntityType().equals(EntityType.ENDER_DRAGON)){
                final Land l = new Land(event.getBlock().getLocation());
                final Kingdom king = Feudal.getLandKingdom(l);
                if(king != null){
                    if(!king.hasProtection()){
                        return;
                    }
                    if(king.isShielded()){
                        if(Feudal.getConfiguration().getConfig().getBoolean("kingdom.shieldProtection.enderDragon")){
                            event.setCancelled(true);
                        }
                    }else{
                        if(Feudal.getConfiguration().getConfig().getBoolean("kingdom.landProtection.enderDragon")){
                            event.setCancelled(true);
                        }
                    }
                }
            }else if(event.getEntityType().equals(EntityType.ENDERMAN)){
                final Land l = new Land(event.getBlock().getLocation());
                final Kingdom king = Feudal.getLandKingdom(l);
                if(king != null){
                    if(!king.hasProtection()){
                        return;
                    }
                    if(king.isShielded()){
                        if(Feudal.getConfiguration().getConfig().getBoolean("kingdom.shieldProtection.enderman")){
                            event.setCancelled(true);
                        }
                    }else{
                        if(Feudal.getConfiguration().getConfig().getBoolean("kingdom.landProtection.enderman")){
                            event.setCancelled(true);
                        }
                    }
                }
            }else if(event.getEntityType().equals(EntityType.VILLAGER)){
                final Land l = new Land(event.getBlock().getLocation());
                final Kingdom king = Feudal.getLandKingdom(l);
                if(king != null){
                    if(!king.hasProtection()){
                        return;
                    }
                    if(king.isShielded()){
                        if(Feudal.getConfiguration().getConfig().getBoolean("kingdom.shieldProtection.villagers")){
                            event.setCancelled(true);
                        }
                    }else{
                        if(Feudal.getConfiguration().getConfig().getBoolean("kingdom.landProtection.villagers")){
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    public static void arrowHit(ProjectileHitEvent event) {
        if(event.getEntity() != null && event.getEntity() instanceof Player) {
            if(event.getEntity().getShooter() != null && event.getEntity().getShooter() instanceof Player) {
                final Kingdom defender = Feudal.getAPI().getKingdom((Player)event.getEntity());
                final Kingdom attacker = Feudal.getAPI().getKingdom((Player)event.getEntity().getShooter());
                if(defender != null && attacker != null && (defender.equals(attacker) || defender.isAllied(attacker))) {
                    event.getEntity().setFireTicks(0);
                    event.getEntity().remove();
                }
            }
        }
    }

    public static void damageByEntity(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Player){
            final User u = Feudal.getUser(((Player) event.getEntity()).getUniqueId().toString());
            if(u != null && !u.getKingdomUUID().equals("")){
                User attacker = null;
                if(event.getDamager() instanceof Player){
                    attacker = Feudal.getUser(((Player) event.getDamager()).getUniqueId().toString());
                }else if(event.getDamager() instanceof Projectile){
                    final Projectile pro = (Projectile) event.getDamager();
                    if(pro.getShooter() != null && pro.getShooter() instanceof Player){
                        attacker = Feudal.getUser(((Player)pro.getShooter()).getUniqueId().toString());
                    }
                }
                if(attacker == u){
                    return;
                }
                if(attacker != null && attacker.getKingdomUUID().equals(u.getKingdomUUID()) && !Sparing.areSparing(u.getPlayer(), attacker.getPlayer())){
                    event.setCancelled(true);
                    if(event.getDamager() instanceof Projectile){
                        ((Projectile) event.getDamager()).setFireTicks(0);
                    }
                    if(attacker.getPlayer() != null){
                        attacker.getPlayer().sendMessage(Feudal.getMessage("land.attack.mem"));
                    }
                }else if(attacker != null){
                    final Kingdom def = Feudal.getKingdom(u.getKingdomUUID());
                    final Kingdom att = Feudal.getKingdom(attacker.getKingdomUUID());
                    if(def != null && att != null){
                        if(def.isAllied(att) && !Sparing.areSparing(attacker.getPlayer(), u.getPlayer())){
                            event.setCancelled(true);
                            if(event.getDamager() instanceof Projectile){
                                ((Projectile) event.getDamager()).setFireTicks(0);
                            }
                            if(attacker.getPlayer() != null){
                                attacker.getPlayer().sendMessage(Feudal.getMessage("land.attack.ally"));
                            }
                        }
                    }
                }
            }
        }
        if(event.getEntity() instanceof ItemFrame || event.getEntity() instanceof Painting){
            if(event.getDamager() instanceof Player){
                final Player p = (Player) event.getDamager();
                final Land l = new Land(event.getEntity().getLocation());
                final Kingdom king = Feudal.getLandKingdom(l);
                if(king == null){
                    return;
                }
                if(!king.hasProtection()){
                    return;
                }
                if(king.isShielded()){
                    if(Feudal.getConfiguration().getConfig().getBoolean("kingdom.shieldProtection.interact")){
                        if(!LandManagement.hasPermission(king, p)){
                            event.setCancelled(true);
                            p.sendMessage(Feudal.getMessage("land.vacation"));
                        }
                    }
                }else if(Feudal.getConfiguration().getConfig().getBoolean("kingdom.landProtection.interact")){
                    if(!LandManagement.hasPermission(king, p)){
                        event.setCancelled(true);
                        p.sendMessage(Feudal.getMessage("land.deny.interact"));
                    }
                }
            }
        }
    }

    public static void splash(PotionSplashEvent event) {
        if(event.getPotion().getShooter() instanceof Player){
            final User u = Feudal.getUser(((Player) event.getPotion().getShooter()).getUniqueId().toString());
            if(u != null && !u.getKingdomUUID().equals("")){
                if(event.getPotion().getItem() != null){
                    final ItemStack i = event.getPotion().getItem();
                    if(i.getDurability() == 16388 || i.getDurability() == 16420 || i.getDurability() == 16452 ||
                            i.getDurability() == 16392 || i.getDurability() == 16424 || i.getDurability() == 16456 ||
                            i.getDurability() == 16394 || i.getDurability() == 16426 || i.getDurability() == 16458){
                        boolean cancel = false;
                        boolean allies = false;
                        final ArrayList<LivingEntity> remove = new ArrayList<>();
                        for(final LivingEntity e : event.getAffectedEntities()){
                            if(e instanceof Player){
                                final User def = Feudal.getUser(((Player)e).getUniqueId().toString());
                                if(def != u && !u.getKingdomUUID().equals("")){
                                    if(def.getKingdomUUID().equals(u.getKingdomUUID()) && !Sparing.areSparing(def.getPlayer(), u.getPlayer())){
                                        remove.add(e);
                                        cancel = true;
                                    }else{
                                        final Kingdom attack = Feudal.getKingdom(u.getKingdomUUID());
                                        final Kingdom defend = Feudal.getKingdom(def.getKingdomUUID());
                                        if(attack != null && defend != null && attack.isAllied(defend) && !Sparing.areSparing(u.getPlayer(), def.getPlayer())){
                                            remove.add(e);
                                            allies = true;
                                        }
                                    }
                                }
                            }
                        }
                        for(final LivingEntity e : remove){
                            event.setIntensity(e, 0);
                        }
                        if(cancel){
                            if(u.getPlayer() != null){
                                u.getPlayer().sendMessage(Feudal.getMessage("land.attack.mem"));
                            }
                        }else if(allies){
                            if(u.getPlayer() != null){
                                u.getPlayer().sendMessage(Feudal.getMessage("land.attack.ally"));
                            }
                        }
                    }
                }
            }
        }
    }

    public static void commandEvent(PlayerCommandPreprocessEvent event) {
        if(!event.isCancelled() && event.getMessage() != null) {
            final Kingdom kingLand = Feudal.getAPI().getKingdom(event.getPlayer().getLocation());
            final Kingdom userKingdom = Feudal.getAPI().getKingdom(event.getPlayer());
            if(kingLand != null &&
                    (userKingdom == null || (userKingdom != kingLand && !userKingdom.isAllied(kingLand)))
            ) {
                for(final String cmd : Feudal.getConfiguration().getConfig().getStringList("kingdom.land.bannedcommands")) {
                    if(event.getMessage().toLowerCase().startsWith(cmd.toLowerCase())) {
                        event.setCancelled(true);
                        event.getPlayer().sendMessage(Feudal.getMessage("landBannedCommand"));
                    }
                }
            }
        }
    }

}