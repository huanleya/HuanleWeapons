package com.huanle;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModPotions {
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, HuanleMod.MOD_ID);

    public static final RegistryObject<Potion> FEAR_POTION = POTIONS.register("fear_potion",
            () -> new Potion(new MobEffectInstance(ModEffects.FEAR.get(), 3600, 0)));

    public static final RegistryObject<Potion> LONG_FEAR_POTION = POTIONS.register("long_fear_potion",
            () -> new Potion(new MobEffectInstance(ModEffects.FEAR.get(), 7200, 0)));

    public static final RegistryObject<Potion> STRONG_FEAR_POTION = POTIONS.register("strong_fear_potion",
            () -> new Potion(new MobEffectInstance(ModEffects.FEAR.get(), 1200, 1)));

    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }
}
