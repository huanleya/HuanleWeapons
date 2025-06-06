package com.huanle;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEnchantments {
    // 创建一个延迟注册器来注册附魔
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(
            ForgeRegistries.ENCHANTMENTS, HunleMod.MOD_ID);

    // 注册附雷附魔
    public static final RegistryObject<Enchantment> LIGHTNING_STRIKE = ENCHANTMENTS.register(
            "lightning_strike", LightningEnchantment::new);

    // 在模组初始化时调用此方法来注册所有附魔
    public static void register(IEventBus eventBus) {
        ENCHANTMENTS.register(eventBus);
    }
}