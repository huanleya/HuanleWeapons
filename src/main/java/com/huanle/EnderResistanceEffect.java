package com.huanle;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.particles.ParticleTypes;

public class EnderResistanceEffect extends MobEffect {
    
    public EnderResistanceEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x9370DB); // 紫色，表示末影抗性效果
    }
    
    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        // 添加末影粒子效果
        if (entity.level().isClientSide) {
            for (int i = 0; i < 3; i++) {
                double offsetX = entity.getRandom().nextDouble() * 0.6 - 0.3;
                double offsetY = entity.getRandom().nextDouble() * 1.8;
                double offsetZ = entity.getRandom().nextDouble() * 0.6 - 0.3;
                
                entity.level().addParticle(
                    ParticleTypes.PORTAL, // 使用末影传送门粒子
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
        // 每10刻更新一次粒子效果
        return duration % 10 == 0;
    }
    
    @Override
    public String getDescriptionId() {
        return "effect.huanle.ender_resistance";
    }
}