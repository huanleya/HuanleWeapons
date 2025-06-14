package com.huanle;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;
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

    public static final RegistryObject<Item> FLAME_GOD_INGOT = ITEMS.register("flame_god_ingot",
            () -> new Item(new Item.Properties().fireResistant()));

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
            
    public static final RegistryObject<Item> FEAR_TEAR = ITEMS.register("fear_tear",
            () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));
}