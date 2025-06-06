package com.huanle;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;
import net.minecraftforge.client.model.obj.ObjLoader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(HuanleMod.MOD_ID)
public class HuanleMod {
    public static final String MOD_ID = "huanle";

    public HuanleMod() {
        // 注册物品到模组事件总线
        // 使用 FMLModContainer 获取模组事件总线
        FMLModContainer modContainer = (FMLModContainer) ModList.get().getModContainerById(MOD_ID).orElseThrow();
        IEventBus modEventBus = modContainer.getEventBus();
        
        // 注册物品、实体、附魔和创造模式标签页
        ModItems.ITEMS.register(modEventBus);
        ModEntities.register(modEventBus);
        ModEnchantments.register(modEventBus);
        ModCreativeTabs.register(modEventBus);

        // 注册OBJ模型加载器（仅在客户端）
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ObjLoader.INSTANCE.addDomain(MOD_ID);
        }
    }
}