package com.huanle.items.weapons;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.huanle.capabilities.ManaUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class EarthSword extends SwordItem {
    private static final int EARTHQUAKE_COOLDOWN_TICKS = 200; // 10秒冷�?
    private static final int PULL_COOLDOWN_TICKS = 200; // 10秒冷�?
    private static final double EARTHQUAKE_RADIUS = 8.0;
    private static final double PULL_RANGE = 30.0;
    private static final float EARTHQUAKE_DAMAGE = 8.0F;
    private static final float PULL_DAMAGE = 12.0F;
    private static final String EARTHQUAKE_COOLDOWN_KEY = "earthquake_cooldown";
    private static final String PULL_COOLDOWN_KEY = "pull_cooldown";

    public EarthSword(@Nonnull Tier tier, int attackDamageModifier, float attackSpeedModifier, @Nonnull Item.Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }

    @Override
    public @Nonnull InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        
        if (!level.isClientSide) {
            if (player.isCrouching()) {
                if (!isOnCooldown(player, EARTHQUAKE_COOLDOWN_KEY)) {
                    float currentMana = ManaUtils.getCurrentMana(player);
                    if (currentMana >= 10.0f) {
                        ManaUtils.consumeManaWithEnchantment(player, 10.0f, itemstack);
                        
                        performEarthquakeAttack(level, player);
                        setCooldown(player, EARTHQUAKE_COOLDOWN_KEY, EARTHQUAKE_COOLDOWN_TICKS);
                        if (!player.getAbilities().instabuild) {
                            itemstack.hurtAndBreak(2, player, (p) -> p.broadcastBreakEvent(hand));
                        }
                        return InteractionResultHolder.success(itemstack);
                    } else {
                        player.displayClientMessage(
                            Component.translatable("item.huanle.earth_sword.not_enough_mana", 10),
                            true
                        );
                    }
                } else {
                    int remainingCooldown = getRemainingCooldown(player, EARTHQUAKE_COOLDOWN_KEY);
                    player.displayClientMessage(
                        Component.translatable("item.huanle.earth_sword.cooldown_earthquake", remainingCooldown / 20),
                        true
                    );
                }
            } else if (player.isShiftKeyDown()) {
                if (!isOnCooldown(player, "shift_right_click_cooldown")) {
                    performShiftRightClickEffect(level, player);
                    setCooldown(player, "shift_right_click_cooldown", 200);
                    return InteractionResultHolder.success(itemstack);
                }
            } else {
                if (!isOnCooldown(player, PULL_COOLDOWN_KEY)) {
                    float currentMana = ManaUtils.getCurrentMana(player);
                    if (currentMana >= 5.0f) {
                        ManaUtils.consumeManaWithEnchantment(player, 5.0f, itemstack);
                        
                        performBlastAttack(level, player);
                        setCooldown(player, PULL_COOLDOWN_KEY, PULL_COOLDOWN_TICKS);
                        if (!player.getAbilities().instabuild) {
                            itemstack.hurtAndBreak(2, player, (p) -> p.broadcastBreakEvent(hand));
                        }
                        return InteractionResultHolder.success(itemstack);
                    } else {
                        player.displayClientMessage(
                            Component.translatable("item.huanle.earth_sword.not_enough_mana", 5),
                            true
                        );
                    }
                } else {
                    int remainingCooldown = getRemainingCooldown(player, PULL_COOLDOWN_KEY);
                    player.displayClientMessage(
                        Component.translatable("item.huanle.earth_sword.cooldown_pull", remainingCooldown / 20),
                        true
                    );
                }
            }
        }
        
        return InteractionResultHolder.fail(itemstack);
    }

    private void performShiftRightClickEffect(Level level, Player player) {
        Vec3 playerPos = player.position();

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ENDER_DRAGON_FLAP, SoundSource.PLAYERS, 1.0F, 0.8F);

        if (level instanceof ServerLevel serverLevel) {
            for (int i = 0; i < 50; i++) {
                double offsetX = (level.random.nextDouble() - 0.5) * 5;
                double offsetY = (level.random.nextDouble() - 0.5) * 5;
                double offsetZ = (level.random.nextDouble() - 0.5) * 5;
                
                serverLevel.sendParticles(ParticleTypes.PORTAL,
                        playerPos.x + offsetX, playerPos.y + offsetY, playerPos.z + offsetZ,
                        1, 0.1, 0.1, 0.1, 0.0);
            }
        }
    }

    private boolean isOnCooldown(Player player, String cooldownKey) {
        return player.getPersistentData().contains(cooldownKey) && 
               player.getPersistentData().getInt(cooldownKey) > player.level().getGameTime();
    }

    private void setCooldown(Player player, String cooldownKey, int ticks) {
        player.getPersistentData().putInt(cooldownKey, (int)player.level().getGameTime() + ticks);
    }
    
    private void performEarthquakeAttack(Level level, Player player) {
        BlockPos playerPos = player.blockPosition();

        level.playSound(null, player.getX(), player.getY(), player.getZ(), 
                SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1.0F, 0.5F);

        if (level instanceof ServerLevel serverLevel) {

            for (int i = 0; i < 100; i++) {
                double offsetX = (level.random.nextDouble() - 0.5) * EARTHQUAKE_RADIUS * 2;
                double offsetZ = (level.random.nextDouble() - 0.5) * EARTHQUAKE_RADIUS * 2;
                double distance = Math.sqrt(offsetX * offsetX + offsetZ * offsetZ);
                
                if (distance <= EARTHQUAKE_RADIUS) {
                    BlockPos effectPos = playerPos.offset((int)offsetX, 0, (int)offsetZ);
                    BlockPos groundPos = findGroundLevel(level, effectPos);
                    
                    if (groundPos != null) {
                        double time = level.getGameTime() * 0.1;
                        double waveHeight = Math.sin(time + distance) * 0.5 + 0.5;

                        serverLevel.sendParticles(ParticleTypes.EXPLOSION,
                                groundPos.getX() + 0.5, groundPos.getY() + 1 + waveHeight, groundPos.getZ() + 0.5,
                                1, 0.2, 0.2, 0.2, 0.0);

                        serverLevel.sendParticles(
                                new net.minecraft.core.particles.BlockParticleOption(ParticleTypes.BLOCK, level.getBlockState(groundPos)),
                                groundPos.getX() + 0.5, groundPos.getY() + 1, groundPos.getZ() + 0.5,
                                5, 0.3, 0.3, 0.3, 0.1);

                        if (level.random.nextFloat() < 0.3) {
                            serverLevel.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                                    groundPos.getX() + 0.5, groundPos.getY() + 1.2, groundPos.getZ() + 0.5,
                                    1, 0.1, 0.1, 0.1, 0.01);
                        }
                    }
                }
            }
        }

        AABB damageArea = new AABB(player.getX() - EARTHQUAKE_RADIUS, player.getY() - 2, player.getZ() - EARTHQUAKE_RADIUS,
                                   player.getX() + EARTHQUAKE_RADIUS, player.getY() + 3, player.getZ() + EARTHQUAKE_RADIUS);
        
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, damageArea);
        for (LivingEntity entity : entities) {
            if (entity != player) {
                double distance = entity.distanceTo(player);
                if (distance <= EARTHQUAKE_RADIUS) {
                    float damage = EARTHQUAKE_DAMAGE * (float)(1.0 - distance / EARTHQUAKE_RADIUS * 0.5);
                    entity.hurt(level.damageSources().playerAttack(player), damage);
                    Vec3 knockback = entity.position().subtract(player.position()).normalize().scale(0.5);
                    entity.setDeltaMovement(entity.getDeltaMovement().add(knockback.x, 0.3, knockback.z));
                }
            }
        }
    }

    private void performBlastAttack(Level level, Player player) {
        Vec3 lookVec = player.getLookAngle();
        Vec3 startPos = player.getEyePosition();
        level.playSound(null, player.getX(), player.getY(), player.getZ(), 
                SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1.0F, 0.8F);
        for (double d = 1.0; d <= PULL_RANGE; d += 0.5) {
            Vec3 checkPos = startPos.add(lookVec.scale(d));
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.EXPLOSION,
                        checkPos.x, checkPos.y, checkPos.z,
                        1, 0.1, 0.1, 0.1, 0.0);
                BlockPos groundPos = findGroundLevel(level, new BlockPos((int)checkPos.x, (int)checkPos.y, (int)checkPos.z));
                if (groundPos != null && level.random.nextFloat() < 0.3) {
                    double time = level.getGameTime() * 0.1;
                    double waveEffect = Math.sin(time + d * 0.5) * 0.3 + 0.2;
                    
                    serverLevel.sendParticles(
                            new net.minecraft.core.particles.BlockParticleOption(ParticleTypes.BLOCK, level.getBlockState(groundPos)),
                            groundPos.getX() + 0.5, groundPos.getY() + 1 + waveEffect, groundPos.getZ() + 0.5,
                            3, 0.2, 0.1, 0.2, 0.05);
                }
            }
            AABB checkArea = new AABB(checkPos.x - 1, checkPos.y - 1, checkPos.z - 1,
                                 checkPos.x + 1, checkPos.y + 1, checkPos.z + 1);
            
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, checkArea);
            for (LivingEntity entity : entities) {
                if (entity != player) {
                    entity.hurt(level.damageSources().playerAttack(player), PULL_DAMAGE);
                    Vec3 knockback = entity.position().subtract(player.position()).normalize().scale(0.5);
                    entity.setDeltaMovement(entity.getDeltaMovement().add(knockback.x, 0.1, knockback.z));
                }
            }
        }
    }
    
    private BlockPos findGroundLevel(Level level, BlockPos pos) {
        for (int y = pos.getY(); y >= level.getMinBuildHeight(); y--) {
            BlockPos checkPos = new BlockPos(pos.getX(), y, pos.getZ());
            BlockState state = level.getBlockState(checkPos);
            if (!state.isAir() && state.getBlock() != Blocks.WATER && state.getBlock() != Blocks.LAVA) {
                return checkPos;
            }
        }
        return null;
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag flag) {
        tooltipComponents.add(Component.translatable("item.huanle.earth_sword.desc"));
        tooltipComponents.add(Component.translatable("item.huanle.earth_sword.desc2"));
        super.appendHoverText(stack, level, tooltipComponents, flag);
    }

    private int getRemainingCooldown(Player player, String cooldownKey) {
        if (player.getPersistentData().contains(cooldownKey)) {
            int endTime = player.getPersistentData().getInt(cooldownKey);
            long currentTime = player.level().getGameTime();
            return Math.max(0, endTime - (int)currentTime);
        }
        return 0;
    }
}
