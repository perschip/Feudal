package me.invertmc.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cryptomorin.xseries.XMaterial;
import me.invertmc.Feudal;
import me.invertmc.user.attributes.Attribute;
import me.invertmc.user.attributes.Attributes;
import me.invertmc.utils.InventoryGui;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class AttributeRedistribute {
    private final InventoryGui inventory1;
    private final Player player;
    private final User user;
    private int points = 0;
    private static int pointsPer = Feudal.getConfiguration().getConfig().getInt("redistribute.pointsPer");

    private int strength = 0;
    private int toughness = 0;
    private int speed = 0;
    private int stamina = 0;
    private int luck = 0;

    private boolean complete = false;
    //private boolean open = false;
    //private boolean running = false;
    //private int schedule = -1;


    /**
     * Creates an inventory selection setup for a new player.
     * @param player
     */
    public AttributeRedistribute(User user, Player player) {
        this.user = user;
        this.player = player;
        setupDefaults();
        inventory1 = createAttributesInventory();
        openInventory();
    }

    private void setupDefaults() {
        final int extra = (user.getStrength().getLevel() % pointsPer) +
                (user.getToughness().getLevel() % pointsPer) +
                (user.getSpeed().getLevel() % pointsPer) +
                (user.getStamina().getLevel() % pointsPer) +
                (user.getLuck().getLevel() % pointsPer);
        strength = (user.getStrength().getLevel() / pointsPer) * pointsPer;
        toughness = (user.getToughness().getLevel() / pointsPer) * pointsPer;
        speed = (user.getSpeed().getLevel() / pointsPer) * pointsPer;
        stamina = (user.getStamina().getLevel() / pointsPer) * pointsPer;
        luck = (user.getLuck().getLevel() / pointsPer) * pointsPer;
        points = extra / 10;
        /*
         * Have levels round DOWN to nearest tens place.
         * Any levels lost from remaining (int) / 10 as POINTS
         */
    }

    /**
     * Opens the inventory the player is currently set to use.
     */
    public void openInventory(){
        //open = false;
        player.openInventory(inventory1.getInventory());
    }

    private InventoryGui createAttributesInventory() {
        final InventoryGui i = new InventoryGui(Bukkit.createInventory(player, 27, InventoryGui.lim(Feudal.getMessage("sel_gui.setupAtt"))));

        setAttributeItems(i);

        return i;
    }

    private void setAttributeItems(InventoryGui i){
        i.clear();

        i.setItem("POINTS", 18, createItem(XMaterial.EMERALD.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.points") + points, new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.pointsAtt")}))));
        i.setItem("CANCEL", 0, createItem(XMaterial.TNT.parseMaterial(), 1, (short) 0, Feudal.getMessage("redistribute.cancel"), null));


        i.setItem("ADD_STRENGTH", 2, createItem(XMaterial.GREEN_WOOL.parseMaterial(), 1, (short) 11, Feudal.getMessage("sel_gui.addStrength"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.addCost"), Feudal.getMessage("sel_gui.addLevel") + pointsPer}))));
        i.setItem("ADD_TOUGHNESS", 3, createItem(XMaterial.GREEN_WOOL.parseMaterial(), 1, (short) 11, Feudal.getMessage("sel_gui.addToughness"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.addCost"), Feudal.getMessage("sel_gui.addLevel") + pointsPer}))));
        i.setItem("ADD_SPEED", 4, createItem(XMaterial.GREEN_WOOL.parseMaterial(), 1, (short) 11, Feudal.getMessage("sel_gui.addSpeed"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.addCost"), Feudal.getMessage("sel_gui.addLevel") + pointsPer}))));
        i.setItem("ADD_STAMINA", 5, createItem(XMaterial.GREEN_WOOL.parseMaterial(), 1, (short) 11, Feudal.getMessage("sel_gui.addStamina"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.addCost"), Feudal.getMessage("sel_gui.addLevel") + pointsPer}))));
        i.setItem("ADD_LUCK", 6, createItem(XMaterial.GREEN_WOOL.parseMaterial(), 1, (short) 11, Feudal.getMessage("sel_gui.addLuck"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.addCost"), Feudal.getMessage("sel_gui.addLevel") + pointsPer}))));

        i.setItem("SUBTRACT_STRENGTH", 20, createItem(XMaterial.RED_WOOL.parseMaterial(), 1, (short) 4, Feudal.getMessage("sel_gui.remStrength"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.earn"), Feudal.getMessage("sel_gui.remLevel") + pointsPer}))));
        i.setItem("SUBTRACT_TOUGHNESS", 21, createItem(XMaterial.RED_WOOL.parseMaterial(), 1, (short) 4, Feudal.getMessage("sel_gui.remToughness"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.earn"), Feudal.getMessage("sel_gui.remLevel") + pointsPer}))));
        i.setItem("SUBTRACT_SPEED", 22, createItem(XMaterial.RED_WOOL.parseMaterial(), 1, (short) 4, Feudal.getMessage("sel_gui.remSpeed"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.earn"), Feudal.getMessage("sel_gui.remLevel") + pointsPer}))));
        i.setItem("SUBTRACT_STAMINA", 23, createItem(XMaterial.RED_WOOL.parseMaterial(), 1, (short) 4, Feudal.getMessage("sel_gui.remStamina"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.earn"), Feudal.getMessage("sel_gui.remLevel") + pointsPer}))));
        i.setItem("SUBTRACT_LUCK", 24, createItem(XMaterial.RED_WOOL.parseMaterial(), 1, (short) 4, Feudal.getMessage("sel_gui.remLuck"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.earn"), Feudal.getMessage("sel_gui.remLevel") + pointsPer}))));

        i.setItem("STRENGTH", 11, createItem(XMaterial.ANVIL.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.strengthT"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.level")+strength+" / " + Feudal.getConfiguration().getConfig().getInt("Strength.MaxLevel"), Feudal.getMessage("sel_gui.strengthT1") + Attributes.getStrengthDamageEffect(strength), Feudal.getMessage("sel_gui.strengthT2") + Attributes.getStrengthHasteEffect(strength), Feudal.getMessage("sel_gui.strengthT3"), Feudal.getMessage("sel_gui.strengthT4")}))));
        i.setItem("TOUGHNESS", 12, createItem(XMaterial.BEDROCK.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.toughnessT"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.level")+toughness+" / " + Feudal.getConfiguration().getConfig().getInt("Toughness.MaxLevel"), Feudal.getMessage("sel_gui.toughnessT1") + Attributes.getToughnessHealthEffect(toughness), Feudal.getMessage("sel_gui.toughnessT2") + Attributes.getToughnessRegenRate(toughness), Feudal.getMessage("sel_gui.toughnessT3"), Feudal.getMessage("sel_gui.toughnessT4")}))));
        i.setItem("SPEED", 13, createItem(XMaterial.BLAZE_POWDER.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.speedT"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.level")+speed+" / " + Feudal.getConfiguration().getConfig().getInt("Speed.MaxLevel"), Feudal.getMessage("sel_gui.speedT1") + Attributes.getSpeedEffect(speed), Feudal.getMessage("sel_gui.speedT2")}))));
        i.setItem("STAMINA", 14, createItem(XMaterial.LEATHER_BOOTS.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.staminaT"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.level")+stamina+" / " + Feudal.getConfiguration().getConfig().getInt("Stamina.MaxLevel"), Feudal.getMessage("sel_gui.staminaT1") + Attributes.getStaminaEffect(stamina), Feudal.getMessage("sel_gui.staminaT2")}))));
        i.setItem("LUCK", 15, createItem(XMaterial.GOLD_INGOT.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.luckT"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.level")+luck+" / " + Feudal.getConfiguration().getConfig().getInt("Luck.MaxLevel"), Feudal.getMessage("sel_gui.luckT1") + Feudal.round((Attributes.getLuck(luck)*100)) + "%"}))));


        i.setItem("FINISH", 8, createItem(XMaterial.CRAFTING_TABLE.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.finish"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.saveAtt1"), Feudal.getMessage("sel_gui.saveAtt2"), Feudal.getMessage("sel_gui.saveAtt3"), Feudal.getMessage("sel_gui.saveAtt4"), Feudal.getMessage("sel_gui.saveAtt5"), Feudal.getMessage("sel_gui.saveAtt6")}))));

        i.setItem("INFO", 26, createItem(XMaterial.BOOK.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.helpAtt"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.helpAtt1"), Feudal.getMessage("sel_gui.helpAtt3"), Feudal.getMessage("sel_gui.helpAtt4"), Feudal.getMessage("sel_gui.helpAtt5"), Feudal.getMessage("sel_gui.helpAtt6")}))));

    }

    private void updateAttributesInventory() {
        final InventoryGui i = inventory1;
        setAttributeItems(i);
    }

    /**
     * Gets the player.
     * @return
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Creates an item with a type, amount, durability, name, and lore.
     * @param type
     * @param amount
     * @param data
     * @param name
     * @param lore
     * @return
     */
    public static ItemStack createItem(Material type, int amount, short data, String name, List<String> lore){
        final ItemStack i = new ItemStack(type, amount, data);
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

    private void soundDeny(){
        if(!Feudal.getVersion().contains("1.8") && !Feudal.getVersion().equals("1.7")){
            player.playSound(player.getLocation(), Sound.valueOf("BLOCK_ANVIL_LAND"), 1, 0.5F);
        }else{
            player.playSound(player.getLocation(), Sound.valueOf("ANVIL_LAND"), 1, 0.5F);
        }
    }

    private void soundClick(){
        if(!Feudal.getVersion().equals("1.8") && !Feudal.getVersion().equals("1.7")){
            player.playSound(player.getLocation(), Sound.valueOf("UI_BUTTON_CLICK"), 1, 1);
        }else{
            player.playSound(player.getLocation(), Sound.valueOf("CLICK"), 1, 1);
        }
    }

    private void soundComplete(){
        if(!Feudal.getVersion().contains("1.8") && !Feudal.getVersion().equals("1.7")){
            player.playSound(player.getLocation(), Sound.valueOf("ENTITY_PLAYER_LEVELUP"), 1, 0.5F);
        }else{
            player.playSound(player.getLocation(), Sound.valueOf("LEVEL_UP"), 1, 0.5F);
        }
    }

    private void soundPitched(float value, float max){
        if(!Feudal.getVersion().contains("1.8") && !Feudal.getVersion().equals("1.7")){
            player.playSound(player.getLocation(), Sound.valueOf("ENTITY_ARROW_HIT_PLAYER"), 1, (float) (((value * (1.7)) / max) + 0.3));
        }else{
            player.playSound(player.getLocation(), Sound.valueOf("ARROW_HIT"), 1, (float) (((value * (1.7)) / max) + 0.3));
        }
    }

    private void delayOpen(){
		/*open = true;
		if(running){
			Bukkit.getScheduler().cancelTask(schedule);
		}
		running = true;
		schedule = Bukkit.getScheduler().scheduleSyncDelayedTask(Feudal.getPlugin(), new Runnable(){
			public void run(){
				if(open){
					running = false;
					player.closeInventory();
					AttributeRedistribute.this.openInventory();
				}
			}
		}, 20);*/
    }

    private boolean click(String itemString) {
        if(itemString != null){
            if(itemString.equals("ADD_STRENGTH")){
                if(points >= 1){
                    final int rate = pointsPer;
                    if(!attributeIsMax(rate) || !Feudal.getConfiguration().getConfig().getBoolean("attributeCap.enable")){
                        final int max = Feudal.getConfiguration().getConfig().getInt("Strength.MaxLevel");
                        if(strength >= max){
                            soundDeny();
                            player.sendMessage(Feudal.getMessage("selection.s"));
                            delayOpen();
                        }else{
                            points--;
                            strength += rate;
                            if(strength > max){
                                strength = max;
                            }
                            soundPitched(strength, max);
                            updateAttributesInventory();
                            player.updateInventory();
                        }
                    }else{
                        soundDeny();
                        player.sendMessage(Feudal.getMessage("selection.maxAtt"));
                        delayOpen();
                    }
                }else{
                    soundDeny();
                    player.sendMessage(Feudal.getMessage("selection.np"));
                    delayOpen();
                }
            }else if(itemString.equals("ADD_TOUGHNESS")){
                if(points >= 1){
                    final int rate = pointsPer;
                    if(!attributeIsMax(rate) || !Feudal.getConfiguration().getConfig().getBoolean("attributeCap.enable")){
                        final int max = Feudal.getConfiguration().getConfig().getInt("Toughness.MaxLevel");
                        if(toughness >= max){
                            soundDeny();
                            player.sendMessage(Feudal.getMessage("selection.t"));
                            delayOpen();
                        }else{
                            points--;
                            toughness += rate;
                            if(toughness > max){
                                toughness = max;
                            }
                            soundPitched(toughness, max);
                            updateAttributesInventory();
                            player.updateInventory();
                        }
                    }else{
                        soundDeny();
                        player.sendMessage(Feudal.getMessage("selection.maxAtt"));
                        delayOpen();
                    }
                }else{
                    soundDeny();
                    player.sendMessage(Feudal.getMessage("selection.np"));
                    delayOpen();
                }
            }else if(itemString.equals("ADD_SPEED")){
                if(points >= 1){
                    final int rate = pointsPer;
                    if(!attributeIsMax(rate) || !Feudal.getConfiguration().getConfig().getBoolean("attributeCap.enable")){
                        final int max = Feudal.getConfiguration().getConfig().getInt("Speed.MaxLevel");
                        if(speed >= max){
                            soundDeny();
                            player.sendMessage(Feudal.getMessage("selection.sp"));
                            delayOpen();
                        }else{
                            points--;
                            speed += rate;
                            if(speed > max){
                                speed = max;
                            }
                            soundPitched(speed, max);
                            updateAttributesInventory();
                            player.updateInventory();
                        }
                    }else{
                        soundDeny();
                        player.sendMessage(Feudal.getMessage("selection.maxAtt"));
                        delayOpen();
                    }
                }else{
                    soundDeny();
                    player.sendMessage(Feudal.getMessage("selection.np"));
                    delayOpen();
                }
            }else if(itemString.equals("ADD_STAMINA")){
                if(points >= 1){
                    final int rate = pointsPer;
                    if(!attributeIsMax(rate) || !Feudal.getConfiguration().getConfig().getBoolean("attributeCap.enable")){
                        final int max = Feudal.getConfiguration().getConfig().getInt("Stamina.MaxLevel");
                        if(stamina >= max){
                            soundDeny();
                            player.sendMessage(Feudal.getMessage("selection.st"));
                            delayOpen();
                        }else{
                            points--;
                            stamina += rate;
                            if(stamina > max){
                                stamina = max;
                            }
                            soundPitched(stamina, max);
                            updateAttributesInventory();
                            player.updateInventory();
                        }
                    }else{
                        soundDeny();
                        player.sendMessage(Feudal.getMessage("selection.maxAtt"));
                        delayOpen();
                    }
                }else{
                    soundDeny();
                    player.sendMessage(Feudal.getMessage("selection.np"));
                    delayOpen();
                }
            }else if(itemString.equals("ADD_LUCK")){
                if(points >= 1){
                    final int rate = pointsPer;
                    if(!attributeIsMax(rate) || !Feudal.getConfiguration().getConfig().getBoolean("attributeCap.enable")){
                        final int max = Feudal.getConfiguration().getConfig().getInt("Luck.MaxLevel");
                        if(luck >= max){
                            soundDeny();
                            player.sendMessage(Feudal.getMessage("selection.l"));
                            delayOpen();
                        }else{
                            points--;
                            luck += rate;
                            if(luck > max){
                                luck = max;
                            }
                            soundPitched(luck, max);
                            updateAttributesInventory();
                            player.updateInventory();
                        }
                    }else{
                        soundDeny();
                        player.sendMessage(Feudal.getMessage("selection.maxAtt"));
                        delayOpen();
                    }
                }else{
                    soundDeny();
                    player.sendMessage(Feudal.getMessage("selection.np"));
                    delayOpen();
                }
            }else if(itemString.equals("SUBTRACT_STRENGTH")){
                final int rate = pointsPer;
                final int max = Feudal.getConfiguration().getConfig().getInt("Strength.MaxLevel");
                if(strength - rate < 0){
                    soundDeny();
                    player.sendMessage(Feudal.getMessage("selection.less0"));
                    delayOpen();
                }else{
                    points++;
                    if(strength == max && max % rate != 0){
                        strength -= (max % rate);
                    }else{
                        strength -= rate;
                    }
                    soundPitched(strength, max);
                    updateAttributesInventory();
                    player.updateInventory();
                }
            }else if(itemString.equals("SUBTRACT_TOUGHNESS")){
                final int rate = pointsPer;
                final int max = Feudal.getConfiguration().getConfig().getInt("Toughness.MaxLevel");
                if(toughness - rate < 0){
                    soundDeny();
                    player.sendMessage(Feudal.getMessage("selection.less0"));
                    delayOpen();
                }else{
                    points++;
                    if(toughness == max && max % rate != 0){
                        toughness -= (max % rate);
                    }else{
                        toughness -= rate;
                    }
                    soundPitched(toughness, max);
                    updateAttributesInventory();
                    player.updateInventory();
                }
            }else if(itemString.equals("SUBTRACT_SPEED")){
                final int rate = pointsPer;
                final int max = Feudal.getConfiguration().getConfig().getInt("Speed.MaxLevel");
                if(speed - rate < 0){
                    soundDeny();
                    player.sendMessage(Feudal.getMessage("selection.less0"));
                    delayOpen();
                }else{
                    points++;
                    if(speed == max && max % rate != 0){
                        speed -= (max % rate);
                    }else{
                        speed -= rate;
                    }
                    soundPitched(speed, max);
                    updateAttributesInventory();
                    player.updateInventory();
                }
            }else if(itemString.equals("SUBTRACT_STAMINA")){
                final int rate = pointsPer;
                final int max = Feudal.getConfiguration().getConfig().getInt("Stamina.MaxLevel");
                if(stamina - rate < 0){
                    soundDeny();
                    player.sendMessage(Feudal.getMessage("selection.less0"));
                    delayOpen();
                }else{
                    points++;
                    if(stamina == max && max % rate != 0){
                        stamina -= (max % rate);
                    }else{
                        stamina -= rate;
                    }
                    soundPitched(stamina, max);
                    updateAttributesInventory();
                    player.updateInventory();
                }
            }else if(itemString.equals("SUBTRACT_LUCK")){
                final int rate = pointsPer;
                final int max = Feudal.getConfiguration().getConfig().getInt("Luck.MaxLevel");
                if(luck - rate < 0){
                    soundDeny();
                    player.sendMessage(Feudal.getMessage("selection.less0"));
                    delayOpen();
                }else{
                    points++;
                    if(luck == max && max % rate != 0){
                        luck -= (max % rate);
                    }else{
                        luck -= rate;
                    }
                    soundPitched(luck, max);
                    updateAttributesInventory();
                    player.updateInventory();
                }
            }else if(itemString.equals("FINISH")){
                if(points <= 0 || attributeIsMax(pointsPer)){
                    if(Feudal.getOnlinePlayers().containsKey(player.getUniqueId().toString())){
                        soundComplete();
                        final User u = Feudal.getOnlinePlayers().get(player.getUniqueId().toString());
                        u.setStrength(new Attributes(Attribute.STRENGTH, 0, strength, u));
                        u.setToughness(new Attributes(Attribute.TOUGHNESS, 0, toughness, u));
                        u.setSpeed(new Attributes(Attribute.SPEED, 0, speed, u));
                        u.setStamina(new Attributes(Attribute.STAMINA, 0, stamina, u));
                        u.setLuck(new Attributes(Attribute.LUCK, 0, luck, u));
                        u.effectAttributes();
                        u.save(true);
                        complete = true;
                        player.closeInventory();
                        player.sendMessage(Feudal.getMessage("selection.c"));
                    }else{
                        soundDeny();
                        player.sendMessage(Feudal.getMessage("selection.cf"));
                    }
                }else{
                    soundDeny();
                    player.sendMessage(Feudal.getMessage("redistribute.spendPoints"));
                }
            }else if(itemString.equals("CANCEL")){
                soundClick();
                complete = true;
                player.closeInventory();
            }
            return true;
        }else{
            return false;
        }
    }

    private boolean attributeIsMax(int rate) {
        if(Feudal.getConfiguration().getConfig().getBoolean("attributeCap.enable") && !player.hasPermission("feudal.admin.passAttributeFix")){
            if(strength + speed + toughness + stamina + luck + rate > Feudal.getConfiguration().getConfig().getInt("attributeCap.cap")){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }



    //
    //
    //
    //
    //EVENTS:

    public static void closeEvent(InventoryCloseEvent event) {
        if(Feudal.getAttributeRedistributers().size() > 0){
            final ArrayList<AttributeRedistribute> remove = new ArrayList<>();
            for(final AttributeRedistribute sel : Feudal.getAttributeRedistributers()){
                if(sel.complete && !sel.getPlayer().equals(event.getPlayer())){
                    remove.add(sel);
                }else{
                    if(!sel.complete && sel.getPlayer().equals(event.getPlayer())){
                        remove.add(sel);
                    }
                }
            }
            for(final AttributeRedistribute sel : remove){
                Feudal.getAttributeRedistributers().remove(sel);
            }
        }
    }

    public static void inventoryClickEvent(InventoryClickEvent event) {
        if(Feudal.getAttributeRedistributers().size() > 0){
            final ArrayList<AttributeRedistribute> remove = new ArrayList<>();
            for(final AttributeRedistribute sel : Feudal.getAttributeRedistributers()){
                if(sel.complete){
                    remove.add(sel);
                }else{
                    if(sel.getPlayer().equals(event.getWhoClicked())){
                        if(sel.click(sel.inventory1.getItemString(event.getCurrentItem()))){
                            event.setCancelled(true);
                        }
                        if(event.getInventory().equals(sel.inventory1.getInventory())){
                            event.setCancelled(true);
                        }
                    }
                    if(sel.complete){
                        remove.add(sel);
                    }
                }
            }
            for(final AttributeRedistribute sel : remove){
                Feudal.getAttributeRedistributers().remove(sel);
            }
        }
    }

}
