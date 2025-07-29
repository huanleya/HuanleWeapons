package com.huanle;

import com.huanle.effects.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, HuanleMod.MOD_ID);

    public static final RegistryObject<MobEffect> FREEZING = EFFECTS.register("freezing", FreezingEffect::new);
    public static final RegistryObject<MobEffect> BLEEDING = EFFECTS.register("bleeding", BleedingEffect::new);
    public static final RegistryObject<MobEffect> FEAR = EFFECTS.register("fear", FearEffect::new);
    public static final RegistryObject<MobEffect> ENDER_RESISTANCE = EFFECTS.register("ender_resistance", EnderResistanceEffect::new);
    public static final RegistryObject<MobEffect> STUN = EFFECTS.register("stun", StunEffect::new);
    public static final RegistryObject<MobEffect> FROSTY = EFFECTS.register("frosty", FrostyEffect::new);
}
