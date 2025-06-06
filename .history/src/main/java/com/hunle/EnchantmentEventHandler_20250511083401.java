package com.huanle;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.enchanting.GetEnchantmentLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HunleMod.MOD_ID)
public class EnchantmentEventHandler {

    @SubscribeEvent
    public static void onGetEnchantmentLevel(GetEnchantmentLevelEvent event) {
        ItemStack stack = event.getItem();
        Enchantment enchantment = event.getEnchantment();
        
        if (stack.getItem() instanceof ThrowingAxeItem && 
            enchantment == Enchantments.LOYALTY) {
            // 确保忠诚附魔可以应用于投掷斧头
            int level = event.getEnchantmentLevel();
            if (level <= 0) {
                event.setEnchantmentLevel(1); // 最低等级1
            } else if (level > 3) {
                event.setEnchantmentLevel(3); // 最高等级3
            }
        }
    }
}