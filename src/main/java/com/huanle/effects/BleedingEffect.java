package com.huanle.effects;

import javax.annotation.Nonnull;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.core.particles.ParticleTypes;

public class BleedingEffect extends MobEffect {
    private static final float DAMAGE_PER_TICK = 0.5f / 20f;

    public BleedingEffect() {
        super(MobEffectCategory.HARMFUL, 0xAA0000);
    }

    @Override
    public void applyEffectTick(@Nonnull LivingEntity entity, int amplifier) {
        float damage = DAMAGE_PER_TICK * (amplifier + 1);
        entity.hurt(entity.damageSources().magic(), damage);
        if (entity.level().isClientSide) {
            for (int i = 0; i < 3; i++) {
                double offsetX = entity.getRandom().nextDouble() * 0.6 - 0.3;
                double offsetY = entity.getRandom().nextDouble() * 1.8;
                double offsetZ = entity.getRandom().nextDouble() * 0.6 - 0.3;
                
                entity.level().addParticle(
                    ParticleTypes.DRIPPING_LAVA,
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
        return "effect.huanle.bleeding";
    }
}
