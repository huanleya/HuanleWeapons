package com.huanle;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HunleMod.MOD_ID)
public class EnchantmentEventHandler {

    // 使用事件监听器来检查物品是否可以接受忠诚附魔，而不是使用反射修改附魔类别
    @SubscribeEvent
    public static void onEnchantmentLevelCheck(PlayerEvent.ItemEnchantmentLevelCheck event) {
        ItemStack stack = event.getItemStack();
        
        // 如果是检查忠诚附魔，并且物品是投掷斧头
        if (event.getEnchantment() == Enchantments.LOYALTY && stack.getItem() instanceof ThrowingAxeItem) {
            // 允许忠诚附魔应用于投掷斧头
            // 返回1-3之间的值，表示可以接受的附魔等级
            int currentLevel = event.getEnchantmentLevel();
            if (currentLevel <= 0) {
                // 如果当前没有附魔等级，设置为1
                event.setEnchantmentLevel(1);
            } else if (currentLevel > 3) {
                // 限制最高等级为3
                event.setEnchantmentLevel(3);
            }
            // 否则保持当前等级不变
        }
    }
}