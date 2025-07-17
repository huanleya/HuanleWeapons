package com.huanle.entities;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MysteriousMerchantEntity extends AbstractVillager {
    
    private static final List<VillagerTrades.ItemListing> CUSTOM_TRADES = new ArrayList<>();
    
    public MysteriousMerchantEntity(EntityType<? extends AbstractVillager> entityType, Level level) {
        super(entityType, level);
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.5D)
                .add(Attributes.FOLLOW_RANGE, 48.0D);
    }
    
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new TradeWithPlayerGoal(this));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Zombie.class, 8.0F, 0.5D, 0.5D));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Evoker.class, 12.0F, 0.5D, 0.5D));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Vindicator.class, 8.0F, 0.5D, 0.5D));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Vex.class, 8.0F, 0.5D, 0.5D));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Pillager.class, 15.0F, 0.5D, 0.5D));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Illusioner.class, 12.0F, 0.5D, 0.5D));
        this.goalSelector.addGoal(1, new PanicGoal(this, 0.5D));
        this.goalSelector.addGoal(2, new LookAtTradingPlayerGoal(this));
        this.goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 0.35D));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 0.35D));
        this.goalSelector.addGoal(9, new InteractGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
    }
    
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (!itemstack.is(Items.VILLAGER_SPAWN_EGG) && this.isAlive() && !this.isTrading() && !this.isBaby()) {
            if (hand == InteractionHand.MAIN_HAND) {
                player.awardStat(Stats.TALKED_TO_VILLAGER);
            }
            
            if (this.getOffers().isEmpty()) {
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            } else {
                if (!this.level().isClientSide) {
                    this.setTradingPlayer(player);
                    this.openTradingScreen(player, this.getDisplayName(), 1);
                }
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
        } else {
            return super.mobInteract(player, hand);
        }
    }
    
    @Override
    protected void updateTrades() {
        if (this.getOffers().isEmpty()) {
            this.addOffersFromItemListings(this.getOffers(), getCustomTrades(), 5);
        }
    }
    
    private static VillagerTrades.ItemListing[] getCustomTrades() {
        if (CUSTOM_TRADES.isEmpty()) {
            initializeDefaultTrades();
        }
        return CUSTOM_TRADES.toArray(new VillagerTrades.ItemListing[0]);
    }
    
    private static void initializeDefaultTrades() {
        CUSTOM_TRADES.add((trader, random) -> new MerchantOffer(
            new ItemStack(Items.DIAMOND, 1),
            new ItemStack(Items.EMERALD, 2),
            10, 2, 0.02f
        ));

        CUSTOM_TRADES.add((trader, random) -> new MerchantOffer(
            new ItemStack(Items.EMERALD, 3),
            new ItemStack(Items.DIAMOND, 1),
            5, 10, 0.02f
        ));

        CUSTOM_TRADES.add((trader, random) -> new MerchantOffer(
            new ItemStack(Items.EMERALD, 2),
            new ItemStack(Items.EXPERIENCE_BOTTLE, 1),
            12, 5, 0.02f
        ));
    }

    public static void addCustomTrade(VillagerTrades.ItemListing trade) {
        CUSTOM_TRADES.add(trade);
    }

    public static void clearCustomTrades() {
        CUSTOM_TRADES.clear();
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isTrading() ? SoundEvents.VILLAGER_TRADE : SoundEvents.VILLAGER_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.VILLAGER_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.VILLAGER_DEATH;
    }
    
    @Override
    protected SoundEvent getTradeUpdatedSound(boolean getYesSound) {
        return getYesSound ? SoundEvents.VILLAGER_YES : SoundEvents.VILLAGER_NO;
    }
    
    @Override
    public boolean showProgressBar() {
        return false;
    }
    
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        this.updateTrades();
        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }
    
    @Override
    public Component getDisplayName() {
        return Component.translatable("entity.huanle.mysterious_merchant");
    }
    
    @Override
    protected void rewardTradeXp(MerchantOffer offer) {

    }
    
    @Override
    public Vec3 getRopeHoldPosition(float partialTicks) {
        return new Vec3(0.0D, 0.6D * this.getEyeHeight(), this.getBbWidth() * 0.4D);
    }
    
    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }
}