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
        if (event.getType() == net.minecraft.world.entity.npc.VillagerProfession.LIBRARIAN) {
            VillagerTrades.ItemListing lavaWalkerBook = (entity, random) -> {
                ItemStack book = EnchantedBookItem.createForEnchantment(
                    new EnchantmentInstance(ModEnchantments.LAVA_WALKER.get(), 1)
                );
                return new MerchantOffer(
                    new ItemStack(Items.EMERALD, 30), // 价格可调整
                    new ItemStack(Items.BOOK),
                    book,
                    5, 30, 0.2F
                );
            };
            event.getTrades().get(5).add(lavaWalkerBook); // 5级图书管理员
        }
    }
} 