package me.invertmc.managergui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cryptomorin.xseries.XMaterial;
import me.invertmc.Feudal;
import me.invertmc.user.User;
import me.invertmc.user.classes.Profession;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;


public class InviteMembers extends InventoryGui2 {

    private int page = 0;
    private int maxPage = 0;
    private List<User> users;
    private final InventoryGui2 parent;

    public InviteMembers(InventoryGui2 parent, Player player) throws Exception {
        super(Bukkit.createInventory(null, getInventorySize(54), lim(Feudal.getMessage("m.title5"))), player);

        this.parent = parent;
        this.users = getUserList();
        this.maxPage = getMaxPage();

        createInventory();
        openInventory();
    }

    private void createInventory() {
        this.clear();
        this.getInventory().clear();
        this.setItem("BACK", 49, createItem(XMaterial.WHITE_BED.parseMaterial(), 1, 0, Feudal.getMessage("m.back"), Arrays.asList(new String[] {Feudal.getMessage("m.back2"), "�a", Feudal.getMessage("m.page") + (page+1)})));

        if(page > 0) {
            //PAGE BACK
            this.setItem("PAGEBACK", 45, createItem(XMaterial.RED_WOOL.parseMaterial(), 1, 14, Feudal.getMessage("m.pageBack"), Arrays.asList(new String[] {Feudal.getMessage("m.pageBack2")})));
        }else if(page < maxPage) {
            //PAGE FOWARD
            this.setItem("PAGENEXT", 53, createItem(XMaterial.GREEN_WOOL.parseMaterial(), 1, 5, Feudal.getMessage("m.pageForward"), Arrays.asList(new String[] {Feudal.getMessage("m.pageForward2")})));
        }

        for(int i = 0, m = page * 36; i < 36 && m < users.size(); i++, m++) {
            //SHOW USERS
            final User user = users.get(m);

            if(user.getPlayer() != null && user.getPlayer().isOnline()) {
                final List<String> lore = new ArrayList<>();
                lore.add(Feudal.getMessage("m.reputation") + user.getReputation());
                lore.add(Feudal.getMessage("m.profession") + user.getProfession().getType().getName());
                lore.add(Feudal.getMessage("m.strength") + user.getStrength().getLevel() + " / " + user.getStrength().getMaxLevel());
                lore.add(Feudal.getMessage("m.toughness") + user.getToughness().getLevel() + " / " + user.getToughness().getMaxLevel());
                lore.add(Feudal.getMessage("m.speed") + user.getSpeed().getLevel() + " / " + user.getSpeed().getMaxLevel());
                lore.add(Feudal.getMessage("m.stamina") + user.getStamina().getLevel() + " / " + user.getStamina().getMaxLevel());
                lore.add(Feudal.getMessage("m.luck") + user.getLuck().getLevel() + " / " + user.getLuck().getMaxLevel());

                final ItemStack item = createItem(XMaterial.PLAYER_HEAD.parseMaterial(), 1, 3, "�d" + user.getPlayer().getName(), lore);

                try {
                    final SkullMeta it = (SkullMeta) item.getItemMeta();
                    it.setOwner(user.getPlayer().getName());
                }catch(final Exception e) {}

                this.setItem("I" + m, i, item);
            }
        }
    }

    private void openInventory() {
        getPlayer().openInventory(getInventory());
    }

    @Override
    public void click(String id) {
        if(id != null) {
            if(id.equals("BACK")) {
                getPlayer().closeInventory();
                soundClick(getPlayer());
                parent.reload();
            }else if(id.equals("PAGEBACK")) {
                if(page > 0) {
                    page--;
                    soundClick(getPlayer());
                    createInventory();
                    getPlayer().updateInventory();
                }
            }else if(id.equals("PAGENEXT")) {
                if(page < maxPage) {
                    page++;
                    soundClick(getPlayer());
                    createInventory();
                    getPlayer().updateInventory();
                }
            }else if(id.startsWith("I")) {
                getPlayer().closeInventory();

                try {
                    final User user = users.get(Integer.parseInt(id.substring(1)));
                    if(user.getPlayer() != null) {
                        soundClick(getPlayer());
                        getPlayer().performCommand("k invite " + user.getPlayer().getName());
                    }else {
                        soundDeny(getPlayer());
                    }
                }catch(final Exception e) {
                    e.printStackTrace();
                    soundDeny(getPlayer());
                }
            }
        }
    }

    private List<User> getUserList() {
        final List<User> users = new ArrayList<>();

        for(final User user : Feudal.getOnlinePlayers().values()) {
            if(user != null && user.getKingdomUUID() != null &&
                    user.getKingdomUUID().isEmpty() && user.getProfession() != null && user.getProfession().getType() != Profession.Type.NONE) {
                users.add(user);
            }
        }

        return users;
    }

    private int getMaxPage() {
        return users.size() / 36;
        //return (int) Math.ceil(((double)users.size()) / 36.0);
    }

    @Override
    public void close() {}

    @Override
    public void reinitiate() {
        this.users = getUserList();
        this.maxPage = getMaxPage();
        page = 0;
        createInventory();
        openInventory();
    }

}
