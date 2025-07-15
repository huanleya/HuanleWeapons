package com.huanle.entities;

import com.huanle.ModEnchantments;
import com.huanle.ModEntities;
import com.huanle.enchantments.LightningEnchantment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class ThrowingAxeEntity extends AbstractArrow implements ItemSupplier {
    private static final EntityDataAccessor<ItemStack> DATA_ITEM = SynchedEntityData.defineId(ThrowingAxeEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Boolean> DATA_LOYALTY = SynchedEntityData.defineId(ThrowingAxeEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Byte> DATA_HIT_DIRECTION = SynchedEntityData.defineId(ThrowingAxeEntity.class, EntityDataSerializers.BYTE);
    
    private ItemStack axeItem = ItemStack.EMPTY;
    private boolean dealtDamage;
    private int loyaltyLevel;
    private int returningTicks;

    private BlockPos stuckBlockPos = null;
    private Direction stuckDirection = null;

    public ThrowingAxeEntity(EntityType<? extends ThrowingAxeEntity> entityType, Level level) {
        super(entityType, level);
    }

    public ThrowingAxeEntity(Level level, LivingEntity shooter, ItemStack stack) {
        super(ModEntities.THROWING_AXE.get(), shooter, level);
        this.axeItem = stack.copy();
        this.entityData.set(DATA_ITEM, stack.copy());
        this.loyaltyLevel = EnchantmentHelper.getLoyalty(stack);
        this.entityData.set(DATA_LOYALTY, this.loyaltyLevel > 0);
        this.pickup = shooter instanceof Player ? AbstractArrow.Pickup.ALLOWED : AbstractArrow.Pickup.DISALLOWED;

        System.out.println("ThrowingAxeEntity created with item: " + stack.getItem().getDescriptionId());
        System.out.println("Entity position: " + this.getX() + ", " + this.getY() + ", " + this.getZ());
        System.out.println("Entity UUID: " + this.getUUID());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ITEM, ItemStack.EMPTY);
        this.entityData.define(DATA_LOYALTY, false);
        this.entityData.define(DATA_HIT_DIRECTION, (byte)0);
    }
    
    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);

        this.stuckBlockPos = blockHitResult.getBlockPos();
        this.stuckDirection = blockHitResult.getDirection();

        this.entityData.set(DATA_HIT_DIRECTION, (byte)this.stuckDirection.get3DDataValue());

        Vec3 hitLocation = blockHitResult.getLocation();
        this.setPos(hitLocation.x, hitLocation.y, hitLocation.z);

        this.setDeltaMovement(Vec3.ZERO);
        this.dealtDamage = true;

        this.playSound(SoundEvents.TRIDENT_HIT, 1.0F, 1.0F);
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        Entity entity = this.getOwner();
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

    @Override
    public void playerTouch(Player player) {
        if (!this.level().isClientSide && (this.inGroundTime > 0 || this.isNoPhysics()) && this.shakeTime <= 0) {
            Entity owner = this.getOwner();
            boolean isCreativeOwner = player.getAbilities().instabuild && 
                                    owner != null && 
                                    owner.getUUID().equals(player.getUUID());

            if (owner == null || owner.getUUID().equals(player.getUUID())) {
                if (isCreativeOwner) {
                    this.playSound(SoundEvents.ITEM_PICKUP, 0.2F, 
                        ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    player.take(this, 1);
                    this.discard();
                }
                else if (player.getInventory().add(this.getPickupItem())) {
                    this.playSound(SoundEvents.ITEM_PICKUP, 0.2F, 
                        ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    player.take(this, 1);
                    this.discard();
                }
            }
        }
    }

    @Override
    protected void tickDespawn() {
        if (this.pickup == AbstractArrow.Pickup.ALLOWED) {
            if (this.tickCount % 2 == 0) {
                super.tickDespawn();
            }
        } else {
            super.tickDespawn();
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return this.axeItem.copy();
    }

    @Override
    public ItemStack getItem() {
        return this.axeItem.copy();
    }

    @Override
    protected EntityHitResult findHitEntity(Vec3 startVec, Vec3 endVec) {
        return this.dealtDamage ? null : super.findHitEntity(startVec, endVec);
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        Entity entity = hitResult.getEntity();
        float damage = 8.0F;
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            damage += EnchantmentHelper.getDamageBonus(this.axeItem, livingEntity.getMobType());
        }

        Entity owner = this.getOwner();
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
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }
    

    public boolean isInGround() {

        return this.inGround || this.stuckBlockPos != null;
    }
    

    public Direction getHitDirection() {
        if (this.stuckDirection != null) {
            return this.stuckDirection;
        }

        byte dirValue = this.entityData.get(DATA_HIT_DIRECTION);
        return Direction.from3DDataValue(dirValue);
    }
    

    public BlockPos getStuckBlockPos() {
        return this.stuckBlockPos;
    }

    private boolean shouldReturnToThrower() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayer) || !entity.isSpectator();
        }
        return false;
    }
}
