package me.invertmc.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryGui {
    private final Inventory inventory;
    private final HashMap<ItemStack, String> items = new HashMap<>();

    /**
     * Creates now inventory gui.
     * @param inventory
     */
    public InventoryGui(Inventory inventory){
        this.inventory = inventory;
    }

    /**
     * sets an item in the items hashmap and inventory
     * @param name
     * @param index
     * @param item
     */
    public ItemStack setItem(String name, int index, ItemStack item){
		/*ItemMeta meta = item.getItemMeta();
		if(meta != null && meta.getDisplayName() != null){
			meta.setDisplayName(meta.getDisplayName()+"\u00a70\u00a71\u00a72\u00a73\u00a7f\u00a7l\u00a7o\u00a7k\u00a7r");
		}*/
		/*if(Main.getVersion().equals("1.9") && (item.getType().equals(Material.MONSTER_EGG) || item.getType().equals(Material.POTION) || item.getType().equals(Material.LINGERING_POTION) ||
				item.getType().equals(Material.SPLASH_POTION) || item.getType().equals(Material.TIPPED_ARROW))){
			Random rand = new Random();
			String time = System.nanoTime() + "";
			String start = rand.nextInt(99) + (time.length() > 8 ? time.substring(time.length() - 9, time.length()) : time) + "";
			String end = "";
			for(char c : start.toCharArray()){
				end += "\u00a7" + c;
			}
			end += "\u00a7r";
		}*/

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
                end += "\u00a7" + c;
            }
            end += "\u00a7r";
            metaName=end;
			for(char c : (System.currentTimeMillis() + "").toCharArray()){
				if(c == '0'){
					metaName += "\u00a70";
				}else if(c == '1'){
					metaName += "\u00a71";
				}else if(c == '2'){
					metaName += "\u00a72";
				}else if(c == '3'){
					metaName += "\u00a73";
				}else if(c == '4'){
					metaName += "\u00a74";
				}else if(c == '5'){
					metaName += "\u00a75";
				}else if(c == '6'){
					metaName += "\u00a76";
				}else if(c == '7'){
					metaName += "\u00a77";
				}else if(c == '8'){
					metaName += "\u00a78";
				}else if(c == '9'){
					metaName += "\u00a79";
				}
			}
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
     * @return
     */
    public Inventory getInventory(){
        return inventory;
    }

    /**
     * Gets an item string saved in the items hashmap.
     * @param item
     * @return
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
			/*if(Main.getVersion().equals("1.9") && (item.getType().equals(Material.MONSTER_EGG) || item.getType().equals(Material.POTION) || item.getType().equals(Material.LINGERING_POTION) ||
					item.getType().equals(Material.SPLASH_POTION) || item.getType().equals(Material.TIPPED_ARROW))){
				for(ItemStack i : items.keySet()){
					if(i.getItemMeta() != null && i.getItemMeta().getDisplayName().startsWith())
				}
			}*/

            for(final ItemStack is : items.keySet()){
                if(is != null){
                    if(is.equals(item)){
                        return items.get(is);
                    }
                }
            }
            return null;
			/*if(items.containsKey(item)){
				return items.get(item);
			}else{
				return null;
			}*/
        }
    }

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

    public static String lim(String message) {//Limit to 31 characters max
        if(message.length() > 31){
            return message.substring(0, 31);
        }else{
            return message;
        }
    }
}