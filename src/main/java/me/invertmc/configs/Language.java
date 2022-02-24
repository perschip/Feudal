package me.invertmc.configs;

import java.io.File;

import me.invertmc.Feudal;
import org.bukkit.configuration.file.FileConfiguration;


public class Language {
    protected static boolean language() throws Exception{
        boolean load = true;
        Feudal.setLanguage(new Configuration(new File(Feudal.getPluginFolder(), "language.yml")));
        try {
            if(Feudal.getLanguage().loadConfig()){
                createLanguageDefaults();
            }
        } catch (Exception e) {
            load = false;

            Feudal.warn("Failed to load config: " + Feudal.getLanguage().getFile().getAbsolutePath() + ".  Please check to make sure the config does not have any syntax errors.");
            try {
                Feudal.getLanguage().broke();
            } catch (Exception e1) {

                Feudal.error("FAILED TO SAVE BROKEN BACKUP OF CONFIG: " + Feudal.getLanguage().getFile().getAbsolutePath());
                throw e1;			}
            try{
                Feudal.getLanguage().loadConfig();
                createLanguageDefaults();
            }catch(Exception e1){
                throw e1;
            }
            throw e;
        }

        updateDefaults();

        try {
            Feudal.getLanguage().save();
        } catch (Exception e) {
            load = false;

            Feudal.error("Failed to save config: " + Feudal.getLanguage().getFile().getAbsolutePath());
            throw e;
        }
        return load;
    }

