package me.invertmc.configs;

import com.cryptomorin.xseries.XMaterial;
import me.invertmc.Feudal;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainConfig {
    public static boolean config() throws Exception{
        boolean load = true;
        Feudal.setConfiguration(new Configuration(new File(Feudal.getPluginFolder(), "config.yml")));
        try {
            if(Feudal.getConfiguration().loadConfig()){
                createConfigDefaults();
            }else{
                updateConfigDefaults();
            }
        } catch (final Exception e) {
            load = false;

            Feudal.warn("Failed to load config: " + Feudal.getConfiguration().getFile().getAbsolutePath() + ".  Please check to make sure the config does not have any syntax errors.");
            try {
                Feudal.getConfiguration().broke();
            } catch (final Exception e1) {

                Feudal.error("FAILED TO SAVE BROKEN BACKUP OF CONFIG: " + Feudal.getConfiguration().getFile().getAbsolutePath());
                throw e1;			}
            try{
                Feudal.getConfiguration().loadConfig();
                createConfigDefaults();
            }catch(final Exception e1){
                throw e1;			}

            throw e;
        }
        try {
            Feudal.getConfiguration().save();
        } catch (final Exception e) {
            load = false;

            Feudal.error("Failed to save config: " + Feudal.getConfiguration().getFile().getAbsolutePath());
            throw e;
        }
        return load;
    }

    private static void updateConfigDefaults() {
        if(!Feudal.getConfiguration().getConfig().contains("Stamina.maxGoodPotionMultiplier")){
            Feudal.getConfiguration().getConfig().set("Stamina.maxGoodPotionMultiplier", 2.0);
            Feudal.getConfiguration().getConfig().set("Stamina.minGoodPotionMultiplier", 1.0);
            Feudal.getConfiguration().getConfig().set("Stamina.maxBadPotionMultiplier", 0.5);
            Feudal.getConfiguration().getConfig().set("Stamina.minBadPotionMultiplier", 1.0);
            Feudal.getConfiguration().getConfig().set("Stamina.defaultDamage", 1.0);
            Feudal.getConfiguration().getConfig().set("Stamina.minDamage", 0.5);
            Feudal.getConfiguration().getConfig().set("Stamina.damageRegen", 0.01);
            Feudal.getConfiguration().getConfig().set("Stamina.damageRegenTimerTicks", 100);
            Feudal.getConfiguration().getConfig().set("Stamina.noStaminaDamageMaxHits", 15);
            Feudal.getConfiguration().getConfig().set("Stamina.fullStaminaDamageMaxHits", 200);
        }
        if(!Feudal.getConfiguration().getConfig().contains("Assassin.randomTracker")){
            Feudal.getConfiguration().getConfig().set("Assassin.randomTracker", true);
            Feudal.getConfiguration().getConfig().set("Assassin.randomNonassassin", true);
        }
        if(!Feudal.getConfiguration().getConfig().contains("kingdom.challenges.winban")){
            Feudal.getConfiguration().getConfig().set("kingdom.challenges.winban", true);
        }
        if(!Feudal.getConfiguration().getConfig().contains("protectSpawners")){
            Feudal.getConfiguration().getConfig().set("protectSpawners", true);
        }
        if(!Feudal.getConfiguration().getConfig().contains("reputation.playerKill")){
            Feudal.getConfiguration().getConfig().set("reputation.playerKill", 1);
            Feudal.getConfiguration().getConfig().set("reputation.playerDeath", -1);
        }
        if(!Feudal.getConfiguration().getConfig().contains("spar.timeoutseconds")){
            Feudal.getConfiguration().getConfig().set("spar.timeoutseconds", 60);
            Feudal.getConfiguration().getConfig().set("spar.maxDistance", 50.0);
            Feudal.getConfiguration().getConfig().set("spar.percentHealthWin", 50.0);
        }
        if(!Feudal.getConfiguration().getConfig().contains("redistribute.pointsPer")){
            Feudal.getConfiguration().getConfig().set("redistribute.pointsPer", 10);
        }
        if(!Feudal.getConfiguration().getConfig().contains("kingdom.challenges.forceScoreboards")){
            Feudal.getConfiguration().getConfig().set("kingdom.challenges.forceScoreboards", true);
            Feudal.getConfiguration().getConfig().set("kingdom.challenges.enableScoreboards", true);
        }
        if(!Feudal.getConfiguration().getConfig().contains("setup.requireCmdExceptions")){
            final ArrayList<String> exceptions = new ArrayList<>();
            exceptions.add("/register");
            exceptions.add("/login");
            exceptions.add("/pass");
            Feudal.getConfiguration().getConfig().set("setup.requireCmdExceptions", false);
        }
        if(!Feudal.getConfiguration().getConfig().contains("bannedWorlds")){
            final ArrayList<String> bannedWorlds = new ArrayList<>();
            bannedWorlds.add("nonFeudalWorld");
            bannedWorlds.add("nonFeudalWorld_nether");
            bannedWorlds.add("nonFeudalWorld_end");
            Feudal.getConfiguration().getConfig().set("bannedWorlds", bannedWorlds);
            final ArrayList<String> noClaimWorlds = new ArrayList<>();
            noClaimWorlds.add("nonClaimWorld");
            noClaimWorlds.add("nonClaimWorld_nether");
            noClaimWorlds.add("nonClaimWorld_end");
            Feudal.getConfiguration().getConfig().set("noClaimWorlds", noClaimWorlds);
        }
        if(!Feudal.getConfiguration().getConfig().contains("Farmer.permissions")){
            Feudal.getConfiguration().getConfig().set("Farmer.permissions", Arrays.asList(new String[]{"list.of.permissions"}));
            Feudal.getConfiguration().getConfig().set("Logger.permissions", Arrays.asList(new String[]{"list.of.permissions"}));
            Feudal.getConfiguration().getConfig().set("Hunter.permissions", Arrays.asList(new String[]{"list.of.permissions"}));
            Feudal.getConfiguration().getConfig().set("Miner.permissions", Arrays.asList(new String[]{"list.of.permissions"}));
            Feudal.getConfiguration().getConfig().set("Cook.permissions", Arrays.asList(new String[]{"list.of.permissions"}));
            Feudal.getConfiguration().getConfig().set("Fisher.permissions", Arrays.asList(new String[]{"list.of.permissions"}));
            Feudal.getConfiguration().getConfig().set("Builder.permissions", Arrays.asList(new String[]{"list.of.permissions"}));
            Feudal.getConfiguration().getConfig().set("Shepherd.permissions", Arrays.asList(new String[]{"list.of.permissions"}));
            Feudal.getConfiguration().getConfig().set("Scribe.permissions", Arrays.asList(new String[]{"list.of.permissions"}));
            Feudal.getConfiguration().getConfig().set("Guard.permissions", Arrays.asList(new String[]{"list.of.permissions"}));
            Feudal.getConfiguration().getConfig().set("Assassin.permissions", Arrays.asList(new String[]{"list.of.permissions"}));
            Feudal.getConfiguration().getConfig().set("Alchemist.permissions", Arrays.asList(new String[]{"list.of.permissions"}));
            Feudal.getConfiguration().getConfig().set("Blacksmith.permissions", Arrays.asList(new String[]{"list.of.permissions"}));
            Feudal.getConfiguration().getConfig().set("Healer.permissions", Arrays.asList(new String[]{"list.of.permissions"}));
            Feudal.getConfiguration().getConfig().set("Merchant.permissions", Arrays.asList(new String[]{"list.of.permissions"}));
            Feudal.getConfiguration().getConfig().set("Squire.permissions", Arrays.asList(new String[]{"list.of.permissions"}));
            Feudal.getConfiguration().getConfig().set("Knight.permissions", Arrays.asList(new String[]{"list.of.permissions"}));
            Feudal.getConfiguration().getConfig().set("Baron.permissions", Arrays.asList(new String[]{"list.of.permissions"}));
            Feudal.getConfiguration().getConfig().set("King.permissions", Arrays.asList(new String[]{"list.of.permissions"}));
        }
        if(!Feudal.getConfiguration().getConfig().contains("kingdom.land.sendtitles")){
            Feudal.getConfiguration().getConfig().set("kingdom.land.sendtitles", true);
        }
        if(!Feudal.getConfiguration().getConfig().contains("kingdom.challenges.keepInventory")){
            Feudal.getConfiguration().getConfig().set("kingdom.challenges.keepInventory", true);
            Feudal.getConfiguration().getConfig().set("kingdom.keepInventoryOnLand", true);
        }

        if (!Feudal.getConfiguration().getConfig().contains("forceUsePoints")){
            Feudal.getConfiguration().getConfig().set("forceUseConfig", false);
        }

        if(!Feudal.getConfiguration().getConfig().contains("goldcarrot.nightvision")){
            Feudal.getConfiguration().getConfig().set("goldcarrot.nightvision", true);
            Feudal.getConfiguration().getConfig().set("goldcarrot.nightvisionticks", 2400);
            Feudal.getConfiguration().getConfig().set("goldcarrot.speed", true);
            Feudal.getConfiguration().getConfig().set("goldcarrot.speedticks", 400);
            Feudal.getConfiguration().getConfig().set("goldcarrot.speedlevel", 0);
            Feudal.getConfiguration().getConfig().set("goldcarrot.increasehealth", true);
            Feudal.getConfiguration().getConfig().set("goldcarrot.goldappleeffects", true);
        }

        if(!Feudal.getConfiguration().getConfig().contains("kingdom.challenges.winland")){
            Feudal.getConfiguration().getConfig().set("kingdom.challenges.winland", true);
        }

        if(!Feudal.getConfiguration().getConfig().contains("kingdom.challenges.loseLand")){
            Feudal.getConfiguration().getConfig().set("kingdom.challenges.loseLand", true);
        }
        if(!Feudal.getConfiguration().getConfig().contains("setup.default.level")){
            Feudal.getConfiguration().getConfig().set("setup.default.enable", false);
            Feudal.getConfiguration().getConfig().set("setup.default.profession", "guard");
            Feudal.getConfiguration().getConfig().set("setup.default.level", 50);
            Feudal.getConfiguration().getConfig().set("setup.default.strength", 200);
            Feudal.getConfiguration().getConfig().set("setup.default.toughness", 200);
            Feudal.getConfiguration().getConfig().set("setup.default.speed", 200);
            Feudal.getConfiguration().getConfig().set("setup.default.stamina", 200);
            Feudal.getConfiguration().getConfig().set("setup.default.luck", 200);
        }
        if(!Feudal.getConfiguration().getConfig().contains("kingdom.landProtection.villagers")){
            Feudal.getConfiguration().getConfig().set("kingdom.landProtection.villagers", false);
            Feudal.getConfiguration().getConfig().set("kingdom.shieldProtection.villagers", true);
            Feudal.getConfiguration().getConfig().set("kingdom.landProtection.enderpearls", true);
            Feudal.getConfiguration().getConfig().set("kingdom.shieldProtection.enderpearls", true);
            Feudal.getConfiguration().getConfig().set("Toughness.effectHealth", true);
        }
        if(!Feudal.getConfiguration().getConfig().contains("kingdom.chat.symbols.enabled")){
            Feudal.getConfiguration().getConfig().set("kingdom.chat.symbols.enabled", true);
            Feudal.getConfiguration().getConfig().set("kingdom.chat.symbols.ally", "!");
            Feudal.getConfiguration().getConfig().set("kingdom.chat.symbols.kingdom", ":");
        }
        if(!Feudal.getConfiguration().getConfig().contains("sql.enable")){
            //1.10.0
            Feudal.getConfiguration().getConfig().set("sql.enable", false);
            Feudal.getConfiguration().getConfig().set("sql.sqlLite", false);
            Feudal.getConfiguration().getConfig().set("sql.host_ip", "localhost");
            Feudal.getConfiguration().getConfig().set("sql.port", "3306");
            Feudal.getConfiguration().getConfig().set("sql.database", "feudal");
            Feudal.getConfiguration().getConfig().set("sql.username", "root");
            Feudal.getConfiguration().getConfig().set("sql.password", "password");
            Feudal.getConfiguration().getConfig().set("UseVault", null);
            //1.10.0
        }
        if(!Feudal.getConfiguration().getConfig().contains("Farmer.moneyXP")) {
            //1.11.0
            Feudal.getConfiguration().getConfig().set("Farmer.moneyXP", 0.0);
            Feudal.getConfiguration().getConfig().set("Logger.moneyXP", 0.0);
            Feudal.getConfiguration().getConfig().set("Hunter.moneyXP", 0.0);
            Feudal.getConfiguration().getConfig().set("Miner.moneyXP", 0.0);
            Feudal.getConfiguration().getConfig().set("Cook.moneyXP", 0.0);
            Feudal.getConfiguration().getConfig().set("Fisher.moneyXP", 0.0);
            Feudal.getConfiguration().getConfig().set("Builder.moneyXP", 0.0);
            Feudal.getConfiguration().getConfig().set("Shepherd.moneyXP", 0.0);
            Feudal.getConfiguration().getConfig().set("Scribe.moneyXP", 0.0);
            Feudal.getConfiguration().getConfig().set("Guard.moneyXP", 0.0);
            Feudal.getConfiguration().getConfig().set("Assassin.moneyXP", 0.0);
            Feudal.getConfiguration().getConfig().set("Alchemist.moneyXP", 0.0);
            Feudal.getConfiguration().getConfig().set("Blacksmith.moneyXP", 0.0);
            Feudal.getConfiguration().getConfig().set("Healer.moneyXP", 0.0);
            Feudal.getConfiguration().getConfig().set("Merchant.moneyXP", 0.0);
            Feudal.getConfiguration().getConfig().set("Squire.moneyXP", 0.0);
            Feudal.getConfiguration().getConfig().set("Knight.moneyXP", 0.0);
            Feudal.getConfiguration().getConfig().set("Baron.moneyXP", 0.0);
            Feudal.getConfiguration().getConfig().set("King.moneyXP", 0.0);

            Feudal.getConfiguration().getConfig().set("kingdom.cost", 0.0);

            Feudal.getConfiguration().getConfig().set("Luck.Effect.doubleLogs", 70.0);
            Feudal.getConfiguration().getConfig().set("Luck.Effect.builderFood", 25.0);
            //1.11.0
        }
        if(!Feudal.getConfiguration().getConfig().contains("kingdom.land.tax")) {
            //1.12.0
            Feudal.getConfiguration().getConfig().set("kingdom.land.tax", Feudal.getConfiguration().getConfig().getDouble("kingdom.tax.land", 2.0));
            Feudal.getConfiguration().getConfig().set("kingdom.tax.land", null);

            final List<String> cmds = new ArrayList<>();
            cmds.add("/sethome");
            cmds.add("/home");
            cmds.add("/f home");
            cmds.add("/tpa");
            cmds.add("/tpahere");
            Feudal.getConfiguration().getConfig().set("kingdom.land.bannedcommands", cmds);

            final List<String> blocks = new ArrayList<>();
            blocks.add(XMaterial.STONE_BRICKS.name());
            blocks.add(XMaterial.OAK_WOOD.name());
            blocks.add(XMaterial.STONE.name());
            blocks.add(XMaterial.STONE_BRICK_STAIRS.name());
            blocks.add(XMaterial.OAK_STAIRS.name());
            blocks.add(XMaterial.SPRUCE_STAIRS.name());
            blocks.add(XMaterial.ACACIA_STAIRS.name());
            blocks.add(XMaterial.JUNGLE_STAIRS.name());
            blocks.add(XMaterial.BIRCH_STAIRS.name());
            blocks.add(XMaterial.DARK_OAK_STAIRS.name());
            blocks.add(XMaterial.STONE_SLAB.name());
            blocks.add(XMaterial.STONE_SLAB.name());
            blocks.add(XMaterial.GLASS.name());
            blocks.add(XMaterial.WHITE_STAINED_GLASS.name());
            blocks.add(XMaterial.SANDSTONE.name());
            blocks.add(XMaterial.WHITE_WOOL.name());
            blocks.add(XMaterial.GOLD_BLOCK.name());
            blocks.add(XMaterial.IRON_BLOCK.name());
            blocks.add(XMaterial.DIAMOND_BLOCK.name());
            blocks.add(XMaterial.EMERALD_BLOCK.name());
            blocks.add(XMaterial.BOOKSHELF.name());
            blocks.add(XMaterial.CHEST.name());
            blocks.add(XMaterial.CRAFTING_TABLE.name());
            blocks.add(XMaterial.FURNACE.name());
            blocks.add(XMaterial.LADDER.name());
            blocks.add(XMaterial.LAPIS_BLOCK.name());
            blocks.add(XMaterial.IRON_DOOR.name());
            blocks.add(XMaterial.OAK_FENCE.name());
            blocks.add(XMaterial.REDSTONE_BLOCK.name());
            blocks.add(XMaterial.IRON_BARS.name());
            blocks.add(XMaterial.QUARTZ_BLOCK.name());
            blocks.add(XMaterial.WHITE_STAINED_GLASS_PANE.name());
            Feudal.getConfiguration().getConfig().set("Builder.blocks", blocks);
            //1.12.0
        }
        if(Feudal.getConfiguration().getConfig().contains("spigotUsername")) {
            Feudal.getConfiguration().getConfig().set("spigotUsername", null);
        }
        if(!Feudal.getConfiguration().getConfig().contains("customCommands")) {
            //1.12.3
            final ArrayList<String> customCommands = new ArrayList<>();
            customCommands.add("realm");
            customCommands.add("faction");
            Feudal.getConfiguration().getConfig().set("customCommands", customCommands);
            //1.12.3
        }
        if(!Feudal.getConfiguration().getConfig().contains("Luck.Effect.shearBonus")) {
            //1.13
            Feudal.getConfiguration().getConfig().set("Luck.Effect.shearBonus", 15.0);
            Feudal.getConfiguration().getConfig().set("Blacksmith.xpSaddle", 60);
            //1.13
        }
    }

    private static void createConfigDefaults() {
        //1.10.0
        Feudal.getConfiguration().getConfig().set("sql.enable", false);
        Feudal.getConfiguration().getConfig().set("sql.sqlLite", false);
        Feudal.getConfiguration().getConfig().set("sql.host_ip", "localhost");
        Feudal.getConfiguration().getConfig().set("sql.port", "3306");
        Feudal.getConfiguration().getConfig().set("sql.database", "feudal");
        Feudal.getConfiguration().getConfig().set("sql.username", "root");
        Feudal.getConfiguration().getConfig().set("sql.password", "password");
        //1.10.0

        Feudal.getConfiguration().getConfig().set("forceUsePoints", true);
        //Feudal.getConfiguration().getConfig().set("UseVault", true);
        Feudal.getConfiguration().getConfig().set("startBalance", 100);
        Feudal.getConfiguration().getConfig().set("startPoints", 50);

        Feudal.getConfiguration().getConfig().set("landMustBeConnected", true);

        //1.12.3
        final ArrayList<String> customCommands = new ArrayList<>();
        customCommands.add("realm");
        customCommands.add("faction");
        Feudal.getConfiguration().getConfig().set("customCommands", customCommands);
        //1.12.3

        final ArrayList<String> bannedWorlds = new ArrayList<>();
        bannedWorlds.add("nonFeudalWorld");
        bannedWorlds.add("nonFeudalWorld_nether");
        bannedWorlds.add("nonFeudalWorld_end");
        Feudal.getConfiguration().getConfig().set("bannedWorlds", bannedWorlds);
        final ArrayList<String> noClaimWorlds = new ArrayList<>();
        noClaimWorlds.add("nonClaimWorld");
        noClaimWorlds.add("nonClaimWorld_nether");
        noClaimWorlds.add("nonClaimWorld_end");
        Feudal.getConfiguration().getConfig().set("noClaimWorlds", noClaimWorlds);

        Feudal.getConfiguration().getConfig().set("kingdom.name.maxLength", 20);
        Feudal.getConfiguration().getConfig().set("kingdom.name.minLength", 3);
        Feudal.getConfiguration().getConfig().set("kingdom.name.allowSymbols", false);
        final ArrayList<String> bannedNames = new ArrayList<>();
        bannedNames.add("curseWordsHere");
        bannedNames.add("racistWordsHere");
        bannedNames.add("Forseth11");
        bannedNames.add("Minecraft");
        bannedNames.add("Kingdom");
        bannedNames.add("Kingdoms");
        Feudal.getConfiguration().getConfig().set("kingdom.name.banned", bannedNames);
        Feudal.getConfiguration().getConfig().set("kingdom.challenges.lengthInHours", 168);
        Feudal.getConfiguration().getConfig().set("kingdom.challenges.forceScoreboards", true);
        Feudal.getConfiguration().getConfig().set("kingdom.challenges.enableScoreboards", true);
        Feudal.getConfiguration().getConfig().set("kingdom.challenges.startTax", 50.0);
        Feudal.getConfiguration().getConfig().set("kingdom.challenges.taxAdd", 15.0);
        Feudal.getConfiguration().getConfig().set("kingdom.challenges.taxPerMinuteWhileFightersOnline", true);
        Feudal.getConfiguration().getConfig().set("kingdom.challenges.minutesToWinWhileFightersOnlineBeforeAccepting", 100);
        Feudal.getConfiguration().getConfig().set("kingdom.challenges.maxDefence", 3);
        Feudal.getConfiguration().getConfig().set("kingdom.challenges.winban", true);
        Feudal.getConfiguration().getConfig().set("kingdom.challenges.winland", true);
        Feudal.getConfiguration().getConfig().set("kingdom.challenges.onlinePercentNeededForAttackersToWinByDefault", 70.0);
        Feudal.getConfiguration().getConfig().set("kingdom.challenges.secondsToBeginAttack", 600);
        Feudal.getConfiguration().getConfig().set("kingdom.challenges.secondsToDefendPerChunk", 30);
        Feudal.getConfiguration().getConfig().set("kingdom.challenges.percentOfTreasuryWonByDefault", 20.0);
        Feudal.getConfiguration().getConfig().set("kingdom.challenges.percentOfTreasuryWon", 85.0);
        Feudal.getConfiguration().getConfig().set("kingdom.challenges.attackerCounterAttackBanInHours", 168);
        Feudal.getConfiguration().getConfig().set("kingdom.challenges.defenderCounterAttackBanInHours", 48);
        Feudal.getConfiguration().getConfig().set("kingdom.challenges.nobleWinXPMultiplier", 1.35);
        Feudal.getConfiguration().getConfig().set("kingdom.challenges.xpWin", 20000);
        Feudal.getConfiguration().getConfig().set("kingdom.challenges.xpWinDefault", 8000);
        Feudal.getConfiguration().getConfig().set("kingdom.challenges.percentAttackMembersNeededToFight", 40.0);
        Feudal.getConfiguration().getConfig().set("kingdom.challenges.percentDefenderMembersNeededToFight", 40.0);
        Feudal.getConfiguration().getConfig().set("kingdom.challenges.claimAgeRequiredHours", 120.0);
        Feudal.getConfiguration().getConfig().set("kingdom.challenges.fighterSeparation", 2);

        Feudal.getConfiguration().getConfig().set("kingdom.challenges.keepInventory", true);
        Feudal.getConfiguration().getConfig().set("kingdom.challenges.loseLand", true);

        //1.8.1
        Feudal.getConfiguration().getConfig().set("kingdom.chat.symbols.enabled", true);
        Feudal.getConfiguration().getConfig().set("kingdom.chat.symbols.ally", "!");
        Feudal.getConfiguration().getConfig().set("kingdom.chat.symbols.kingdom", ":");
        //

        Feudal.getConfiguration().getConfig().set("kingdom.keepInventoryOnLand", true);

        Feudal.getConfiguration().getConfig().set("kingdom.shield.1.price", 15000);
        Feudal.getConfiguration().getConfig().set("kingdom.shield.2.price", 30000);
        Feudal.getConfiguration().getConfig().set("kingdom.shield.3.price", 50000);
        Feudal.getConfiguration().getConfig().set("kingdom.shield.vacation.price", 5000);
        Feudal.getConfiguration().getConfig().set("kingdom.shield.1.coolDownDays", 30);
        Feudal.getConfiguration().getConfig().set("kingdom.shield.2.coolDownDays", 45);
        Feudal.getConfiguration().getConfig().set("kingdom.shield.3.coolDownDays", 60);
        Feudal.getConfiguration().getConfig().set("kingdom.shield.vacation.coolDownDays", 25);

        Feudal.getConfiguration().getConfig().set("kingdom.landProtection.blockPlace", true);
        Feudal.getConfiguration().getConfig().set("kingdom.landProtection.blockBreak", true);
        Feudal.getConfiguration().getConfig().set("kingdom.landProtection.chestAccess", true);
        Feudal.getConfiguration().getConfig().set("kingdom.landProtection.piston", true);
        Feudal.getConfiguration().getConfig().set("kingdom.landProtection.creeperExplosion", false);
        Feudal.getConfiguration().getConfig().set("kingdom.landProtection.otherExplosion", true);
        Feudal.getConfiguration().getConfig().set("kingdom.landProtection.lighter", true);
        Feudal.getConfiguration().getConfig().set("kingdom.landProtection.interact", true);
        Feudal.getConfiguration().getConfig().set("kingdom.landProtection.fireSpread", true);
        Feudal.getConfiguration().getConfig().set("kingdom.landProtection.enderman", false);
        Feudal.getConfiguration().getConfig().set("kingdom.landProtection.enderDragon", false);
        Feudal.getConfiguration().getConfig().set("kingdom.landProtection.villagers", true);
        Feudal.getConfiguration().getConfig().set("kingdom.landProtection.enderpearls", true);

        Feudal.getConfiguration().getConfig().set("kingdom.home.teleportDelayInSeconds", 3);

        Feudal.getConfiguration().getConfig().set("kingdom.shieldProtection.blockPlace", true);
        Feudal.getConfiguration().getConfig().set("kingdom.shieldProtection.blockBreak", true);
        Feudal.getConfiguration().getConfig().set("kingdom.shieldProtection.chestAccess", true);
        Feudal.getConfiguration().getConfig().set("kingdom.shieldProtection.piston", true);
        Feudal.getConfiguration().getConfig().set("kingdom.shieldProtection.creeperExplosion", true);
        Feudal.getConfiguration().getConfig().set("kingdom.shieldProtection.otherExplosion", true);
        Feudal.getConfiguration().getConfig().set("kingdom.shieldProtection.lighter", true);
        Feudal.getConfiguration().getConfig().set("kingdom.shieldProtection.interact", true);
        Feudal.getConfiguration().getConfig().set("kingdom.shieldProtection.fireSpread", true);
        Feudal.getConfiguration().getConfig().set("kingdom.shieldProtection.enderman", true);
        Feudal.getConfiguration().getConfig().set("kingdom.shieldProtection.enderDragon", true);
        Feudal.getConfiguration().getConfig().set("kingdom.shieldProtection.villagers", true);
        Feudal.getConfiguration().getConfig().set("kingdom.shieldProtection.enderpearls", true);

        //Feudal.getConfiguration().getConfig().set("kingdom.tax.land", 2.0); OLD
        Feudal.getConfiguration().getConfig().set("kingdom.land.sendtitles", true);
        Feudal.getConfiguration().getConfig().set("kingdom.land.tax", 2.0);

        final List<String> cmds = new ArrayList<>();
        cmds.add("/sethome");
        cmds.add("/home");
        cmds.add("/f home");
        cmds.add("/tpa");
        cmds.add("/tpahere");
        Feudal.getConfiguration().getConfig().set("kingdom.land.bannedcommands", cmds);
        Feudal.getConfiguration().getConfig().set("kingdom.inactiveDeleteDays", 60);
        Feudal.getConfiguration().getConfig().set("kingdom.maxMembers", 15);
        Feudal.getConfiguration().getConfig().set("kingdom.cost", 0.0);
        Feudal.getConfiguration().getConfig().set("kingdom.startShieldHours", 120);


        Feudal.getConfiguration().getConfig().set("setup.peasantry.cost", 30);
        Feudal.getConfiguration().getConfig().set("setup.commoner.cost", 35);
        Feudal.getConfiguration().getConfig().set("setup.noble.cost", 40);
        Feudal.getConfiguration().getConfig().set("setup.require", false);
        final ArrayList<String> exceptions = new ArrayList<>();
        exceptions.add("/register");
        exceptions.add("/login");
        exceptions.add("/pass");
        Feudal.getConfiguration().getConfig().set("setup.requireCmdExceptions", false);

        //New config stuff 1.7.2
        Feudal.getConfiguration().getConfig().set("setup.default.enable", false);
        Feudal.getConfiguration().getConfig().set("setup.default.profession", "guard");
        Feudal.getConfiguration().getConfig().set("setup.default.level", 50);
        Feudal.getConfiguration().getConfig().set("setup.default.strength", 200);
        Feudal.getConfiguration().getConfig().set("setup.default.toughness", 200);
        Feudal.getConfiguration().getConfig().set("setup.default.speed", 200);
        Feudal.getConfiguration().getConfig().set("setup.default.stamina", 200);
        Feudal.getConfiguration().getConfig().set("setup.default.luck", 200);

        //NEW CONFIG STUFF
        Feudal.getConfiguration().getConfig().set("attributeCap.enable", true);
        Feudal.getConfiguration().getConfig().set("attributeCap.enableFixer", true);
        Feudal.getConfiguration().getConfig().set("attributeCap.fixerLevelsPer", 10);
        Feudal.getConfiguration().getConfig().set("attributeCap.cap", 3250);
        Feudal.getConfiguration().getConfig().set("redistribute.pointsPer", 10);
        //

        Feudal.getConfiguration().getConfig().set("reputation.start", 1000);
        Feudal.getConfiguration().getConfig().set("reputation.max", 10000);
        Feudal.getConfiguration().getConfig().set("reputation.min", 0);
        Feudal.getConfiguration().getConfig().set("reputation.leaveKingdom", -175);
        Feudal.getConfiguration().getConfig().set("reputation.enemyAlly", -20);
        Feudal.getConfiguration().getConfig().set("reputation.neutralAlly", -10);
        Feudal.getConfiguration().getConfig().set("reputation.winChallenge", 50);
        Feudal.getConfiguration().getConfig().set("reputation.loseChallenge", -25);
        Feudal.getConfiguration().getConfig().set("reputation.winDefault", 25);
        Feudal.getConfiguration().getConfig().set("reputation.loseDefault", -25);
        Feudal.getConfiguration().getConfig().set("reputation.placeChallenge", 5);
        Feudal.getConfiguration().getConfig().set("reputation.acceptChallenge", 15);
        Feudal.getConfiguration().getConfig().set("reputation.ignoreChallengePerHour", -1);
        Feudal.getConfiguration().getConfig().set("reputation.maxProfession", 175);
        Feudal.getConfiguration().getConfig().set("reputation.changeProfessionWithoutMax", -10);
        Feudal.getConfiguration().getConfig().set("reputation.failToPayLandTax", -6);
        Feudal.getConfiguration().getConfig().set("reputation.deleteKingdom", -15);
        Feudal.getConfiguration().getConfig().set("reputation.kickPlayerFromKingdom", -3);
        Feudal.getConfiguration().getConfig().set("reputation.buyVacation", -3);
        Feudal.getConfiguration().getConfig().set("reputation.attackPrevKingdom", -200);
        Feudal.getConfiguration().getConfig().set("reputation.playerKill", 1);//playerKill
        Feudal.getConfiguration().getConfig().set("reputation.playerDeath", -1);//playerDeath


        //SPAR
        Feudal.getConfiguration().getConfig().set("spar.timeoutseconds", 60);
        Feudal.getConfiguration().getConfig().set("spar.maxDistance", 50.0);
        Feudal.getConfiguration().getConfig().set("spar.percentHealthWin", 50.0);
        //

        //GOLD CARROT
        Feudal.getConfiguration().getConfig().set("goldcarrot.nightvision", true);
        Feudal.getConfiguration().getConfig().set("goldcarrot.nightvisionticks", 2400);
        Feudal.getConfiguration().getConfig().set("goldcarrot.speed", true);
        Feudal.getConfiguration().getConfig().set("goldcarrot.speedticks", 400);
        Feudal.getConfiguration().getConfig().set("goldcarrot.speedlevel", 0);
        Feudal.getConfiguration().getConfig().set("goldcarrot.increasehealth", true);
        Feudal.getConfiguration().getConfig().set("goldcarrot.goldappleeffects", true);
        //

        //
        Feudal.getConfiguration().getConfig().set("Strength.MaxLevel", 1000);
        Feudal.getConfiguration().getConfig().set("Toughness.MaxLevel", 1000);
        Feudal.getConfiguration().getConfig().set("Speed.MaxLevel", 1000);
        Feudal.getConfiguration().getConfig().set("Stamina.MaxLevel", 1000);
        Feudal.getConfiguration().getConfig().set("Luck.MaxLevel", 1000);
        Feudal.getConfiguration().getConfig().set("Strength.EndXP", 5000);
        Feudal.getConfiguration().getConfig().set("Toughness.EndXP", 5000);
        Feudal.getConfiguration().getConfig().set("Speed.EndXP", 5000);
        Feudal.getConfiguration().getConfig().set("Stamina.EndXP", 5000);
        Feudal.getConfiguration().getConfig().set("Luck.EndXP", 5000);
        Feudal.getConfiguration().getConfig().set("Strength.StartXP", 100);
        Feudal.getConfiguration().getConfig().set("Toughness.StartXP", 100);
        Feudal.getConfiguration().getConfig().set("Speed.StartXP", 100);
        Feudal.getConfiguration().getConfig().set("Stamina.StartXP", 100);
        Feudal.getConfiguration().getConfig().set("Luck.StartXP", 100);
        Feudal.getConfiguration().getConfig().set("Strength.MinDamageIncrease", 0.5);
        Feudal.getConfiguration().getConfig().set("Strength.MaxDamageIncrease", 2);
        Feudal.getConfiguration().getConfig().set("Toughness.MinRegenRate", 0.5);
        Feudal.getConfiguration().getConfig().set("Toughness.MaxRegenRate", 1.5);
        Feudal.getConfiguration().getConfig().set("Toughness.MinHealth", 16.0);//8 hearts
        Feudal.getConfiguration().getConfig().set("Toughness.MaxHealth", 40.0);
        Feudal.getConfiguration().getConfig().set("Toughness.effectHealth", true);
        Feudal.getConfiguration().getConfig().set("Speed.Min", 0.8);
        Feudal.getConfiguration().getConfig().set("Speed.Max", 2.0);
        Feudal.getConfiguration().getConfig().set("Stamina.MinRate", 0.0);
        Feudal.getConfiguration().getConfig().set("Stamina.MaxRate", 4.0);

        //NEW
        Feudal.getConfiguration().getConfig().set("Stamina.maxGoodPotionMultiplier", 2.0);
        Feudal.getConfiguration().getConfig().set("Stamina.minGoodPotionMultiplier", 1.0);
        Feudal.getConfiguration().getConfig().set("Stamina.maxBadPotionMultiplier", 0.5);
        Feudal.getConfiguration().getConfig().set("Stamina.minBadPotionMultiplier", 1.0);
        Feudal.getConfiguration().getConfig().set("Stamina.defaultDamage", 1.0);
        Feudal.getConfiguration().getConfig().set("Stamina.minDamage", 0.5);
        Feudal.getConfiguration().getConfig().set("Stamina.damageRegen", 0.01);
        Feudal.getConfiguration().getConfig().set("Stamina.damageRegenTimerTicks", 100);
        Feudal.getConfiguration().getConfig().set("Stamina.noStaminaDamageMaxHits", 15);
        Feudal.getConfiguration().getConfig().set("Stamina.fullStaminaDamageMaxHits", 200);
        //

        Feudal.getConfiguration().getConfig().set("Stamina.Tick", 600);
        Feudal.getConfiguration().getConfig().set("Luck.MinPercent", 0.0);
        Feudal.getConfiguration().getConfig().set("Luck.MaxPercent", 40.0);

        Feudal.getConfiguration().getConfig().set("Luck.Effect.dropDoublePercent", 75.0);
        Feudal.getConfiguration().getConfig().set("Luck.Effect.cheatDeathPercent", 10.0);
        Feudal.getConfiguration().getConfig().set("Luck.Effect.doubleXPPercent", 100.0);
        Feudal.getConfiguration().getConfig().set("Luck.Effect.higherEnchantmentsPercent", 75.0);
        Feudal.getConfiguration().getConfig().set("Luck.Effect.betterPotionEffectsPercent", 90.0);
        Feudal.getConfiguration().getConfig().set("Luck.Effect.criticalHitsPercent", 50.0);
        Feudal.getConfiguration().getConfig().set("Luck.Effect.toolDamageCancelPercent", 100.0);
        Feudal.getConfiguration().getConfig().set("Luck.Effect.reducedDamagePercent", 40.0);
        Feudal.getConfiguration().getConfig().set("Luck.Effect.fishCatchPercent", 90.0);//90
        Feudal.getConfiguration().getConfig().set("Luck.Effect.doubleLogs", 70.0);
        Feudal.getConfiguration().getConfig().set("Luck.Effect.builderFood", 25.0);
        Feudal.getConfiguration().getConfig().set("Luck.Effect.shearBonus", 15.0);


        Feudal.getConfiguration().getConfig().set("Strength.LevelPerPoint", 100);
        Feudal.getConfiguration().getConfig().set("Toughness.LevelPerPoint", 100);
        Feudal.getConfiguration().getConfig().set("Speed.LevelPerPoint", 100);
        Feudal.getConfiguration().getConfig().set("Stamina.LevelPerPoint", 100);
        Feudal.getConfiguration().getConfig().set("Luck.LevelPerPoint", 100);

        Feudal.getConfiguration().getConfig().set("Strength.NotifyLevelUpPer", 5);
        Feudal.getConfiguration().getConfig().set("Toughness.NotifyLevelUpPer", 5);
        Feudal.getConfiguration().getConfig().set("Speed.NotifyLevelUpPer", 5);
        Feudal.getConfiguration().getConfig().set("Stamina.NotifyLevelUpPer", 5);
        Feudal.getConfiguration().getConfig().set("Luck.NotifyLevelUpPer", 5);

        Feudal.getConfiguration().getConfig().set("Profession.NotifyLevelUpPer", 1);
        Feudal.getConfiguration().getConfig().set("Profession.requiredPeasantCompletionPercent", 35.0);
        Feudal.getConfiguration().getConfig().set("Profession.requiredCommonerCompletion", 45.0);
        Feudal.getConfiguration().getConfig().set("Profession.doRankupForNobles", true);
        //

        Feudal.getConfiguration().getConfig().set("moneyPercentLostOnDeath", 10.0);
        Feudal.getConfiguration().getConfig().set("moneyPercentLostOnDeathByAssassinKiller", 16.0);

        Feudal.getConfiguration().getConfig().set("protectSpawners", true);

        //Professions
        Feudal.getConfiguration().getConfig().set("Farmer.land", 3);
        Feudal.getConfiguration().getConfig().set("Farmer.MaxLevel", 100);
        Feudal.getConfiguration().getConfig().set("Farmer.EndXP", 10000);
        Feudal.getConfiguration().getConfig().set("Farmer.StartXP", 100);
        Feudal.getConfiguration().getConfig().set("Farmer.MaxPercent", 100.0);
        Feudal.getConfiguration().getConfig().set("Farmer.pointCost", 5);
        Feudal.getConfiguration().getConfig().set("Farmer.taxPercent", 3.0);
        Feudal.getConfiguration().getConfig().set("Farmer.rentPercent", 100.0);
        Feudal.getConfiguration().getConfig().set("Farmer.xpPerCrop", 235);

        Feudal.getConfiguration().getConfig().set("Logger.land", 3);
        Feudal.getConfiguration().getConfig().set("Logger.MaxLevel", 100);
        Feudal.getConfiguration().getConfig().set("Logger.EndXP", 10000);
        Feudal.getConfiguration().getConfig().set("Logger.StartXP", 100);
        Feudal.getConfiguration().getConfig().set("Logger.MaxPercent", 100.0);
        Feudal.getConfiguration().getConfig().set("Logger.pointCost", 5);
        Feudal.getConfiguration().getConfig().set("Logger.taxPercent", 3.0);
        Feudal.getConfiguration().getConfig().set("Logger.rentPercent", 100.0);
        Feudal.getConfiguration().getConfig().set("Logger.xpPerLog", 100);

        Feudal.getConfiguration().getConfig().set("Hunter.land", 3);
        Feudal.getConfiguration().getConfig().set("Hunter.MaxLevel", 100);
        Feudal.getConfiguration().getConfig().set("Hunter.EndXP", 10000);
        Feudal.getConfiguration().getConfig().set("Hunter.StartXP", 100);
        Feudal.getConfiguration().getConfig().set("Hunter.MaxPercent", 100.0);
        Feudal.getConfiguration().getConfig().set("Hunter.pointCost", 5);
        Feudal.getConfiguration().getConfig().set("Hunter.taxPercent", 3.0);
        Feudal.getConfiguration().getConfig().set("Hunter.rentPercent", 100.0);
        Feudal.getConfiguration().getConfig().set("Hunter.xpPerAnimal", 500);
        Feudal.getConfiguration().getConfig().set("Hunter.xpPerAnimalPlus", 1400);


        Feudal.getConfiguration().getConfig().set("Miner.land", 3);
        Feudal.getConfiguration().getConfig().set("Miner.MaxLevel", 100);
        Feudal.getConfiguration().getConfig().set("Miner.EndXP", 10000);
        Feudal.getConfiguration().getConfig().set("Miner.StartXP", 100);
        Feudal.getConfiguration().getConfig().set("Miner.MaxPercent", 100.0);
        Feudal.getConfiguration().getConfig().set("Miner.pointCost", 5);
        Feudal.getConfiguration().getConfig().set("Miner.taxPercent", 3.0);
        Feudal.getConfiguration().getConfig().set("Miner.rentPercent", 100.0);
        Feudal.getConfiguration().getConfig().set("Miner.xpPerCoal", 50);
        Feudal.getConfiguration().getConfig().set("Miner.xpPerIron", 100);
        Feudal.getConfiguration().getConfig().set("Miner.xpPerGold", 1000);
        Feudal.getConfiguration().getConfig().set("Miner.xpPerEmerald", 6000);
        Feudal.getConfiguration().getConfig().set("Miner.xpPerDiamond", 3000);
        Feudal.getConfiguration().getConfig().set("Miner.xpPerLapis", 300);
        Feudal.getConfiguration().getConfig().set("Miner.xpPerRedstone", 200);
        Feudal.getConfiguration().getConfig().set("Miner.xpPerQuartz", 250);

        Feudal.getConfiguration().getConfig().set("Cook.land", 3);
        Feudal.getConfiguration().getConfig().set("Cook.MaxLevel", 100);
        Feudal.getConfiguration().getConfig().set("Cook.EndXP", 10000);
        Feudal.getConfiguration().getConfig().set("Cook.StartXP", 100);
        Feudal.getConfiguration().getConfig().set("Cook.MaxPercent", 100.0);
        Feudal.getConfiguration().getConfig().set("Cook.pointCost", 5);
        Feudal.getConfiguration().getConfig().set("Cook.taxPercent", 3.0);
        Feudal.getConfiguration().getConfig().set("Cook.rentPercent", 100.0);
        Feudal.getConfiguration().getConfig().set("Cook.xpPerFood", 200);
        Feudal.getConfiguration().getConfig().set("Cook.xpPerCookie", 100);
        Feudal.getConfiguration().getConfig().set("Cook.xpPerSoup", 1600);
        Feudal.getConfiguration().getConfig().set("Cook.xpPerCake", 4500);

        Feudal.getConfiguration().getConfig().set("Fisher.land", 3);
        Feudal.getConfiguration().getConfig().set("Fisher.MaxLevel", 100);
        Feudal.getConfiguration().getConfig().set("Fisher.EndXP", 10000);
        Feudal.getConfiguration().getConfig().set("Fisher.StartXP", 100);
        Feudal.getConfiguration().getConfig().set("Fisher.MaxPercent", 100.0);
        Feudal.getConfiguration().getConfig().set("Fisher.pointCost", 5);
        Feudal.getConfiguration().getConfig().set("Fisher.taxPercent", 3.0);
        Feudal.getConfiguration().getConfig().set("Fisher.rentPercent", 100.0);
        Feudal.getConfiguration().getConfig().set("Fisher.xpPerCatch", 1650);

        Feudal.getConfiguration().getConfig().set("Builder.land", 3);
        Feudal.getConfiguration().getConfig().set("Builder.MaxLevel", 100);
        Feudal.getConfiguration().getConfig().set("Builder.EndXP", 10000);
        Feudal.getConfiguration().getConfig().set("Builder.StartXP", 100);
        Feudal.getConfiguration().getConfig().set("Builder.MaxPercent", 100.0);
        Feudal.getConfiguration().getConfig().set("Builder.pointCost", 5);
        Feudal.getConfiguration().getConfig().set("Builder.taxPercent", 3.0);
        Feudal.getConfiguration().getConfig().set("Builder.rentPercent", 100.0);
        Feudal.getConfiguration().getConfig().set("Builder.xpPerBlock", 35);
        Feudal.getConfiguration().getConfig().set("Builder.xpOnKingdomBonus", 75);

        final List<String> blocks = new ArrayList<>();
        blocks.add(XMaterial.STONE_BRICKS.name());
        blocks.add(XMaterial.OAK_WOOD.name());
        blocks.add(XMaterial.STONE.name());
        blocks.add(XMaterial.STONE_BRICK_STAIRS.name());
        blocks.add(XMaterial.OAK_STAIRS.name());
        blocks.add(XMaterial.SPRUCE_STAIRS.name());
        blocks.add(XMaterial.ACACIA_STAIRS.name());
        blocks.add(XMaterial.JUNGLE_STAIRS.name());
        blocks.add(XMaterial.BIRCH_STAIRS.name());
        blocks.add(XMaterial.DARK_OAK_STAIRS.name());
        blocks.add(XMaterial.STONE_SLAB.name());
        blocks.add(XMaterial.STONE_SLAB.name());
        blocks.add(XMaterial.GLASS.name());
        blocks.add(XMaterial.WHITE_STAINED_GLASS.name());
        blocks.add(XMaterial.SANDSTONE.name());
        blocks.add(XMaterial.WHITE_WOOL.name());
        blocks.add(XMaterial.GOLD_BLOCK.name());
        blocks.add(XMaterial.IRON_BLOCK.name());
        blocks.add(XMaterial.DIAMOND_BLOCK.name());
        blocks.add(XMaterial.EMERALD_BLOCK.name());
        blocks.add(XMaterial.BOOKSHELF.name());
        blocks.add(XMaterial.CHEST.name());
        blocks.add(XMaterial.CRAFTING_TABLE.name());
        blocks.add(XMaterial.FURNACE.name());
        blocks.add(XMaterial.LADDER.name());
        blocks.add(XMaterial.LAPIS_BLOCK.name());
        blocks.add(XMaterial.IRON_DOOR.name());
        blocks.add(XMaterial.OAK_FENCE.name());
        blocks.add(XMaterial.REDSTONE_BLOCK.name());
        blocks.add(XMaterial.IRON_BARS.name());
        blocks.add(XMaterial.QUARTZ_BLOCK.name());
        blocks.add(XMaterial.WHITE_STAINED_GLASS_PANE.name());
        Feudal.getConfiguration().getConfig().set("Builder.blocks", blocks);

        Feudal.getConfiguration().getConfig().set("Shepherd.land", 3);
        Feudal.getConfiguration().getConfig().set("Shepherd.MaxLevel", 100);
        Feudal.getConfiguration().getConfig().set("Shepherd.EndXP", 10000);
        Feudal.getConfiguration().getConfig().set("Shepherd.StartXP", 100);
        Feudal.getConfiguration().getConfig().set("Shepherd.MaxPercent", 100.0);
        Feudal.getConfiguration().getConfig().set("Shepherd.pointCost", 5);
        Feudal.getConfiguration().getConfig().set("Shepherd.taxPercent", 3.0);
        Feudal.getConfiguration().getConfig().set("Shepherd.rentPercent", 100.0);
        Feudal.getConfiguration().getConfig().set("Shepherd.xpPerShear", 700);
        Feudal.getConfiguration().getConfig().set("Shepherd.xpPerAnimalKill", 400);

        Feudal.getConfiguration().getConfig().set("Scribe.land", 3);
        Feudal.getConfiguration().getConfig().set("Scribe.MaxLevel", 100);
        Feudal.getConfiguration().getConfig().set("Scribe.EndXP", 10000);
        Feudal.getConfiguration().getConfig().set("Scribe.StartXP", 100);
        Feudal.getConfiguration().getConfig().set("Scribe.MaxPercent", 100.0);
        Feudal.getConfiguration().getConfig().set("Scribe.pointCost", 5);
        Feudal.getConfiguration().getConfig().set("Scribe.taxPercent", 3.0);
        Feudal.getConfiguration().getConfig().set("Scribe.rentPercent", 100.0);
        Feudal.getConfiguration().getConfig().set("Scribe.xpStart", 150);
        Feudal.getConfiguration().getConfig().set("Scribe.xpPerWord", 50);
        Feudal.getConfiguration().getConfig().set("Scribe.xpPerNonWord", -45);

        Feudal.getConfiguration().getConfig().set("Guard.land", 6);
        Feudal.getConfiguration().getConfig().set("Guard.MaxLevel", 100);
        Feudal.getConfiguration().getConfig().set("Guard.EndXP", 12000);
        Feudal.getConfiguration().getConfig().set("Guard.StartXP", 100);
        Feudal.getConfiguration().getConfig().set("Guard.MaxPercent", 100.0);
        Feudal.getConfiguration().getConfig().set("Guard.pointCost", 5);
        Feudal.getConfiguration().getConfig().set("Guard.taxPercent", 5.0);
        Feudal.getConfiguration().getConfig().set("Guard.rentPercent", 100.0);
        Feudal.getConfiguration().getConfig().set("Guard.damageResistancePercent", 10.0);
        Feudal.getConfiguration().getConfig().set("Guard.xpPerPlayerKill", 12000);
        Feudal.getConfiguration().getConfig().set("Guard.xpPerMobKill", 600);

        Feudal.getConfiguration().getConfig().set("Assassin.land", 1);
        Feudal.getConfiguration().getConfig().set("Assassin.MaxLevel", 100);
        Feudal.getConfiguration().getConfig().set("Assassin.EndXP", 10000);
        Feudal.getConfiguration().getConfig().set("Assassin.StartXP", 100);
        Feudal.getConfiguration().getConfig().set("Assassin.MaxPercent", 100.0);
        Feudal.getConfiguration().getConfig().set("Assassin.pointCost", 6);
        Feudal.getConfiguration().getConfig().set("Assassin.trackRange.min", 950);
        Feudal.getConfiguration().getConfig().set("Assassin.trackRange.minNonAssassin", 1500);
        Feudal.getConfiguration().getConfig().set("Assassin.trackRange.minUnderGround", 550);
        Feudal.getConfiguration().getConfig().set("Assassin.trackRange.minNonAssassinUnderGround", 950);
        Feudal.getConfiguration().getConfig().set("Assassin.trackRange.priceMultiplier", 20.0);
        Feudal.getConfiguration().getConfig().set("Assassin.trackRange.price", 5000.0);
        Feudal.getConfiguration().getConfig().set("Assassin.randomTracker", true);
        Feudal.getConfiguration().getConfig().set("Assassin.randomNonassassin", true);
        Feudal.getConfiguration().getConfig().set("Assassin.taxPercent", 5.0);
        Feudal.getConfiguration().getConfig().set("Assassin.rentPercent", 100.0);
        Feudal.getConfiguration().getConfig().set("Assassin.xpPerPlayerKill", 6000);
        Feudal.getConfiguration().getConfig().set("Assassin.xpPerPlayerKillOnEnemyLand", 60000);

        Feudal.getConfiguration().getConfig().set("Alchemist.land", 6);
        Feudal.getConfiguration().getConfig().set("Alchemist.MaxLevel", 100);
        Feudal.getConfiguration().getConfig().set("Alchemist.EndXP", 12000);
        Feudal.getConfiguration().getConfig().set("Alchemist.StartXP", 100);
        Feudal.getConfiguration().getConfig().set("Alchemist.MaxPercent", 100.0);
        Feudal.getConfiguration().getConfig().set("Alchemist.pointCost", 5);
        Feudal.getConfiguration().getConfig().set("Alchemist.taxPercent", 5.0);
        Feudal.getConfiguration().getConfig().set("Alchemist.rentPercent", 100.0);

        Feudal.getConfiguration().getConfig().set("Alchemist.fire_resistance", 950);
        Feudal.getConfiguration().getConfig().set("Alchemist.speed", 500);
        Feudal.getConfiguration().getConfig().set("Alchemist.instant_health", 800);
        Feudal.getConfiguration().getConfig().set("Alchemist.poison", 650);
        Feudal.getConfiguration().getConfig().set("Alchemist.regeneration", 1000);
        Feudal.getConfiguration().getConfig().set("Alchemist.strength", 750);
        Feudal.getConfiguration().getConfig().set("Alchemist.weakness", 700);
        Feudal.getConfiguration().getConfig().set("Alchemist.night_vision", 850);
        Feudal.getConfiguration().getConfig().set("Alchemist.water_breathing", 900);
        Feudal.getConfiguration().getConfig().set("Alchemist.leaping", 800);
        Feudal.getConfiguration().getConfig().set("Alchemist.invisibility", 650);
        Feudal.getConfiguration().getConfig().set("Alchemist.extend", 150);
        Feudal.getConfiguration().getConfig().set("Alchemist.splash", 250);
        Feudal.getConfiguration().getConfig().set("Alchemist.strong", 200);
        Feudal.getConfiguration().getConfig().set("Alchemist.linger", 650);

        Feudal.getConfiguration().getConfig().set("Blacksmith.land", 6);
        Feudal.getConfiguration().getConfig().set("Blacksmith.MaxLevel", 100);
        Feudal.getConfiguration().getConfig().set("Blacksmith.EndXP", 12000);
        Feudal.getConfiguration().getConfig().set("Blacksmith.StartXP", 100);
        Feudal.getConfiguration().getConfig().set("Blacksmith.MaxPercent", 100.0);
        Feudal.getConfiguration().getConfig().set("Blacksmith.pointCost", 5);
        Feudal.getConfiguration().getConfig().set("Blacksmith.taxPercent", 5.0);
        Feudal.getConfiguration().getConfig().set("Blacksmith.rentPercent", 100.0);
        Feudal.getConfiguration().getConfig().set("Blacksmith.xp1Wood", 30);
        Feudal.getConfiguration().getConfig().set("Blacksmith.xp2Wood", 60);
        Feudal.getConfiguration().getConfig().set("Blacksmith.xp3Wood", 90);
        Feudal.getConfiguration().getConfig().set("Blacksmith.xp1Gold", 300);
        Feudal.getConfiguration().getConfig().set("Blacksmith.xp2Gold", 600);
        Feudal.getConfiguration().getConfig().set("Blacksmith.xp3Gold", 900);
        Feudal.getConfiguration().getConfig().set("Blacksmith.xp4Gold", 1200);
        Feudal.getConfiguration().getConfig().set("Blacksmith.xp5Gold", 1500);
        Feudal.getConfiguration().getConfig().set("Blacksmith.xp7Gold", 2100);
        Feudal.getConfiguration().getConfig().set("Blacksmith.xp8Gold", 2400);
        Feudal.getConfiguration().getConfig().set("Blacksmith.xp1Iron", 230);
        Feudal.getConfiguration().getConfig().set("Blacksmith.xp2Iron", 450);
        Feudal.getConfiguration().getConfig().set("Blacksmith.xp3Iron", 680);
        Feudal.getConfiguration().getConfig().set("Blacksmith.xp4Iron", 920);
        Feudal.getConfiguration().getConfig().set("Blacksmith.xp5Iron", 1150);
        Feudal.getConfiguration().getConfig().set("Blacksmith.xp7Iron", 1610);
        Feudal.getConfiguration().getConfig().set("Blacksmith.xp8Iron", 1840);
        Feudal.getConfiguration().getConfig().set("Blacksmith.xp1Diamond", 450);
        Feudal.getConfiguration().getConfig().set("Blacksmith.xp2Diamond", 900);
        Feudal.getConfiguration().getConfig().set("Blacksmith.xp3Diamond", 1350);
        Feudal.getConfiguration().getConfig().set("Blacksmith.xp4Diamond", 1900);
        Feudal.getConfiguration().getConfig().set("Blacksmith.xp5Diamond", 2350);
        Feudal.getConfiguration().getConfig().set("Blacksmith.xp7Diamond", 3310);
        Feudal.getConfiguration().getConfig().set("Blacksmith.xp8Diamond", 3740);
        Feudal.getConfiguration().getConfig().set("Blacksmith.xpBow", 200);
        Feudal.getConfiguration().getConfig().set("Blacksmith.xpArrow", 10);
        Feudal.getConfiguration().getConfig().set("Blacksmith.xp4Leather", 200);
        Feudal.getConfiguration().getConfig().set("Blacksmith.xp5Leather", 250);
        Feudal.getConfiguration().getConfig().set("Blacksmith.xp7Leather", 350);
        Feudal.getConfiguration().getConfig().set("Blacksmith.xp8Leather", 400);
        Feudal.getConfiguration().getConfig().set("Blacksmith.xpChainBoots", 900);
        Feudal.getConfiguration().getConfig().set("Blacksmith.xpChainHat", 1120);
        Feudal.getConfiguration().getConfig().set("Blacksmith.xpChainLegs", 1600);
        Feudal.getConfiguration().getConfig().set("Blacksmith.xpChainChest", 1800);
        Feudal.getConfiguration().getConfig().set("Blacksmith.xpSaddle", 60);

        Feudal.getConfiguration().getConfig().set("Healer.land", 6);
        Feudal.getConfiguration().getConfig().set("Healer.MaxLevel", 100);
        Feudal.getConfiguration().getConfig().set("Healer.EndXP", 12000);
        Feudal.getConfiguration().getConfig().set("Healer.StartXP", 100);
        Feudal.getConfiguration().getConfig().set("Healer.MaxPercent", 100.0);
        Feudal.getConfiguration().getConfig().set("Healer.pointCost", 5);
        Feudal.getConfiguration().getConfig().set("Healer.taxPercent", 5.0);
        Feudal.getConfiguration().getConfig().set("Healer.rentPercent", 100.0);
        Feudal.getConfiguration().getConfig().set("Healer.canCraftGodApple", true);
        Feudal.getConfiguration().getConfig().set("Healer.anyoneCanCraftGodApple", false);
        Feudal.getConfiguration().getConfig().set("Healer.xpPerGoldApple", 1100);
        Feudal.getConfiguration().getConfig().set("Healer.xpPerGodApple", 11000);
        Feudal.getConfiguration().getConfig().set("Healer.xpPerGoldCarrot", 110);

        Feudal.getConfiguration().getConfig().set("Merchant.land", 6);
        Feudal.getConfiguration().getConfig().set("Merchant.MaxLevel", 100);
        Feudal.getConfiguration().getConfig().set("Merchant.EndXP", 12000);
        Feudal.getConfiguration().getConfig().set("Merchant.StartXP", 100);
        Feudal.getConfiguration().getConfig().set("Merchant.MaxPercent", 100.0);
        Feudal.getConfiguration().getConfig().set("Merchant.pointCost", 5);
        Feudal.getConfiguration().getConfig().set("Merchant.taxPercent", 5.0);
        Feudal.getConfiguration().getConfig().set("Merchant.rentPercent", 100.0);
        Feudal.getConfiguration().getConfig().set("Merchant.xpFixed", 10);
        Feudal.getConfiguration().getConfig().set("Merchant.xpRandomAdded", 50);

        Feudal.getConfiguration().getConfig().set("Squire.land", 10);
        Feudal.getConfiguration().getConfig().set("Squire.MaxLevel", 100);
        Feudal.getConfiguration().getConfig().set("Squire.EndXP", 14000);
        Feudal.getConfiguration().getConfig().set("Squire.StartXP", 100);
        Feudal.getConfiguration().getConfig().set("Squire.MaxPercent", 100.0);
        Feudal.getConfiguration().getConfig().set("Squire.pointCost", 7);
        Feudal.getConfiguration().getConfig().set("Squire.taxPercent", 8.0);
        Feudal.getConfiguration().getConfig().set("Squire.rentPercent", 50.0);
        Feudal.getConfiguration().getConfig().set("Squire.damageResistancePercent", 10.0);
        Feudal.getConfiguration().getConfig().set("Squire.xpPerPlayerKill", 18000);
        Feudal.getConfiguration().getConfig().set("Squire.xpPerPlayerKillOnEnemyLand", 18000);
        Feudal.getConfiguration().getConfig().set("Squire.xpPerKingdomMember", 5);
        Feudal.getConfiguration().getConfig().set("Squire.xpPerMobKill", 400);

        Feudal.getConfiguration().getConfig().set("Knight.land", 14);
        Feudal.getConfiguration().getConfig().set("Knight.MaxLevel", 100);
        Feudal.getConfiguration().getConfig().set("Knight.EndXP", 16000);
        Feudal.getConfiguration().getConfig().set("Knight.StartXP", 100);
        Feudal.getConfiguration().getConfig().set("Knight.MaxPercent", 100.0);
        Feudal.getConfiguration().getConfig().set("Knight.pointCost", 50);
        Feudal.getConfiguration().getConfig().set("Knight.taxPercent", 10.0);
        Feudal.getConfiguration().getConfig().set("Knight.rentPercent", 30.0);
        Feudal.getConfiguration().getConfig().set("Knight.damageResistancePercent", 10.0);
        Feudal.getConfiguration().getConfig().set("Knight.xpPerPlayerKill", 17000);
        Feudal.getConfiguration().getConfig().set("Knight.xpPerKingdomMember", 5);
        Feudal.getConfiguration().getConfig().set("Knight.xpPerMobKill", 300);

        Feudal.getConfiguration().getConfig().set("Baron.land", 22);
        Feudal.getConfiguration().getConfig().set("Baron.MaxLevel", 100);
        Feudal.getConfiguration().getConfig().set("Baron.EndXP", 23000);
        Feudal.getConfiguration().getConfig().set("Baron.StartXP", 100);
        Feudal.getConfiguration().getConfig().set("Baron.MaxPercent", 100.0);
        Feudal.getConfiguration().getConfig().set("Baron.pointCost", 50);
        Feudal.getConfiguration().getConfig().set("Baron.taxPercent", 15.0);
        Feudal.getConfiguration().getConfig().set("Baron.rentPercent", 10.0);
        Feudal.getConfiguration().getConfig().set("Baron.xpPerKingdomMember", 35);
        Feudal.getConfiguration().getConfig().set("Baron.xpPerClaimedLand", 12);
        Feudal.getConfiguration().getConfig().set("Baron.xpPerMobKill", 300);

        Feudal.getConfiguration().getConfig().set("King.land", 30);
        Feudal.getConfiguration().getConfig().set("King.MaxLevel", 100);
        Feudal.getConfiguration().getConfig().set("King.EndXP", 40000);
        Feudal.getConfiguration().getConfig().set("King.StartXP", 100);
        Feudal.getConfiguration().getConfig().set("King.MaxPercent", 100.0);
        Feudal.getConfiguration().getConfig().set("King.pointCost", 50);//50
        Feudal.getConfiguration().getConfig().set("King.taxPercent", 20.0);
        Feudal.getConfiguration().getConfig().set("King.rentPercent", 0.0);
        Feudal.getConfiguration().getConfig().set("King.damageResistancePercent", 10.0);
        Feudal.getConfiguration().getConfig().set("King.xpPerPlayerKill", 6500);
        Feudal.getConfiguration().getConfig().set("King.xpPerKingdomMember", 35);
        Feudal.getConfiguration().getConfig().set("King.xpPerClaimedLand", 12);

        Feudal.getConfiguration().getConfig().set("Farmer.permissions", Arrays.asList(new String[]{"list.of.permissions"}));
        Feudal.getConfiguration().getConfig().set("Logger.permissions", Arrays.asList(new String[]{"list.of.permissions"}));
        Feudal.getConfiguration().getConfig().set("Hunter.permissions", Arrays.asList(new String[]{"list.of.permissions"}));
        Feudal.getConfiguration().getConfig().set("Miner.permissions", Arrays.asList(new String[]{"list.of.permissions"}));
        Feudal.getConfiguration().getConfig().set("Cook.permissions", Arrays.asList(new String[]{"list.of.permissions"}));
        Feudal.getConfiguration().getConfig().set("Fisher.permissions", Arrays.asList(new String[]{"list.of.permissions"}));
        Feudal.getConfiguration().getConfig().set("Builder.permissions", Arrays.asList(new String[]{"list.of.permissions"}));
        Feudal.getConfiguration().getConfig().set("Shepherd.permissions", Arrays.asList(new String[]{"list.of.permissions"}));
        Feudal.getConfiguration().getConfig().set("Scribe.permissions", Arrays.asList(new String[]{"list.of.permissions"}));
        Feudal.getConfiguration().getConfig().set("Guard.permissions", Arrays.asList(new String[]{"list.of.permissions"}));
        Feudal.getConfiguration().getConfig().set("Assassin.permissions", Arrays.asList(new String[]{"list.of.permissions"}));
        Feudal.getConfiguration().getConfig().set("Alchemist.permissions", Arrays.asList(new String[]{"list.of.permissions"}));
        Feudal.getConfiguration().getConfig().set("Blacksmith.permissions", Arrays.asList(new String[]{"list.of.permissions"}));
        Feudal.getConfiguration().getConfig().set("Healer.permissions", Arrays.asList(new String[]{"list.of.permissions"}));
        Feudal.getConfiguration().getConfig().set("Merchant.permissions", Arrays.asList(new String[]{"list.of.permissions"}));
        Feudal.getConfiguration().getConfig().set("Squire.permissions", Arrays.asList(new String[]{"list.of.permissions"}));
        Feudal.getConfiguration().getConfig().set("Knight.permissions", Arrays.asList(new String[]{"list.of.permissions"}));
        Feudal.getConfiguration().getConfig().set("Baron.permissions", Arrays.asList(new String[]{"list.of.permissions"}));
        Feudal.getConfiguration().getConfig().set("King.permissions", Arrays.asList(new String[]{"list.of.permissions"}));

        Feudal.getConfiguration().getConfig().set("Farmer.moneyXP", 0.0);
        Feudal.getConfiguration().getConfig().set("Logger.moneyXP", 0.0);
        Feudal.getConfiguration().getConfig().set("Hunter.moneyXP", 0.0);
        Feudal.getConfiguration().getConfig().set("Miner.moneyXP", 0.0);
        Feudal.getConfiguration().getConfig().set("Cook.moneyXP", 0.0);
        Feudal.getConfiguration().getConfig().set("Fisher.moneyXP", 0.0);
        Feudal.getConfiguration().getConfig().set("Builder.moneyXP", 0.0);
        Feudal.getConfiguration().getConfig().set("Shepherd.moneyXP", 0.0);
        Feudal.getConfiguration().getConfig().set("Scribe.moneyXP", 0.0);
        Feudal.getConfiguration().getConfig().set("Guard.moneyXP", 0.0);
        Feudal.getConfiguration().getConfig().set("Assassin.moneyXP", 0.0);
        Feudal.getConfiguration().getConfig().set("Alchemist.moneyXP", 0.0);
        Feudal.getConfiguration().getConfig().set("Blacksmith.moneyXP", 0.0);
        Feudal.getConfiguration().getConfig().set("Healer.moneyXP", 0.0);
        Feudal.getConfiguration().getConfig().set("Merchant.moneyXP", 0.0);
        Feudal.getConfiguration().getConfig().set("Squire.moneyXP", 0.0);
        Feudal.getConfiguration().getConfig().set("Knight.moneyXP", 0.0);
        Feudal.getConfiguration().getConfig().set("Baron.moneyXP", 0.0);
        Feudal.getConfiguration().getConfig().set("King.moneyXP", 0.0);

    }
}