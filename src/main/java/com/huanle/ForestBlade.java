package com.huanle;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class ForestBlade extends SwordItem {
    private final Random random = new Random();
    
    public ForestBlade(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }
    
    @Override
    public void onCraftedBy(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull Player player) {
        // 初始化物品
        super.onCraftedBy(stack, level, player);
    }
    
    @Override
    public void inventoryTick(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide && entity instanceof Player player) {
            // 检查玩家是否在水中且手持此物品
            if (isSelected && player.isInWater()) {
                // 玩家在水中手持森林之刃时获得生命恢复I效果
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 0, false, true));
            }
        }
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }
    
    @Override
    public boolean hurtEnemy(@Nonnull ItemStack stack, @Nonnull LivingEntity target, @Nonnull LivingEntity attacker) {
        // 基础伤害由SwordItem处理
        boolean result = super.hurtEnemy(stack, target, attacker);
        
        if (random.nextFloat() <= 0.3f) {
            // 30%几率施加凋零II效果，持续5秒（100tick）
            target.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 1)); // 1表示等级II
        }
        
        // 3%几率让自己回满血
        if (random.nextFloat() <= 0.03f && attacker instanceof Player) {
            Player player = (Player) attacker;
            player.setHealth(player.getMaxHealth());
        }
        
        return result;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        
        // 检查冷却时间
        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.pass(itemstack);
        }
        
        // 检查玩家是否蹲下并且有足够的生命值
        if (player.isShiftKeyDown() && player.getHealth() > 10f) {
            // 消耗10点生命值
            player.hurt(player.damageSources().magic(), 10f);
            
            // 给予力量II效果，持续10秒（200tick）
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 1)); // 1表示等级II
            
            // 添加10秒冷却时间（200 ticks）
            player.getCooldowns().addCooldown(this, 200);
            
            return InteractionResultHolder.success(itemstack);
        }
        
        return InteractionResultHolder.pass(itemstack);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag flag) {
        tooltipComponents.add(Component.translatable("item.huanle.forest_blade.desc"));
        tooltipComponents.add(Component.translatable("item.huanle.forest_blade.desc2"));
        tooltipComponents.add(Component.translatable("item.huanle.forest_blade.desc3"));
        tooltipComponents.add(Component.translatable("item.huanle.forest_blade.desc4"));
        super.appendHoverText(stack, level, tooltipComponents, flag);
    }
}
