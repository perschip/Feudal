package me.invertmc.user;

import java.util.ArrayList;
import java.util.List;

import me.invertmc.Feudal;
import me.invertmc.utils.InventoryGui;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class ChangeProfession {
    private InventoryGui inventory;
    private final Player player;
    private final User playerEdit;
    private Profession.Type type = null;
    private boolean max;
    private boolean changingInv = false;
    private boolean finished = false;
    private static ArrayList<ChangeProfession> changing = new ArrayList<>();

    private boolean king = false;

    public ChangeProfession(Player player, User playerEdit){
        changing.add(this);
        this.player = player;
        this.playerEdit = playerEdit;
        inventory = this.changeProfession();
        openInventory();
    }

    private InventoryGui confirm(ItemStack item){
        final InventoryGui i = new InventoryGui(Bukkit.createInventory(player, 9, InventoryGui.lim(Feudal.getMessage("changeProfession.confirm"))));


        i.setItem("CON-NO", 0, Selection.createItem(XMaterial.TNT.parseMaterial(), 1, (short) 0, Feudal.getMessage("changeProfession.conno"), new ArrayList<String>()));
        i.setItem("CON-ITEM", 4, item);
        i.setItem("CON-YES", 8, Selection.createItem(XMaterial.EMERALD_BLOCK.parseMaterial(), 1, (short) 0, Feudal.getMessage("changeProfession.conyes"), new ArrayList<String>()));


        return i;
    }

    private InventoryGui changeProfession() {
        final InventoryGui i = new InventoryGui(Bukkit.createInventory(player, Feudal.getConfiguration().getConfig().contains("ArchBishop.land") ? 54 : 45, InventoryGui.lim(Feudal.getMessage("changeProfession.title"))));

        if(playerEdit.getProfession().getLevel() >= playerEdit.getProfession().getMaxLevel()){
            max = true;
        }
        final ArrayList<Profession> pastProfessions = playerEdit.getPastProfessions();
        float completePeasant = 0;
        float completeCommoner = 0;
        boolean squire = false;
        boolean knight = false;
        boolean baron = false;
        king = false;
        for(final Profession past : pastProfessions){
            if(!past.getType().equals(playerEdit.getProfession().getType())){
                if(past.getLevel() >= past.getMaxLevel()){
                    if(past.getType().equals(Profession.Type.SQUIRE)){
                        squire = true;
                    }
                    if(past.getType().equals(Profession.Type.KNIGHT)){
                        knight = true;
                    }
                    if(past.getType().equals(Profession.Type.BARON)){
                        baron = true;
                    }
                    if(past.getType().equals(Profession.Type.KING)){
                        king = true;
                    }
                    if(isPeasant(past.getType())){
                        completePeasant++;
                    }
                    if(isCommoner(past.getType())){
                        completeCommoner++;
                    }
                }
            }
        }
        if(max){
            if(playerEdit.getProfession().getType() == Type.SQUIRE){
                squire = true;
            }else if(playerEdit.getProfession().getType() == Type.KNIGHT){
                knight = true;
            }else if(playerEdit.getProfession().getType() == Type.BARON){
                baron = true;
            }else if(playerEdit.getProfession().getType() == Type.KING){
                king = true;
            }
            if(isPeasant(playerEdit.getProfession().getType())){
                completePeasant++;
            }
            if(isCommoner(playerEdit.getProfession().getType())){
                completeCommoner++;
            }
        }
        completePeasant = (completePeasant / 9) * 100; //9 is peasant total
        completeCommoner = (completeCommoner / 6) * 100; //6 is peasant total

        final boolean FARMER = Profession.checkProfession(Type.FARMER);
        final boolean LOGGER = Profession.checkProfession(Type.LOGGER);
        final boolean HUNTER = Profession.checkProfession(Type.HUNTER);
        final boolean MINER = Profession.checkProfession(Type.MINER);
        final boolean COOK = Profession.checkProfession(Type.COOK);
        final boolean FISHER = Profession.checkProfession(Type.FISHER);
        final boolean BUILDER = Profession.checkProfession(Type.BUILDER);
        final boolean SHEPHERD = Profession.checkProfession(Type.SHEPHERD);
        final boolean SCRIBE = Profession.checkProfession(Type.SCRIBE);

        final boolean GUARD = Profession.checkProfession(Type.GUARD);
        final boolean ASSASSIN = Profession.checkProfession(Type.ASSASSIN);
        final boolean ALCHEMIST = Profession.checkProfession(Type.ALCHEMIST);
        final boolean BLACKSMITH = Profession.checkProfession(Type.BLACKSMITH);
        final boolean HEALER = Profession.checkProfession(Type.HEALER);
        final boolean MERCHANT = Profession.checkProfession(Type.MERCHANT);

        final boolean SQUIRE = Profession.checkProfession(Type.SQUIRE);
        final boolean KNIGHT = Profession.checkProfession(Type.KNIGHT);
        final boolean BARON = Profession.checkProfession(Type.BARON);
        final boolean KING = Profession.checkProfession(Type.KING);


        final String strength = Feudal.getMessage("changeProfession.Strength");
        final String stamina = Feudal.getMessage("changeProfession.Stamina");
        final String luck = Feudal.getMessage("changeProfession.Luck");
        final String speed = Feudal.getMessage("changeProfession.Speed");
        final String toughness = Feudal.getMessage("changeProfession.Toughness");

        final ArrayList<String> farmer = setInformation("Farmer", max, completePeasant, completeCommoner, squire, knight, baron, FARMER, (byte)0,
                playerEdit.getProfession(), strength + " & " + stamina, Feudal.getMessage("changeProfession.farmer1"));
        final ArrayList<String> logger = setInformation("Logger", max, completePeasant, completeCommoner, squire, knight, baron, LOGGER, (byte)0,
                playerEdit.getProfession(), strength + " & " + toughness, Feudal.getMessage("changeProfession.logger1"));
        final ArrayList<String> hunter = setInformation("Hunter", max, completePeasant, completeCommoner, squire, knight, baron, HUNTER, (byte)0,
                playerEdit.getProfession(), speed + " & " + stamina, Feudal.getMessage("changeProfession.hunter1"));
        final ArrayList<String> miner = setInformation("Miner", max, completePeasant, completeCommoner, squire, knight, baron, MINER, (byte)0,
                playerEdit.getProfession(), strength + " & " + luck, Feudal.getMessage("changeProfession.miner1"));
        final ArrayList<String> cook = setInformation("Cook", max, completePeasant, completeCommoner, squire, knight, baron, COOK, (byte)0,
                playerEdit.getProfession(), stamina + " & " + luck, Feudal.getMessage("changeProfession.cook1"));
        final ArrayList<String> fisher = setInformation("Fisher", max, completePeasant, completeCommoner, squire, knight, baron, FISHER, (byte)0,
                playerEdit.getProfession(), toughness + " & " + luck, Feudal.getMessage("changeProfession.fisher1"));
        final ArrayList<String> builder = setInformation("Builder", max, completePeasant, completeCommoner, squire, knight, baron, BUILDER, (byte)0,
                playerEdit.getProfession(), strength + " & " + stamina, Feudal.getMessage("changeProfession.builder1"));
        final ArrayList<String> shepherd = setInformation("Shepherd", max, completePeasant, completeCommoner, squire, knight, baron, SHEPHERD, (byte)0,
                playerEdit.getProfession(), stamina + " & " + toughness, Feudal.getMessage("changeProfession.shepherd1"));
        final ArrayList<String> scribe = setInformation("Scribe", max, completePeasant, completeCommoner, squire, knight, baron, SCRIBE, (byte)0,
                playerEdit.getProfession(), toughness + " & " + luck, Feudal.getMessage("changeProfession.scribe1"));

        final ArrayList<String> guard = setInformation("Guard", max, completePeasant, completeCommoner, squire, knight, baron, GUARD, (byte)1,
                playerEdit.getProfession(), strength + " & " + toughness, Feudal.getMessage("changeProfession.guard1"));
        final ArrayList<String> assassin = setInformation("Assassin", max, completePeasant, completeCommoner, squire, knight, baron, ASSASSIN, (byte)1,
                playerEdit.getProfession(), speed + " & " + luck, Feudal.getMessage("changeProfession.assassin1"));
        assassin.add(Feudal.getMessage("changeProfession.assassin2"));
        assassin.add(Feudal.getMessage("changeProfession.assassin3"));
        final ArrayList<String> alchemist = setInformation("Alchemist", max, completePeasant, completeCommoner, squire, knight, baron, ALCHEMIST, (byte)1,
                playerEdit.getProfession(), speed + " & " + stamina, Feudal.getMessage("changeProfession.alchemist1"));
        final ArrayList<String> blacksmith = setInformation("Blacksmith", max, completePeasant, completeCommoner, squire, knight, baron, BLACKSMITH, (byte)1,
                playerEdit.getProfession(), strength + " & " + stamina, Feudal.getMessage("changeProfession.blacksmith1"));
        final ArrayList<String> healer = setInformation("Healer", max, completePeasant, completeCommoner, squire, knight, baron, HEALER, (byte)1,
                playerEdit.getProfession(), luck + " & " + stamina, Feudal.getMessage("changeProfession.healer1"));
        final ArrayList<String> merchant = setInformation("Merchant", max, completePeasant, completeCommoner, squire, knight, baron, MERCHANT, (byte)1,
                playerEdit.getProfession(), stamina + " & " + luck, Feudal.getMessage("changeProfession.merchant1"));

        final ArrayList<String> squireArr = setInformation("Squire", max, completePeasant, completeCommoner, squire, knight, baron, SQUIRE, (byte)2,
                playerEdit.getProfession(), speed + " & " + toughness, Feudal.getMessage("changeProfession.squire1"));
        final ArrayList<String> knightArr = setInformation("Knight", max, completePeasant, completeCommoner, squire, knight, baron, KNIGHT, (byte)3,
                playerEdit.getProfession(), strength + " & " + toughness, Feudal.getMessage("changeProfession.knight1"));
        final ArrayList<String> baronArr = setInformation("Baron", max, completePeasant, completeCommoner, squire, knight, baron, BARON, (byte)4,
                playerEdit.getProfession(), Feudal.getMessage("changeProfession.all"), Feudal.getMessage("changeProfession.baron1"));
        final ArrayList<String> kingArr = setInformation("King", max, completePeasant, completeCommoner, squire, knight, baron, KING, (byte)5,
                playerEdit.getProfession(), Feudal.getMessage("changeProfession.all"), Feudal.getMessage("changeProfession.king1"));

        ArrayList<String> archbishop = null;
        if(Feudal.getConfiguration().getConfig().contains("ArchBishop.land")){
            archbishop = setInformation("ArchBishop", max, completePeasant, completeCommoner, squire, knight, baron, KING, (byte)6,
                    playerEdit.getProfession(), Feudal.getMessage("changeProfession.all"), "Like a king but better.");
        }


        i.setItem("FARMER", 0, Selection.createItem(XMaterial.STONE_HOE.parseMaterial(), 1, (short) 0, Feudal.getMessage("changeProfession.farmer"), farmer));
        i.setItem("LOGGER", 1, Selection.createItem(XMaterial.OAK_LOG.parseMaterial(), 1, (short) 0, Feudal.getMessage("changeProfession.Logger"), logger));
        i.setItem("HUNTER", 2, Selection.createItem(XMaterial.BOW.parseMaterial(), 1, (short) 0, Feudal.getMessage("changeProfession.Hunter"), hunter));
        i.setItem("MINER", 3, Selection.createItem(XMaterial.STONE_PICKAXE.parseMaterial(), 1, (short) 0, Feudal.getMessage("changeProfession.Miner"), miner));
        i.setItem("COOK", 4, Selection.createItem(XMaterial.CAKE.parseMaterial(), 1, (short) 0, Feudal.getMessage("changeProfession.Cook"), cook));
        i.setItem("FISHER", 5, Selection.createItem(XMaterial.FISHING_ROD.parseMaterial(), 1, (short) 0, Feudal.getMessage("changeProfession.Fisher"), fisher));
        i.setItem("BUILDER", 6, Selection.createItem(XMaterial.STONE_BRICKS.parseMaterial(), 1, (short) 0, Feudal.getMessage("changeProfession.Builder"), builder));
        i.setItem("SHEPHERD", 7, Selection.createItem(XMaterial.SHEARS.parseMaterial(), 1, (short) 0, Feudal.getMessage("changeProfession.Shepherd"), shepherd));
        i.setItem("SCRIBE", 8, Selection.createItem(XMaterial.WRITABLE_BOOK.parseMaterial(), 1, (short) 0, Feudal.getMessage("changeProfession.Scribe"), scribe));

        i.setItem("GUARD", 19, Selection.createItem(XMaterial.STONE_SWORD.parseMaterial(), 1, (short) 0, Feudal.getMessage("changeProfession.Guard"), guard));
        i.setItem("ASSASSIN", 20, Selection.createItem(XMaterial.COMPASS.parseMaterial(), 1, (short) 0, Feudal.getMessage("changeProfession.Assassin"), assassin));
        i.setItem("ALCHEMIST", 21, Selection.createItem(XMaterial.BREWING_STAND.parseMaterial(), 1, (short) 0, Feudal.getMessage("changeProfession.Alchemist"), alchemist));
        i.setItem("BLACKSMITH", 23, Selection.createItem(XMaterial.ANVIL.parseMaterial(), 1, (short) 0, Feudal.getMessage("changeProfession.Blacksmith"), blacksmith));
        i.setItem("HEALER", 24, Selection.createItem(XMaterial.GOLDEN_APPLE.parseMaterial(), 1, (short) 0, Feudal.getMessage("changeProfession.Healer"), healer));
        i.setItem("MERCHANT", 25, Selection.createItem(XMaterial.EMERALD.parseMaterial(), 1, (short) 0, Feudal.getMessage("changeProfession.Merchant"), merchant));

        i.setItem("SQUIRE", 37, Selection.createItem(XMaterial.IRON_SWORD.parseMaterial(), 1, (short) 0, Feudal.getMessage("changeProfession.Squire"), squireArr));
        i.setItem("KNIGHT", 38, Selection.createItem(XMaterial.DIAMOND_SWORD.parseMaterial(), 1, (short) 0, Feudal.getMessage("changeProfession.Knight"), knightArr));
        i.setItem("BARON", 42, Selection.createItem(XMaterial.DIAMOND_HELMET.parseMaterial(), 1, (short) 0, Feudal.getMessage("changeProfession.Baron"), baronArr));
        i.setItem("KING", 43, Selection.createItem(XMaterial.GOLDEN_HELMET.parseMaterial(), 1, (short) 0, Feudal.getMessage("changeProfession.King"), kingArr));

        if(archbishop != null){
            i.setItem("ARCHBISHOP", 49, Selection.createItem(XMaterial.NETHER_STAR.parseMaterial(), 1, (short) 0, "\u00a7a\u00a7lArchBishop", archbishop));
        }

        return i;
    }

    private ArrayList<String> setInformation(String name, boolean max,
                                             float completePeasant, float completeCommoner, boolean squire,
                                             boolean knight, boolean baron, boolean available, byte group, Profession current, String attributes, String desc) {//group: 0 = peasant, 1 = commoner, 2 = squire, 3 = knight, 4 = baron, 5 = king
        final ArrayList<String> a = new ArrayList<>();
        if(current.getType().toString().equalsIgnoreCase(name)){
            a.add(Feudal.getMessage("changeProfession.cur"));
        }else if(group == 1 && isPeasant(current.getType()) && completePeasant < Feudal.getConfiguration().getConfig().getDouble("Profession.requiredPeasantCompletionPercent")){
            a.add(Feudal.getMessage("changeProfession.lock"));
            final double percent = (Feudal.getConfiguration().getConfig().getDouble("Profession.requiredPeasantCompletionPercent") - completePeasant);
            final double needed = percent/(100/9);
            int n = (int) needed;
            if(n < needed){
                n++;
            }
            if(n > 9) {
                n = 9;
            }

            a.add(Feudal.getMessage("changeProfession.needMaster").replace("%n%", n+""));
        }else if(group == 2 && (isCommoner(current.getType()) || isPeasant(current.getType())) && completeCommoner < Feudal.getConfiguration().getConfig().getDouble("Profession.requiredCommonerCompletion")){//SQUIRE
            a.add(Feudal.getMessage("changeProfession.lock"));
            final double percent = (Feudal.getConfiguration().getConfig().getDouble("Profession.requiredCommonerCompletion") - completeCommoner);
            final double needed = percent/(100/6);
            int n = (int) needed;
            if(n < needed){
                n++;
            }
            if(n > 6) {
                n = 6;
            }

            a.add(Feudal.getMessage("changeProfession.needMasterComm").replace("%n%", n+""));
        }else if(group == 3 && !squire && Feudal.getConfiguration().getConfig().getBoolean("Profession.doRankupForNobles")){
            a.add(Feudal.getMessage("changeProfession.lock"));
            a.add(Feudal.getMessage("changeProfession.squireMaster"));
        }else if(group == 4 && !knight && Feudal.getConfiguration().getConfig().getBoolean("Profession.doRankupForNobles")){
            a.add(Feudal.getMessage("changeProfession.lock"));
            a.add(Feudal.getMessage("changeProfession.knightMaster"));
        }else if(group == 5 && !baron && Feudal.getConfiguration().getConfig().getBoolean("Profession.doRankupForNobles")){
            a.add(Feudal.getMessage("changeProfession.lock"));
            a.add(Feudal.getMessage("changeProfession.baronMaster"));
        }else if(group == 6 && (!king || !baron || !squire || !knight || completePeasant < 100 || completeCommoner < 100)){
            a.add(Feudal.getMessage("changeProfession.lock"));
            a.add("\u00a7cYou must master ALL PROFESSIONS first.");
        }else if(group >= 3 && !Feudal.getConfiguration().getConfig().getBoolean("Profession.doRankupForNobles") && completeCommoner < Feudal.getConfiguration().getConfig().getDouble("Profession.requiredCommonerCompletionPercent")){
            a.add(Feudal.getMessage("changeProfession.lock"));
            final double percent = (Feudal.getConfiguration().getConfig().getDouble("Profession.requiredCommonerCompletionPercent") - completeCommoner);
            final double needed = percent/(100/6);
            int n = (int) needed;
            if(n < needed){
                n++;
            }
            if(n > 6) {
                n = 6;
            }

            a.add(Feudal.getMessage("changeProfession.needMasterComm").replace("%n%", n+""));
        }else if(!max){
            a.add(Feudal.getMessage("changeProfession.warn"));
            a.add(Feudal.getMessage("changeProfession.warn2"));
            a.add(Feudal.getMessage("changeProfession.warn3"));
            a.add(Feudal.getMessage("changeProfession.warn4"));
        }

        if(group == 0){
            a.add(Feudal.getMessage("changeProfession.sp"));
        }else if(group == 1){
            a.add(Feudal.getMessage("changeProfession.sc"));
        }else{
            a.add(Feudal.getMessage("changeProfession.sn"));
        }
        a.add(Feudal.getMessage("changeProfession.landA") + Feudal.getConfiguration().getConfig().getInt(name + ".land"));
        a.add(Feudal.getMessage("changeProfession.ag") + attributes);
        a.add(Feudal.getMessage("changeProfession.dd") + desc);

        return a;
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

    private boolean isCommoner(Type type) {
        if(type.equals(Profession.Type.GUARD) || type.equals(Profession.Type.ASSASSIN) || type.equals(Profession.Type.ALCHEMIST) || type.equals(Profession.Type.BLACKSMITH) || type.equals(Profession.Type.HEALER) || type.equals(Profession.Type.MERCHANT)){
            return true;
        }else{
            return false;
        }
    }

    private boolean isPeasant(Type type) {
        if(type.equals(Profession.Type.FARMER) || type.equals(Profession.Type.LOGGER) || type.equals(Profession.Type.HUNTER) || type.equals(Profession.Type.MINER) ||
                type.equals(Profession.Type.COOK) || type.equals(Profession.Type.FISHER) || type.equals(Profession.Type.BUILDER) || type.equals(Profession.Type.SHEPHERD) || type.equals(Profession.Type.SCRIBE)){
            return true;
        }else{
            return false;
        }
    }

    public void openInventory(){
        player.openInventory(inventory.getInventory());
    }

    public static void close(InventoryCloseEvent event) {
        if(changing.size() > 0){
            final ArrayList<ChangeProfession> remove = new ArrayList<>();
            for(final ChangeProfession c : changing){
                if(c.inventory.getInventory().getViewers().size() == 0 && !c.changingInv){
                    remove.add(c);
                }else{
                    if(c.player.equals(event.getPlayer()) && !c.changingInv){
                        if(event.getInventory().equals(c.inventory.getInventory())){
                            remove.add(c);
                        }
                    }
                }
            }
            for(final ChangeProfession c : remove){
                changing.remove(c);
            }
        }
    }

    public static void inventoryClickEvent(InventoryClickEvent event) {
        if(changing.size() > 0){
            final ArrayList<ChangeProfession> remove = new ArrayList<>();
            for(final ChangeProfession c : changing){
                if(c.inventory.getInventory().getViewers().size() == 0){
                    remove.add(c);
                }else{
                    if(c.player.equals(event.getWhoClicked())){
                        c.click(c.inventory.getItemString(event.getCurrentItem()));
                        if(c.finished){
                            remove.add(c);
                        }
                        event.setCancelled(true);
                    }
                }
            }
            for(final ChangeProfession c : remove){
                changing.remove(c);
            }
        }
    }

    private void click(String itemString) {
        if(itemString != null){
            if(itemString.startsWith("CON-")){
                if(itemString.equals("CON-NO")){
                    changingInv = true;
                    player.closeInventory();
                    inventory = this.changeProfession();
                    openInventory();
                    changingInv = false;
                    this.soundClick();
                }else if(itemString.equals("CON-YES")){
                    if(max){
                        this.playerEdit.addPastProfession(this.playerEdit.getProfession());
                    }else{
                        playerEdit.effectReputation(Feudal.getConfiguration().getConfig().getInt("reputation.changeProfessionWithoutMax"), Feudal.getMessage("reputation.changeProfessionWithoutMax"));
                    }
                    Profession p = null;
                    for(final Profession past : this.playerEdit.getPastProfessions()){
                        if(past.getType().equals(type)){
                            p = past;
                        }
                    }
                    if(p == null){
                        p = new Profession(playerEdit, type, 0, 0);
                    }
                    playerEdit.unloadPermissions();
                    this.playerEdit.setProfession(p);
                    playerEdit.loadPermissions();
                    this.playerEdit.effectAttributes();
                    this.playerEdit.save(true);

                    if(!playerEdit.getKingdomUUID().equals("")){
                        final Kingdom k = Feudal.getKingdom(playerEdit.getKingdomUUID());
                        if(k != null){
                            k.updateFightersAndLand();
                            k.updateIncomeTax();
							/*try {
								k.save();
							} catch (Exception e) {
								ErrorManager.error(87, e);
							}*/
                        }
                    }

                    this.soundComplete();
                    if(playerEdit.getPlayer().equals(player)){
                        player.sendMessage(Feudal.getMessage("changeProfession.change").replace("%a%", p.getType().getNameLang()));
                    }else{
                        player.sendMessage(Feudal.getMessage("changeProfession.changeOther").replace("%name%", playerEdit.getName()).replace("%type%", p.getType().getNameLang()));
                        if(playerEdit.getPlayer() != null && playerEdit.getPlayer().isOnline()){
                            playerEdit.getPlayer().sendMessage(Feudal.getMessage("changeProfession.change").replace("%a%", p.getType().getNameLang()));
                            soundComplete();
                        }
                    }
                    changingInv = true;
                    player.closeInventory();
                    changingInv = false;
                    finished  = true;
                }
            }else{
                final ItemStack item = inventory.getItemStack(itemString);
                if(item != null && item.getItemMeta() != null && item.getItemMeta().getLore() != null && item.getItemMeta().getLore().size() > 0){
                    final List<String> lore = item.getItemMeta().getLore();
                    if(lore.get(0).equals(Feudal.getMessage("changeProfession.cur"))){
                        player.sendMessage(Feudal.getMessage("changeProfession.thatIsCur"));
                        this.soundDeny();
                    }else if(lore.get(0).equals(Feudal.getMessage("changeProfession.lock")) && !player.hasPermission("feudal.commands.admin.character.changeprofession")){
                        player.sendMessage(Feudal.getMessage("landUpdate.lockedProfession"));
                        this.soundDeny();
                    }else{
                        type = Profession.Type.fromString(itemString);
                        this.soundClick();
                        changingInv = true;
                        player.closeInventory();
                        inventory = this.confirm(item);
                        openInventory();
                        changingInv = false;
                    }
                }
            }
        }
    }
}
