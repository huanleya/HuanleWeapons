package com.huanle;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import net.minecraft.util.RandomSource;

@Mod.EventBusSubscriber(modid = HuanleMod.MOD_ID)
public class VillagerTradeHandler {

    @SubscribeEvent
    public static void onVillagerTradesEvent(VillagerTradesEvent event) {
        // 只为图书管理员添加熔岩行者附魔书交易
        if (event.getType() == VillagerProfession.LIBRARIAN) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            
            // 添加到专家级别（4级）交易
            trades.get(4).add(new EnchantedBookForEmeraldsTrade(ModEnchantments.LAVA_WALKER.get(), 1, 15, 30));
            
            // 添加到大师级别（5级）交易，更高级别的附魔
            trades.get(5).add(new EnchantedBookForEmeraldsTrade(ModEnchantments.LAVA_WALKER.get(), 2, 30, 15));
        }
    }
    
    /**
     * 自定义交易：用绿宝石换取指定附魔的附魔书
     */
    private static class EnchantedBookForEmeraldsTrade implements VillagerTrades.ItemListing {
        private final net.minecraft.world.item.enchantment.Enchantment enchantment;
        private final int level;
        private final int emeraldCost;
        private final int maxUses;
        private final float priceMultiplier;
        
        public EnchantedBookForEmeraldsTrade(net.minecraft.world.item.enchantment.Enchantment enchantment, int level, int emeraldCost, int maxUses) {
            this.enchantment = enchantment;
            this.level = level;
            this.emeraldCost = emeraldCost;
            this.maxUses = maxUses;
            this.priceMultiplier = 0.2F;
        }
        
        @Override
        public MerchantOffer getOffer(net.minecraft.world.entity.Entity trader, RandomSource rand) {
            // 创建附魔书
            ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);
            EnchantmentHelper.setEnchantments(
                java.util.Collections.singletonMap(enchantment, level),
                enchantedBook
            );
            
            // 返回交易选项
            return new MerchantOffer(
                new ItemStack(Items.EMERALD, emeraldCost),
                ItemStack.EMPTY,
                enchantedBook,
                maxUses,
                5, // 经验值
                priceMultiplier
            );
        }
    }
}