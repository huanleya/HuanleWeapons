package com.huanle;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class FlameArrowEntity extends AbstractArrow implements ItemSupplier {
    private static final int DEFAULT_BURN_TIME = 5;
    private final int burnTime = DEFAULT_BURN_TIME;
    private int shakeTime = 0;
    private boolean isInGround = false;
    private int groundEffectTime = 0;


    public FlameArrowEntity(EntityType<? extends FlameArrowEntity> entityType, Level level) {
        super(entityType, level);
        this.setSecondsOnFire(burnTime);
    }


    public FlameArrowEntity(Level level, LivingEntity shooter, ItemStack stack) {
        super(ModEntities.FLAME_ARROW.get(), shooter, level);
        this.setSecondsOnFire(burnTime);
    }


    public FlameArrowEntity(Level level, double x, double y, double z) {
        super(ModEntities.FLAME_ARROW.get(), x, y, z, level);
        this.setSecondsOnFire(burnTime);
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(ModItems.FLAME_ARROW.get());
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(ModItems.FLAME_ARROW.get());
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);

        if (hitResult.getEntity() != null) {
            hitResult.getEntity().setSecondsOnFire(burnTime);
            this.shakeTime = 10;
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (hitResult.getType() != HitResult.Type.ENTITY) {
            this.shakeTime = 10; // 击中非实体目标也设置抖动时间
        }
    }


    public int getShakeTime() {
        return this.shakeTime;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.shakeTime > 0) {
            this.shakeTime--;
        }

        if (this.inGround && !this.isInGround) {
            this.isInGround = true;
            this.groundEffectTime = 1200;
        }


        if (this.isInGround && this.groundEffectTime > 0) {
            this.groundEffectTime--;

            if (this.level().isClientSide && this.random.nextInt(3) == 0) {

                for (int i = 0; i < 2; i++) {
                    double offsetX = this.random.nextDouble() * 0.2 - 0.1;
                    double offsetY = this.random.nextDouble() * 0.2;
                    double offsetZ = this.random.nextDouble() * 0.2 - 0.1;

                    this.level().addParticle(
                            ParticleTypes.FLAME,
                            this.getX() + offsetX,
                            this.getY() + 0.1 + offsetY,
                            this.getZ() + offsetZ,
                            0.0, 0.01, 0.0
                    );
                }
            }
        }


        if (this.groundEffectTime <= 0) {
            this.isInGround = false;
        }
    }
}