package me.invertmc.kingdoms;

import java.util.ArrayList;

import com.cryptomorin.xseries.XMaterial;
import me.invertmc.Feudal;
import me.invertmc.user.User;
import me.invertmc.utils.Utils;
import me.invertmc.utils.UtilsAbove1_7;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;


public class ChallengeManager {

    public static boolean placeChallenge(BlockPlaceEvent event, User u, Kingdom challenger, Kingdom king) {
        if(u != null && u.getPlayer() != null && u.getPlayer().hasPermission("feudal.commands.user.kingdoms.placechallenge")){
            if(challenger.isShielded()){
                event.getPlayer().sendMessage(Feudal.getMessage("challenge.shield"));
                return false;
            }
            if(challenger.hasSpecialShield()){
                challenger.disableSpecialShield();
            }
            if(Feudal.getChallenge(challenger, king) == null && Feudal.getChallenge(king, challenger) == null){
                if(challenger.getLand().size() > 0){
                    final long requiredClaimAge = (long) (Feudal.getConfiguration().getConfig().getDouble("kingdom.challenges.claimAgeRequiredHours") * 3600000);
                    if(System.currentTimeMillis() >= requiredClaimAge + challenger.getFirstClaim() && !challenger.hasSpecialShield()){
                        challenger.checkBans();
                        king.checkBans();
                        if(challenger.isBanned(king.getUUID())){
                            event.getPlayer().sendMessage(Feudal.getMessage("challenge.ban"));
                            event.getPlayer().sendMessage(challenger.getBanInfo(king));
                            return false;
                        }else{
                            final ArrayList<Challenge> cs = Feudal.getChallenges(king);
                            int count = 0;
                            final int max = Feudal.getConfiguration().getConfig().getInt("kingdom.challenges.maxDefence");
                            if(max != -1){
                                for(final Challenge c : cs){
                                    if(c.getDefender().equals(king)){
                                        count++;
                                    }
                                }
                                if(count >= max){
                                    event.getPlayer().sendMessage(Feudal.getMessage("challenge.max").replace("%max%", max+""));
                                    return false;
                                }
                            }

                            Bukkit.broadcastMessage(Feudal.getMessage("challenge.broadcast").replace("%attacker%", challenger.getName()).replace("%defender%", king.getName()));
                            Feudal.getChallenges().add(new Challenge(challenger, king, event.getBlock().getLocation(), false, (float) Feudal.getConfiguration().getConfig().getDouble("kingdom.challenges.startTax")));
                            king.messageMembers(Feudal.getMessage("challenge.cd").replace("%attacker%", challenger.getName()), true);
                            challenger.messageMembers(Feudal.getMessage("challenge.ca").replace("%defender%", king.getName()), true);
                            challenger.effectReputation(Feudal.getConfiguration().getConfig().getInt("reputation.placeChallenge"), Feudal.getMessage("reputation.placeChallenge"));
                            Feudal.saveChallengesRough();

                            return true;
                        }
                    }else{
                        final long rem = (requiredClaimAge + challenger.getFirstClaim()) - System.currentTimeMillis();
                        final long days = rem / 86400000;
                        final long hours = ((rem - (days * 86400000)) / 3600000);
                        final long minutes = (rem - (days * 86400000) - (hours * 3600000)) / 60000;
                        final long seconds = (rem - (days * 86400000) - (hours * 3600000) - (minutes * 60000)) / 1000;
                        String time = "";
                        if(days == 0){
                            time = hours + ":" + minutes + ":" + seconds;
                        }else{
                            time = days + (days == 1 ? " day" : " days") + " " + hours + ":" + minutes + ":" + seconds;
                        }
                        event.getPlayer().sendMessage(Feudal.getMessage("challenge.claimAge").replace("%time%", time));
                        return false;
                    }
                }else{
                    event.getPlayer().sendMessage(Feudal.getMessage("challenge.needland"));
                    return false;
                }
            }else{
                event.getPlayer().sendMessage(Feudal.getMessage("challenge.already").replace("%defender%", king.getName()));
                return false;
            }
        }else{
            event.getPlayer().sendMessage(Feudal.getMessage("land.deny.place"));
            return false;
        }
    }

    public static void join(Player player) {
        Challenge.resetLeaderBoard(player);
        final User u = Feudal.getUser(player.getUniqueId().toString());
        if(u != null && !u.getKingdomUUID().equals("")){
            final Kingdom k = Feudal.getKingdom(u.getKingdomUUID());
            if(k != null){
                if(k.isFighter(u.getUUID())){
                    for(final Challenge c : Feudal.getChallenges(k)){
                        c.fighterJoin(c.getAttacker().equals(k));
                    }
                }else{
                    for(final Challenge c : Feudal.getChallenges(k)){
                        c.memberJoin(c.getAttacker().equals(k));
                    }
                }
            }
        }
    }

