package me.invertmc.api.events;
import me.invertmc.user.User;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;



public class XPGainEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private User user;
    private int xp;
    private int origionalXp;
    private boolean cancel = false;

    public XPGainEvent(User user, int xp) {
        this.user = user;
        this.xp = xp;
        this.origionalXp = xp;
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
     * Get the amount of xp gained.
     * @return xp
     */
    public int getXp(){
        return xp;
    }

    /**
     * Get the origional xp gained incase another plugin changed the xp value.
     * @return
     */
    public int getOrigionalXp(){
        return origionalXp;
    }

    /**
     * Set the xp gained.
     * @param xp
     */
    public void setXp(int xp){
        this.xp = xp;
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
