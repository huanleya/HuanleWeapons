package com.huanle;

import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HuanleMod.MOD_ID)
public class VillagerTradeHandler {
    @SubscribeEvent
    public static void onVillagerTrades(VillagerTradesEvent event) {
        // 预留给未来添加其他交易
    }
} 