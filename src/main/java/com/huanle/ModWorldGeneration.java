package com.huanle;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;



@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = HuanleMod.MOD_ID)

public class ModWorldGeneration {

    public static final DeferredRegister<BiomeModifier> BIOME_MODIFIERS = 
        DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIERS, HuanleMod.MOD_ID);
    

    public static void register(IEventBus eventBus) {
        BIOME_MODIFIERS.register(eventBus);
    }
    

    @SubscribeEvent
    public static void onRegister(RegisterEvent event) {

        event.register(Registries.CONFIGURED_FEATURE, helper -> {

        });
    }


}
