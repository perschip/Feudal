package me.invertmc.market;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import me.invertmc.Feudal;
import me.invertmc.user.User;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MarketItem {//About 1142 bytes per instance (1kb)
    private float price;//4
    private float soldPrice;//4
    private ItemStack item;//~~952
    private boolean merchant;//1
    private String merchantUUID;//112
    private int amount;//4
    private String tag;//64
    private boolean outOfStock;//1
    private long date;

    public MarketItem(String tag, double price, double soldPrice, ItemStack item, boolean merchant, String merchantUUID, int amount, long date){
        this.tag = tag;
        this.price = (float) price;
        this.item = item;
        this.merchant = merchant;
        this.merchantUUID = merchantUUID;
        this.soldPrice = (float) soldPrice;
        this.amount = amount;
        if(this.amount > 0){
            this.outOfStock = false;
        }else{
            this.outOfStock = true;
        }
        this.date = date;
    }

    public long getDate(){
        return date;
    }

    public double getPrice() {
        return price;
    }

    public double getSoldPrice() {
        return soldPrice;
    }

    public ItemStack getItem() {
        return item;
    }

    public boolean isMerchant() {
        return merchant;
    }

    public String getMerchantUUID() {
        return merchantUUID;
    }

    public int getAmount() {
        return amount;
    }

    public void setOutOfStock(boolean b) {
        outOfStock = b;
    }

    public boolean isOutOfStock() {
        return outOfStock;
    }

    public String getTag() {
        return tag;
    }

    public void setMarketData() {
        Feudal.getMarketData().getConfig().set("items." + tag + ".itemStack", item);
        Feudal.getMarketData().getConfig().set("items." + tag + ".amount", amount);
        Feudal.getMarketData().getConfig().set("items." + tag + ".soldPrice", soldPrice);
        Feudal.getMarketData().getConfig().set("items." + tag + ".merchant.isMerchant", merchant);
        Feudal.getMarketData().getConfig().set("items." + tag + ".merchant.seller", merchantUUID);
        Feudal.getMarketData().getConfig().set("items." + tag + ".merchant.price", price);
    }

    public void purge(){
        Feudal.getMarketData().getConfig().set("items." + tag + "", null);
    }

    public void setAmount(int i) {
        amount = i;
    }

    //This get the item which is to be displayed in the market
    public ItemStack getMarketItem(boolean merchant) {
        if(!Feudal.getMarketConfig().getConfig().getBoolean("merchantsCanBuyForSellPrice")){
            merchant = false;
        }
        ItemStack stack = item.clone();
        ItemMeta meta = stack.getItemMeta();
        if(meta == null){
            Feudal.error("Item meta is null for a MarketItem: " + item.toString());
        }else{
            if(merchant){
                List<String> lores = new LinkedList<String>();
                User merchU = Feudal.getUser(merchantUUID);
                if(this.isMerchant() && merchU != null){
                    lores.addAll(Arrays.asList(new String[]{Feudal.getMessage("marketUpdate.price") + Feudal.round(soldPrice), Feudal.getMessage("marketUpdate.amount") + amount, Feudal.getMessage("marketUpdate.merchantName") + merchU.getName()}));
                }else{
                    lores.addAll(Arrays.asList(new String[]{Feudal.getMessage("marketUpdate.price") + Feudal.round(soldPrice), Feudal.getMessage("marketUpdate.amount") + amount}));
                }
                if(meta.getLore() != null){
                    lores.addAll(meta.getLore());
                }
                meta.setLore(lores);
            }else{
                List<String> lores = new LinkedList<String>();
                User merchU = Feudal.getUser(merchantUUID);
                if(this.isMerchant() && merchU != null){
                    lores.addAll(Arrays.asList(new String[]{Feudal.getMessage("marketUpdate.price") + Feudal.round(price * Market.getSAD(this.getItem().getType().toString())), Feudal.getMessage("marketUpdate.amount") + amount, Feudal.getMessage("marketUpdate.merchantName") + merchU.getName()}));
                }else{
                    lores.addAll(Arrays.asList(new String[]{Feudal.getMessage("marketUpdate.price") + Feudal.round(price * Market.getSAD(this.getItem().getType().toString())), Feudal.getMessage("marketUpdate.amount") + amount}));
                }
                if(meta.getLore() != null){
                    lores.addAll(meta.getLore());
                }
                meta.setLore(lores);
            }
            stack.setItemMeta(meta);
        }

        return stack;
    }

    public ItemStack getMarketItem(boolean merchant, int selectAmount, double balance, double materialAmount) {
        if(!Feudal.getMarketConfig().getConfig().getBoolean("merchantsCanBuyForSellPrice")){
            merchant = false;
        }
        ItemStack stack = item.clone();
        ItemMeta meta = stack.getItemMeta();
        if(merchant){
            meta.setLore(Arrays.asList(new String[]{Feudal.getMessage("marketUpdate.price") + (float)soldPrice, Feudal.getMessage("marketUpdate.totalPrice") + (float)(soldPrice*selectAmount), Feudal.getMessage("marketUpdate.balance") + (float)balance, Feudal.getMessage("marketUpdate.amount") + selectAmount, Feudal.getMessage("marketUpdate.maxAmount") + amount}));
        }else{
            meta.setLore(Arrays.asList(new String[]{Feudal.getMessage("marketUpdate.price") + Feudal.round(getPrice(merchant, selectAmount, materialAmount)), Feudal.getMessage("marketUpdate.totalPrice") + Feudal.round(getPrice(merchant, selectAmount, materialAmount)*selectAmount), Feudal.getMessage("marketUpdate.balance") + (float)balance, Feudal.getMessage("marketUpdate.amount") + selectAmount, Feudal.getMessage("marketUpdate.maxAmount") + amount}));
        }
        stack.setItemMeta(meta);

        return stack;
    }

    public double getPrice(boolean merchant) {
        double p = price * Market.getSAD(this.getItem().getType().toString());
        if(merchant){
            if(p < soldPrice){
                return p;
            }
            return soldPrice;
        }else{
            return p;
        }
    }

    public double getPrice(boolean merchant, int selectAmount, double materialAmount) {
        if(!Feudal.getMarketConfig().getConfig().getBoolean("SupplyAndDemand") || merchant){
            return getPrice(merchant);
        }else{
            return price * Market.predictSAD(this.getItem().getType().toString(), selectAmount, materialAmount);
        }
    }
}