package me.invertmc.api.events.custom;

import me.invertmc.user.User;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MoneyRemoveEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private User user;
    private double money;
    private boolean override;

    public MoneyRemoveEvent(User user, double money) {
        this.user = user;
        this.money = money;
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

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

}