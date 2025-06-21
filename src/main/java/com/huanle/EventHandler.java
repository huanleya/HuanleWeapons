package com.huanle;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

@Mod.EventBusSubscriber(modid = HuanleMod.MOD_ID)
public class EventHandler {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity livingEntity = null;
        if (event.getEntity() instanceof LivingEntity) {
            livingEntity = (LivingEntity) event.getEntity();
        }

        // 处理末影抗性效果 - 免疫末影龙吐息伤害
        if (livingEntity != null) {
            // 检查是否有末影抗性效果
            if (livingEntity.hasEffect(ModEffects.ENDER_RESISTANCE.get())) {
                // 检查伤害类型是否为末影龙吐息
                if (event.getSource().is(DamageTypes.DRAGON_BREATH) || 
                    (event.getSource().getDirectEntity() != null && 
                     event.getSource().getDirectEntity().getType().getDescriptionId().equals("entity.minecraft.ender_dragon"))) {
                    // 取消伤害事件
                    event.setCanceled(true);
                    return;
                }
            }
        }

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
    
    /**
     * 处理末影珍珠瞬移事件，当实体有末影抗性效果时取消瞬移伤害
     */
    @SubscribeEvent
    public static void onEnderPearlTeleport(EntityTeleportEvent.EnderPearl event) {
        if (event.getEntity() instanceof LivingEntity livingEntity) {
            // 检查是否有末影抗性效果
            if (livingEntity.hasEffect(ModEffects.ENDER_RESISTANCE.get())) {
                // 取消瞬移伤害（将伤害设为0）
                event.setAttackDamage(0.0F);
            }
        }
    }
}