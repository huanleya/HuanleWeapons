package com.huanle.entities;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nullable;

public class SkeletonMinerEntity extends AbstractSkeleton implements RangedAttackMob {
    
    private final RangedAttackGoal rangedAttackGoal = new RangedAttackGoal(this, 1.0D, 20, 15.0F);
    private final MeleeAttackGoal meleeAttackGoal = new MeleeAttackGoal(this, 1.2D, false) {
        @Override
        public void stop() {
            super.stop();
            SkeletonMinerEntity.this.setAggressive(false);
        }
        
        @Override
        public void start() {
            super.start();
            SkeletonMinerEntity.this.setAggressive(true);
        }
    };
    
    public SkeletonMinerEntity(EntityType<? extends AbstractSkeleton> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 6; // 比普通骷髅多一点经验
        this.reassessWeaponGoal();
    }
    
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new RestrictSunGoal(this));
        this.goalSelector.addGoal(3, new FleeSunGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Wolf.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return AbstractSkeleton.createAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.ARMOR, 2.0D)
                .add(Attributes.MAX_HEALTH, 30.0D);
    }
    
    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, 
                                       MobSpawnType reason, @Nullable SpawnGroupData spawnData, 
                                       @Nullable CompoundTag dataTag) {
        SpawnGroupData spawngroupdata = super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);

        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(getRandomHelmet()));
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(getRandomPickaxe()));

        this.setDropChance(EquipmentSlot.HEAD, 0.1F);
        this.setDropChance(EquipmentSlot.MAINHAND, 0.1F);
        
        this.reassessWeaponGoal();
        return spawngroupdata;
    }

    private net.minecraft.world.item.Item getRandomHelmet() {
        int random = this.random.nextInt(100);
        if (random < 30) {
            return Items.LEATHER_HELMET;
        } else if (random < 55) {
            return Items.CHAINMAIL_HELMET;
        } else if (random < 75) {
            return Items.IRON_HELMET;
        } else if (random < 90) {
            return Items.GOLDEN_HELMET;
        } else {
            return Items.DIAMOND_HELMET;
        }
    }
    

    private net.minecraft.world.item.Item getRandomPickaxe() {
        int random = this.random.nextInt(100);
        if (random < 40) {
            return Items.STONE_PICKAXE;
        } else if (random < 75) {
            return Items.IRON_PICKAXE;
        } else if (random < 90) {
            return Items.GOLDEN_PICKAXE;
        } else {
            return Items.DIAMOND_PICKAXE;
        }
    }
    
    @Override
    public void reassessWeaponGoal() {
        if (this.level() != null && !this.level().isClientSide && this.goalSelector != null && this.meleeAttackGoal != null && this.rangedAttackGoal != null) {
            this.goalSelector.removeGoal(this.meleeAttackGoal);
            this.goalSelector.removeGoal(this.rangedAttackGoal);
            ItemStack itemstack = this.getItemInHand(InteractionHand.MAIN_HAND);
            if (itemstack.is(Items.BOW)) {
                int i = 20;
                if (this.level().getDifficulty() != Difficulty.HARD) {
                    i = 40;
                }
                this.goalSelector.addGoal(4, this.rangedAttackGoal);
            } else {
                this.goalSelector.addGoal(4, this.meleeAttackGoal);
            }
        }
    }
    
    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        ItemStack itemstack = this.getProjectile(this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof BowItem)));
        AbstractArrow abstractarrow = this.getArrow(itemstack, distanceFactor);
        if (this.getMainHandItem().getItem() instanceof BowItem)
            abstractarrow = ((BowItem)this.getMainHandItem().getItem()).customArrow(abstractarrow);
        double d0 = target.getX() - this.getX();
        double d1 = target.getY(0.3333333333333333D) - abstractarrow.getY();
        double d2 = target.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        abstractarrow.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, (float)(14 - this.level().getDifficulty().getId() * 4));
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(abstractarrow);
    }
    
    @Override
    protected AbstractArrow getArrow(ItemStack arrowStack, float distanceFactor) {
        return ProjectileUtil.getMobArrow(this, arrowStack, distanceFactor);
    }
    
    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }
    
    @Override
    protected SoundEvent getStepSound() {
        return SoundEvents.SKELETON_STEP;
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.SKELETON_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_DEATH;
    }

    public static boolean checkSkeletonMinerSpawnRules(EntityType<SkeletonMinerEntity> entityType, 
                                                       ServerLevelAccessor level, MobSpawnType spawnType, 
                                                       BlockPos pos, RandomSource random) {

        if (!AbstractSkeleton.checkMonsterSpawnRules(entityType, level, spawnType, pos, random)) {
            return false;
        }

        if (pos.getY() < 50) {
            return random.nextInt(100) < 50;
        }

        return random.nextInt(100) < 15;
    }
}