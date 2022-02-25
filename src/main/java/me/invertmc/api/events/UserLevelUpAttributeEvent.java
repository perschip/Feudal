package me.invertmc.api.events;

import me.invertmc.user.User;
import me.invertmc.user.attributes.Attribute;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UserLevelUpAttributeEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private User user;
    private int level;
    private Attribute attribute;

    public UserLevelUpAttributeEvent(User user, Attribute attribute, int level) {
        this.user = user;
        this.level = level;
        this.attribute = attribute;
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
     * Get the @Attribute which the player just leveled up.
     * @return @Attribute leveled up
     */
    public Attribute getAttribute(){
        return attribute;
    }

}
