package com.huanle;

import com.huanle.items.ManaBottleItem;
import com.huanle.items.weapons.*;
import com.huanle.items.weapons.MagicSword;
import com.huanle.items.tools.*;
import com.huanle.items.food.*;
import com.huanle.items.armor.*;
import com.huanle.items.PowderItem;
import com.huanle.materials.*;
import com.huanle.entities.*;
import com.huanle.items.curios.*;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, HuanleMod.MOD_ID);

    public static final RegistryObject<Item> HUANLE_SWORD = ITEMS.register("huanle_sword",
            () -> new HuanleSword(Tiers.NETHERITE, 20, -2.4F,
                    new Item.Properties().durability(2000)));

    public static final RegistryObject<Item> THROWING_AXE = ITEMS.register("throwing_axe",
            () -> new ThrowingAxeItem(Tiers.NETHERITE, 8.0F, -3.0F,
                    new Item.Properties().durability(1500)));

    public static final RegistryObject<Item> MANA_BOTTLE = ITEMS.register("mana_bottle",
            () -> new ManaBottleItem(new Item.Properties().rarity(Rarity.EPIC).stacksTo(16)));

    public static final RegistryObject<Item> SUN_GOD_SWORD = ITEMS.register("sun_god_sword",
            () -> new SunGodSword(Tiers.NETHERITE, 15, -2.4F,
                    new Item.Properties().durability(2000)));

    public static final RegistryObject<Item> ENDER_SWORD = ITEMS.register("ender_sword",
            () -> new EnderSword(Tiers.NETHERITE, 15, -2.4F,
                    new Item.Properties().durability(2000)));

    public static final RegistryObject<Item> FLAME_BOW = ITEMS.register("flame_bow",
            () -> new FlameBowItem(new Item.Properties().durability(500)));

    public static final RegistryObject<Item> FLAME_ARROW = ITEMS.register("flame_arrow",
            () -> new FlameArrowItem(new Item.Properties()));

    public static final RegistryObject<Item> MAGIC_CRYSTAL = ITEMS.register("magic_crystal",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> GREEN_DRAGON_SWORD = ITEMS.register("green_dragon_sword",
            () -> new GreenDragonSword(Tiers.NETHERITE, 10, -2.4F,
                    new Item.Properties().durability(2000)));

    public static final RegistryObject<Item> MOONLIGHT_SWORD = ITEMS.register("moonlight_sword",
            () -> new MoonlightSword(Tiers.NETHERITE, 10, -2.4F,
                    new Item.Properties().durability(2000)));

    public static final RegistryObject<Item> MOON_CRESCENT_SWORD = ITEMS.register("moon_crescent_sword",
            () -> new MoonCrescentSword(Tiers.NETHERITE, 5, -2.4F,
                    new Item.Properties().durability(1500)));

    public static final RegistryObject<Item> BLUE_FLAME_SWORD = ITEMS.register("blue_flame_sword",
            () -> new BlueFlameSword(Tiers.NETHERITE, 10, -2.4F,
                    new Item.Properties().durability(1500)));

    public static final RegistryObject<Item> FOREST_BLADE = ITEMS.register("forest_blade",
            () -> new ForestBlade(Tiers.NETHERITE, 10, -2.4F,
                    new Item.Properties().durability(1500)));

    public static final RegistryObject<Item> ENDER_HAMMER = ITEMS.register("ender_hammer",
            () -> new EnderHammer(Tiers.NETHERITE, 10, -3.5F,
                    new Item.Properties().durability(3000)));

    public static final RegistryObject<Item> DEATH_SCYTHE = ITEMS.register("death_scythe",
            () -> new DeathScythe(Tiers.NETHERITE, 10, -2.8F,
                    new Item.Properties().durability(2000)));

    public static final RegistryObject<Item> LAVA_INGOT = ITEMS.register("lava_ingot",
            () -> new Item(new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> LAVA_SWORD = ITEMS.register("lava_sword",
            () -> new LavaSword(Tiers.NETHERITE, 8, -2.4F,
                    new Item.Properties().durability(2000).fireResistant()));

    public static final RegistryObject<Item> LOOTING_DAGGER = ITEMS.register("looting_dagger",
            () -> new LootingDagger(Tiers.NETHERITE, 3, -1.8F,
                    new Item.Properties().durability(1000)));

    public static final RegistryObject<Item> FIRE_GOD_INGOT = ITEMS.register("fire_god_ingot",
            () -> new FireGodIngot(new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> FIRE_GOD_DAGGER = ITEMS.register("fire_god_dagger",
            () -> new FireGodDagger(Tiers.NETHERITE, 8, -1.8F,
                    new Item.Properties().durability(1500).fireResistant()));

    public static final RegistryObject<Item> LIFE_STAFF = ITEMS.register("life_staff",
            () -> new LifeStaff(new Item.Properties().durability(2000)));

    public static final RegistryObject<Item> DRAGON_HUNTER_SWORD = ITEMS.register("dragon_hunter_sword",
            () -> new DragonHunterSword(Tiers.NETHERITE, 15, -2.4F,
                    new Item.Properties().durability(3000)));

    public static final RegistryObject<Item> FLAME_GOD_SMITHING_TEMPLATE = ITEMS.register("flame_god_smithing_template",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> FLAME_GOD_AXE = ITEMS.register("flame_god_axe",
            () -> new FlameGodAxe(Tiers.NETHERITE, 12, -3.0F,
                    new Item.Properties().durability(2500).fireResistant()));

    public static final RegistryObject<Item> FLAME_GOD_PICKAXE = ITEMS.register("flame_god_pickaxe",
            () -> new FlameGodPickaxe(Tiers.NETHERITE, 6, -2.8F,
                    new Item.Properties().durability(2500).fireResistant()));

    public static final RegistryObject<Item> FLAME_GOD_HOE = ITEMS.register("flame_god_hoe",
            () -> new FlameGodHoe(Tiers.NETHERITE, -2, -1.0F,
                    new Item.Properties().durability(2500).fireResistant()));

    public static final RegistryObject<Item> FLAME_GOD_SHOVEL = ITEMS.register("flame_god_shovel",
            () -> new FlameGodShovel(Tiers.NETHERITE, 6.5F, -3.0F,
                    new Item.Properties().durability(2500).fireResistant()));

    public static final RegistryObject<Item> FOREST_SMITHING_TEMPLATE = ITEMS.register("forest_smithing_template",
            () -> new ForestSmithingTemplate(new Item.Properties()));
            
    public static final RegistryObject<Item> FOREST_INGOT = ITEMS.register("forest_ingot",
            () -> new ForestIngot(new Item.Properties()));
            
    public static final RegistryObject<Item> FOREST_AXE = ITEMS.register("forest_axe",
            () -> new ForestAxe(Tiers.NETHERITE, 10.0F, -3.0F,
                    new Item.Properties().durability(3000)));
                    
    public static final RegistryObject<Item> FOREST_PICKAXE = ITEMS.register("forest_pickaxe",
            () -> new ForestPickaxe(Tiers.NETHERITE, 5, -2.8F,
                    new Item.Properties().durability(3000)));
                    
    public static final RegistryObject<Item> FOREST_HOE = ITEMS.register("forest_hoe",
            () -> new ForestHoe(Tiers.NETHERITE, -2, -1.0F,
                    new Item.Properties().durability(3000)));
                    
    public static final RegistryObject<Item> FOREST_SHOVEL = ITEMS.register("forest_shovel",
            () -> new ForestShovel(Tiers.NETHERITE, 6.5F, -3.0F,
                    new Item.Properties().durability(3000)));
                    
    public static final RegistryObject<Item> MAGIC_CRYSTAL_APPLE = ITEMS.register("magic_crystal_apple",
            () -> new MagicCrystalApple(new Item.Properties().rarity(Rarity.EPIC)));
            
    public static final RegistryObject<Item> ENCHANTED_MAGIC_CRYSTAL_APPLE = ITEMS.register("enchanted_magic_crystal_apple",
            () -> new EnchantedMagicCrystalApple(new Item.Properties().rarity(Rarity.EPIC)));
            
    public static final RegistryObject<Item> FEAR_TEAR = ITEMS.register("fear_tear",
            () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));
            
    public static final RegistryObject<Item> EARTH_SWORD = ITEMS.register("earth_sword",
            () -> new EarthSword(Tiers.NETHERITE, 15, -2.4F,
                    new Item.Properties().durability(3000)));

    public static final RegistryObject<Item> FOREST_HELMET = ITEMS.register("forest_helmet",
            () -> new ForestArmorItem(ArmorItem.Type.HELMET,
                    new Item.Properties().durability(1000)));

    public static final RegistryObject<Item> FOREST_CHESTPLATE = ITEMS.register("forest_chestplate",
            () -> new ForestArmorItem(ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(1000)));

    public static final RegistryObject<Item> FOREST_LEGGINGS = ITEMS.register("forest_leggings",
            () -> new ForestArmorItem(ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(1000)));

    public static final RegistryObject<Item> FOREST_BOOTS = ITEMS.register("forest_boots",
            () -> new ForestArmorItem(ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(1000)));

    public static final RegistryObject<Item> BLACK_GOLD_KATANA = ITEMS.register("black_gold_katana",
            () -> new BlackGoldKatana(Tiers.NETHERITE, 4, -2.4F,
                    new Item.Properties().durability(1500)));
                    
    public static final RegistryObject<Item> ENDER_CRYSTAL = ITEMS.register("ender_crystal",
            () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));
            
    public static final RegistryObject<Item> ENDER_CRYSTAL_APPLE = ITEMS.register("ender_crystal_apple",
            () -> new EnderCrystalApple(new Item.Properties().rarity(Rarity.EPIC)));
            
    public static final RegistryObject<Item> ENCHANTED_ENDER_CRYSTAL_APPLE = ITEMS.register("enchanted_ender_crystal_apple",
            () -> new EnchantedEnderCrystalApple(new Item.Properties().rarity(Rarity.EPIC)));
            
    public static final RegistryObject<Item> AMETHYST_APPLE = ITEMS.register("amethyst_apple",
            () -> new AmethystApple(new Item.Properties().rarity(Rarity.EPIC)));
            
    public static final RegistryObject<Item> ENCHANTED_AMETHYST_APPLE = ITEMS.register("enchanted_amethyst_apple",
            () -> new EnchantedAmethystApple(new Item.Properties().rarity(Rarity.EPIC)));
            
    public static final RegistryObject<Item> FIRE_GOD_HELMET = ITEMS.register("fire_god_helmet",
            () -> new FireGodArmorItem(FireGodArmorMaterial.FIRE_GOD, ArmorItem.Type.HELMET,
                    new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> FIRE_GOD_CHESTPLATE = ITEMS.register("fire_god_chestplate",
            () -> new FireGodArmorItem(FireGodArmorMaterial.FIRE_GOD, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> FIRE_GOD_LEGGINGS = ITEMS.register("fire_god_leggings",
            () -> new FireGodArmorItem(FireGodArmorMaterial.FIRE_GOD, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> FIRE_GOD_BOOTS = ITEMS.register("fire_god_boots",
            () -> new FireGodArmorItem(FireGodArmorMaterial.FIRE_GOD, ArmorItem.Type.BOOTS,
                    new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> ENDER_CRYSTAL_SWORD = ITEMS.register("ender_crystal_sword",
            () -> new EnderCrystalSword(new Item.Properties().durability(2000)));

    public static final RegistryObject<Item> ENDER_CRYSTAL_PICKAXE = ITEMS.register("ender_crystal_pickaxe",
            () -> new EnderCrystalPickaxe(new Item.Properties().durability(2000)));

    public static final RegistryObject<Item> ENDER_CRYSTAL_AXE = ITEMS.register("ender_crystal_axe",
            () -> new EnderCrystalAxe(new Item.Properties().durability(2000)));

    public static final RegistryObject<Item> ENDER_CRYSTAL_HOE = ITEMS.register("ender_crystal_hoe",
            () -> new EnderCrystalHoe(new Item.Properties().durability(2000)));

    public static final RegistryObject<Item> ENDER_CRYSTAL_SHOVEL = ITEMS.register("ender_crystal_shovel",
            () -> new EnderCrystalShovel(new Item.Properties().durability(2000)));

    public static final RegistryObject<Item> EMERALD_SWORD = ITEMS.register("emerald_sword",
            () -> new EmeraldSword(new Item.Properties().durability(1261)));

    public static final RegistryObject<Item> EMERALD_PICKAXE = ITEMS.register("emerald_pickaxe",
            () -> new EmeraldPickaxe(new Item.Properties().durability(1261)));

    public static final RegistryObject<Item> EMERALD_AXE = ITEMS.register("emerald_axe",
            () -> new EmeraldAxe(new Item.Properties().durability(1261)));

    public static final RegistryObject<Item> EMERALD_SHOVEL = ITEMS.register("emerald_shovel",
            () -> new EmeraldShovel(new Item.Properties().durability(1261)));

    public static final RegistryObject<Item> EMERALD_HOE = ITEMS.register("emerald_hoe",
            () -> new EmeraldHoe(new Item.Properties().durability(1261)));

    public static final RegistryObject<Item> ENDER_CRYSTAL_HELMET = ITEMS.register("ender_crystal_helmet",
            () -> new EnderCrystalArmorItem(EnderCrystalArmorMaterial.ENDER_CRYSTAL, ArmorItem.Type.HELMET,
                    new Item.Properties()));

    public static final RegistryObject<Item> ENDER_CRYSTAL_CHESTPLATE = ITEMS.register("ender_crystal_chestplate",
            () -> new EnderCrystalArmorItem(EnderCrystalArmorMaterial.ENDER_CRYSTAL, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties()));

    public static final RegistryObject<Item> ENDER_CRYSTAL_LEGGINGS = ITEMS.register("ender_crystal_leggings",
            () -> new EnderCrystalArmorItem(EnderCrystalArmorMaterial.ENDER_CRYSTAL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties()));

    public static final RegistryObject<Item> ENDER_CRYSTAL_BOOTS = ITEMS.register("ender_crystal_boots",
            () -> new EnderCrystalArmorItem(EnderCrystalArmorMaterial.ENDER_CRYSTAL, ArmorItem.Type.BOOTS,
                    new Item.Properties()));

    // 绿宝石装备
    public static final RegistryObject<Item> EMERALD_HELMET = ITEMS.register("emerald_helmet",
            () -> new EmeraldArmorItem(EmeraldArmorMaterial.EMERALD, ArmorItem.Type.HELMET,
                    new Item.Properties()));

    public static final RegistryObject<Item> EMERALD_CHESTPLATE = ITEMS.register("emerald_chestplate",
            () -> new EmeraldArmorItem(EmeraldArmorMaterial.EMERALD, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties()));

    public static final RegistryObject<Item> EMERALD_LEGGINGS = ITEMS.register("emerald_leggings",
            () -> new EmeraldArmorItem(EmeraldArmorMaterial.EMERALD, ArmorItem.Type.LEGGINGS,
                    new Item.Properties()));

    public static final RegistryObject<Item> EMERALD_BOOTS = ITEMS.register("emerald_boots",
            () -> new EmeraldArmorItem(EmeraldArmorMaterial.EMERALD, ArmorItem.Type.BOOTS,
                    new Item.Properties()));
                    
    public static final RegistryObject<Item> ABOMINABLE_GREATSABER = ITEMS.register("abominable_greatsaber",
            () -> new AbominableGreatsaber(Tiers.NETHERITE, 9, -2.4F,
                    new Item.Properties().durability(3000)));

    public static final RegistryObject<Item> VOID_ASH_BLADE = ITEMS.register("void_ash_blade",
            () -> new VoidAshBlade(Tiers.NETHERITE, 12, -2.6F,
                    new Item.Properties().durability(2200)));
                    
    public static final RegistryObject<Item> SUN_GOD_EYE = ITEMS.register("sun_god_eye",
            () -> new SunGodEye(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
            
    public static final RegistryObject<Item> LAVA_NECKLACE = ITEMS.register("lava_necklace",
            () -> new LavaNecklace(new Item.Properties().rarity(Rarity.RARE).stacksTo(1).fireResistant()));
            
    public static final RegistryObject<Item> LIFE_RING = ITEMS.register("life_ring",
            () -> new LifeRing(new Item.Properties().rarity(Rarity.RARE).stacksTo(1)));
            
    public static final RegistryObject<Item> FIRE_GOD_CORE = ITEMS.register("fire_god_core",
            () -> new FireGodCore(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    
    public static final RegistryObject<Item> ENDER_BELT = ITEMS.register("ender_belt",
            () -> new EnderBelt(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
            
    public static final RegistryObject<Item> EVIL_BLADE_SWORD = ITEMS.register("evil_blade_sword",
            () -> new EvilBladeSword(Tiers.NETHERITE, 10, -2.4F,
                    new Item.Properties().durability(3000)));
                    
    public static final RegistryObject<Item> ROYAL_GREATSWORD = ITEMS.register("royal_greatsword",
            () -> new RoyalGreatsword(Tiers.NETHERITE, 15, -2.4F,
                    new Item.Properties().durability(3000)));
                    
    public static final RegistryObject<Item> MAGIC_SWORD = ITEMS.register("magic_sword",
            () -> new MagicSword(Tiers.NETHERITE, 5, -2.4F,
                    new Item.Properties().durability(2000).rarity(Rarity.RARE)));
                    
    public static final RegistryObject<Item> SUN_GOD_STAFF = ITEMS.register("sun_god_staff",
            () -> new SunGodStaff(new Item.Properties().durability(3000).rarity(Rarity.EPIC).fireResistant()));

    public static final RegistryObject<Item> ZOMBIE_MINER_SPAWN_EGG = ITEMS.register("zombie_miner_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.ZOMBIE_MINER, 0x00AFAF, 0x799C65,
                    new Item.Properties()));

    public static final RegistryObject<Item> SKELETON_MINER_SPAWN_EGG = ITEMS.register("skeleton_miner_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.SKELETON_MINER, 0xC1C1C1, 0x494949,
                    new Item.Properties()));

    public static final RegistryObject<Item> MYSTERIOUS_MERCHANT_SPAWN_EGG = ITEMS.register("mysterious_merchant_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.MYSTERIOUS_MERCHANT, 0x8B4513, 0xFFD700,
                    new Item.Properties()));

    public static final RegistryObject<Item> GRINDER = ITEMS.register("grinder",
            () -> new BlockItem(ModBlocks.GRINDER.get(), new Item.Properties()));

    public static final RegistryObject<Item> NETHERITE_POWDER = ITEMS.register("netherite_powder",
            () -> new PowderItem(new Item.Properties(), "netherite"));

    public static final RegistryObject<Item> DIAMOND_POWDER = ITEMS.register("diamond_powder",
            () -> new PowderItem(new Item.Properties(), "diamond"));

    public static final RegistryObject<Item> EMERALD_POWDER = ITEMS.register("emerald_powder",
            () -> new PowderItem(new Item.Properties(), "emerald"));

    public static final RegistryObject<Item> GOLD_POWDER = ITEMS.register("gold_powder",
            () -> new PowderItem(new Item.Properties(), "gold"));

    public static final RegistryObject<Item> IRON_POWDER = ITEMS.register("iron_powder",
            () -> new PowderItem(new Item.Properties(), "iron"));

    public static final RegistryObject<Item> COPPER_POWDER = ITEMS.register("copper_powder",
            () -> new PowderItem(new Item.Properties(), "copper"));

}