package me.invertmc.market;

import java.util.List;

public class Category {

    private static int maxIndex = 0;

    private String category;
    private String itemName;
    private List<String> items;
    private byte index;
    private String itemType;
    private short itemData;

    public Category(String category, String itemName, List<String> items, int index, String itemType, int itemData){
        this.category = category;
        this.itemName = itemName.replace("&", "\u00a7").replace("\u00a7\u00a7", "&");//Use && for &
        this.items = items;
        this.index = (byte) index;
        this.itemType = itemType;
        this.itemData = (short) itemData;
        if(index > maxIndex){
            maxIndex = index;
        }
    }

    public static int getMaxIndex(){
        return maxIndex;
    }

    public String getCategory() {
        return category;
    }

    public String getItemName() {
        return itemName;
    }

    public List<String> getItems() {
        return items;
    }

    public int getIndex() {
        return index;
    }

    public String getItemType() {
        return itemType;
    }

    public int getItemData() {
        return itemData;
    }
}