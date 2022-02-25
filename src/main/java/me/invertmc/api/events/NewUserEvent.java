package me.invertmc.api.events;

import me.invertmc.user.User;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NewUserEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private User user;

    public NewUserEvent(User user) {
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
     * Gets the Feudal @User who is claiming land.
     * @return @User claiming land
     */
    public User getUser() {
        return user;
    }

}
