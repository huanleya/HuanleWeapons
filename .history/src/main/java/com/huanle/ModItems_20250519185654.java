package com.huanle;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    // 创建物品注册器
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, HuanleMod.MOD_ID);

    // 注册终焉剑
    public static final RegistryObject<Item> HUANLE_SWORD = ITEMS.register("huanle_sword",
            () -> new HuanleSword(Tiers.NETHERITE, 20, -2.4F,
                    new Item.Properties().durability(2000).hideTooltip()));
                    
    // 注册投掷斧头
    public static final RegistryObject<Item> THROWING_AXE = ITEMS.register("throwing_axe",
            () -> new ThrowingAxeItem(Tiers.NETHERITE, 8.0F, -3.0F,
                    new Item.Properties().durability(1500).hideTooltip()));

    //注册太阳神剑
    public static final RegistryObject<Item> SUN_GOD_SWORD = ITEMS.register("sun_god_sword",
            () -> new SunGodSword(Tiers.NETHERITE, 20, -2.4F,
                    new Item.Properties().durability(2000).hideTooltip()));
                    
    //注册末影之剑
    public static final RegistryObject<Item> ENDER_SWORD = ITEMS.register("ender_sword",
            () -> new EnderSword(Tiers.NETHERITE, 20, -2.4F,
                    new Item.Properties().durability(2000).hideTooltip()));
                    
    //注册烈焰弓
    public static final RegistryObject<Item> FLAME_BOW = ITEMS.register("flame_bow",
            () -> new FlameBowItem(new Item.Properties().durability(500).hideTooltip()));

    //注册烈焰箭
    public static final RegistryObject<Item> FLAME_ARROW = ITEMS.register("flame_arrow",
            () -> new FlameArrowItem(new Item.Properties().hideTooltip()));

}