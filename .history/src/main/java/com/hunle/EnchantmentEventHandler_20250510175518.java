package com.huanle;

import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@Mod.EventBusSubscriber(modid = HunleMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EnchantmentEventHandler {

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // 修改忠诚附魔的适用范围，使其也能应用于投掷斧头
            try {
                // 获取忠诚附魔的类别字段
                Field categoryField = Enchantments.LOYALTY.getClass().getDeclaredField("category");
                categoryField.setAccessible(true);
                
                // 移除final修饰符
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(categoryField, categoryField.getModifiers() & ~Modifier.FINAL);
                
                // 创建一个新的附魔类别，包括三叉戟和投掷斧头
                EnchantmentCategory newCategory = EnchantmentCategory.create(
                        "trident_and_throwing_axe",
                        item -> EnchantmentCategory.TRIDENT.canEnchant(item) || item instanceof ThrowingAxeItem
                );
                
                // 设置新的附魔类别
                categoryField.set(Enchantments.LOYALTY, newCategory);
            } catch (Exception e) {
                // 记录错误
                System.err.println("Failed to modify Loyalty enchantment category: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}