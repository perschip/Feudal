package me.invertmc.api.events;

import me.invertmc.kingdoms.Kingdom;
import me.invertmc.user.User;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LeaveKingdomEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private Kingdom kingdom;
    private User user;

    public LeaveKingdomEvent(Kingdom kingdom, User user) {
        this.user = user;
        this.kingdom = kingdom;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Gets the Feudal @User who left a kingdom
     * @return @User who left kingdom
     */
    public User getUser() {
        return user;
    }

    /**
     * Gets the @Kingdom left.
     * @return @Kingdom left
     */
    public Kingdom getKingdom() {
        return kingdom;
    }

}
