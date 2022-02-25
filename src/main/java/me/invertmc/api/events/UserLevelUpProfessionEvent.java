package me.invertmc.api.events;
import me.invertmc.user.User;
import me.invertmc.user.classes.Profession;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class UserLevelUpProfessionEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private User user;
    private int level;
    private Profession profession;

    public UserLevelUpProfessionEvent(User user, Profession profession, int level) {
        this.user = user;
        this.level = level;
        this.profession = profession;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Get the @User who just leveld up.
     * @return Feudal @User who leveled up.
     */
    public User getUser() {
        return user;
    }

    /**
     * Get the level the player just reached.
     * @return level reached
     */
    public int getLevel(){
        return level;
    }

    /**
     * Get the @Profession which the player just leveled up.
     * @return @Profession leveled up
     */
    public Profession getProfession(){
        return profession;
    }

}