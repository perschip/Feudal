package me.invertmc.configs;

import me.invertmc.Feudal;
import me.invertmc.market.MarketItem;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class MarketData {
    protected static boolean marketData() throws Exception{
        boolean load = true;
        Feudal.setMarketData(new Configuration(new File(Feudal.getDataFolderFeudal(), "marketData.yml")));
        try {
            if(!Feudal.getMarketData().loadConfig()){
                setupMarketItems();
            }
        } catch (Exception e) {
            load = false;

            Feudal.warn("Failed to load config: " + Feudal.getMarketData().getFile().getAbsolutePath() + ".  Please check to make sure the config does not have any syntax errors.");
            try {
                Feudal.getMarketData().broke();
            } catch (Exception e1) {

                Feudal.error("FAILED TO SAVE BROKEN BACKUP OF CONFIG: " + Feudal.getMarketData().getFile().getAbsolutePath());
                throw e1;
            }
            try{
                Feudal.getMarketData().loadConfig();
            }catch(Exception e1){
                throw e1;
            }
            throw e;
        }
        try {
            Feudal.getMarketData().save();
        } catch (Exception e) {
            load = false;


            Feudal.error("Failed to save config: " + Feudal.getMarketData().getFile().getAbsolutePath());
            throw e;
        }
        return load;
    }

    private static void setupMarketItems() throws Exception{
        if(Feudal.getMarketData().getConfig().contains("items")){
            for(String str : Feudal.getMarketData().getConfig().getConfigurationSection("items").getKeys(false)){
                try{
                    ItemStack stack = Feudal.getMarketData().getConfig().getItemStack("items."+str + ".itemStack");
                    long date = Long.MAX_VALUE;
                    try{
                        Long.parseLong(str.split("-")[0]);
                    }catch(Exception e){}
                    MarketItem item = new MarketItem(str, Feudal.getMarketData().getConfig().getDouble("items."+str+".merchant.price"), Feudal.getMarketData().getConfig().getDouble("items."+str+".soldPrice"), stack, Feudal.getMarketData().getConfig().getBoolean("items."+str + ".merchant.isMerchant"), Feudal.getMarketData().getConfig().getString("items."+str + ".merchant.seller"), Feudal.getMarketData().getConfig().getInt("items."+str + ".amount"), date);
                    if(item.getItem() != null && item.getItem().getItemMeta() != null){
                        Feudal.getMarketItems().add(item);
                    }
                }catch(Exception e){

                    Feudal.error("Failed to load market item: " + str);
                    throw e;
                }
            }
        }
    }
}
