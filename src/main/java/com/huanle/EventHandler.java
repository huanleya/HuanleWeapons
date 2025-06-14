package com.huanle;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HuanleMod.MOD_ID)
public class EventHandler {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {

        if (event.getSource().getEntity() instanceof LivingEntity) {
            LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
            

            ItemStack mainHandWeapon = attacker.getMainHandItem();
            ItemStack offHandWeapon = attacker.getOffhandItem();
            

            int mainHandLevel = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.LIGHTNING_STRIKE.get(), mainHandWeapon);

            int offHandLevel = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.LIGHTNING_STRIKE.get(), offHandWeapon);

            if (event.getEntity() instanceof LivingEntity) {
                LivingEntity target = (LivingEntity) event.getEntity();
                if (mainHandLevel > 0) {
                    LightningEnchantment.onHitEntity(target, attacker, mainHandWeapon, mainHandLevel);
                }
                if (offHandLevel > 0) {
                    LightningEnchantment.onHitEntity(target, attacker, offHandWeapon, offHandLevel);
                }
            }
        }
    }
    
    /**
     * 处理生命恢复事件，当实体有恐惧效果时取消生命恢复
     */
    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event) {
        LivingEntity entity = event.getEntity();
        
        // 检查实体是否有恐惧效果
        if (entity.hasEffect(ModEffects.FEAR.get())) {
            // 取消生命恢复事件
            event.setCanceled(true);
        }
    }
}