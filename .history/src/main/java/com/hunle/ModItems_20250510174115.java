package com.huanle;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    // 创建物品注册器
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, HunleMod.MOD_ID);

    // 注册终焉剑
    public static final RegistryObject<Item> HUNLE_SWORD = ITEMS.register("hunle_sword",
            () -> new HunleSword(Tiers.NETHERITE, 20, -2.4F,
                    new Item.Properties().durability(2000)));
                    
    // 注册投掷斧头
    public static final RegistryObject<Item> THROWING_AXE = ITEMS.register("throwing_axe",
            () -> new ThrowingAxeItem(Tiers.NETHERITE, 8.0F, -3.0F,
                    new Item.Properties().durability(1500)));
}