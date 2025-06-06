package com.huanle;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HuanleMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCreativeTabs {
    
    @SubscribeEvent
    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        // 将物品添加到战斗标签页
        if (event.getTabKey() == CreativeModeTab.TAB_COMBAT) {
            event.accept(ModItems.HUNLE_SWORD.get());
            event.accept(ModItems.THROWING_AXE.get());
        }
    }
} 