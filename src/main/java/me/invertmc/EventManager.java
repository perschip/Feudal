package me.invertmc;

import java.util.ArrayList;

import com.cryptomorin.xseries.XBiome;
import com.cryptomorin.xseries.XMaterial;
import me.invertmc.kingdoms.BlockValidator;
import me.invertmc.kingdoms.Kingdom;
import me.invertmc.kingdoms.Land;
import me.invertmc.kingdoms.Rank;
import me.invertmc.user.Selection;
import me.invertmc.user.TrackPlayer;
import me.invertmc.user.User;
import me.invertmc.utils.ErrorManager;
import me.invertmc.utils.Utils;
import me.invertmc.utils.UtilsAbove1_7;
import me.invertmc.utils.VaultUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class EventManager implements Listener{
    @EventHandler(priority = EventPriority.HIGHEST)
    public void chat(AsyncPlayerChatEvent event){//TAGS {PROFESSION}, {KINGDOM}, {RANK}, {RANKSPACER}

        if(event.isCancelled()) {
            return;
        }

        try{
            String profession = "NONE";
            String kingdom = "";
            String rank = "";
            String rankSpace = "";
            final User u = Feudal.getUser(event.getPlayer().getUniqueId().toString());
            if(u != null && u.getProfession() != null && !u.getProfession().getType().equals(Type.NONE)){
                profession = u.getProfession().getType().getNameLang();
                if(!u.getKingdomUUID().equals("")){
                    final Kingdom k = Feudal.getKingdom(u.getKingdomUUID());
                    if(k != null){
                        kingdom = k.getName();
                        final Rank r = k.getRank(u.getUUID());
                        if(r != null){
                            if(r.equals(Rank.LEADER)){
                                rank = Feudal.getMessage("allyChat.leader");
                            }else if(r.equals(Rank.EXECUTIVE)){
                                rank = Feudal.getMessage("allyChat.executive");
                            }else if(r.equals(Rank.MEMBER)){
                                rank = Feudal.getMessage("allyChat.member");
                            }else if(r.equals(Rank.GUEST)){
                                rank = Feudal.getMessage("allyChat.guest");
                            }
                            rankSpace = " ";

                            boolean kingdomChat = u.getChat() > 0;
                            boolean allyChat = u.getChat() == 2;
                            String message = event.getMessage();
                            if(Feudal.getConfiguration().getConfig().getBoolean("kingdom.chat.symbols.enabled")) {
                                if(message.startsWith(Feudal.getConfiguration().getConfig().getString("kingdom.chat.symbols.kingdom"))){
                                    kingdomChat = true;
                                    allyChat = false;
                                    message = message.substring(1);
                                }else if(message.startsWith(Feudal.getConfiguration().getConfig().getString("kingdom.chat.symbols.ally"))){
                                    kingdomChat = true;
                                    allyChat = true;
                                    message = message.substring(1);
                                }
                            }

                            if(kingdomChat){
                                event.setCancelled(true);

                                if(Bukkit.getPluginManager().getPlugin("AdvancedBan") != null){
                                    if(AdvancedBanUtil.isMuted(event.getPlayer())) {
                                        event.getPlayer().sendMessage(Feudal.getMessage("181.mute"));
                                        return;
                                    }
                                }

                                final String msg = Feudal.getMessage("allyChat.kingdom").replace("%rank%", rank).replace("%name%", event.getPlayer().getDisplayName()).replace("%message%", message);//rank + " \u00a7e" + event.getPlayer().getDisplayName() + " \u00a75\u00a7l>> \u00a7b" + event.getMessage();
                                k.messageMembers(msg, false);
                                Feudal.log("[KC] " + msg);
                                for(final World w : Bukkit.getWorlds()){
                                    for(final Player players : w.getPlayers()){
                                        if(players.hasPermission("feudal.commands.admin.kingdomchat")){
                                            if(!k.isMember(players.getUniqueId().toString())){
                                                players.sendMessage("\u00a7eAdmin [KC] \u00a7r" + msg);
                                            }
                                        }
                                    }
                                }
                                if(allyChat){
                                    final String msg2 = Feudal.getMessage("allyChat.ally").replace("%rank%", rank).replace("%kingdom%", k.getName()).replace("%name%", event.getPlayer().getDisplayName()).replace("%message%", message);
                                    //String msg2 = rank + "\u00a7a " + k.getName() + " \u00a7e" + event.getPlayer().getDisplayName() + " \u00a75\u00a7l>> \u00a79" + event.getMessage();
                                    for(final String kingdomUUID : k.getAlliesList()){
                                        final Kingdom ally = Feudal.getKingdom(kingdomUUID);
                                        if(ally != null && ally.isAllied(k)){
                                            ally.messageMembers(msg2, false);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            event.setFormat(Utils.essentialsChatFormat(event.getFormat(), event.getPlayer()));
            event.setFormat(event.getFormat().replaceAll("\\{PROFESSION\\}", profession).replaceAll("\\{KINGDOM\\}", kingdom).replaceAll("\\{RANK\\}", rank).replaceAll("\\{RANKSPACER\\}", rankSpace));
        }catch(final Exception e){
            ErrorManager.error(40, e);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void joinAsync(AsyncPlayerPreLoginEvent event) {
        try {

            if(Feudal.getPlugin().isSaving(event.getUniqueId().toString())) {
                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                event.setKickMessage(Feudal.getMessage("saveJoinMessage"));
                return;
            }

            Feudal.logPlayer(event.getName(), event.getUniqueId());
        }catch(final Exception e){
            ErrorManager.error(5251114, e);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void join(PlayerJoinEvent event){
        try{

            Feudal.loadPlayer(event.getPlayer());

            if(Feudal.getEco() == 1) {
                final User user = Feudal.getUser(event.getPlayer().getUniqueId().toString());
                if(user == null || user.getProfession() == null || user.getProfession().getType().equals(Type.NONE)){
                    VaultUtils.removePermission(event.getPlayer(), "feudal.character.setup");
                    VaultUtils.addPermission(event.getPlayer(), "feudal.character.notsetup");
                }else {
                    VaultUtils.addPermission(event.getPlayer(), "feudal.character.setup");
                    VaultUtils.removePermission(event.getPlayer(), "feudal.character.notsetup");
                }
            }

            if(event.getPlayer().hasPermission("feudal.admin")){
			/*if(!ErrorManager.isLatest()){
				event.getPlayer().sendMessage("\u00a7eA new version of Feudal is available. Get it here: \u00a7dhttp://download.feudal.coremod.com/");
			}else if(!ErrorManager.hasSpigot() && ErrorManager.isLoaded()){
				event.getPlayer().sendMessage("\u00a7cFeudal errors will still be shown in console since your Spigot contact information has not been entered in the main config (spigotUsername).  Providing your spigot username will allow the Feudal creator to contact you in the case Feudal does have an issue.");
			}*/
            }
        }catch(final Exception e){
            ErrorManager.error(41, e);
        }
    }

    @EventHandler
    public void leave(PlayerQuitEvent event){
        try{
            Feudal.unloadPlayer(event.getPlayer());
            ChallengeManager.leave(event.getPlayer());
            final ArrayList<Location> rem = new ArrayList<>();
            for(final Location loc : XP.getBrewing().keySet()){
                if(XP.getBrewing().get(loc).equals(event.getPlayer().getUniqueId().toString())){
                    rem.add(loc);
                }
            }
            for(final Location loc : rem){
                XP.getBrewing().remove(loc);
            }

            Sparing.leave(event);
        }catch(final Exception e){
            ErrorManager.error(42, e);
        }
    }

    @EventHandler
    public void interact(PlayerInteractEvent event){
        try{
		/*//DEBUG:
		if(event.getPlayer().isOp() && event.getAction().equals(Action.LEFT_CLICK_AIR)){
			net.minecraft.server.v1_9_R1.ItemStack stack = CraftItemStack.asNMSCopy(event.getPlayer().getInventory().getItemInMainHand());
	        NBTTagCompound tagCompound = stack.getTag();
	        if (tagCompound != null) {
	        	String tag = tagCompound.toString();
	        	event.getPlayer().sendMessage("TAG: " + tag);
	        }
		}
		//*/


            us.forseth11.feudal.user.classes.Effect.use(event);
            LandManagement.interact(event);
            Extra.interact(event);
            BlockValidator.onInteract(event); // check if block is valid
        }catch(final Exception e){
            ErrorManager.error(43, e);
        }
    }

	/*@EventHandler
	public void respawn(PlayerRespawnEvent event){
		//ChallengeManager.respawn(event);
	}*/

    @EventHandler
    public void pistonRetract(BlockPistonRetractEvent event){
            UtilsAbove1_7.pistonRetract(event);

    }

    @EventHandler
    public void pistonExtend(BlockPistonExtendEvent event){
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
            ErrorManager.error(45, e);
        }
    }

    @EventHandler
    public void explode(EntityExplodeEvent event){
        try{
            LandManagement.explode(event);
        }catch(final Exception e){
            ErrorManager.error(46, e);
        }
    }

    @EventHandler
    public void fireSpread(BlockIgniteEvent event){
        try{
            LandManagement.fireSpread(event);
        }catch(final Exception e){
            ErrorManager.error(47, e);
        }
    }

    @EventHandler
    public void burn(BlockBurnEvent event){
        try{
            LandManagement.burn(event);
        }catch(final Exception e){
            ErrorManager.error(48, e);
        }
    }

    @EventHandler
    public void entityChangeBlock(EntityChangeBlockEvent event){
        try{
            LandManagement.entityChangeBlock(event);
        }catch(final Exception e){
            ErrorManager.error(49, e);
        }
    }

    @EventHandler
    public void move(PlayerMoveEvent event){
        try{
            if(event.isCancelled()){
                return;
            }
            if(!event.getTo().getBlock().equals(event.getFrom().getBlock())){
                Selection.moveEvent(event);
                AttributeFixer.moveEvent(event);
            }
            if(event.getTo().getChunk().getX() != event.getFrom().getChunk().getX() || event.getTo().getChunk().getZ() != event.getFrom().getChunk().getZ()){
                LandManagement.moveChunk(event.getPlayer(), event.getTo());
                final User u = Feudal.getUser(event.getPlayer().getUniqueId().toString());
                if(u != null && !u.getKingdomUUID().equals("")){
                    final Kingdom k = Feudal.getKingdom(u.getKingdomUUID());
                    if(k != null){
                        for(final Challenge c : Feudal.getChallenges(k)){
                            if(c.isFighting()){

                                boolean attacker = false;
                                if(c.getAttacker().equals(k)){
                                    attacker = true;
                                }
                                c.checkUser(u, attacker, event.getTo());
                            }
                        }
                    }
                }
            }

            Sparing.move(event);
        }catch(final Exception e){
            ErrorManager.error(50, e);
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void teleport(PlayerTeleportEvent event){
        try{
            if(!event.isCancelled()){
                try{
                    if(event.getCause().equals(TeleportCause.ENDER_PEARL) || (!Feudal.getVersion().equals("1.8") && !Feudal.getVersion().equals("1.7") && event.getCause().equals(TeleportCause.valueOf("CHORUS_FRUIT")))){
                        final Land land = new Land(event.getTo());
                        final Kingdom king = Feudal.getLandKingdom(land);
                        if(king != null){
                            if((king.isShielded() && Feudal.getConfiguration().getConfig().getBoolean("kingdom.shieldProtection.enderpearls"))
                                    || (!king.isShielded() && Feudal.getConfiguration().getConfig().getBoolean("kingdom.landProtection.enderpearls"))){
                                final User u = Feudal.getUser(event.getPlayer().getUniqueId().toString());
                                if(u != null && !u.getKingdomUUID().equals("")){
                                    final Kingdom mine = Feudal.getKingdom(u.getKingdomUUID());
                                    if(mine != null){
                                        if(!mine.equals(king) && !mine.isAllied(king)){
                                            event.setCancelled(true);
                                            return;
                                        }
                                    }else{
                                        event.setCancelled(true);
                                        return;
                                    }
                                }else{
                                    event.setCancelled(true);
                                    return;
                                }
                            }else {
                                return;
                            }
                        }
                    }
                }catch(final Exception e){
                    //
                }

                if(event.getTo().getChunk().getX() != event.getFrom().getChunk().getX() || event.getTo().getChunk().getZ() != event.getFrom().getChunk().getZ()){
                    LandManagement.moveChunk(event.getPlayer(), event.getTo());
                    final User u = Feudal.getUser(event.getPlayer().getUniqueId().toString());
                    if(u != null){
                        if(event.getTo().getWorld().equals(event.getFrom().getWorld())){
                            Bukkit.getScheduler().scheduleSyncDelayedTask(Feudal.getPlugin(), () -> u.effectAttributes(), 20);
                        }
                    }
                    if(u != null && !u.getKingdomUUID().equals("")){
                        final Kingdom k = Feudal.getKingdom(u.getKingdomUUID());
                        if(k != null){
                            for(final Challenge c : Feudal.getChallenges(k)){
                                if(c.isFighting()){

                                    boolean attacker = false;
                                    if(c.getAttacker().equals(k)){
                                        attacker = true;
                                    }
                                    c.checkUser(u, attacker, event.getTo());
                                }
                            }
                        }
                    }
                }
            }

            Sparing.teleport(event);

        }catch(final Exception e){
            ErrorManager.error(51, e);
        }
    }



    @EventHandler
    public void command(PlayerCommandPreprocessEvent event){
        try{
            LandManagement.commandEvent(event);
            Selection.commandEvent(event);
            AttributeFixer.commandEvent(event);
        }catch(final Exception e){
            ErrorManager.error(52, e);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void command2(PlayerCommandPreprocessEvent event) {
        if(!event.isCancelled()) {
            event.setCancelled(Feudal.getPlugin().getCommands().preprocess(event.getMessage(), event.getPlayer()));
        }
    }

    @EventHandler
    public void click(InventoryClickEvent event){
        try{
            Selection.inventoryClickEvent(event);
            AttributeFixer.inventoryClickEvent(event);
            Market1_12.inventoryClickEvent(event);

            XP.inventoryClick(event);
            TrackPlayer.inventoryClickEvent(event);
            ChangeProfession.inventoryClickEvent(event);
            AttributeRedistribute.inventoryClickEvent(event);
        }catch(final Exception e){
            ErrorManager.error(53, e);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void damageByEntity(EntityDamageByEntityEvent event){
        try{
            if(!event.isCancelled()){
                Effect.damageByEntity(event);
                Luck.damageEntity(event);
                Extra.damage(event);
                LandManagement.damageByEntity(event);
            }

            Sparing.damage(event);
        }catch(final Exception e){
            ErrorManager.error(54, e);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void arrow(ProjectileHitEvent event) {
        try {
            LandManagement.arrowHit(event);
        }catch(final Exception e) {
            ErrorManager.error(110041518, e);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void brewEvent(BrewEvent event){
        try{
            if(!event.isCancelled()){
                XP.brew(event);
            }
        }catch(final Exception e){
            ErrorManager.error(55, e);
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void grow(BlockGrowEvent event){
        try{
            if(event.getBlock() != null){
                if(event.getBlock().getType().equals(XMaterial.WHEAT.parseItem()) || event.getBlock().getType().equals(XMaterial.POTATO.parseItem()) || event.getBlock().getType().equals(XMaterial.CARROT.parseItem()) || event.getBlock().getType().equals(XMaterial.BEETROOTS.parseItem())){
                    final Biome b = event.getBlock().getBiome();
                    try{
                        if(!b.equals(Biome.FROZEN_OCEAN) && !b.equals(Biome.FROZEN_RIVER) && !b.equals(XBiome.SNOWY_BEACH) && !b.equals(Biome.valueOf("TAIGA_COLD")) &&
                                !b.equals(Biome.valueOf("TAIGA_COLD_HILLS")) && !b.equals(Biome.DESERT) && !b.equals(Biome.DESERT)){
                            if(event.getBlock().getLocation().getY() < (event.getBlock().getLocation().getWorld().getSeaLevel()-7) || (event.getBlock().getLocation().getY() > (event.getBlock().getLocation().getWorld().getMaxHeight() - 100) && event.getBlock().getLocation().getWorld().getMaxHeight() > (event.getBlock().getLocation().getWorld().getSeaLevel() + 120))){
                                if(System.currentTimeMillis() % 5 != 0){
                                    event.setCancelled(true);
                                }
                            }else if(event.getBlock().getLocation().getY() == (event.getBlock().getLocation().getWorld().getSeaLevel()+1)){
                                if(System.currentTimeMillis() % 2 == 0){
                                    event.getBlock().setData((byte)(event.getBlock().getData()+1));
                                }
                            }
                        }else{
                            event.setCancelled(true);
                            event.getBlock().setType(Material.AIR);
                        }
                    }catch(final Exception e){
                        //
                    }
                }
            }
        }catch(final Exception e){
            ErrorManager.error(56, e);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void blockBreak(BlockBreakEvent event){
        try{
            if(event.getBlock() != null && event.getBlock().getType().equals(XMaterial.OAK_FENCE.parseItem()) && Land.getMarks().contains(event.getBlock())){
                event.setCancelled(true);
                return;
            }
            Effect.blockBreak(event);
            LandManagement.blockBreak(event);
            ChallengeManager.blockBreak(event);
        }catch(final Exception e){
            ErrorManager.error(57, e);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void blockBreakMonitor(BlockBreakEvent event){
        try{
            if(!event.isCancelled()){
                XP.blockBreak(event);
            }
        }catch(final Exception e){
            ErrorManager.error(96, e);
        }
    }

    @EventHandler
    public void blockPlace(BlockPlaceEvent event){
        try{
            LandManagement.blockPlace(event);
            XP.blockPlace(event);
        }catch(final Exception e){
            ErrorManager.error(58, e);
        }
    }

    @EventHandler
    public void regenerateHealth(EntityRegainHealthEvent event){
        try{
            Effect.regenerateHealth(event);
        }catch(final Exception e){
            ErrorManager.error(59, e);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void craftItem(CraftItemEvent event){
        try{
            XP.craftItemEvent(event);
        }catch(final Exception e){
            ErrorManager.error(60, e);
        }
    }

    @EventHandler
    public void fish(PlayerFishEvent event){
        try{
            if(event.getState().equals(State.CAUGHT_FISH)){
                XP.fish(event);
                Luck.fish(event);
            }
        }catch(final Exception e){
            ErrorManager.error(61, e);
        }
    }

    @EventHandler
    public void shear(PlayerShearEntityEvent event){
        try{
            XP.shear(event);
        }catch(final Exception e){
            ErrorManager.error(62, e);
        }
    }

    @EventHandler
    public void bookWrite(PlayerEditBookEvent event){
        try{
            XP.bookWrite(event);
        }catch(final Exception e){
            ErrorManager.error(63, e);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void consume(PlayerItemConsumeEvent event){
        try{
            us.forseth11.feudal.user.classes.Effect.eat(event);
            if(event.getItem() != null && event.getItem().getType() != null && event.getItem().getType().equals(Material.POTION)){
                Luck.potionConsume(event);
            }
        }catch(final Exception e){
            ErrorManager.error(64, e);
        }
    }

    @EventHandler
    public void closeInventory(InventoryCloseEvent event){
        try{
            if(Feudal.getVersion().equals("1.9")){
                Market1_9.close(event);
            }else if(Feudal.getVersion().equals("1.9.4")){
                Market1_9_4.close(event);
            }else if(Feudal.getVersion().equals("1.10")){
                Market1_10.close(event);
            }else if(Feudal.getVersion().equals("1.11")){
                Market1_11.close(event);
            }else if(Feudal.getVersion().equals("1.12")){
                Market1_12.close(event);
            }else{
                Market1_8.close(event);
            }
            TrackPlayer.close(event);
            ChangeProfession.close(event);
            AttributeRedistribute.closeEvent(event);
        }catch(final Exception e){
            ErrorManager.error(65, e);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void entityDeath(EntityDeathEvent event){
        try{
            Luck.entityDeath(event);
            XP.entityDeathEvent(event);

            if(event.getEntity() instanceof Player){

                if(event.getEntity().getKiller() != null){
                    final User u = Feudal.getUser(event.getEntity().getKiller().getUniqueId().toString());
                    if(u != null && XP.can(event.getEntity().getKiller(), event.getEntity().getLocation())){
                        //Add reputation
                        u.effectReputation(Feudal.getConfiguration().getConfig().getInt("reputation.playerKill"), Feudal.getMessage("landUpdate.killed"));
                    }
                }

                final Player p = (Player) event.getEntity();
                final User u = Feudal.getUser(p.getUniqueId().toString());
                if(u != null && XP.can(p, p.getLocation())){
                    //Remove reputation
                    u.effectReputation(Feudal.getConfiguration().getConfig().getInt("reputation.playerDeath"), Feudal.getMessage("landUpdate.died"));
                }
            }
        }catch(final Exception e){
            ErrorManager.error(66, e);
        }
    }

    @EventHandler
    public void playerDeath(PlayerDeathEvent event){
        try{
            ChallengeManager.entityDeath(event);
        }catch(final Exception e){
            ErrorManager.error(67, e);
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void entityDamage(EntityDamageEvent event){
        try{
            Luck.playerDamage(event);
            if(event.getEntity() instanceof Player){
                final User u = Feudal.getUser(((Player)event.getEntity()).getUniqueId().toString());
                if(u != null && u.canHome()){
                    u.setHome(false);
                }
            }
        }catch(final Exception e){
            ErrorManager.error(68, e);
        }
    }

    @EventHandler
    public void xpChange(PlayerExpChangeEvent event){
        try{
            Luck.xpGain(event);
        }catch(final Exception e){
            ErrorManager.error(69, e);
        }
    }

    @EventHandler
    public void enchantItem(EnchantItemEvent event){
        try{
            Luck.enchantItem(event);
        }catch(final Exception e){
            ErrorManager.error(70, e);
        }
    }

    @EventHandler
    public void potionSplash(PotionSplashEvent event){
        try{
            LandManagement.splash(event);
            Luck.potionSplash(event);
        }catch(final Exception e){
            ErrorManager.error(71, e);
        }
    }

    @EventHandler
    public void itemDamage(PlayerItemDamageEvent event){
        try{
            Luck.damageItem(event);
        }catch(final Exception e){
            ErrorManager.error(72, e);
        }
    }

    @EventHandler
    public void itemPickup(PlayerPickupItemEvent event) {
        try {
            ItemProtector.itemPickup(event);
        }catch(final Exception e){
            ErrorManager.error(5391114, e);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void itemDespawn(ItemDespawnEvent event) {
        try {
            ItemProtector.itemDespawn(event);
        }catch(final Exception e){
            ErrorManager.error(5401114, e);
        }
    }

}