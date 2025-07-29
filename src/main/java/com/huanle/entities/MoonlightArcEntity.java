package com.huanle.entities;

import com.huanle.ModEffects;
import com.huanle.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class MoonlightArcEntity extends AbstractArrow {
    private static final double CURVE_FACTOR = 0.05;
    public int age;
    private Vec3 startPos;
    private double initialYaw;

    public MoonlightArcEntity(EntityType<? extends MoonlightArcEntity> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
        this.setPierceLevel((byte) 1);
        this.setBaseDamage(5.0D);
    }

    public MoonlightArcEntity(Level level, LivingEntity shooter) {
        super(ModEntities.MOONLIGHT_ARC.get(), shooter, level);
        this.setNoGravity(true);
        this.setPierceLevel((byte) 1);
        this.setBaseDamage(5.0D);
        this.startPos = shooter.position();
        this.initialYaw = shooter.getYRot();
        this.pickup = Pickup.DISALLOWED;
    }

    @Override
    public void tick() {
        super.tick();
        age++;

        if (age > 60) {
            this.discard();
            return;
        }

        if (this.getDeltaMovement().length() > 0.01 && age < 40) {
            double curveFactor = CURVE_FACTOR * Math.sin(age * 0.2);
            Vec3 motion = this.getDeltaMovement();

            float yaw = this.getYRot();
            Vec3 sideVector = new Vec3(
                    Math.cos(Math.toRadians(yaw)), 
                    0, 
                    Math.sin(Math.toRadians(yaw))
            ).cross(new Vec3(0, 1, 0)).normalize();

            Vec3 newMotion = motion.add(sideVector.scale(curveFactor));
            this.setDeltaMovement(newMotion);
        }

        if (this.level().isClientSide) {
            for (int i = 0; i < 3; i++) {
                double offsetX = this.random.nextDouble() * 0.2 - 0.1;
                double offsetY = this.random.nextDouble() * 0.2 - 0.1;
                double offsetZ = this.random.nextDouble() * 0.2 - 0.1;
                
                this.level().addParticle(
                    ParticleTypes.SOUL_FIRE_FLAME,
                    this.getX() + offsetX,
                    this.getY() + offsetY,
                    this.getZ() + offsetZ,
                    0, 0, 0
                );
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        
        if (hitResult.getEntity() instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(new MobEffectInstance(ModEffects.FREEZING.get(), 200, 0));
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }
}
