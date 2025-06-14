package com.huanle;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;

public class FearEffect extends MobEffect {
    private static final String PREVIOUS_HEALTH_KEY = "FearEffectPreviousHealth";
    private static final String PREVIOUS_FOOD_LEVEL_KEY = "FearEffectPreviousFoodLevel";
    private static final String PREVIOUS_SATURATION_KEY = "FearEffectPreviousSaturation";
    
    public FearEffect() {
        super(MobEffectCategory.HARMFUL, 0x4A0069); // 深紫色，表示恐惧效果
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        // 移除任何生命恢复效果
        if (entity.hasEffect(MobEffects.REGENERATION)) {
            entity.removeEffect(MobEffects.REGENERATION);
        }
        
        // 移除瞬间治疗效果
        if (entity.hasEffect(MobEffects.HEAL)) {
            entity.removeEffect(MobEffects.HEAL);
        }
        
        // 检测并阻止任何形式的生命恢复
        float currentHealth = entity.getHealth();
        float previousHealth = entity.getPersistentData().contains(PREVIOUS_HEALTH_KEY) ? 
                              entity.getPersistentData().getFloat(PREVIOUS_HEALTH_KEY) : currentHealth;
        
        // 如果生命值增加了，说明有某种形式的生命恢复发生
        if (currentHealth > previousHealth) {
            // 将生命值恢复到之前的值，有效阻止任何形式的生命恢复
            entity.setHealth(previousHealth);
        }
        
        // 保存当前生命值，用于下一次检查
        entity.getPersistentData().putFloat(PREVIOUS_HEALTH_KEY, entity.getHealth());

        // 如果是玩家，确保饥饿值回血被阻止
        if (entity instanceof Player player) {
            // 这里不再需要使用凋零效果，因为我们直接检测并阻止了生命值的增加
            
            // 如果效果等级是II级或更高，阻止饱食度恢复
            if (amplifier >= 1) { // amplifier 0表示I级，1表示II级，以此类推
                FoodData foodData = player.getFoodData();
                // 保存当前的饱食度值，防止在同一tick内增加
                if (!player.getPersistentData().contains(PREVIOUS_FOOD_LEVEL_KEY)) {
                    player.getPersistentData().putInt(PREVIOUS_FOOD_LEVEL_KEY, foodData.getFoodLevel());
                    player.getPersistentData().putFloat(PREVIOUS_SATURATION_KEY, foodData.getSaturationLevel());
                }
                
                int previousFoodLevel = player.getPersistentData().getInt(PREVIOUS_FOOD_LEVEL_KEY);
                float previousSaturation = player.getPersistentData().getFloat(PREVIOUS_SATURATION_KEY);
                
                // 如果饱食度增加了，将其恢复到之前的值
                if (foodData.getFoodLevel() > previousFoodLevel) {
                    foodData.setFoodLevel(previousFoodLevel);
                }
                
                // 如果饱和度增加了，将其恢复到之前的值
                if (foodData.getSaturationLevel() > previousSaturation) {
                    foodData.setSaturation(previousSaturation);
                }
                
                // 更新保存的值
                player.getPersistentData().putInt(PREVIOUS_FOOD_LEVEL_KEY, foodData.getFoodLevel());
                player.getPersistentData().putFloat(PREVIOUS_SATURATION_KEY, foodData.getSaturationLevel());
            }
        }

        // 添加恐惧粒子效果
        if (entity.level().isClientSide) {
            for (int i = 0; i < 2 + amplifier; i++) {
                double offsetX = entity.getRandom().nextDouble() * 0.6 - 0.3;
                double offsetY = entity.getRandom().nextDouble() * 1.8;
                double offsetZ = entity.getRandom().nextDouble() * 0.6 - 0.3;

                entity.level().addParticle(
                    ParticleTypes.SMOKE, // 使用烟雾粒子表示恐惧
                    entity.getX() + offsetX,
                    entity.getY() + offsetY,
                    entity.getZ() + offsetZ,
                    0, 0, 0
                );
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        // 每tick都应用效果以确保持续阻止生命恢复
        return true;
    }

    @Override
    public String getDescriptionId() {
        return "effect.huanle.fear";
    }
    
    @Override
    public void addAttributeModifiers(LivingEntity entity, net.minecraft.world.entity.ai.attributes.AttributeMap attributeMap, int amplifier) {
        super.addAttributeModifiers(entity, attributeMap, amplifier);
        // 保存当前生命值，用于后续检测生命值是否增加
        if (entity instanceof Player player) {
            player.getPersistentData().putFloat(PREVIOUS_HEALTH_KEY, player.getHealth());
            
            // 如果是II级或更高，保存当前饱食度数据
            if (amplifier >= 1) {
                FoodData foodData = player.getFoodData();
                player.getPersistentData().putInt(PREVIOUS_FOOD_LEVEL_KEY, foodData.getFoodLevel());
                player.getPersistentData().putFloat(PREVIOUS_SATURATION_KEY, foodData.getSaturationLevel());
            }
        }
    }
    
    @Override
    public void removeAttributeModifiers(LivingEntity entity, net.minecraft.world.entity.ai.attributes.AttributeMap attributeMap, int amplifier) {
        super.removeAttributeModifiers(entity, attributeMap, amplifier);
        // 清除保存的数据
        if (entity instanceof Player player) {
            player.getPersistentData().remove(PREVIOUS_HEALTH_KEY);
            
            // 如果是II级或更高，清除饱食度数据
            if (amplifier >= 1) {
                player.getPersistentData().remove(PREVIOUS_FOOD_LEVEL_KEY);
                player.getPersistentData().remove(PREVIOUS_SATURATION_KEY);
            }
        }
    }
}
