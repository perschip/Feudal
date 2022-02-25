package me.invertmc.api.events;

import me.invertmc.user.User;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UserCreateEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private User user;

    public UserCreateEvent(User user) {
        this.user = user;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Get the @User who just setup their character.
     * @return Feudal @User who created their character.
     */
    public User getUser() {
        return user;
    }

}