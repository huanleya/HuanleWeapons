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

        FMLModContainer modContainer = (FMLModContainer) ModList.get().getModContainerById(MOD_ID).orElseThrow();
        IEventBus modEventBus = modContainer.getEventBus();
        

        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlockItems.register(modEventBus);
        ModEntities.register(modEventBus);
        ModEnchantments.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModEffects.EFFECTS.register(modEventBus);

    }
    

}