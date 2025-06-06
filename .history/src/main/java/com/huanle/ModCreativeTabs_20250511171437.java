package com.huanle;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = HuanleMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCreativeTabs {
    
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = 
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HuanleMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> HUNLE_TAB = CREATIVE_MODE_TABS.register("hunle_tab",
        () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.hunle_tab"))
            .icon(() -> new ItemStack(ModItems.HUNLE_SWORD.get()))
            .displayItems((parameters, output) -> {
                output.accept(ModItems.HUNLE_SWORD.get());
                output.accept(ModItems.THROWING_AXE.get());
            })
            .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
} 