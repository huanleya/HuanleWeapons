package com.huanle;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HunleMod.MOD_ID)
public class EventHandler {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        // 检查是否是实体攻击
        if (event.getSource().getEntity() instanceof LivingEntity) {
            LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
            
            // 获取攻击者的主手物品
            ItemStack weapon = attacker.getMainHandItem();
            
            // 检查物品是否有附雷附魔
            int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.LIGHTNING_STRIKE.get(), weapon);
            
            // 如果有附魔，触发附魔效果
            if (level > 0 && event.getEntity() instanceof LivingEntity) {
                LivingEntity target = (LivingEntity) event.getEntity();
                LightningEnchantment.onHitEntity(target, attacker, weapon, level);
            }
        }
    }
}