    private static void createLanguageDefaults() {
        FileConfiguration c = Feudal.getLanguage().getConfig();

        c.set("kingdom.defaultDesc", "Thou shalt not pass! %leader%'s kingdom");

        c.set("luck.heal", "&a...and his servant was healed at that moment. &7(Mat 8:16)");
        c.set("luck.enchant", "&eLooks like you got some lucky enchantments, eh.");

        c.set("noPlayersToTrack", "&cNo players are online to track.");
        c.set("track.pay", "&aYou paid &7$%price% &ato track &7%player%.");
        c.set("track.point", "&6Compass pointer set to &7%player%'s &6general location.");
        c.set("track.tooClose", "&cYou are too close to track that player.");
        c.set("track.wrongWorld", "&cThat player is not in this world.");
        c.set("track.offline", "&cThat player is not online.");
        c.set("track.noMoney", "&cYou do not have enough money to track that player.");


        c.set("xp.p.level", "&6Profession &7[%profession%]&6 leveled up! %level% &7/ &6%max-level%");
        c.set("xp.p.max", "&6&lLeveled up profession: %profession% to level %level%! &b&lMax level reached!");
        c.set("xp.p.change", "&6To change professions: &7/f cp");

        c.set("xp.a.level", "&6Attribute &7[%attribute%]&6 leveled up! %level% &7/ &6%max-level%");
        c.set("xp.a.max", "&6&lLeveled up attribute: %attribute% to level %level%! &b&lMax level reached!");

        c.set("xp.death.loseMoney", "&7$%money% &clost because you died.");
        c.set("xp.death.earnMoney", "&aYou killed &7%player%&a! You earned &7$%money%");
        c.set("xp.death.earnMoneyKingdom", "&aYou killed &7%player%&a! You earned &7$%money% &aYour kingdom earned &7$%kingdom-money%");


        c.set("market.err.open", "&c&lERROR: Failed to open market!");
        c.set("market.cantsell", "&cThat item can not be sold on the market.");
        c.set("market.invalidprice", "&cThat item can not be sold on the market because it does not have a valid buy price.");
        c.set("market.add.merchant", "&aItem added to the market.  You will earn &7$%price% when someone buys the item.");
        c.set("market.add.kingdom", "&aItem added to the market.  You earned &7$%price% &aYour kingdom earned &7$%kingdom-money%");
        c.set("market.add.player", "&aItem added to the market.  You earned &7$%price%");
        c.set("market.sold.kingdom", "&aItem sold on market. [%amount%]  You earned &7$%price% &aYour kingdom earned &7$%kingdom-money%");
        c.set("market.sold.player", "&aItem sold on market. [%amount%]  You earned &7$%price%");
        c.set("market.buy", "&aItem bought from market. [%amount%]  You spent &7$%price%");


        c.set("land.enter", "%color%%kingdom% &7 %desc%");
        c.set("land.shielded", "&cThis land is shielded.");
        c.set("land.deny.break", "&cYou do not have permission to break blocks on this land.");
        c.set("land.deny.place", "&cYou do not have permission to place blocks on this land.");
        c.set("land.tochallenge", "&cTo challenge this kingdom type &7/f enemy %kingdom%");
        c.set("land.deny.ally", "&cYou can not challenge your allies.");
        c.set("land.vacation", "&cThis land is on vacation.");
        c.set("land.deny.chest", "&cYou do not have permission to open chests on this land.");
        c.set("land.deny.fire", "&cYou do not have permission to light fires on this land.");
        c.set("land.deny.interact", "&cYou do not have permission to interact with blocks on this land.");
        c.set("land.attack.mem", "&cYou can not attack kingdom members.");
        c.set("land.attack.ally", "&cYou can not attack your allies.");


        c.set("kingdom.ban.info", "&6%kingdom% - Ends in: &7%age%");
        c.set("kingdom.ban.title", "&6&l===[&4&lChallenge Bans: &7&l%kingdom%&6&l]===");
        c.set("kingdom.ban.none", "&cThis kingdom currently has no challenge bans.");
        c.set("kingdom.delete", "&7%kingdom% &ckingdom deleted by: &7%name%");
        c.set("kingdom.leader", "&a&lYou have been promoted to leader!");
        c.set("kingdom.leaderall", "&e%leader% is the new leader of your kingdom!");
        c.set("kingdom.set.enemy", "&cYour kingdom is now enemies with %kingdom%");
        c.set("kingdom.enemy", "&c%kingdom% has made your kingdom their enemy!");
        c.set("kingdom.set.neutral", "&fYour kingdom is now neutral with %kingdom%");
        c.set("kingdom.set.ally", "&aYour kingdom is now allied with %kingdom%");
        c.set("kingdom.tax.fail", "&cFailed to pay land tax. Unclaimed: &7%x%, %z%");
        c.set("kingdom.tax.remove", "&7$%tax% &6removed from treasury for land taxes. &6Treasury: &7$%money%");

        c.set("kingdom.info.title", "&4&l==<&6&l%kingdom% Info &7(%online%/%total%)&4&l>==");
        c.set("kingdom.info.desc", "&6Desc: &7%desc%");
        c.set("kingdom.info.ally", "&6Allies: &7%allies%");
        c.set("kingdom.info.enemy", "&6Enemies: &7%enemies%");
        c.set("kingdom.info.mem", "&6Members: %members%");
        c.set("kingdom.info.age", "&6Age: &7%age%");
        c.set("kingdom.info.open", "&6Open Joining: &7%open%");
        c.set("kingdom.info.land", "&6Land: &7%land% / %total%");
        c.set("kingdom.info.landtax", "&6Hourly Land Tax: &7$%tax%");
        c.set("kingdom.info.treasury", "&6Treasury: &7$%money%");
        c.set("kingdom.info.incometax", "&6Income Tax: &7%tax%%");
        c.set("kingdom.info.challenges", "&6Challenges (wins / total): &7%won% / %total%");
        c.set("kingdom.info.online", "&6Last Online: &7%online%");
        c.set("kingdom.info.shield", "&6Shield: &7%time%");


        c.set("challenge.shield", "&cYour kingdom currently has a shield. You can not make challenges while shielded.");
        c.set("challenge.ban", "&cYou are currently not allowed to challenge this kingdom.  This challenge ban is to prevent instant counter attacks.");
        c.set("challenge.max", "&cThis kingdom has reached its max of &7%max% &cdefence challenges.");
        c.set("challenge.broadcast", "&7%attacker%&4 has challenged &7%defender%");
        c.set("challenge.cd", "&7%attacker% &chas challenged your kingdom! &6Use &7/f challenge accept %attacker% &6to accept the challenge.");
        c.set("challenge.ca", "&aYour kingdom has challenged &7%defender%. &6Use &7/f challenge &6for help.");
        c.set("challenge.needland", "&cYou must have land in order to make a challenge.");
        c.set("challenge.already", "&cYou already have a challenge with &7%defender%");
        c.set("challenge.breakbanner", "&cThis banner is apart of a challenge.");
        c.set("challenge.claimAge", "&cYou must own land for &7%time% &cbefore you can start a challenge.");

        c.set("c.tax.hour.tax", "&7$%tax%&c removed from treasury for challenge tax against &7%attacker%. &cAccept the challenge to stop this hourly tax.");
        c.set("c.tax.hour.fail", "&cFAILED to pay challenge tax against &7%attacker%");
        c.set("c.tax.hour.win", "&7%defender% failed to pay the challenge tax.");
        c.set("c.tax.minute", "&7$%tax%&c removed from treasury for challenge tax against &7%attacker%.");
        c.set("c.tax.command", "&cDo &7/f challenge accept %attacker% &cto stop this tax.");
        c.set("c.tax.t", "&cTreasury: &7$%money%");
        c.set("c.a", "&4%defender% &6has accepted your challenge.");

        c.set("c.win.defender.default.d", "&aYou won the challenge (by default) defending against &7%attacker%.");
        c.set("c.win.defender.default.a", "&cYou lost the challenge (by default) while attacking &7%defender%.");
        c.set("c.win.attacker.default.a", "&aYou won the challenge (by default) attacking &7%defender%. &aYou gained &7$%money% &6 of their treasury.");
        c.set("c.win.attacker.default.d", "&cYou lost the challenge (by default) defending against &7%attacker%. &cYou lost your land where the fight took place. The attackers won &7$%money% &6of your kingdom's treasury.");
        c.set("c.win.defender.d", "&cYou failed your attack against &7%defender% &cThe enemy spies have found your kingdom!");
        c.set("c.win.defender.a", "&aYou won the defence against &7%attacker% &aThe enemy's kingdom has been located: &7%loc%");
        c.set("c.win.attacker.a", "&aYou won the challenge attacking &7%defender%. &aYou win their land and you get &7$%money% &6 of their treasury.");
        c.set("c.win.attacker.d", "&cYou lost the challenge defending against &7%attacker%. &cThey win your land where the fight took place. They also won &7$%money% &6of your kingdom's treasury.");

        c.set("c.err.1", "&cError occured while figuring out who won the challenge.");
        c.set("c.b.d", "&7%defender%&a successfully defended against &7%attacker%");
        c.set("c.b.a", "&7%attacker%&a defeated &7%defender%");

        c.set("c.timer.attack", "&cYour kingdom has &7%time% &cto return to the fight with &4%defender%");
        c.set("c.timer.defence", "&4%attacker% &cis capturing your land! You have %time% &cto return to your land.");

        c.set("c.attacker.l.a", "&4Your kingdom left the fight! &cYou have &7%time% &c to return to the fight against &4%defender%");
        c.set("c.attacker.l.d", "&4%attacker% &ahas left the fight. &aThey have &7%time% &ato return to the fight.");

        c.set("c.attacker.e.a", "&aYour kingdom has entered the fight!");
        c.set("c.attacker.e.d", "&4%attacker% &chas entered the fight!");

        c.set("c.defender.l.a", "&aCapturing land! The defenders have &7%time% &ato get back on defence.");
        c.set("c.defender.l.d", "&4%attacker% &cis on your land! You have &7%time% &cto return to your land to protect it! &cDo &7/f challenge tp %attacker%");

        c.set("c.defender.e.a", "&aYour kingdom has entered the fight!");
        c.set("c.defender.e.d", "&4%defender% &chas entered the fight!");

        c.set("c.infight.d", "&6Fight with &7%attacker% &6will start after they finish the fight they are in.");
        c.set("c.infight.a", "&6Fighting against &7%defender% &6will start after they finish the fight they are in.");

        c.set("c.s.sep", "&4&l====================================");
        c.set("c.s.a1", "&4Attack started against &7%defender%");
        c.set("c.s.a2", "&6Teleport to the fight: &7/f challenge tp %defender%");
        c.set("c.s.a3", "&cYour kingdom has &7%time% &cto start attacking.");
        c.set("c.s.d1", "&4Defence started against &7%attacker%");
        c.set("c.s.d2", "&6Teleport to the fight: &7/f challenge tp %attacker%");
        c.set("c.s.d3", "&7%attacker% &chas &7%time% &cto start attacking you.");


        c.set("reputation.maxP", "Maxing out your profession");
        c.set("reputation.placeChallenge", "Placed Challenge");
        c.set("reputation.acceptChallenge", "Accepting Challenge");
        c.set("reputation.winDefault", "Winning a challenge by default");
        c.set("reputation.loseDefault", "Losing a challenge by default");
        c.set("reputation.win", "Winning a challenge");
        c.set("reputation.lose", "Losing a challenge");
        c.set("reputation.leaveKingdom", "Leaving your kingdom.");
        c.set("reputation.enemyAlly", "Setting an ally as an enemy.");
        c.set("reputation.neutralAlly", "Setting an ally as neutral.");
        c.set("reputation.ignoreChallengePerHour", "Ignoring challenge");
        c.set("reputation.changeProfessionWithoutMax", "Changing professions without reaching the max level.");
        c.set("reputation.failToPayLandTax", "Your kingdom failed to pay its land tax");
        c.set("reputation.deleteKingdom", "Deleting kingdom");
        c.set("reputation.kickPlayerFromKingdom", "Kicking a kingdom member");
        c.set("reputation.buyVacation", "Using a vacation shield");
        c.set("reputation.changed", "&eReputation changed by: &7%player%");
        c.set("reputation.c", "&6Reputation changed: &7%change% &6Reason: &7%reason%");
        c.set("reputation.n", "&6New Reputation: &7%rep%");
        c.set("reputation.attackPreKingdom", "During a challenge, you killed a kingdom member of a kingdom you were once in.");

        c.set("offlineMsg.start", "&6&lServer Messages:");
        c.set("offlineMsg.list", "&6- %msg%");

        c.set("selection.np", "&cYou do not have enough points.");
        c.set("selection.maxAtt", "&cAttribute limit has been reached.");
        c.set("selection.deny", "&cAccess denied. Choose a different profession.");
        c.set("selection.s", "&cStrength level maxed out.");
        c.set("selection.t", "&cToughness level maxed out.");
        c.set("selection.sp", "&cSpeed level maxed out.");
        c.set("selection.st", "&cStamina level maxed out.");
        c.set("selection.l", "&cLuck level maxed out.");
        c.set("selection.less0", "&cAttributes can not have less than 0 levels.");
        c.set("selection.c", "&a&lCharacter setup complete!");
        c.set("selection.cf", "&cFailed to save character data. Please relog.");
        c.set("selection.cmd", "&cYou can not use any commands until you setup your character. &6Type /feudal to open the character setup.");
        c.set("selection.unull", "&cYour user data is missing or has failed to load. Relog to repair this. If that does not work notify a server admin.");
        c.set("selection.forceUsePoints", "&cYou must use all your points before ending the setup");

        c.set("a.noperm", "&cYou do not have permission.");
        c.set("a.1", "&cHelp page must be an integer.");
        c.set("a.2", "&4&l<&9&lTutorial&4&l>");
        c.set("a.3", "&6Short Tutorial Video: &7%a%");
        c.set("a.4", "&6Full Tutorial Video: &7%a%");
        c.set("a.5", "&6Feudal Wiki: &7http://Feudal.coremod.com");
        c.set("a.6", "&cThe price must be a number.");
        c.set("a.7", "&cThe sell price must be greater than 0.");
        c.set("a.8", "&cYou must be a merchant to sell items for a custom price.");
        c.set("a.ufail", "&cFailed to load your user data!");
        c.set("a.9", "&aSell Price: &c$%p%");
        c.set("a.10", "&aYour Sell Price: &c$%p% &7This price is different because you do not have the profession bonus for this item.");
        c.set("a.11", "&cYou must hold an item in your hand.");
        c.set("a.12", "&cInvalid player: %p%");
        c.set("a.13", "&cPlayer data not available...");
        c.set("a.14", "&cFailed to load user data for user: %p%");

        c.set("commands.inviteReputation", "&7%player% &ehas &7%rep% &ereputation. Reputation starts at &7%start%.");
        c.set("commands.78", "&e&l>> &4&lFeudal &e&l<<");
        c.set("commands.activeChallenges", "&4&l<&6&lActive Challenges&4&l>");
        c.set("commands.81", "&6About: &7Feudal is a minecraft server plugin which creates an open world RPG gameplay in minecraft.  You can create kingdoms (groups).  You group together with other players and can ally with others.  You get to fight others to take their land and money.  When you first start you get to setup your character which include your social class and profession and your attributes (skills).  There are five attributes: Strength, Toughness, Speed, Stamina, and Luck.  Strength increases how fast you can mine and it gives a booster to all forms of attacks to players and mobs.  Toughness increases your health and regeneration speed.  Speed increases how fast you can run or walk.  Stamina decreases the rate your hunger bar goes down.  Luck makes you have a higher chance for about anything which has a chance of happening.  You can level up your profession and attributes by doing the job for your profession.  For example, if you are a logger then you level up if you cut down trees.  For more information or help about feudal do /feudal tutorial.");
        c.set("commands.158", "&6&l--<&4&l%a%&6&l>--");
        c.set("commands.160", "&6Profession: &7%a%");
        c.set("commands.162", "&6Strength: &7%a% / %b% - %c%XP (DMG:%d%x, HASTE:%e%)");
        c.set("commands.164", "&6Toughness: &7%a% / %b% - %c%XP (REGEN:%d%x, HP:%e%)");
        c.set("commands.165", "&6Speed: &7%a% / %b% - %c%XP (SPEED:%d%)");
        c.set("commands.166", "&6Stamina: &7%a% / %b% - %c%XP (STAMINA:%d%)");
        c.set("commands.167", "&6Luck: &7%a% / %b% - %c%XP (CHANCE:%d%%)");
        c.set("commands.attOverall", "&6Attributes: &7%a% / %b%");
        c.set("commands.170", "&6Kingdom: &7: %a%");
        c.set("commands.172", "&6Kingdom: &7NONE");
        c.set("commands.174", "&6Reputation: &7%a%");
        c.set("commands.175", "&6Online last: &7%a%");
        c.set("commands.185", "&6Previous Professions: &7%a%");
        c.set("commands.197", "&6&l--<&4&l%a%&6&l>--");
        c.set("commands.199", "&6Profession: &7%a%");
        c.set("commands.201", "&6Strength: &7%a% / %b% - %c%XP (DMG:%d%x, HASTE:%e%)");
        c.set("commands.203", "&6Toughness: &7%a% / %b% - %c%XP (REGEN:%d%x, HP:%e%)");
        c.set("commands.204", "&6Speed: &7%a% / %b% - %c%XP (SPEED:%d%)");
        c.set("commands.205", "&6Stamina: &7%a% / %b% - %c%XP (STAMINA:%d%)");
        c.set("commands.206", "&6Luck: &7%a% / %b% - %c%XP (CHANCE:%d%%)");
        c.set("commands.209", "&6Kingdom: &7: %a%");
        c.set("commands.211", "&6Kingdom: &7NONE");
        c.set("commands.213", "&6Reputation: &7%a%");
        c.set("commands.214", "&6Online last: &7%a%");
        c.set("commands.224", "&6Previous Professions: &7%a%");
        c.set("commands.246", "&6&l--<&4&l%a%'s Speed&6&l>--");
        c.set("commands.247", "&6Speed level: &7%a% / %b%");
        c.set("commands.248", "&6XP Needed: &7%a%");
        c.set("commands.249", "&6Speed: &7%a%");
        c.set("commands.251", "&cFailed to load user data for user: %a%");
        c.set("commands.257", "&6&l--<&4&lYour Speed&6&l>--");
        c.set("commands.258", "&6Speed level: &7%a% / %b%");
        c.set("commands.259", "&6XP Needed: &7%a%");
        c.set("commands.260", "&6Speed: &7%a%");
        c.set("commands.278", "&6&l--<&4&l%a%'s Strength&6&l>--");
        c.set("commands.279", "&6Strength level: &7%a% / %b%");
        c.set("commands.280", "&6XP Needed: &7%a%");
        c.set("commands.281", "&6Damage: &7%a%x");
        c.set("commands.282", "&6Haste: &7%a%");
        c.set("commands.284", "&cFailed to load user data for user: %a%");
        c.set("commands.290", "&6&l--<&4&lYour Strength&6&l>--");
        c.set("commands.291", "&6Strength level: &7%a% / %b%");
        c.set("commands.292", "&6XP Needed: &7%a%");
        c.set("commands.293", "&6Damage: &7%a%x");
        c.set("commands.294", "&6Haste: &7%a%");
        c.set("commands.312", "&6&l--<&4&l%a%'s Toughness&6&l>--");
        c.set("commands.313", "&6Toughness level: &7%a% / %b%");
        c.set("commands.314", "&6XP Needed: &7%a%");
        c.set("commands.315", "&6Regen: &7%a%x");
        c.set("commands.316", "&6Health: &7%a%");
        c.set("commands.318", "&cFailed to load user data for user: %a%");
        c.set("commands.324", "&6&l--<&4&lYour Toughness&6&l>--");
        c.set("commands.325", "&6Toughness level: &7%a% / %b%");
        c.set("commands.326", "&6XP Needed: &7%a%");
        c.set("commands.327", "&6Regen: &7%a%x");
        c.set("commands.328", "&6Health: &7%a%");
        c.set("commands.346", "&6&l--<&4&l%a%'s Stamina&6&l>--");
        c.set("commands.347", "&6Stamina level: &7%a% / %b%");
        c.set("commands.348", "&6XP Needed: &7%a%");
        c.set("commands.349", "&6Stamina: &7%a%");
        c.set("commands.351", "&cFailed to load user data for user: %a%");
        c.set("commands.357", "&6&l--<&4&lYour Stamina&6&l>--");
        c.set("commands.358", "&6Stamina level: &7%a% / %b%");
        c.set("commands.359", "&6XP Needed: &7%a%");
        c.set("commands.360", "&6Stamina: &7%a%");
        c.set("commands.378", "&6&l--<&4&l%a%'s Luck&6&l>--");
        c.set("commands.379", "&6Luck level: &7%a% / %b%");
        c.set("commands.380", "&6XP Needed: &7%a%");
        c.set("commands.381", "&6Chance: &7%a%%");
        c.set("commands.383", "&cFailed to load user data for user: %a%");
        c.set("commands.389", "&6&l--<&4&lYour Luck&6&l>--");
        c.set("commands.390", "&6Luck level: &7%a% / %b%");
        c.set("commands.391", "&6XP Needed: &7%a%");
        c.set("commands.392", "&6Chance: &7%a%%");
        c.set("commands.410", "&6&l--<&4&l%a%'s Profession&6&l>--");
        c.set("commands.411", "&6Profession: &7%a%");
        c.set("commands.412", "&6Profession Level: &7%a% / %b%");
        c.set("commands.413", "&6XP Needed: &7%a%");
        c.set("commands.422", "&6Previous Professions: &7%a%");
        c.set("commands.425", "&cFailed to load user data for user: %a%");
        c.set("commands.431", "&6&l--<&4&lYour Profession&6&l>--");
        c.set("commands.432", "&6Profession: &7%a%");
        c.set("commands.433", "&6Profession Level: &7%a% / %b%");
        c.set("commands.434", "&6XP Needed: &7%a%");
        c.set("commands.443", "&6Previous Professions: &7%a%");
        c.set("commands.465", "&4&l--[&6&l%a%&4&l]--");
        c.set("commands.466", "&6Current time: &7%a%");
        c.set("commands.467", "&6Online last: &7%a%");
        c.set("commands.470", "&6Online percent: &7%a%%");
        c.set("commands.474", "%a%");
        c.set("commands.477", "&cFailed to load user data for user: %a%");
        c.set("commands.481", "&cUsage: &7/f online <player>");
        c.set("commands.498", "&cFailed to load user data for user: %a%");
        c.set("commands.502", "&cYou can not change another player's profession.");
        c.set("commands.522", "&cThe level must be an integer.");
        c.set("commands.534", "&cFailed to load user data for user: %a%");
        c.set("commands.548", "&cProfession level must be greater than or equal to 0.");
        c.set("commands.550", "&cProfession level exceeds max level of %a%");
        c.set("commands.554", "&aSet \"&e%a%&a\"'s profession level to %b%.");
        c.set("commands.557", "&cNULL profession data for user: %a%");
        c.set("commands.560", "&cUsage: /f setProfessionLevel <level> [player]");
        c.set("commands.572", "&cThe level must be an integer.");
        c.set("commands.584", "&cFailed to load user data for user: %a%");
        c.set("commands.598", "&cSpeed level must be greater than or equal to 0.");
        c.set("commands.600", "&cSpeed level exceeds max level of %a%");
        c.set("commands.605", "&aSet \"&e%a%&a\"'s speed level to %b%.");
        c.set("commands.608", "&cNULL speed data for user: %a%");
        c.set("commands.611", "&cUsage: /f setSpeedLevel <level> [player]");
        c.set("commands.623", "&cThe level must be an integer.");
        c.set("commands.635", "&cFailed to load user data for user: %a%");
        c.set("commands.649", "&cStrength level must be greater than or equal to 0.");
        c.set("commands.651", "&cStrength level exceeds max level of %a%");
        c.set("commands.656", "&aSet \"&e%a%&a\"'s strength level to %b%.");
        c.set("commands.659", "&cNULL strength data for user: %a%");
        c.set("commands.662", "&cUsage: /f setStrengthLevel <level> [player]");
        c.set("commands.674", "&cThe level must be an integer.");
        c.set("commands.686", "&cFailed to load user data for user: %a%");
        c.set("commands.700", "&cToughness level must be greater than or equal to 0.");
        c.set("commands.702", "&cToughness level exceeds max level of %a%");
        c.set("commands.707", "&aSet \"&e%a%&a\"'s toughness level to %b%.");
        c.set("commands.710", "&cNULL toughness data for user: %a%");
        c.set("commands.713", "&cUsage: /f setToughnessLevel <level> [player]");
        c.set("commands.725", "&cThe level must be an integer.");
        c.set("commands.737", "&cFailed to load user data for user: %a%");
        c.set("commands.751", "&cStamina level must be greater than or equal to 0.");
        c.set("commands.753", "&cStamina level exceeds max level of %a%");
        c.set("commands.758", "&aSet \"&e%a%&a\"'s stamina level to %b%.");
        c.set("commands.761", "&cNULL stamina data for user: %a%");
        c.set("commands.764", "&cUsage: /f setStaminaLevel <level> [player]");
        c.set("commands.776", "&cThe level must be an integer.");
        c.set("commands.788", "&cFailed to load user data for user: %a%");
        c.set("commands.802", "&cLuck level must be greater than or equal to 0.");
        c.set("commands.804", "&cLuck level exceeds max level of %a%");
        c.set("commands.809", "&aSet \"&e%a%&a\"'s luck level to %b%.");
        c.set("commands.812", "&cNULL luck data for user: %a%");
        c.set("commands.815", "&cUsage: /f setLuckLevel <level> [player]");
        c.set("commands.852", "&aReset: %a%");
        c.set("commands.854", "&cFailed to load user data for user: %a%");
        c.set("commands.858", "&cYou must enter a player to be reset.");
        c.set("commands.867", "&cA kingdom name must be at least &6%a%&c characters long.");
        c.set("commands.871", "&cA kingdom name's length must be less than or equal to &6%a%&c characters.");
        c.set("commands.875", "&cThat name is not allowed.");
        c.set("commands.883", "&cThe name &6%a%&c is banned on this server.");
        c.set("commands.notAlpha", "&cA kingdom name must be characters only. No numbers or symbols.");
        c.set("commands.890", "&cThat kingdom already exists.");
        c.set("commands.896", "&cFailed to load your user data.");
        c.set("commands.900", "&cYou are currently in a kingdom.");
        c.set("commands.904", "&cYou are an assassin and assassins can not create or be a leader of a kingdom.");
        c.set("commands.915", "&cKingdom data file already exists! Please try again.");
        c.set("commands.921", "&cFailed to create kingdom data file.");
        c.set("commands.935", "&cFailed to load config for kingdom.");
        c.set("commands.943", "&cFailed to create kingdom!");
        c.set("commands.951", "&cFailed to create kingdom! Could not save data.");
        c.set("commands.958", "&a&lKingdom successfully created!");
        c.set("commands.961", "&cYou must enter a kingdom name.");
        c.set("commands.984", "&cThat kingdom does not exist.");
        c.set("commands.989", "&cFailed to load your user data.");
        c.set("commands.993", "&cYou are currently in a kingdom.");
        c.set("commands.1004", "&cFailed to join kingdom!");
        c.set("commands.1011", "&aKingdom joined: &7%a%");
        c.set("commands.1013", "&cYou must be invited to that kingdom to join.");
        c.set("commands.1016", "&cYou must enter a kingdom name.");
        c.set("commands.1026", "&cFailed to load your user data.");
        c.set("commands.1030", "&cYou are not in a kingdom.");
        c.set("commands.1037", "&c%a% is currently in a kingdom.");
        c.set("commands.1046", "&a%a% invited you to join his kingdom: &e%b%");
        c.set("commands.1050", "&a%a% has been invited to your kingdom.");
        c.set("commands.1052", "&e%a% has been invited to your kingdom.  &cThey did not receive a notification because they are not online.");
        c.set("commands.1056", "&cThat user has already been invited to your kingdom. &6%a% has been removed from the invite list.");
        c.set("commands.1059", "&cYou must be a member, executive, leader to invite people to your kingdom.");
        c.set("commands.1062", "&cInvalid kingdom rank.");
        c.set("commands.1066", "&cInvalid player.");
        c.set("commands.1069", "&cYou must enter a player's name.");
        c.set("commands.1078", "&cFailed to load your user data.");
        c.set("commands.1082", "&cYou are not in a kingdom.");
        c.set("commands.1087", "&cYou are not in a kingdom.");
        c.set("commands.1098", "&cCommand usage: /f setOpen [true/false]");
        c.set("commands.1107", "&cError saving kingdom.");
        c.set("commands.1111", "&aKingdom open: &7%a%");
        c.set("commands.1114", "&cError obtaining kingdom rank.");
        c.set("commands.1118", "&6Kingdom joining: &7OPEN");
        c.set("commands.1120", "&aDo &7/f setOpen false &ato only let invited people join.");
        c.set("commands.1123", "&6Kingdom joining: &7INVITE ONLY");
        c.set("commands.1125", "&aDo &7/f setOpen true &ato have people join without an invite.");
        c.set("commands.1150", "&aConfirm deletion: Type the delete command again within 30 seconds to delete your kingdom.");
        c.set("commands.1153", "&cYou do not have permission to delete other kingdoms.");
        c.set("commands.1158", "&cFailed to load your user data.");
        c.set("commands.1162", "&cYou are not in a kingdom.");
        c.set("commands.1167", "&cYou are not in a kingdom.");
        c.set("commands.1176", "&aConfirm deletion: Type the delete command again within 30 seconds to delete your kingdom.");
        c.set("commands.1196", "&cFailed to load your user data.");
        c.set("commands.1200", "&cYou are not in a kingdom.");
        c.set("commands.1205", "&cYou are not in a kingdom.");
        c.set("commands.1213", "&cError getting rank for your kingdom.");
        c.set("commands.1219", "&cThat player does not exist.");
        c.set("commands.1228", "&cFailed to save kingdom.");
        c.set("commands.1232", "&a%a% has been set to the new leader.");
        c.set("commands.1234", "&cThat player is not a member of this kingdom.");
        c.set("commands.1238", "&cYou must be the leader of your kingdom to set another user as a leader.");
        c.set("commands.1241", "&cYou must enter a player's name.");
        c.set("commands.1250", "&cFailed to load your user data.");
        c.set("commands.1254", "&cYou are not in a kingdom.");
        c.set("commands.1259", "&cYou are not in a kingdom.");
        c.set("commands.1264", "&cError getting rank for your kingdom.");
        c.set("commands.1268", "&cYou are the only member in your kingdom. Delete your kingdom to leave.");
        c.set("commands.1272", "&cYou are the leader of your kingdom. Give leadership to another member to leave.");
        c.set("commands.1280", "&cFailed to save kingdom.");
        c.set("commands.1289", "&aYou have left your kingdom: %a%");
        c.set("commands.1298", "&cFailed to load your user data.");
        c.set("commands.1302", "&cYou are not in a kingdom.");
        c.set("commands.1307", "&cYou are not in a kingdom.");
        c.set("commands.1312", "&cError getting rank for your kingdom.");
        c.set("commands.1331", "&cThat kingdom does not exist.");
        c.set("commands.1345", "&cYou are already enemies with that kingdom.");
        c.set("commands.1353", "&4FAILED to save kingdom data!");
        c.set("commands.1359", "&cYou must be an executive or the leader of your kingdom to do this.");
        c.set("commands.1362", "&cEnter a kingdom to set as an enemy.");
        c.set("commands.1372", "&cFailed to load your user data.");
        c.set("commands.1376", "&cYou are not in a kingdom.");
        c.set("commands.1381", "&cYou are not in a kingdom.");
        c.set("commands.1386", "&cError getting rank for your kingdom.");
        c.set("commands.1405", "&cThat kingdom does not exist.");
        c.set("commands.1416", "&4FAILED to save kingdom data!");
        c.set("commands.1440", "&4FAILED to save kingdom data!");
        c.set("commands.1445", "&cYou are already neutral with that kingdom.");
        c.set("commands.1448", "&cYou must be an executive or the leader of your kingdom to do this.");
        c.set("commands.1451", "&cEnter a kingdom to set as neutral.");
        c.set("commands.1461", "&cFailed to load your user data.");
        c.set("commands.1465", "&cYou are not in a kingdom.");
        c.set("commands.1470", "&cYou are not in a kingdom.");
        c.set("commands.1475", "&cError getting rank for your kingdom.");
        c.set("commands.1494", "&cThat kingdom does not exist.");
        c.set("commands.1505", "&4FAILED to save kingdom data!");
        c.set("commands.1515", "&cYou are already allied with that kingdom.");
        c.set("commands.1518", "&cYou must be an executive or the leader of your kingdom to do this.");
        c.set("commands.1521", "&cEnter a kingdom to set as an ally.");
        c.set("commands.1531", "&cFailed to load your user data.");
        c.set("commands.1535", "&cYou are not in a kingdom.");
        c.set("commands.1540", "&cYou are not in a kingdom.");
        c.set("commands.1545", "&cError getting rank for your kingdom.");
        c.set("commands.1559", "&cYou can not kick yourself.");
        c.set("commands.1568", "&4Failed to save kingdom data!");
        c.set("commands.1576", "&5[&6%a%&5] &cYou have been kicked from your kingdom by %b%");
        c.set("commands.1581", "&cYou do not have permission to kick that player.");
        c.set("commands.1584", "&cFailed to check that user's rank.");
        c.set("commands.1587", "&cThat player is not in your kingdom.");
        c.set("commands.1594", "&cEnter a player to kick.");
        c.set("commands.1604", "&cFailed to load your user data.");
        c.set("commands.1608", "&cYou are not in a kingdom.");
        c.set("commands.1613", "&cYou are not in a kingdom.");
        c.set("commands.1618", "&cError getting rank for your kingdom.");
        c.set("commands.1626", "&cYou can not claim or unclaim land while in a defence challenge.");
        c.set("commands.1638", "&cYou must enter a valid number for the radius.");
        c.set("commands.1642", "&cThe radius must be greater than 0.");
        c.set("commands.1644", "&cThe radius must be less than or equal to 6.");
        c.set("commands.1668", "&c%a% currently owns this land.  Challenge them to take their land.%b% Land: %c%, ");
        c.set("commands.1680", "&cLand: &7%a%, %b% &ccan not be claimed.");
        c.set("commands.1683", "&cYou can only claim land which is connected to other land you own.");
        c.set("commands.1712", "&4Failed to save kingdom data!");
        c.set("commands.1717", "&cNOTICE &e> &6Hourly land tax: &7$%a%");
        c.set("commands.1726", "&eYour kingdom already owns this land: %a%, %b%");
        c.set("commands.1728", "&c%a% currently owns this land.  Challenge them to take their land.");
        c.set("commands.1742", "&4Failed to save kingdom data!");
        c.set("commands.1748", "&cNOTICE &e> &6Hourly land tax: &7$%a%");
        c.set("commands.1750", "&cLand: &7%a%, %b% &ccan not be claimed.");
        c.set("commands.1753", "&cYou can only claim land which is connected to other land you own.");
        c.set("commands.1757", "&cYour kingdom has reached it's maximum amount of land: %a% chunks");
        c.set("commands.1761", "&cYou do not have permission to claim land for your kingdom.");
        c.set("commands.1771", "&cFailed to load your user data.");
        c.set("commands.1775", "&cYou are not in a kingdom.");
        c.set("commands.1780", "&cYou are not in a kingdom.");
        c.set("commands.1785", "&cError getting rank for your kingdom.");
        c.set("commands.1793", "&cYou can not claim or unclaim land while in a defence challenge.");
        c.set("commands.1801", "&cYour kingdom does not own any land.");
        c.set("commands.1812", "&6Unclaimed land: &7%a%, %b%");
        c.set("commands.1818", "&4Failed to save kingdom data!");
        c.set("commands.1828", "&cYou must enter a valid number for the radius.");
        c.set("commands.1832", "&cThe radius must be greater than 0.");
        c.set("commands.1834", "&cThe radius must be less than 6.");
        c.set("commands.1860", "&6Unclaimed land: &7%a%, %b%");
        c.set("commands.1862", "&cUmm... something went wrong. Let the server owner know that Feudal had an error. Lets see... call it error YO MAMA because the only way this error could ever happen is if someone's mom was involved..");
        c.set("commands.1891", "&4Failed to save kingdom data!");
        c.set("commands.1896", "&cNo land found to unclaim.");
        c.set("commands.1911", "&4Failed to save kingdom data!");
        c.set("commands.1915", "&eUnclaimed land: %a%, %b%");
        c.set("commands.1917", "&cUmm... something went wrong. Let the server owner know that Feudal had an error. Lets see... call it error YO MAMA because the only way this error could ever happen is if someone's mom was involved..");
        c.set("commands.1920", "&cYour kingdom does not own this land.");
        c.set("commands.1924", "&cYou do not have permission to unclaim land for your kingdom.");
        c.set("commands.1934", "&cFailed to load your user data.");
        c.set("commands.1938", "&cYou are not in a kingdom.");
        c.set("commands.1943", "&cYou are not in a kingdom.");
        c.set("commands.1948", "&cError getting rank for your kingdom.");
        c.set("commands.1956", "&aKingdom home set!");
        c.set("commands.1959", "&4Failed to save kingdom data!");
        c.set("commands.1962", "&cYour kingdom home must be on your kingdom land.");
        c.set("commands.1965", "&cYou must be executive or leader of your kingdom to set the kingdom home.");
        c.set("commands.1974", "&cFailed to load your user data.");
        c.set("commands.1978", "&cYou are not in a kingdom.");
        c.set("commands.1983", "&cYou are not in a kingdom.");
        c.set("commands.1988", "&cYour kingdom does not have a home set.");
        c.set("commands.1993", "&aTeleporting in &7%a% &aseconds. Don't move.");
        c.set("commands.2000", "&4&l> &e&lTELEPORTING &4&l<");
        c.set("commands.2003", "&cYou moved. Teleport canceled.");
        c.set("commands.2018", "&cFailed to load your user data.");
        c.set("commands.2096", "&6Kingdom: %a%");
        c.set("commands.2098", "&6Kingdom: %a%");
        c.set("commands.2108", "%a%");
        c.set("commands.2110", "%a%");
        c.set("commands.2112", "&cAn error occured while creating the kingdom map!");
        c.set("commands.2135", "&cThat kingdom does not exist.");
        c.set("commands.2142", "&cFailed to load your user data.");
        c.set("commands.2146", "&cYou are not in a kingdom.");
        c.set("commands.2151", "&cYou are not in a kingdom.");
        c.set("commands.2164", "&cFailed to load your user data.");
        c.set("commands.2168", "&cYou are not in a kingdom.");
        c.set("commands.2173", "&cYou are not in a kingdom.");
        c.set("commands.2178", "&cError getting rank for your kingdom.");
        c.set("commands.2187", "&cYou must enter a valid number for the amount.");
        c.set("commands.2198", "&cFailed to save kingdom or user data!");
        c.set("commands.2202", "&7$%a% &6added to the treasury. New treasury balance: &7$%b%");
        c.set("commands.2204", "&cYou do not have enough money to add: &7$%a% &cBalance: &7$%b%");
        c.set("commands.2215", "&cFailed to save kingdom or user data!");
        c.set("commands.2219", "&7$%a% &6removed from the treasury. New treasury balance: &7$%b%");
        c.set("commands.2221", "&cThe treasury does not have enough money to remove: &7$%a% &cTreasury: &7$%b%");
        c.set("commands.2224", "&cYou do not have permission to take money from your kingdom's treasury.");
        c.set("commands.2227", "&cUsage: &7/f treasury <add/remove> <amount>");
        c.set("commands.2230", "&6Kingdom treasury: &7$%a%");
        c.set("commands.2232", "&cInvalid command. Do /f help for help.");
        c.set("commands.2242", "&cFailed to load your user data.");
        c.set("commands.2246", "&cYou are not in a kingdom.");
        c.set("commands.2251", "&cYou are not in a kingdom.");
        c.set("commands.2256", "&cError getting rank for your kingdom.");
        c.set("commands.2272", "&cFailed to save kingdom data!");
        c.set("commands.2276", "&6Kingdom Desc updated: &7%a%");
        c.set("commands.2278", "&cYou must be a &7Leader &c or an &7Executive &cto change your kingdom's Desc.");
        c.set("commands.2281", "&6Kingdom's Desc: &7%a%");
        c.set("commands.2304", "&cThat kingdom does not exist.");
        c.set("commands.2311", "&cFailed to load your user data.");
        c.set("commands.2315", "&cYou are not in a kingdom.");
        c.set("commands.2320", "&cYou are not in a kingdom.");
        c.set("commands.2334", "&cFailed to load your user data.");
        c.set("commands.2338", "&cYou are not in a kingdom.");
        c.set("commands.2343", "&cYou are not in a kingdom.");
        c.set("commands.2357", "&aBanner set to kingdom banner.");
        c.set("commands.2359", "&cFailed to get kingdom banner.");
        c.set("commands.2362", "&cPlease hold a banned in your hand to set its design to your kingdom banner.");
        c.set("commands.2372", "&cFailed to load your user data.");
        c.set("commands.2376", "&cYou are not in a kingdom.");
        c.set("commands.2381", "&cYou are not in a kingdom.");
        c.set("commands.2386", "&cError getting rank for your kingdom.");
        c.set("commands.2397", "&cFailed to save kingdom data!");
        c.set("commands.2402", "&6Kingdom banner set to: &7RANDOM");
        c.set("commands.2415", "&cFailed to save kingdom data!");
        c.set("commands.2421", "&6Kingdom banner set to: &7BANNER IN HAND");
        c.set("commands.2423", "&cFailed to the banner you are holding.");
        c.set("commands.2426", "&cPlease hold a banned in your hand which you want to set as your kingdom banner.");
        c.set("commands.2430", "&cYou must be a &7Leader &c or an &7Executive &cto change your kingdom's banner.");
        c.set("commands.2454", "&cThat kingdom does not exist.");
        c.set("commands.2463", "&cFailed to save kingdom!");
        c.set("commands.2469", "&4You do not have permission.");
        c.set("commands.2474", "&cA kingdom name must be at least &6%a%&c characters long.");
        c.set("commands.2478", "&cA kingdom name's length must be less than or equal to &6%a%&c characters.");
        c.set("commands.2482", "&cThat name is not allowed.");
        c.set("commands.2490", "&cThe name &6%a%&c is banned on this server.");
        c.set("commands.2496", "&cFailed to load your user data.");
        c.set("commands.2500", "&cYou are not in a kingdom.");
        c.set("commands.2505", "&cYou are not in a kingdom.");
        c.set("commands.2510", "&cError getting rank for your kingdom.");
        c.set("commands.2520", "&cFailed to save kingdom!");
        c.set("commands.2526", "&cYou must be a &7Leader &c or an &7Executive &cto change your kingdom's name.");
        c.set("commands.2529", "&cYou must enter a name for your kingdom.");
        c.set("commands.2539", "&cFailed to load your user data.");
        c.set("commands.2543", "&cYou are not in a kingdom.");
        c.set("commands.2548", "&cYou are not in a kingdom.");
        c.set("commands.2553", "&cError getting rank for your kingdom.");
        c.set("commands.2561", "&cNo challenges to list.");
        c.set("commands.2572", "&4&l�=&9&lChallenges 1/%a%&4&l=�");
        c.set("commands.2584", "&cInvalid page.");
        c.set("commands.2588", "&cThe page number must be greater than 0.");
        c.set("commands.2599", "&cThe page number can not exceed &7%a%");
        c.set("commands.2602", "&4&l-=�&9&lChallenges 1/%a%&4&l=-");
        c.set("commands.2629", "&cKingdom not found.");
        c.set("commands.2633", "&cEnter a challenging kingdom, not your own kingdom.  Do &7/f challenge list &cto view your active challenges.");
        c.set("commands.2640", "&cNo challenge found for that kingdom. Try: &7/f challenge list &cto list all challenges.");
        c.set("commands.2650", "&6Defence challenge with &7%a%&6 has been accepted. &7Fighting will begin when the enemy fighters are online and your fighters are online.  If they do not come online at the same time as your kingdom, then the kingdom with the most online time will win after one week.");
        c.set("commands.2652", "&cYour kingdom has already accepted this challenge.");
        c.set("commands.2655", "&cYou are not the defender in this challenge.");
        c.set("commands.2658", "&cYou do not have permission to accept challenges.");
        c.set("commands.2662", "&cSurrendering...");
        c.set("commands.2666", "&cYou do not have permission to surrender.");
        c.set("commands.2669", "&4&l-=&9&lChallenge&4&l=-");
        c.set("commands.2670", "&6Attacker: &7%a%");
        c.set("commands.2671", "&6Defender: &7%a%");
        c.set("commands.2672", "&6Location: &7%a%");
        c.set("commands.2673", "&6Accepted: &7%a%");
        c.set("commands.2675", "&6Time remaining: &7%a%");
        c.set("commands.2680", "&4&l> &e&lTELEPORTING &4&l<");
        c.set("commands.2683", "&cThat's weird. There is no location to teleport you to.");
        c.set("commands.2712", "&cKingdom not found.");
        c.set("commands.2721", "&4&l-=&6&l%a%&4&l=-");
        c.set("commands.2722", "&6Current time: &7%a%");
        c.set("commands.2724", "&6ONLINE NOW");
        c.set("commands.2726", "&6Online last: &7%a%");
        c.set("commands.2730", "%a%");
        c.set("commands.2733", "&cYou must enter a kingdom's name.");
        c.set("commands.2755", "&cKingdom 1 not found.");
        c.set("commands.2773", "&cKingdom 2 not found.");
        c.set("commands.2783", "&7%a% &6was forced to win their challenge with &7%b%");
        c.set("commands.2785", "&cA challenge with attacker: &7%a% &cand defender: &7%b% &ccould not be found.");
        c.set("commands.2788", "&cUsage: &7/f forceWin <kingdom> <vsKingdom>");
        c.set("commands.2798", "&cFailed to load your user data.");
        c.set("commands.2802", "&cYou are not in a kingdom.");
        c.set("commands.2807", "&cYou are not in a kingdom.");
        c.set("commands.2812", "&cError getting rank for your kingdom.");
        c.set("commands.2827", "&cYou can not promote that player. Use &7/f leader %a% &cto set this player as the kingdom leader.");
        c.set("commands.2834", "&cFailed to save kingdom data!");
        c.set("commands.2838", "&7%a% &6was promoted to &7%b%");
        c.set("commands.2840", "&2You have been promoted by &7%a% &6New Rank: &7%b%");
        c.set("commands.2844", "&cYou do not have permission to promote this player.");
        c.set("commands.2847", "&cThere was a problem getting the players current rank.");
        c.set("commands.2850", "&cThat player is not in your kingdom.");
        c.set("commands.2853", "&cInvalid player.");
        c.set("commands.2856", "&cInvalid player.");
        c.set("commands.2859", "&cInvalid command. Try /f help");
        c.set("commands.2869", "&cFailed to load your user data.");
        c.set("commands.2873", "&cYou are not in a kingdom.");
        c.set("commands.2878", "&cYou are not in a kingdom.");
        c.set("commands.2883", "&cError getting rank for your kingdom.");
        c.set("commands.2898", "&cYou can not demote that player. Use &7/f kick %a% &cto kick this player.");
        c.set("commands.2905", "&cFailed to save kingdom data!");
        c.set("commands.2909", "&7%a% &6was demoted to &7%b%");
        c.set("commands.2911", "&cYou have been demoted by &7%a% &6New Rank: &7%b%");
        c.set("commands.2915", "&cYou do not have permission to demote this player.");
        c.set("commands.2918", "&cThere was a problem getting the players current rank.");
        c.set("commands.2921", "&cThat player is not in your kingdom.");
        c.set("commands.2924", "&cInvalid player.");
        c.set("commands.2927", "&cInvalid player.");
        c.set("commands.2930", "&cInvalid command. Try /f help");
        c.set("commands.2938", "&6There are no kingdoms to list.");
        c.set("commands.2947", "&cInvalid page number.");
        c.set("commands.2951", "&cThe page number must be greater than &70");
        c.set("commands.2954", "&cThe page number must be less than or equal to &7%a%");
        c.set("commands.2960", "&9&l===[&c&lKingdom List %a%/%b%&9&l]===");
        c.set("commands.2962", "&6%a%: &7%b%/%c% online, %d%/%e%, $%f%");
        c.set("commands.2965", "&cFailed to list kingdoms.");
        c.set("commands.2971", "&9&l===[&c&lKingdom List 1/%a%&9&l]===");
        c.set("commands.2973", "&6%a%: &7%b%/%c% online, %d%/%e%, $%f%");
        c.set("commands.2976", "&cFailed to list kingdoms.");
        c.set("commands.2989", "&cInvalid number for reputation.");
        c.set("commands.2993", "&cReputation must be greater than or equal to &6%a%");
        c.set("commands.2995", "&cReputation must be less than or equal to &6%a%");
        c.set("commands.3009", "&cError saving reputation to player.");
        c.set("commands.3012", "&7%a% &6reputation set to &7%b%");
        c.set("commands.3019", "&cUsage: &7/f setReputation <player> <reputation>");
        c.set("commands.3029", "&cFailed to load your user data.");
        c.set("commands.3033", "&cYou are not in a kingdom.");
        c.set("commands.3038", "&cYou are not in a kingdom.");
        c.set("commands.3043", "&cError getting rank for your kingdom.");
        c.set("commands.3050", "&cYou can not start a shield with active challenges.");
        c.set("commands.3059", "&cFailed to save data.");
        c.set("commands.3064", "&cYour kingdom is not shielded or in vacation mode.");
        c.set("commands.3078", "&cFailed to save data.");
        c.set("commands.3083", "&cYour kingdom currently has an active shield.");
        c.set("commands.3086", "&cOne week shield costs &7$%a%. &cYou need $%b% &cmore.");
        c.set("commands.3089", "&cYou do not have permission to use a one week shield.");
        c.set("commands.3103", "&cFailed to save data.");
        c.set("commands.3108", "&cYour kingdom currently has an active shield.");
        c.set("commands.3111", "&cTwo week shield costs &7$%a%. &cYou need $%b% &cmore.");
        c.set("commands.3114", "&cYou do not have permission to use a two week shield.");
        c.set("commands.3128", "&cFailed to save data.");
        c.set("commands.3133", "&cYour kingdom currently has an active shield.");
        c.set("commands.3136", "&cThree week shield costs &7$%a%. &cYou need $%b% &cmore.");
        c.set("commands.3139", "&cYou do not have permission to use a three week shield.");
        c.set("commands.3142", "&cUsage: &7/shield [1/2/3/stop]");
        c.set("commands.3145", "&cYou must be an executive or the leader of your kingdom to do this.");
        c.set("commands.3148", "&4&l+++< &2&lShields & Vacation &4&l>+++");
        c.set("commands.3149", "&7Shields protect your kingdom from any attack for its time period.");
        c.set("commands.3150", "&7Vacation protects your kingdom, but you and your kingdom can not build on your land for this time. (Duration: 1 Week)");
        c.set("commands.3151", "&6One Week Shield: &7$%a% &6Cool down: &7%b% days");
        c.set("commands.3152", "&6Two Week Shield: &7$%a% &6Cool down: &7%b% days");
        c.set("commands.3153", "&6Three Week Shield: &7$%a% &6Cool down: &7%b% days");
        c.set("commands.3154", "&6Vacation: &7$%a% &6Cool down: &7%b% days");
        c.set("commands.3155", "&2Commands: &7/shield <1/2/3/stop>, /vacation");
        c.set("commands.3157", "&aActive Shield: &7%a%");
        c.set("commands.3168", "&cFailed to load your user data.");
        c.set("commands.3172", "&cYou are not in a kingdom.");
        c.set("commands.3177", "&cYou are not in a kingdom.");
        c.set("commands.3182", "&cError getting rank for your kingdom.");
        c.set("commands.3189", "&cYou can not go on vacation with active challenges.");
        c.set("commands.3200", "&cFailed to save data.");
        c.set("commands.3206", "&cYou do not have enough money to do this. Money needed: &7$%a%");
        c.set("commands.3210", "&cYour kingdom has an active shield. Do &7/f shield stop &cto stop your shield.");
        c.set("commands.3213", "&cYou must be an executive or the leader of your kingdom to do this.");
        c.set("commands.3216", "&cUnknown command. Try &7/f help &cfor help.");
        c.set("commands.3228", "&cInvalid number for reputation.");
        c.set("commands.3232", "&cReputation must be greater than or equal to &6%a%");
        c.set("commands.3234", "&cReputation must be less than or equal to &6%a%");
        c.set("commands.3238", "&cInvalid player: &7%a%");
        c.set("commands.3248", "&cError saving reputation to player.");
        c.set("commands.3251", "&7%a% &6reputation set to &7%b%");
        c.set("commands.3258", "Usage: /f setReputation <player> <reputation>");
        c.set("commands.3261", "Invalid command.");
        c.set("commands.3264", "Invalid command. Console commands: /f changeReputation <player> <change>, /f addXP/addLevel <player> <profession/strength/toughness/speed/stamina/luck/all/attributes> <int> [true/false], /f reload");
        c.set("commands.3318", "&4&l=====< &b&lCHALLENGES &4&l >=====");
        c.set("commands.3319", "&7Challenges are how you fight other kingdoms. To place a challenge, place a banner on your enemy's land. Ignoring challenges makes you lose money.");
        c.set("commands.3320", "&6Challenge commands:");
        c.set("commands.3321", "&6/f challenge list [page] &7Lists your challenges");
        c.set("commands.3322", "&6/f challenge accept <kingdom> &7Accepts a challenge.");
        c.set("commands.3323", "&6/f challenge surrender <kingdom> &7Surrender to the enemy.");
        c.set("commands.3324", "&6/f challenge info <kingdom> &7Get information about a specific challenge.");
        c.set("commands.3325", "&6/f challenge tp <kingdom> &7Teleport to the fight.");
        c.set("commands.3337", "&9&l===< &6&lFeudal %a% / %b% &9&l>===");
        c.set("commands.3344", "&6&l> &2/f about &7- gives credit and information about Feudal");
        c.set("commands.2612", "&e%a% &7vs &e%b%%c%");
        c.set("commands.2612_c1", " &4&lFIGHTING");
        c.set("commands.2612_c2", " &4&lACTIVE: %time%");
        c.set("commands.2656", "%a%%e% &7vs %b%%c%%d%");
        c.set("commands.3175", "&c%a% ended by: &7%b% &c%a% disabled for &7%c%");
        c.set("commands.3175_a1", "Vacation");
        c.set("commands.3175_a2", "Shield");
        c.set("commands.3196", "&aShield Created by &7%a% &aShield &7%b%");
        c.set("commands.3321a", "&aVacation shield activated! Shield duration: &7One Week");
        c.set("commands.1019", "&a%a% has joined your kingdom.");
        c.set("commands.1314", "&e%a% has left the kingdom.");
        c.set("commands.1453", "&a%a% has requested to no longer be enemies with your kingdom. Do /f neutral %a% to accept their request.");
        c.set("commands.1543", "&a%a% has requested to be allies with your kingdom. Do /f ally %a% to accept their request.");
        c.set("commands.1612", "&c%a% was kicked from your kingdom by %b%");
        c.set("commands.kc1", "&6Kingdom chat set to: &7%b%");

        c.set("commands2.1020", "&a%a% has joined your kingdom.%b%");
        c.set("commands2.1315", "&e%a% left the kingdom.%b%");
        c.set("commands2.1454", "&a%b%%a% has requested to no longer be enemies with your kingdom. Do /f neutral %b%%a% to accept their request.%c%");
        c.set("commands2.1544", "&a%b%%a% has requested to be allies with your kingdom. Do /f ally %b%%a% to accept their request.%c%");
        c.set("commands2.1613", "&c%a% was kicked from your kingdom by %b%");
        c.set("commands2.1715", "&7%a%&e claimed land for your kingdom. &7(%b%, %c%)%d%");
        c.set("commands2.1788", "&7%a%&e claimed land for your kingdom. &7(%b%, %c%)%d%");
        c.set("commands2.2446", "&6Kingdom banner changed by: &7%a%");
        c.set("commands2.2465", "&6Kingdom banner changed by: &7%a%");
        c.set("commands2.2537", "&7%a% &akingdom name changed to &7%b%");
        c.set("commands2.2594", "&7%a% &akingdom name changed to &7%b%");
        c.set("commands2.3202", "&aShield Created by &7%a% &aShield &7%b%");
        c.set("commands2.3228", "&aShield Created by &7%a% &aShield &7%b%");
        c.set("commands2.3254", "&aShield Created by &7%a% &aShield &7%b%");

        c.set("commandHelp.18", "&6&l> %a%/f help [page] &7- list of feudal commands");
        c.set("commandHelp.20", "&6&l> %a%/f tutorial &7- gives links to videos, tutorials, and the wiki");
        c.set("commandHelp.24", "&6&l> %a%/f market &7- open the market");
        c.set("commandHelp.26", "&6&l> %a%/f sell [price] &7- sell the item in your hand");
        c.set("commandHelp.28", "&6&l> %a%/f price &7- get the price of the item(s) you are holding");
        c.set("commandHelp.new1246", "&6&l> %a%/f unenchant &7- unenchant item in hand");
        c.set("commandHelp.30", "&6&l> %a%/f c [player] &7- view character information");
        c.set("commandHelp.32", "&6&l> %a%/f cp [player] &7- change your profession");
        c.set("commandHelp.34", "&6&l> %a%/f create <kingdom> &7- create a kingdom");
        c.set("commandHelp.36", "&6&l> %a%/f join <kingdom> &7- join a kingdom");
        c.set("commandHelp.38", "&6&l> %a%/f invite <player> &7- invite a player to join your kingdom");
        c.set("commandHelp.40", "&6&l> %a%/f challenge help &7- view challenge help and information");
        c.set("commandHelp.42", "&6&l> %a%/f delete [kingdom] &7- delete your kingdom");
        c.set("commandHelp.44", "&6&l> %a%/f leader <player> &7- make a player leader of your kingdom");
        c.set("commandHelp.46", "&6&l> %a%/f leave &7- leave your kingdom");
        c.set("commandHelp.48", "&6&l> %a%/f kick <player> &7- kick a player from your kingdom");
        c.set("commandHelp.50", "&6&l> %a%/f claim [radius] &7- claim land for your kingdom");
        c.set("commandHelp.52", "&6&l> %a%/f unclaim [all] &7- unclaims your kingdom land");
        c.set("commandHelp.54", "&6&l> %a%/f setHome &7- set the home for your kingdom");
        c.set("commandHelp.56", "&6&l> %a%/f home &7- go to your kingdom's home");
        c.set("commandHelp.58", "&6&l> %a%/f view &7- lets you view what kingdoms are around you");
        c.set("commandHelp.60", "&6&l> %a%/f promote <player> &7- promote a player in your kingdom");
        c.set("commandHelp.62", "&6&l> %a%/f demote <player> &7- demote a player in your kingdom");
        c.set("commandHelp.64", "&6&l> %a%/f list [page] &7- list all the kingdoms");
        c.set("commandHelp.66", "&6&l> %a%/f treasury [add/remove] <amount> &7- view, add, or remove money from your kingdom's treasury");
        c.set("commandHelp.68", "&6&l> %a%/f info <kingdom> &7- display information about a kingdom");
        c.set("commandHelp.70", "&6&l> %a%/f d [Desc] &7- views and/or sets your kingdom Desc");
        c.set("commandHelp.72", "&6&l> %a%/f name <name> &7- change your kingdom's name");
        c.set("commandHelp.74", "&6&l> %a%/f banner &7- set the banner in your hand to your kingdom's banner");
        c.set("commandHelp.76", "&6&l> %a%/f setBanner [random] &7- sets your kingdom banner as the one you are holding and/or sets your kingdom banner as a random banner");
        c.set("commandHelp.78", "&6&l> %a%/f kc &7- Toggle kingdom chat");
        c.set("commandHelp.80", "&6&l> %a%/f challenge accept <kingdom> &7- accept a challenge from a kingdom");
        c.set("commandHelp.82", "&6&l> %a%/f challenge surrender <kingdom> &7- give up in a challenge. This lets the enemy take your land and treasury if you are the defender");
        c.set("commandHelp.84", "&6&l> %a%/f challenge info <kingdom> &7- view info for a challenge with a kingdom");
        c.set("commandHelp.86", "&6&l> %a%/f challenge tp <kingdom> &7- teleport to the fighting area for a challenge");
        c.set("commandHelp.88", "&6&l> %a%/f challenge list [page] &7- list your kingdom's current challenges");
        c.set("commandHelp.90", "&6&l> %a%/f enemy <kingdom> &7- set a kingdom as an enemy");
        c.set("commandHelp.92", "&6&l> %a%/f neutral <kingdom> &7- set a kingdom as neutral");
        c.set("commandHelp.94", "&6&l> %a%/f ally <kingdom> &7- set a kingdom as an ally");
        c.set("commandHelp.96", "&6&l> %a%/f setOpen <true/false> &7- make it possible for anyone to join your kingdom without an invite");
        c.set("commandHelp.98", "&6&l> %a%/f banInfo [kingdom] &7- list kingdoms and time remaining which you cannot challenge");
        c.set("commandHelp.100", "&6&l> %a%/f kingdomOnline <kingdom> &7- check when a kingdom is online the most");
        c.set("commandHelp.102", "&6&l> %a%/f speed [player] &7- view info on a player's speed");
        c.set("commandHelp.104", "&6&l> %a%/f strength [player] &7- view info on a player's strength");
        c.set("commandHelp.106", "&6&l> %a%/f toughness [player] &7- view info on a player's toughness");
        c.set("commandHelp.108", "&6&l> %a%/f stamina [player] &7- view info on a player's stamina");
        c.set("commandHelp.110", "&6&l> %a%/f luck [player] &7- view info on a player's luck");
        c.set("commandHelp.112", "&6&l> %a%/f p [player] &7- view information about a player's profession");
        c.set("commandHelp.114", "&6&l> %a%/f online <player> &7- check what times a player is online the most");
        c.set("commandHelp.116", "&6&l> %a%/f shield <1/2/3> &7- buy a shield for your kingdom (1, 2, or 3 weeks)");
        c.set("commandHelp.118", "&6&l> %a%/f shield stop &7- stop your current shield (no refunds)");
        c.set("commandHelp.120", "&6&l> %a%/f shield &7- view information about shields");
        c.set("commandHelp.122", "&6&l> %a%/f vacation &7- buy a vacation shield");
        c.set("commandHelp.124", "&6&l> %a%/f setProfessionLevel <level> [player] &7- set a player's profession level (ADMIN)");
        c.set("commandHelp.126", "&6&l> %a%/f setSpeedLevel <level> [player] &7- sets a player's speed level (ADMIN)");
        c.set("commandHelp.128", "&6&l> %a%/f setStrengthLevel <level> [player] &7- sets a player's strength level (ADMIN)");
        c.set("commandHelp.130", "&6&l> %a%/f setToughnessLevel <level> [player] &7- sets a player's toughness level (ADMIN)");
        c.set("commandHelp.132", "&6&l> %a%/f setStaminaLevel <level> [player] &7- sets a player's stamina level (ADMIN)");
        c.set("commandHelp.134", "&6&l> %a%/f setLuckLevel <level> [player] &7- sets a player's luck level (ADMIN)");
        c.set("commandHelp.136", "&6&l> %a%/f resetCharacter <player> &7- Resets an entire character. Delete their kingdom if they are the leader. (ADMIN)");
        c.set("commandHelp.138", "&6&l> %a%/f setReputation <player> <reputation> &7- Change a players reputation (ADMIN)");
        c.set("commandHelp.140", "&6&l> %a%/f forceWin <Kingdom> <vs> &7- Make kingdom win (ADMIN)");
        c.set("commandHelp.141", "&6&l> %a%/f findclaims &7- View claimed land");


        c.set("commands.professionLevel", "&6Profession Level: &7%level% / %max% - %xp% XP Needed");

        c.set("leader.attacker", "&6Attacker");
        c.set("leader.defender", "&6Defender");
        c.set("leader.inFight", "&aIn Fight");
        c.set("leader.ch", "&b&lChallenge");

        c.set("track.title", "&4Track Player");
        c.set("track.back", "&c&lBACK");
        c.set("track.next", "&a&lNEXT");
        c.set("track.price", "&6Price: &7");
        c.set("track.dist", "&6Estimated Distance: &7");

        c.set("market.title", "&4Market");
        c.set("market.back", "&e&lBACK");
        c.set("market.back2", "&c&lBACK");
        c.set("market.next", "&e&lNEXT");
        c.set("market.bal", "&6Balance: &7");
        c.set("market.cat", "&6Category: &7");
        c.set("market.itemshow", "&6Items Shown: &7");
        c.set("market.total", "&6Total Items: &7");
        c.set("market.purchase", "&a&lPurchase");
        c.set("market.add1", "&aAdd 1");
        c.set("market.add10", "&aAdd 10");
        c.set("market.inc", "&7This will increase");
        c.set("market.inc1", "&7the quantity by 1.");
        c.set("market.inc10", "&7the quantity by 10.");
        c.set("market.sub1", "&eSubtract 1");
        c.set("market.sub10", "&eSubtract 10");
        c.set("market.dec", "&7This will decrease");
        c.set("market.dec1", "&7the quantity by 1.");
        c.set("market.dec10", "&7the quantity by 10.");


        c.set("changeProfession.confirm", "&4Confirm Change");
        c.set("changeProfession.conno", "&c&lBack");
        c.set("changeProfession.conyes", "&a&lChange Profession");
        c.set("changeProfession.title", "&4Change Profession");

        c.set("changeProfession.farmer", "&a&lFarmer");
        c.set("changeProfession.Logger", "&a&lLogger");
        c.set("changeProfession.Hunter", "&a&lHunter");
        c.set("changeProfession.Miner", "&a&lMiner");
        c.set("changeProfession.Cook", "&a&lCook");
        c.set("changeProfession.Fisher", "&a&lFisher");
        c.set("changeProfession.Builder", "&a&lBuilder");
        c.set("changeProfession.Shepherd", "&a&lShepherd");
        c.set("changeProfession.Scribe", "&a&lScribe");
        c.set("changeProfession.Guard", "&a&lGuard");
        c.set("changeProfession.Assassin", "&a&lAssassin");
        c.set("changeProfession.Alchemist", "&a&lAlchemist");
        c.set("changeProfession.Blacksmith", "&a&lBlacksmith");
        c.set("changeProfession.Healer", "&a&lHealer");
        c.set("changeProfession.Merchant", "&a&lMerchant");
        c.set("changeProfession.Squire", "&a&lSquire");
        c.set("changeProfession.Knight", "&a&lKnight");
        c.set("changeProfession.Baron", "&a&lBaron");
        c.set("changeProfession.King", "&a&lKing");

        c.set("changeProfession.Strength", "Strength");
        c.set("changeProfession.Stamina", "Stamina");
        c.set("changeProfession.Luck", "Luck");
        c.set("changeProfession.Speed", "Speed");
        c.set("changeProfession.Toughness", "Toughness");
        c.set("changeProfession.all", "ALL");

        c.set("changeProfession.farmer1", "Grows crops");
        c.set("changeProfession.logger1", "Cuts down trees.");
        c.set("changeProfession.hunter1", "Hunts wild animals.");
        c.set("changeProfession.miner1", "Mines for ores.");
        c.set("changeProfession.cook1", "Cooks food.");
        c.set("changeProfession.fisher1", "Fishes for fish");
        c.set("changeProfession.builder1", "Builds things for their kingdom.");
        c.set("changeProfession.shepherd1", "Breeds and herd animals.");
        c.set("changeProfession.scribe1", "Writes books.");

        c.set("changeProfession.guard1", "Guards kingdom from mobs and players.");
        c.set("changeProfession.assassin1", "Hunts down and assassinates people.");
        c.set("changeProfession.assassin2", "&7Assassin is the only profession which are");
        c.set("changeProfession.assassin3", "&7not allowed to create or lead a kingdom.");
        c.set("changeProfession.alchemist1", "Makes potions.");
        c.set("changeProfession.blacksmith1", "Creates weapons, tools, and armor.");
        c.set("changeProfession.healer1", "Makes healing potions and golden apples.");
        c.set("changeProfession.merchant1", "Buys and sells items.");

        c.set("changeProfession.squire1", "Guards and attacks.");
        c.set("changeProfession.knight1", "Guards and attacks.");
        c.set("changeProfession.baron1", "Owns land and collects taxes.");
        c.set("changeProfession.king1", "Owns land and collects taxes.");

        c.set("changeProfession.cur", "&b&lCurrent Profession");
        c.set("changeProfession.lock", "&c&lLOCKED");
        c.set("changeProfession.needMaster", "&6%n%&c peasant needed.");
        c.set("changeProfession.needMasterComm", "&6%n%&c commoner needed.");
        c.set("changeProfession.squireMaster", "&cYou must master SQUIRE first.");
        c.set("changeProfession.knightMaster", "&cYou must master KNIGHT first.");
        c.set("changeProfession.baronMaster", "&cYou must master BARON first.");
        c.set("changeProfession.warn", "&c&lWARNING:");
        c.set("changeProfession.warn2", "&cProgress for your current");
        c.set("changeProfession.warn3", "&cprofession will &c&lnot save!");
        c.set("changeProfession.warn4", "&cMax out a profession to complete it.");
        c.set("changeProfession.sp", "&6Social class: &7Peasantry");
        c.set("changeProfession.sc", "&6Social class: &7Commoner");
        c.set("changeProfession.sn", "&6Social class: &7Nobility");
        c.set("changeProfession.landA", "&6Land: &7");
        c.set("changeProfession.ag", "&6Attributes: &7");
        c.set("changeProfession.dd", "&6Desc: &7");

        c.set("changeProfession.change", "&aProfession changed to &c&l%a%");
        c.set("changeProfession.changeOther", "&a%name% profession changed to &c&l%type%");
        c.set("changeProfession.thatIsCur", "&4That is your current profession.");


        c.set("sel_gui.con", "&4Confirm");
        c.set("sel_gui.finish", "&e&lFINISH");
        c.set("sel_gui.finish1", "&cThis will save your character.");
        c.set("sel_gui.finish2", "&cOnce saved you can not redo");
        c.set("sel_gui.finish3", "&cany settings.");
        c.set("sel_gui.back", "&c&lBACK");
        c.set("sel_gui.you", "&c&lYOU");
        c.set("sel_gui.you2", "&6Profession: &7");
        c.set("sel_gui.strengthl", "&6Strength Level: &7");
        c.set("sel_gui.toughnessl", "&6Toughness Level: &7");
        c.set("sel_gui.speedl", "&6Speed Level: &7");
        c.set("sel_gui.staminal", "&6Stamina Level: &7");
        c.set("sel_gui.luckl", "&6Luck Level: &7");
        c.set("sel_gui.unusedp", "&6Unused Points: &7");
        c.set("sel_gui.setupAtt", "&4Setup Attributes");
        c.set("sel_gui.points", "&e&lPoints: &c&l");
        c.set("sel_gui.pointsAtt", "&aUse points to upgrade attributes.");
        c.set("sel_gui.addStrength", "&9&lAdd Strength");
        c.set("sel_gui.addToughness", "&9&lAdd Toughness");
        c.set("sel_gui.addSpeed", "&9&lAdd Speed");
        c.set("sel_gui.addStamina", "&9&lAdd Stamina");
        c.set("sel_gui.addLuck", "&9&lAdd Luck");
        c.set("sel_gui.addCost", "&6Cost: &71");
        c.set("sel_gui.addLevel", "&6Added Level: &7");

        c.set("sel_gui.remStrength", "&e&lRemove Strength");
        c.set("sel_gui.remToughness", "&e&lRemove Toughness");
        c.set("sel_gui.remSpeed", "&e&lRemove Speed");
        c.set("sel_gui.remStamina", "&e&lRemove Stamina");
        c.set("sel_gui.remLuck", "&e&lRemove Luck");
        c.set("sel_gui.earn", "&6Earn: &71 point");
        c.set("sel_gui.remLevel", "&6Removed Level:&7 ");


        c.set("sel_gui.strengthT", "&c&lStrength");
        c.set("sel_gui.strengthT1", "&6Damage Effect: &7");
        c.set("sel_gui.strengthT2", "&6Haste Effect: &7");
        c.set("sel_gui.strengthT3", "&7(Default damage: 1.0)");
        c.set("sel_gui.strengthT4", "&7(Default haste: 0)");
        c.set("sel_gui.toughnessT", "&c&lToughness");
        c.set("sel_gui.toughnessT1", "&6Health: &7");
        c.set("sel_gui.toughnessT2", "&6Regeneration Rate: &7");
        c.set("sel_gui.toughnessT3", "&7(Default health: 20)");
        c.set("sel_gui.toughnessT4", "&7(Default regeneration rate: 1.0)");
        c.set("sel_gui.speedT", "&c&lSpeed");
        c.set("sel_gui.speedT1", "&6Speed: &7");
        c.set("sel_gui.speedT2", "&7(Default speed: 1.0)");
        c.set("sel_gui.staminaT", "&c&lStamina");
        c.set("sel_gui.staminaT1", "&6Saturation regain rate: &7");
        c.set("sel_gui.staminaT2", "&7(Default rate: 0.0)");
        c.set("sel_gui.luckT", "&c&lLuck");
        c.set("sel_gui.luckT1", "&6Luck: &7");
        c.set("sel_gui.level", "&6Level: &7");
        c.set("sel_gui.saveAtt1", "&aThis will save all your settings.");
        c.set("sel_gui.saveAtt2", "&aYou can finish with leftover points,");
        c.set("sel_gui.saveAtt3", "&abut your points will not save.");
        c.set("sel_gui.saveAtt4", "&cOnce you finish you can never");
        c.set("sel_gui.saveAtt5", "&cchange these settings again.");
        c.set("sel_gui.saveAtt6", "&cYou can only level up.");
        c.set("sel_gui.helpAtt", "&d&lHELP");
        c.set("sel_gui.helpAtt1", "&7Use points to setup your attributes.");
        c.set("sel_gui.helpAtt2", "&7This is a one time setup.");
        c.set("sel_gui.helpAtt3", "&7Click the blue wool to add to an attribute.");
        c.set("sel_gui.helpAtt4", "&7Click the yellow wool to remove from an attribute.");
        c.set("sel_gui.helpAtt5", "&7Click the crafting bench when you are finished.");
        c.set("sel_gui.helpAtt6", "&7Click the tnt to go back.");
        c.set("sel_gui.socTitle", "&4Choose your social class");
        c.set("sel_gui.socPoints", "&e&lPoints: &c&l");
        c.set("sel_gui.socPoints1", "&aUse points to choose a social class,");
        c.set("sel_gui.socPoints2", "&aprofession, and setup attributes.");
        c.set("sel_gui.socPoints3", "&cNOTICE: &7Choosing a higher social class will leave");
        c.set("sel_gui.socPoints4", "&7you with less points to add to attributes.");
        c.set("sel_gui.socPeasant", "&c&lPeasantry");
        c.set("sel_gui.socCommoner", "&c&lCommoner");
        c.set("sel_gui.socNoble", "&c&lNoble");
        c.set("sel_gui.socCost", "&6Cost: &7");

        c.set("sel_gui.pesTitle", "&4Peasantry Professions");
        c.set("sel_gui.comTitle", "&4Commoner Professions");
        c.set("sel_gui.nobTitle", "&4Peasantry Professions");
        c.set("sel_gui.Farmer", "&c&lFarmer");
        c.set("sel_gui.Logger", "&c&lLogger");
        c.set("sel_gui.Hunter", "&c&lHunter");
        c.set("sel_gui.Miner", "&c&lMiner");
        c.set("sel_gui.Cook", "&c&lCook");
        c.set("sel_gui.Fisher", "&c&lFisher");
        c.set("sel_gui.Builder", "&c&lBuilder");
        c.set("sel_gui.Shepherd", "&c&lShepherd");
        c.set("sel_gui.Scribe", "&c&lScribe");
        c.set("sel_gui.Guard", "&c&lGuard");
        c.set("sel_gui.Assassin", "&c&lAssassin");
        c.set("sel_gui.Alchemist", "&c&lAlchemist");
        c.set("sel_gui.Blacksmith", "&c&lBlacksmith");
        c.set("sel_gui.Healer", "&c&lHealer");
        c.set("sel_gui.Merchant", "&c&lMerchant");
        c.set("sel_gui.Squire", "&c&lSquire");
        c.set("sel_gui.Knight", "&c&lKnight");
        c.set("sel_gui.Baron", "&c&lBaron");
        c.set("sel_gui.King", "&c&lKing");
        c.set("sel_gui.land", "&6Land: &7");
        c.set("sel_gui.Farmer1", "&6Attributes: &7Strength && Stamina");
        c.set("sel_gui.Farmer2", "&6Desc: &7Grows crops.");
        c.set("sel_gui.Logger1", "&6Attributes: &7Strength && Toughness");
        c.set("sel_gui.Logger2", "&6Desc: &7Cuts down trees.");
        c.set("sel_gui.Hunter1", "&6Attributes: &7Speed && Stamina");
        c.set("sel_gui.Hunter2", "&6Desc: &7Hunts wild animals.");
        c.set("sel_gui.Miner1", "&6Attributes: &7Strength && Luck");
        c.set("sel_gui.Miner2", "&6Desc: &7Mines for ores.");
        c.set("sel_gui.Cook1", "&6Attributes: &7Stamina && Luck");
        c.set("sel_gui.Cook2", "&6Desc: &7Cooks food.");
        c.set("sel_gui.Fisher1", "&6Attributes: &7Toughness && Luck");
        c.set("sel_gui.Fisher2", "&6Desc: &7Fishes for fish.");
        c.set("sel_gui.Builder1", "&6Attributes: &7Strength && Stamina");
        c.set("sel_gui.Builder2", "&6Desc: &7Builds things for their kingdom.");
        c.set("sel_gui.Shepherd1", "&6Attributes: &7Stamina && Toughness");
        c.set("sel_gui.Shepherd2", "&6Desc: &7Breeds and herds animals.");
        c.set("sel_gui.Scribe1", "&6Attributes: &7Toughness && Luck");
        c.set("sel_gui.Scribe2", "&6Desc: &7Writes books.");

        c.set("sel_gui.Guard1", "&6Attributes: &7Strength && Toughness");
        c.set("sel_gui.Guard2", "&6Desc: &7Guards kingdom from mobs and players.");
        c.set("sel_gui.Assassin1", "&6Attributes: &7Speed && Luck");
        c.set("sel_gui.Assassin2", "&6Desc: &7Hunts down and assassinates people.");
        c.set("sel_gui.Assassin3", "&7Assassin is the only profession which are");
        c.set("sel_gui.Assassin4", "&7not allowed to create or lead a kingdom.");
        c.set("sel_gui.Alchemist1", "&6Attributes: &7Speed && Stamina");
        c.set("sel_gui.Alchemist2", "&6Desc: &7Makes potions.");
        c.set("sel_gui.Blacksmith1", "&6Attributes: &7Strength && Stamina");
        c.set("sel_gui.Blacksmith2", "&6Desc: &7Creates weapons, tools, and armor.");
        c.set("sel_gui.Healer1", "&6Attributes: &7Luck && Stamina");
        c.set("sel_gui.Healer2", "&6Desc: &7Makes healing potions and golden apples.");
        c.set("sel_gui.Merchant1", "&6Attributes: &7Stamina && Luck");
        c.set("sel_gui.Merchant2", "&6Desc: &7Buys and sells items.");
        c.set("sel_gui.Squire1", "&6Attributes: &7Speed && Toughness");
        c.set("sel_gui.Squire2", "&6Desc: &7Guards and attacks.");
        c.set("sel_gui.Knight1", "&6Attributes: &7Strength && Toughness");
        c.set("sel_gui.Knight2", "&6Desc: &7Guards and attacks.");
        c.set("sel_gui.Baron1", "&6Attributes: &7ALL");
        c.set("sel_gui.Baron2", "&6Desc: &7Owns land and collects taxes.");
        c.set("sel_gui.King1", "&6Attributes: &7ALL");
        c.set("sel_gui.King2", "&6Desc: &7Owns land and collects taxes.");

        c.set("sel_gui.proCost", "&6Cost: &7");

        c.set("added.enemySelf", "&cYou can not enemy yourself.");
        c.set("added.neutralSelf", "&cYou can not neutral yourself.");
        c.set("added.allySelf", "&cYou can not ally yourself.");
        c.set("added.allyRequestSent", "&aAlly request sent.");
        c.set("added.kingdomMaxMembers", "&cThis kingdom has reached its maximum amount of members. (%members%)");

        c.set("newCommands.fighterArg", "&cCommand usage: /f fighter <member>");
        c.set("newCommands.fighterNoExist", "&cThat player does not exist.");
        c.set("newCommands.notInKingdom", "&cThat player is not in your kingdom.");
        c.set("newCommands.leader", "&cYou must be leader to do that.");
        c.set("newCommands.noSelf", "&cYou can not remove yourself as a fighter.");
        c.set("newCommands.nowFighter", "&c%p% &eis now a fighter.");
        c.set("newCommands.remFighter", "&b%p% &eis no longer a fighter.");
        c.set("newCommands.helpFighter", "&6&l> %a%/f fighter <player> &7- toggle kingdom member as a fighter");


        c.set("newTracker.noPlayers", "&cThere are no players to track.");
        c.set("newTracker.trackRandom", "&c&lTrack random player");
        c.set("newTracker.random", "RANDOM PLAYER");

        c.set("kingdomnew.name", "&cThat kingdom name is already taken.");

        c.set("reloadconfig.error", "&cError reloading configs - See console for details. Restart recommended.");
        c.set("reloadconfig.done", "&eConfigs reloaded!  This does not include any kingdom, user, or data files.");
        c.set("commandHelp.141", "&6&l> %a%/f reload &7- Reload configs (ADMIN)");

        c.set("kingdomlog.noLog", "&cThis player has no kingdom log.");
        c.set("kingdomlog.title", "&6&lKingdom logs (%name%)");
        c.set("kingdomlog.log", "&d%join% %kingdom% %time%");
        c.set("kingdomlog.cmd", "&6&l> %a%/f kingdomlog <player> &7- See a player's kingdom log (ADMIN)");
        c.set("kingdomlog.noInt", "&cThe page number must be an integer.");

        c.set("newshield.cooldown", "&cYou can not shield for %time%");


        //SPAR
        c.set("spar.timeout", "&cSpar request timed out.");
        c.set("spar.inSpar", "&cYou are currently in a spar.");
        c.set("spar.inPendingSpar", "&cYou are currently in a pending spar.");
        c.set("spar.myUser", "&cYour character has not been setup or has failed to load.");
        c.set("spar.offline", "&cThat player is offline.");
        c.set("spar.noUser", "&cThat player has not setup their character yet.");
        c.set("spar.theyInSpar", "&cThat player is currently in a spar.");
        c.set("spar.noBetMoney", "&cYou do not have enough money to place that bet.");
        c.set("spar.request", "&aSpar request sent to &c%p%&a. Bet held: &c$%bet%. &aRequest will timeout in &c%timeout% &aseconds.");
        c.set("spar.requestRecieve", "&c%p%&a would like to spar you. They placed a bet of: &c$%bet%. &aRequest will timeout in &c%timeout% &aseconds. &aType &c/f spar %p% &ato begin the fight.");
        c.set("spar.accept", "&aSpar started with &c%p%. &aBet: &c$%bet%");
        c.set("spar.commandUsage", "&cUsage: /f spar <player> [bet]");
        c.set("spar.winNoWinner", "&aSpar ended. No winner.");
        c.set("spar.win", "&aYou defeated &c%p%&a in a spar.  You win &c$%bet%");
        c.set("spar.lose", "&c%p% &edefeated you in a spar.");
        c.set("spar.tooFar", "&cYou must be within %a% blocks of that player to spar them.");
        c.set("spar.sparSelf", "&cYou cannot spar yourself!");

        c.set("spar.commandHelp", "&6&l> %a%/f spar <player> [bet] &7- Spar a player or kingdom member.");

        //Redistribute
        c.set("redistribute.noUser", "&cYour character has not been setup or has failed to load.");
        c.set("redistribute.commandHelp", "&6&l> %a%/f attributes &7- Redistribute attributes");
        c.set("redistribute.cancel", "&c&lCANCEL");
        c.set("redistribute.spendPoints", "&cYou must spend all your points first.");

        c.set("land.unconquered", "Unconquered Land");
        c.set("land.noClaimWorld", "&cYou can not claim land in this world.");
        c.set("spar.noSparWorld", "&cYou can not spar in this world.");


        c.set("landUpdate.died", "Died");
        c.set("landUpdate.killed", "Killed Player");
        c.set("landUpdate.createKingdom", " &acreated a new kingdom: &7");
        c.set("landUpdate.endin", "Ends in");
        c.set("landUpdate.day", " day");
        c.set("landUpdate.days", " days");
        c.set("landUpdate.week", " week");
        c.set("landUpdate.weeks", " weeks");
        c.set("landUpdate.year", " year");
        c.set("landUpdate.years", " years");
        c.set("landUpdate.lives", "&aLives: ");
        c.set("landUpdate.noAllies", "No allies");
        c.set("landUpdate.noEnemies", "No enemies");
        c.set("landUpdate.false", "false");
        c.set("landUpdate.true", "true");
        c.set("landUpdate.onlineNow", "ONLINE NOW");
        c.set("landUpdate.diedTooMany", " &6has died too many times.");
        c.set("landUpdate.abandoned", " &6abandoned the fight.");
        c.set("landUpdate.surrender", " &6has surrendered.");
        c.set("landUpdate.lockedProfession", "&4That profession is locked.");

        c.set("allyChat.leader", "&b[L]&r");
        c.set("allyChat.executive", "&a[E]&r");
        c.set("allyChat.member", "&6[M]&r");
        c.set("allyChat.guest", "&2[G]&r");
        c.set("allyChat.kingdom", "%rank% &e%name% &5&l>> &b%message%");
        c.set("allyChat.ally", "%rank%&a %kingdom% &e%name% &5&l>> &9%message%");

        c.set("changeProfession.none", "None");

        c.set("marketUpdate.price", "&6Price: &7");
        c.set("marketUpdate.amount", "&6Amount: &7");
        c.set("marketUpdate.totalPrice", "&6Total Price: &7");
        c.set("marketUpdate.balance", "&6Balance: &7");
        c.set("marketUpdate.maxAmount", "&6Max Amount: &7");

        c.set("cantunclaim", "&cYou can not unclaim this chunk because it will disconnect your land.");
        c.set("marketUpdate.merchantName", "&6Seller: &7");
        c.set("unclaimUsage", "&cUsage: &7/f unclaim [all]");

        c.set("commandTreasuryNew.denyChallenge", "&cYou can not remove money from your treasury while there is an active challenge against you.");

        c.set("findclaims.claims", "&e&lChunk Claims: (x, z)");
        c.set("findclaims.none", "&cYour kingdom does not have any land.");

        c.set("181.mute", "�cYou cannot talk in kingdom chat while muted.");
        c.set("181.findclaims", "�7Showing claimed land nearby.");

        //1.11.0
        c.set("commands.kingdomCost", "&cYou must have at least &7$%cost% &cto create a kingdom.");
        c.set("commands.kingdomBuy", "&d$%cost% &aused to create a kingdom.");
        c.set("commands.marketCat", "&cThat category does not exist.");

        //1.11.1
        c.set("saveJoinMessage", "&eYour user information is currently saving. Please try again in a moment.");

        //1.12.0
        c.set("landBannedCommand", "&cYou can not use that command while on another kingdom's land.");
        c.set("m.title", "&cKingdom Manager");
        c.set("m.title2", "&cWar List");
        c.set("m.title3", "&cWar Info");
        c.set("m.title4", "&cManage Members");
        c.set("m.title5", "&cInvite Members");
        c.set("m.title6", "&cManage Member");
        c.set("m.banner1", "&6Members: �7");
        c.set("m.banner2", "&6Treasury: &7$");
        c.set("m.banner3", "&6Wars won: &7");
        c.set("m.banner4", "&7(Click to view members)");
        c.set("m.home", "&cHOME");
        c.set("m.home2", "&7(Teleport to kingdom home)");
        c.set("m.claim", "&aClaim land");
        c.set("m.claim2", "&7(Click to claim)");
        c.set("m.unclaim", "&cUnclaim land");
        c.set("m.unclaim2", "&7(Click to unclaim)");
        c.set("m.sethome", "&dSet kingdom home");
        c.set("m.sethome2", "&7(Set kingdom home)");
        c.set("m.findclaims", "&dFind claims");
        c.set("m.findclaims2", "&7(Show your claims around you)");
        c.set("m.view", "&dKingdom map");
        c.set("m.view2", "&7(Show map of kingdom claims)");
        c.set("m.delete", "&cDELETE KINGDOM");
        c.set("m.delete2", "&7(Click to delete kingdom)");
        c.set("m.leave", "&cLEAVE KINGDOM");
        c.set("m.leave2", "&7(Click to leave kingdom)");
        c.set("m.war", "&cWAR INFO");
        c.set("m.war2", "&6Active wars: &7");
        c.set("m.war3", "&7(Click to view war info)");
        c.set("commandHelp.460", "&6&l> %a%/f manage &7- manage kingdom with GUI");
        c.set("m.error", "&cError: &7failed to load kingdom manager.");
        c.set("m.error2", "&cError: &7failed to create menu.");
        c.set("m.warList1", "&cDEFENCE");
        c.set("m.warList2", "&cATTACK");
        c.set("m.warList3", "&7(Click to view war)");
        c.set("m.warList4", "&6Stage: �7");
        c.set("m.back", "&cBACK");
        c.set("m.back2", "&7(Click to go back)");
        c.set("m.warNone", "&cThere are no active wars.");
        c.set("m.teleport", "&cTeleport");
        c.set("m.teleport2", "&7(Teleport to war)");
        c.set("m.accept", "&aACCEPT WAR");
        c.set("m.accept2", "&7Kingdom loses money and land if not accepted");
        c.set("m.accept3", "&7(Click to accept war)");
        c.set("m.surrender", "&cSURRENDER WAR");
        c.set("m.surrender2", "&7Your kingdom will lose");
        c.set("m.surrender3", "&7the land which was challenged");
        c.set("m.surrender4", "&7(Click to SURRENDER)");
        c.set("m.inviteList", "&aInvite Members");
        c.set("m.inviteList2", "&7(View players to invite)");
        c.set("m.rank", "&6Rank: &7");
        c.set("m.fighter", "&4FIGHTER");
        c.set("m.online", "&a�lONLINE");
        c.set("m.offline", "&cOffline");
        c.set("m.page", "&6Page: �7");
        c.set("m.pageBack", "&cPage back");
        c.set("m.pageBack2", "&7(Go back a page)");
        c.set("m.pageForward", "&aNext page");
        c.set("m.pageForward2", "&7(Go to next page)");

        c.set("m.reputation", "&6Reputation: &7");
        c.set("m.profession", "&6Profession: &7");
        c.set("m.strength", "&6Strength: &7");
        c.set("m.toughness", "&6Toughness: &7");
        c.set("m.speed", "&6Speed: &7");
        c.set("m.stamina", "&6Stamina: &7");
        c.set("m.luck", "&6Luck: &7");

        c.set("m.setleader", "&c&lSET LEADER");
        c.set("m.setleader2", "&c(Click to set as LEADER)");

        c.set("m.fighter1", "&aCivilian");
        c.set("m.fighter2", "&7(Set as fighter)");
        c.set("m.fighter3", "&7Soldiers are main fighters in war");
        c.set("m.fighter4", "&c&lSOLDIER");
        c.set("m.fighter5", "&7(Set as civilian)");

        c.set("m.kickmember", "&cKICK MEMBER");
        c.set("m.kickmember2", "&7(Kick member from kingdom)");

        c.set("m.leader", "Leader");
        c.set("m.executive", "Executive");
        c.set("m.member", "Member");
        c.set("m.guest", "Guest");
        c.set("m.currentrank", "&7(Current rank)");

        c.set("m.setrank", "&7%a% &6was set to &7%b%");
        c.set("m.setrank2", "&2Your rank has been changed by &7%a% &6New Rank: &7%b%");
        c.set("m.setrank3", "&cThat player is already that rank.");
        c.set("m.wait", "&cPlease wait: &7");
        //1.12.0

        //1.12.2
        c.set("pholder.kingdomless", "Kingdomless");
        c.set("pholder.rankless", "Rankless");
        //1.12.2

        //1.12.3
        c.set("placeholder.guest", "G");
        c.set("placeholder.member", "M");
        c.set("placeholder.executive", "E");
        c.set("placeholder.leader", "L");
        //1.12.3

        //1.13
        c.set("loadingtext", "�7Loading...");
        c.set("unenchant.removed", "&aEnchantments removed!");
        c.set("unenchant.unable", "&cThat item does not have any enchantments on it.");
        c.set("cookedby", "&7Cooked by: &6%n%");
        //1.13

        //1.13.1
        c.set("chat.normal", "[Normal]");
        c.set("chat.kingdom", "[Kingdom]");
        c.set("chat.ally", "[Ally]");
        //1.13.1
    }


