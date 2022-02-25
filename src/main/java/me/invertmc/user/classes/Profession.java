package me.invertmc.user.classes;

import me.invertmc.Feudal;
import me.invertmc.api.events.UserLevelUpProfessionEvent;
import me.invertmc.user.User;
import org.bukkit.Bukkit;

public class Profession {
    public enum Type{
        NONE(SocialClass.NONE), FARMER(SocialClass.PEASANT), LOGGER(SocialClass.PEASANT),
        HUNTER(SocialClass.PEASANT), MINER(SocialClass.PEASANT), COOK(SocialClass.PEASANT),
        FISHER(SocialClass.PEASANT), BUILDER(SocialClass.PEASANT), SCRIBE(SocialClass.PEASANT),
        GUARD(SocialClass.COMMONER), ASSASSIN(SocialClass.COMMONER), ALCHEMIST(SocialClass.COMMONER),
        BLACKSMITH(SocialClass.COMMONER), HEALER(SocialClass.COMMONER), MERCHANT(SocialClass.COMMONER),
        SQUIRE(SocialClass.NOBLE), KNIGHT(SocialClass.NOBLE), BARON(SocialClass.NOBLE), KING(SocialClass.NOBLE), SHEPHERD(SocialClass.PEASANT), ARCHBISHOP(SocialClass.NOBLE);

        public SocialClass CLASS;

        private Type(SocialClass _class) {
            CLASS = _class;
        }

        public static Type fromString(String string){
            if(string.equalsIgnoreCase("FARMER")){
                return Type.FARMER;
            }else if(string.equalsIgnoreCase("LOGGER")){
                return Type.LOGGER;
            }else if(string.equalsIgnoreCase("HUNTER")){
                return Type.HUNTER;
            }else if(string.equalsIgnoreCase("MINER")){
                return Type.MINER;
            }else if(string.equalsIgnoreCase("COOK")){
                return Type.COOK;
            }else if(string.equalsIgnoreCase("FISHER")){
                return Type.FISHER;
            }else if(string.equalsIgnoreCase("BUILDER")){
                return Type.BUILDER;
            }else if(string.equalsIgnoreCase("SHEPHERD")){
                return Type.SHEPHERD;
            }else if(string.equalsIgnoreCase("SCRIBE")){
                return Type.SCRIBE;
            }else if(string.equalsIgnoreCase("GUARD")){
                return Type.GUARD;
            }else if(string.equalsIgnoreCase("ASSASSIN")){
                return Type.ASSASSIN;
            }else if(string.equalsIgnoreCase("ALCHEMIST")){
                return Type.ALCHEMIST;
            }else if(string.equalsIgnoreCase("BLACKSMITH")){
                return Type.BLACKSMITH;
            }else if(string.equalsIgnoreCase("HEALER")){
                return Type.HEALER;
            }else if(string.equalsIgnoreCase("MERCHANT")){
                return Type.MERCHANT;
            }else if(string.equalsIgnoreCase("SQUIRE")){
                return Type.SQUIRE;
            }else if(string.equalsIgnoreCase("KNIGHT")){
                return Type.KNIGHT;
            }else if(string.equalsIgnoreCase("BARON")){
                return Type.BARON;
            }else if(string.equalsIgnoreCase("KING")){
                return Type.KING;
            }else if(string.equalsIgnoreCase("ARCHBISHOP")){
                return Type.ARCHBISHOP;
            }else{
                return Type.NONE;
            }
        }

