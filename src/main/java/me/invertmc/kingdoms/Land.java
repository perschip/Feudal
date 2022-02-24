package me.invertmc.kingdoms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.cryptomorin.xseries.XMaterial;
import me.invertmc.Feudal;
import me.invertmc.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;


public class Land {
    private final int x;
    private final int z;
    private final World world;
    private final String worldName;

    private static ArrayList<Block> marks = new ArrayList<>();
    private static Map<PlayerContainer, Long> timePlayer = new HashMap<>();

    /**
     * New land from x, z, and world x and z being chunk cords.
     * @param x
     * @param z
     * @param world
     */
    public Land(int x, int z, World world, String worldName){
        this.x = x;
        this.z = z;
        this.world = world;
        this.worldName = worldName;
    }

    /**
     * New land from a location.
     * @param location
     */
    public Land(Location location) {
        this.x = location.getChunk().getX();
        this.z = location.getChunk().getZ();
        this.world = location.getWorld();
        this.worldName = world.getName();
    }

    public World getWorld() {
        if(!Utils.isWorldLoaded(getWorldName())){
            Utils.loadWorld(getWorldName());
        }
        return world;
    }

    public int getZ() {
        return z;
    }

    public int getX() {
        return x;
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null){
            return false;
        }else if(getClass() != obj.getClass()){
            return false;
        }
        if(obj instanceof Land){
            final Land other = (Land) obj;
            if(other.getWorldName().equals(worldName) && other.getX() == x && other.getZ() == z){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    /**
     * Check if this land is connected to another piece of land.
     * @param land2
     * @return
     */
    public boolean isConnected(Land land2) {
        if(land2.getWorldName().equals(this.worldName)){
            if(land2.getX() == this.x){
                if(land2.getZ()-1 == this.z || land2.getZ()+1 == this.z){
                    return true;
                }else{
                    return false;
                }
            }else if(land2.getZ() == this.z){
                if(land2.getX()-1 == this.x || land2.getX()+1 == this.x){
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    /**
     * Get all land marks. Marks are blocks used to show a claim.
     * @return
     */
    public static ArrayList<Block> getMarks(){
        return marks;
    }

	/**
	 * Visually mark some land.
	 * @param c
	 * @param y
	 * @deprecated
	 */
	public static void markChunk(Chunk c, int y) {
		final Block blocks[] = new Block[]{c.getBlock(0, y, 0),
				c.getBlock(2, y, 0),
				c.getBlock(4, y, 0),
				c.getBlock(6, y, 0),
				c.getBlock(8, y, 0),
				c.getBlock(10, y, 0),
				c.getBlock(12, y, 0),
				c.getBlock(14, y, 0),
				c.getBlock(15, y, 1),
				c.getBlock(15, y, 3),
				c.getBlock(15, y, 5),
				c.getBlock(15, y, 7),
				c.getBlock(15, y, 9),
				c.getBlock(15, y, 11),
				c.getBlock(15, y, 13),
				c.getBlock(15, y, 15),
				c.getBlock(13, y, 15),
				c.getBlock(11, y, 15),
				c.getBlock(9, y, 15),
				c.getBlock(7, y, 15),
				c.getBlock(5, y, 15),
				c.getBlock(3, y, 15),
				c.getBlock(1, y, 15),
				c.getBlock(0, y, 2),
				c.getBlock(0, y, 4),
				c.getBlock(0, y, 6),
				c.getBlock(0, y, 8),
				c.getBlock(0, y, 10),
				c.getBlock(0, y, 12),
				c.getBlock(0, y, 14)};
		for(Block b : blocks){
			if(b != null && b.getType().equals(Material.AIR)){
				marks.add(b);
				b.setType(Material.OAK_FENCE);
			}
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(Feudal.getPlugin(Feudal.class), new Runnable(){
			@Override
			public void run() {
				for(Block b : blocks){
					if(b != null && marks.contains(b)){
						b.setType(Material.AIR);
						marks.remove(b);
					}
				}
			}
		}, 150);
	}

    public String getWorldName() {
        return worldName;
    }

    public double distanceSquared(Chunk chunk) {
        if(!chunk.getWorld().equals(this.world)) {
            return Double.MAX_VALUE;
        }
        return Math.pow((chunk.getX()-x), 2) + Math.pow((chunk.getZ()-z), 2);
    }

    public double distanceSquared(Land land) {
        if(!land.getWorld().equals(this.world)) {
            return Double.MAX_VALUE;
        }
        return Math.pow((land.getX()-x), 2) + Math.pow((land.getZ()-z), 2);
    }

    @SuppressWarnings("deprecation")
    public void show(Player p) {

        final List<PlayerContainer> remove = new ArrayList<>();
        for(final PlayerContainer uuid : timePlayer.keySet()) {
            if(System.currentTimeMillis() - timePlayer.get(uuid) > 9000) {
                remove.add(uuid);
            }
        }
        for(final PlayerContainer u : remove) {
            timePlayer.remove(u);
        }

        final PlayerContainer pc = new PlayerContainer(p.getUniqueId(), this);
        for(final PlayerContainer con : timePlayer.keySet()) {
            if(con.equals(pc)) {
                return;
            }
        }

        timePlayer.put(pc, System.currentTimeMillis());

        final int y = p.getEyeLocation().getBlockY();
        final Chunk c = p.getWorld().getChunkAt(x, z);
        final Block blocks[] = new Block[]{c.getBlock(0, y, 0),
                c.getBlock(2, y, 0),
                c.getBlock(4, y, 0),
                c.getBlock(6, y, 0),
                c.getBlock(8, y, 0),
                c.getBlock(10, y, 0),
                c.getBlock(12, y, 0),
                c.getBlock(14, y, 0),
                c.getBlock(15, y, 1),
                c.getBlock(15, y, 3),
                c.getBlock(15, y, 5),
                c.getBlock(15, y, 7),
                c.getBlock(15, y, 9),
                c.getBlock(15, y, 11),
                c.getBlock(15, y, 13),
                c.getBlock(15, y, 15),
                c.getBlock(13, y, 15),
                c.getBlock(11, y, 15),
                c.getBlock(9, y, 15),
                c.getBlock(7, y, 15),
                c.getBlock(5, y, 15),
                c.getBlock(3, y, 15),
                c.getBlock(1, y, 15),
                c.getBlock(0, y, 2),
                c.getBlock(0, y, 4),
                c.getBlock(0, y, 6),
                c.getBlock(0, y, 8),
                c.getBlock(0, y, 10),
                c.getBlock(0, y, 12),
                c.getBlock(0, y, 14)};
        for(final Block b : blocks){
            if(b != null && b.getType().equals(XMaterial.AIR.parseItem())){
                if(!marks.contains(b)) {
                    marks.add(b);
                    p.sendBlockChange(b.getLocation(), XMaterial.WHITE_WOOL.parseMaterial(), (byte) 0);
                    //b.setType(Material.WOOL);
                }
            }
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(Feudal.getPlugin(Feudal.class), () -> {
            for(final Block b : blocks){
                if(b != null && marks.contains(b)){
                    p.sendBlockChange(b.getLocation(), b.getType(), b.getData());
                    marks.remove(b);
                }
            }
        }, 150);

    }

    private static class PlayerContainer{

        private final UUID uuid;
        private final Land land;

        public PlayerContainer(UUID uuid, Land land) {
            this.uuid = uuid;
            this.land = land;
        }

        @Override
        public boolean equals(Object object) {
            if(object instanceof PlayerContainer) {
                if(uuid.equals(((PlayerContainer) object).uuid) && land.equals(((PlayerContainer) object).land)) {
                    return true;
                }
            }
            return false;
        }

    }
}
