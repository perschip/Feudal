package me.invertmc.api.events.custom;

import me.invertmc.user.User;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MoneyGetEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private User user;
    private double money;
    private boolean override;

    public MoneyGetEvent(User user) {
        this.user = user;
        this.money = 0;
        this.override = false;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

    public void setOverride(boolean c){
        override = c;
    }

    public boolean isOverrided(){
        return override;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public User getUser() {
        return user;
    }

    /**
     * This will be 0 unless something else changes it.
     * @return
     */
    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

}