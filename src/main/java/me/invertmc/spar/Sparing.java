package me.invertmc.spar;

import java.util.ArrayList;

import me.invertmc.Feudal;
import me.invertmc.user.User;
import me.invertmc.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;


public class Sparing {
    private static ArrayList<Spar> spars = new ArrayList<Spar>();

    public static void sparCommand(Player p, String who, double bet){
        ArrayList<Spar> remove = new ArrayList<Spar>();
        for(Spar s : spars){
            if(s.hasExpired()){
                remove.add(s);
            }
        }
        for(Spar s : remove){
            spars.remove(s);
        }

        for(Spar s : spars){
            if(s.hasPlayer(p)){
                if(s.isAccepted()){
                    p.sendMessage(Feudal.getMessage("spar.inSpar"));
                    return;
                }else{
                    User player1 = s.getPlayer1();
                    if(player1 != null && player1.getName().equalsIgnoreCase(who)){
                        User u = Feudal.getUser(p.getUniqueId().toString());
                        if(u == null){
                            p.sendMessage(Feudal.getMessage("spar.myUser"));
                            return;
                        }
                        if(u.getBalance() >= s.getBet()){
                            if(s.checkPosition()){
                                u.removeMoney(s.getBet());
                                p.sendMessage(Feudal.getMessage("spar.accept").replace("%bet%", ""+s.getBet()).replace("%p%", player1.getName()));
                                player1.getPlayer().sendMessage(Feudal.getMessage("spar.accept").replace("%bet%", ""+s.getBet()).replace("%p%", p.getName()));
                                s.begin();
                            }else{
                                s.setExpired(true);
                                p.sendMessage(Feudal.getMessage("spar.tooFar").replace("%a%", ""+Feudal.getConfiguration().getConfig().getInt("spar.maxDistance")));
                            }
                            return;
                        }else{
                            p.sendMessage(Feudal.getMessage("spar.noBetMoney"));
                            return;
                        }
                    }else{
                        p.sendMessage(Feudal.getMessage("spar.inPendingSpar"));
                        return;
                    }
                }
            }
        }

        User u = Feudal.getUser(p.getUniqueId().toString());
        if(u == null){
            p.sendMessage(Feudal.getMessage("spar.myUser"));
            return;
        }
        Player p2 = Bukkit.getPlayer(who);
        if(p2 != null){

            if(p2.equals(p)) {
                p.sendMessage(Feudal.getMessage("spar.sparSelf"));
                return;
            }

            User u2 = Feudal.getUser(p2.getUniqueId().toString());
            if(u2 != null){
                for(Spar s : spars){
                    if(s.hasPlayer(p2)){
                        p.sendMessage(Feudal.getMessage("spar.theyInSpar"));
                        return;
                    }
                }
                if(u.getBalance() >= bet){
                    u.removeMoney(bet);
                    p.sendMessage(Feudal.getMessage("spar.request").replace("%p%", p2.getName()).replace("%bet%", bet+"").replace("%timeout%", ""+Feudal.getConfiguration().getConfig().getInt("spar.timeoutseconds")));
                    p2.sendMessage(Feudal.getMessage("spar.requestRecieve").replace("%p%", p.getName()).replace("%bet%", bet+"").replace("%timeout%", ""+Feudal.getConfiguration().getConfig().getInt("spar.timeoutseconds")));
                    Spar spar = new Spar(u, u2, bet);
                    if(spar.checkPosition()){
                        spar.startTimer();
                        spars.add(spar);
                    }else{
                        spar.setExpired(true);
                        p.sendMessage(Feudal.getMessage("spar.tooFar").replace("%a%", ""+Feudal.getConfiguration().getConfig().getInt("spar.maxDistance")));
                    }
                }else{
                    p.sendMessage(Feudal.getMessage("spar.noBetMoney"));
                }
            }else{
                p.sendMessage(Feudal.getMessage("spar.noUser"));
            }
        }else{
            p.sendMessage(Feudal.getMessage("spar.offline"));
        }

    }

    public static void leave(PlayerQuitEvent event){
        for(Spar s : spars){
            if(s.hasPlayer(event.getPlayer())){
                s.end(event.getPlayer());
            }
        }
    }

    public static void move(PlayerMoveEvent event){
        if(!event.isCancelled()){
            if(event.getTo().getBlockX() != event.getFrom().getBlockX() || event.getTo().getBlockY() != event.getFrom().getBlockY() || event.getTo().getBlockZ() != event.getFrom().getBlockZ()){
                for(Spar s : spars){
                    if(s.hasPlayer(event.getPlayer())){
                        s.checkPosition(event.getPlayer());
                    }
                }
            }
        }
    }

    public static void teleport(PlayerTeleportEvent event){
        if(!event.isCancelled()){
            for(Spar s : spars){
                if(s.hasPlayer(event.getPlayer())){
                    s.checkPosition(event.getPlayer());
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    public static void damage(EntityDamageByEntityEvent event){
        if(!event.isCancelled()){
            if(event.getEntity() instanceof Player && event.getDamager() instanceof Player){
                Player p1 = ((Player) event.getEntity());
                if(((Utils.getHealth(p1)) / Utils.getMaxHealth(p1) * 100) <= Feudal.getConfiguration().getConfig().getDouble("spar.percentHealthWin") || (Utils.getHealth(p1) - event.getDamage() <= 0)){
                    for(Spar s : spars){
                        if(s.hasPlayer(p1) && s.hasPlayer((Player) event.getDamager())){
                            event.setDamage(0);
                            p1.setHealth(Utils.getMaxHealth(p1)/2);
                            s.end(p1);
                        }
                    }
                }
            }else if(event.getEntity() instanceof Player && event.getDamager() instanceof Projectile) {
                Projectile projectile = (Projectile) event.getDamager();
                if(projectile.getShooter() instanceof Player) {
                    Player p1 = ((Player) event.getEntity());
                    if(((Utils.getHealth(p1)) / Utils.getMaxHealth(p1) * 100) <= Feudal.getConfiguration().getConfig().getDouble("spar.percentHealthWin") || (Utils.getHealth(p1) - event.getDamage() <= 0)){
                        for(Spar s : spars){
                            if(s.hasPlayer(p1) && s.hasPlayer((Player) projectile.getShooter())){
                                event.setDamage(0);
                                p1.setHealth(Utils.getMaxHealth(p1)/2);
                                s.end(p1);
                            }
                        }
                    }
                }
            }
        }
    }

    public static boolean areSparing(Player p1, Player p2){
        for(Spar s : spars){
            if(s.hasPlayer(p1) && s.hasPlayer(p2)){
                if(s.isAccepted()){
                    return true;
                }else{
                    return false;
                }
            }
        }
        return false;
    }
}
