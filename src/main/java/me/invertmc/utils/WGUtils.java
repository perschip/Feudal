package me.invertmc.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;


public class WGUtils {
    private static WorldGuardPlugin worldGuard = null;

    public static void setupWG(){
        worldGuard = getWG();
    }

    private static WorldGuardPlugin getWG() {
        final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");

        // WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null; // Maybe you want throw an exception instead
        }

        return (WorldGuardPlugin) plugin;
    }

	/*public static WorldGuardPlugin getWorldGuard(){
		return worldGuard;
	}*/

    public static boolean hasWG() {
        return worldGuard!=null;
    }
/*
	public static boolean canBuild(Player p, Block b) {
		return worldGuard.canBuild(p, b);
	}

	public static boolean canBuild(Player p, Location location) {
		return worldGuard.canBuild(p, location);
	}*/

    public static boolean canBuild(Player p, Location loc) {
        final RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        final ApplicableRegionSet regionSet = container.createQuery().getApplicableRegions(BukkitAdapter.adapt(loc));
        final LocalPlayer wgplayer = getWG().wrapPlayer(p);
        for (final ProtectedRegion region : regionSet.getRegions()) {
            if (region.isMember(wgplayer) || region.isOwner(wgplayer)) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    public static boolean canBuild(Player p, Block b) {
        final RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        final ApplicableRegionSet regionSet = container.createQuery().getApplicableRegions(null);
        final LocalPlayer wgplayer = getWG().wrapPlayer(p);
        for (final ProtectedRegion region : regionSet.getRegions()) {
            if (region.isMember(wgplayer) || region.isOwner(wgplayer)) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

}