package com.huanle;

import com.huanle.enchantments.*;
import com.huanle.enchantments.ManaMasteryEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEnchantments {

    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(
            ForgeRegistries.ENCHANTMENTS, HuanleMod.MOD_ID);


    public static final RegistryObject<Enchantment> LIGHTNING_STRIKE = ENCHANTMENTS.register(
            "lightning_strike", LightningEnchantment::new);

    public static final RegistryObject<Enchantment> QUICK_DRAW = ENCHANTMENTS.register(
            "quick_draw", QuickDrawEnchantment::new);

    public static final RegistryObject<Enchantment> MANA_MASTERY = ENCHANTMENTS.register(
            "mana_mastery", ManaMasteryEnchantment::new);

    public static void register(IEventBus eventBus) {
        ENCHANTMENTS.register(eventBus);
    }
}