package me.invertmc.api.events;

import me.invertmc.user.User;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ReputationChangeEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private User user;
    private int change;
    private int currentReputation;
    private String reason;
    private boolean cancel = false;

    public ReputationChangeEvent(User user, int change, String reason) {
        this.user = user;
        this.change = change;
        this.reason = reason;
        currentReputation = user.getReputation();
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Get the @User who just gained xp.
     * @return Feudal @User who just gained xp.
     */
    public User getUser() {
        return user;
    }

    /**
     * Get the amount reputation will be changed.
     * <br><br>
     * This will be positive if gaining reputation, and it will be negative if losing reputation.
     *
     * @return reputation change
     */
    public int getReputationChange(){
        return change;
    }

    /**
     * Set amount reputation will be changed.
     *
     */
    public void setReputationChange(int change){
        this.change = change;
    }

    /**
     * Get the current reputation for the @User.
     * @return current reputation
     */
    public int getCurrentReputation(){
        return currentReputation;
    }

    /**
     * Get the reason the reputation was changed.
     * @return reason
     */
    public String getReason(){
        return reason;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        cancel = cancelled;
    }

}