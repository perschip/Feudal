package me.invertmc.utils;

import java.util.ArrayList;
import java.util.List;

import me.invertmc.Feudal;
import me.invertmc.kingdoms.Kingdom;
import me.invertmc.kingdoms.Rank;
import me.invertmc.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Utils {

    public static boolean isFeudalCommand(String command) {
        List<String> commands = Feudal.getConfiguration().getConfig().getStringList("customCommands");
        commands.add("feudal");
        commands.add("f");
        commands.add("k");
        commands.add("kingdom");

        for(String c : commands) {
            if(command.equalsIgnoreCase("/" + c) || command.toLowerCase().startsWith("/"+c.toLowerCase()+" ")){
                return true;
            }
        }

        return false;
    }

    public static void loadWorld(String wName) {
        if(wName != null && !wName.equals("")){
            Bukkit.getServer().createWorld(new WorldCreator(wName));
        }
    }

    public static boolean isWorldLoaded(String wName) {
        for(World w : Bukkit.getWorlds()){
            if(w.getName().equalsIgnoreCase(wName)){
                return true;
            }
        }
        return false;
    }

    public static String essentialsChatFormat(String format, Player p) {
        try{
            //placeholder: %feudal_kingdom%
            if(p == null){
                return format;
            }
            User u = Feudal.getUser(p.getUniqueId().toString());
            if(u == null){
                return format;
            }
            Kingdom k = null;
            if(!u.getKingdomUUID().equals("")){
                k = Feudal.getKingdom(u.getKingdomUUID());
            }

            if(format.contains("feudal_kingdom")){
                if(k != null){
                    format = format.replace("feudal_kingdom", k.getName());
                }
            }else if(format.contains("feudal_reputation")){
                format = format.replace("feudal_reputation", u.getReputation()+"");
            }else if(format.contains("feudal_rank")){
                if(k != null){
                    if(k.isMember(u.getUUID())){
                        Rank r = k.getMembers().get(u.getUUID());
                        if(r.equals(Rank.GUEST)){
                            format = format.replace("feudal_rank", "G");
                        }else if(r.equals(Rank.MEMBER)){
                            format = format.replace("feudal_rank", "M");
                        }else if(r.equals(Rank.EXECUTIVE)){
                            format = format.replace("feudal_rank", "E");
                        }else{
                            format = format.replace("feudal_rank", "L");
                        }
                    }
                }
            }else if(format.contains("feudal_profession")){
                if(u.getProfession() != null && !u.getProfession().getType().equals(Profession.Type.NONE)){
                    format = format.replace("feudal_profession", u.getProfession().getType().getNameLang());
                }
            }else if(format.contains("feudal_luck")){
                if(u.getLuck() != null){
                    format = format.replace("feudal_luck", u.getLuck().getLevel() + "");
                }
            }else if(format.contains("feudal_strength")){
                if(u.getStrength() != null){
                    format = format.replace("feudal_strength", u.getStrength().getLevel() + "");
                }
            }else if(format.contains("feudal_speed")){
                if(u.getSpeed() != null){
                    format = format.replace("feudal_speed", u.getSpeed().getLevel() + "");
                }
            }else if(format.contains("feudal_toughness")){
                if(u.getToughness() != null){
                    format = format.replace("feudal_toughness", u.getToughness().getLevel() + "");
                }
            }else if(format.contains("feudal_stamina")){
                if(u.getStamina() != null){
                    format = format.replace("feudal_stamina", u.getStamina().getLevel() + "");
                }
            }else if(format.contains("feudal_profession_level")){
                if(u.getProfession() != null){
                    format = format.replace("feudal_profession_level", u.getProfession().getLevel() + "");
                }
            }else if(format.contains("feudal_treasury")){
                if(k != null){
                    format = format.replace("feudal_treasury", k.getTreasury()+"");
                }
            }else if(format.contains("feudal_kingdom_less")){
                if(k != null){
                    format = format.replace("feudal_kingdom_less", k.getName());
                }else{
                    format = format.replace("feudal_kingdom_less", "Kingdomless");
                }
            }else if(format.equalsIgnoreCase("feudal_kingdom_members")){
                if(k != null){
                    format = format.replace("feudal_kingdom_members", k.getMembersString());
                }
            }

            return format;
        }catch(Exception e){
            ErrorManager.error(78, e);
            return null;
        }
    }

    public static boolean isType(EntityType type, String name) {
        try {
            if(type.equals(EntityType.valueOf(name.toUpperCase()))) {
                return true;
            }else {
                return false;
            }
        }catch(Exception e) {
            return false;
        }
    }

    public static boolean isMaterial(Material material, String type) {
        try {
            if(material != null && material.equals(Material.valueOf(type))) {
                return true;
            }
            return false;
        }catch(Exception e) {
            return false;
        }
    }

    public static List<Player> getOnlinePlayers(){
        List<Player> players = new ArrayList<Player>();
        for(Player player : Bukkit.getOnlinePlayers()) {
            players.add(player);
        }
        return players;
    }

   /* public static double getHealth(LivingEntity entity) {
        if(Feudal.getVersion().equals("1.7")) {
            return Utils1_7.getHealth(entity);
        }else {
            return UtilsAbove1_7.getHealth(entity);
        }
    }

    public static double getMaxHealth(LivingEntity entity) {
        if(Feudal.getVersion().equals("1.7")) {
            return Utils1_7.getMaxHealth(entity);
        }else {
            return UtilsAbove1_7.getMaxHealth(entity);
        }
    }

    public static PotionEffect getHiddenPotionEffect(PotionEffectType type, int duration, int level) {
        if(Feudal.getVersion().equals("1.7")) {
            return Utils1_7.getHiddenPotionEffect(type, duration, level);
        }else {
            return UtilsAbove1_7.getHiddenPotionEffect(type, duration, level);
        }
    }*/

    public static int countMatches(String shape, String string) {
            return UtilsAbove1_7.countMatches(shape, string);

    }

 /*   public static boolean isShulkerBox(Material type) {
        if(type != null && type.name().toUpperCase().endsWith("_SHULKER_BOX")) {
            return true;
        }else {
            return false;
        }
    }

    public static ItemStack randomItem() {
        Randomizer<ItemStack> r = new Randomizer<ItemStack>(Feudal.RANDOM);//Out of 1000
        r.add(new ItemStack(Material.WOOD, 3), 50);
        r.add(new ItemStack(Material.STONE, 3), 50);
        r.add(new ItemStack(Material.GLASS, 3), 50);
        r.add(new ItemStack(Material.COAL, 1), 50);
        r.add(new ItemStack(Material.APPLE, 1), 50);
        r.add(new ItemStack(Material.BLAZE_ROD, 1), 50);
        r.add(new ItemStack(Material.BLAZE_POWDER, 4), 50);
        r.add(new ItemStack(Material.NETHER_WARTS, 3), 50);
        r.add(new ItemStack(Material.ARROW, 5), 50);
        r.add(new ItemStack(Material.CARROT_ITEM, 1), 50);
        r.add(new ItemStack(Material.MELON_SEEDS, 10), 50);
        r.add(new ItemStack(Material.CAKE, 1), 50);
        r.add(new ItemStack(Material.LOG, 2), 50);
        r.add(new ItemStack(Material.WEB, 1), 50);
        r.add(new ItemStack(Material.CLAY_BALL, 5), 50);
        r.add(new ItemStack(Material.IRON_ORE, 1), 50);
        r.add(new ItemStack(Material.REDSTONE, 6), 50);
        r.add(new ItemStack(Material.BUCKET, 1), 50);
        r.add(new ItemStack(Material.GOLD_ORE, 1), 50);
        r.add(new ItemStack(Material.IRON_ORE, 7), 10);
        r.add(new ItemStack(Material.DIAMOND_HOE, 1), 10);
        r.add(new ItemStack(Material.DIAMOND_PICKAXE, 1), 10);
        r.add(new ItemStack(Material.CHEST, 20), 10);
        r.add(new ItemStack(Material.ANVIL, 1), 5);
        r.add(new ItemStack(Material.DIAMOND_BLOCK, 1), 1);
        r.add(new ItemStack(Material.EMERALD_BLOCK, 1), 1);
        r.add(new ItemStack(Material.IRON_ORE, 50), 1);
        r.add(new ItemStack(Material.DIAMOND, 1), 1);
        r.add(new ItemStack(Material.EMERALD, 1), 1);

        return r.randomize();
    }*/
}