    private static void updateDefaults() {
        FileConfiguration c = Feudal.getLanguage().getConfig();

        if(!c.contains("cantunclaim")){//Update 1.6.0
            c.set("cantunclaim", "&cYou can not unclaim this chunk because it will disconnect your land.");
            c.set("marketUpdate.merchantName", "&6Seller: &7");
            c.set("unclaimUsage", "&cUsage: &7/f unclaim [all]");
            c.set("commandHelp.52", "&6&l> %a%/f unclaim [all] &7- unclaims your kingdom land");
        }

        if(!c.contains("added.enemySelf")
                || !c.contains("added.neutralSelf")
                || !c.contains("added.allySelf")
                || !c.contains("added.allyRequestSent")
                || !c.contains("added.kingdomMaxMembers")
                || !c.contains("selection.maxAtt")
                || !c.contains("selection.forceUsePoints")){	     //Pre-Release minor 1

            c.set("added.enemySelf", "&cYou can not enemy yourself.");
            c.set("added.neutralSelf", "&cYou can not neutral yourself.");
            c.set("added.allySelf", "&cYou can not ally yourself.");
            c.set("added.allyRequestSent", "&aAlly request sent.");
            c.set("added.kingdomMaxMembers", "&cThis kingdom has reached its maximum amount of members. (%members%)");
            c.set("selection.maxAtt", "&cAttribute limit has been reached.");
            c.set("selection.forceUsePoints", true);
        }

        if(!c.contains("commands.attOverall")){
            c.set("commands.attOverall", "&6Attributes: &7%a% / %b%");
        }
        if(!c.contains("newCommands.fighterArg")){
            c.set("newCommands.fighterArg", "&cCommand usage: /f figher <member>");
            c.set("newCommands.fighterNoExist", "&cThat player does not exist.");
            c.set("newCommands.notInKingdom", "&cThat player is not in your kingdom.");
            c.set("newCommands.leader", "&cYou must be leader to do that.");
            c.set("newCommands.noSelf", "&cYou can not remove yourself as a fighter.");
            c.set("newCommands.nowFighter", "&c%p% &eis now a figher.");
            c.set("newCommands.remFighter", "&b%p% &eis no longer a figher.");
            c.set("newCommands.helpFighter", "&6&l> %a%/f fighter <player> &7- toggle kingdom member as a fighter");
        }
        if(!c.contains("newTracker.noPlayers")){
            c.set("newTracker.noPlayers", "&cThere are no players to track.");
            c.set("newTracker.trackRandom", "&c&lTrack random player");
            c.set("newTracker.random", "RANDOM PLAYER");
        }
        if(!c.contains("kingdomnew.name")){
            c.set("kingdomnew.name", "&cThat kingdom name is already taken.");
        }
        if(!c.contains("reloadconfig.error")){
            c.set("reloadconfig.error", "&cError reloading configs - See console for details. Restart recommended.");
            c.set("reloadconfig.done", "&eConfigs reloaded!  This does not include any kingdom, user, or data files.");
            c.set("commandHelp.141", "&6&l> %a%/f reload &7- Reload configs (ADMIN)");
        }
        if(!c.contains("kingdomlog.noInt")){
            c.set("kingdomlog.noLog", "&cThis player has no kingdom log.");
            c.set("kingdomlog.title", "&6&lKingdom logs (%name%)");
            c.set("kingdomlog.log", "&d%join% %kingdom% %time%");
            c.set("kingdomlog.cmd", "&6&l> %a%/f kingdomlog <player> [page] &7- See a player's kingdom log (ADMIN)");
            c.set("kingdomlog.noInt", "&cThe page number must be an integer.");
        }
        if(!c.contains("newshield.cooldown")){
            c.set("newshield.cooldown", "&cYou can not shield for %time%");
        }
        if(!c.contains("spar.commandHelp")){
            c.set("spar.timeout", "&cSpar request timed out.");
            c.set("spar.inSpar", "&cYou are currently in a spar.");
            c.set("spar.inPendingSpar", "&cYou are currently in a pending spar.");
            c.set("spar.myUser", "&cYour character has not been setup or has failed to load.");
            c.set("spar.offline", "&cThat player is offline.");
            c.set("spar.noUser", "&cThat player has not setup their character yet.");
            c.set("spar.theyInSpar", "&cThat player is currently in a spar.");
            c.set("spar.noBetMoney", "&cYou do not have enough money to place that bet.");
            c.set("spar.request", "&aSpar request sent to &c%p%&a. Bet held: &c$%bet%. &aRequest will timeout in &c%timeout% &aseconds.");
            c.set("spar.requestRecieve", "&c%p%&a would like to spar you. They placed a bet of: &c$%bet%. &aRequest will timeout in &c%timeout% &aseconds. &aType &c/f spar %p% &ato begin the fight.");
            c.set("spar.accept", "&aSpar started with &c%p%. &aBet: &c$%bet%");
            c.set("spar.commandUsage", "&cUsage: /f spar <player> [bet]");
            c.set("spar.winNoWinner", "&aSpar ended. No winner.");
            c.set("spar.win", "&aYou defeated &c%p%&a in a spar.  You win &c$%bet%");
            c.set("spar.lose", "&c%p% &edefeated you in a spar.");
            c.set("spar.commandHelp", "&6&l> %a%/f spar <player> [bet] &7- Spar a player or kingdom member.");
        }
        if(!c.contains("redistribute.spendPoints")){
            c.set("redistribute.noUser", "&cYour character has not been setup or has failed to load.");
            c.set("redistribute.commandHelp", "&6&l> %a%/f attributes &7- Redistribute attributes");
            c.set("redistribute.cancel", "&c&lCANCEL");
            c.set("redistribute.spendPoints", "&cYou must spend all your points first.");
        }
        if(!c.contains("land.unconquered")){
            c.set("land.unconquered", "Unconquered Land");
            c.set("land.noClaimWorld", "&cYou can not claim land in this world.");
            c.set("spar.noSparWorld", "&cYou can not spar in this world.");
        }
        if(!c.contains("landUpdate.died")){
            c.set("landUpdate.died", "Died");
            c.set("landUpdate.killed", "Killed Player");
            c.set("landUpdate.createKingdom", " &acreated a new kingdom: &7");
            c.set("landUpdate.endin", "Ends in");
            c.set("landUpdate.day", " day");
            c.set("landUpdate.days", " days");
            c.set("landUpdate.week", " week");
            c.set("landUpdate.weeks", " weeks");
            c.set("landUpdate.year", " year");
            c.set("landUpdate.years", " years");
            c.set("landUpdate.lives", "&aLives: ");
            c.set("landUpdate.noAllies", "No allies");
            c.set("landUpdate.noEnemies", "No enemies");
            c.set("landUpdate.false", "false");
            c.set("landUpdate.true", "true");
            c.set("landUpdate.onlineNow", "ONLINE NOW");
            c.set("landUpdate.diedTooMany", " &6has died too many times.");
            c.set("landUpdate.abandoned", " &6abandoned the fight.");
            c.set("landUpdate.surrender", " &6has surrendered.");
            c.set("landUpdate.lockedProfession", "&4That profession is locked.");
        }
        if(!c.contains("allyChat.leader")){
            c.set("allyChat.leader", "&b[L]&r");
            c.set("allyChat.executive", "&a[E]&r");
            c.set("allyChat.member", "&6[M]&r");
            c.set("allyChat.guest", "&2[G]&r");
            c.set("allyChat.kingdom", "%rank% &e%name% &5&l>> &b%message%");
            c.set("allyChat.ally", "%rank%&a %kingdom% &e%name% &5&l>> &9%message%");
        }
        if(!c.contains("changeProfession.none")){
            c.set("changeProfession.none", "None");
        }
        if(!c.contains("marketUpdate.price")){
            c.set("marketUpdate.price", "&6Price: &7");
            c.set("marketUpdate.amount", "&6Amount: &7");
            c.set("marketUpdate.totalPrice", "&6Total Price: &7");
            c.set("marketUpdate.balance", "&6Balance: &7");
            c.set("marketUpdate.maxAmount", "&6Max Amount: &7");
        }
        if(!c.contains("kingdom.info.shield")){
            c.set("kingdom.info.shield", "&6Shield: &7%time%");
        }
        if(!c.contains("commandTreasuryNew.denyChallenge")){
            c.set("commandTreasuryNew.denyChallenge", "&cYou can not remove money from your treasury while there is an active challenge against you.");
        }
        if(!c.contains("findclaims.none")) {
            c.set("commandHelp.141", "&6&l> %a%/f findclaims &7- View claimed land");
            c.set("findclaims.claims", "&e&lChunk Claims: (x, z)");
            c.set("findclaims.none", "&cYour kingdom does not have any land.");
        }
        if(!c.contains("181.mute")) {
            c.set("181.mute", "�cYou cannot talk in kingdom chat while muted.");
            c.set("181.findclaims", "�7Showing claimed land nearby.");
        }
        if(!c.contains("commands.kingdomCost")) {
            c.set("commands.kingdomCost", "&cYou must have at least &7$%cost% &cto create a kingdom.");
            c.set("commands.kingdomBuy", "&d$%cost% &aused to create a kingdom.");
            c.set("commands.marketCat", "&cThat category does not exist.");
        }
        if(!c.contains("saveJoinMessage")) {
            c.set("saveJoinMessage", "&eYour user information is currently saving. Please try again in a moment.");
        }
        if(!c.contains("landBannedCommand")) {
            //1.12.0
            c.set("landBannedCommand", "&cYou can not use that command while on another kingdom's land.");
            c.set("m.title", "&cKingdom Manager");
            c.set("m.title2", "&cWar List");
            c.set("m.title3", "&cWar Info");
            c.set("m.title4", "&cManage Members");
            c.set("m.title5", "&cInvite Members");
            c.set("m.title6", "&cManage Member");
            c.set("m.banner1", "&6Members: �7");
            c.set("m.banner2", "&6Treasury: &7$");
            c.set("m.banner3", "&6Wars won: &7");
            c.set("m.banner4", "&7(Click to view members)");
            c.set("m.home", "&cHOME");
            c.set("m.home2", "&7(Teleport to kingdom home)");
            c.set("m.claim", "&aClaim land");
            c.set("m.claim2", "&7(Click to claim)");
            c.set("m.unclaim", "&cUnclaim land");
            c.set("m.unclaim2", "&7(Click to unclaim)");
            c.set("m.sethome", "&dSet kingdom home");
            c.set("m.sethome2", "&7(Set kingdom home)");
            c.set("m.findclaims", "&dFind claims");
            c.set("m.findclaims2", "&7(Show your claims around you)");
            c.set("m.view", "&dKingdom map");
            c.set("m.view2", "&7(Show map of kingdom claims)");
            c.set("m.delete", "&cDELETE KINGDOM");
            c.set("m.delete2", "&7(Click to delete kingdom)");
            c.set("m.leave", "&cLEAVE KINGDOM");
            c.set("m.leave2", "&7(Click to leave kingdom)");
            c.set("m.war", "&cWAR INFO");
            c.set("m.war2", "&6Active wars: &7");
            c.set("m.war3", "&7(Click to view war info)");
            c.set("commandHelp.460", "&6&l> %a%/f manage &7- manage kingdom with GUI");
            c.set("m.error", "&cError: &7failed to load kingdom manager.");
            c.set("m.error2", "&cError: &7failed to create menu.");
            c.set("m.warList1", "&cDEFENCE");
            c.set("m.warList2", "&cATTACK");
            c.set("m.warList3", "&7(Click to view war)");
            c.set("m.warList4", "&6Stage: �7");
            c.set("m.back", "&cBACK");
            c.set("m.back2", "&7(Click to go back)");
            c.set("m.warNone", "&cThere are no active wars.");
            c.set("m.teleport", "&cTeleport");
            c.set("m.teleport2", "&7(Teleport to war)");
            c.set("m.accept", "&aACCEPT WAR");
            c.set("m.accept2", "&7Kingdom loses money and land if not accepted");
            c.set("m.accept3", "&7(Click to accept war)");
            c.set("m.surrender", "&cSURRENDER WAR");
            c.set("m.surrender2", "&7Your kingdom will lose");
            c.set("m.surrender3", "&7the land which was challenged");
            c.set("m.surrender4", "&7(Click to SURRENDER)");
            c.set("m.inviteList", "&aInvite Members");
            c.set("m.inviteList2", "&7(View players to invite)");
            c.set("m.rank", "&6Rank: &7");
            c.set("m.fighter", "&4FIGHTER");
            c.set("m.online", "&a�lONLINE");
            c.set("m.offline", "&cOffline");
            c.set("m.page", "&6Page: �7");
            c.set("m.pageBack", "&cPage back");
            c.set("m.pageBack2", "&7(Go back a page)");
            c.set("m.pageForward", "&aNext page");
            c.set("m.pageForward2", "&7(Go to next page)");

            c.set("m.reputation", "&6Reputation: &7");
            c.set("m.profession", "&6Profession: &7");
            c.set("m.strength", "&6Strength: &7");
            c.set("m.toughness", "&6Toughness: &7");
            c.set("m.speed", "&6Speed: &7");
            c.set("m.stamina", "&6Stamina: &7");
            c.set("m.luck", "&6Luck: &7");

            c.set("m.setleader", "&c&lSET LEADER");
            c.set("m.setleader2", "&c(Click to set as LEADER)");

            c.set("m.fighter1", "&aCivilian");
            c.set("m.fighter2", "&7(Set as fighter)");
            c.set("m.fighter3", "&7Soldiers are main fighters in war");
            c.set("m.fighter4", "&c&lSOLDIER");
            c.set("m.fighter5", "&7(Set as civilian)");

            c.set("m.kickmember", "&cKICK MEMBER");
            c.set("m.kickmember2", "&7(Kick member from kingdom)");

            c.set("m.leader", "Leader");
            c.set("m.executive", "Executive");
            c.set("m.member", "Member");
            c.set("m.guest", "Guest");
            c.set("m.currentrank", "&7(Current rank)");

            c.set("m.setrank", "&7%a% &6was set to &7%b%");
            c.set("m.setrank2", "&2Your rank has been changed by &7%a% &6New Rank: &7%b%");
            c.set("m.setrank3", "&cThat player is already that rank.");
            c.set("m.wait", "&cPlease wait: &7");
            //1.12.0
        }
        if(!c.contains("pholder.kingdomless")) {
            //1.12.2
            c.set("pholder.kingdomless", "Kingdomless");
            c.set("pholder.rankless", "Rankless");
            //1.12.2
        }
        if(!c.contains("changeProfession.Squire")) {
            c.set("changeProfession.Squire", "&a&lSquire");
        }
        if(!c.contains("placeholder.guest")) {
            c.set("placeholder.guest", "G");
            c.set("placeholder.member", "M");
            c.set("placeholder.executive", "E");
            c.set("placeholder.leader", "L");
        }
        if(!c.contains("spar.sparSelf")) {
            //1.13
            c.set("spar.sparSelf", "&cYou cannot spar yourself!");
            c.set("loadingtext", "�7Loading...");
            c.set("commandHelp.new1246", "&6&l> %a%/f unenchant &7- unenchant item in hand");
            c.set("unenchant.removed", "&aEnchantments removed!");
            c.set("unenchant.unable", "&cThat item does not have any enchantments on it.");
            c.set("cookedby", "&7Cooked by: &6%n%");
            //1.13
        }
        if(!c.contains("chat.normal")) {
            //1.13.1
            c.set("chat.normal", "[Normal]");
            c.set("chat.kingdom", "[Kingdom]");
            c.set("chat.ally", "[Ally]");
            //1.13.1
        }
    }
}