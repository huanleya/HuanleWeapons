package com.huanle;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.BlockPos;

public class DragonHunterSword extends SwordItem {
    private final Random random = new Random();
    private static final String AWAKENED_KEY = "awakened";
    private static final String LAST_TELEPORT_KEY = "lastTeleport";
    private static final String LAST_TRANSFORM_KEY = "lastTransform";
    private static final String LAST_GLOW_KEY = "lastGlow";
    private static final String TRANSFORM_COOLDOWN_MESSAGE_SENT = "transformCooldownMessageSent";
    private static final String TELEPORT_COOLDOWN_MESSAGE_SENT = "teleportCooldownMessageSent";
    private static final String GLOW_COOLDOWN_MESSAGE_SENT = "glowCooldownMessageSent";
    private static final String TELEPORT_UNSAFE_MESSAGE_SENT = "teleportUnsafeMessageSent";
    
    public DragonHunterSword(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }
    
    @Override
    public void inventoryTick(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide && entity instanceof Player player && isSelected && isAwakened(stack)) {
            // 觉醒形态时生成末影粒子特效
            if (level instanceof ServerLevel serverLevel) {
                Vec3 pos = player.position();
                for (int i = 0; i < 3; i++) {
                    double offsetX = (random.nextDouble() - 0.5) * 2.0;
                    double offsetY = random.nextDouble() * 2.0;
                    double offsetZ = (random.nextDouble() - 0.5) * 2.0;
                    serverLevel.sendParticles(ParticleTypes.PORTAL, 
                        pos.x + offsetX, pos.y + offsetY, pos.z + offsetZ, 
                        1, 0, 0, 0, 0.1);
                }
            }
        }
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }
    
    @Override
    public boolean hurtEnemy(@Nonnull ItemStack stack, @Nonnull LivingEntity target, @Nonnull LivingEntity attacker) {
        boolean result = super.hurtEnemy(stack, target, attacker);
        
        if (isAwakened(stack)) {
            // 觉醒形态：20%概率造成额外伤害并施加虚弱效果
            if (random.nextFloat() <= 0.2f) {
                float extraDamage = this.getDamage() * 0.5f;
                target.hurt(target.level().damageSources().generic(), extraDamage);
                target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 1));
            }
            
            // 对发光的实体造成双倍伤害
            if (target.hasEffect(MobEffects.GLOWING)) {
                float doubleDamage = this.getDamage();
                target.hurt(target.level().damageSources().generic(), doubleDamage);
                target.removeEffect(MobEffects.GLOWING);
            }
        }
        
        return result;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        long currentTime = level.getGameTime();

        if (level.isClientSide) {
            return InteractionResultHolder.pass(itemstack);
        }

        if (player.isShiftKeyDown()) {
            // 蹲下右键：形态切换
            return handleTransform(level, player, itemstack, currentTime);
        } else {
            // 普通右键：技能使用
            if (isAwakened(itemstack)) {
                return handleAwakenedSkill(level, player, itemstack, currentTime);
            } else {
                return handleNormalSkill(level, player, itemstack, currentTime);
            }
        }
    }
    
    private InteractionResultHolder<ItemStack> handleTransform(Level level, Player player, ItemStack stack, long currentTime) {
        CompoundTag tag = stack.getOrCreateTag();
        long lastTransform = tag.getLong(LAST_TRANSFORM_KEY);
        
        // 检查60秒冷却
        if (currentTime - lastTransform < 1200) { // 60秒 = 1200 ticks
            long remainingTime = (1200 - (currentTime - lastTransform)) / 20; // convert ticks to seconds
            if (!level.isClientSide) {
                if (!tag.getBoolean(TRANSFORM_COOLDOWN_MESSAGE_SENT)) {
                    player.sendSystemMessage(Component.translatable("item.huanle.dragon_hunter_sword.transform_cooldown_time", remainingTime));
                    tag.putBoolean(TRANSFORM_COOLDOWN_MESSAGE_SENT, true);
                }
            }
            return InteractionResultHolder.pass(stack);
        }
        
        boolean awakened = isAwakened(stack);
        tag.putBoolean(AWAKENED_KEY, !awakened);
        tag.putLong(LAST_TRANSFORM_KEY, currentTime);
        tag.putBoolean(TRANSFORM_COOLDOWN_MESSAGE_SENT, false); // Reset flag on successful transform
        
        if (!awakened) {
            // 切换到觉醒形态：获得力量II和速度II，持续30秒
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600, 1));
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600, 1));
            if (!level.isClientSide) {
                player.sendSystemMessage(Component.translatable("item.huanle.dragon_hunter_sword.transformed_awakened"));
            }
            
            // 设置自定义模型数据以显示觉醒形态贴图
            tag.putInt("CustomModelData", 1);
            
            level.playSound(null, player.getX(), player.getY(), player.getZ(), 
                SoundEvents.ENDER_DRAGON_GROWL, SoundSource.PLAYERS, 1.0F, 1.0F);
        } else {
            // 切换回普通形态
            tag.remove("CustomModelData");
            if (!level.isClientSide) {
                player.sendSystemMessage(Component.translatable("item.huanle.dragon_hunter_sword.transformed_normal"));
            }
            
            level.playSound(null, player.getX(), player.getY(), player.getZ(), 
                SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
        
        return InteractionResultHolder.success(stack);
    }
    
    private InteractionResultHolder<ItemStack> handleNormalSkill(Level level, Player player, ItemStack stack, long currentTime) {
        CompoundTag tag = stack.getOrCreateTag();
        long lastTeleport = tag.getLong(LAST_TELEPORT_KEY);
        
        // 检查5秒冷却
        if (currentTime - lastTeleport < 100) { // 5秒 = 100 ticks
            long remainingTime = (100 - (currentTime - lastTeleport)) / 20; // convert ticks to seconds
            if (!level.isClientSide) {
                if (!tag.getBoolean(TELEPORT_COOLDOWN_MESSAGE_SENT)) {
                    player.sendSystemMessage(Component.translatable("item.huanle.dragon_hunter_sword.teleport_cooldown_time", remainingTime));
                    tag.putBoolean(TELEPORT_COOLDOWN_MESSAGE_SENT, true);
                }
            }
            return InteractionResultHolder.pass(stack);
        }

        tag.putBoolean(TELEPORT_COOLDOWN_MESSAGE_SENT, false);

        tag.putBoolean(TELEPORT_UNSAFE_MESSAGE_SENT, false);
        
        // 瞬移8格距离
        Vec3 lookVec = player.getLookAngle();
        Vec3 teleportPos = player.position().add(lookVec.scale(8.0));
        
        // 确保瞬移位置安全
        if (level.getBlockState(new BlockPos((int)teleportPos.x, (int)teleportPos.y + 1, (int)teleportPos.z)).isAir() && 
            level.getBlockState(new BlockPos((int)teleportPos.x, (int)teleportPos.y, (int)teleportPos.z)).isAir()) {
            player.teleportTo(teleportPos.x, teleportPos.y, teleportPos.z);
            
            level.playSound(null, player.getX(), player.getY(), player.getZ(), 
                SoundEvents.ENDER_PEARL_THROW, SoundSource.PLAYERS, 0.5F, 0.4F);
            
            tag.putLong(LAST_TELEPORT_KEY, currentTime);
            if (!level.isClientSide) {
                player.sendSystemMessage(Component.translatable("item.huanle.dragon_hunter_sword.teleport_success"));
            }
            // Reset unsafe message flag on successful teleport
            tag.putBoolean(TELEPORT_UNSAFE_MESSAGE_SENT, false);
            return InteractionResultHolder.success(stack);
        }
        
        if (!level.isClientSide) {
            // Send unsafe message only once until the condition changes
            if (!tag.getBoolean(TELEPORT_UNSAFE_MESSAGE_SENT)) {
                player.sendSystemMessage(Component.translatable("item.huanle.dragon_hunter_sword.teleport_unsafe"));
                tag.putBoolean(TELEPORT_UNSAFE_MESSAGE_SENT, true);
            }
        }
        return InteractionResultHolder.pass(stack);
    }
    
    private InteractionResultHolder<ItemStack> handleAwakenedSkill(Level level, Player player, ItemStack stack, long currentTime) {
        CompoundTag tag = stack.getOrCreateTag();
        long lastGlow = tag.getLong(LAST_GLOW_KEY);
        
        // 检查60秒冷却
        if (currentTime - lastGlow < 1200) { // 60秒 = 1200 ticks
            long remainingTime = (1200 - (currentTime - lastGlow)) / 20; // convert ticks to seconds
            if (!level.isClientSide) {
                if (!tag.getBoolean(GLOW_COOLDOWN_MESSAGE_SENT)) {
                    player.sendSystemMessage(Component.translatable("item.huanle.dragon_hunter_sword.glow_cooldown_time", remainingTime));
                    tag.putBoolean(GLOW_COOLDOWN_MESSAGE_SENT, true);
                }
            }
            return InteractionResultHolder.pass(stack);
        }

        // Reset glow cooldown message flag if not on cooldown
        tag.putBoolean(GLOW_COOLDOWN_MESSAGE_SENT, false);
        
        // 对20x20范围内的实体施加发光效果
        AABB area = new AABB(player.getX() - 10, player.getY() - 10, player.getZ() - 10,
                            player.getX() + 10, player.getY() + 10, player.getZ() + 10);
        
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area);
        for (LivingEntity entity : entities) {
            if (entity != player) {
                entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 1200, 0)); // 60秒发光
            }
        }
        
        level.playSound(null, player.getX(), player.getY(), player.getZ(), 
            SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.0F, 1.0F);
        
        tag.putLong(LAST_GLOW_KEY, currentTime);
        if (!level.isClientSide) {
            player.sendSystemMessage(Component.translatable("item.huanle.dragon_hunter_sword.glow_success"));
        }
        return InteractionResultHolder.success(stack);
    }
    
    private boolean isAwakened(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.getBoolean(AWAKENED_KEY);
    }
    
    @Override
    public Component getName(@Nonnull ItemStack stack) {
        if (isAwakened(stack)) {
            return Component.translatable("item.huanle.awakened_dragon_hunter_sword");
        }
        return Component.translatable("item.huanle.dragon_hunter_sword");
    }
    
    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag flag) {
        if (isAwakened(stack)) {
            tooltipComponents.add(Component.translatable("item.huanle.awakened_dragon_hunter_sword.desc"));
            tooltipComponents.add(Component.translatable("item.huanle.awakened_dragon_hunter_sword.desc2"));
            tooltipComponents.add(Component.translatable("item.huanle.awakened_dragon_hunter_sword.desc3"));
            tooltipComponents.add(Component.translatable("item.huanle.awakened_dragon_hunter_sword.desc4"));
        } else {
            tooltipComponents.add(Component.translatable("item.huanle.dragon_hunter_sword.desc"));
            tooltipComponents.add(Component.translatable("item.huanle.dragon_hunter_sword.desc2"));
            tooltipComponents.add(Component.translatable("item.huanle.dragon_hunter_sword.desc3"));
        }
        super.appendHoverText(stack, level, tooltipComponents, flag);
    }
}