package com.huanle;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.enchanting.EnchantmentLevelSetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HunleMod.MOD_ID)
public class EnchantmentEventHandler {

    // 使用EnchantmentLevelSetEvent来修改附魔等级
    @SubscribeEvent
    public static void onEnchantmentLevelSet(EnchantmentLevelSetEvent event) {
        ItemStack stack = event.getItem();
        
        // 如果物品是投掷斧头，并且正在设置忠诚附魔
        if (stack.getItem() instanceof ThrowingAxeItem && event.getEnchantment() == Enchantments.LOYALTY) {
            // 确保忠诚附魔可以应用于投掷斧头
            int level = event.getLevel();
            if (level <= 0) {
                // 如果当前没有附魔等级，设置为1
                event.setLevel(1);
            } else if (level > 3) {
                // 限制最高等级为3
                event.setLevel(3);
            }
            // 否则保持当前等级不变
        }
    }
}