    public static void leave(Player player) {
        final User u = Feudal.getUser(player.getUniqueId().toString());
        if(u != null && !u.getKingdomUUID().equals("")){
            final Kingdom k = Feudal.getKingdom(u.getKingdomUUID());
            if(k != null){
                if(k.isFighter(u.getUUID())){
                    if(k.getFightersOnline() <= 1){
                        for(final Challenge c : Feudal.getChallenges(k)){
                            c.removeLeaderBoard(player);
                            c.fighterLeave(c.getAttacker().equals(k));
							/*if(c.isFighting()){
								if(player.getHealth() > 0){
									c.death(u, c.getAttacker().equals(k));
								}
							}*/
                        }
                    }
                }else{
                    for(final Challenge c : Feudal.getChallenges(k)){
                        c.removeLeaderBoard(player);
                    }
                }
            }
        }
    }

    public static void entityDeath(PlayerDeathEvent event) {
        if(Utils.getHealth(event.getEntity()) < 1){
            if(event.getEntity() instanceof Player){
                final User u = Feudal.getUser(event.getEntity().getUniqueId().toString());
                if(u != null && !u.getKingdomUUID().equals("")){
                    final Kingdom k = Feudal.getKingdom(u.getKingdomUUID());
                    if(k != null){
                        boolean chal = false;
                        for(final Challenge c : Feudal.getChallenges(k)){
                            if(c.isFighting()){
                                chal = true;
                                c.death(u, c.getAttacker().equals(k));
                                final boolean attacker = c.getAttacker().equals(k);
                                if(attacker){
                                    c.addAttackerDeath(u.getPlayer());
                                }else{
                                    c.addDefenderDeath(u.getPlayer());
                                }
                                if(u.getPlayer().getKiller() != null){
                                    final User killer = Feudal.getUser(u.getPlayer().getKiller().getUniqueId().toString());
                                    if(killer != null){
                                        c.killCheck(u, killer, attacker);
                                    }
                                }

                                if(c.isFighting()){
                                    if(Feudal.getConfiguration().getConfig().getBoolean("kingdom.challenges.keepInventory")){
                                        if(!Feudal.getVersion().equals("1.7")) {
                                            UtilsAbove1_7.keepInventory(event);
                                        }
                                        checkEnchantPlus(event.getEntity());
                                    }
                                }
                            }
                        }
                        if(!chal){
                            final Land land = new Land(u.getPlayer().getLocation());
                            if(land != null){
                                if(k.getLand().contains(land)){
                                    if(Feudal.getConfiguration().getConfig().getBoolean("kingdom.keepInventoryOnLand")){
                                        if(!Feudal.getVersion().equals("1.7")) {
                                            UtilsAbove1_7.keepInventory(event);
                                        }
                                        checkEnchantPlus(event.getEntity());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static void checkEnchantPlus(Player entity) {
        if(Bukkit.getPluginManager().isPluginEnabled("EnchantsPlus")){
            final ArrayList<ItemStack> remove = new ArrayList<>();
            for(final ItemStack i : remove){
                if(entity.getInventory().contains(i)){
                    entity.getInventory().remove(i);
                }
            }
        }
    }

	/*public static void respawn(PlayerRespawnEvent event){
		if(items.containsKey(event.getPlayer().getUniqueId().toString())){
			event.getPlayer().getInventory().clear();
            for(ItemStack stack : items.get(event.getPlayer().getUniqueId().toString())){
                event.getPlayer().getInventory().addItem(stack);
            }

            items.remove(event.getPlayer());
		}
	}*/

    public static void blockBreak(BlockBreakEvent event) {
        if(event.getBlock() != null && event.getBlock().getType() != null &&
                ((!Feudal.getVersion().equals("1.7") && event.getBlock().getType().equals(XMaterial.WHITE_BANNER.parseItem())) ||

                        (Feudal.getVersion().equals("1.7") && event.getBlock().getType().equals(XMaterial.NOTE_BLOCK.parseItem()))

                )
        ){
            for(final Challenge c : Feudal.getChallenges()){
                if(c.getBannerLocation() != null && c.getBannerLocation().getBlock() != null && c.getBannerLocation().getBlock().equals(event.getBlock())){
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(Feudal.getMessage("challenge.breakbanner"));
                    break;
                }
            }
        }
    }

}