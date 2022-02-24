package me.invertmc.market;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import me.invertmc.Feudal;
import me.invertmc.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Market {
    protected InventoryGui inventory;
    protected byte inventoryType;
    protected Player player;
    protected ArrayList<MarketItem> marketItems;
    protected byte page = 0;
    protected Category cat;
    protected int selectAmount = 0;
    protected User user;
    protected boolean merchant;
    protected MarketItem selectItem;
    protected boolean complete = false;
    protected long purchased = 0;
    private double materialAmount = 1;
    private boolean backButton = true;

    public Market(Player p, String section) {
        player = p;

        if(section == null || section.equals("")) {
            inventory = createCategoriesInventory();
            inventoryType = 0;
            p.closeInventory();
            openInventory();
        }else {
            backButton = false;
            viewCategory(section);
        }
    }

    public Player getPlayer() {
        return player;
    }

    protected void click(String itemString){
        if(itemString != null){
            if(inventoryType == 0){//Select category
                if(itemString.equalsIgnoreCase("ALL")){
                    soundClick();
                    viewCategory("ALL");
                }
                for(final Category cat : Feudal.getCategories()){
                    if(cat.getCategory().equals(itemString)){
                        soundClick();
                        viewCategory(cat.getCategory());
                        return;
                    }
                }
            }else if(inventoryType == 1){
                if(itemString.equalsIgnoreCase("BACK")){
                    if(page > 1){
                        page--;
                        updateMarket(inventory);
                        soundClick();
                    }else{
                        inventory = createCategoriesInventory();
                        inventoryType = 0;
                        player.closeInventory();
                        openInventory();
                        soundClick();
                    }
                }else if(itemString.equalsIgnoreCase("NEXT")){
                    if(page < getPages()){
                        page++;
                        updateMarket(inventory);
                        soundClick();
                    }else{
                        soundDeny();
                    }
                }else if(!itemString.equalsIgnoreCase("INFO")){
                    if(Feudal.getOnlinePlayers().containsKey(player.getUniqueId().toString())){
                        final User u = Feudal.getOnlinePlayers().get(player.getUniqueId().toString());
                        for(final MarketItem item : Feudal.getMarketItems()){
                            if(item.getTag().equals(itemString)){
                                if(u.getBalance() >= (merchant ? item.getSoldPrice() : item.getPrice())){
                                    soundClick();
                                    selectAmount(item, u);
                                }else{
                                    soundDeny();
                                }
                            }
                        }
                    }else{
                        soundDeny();
                    }
                }
            }else if(inventoryType == 2){
                if(itemString.equalsIgnoreCase("BACK")){
                    if(cat != null){
                        viewCategory(cat.getCategory());
                    }else{
                        soundDeny();
                    }
                }else if(itemString.equalsIgnoreCase("BUY")){
                    if(System.currentTimeMillis() - purchased > 1500){
                        user.syncBalance();
                        final double money = selectItem.getPrice(merchant, selectAmount, materialAmount) * selectAmount;//(selectAmount * selectItem.getPrice(merchant));
                        if((money <= user.getBalance()) && selectAmount > 0 && selectAmount <= selectItem.getAmount() && !selectItem.isOutOfStock()){
                            if(Feudal.getMarketItems().contains(selectItem)){
                                purchased = System.currentTimeMillis();
                                final ItemStack item = selectItem.getItem().clone();
                                final int totalSelectAmount = selectAmount;
                                while(selectAmount > 64){
                                    item.setAmount(64);
                                    final Item entityItem = player.getWorld().dropItem(player.getLocation(), item);
                                    ItemProtector.protect(new ItemProtector(entityItem, player));
                                    selectAmount -= 64;
                                }
                                if(selectAmount > 0){
                                    item.setAmount(selectAmount);
                                    final Item entityItem = player.getWorld().dropItem(player.getLocation(), item);
                                    ItemProtector.protect(new ItemProtector(entityItem, player));
                                }

                                selectItem.setAmount(selectItem.getAmount() - totalSelectAmount);
                                if(selectItem.getAmount() < 1){
                                    selectItem.purge();
                                    Feudal.getMarketItems().remove(selectItem);
                                    selectItem.setOutOfStock(true);
                                    Feudal.saveMarketItems();
                                }
                                buySAD(item.getType(), totalSelectAmount);

                                soundComplete();
                                user.removeMoney(money);

                                //Give merchant xp and money.
                                if(selectItem.isMerchant()){
                                    final User u = Feudal.getUser(selectItem.getMerchantUUID());
                                    double toKingdom = 0;
                                    if(u != null){
                                        u.syncBalance();
                                        double sell = money;
                                        Kingdom k = null;
                                        if(!u.getKingdomUUID().equals("")){
                                            k = Feudal.getKingdom(u.getKingdomUUID());
                                            if(k != null){
                                                toKingdom = sell * k.getIncomeTax(u.getUUID());
                                                sell-=toKingdom;
                                            }
                                        }
                                        if(toKingdom > 0 && k != null){
                                            k.setTreasury(toKingdom + k.getTreasury());
											/*try {
												k.save();
											} catch (Exception e) {
												ErrorManager.error(86, e);
											}*/
                                        }
                                        u.addMoney(sell);
                                        String message = Feudal.getMessage("market.sold.kingdom").replace("%amount%", totalSelectAmount + "").replace("%price%", Feudal.round(sell) + "").replace("%kingdom-money%", Feudal.round(toKingdom) + "");
                                        if(toKingdom <= 0){
                                            message = (Feudal.getMessage("market.sold.player").replace("%amount%", totalSelectAmount + "").replace("%price%", Feudal.round(sell) + ""));
                                        }
                                        if(u.getPlayer() != null && u.getPlayer().isOnline()){
                                            u.getPlayer().sendMessage(message);
                                        }else{
                                            u.sendOfflineMessage(message);
                                        }
                                        if(u.getProfession() != null && u.getProfession().getType().equals(Profession.Type.MERCHANT)){
                                            if(!(!u.getKingdomUUID().equals("") && user.getKingdomUUID().equals(u.getKingdomUUID()))){
                                                int xpAddedRandom = Feudal.getConfiguration().getConfig().getInt("Merchant.xpRandomAdded");
                                                if(xpAddedRandom < 1) {
                                                    xpAddedRandom = 1;
                                                }
                                                XP.addXP(u, (int)((sell+toKingdom)*(Feudal.RANDOM.nextInt(xpAddedRandom) + Feudal.getConfiguration().getConfig().getInt("Merchant.xpFixed"))));
                                            }
                                        }
                                    }
                                }

                                player.sendMessage(Feudal.getMessage("market.buy").replace("%amount%", totalSelectAmount + "").replace("%price%", Feudal.round(money) + ""));

                                if(selectItem.isOutOfStock()){
                                    if(cat != null){
                                        viewCategory(cat.getCategory());
                                    }else{
                                        player.closeInventory();
                                    }
                                }else{
                                    selectAmount = 1;
                                    updateSelectAmount(inventory);
                                }
                            }else{
                                soundDeny();
                            }
                        }else{
                            soundDeny();
                        }
                    }
                }else if(itemString.equalsIgnoreCase("ADD_ONE")){
                    user.syncBalance();
                    if(((selectAmount + 1) <= selectItem.getAmount()) && ((selectAmount+1)*(selectItem.getPrice(merchant, selectAmount+1, materialAmount)) <= user.getBalance())){
                        selectAmount++;
                        updateSelectAmount(inventory);
                        soundPitched(selectAmount, selectItem.getAmount());
                    }else{
                        soundDeny();
                    }
                }else if(itemString.equalsIgnoreCase("ADD_TEN")){
                    user.syncBalance();
                    if(((selectAmount + 10) <= selectItem.getAmount()) && ((selectAmount+10)*(selectItem.getPrice(merchant, selectAmount+10, materialAmount)) <= user.getBalance())){
                        selectAmount+=10;
                        updateSelectAmount(inventory);
                        soundPitched(selectAmount, selectItem.getAmount());
                    }else{
                        soundDeny();
                    }
                }else if(itemString.equalsIgnoreCase("SUBTRACT_ONE")){
                    if(((selectAmount - 1) > 0)){
                        selectAmount--;
                        updateSelectAmount(inventory);
                        soundPitched(selectAmount, selectItem.getAmount());
                    }else{
                        soundDeny();
                    }
                }else if(itemString.equalsIgnoreCase("SUBTRACT_TEN")){
                    if(((selectAmount - 10) > 0)){
                        selectAmount-=10;
                        updateSelectAmount(inventory);
                        soundPitched(selectAmount, selectItem.getAmount());
                    }else{
                        soundDeny();
                    }
                }
            }
        }
    }

    protected static void sellItem(ItemStack stack, User seller, boolean merchant, double sellPrice, double buyPrice, double marketValueSell){
        double toKingdom = 0;
        if(seller.getPlayer() != null && seller.getPlayer().isOnline()){
            //double buyPrice = getItemBuyPrice(stack);
            double saveSellPrice = sellPrice;
            if(!merchant){
                sellPrice = marketValueSell;
                //sellPrice = getItemSellPriceFresh(stack);
                saveSellPrice = sellPrice;
                if(seller.getProfession() == null || seller.getProfession().getType().equals(Profession.Type.NONE)){
                    saveSellPrice = 0;
                    sellPrice = 0;
                }else if(!isProfessionItem(stack, seller.getProfession().getType())){
                    saveSellPrice = sellPrice;
                    sellPrice *= Feudal.getMarketConfig().getConfig().getDouble("NonProfessionSellMultiplier");
                }

                if(sellPrice > 0){
                    double sell = sellPrice*stack.getAmount();
                    Kingdom k = null;
                    if(!seller.getKingdomUUID().equals("")){
                        k = Feudal.getKingdom(seller.getKingdomUUID());
                        if(k != null){
                            toKingdom = sell * k.getIncomeTax(seller.getUUID());
                            sell-=toKingdom;
                        }
                    }
                    seller.addMoney(sell);
                    if(toKingdom > 0 && k != null){
                        k.setTreasury(toKingdom + k.getTreasury());
						/*try {
							k.save();
						} catch (Exception e) {
							ErrorManager.error(86, e);
						}*/
                    }
                    sellPrice = sell;
                }else{
                    seller.getPlayer().sendMessage(Feudal.getMessage("market.cantsell"));
                    return;
                }
            }else{
                buyPrice = sellPrice;
            }
            if(buyPrice <= 0){
                seller.getPlayer().sendMessage(Feudal.getMessage("market.invalidprice"));
                return;
            }
            final ItemStack newStack = stack.clone();
            Feudal.setItemInHand(seller.getPlayer(), new ItemStack(XMaterial.AIR.parseItem()));
            newStack.setAmount(1);
            final MarketItem mItem = new MarketItem(System.currentTimeMillis() + "-" + new Random().nextInt(100),
                    (float) buyPrice, saveSellPrice,
                    newStack, merchant, seller.getUUID(), stack.getAmount(), System.currentTimeMillis());
            boolean already = false;
            if(Feudal.getMarketConfig().getConfig().getBoolean("SupplyAndDemand")){
                for(final MarketItem item : Feudal.getMarketItems()){
                    if(item.getItem().equals(mItem.getItem()) && item.getPrice() == mItem.getPrice() && !item.isMerchant() && !mItem.isMerchant()){
                        item.setAmount(item.getAmount() + mItem.getAmount());
                        already = true;
                        break;
                    }else if(item.getItem().equals(mItem.getItem()) && item.getPrice() == mItem.getPrice() && item.getSoldPrice() == mItem.getSoldPrice()){
                        if(((item.isMerchant() || mItem.isMerchant()) && item.getMerchantUUID().equals(mItem.getMerchantUUID())) || (!item.isMerchant() && !mItem.isMerchant())){
                            item.setAmount(item.getAmount() + mItem.getAmount());
                            already = true;
                            break;
                        }
                    }
                }
            }else{
                for(final MarketItem item : Feudal.getMarketItems()){
                    if(item.getItem().equals(mItem.getItem()) && item.getPrice() == mItem.getPrice() && item.getSoldPrice() == mItem.getSoldPrice()){
                        if(((item.isMerchant() || mItem.isMerchant()) && item.getMerchantUUID().equals(mItem.getMerchantUUID())) || (!item.isMerchant() && !mItem.isMerchant())){
                            item.setAmount(item.getAmount() + mItem.getAmount());
                            already = true;
                            break;
                        }
                    }
                }
            }
            if(!already){
                Feudal.getMarketItems().add(mItem);
            }
            Feudal.saveMarketItems();
            if(merchant){
                seller.getPlayer().sendMessage(Feudal.getMessage("market.add.merchant").replace("%price%", Feudal.round(sellPrice) + ""));
            }else{

                sellSAD(newStack.getType(), stack.getAmount());

                Feudal.log("FeudalMarket-> p: " + seller.getPlayer().getName() + ", price: $" + (sellPrice / stack.getAmount()) + ", Amount: " + stack.getAmount() + ", Total: $" + sellPrice + ", ITEM:"+stack.getType().toString());
                if(toKingdom > 0){
                    seller.getPlayer().sendMessage(Feudal.getMessage("market.add.kingdom").replace("%price%", Feudal.round(sellPrice) + "").replace("%kingdom-money%", Feudal.round(toKingdom) + ""));
                }else{
                    seller.getPlayer().sendMessage(Feudal.getMessage("market.add.player").replace("%price%", Feudal.round(sellPrice) + ""));
                }
            }
        }
    }

    protected void selectAmount(MarketItem item, User u){
        user = u;
        if(user.getProfession() != null && user.getProfession().getType().equals(Profession.Type.MERCHANT)){
            merchant = true;
        }else{
            merchant = false;
        }
        selectAmount = 1;
        selectItem = item;
        inventory = createSelectAmountInventory();
        inventoryType = 2;
        player.closeInventory();
        openInventory();
    }

    protected InventoryGui createSelectAmountInventory() {
        final InventoryGui i = new InventoryGui(Bukkit.createInventory(player, 36, Feudal.getMessage("market.title")));
        materialAmount = Market.countMaterials(selectItem.getItem().getType());
        updateSelectAmount(i);
        return i;
    }

    protected void updateSelectAmount(InventoryGui i) {
        i.clear();
        i.getInventory().clear();

        i.setItem("BACK", 27, Selection.createItem(XMaterial.RED_WOOL.parseMaterial(), 1, (short) 14, Feudal.getMessage("market.back2"), null));
        i.setItem("BUY", 35, Selection.createItem(XMaterial.GREEN_WOOL.parseMaterial(), 1, (short) 5, Feudal.getMessage("market.purchase"), null));


        i.setItem("ADD_ONE", 16, Selection.createItem(XMaterial.LIME_STAINED_GLASS_PANE.parseMaterial(), 1, (short) 5, Feudal.getMessage("market.add1"), Arrays.asList(new String[]{Feudal.getMessage("market.inc"), Feudal.getMessage("market.inc1")})));
        i.setItem("ADD_TEN", 7, Selection.createItem(XMaterial.LIME_STAINED_GLASS_PANE.parseMaterial(), 1, (short) 5, Feudal.getMessage("market.add10"), Arrays.asList(new String[]{Feudal.getMessage("market.inc"), Feudal.getMessage("market.inc10")})));
        i.setItem("ADD_TEN", 15, Selection.createItem(XMaterial.LIME_STAINED_GLASS_PANE.parseMaterial(), 1, (short) 5,  Feudal.getMessage("market.add10"), Arrays.asList(new String[]{Feudal.getMessage("market.inc"), Feudal.getMessage("market.inc10")})));
        i.setItem("ADD_TEN", 17, Selection.createItem(XMaterial.LIME_STAINED_GLASS_PANE.parseMaterial(), 1, (short) 5,  Feudal.getMessage("market.add10"), Arrays.asList(new String[]{Feudal.getMessage("market.inc"), Feudal.getMessage("market.inc10")})));
        i.setItem("ADD_TEN", 25, Selection.createItem(XMaterial.LIME_STAINED_GLASS_PANE.parseMaterial(), 1, (short) 5,  Feudal.getMessage("market.add10"), Arrays.asList(new String[]{Feudal.getMessage("market.inc"), Feudal.getMessage("market.inc10")})));

        i.setItem("SUBTRACT_ONE", 10, Selection.createItem(XMaterial.YELLOW_STAINED_GLASS_PANE.parseMaterial(), 1, (short) 4, Feudal.getMessage("market.sub1"), Arrays.asList(new String[]{Feudal.getMessage("market.dec"), Feudal.getMessage("market.dec1")})));
        i.setItem("SUBTRACT_TEN", 9, Selection.createItem(XMaterial.YELLOW_STAINED_GLASS_PANE.parseMaterial(), 1, (short) 4,  Feudal.getMessage("market.sub10"), Arrays.asList(new String[]{Feudal.getMessage("market.dec"), Feudal.getMessage("market.dec10")})));
        i.setItem("SUBTRACT_TEN", 11, Selection.createItem(XMaterial.YELLOW_STAINED_GLASS_PANE.parseMaterial(), 1, (short) 4,  Feudal.getMessage("market.sub10"), Arrays.asList(new String[]{Feudal.getMessage("market.dec"), Feudal.getMessage("market.dec10")})));


        user.syncBalance();
        i.setItem("MARKET_ITEM", 31, selectItem.getMarketItem(merchant, selectAmount, user.getBalance(), materialAmount));
    }

    protected void viewCategory(String category){
        for(final Category cat : Feudal.getCategories()){
            if(cat.getCategory().equalsIgnoreCase(category)){
                this.cat = cat;
                break;
            }
        }
        if(cat == null){
            player.sendMessage(Feudal.getMessage("market.err.open"));
            player.closeInventory();
        }
        page = 1;
        marketItems = getMarketItems();
        if(Feudal.getOnlinePlayers().containsKey(player.getUniqueId().toString())){
            user = Feudal.getOnlinePlayers().get(player.getUniqueId().toString());
            inventory = createMarketInventory(page);
            inventoryType = 1;
            player.closeInventory();
            openInventory();
        }else{
            player.closeInventory();
            soundDeny();
        }
    }

    protected InventoryGui createMarketInventory(int page) {
        final InventoryGui i = new InventoryGui(Bukkit.createInventory(player, 54, Feudal.getMessage("market.title")));
        updateMarket(i);
        return i;
    }

    protected void updateMarket(InventoryGui i){
        final DecimalFormat df2 = new DecimalFormat("#,###,###,##0.00");
        i.clear();
        i.getInventory().clear();
        if(backButton || page > 1) {
            i.setItem("BACK", 45, Selection.createItem(XMaterial.YELLOW_WOOL.parseMaterial(), 1, (short) 4, Feudal.getMessage("market.back"), null));
        }
        i.setItem("NEXT", 53, Selection.createItem(XMaterial.YELLOW_WOOL.parseMaterial(), 1, (short) 4, Feudal.getMessage("market.next"), null));
        final ArrayList<MarketItem> items = getMarketItems(page);
        user.syncBalance();
        i.setItem("INFO", 49, Selection.createItem(XMaterial.BOOK.parseMaterial(), 1, (short) 0, "\u00a76\u00a7l" + page + " \u00a77\u00a7l/ \u00a76\u00a7l" + getPages(), Arrays.asList(new String[]{Feudal.getMessage("market.bal") + df2.format(user.getBalance())/*user.getBalance()*/, Feudal.getMessage("market.cat") + ChatColor.stripColor(cat.getItemName()), Feudal.getMessage("market.itemshow") + items.size(), Feudal.getMessage("market.total") + marketItems.size()})));

        boolean merchant = false;
        if(Feudal.getOnlinePlayers().containsKey(player.getUniqueId().toString())){
            final User u = Feudal.getOnlinePlayers().get(player.getUniqueId().toString());
            if(u.getProfession() != null){
                if(u.getProfession().getType().equals(Profession.Type.MERCHANT)){
                    merchant = true;
                }
            }
        }

        for(int j = 0; j < items.size(); j++){
            i.setItem(items.get(j).getTag(), j, items.get(j).getMarketItem(merchant));
        }
    }

    protected ArrayList<MarketItem> getMarketItems(int page) {
        final ArrayList<MarketItem> items = new ArrayList<>();
        int endIndex = page*36;
        page--;
        final int index = page*36;
        if(index < marketItems.size()){
            if(endIndex > marketItems.size()){
                endIndex = marketItems.size();
            }
            for(int i = index; i < endIndex; i++){
                items.add(marketItems.get(i));
            }
        }
        return items;
    }

    protected ArrayList<MarketItem> getMarketItems() {
        final ArrayList<MarketItem> items = new ArrayList<>();
        for(final MarketItem item : Feudal.getMarketItems()){
            if(item.getItem() != null){
                if(item.getItem().getItemMeta() != null){
                    if(cat.getCategory().equalsIgnoreCase("ALL")){
                        items.add(item);
                    }else if(cat.getItems().contains(item.getItem().getType().toString())){
                        items.add(item);
                    }
                }
            }
        }

        //Dump old items
        final int maxSize = Feudal.getMarketConfig().getConfig().getInt("dumpMarketItems");
        if(items.size() > maxSize){
            final ArrayList<MarketItem> newItems = new ArrayList<>();

            while(newItems.size() < items.size()){
                MarketItem nextItem = null;
                long age = Long.MAX_VALUE;

                for(final MarketItem item : items){
                    if(!newItems.contains(item)){
                        if(nextItem == null){
                            nextItem = item;
                            age = item.getDate();
                        }else if(item.getDate() <= age){
                            nextItem = item;
                            age = item.getDate();
                        }
                    }
                }

                if(nextItem == null){
                    break;
                }else{
                    newItems.add(nextItem);
                }
            }

            items.clear();
            final Iterator<MarketItem> itr = newItems.iterator();
            while(itr.hasNext()){//Merchant first
                if(items.size() < maxSize){
                    final MarketItem item = itr.next();
                    if(item.isMerchant()){
                        items.add(item);
                        itr.remove();
                    }
                }else{
                    break;
                }
            }

            for(final MarketItem item : newItems){
                if(items.size() < maxSize){
                    items.add(item);
                }else{
                    break;
                }
            }

            Feudal.getMarketItems().clear();
            Feudal.getMarketItems().addAll(items);
            Feudal.saveMarketItems();

            return items;

        }else{
            return items;
        }
    }

    protected int getPages() {
        int size = marketItems.size() / 36;
        if(marketItems.size() % 36 > 0){
            size++;
        }
        return size;
    }

    protected void soundDeny(){
        if(Feudal.getVersion().contains("1.8") || Feudal.getVersion().equals("1.7")){
            player.playSound(player.getLocation(), Sound.valueOf("ANVIL_LAND"), 1, 0.5F);
        }else{
            player.playSound(player.getLocation(), Sound.valueOf("BLOCK_ANVIL_LAND"), 1, 0.5F);
        }
    }

    protected void soundClick(){
        if(Feudal.getVersion().contains("1.8") || Feudal.getVersion().equals("1.7")){
            player.playSound(player.getLocation(), Sound.valueOf("CLICK"), 1, 1);
        }else{
            player.playSound(player.getLocation(), Sound.valueOf("UI_BUTTON_CLICK"), 1, 1);
        }
    }

    protected void soundComplete(){
        if(Feudal.getVersion().contains("1.8") || Feudal.getVersion().equals("1.7")){
            player.playSound(player.getLocation(), Sound.valueOf("LEVEL_UP"), 1, 0.5F);
        }else{
            player.playSound(player.getLocation(), Sound.valueOf("ENTITY_PLAYER_LEVELUP"), 1, 0.5F);
        }
    }

    protected void soundPitched(float value, float max){
        if(Feudal.getVersion().contains("1.8") || Feudal.getVersion().equals("1.7")){
            player.playSound(player.getLocation(), Sound.valueOf("ARROW_HIT"), 1, (float) (((value * (1.7)) / max) + 0.3));
        }else{
            player.playSound(player.getLocation(), Sound.valueOf("ENTITY_ARROW_HIT_PLAYER"), 1, (float) (((value * (1.7)) / max) + 0.3));
        }
    }




    protected void openInventory() {
        player.openInventory(inventory.getInventory());
    }

    public InventoryGui createCategoriesInventory(){
        int size = Category.getMaxIndex();
        if(size > 45){
            size = 54;
        }else if(size > 36){
            size = 45;
        }else if(size > 27){
            size = 36;
        }else if(size > 18){
            size = 27;
        }else if(size > 9){
            size = 18;
        }else{
            size = 9;
        }
        final InventoryGui i = new InventoryGui(Bukkit.createInventory(player, size, Feudal.getMessage("market.title")));
        for(final Category cat : Feudal.getCategories()){
            final String type = cat.getItemType();
            final String name = cat.getItemName();
            final byte data = (byte) cat.getItemData();

            i.setItem(cat.getCategory(), cat.getIndex(), Selection.createItem(XMaterial.requestXMaterial(type, data).parseMaterial(), 1, data, name, null));
            //i.setItem(cat.getCategory(), cat.getIndex(), Selection.createItem(XMaterial.getMaterial(cat.getItemType()), 1, (short) cat.getItemData(), cat.getItemName(), null));
            //i.setItem(cat.getCategory(), cat.getIndex(), Selection.createItem(XMaterial.requestXMaterial(XMaterial.getMaterial(cat.getItemType()), 1, (short) cat.getItemData(), cat.getItemName(), null), size, inventoryType, null, null));
        }
        return i;
    }

    protected static boolean isProfessionItem(ItemStack stack, Profession.Type type) {
        final Material m = stack.getType();
        final int dur = stack.getDurability();
        if(type.equals(Profession.Type.FARMER) && (

                m.equals(XMaterial.HAY_BLOCK) || m.equals(XMaterial.WHEAT_SEEDS.parseItem()) || m.equals(XMaterial.PUMPKIN_SEEDS) || m.equals(XMaterial.WHEAT)
                        || m.equals(XMaterial.MELON_SEEDS) || (m.equals(XMaterial.INK_SAC) && dur == 3) || m.equals(XMaterial.SUGAR_CANE)
                        || m.equals(XMaterial.NETHER_WART) || m.equals(XMaterial.POTATO) || m.equals(XMaterial.CARROT)
                        || m.equals(XMaterial.POTATO) || m.equals(XMaterial.BEETROOT) || m.equals(XMaterial.BEETROOT_SEEDS)

        )){
            return true;
        }else if(type.equals(Profession.Type.LOGGER) && (

                m.equals(XMaterial.OAK_LOG.parseItem()) || m.equals(XMaterial.OAK_LOG.parseItem()) || m.equals(XMaterial.OAK_WOOD.parseItem())

        )){
            return true;
        }else if(type.equals(Profession.Type.HUNTER) && (

                m.equals(XMaterial.PORKCHOP) || m.equals(XMaterial.BEEF) || m.equals(XMaterial.CHICKEN) || (!Feudal.getVersion().equals("1.7") && m.equals(XMaterial.valueOf("MUTTON")))
                        || m.equals(XMaterial.LEATHER) || (!Feudal.getVersion().equals("1.7") && m.equals(XMaterial.valueOf("RABBIT_FOOT"))) || (!Feudal.getVersion().equals("1.7") && m.equals(XMaterial.valueOf("RABBIT_HIDE")))
                        || (m.equals(XMaterial.INK_SAC) && dur == 0)

        )){
            return true;
        }else if(type.equals(Profession.Type.MINER) && (

                m.equals(XMaterial.COAL) || m.equals(XMaterial.COAL_BLOCK) || m.equals(XMaterial.IRON_INGOT) || m.equals(XMaterial.IRON_BLOCK)
                        || m.equals(XMaterial.IRON_ORE) || m.equals(XMaterial.GOLD_INGOT) || m.equals(XMaterial.GOLD_BLOCK)
                        || m.equals(XMaterial.GOLD_ORE) || m.equals(XMaterial.EMERALD) || m.equals(XMaterial.EMERALD_BLOCK)
                        || m.equals(XMaterial.DIAMOND) || m.equals(XMaterial.DIAMOND_BLOCK) || m.equals(XMaterial.LAPIS_BLOCK)
                        || (m.equals(XMaterial.INK_SAC) && dur == 4) || m.equals(XMaterial.QUARTZ_BLOCK) || m.equals(XMaterial.QUARTZ)

        )){
            return true;
        }else if(type.equals(Profession.Type.COOK) && (

                m.equals(XMaterial.COOKED_BEEF) || m.equals(XMaterial.COOKED_CHICKEN) || m.equals(XMaterial.COOKED_COD) || (!Feudal.getVersion().equals("1.7") && m.equals(XMaterial.valueOf("COOKED_MUTTON")))
                        || (!Feudal.getVersion().equals("1.7") && m.equals(XMaterial.valueOf("COOKED_RABBIT"))) || m.equals(XMaterial.BAKED_POTATO) || m.equals(XMaterial.COOKED_PORKCHOP)
                        || m.equals(XMaterial.COOKIE) || m.equals(XMaterial.CAKE) || (!Feudal.getVersion().equals("1.7") && m.equals(XMaterial.valueOf("RABBIT_STEW")))
                        || m.equals(XMaterial.MUSHROOM_STEW.parseMaterial()) || m.equals(XMaterial.PUMPKIN_PIE) || m.equals(XMaterial.BEETROOT_SOUP)

        )){
            return true;
        }else if(type.equals(Profession.Type.FISHER) && (

                m.equals(XMaterial.FISHING_ROD) || m.equals(XMaterial.COOKED_COD) || m.equals(XMaterial.COD)

        )){
            return true;
        }else if(type.equals(Profession.Type.SHEPHERD) && (

                m.equals(XMaterial.CHICKEN) || m.equals(XMaterial.BEEF) || m.equals(XMaterial.LEATHER)
                        || m.equals(XMaterial.PORKCHOP) || (!Feudal.getVersion().equals("1.7") && m.equals(XMaterial.valueOf("RABBIT_HIDE"))) || (!Feudal.getVersion().equals("1.7") && m.equals(XMaterial.valueOf("RABBIT_FOOT")))
                        || m.equals(XMaterial.WHITE_WOOL.parseItem()) || (!Feudal.getVersion().equals("1.7") && m.equals(XMaterial.valueOf("MUTTON"))) || m.equals(XMaterial.EGG)

        )){
            return true;
        }else if(type.equals(Profession.Type.SCRIBE) && (

                m.equals(XMaterial.WRITTEN_BOOK) || m.equals(XMaterial.BOOK) || m.equals(XMaterial.WRITABLE_BOOK)

        )){
            return true;
        }else if(( type.equals(Profession.Type.GUARD) || type.equals(Profession.Type.SQUIRE) || type.equals(Profession.Type.KNIGHT)  || type.equals(Profession.Type.BARON) )&& (

                m.equals(XMaterial.ROTTEN_FLESH) || m.equals(XMaterial.SPIDER_EYE) || m.equals(XMaterial.STRING) || m.equals(XMaterial.BONE) ||
                        m.equals(XMaterial.ENDER_PEARL) || m.equals(XMaterial.SHULKER_SHELL) || m.equals(XMaterial.TOTEM_OF_UNDYING)

        )){
            return true;
        }else if(type.equals(Profession.Type.ALCHEMIST) && (

                m.equals(XMaterial.POTION) || m.equals(XMaterial.LINGERING_POTION) || m.equals(XMaterial.SPLASH_POTION) || m.equals(XMaterial.DRAGON_BREATH)

        )){
            return true;
        }else if(type.equals(Profession.Type.BLACKSMITH) && (

                m.equals(XMaterial.WOODEN_SWORD) || m.equals(XMaterial.WOODEN_AXE) || m.equals(XMaterial.WOODEN_PICKAXE) || m.equals(XMaterial.WOODEN_SHOVEL) || m.equals(XMaterial.WOODEN_HOE) ||
                        m.equals(XMaterial.IRON_SWORD) || m.equals(XMaterial.IRON_AXE) || m.equals(XMaterial.IRON_PICKAXE) || m.equals(XMaterial.IRON_SHOVEL) || m.equals(XMaterial.IRON_HOE) ||
                        m.equals(XMaterial.GOLDEN_SWORD) || m.equals(XMaterial.GOLDEN_AXE) || m.equals(XMaterial.GOLDEN_PICKAXE) || m.equals(XMaterial.GOLDEN_SHOVEL) || m.equals(XMaterial.GOLDEN_HOE) ||
                        m.equals(XMaterial.DIAMOND_SWORD) || m.equals(XMaterial.DIAMOND_AXE) || m.equals(XMaterial.DIAMOND_PICKAXE) || m.equals(XMaterial.DIAMOND_SHOVEL) || m.equals(XMaterial.DIAMOND_HOE) ||
                        m.equals(XMaterial.BOW) || m.equals(XMaterial.ARROW) || m.equals(XMaterial.DIAMOND_HORSE_ARMOR.parseItem()) || m.equals(XMaterial.IRON_HORSE_ARMOR.parseItem()) || m.equals(XMaterial.GOLDEN_HORSE_ARMOR.parseItem()) ||
                        m.equals(XMaterial.LEATHER_BOOTS) || m.equals(XMaterial.LEATHER_LEGGINGS) || m.equals(XMaterial.LEATHER_CHESTPLATE) || m.equals(XMaterial.LEATHER_HELMET) ||
                        m.equals(XMaterial.GOLDEN_BOOTS) || m.equals(XMaterial.GOLDEN_LEGGINGS) || m.equals(XMaterial.GOLDEN_CHESTPLATE) || m.equals(XMaterial.GOLDEN_HELMET) ||
                        m.equals(XMaterial.CHAINMAIL_BOOTS) || m.equals(XMaterial.CHAINMAIL_LEGGINGS) || m.equals(XMaterial.CHAINMAIL_CHESTPLATE) || m.equals(XMaterial.CHAINMAIL_HELMET) ||
                        m.equals(XMaterial.IRON_BOOTS) || m.equals(XMaterial.IRON_LEGGINGS) || m.equals(XMaterial.IRON_CHESTPLATE) || m.equals(XMaterial.IRON_HELMET) ||
                        m.equals(XMaterial.DIAMOND_BOOTS) || m.equals(XMaterial.DIAMOND_LEGGINGS) || m.equals(XMaterial.DIAMOND_CHESTPLATE) || m.equals(XMaterial.DIAMOND_HELMET)

        )){
            return true;
        }else if(type.equals(Profession.Type.HEALER) && (

                m.equals(XMaterial.GOLDEN_APPLE) || m.equals(XMaterial.GOLDEN_CARROT)

        )){
            return true;
        }else if(type.equals(Profession.Type.BARON) && (

                m.getMaxDurability() == 0

        )){
            return true;
        }else if(type.equals(Profession.Type.KING)){
            return true;
        }else{
            if(m.equals(XMaterial.WOODEN_SWORD.parseItem()) || m.equals(XMaterial.WOODEN_AXE) || m.equals(XMaterial.WOODEN_PICKAXE) || m.equals(XMaterial.WOODEN_SHOVEL) || m.equals(XMaterial.WOODEN_HOE) ||
                    m.equals(XMaterial.IRON_SWORD) || m.equals(XMaterial.IRON_AXE) || m.equals(XMaterial.IRON_PICKAXE) || m.equals(XMaterial.IRON_SHOVEL) || m.equals(XMaterial.IRON_HOE) ||
                    m.equals(XMaterial.GOLDEN_SWORD) || m.equals(XMaterial.GOLDEN_AXE) || m.equals(XMaterial.GOLDEN_PICKAXE) || m.equals(XMaterial.GOLDEN_SHOVEL) || m.equals(XMaterial.GOLDEN_HOE) ||
                    m.equals(XMaterial.DIAMOND_SWORD) || m.equals(XMaterial.DIAMOND_AXE) || m.equals(XMaterial.DIAMOND_PICKAXE) || m.equals(XMaterial.DIAMOND_SHOVEL) || m.equals(XMaterial.DIAMOND_HOE) ||
                    m.equals(XMaterial.BOW) || m.equals(XMaterial.ARROW) || m.equals(XMaterial.DIAMOND_HORSE_ARMOR.parseItem()) || m.equals(XMaterial.IRON_HORSE_ARMOR.parseItem()) || m.equals(XMaterial.GOLDEN_HORSE_ARMOR.parseItem()) ||
                    m.equals(XMaterial.LEATHER_BOOTS) || m.equals(XMaterial.LEATHER_LEGGINGS) || m.equals(XMaterial.LEATHER_CHESTPLATE) || m.equals(XMaterial.LEATHER_HELMET) ||
                    m.equals(XMaterial.GOLDEN_BOOTS) || m.equals(XMaterial.GOLDEN_LEGGINGS) || m.equals(XMaterial.GOLDEN_CHESTPLATE) || m.equals(XMaterial.GOLDEN_HELMET) ||
                    m.equals(XMaterial.CHAINMAIL_BOOTS) || m.equals(XMaterial.CHAINMAIL_LEGGINGS) || m.equals(XMaterial.CHAINMAIL_CHESTPLATE) || m.equals(XMaterial.CHAINMAIL_HELMET) ||
                    m.equals(XMaterial.IRON_BOOTS) || m.equals(XMaterial.IRON_LEGGINGS) || m.equals(XMaterial.IRON_CHESTPLATE) || m.equals(XMaterial.IRON_HELMET) ||
                    m.equals(XMaterial.DIAMOND_BOOTS) || m.equals(XMaterial.DIAMOND_LEGGINGS) || m.equals(XMaterial.DIAMOND_CHESTPLATE) || m.equals(XMaterial.DIAMOND_HELMET)

                    ||

                    m.equals(XMaterial.GOLDEN_APPLE) || m.equals(XMaterial.GOLDEN_CARROT) || m.equals(XMaterial.POTION) || m.equals(XMaterial.LINGERING_POTION) || m.equals(XMaterial.SPLASH_POTION)

                    ||

                    m.equals(XMaterial.ROTTEN_FLESH) || m.equals(XMaterial.SPIDER_EYE) || m.equals(XMaterial.STRING) || m.equals(XMaterial.BONE) ||
                    m.equals(XMaterial.ENDER_PEARL)

                    ||

                    m.equals(XMaterial.WRITTEN_BOOK) || m.equals(XMaterial.BOOK) || m.equals(XMaterial.WRITABLE_BOOK)

                    ||

                    m.equals(XMaterial.CHICKEN) || m.equals(XMaterial.BEEF) || m.equals(XMaterial.LEATHER)
                    || m.equals(XMaterial.PORKCHOP) || (!Feudal.getVersion().equals("1.7") && m.equals(XMaterial.valueOf("RABBIT_HIDE"))) || (!Feudal.getVersion().equals("1.7") && m.equals(XMaterial.valueOf("RABBIT_FOOT")))
                    || m.equals(XMaterial.WHITE_WOOL.parseItem()) || (!Feudal.getVersion().equals("1.7") && m.equals(XMaterial.valueOf("MUTTON"))) || m.equals(XMaterial.EGG)

                    ||

                    m.equals(XMaterial.FISHING_ROD) || m.equals(XMaterial.COOKED_COD) || m.equals(XMaterial.COD)

                    ||

                    m.equals(XMaterial.COOKED_BEEF) || m.equals(XMaterial.COOKED_CHICKEN) || m.equals(XMaterial.COOKED_COD) || (!Feudal.getVersion().equals("1.7") && m.equals(XMaterial.valueOf("COOKED_MUTTON")))
                    || (!Feudal.getVersion().equals("1.7") && m.equals(XMaterial.valueOf("COOKED_RABBIT"))) || m.equals(XMaterial.BAKED_POTATO) || m.equals(XMaterial.COOKED_PORKCHOP)
                    || m.equals(XMaterial.COOKIE) || m.equals(XMaterial.CAKE) || (!Feudal.getVersion().equals("1.7") && m.equals(XMaterial.valueOf("RABBIT_STEW")))
                    || m.equals(XMaterial.MUSHROOM_STEW.parseItem()) || m.equals(XMaterial.PUMPKIN_PIE)

                    ||

                    m.equals(XMaterial.COAL) || m.equals(XMaterial.COAL_BLOCK) || m.equals(XMaterial.IRON_INGOT) || m.equals(XMaterial.IRON_BLOCK)
                    || m.equals(XMaterial.IRON_ORE) || m.equals(XMaterial.GOLD_INGOT) || m.equals(XMaterial.GOLD_BLOCK)
                    || m.equals(XMaterial.GOLD_ORE) || m.equals(XMaterial.EMERALD) || m.equals(XMaterial.EMERALD_BLOCK)
                    || m.equals(XMaterial.DIAMOND) || m.equals(XMaterial.DIAMOND_BLOCK) || m.equals(XMaterial.LAPIS_BLOCK)
                    || (m.equals(XMaterial.INK_SAC) && dur == 4) || m.equals(XMaterial.QUARTZ_BLOCK) || m.equals(XMaterial.QUARTZ)

                    ||

                    m.equals(XMaterial.PORKCHOP) || m.equals(XMaterial.BEEF) || m.equals(XMaterial.CHICKEN) || (!Feudal.getVersion().equals("1.7") && m.equals(XMaterial.valueOf("MUTTON")))
                    || m.equals(XMaterial.LEATHER) || (!Feudal.getVersion().equals("1.7") && m.equals(XMaterial.valueOf("RABBIT_FOOT"))) || (!Feudal.getVersion().equals("1.7") && m.equals(XMaterial.valueOf("RABBIT_HIDE")))
                    || (m.equals(XMaterial.INK_SAC) && dur == 0)

                    ||

                    m.equals(XMaterial.OAK_LOG.parseItem()) || m.equals(XMaterial.OAK_LOG.parseItem()) || m.equals(XMaterial.OAK_WOOD.parseItem())

                    ||

                    m.equals(XMaterial.HAY_BLOCK) || m.equals(XMaterial.WHEAT_SEEDS.parseItem()) || m.equals(XMaterial.PUMPKIN_SEEDS) || m.equals(XMaterial.WHEAT)
                    || m.equals(XMaterial.MELON_SEEDS) || (m.equals(XMaterial.INK_SAC) && dur == 3) || m.equals(XMaterial.SUGAR_CANE)
                    || m.equals(XMaterial.NETHER_WART) || m.equals(XMaterial.POTATO) || m.equals(XMaterial.CARROT)
                    || m.equals(XMaterial.BAKED_POTATO)
            ){
                return false;
            }else{
                return true;
            }
        }
    }

    private static void sellSAD(Material type, double amount) {
        final double marketAmount = countMaterials(type);

        // multipliers
        double min_multiplier, max_multiplier;

        min_multiplier = Feudal.getMarketConfig().getConfig().getDouble("Min-multiplier");
        max_multiplier = Feudal.getMarketConfig().getConfig().getDouble("Max-multiplier");

        //System.out.println("TYPE: " + type + ", amount: " + amount + ", marketAmount: " + marketAmount);
        double sad = getSAD(type.toString()) - (amount/marketAmount/10);
        if(sad > max_multiplier){
            sad = max_multiplier;
        }else if(sad < min_multiplier){
            sad = min_multiplier;
        }
        setSAD(type.toString(), sad);
    }

    private static void buySAD(Material type, double amount) {
        final double amountAfter = countMaterials(type) - amount;

        // multipliers
        double min_multiplier, max_multiplier;

        min_multiplier = Feudal.getMarketConfig().getConfig().getDouble("Min-multiplier");
        max_multiplier = Feudal.getMarketConfig().getConfig().getDouble("Max-multiplier");

        if(amountAfter <= 0){
            setSAD(type.toString(), 1);
        }
        double sad = getSAD(type.toString()) + (amount / amountAfter/10);
        if(sad > max_multiplier){
            sad = max_multiplier;
        }else if(sad < min_multiplier){
            sad = min_multiplier;
        }
        setSAD(type.toString(), sad);
    }

    protected static double countMaterials(Material type) {
        int total = 0;
        for(final MarketItem item : Feudal.getMarketItems()){
            if(item != null){
                if(!item.isMerchant()){
                    if(item.getItem().getType().equals(type)){
                        total+=item.getAmount();
                    }
                }
            }
        }
        return total;
    }

    private static void setSAD(String path, double d) {
        Feudal.getMarketData().getConfig().set("SAD." + path, d);
    }

    public static double getSAD(String path) {
        if(Feudal.getMarketData().getConfig().contains("SAD." + path)){
            if(!Feudal.getMarketConfig().getConfig().getBoolean("SupplyAndDemand")){
                return 1;
            }
            return Feudal.getMarketData().getConfig().getDouble("SAD." + path);
        }else{
            Feudal.getMarketData().getConfig().set("SAD." + path, 1.0);
            return 1;
        }
    }

    public static float predictSAD(String string, double selectAmount,
                                   double materialAmount) {
        double sad = getSAD(string);
        double after = materialAmount - selectAmount;

        // multipliers
        double min_multiplier, max_multiplier;
        min_multiplier = Feudal.getMarketConfig().getConfig().getDouble("Min-multiplier");
        max_multiplier = Feudal.getMarketConfig().getConfig().getDouble("Max-multiplier");

        if(after <= 0){
            after = 1;
        }
        sad += selectAmount / after / 10;
        if(sad < min_multiplier){
            return (float)min_multiplier;
        }else if(sad > max_multiplier){
            return (float) max_multiplier;
        }else{
            return (float) sad;
        }
    }

    public static void updateSAD() {
        final HashMap<String, Double> setValues = new HashMap<>();
        if(Feudal.getMarketData().getConfig().contains("SAD")){
            for(final String key : Feudal.getMarketData().getConfig().getConfigurationSection("SAD").getKeys(false)){
                double d = Feudal.getMarketData().getConfig().getDouble("SAD." + key);
                if(d > 1){
                    d -= d*0.01;
                }else if(d < 1){
                    d += d*0.01;
                }
                setValues.put(key, d);
            }
        }
        for(final String key : setValues.keySet()){
            Feudal.getMarketData().getConfig().set("SAD." + key, setValues.get(key));
        }
        Feudal.saveMarketItems();
    }

    protected static double getItemBuyPriceStd(ItemStack stack, String tag){
        final int dur = stack.getDurability();
        int b = 0;
        if(Feudal.getMarketConfig().getConfig().contains("prices." + stack.getType().toString() + ".buy." + dur)){
            b = 1;
        }
        if(Feudal.getMarketConfig().getConfig().contains("prices." + stack.getType().toString() + ".buy.price") || b > 0){
            double price = 0;
            if(tag != null){
                price = Feudal.getMarketConfig().getConfig().getDouble("prices." + stack.getType().toString() + ".buy." + tag);
            }else if(b == 1){
                price = Feudal.getMarketConfig().getConfig().getDouble("prices." + stack.getType().toString() + ".buy." + dur);
            }else{
                price = Feudal.getMarketConfig().getConfig().getDouble("prices." + stack.getType().toString() + ".buy.price");
            }

            //Enchantments
            if(stack != null && stack.getEnchantments() != null && stack.getEnchantments().size() > 0){
                for(final Enchantment e : stack.getEnchantments().keySet()){
                    if(Feudal.getMarketConfig().getConfig().contains("enchantmentPrices.buy." + e.getName())){
                        price += (Feudal.getMarketConfig().getConfig().getDouble("enchantmentPrices.buy." + e.getName()) * stack.getEnchantmentLevel(e));
                    }
                }
            }

            final int maxDur = stack.getType().getMaxDurability();
            if(maxDur > 0){
                if(dur >= maxDur){
                    return 0;
                }
                if(Feudal.getMarketConfig().getConfig().getBoolean("priceToolsOnDurability")){
                    price *= (1 - ((double) dur / (double) maxDur));
                }
            }

            price *= Feudal.getMarketConfig().getConfig().getDouble("Buy-multiplier");

            return price;
        }else{
            return 0;
        }
    }

    public static double getItemSellPriceFreshStd(ItemStack stack, String tag){
        final int dur = stack.getDurability();
        int b = 0;
        if(Feudal.getMarketConfig().getConfig().contains("prices." + stack.getType().toString() + ".sell." + dur)){
            b = 1;
        }
        if(Feudal.getMarketConfig().getConfig().contains("prices." + stack.getType().toString() + ".sell.price") || b > 0){
            double price = 0;
            if(tag != null){
                price = Feudal.getMarketConfig().getConfig().getDouble("prices." + stack.getType().toString() + ".sell." + tag);
            }else if(b == 1){
                price = Feudal.getMarketConfig().getConfig().getDouble("prices." + stack.getType().toString() + ".sell." + dur);
            }else{
                price = Feudal.getMarketConfig().getConfig().getDouble("prices." + stack.getType().toString() + ".sell.price");
            }

            //Enchantments
            if(stack != null && stack.getEnchantments() != null && stack.getEnchantments().size() > 0){
                for(final Enchantment e : stack.getEnchantments().keySet()){
                    if(Feudal.getMarketConfig().getConfig().contains("enchantmentPrices.sell." + e.getName())){
                        price += (Feudal.getMarketConfig().getConfig().getDouble("enchantmentPrices.sell." + e.getName()) * stack.getEnchantmentLevel(e));
                    }
                }
            }

            final int maxDur = stack.getType().getMaxDurability();
            if(maxDur > 0){
                if(dur > 0 && !Feudal.getMarketConfig().getConfig().getBoolean("canSellDamagedItems")){
                    return 0;
                }
                if(dur >= maxDur){
                    return 0;
                }
                if(maxDur - dur < Feudal.getMarketConfig().getConfig().getInt("minDurability")){
                    return 0;
                }
                if(Feudal.getMarketConfig().getConfig().getBoolean("priceToolsOnDurability")){
                    price *= (1 - ((double) dur / (double) maxDur));
                }
            }

            price *= Market.getSAD(stack.getType().toString());
            price *= Feudal.getMarketConfig().getConfig().getDouble("Sell-multiplier");

            return price;
        }else{
            return 0;
        }
    }
}