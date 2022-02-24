package me.invertmc.market;

import java.util.List;

import me.invertmc.Feudal;
import me.invertmc.utils.ErrorManager;
import me.invertmc.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

public class CheckForMarketErrors {
    /**
     * Check for market errors.  If the buy price is lower than the sell price and if crafting can increase value.
     */
    public CheckForMarketErrors(){
        try{
            for(String s : Feudal.getMarketConfig().getConfig().getConfigurationSection("prices").getKeys(false)){
                if(Feudal.getMarketConfig().getConfig().contains("prices." + s + ".sell")) {
                    for(String s2 : Feudal.getMarketConfig().getConfig().getConfigurationSection("prices." + s + ".sell").getKeys(false)){
                        try{
                            if(Feudal.getMarketConfig().getConfig().contains("prices." + s + ".buy." + s2)){
                                double sell = Feudal.getMarketConfig().getConfig().getDouble("prices." + s + ".sell." + s2, 0);
                                double buy = Feudal.getMarketConfig().getConfig().getDouble("prices." + s + ".buy." + s2, 0);
                                if(sell > buy){
                                    Bukkit.getConsoleSender().sendMessage("\u00a7cMarket dupe error! " + s + " can be sold for more than it can be bought.");
                                }

                                short dur = 0;
                                if(!s2.equalsIgnoreCase("price")){
                                    try{
                                        dur = Short.parseShort(s2);
                                    }catch(Exception e){
                                        continue;
                                    }
                                }
                                ItemStack item = new ItemStack(Material.getMaterial(s), 1, dur);
                                try{
                                    if(item != null){
                                        List<Recipe> recipes = Bukkit.getRecipesFor(item);
                                        if(recipes != null){
                                            boolean bad = false;
                                            double suggestedPrice = 0;
                                            for(Recipe r : recipes){
                                                if(r instanceof ShapedRecipe){
                                                    double price = 0;
                                                    ShapedRecipe sr = (ShapedRecipe) r;
                                                    for(Character c : sr.getIngredientMap().keySet()){
                                                        ItemStack i = sr.getIngredientMap().get(c);
                                                        if(i != null){
                                                            if(Feudal.getMarketConfig().getConfig().contains("prices." + i.getType().toString() + ".sell." + i.getDurability())){
                                                                price += Feudal.getMarketConfig().getConfig().getDouble("prices." + i.getType().toString() + ".sell." + i.getDurability());
                                                            }else{
                                                                price += Feudal.getMarketConfig().getConfig().getDouble("prices." + i.getType().toString() + ".sell.price");
                                                            }
                                                            int count = 0;
                                                            for(String shape : sr.getShape()){
                                                                count+= Utils.countMatches(shape, c.charValue() + "");
                                                            }
                                                            price *= count;
                                                        }
                                                    }
                                                    price /= ((double)sr.getResult().getAmount());
                                                    if(price <= 0){
                                                        break;
                                                    }
                                                    if(sell > price+1){
                                                        suggestedPrice = price;
                                                        bad = true;
                                                        break;
                                                    }
                                                }if(r instanceof ShapelessRecipe){
                                                    double price = 0;
                                                    ShapelessRecipe sr = (ShapelessRecipe) r;
                                                    for(ItemStack i : sr.getIngredientList()){
                                                        if(Feudal.getMarketConfig().getConfig().contains("prices." + i.getType().toString() + ".sell." + i.getDurability())){
                                                            price += Feudal.getMarketConfig().getConfig().getDouble("prices." + i.getType().toString() + ".sell." + i.getDurability());
                                                        }else{
                                                            price += Feudal.getMarketConfig().getConfig().getDouble("prices." + i.getType().toString() + ".sell.price");
                                                        }
                                                    }
                                                    price /= ((double)sr.getResult().getAmount());
                                                    if(price <= 0){
                                                        break;
                                                    }
                                                    if(sell > price+1){
                                                        suggestedPrice = price;
                                                        bad = true;
                                                        break;
                                                    }
                                                }
                                            }
                                            if(bad){
                                                if(!s.equalsIgnoreCase("TIPPED_ARROW")){
                                                    if(s2.equalsIgnoreCase("price")){
                                                        Feudal.warn("Crafting price increases for market item: '" + s + "' SUGGESTED SELL PRICE: " + suggestedPrice);
                                                    }else{
                                                        Feudal.warn("Crafting price increases for market item: '" + s + "' sub: '" + s2 + "' SUGGESTED SELL PRICE: " + suggestedPrice);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }catch(Exception e){
                                    ErrorManager.error(3, e);
                                }
                            }
                        }catch(Exception e){

                        }
                    }
                }
            }
        }catch(Exception e){
            ErrorManager.error(2, e);
        }
    }

}