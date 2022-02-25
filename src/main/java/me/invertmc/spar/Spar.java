package me.invertmc.spar;

import me.invertmc.Feudal;
import me.invertmc.user.User;
import me.invertmc.utils.ErrorManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;


public class Spar {
    private User player1;
    private User player2;
    private double bet = 0;
    private boolean accepted = false;
    private boolean expired = false;

    public Spar(User p1, User p2, double bet){
        player1 = p1;
        player2 = p2;
        this.bet = bet;
    }

    public void startTimer(){
        Bukkit.getScheduler().scheduleSyncDelayedTask(Feudal.getPlugin(), new Runnable(){

            @Override
            public void run() {
                try{
                    if(!accepted && !expired){
                        Spar.this.timeout();
                    }
                }catch(Exception e){
                    ErrorManager.error(95, e);
                }
            }

        }, Feudal.getConfiguration().getConfig().getInt("spar.timeoutseconds")*20);
    }

    protected void timeout() {
        if(player1.getPlayer() != null){
            player1.getPlayer().sendMessage(Feudal.getMessage("spar.timeout"));
        }
        expired = true;
        player1.addMoney(bet);
    }

    public boolean isAccepted(){
        return accepted;
    }

    public boolean hasExpired(){
        return expired;
    }

    public boolean checkPosition(){
        if(hasExpired()){
            return false;
        }
        Player p1 = player1.getPlayer();
        Player p2 = player2.getPlayer();
        if(p1 != null && p2 != null){
            if(p1.getWorld() != null && p2.getWorld() != null && p1.getWorld().equals(p2.getWorld())){
                if(p1.getLocation().distanceSquared(p2.getLocation()) > Math.pow(Feudal.getConfiguration().getConfig().getDouble("spar.maxDistance"), 2)){
                    return false;
                }
            }else{
                return false;
            }
        }else{
            return false;
        }
        return true;
    }

    public void checkPosition(Player p){
        if(hasExpired()){
            return;
        }
        Player p1 = player1.getPlayer();
        Player p2 = player2.getPlayer();
        if(p1 != null && p2 != null){
            if(p1.getWorld() != null && p2.getWorld() != null && p1.getWorld().equals(p2.getWorld())){
                if(p1.getLocation().distanceSquared(p2.getLocation()) > Math.pow(Feudal.getConfiguration().getConfig().getDouble("spar.maxDistance"), 2)){
                    end(p);
                }
            }else{
                end(p);
            }
        }else{
            end(null);
        }
    }

    public void begin(){
        if(player1.getPlayer() != null){
            soundWin(player1.getPlayer());
        }
        if(player2.getPlayer() != null){
            soundWin(player2.getPlayer());
        }
        accepted = true;
        bet *= 2;
    }

    //End with a winner (can be null)
    public void end(Player loser) {
        if(loser != null && hasPlayer(loser)){
            if(player1.getPlayer() != null){
                if(loser.equals(player1.getPlayer())){
                    soundLose(player1.getPlayer());
                    player1.getPlayer().sendMessage(Feudal.getMessage("spar.lose").replace("%p%", player2.getName()).replace("%bet%", bet+""));
                }else{
                    soundWin(player1.getPlayer());
                    player1.getPlayer().sendMessage(Feudal.getMessage("spar.win").replace("%p%", player2.getName()).replace("%bet%", bet+""));
                    player1.addMoney(bet);
                }
            }
            if(player2.getPlayer() != null){
                if(loser.equals(player2.getPlayer())){
                    soundLose(player2.getPlayer());
                    player2.getPlayer().sendMessage(Feudal.getMessage("spar.lose").replace("%p%", player1.getName()).replace("%bet%", bet+""));
                }else{
                    soundWin(player2.getPlayer());
                    player2.getPlayer().sendMessage(Feudal.getMessage("spar.win").replace("%p%", player1.getName()).replace("%bet%", bet+""));
                    player2.addMoney(bet);
                }
            }
        }else{
            if(player1.getPlayer() != null){
                soundLose(player1.getPlayer());
                player1.getPlayer().sendMessage(Feudal.getMessage("spar.winNoWinner"));
                player1.addMoney(bet/2);
            }
            if(player2.getPlayer() != null){
                soundLose(player2.getPlayer());
                player2.getPlayer().sendMessage(Feudal.getMessage("spar.winNoWinner"));
                player2.addMoney(bet/2);
            }
        }

        expired = true;
    }

    private void soundLose(Player p){
        if(!Feudal.getVersion().contains("1.8") && !Feudal.getVersion().equals("1.7")){
            p.playSound(p.getLocation(), Sound.valueOf("BLOCK_ANVIL_LAND"), 1, 0.5F);
        }else{
            p.playSound(p.getLocation(), Sound.valueOf("ANVIL_LAND"), 1, 0.5F);
        }
    }

    private void soundWin(Player p){
        if(!Feudal.getVersion().contains("1.8") && !Feudal.getVersion().equals("1.7")){
            p.playSound(p.getLocation(), Sound.valueOf("ENTITY_PLAYER_LEVELUP"), 1, 0.5F);
        }else{
            p.playSound(p.getLocation(), Sound.valueOf("LEVEL_UP"), 1, 0.5F);
        }
    }

    public boolean hasPlayer(Player player) {
        if(hasExpired()){
            return false;
        }
        if(player1.getPlayer() != null && player2.getPlayer() != null && (player1.getPlayer().equals(player) || player2.getPlayer().equals(player))){
            return true;
        }else{
            return false;
        }
    }

    public User getPlayer2() {
        return player2;
    }

    public User getPlayer1() {
        return player1;
    }

    public double getBet() {
        return bet;
    }

    public void setExpired(boolean b) {
        expired = b;
    }
}