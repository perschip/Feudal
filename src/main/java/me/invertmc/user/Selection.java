package me.invertmc.user;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cryptomorin.xseries.XMaterial;
import me.invertmc.Feudal;
import me.invertmc.api.events.UserCreateEvent;
import me.invertmc.user.attributes.Attribute;
import me.invertmc.user.attributes.Attributes;
import me.invertmc.user.classes.Profession;
import me.invertmc.utils.InventoryGui;
import me.invertmc.utils.Utils;
import me.invertmc.utils.VaultUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class Selection {
    private InventoryGui inventory1;
    private int inventory;
    private final Player player;
    private int points;
    private int pointsUsedClass;//used for going back
    private int pointsUsedJob;
    private int classIndex;//Used for going from attributes to job.

    private int strength = 0;
    private int toughness = 0;
    private int speed = 0;
    private int stamina = 0;
    private int luck = 0;

    private boolean complete = false;
    private boolean open = false;
    private boolean running = false;
    private int schedule = -1;

    private Profession profession;
    private final User user;

    /**
     * Creates an inventory selection setup for a new player.
     * @param player
     */
    public Selection(Player player) {
        points = Feudal.getConfiguration().getConfig().getInt("startPoints");
        this.player = player;
        user = Feudal.getUser(player.getUniqueId().toString());
        chooseSocialClass();
    }

    private void chooseSocialClass() {
        inventory = 0;
        inventory1 = createSocialClassInventory();
    }

    private void chooseJob(int type) {
        inventory = type;
        if(type == 1){
            classIndex = 1;
            inventory1 = createPeasantInventory();
        }
        else if(type == 2){
            classIndex = 2;
            inventory1 = createCommonerInventory();
        }
        else if(type == 3){
            classIndex = 3;
            inventory1 = createNobleInventory();
        }
    }

    private void chooseAttributes() {
        inventory = 4;
        inventory1 = createAttributesInventory();
    }

    private void confirmFinish() {
        inventory = 5;
        inventory1 = createConfirmFinishInventory();
    }

    /**
     * Opens the inventory the player is currently set to use.
     */
    public void openInventory(){
        open = false;
        player.openInventory(inventory1.getInventory());
    }

    private InventoryGui createConfirmFinishInventory() {
        final InventoryGui i = new InventoryGui(Bukkit.createInventory(player, 9, InventoryGui.lim(Feudal.getMessage("sel_gui.con"))));
        i.setItem("FINISH", 8, createItem(XMaterial.CRAFTING_TABLE.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.finish"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.finish1"), Feudal.getMessage("sel_gui.finish2"), Feudal.getMessage("sel_gui.finish3")}))));
        i.setItem("BACK", 0, createItem(XMaterial.TNT.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.back"), null));

        i.setItem("INFO", 4, createItem(XMaterial.BOOK.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.you"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.you2") + profession.getType().getNameLang(),
                Feudal.getMessage("sel_gui.strengthl") + strength + " / " + Feudal.getConfiguration().getConfig().getInt("Strength.MaxLevel"),
                Feudal.getMessage("sel_gui.toughnessl") + toughness + " / " + Feudal.getConfiguration().getConfig().getInt("Toughness.MaxLevel"),
                Feudal.getMessage("sel_gui.speedl") + speed + " / " + Feudal.getConfiguration().getConfig().getInt("Speed.MaxLevel"),
                Feudal.getMessage("sel_gui.staminal") + stamina + " / " + Feudal.getConfiguration().getConfig().getInt("Stamina.MaxLevel"),
                Feudal.getMessage("sel_gui.luckl") + luck + " / " + Feudal.getConfiguration().getConfig().getInt("Luck.MaxLevel"),
                Feudal.getMessage("sel_gui.unusedp") + points}))));

        return i;
    }

    private InventoryGui createAttributesInventory() {
        final InventoryGui i = new InventoryGui(Bukkit.createInventory(player, 27, InventoryGui.lim(Feudal.getMessage("sel_gui.setupAtt"))));

        i.setItem("POINTS", 18, createItem(XMaterial.EMERALD.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.points") + points, new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.pointsAtt")}))));


        i.setItem("ADD_STRENGTH", 2, createItem(XMaterial.BLUE_WOOL.parseMaterial(), 1, (short) 11, Feudal.getMessage("sel_gui.addStrength"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.addCost"), Feudal.getMessage("sel_gui.addLevel") + Feudal.getConfiguration().getConfig().getInt("Strength.LevelPerPoint")}))));
        i.setItem("ADD_TOUGHNESS", 3, createItem(XMaterial.BLUE_WOOL.parseMaterial(), 1, (short) 11, Feudal.getMessage("sel_gui.addToughness"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.addCost"), Feudal.getMessage("sel_gui.addLevel") + Feudal.getConfiguration().getConfig().getInt("Toughness.LevelPerPoint")}))));
        i.setItem("ADD_SPEED", 4, createItem(XMaterial.BLUE_WOOL.parseMaterial(), 1, (short) 11, Feudal.getMessage("sel_gui.addSpeed"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.addCost"), Feudal.getMessage("sel_gui.addLevel") + Feudal.getConfiguration().getConfig().getInt("Speed.LevelPerPoint")}))));
        i.setItem("ADD_STAMINA", 5, createItem(XMaterial.BLUE_WOOL.parseMaterial(), 1, (short) 11, Feudal.getMessage("sel_gui.addStamina"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.addCost"), Feudal.getMessage("sel_gui.addLevel") + Feudal.getConfiguration().getConfig().getInt("Stamina.LevelPerPoint")}))));
        i.setItem("ADD_LUCK", 6, createItem(XMaterial.BLUE_WOOL.parseMaterial(), 1, (short) 11, Feudal.getMessage("sel_gui.addLuck"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.addCost"), Feudal.getMessage("sel_gui.addLevel") + Feudal.getConfiguration().getConfig().getInt("Luck.LevelPerPoint")}))));

        i.setItem("SUBTRACT_STRENGTH", 20, createItem(XMaterial.YELLOW_WOOL.parseMaterial(), 1, (short) 4, Feudal.getMessage("sel_gui.remStrength"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.earn"), Feudal.getMessage("sel_gui.remLevel") + Feudal.getConfiguration().getConfig().getInt("Strength.LevelPerPoint")}))));
        i.setItem("SUBTRACT_TOUGHNESS", 21, createItem(XMaterial.YELLOW_WOOL.parseMaterial(), 1, (short) 4, Feudal.getMessage("sel_gui.remToughness"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.earn"), Feudal.getMessage("sel_gui.remLevel") + Feudal.getConfiguration().getConfig().getInt("Toughness.LevelPerPoint")}))));
        i.setItem("SUBTRACT_SPEED", 22, createItem(XMaterial.YELLOW_WOOL.parseMaterial(), 1, (short) 4, Feudal.getMessage("sel_gui.remSpeed"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.earn"), Feudal.getMessage("sel_gui.remLevel") + Feudal.getConfiguration().getConfig().getInt("Speed.LevelPerPoint")}))));
        i.setItem("SUBTRACT_STAMINA", 23, createItem(XMaterial.YELLOW_WOOL.parseMaterial(), 1, (short) 4, Feudal.getMessage("sel_gui.remStamina"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.earn"), Feudal.getMessage("sel_gui.remLevel") + Feudal.getConfiguration().getConfig().getInt("Stamina.LevelPerPoint")}))));
        i.setItem("SUBTRACT_LUCK", 24, createItem(XMaterial.YELLOW_WOOL.parseMaterial(), 1, (short) 4, Feudal.getMessage("sel_gui.remLuck"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.earn"), Feudal.getMessage("sel_gui.remLevel") + Feudal.getConfiguration().getConfig().getInt("Luck.LevelPerPoint")}))));

        i.setItem("STRENGTH", 11, createItem(XMaterial.ANVIL.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.strengthT"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.level")+strength+" / " + Feudal.getConfiguration().getConfig().getInt("Strength.MaxLevel"), Feudal.getMessage("sel_gui.strengthT1") + Attributes.getStrengthDamageEffect(strength), Feudal.getMessage("sel_gui.strengthT2") + Attributes.getStrengthHasteEffect(strength), Feudal.getMessage("sel_gui.strengthT3"), Feudal.getMessage("sel_gui.strengthT4")}))));
        i.setItem("TOUGHNESS", 12, createItem(XMaterial.BEDROCK.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.toughnessT"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.level")+toughness+" / " + Feudal.getConfiguration().getConfig().getInt("Toughness.MaxLevel"), Feudal.getMessage("sel_gui.toughnessT1") + Attributes.getToughnessHealthEffect(toughness), Feudal.getMessage("sel_gui.toughnessT2") + Attributes.getToughnessRegenRate(toughness), Feudal.getMessage("sel_gui.toughnessT3"), Feudal.getMessage("sel_gui.toughnessT4")}))));
        i.setItem("SPEED", 13, createItem(XMaterial.BLAZE_POWDER.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.speedT"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.level")+speed+" / " + Feudal.getConfiguration().getConfig().getInt("Speed.MaxLevel"), Feudal.getMessage("sel_gui.speedT1") + Attributes.getSpeedEffect(speed), Feudal.getMessage("sel_gui.speedT2")}))));
        i.setItem("STAMINA", 14, createItem(XMaterial.LEATHER_BOOTS.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.staminaT"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.level")+stamina+" / " + Feudal.getConfiguration().getConfig().getInt("Stamina.MaxLevel"), Feudal.getMessage("sel_gui.staminaT1") + Attributes.getStaminaEffect(stamina), Feudal.getMessage("sel_gui.staminaT2")}))));
        i.setItem("LUCK", 15, createItem(XMaterial.GOLD_INGOT.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.luckT"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.level")+luck+" / " + Feudal.getConfiguration().getConfig().getInt("Luck.MaxLevel"), Feudal.getMessage("sel_gui.luckT1") + Feudal.round((Attributes.getLuck(luck)*100)) + "%"}))));


        i.setItem("FINISH", 8, createItem(XMaterial.CRAFTING_TABLE.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.finish"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.saveAtt1"), Feudal.getMessage("sel_gui.saveAtt2"), Feudal.getMessage("sel_gui.saveAtt3"), Feudal.getMessage("sel_gui.saveAtt4"), Feudal.getMessage("sel_gui.saveAtt5"), Feudal.getMessage("sel_gui.saveAtt6")}))));
        i.setItem("BACK", 0, createItem(XMaterial.TNT.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.back"), null));

        i.setItem("INFO", 26, createItem(XMaterial.BOOK.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.helpAtt"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.helpAtt1"), Feudal.getMessage("sel_gui.helpAtt2"), Feudal.getMessage("sel_gui.helpAtt3"), Feudal.getMessage("sel_gui.helpAtt4"), Feudal.getMessage("sel_gui.helpAtt5"), Feudal.getMessage("sel_gui.helpAtt6")}))));

        return i;
    }

    private void updateAttributesInventory() {
        final InventoryGui i = inventory1;

        i.clear();

        i.setItem("POINTS", 18, createItem(XMaterial.EMERALD.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.points") + points, new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.pointsAtt")}))));


        i.setItem("ADD_STRENGTH", 2, createItem(XMaterial.BLUE_WOOL.parseMaterial(), 1, (short) 11, Feudal.getMessage("sel_gui.addStrength"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.addCost"), Feudal.getMessage("sel_gui.addLevel") + Feudal.getConfiguration().getConfig().getInt("Strength.LevelPerPoint")}))));
        i.setItem("ADD_TOUGHNESS", 3, createItem(XMaterial.BLUE_WOOL.parseMaterial(), 1, (short) 11, Feudal.getMessage("sel_gui.addToughness"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.addCost"), Feudal.getMessage("sel_gui.addLevel") + Feudal.getConfiguration().getConfig().getInt("Toughness.LevelPerPoint")}))));
        i.setItem("ADD_SPEED", 4, createItem(XMaterial.BLUE_WOOL.parseMaterial(), 1, (short) 11, Feudal.getMessage("sel_gui.addSpeed"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.addCost"), Feudal.getMessage("sel_gui.addLevel") + Feudal.getConfiguration().getConfig().getInt("Speed.LevelPerPoint")}))));
        i.setItem("ADD_STAMINA", 5, createItem(XMaterial.BLUE_WOOL.parseMaterial(), 1, (short) 11, Feudal.getMessage("sel_gui.addStamina"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.addCost"), Feudal.getMessage("sel_gui.addLevel") + Feudal.getConfiguration().getConfig().getInt("Stamina.LevelPerPoint")}))));
        i.setItem("ADD_LUCK", 6, createItem(XMaterial.BLUE_WOOL.parseMaterial(), 1, (short) 11, Feudal.getMessage("sel_gui.addLuck"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.addCost"), Feudal.getMessage("sel_gui.addLevel") + Feudal.getConfiguration().getConfig().getInt("Luck.LevelPerPoint")}))));

        i.setItem("SUBTRACT_STRENGTH", 20, createItem(XMaterial.YELLOW_WOOL.parseMaterial(), 1, (short) 4, Feudal.getMessage("sel_gui.remStrength"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.earn"), Feudal.getMessage("sel_gui.remLevel") + Feudal.getConfiguration().getConfig().getInt("Strength.LevelPerPoint")}))));
        i.setItem("SUBTRACT_TOUGHNESS", 21, createItem(XMaterial.YELLOW_WOOL.parseMaterial(), 1, (short) 4, Feudal.getMessage("sel_gui.remToughness"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.earn"), Feudal.getMessage("sel_gui.remLevel") + Feudal.getConfiguration().getConfig().getInt("Toughness.LevelPerPoint")}))));
        i.setItem("SUBTRACT_SPEED", 22, createItem(XMaterial.YELLOW_WOOL.parseMaterial(), 1, (short) 4, Feudal.getMessage("sel_gui.remSpeed"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.earn"), Feudal.getMessage("sel_gui.remLevel") + Feudal.getConfiguration().getConfig().getInt("Speed.LevelPerPoint")}))));
        i.setItem("SUBTRACT_STAMINA", 23, createItem(XMaterial.YELLOW_WOOL.parseMaterial(), 1, (short) 4, Feudal.getMessage("sel_gui.remStamina"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.earn"), Feudal.getMessage("sel_gui.remLevel") + Feudal.getConfiguration().getConfig().getInt("Stamina.LevelPerPoint")}))));
        i.setItem("SUBTRACT_LUCK", 24, createItem(XMaterial.YELLOW_WOOL.parseMaterial(), 1, (short) 4, Feudal.getMessage("sel_gui.remLuck"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.earn"), Feudal.getMessage("sel_gui.remLevel") + Feudal.getConfiguration().getConfig().getInt("Luck.LevelPerPoint")}))));

        i.setItem("STRENGTH", 11, createItem(XMaterial.ANVIL.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.strengthT"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.level")+strength+" / " + Feudal.getConfiguration().getConfig().getInt("Strength.MaxLevel"), Feudal.getMessage("sel_gui.strengthT1") + Attributes.getStrengthDamageEffect(strength), Feudal.getMessage("sel_gui.strengthT2") + Attributes.getStrengthHasteEffect(strength), Feudal.getMessage("sel_gui.strengthT3"), Feudal.getMessage("sel_gui.strengthT4")}))));
        i.setItem("TOUGHNESS", 12, createItem(XMaterial.BEDROCK.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.toughnessT"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.level")+toughness+" / " + Feudal.getConfiguration().getConfig().getInt("Toughness.MaxLevel"), Feudal.getMessage("sel_gui.toughnessT1") + Attributes.getToughnessHealthEffect(toughness), Feudal.getMessage("sel_gui.toughnessT2") + Attributes.getToughnessRegenRate(toughness), Feudal.getMessage("sel_gui.toughnessT3"), Feudal.getMessage("sel_gui.toughnessT4")}))));
        i.setItem("SPEED", 13, createItem(XMaterial.BLAZE_POWDER.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.speedT"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.level")+speed+" / " + Feudal.getConfiguration().getConfig().getInt("Speed.MaxLevel"), Feudal.getMessage("sel_gui.speedT1") + Attributes.getSpeedEffect(speed), Feudal.getMessage("sel_gui.speedT2")}))));
        i.setItem("STAMINA", 14, createItem(XMaterial.LEATHER_BOOTS.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.staminaT"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.level")+stamina+" / " + Feudal.getConfiguration().getConfig().getInt("Stamina.MaxLevel"), Feudal.getMessage("sel_gui.staminaT1") + Attributes.getStaminaEffect(stamina), Feudal.getMessage("sel_gui.staminaT2")}))));
        i.setItem("LUCK", 15, createItem(XMaterial.GOLD_INGOT.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.luckT"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.level")+luck+" / " + Feudal.getConfiguration().getConfig().getInt("Luck.MaxLevel"), Feudal.getMessage("sel_gui.luckT1") + Feudal.round((Attributes.getLuck(luck)*100)) + "%"}))));


        i.setItem("FINISH", 8, createItem(XMaterial.CRAFTING_TABLE.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.finish"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.saveAtt1"), Feudal.getMessage("sel_gui.saveAtt2"), Feudal.getMessage("sel_gui.saveAtt3"), Feudal.getMessage("sel_gui.saveAtt4"), Feudal.getMessage("sel_gui.saveAtt5"), Feudal.getMessage("sel_gui.saveAtt6")}))));
        i.setItem("BACK", 0, createItem(XMaterial.TNT.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.back"), null));

        i.setItem("INFO", 26, createItem(XMaterial.BOOK.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.helpAtt"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.helpAtt1"), Feudal.getMessage("sel_gui.helpAtt2"), Feudal.getMessage("sel_gui.helpAtt3"), Feudal.getMessage("sel_gui.helpAtt4"), Feudal.getMessage("sel_gui.helpAtt5"), Feudal.getMessage("sel_gui.helpAtt6")}))));

		/*i.setItem("POINTS", 18, createItem(Material.EMERALD, 1, (short) 0, "\u00a7e\u00a7lPoints: \u00a7c\u00a7l" + points, new ArrayList<String>(Arrays.asList(new String[]{"\u00a7aUse points to upgrade attributes."}))));


		i.setItem("ADD_STRENGTH", 2, createItem(Material.WOOL, 1, (short) 11, "\u00a79\u00a7lAdd Strength", new ArrayList<String>(Arrays.asList(new String[]{"\u00a76Cost: \u00a771", "\u00a76Added Level: \u00a77" + Main.getConfiguration().getConfig().getInt("Strength.LevelPerPoint")}))));
		i.setItem("ADD_TOUGHNESS", 3, createItem(Material.WOOL, 1, (short) 11, "\u00a79\u00a7lAdd Toughness", new ArrayList<String>(Arrays.asList(new String[]{"\u00a76Cost: \u00a771", "\u00a76Added Level: \u00a77" + Main.getConfiguration().getConfig().getInt("Toughness.LevelPerPoint")}))));
		i.setItem("ADD_SPEED", 4, createItem(Material.WOOL, 1, (short) 11, "\u00a79\u00a7lAdd Speed", new ArrayList<String>(Arrays.asList(new String[]{"\u00a76Cost: \u00a771", "\u00a76Added Level: \u00a77" + Main.getConfiguration().getConfig().getInt("Speed.LevelPerPoint")}))));
		i.setItem("ADD_STAMINA", 5, createItem(Material.WOOL, 1, (short) 11, "\u00a79\u00a7lAdd Stamina", new ArrayList<String>(Arrays.asList(new String[]{"\u00a76Cost: \u00a771", "\u00a76Added Level: \u00a77" + Main.getConfiguration().getConfig().getInt("Stamina.LevelPerPoint")}))));
		i.setItem("ADD_LUCK", 6, createItem(Material.WOOL, 1, (short) 11, "\u00a79\u00a7lAdd Luck", new ArrayList<String>(Arrays.asList(new String[]{"\u00a76Cost: \u00a771", "\u00a76Added Level: \u00a77" + Main.getConfiguration().getConfig().getInt("Luck.LevelPerPoint")}))));

		i.setItem("SUBTRACT_STRENGTH", 20, createItem(Material.WOOL, 1, (short) 4, "\u00a7e\u00a7lRemove Strength", new ArrayList<String>(Arrays.asList(new String[]{"\u00a76Earn: \u00a771 point", "\u00a76Removed Level:\u00a77 " + Main.getConfiguration().getConfig().getInt("Strength.LevelPerPoint")}))));
		i.setItem("SUBTRACT_TOUGHNESS", 21, createItem(Material.WOOL, 1, (short) 4, "\u00a7e\u00a7lRemove Toughness", new ArrayList<String>(Arrays.asList(new String[]{"\u00a76Earn: \u00a771 point", "\u00a76Removed Level: \u00a77" + Main.getConfiguration().getConfig().getInt("Toughness.LevelPerPoint")}))));
		i.setItem("SUBTRACT_SPEED", 22, createItem(Material.WOOL, 1, (short) 4, "\u00a7e\u00a7lRemove Speed", new ArrayList<String>(Arrays.asList(new String[]{"\u00a76Earn: \u00a771 point", "\u00a76Removed Level: \u00a77" + Main.getConfiguration().getConfig().getInt("Speed.LevelPerPoint")}))));
		i.setItem("SUBTRACT_STAMINA", 23, createItem(Material.WOOL, 1, (short) 4, "\u00a7e\u00a7lRemove Stamina", new ArrayList<String>(Arrays.asList(new String[]{"\u00a76Earn: \u00a771 point", "\u00a76Removed Level: \u00a77" + Main.getConfiguration().getConfig().getInt("Stamina.LevelPerPoint")}))));
		i.setItem("SUBTRACT_LUCK", 24, createItem(Material.WOOL, 1, (short) 4, "\u00a7e\u00a7lRemove Luck", new ArrayList<String>(Arrays.asList(new String[]{"\u00a76Earn: \u00a771 point", "\u00a76Removed Level: \u00a77" + Main.getConfiguration().getConfig().getInt("Luck.LevelPerPoint")}))));

		i.setItem("STRENGTH", 11, createItem(Material.ANVIL, 1, (short) 0, "\u00a7c\u00a7lStrength", new ArrayList<String>(Arrays.asList(new String[]{"\u00a76Level: \u00a77"+strength+" / " + Main.getConfiguration().getConfig().getInt("Strength.MaxLevel"), "\u00a76Damage Effect: \u00a77" + Attributes.getStrengthDamageEffect(strength), "\u00a76Haste Effect: \u00a77" + Attributes.getStrengthHasteEffect(strength), "\u00a77(Default damage: 1.0)", "\u00a77(Default haste: 0)"}))));
		i.setItem("TOUGHNESS", 12, createItem(Material.BEDROCK, 1, (short) 0, "\u00a7c\u00a7lToughness", new ArrayList<String>(Arrays.asList(new String[]{"\u00a76Level: \u00a77"+toughness+" / " + Main.getConfiguration().getConfig().getInt("Toughness.MaxLevel"), "\u00a76Health: \u00a77" + Attributes.getToughnessHealthEffect(toughness), "\u00a76Regeneration Rate: \u00a77" + Attributes.getToughnessRegenRate(toughness), "\u00a77(Default health: 20)", "\u00a77(Default regeneration rate: 1.0)"}))));
		i.setItem("SPEED", 13, createItem(Material.BLAZE_POWDER, 1, (short) 0, "\u00a7c\u00a7lSpeed", new ArrayList<String>(Arrays.asList(new String[]{"\u00a76Level: \u00a77"+speed+" / " + Main.getConfiguration().getConfig().getInt("Speed.MaxLevel"), "\u00a76Speed: \u00a77" + Attributes.getSpeedEffect(speed), "\u00a77(Default speed: 1.0)"}))));
		i.setItem("STAMINA", 14, createItem(Material.LEATHER_BOOTS, 1, (short) 0, "\u00a7c\u00a7lStamina", new ArrayList<String>(Arrays.asList(new String[]{"\u00a76Level: \u00a77"+stamina+" / " + Main.getConfiguration().getConfig().getInt("Stamina.MaxLevel"), "\u00a76Saturation regain rate: \u00a77" + Attributes.getStaminaEffect(stamina), "\u00a77(Default rate: 0.0)"}))));
		i.setItem("LUCK", 15, createItem(Material.GOLD_INGOT, 1, (short) 0, "\u00a7c\u00a7lLuck", new ArrayList<String>(Arrays.asList(new String[]{"\u00a76Level: \u00a77"+luck+" / " + Main.getConfiguration().getConfig().getInt("Luck.MaxLevel"), "\u00a76Luck: \u00a77" + Main.round((Attributes.getLuck(luck)*100)) + "%"}))));


		i.setItem("FINISH", 8, createItem(Material.WORKBENCH, 1, (short) 0, "\u00a7e\u00a7lFINISH", new ArrayList<String>(Arrays.asList(new String[]{"\u00a7aThis will save all your settings.", "\u00a7aYou can finish with leftover points,", "\u00a7abut your points will not save.", "\u00a7cOnce you finish you can never", "\u00a7cchange these settings again.", "\u00a7cYou can only level up."}))));
		i.setItem("BACK", 0, createItem(Material.TNT, 1, (short) 0, "\u00a7c\u00a7lBACK", null));

		i.setItem("INFO", 26, createItem(Material.BOOK, 1, (short) 0, "\u00a7d\u00a7lHELP", new ArrayList<String>(Arrays.asList(new String[]{"\u00a77Use points to setup your attributes.", "\u00a77This is a one time setup.", "\u00a77Click the blue wool to add to an attribute.", "\u00a77Click the yellow wool to remove from an attribute.", "\u00a77Click the crafting bench when you are finished.", "\u00a77Click the tnt to go back."}))));
		 */
    }


    private InventoryGui createSocialClassInventory(){
        final InventoryGui i = new InventoryGui(Bukkit.createInventory(player, 9, InventoryGui.lim(Feudal.getMessage("sel_gui.socTitle"))));

        i.setItem("INFO", 0, createItem(XMaterial.BOOK.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.socPoints") + points, new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.socPoints1"), Feudal.getMessage("sel_gui.socPoints2"), Feudal.getMessage("sel_gui.socPoints3"), Feudal.getMessage("sel_gui.socPoints4")}))));

        i.setItem("PEASANTRY", 2, createItem(XMaterial.LEATHER_CHESTPLATE.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.socPeasant"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.socCost") + getCost(Feudal.getConfiguration().getConfig().getInt("setup.peasantry.cost"))}))));
        i.setItem("COMMONER", 4, createItem(XMaterial.CHAINMAIL_CHESTPLATE.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.socCommoner"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.socCost") + getCost(Feudal.getConfiguration().getConfig().getInt("setup.commoner.cost"))}))));
        i.setItem("NOBLE", 6, createItem(XMaterial.DIAMOND_CHESTPLATE.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.socNoble"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.socCost") + getCost(Feudal.getConfiguration().getConfig().getInt("setup.noble.cost"))}))));

        i.setItem("INFO", 8, createItem(XMaterial.BOOK.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.socPoints") + points, new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.socPoints1"), Feudal.getMessage("sel_gui.socPoints2"), Feudal.getMessage("sel_gui.socPoints3"), Feudal.getMessage("sel_gui.socPoints4")}))));

        return i;
    }

    private String getCost(int cost) {
        if(cost <= points) {
            return cost + "";
        }else {
            return Feudal.getMessage("changeProfession.lock");
        }
    }

    private InventoryGui createPeasantInventory() {
        final InventoryGui i = new InventoryGui(Bukkit.createInventory(player, 27, InventoryGui.lim(Feudal.getMessage("sel_gui.pesTitle"))));

        i.setItem("FARMER", 0, createItem(XMaterial.STONE_HOE.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.Farmer"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.proCost") + getCost(Feudal.getConfiguration().getConfig().getInt("Farmer.pointCost")),
                Feudal.getMessage("sel_gui.land") + Feudal.getConfiguration().getConfig().getInt("Farmer.land"), Feudal.getMessage("sel_gui.Farmer1"), Feudal.getMessage("sel_gui.Farmer2")}))));

        i.setItem("LOGGER", 1, createItem(XMaterial.OAK_LOG.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.Logger"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.proCost") + getCost(Feudal.getConfiguration().getConfig().getInt("Logger.pointCost")),
                Feudal.getMessage("sel_gui.land") + Feudal.getConfiguration().getConfig().getInt("Logger.land"), Feudal.getMessage("sel_gui.Logger1"), Feudal.getMessage("sel_gui.Logger2")}))));

        i.setItem("HUNTER", 2, createItem(XMaterial.BOW.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.Hunter"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.proCost") + getCost(Feudal.getConfiguration().getConfig().getInt("Hunter.pointCost")),
                Feudal.getMessage("sel_gui.land") + Feudal.getConfiguration().getConfig().getInt("Hunter.land"), Feudal.getMessage("sel_gui.Hunter1"), Feudal.getMessage("sel_gui.Hunter2")}))));

        i.setItem("MINER", 3, createItem(XMaterial.STONE_PICKAXE.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.Miner"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.proCost") + getCost(Feudal.getConfiguration().getConfig().getInt("Miner.pointCost")),
                Feudal.getMessage("sel_gui.land") + Feudal.getConfiguration().getConfig().getInt("Miner.land"), Feudal.getMessage("sel_gui.Miner1"), Feudal.getMessage("sel_gui.Miner2")}))));

        i.setItem("COOK", 4, createItem(XMaterial.CAKE.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.Cook"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.proCost") + getCost(Feudal.getConfiguration().getConfig().getInt("Cook.pointCost")),
                Feudal.getMessage("sel_gui.land") + Feudal.getConfiguration().getConfig().getInt("Cook.land"), Feudal.getMessage("sel_gui.Cook1"), Feudal.getMessage("sel_gui.Cook2")}))));

        i.setItem("FISHER", 5, createItem(XMaterial.FISHING_ROD.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.Fisher"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.proCost") + getCost(Feudal.getConfiguration().getConfig().getInt("Fisher.pointCost")),
                Feudal.getMessage("sel_gui.land") + Feudal.getConfiguration().getConfig().getInt("Fisher.land"),  Feudal.getMessage("sel_gui.Fisher1"), Feudal.getMessage("sel_gui.Fisher2")}))));

        i.setItem("BUILDER", 6, createItem(XMaterial.STONE_BRICKS.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.Builder"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.proCost") + getCost(Feudal.getConfiguration().getConfig().getInt("Builder.pointCost")),
                Feudal.getMessage("sel_gui.land") + Feudal.getConfiguration().getConfig().getInt("Builder.land"),  Feudal.getMessage("sel_gui.Builder1"), Feudal.getMessage("sel_gui.Builder2")}))));

        i.setItem("SHEPHERD", 7, createItem(XMaterial.SHEARS.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.Shepherd"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.proCost") + getCost(Feudal.getConfiguration().getConfig().getInt("Shepherd.pointCost")),
                Feudal.getMessage("sel_gui.land") + Feudal.getConfiguration().getConfig().getInt("Shepherd.land"),  Feudal.getMessage("sel_gui.Shepherd1"), Feudal.getMessage("sel_gui.Shepherd2")}))));

        i.setItem("SCRIBE", 8, createItem(XMaterial.WRITABLE_BOOK.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.Scribe"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.proCost") + getCost(Feudal.getConfiguration().getConfig().getInt("Scribe.pointCost")),
                Feudal.getMessage("sel_gui.land") + Feudal.getConfiguration().getConfig().getInt("Scribe.land"),  Feudal.getMessage("sel_gui.Scribe1"), Feudal.getMessage("sel_gui.Scribe2")}))));


        i.setItem("BACK", 18, createItem(XMaterial.TNT.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.back"), null));

        i.setItem("INFO", 26, createItem(XMaterial.BOOK.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.socPoints") + points, new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.socPoints1"), Feudal.getMessage("sel_gui.socPoints2"), Feudal.getMessage("sel_gui.socPoints3"), Feudal.getMessage("sel_gui.socPoints4")}))));
        return i;
    }

    private InventoryGui createCommonerInventory() {
        final InventoryGui i = new InventoryGui(Bukkit.createInventory(player, 27, InventoryGui.lim(Feudal.getMessage("sel_gui.comTitle"))));

        i.setItem("GUARD", 1, createItem(XMaterial.STONE_SWORD.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.Guard"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.proCost") + getCost(Feudal.getConfiguration().getConfig().getInt("Guard.pointCost")),
                Feudal.getMessage("sel_gui.land") + Feudal.getConfiguration().getConfig().getInt("Guard.land"), Feudal.getMessage("sel_gui.Guard1"), Feudal.getMessage("sel_gui.Guard2")}))));

        i.setItem("ASSASSIN", 2, createItem(XMaterial.COMPASS.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.Assassin"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.proCost") + getCost(Feudal.getConfiguration().getConfig().getInt("Assassin.pointCost")),
                Feudal.getMessage("sel_gui.land") + Feudal.getConfiguration().getConfig().getInt("Assassin.land"), Feudal.getMessage("sel_gui.Assassin1"), Feudal.getMessage("sel_gui.Assassin2"), Feudal.getMessage("sel_gui.Assassin3"), Feudal.getMessage("sel_gui.Assassin4")}))));

        i.setItem("ALCHEMIST", 3, createItem(XMaterial.BREWING_STAND.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.Alchemist"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.proCost") + getCost(Feudal.getConfiguration().getConfig().getInt("Alchemist.pointCost")),
                Feudal.getMessage("sel_gui.land") + Feudal.getConfiguration().getConfig().getInt("Alchemist.land"), Feudal.getMessage("sel_gui.Alchemist1"), Feudal.getMessage("sel_gui.Alchemist2")}))));

        i.setItem("BLACKSMITH", 5, createItem(XMaterial.ANVIL.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.Blacksmith"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.proCost") + getCost(Feudal.getConfiguration().getConfig().getInt("Blacksmith.pointCost")),
                Feudal.getMessage("sel_gui.land") + Feudal.getConfiguration().getConfig().getInt("Blacksmith.land"), Feudal.getMessage("sel_gui.Blacksmith1"), Feudal.getMessage("sel_gui.Blacksmith2")}))));

        i.setItem("HEALER", 6, createItem(XMaterial.GOLDEN_APPLE.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.Healer"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.proCost") + getCost(Feudal.getConfiguration().getConfig().getInt("Healer.pointCost")),
                Feudal.getMessage("sel_gui.land") + Feudal.getConfiguration().getConfig().getInt("Healer.land"), Feudal.getMessage("sel_gui.Healer1"), Feudal.getMessage("sel_gui.Healer2")}))));

        i.setItem("MERCHANT", 7, createItem(XMaterial.EMERALD.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.Merchant"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.proCost") + getCost(Feudal.getConfiguration().getConfig().getInt("Merchant.pointCost")),
                Feudal.getMessage("sel_gui.land") + Feudal.getConfiguration().getConfig().getInt("Merchant.land"), Feudal.getMessage("sel_gui.Merchant1"), Feudal.getMessage("sel_gui.Merchant2")}))));

        i.setItem("BACK", 18, createItem(XMaterial.TNT.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.back"), null));
        i.setItem("INFO", 26, createItem(XMaterial.BOOK.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.socPoints") + points, new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.socPoints1"), Feudal.getMessage("sel_gui.socPoints2"), Feudal.getMessage("sel_gui.socPoints3"), Feudal.getMessage("sel_gui.socPoints4")}))));

        return i;
    }

    private InventoryGui createNobleInventory() {
        final InventoryGui i = new InventoryGui(Bukkit.createInventory(player, 27, InventoryGui.lim(Feudal.getMessage("sel_gui.nobTitle"))));

        i.setItem("SQUIRE", 1, createItem(XMaterial.IRON_SWORD.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.Squire"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.proCost") + getCost(Feudal.getConfiguration().getConfig().getInt("Squire.pointCost")),
                Feudal.getMessage("sel_gui.land") + Feudal.getConfiguration().getConfig().getInt("Squire.land"), Feudal.getMessage("sel_gui.Squire1"), Feudal.getMessage("sel_gui.Squire2")}))));

        i.setItem("KNIGHT", 3, createItem(XMaterial.DIAMOND_SWORD.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.Knight"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.proCost") + getCost(Feudal.getConfiguration().getConfig().getInt("Knight.pointCost")),
                Feudal.getMessage("sel_gui.land") + Feudal.getConfiguration().getConfig().getInt("Knight.land"), Feudal.getMessage("sel_gui.Knight1"), Feudal.getMessage("sel_gui.Knight2")}))));

        i.setItem("BARON", 5, createItem(XMaterial.DIAMOND_HELMET.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.Baron"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.proCost") + getCost(Feudal.getConfiguration().getConfig().getInt("Baron.pointCost")),
                Feudal.getMessage("sel_gui.land") + Feudal.getConfiguration().getConfig().getInt("Baron.land"), Feudal.getMessage("sel_gui.Baron1"), Feudal.getMessage("sel_gui.Baron2")}))));

        i.setItem("KING", 7, createItem(XMaterial.GOLDEN_HELMET.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.King"), new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.proCost") + getCost(Feudal.getConfiguration().getConfig().getInt("King.pointCost")),
                Feudal.getMessage("sel_gui.land") + Feudal.getConfiguration().getConfig().getInt("King.land"), Feudal.getMessage("sel_gui.King1"), Feudal.getMessage("sel_gui.King2")}))));


        i.setItem("BACK", 18, createItem(XMaterial.TNT.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.back"), null));

        i.setItem("INFO", 26, createItem(XMaterial.BOOK.parseMaterial(), 1, (short) 0, Feudal.getMessage("sel_gui.socPoints") + points, new ArrayList<>(Arrays.asList(new String[]{Feudal.getMessage("sel_gui.socPoints1"), Feudal.getMessage("sel_gui.socPoints2"), Feudal.getMessage("sel_gui.socPoints3"), Feudal.getMessage("sel_gui.socPoints4")}))));

        return i;
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
        if(!Feudal.getVersion().contains("1.8") && !Feudal.getVersion().equals("1.7")){
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
        try{
            if(!Feudal.getVersion().contains("1.8") && !Feudal.getVersion().equals("1.7")){
                player.playSound(player.getLocation(), Sound.valueOf("ENTITY_ARROW_HIT_PLAYER"), 1, (float) (((value * (1.7)) / max) + 0.3));
            }else{
                player.playSound(player.getLocation(), Sound.valueOf("SUCCESSFUL_HIT"), 1, (float) (((value * (1.7)) / max) + 0.3));
            }
        }catch(final Exception e){
            //
        }
    }

    private void delayOpen(){
        open = true;
        if(running){
            Bukkit.getScheduler().cancelTask(schedule);
        }
        running = true;
        schedule = Bukkit.getScheduler().scheduleSyncDelayedTask(Feudal.getPlugin(), () -> {
            if(open){
                running = false;
                player.closeInventory();
                Selection.this.openInventory();
            }
        }, 20);
    }

    private boolean click(String itemString) {
        if(itemString != null){
            if(inventory == 0){
                if(itemString.equals("PEASANTRY")){
                    final int p = Feudal.getConfiguration().getConfig().getInt("setup.peasantry.cost");
                    if(points >= p){
                        soundClick();
                        points -= p;
                        pointsUsedClass = p;
                        chooseJob(1);
                        player.closeInventory();
                        this.openInventory();
                    }else{
                        soundDeny();
                        player.closeInventory();
                        player.sendMessage(Feudal.getMessage("selection.np"));
                        delayOpen();
                    }
                }else if(itemString.equals("COMMONER")){
                    final int p = Feudal.getConfiguration().getConfig().getInt("setup.commoner.cost");
                    if(points >= p){
                        soundClick();
                        points -= p;
                        pointsUsedClass = p;
                        chooseJob(2);
                        player.closeInventory();
                        this.openInventory();
                    }else{
                        soundDeny();
                        player.closeInventory();
                        player.sendMessage(Feudal.getMessage("selection.np"));
                        delayOpen();
                    }
                }else if(itemString.equals("NOBLE")){
                    final int p = Feudal.getConfiguration().getConfig().getInt("setup.noble.cost");
                    if(points >= p){
                        soundClick();
                        points -= p;
                        pointsUsedClass = p;
                        chooseJob(3);
                        player.closeInventory();
                        this.openInventory();
                    }else{
                        soundDeny();
                        player.closeInventory();
                        player.sendMessage(Feudal.getMessage("selection.np"));
                        delayOpen();
                    }
                }
            }else if(inventory == 1){//peasant
                if(itemString.equals("FARMER")){
                    final int p = Feudal.getConfiguration().getConfig().getInt("Farmer.pointCost");
                    if(points >= p){
                        if(Profession.checkProfession(Profession.Type.FARMER)){
                            soundClick();
                            profession = new Profession(user, Profession.Type.FARMER, 0, 0);
                            points -= p;
                            pointsUsedJob = p;
                            chooseAttributes();
                            player.closeInventory();
                            this.openInventory();
                        }else{
                            soundDeny();
                            player.closeInventory();
                            player.sendMessage(Feudal.getMessage("selection.deny"));
                            delayOpen();
                        }
                    }else{
                        soundDeny();
                        player.closeInventory();
                        player.sendMessage(Feudal.getMessage("selection.np"));
                        delayOpen();
                    }
                }else if(itemString.equals("LOGGER")){
                    final int p = Feudal.getConfiguration().getConfig().getInt("Logger.pointCost");
                    if(points >= p){
                        if(Profession.checkProfession(Profession.Type.LOGGER)){
                            soundClick();
                            profession = new Profession(user, Profession.Type.LOGGER, 0, 0);
                            points -= p;
                            pointsUsedJob = p;
                            chooseAttributes();
                            player.closeInventory();
                            this.openInventory();
                        }else{
                            soundDeny();
                            player.closeInventory();
                            player.sendMessage(Feudal.getMessage("selection.deny"));
                            delayOpen();
                        }
                    }else{
                        soundDeny();
                        player.closeInventory();
                        player.sendMessage(Feudal.getMessage("selection.np"));
                        delayOpen();
                    }
                }else if(itemString.equals("HUNTER")){
                    final int p = Feudal.getConfiguration().getConfig().getInt("Hunter.pointCost");
                    if(points >= p){
                        if(Profession.checkProfession(Profession.Type.HUNTER)){
                            soundClick();
                            profession = new Profession(user, Profession.Type.HUNTER, 0, 0);
                            points -= p;
                            pointsUsedJob = p;
                            chooseAttributes();
                            player.closeInventory();
                            this.openInventory();
                        }else{
                            soundDeny();
                            player.closeInventory();
                            player.sendMessage(Feudal.getMessage("selection.deny"));
                            delayOpen();
                        }
                    }else{
                        soundDeny();
                        player.closeInventory();
                        player.sendMessage(Feudal.getMessage("selection.np"));
                        delayOpen();
                    }
                }else if(itemString.equals("MINER")){
                    final int p = Feudal.getConfiguration().getConfig().getInt("Miner.pointCost");
                    if(points >= p){
                        if(Profession.checkProfession(Profession.Type.MINER)){
                            soundClick();
                            profession = new Profession(user, Profession.Type.MINER, 0, 0);
                            points -= p;
                            pointsUsedJob = p;
                            chooseAttributes();
                            player.closeInventory();
                            this.openInventory();
                        }else{
                            soundDeny();
                            player.closeInventory();
                            player.sendMessage(Feudal.getMessage("selection.deny"));
                            delayOpen();
                        }
                    }else{
                        soundDeny();
                        player.closeInventory();
                        player.sendMessage(Feudal.getMessage("selection.np"));
                        delayOpen();
                    }
                }else if(itemString.equals("COOK")){
                    final int p = Feudal.getConfiguration().getConfig().getInt("Cook.pointCost");
                    if(points >= p){
                        if(Profession.checkProfession(Profession.Type.COOK)){
                            soundClick();
                            profession = new Profession(user, Profession.Type.COOK, 0, 0);
                            points -= p;
                            pointsUsedJob = p;
                            chooseAttributes();
                            player.closeInventory();
                            this.openInventory();
                        }else{
                            soundDeny();
                            player.closeInventory();
                            player.sendMessage(Feudal.getMessage("selection.deny"));
                            delayOpen();
                        }
                    }else{
                        soundDeny();
                        player.closeInventory();
                        player.sendMessage(Feudal.getMessage("selection.np"));
                        delayOpen();
                    }
                }else if(itemString.equals("FISHER")){
                    final int p = Feudal.getConfiguration().getConfig().getInt("Fisher.pointCost");
                    if(points >= p){
                        if(Profession.checkProfession(Profession.Type.FISHER)){
                            soundClick();
                            profession = new Profession(user, Profession.Type.FISHER, 0, 0);
                            points -= p;
                            pointsUsedJob = p;
                            chooseAttributes();
                            player.closeInventory();
                            this.openInventory();
                        }else{
                            soundDeny();
                            player.closeInventory();
                            player.sendMessage(Feudal.getMessage("selection.deny"));
                            delayOpen();
                        }
                    }else{
                        soundDeny();
                        player.closeInventory();
                        player.sendMessage(Feudal.getMessage("selection.np"));
                        delayOpen();
                    }
                }else if(itemString.equals("BUILDER")){
                    final int p = Feudal.getConfiguration().getConfig().getInt("Builder.pointCost");
                    if(points >= p){
                        if(Profession.checkProfession(Profession.Type.BUILDER)){
                            soundClick();
                            profession = new Profession(user, Profession.Type.BUILDER, 0, 0);
                            points -= p;
                            pointsUsedJob = p;
                            chooseAttributes();
                            player.closeInventory();
                            this.openInventory();
                        }else{
                            soundDeny();
                            player.closeInventory();
                            player.sendMessage(Feudal.getMessage("selection.deny"));
                            delayOpen();
                        }
                    }else{
                        soundDeny();
                        player.closeInventory();
                        player.sendMessage(Feudal.getMessage("selection.np"));
                        delayOpen();
                    }
                }else if(itemString.equals("SHEPHERD")){
                    final int p = Feudal.getConfiguration().getConfig().getInt("Shepherd.pointCost");
                    if(points >= p){
                        if(Profession.checkProfession(Profession.Type.SHEPHERD)){
                            soundClick();
                            profession = new Profession(user, Profession.Type.SHEPHERD, 0, 0);
                            points -= p;
                            pointsUsedJob = p;
                            chooseAttributes();
                            player.closeInventory();
                            this.openInventory();
                        }else{
                            soundDeny();
                            player.closeInventory();
                            player.sendMessage(Feudal.getMessage("selection.deny"));
                            delayOpen();
                        }
                    }else{
                        soundDeny();
                        player.closeInventory();
                        player.sendMessage(Feudal.getMessage("selection.np"));
                        delayOpen();
                    }
                }else if(itemString.equals("SCRIBE")){
                    final int p = Feudal.getConfiguration().getConfig().getInt("Scribe.pointCost");
                    if(points >= p){
                        if(Profession.checkProfession(Profession.Type.SCRIBE)){
                            soundClick();
                            profession = new Profession(user, Profession.Type.SCRIBE, 0, 0);
                            points -= p;
                            pointsUsedJob = p;
                            chooseAttributes();
                            player.closeInventory();
                            this.openInventory();
                        }else{
                            soundDeny();
                            player.closeInventory();
                            player.sendMessage(Feudal.getMessage("selection.deny"));
                            delayOpen();
                        }
                    }else{
                        soundDeny();
                        player.closeInventory();
                        player.sendMessage(Feudal.getMessage("selection.np"));
                        delayOpen();
                    }
                }else if(itemString.equals("BACK")){
                    soundClick();
                    points += pointsUsedClass;
                    pointsUsedClass = 0;
                    chooseSocialClass();
                    player.closeInventory();
                    this.openInventory();
                }
            }else if(inventory == 2){//commoner
                if(itemString.equals("GUARD")){
                    final int p = Feudal.getConfiguration().getConfig().getInt("Guard.pointCost");
                    if(points >= p){
                        if(Profession.checkProfession(Profession.Type.GUARD)){
                            soundClick();
                            profession = new Profession(user, Profession.Type.GUARD, 0, 0);
                            points -= p;
                            pointsUsedJob = p;
                            chooseAttributes();
                            player.closeInventory();
                            this.openInventory();
                        }else{
                            soundDeny();
                            player.closeInventory();
                            player.sendMessage(Feudal.getMessage("selection.deny"));
                            delayOpen();
                        }
                    }else{
                        soundDeny();
                        player.closeInventory();
                        player.sendMessage(Feudal.getMessage("selection.np"));
                        delayOpen();
                    }
                }else if(itemString.equals("ASSASSIN")){
                    final int p = Feudal.getConfiguration().getConfig().getInt("Assassin.pointCost");
                    if(points >= p){
                        if(Profession.checkProfession(Profession.Type.ASSASSIN)){
                            soundClick();
                            profession = new Profession(user, Profession.Type.ASSASSIN, 0, 0);
                            points -= p;
                            pointsUsedJob = p;
                            chooseAttributes();
                            player.closeInventory();
                            this.openInventory();
                        }else{
                            soundDeny();
                            player.closeInventory();
                            player.sendMessage(Feudal.getMessage("selection.deny"));
                            delayOpen();
                        }
                    }else{
                        soundDeny();
                        player.closeInventory();
                        player.sendMessage(Feudal.getMessage("selection.np"));
                        delayOpen();
                    }
                }else if(itemString.equals("ALCHEMIST")){
                    final int p = Feudal.getConfiguration().getConfig().getInt("Alchemist.pointCost");
                    if(points >= p){
                        if(Profession.checkProfession(Profession.Type.ALCHEMIST)){
                            soundClick();
                            profession = new Profession(user, Profession.Type.ALCHEMIST, 0, 0);
                            points -= p;
                            pointsUsedJob = p;
                            chooseAttributes();
                            player.closeInventory();
                            this.openInventory();
                        }else{
                            soundDeny();
                            player.closeInventory();
                            player.sendMessage(Feudal.getMessage("selection.deny"));
                            delayOpen();
                        }
                    }else{
                        soundDeny();
                        player.closeInventory();
                        player.sendMessage(Feudal.getMessage("selection.np"));
                        delayOpen();
                    }
                }else if(itemString.equals("BLACKSMITH")){
                    final int p = Feudal.getConfiguration().getConfig().getInt("Blacksmith.pointCost");
                    if(points >= p){
                        if(Profession.checkProfession(Profession.Type.BLACKSMITH)){
                            soundClick();
                            profession = new Profession(user, Profession.Type.BLACKSMITH, 0, 0);
                            points -= p;
                            pointsUsedJob = p;
                            chooseAttributes();
                            player.closeInventory();
                            this.openInventory();
                        }else{
                            soundDeny();
                            player.closeInventory();
                            player.sendMessage(Feudal.getMessage("selection.deny"));
                            delayOpen();
                        }
                    }else{
                        soundDeny();
                        player.closeInventory();
                        player.sendMessage(Feudal.getMessage("selection.np"));
                        delayOpen();
                    }
                }else if(itemString.equals("HEALER")){
                    final int p = Feudal.getConfiguration().getConfig().getInt("Healer.pointCost");
                    if(points >= p){
                        if(Profession.checkProfession(Profession.Type.HEALER)){
                            soundClick();
                            profession = new Profession(user, Profession.Type.HEALER, 0, 0);
                            points -= p;
                            pointsUsedJob = p;
                            chooseAttributes();
                            player.closeInventory();
                            this.openInventory();
                        }else{
                            soundDeny();
                            player.closeInventory();
                            player.sendMessage(Feudal.getMessage("selection.deny"));
                            delayOpen();
                        }
                    }else{
                        soundDeny();
                        player.closeInventory();
                        player.sendMessage(Feudal.getMessage("selection.np"));
                        delayOpen();
                    }
                }else if(itemString.equals("MERCHANT")){
                    final int p = Feudal.getConfiguration().getConfig().getInt("Merchant.pointCost");
                    if(points >= p){
                        if(Profession.checkProfession(Profession.Type.MERCHANT)){
                            soundClick();
                            profession = new Profession(user, Profession.Type.MERCHANT, 0, 0);
                            points -= p;
                            pointsUsedJob = p;
                            chooseAttributes();
                            player.closeInventory();
                            this.openInventory();
                        }else{
                            soundDeny();
                            player.closeInventory();
                            player.sendMessage(Feudal.getMessage("selection.deny"));
                            delayOpen();
                        }
                    }else{
                        soundDeny();
                        player.closeInventory();
                        player.sendMessage(Feudal.getMessage("selection.np"));
                        delayOpen();
                    }
                }else if(itemString.equals("BACK")){
                    soundClick();
                    points += pointsUsedClass;
                    pointsUsedClass = 0;
                    chooseSocialClass();
                    player.closeInventory();
                    this.openInventory();
                }
            }else if(inventory == 3){//noble
                if(itemString.equals("SQUIRE")){
                    final int p = Feudal.getConfiguration().getConfig().getInt("Squire.pointCost");
                    if(points >= p){
                        if(Profession.checkProfession(Profession.Type.SQUIRE)){
                            soundClick();
                            profession = new Profession(user, Profession.Type.SQUIRE, 0, 0);
                            points -= p;
                            pointsUsedJob = p;
                            chooseAttributes();
                            player.closeInventory();
                            this.openInventory();
                        }else{
                            soundDeny();
                            player.closeInventory();
                            player.sendMessage(Feudal.getMessage("selection.deny"));
                            delayOpen();
                        }
                    }else{
                        soundDeny();
                        player.closeInventory();
                        player.sendMessage(Feudal.getMessage("selection.np"));
                        delayOpen();
                    }
                }else if(itemString.equals("KNIGHT")){
                    final int p = Feudal.getConfiguration().getConfig().getInt("Knight.pointCost");
                    if(points >= p){
                        if(Profession.checkProfession(Profession.Type.KNIGHT)){
                            soundClick();
                            profession = new Profession(user, Profession.Type.KNIGHT, 0, 0);
                            points -= p;
                            pointsUsedJob = p;
                            chooseAttributes();
                            player.closeInventory();
                            this.openInventory();
                        }else{
                            soundDeny();
                            player.closeInventory();
                            player.sendMessage(Feudal.getMessage("selection.deny"));
                            delayOpen();
                        }
                    }else{
                        soundDeny();
                        player.closeInventory();
                        player.sendMessage(Feudal.getMessage("selection.np"));
                        delayOpen();
                    }
                }else if(itemString.equals("BARON")){
                    final int p = Feudal.getConfiguration().getConfig().getInt("Baron.pointCost");
                    if(points >= p){
                        if(Profession.checkProfession(Profession.Type.BARON)){
                            soundClick();
                            profession = new Profession(user, Profession.Type.BARON, 0, 0);
                            points -= p;
                            pointsUsedJob = p;
                            chooseAttributes();
                            player.closeInventory();
                            this.openInventory();
                        }else{
                            soundDeny();
                            player.closeInventory();
                            player.sendMessage(Feudal.getMessage("selection.deny"));
                            delayOpen();
                        }
                    }else{
                        soundDeny();
                        player.closeInventory();
                        player.sendMessage(Feudal.getMessage("selection.np"));
                        delayOpen();
                    }
                }else if(itemString.equals("KING")){
                    final int p = Feudal.getConfiguration().getConfig().getInt("King.pointCost");
                    if(points >= p){
                        if(Profession.checkProfession(Profession.Type.KING)){
                            soundClick();
                            profession = new Profession(user, Profession.Type.KING, 0, 0);
                            points -= p;
                            pointsUsedJob = p;
                            chooseAttributes();
                            player.closeInventory();
                            this.openInventory();
                        }else{
                            soundDeny();
                            player.closeInventory();
                            player.sendMessage(Feudal.getMessage("selection.deny"));
                            delayOpen();
                        }
                    }else{
                        soundDeny();
                        player.closeInventory();
                        player.sendMessage(Feudal.getMessage("selection.np"));
                        delayOpen();
                    }
                }else if(itemString.equals("BACK")){
                    soundClick();
                    points += pointsUsedClass;
                    pointsUsedClass = 0;
                    chooseSocialClass();
                    player.closeInventory();
                    this.openInventory();
                }
            }else if(inventory == 4){
                if(itemString.equals("ADD_STRENGTH")){
                    if(points >= 1){
                        final int rate = Feudal.getConfiguration().getConfig().getInt("Strength.LevelPerPoint");
                        if(!attributeIsMax(rate) || !Feudal.getConfiguration().getConfig().getBoolean("attributeCap.enable")){
                            final int max = Feudal.getConfiguration().getConfig().getInt("Strength.MaxLevel");
                            if(strength >= max){
                                soundDeny();
                                player.closeInventory();
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
                            player.closeInventory();
                            player.sendMessage(Feudal.getMessage("selection.maxAtt"));
                            delayOpen();
                        }
                    }else{
                        soundDeny();
                        player.closeInventory();
                        player.sendMessage(Feudal.getMessage("selection.np"));
                        delayOpen();
                    }
                }else if(itemString.equals("ADD_TOUGHNESS")){
                    if(points >= 1){
                        final int rate = Feudal.getConfiguration().getConfig().getInt("Toughness.LevelPerPoint");
                        if(!attributeIsMax(rate) || !Feudal.getConfiguration().getConfig().getBoolean("attributeCap.enable")){
                            final int max = Feudal.getConfiguration().getConfig().getInt("Toughness.MaxLevel");
                            if(toughness >= max){
                                soundDeny();
                                player.closeInventory();
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
                            player.closeInventory();
                            player.sendMessage(Feudal.getMessage("selection.maxAtt"));
                            delayOpen();
                        }
                    }else{
                        soundDeny();
                        player.closeInventory();
                        player.sendMessage(Feudal.getMessage("selection.np"));
                        delayOpen();
                    }
                }else if(itemString.equals("ADD_SPEED")){
                    if(points >= 1){
                        final int rate = Feudal.getConfiguration().getConfig().getInt("Speed.LevelPerPoint");
                        if(!attributeIsMax(rate) || !Feudal.getConfiguration().getConfig().getBoolean("attributeCap.enable")){
                            final int max = Feudal.getConfiguration().getConfig().getInt("Speed.MaxLevel");
                            if(speed >= max){
                                soundDeny();
                                player.closeInventory();
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
                            player.closeInventory();
                            player.sendMessage(Feudal.getMessage("selection.maxAtt"));
                            delayOpen();
                        }
                    }else{
                        soundDeny();
                        player.closeInventory();
                        player.sendMessage(Feudal.getMessage("selection.np"));
                        delayOpen();
                    }
                }else if(itemString.equals("ADD_STAMINA")){
                    if(points >= 1){
                        final int rate = Feudal.getConfiguration().getConfig().getInt("Stamina.LevelPerPoint");
                        if(!attributeIsMax(rate) || !Feudal.getConfiguration().getConfig().getBoolean("attributeCap.enable")){
                            final int max = Feudal.getConfiguration().getConfig().getInt("Stamina.MaxLevel");
                            if(stamina >= max){
                                soundDeny();
                                player.closeInventory();
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
                            player.closeInventory();
                            player.sendMessage(Feudal.getMessage("selection.maxAtt"));
                            delayOpen();
                        }
                    }else{
                        soundDeny();
                        player.closeInventory();
                        player.sendMessage(Feudal.getMessage("selection.np"));
                        delayOpen();
                    }
                }else if(itemString.equals("ADD_LUCK")){
                    if(points >= 1){
                        final int rate = Feudal.getConfiguration().getConfig().getInt("Luck.LevelPerPoint");
                        if(!attributeIsMax(rate) || !Feudal.getConfiguration().getConfig().getBoolean("attributeCap.enable")){
                            final int max = Feudal.getConfiguration().getConfig().getInt("Luck.MaxLevel");
                            if(luck >= max){
                                soundDeny();
                                player.closeInventory();
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
                            player.closeInventory();
                            player.sendMessage(Feudal.getMessage("selection.maxAtt"));
                            delayOpen();
                        }
                    }else{
                        soundDeny();
                        player.closeInventory();
                        player.sendMessage(Feudal.getMessage("selection.np"));
                        delayOpen();
                    }
                }else if(itemString.equals("SUBTRACT_STRENGTH")){
                    final int rate = Feudal.getConfiguration().getConfig().getInt("Strength.LevelPerPoint");
                    final int max = Feudal.getConfiguration().getConfig().getInt("Strength.MaxLevel");
                    if(strength - rate < 0){
                        soundDeny();
                        player.closeInventory();
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
                    final int rate = Feudal.getConfiguration().getConfig().getInt("Toughness.LevelPerPoint");
                    final int max = Feudal.getConfiguration().getConfig().getInt("Toughness.MaxLevel");
                    if(toughness - rate < 0){
                        soundDeny();
                        player.closeInventory();
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
                    final int rate = Feudal.getConfiguration().getConfig().getInt("Speed.LevelPerPoint");
                    final int max = Feudal.getConfiguration().getConfig().getInt("Speed.MaxLevel");
                    if(speed - rate < 0){
                        soundDeny();
                        player.closeInventory();
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
                    final int rate = Feudal.getConfiguration().getConfig().getInt("Stamina.LevelPerPoint");
                    final int max = Feudal.getConfiguration().getConfig().getInt("Stamina.MaxLevel");
                    if(stamina - rate < 0){
                        soundDeny();
                        player.closeInventory();
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
                    final int rate = Feudal.getConfiguration().getConfig().getInt("Luck.LevelPerPoint");
                    final int max = Feudal.getConfiguration().getConfig().getInt("Luck.MaxLevel");
                    if(luck - rate < 0){
                        soundDeny();
                        player.closeInventory();
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
                }else if(itemString.equals("BACK")){
                    soundClick();
                    final int rateStrength = Feudal.getConfiguration().getConfig().getInt("Strength.LevelPerPoint");
                    final int rateToughness = Feudal.getConfiguration().getConfig().getInt("Toughness.LevelPerPoint");
                    final int rateSpeed = Feudal.getConfiguration().getConfig().getInt("Speed.LevelPerPoint");
                    final int rateStamina = Feudal.getConfiguration().getConfig().getInt("Stamina.LevelPerPoint");
                    final int rateLuck = Feudal.getConfiguration().getConfig().getInt("Luck.LevelPerPoint");
                    final int maxStrength = Feudal.getConfiguration().getConfig().getInt("Strength.MaxLevel");
                    final int maxToughness = Feudal.getConfiguration().getConfig().getInt("Toughness.MaxLevel");
                    final int maxSpeed = Feudal.getConfiguration().getConfig().getInt("Speed.MaxLevel");
                    final int maxStamina = Feudal.getConfiguration().getConfig().getInt("Stamina.MaxLevel");
                    final int maxLuck = Feudal.getConfiguration().getConfig().getInt("Luck.MaxLevel");

                    if(strength == maxStrength && maxStrength % rateStrength > 0){
                        points++;
                    }
                    if(toughness == maxToughness && maxToughness % rateToughness > 0){
                        points++;
                    }
                    if(speed == maxSpeed && maxSpeed % rateSpeed > 0){
                        points++;
                    }
                    if(stamina == maxStamina && maxStamina % rateStamina > 0){
                        points++;
                    }
                    if(luck == maxLuck && maxLuck % rateLuck > 0){
                        points++;
                    }
                    points+=((strength / rateStrength) + (toughness / rateToughness) + (speed / rateSpeed) + (stamina / rateStamina) + (luck / rateLuck) + pointsUsedJob);
                    strength = 0;
                    toughness = 0;
                    speed = 0;
                    stamina = 0;
                    luck = 0;

                    chooseJob(classIndex);
                    player.closeInventory();
                    this.openInventory();

                }else if(itemString.equals("FINISH")){
                    soundClick();
                    confirmFinish();
                    player.closeInventory();
                    this.openInventory();
                }
            }else if(inventory == 5){
                if(itemString.equals("BACK")){
                    soundClick();
                    chooseAttributes();
                    player.closeInventory();
                    this.openInventory();
                }else if(itemString.equals("FINISH")){
                    player.closeInventory();
                    if(Feudal.getOnlinePlayers().containsKey(player.getUniqueId().toString())){
                        if (Feudal.getConfiguration().getConfig().getBoolean("forceUsePoints")){
                            if (points > 0){
                                player.sendMessage(Feudal.getMessage("redistribute.spendPoints"));
                                return false;
                            }
                        }
                        soundComplete();
                        final User u = Feudal.getOnlinePlayers().get(player.getUniqueId().toString());
                        u.setProfession(profession);
                        u.setStrength(new Attributes(Attribute.STRENGTH, 0, strength, u));
                        u.setToughness(new Attributes(Attribute.TOUGHNESS, 0, toughness, u));
                        u.setSpeed(new Attributes(Attribute.SPEED, 0, speed, u));
                        u.setStamina(new Attributes(Attribute.STAMINA, 0, stamina, u));
                        u.setLuck(new Attributes(Attribute.LUCK, 0, luck, u));
                        u.effectAttributes();
                        u.save(true);

                        if(Feudal.getEco() == 1) {
                            final User user = Feudal.getUser(player.getUniqueId().toString());
                            if(user == null || user.getProfession() == null || user.getProfession().getType().equals(Profession.Type.NONE)){
                                VaultUtils.removePermission(player, "feudal.character.setup");
                                VaultUtils.addPermission(player, "feudal.character.notsetup");
                            }else {
                                VaultUtils.addPermission(player, "feudal.character.setup");
                                VaultUtils.removePermission(player, "feudal.character.notsetup");
                            }
                        }

                        complete = true;
                        player.sendMessage(Feudal.getMessage("selection.c"));

                        Bukkit.getPluginManager().callEvent(new UserCreateEvent(u));
                    }else{
                        soundDeny();
                        player.sendMessage(Feudal.getMessage("selection.cf"));
                    }
                }
            }
            return true;
        }else{
            return false;
        }
    }

    private boolean attributeIsMax(int rate) {
        if(strength + speed + toughness + stamina + luck + rate > Feudal.getConfiguration().getConfig().getInt("attributeCap.cap")){
            return true;
        }else{
            return false;
        }
    }



    //
    //
    //
    //
    //EVENTS:
    public static void commandEvent(PlayerCommandPreprocessEvent event) {
        if(Feudal.getSelections().size() > 0){
            final ArrayList<Selection> remove = new ArrayList<>();
            boolean found = false;
            for(final Selection sel : Feudal.getSelections()){
                if(sel.complete){
                    remove.add(sel);
                }else{
                    if(sel.getPlayer().equals(event.getPlayer())){
                        if(Feudal.getConfiguration().getConfig().getBoolean("setup.require")){
                            found = true;
                            if(Utils.isFeudalCommand(event.getMessage())){
                                sel.openInventory();
                                event.setCancelled(true);
                            }else{
                                for(final String l : Feudal.getConfiguration().getConfig().getStringList("setup.requireCmdExceptions")){
                                    if(event.getMessage().toLowerCase().startsWith(l.toLowerCase())){
                                        return;
                                    }
                                }
                                event.getPlayer().sendMessage(Feudal.getMessage("selection.cmd"));
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
            if(!found){
                setupWhenEver(event);
            }
            for(final Selection sel : remove){
                Feudal.getSelections().remove(sel);
            }
        }else{
            setupWhenEver(event);
        }
    }

    private static void setupWhenEver(PlayerCommandPreprocessEvent event){
        if(Utils.isFeudalCommand(event.getMessage())){
            User u = Feudal.getUser(event.getPlayer().getUniqueId().toString());
            boolean sel = false;
            if(u == null){
                event.setCancelled(true);
                Feudal.unloadPlayer(event.getPlayer());
                Feudal.loadPlayer(event.getPlayer());
                u = Feudal.getUser(event.getPlayer().getUniqueId().toString());
                if(u == null){
                    event.getPlayer().sendMessage(Feudal.getMessage("selection.unull"));
                    return;
                }else{
                    sel = true;
                }
            }else{
                if(u.getProfession() == null || u.getProfession().getType().equals(Profession.Type.NONE)){
                    event.setCancelled(true);
                    sel = true;
                }
            }
            if(sel){
                boolean f = false;
                for(final Selection s : Feudal.getSelections()){
                    if(s.getPlayer().equals(event.getPlayer())){
                        s.openInventory();
                        f = true;
                    }
                }
                if(!f){
                    final Selection select = new Selection(event.getPlayer());
                    Feudal.getSelections().add(select);
                    select.openInventory();
                }
            }
        }
    }

    public static void moveEvent(PlayerMoveEvent event) {
        if(Feudal.getSelections().size() > 0){
            final ArrayList<Selection> remove = new ArrayList<>();
            for(final Selection sel : Feudal.getSelections()){
                if(sel.complete){
                    remove.add(sel);
                }else{
                    if(sel.getPlayer().equals(event.getPlayer())){
                        if(Feudal.getConfiguration().getConfig().getBoolean("setup.require")){
                            sel.openInventory();
                        }
                    }
                }
            }
            for(final Selection sel : remove){
                Feudal.getSelections().remove(sel);
            }
        }
    }

    public static void inventoryClickEvent(InventoryClickEvent event) {
        if(Feudal.getSelections().size() > 0){
            final ArrayList<Selection> remove = new ArrayList<>();
            for(final Selection sel : Feudal.getSelections()){
                if(sel.complete){
                    remove.add(sel);
                }else{
                    if(sel.getPlayer().equals(event.getWhoClicked())){
                        if(sel.click(sel.inventory1.getItemString(event.getCurrentItem()))){
                            event.setCancelled(true);
                        }
                        if(Feudal.getConfiguration().getConfig().getBoolean("setup.require") || event.getInventory().equals(sel.inventory1.getInventory())){
                            event.setCancelled(true);
                        }
                    }
                }
            }
            for(final Selection sel : remove){
                Feudal.getSelections().remove(sel);
            }
        }
    }

}
