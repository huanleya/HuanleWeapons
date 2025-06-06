package com.huanle;

import javax.annotation.Nonnull;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.core.particles.ParticleTypes;

public class FreezingEffect extends MobEffect {
    private static final double MOVEMENT_PENALTY_PER_LEVEL = 0.3; // 30% slowdown per level
    private static final float MAGIC_DAMAGE_PER_TICK = 0.5f / 20f; // 0.5 damage per second (divided by 20 ticks)

    public FreezingEffect() {
        super(MobEffectCategory.HARMFUL, 0x77BDFD); // Blue color for freezing effect
        
        // Add movement speed attribute modifier
        this.addAttributeModifier(
            Attributes.MOVEMENT_SPEED, 
            "3121ce83-3a97-4e38-a74d-a3e15c2f7a16", 
            -MOVEMENT_PENALTY_PER_LEVEL, 
            AttributeModifier.Operation.MULTIPLY_TOTAL
        );
    }

    @Override
    public void applyEffectTick(@Nonnull LivingEntity entity, int amplifier) {
        // Apply magic damage each tick
        float damage = MAGIC_DAMAGE_PER_TICK * (amplifier + 1);
        entity.hurt(entity.damageSources().magic(), damage);
        
        // Add visual ice particles
        if (entity.level().isClientSide) {
            for (int i = 0; i < 5; i++) {
                double offsetX = entity.getRandom().nextDouble() * 0.6 - 0.3;
                double offsetY = entity.getRandom().nextDouble() * 1.8;
                double offsetZ = entity.getRandom().nextDouble() * 0.6 - 0.3;
                
                entity.level().addParticle(
                    ParticleTypes.SNOWFLAKE,
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
        // Apply effect every tick
        return true;
    }
    
    @Override
    public String getDescriptionId() {
        return "effect.huanle.freezing";
    }
}
