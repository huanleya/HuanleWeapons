package com.huanle.entities;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nullable;

public class ZombieMinerEntity extends Zombie {
    
    public ZombieMinerEntity(EntityType<? extends Zombie> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 6;
    }
    
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, new ZombieAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.addBehaviourGoals();
    }
    
    @Override
    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers(ZombieMinerEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createAttributes()
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23F)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.ARMOR, 2.0D)
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
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
    public MobType getMobType() {
        return MobType.UNDEAD;
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ZOMBIE_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ZOMBIE_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_DEATH;
    }
    
    @Override
    protected SoundEvent getStepSound() {
        return SoundEvents.ZOMBIE_STEP;
    }
    

    public static boolean checkZombieMinerSpawnRules(EntityType<ZombieMinerEntity> entityType, 
                                                     ServerLevelAccessor level, MobSpawnType spawnType, 
                                                     BlockPos pos, RandomSource random) {

        if (!Zombie.checkMonsterSpawnRules(entityType, level, spawnType, pos, random)) {
            return false;
        }

        if (pos.getY() < 50) {
            return random.nextInt(100) < 50;
        }

        return random.nextInt(100) < 15;
    }
}