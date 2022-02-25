package me.invertmc.utils;


import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;


public class VaultUtils {
    private static Economy economy = null;
    private static Permission permission = null;

    public static boolean setupEconomy(){
        try{
            RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
            if (economyProvider != null) {
                economy = economyProvider.getProvider();
            }

            RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
            if (permissionProvider != null) {
                permission = permissionProvider.getProvider();
            }
        }catch(Exception e){
            return (economy != null);
        }

        return (economy != null);
    }

    public static Economy getEconomy() {
        return economy;
    }

    public static boolean hasEconomy() {
        return economy!=null;
    }

    public static Permission getPerms() {
        return permission;
    }

    public static boolean hasPerms() {
        return permission!=null;
    }

    public static void addPermission(Player player, String perm){
        permission.playerAdd(player, perm);
    }

    public static void removePermission(Player player, String perm){
        try{
            permission.playerRemove(player, perm);
        }catch(Exception e){

        }
    }

    public static boolean hasAccount(OfflinePlayer player) {
        return economy.hasAccount(player);
    }

    public static double balance(OfflinePlayer player) {
        return economy.getBalance(player);
    }

    public static void deposit(OfflinePlayer offlinePlayer, double money) {
        economy.depositPlayer(offlinePlayer, money);
    }

    public static void withdraw(OfflinePlayer offlinePlayer, double money) {
        economy.withdrawPlayer(offlinePlayer, money);
    }
}