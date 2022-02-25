package me.invertmc.user.attributes;

import me.invertmc.Feudal;
import me.invertmc.api.events.UserLevelUpAttributeEvent;
import me.invertmc.user.User;
import org.bukkit.Bukkit;

public class Attributes {
    private Attribute type;
    private User u;
    private short xp;//CAN NOT BE LARGER THAN 32000
    private int level;
    private boolean max = false;

    /**
     * Creates new attributes instance
     * @param type
     * @param xp
     * @param level
     * @param u
     */
    public Attributes(Attribute type, int xp, int level, User u){
        this.type = type;
        this.xp = (short) xp;
        this.level = level;
        if(level >= Feudal.getConfiguration().getConfig().getInt(this.getName() + ".MaxLevel")){
            max = true;
        }
        this.u = u;
    }

    public boolean isMax(){
        return max;
    }

    public void setMax(boolean max){
        u.setChange(true);
        this.max = max;
    }

    /**
     * This checks if the xp is enough to level up.  Returns -1 no level up, 0 level up, 1 reached max level.
     * @return
     */
    private int checkXP(){
        String name = this.getName();
        if(!name.equals("None")){
            int levelXP = xpForLevel(name, level);
            if(xp >= levelXP){
                u.setChange(true);
                xp -= levelXP;
                if(!Feudal.getConfiguration().getConfig().getBoolean("attributeCap.enable") || u.getTotalAtt() < Feudal.getConfiguration().getConfig().getInt("attributeCap.cap")){
                    level++;

                    if(level <= Feudal.getConfiguration().getConfig().getInt(name + ".MaxLevel"))
                        Bukkit.getPluginManager().callEvent(new UserLevelUpAttributeEvent(u, type, level));

                    if(level >= Feudal.getConfiguration().getConfig().getInt(name + ".MaxLevel")){
                        level = Feudal.getConfiguration().getConfig().getInt(name + ".MaxLevel");
                        return 1;
                    }else{
                        if(checkXP() == 1){
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
        }else{
            return -1;
        }
    }

    private int xpForLevel(String name, double level){
        double j = Feudal.getConfiguration().getConfig().getDouble(name + ".MaxLevel");
        double i = Feudal.getConfiguration().getConfig().getDouble(name + ".StartXP");
        double k = Feudal.getConfiguration().getConfig().getDouble(name + ".EndXP");
        return (int)( (((k-i)/(j-1)) * level) + i );
    }

    public int getXPToNext(){
        String name = this.getName();
        if(!name.equals("None")){
            int levelXP = xpForLevel(name, level);
            return levelXP - xp;
        }else{
            return 0;
        }
    }

    /**
     * This gets the type name.
     * @return
     */
    public String getName() {
        if(type.equals(Attribute.STRENGTH)){
            return "Strength";
        }else if(type.equals(Attribute.TOUGHNESS)){
            return "Toughness";
        }else if(type.equals(Attribute.SPEED)){
            return "Speed";
        }else if(type.equals(Attribute.STAMINA)){
            return "Stamina";
        }else{
            return "Luck";
        }
    }

    public int addXp(int xp){
        u.setChange(true);
        this.xp += xp;
        return checkXP();
    }

    public int getXp() {
        return xp;
    }

    /**
     * Sets xp.  Returns 0 if leveled up.  Returns -1 if not leveled up.  Returns 1 if hit max level.
     * @param xp
     * @return
     */
    public int setXp(int xp) {
        u.setChange(true);
        this.xp = (short) xp;
        return checkXP();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        u.setChange(true);
        this.level = level;
    }

    public Attribute getType() {
        return type;
    }

    /**
     * Gets the damage multiplier based on a level.  Make sure the level is greater than or equal to zero.  The level should be equal to or below the max. Levels start at 0.
     * @param level
     * @return
     */
    public static float getStrengthDamageEffect(int level) {
        double min = Feudal.getConfiguration().getConfig().getDouble("Strength.MinDamageIncrease");
        return (float) (((level * (Feudal.getConfiguration().getConfig().getDouble("Strength.MaxDamageIncrease") - min)) / Feudal.getConfiguration().getConfig().getInt("Strength.MaxLevel")) + min);
    }

    /**
     * Gets the haste effect based on a level.  Note: When adding the effect subtract one if it is greater than 0. If not greater than 0 do not add effect.
     * Make sure the level is greater than or equal to zero.  The level should be equal to or below the max. Levels start at 0.
     * @param level
     * @return
     */
    public static int getStrengthHasteEffect(int level) {//does not use config for haste limits because it is not wide enought
        return (int) Math.round((level * (2.0)) / Feudal.getConfiguration().getConfig().getInt("Strength.MaxLevel"));
    }

    /**
     * Gets the toughness regain rate based on a level. Make sure the level is greater than or equal to zero.  The level should be equal to or below the max. Levels start at 0.
     * @param level
     * @return
     */
    public static float getToughnessRegenRate(int level) {
        double min = Feudal.getConfiguration().getConfig().getDouble("Toughness.MinRegenRate");
        return (float) (((level * (Feudal.getConfiguration().getConfig().getDouble("Toughness.MaxRegenRate") - min)) / Feudal.getConfiguration().getConfig().getInt("Toughness.MaxLevel")) + min);
    }

    /**
     * Get the toughness health effect based on a level.
     * Make sure the level is greater than or equal to zero.  The level should be equal to or below the max. Levels start at 0.
     * @param level
     * @return
     */
    public static float getToughnessHealthEffect(int level) {
        double min = Feudal.getConfiguration().getConfig().getDouble("Toughness.MinHealth");
        return (float) (((level * (Feudal.getConfiguration().getConfig().getDouble("Toughness.MaxHealth") - min)) / Feudal.getConfiguration().getConfig().getInt("Toughness.MaxLevel")) + min);
    }

    /**
     * Gets a speed effect based on a level. Make sure the level is greater than or equal to zero.  The level should be equal to or below the max. Levels start at 0.
     * @param level
     * @return
     */
    public static float getSpeedEffect(int level) {
        double min = Feudal.getConfiguration().getConfig().getDouble("Speed.Min");
        return (float) (((level * (Feudal.getConfiguration().getConfig().getDouble("Speed.Max") - min)) / Feudal.getConfiguration().getConfig().getInt("Speed.MaxLevel")) + min);
    }

    public static float getSpeedEffectActual(int level) {
        double min = Feudal.getConfiguration().getConfig().getDouble("Speed.Min");
        float f = (float) (((level * (Feudal.getConfiguration().getConfig().getDouble("Speed.Max") - min)) / Feudal.getConfiguration().getConfig().getInt("Speed.MaxLevel")) + min);
        if(f > 10){
            return 0.2F;
        }else if(f < 0){
            return 0.2F;
        }else if(f < 0.0001){
            return 0.0001F;
        }

        float[] floats = new float[]{0.0001F, 0.2F, 0.2888889F, 0.37777779F, 0.4666667F, 0.5555556F, 0.64444447F, 0.73333335F, 0.82222223F, 0.9111111F, 1};
        if(((int) f) == f){
            return floats[(int) f];
        }else{
            f = floats[(int)f] + ((floats[((int)f)+1]-floats[(int)f]) * ((f-((int) f))/((((int)f)+1)-((int)f))));
        }

        if(f < 0.0001){
            return 0.0001F;
        }else if(f > 1){
            return 1;
        }else{
            return f;
        }
    }

    /**
     * Gets a stamina effect rate based on a level. Make sure the level is greater than or equal to zero.  The level should be equal to or below the max. Levels start at 0.
     * @param level
     * @return
     */
    public static float getStaminaEffect(int level) {
        double min = Feudal.getConfiguration().getConfig().getDouble("Stamina.MinRate");
        return (float) (((level * (Feudal.getConfiguration().getConfig().getDouble("Stamina.MaxRate") - min)) / Feudal.getConfiguration().getConfig().getInt("Stamina.MaxLevel")) + min);
    }

    /**
     * Gets luck percent in decimal form based on a level. Make sure the level is greater than or equal to zero.  The level should be equal to or below the max. Levels start at 0.
     * @param level
     * @return
     */
    public static float getLuck(int level) {
        double min = Feudal.getConfiguration().getConfig().getDouble("Luck.MinPercent");
        return (float) ((((level * (Feudal.getConfiguration().getConfig().getDouble("Luck.MaxPercent") - min)) / Feudal.getConfiguration().getConfig().getInt("Luck.MaxLevel")) + min) / 100);
    }

    public int getMaxLevel() {
        return Feudal.getConfiguration().getConfig().getInt(this.getName() + ".MaxLevel");
    }

    public void resetXP() {
        u.setChange(true);
        xp = 0;
    }

    public static float getStaminaGoodPotionMultiplier(int level) {
        double min = Feudal.getConfiguration().getConfig().getDouble("Stamina.minGoodPotionMultiplier");
        return (float) (((level * (Feudal.getConfiguration().getConfig().getDouble("Stamina.maxGoodPotionMultiplier") - min)) / Feudal.getConfiguration().getConfig().getInt("Stamina.MaxLevel")) + min);
    }

    public static float getStaminaBadPotionMultiplier(int level) {
        double min = Feudal.getConfiguration().getConfig().getDouble("Stamina.minBadPotionMultiplier");
        return (float) (((level * (Feudal.getConfiguration().getConfig().getDouble("Stamina.maxBadPotionMultiplier") - min)) / Feudal.getConfiguration().getConfig().getInt("Stamina.MaxLevel")) + min);
    }

    public static float getStaminaRemove(int level) {
        double min = Feudal.getConfiguration().getConfig().getDouble("Stamina.noStaminaDamageMaxHits");
        return (float) (((level * (Feudal.getConfiguration().getConfig().getDouble("Stamina.fullStaminaDamageMaxHits") - min)) / Feudal.getConfiguration().getConfig().getInt("Stamina.MaxLevel")) + min);
    }
}