        public String getNameLang(){
            if(this.equals(Type.FARMER)){
                return Feudal.getMessage("changeProfession.farmer");
            }else if(this.equals(Type.LOGGER)){
                return Feudal.getMessage("changeProfession.Logger");
            }else if(this.equals(Type.HUNTER)){
                return Feudal.getMessage("changeProfession.Hunter");
            }else if(this.equals(Type.MINER)){
                return Feudal.getMessage("changeProfession.Miner");
            }else if(this.equals(Type.COOK)){
                return Feudal.getMessage("changeProfession.Cook");
            }else if(this.equals(Type.FISHER)){
                return Feudal.getMessage("changeProfession.Fisher");
            }else if(this.equals(Type.BUILDER)){
                return Feudal.getMessage("changeProfession.Builder");
            }else if(this.equals(Type.SHEPHERD)){
                return Feudal.getMessage("changeProfession.Shepherd");
            }else if(this.equals(Type.SCRIBE)){
                return Feudal.getMessage("changeProfession.Scribe");
            }else if(this.equals(Type.GUARD)){
                return Feudal.getMessage("changeProfession.Guard");
            }else if(this.equals(Type.ASSASSIN)){
                return Feudal.getMessage("changeProfession.Assassin");
            }else if(this.equals(Type.ALCHEMIST)){
                return Feudal.getMessage("changeProfession.Alchemist");
            }else if(this.equals(Type.BLACKSMITH)){
                return Feudal.getMessage("changeProfession.Blacksmith");
            }else if(this.equals(Type.HEALER)){
                return Feudal.getMessage("changeProfession.Healer");
            }else if(this.equals(Type.MERCHANT)){
                return Feudal.getMessage("changeProfession.Merchant");
            }else if(this.equals(Type.SQUIRE)){
                return Feudal.getMessage("changeProfession.Squire");
            }else if(this.equals(Type.KNIGHT)){
                return Feudal.getMessage("changeProfession.Knight");
            }else if(this.equals(Type.BARON)){
                return Feudal.getMessage("changeProfession.Baron");
            }else if(this.equals(Type.KING)){
                return Feudal.getMessage("changeProfession.King");
            }else if(this.equals(Type.ARCHBISHOP)){
                return "\u00a7a\u00a7lArchBishop";
            }else{
                return Feudal.getMessage("changeProfession.None");
            }
        }

        public String getName(){
            if(this.equals(Type.FARMER)){
                return "Farmer";
            }else if(this.equals(Type.LOGGER)){
                return "Logger";
            }else if(this.equals(Type.HUNTER)){
                return "Hunter";
            }else if(this.equals(Type.MINER)){
                return "Miner";
            }else if(this.equals(Type.COOK)){
                return "Cook";
            }else if(this.equals(Type.FISHER)){
                return "Fisher";
            }else if(this.equals(Type.BUILDER)){
                return "Builder";
            }else if(this.equals(Type.SHEPHERD)){
                return "Shepherd";
            }else if(this.equals(Type.SCRIBE)){
                return "Scribe";
            }else if(this.equals(Type.GUARD)){
                return "Guard";
            }else if(this.equals(Type.ASSASSIN)){
                return "Assassin";
            }else if(this.equals(Type.ALCHEMIST)){
                return "Alchemist";
            }else if(this.equals(Type.BLACKSMITH)){
                return "Blacksmith";
            }else if(this.equals(Type.HEALER)){
                return "Healer";
            }else if(this.equals(Type.MERCHANT)){
                return "Merchant";
            }else if(this.equals(Type.SQUIRE)){
                return "Squire";
            }else if(this.equals(Type.KNIGHT)){
                return "Knight";
            }else if(this.equals(Type.BARON)){
                return "Baron";
            }else if(this.equals(Type.KING)){
                return "King";
            }else if(this.equals(Type.ARCHBISHOP)){
                return "ArchBishop";
            }else{
                return "None";
            }
        }

        public double getMoneyXP() {
            return Feudal.getConfiguration().getConfig().getDouble(getName() + ".moneyXP", 0.0);
        }
    }

    private Type type;
    private short level;//NOTE LEVEL CAN NOT BE LARGER THAN 32K
    private int xp;
    private boolean max;
    private User user;

    public Profession(String profession, User user) throws Exception{// type/level/xp
        this.user = user;
        String split[] = profession.split("/");
        if(split.length == 3){
            level = (short) Integer.parseInt(split[1]);
            xp = Integer.parseInt(split[2]);
            type = Type.fromString(split[0]);
            max = checkMax();
        }else{
            throw new Exception("Invalid profession string.");
        }
    }

    public Profession(User user, Type type, int level, int xp){
        this.user = user;
        this.type = type;
        this.level = (short) level;
        this.xp = xp;
        max = checkMax();
    }

