package com.huanle.entities;

import com.huanle.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import java.util.List;


public class FireballEntity extends AbstractHurtingProjectile {
    
    private static final float DAMAGE = 8.0F;
    private static final float EXPLOSION_POWER = 2.5F;
    private static final int BURN_TIME = 5;
    
    public FireballEntity(EntityType<? extends FireballEntity> entityType, Level level) {
        super(entityType, level);
    }
    
    public FireballEntity(Level level, LivingEntity shooter, double accelX, double accelY, double accelZ) {
        super(ModEntities.FIREBALL.get(), shooter, accelX, accelY, accelZ, level);
        if (ModEntities.FIREBALL.get() == null) {
            throw new IllegalStateException("FIREBALL EntityType is not registered!");
        }
    }
    
    public FireballEntity(Level level, double x, double y, double z, double accelX, double accelY, double accelZ) {
        super(ModEntities.FIREBALL.get(), x, y, z, accelX, accelY, accelZ, level);
    }
    
    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        
        if (!this.level().isClientSide) {
            if (hitResult.getEntity() instanceof LivingEntity target) {
                Entity owner = this.getOwner();
                if (owner instanceof LivingEntity livingOwner) {
                    target.hurt(this.damageSources().mobProjectile(this, livingOwner), DAMAGE);
                } else {
                    target.hurt(this.damageSources().magic(), DAMAGE);
                }
                target.setSecondsOnFire(BURN_TIME);
            }
            createProtectedExplosion(this.getX(), this.getY(), this.getZ(), EXPLOSION_POWER);
            this.discard();
        }
    }
    
    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        
        if (!this.level().isClientSide) {
            createProtectedExplosion(this.getX(), this.getY(), this.getZ(), EXPLOSION_POWER);
            this.discard();
        }
    }
    
    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            for (int i = 0; i < 3; i++) {
                double offsetX = this.random.nextGaussian() * 0.02;
                double offsetY = this.random.nextGaussian() * 0.02;
                double offsetZ = this.random.nextGaussian() * 0.02;
                
                this.level().addParticle(
                    ParticleTypes.FLAME,
                    this.getX() + offsetX,
                    this.getY() + 0.5 + offsetY,
                    this.getZ() + offsetZ,
                    0.0, 0.0, 0.0
                );
            }
            if (this.random.nextInt(3) == 0) {
                this.level().addParticle(
                    ParticleTypes.SMOKE,
                    this.getX(),
                    this.getY() + 0.5,
                    this.getZ(),
                    0.0, 0.0, 0.0
                );
            }
        }
        if (this.tickCount > 200) {
            this.discard();
        }
    }
    
    @Override
    protected boolean shouldBurn() {
        return false;
    }
    
    @Override
    public boolean isOnFire() {
        return false;
    }

    private void createProtectedExplosion(double x, double y, double z, float power) {
        AABB explosionArea = new AABB(
            x - power, y - power, z - power,
            x + power, y + power, z + power
        );
        
        List<Entity> entitiesInRange = this.level().getEntities(this, explosionArea);
        List<Entity> protectedEntities = entitiesInRange.stream()
            .filter(entity -> entity instanceof ItemFrame || entity instanceof ArmorStand)
            .toList();
        protectedEntities.forEach(entity -> entity.setInvulnerable(true));
        this.level().explode(this, x, y, z, power, Level.ExplosionInteraction.NONE);
        protectedEntities.forEach(entity -> entity.setInvulnerable(false));
    }
}