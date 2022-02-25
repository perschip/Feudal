package me.invertmc.managergui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public abstract class InventoryGui2{
    private static InventoryEvents instanceEnable = null;//This instance runs the Events needed for InventoryGui

    private Inventory inventory;//Bukkit inventory
    private final Map<ItemStack, String> items = new HashMap<>();//ITEM : TAG - Items and tags saved to the inventory.
    private boolean complete = false;//If true, the inventory will be removed on inventory click or inventory close of any inventory.
    private boolean refreshing = false;//If true, the inventory will not be removed on inventory close
    private final Player player;

    /**
     * Create an instance of InventoryGui with Player as the owner of this inventory.
     * <p>
     * The inventory must be a multiple of 9, but no greater than 54.
     * <p>
     * The inventory is a Bukkit
     *
     * @param inventory Bukkit Inventory
     * @param player Bukkit Player
     * @throws Exception if InventoryGui not yet enabled
     */
    public InventoryGui2(Inventory inventory, Player player) throws Exception{
        this.player = player;
        this.inventory = inventory;
        if(instanceEnable != null){
            instanceEnable.guis.add(this);
        }else{
            throw new Exception("Internal error - InventoryGui class must be enabled before it can be used!");
        }
    }

    /**
     * Gets the player this inventory is for
     * @return Bukkit Player
     */
    public Player getPlayer(){
        return player;
    }

    /**
     * Resets the inventory as a new inventory.
     * <p>
     * This will clear the map of items and set complete to false and refreshing to false.
     * @param inventory Bukkit Inventory
     */
    public void resetInventory(Inventory inventory){
        this.inventory = inventory;
        items.clear();
        complete = false;
        refreshing = false;
    }

    /**
     * Get map of items used to indicate which item is which
     * @return
     */
    public Map<ItemStack, String> getItemMap(){
        return items;
    }

    /**
     * Sets an item in the items hashmap and inventory
     * @param name item tag/name
     * @param index item index in the inventory
     * @param item Bukkit ItemStack
     */
    public ItemStack setItem(String name, int index, ItemStack item){

        final ItemMeta meta = item.getItemMeta();
        if(item.getType().equals(XMaterial.PLAYER_HEAD.parseItem()) && meta != null){
            List<String> lore = new ArrayList<>();
            if(meta.getLore() != null){
                lore = meta.getLore();
            }
            String metaName = "";
            final Random rand = new Random();
            final String time = System.nanoTime() + "";
            final String start = rand.nextInt(99) + (time.length() > 12 ? time.substring(time.length() - 13, time.length()) : time) + "";
            String end = "";
            for(final char c : start.toCharArray()){
                end += "�" + c;
            }
            end += "�r";
            metaName=end;
            lore.add(metaName);
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        inventory.setItem(index, item);
        items.put(item, name);
        return item;
    }

    /**
     * Gets the bukkit inventory.
     * @return Bukkit Inventory
     */
    public Inventory getInventory(){
        return inventory;
    }

    /**
     * Gets an item string saved in the items hashmap.
     * @param item Bukkit ItemStack
     * @return Item Tag
     */
    public String getItemString(ItemStack item){
        if(item != null && item.getType().equals(XMaterial.PLAYER_HEAD.parseItem()) && item.getItemMeta() != null && item.getItemMeta().getLore() != null){
            for(final ItemStack i : items.keySet()){
                if(i.getType().equals(XMaterial.PLAYER_HEAD.parseItem()) && i.getItemMeta() != null && i.getItemMeta().getLore() != null){
                    if(i.getItemMeta().getLore().size() == item.getItemMeta().getLore().size()){
                        boolean equal = true;
                        for(int j = 0; j < i.getItemMeta().getLore().size(); j++){
                            if(!i.getItemMeta().getLore().get(j).equals(item.getItemMeta().getLore().get(j))){
                                equal = false;
                            }
                        }
                        if(equal){
                            return items.get(i);
                        }
                    }
                }
            }
            return null;
        }else{
            for(final ItemStack is : items.keySet()){
                if(is != null){
                    if(is.equals(item)){
                        return items.get(is);
                    }
                }
            }
            return null;
        }
    }

    /**
     * Creates an item with a type, amount, durability, name, and lore.
     * @param type Bukkit Material
     * @param amount item amount. If it is less than1, it will become 1.
     * @param data item durability
     * @param name item name
     * @param lore item lore as java.util.List
     * @return Bukkit ItemStack
     */
    public static ItemStack createItem(Material type, int amount, int data, String name, List<String> lore){
        if(amount < 1){
            amount = 1;
        }
        final ItemStack i = new ItemStack(type, amount, (short)data);
        if(name != null || lore != null){
            final ItemMeta meta = i.getItemMeta();
            if(name != null){
                meta.setDisplayName(name);
            }
            if(lore != null){
                meta.setLore(lore);
            }
            i.setItemMeta(meta);
        }
        return i;
    }

    /**
     * Get an item stack from the inventory based on an item tag
     * @param itemString Item Tag used for the inventory hashmap
     * @return Bukkit ItemStack
     */
    public ItemStack getItemStack(String itemString){
        for(final ItemStack stack : items.keySet()){
            if(items.get(stack).equals(itemString)){
                return stack;
            }
        }
        return null;
    }

    /**
     * Clears items hashmap.
     */
    public void clear(){
        items.clear();
    }

    /**
     * Limit a string to 31 characters.
     * <p>
     * This is used because an inventory title must be less than 31 characters.
     * <p>
     * This method is not automatic.
     *
     * @param message
     * @return new string reduced in length
     */
    public static String lim(String message) {//Limit to 31 characters max
        if(message.length() > 31){
            return message.substring(0, 31);
        }else{
            return message;
        }
    }

    /**
     * Run the click method if the item tag is not null.
     * @param itemString item name / item tag
     */
    public void clickCheck(String itemString) {
        if(itemString != null){
            click(itemString);
        }
    }

    /**
     * Reload this inventory to be reused.
     * <p>
     * Call this before reopening this menu.
     * <p>
     * Sets complete to false. Sets refreshing to false.
     * Clears the inventory items. Adds this inventory instance back to the events list.
     * Calls the abstract method reinitiate, so a sub class can reload/reinitiate their variables.
     */
    public void reload(){
        complete = false;
        refreshing = false;
        clear();
        inventory.clear();
        instanceEnable.guis.add(this);
        reinitiate();
    }

    /**
     * Enable InventoryGui for use in this plugin. This must be called before creating an instance of InventoryGui.
     * @param plugin Bukkit Plugin
     */
    public static void enable(Plugin plugin){
        instanceEnable = new InventoryEvents(plugin);
    }

    /**
     * This method should be called in onDisable method in the plugin.
     * This will close all player's inventories who are using an InventoryGui to prevent stealing items.
     */
    public static void disable() {
        if(instanceEnable != null){
            for(final InventoryGui2 gui : instanceEnable.guis){
                if(gui.getPlayer() != null){
                    gui.getPlayer().closeInventory();
                }
            }
        }
    }

    /**
     *
     * This class is used to manage the events for InventoryGui without creating many event handlers for each InventoryGui instance.
     *
     * @author Michael Forseth
     *
     */
    static class InventoryEvents implements Listener{

        private final List<InventoryGui2> guis = new ArrayList<>();

        public InventoryEvents(Plugin plugin){
            final PluginManager m = plugin.getServer().getPluginManager();
            m.registerEvents(this, plugin);
        }

        @EventHandler
        public void inventoryClickEvent(InventoryClickEvent event) {
            try{
                final List<InventoryGui2> guis = new ArrayList<>();
                for(final InventoryGui2 gui : this.guis){
                    guis.add(gui);
                }
                if(guis.size() > 0){
                    final List<InventoryGui2> remove = new ArrayList<>();
                    for(final InventoryGui2 gui : guis){
                        if(gui.complete){
                            remove.add(gui);
                        }else{
                            if(gui.player.equals(event.getWhoClicked())){
                                if(gui.getInventory() != null && event.getInventory().equals(gui.getInventory())){
                                    final boolean cancel = true;
                                    final String itemString = gui.getItemString(event.getCurrentItem());
                                    if(itemString != null){
                                        gui.click(itemString);
                                    }
                                    if(gui.complete){
                                        remove.add(gui);
                                    }
                                    event.setCancelled(cancel);
                                }
                            }
                        }
                    }
                    for(final InventoryGui2 gui : remove){
                        this.guis.remove(gui);
                    }
                }
            }catch(final Exception ee){
                ee.printStackTrace();;
            }
        }

        @EventHandler
        public void close(InventoryCloseEvent event) {
            try{
                final List<InventoryGui2> guis = new ArrayList<>();
                for(final InventoryGui2 gui : this.guis){
                    guis.add(gui);
                }
                if(guis.size() > 0){
                    final List<InventoryGui2> remove = new ArrayList<>();
                    for(final InventoryGui2 gui : guis){
                        if(gui.complete){
                            remove.add(gui);
                        }else{
                            if(gui.player.equals(event.getPlayer())){
                                if(gui.getInventory() != null && event.getInventory().equals(gui.getInventory())){
                                    if(!gui.isRefreshing()){
                                        gui.close();
                                        remove.add(gui);
                                    }else{
                                        gui.close();
                                        gui.refreshing = false;
                                    }
                                }
                            }
                        }
                    }
                    for(final InventoryGui2 mar : remove){
                        this.guis.remove(mar);
                    }
                }
            }catch(final Exception ee){
                ee.printStackTrace();;
            }
        }

        @EventHandler
        public void drop(PlayerDropItemEvent event){
            try{
                //This will make sure items from the menu can not be dropped.
                if(event.getItemDrop() != null && event.getItemDrop().getItemStack() != null){
                    final ItemStack item = event.getItemDrop().getItemStack();
                    for(final InventoryGui2 gui : guis){
                        if(gui.getItemString(item) != null){
                            event.setCancelled(true);
                            break;
                        }
                    }
                }
            }catch(final Exception ee){
                ee.printStackTrace();;
            }
        }

        @EventHandler
        public void leave(PlayerQuitEvent event){
            try{
                final List<InventoryGui2> guis = new ArrayList<>();
                for(final InventoryGui2 gui : this.guis){
                    guis.add(gui);
                }
                if(guis.size() > 0){
                    final List<InventoryGui2> remove = new ArrayList<>();
                    for(final InventoryGui2 gui : guis){
                        if(gui.getPlayer().equals(event.getPlayer())){
                            remove.add(gui);
                        }
                    }
                    for(final InventoryGui2 mar : remove){
                        this.guis.remove(mar);
                    }
                }
            }catch(final Exception ee){
                ee.printStackTrace();;
            }
        }

        /**
         * This will remove this inventory menu from use if the player for this menu is the same as the arg player.
         * @param player Bukkit Player
         */
        public void dumpPlayer(Player player) {
            final List<InventoryGui2> remove = new ArrayList<>();
            for(final InventoryGui2 gui : guis){
                if(gui.getPlayer().equals(player)){
                    remove.add(gui);
                }
            }
            for(final InventoryGui2 r : remove){
                guis.remove(r);
            }
        }

    }

    /**
     * Check if the inventory menu is refreshing.
     * @return refreshing
     */
    public boolean isRefreshing() {
        return refreshing;
    }

    /**
     * Set refreshing to true
     */
    public void refresh(){
        refreshing = true;
    }

    /**
     * Close this InventoryGui.
     * This will set refreshing to true before closing to avoid current modifier exception,
     * and it will set complete to true after exiting to dump this menu from ram when another
     * inventory is used.
     */
    public void exit() {
        refreshing = true;
        player.closeInventory();
        complete = true;
    }

    /**
     * This will remove all InventoryGuis from use by this Bukkit Player
     * @param player Bukkit Player
     */
    public static void dumpPlayer(Player player) {
        if(instanceEnable != null){
            instanceEnable.dumpPlayer(player);
        }
    }

    public static List<InventoryGui2> getGuis(){
        if(instanceEnable != null){
            return instanceEnable.guis;
        }else{
            return new ArrayList<>();
        }
    }

    public static void soundDeny(Player player){
        try{
            //if(GUI.getVersion().contains("1.8")){
            player.playSound(player.getLocation(), Sound.valueOf("ANVIL_LAND"), 1, 0.5F);
			/*}else{
			player.playSound(player.getLocation(), Sound.valueOf("BLOCK_ANVIL_LAND"), 1, 0.5F);
		}*/
        }catch(final Exception e){}
    }

    public static void soundClick(Player player){
        try{
            //if(GUI.getVersion().contains("1.8")){
            player.playSound(player.getLocation(), Sound.valueOf("CLICK"), 1, 1);
			/*}else{
			player.playSound(player.getLocation(), Sound.valueOf("UI_BUTTON_CLICK"), 1, 1);
		}*/
        }catch(final Exception e){}
    }

    public static void soundComplete(Player player){
        try{
            //if(GUI.getVersion().contains("1.8")){
            player.playSound(player.getLocation(), Sound.valueOf("LEVEL_UP"), 1, 0.5F);
			/*}else{
			player.playSound(player.getLocation(), Sound.valueOf("ENTITY_PLAYER_LEVELUP"), 1, 0.5F);
		}*/
        }catch(final Exception e){}
    }

    /**
     * Get inventory size to fit how many items are needed. Cannot excede 54.
     * @param max Size of inventory looking for
     * @return Multiple of 9 (9 - 54)
     */
    public static int getInventorySize(int max) {
        if (max <= 0) return 9;
        final int quotient = (int)Math.ceil(max / 9.0);
        return quotient > 5 ? 54: quotient * 9;
    }

    /**
     * Abstract method called when an item is clicked on in the inventory.
     * @param id Item Key - This will not be null
     */
    public abstract void click(String id);

    /**
     * Abstract method called when the inventory is closed.
     */
    public abstract void close();

    /**
     * Abstract method called when the reload method is called.
     */
    public abstract void reinitiate();
}