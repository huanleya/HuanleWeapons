package com.huanle;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.enchanting.EnchantmentLevelSetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HuanleMod.MOD_ID)
public class EnchantmentEventHandler {


    @SubscribeEvent
    public static void onEnchantmentModify(EnchantmentLevelSetEvent event) {
        ItemStack stack = event.getItem();

    }
}