package com.huanle;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = HuanleMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCreativeTabs {
    
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = 
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HuanleMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> HUANLE_TAB = CREATIVE_MODE_TABS.register("huanle_tab",
        () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.huanle_tab"))
            .icon(() -> new ItemStack(ModItems.HUANLE_SWORD.get()))
            .displayItems((parameters, output) -> {
                output.accept(ModItems.SUN_GOD_SWORD.get());
                output.accept(ModItems.HUANLE_SWORD.get());
                output.accept(ModItems.ENDER_SWORD.get());
                output.accept(ModItems.GREEN_DRAGON_SWORD.get());
                output.accept(ModItems.MOONLIGHT_SWORD.get());
                output.accept(ModItems.MOON_CRESCENT_SWORD.get());
                output.accept(ModItems.BLUE_FLAME_SWORD.get());
                output.accept(ModItems.LAVA_SWORD.get());
                output.accept(ModItems.FIRE_GOD_DAGGER.get());
                output.accept(ModItems.LOOTING_DAGGER.get());
                output.accept(ModItems.FOREST_BLADE.get());
                output.accept(ModItems.DEATH_SCYTHE.get());
                output.accept(ModItems.THROWING_AXE.get());
                output.accept(ModItems.ENDER_HAMMER.get());
                output.accept(ModItems.FLAME_BOW.get());
                output.accept(ModItems.FLAME_ARROW.get());
                output.accept(ModItems.LIFE_STAFF.get());
            })
            .build());
            
    public static final RegistryObject<CreativeModeTab> HUANLE_MATERIALS_TAB = CREATIVE_MODE_TABS.register("huanle_materials_tab",
        () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.huanle_materials_tab"))
            .icon(() -> new ItemStack(ModItems.FIRE_GOD_INGOT.get()))
            .displayItems((parameters, output) -> {
                output.accept(ModItems.LAVA_INGOT.get());
                output.accept(ModItems.FIRE_GOD_INGOT.get());
                output.accept(ModItems.MAGIC_CRYSTAL.get());
                output.accept(ModBlockItems.MAGIC_CRYSTAL_ORE_ITEM.get());
                output.accept(ModBlockItems.DEEPSLATE_MAGIC_CRYSTAL_ORE_ITEM.get());
                output.accept(ModBlockItems.MAGIC_CRYSTAL_BLOCK_ITEM.get());
            })
            .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}