    private boolean checkMax() {
        String name = type.getName();
        if(!name.equals("None")){
            if(level >= Feudal.getConfiguration().getConfig().getInt(name + ".MaxLevel")){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    /**
     * This checks if the xp is enough to level up.  Returns -1 no level up, 0 level up, 1 reached max level.
     * @return
     */
    private int checkXP(User user){
        String name = type.getName();
        if(!name.equals("None")){
            int levelXP = xpForLevel(name, level);
            if(xp >= levelXP){
                user.setChange(true);
                xp -= levelXP;
                level++;

                if(level <= Feudal.getConfiguration().getConfig().getInt(name + ".MaxLevel")){
                    Bukkit.getPluginManager().callEvent(new UserLevelUpProfessionEvent(user, this, level));
                }

                if(level >= Feudal.getConfiguration().getConfig().getInt(name + ".MaxLevel")){
                    return 1;
                }else{
                    if(checkXP(user) == 1){
                        return 1;
                    }else{
                        return 0;
                    }
                }
            }else{
                return -1;
            }
        }else{
            return -1;
        }
    }

    private int xpForLevel(String name, double level){
        double j = Feudal.getConfiguration().getConfig().getInt(name + ".MaxLevel");
        double i = Feudal.getConfiguration().getConfig().getInt(name + ".StartXP");
        double k = Feudal.getConfiguration().getConfig().getInt(name + ".EndXP");
        return (int)( (((k-i)/(j-1)) * level) + i );
    }

    @Override
    public String toString(){
        return type.toString() + "/" + level + "/" + xp;
    }

    public Type getType() {
        return type;
    }

    public int addXp(int xp, User user){
        user.setChange(true);
        this.xp += xp;
        return checkXP(user);
    }

    public int getXp() {
        return xp;
    }

	/*public int setXps(int xp) {
		this.xp = xp;
		return checkXP();
	}*/

    public void setType(Type type) {
        user.setChange(true);
        this.type = type;
    }

    public void setLevel(int level) {
        user.setChange(true);
        this.level = (short) level;
    }

    public boolean isMax() {
        return max;
    }

    public void setMax(boolean max) {
        user.setChange(true);
        this.max = max;
    }

    public int getLevel() {
        return level;
    }

    public int getMaxLevel() {
        return Feudal.getConfiguration().getConfig().getInt(type.getName() + ".MaxLevel");
    }

    public static boolean checkProfession(Profession.Type type){
        //FARMER, LOGGER, HUNTER, MINER, COOK, FISHER, BUILDER, SHEPERD, SCRIBE, GUARD, ASSASSIN, ALCHEMIST,
        //BLACKSMITH, HEALER, MERCHANT, SQUIRE, KNIGHT, BARON, KING
        double total = 0;
        total += Feudal.getProfessionData().getConfig().getStringList("FARMER").size();
        total += Feudal.getProfessionData().getConfig().getStringList("LOGGER").size();
        total += Feudal.getProfessionData().getConfig().getStringList("HUNTER").size();
        total += Feudal.getProfessionData().getConfig().getStringList("MINER").size();
        total += Feudal.getProfessionData().getConfig().getStringList("COOK").size();
        total += Feudal.getProfessionData().getConfig().getStringList("FISHER").size();
        total += Feudal.getProfessionData().getConfig().getStringList("BUILDER").size();
        total += Feudal.getProfessionData().getConfig().getStringList("SHEPHERD").size();
        total += Feudal.getProfessionData().getConfig().getStringList("SCRIBE").size();
        total += Feudal.getProfessionData().getConfig().getStringList("GUARD").size();
        total += Feudal.getProfessionData().getConfig().getStringList("ASSASSIN").size();
        total += Feudal.getProfessionData().getConfig().getStringList("ALCHEMIST").size();
        total += Feudal.getProfessionData().getConfig().getStringList("BLACKSMITH").size();
        total += Feudal.getProfessionData().getConfig().getStringList("HEALER").size();
        total += Feudal.getProfessionData().getConfig().getStringList("MERCHANT").size();
        total += Feudal.getProfessionData().getConfig().getStringList("SQUIRE").size();
        total += Feudal.getProfessionData().getConfig().getStringList("KNIGHT").size();
        total += Feudal.getProfessionData().getConfig().getStringList("BARON").size();
        total += Feudal.getProfessionData().getConfig().getStringList("KING").size();
        if(total <= 0){
            if(Feudal.getConfiguration().getConfig().getDouble(type.getName() + ".MaxPercent") == 0){
                return false;
            }else{
                return true;
            }
        }

        total = ((double) Feudal.getProfessionData().getConfig().getStringList(type.toString()).size() / (double) total) * 100;

        if(Feudal.getConfiguration().getConfig().getDouble(type.getName() + ".MaxPercent") > total){
            return true;
        }else{
            return (checkAllProfessions());
        }
    }

    private static boolean checkAllProfessions() {
        double total = 0;
        total += Feudal.getProfessionData().getConfig().getStringList("FARMER").size();
        total += Feudal.getProfessionData().getConfig().getStringList("LOGGER").size();
        total += Feudal.getProfessionData().getConfig().getStringList("HUNTER").size();
        total += Feudal.getProfessionData().getConfig().getStringList("MINER").size();
        total += Feudal.getProfessionData().getConfig().getStringList("COOK").size();
        total += Feudal.getProfessionData().getConfig().getStringList("FISHER").size();
        total += Feudal.getProfessionData().getConfig().getStringList("BUILDER").size();
        total += Feudal.getProfessionData().getConfig().getStringList("SHEPHERD").size();
        total += Feudal.getProfessionData().getConfig().getStringList("SCRIBE").size();
        total += Feudal.getProfessionData().getConfig().getStringList("GUARD").size();
        total += Feudal.getProfessionData().getConfig().getStringList("ASSASSIN").size();
        total += Feudal.getProfessionData().getConfig().getStringList("ALCHEMIST").size();
        total += Feudal.getProfessionData().getConfig().getStringList("BLACKSMITH").size();
        total += Feudal.getProfessionData().getConfig().getStringList("HEALER").size();
        total += Feudal.getProfessionData().getConfig().getStringList("MERCHANT").size();
        total += Feudal.getProfessionData().getConfig().getStringList("SQUIRE").size();
        total += Feudal.getProfessionData().getConfig().getStringList("KNIGHT").size();
        total += Feudal.getProfessionData().getConfig().getStringList("BARON").size();
        total += Feudal.getProfessionData().getConfig().getStringList("KING").size();

        if(Feudal.getConfiguration().getConfig().getDouble(Profession.Type.ALCHEMIST.getName() + ".MaxPercent") > (((double) Feudal.getProfessionData().getConfig().getStringList(Profession.Type.ALCHEMIST.toString()).size() / (double) total) * 100)){
            return false;
        }else if(Feudal.getConfiguration().getConfig().getDouble(Profession.Type.ASSASSIN.getName() + ".MaxPercent") > (((double) Feudal.getProfessionData().getConfig().getStringList(Profession.Type.ASSASSIN.toString()).size() / (double) total) * 100)){
            return false;
        }else if(Feudal.getConfiguration().getConfig().getDouble(Profession.Type.BARON.getName() + ".MaxPercent") > (((double) Feudal.getProfessionData().getConfig().getStringList(Profession.Type.BARON.toString()).size() / (double) total) * 100)){
            return false;
        }else if(Feudal.getConfiguration().getConfig().getDouble(Profession.Type.BLACKSMITH.getName() + ".MaxPercent") > (((double) Feudal.getProfessionData().getConfig().getStringList(Profession.Type.BLACKSMITH.toString()).size() / (double) total) * 100)){
            return false;
        }else if(Feudal.getConfiguration().getConfig().getDouble(Profession.Type.BUILDER.getName() + ".MaxPercent") > (((double) Feudal.getProfessionData().getConfig().getStringList(Profession.Type.BUILDER.toString()).size() / (double) total) * 100)){
            return false;
        }else if(Feudal.getConfiguration().getConfig().getDouble(Profession.Type.COOK.getName() + ".MaxPercent") > (((double) Feudal.getProfessionData().getConfig().getStringList(Profession.Type.COOK.toString()).size() / (double) total) * 100)){
            return false;
        }else if(Feudal.getConfiguration().getConfig().getDouble(Profession.Type.FARMER.getName() + ".MaxPercent") > (((double) Feudal.getProfessionData().getConfig().getStringList(Profession.Type.FARMER.toString()).size() / (double) total) * 100)){
            return false;
        }else if(Feudal.getConfiguration().getConfig().getDouble(Profession.Type.FISHER.getName() + ".MaxPercent") > (((double) Feudal.getProfessionData().getConfig().getStringList(Profession.Type.FISHER.toString()).size() / (double) total) * 100)){
            return false;
        }else if(Feudal.getConfiguration().getConfig().getDouble(Profession.Type.GUARD.getName() + ".MaxPercent") > (((double) Feudal.getProfessionData().getConfig().getStringList(Profession.Type.GUARD.toString()).size() / (double) total) * 100)){
            return false;
        }else if(Feudal.getConfiguration().getConfig().getDouble(Profession.Type.HEALER.getName() + ".MaxPercent") > (((double) Feudal.getProfessionData().getConfig().getStringList(Profession.Type.HEALER.toString()).size() / (double) total) * 100)){
            return false;
        }else if(Feudal.getConfiguration().getConfig().getDouble(Profession.Type.HUNTER.getName() + ".MaxPercent") > (((double) Feudal.getProfessionData().getConfig().getStringList(Profession.Type.HUNTER.toString()).size() / (double) total) * 100)){
            return false;
        }else if(Feudal.getConfiguration().getConfig().getDouble(Profession.Type.KING.getName() + ".MaxPercent") > (((double) Feudal.getProfessionData().getConfig().getStringList(Profession.Type.KING.toString()).size() / (double) total) * 100)){
            return false;
        }else if(Feudal.getConfiguration().getConfig().getDouble(Profession.Type.KNIGHT.getName() + ".MaxPercent") > (((double) Feudal.getProfessionData().getConfig().getStringList(Profession.Type.KNIGHT.toString()).size() / (double) total) * 100)){
            return false;
        }else if(Feudal.getConfiguration().getConfig().getDouble(Profession.Type.LOGGER.getName() + ".MaxPercent") > (((double) Feudal.getProfessionData().getConfig().getStringList(Profession.Type.LOGGER.toString()).size() / (double) total) * 100)){
            return false;
        }else if(Feudal.getConfiguration().getConfig().getDouble(Profession.Type.MERCHANT.getName() + ".MaxPercent") > (((double) Feudal.getProfessionData().getConfig().getStringList(Profession.Type.MERCHANT.toString()).size() / (double) total) * 100)){
            return false;
        }else if(Feudal.getConfiguration().getConfig().getDouble(Profession.Type.MINER.getName() + ".MaxPercent") > (((double) Feudal.getProfessionData().getConfig().getStringList(Profession.Type.MINER.toString()).size() / (double) total) * 100)){
            return false;
        }else if(Feudal.getConfiguration().getConfig().getDouble(Profession.Type.SCRIBE.getName() + ".MaxPercent") > (((double) Feudal.getProfessionData().getConfig().getStringList(Profession.Type.SCRIBE.toString()).size() / (double) total) * 100)){
            return false;
        }else if(Feudal.getConfiguration().getConfig().getDouble(Profession.Type.SHEPHERD.getName() + ".MaxPercent") > (((double) Feudal.getProfessionData().getConfig().getStringList(Profession.Type.SHEPHERD.toString()).size() / (double) total) * 100)){
            return false;
        }else if(Feudal.getConfiguration().getConfig().getDouble(Profession.Type.SQUIRE.getName() + ".MaxPercent") > (((double) Feudal.getProfessionData().getConfig().getStringList(Profession.Type.SQUIRE.toString()).size() / (double) total) * 100)){
            return false;
        }else{
            return true;
        }
    }

    public int getXPToNextLevel() {
        String name = type.getName();
        if(!name.equals("None")){
            int levelXP = this.xpForLevel(name, level);
            return levelXP - xp;
        }else{
            return 0;
        }
    }

    public static double getTaxPercent(Type type) {
        return Feudal.getConfiguration().getConfig().getDouble(type.getName() + ".taxPercent");
    }

    public static int getLand(Type type) {
        return Feudal.getConfiguration().getConfig().getInt(type.getName() + ".land");
    }

    public void resetXP() {
        if(user != null) {
            user.setChange(true);
        }
        xp = 0;
    }
}
