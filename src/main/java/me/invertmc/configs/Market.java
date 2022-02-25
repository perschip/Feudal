package me.invertmc.configs;

import java.io.File;
import java.util.ArrayList;

import me.invertmc.Feudal;
import me.invertmc.market.Category;
import org.bukkit.configuration.file.FileConfiguration;

public class Market {
    private static void createMarketConfigDefaults() {
        final ArrayList<String> foodstuff = new ArrayList<>();
        foodstuff.add("APPLE");
        foodstuff.add("MUSHROOM_SOUP");
        foodstuff.add("BREAD");
        foodstuff.add("PORK");
        foodstuff.add("GRILLED_PORK");
        foodstuff.add("GOLDEN_APPLE");
        foodstuff.add("RAW_FISH");
        foodstuff.add("COOKED_FISH");
        foodstuff.add("CAKE");
        foodstuff.add("COOKIE");
        foodstuff.add("MELON");
        foodstuff.add("RAW_BEEF");
        foodstuff.add("COOKED_BEEF");
        foodstuff.add("RAW_CHICKEN");
        foodstuff.add("COOKED_CHICKEN");
        foodstuff.add("ROTTEN_FLESH");
        foodstuff.add("SPIDER_EYE");
        foodstuff.add("CARROT_ITEM");
        foodstuff.add("POTATO_ITEM");
        foodstuff.add("BAKED_POTATO");
        foodstuff.add("POISONOUS_POTATO");
        foodstuff.add("PUMPKIN_PIE");
        foodstuff.add("RABBIT");
        foodstuff.add("COOKED_RABBIT");
        foodstuff.add("RABBIT_STEW");
        foodstuff.add("MUTTON");
        foodstuff.add("COOKED_MUTTON");
        foodstuff.add("BEETROOT");
        foodstuff.add("BEETROOT_SOUP");
        foodstuff.add("DRIED_KELP");
        Feudal.getMarketConfig().getConfig().set("categories.FOODSTUFFS", foodstuff);

        final ArrayList<String> tools = new ArrayList<>();
        tools.add("IRON_SHOVEL");
        tools.add("IRON_PICKAXE");
        tools.add("IRON_AXE");
        tools.add("FLINT_AND_STEEL");
        tools.add("WOODEN_SHOVEL");
        tools.add("WOODEn_PICKAXE");
        tools.add("WOODEN_AXE");
        tools.add("STONE_SHOVEL");
        tools.add("STONE_PICKAXE");
        tools.add("STONE_AXE");
        tools.add("DIAMOND_SHOVEL");
        tools.add("DIAMOND_PICKAXE");
        tools.add("DIAMOND_AXE");
        tools.add("GOLDEN_SHOVEL");
        tools.add("GOLDEN_PICKAXE");
        tools.add("GOLDEN_AXE");
        tools.add("WOODEN_HOE");
        tools.add("STONE_HOE");
        tools.add("IRON_HOE");
        tools.add("DIAMOND_HOE");
        tools.add("GOLDEN_HOE");
        tools.add("COMPASS");
        tools.add("FISHING_ROD");
        tools.add("WATCH");
        tools.add("SHEARS");
        tools.add("LEASH");
        tools.add("NAME_TAG");
        Feudal.getMarketConfig().getConfig().set("categories.TOOLS", tools);

        final ArrayList<String> combat = new ArrayList<>();
        combat.add("BOW");
        combat.add("ARROW");
        combat.add("IRON_SWORD");
        combat.add("WOODEN_SWORD");
        combat.add("STONE_SWORD");
        combat.add("DIAMOND_SWORD");
        combat.add("GOLDEn_SWORD");
        combat.add("LEATHER_HELMET");
        combat.add("LEATHER_CHESTPLATE");
        combat.add("LEATHER_LEGGINGS");
        combat.add("LEATHER_BOOTS");
        combat.add("CHAINMAIL_HELMET");
        combat.add("CHAINMAIL_CHESTPLATE");
        combat.add("CHAINMAIL_LEGGINGS");
        combat.add("CHAINMAIL_BOOTS");
        combat.add("IRON_HELMET");
        combat.add("IRON_CHESTPLATE");
        combat.add("IRON_LEGGINGS");
        combat.add("IRON_BOOTS");
        combat.add("DIAMOND_HELMET");
        combat.add("DIAMOND_CHESTPLATE");
        combat.add("DIAMOND_LEGGINGS");
        combat.add("DIAMOND_BOOTS");
        combat.add("GOLDEN_HELMET");
        combat.add("GOLDEN_CHESTPLATE");
        combat.add("GOLDEN_LEGGINGS");
        combat.add("GOLDEN_BOOTS");
        combat.add("SHIELD");
        combat.add("SPECTRAL_ARROW");
        combat.add("TIPPED_ARROW");
        //1.11
        combat.add("TOTEM");
        //
        Feudal.getMarketConfig().getConfig().set("categories.COMBAT", combat);

        final ArrayList<String> brewing = new ArrayList<>();
        brewing.add("GHAST_TEAR");
        brewing.add("POTION");
        brewing.add("GLASS_BOTTLE");
        brewing.add("FERMENTED_SPIDER_EYE");
        brewing.add("BLAZE_POWDER");
        brewing.add("MAGMA_CREAM");
        brewing.add("BREWING_STAND_ITEM");
        brewing.add("CAULDRON_ITEM");
        brewing.add("SPECKLED_MELON");
        brewing.add("GOLDEN_CARROT");
        brewing.add("RABBIT_FOOT");
        brewing.add("SPLASH_POTION");
        brewing.add("LINGERING_POTION");
        brewing.add("DRAGONS_BREATH");
        Feudal.getMarketConfig().getConfig().set("categories.BREWING", brewing);

        final ArrayList<String> materials = new ArrayList<>();
        materials.add("COAL");
        materials.add("DIAMOND");
        materials.add("IRON_INGOT");
        materials.add("GOLD_INGOT");
        materials.add("STICK");
        materials.add("BOWL");
        materials.add("STRING");
        materials.add("FEATHER");
        materials.add("SULPHUR");
        materials.add("SEEDS");
        materials.add("WHEAT");
        materials.add("FLINT");
        materials.add("LEATHER");
        materials.add("CLAY_BRICK");
        materials.add("CLAY_BALL");
        materials.add("SUGAR_CANE");
        materials.add("EGG");
        materials.add("GLOWSTONE_DUST");
        materials.add("INK_SACK");
        materials.add("SUGAR");
        materials.add("PUMPKIN_SEEDS");
        materials.add("MELON_SEEDS");
        materials.add("BLAZE_ROD");
        materials.add("GOLD_NUGGET");
        materials.add("NETHER_STALK");
        materials.add("EMERALD");
        materials.add("NETHER_STAR");
        materials.add("NETHER_BRICK_ITEM");
        materials.add("QUARTZ");
        materials.add("PRISMARINE_SHARD");
        materials.add("PRISMARINE_CRYSTALS");
        materials.add("RABBIT_HIDE");
        materials.add("BEETROOT_SEEDS");
        materials.add("CHORUS_FRUIT");
        materials.add("CHORUS_FRUIT_POPPED");
        //1.11
        materials.add("SHULKER_SHELL");
        //
        materials.add("CONCRETE_POWDER");
        Feudal.getMarketConfig().getConfig().set("categories.MATERIALS", materials);

        final ArrayList<String> buildingBlocks = new ArrayList<>();
        buildingBlocks.add("NETHER_BRICK");
        buildingBlocks.add("STONE");
        buildingBlocks.add("GRASS");
        buildingBlocks.add("GRASS_BLOCK");
        buildingBlocks.add("DIRT");
        buildingBlocks.add("COBBLESTONE");
        buildingBlocks.add("WOOD");
        buildingBlocks.add("BEDROCK");
        buildingBlocks.add("SAND");
        buildingBlocks.add("GRAVEL");
        buildingBlocks.add("GOLD_ORE");
        buildingBlocks.add("IRON_ORE");
        buildingBlocks.add("COAL_ORE");
        buildingBlocks.add("LOG");
        buildingBlocks.add("SPONGE");
        buildingBlocks.add("GLASS");
        buildingBlocks.add("LAPIS_ORE");
        buildingBlocks.add("LAPIS_BLOCK");
        buildingBlocks.add("SANDSTONE");
        buildingBlocks.add("WHITE_WOOL");
        buildingBlocks.add("BLACK_WOOL");
        buildingBlocks.add("BLUE_WOOL");
        buildingBlocks.add("BROWN_WOOL");
        buildingBlocks.add("CYAN_WOOL");
        buildingBlocks.add("GRAY_WOOL");
        buildingBlocks.add("GREEN_WOOL");
        buildingBlocks.add("LIGHT_BLUE_WOOL");
        buildingBlocks.add("LIGHT_GRAY_WOOL");
        buildingBlocks.add("LIME_WOOL");
        buildingBlocks.add("MAGENTA_WOOL");
        buildingBlocks.add("ORANGE_WOOL");
        buildingBlocks.add("PINK_WOOL");
        buildingBlocks.add("PURPLE_WOOL");
        buildingBlocks.add("RED_WOOL");
        buildingBlocks.add("YELLOW_WOOL");
        buildingBlocks.add("GOLD_BLOCK");
        buildingBlocks.add("IRON_BLOCK");
        buildingBlocks.add("STEP");
        buildingBlocks.add("BRICK");
        buildingBlocks.add("BOOKSHELF");
        buildingBlocks.add("MOSSY_COBBLESTONE");
        buildingBlocks.add("OBSIDIAN");
        buildingBlocks.add("WOOD_STAIRS");
        buildingBlocks.add("DIAMOND_ORE");
        buildingBlocks.add("DIAMOND_BLOCK");
        buildingBlocks.add("COBBLESTONE_STAIRS");
        buildingBlocks.add("REDSTONE_ORE");
        buildingBlocks.add("ICE");
        buildingBlocks.add("SNOW_BLOCK");
        buildingBlocks.add("CLAY");
        buildingBlocks.add("PUMPKIN");
        buildingBlocks.add("NETHERRACK");
        buildingBlocks.add("SOUL_SAND");
        buildingBlocks.add("GLOWSTONE");
        buildingBlocks.add("JACK_O_LANTERN");
        buildingBlocks.add("STAINED_GLASS");
        buildingBlocks.add("SMOOTH_BRICK");
        buildingBlocks.add("MELON_BLOCK");
        buildingBlocks.add("BRICK_STAIRS");
        buildingBlocks.add("SMOOTH_STAIRS");
        buildingBlocks.add("MYCEL");
        buildingBlocks.add("NETHER_BRICK");
        buildingBlocks.add("NETHER_BRICK_STAIRS");
        buildingBlocks.add("ENDER_STONE");
        buildingBlocks.add("WOOD_STEP");
        buildingBlocks.add("SANDSTONE_STAIRS");
        buildingBlocks.add("EMERALD_ORE");
        buildingBlocks.add("EMERALD_BLOCK");
        buildingBlocks.add("SPRUCE_WOOD_STAIRS");
        buildingBlocks.add("BIRCH_WOOD_STAIRS");
        buildingBlocks.add("JUNGLE_WOOD_STAIRS");
        buildingBlocks.add("COBBLE_WALL");
        buildingBlocks.add("QUARTZ_ORE");
        buildingBlocks.add("QUARTZ_BLOCK");
        buildingBlocks.add("QUARTZ_STAIRS");
        buildingBlocks.add("STAINED_CLAY");
        buildingBlocks.add("LOG_2");
        buildingBlocks.add("ACACIA_STAIRS");
        buildingBlocks.add("DARK_OAK_STAIRS");
        buildingBlocks.add("PRISMARINE");
        buildingBlocks.add("SEA_LANTERN");
        buildingBlocks.add("HAY_BLOCK");
        buildingBlocks.add("HARD_CLAY");
        buildingBlocks.add("COAL_BLOCK");
        buildingBlocks.add("PACKED_ICE");
        buildingBlocks.add("RED_SANDSTONE");
        buildingBlocks.add("RED_SANDSTONE_STAIRS");
        buildingBlocks.add("STONE_SLAB2");
        buildingBlocks.add("PURPUR_BLOCK");
        buildingBlocks.add("PURPUR_PILLAR");
        buildingBlocks.add("PURPUR_STAIRS");
        buildingBlocks.add("PURPUR_SLAB");
        buildingBlocks.add("END_BRICKS");
        buildingBlocks.add("CONCRETE");
        Feudal.getMarketConfig().getConfig().set("categories.BUILDING_BLOCKS", buildingBlocks);

        final ArrayList<String> decorationBlocks = new ArrayList<>();
        decorationBlocks.add("SAPLING");
        decorationBlocks.add("LEAVES");
        decorationBlocks.add("WEB");
        decorationBlocks.add("LONG_GRASS");
        decorationBlocks.add("DEAD_BUSH");
        decorationBlocks.add("YELLOW_FLOWER");
        decorationBlocks.add("RED_ROSE");
        decorationBlocks.add("BROWN_MUSHROOM");
        decorationBlocks.add("RED_MUSHROOM");
        decorationBlocks.add("TORCH");
        decorationBlocks.add("CHEST");
        decorationBlocks.add("WORKBENCH");
        decorationBlocks.add("FURNACE");
        decorationBlocks.add("LADDER");
        decorationBlocks.add("SNOW");
        decorationBlocks.add("CACTUS");
        decorationBlocks.add("JUKEBOX");
        decorationBlocks.add("FENCE");
        decorationBlocks.add("MONSTER_EGGS");
        decorationBlocks.add("IRON_FENCE");
        decorationBlocks.add("THIN_GLASS");
        decorationBlocks.add("VINE");
        decorationBlocks.add("WATER_LILY");
        decorationBlocks.add("NETHER_FENCE");
        decorationBlocks.add("ENCHANTMENT_TABLE");
        decorationBlocks.add("ENDER_PORTAL_FRAME");
        decorationBlocks.add("ENDER_CHEST");
        decorationBlocks.add("ANVIL");
        decorationBlocks.add("TRAPPED_CHEST");
        decorationBlocks.add("STAINED_GLASS_PANE");
        decorationBlocks.add("LEAVES_2");
        decorationBlocks.add("SLIME_BLOCK");
        decorationBlocks.add("CARPET");
        decorationBlocks.add("DOUBLE_PLANT");
        decorationBlocks.add("SPRUCE_FENCE");
        decorationBlocks.add("BIRCH_FENCE");
        decorationBlocks.add("JUNGLE_FENCE");
        decorationBlocks.add("DARK_OAK_FENCE");
        decorationBlocks.add("ACACIA_FENCE");
        decorationBlocks.add("PAINTING");
        decorationBlocks.add("SIGN");
        decorationBlocks.add("WHITE_BED");
        decorationBlocks.add("BLACK_BED");
        decorationBlocks.add("BROWN_BED");
        decorationBlocks.add("BLUE_BED");
        decorationBlocks.add("CYAN_BED");
        decorationBlocks.add("GRAY_BED");
        decorationBlocks.add("GREEN_BED");
        decorationBlocks.add("LIGHT_BLUE_BED");
        decorationBlocks.add("LIGHT_GRAY_BED");
        decorationBlocks.add("LIME_BED");
        decorationBlocks.add("MAGENTA_BED");
        decorationBlocks.add("ORANGE_BED");
        decorationBlocks.add("PINK_BED");
        decorationBlocks.add("PURPLE_BED");
        decorationBlocks.add("RED_BED");
        decorationBlocks.add("ITEM_FRAME");
        decorationBlocks.add("FLOWER_POT_ITEM");
        decorationBlocks.add("SKULL_ITEM");
        decorationBlocks.add("ARMOR_STAND");
        decorationBlocks.add("BANNER");
        decorationBlocks.add("CHORUS_FLOWER");
        decorationBlocks.add("END_ROD");
        decorationBlocks.add("END_CRYSTAL");
        //1.11
        decorationBlocks.add("WHITE_SHULKER_BOX");
        decorationBlocks.add("ORANGE_SHULKER_BOX");
        decorationBlocks.add("MAGENTA_SHULKER_BOX");
        decorationBlocks.add("LIGHT_BLUE_SHULKER_BOX");
        decorationBlocks.add("YELLOW_SHULKER_BOX");
        decorationBlocks.add("LIME_SHULKER_BOX");
        decorationBlocks.add("PINK_SHULKER_BOX");
        decorationBlocks.add("GRAY_SHULKER_BOX");
        decorationBlocks.add("SILVER_SHULKER_BOX");
        decorationBlocks.add("CYAN_SHULKER_BOX");
        decorationBlocks.add("PURPLE_SHULKER_BOX");
        decorationBlocks.add("BLUE_SHULKER_BOX");
        decorationBlocks.add("BROWN_SHULKER_BOX");
        decorationBlocks.add("GREEN_SHULKER_BOX");
        decorationBlocks.add("RED_SHULKER_BOX");
        decorationBlocks.add("BLACK_SHULKER_BOX");
        //
        Feudal.getMarketConfig().getConfig().set("categories.DECORATION_BLOCKS", decorationBlocks);

        final ArrayList<String> redstone = new ArrayList<>();
        redstone.add("DISPENSER");
        redstone.add("NOTE_BLOCK");
        redstone.add("PISTON_STICKY_BASE");
        redstone.add("PISTON_BASE");
        redstone.add("TNT");
        redstone.add("LEVER");
        redstone.add("STONE_PLATE");
        redstone.add("WOOD_PLATE");
        redstone.add("REDSTONE_TORCH_ON");
        redstone.add("STONE_BUTTON");
        redstone.add("TRAP_DOOR");
        redstone.add("FENCE_GATE");
        redstone.add("REDSTONE_LAMP_OFF");
        redstone.add("TRIPWIRE_HOOK");
        redstone.add("WOOD_BUTTON");
        redstone.add("GOLD_PLATE");
        redstone.add("IRON_PLATE");
        redstone.add("DAYLIGHT_DETECTOR");
        redstone.add("REDSTONE_BLOCK");
        redstone.add("HOPPER");
        redstone.add("DROPPER");
        redstone.add("IRON_TRAPDOOR");
        redstone.add("SPRUCE_FENCE_GATE");
        redstone.add("BIRCH_FENCE_GATE");
        redstone.add("JUNGLE_FENCE_GATE");
        redstone.add("DARK_OAK_FENCE_GATE");
        redstone.add("ACACIA_FENCE_GATE");
        redstone.add("OAK_DOOR");
        redstone.add("IRON_DOOR");
        redstone.add("REDSTONE");
        redstone.add("DIODE");
        redstone.add("REDSTONE_COMPARATOR");
        redstone.add("SPRUCE_DOOR");
        redstone.add("BIRCH_DOOR");
        redstone.add("JUNGLE_DOOR");
        redstone.add("ACACIA_DOOR");
        redstone.add("DARK_OAK_DOOR");
        //1.11
        redstone.add("OBSERVER");
        //
        Feudal.getMarketConfig().getConfig().set("categories.REDSTONE", redstone);

        final ArrayList<String> transportation = new ArrayList<>();
        transportation.add("POWERED_RAIL");
        transportation.add("DETECTOR_RAIL");
        transportation.add("RAILS");
        transportation.add("ACTIVATOR_RAIL");
        transportation.add("MINECART");
        transportation.add("SADDLE");
        transportation.add("BOAT");
        transportation.add("STORAGE_MINECART");
        transportation.add("POWERED_MINECART");
        transportation.add("CARROT_STICK");
        transportation.add("EXPLOSIVE_MINECART");
        transportation.add("HOPPER_MINECART");
        transportation.add("ELYTRA");
        transportation.add("SPRUCE_BOAT");
        transportation.add("BIRCH_BOAT");
        transportation.add("ACACIA_BOAT");
        transportation.add("JUNGLE_BOAT");
        transportation.add("DARK_OAK_BOAT");
        Feudal.getMarketConfig().getConfig().set("categories.TRANSPORTATION", transportation);

        final ArrayList<String> miscellaneous = new ArrayList<>();
        miscellaneous.add("BEACON");
        miscellaneous.add("BUCKET");
        miscellaneous.add("WATER_BUCKET");
        miscellaneous.add("LAVA_BUCKET");
        miscellaneous.add("SNOW_BALL");
        miscellaneous.add("MILK_BUCKET");
        miscellaneous.add("PAPER");
        miscellaneous.add("BOOK");
        miscellaneous.add("SLIME_BALL");
        miscellaneous.add("BONE");
        miscellaneous.add("ENDER_PEARL");
        miscellaneous.add("EYE_OF_ENDER");
        miscellaneous.add("MONSTER_EGG");
        miscellaneous.add("EXP_BOTTLE");
        miscellaneous.add("FIREBALL");
        miscellaneous.add("BOOK_AND_QUILL");
        miscellaneous.add("EMPTY_MAP");
        miscellaneous.add("MAP");
        miscellaneous.add("FIREWORK_CHARGE");
        miscellaneous.add("IRON_BARDING");
        miscellaneous.add("GOLD_BARDING");
        miscellaneous.add("DIAMOND_BARDING");
        miscellaneous.add("GOLD_RECORD");
        miscellaneous.add("GREEN_RECORD");
        miscellaneous.add("RECORD_3");
        miscellaneous.add("RECORD_4");
        miscellaneous.add("RECORD_5");
        miscellaneous.add("RECORD_6");
        miscellaneous.add("RECORD_7");
        miscellaneous.add("RECORD_8");
        miscellaneous.add("RECORD_9");
        miscellaneous.add("RECORD_10");
        miscellaneous.add("RECORD_11");
        miscellaneous.add("RECORD_12");
        miscellaneous.add("ENCHANTED_BOOK");
        miscellaneous.add("WRITTEN_BOOK");
        Feudal.getMarketConfig().getConfig().set("categories.MISCELLANEOUS", miscellaneous);

        //////
        //ALL
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.ALL.index", 22);
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.ALL.item.type", "COMPASS");
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.ALL.item.data", 0);
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.ALL.item.name", "&c&lALL");

        //FOODSTUFFS
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.FOODSTUFFS.index", 0);
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.FOODSTUFFS.item.type", "APPLE");
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.FOODSTUFFS.item.data", 0);
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.FOODSTUFFS.item.name", "&c&lFood stuff");

        //TOOLS
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.TOOLS.index", 2);
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.TOOLS.item.type", "IRON_AXE");
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.TOOLS.item.data", 0);
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.TOOLS.item.name", "&c&lTools");

        //COMBAT
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.COMBAT.index", 4);
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.COMBAT.item.type", "GOLD_SWORD");
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.COMBAT.item.data", 0);
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.COMBAT.item.name", "&c&lCombat");

        //BREWING
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.BREWING.index", 6);
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.BREWING.item.type", "GLASS_BOTTLE");
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.BREWING.item.data", 0);
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.BREWING.item.name", "&c&lBrewing");

        //MATERIALS
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.MATERIALS.index", 8);
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.MATERIALS.item.type", "STICK");
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.MATERIALS.item.data", 0);
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.MATERIALS.item.name", "&c&lMaterials");

        //BUILDING BLOCKS
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.BUILDING_BLOCKS.index", 36);
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.BUILDING_BLOCKS.item.type", "BRICK");
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.BUILDING_BLOCKS.item.data", 0);
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.BUILDING_BLOCKS.item.name", "&c&lBuilding Blocks");

        //DECORATION BLOCKS
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.DECORATION_BLOCKS.index", 38);
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.DECORATION_BLOCKS.item.type", "DOUBLE_PLANT");
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.DECORATION_BLOCKS.item.data", 5);
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.DECORATION_BLOCKS.item.name", "&c&lDecoration Blocks");

        //REDSTONE
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.REDSTONE.index", 40);
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.REDSTONE.item.type", "REDSTONE");
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.REDSTONE.item.data", 0);
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.REDSTONE.item.name", "&c&lRedstone");

        //TRANSPORTATION
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.TRANSPORTATION.index", 42);
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.TRANSPORTATION.item.type", "POWERED_RAIL");
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.TRANSPORTATION.item.data", 0);
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.TRANSPORTATION.item.name", "&c&lTransportation");

        //MISCELLANEOUS
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.MISCELLANEOUS.index", 44);
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.MISCELLANEOUS.item.type", "LAVA_BUCKET");
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.MISCELLANEOUS.item.data", 0);
        Feudal.getMarketConfig().getConfig().set("categoriesInventory.MISCELLANEOUS.item.name", "&c&lMiscellaneous");
        ////////


        //1.13 & 1.14 Items
        Feudal.getMarketConfig().getConfig().set("prices.buy.BLUE_ICE.price", 4);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BLUE_ICE.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.DARK_OAK_BUTTON.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.sell.DARK_OAK_BUTTON.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.buy.JUNGLE_BUTTON.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.sell.JUNGLE_BUTTON.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPRUCE_BUTTON.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPRUCE_BUTTON.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.buy.ACACIA_BUTTON.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.sell.ACACIA_BUTTON.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.buy.BIRCH_BUTTON.price", 4);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BIRCH_BUTTON.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.buy.CARVED_PUMPKIN.price", 8);
        Feudal.getMarketConfig().getConfig().set("prices.sell.CARVED_PUMPKIN.price", 4);
        Feudal.getMarketConfig().getConfig().set("prices.buy.CONDUIT.price", 7000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.CONDUIT.price", 2500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.KELP.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.sell.KELP.price", 0.33);
        Feudal.getMarketConfig().getConfig().set("prices.buy.DRIED_KELP.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.sell.DIRED_KELP.price", 0.23);
        Feudal.getMarketConfig().getConfig().set("prices.buy.DRIED_KELP_BLOCK.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.sell.DRIED_KELP_BLOCK.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.buy.PRISMARINE_BRICK_SLAB.price", 8);
        Feudal.getMarketConfig().getConfig().set("prices.sell.PRISMARINE_BRICK_SLAB.price", 4);
        Feudal.getMarketConfig().getConfig().set("prices.buy.PRISMARINE_BRICK_STAIRS.price", 8);
        Feudal.getMarketConfig().getConfig().set("prices.sell.PRISMARINE_BRICK_STAIRS.price", 4);
        Feudal.getMarketConfig().getConfig().set("prices.buy.DARK_PRISMARINE_SLAB.price", 8);
        Feudal.getMarketConfig().getConfig().set("prices.sell.DARK_PRISMARINE_SLAB.price", 4);
        Feudal.getMarketConfig().getConfig().set("prices.buy.DARK_PRISMARINE_STAIRS.price", 8);
        Feudal.getMarketConfig().getConfig().set("prices.sell.DARK_PRISMARINE_STAIRS.price", 4);
        Feudal.getMarketConfig().getConfig().set("prices.buy.PRISMARINE_STAIRS.price", 8);
        Feudal.getMarketConfig().getConfig().set("prices.sell.PRISMARINE_STAIRS.price", 4);
        Feudal.getMarketConfig().getConfig().set("prices.buy.PRISMARINE_WALL.price", 8);
        Feudal.getMarketConfig().getConfig().set("prices.sell.PRISMARINE_WALL.price", 4);
        Feudal.getMarketConfig().getConfig().set("prices.buy.PRISMARINE_SLAB.price", 8);
        Feudal.getMarketConfig().getConfig().set("prices.sell.PRISMARINE_SLAB.price", 4);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SEAGRASS.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SEAGRASS.price", 0.11);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SEA_PICKLE.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SEA_PICKLE.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.buy.TURTLE_EGG.price", 200);
        Feudal.getMarketConfig().getConfig().set("prices.sell.TURTLE_EGG.price", 75);
        Feudal.getMarketConfig().getConfig().set("prices.buy.TURTLE_HELMET.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.sell.TURTLE_HELMET.price", 125.30);
        Feudal.getMarketConfig().getConfig().set("prices.buy.HEART_OF_THE_SEA.price", 1250);
        Feudal.getMarketConfig().getConfig().set("prices.sell.HEART_OF_THE_SEA.price", 475);
        Feudal.getMarketConfig().getConfig().set("prices.buy.NAUTILUS_SHELL.price", 900);
        Feudal.getMarketConfig().getConfig().set("prices.sell.NAUTILUS_SHELL.price", 220);
        Feudal.getMarketConfig().getConfig().set("prices.buy.PHANTOM_MEMBRANE.price", 350);
        Feudal.getMarketConfig().getConfig().set("prices.sell.PHANTOM_MEMBRANE.price", 125);
        Feudal.getMarketConfig().getConfig().set("prices.buy.TRIDENT.price", 2000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.TRIDENT.price", 1025.50);
        Feudal.getMarketConfig().getConfig().set("prices.buy.COD.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.sell.COD.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.buy.COOKED_COD.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.sell.COOKED_COD.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.buy.COOKED_SALMON.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.sell.COOKED_SALMON.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SALMON.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SALMON.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.buy.PUFFERFISH.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.sell.PUFFERFISH.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.buy.TROPICAL_FISH.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.sell.TROPICAL_FISH.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.buy.BAMBOO.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BAMBOO.price", 3);
        Feudal.getMarketConfig().getConfig().set("prices.buy.BARREL.price", 5);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BARREL.price", 2.5);
        Feudal.getMarketConfig().getConfig().set("prices.buy.BELL.price", 50);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BELL.price", 15);
        Feudal.getMarketConfig().getConfig().set("prices.buy.BLAST_FURNACE.price", 75);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BLAST_FURNACE.price", 37);
        Feudal.getMarketConfig().getConfig().set("prices.buy.LANTERN.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.sell.LANTERN.price", 20);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SCAFFOLDING.price", 15);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SCAFFOLDING.price", 7);
        Feudal.getMarketConfig().getConfig().set("prices.buy.CROSSBOW.price", 2000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.CROSSBOW.price", 1300);
        Feudal.getMarketConfig().getConfig().set("prices.buy.CARROT.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.sell.CARROT.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTATO.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTATO.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.buy.BLACK_BED.price", 24);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BLACK_BED.price", 12);
        Feudal.getMarketConfig().getConfig().set("prices.buy.RED_BED.price", 24);
        Feudal.getMarketConfig().getConfig().set("prices.sell.RED_BED.price", 12);
        Feudal.getMarketConfig().getConfig().set("prices.buy.YELLOW_BED.price", 24);
        Feudal.getMarketConfig().getConfig().set("prices.sell.YELLOW_BED.price", 12);
        Feudal.getMarketConfig().getConfig().set("prices.buy.BLUE_BED.price", 24);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BLUE_BED.price", 12);
        Feudal.getMarketConfig().getConfig().set("prices.buy.BROWN_BED.price", 24);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BROWN_BED.price", 12);
        Feudal.getMarketConfig().getConfig().set("prices.buy.CYAN_BED.price", 24);
        Feudal.getMarketConfig().getConfig().set("prices.sell.CYAN_BED.price", 12);
        Feudal.getMarketConfig().getConfig().set("prices.buy.GRAY_BED.price", 24);
        Feudal.getMarketConfig().getConfig().set("prices.sell.GRAY_BED.price", 12);
        Feudal.getMarketConfig().getConfig().set("prices.buy.GREEN_BED.price", 24);
        Feudal.getMarketConfig().getConfig().set("prices.sell.GREEN_BED.price", 12);
        Feudal.getMarketConfig().getConfig().set("prices.buy.LIGHT_BLUE_BED.price", 24);
        Feudal.getMarketConfig().getConfig().set("prices.sell.LIGHT_BLUE_BED.price", 12);
        Feudal.getMarketConfig().getConfig().set("prices.buy.LIGHT_GRAY_BED.price", 24);
        Feudal.getMarketConfig().getConfig().set("prices.sell.LIGHT_GRAY_BED.price", 12);
        Feudal.getMarketConfig().getConfig().set("prices.buy.LIME_BED.price", 24);
        Feudal.getMarketConfig().getConfig().set("prices.sell.LIME_BED.price", 12);
        Feudal.getMarketConfig().getConfig().set("prices.buy.MAGENTA_BED.price", 24);
        Feudal.getMarketConfig().getConfig().set("prices.sell.MAGENTA_BED.price", 12);
        Feudal.getMarketConfig().getConfig().set("prices.buy.ORANGE_BED.price", 24);
        Feudal.getMarketConfig().getConfig().set("prices.sell.ORANGE_BED.price", 12);
        Feudal.getMarketConfig().getConfig().set("prices.buy.PINK_BED.price", 24);
        Feudal.getMarketConfig().getConfig().set("prices.sell.PINK_BED.price", 12);
        Feudal.getMarketConfig().getConfig().set("prices.buy.PURPLE_BED.price", 24);
        Feudal.getMarketConfig().getConfig().set("prices.sell.PURPLE_BED.price", 12);
        Feudal.getMarketConfig().getConfig().set("prices.buy.BLACK_WOOL.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BLACK_WOOL.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.BLUE_WOOL.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BLUE_WOOL.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.BROWN_WOOL.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BROWN_WOOL.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.CYAN_WOOL.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.CYAN_WOOL.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.GRAY_WOOL.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.GRAY_WOOL.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.GREEN_WOOL.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.GREEN_WOOL.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.LIGHT_BLUE_WOOL.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.LIGHT_BLUE_WOOL.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.LIGHT_GRAY_WOOL.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.LIGHT_GRAY_WOOL.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.LIME_WOOL.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.LIME_WOOL.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.MAGENTA_WOOL.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.MAGENTA_WOOL.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.ORANGE_WOOL.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.ORANGE_WOOL.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.PINK_WOOL.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.PINK_WOOL.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.PURPLE_WOOL.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.PURPLE_WOOL.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.RED_WOOL.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.RED_WOOL.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.YELLOW_WOOL.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.YELLOW_WOOL.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.ACACIA_SIGN.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.ACACIA_SIGN.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.BIRCH_SIGN.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BIRCH_SIGN.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.DARK_OAK_SIGN.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.DARK_OAK_SIGN.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.JUNGLE_SIGN.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.JUNGLE_SIGN.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPRUCE_SIGN.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPRUCE_SIGN.price", 2);

        Feudal.getMarketConfig().getConfig().set("prices.buy.ACACIA_DOOR.price", 8);
        Feudal.getMarketConfig().getConfig().set("prices.sell.ACACIA_DOOR.price", 4);
        Feudal.getMarketConfig().getConfig().set("prices.buy.ACACIA_FENCE.price", 7);
        Feudal.getMarketConfig().getConfig().set("prices.sell.ACACIA_FENCE.price", 3.3);
        Feudal.getMarketConfig().getConfig().set("prices.buy.ACACIA_FENCE_GATE.price", 15);
        Feudal.getMarketConfig().getConfig().set("prices.sell.ACACIA_FENCE_GATE.price", 8);
        Feudal.getMarketConfig().getConfig().set("prices.buy.ACTIVATOR_RAIL.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.sell.ACTIVATOR_RAIL.price", 50);
        Feudal.getMarketConfig().getConfig().set("prices.buy.ANVIL.price", 2200);
        Feudal.getMarketConfig().getConfig().set("prices.buy.ANVIL.1", 1400);
        Feudal.getMarketConfig().getConfig().set("prices.buy.ANVIL.2", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.ANVIL.price", 1520.0);
        Feudal.getMarketConfig().getConfig().set("prices.sell.ANVIL.1", 760);
        Feudal.getMarketConfig().getConfig().set("prices.sell.ANVIL.2", 380);
        Feudal.getMarketConfig().getConfig().set("prices.buy.APPLE.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.APPLE.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.ARMOR_STAND.price", 14);
        Feudal.getMarketConfig().getConfig().set("prices.sell.ARMOR_STAND.price", 7);
        Feudal.getMarketConfig().getConfig().set("prices.buy.ARROW.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.ARROW.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.BAKED_POTATO.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BAKED_POTATO.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.BANNER.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BANNER.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.buy.BEACON.price", 100000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BEACON.price", 20000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.WHITE_BED.price", 24);
        Feudal.getMarketConfig().getConfig().set("prices.sell.WHITE_BED.price", 12);
        Feudal.getMarketConfig().getConfig().set("prices.buy.BIRCH_DOOR.price", 8);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BIRCH_DOOR.price", 4);
        Feudal.getMarketConfig().getConfig().set("prices.buy.BIRCH_FENCE.price", 7);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BIRCH_FENCE.price", 3.3);
        Feudal.getMarketConfig().getConfig().set("prices.buy.BIRCH_FENCE_GATE.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BIRCH_FENCE_GATE.price", 5);
        Feudal.getMarketConfig().getConfig().set("prices.buy.BIRCH_WOOD_STAIRS.price", 6);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BIRCH_WOOD_STAIRS.price", 3);
        Feudal.getMarketConfig().getConfig().set("prices.buy.BLAZE_POWDER.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BLAZE_POWDER.price", 50);
        Feudal.getMarketConfig().getConfig().set("prices.buy.BLAZE_ROD.price", 200);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BLAZE_ROD.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.buy.BOAT.price", 20);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BOAT.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.buy.BONE.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BONE.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.BOOK.price", 44);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BOOK.price", 22);
        Feudal.getMarketConfig().getConfig().set("prices.buy.BOOK_AND_QUILL.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BOOK_AND_QUILL.price", 50);
        Feudal.getMarketConfig().getConfig().set("prices.buy.BOOKSHELF.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BOOKSHELF.price", 50);
        Feudal.getMarketConfig().getConfig().set("prices.buy.BOW.price", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BOW.price", 50);
        Feudal.getMarketConfig().getConfig().set("prices.buy.BOWL.price", 5);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BOWL.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.buy.BREAD.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BREAD.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.BREWING_STAND_ITEM.price", 200);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BREWING_STAND_ITEM.price", 50);
        Feudal.getMarketConfig().getConfig().set("prices.buy.BRICK.price", 40);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BRICK.price", 20);
        Feudal.getMarketConfig().getConfig().set("prices.buy.BRICK_STAIRS.price", 20);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BRICK_STAIRS.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.buy.BROWN_MUSHROOM.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BROWN_MUSHROOM.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.BUCKET.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BUCKET.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.CACTUS.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.CACTUS.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.CAKE.price", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.CAKE.price", 200);
        Feudal.getMarketConfig().getConfig().set("prices.buy.CARPET.price", 3);
        Feudal.getMarketConfig().getConfig().set("prices.sell.CARPET.price", 1.3);
        Feudal.getMarketConfig().getConfig().set("prices.buy.CARROT_ITEM.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.CARROT_ITEM.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.CARROT_STICK.price", 8);
        Feudal.getMarketConfig().getConfig().set("prices.sell.CARROT_STICK.price", 4);
        Feudal.getMarketConfig().getConfig().set("prices.buy.CAULDRON_ITEM.price", 750);
        Feudal.getMarketConfig().getConfig().set("prices.sell.CAULDRON_ITEM.price", 20);
        Feudal.getMarketConfig().getConfig().set("prices.buy.CHAINMAIL_BOOTS.price", 300);
        Feudal.getMarketConfig().getConfig().set("prices.sell.CHAINMAIL_BOOTS.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.buy.CHAINMAIL_CHESTPLATE.price", 500 );
        Feudal.getMarketConfig().getConfig().set("prices.sell.CHAINMAIL_CHESTPLATE.price", 200);
        Feudal.getMarketConfig().getConfig().set("prices.buy.CHAINMAIL_HELMET.price", 350 );
        Feudal.getMarketConfig().getConfig().set("prices.sell.CHAINMAIL_HELMET.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.buy.CHAINMAIL_LEGGINGS.price", 400);
        Feudal.getMarketConfig().getConfig().set("prices.sell.CHAINMAIL_LEGGINGS.price", 200);
        Feudal.getMarketConfig().getConfig().set("prices.buy.CHEST.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.CHEST.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.CLAY.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.CLAY.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.CLAY_BALL.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.CLAY_BALL.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.CLAY_BRICK.price", 15);
        Feudal.getMarketConfig().getConfig().set("prices.sell.CLAY_BRICK.price", 5);
        Feudal.getMarketConfig().getConfig().set("prices.buy.COAL.price", 20);
        Feudal.getMarketConfig().getConfig().set("prices.sell.COAL.price",5);
        Feudal.getMarketConfig().getConfig().set("prices.buy.COAL_BLOCK.price", 90);
        Feudal.getMarketConfig().getConfig().set("prices.sell.COAL_BLOCK.price", 45);
        Feudal.getMarketConfig().getConfig().set("prices.buy.COAL_ORE.price", 160);
        Feudal.getMarketConfig().getConfig().set("prices.sell.COAL_ORE.price", 80);
        Feudal.getMarketConfig().getConfig().set("prices.buy.COBBLE_WALL.price", 4);
        Feudal.getMarketConfig().getConfig().set("prices.sell.COBBLE_WALL.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.COBBLESTONE.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.COBBLESTONE.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.COBBLESTONE_STAIRS.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.COBBLESTONE_STAIRS.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.COCOA.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.sell.COCOA.price", 20);
        Feudal.getMarketConfig().getConfig().set("prices.buy.COMPASS.price", 700);
        Feudal.getMarketConfig().getConfig().set("prices.sell.COMPASS.price", 200);
        Feudal.getMarketConfig().getConfig().set("prices.buy.COOKED_BEEF.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.COOKED_BEEF.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.COOKED_CHICKEN.price", 5);
        Feudal.getMarketConfig().getConfig().set("prices.sell.COOKED_CHICKEN.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.buy.COOKED_FISH.price", 5);
        Feudal.getMarketConfig().getConfig().set("prices.sell.COOKED_FISH.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.buy.COOKED_MUTTON.price", 5);
        Feudal.getMarketConfig().getConfig().set("prices.sell.COOKED_MUTTON.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.buy.COOKED_RABBIT.price", 5);
        Feudal.getMarketConfig().getConfig().set("prices.sell.COOKED_RABBIT.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.buy.COOKIE.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.CROPS.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.CROPS.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.DARK_OAK_DOOR.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.DARK_OAK_DOOR.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.DARK_OAK_FENCE.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.DARK_OAK_FENCE.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.DARK_OAK_FENCE_GATE.price", 5);
        Feudal.getMarketConfig().getConfig().set("prices.buy.DARK_OAK_FENCE_GATE.price",  15);
        Feudal.getMarketConfig().getConfig().set("prices.sell.DARK_OAK_STAIRS.price", 3);
        Feudal.getMarketConfig().getConfig().set("prices.buy.DARK_OAK_STAIRS.price", 6);
        Feudal.getMarketConfig().getConfig().set("prices.sell.DAYLIGHT_DETECTOR.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.buy.DAYLIGHT_DETECTOR.price", 200);
        Feudal.getMarketConfig().getConfig().set("prices.sell.DEAD_BUSH.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.buy.DEAD_BUSH.price", 5);
        Feudal.getMarketConfig().getConfig().set("prices.sell.DETECTOR_RAIL.price", 50);
        Feudal.getMarketConfig().getConfig().set("prices.buy.DETECTOR_RAIL.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.sell.DIAMOND.price", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.DIAMOND.price", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.DIAMOND_AXE.price", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.DIAMOND_AXE.price", 3500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.DIAMOND_BARDING.price", 1530);
        Feudal.getMarketConfig().getConfig().set("prices.buy.DIAMOND_BARDING.price", 3000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.DIAMOND_BLOCK.price", 4500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.DIAMOND_BLOCK.price", 9000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.DIAMOND_BOOTS.price", 1500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.DIAMOND_BOOTS.price", 3500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.DIAMOND_CHESTPLATE.price", 4000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.DIAMOND_CHESTPLATE.price", 8000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.DIAMOND_HELMET.price", 1500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.DIAMOND_HELMET.price", 3500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.DIAMOND_HOE.price", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.DIAMOND_HOE.price", 3500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.DIAMOND_LEGGINGS.price", 3000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.DIAMOND_LEGGINGS.price", 7500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.DIAMOND_ORE.price", 400);
        Feudal.getMarketConfig().getConfig().set("prices.buy.DIAMOND_ORE.price", 1100);
        Feudal.getMarketConfig().getConfig().set("prices.sell.DIAMOND_PICKAXE.price", 1500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.DIAMOND_PICKAXE.price", 4000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.DIAMOND_SHOVEL.price", 502);
        Feudal.getMarketConfig().getConfig().set("prices.buy.DIAMOND_SHOVEL.price", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.DIAMOND_SWORD.price", 2000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.DIAMOND_SWORD.price", 1001);
        Feudal.getMarketConfig().getConfig().set("prices.buy.DIODE.price", 70);
        Feudal.getMarketConfig().getConfig().set("prices.sell.DIODE.price", 35);
        Feudal.getMarketConfig().getConfig().set("prices.buy.DIRT.price", 5);
        Feudal.getMarketConfig().getConfig().set("prices.sell.DIRT.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.buy.DISPENSER.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.DISPENSER.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.DOUBLE_PLANT.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.DOUBLE_PLANT.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.DRAGON_EGG.price", 100000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.DRAGON_EGG.price", 50000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.DROPPER.price", 80);
        Feudal.getMarketConfig().getConfig().set("prices.sell.DROPPER.price", 39);
        Feudal.getMarketConfig().getConfig().set("prices.buy.EGG.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.EGG.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.EMERALD.price", 1776);
        Feudal.getMarketConfig().getConfig().set("prices.sell.EMERALD.price", 888);
        Feudal.getMarketConfig().getConfig().set("prices.buy.EMERALD_BLOCK.price", 15984);
        Feudal.getMarketConfig().getConfig().set("prices.sell.EMERALD_BLOCK.price", 7992);
        Feudal.getMarketConfig().getConfig().set("prices.buy.EMERALD_ORE.price", 2100);
        Feudal.getMarketConfig().getConfig().set("prices.sell.EMERALD_ORE.price", 1100);
        Feudal.getMarketConfig().getConfig().set("prices.buy.EMPTY_MAP.price", 5);
        Feudal.getMarketConfig().getConfig().set("prices.sell.EMPTY_MAP.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.buy.ENCHANTED_BOOK.price", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.ENCHANTED_BOOK.price", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.ENCHANTMENT_TABLE.price", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.ENCHANTMENT_TABLE.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.buy.ENDER_CHEST.price", 2000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.ENDER_CHEST.price", 1600);
        Feudal.getMarketConfig().getConfig().set("prices.buy.ENDER_PEARL.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.sell.ENDER_PEARL.price", 50);
        Feudal.getMarketConfig().getConfig().set("prices.buy.ENDER_STONE.price", 12);
        Feudal.getMarketConfig().getConfig().set("prices.sell.ENDER_STONE.price", 6);
        Feudal.getMarketConfig().getConfig().set("prices.buy.EXP_BOTTLE.price", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.EXP_BOTTLE.price", 200);
        Feudal.getMarketConfig().getConfig().set("prices.buy.EXPLOSIVE_MINECART.price", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.EXPLOSIVE_MINECART.price", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.EYE_OF_ENDER.price", 120);
        Feudal.getMarketConfig().getConfig().set("prices.sell.EYE_OF_ENDER.price", 60);
        Feudal.getMarketConfig().getConfig().set("prices.buy.FEATHER.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.sell.FEATHER.price", 50);
        Feudal.getMarketConfig().getConfig().set("prices.buy.FENCE.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.FENCE.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.FENCE_GATE.price", 15);
        Feudal.getMarketConfig().getConfig().set("prices.sell.FENCE_GATE.price", 5);
        Feudal.getMarketConfig().getConfig().set("prices.buy.FERMENTED_SPIDER_EYE.price", 15);
        Feudal.getMarketConfig().getConfig().set("prices.sell.FERMENTED_SPIDER_EYE.price", 5);
        Feudal.getMarketConfig().getConfig().set("prices.buy.FIREBALL.price", 200);
        Feudal.getMarketConfig().getConfig().set("prices.sell.FIREBALL.price", 85);
        Feudal.getMarketConfig().getConfig().set("prices.buy.FIREWORK.price", 250);
        Feudal.getMarketConfig().getConfig().set("prices.sell.FIREWORK.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.buy.FIREWORK_CHARGE.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.sell.FIREWORK_CHARGE.price", 50);
        Feudal.getMarketConfig().getConfig().set("prices.buy.FISHING_ROD.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.FISHING_ROD.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.FLINT.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.FLINT.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.FLINT_AND_STEEL.price", 20);
        Feudal.getMarketConfig().getConfig().set("prices.sell.FLINT_AND_STEEL.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.buy.FLOWER_POT.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.FLOWER_POT.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.FLOWER_POT_ITEM.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.FLOWER_POT_ITEM.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.FURNACE.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.FURNACE.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.GHAST_TEAR.price", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.GHAST_TEAR.price", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.GLASS.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.GLASS.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.GLASS_BOTTLE.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.GLASS_BOTTLE.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.GLOWSTONE.price", 80);
        Feudal.getMarketConfig().getConfig().set("prices.sell.GLOWSTONE.price", 40);
        Feudal.getMarketConfig().getConfig().set("prices.buy.GLOWSTONE_DUST.price", 50);
        Feudal.getMarketConfig().getConfig().set("prices.sell.GLOWSTONE_DUST.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.buy.GOLDEN_AXE.price", 1800);
        Feudal.getMarketConfig().getConfig().set("prices.sell.GOLDEN_AXE.price", 750);
        Feudal.getMarketConfig().getConfig().set("prices.buy.GOLDEN_BARDING.price", 1860);
        Feudal.getMarketConfig().getConfig().set("prices.sell.GOLDEN_BARDING.price", 930);
        Feudal.getMarketConfig().getConfig().set("prices.buy.GOLD_BLOCK.price", 5000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.GOLD_BLOCK.price", 2700);
        Feudal.getMarketConfig().getConfig().set("prices.buy.GOLDEN_BOOTS.price", 1200);
        Feudal.getMarketConfig().getConfig().set("prices.sell.GOLDEN_BOOTS.price", 600);
        Feudal.getMarketConfig().getConfig().set("prices.buy.GOLDEN_CHESTPLATE.price", 4800);
        Feudal.getMarketConfig().getConfig().set("prices.sell.GOLDEN_CHESTPLATE.price", 2000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.GOLDEN_HELMET.price", 3500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.GOLDEN_HELMET.price", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.GOLDEN_HOE.price", 1200);
        Feudal.getMarketConfig().getConfig().set("prices.sell.GOLDEN_HOE.price", 400);
        Feudal.getMarketConfig().getConfig().set("prices.buy.GOLD_INGOT.price", 600);
        Feudal.getMarketConfig().getConfig().set("prices.sell.GOLD_INGOT.price", 300);
        Feudal.getMarketConfig().getConfig().set("prices.buy.GOLDEN_LEGGINGS.price", 4200);
        Feudal.getMarketConfig().getConfig().set("prices.sell.GOLDEN_LEGGINGS.price", 2000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.GOLD_NUGGET.price", 60);
        Feudal.getMarketConfig().getConfig().set("prices.sell.GOLD_NUGGET.price", 33.33);
        Feudal.getMarketConfig().getConfig().set("prices.buy.GOLD_ORE.price", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.GOLD_ORE.price", 200);
        Feudal.getMarketConfig().getConfig().set("prices.buy.GOLDEN_PICKAXE.price", 1800);
        Feudal.getMarketConfig().getConfig().set("prices.sell.GOLDEN_PICKAXE.price", 600);
        Feudal.getMarketConfig().getConfig().set("prices.buy.GOLD_PLATE.price", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.GOLD_PLATE.price", 350);
        Feudal.getMarketConfig().getConfig().set("prices.buy.GOLD_RECORD.price", 3000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.GOLD_RECORD.price", 800);
        Feudal.getMarketConfig().getConfig().set("prices.buy.GOLDEN_SHOVEL.price", 604);
        Feudal.getMarketConfig().getConfig().set("prices.sell.GOLDEN_SHOVEL.price", 302);
        Feudal.getMarketConfig().getConfig().set("prices.buy.GOLDEN_SWORD.price", 1200);
        Feudal.getMarketConfig().getConfig().set("prices.sell.GOLDEN_SWORD.price", 400);
        Feudal.getMarketConfig().getConfig().set("prices.buy.GOLDEN_APPLE.price", 5004);
        Feudal.getMarketConfig().getConfig().set("prices.sell.GOLDEN_APPLE.price", 2402);
        Feudal.getMarketConfig().getConfig().set("prices.buy.GOLDEN_APPLE.1", 43204);
        Feudal.getMarketConfig().getConfig().set("prices.sell.GOLDEN_APPLE.1", 21602);
        Feudal.getMarketConfig().getConfig().set("prices.buy.GOLDEN_CARROT.price", 350);
        Feudal.getMarketConfig().getConfig().set("prices.sell.GOLDEN_CARROT.price", 242);
        Feudal.getMarketConfig().getConfig().set("prices.buy.GRASS.price", 5);
        Feudal.getMarketConfig().getConfig().set("prices.sell.GRASS.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.buy.GRASS_BLOCK.price", 5);
        Feudal.getMarketConfig().getConfig().set("prices.sell.GRASS_BLOCK.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.buy.GRAVEL.price", 5);
        Feudal.getMarketConfig().getConfig().set("prices.sell.GRAVEL.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.buy.GREEN_RECORD.price", 5000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.GREEN_RECORD.price", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.COOKED_PORKCHOP.price", 5);
        Feudal.getMarketConfig().getConfig().set("prices.sell.COOKED_PORKCHOP.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.buy.HARD_CLAY.price", 5);
        Feudal.getMarketConfig().getConfig().set("prices.sell.HARD_CLAY.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.buy.HAY_BLOCK.price", 36);
        Feudal.getMarketConfig().getConfig().set("prices.sell.HAY_BLOCK.price", 18);
        Feudal.getMarketConfig().getConfig().set("prices.buy.HOPPER.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.sell.HOPPER.price", 50);
        Feudal.getMarketConfig().getConfig().set("prices.buy.HOPPER_MINECART.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.sell.HOPPER_MINECART.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.buy.HUGE_MUSHROOM_1.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.sell.HUGE_MUSHROOM_1.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.buy.HUGE_MUSHROOM_2.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.sell.HUGE_MUSHROOM_2.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.buy.ICE.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.ICE.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.INK_SACK.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.INK_SACK.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.IRON_AXE.price", 600);
        Feudal.getMarketConfig().getConfig().set("prices.sell.IRON_AXE.price", 200);
        Feudal.getMarketConfig().getConfig().set("prices.buy.IRON_BARDING.price", 550);
        Feudal.getMarketConfig().getConfig().set("prices.sell.IRON_BARDING.price", 270);
        Feudal.getMarketConfig().getConfig().set("prices.buy.IRON_BLOCK.price", 1440);
        Feudal.getMarketConfig().getConfig().set("prices.sell.IRON_BLOCK.price", 720);
        Feudal.getMarketConfig().getConfig().set("prices.buy.IRON_BOOTS.price", 400);
        Feudal.getMarketConfig().getConfig().set("prices.sell.IRON_BOOTS.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.buy.IRON_CHESTPLATE.price", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.IRON_CHESTPLATE.price", 640);
        Feudal.getMarketConfig().getConfig().set("prices.buy.IRON_DOOR.price", 300);
        Feudal.getMarketConfig().getConfig().set("prices.sell.IRON_DOOR.price", 160);
        Feudal.getMarketConfig().getConfig().set("prices.buy.IRON_DOOR_BLOCK.price", 800);
        Feudal.getMarketConfig().getConfig().set("prices.sell.IRON_DOOR_BLOCK.price", 200);
        Feudal.getMarketConfig().getConfig().set("prices.buy.IRON_FENCE.price", 60);
        Feudal.getMarketConfig().getConfig().set("prices.sell.IRON_FENCE.price", 30);
        Feudal.getMarketConfig().getConfig().set("prices.buy.IRON_HELMET.price", 800);
        Feudal.getMarketConfig().getConfig().set("prices.sell.IRON_HELMET.price", 200);
        Feudal.getMarketConfig().getConfig().set("prices.buy.IRON_HOE.price", 400);
        Feudal.getMarketConfig().getConfig().set("prices.sell.IRON_HOE.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.buy.IRON_INGOT.price", 200);
        Feudal.getMarketConfig().getConfig().set("prices.sell.IRON_INGOT.price", 80);
        Feudal.getMarketConfig().getConfig().set("prices.buy.IRON_LEGGINGS.price", 1400);
        Feudal.getMarketConfig().getConfig().set("prices.sell.IRON_LEGGINGS.price", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.IRON_ORE.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.sell.IRON_ORE.price", 50);
        Feudal.getMarketConfig().getConfig().set("prices.buy.IRON_PICKAXE.price", 600);
        Feudal.getMarketConfig().getConfig().set("prices.sell.IRON_PICKAXE.price", 200);
        Feudal.getMarketConfig().getConfig().set("prices.buy.IRON_PLATE.price", 200);
        Feudal.getMarketConfig().getConfig().set("prices.sell.IRON_PLATE.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.buy.IRON_SHOVEL.price", 160);
        Feudal.getMarketConfig().getConfig().set("prices.sell.IRON_SHOVEL.price", 82);
        Feudal.getMarketConfig().getConfig().set("prices.buy.IRON_SWORD.price", 300);
        Feudal.getMarketConfig().getConfig().set("prices.sell.IRON_SWORD.price", 161);
        Feudal.getMarketConfig().getConfig().set("prices.buy.IRON_TRAPDOOR.price", 700);
        Feudal.getMarketConfig().getConfig().set("prices.sell.IRON_TRAPDOOR.price", 320);
        Feudal.getMarketConfig().getConfig().set("prices.buy.ITEM_FRAME.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.sell.ITEM_FRAME.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.buy.JACK_O_LANTERN.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.JACK_O_LANTERN.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.JUKEBOX.price", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.JUKEBOX.price", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.JUNGLE_DOOR.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.JUNGLE_DOOR.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.JUNGLE_FENCE.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.JUNGLE_FENCE.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.JUNGLE_FENCE_GATE.price", 15);
        Feudal.getMarketConfig().getConfig().set("prices.sell.JUNGLE_FENCE_GATE.price", 5);
        Feudal.getMarketConfig().getConfig().set("prices.buy.JUNGLE_WOOD_STAIRS.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.JUNGLE_WOOD_STAIRS.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.LADDER.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.LADDER.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.LAPIS_BLOCK.price", 20);
        Feudal.getMarketConfig().getConfig().set("prices.sell.LAPIS_BLOCK.price", 18);
        Feudal.getMarketConfig().getConfig().set("prices.buy.LAPIS_ORE.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.sell.LAPIS_ORE.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.buy.LAVA_BUCKET.price", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.LAVA_BUCKET.price", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.LEASH.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.sell.LEASH.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.buy.LEATHER.price", 20);
        Feudal.getMarketConfig().getConfig().set("prices.sell.LEATHER.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.buy.LEATHER_BOOTS.price", 200);
        Feudal.getMarketConfig().getConfig().set("prices.sell.LEATHER_BOOTS.price", 20);
        Feudal.getMarketConfig().getConfig().set("prices.buy.LEATHER_CHESTPLATE.price", 800);
        Feudal.getMarketConfig().getConfig().set("prices.sell.LEATHER_CHESTPLATE.price", 40);
        Feudal.getMarketConfig().getConfig().set("prices.buy.LEATHER_HELMET.price", 600);
        Feudal.getMarketConfig().getConfig().set("prices.sell.LEATHER_HELMET.price", 20);
        Feudal.getMarketConfig().getConfig().set("prices.buy.LEATHER_LEGGINGS.price", 700);
        Feudal.getMarketConfig().getConfig().set("prices.sell.LEATHER_LEGGINGS.price", 30);
        Feudal.getMarketConfig().getConfig().set("prices.buy.LEAVES.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.LEAVES.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.LEAVES_2.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.LEAVES_2.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.LEVER.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.LEVER.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.LOG.price", 16);
        Feudal.getMarketConfig().getConfig().set("prices.sell.LOG.price", 8);
        Feudal.getMarketConfig().getConfig().set("prices.buy.LOG_2.price", 16);
        Feudal.getMarketConfig().getConfig().set("prices.sell.LOG_2.price", 8);
        Feudal.getMarketConfig().getConfig().set("prices.buy.LONG_GRASS.price", 5);
        Feudal.getMarketConfig().getConfig().set("prices.sell.LONG_GRASS.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.buy.MAGMA_CREAM.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.sell.MAGMA_CREAM.price", 50);
        Feudal.getMarketConfig().getConfig().set("prices.buy.MAP.price", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.MAP.price", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.MELON.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.MELON.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.MELON_BLOCK.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.MELON_BLOCK.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.MELON_SEEDS.price", 5);
        Feudal.getMarketConfig().getConfig().set("prices.sell.MELON_SEEDS.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.buy.MILK_BUCKET.price", 160);
        Feudal.getMarketConfig().getConfig().set("prices.sell.MILK_BUCKET.price", 80);
        Feudal.getMarketConfig().getConfig().set("prices.buy.MINECART.price", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.MINECART.price", 250);
        Feudal.getMarketConfig().getConfig().set("prices.buy.MOB_SPAWNER.price", 100000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.MOB_SPAWNER.price", 50000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.MONSTER_EGG.price", 5000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.MONSTER_EGG.price", 2500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.MONSTER_EGGS.price", 5000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.MONSTER_EGGS.price", 2500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.MOSSY_COBBLESTONE.price", 8);
        Feudal.getMarketConfig().getConfig().set("prices.sell.MOSSY_COBBLESTONE.price", 4);
        Feudal.getMarketConfig().getConfig().set("prices.buy.MUSHROOM_SOUP.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.MUSHROOM_SOUP.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.MUTTON.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.MUTTON.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.MYCEL.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.MYCEL.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.NAME_TAG.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.NAME_TAG.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.NETHER_BRICK_ITEM.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.NETHER_BRICK_ITEM.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.NETHER_BRICK.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.NETHER_BRICK.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.NETHER_BRICK_STAIRS.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.NETHER_BRICK_STAIRS.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.NETHER_FENCE.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.NETHER_FENCE.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.NETHER_STALK.price", 50);
        Feudal.getMarketConfig().getConfig().set("prices.sell.NETHER_STALK.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.buy.NETHER_STAR.price", 100000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.NETHER_STAR.price", 50000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.NETHER_WARTS.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.NETHER_WARTS.price", 5);
        Feudal.getMarketConfig().getConfig().set("prices.buy.NETHER_WART_BLOCK.price", 90);
        Feudal.getMarketConfig().getConfig().set("prices.sell.NETHER_WART_BLOCK.price", 45);
        Feudal.getMarketConfig().getConfig().set("prices.buy.MAGMA.price", 20);
        Feudal.getMarketConfig().getConfig().set("prices.sell.MAGMA.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.buy.RED_NETHER_BRICK.price", 40);
        Feudal.getMarketConfig().getConfig().set("prices.sell.RED_NETHER_BRICK.price", 14);
        Feudal.getMarketConfig().getConfig().set("prices.buy.BONE_BLOCK.price", 90);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BONE_BLOCK.price", 18);
        Feudal.getMarketConfig().getConfig().set("prices.buy.NETHERRACK.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.NETHERRACK.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.NOTE_BLOCK.price", 50);
        Feudal.getMarketConfig().getConfig().set("prices.sell.NOTE_BLOCK.price", 25);
        Feudal.getMarketConfig().getConfig().set("prices.buy.OBSIDIAN.price", 250);
        Feudal.getMarketConfig().getConfig().set("prices.sell.OBSIDIAN.price", 200);
        Feudal.getMarketConfig().getConfig().set("prices.buy.PACKED_ICE.price", 50);
        Feudal.getMarketConfig().getConfig().set("prices.sell.PACKED_ICE.price", 25);
        Feudal.getMarketConfig().getConfig().set("prices.buy.PAINTING.price", 15);
        Feudal.getMarketConfig().getConfig().set("prices.sell.PAINTING.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.buy.PAPER.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.sell.PAPER.price", 4);
        Feudal.getMarketConfig().getConfig().set("prices.buy.PISTON_BASE.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.sell.PISTON_BASE.price", 50);
        Feudal.getMarketConfig().getConfig().set("prices.buy.PISTON_EXTENSION.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.sell.PISTON_EXTENSION.price", 50);
        Feudal.getMarketConfig().getConfig().set("prices.buy.PISTON_MOVING_PIECE.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.sell.PISTON_MOVING_PIECE.price", 50);
        Feudal.getMarketConfig().getConfig().set("prices.buy.PISTON_STICKY_BASE.price", 200);
        Feudal.getMarketConfig().getConfig().set("prices.sell.PISTON_STICKY_BASE.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POISONOUS_POTATO.price", 5);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POISONOUS_POTATO.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.buy.PORKCHOP.price", 5);
        Feudal.getMarketConfig().getConfig().set("prices.sell.PORKCHOP.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.buy.PORTAL.price", 3000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.PORTAL.price", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTATO_ITEM.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTATO_ITEM.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POWERED_MINECART.price", 400);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POWERED_MINECART.price", 252);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POWERED_RAIL.price", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POWERED_RAIL.price", 250);
        Feudal.getMarketConfig().getConfig().set("prices.buy.PRISMARINE.price", 30);
        Feudal.getMarketConfig().getConfig().set("prices.sell.PRISMARINE.price", 16);
        Feudal.getMarketConfig().getConfig().set("prices.buy.PRISMARINE_CRYSTALS.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.PRISMARINE_CRYSTALS.price", 5);
        Feudal.getMarketConfig().getConfig().set("prices.buy.PRISMARINE_SHARD.price", 8);
        Feudal.getMarketConfig().getConfig().set("prices.sell.PRISMARINE_SHARD.price", 4);
        Feudal.getMarketConfig().getConfig().set("prices.buy.PUMPKIN.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.PUMPKIN.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.PUMPKIN_PIE.price", 16);
        Feudal.getMarketConfig().getConfig().set("prices.sell.PUMPKIN_PIE.price", 8);
        Feudal.getMarketConfig().getConfig().set("prices.buy.PUMPKIN_SEEDS.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.sell.PUMPKIN_SEEDS.price", 0.5);
        Feudal.getMarketConfig().getConfig().set("prices.buy.QUARTZ.price", 40);
        Feudal.getMarketConfig().getConfig().set("prices.sell.QUARTZ.price", 20);
        Feudal.getMarketConfig().getConfig().set("prices.buy.QUARTZ_BLOCK.price", 160);
        Feudal.getMarketConfig().getConfig().set("prices.sell.QUARTZ_BLOCK.price", 80);
        Feudal.getMarketConfig().getConfig().set("prices.buy.QUARTZ_ORE.price", 250);
        Feudal.getMarketConfig().getConfig().set("prices.sell.QUARTZ_ORE.price", 125);
        Feudal.getMarketConfig().getConfig().set("prices.buy.QUARTZ_STAIRS.price", 250);
        Feudal.getMarketConfig().getConfig().set("prices.sell.QUARTZ_STAIRS.price", 120);
        Feudal.getMarketConfig().getConfig().set("prices.buy.RABBIT.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.RABBIT.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.RABBIT_FOOT.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.sell.RABBIT_FOOT.price", 50);
        Feudal.getMarketConfig().getConfig().set("prices.buy.RABBIT_HIDE.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.RABBIT_HIDE.price", 2.5);
        Feudal.getMarketConfig().getConfig().set("prices.buy.RABBIT_STEW.price", 16);
        Feudal.getMarketConfig().getConfig().set("prices.sell.RABBIT_STEW.price", 8);
        Feudal.getMarketConfig().getConfig().set("prices.buy.RAILS.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.sell.RAILS.price", 25);
        Feudal.getMarketConfig().getConfig().set("prices.buy.BEEF.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BEEF.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.CHICKEN.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.CHICKEN.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.RAW_FISH.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.RAW_FISH.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.RECORD_10.price", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.RECORD_10.price", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.RECORD_11.price", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.RECORD_11.price", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.RECORD_12.price", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.RECORD_12.price", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.RECORD_3.price", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.RECORD_3.price", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.RECORD_4.price", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.RECORD_4.price", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.RECORD_5.price", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.RECORD_5.price", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.RECORD_6.price", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.RECORD_6.price", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.RECORD_7.price", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.RECORD_7.price", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.RECORD_8.price", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.RECORD_8.price", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.RECORD_9.price", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.RECORD_9.price", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.RED_MUSHROOM.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.RED_MUSHROOM.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.RED_ROSE.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.RED_ROSE.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.RED_SANDSTONE.price", 16);
        Feudal.getMarketConfig().getConfig().set("prices.sell.RED_SANDSTONE.price", 8);
        Feudal.getMarketConfig().getConfig().set("prices.buy.RED_SANDSTONE_STAIRS.price", 24);
        Feudal.getMarketConfig().getConfig().set("prices.sell.RED_SANDSTONE_STAIRS.price", 12);
        Feudal.getMarketConfig().getConfig().set("prices.buy.REDSTONE.price", 50);
        Feudal.getMarketConfig().getConfig().set("prices.sell.REDSTONE.price", 25);
        Feudal.getMarketConfig().getConfig().set("prices.buy.REDSTONE_BLOCK.price", 450);
        Feudal.getMarketConfig().getConfig().set("prices.sell.REDSTONE_BLOCK.price", 225);
        Feudal.getMarketConfig().getConfig().set("prices.buy.REDSTONE_COMPARATOR.price", 64);
        Feudal.getMarketConfig().getConfig().set("prices.sell.REDSTONE_COMPARATOR.price", 32);
        Feudal.getMarketConfig().getConfig().set("prices.buy.REDSTONE_COMPARATOR_OFF.price", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.REDSTONE_COMPARATOR_OFF.price", 400);
        Feudal.getMarketConfig().getConfig().set("prices.buy.REDSTONE_COMPARATOR_ON.price", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.REDSTONE_COMPARATOR_ON.price", 400);
        Feudal.getMarketConfig().getConfig().set("prices.buy.REDSTONE_LAMP_OFF.price", 200);
        Feudal.getMarketConfig().getConfig().set("prices.sell.REDSTONE_LAMP_OFF.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.buy.REDSTONE_ORE.price", 50);
        Feudal.getMarketConfig().getConfig().set("prices.sell.REDSTONE_ORE.price", 25);
        Feudal.getMarketConfig().getConfig().set("prices.buy.REDSTONE_TORCH_ON.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.REDSTONE_TORCH_ON.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.REDSTONE_WIRE.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.REDSTONE_WIRE.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.ROTTEN_FLESH.price", 5);
        Feudal.getMarketConfig().getConfig().set("prices.sell.ROTTEN_FLESH.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SADDLE.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SADDLE.price", 25);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SAND.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SAND.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SANDSTONE.price", 16);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SANDSTONE.price", 8);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SANDSTONE_STAIRS.price", 24);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SANDSTONE_STAIRS.price", 12);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SAPLING.price", 5);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SAPLING.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SEA_LANTERN.price", 60);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SEA_LANTERN.price", 41);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SEEDS.price", 5);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SEEDS.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SHEARS.price", 5);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SHEARS.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.buy.OAK_SIGN.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.OAK_SIGN.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SIGN_POST.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SIGN_POST.price", 2);

        Feudal.getMarketConfig().getConfig().set("prices.buy.SKULL_ITEM.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SKULL_ITEM.5", 15000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SKULL_ITEM.price", 5);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SKULL_ITEM.5", 10000);

        Feudal.getMarketConfig().getConfig().set("prices.buy.SLIME_BALL.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SLIME_BALL.price", 50);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SLIME_BLOCK.price", 600);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SLIME_BLOCK.price", 450);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SMOOTH_BRICK.price", 4);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SMOOTH_BRICK.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SMOOTH_STAIRS.price", 6);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SMOOTH_STAIRS.price", 3);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SNOW.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SNOW.price", 0.4);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SNOW_BALL.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SNOW_BALL.price", 0.2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SNOW_BLOCK.price", 3);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SNOW_BLOCK.price", 0.8);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SOIL.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SOIL.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SOUL_SAND.price", 50);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SOUL_SAND.price", 25);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPECKLED_MELON.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPECKLED_MELON.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPIDER_EYE.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPIDER_EYE.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPONGE.price", 10000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPONGE.price", 5000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPRUCE_DOOR.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPRUCE_DOOR.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPRUCE_FENCE.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPRUCE_FENCE.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPRUCE_FENCE_GATE.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPRUCE_FENCE_GATE.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPRUCE_STAIRS.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPRUCE_STAIRS.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.STAINED_CLAY.price", 3);
        Feudal.getMarketConfig().getConfig().set("prices.sell.STAINED_CLAY.price", 1.25);
        Feudal.getMarketConfig().getConfig().set("prices.buy.STAINED_GLASS.price", 5);
        Feudal.getMarketConfig().getConfig().set("prices.sell.STAINED_GLASS.price", 2.25);
        Feudal.getMarketConfig().getConfig().set("prices.buy.STAINED_GLASS_PANE.price", 20);
        Feudal.getMarketConfig().getConfig().set("prices.sell.STAINED_GLASS_PANE.price", 0.83);
        Feudal.getMarketConfig().getConfig().set("prices.buy.STANDING_BANNER.price", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.STANDING_BANNER.price", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.STEP.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.sell.STEP.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.buy.STICK.price", 5);
        Feudal.getMarketConfig().getConfig().set("prices.sell.STICK.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.buy.STONE.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.STONE.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.STONE_AXE.price", 16);
        Feudal.getMarketConfig().getConfig().set("prices.sell.STONE_AXE.price", 8);
        Feudal.getMarketConfig().getConfig().set("prices.buy.STONE_BUTTON.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.STONE_BUTTON.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.STONE_HOE.price", 12);
        Feudal.getMarketConfig().getConfig().set("prices.sell.STONE_HOE.price", 6);
        Feudal.getMarketConfig().getConfig().set("prices.buy.STONE_PICKAXE.price", 16);
        Feudal.getMarketConfig().getConfig().set("prices.sell.STONE_PICKAXE.price", 8);
        Feudal.getMarketConfig().getConfig().set("prices.buy.STONE_PLATE.price", 8);
        Feudal.getMarketConfig().getConfig().set("prices.sell.STONE_PLATE.price", 4);
        Feudal.getMarketConfig().getConfig().set("prices.buy.STONE_SLAB2.price", 8);
        Feudal.getMarketConfig().getConfig().set("prices.sell.STONE_SLAB2.price", 4);
        Feudal.getMarketConfig().getConfig().set("prices.buy.STONE_SHOVEL.price", 8);
        Feudal.getMarketConfig().getConfig().set("prices.sell.STONE_SHOVEL.price", 4);
        Feudal.getMarketConfig().getConfig().set("prices.buy.STONE_SWORD.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.STONE_SWORD.price", 5);
        Feudal.getMarketConfig().getConfig().set("prices.buy.STORAGE_MINECART.price", 150);
        Feudal.getMarketConfig().getConfig().set("prices.sell.STORAGE_MINECART.price", 50);
        Feudal.getMarketConfig().getConfig().set("prices.buy.STRING.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.sell.STRING.price", 50);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SUGAR.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SUGAR.price", 4);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SUGAR_CANE.price", 100);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SUGAR_CANE.price", 4);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SUGAR_CANE_BLOCK.price", 800);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SUGAR_CANE_BLOCK.price", 400);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SULPHUR.price", 400);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SULPHUR.price", 200);
        Feudal.getMarketConfig().getConfig().set("prices.buy.THIN_GLASS.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.sell.THIN_GLASS.price", 0.75);
        Feudal.getMarketConfig().getConfig().set("prices.buy.TNT.price", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.TNT.price", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.TORCH.price", 3);
        Feudal.getMarketConfig().getConfig().set("prices.sell.TORCH.price", 1.5);
        Feudal.getMarketConfig().getConfig().set("prices.buy.TRAP_DOOR.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.TRAP_DOOR.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.TRAPPED_CHEST.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.TRAPPED_CHEST.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.TRIPWIRE.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.TRIPWIRE.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.TRIPWIRE_HOOK.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.TRIPWIRE_HOOK.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.VINE.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.VINE.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.WALL_BANNER.price", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.WALL_BANNER.price", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.WALL_SIGN.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.WALL_SIGN.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.WATCH.price", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.WATCH.price", 250);
        Feudal.getMarketConfig().getConfig().set("prices.buy.WATER_BUCKET.price", 150);
        Feudal.getMarketConfig().getConfig().set("prices.sell.WATER_BUCKET.price", 75);
        Feudal.getMarketConfig().getConfig().set("prices.buy.WATER_LILY.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.WATER_LILY.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.WEB.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.WEB.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.WHEAT.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.WHEAT.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.OAK_PLANKS.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.OAK_PLANKS.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.WOODEN_AXE.price", 30);
        Feudal.getMarketConfig().getConfig().set("prices.sell.WOODEN_AXE.price", 6);
        Feudal.getMarketConfig().getConfig().set("prices.buy.OAK_BUTTON.price", 4);
        Feudal.getMarketConfig().getConfig().set("prices.sell.OAK_BUTTON.price", 2);
        //Feudal.getMarketConfig().getConfig().set("prices.buy.OAK_DOOR.price", 10);
        //Feudal.getMarketConfig().getConfig().set("prices.sell.OAK_DOOR.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.WOODEN_HOE.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.WOODEN_HOE.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.WOODEN_PICKAXE.price", 16);
        Feudal.getMarketConfig().getConfig().set("prices.sell.WOODEN_PICKAXE.price", 8);
        Feudal.getMarketConfig().getConfig().set("prices.buy.OAK_PRESSURE_PLATE.price", 8);
        Feudal.getMarketConfig().getConfig().set("prices.sell.OAK_PRESSURE_PLATE.price", 4);
        Feudal.getMarketConfig().getConfig().set("prices.buy.WOODEN_SHOVEL.price", 8);
        Feudal.getMarketConfig().getConfig().set("prices.sell.WOODEN_SHOVEL.price", 4);
        Feudal.getMarketConfig().getConfig().set("prices.buy.OAK_STAIRS.price", 6);
        Feudal.getMarketConfig().getConfig().set("prices.sell.OAK_STAIRS.price", 3);
        Feudal.getMarketConfig().getConfig().set("prices.buy.OAK_SLAB.price", 5);
        Feudal.getMarketConfig().getConfig().set("prices.sell.OAK_SLAB.price", 1);
        Feudal.getMarketConfig().getConfig().set("prices.buy.WOODEN_SWORD.price", 50);
        Feudal.getMarketConfig().getConfig().set("prices.sell.WOODEN_SWORD.price", 20);
        Feudal.getMarketConfig().getConfig().set("prices.buy.OAK_DOOR.price", 50);
        Feudal.getMarketConfig().getConfig().set("prices.sell.OAK_DOOR.price", 20);
        Feudal.getMarketConfig().getConfig().set("prices.buy.WHITE_WOOL.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.WHITE_WOOL.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.CRAFTING_TABLE.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.CRAFTING_TABLE.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.WRITTEN_BOOK.price", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.WRITTEN_BOOK.price", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.DANDELION.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.sell.DANDELION.price", 2);

        //1.9 items
        Feudal.getMarketConfig().getConfig().set("prices.buy.CHORUS_FLOWER.price", 20);
        Feudal.getMarketConfig().getConfig().set("prices.sell.CHORUS_FLOWER.price", 10);

        Feudal.getMarketConfig().getConfig().set("prices.buy.END_ROD.price", 60);
        Feudal.getMarketConfig().getConfig().set("prices.sell.END_ROD.price", 28);

        Feudal.getMarketConfig().getConfig().set("prices.buy.PURPUR_BLOCK.price", 24);
        Feudal.getMarketConfig().getConfig().set("prices.sell.PURPUR_BLOCK.price", 12);

        Feudal.getMarketConfig().getConfig().set("prices.buy.PURPUR_PILLAR.price", 24);
        Feudal.getMarketConfig().getConfig().set("prices.sell.PURPUR_PILLAR.price", 12);

        Feudal.getMarketConfig().getConfig().set("prices.buy.PURPUR_STAIRS.price", 36);
        Feudal.getMarketConfig().getConfig().set("prices.sell.PURPUR_STAIRS.price", 18);

        Feudal.getMarketConfig().getConfig().set("prices.buy.PURPUR_SLAB.price", 12);
        Feudal.getMarketConfig().getConfig().set("prices.sell.PURPUR_SLAB.price", 6);

        Feudal.getMarketConfig().getConfig().set("prices.buy.BEETROOT.price", 4);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BEETROOT.price", 2);

        Feudal.getMarketConfig().getConfig().set("prices.buy.BEETROOT_SOUP.price", 26);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BEETROOT_SOUP.price", 13);

        Feudal.getMarketConfig().getConfig().set("prices.buy.BEETROOT_SEEDS.price", 4);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BEETROOT_SEEDS.price", 2);

        Feudal.getMarketConfig().getConfig().set("prices.buy.CHORUS_FRUIT.price", 20);
        Feudal.getMarketConfig().getConfig().set("prices.sell.CHORUS_FRUIT.price", 10);

        Feudal.getMarketConfig().getConfig().set("prices.buy.DRAGONS_BREATH.price", 300);
        Feudal.getMarketConfig().getConfig().set("prices.sell.DRAGONS_BREATH.price", 150);

        Feudal.getMarketConfig().getConfig().set("prices.buy.ELYTRA.price", 30000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.ELYTRA.price", 15000);

        Feudal.getMarketConfig().getConfig().set("prices.buy.END_CRYSTAL.price", 1148);
        Feudal.getMarketConfig().getConfig().set("prices.sell.END_CRYSTAL.price", 574);

        Feudal.getMarketConfig().getConfig().set("prices.buy.CHORUS_FRUIT_POPPED.price", 24);
        Feudal.getMarketConfig().getConfig().set("prices.sell.CHORUS_FRUIT_POPPED.price", 12);

        Feudal.getMarketConfig().getConfig().set("prices.buy.SHIELD.price", 180);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SHIELD.price", 92);

        Feudal.getMarketConfig().getConfig().set("prices.buy.SPECTRAL_ARROW.price", 42);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPECTRAL_ARROW.price", 21);

        Feudal.getMarketConfig().getConfig().set("prices.buy.SPRUCE_BOAT.price", 50);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPRUCE_BOAT.price", 25);

        Feudal.getMarketConfig().getConfig().set("prices.buy.BIRCH_BOAT.price", 50);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BIRCH_BOAT.price", 25);

        Feudal.getMarketConfig().getConfig().set("prices.buy.ACACIA_BOAT.price", 50);
        Feudal.getMarketConfig().getConfig().set("prices.sell.ACACIA_BOAT.price", 25);

        Feudal.getMarketConfig().getConfig().set("prices.buy.JUNGLE_BOAT.price", 50);
        Feudal.getMarketConfig().getConfig().set("prices.sell.JUNGLE_BOAT.price", 25);

        Feudal.getMarketConfig().getConfig().set("prices.buy.DARK_OAK_BOAT.price", 50);
        Feudal.getMarketConfig().getConfig().set("prices.sell.DARK_OAK_BOAT.price", 25);

        Feudal.getMarketConfig().getConfig().set("prices.buy.END_BRICKS.price", 12);
        Feudal.getMarketConfig().getConfig().set("prices.sell.END_BRICKS.price", 6);

        //end 1.9 items

        //1.11 items
        Feudal.getMarketConfig().getConfig().set("prices.buy.WHITE_SHULKER_BOX.price", 10014);
        Feudal.getMarketConfig().getConfig().set("prices.sell.WHITE_SHULKER_BOX.price", 5004);
        Feudal.getMarketConfig().getConfig().set("prices.buy.ORANGE_SHULKER_BOX.price", 10014);
        Feudal.getMarketConfig().getConfig().set("prices.sell.ORANGE_SHULKER_BOX.price", 5004);
        Feudal.getMarketConfig().getConfig().set("prices.buy.MAGENTA_SHULKER_BOX.price", 10014);
        Feudal.getMarketConfig().getConfig().set("prices.sell.MAGENTA_SHULKER_BOX.price", 5004);
        Feudal.getMarketConfig().getConfig().set("prices.buy.LIGHT_BLUE_SHULKER_BOX.price", 10014);
        Feudal.getMarketConfig().getConfig().set("prices.sell.LIGHT_BLUE_SHULKER_BOX.price", 5004);
        Feudal.getMarketConfig().getConfig().set("prices.buy.YELLOW_SHULKER_BOX.price", 10014);
        Feudal.getMarketConfig().getConfig().set("prices.sell.YELLOW_SHULKER_BOX.price", 5004);
        Feudal.getMarketConfig().getConfig().set("prices.buy.LIME_SHULKER_BOX.price", 10014);
        Feudal.getMarketConfig().getConfig().set("prices.sell.LIME_SHULKER_BOX.price", 5004);
        Feudal.getMarketConfig().getConfig().set("prices.buy.PINK_SHULKER_BOX.price", 10014);
        Feudal.getMarketConfig().getConfig().set("prices.sell.PINK_SHULKER_BOX.price", 5004);
        Feudal.getMarketConfig().getConfig().set("prices.buy.GRAY_SHULKER_BOX.price", 10014);
        Feudal.getMarketConfig().getConfig().set("prices.sell.GRAY_SHULKER_BOX.price", 5004);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SILVER_SHULKER_BOX.price", 10014);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SILVER_SHULKER_BOX.price", 5004);
        Feudal.getMarketConfig().getConfig().set("prices.buy.CYAN_SHULKER_BOX.price", 10014);
        Feudal.getMarketConfig().getConfig().set("prices.sell.CYAN_SHULKER_BOX.price", 5004);
        Feudal.getMarketConfig().getConfig().set("prices.buy.PURPLE_SHULKER_BOX.price", 10010);
        Feudal.getMarketConfig().getConfig().set("prices.sell.PURPLE_SHULKER_BOX.price", 5002);
        Feudal.getMarketConfig().getConfig().set("prices.buy.BLUE_SHULKER_BOX.price", 10014);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BLUE_SHULKER_BOX.price", 5004);
        Feudal.getMarketConfig().getConfig().set("prices.buy.BROWN_SHULKER_BOX.price", 10014);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BROWN_SHULKER_BOX.price", 5004);
        Feudal.getMarketConfig().getConfig().set("prices.buy.GREEN_SHULKER_BOX.price", 10014);
        Feudal.getMarketConfig().getConfig().set("prices.sell.GREEN_SHULKER_BOX.price", 5004);
        Feudal.getMarketConfig().getConfig().set("prices.buy.RED_SHULKER_BOX.price", 10014);
        Feudal.getMarketConfig().getConfig().set("prices.sell.RED_SHULKER_BOX.price", 5004);
        Feudal.getMarketConfig().getConfig().set("prices.buy.BLACK_SHULKER_BOX.price", 10014);
        Feudal.getMarketConfig().getConfig().set("prices.sell.BLACK_SHULKER_BOX.price", 5004);

        Feudal.getMarketConfig().getConfig().set("prices.buy.TOTEM.price", 30000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.TOTEM.price", 15000);

        Feudal.getMarketConfig().getConfig().set("prices.buy.SHULKER_SHELL.price", 5000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SHULKER_SHELL.price", 2500);

        Feudal.getMarketConfig().getConfig().set("prices.buy.OBSERVER.price", 200);
        Feudal.getMarketConfig().getConfig().set("prices.sell.OBSERVER.price", 82);

        Feudal.getMarketConfig().getConfig().set("prices.buy.CONCRETE.price", 4);
        Feudal.getMarketConfig().getConfig().set("prices.sell.CONCRETE.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.buy.CONCRETE_POWDER.price", 3.5);
        Feudal.getMarketConfig().getConfig().set("prices.sell.CONCRETE_POWDER.price", 1.75);
        //


        //NBT ITEMS
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPLASH_POTION.price", 40);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPLASH_POTION.night_vision", 200);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPLASH_POTION.long_night_vision", 1100);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPLASH_POTION.invisibility", 600);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPLASH_POTION.long_invisibility", 1100);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPLASH_POTION.leaping", 850);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPLASH_POTION.long_leaping", 1100);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPLASH_POTION.strong_leaping", 1100);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPLASH_POTION.fire_resistance", 350);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPLASH_POTION.long_fire_resistance", 600);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPLASH_POTION.swiftness", 600);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPLASH_POTION.long_swiftness", 1100);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPLASH_POTION.strong_swiftness", 1100);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPLASH_POTION.slowness", 600);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPLASH_POTION.long_slowness", 1100);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPLASH_POTION.water_breathing", 600);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPLASH_POTION.long_water_breathing", 1100);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPLASH_POTION.healing", 600);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPLASH_POTION.strong_healing", 1100);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPLASH_POTION.harming", 600);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPLASH_POTION.strong_harming", 1100);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPLASH_POTION.poison", 600);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPLASH_POTION.long_poison", 1100);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPLASH_POTION.strong_poison", 1100);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPLASH_POTION.regeneration", 600);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPLASH_POTION.long_regeneration", 1100);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPLASH_POTION.strong_regeneration", 1100);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPLASH_POTION.strength", 1100);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPLASH_POTION.long_strength", 1350);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPLASH_POTION.strong_strength", 1350);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPLASH_POTION.weakness", 600);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPLASH_POTION.long_weakness", 1100);
        Feudal.getMarketConfig().getConfig().set("prices.buy.SPLASH_POTION.luck", 4100);

        Feudal.getMarketConfig().getConfig().set("prices.sell.SPLASH_POTION.price", 20);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPLASH_POTION.night_vision", 150);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPLASH_POTION.long_night_vision", 200);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPLASH_POTION.invisibility", 350);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPLASH_POTION.long_invisibility", 600);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPLASH_POTION.leaping", 475);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPLASH_POTION.long_leaping", 550);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPLASH_POTION.strong_leaping", 600);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPLASH_POTION.fire_resistance", 225);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPLASH_POTION.long_fire_resistance", 350);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPLASH_POTION.swiftness", 350);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPLASH_POTION.long_swiftness", 600);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPLASH_POTION.strong_swiftness", 600);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPLASH_POTION.slowness", 350);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPLASH_POTION.long_slowness", 600);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPLASH_POTION.water_breathing", 350);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPLASH_POTION.long_water_breathing", 600);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPLASH_POTION.healing", 350);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPLASH_POTION.strong_healing", 600);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPLASH_POTION.harming", 350);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPLASH_POTION.strong_harming", 600);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPLASH_POTION.poison", 350);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPLASH_POTION.long_poison", 600);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPLASH_POTION.strong_poison", 600);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPLASH_POTION.regeneration", 350);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPLASH_POTION.long_regeneration", 600);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPLASH_POTION.strong_regeneration", 600);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPLASH_POTION.strength", 600);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPLASH_POTION.long_strength", 850);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPLASH_POTION.strong_strength", 850);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPLASH_POTION.weakness", 350);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPLASH_POTION.long_weakness", 600);
        Feudal.getMarketConfig().getConfig().set("prices.sell.SPLASH_POTION.luck", 2100);


        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.price", 10);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.16", 15);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.32", 15);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.64", 10);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8192", 10);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8193", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8257", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8225", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8194", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8258", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8226", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8195", 250);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8259", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8197", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8229", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8198", 100);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8262", 200);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8201", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8265", 1250);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8233", 1250);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8203", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8267", 750);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8235", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8205", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8269", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8206", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8270", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8196", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8260", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8228", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8200", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8264", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8202", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8266", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8204", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8236", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8289", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8290", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8297", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8292", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8227", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8259", 1500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8261", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8230", 750);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8262", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8232", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8234", 1000);
        //Main.getMarketConfig().getConfig().set("prices.buy.POTION.8268", 1500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.8237", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.16385", 600);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.16417", 1100);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.16449", 1100);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.16386", 600);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.16418", 1100);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.16450", 1100);
        //Main.getMarketConfig().getConfig().set("prices.buy.POTION.16419", 1250);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.16451", 600);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.16388", 600);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.16420", 1100);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.16452", 1100);
        //Main.getMarketConfig().getConfig().set("prices.buy.POTION.16453", 1250);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.16421", 1100);
        //Main.getMarketConfig().getConfig().set("prices.buy.POTION.16422", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.16454", 1100);
        //Main.getMarketConfig().getConfig().set("prices.buy.POTION.16424", 1250);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.16456", 1100);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.16393", 1100);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.16425", 1350);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.16457", 1350);
        //Main.getMarketConfig().getConfig().set("prices.buy.POTION.16426", 1250);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.16458", 1100);
        //Main.getMarketConfig().getConfig().set("prices.buy.POTION.16395", 750);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.16427", 1100);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.16459", 1100);
        //Main.getMarketConfig().getConfig().set("prices.buy.POTION.16460", 1750);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.16428", 1100);
        //Main.getMarketConfig().getConfig().set("prices.buy.POTION.16429", 1250);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.16461", 1100);
        //Main.getMarketConfig().getConfig().set("prices.buy.POTION.16430", 2250);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.16462", 1100);

        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.awkward", 15);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.night_vision", 100);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.long_night_vision", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.invisibility", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.long_invisibility", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.leaping", 750);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.long_leaping", 900);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.strong_leaping", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.fire_resistance", 250);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.long_fire_resistance", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.swiftness", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.long_swiftness", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.strong_swiftness", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.slowness", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.long_slowness", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.water_breathing", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.long_water_breathing", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.healing", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.strong_healing", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.harming", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.strong_harming", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.poison", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.long_poison", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.strong_poison", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.regeneration", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.long_regeneration", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.strong_regeneration", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.strength", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.long_strength", 1250);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.strong_strength", 1250);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.weakness", 500);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.long_weakness", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.buy.POTION.luck", 4000);


        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.price", 2);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.16", 5);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.32", 5);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.64", 2);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8192", 2);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8193", 250);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8257", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8225", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8194", 250);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8258", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8226", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8195", 125);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8259", 250);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8197", 250);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8229", 250);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8198", 50);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8262", 100);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8201", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8265", 750);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8233", 750);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8203", 250);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8267", 375);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8235", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8205", 250);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8269", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8206", 250);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8270", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8196", 250);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8260", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8228", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8200", 250);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8264", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8202", 250);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8266", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8204", 250);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8236", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8289", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8290", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8297", 750);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8292", 1000);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8227", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8259", 750);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8261", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8230", 325);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8262", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8232", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8234", 500);
        //Main.getMarketConfig().getConfig().set("prices.sell.POTION.8268", 750);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.8237", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.16385", 350);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.16417", 600);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.16449", 600);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.16386", 350);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.16418", 600);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.16450", 600);
        //Main.getMarketConfig().getConfig().set("prices.sell.POTION.16419", 625);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.16451", 350);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.16388", 350);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.16420", 600);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.16452", 600);
        //Main.getMarketConfig().getConfig().set("prices.sell.POTION.16453", 625);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.16421", 600);
        //Main.getMarketConfig().getConfig().set("prices.sell.POTION.16422", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.16454", 200);
        //Main.getMarketConfig().getConfig().set("prices.sell.POTION.16424", 625);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.16456", 600);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.16393", 600);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.16425", 850);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.16457", 850);
        //Main.getMarketConfig().getConfig().set("prices.sell.POTION.16426", 650);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.16458", 600);
        //Main.getMarketConfig().getConfig().set("prices.sell.POTION.16395", 375);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.16427", 600);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.16459", 600);
        //Main.getMarketConfig().getConfig().set("prices.sell.POTION.16460", 875);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.16428", 600);
        //Main.getMarketConfig().getConfig().set("prices.sell.POTION.16429", 625);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.16461", 600);
        //Main.getMarketConfig().getConfig().set("prices.sell.POTION.16430", 1125);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.16462", 350);

        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.awkward", 5);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.night_vision", 50);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.long_night_vision", 100);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.invisibility", 250);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.long_invisibility", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.leaping", 375);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.long_leaping", 450);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.strong_leaping", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.fire_resistance", 125);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.long_fire_resistance", 250);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.swiftness", 250);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.long_swiftness", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.strong_swiftness", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.slowness", 250);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.long_slowness", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.water_breathing", 250);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.long_water_breathing", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.healing", 250);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.strong_healing", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.harming", 250);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.strong_harming", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.poison", 250);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.long_poison", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.strong_poison", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.regeneration", 250);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.long_regeneration", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.strong_regeneration", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.strength", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.long_strength", 750);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.strong_strength", 750);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.weakness", 250);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.long_weakness", 500);
        Feudal.getMarketConfig().getConfig().set("prices.sell.POTION.luck", 2000);




        Feudal.getMarketConfig().getConfig().set("prices.buy.LINGERING_POTION.price", 10);
        for(final String section : Feudal.getMarketConfig().getConfig().getConfigurationSection("prices.buy.SPLASH_POTION").getKeys(false)){
            final double price = Feudal.getMarketConfig().getConfig().getDouble("prices.buy.SPLASH_POTION." + section);
            Feudal.getMarketConfig().getConfig().set("prices.buy.LINGERING_POTION." + section, price + 300);
        }
        Feudal.getMarketConfig().getConfig().set("prices.sell.LINGERING_POTION.price", 2);
        for(final String section : Feudal.getMarketConfig().getConfig().getConfigurationSection("prices.sell.SPLASH_POTION").getKeys(false)){
            final double price = Feudal.getMarketConfig().getConfig().getDouble("prices.sell.SPLASH_POTION." + section);
            Feudal.getMarketConfig().getConfig().set("prices.sell.LINGERING_POTION." + section, price + 150);
        }

        Feudal.getMarketConfig().getConfig().set("prices.buy.TIPPED_ARROW.price", 40);
        for(final String section : Feudal.getMarketConfig().getConfig().getConfigurationSection("prices.buy.LINGERING_POTION").getKeys(false)){
            final double price = Feudal.getMarketConfig().getConfig().getDouble("prices.buy.LINGERING_POTION." + section);
            Feudal.getMarketConfig().getConfig().set("prices.buy.TIPPED_ARROW." + section, (int)((80+price)/7));
        }
        Feudal.getMarketConfig().getConfig().set("prices.sell.TIPPED_ARROW.price", 20);
        for(final String section : Feudal.getMarketConfig().getConfig().getConfigurationSection("prices.sell.LINGERING_POTION").getKeys(false)){
            final double price = Feudal.getMarketConfig().getConfig().getDouble("prices.sell.LINGERING_POTION." + section);
            Feudal.getMarketConfig().getConfig().set("prices.sell.TIPPED_ARROW." + section, (int)((20+price)/7));
        }
        //END NBT ITEMS










        Feudal.getMarketConfig().getConfig().set("canSellDamagedItems", false);
        Feudal.getMarketConfig().getConfig().set("priceToolsOnDurability", true);
        Feudal.getMarketConfig().getConfig().set("NonProfessionSellMultiplier", 0.7);

        //Enchantments
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.buy.PROTECTION_ENVIRONMENTAL", 1000);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.buy.PROTECTION_FIRE", 500);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.buy.PROTECTION_FALL", 700);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.buy.PROTECTION_EXPLOSIONS", 500);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.buy.PROTECTION_PROJECTILE", 700);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.buy.OXYGEN", 500);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.buy.WATER_WORKER", 300);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.buy.THORNS", 2000);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.buy.DEPTH_STRIDER", 500);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.buy.DAMAGE_ALL", 2000);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.buy.DAMAGE_UNDEAD", 400);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.buy.DAMAGE_ARTHROPODS", 200);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.buy.KNOCKBACK", 1000);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.buy.FIRE_ASPECT", 1000);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.buy.LOOT_BONUS_MOBS", 500);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.buy.DIG_SPEED", 1000);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.buy.SILK_TOUCH", 2500);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.buy.DURABILITY", 750);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.buy.LOOT_BONUS_BLOCKS", 1500);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.buy.ARROW_DAMAGE", 1000);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.buy.ARROW_KNOCKBACK", 2000);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.buy.ARROW_FIRE", 1500);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.buy.ARROW_INFINITE", 2000);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.buy.LUCK", 1000);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.buy.LURE", 1000);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.buy.BINDING_CURSE", 1);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.buy.VANISHING_CURSE", 1);

        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.sell.PROTECTION_ENVIRONMENTAL", 500);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.sell.PROTECTION_FIRE", 250);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.sell.PROTECTION_FALL", 350);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.sell.PROTECTION_EXPLOSIONS", 250);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.sell.PROTECTION_PROJECTILE", 350);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.sell.OXYGEN", 250);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.sell.WATER_WORKER", 100);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.sell.THORNS", 1000);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.sell.DEPTH_STRIDER", 250);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.sell.DAMAGE_ALL", 1000);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.sell.DAMAGE_UNDEAD", 200);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.sell.DAMAGE_ARTHROPODS", 100);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.sell.KNOCKBACK", 500);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.sell.FIRE_ASPECT", 750);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.sell.LOOT_BONUS_MOBS", 250);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.sell.DIG_SPEED", 500);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.sell.SILK_TOUCH", 1250);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.sell.DURABILITY", 325);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.sell.LOOT_BONUS_BLOCKS", 750);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.sell.ARROW_DAMAGE", 500);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.sell.ARROW_KNOCKBACK", 1000);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.sell.ARROW_FIRE", 750);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.sell.ARROW_INFINITE", 1000);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.sell.LUCK", 500);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.sell.LURE", 500);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.sell.BINDING_CURSE", 0.5);
        Feudal.getMarketConfig().getConfig().set("enchantmentPrices.sell.VANISHING_CURSE", 0.5);
        //

        reformatSellBuy();

        Feudal.getMarketConfig().getConfig().set("merchantsCanBuyForSellPrice", true);
        Feudal.getMarketConfig().getConfig().set("dumpMarketItems", 5000);
        Feudal.getMarketConfig().getConfig().set("minDurability", 10);
        final ArrayList<String> exceptions = new ArrayList<>();
        exceptions.add("name:Exact Item Name with &ecolors");
        exceptions.add("name_contains:Sonic Blaster");
        exceptions.add("lore_contains:String a item lore contains");
        exceptions.add("enchants:Enchantment name such as DURABILITY. View all enchantment names here: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/enchantments/Enchantment.html");
        exceptions.add("enchantsLevel:DURABILITY-101");
        exceptions.add("name:Use the tilde symbol to require multiple factors~enchantsLevel:DAMAGE_ALL-255");
        Feudal.getMarketConfig().getConfig().set("exceptions", exceptions);
        Feudal.getMarketConfig().getConfig().set("SupplyAndDemand", true);

        // Multipliers (Max & Min)
        Feudal.getMarketConfig().getConfig().set("Max-multiplier", 25);
        Feudal.getMarketConfig().getConfig().set("Min-multiplier", 0.04);

        Feudal.getMarketConfig().getConfig().set("Buy-multiplier", 1.0);
        Feudal.getMarketConfig().getConfig().set("Sell-multiplier", 1.0);
    }

    /*
     * 1.12.0 version
     * Changes from prices.buy and prices.sell to prices.item.buy and prices.item.sell
     */
    private static void reformatSellBuy() {
        final FileConfiguration c = Feudal.getMarketConfig().getConfig();
        for(final String sellType : c.getConfigurationSection("prices.sell").getKeys(false)) {
            c.set("prices." + sellType + ".sell", c.get("prices.sell." + sellType, null));
			/*for(String children : c.getConfigurationSection("prices.sell." + sellType).getKeys(true)) {
				Feudal.error("Sell type: " + sellType + " - Children: " + children);
				c.set("prices." + sellType + ".sell." + children, c.get("prices.sell." + sellType, null));
			}*/
            c.set("prices.sell." + sellType, null);
        }
        for(final String buyType : c.getConfigurationSection("prices.buy").getKeys(false)) {
            c.set("prices." + buyType + ".buy", c.get("prices.buy." + buyType, null));
			/*for(String children : c.getConfigurationSection("prices.buy." + buyType).getKeys(true)) {
				c.set("prices." + buyType + ".buy." + children, c.get("prices.buy." + buyType, null));
			}*/
            c.set("prices.buy." + buyType, null);
        }

        c.set("prices.buy", null);
        c.set("prices.sell", null);
    }

    protected static boolean marketConfig() throws Exception{
        boolean load = true;
        Feudal.setMarketConfig(new Configuration(new File(Feudal.getPluginFolder(), "marketConfig.yml")));
        try {
            if(Feudal.getMarketConfig().loadConfig()){
                createMarketConfigDefaults();
            }else{
                updateDefaults();
            }
            makeCategories();
        } catch (final Exception e) {
            load = false;


            Feudal.warn("Failed to load config: " + Feudal.getMarketConfig().getFile().getAbsolutePath() + ".  Please check to make sure the config does not have any syntax errors.");
            try {
                Feudal.getMarketConfig().broke();
            } catch (final Exception e1) {

                Feudal.error("FAILED TO SAVE BROKEN BACKUP OF CONFIG: " + Feudal.getMarketConfig().getFile().getAbsolutePath());
                throw e1;
            }
            try{
                Feudal.getMarketConfig().loadConfig();
                createMarketConfigDefaults();
            }catch(final Exception e1){
                throw e1;
            }
            makeCategories();
            throw e;
        }
        try {
            Feudal.getMarketConfig().save();
        } catch (final Exception e) {
            load = false;

            Feudal.error("Failed to save config: " + Feudal.getMarketConfig().getFile().getAbsolutePath());
            throw e;
        }
        return load;
    }

    private static void updateDefaults() {
        if(!Feudal.getMarketConfig().getConfig().contains("exceptions") && Feudal.getMarketConfig().getConfig().contains("prices.buy.ANVIL.price")){
            final ArrayList<String> exceptions = new ArrayList<>();
            exceptions.add("name:Exact Item Name with &ecolors");
            exceptions.add("name_contains:Sonic Blaster");
            exceptions.add("lore_contains:String a item lore contains");
            exceptions.add("enchants:Enchantment name such as DURABILITY. View all enchantment names here: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/enchantments/Enchantment.html");
            exceptions.add("enchantsLevel:DURABILITY-101");
            exceptions.add("name:Use the tilde symbol to require multiple factors~enchantsLevel:DAMAGE_ALL-255");
            Feudal.getMarketConfig().getConfig().set("exceptions", exceptions);
        }
        if(!Feudal.getMarketConfig().getConfig().contains("prices.buy.NETHER_WART_BLOCK.price") && Feudal.getMarketConfig().getConfig().contains("prices.buy.ANVIL.price")){
            Feudal.getMarketConfig().getConfig().set("prices.buy.NETHER_WART_BLOCK.price", 90);
            Feudal.getMarketConfig().getConfig().set("prices.sell.NETHER_WART_BLOCK.price", 45);
            Feudal.getMarketConfig().getConfig().set("prices.buy.MAGMA.price", 20);
            Feudal.getMarketConfig().getConfig().set("prices.sell.MAGMA.price", 10);
            Feudal.getMarketConfig().getConfig().set("prices.buy.RED_NETHER_BRICK.price", 40);
            Feudal.getMarketConfig().getConfig().set("prices.sell.RED_NETHER_BRICK.price", 14);
            Feudal.getMarketConfig().getConfig().set("prices.buy.BONE_BLOCK.price", 90);
            Feudal.getMarketConfig().getConfig().set("prices.sell.BONE_BLOCK.price", 18);
        }
        if(!Feudal.getMarketConfig().getConfig().contains("prices.buy.WHITE_SHULKER_BOX.price") && Feudal.getMarketConfig().getConfig().contains("prices.buy.ANVIL.price")){
            Feudal.getMarketConfig().getConfig().set("prices.buy.WHITE_SHULKER_BOX.price", 10014);
            Feudal.getMarketConfig().getConfig().set("prices.sell.WHITE_SHULKER_BOX.price", 5004);
            Feudal.getMarketConfig().getConfig().set("prices.buy.ORANGE_SHULKER_BOX.price", 10014);
            Feudal.getMarketConfig().getConfig().set("prices.sell.ORANGE_SHULKER_BOX.price", 5004);
            Feudal.getMarketConfig().getConfig().set("prices.buy.MAGENTA_SHULKER_BOX.price", 10014);
            Feudal.getMarketConfig().getConfig().set("prices.sell.MAGENTA_SHULKER_BOX.price", 5004);
            Feudal.getMarketConfig().getConfig().set("prices.buy.LIGHT_BLUE_SHULKER_BOX.price", 10014);
            Feudal.getMarketConfig().getConfig().set("prices.sell.LIGHT_BLUE_SHULKER_BOX.price", 5004);
            Feudal.getMarketConfig().getConfig().set("prices.buy.YELLOW_SHULKER_BOX.price", 10014);
            Feudal.getMarketConfig().getConfig().set("prices.sell.YELLOW_SHULKER_BOX.price", 5004);
            Feudal.getMarketConfig().getConfig().set("prices.buy.LIME_SHULKER_BOX.price", 10014);
            Feudal.getMarketConfig().getConfig().set("prices.sell.LIME_SHULKER_BOX.price", 5004);
            Feudal.getMarketConfig().getConfig().set("prices.buy.PINK_SHULKER_BOX.price", 10014);
            Feudal.getMarketConfig().getConfig().set("prices.sell.PINK_SHULKER_BOX.price", 5004);
            Feudal.getMarketConfig().getConfig().set("prices.buy.GRAY_SHULKER_BOX.price", 10014);
            Feudal.getMarketConfig().getConfig().set("prices.sell.GRAY_SHULKER_BOX.price", 5004);
            Feudal.getMarketConfig().getConfig().set("prices.buy.SILVER_SHULKER_BOX.price", 10014);
            Feudal.getMarketConfig().getConfig().set("prices.sell.SILVER_SHULKER_BOX.price", 5004);
            Feudal.getMarketConfig().getConfig().set("prices.buy.CYAN_SHULKER_BOX.price", 10014);
            Feudal.getMarketConfig().getConfig().set("prices.sell.CYAN_SHULKER_BOX.price", 5004);
            Feudal.getMarketConfig().getConfig().set("prices.buy.PURPLE_SHULKER_BOX.price", 10010);
            Feudal.getMarketConfig().getConfig().set("prices.sell.PURPLE_SHULKER_BOX.price", 5002);
            Feudal.getMarketConfig().getConfig().set("prices.buy.BLUE_SHULKER_BOX.price", 10014);
            Feudal.getMarketConfig().getConfig().set("prices.sell.BLUE_SHULKER_BOX.price", 5004);
            Feudal.getMarketConfig().getConfig().set("prices.buy.BROWN_SHULKER_BOX.price", 10014);
            Feudal.getMarketConfig().getConfig().set("prices.sell.BROWN_SHULKER_BOX.price", 5004);
            Feudal.getMarketConfig().getConfig().set("prices.buy.GREEN_SHULKER_BOX.price", 10014);
            Feudal.getMarketConfig().getConfig().set("prices.sell.GREEN_SHULKER_BOX.price", 5004);
            Feudal.getMarketConfig().getConfig().set("prices.buy.RED_SHULKER_BOX.price", 10014);
            Feudal.getMarketConfig().getConfig().set("prices.sell.RED_SHULKER_BOX.price", 5004);
            Feudal.getMarketConfig().getConfig().set("prices.buy.BLACK_SHULKER_BOX.price", 10014);
            Feudal.getMarketConfig().getConfig().set("prices.sell.BLACK_SHULKER_BOX.price", 5004);

            Feudal.getMarketConfig().getConfig().set("prices.buy.TOTEM.price", 30000);
            Feudal.getMarketConfig().getConfig().set("prices.sell.TOTEM.price", 15000);

            Feudal.getMarketConfig().getConfig().set("prices.buy.SHULKER_SHELL.price", 5000);
            Feudal.getMarketConfig().getConfig().set("prices.sell.SHULKER_SHELL.price", 2500);

            Feudal.getMarketConfig().getConfig().set("prices.buy.OBSERVER.price", 200);
            Feudal.getMarketConfig().getConfig().set("prices.sell.OBSERVER.price", 82);

            Feudal.getMarketConfig().getConfig().set("SupplyAndDemand", true);

        }
        if(!Feudal.getMarketConfig().getConfig().contains("enchantmentPrices.buy.BINDING_CURSE")  && Feudal.getMarketConfig().getConfig().contains("prices.buy.ANVIL.price")){
            Feudal.getMarketConfig().getConfig().set("enchantmentPrices.buy.BINDING_CURSE", 1);
            Feudal.getMarketConfig().getConfig().set("enchantmentPrices.buy.VANISHING_CURSE", 1);
            Feudal.getMarketConfig().getConfig().set("enchantmentPrices.sell.BINDING_CURSE", 0.5);
            Feudal.getMarketConfig().getConfig().set("enchantmentPrices.sell.VANISHING_CURSE", 0.5);
        }
        if(!Feudal.getMarketConfig().getConfig().contains("Max-multiplier")){
            // Multipliers (Max & Min)
            Feudal.getMarketConfig().getConfig().set("Max-multiplier", 25);
            Feudal.getMarketConfig().getConfig().set("Min-multiplier", 0.04);
        }
        if(!Feudal.getMarketConfig().getConfig().contains("Buy-multiplier")){
            // Buy and sell multipliers to change ALL prices at the same time.
            Feudal.getMarketConfig().getConfig().set("Buy-multiplier", 1.0);
            Feudal.getMarketConfig().getConfig().set("Sell-multiplier", 1.0);
        }
        if(!Feudal.getMarketConfig().getConfig().contains("prices.buy.CONCRETE.price") && Feudal.getMarketConfig().getConfig().contains("prices.buy.ANVIL.price")){
            Feudal.getMarketConfig().getConfig().set("prices.buy.CONCRETE.price", 4);
            Feudal.getMarketConfig().getConfig().set("prices.sell.CONCRETE.price", 2);
            Feudal.getMarketConfig().getConfig().set("prices.buy.CONCRETE_POWDER.price", 3.5);
            Feudal.getMarketConfig().getConfig().set("prices.sell.CONCRETE_POWDER.price", 1.75);
        }
        if(Feudal.getMarketConfig().getConfig().contains("prices.buy.ANVIL.price")) {
            reformatSellBuy();
        }
    }

    private static void makeCategories() throws Exception{
        if(Feudal.getMarketConfig().getConfig().contains("categoriesInventory.ALL")){
            Feudal.getCategories().add(new Category("ALL",
                    Feudal.getMarketConfig().getConfig().getString("categoriesInventory.ALL.item.name"),
                    null,
                    Feudal.getMarketConfig().getConfig().getInt("categoriesInventory.ALL.index"),
                    Feudal.getMarketConfig().getConfig().getString("categoriesInventory.ALL.item.type"),
                    Feudal.getMarketConfig().getConfig().getInt("categoriesInventory.ALL.item.data")));
        }
        if(Feudal.getMarketConfig().getConfig().contains("categories")){
            for(final String str : Feudal.getMarketConfig().getConfig().getConfigurationSection("categories").getKeys(false)){
                if(str.equalsIgnoreCase("ALL")){
                    continue;
                }
                if(Feudal.getMarketConfig().getConfig().contains("categories." + str) && Feudal.getMarketConfig().getConfig().contains("categoriesInventory." + str)){
                    try{
                        Feudal.getCategories().add(new Category(str,
                                Feudal.getMarketConfig().getConfig().getString("categoriesInventory."+str+".item.name"),
                                Feudal.getMarketConfig().getConfig().getStringList("categories."+str),
                                Feudal.getMarketConfig().getConfig().getInt("categoriesInventory." + str + ".index"),
                                Feudal.getMarketConfig().getConfig().getString("categoriesInventory." + str + ".item.type"),
                                Feudal.getMarketConfig().getConfig().getInt("categoriesInventory." + str + ".item.data")));
                    }catch(final Exception e){


                        Feudal.error("Failed to load market category: " + str);
                        throw e;
                    }
                }
            }
        }
    }
}