package com.huanle;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class ThrowingAxeEntity extends AbstractArrow implements net.minecraft.world.entity.projectile.ItemSupplier {
    private static final EntityDataAccessor<ItemStack> DATA_ITEM = SynchedEntityData.defineId(ThrowingAxeEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Boolean> DATA_LOYALTY = SynchedEntityData.defineId(ThrowingAxeEntity.class, EntityDataSerializers.BOOLEAN);
    private ItemStack axeItem = ItemStack.EMPTY;
    private boolean dealtDamage;
    private int loyaltyLevel;
    private int returningTicks;

    public ThrowingAxeEntity(EntityType<? extends ThrowingAxeEntity> entityType, Level level) {
        super(entityType, level);
    }

    public ThrowingAxeEntity(Level level, LivingEntity shooter, ItemStack stack) {
        super(ModEntities.THROWING_AXE.get(), shooter, level);
        this.axeItem = stack.copy();
        this.entityData.set(DATA_ITEM, stack.copy());
        this.loyaltyLevel = EnchantmentHelper.getLoyalty(stack);
        this.entityData.set(DATA_LOYALTY, this.loyaltyLevel > 0);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ITEM, ItemStack.EMPTY);
        this.entityData.define(DATA_LOYALTY, false);
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        Entity entity = this.getOwner();
        // 处理忠诚附魔的返回逻辑
        if ((this.dealtDamage || this.isNoPhysics()) && entity != null) {
            int loyaltyLevel = this.entityData.get(DATA_LOYALTY) ? this.loyaltyLevel : 0;
            if (loyaltyLevel > 0 && !this.shouldReturnToThrower()) {
                if (!this.level().isClientSide && this.pickup == AbstractArrow.Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }
                this.discard();
            } else if (loyaltyLevel > 0) {
                this.setNoPhysics(true);
                Vec3 vec3 = entity.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.015D * loyaltyLevel, this.getZ());
                if (this.level().isClientSide) {
                    this.yOld = this.getY();
                }

                double d0 = 0.05D * loyaltyLevel;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vec3.normalize().scale(d0)));
                if (this.returningTicks == 0) {
                    this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                }

                ++this.returningTicks;
            }
        }

        super.tick();
    }

    private boolean shouldReturnToThrower() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayer) || !entity.isSpectator();
        } else {
            return false;
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return this.axeItem.copy();
    }

    @Override
    public ItemStack getItem() {
        return getPickupItem();
    }

    @Override
    protected EntityHitResult findHitEntity(Vec3 startVec, Vec3 endVec) {
        return this.dealtDamage ? null : super.findHitEntity(startVec, endVec);
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        Entity entity = hitResult.getEntity();
        float damage = 8.0F; // 基础伤害值
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            damage += EnchantmentHelper.getDamageBonus(this.axeItem, livingEntity.getMobType());
        }

        Entity owner = this.getOwner();
        // 使用更通用的方式创建伤害源
        DamageSource damageSource = this.damageSources().thrown(this, owner == null ? this : owner);
        this.dealtDamage = true;
        SoundEvent soundEvent = SoundEvents.TRIDENT_HIT;
        if (entity.hurt(damageSource, damage)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entity;
                if (owner instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingEntity, owner);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity) owner, livingEntity);
                    
                    // 触发附雷附魔效果
                    int lightningLevel = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.LIGHTNING_STRIKE.get(), this.axeItem);
                    if (lightningLevel > 0 && owner instanceof LivingEntity) {
                        LightningEnchantment.onHitEntity(livingEntity, (LivingEntity) owner, this.axeItem, lightningLevel);
                    }
                }
            }
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
        this.playSound(soundEvent, 1.0F, 1.0F);
    }

    @Override
    public void playerTouch(Player player) {
        Entity entity = this.getOwner();
        if (entity == null || entity.getUUID().equals(player.getUUID())) {
            super.playerTouch(player);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("ThrowingAxe", 10)) {
            this.axeItem = ItemStack.of(tag.getCompound("ThrowingAxe"));
        }
        this.dealtDamage = tag.getBoolean("DealtDamage");
        this.loyaltyLevel = EnchantmentHelper.getLoyalty(this.axeItem);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("ThrowingAxe", this.axeItem.save(new CompoundTag()));
        tag.putBoolean("DealtDamage", this.dealtDamage);
    }

    @Override
    public void tickDespawn() {
        int loyaltyLevel = this.entityData.get(DATA_LOYALTY) ? this.loyaltyLevel : 0;
        if (this.pickup != AbstractArrow.Pickup.ALLOWED || loyaltyLevel <= 0) {
            super.tickDespawn();
        }
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }
}