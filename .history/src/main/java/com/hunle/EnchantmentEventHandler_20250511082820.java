package com.huanle;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.enchanting.EnchantmentLevelSetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HunleMod.MOD_ID)
public class EnchantmentEventHandler {

    // 使用EnchantmentLevelSetEvent来处理附魔修改
    @SubscribeEvent
    public static void onEnchantmentModify(EnchantmentLevelSetEvent event) {
        ItemStack stack = event.getItem();
        
        // 获取 EnchantmentInstance
        EnchantmentInstance enchantmentInstance = event.getEnchantmentInstance();
        if (enchantmentInstance != null) {
            Enchantment enchantment = enchantmentInstance.getEnchantment();
            
            // 如果物品是投掷斧头，并且正在修改忠诚附魔
            if (stack.getItem() instanceof ThrowingAxeItem && enchantment == Enchantments.LOYALTY) {
                // 确保忠诚附魔可以应用于投掷斧头
                int level = event.getEnchantmentLevel();
                if (level <= 0) {
                    // 如果当前没有附魔等级，设置为1
                    event.setEnchantmentLevel(1);
                } else if (level > 3) {
                    // 限制最高等级为3
                    event.setEnchantmentLevel(3);
                }
                // 否则保持当前等级不变
            }
        }
    }
}