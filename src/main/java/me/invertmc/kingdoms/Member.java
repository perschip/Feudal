package me.invertmc.kingdoms;

import java.util.UUID;

import me.invertmc.Feudal;
import me.invertmc.user.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class Member implements Comparable<Member> {

    private OfflinePlayer player;
    private User user;
    private Rank rank;
    private boolean fighter;

    public Member(String uuid, Rank rank, boolean fighter) throws Exception {
        player = Bukkit.getOfflinePlayer(UUID.fromString(uuid));

        if(player.isOnline()) {
            user = Feudal.getUser(uuid);
        }

        this.rank = rank;
        this.fighter = fighter;
    }

    public boolean isFighter() {
        return fighter;
    }

    public OfflinePlayer getPlayer() {
        return player;
    }

    public User getUser() {
        if(user == null) {
            return Feudal.getUser(player.getUniqueId().toString());
        }
        return user;
    }

    public Rank getRank() {
        return rank;
    }

    public boolean isOnline() {
        return player.isOnline();
    }

    @Override
    public int compareTo(Member member) {
        if(this.isOnline() && !member.isOnline()) {
            return 1;
        }else if(!this.isOnline() && member.isOnline()) {
            return -1;
        }else {
            return this.rank.ID - member.rank.ID;
        }
    }

    public boolean isSameMember(Member member) {
        if(user != null && member.getUser() != null && member.getUser().getUUID().equals(this.getUser().getUUID())) {
            return true;
        }else {
            return false;
        }
    }

    public void setTempRank(Rank newRank) {
        rank = newRank;
    }

}