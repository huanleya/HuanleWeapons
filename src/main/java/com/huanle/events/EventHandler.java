package com.huanle.events;

import com.huanle.HuanleMod;
import com.huanle.ModEffects;
import com.huanle.ModEnchantments;
import com.huanle.enchantments.LightningEnchantment;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
import net.minecraft.world.entity.EntityType;

@Mod.EventBusSubscriber(modid = HuanleMod.MOD_ID)
public class EventHandler {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity livingEntity = null;
        if (event.getEntity() instanceof LivingEntity) {
            livingEntity = (LivingEntity) event.getEntity();
        }
        if (livingEntity != null) {
            if (livingEntity.hasEffect(ModEffects.ENDER_RESISTANCE.get())) {
                if (event.getSource().is(DamageTypes.DRAGON_BREATH) || 
                    (event.getSource().getDirectEntity() != null && 
                     event.getSource().getDirectEntity().getType().getDescriptionId().equals("entity.minecraft.ender_dragon"))) {
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


    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.hasEffect(ModEffects.FEAR.get())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onEnderPearlTeleport(EntityTeleportEvent.EnderPearl event) {
        if (event.getEntity() instanceof LivingEntity livingEntity) {
            if (livingEntity.hasEffect(ModEffects.ENDER_RESISTANCE.get())) {
                event.setAttackDamage(0.0F);
            }
        }
    }
}
