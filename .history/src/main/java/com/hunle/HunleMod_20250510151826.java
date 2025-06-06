package com.huanle;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;

@Mod(HunleMod.MOD_ID)
public class HunleMod {
    public static final String MOD_ID = "huanle";

    public HunleMod() {
        // 注册物品到模组事件总线
        // 使用 FMLModContainer 获取模组事件总线
        FMLModContainer modContainer = (FMLModContainer) ModList.get().getModContainerById(MOD_ID).orElseThrow();
        IEventBus modEventBus = modContainer.getEventBus();
        ModItems.ITEMS.register(modEventBus);
    }
}
