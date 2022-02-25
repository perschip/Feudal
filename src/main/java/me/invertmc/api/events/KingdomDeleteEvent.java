package me.invertmc.api.events;

import me.invertmc.kingdoms.Kingdom;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class KingdomDeleteEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private Kingdom kingdom;
    private String name;

    public KingdomDeleteEvent(Kingdom kingdom, String name) {
        this.name = name;
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
     * Gets the name of the person who is deleting this kingdom.
     * @return @User May be null or empty
     */
    public String getPersonDeleting() {
        return name;
    }

    /**
     * Gets the @Kingdom deleted.
     * @return @Kingdom deleted
     */
    public Kingdom getKingdom() {
        return kingdom;
    }

}
