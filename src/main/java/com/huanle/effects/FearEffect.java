package com.huanle.effects;

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
        super(MobEffectCategory.HARMFUL, 0x4A0069);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.hasEffect(MobEffects.REGENERATION)) {
            entity.removeEffect(MobEffects.REGENERATION);
        }
        if (entity.hasEffect(MobEffects.HEAL)) {
            entity.removeEffect(MobEffects.HEAL);
        }
        float currentHealth = entity.getHealth();
        float previousHealth = entity.getPersistentData().contains(PREVIOUS_HEALTH_KEY) ? 
                              entity.getPersistentData().getFloat(PREVIOUS_HEALTH_KEY) : currentHealth;
        if (currentHealth > previousHealth) {
            entity.setHealth(previousHealth);
        }

        entity.getPersistentData().putFloat(PREVIOUS_HEALTH_KEY, entity.getHealth());
        if (entity instanceof Player player) {
            if (amplifier >= 1) {
                FoodData foodData = player.getFoodData();
                if (!player.getPersistentData().contains(PREVIOUS_FOOD_LEVEL_KEY)) {
                    player.getPersistentData().putInt(PREVIOUS_FOOD_LEVEL_KEY, foodData.getFoodLevel());
                    player.getPersistentData().putFloat(PREVIOUS_SATURATION_KEY, foodData.getSaturationLevel());
                }
                
                int previousFoodLevel = player.getPersistentData().getInt(PREVIOUS_FOOD_LEVEL_KEY);
                float previousSaturation = player.getPersistentData().getFloat(PREVIOUS_SATURATION_KEY);
                if (foodData.getFoodLevel() > previousFoodLevel) {
                    foodData.setFoodLevel(previousFoodLevel);
                }
                if (foodData.getSaturationLevel() > previousSaturation) {
                    foodData.setSaturation(previousSaturation);
                }
                player.getPersistentData().putInt(PREVIOUS_FOOD_LEVEL_KEY, foodData.getFoodLevel());
                player.getPersistentData().putFloat(PREVIOUS_SATURATION_KEY, foodData.getSaturationLevel());
            }
        }
        if (entity.level().isClientSide) {
            for (int i = 0; i < 2 + amplifier; i++) {
                double offsetX = entity.getRandom().nextDouble() * 0.6 - 0.3;
                double offsetY = entity.getRandom().nextDouble() * 1.8;
                double offsetZ = entity.getRandom().nextDouble() * 0.6 - 0.3;

                entity.level().addParticle(
                    ParticleTypes.SMOKE,
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
        return true;
    }

    @Override
    public String getDescriptionId() {
        return "effect.huanle.fear";
    }
    
    @Override
    public void addAttributeModifiers(LivingEntity entity, net.minecraft.world.entity.ai.attributes.AttributeMap attributeMap, int amplifier) {
        super.addAttributeModifiers(entity, attributeMap, amplifier);
        if (entity instanceof Player player) {
            player.getPersistentData().putFloat(PREVIOUS_HEALTH_KEY, player.getHealth());
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
        if (entity instanceof Player player) {
            player.getPersistentData().remove(PREVIOUS_HEALTH_KEY);
            if (amplifier >= 1) {
                player.getPersistentData().remove(PREVIOUS_FOOD_LEVEL_KEY);
                player.getPersistentData().remove(PREVIOUS_SATURATION_KEY);
            }
        }
    }
}
