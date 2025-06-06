package com.huanle;

import java.util.Random;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;

public class EnderSword extends SwordItem {
    private final Random random = new Random();
    
    public EnderSword(Tier tier, int attackDamageModifier, float attackSpeedModifier, Item.Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }
    
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // 基础伤害由SwordItem处理
        boolean result = super.hurtEnemy(stack, target, attacker);
        
        // 20%概率造成额外伤害（伤害值 = 末影之剑攻击力 × 0.5）
        if (random.nextFloat() <= 0.2f) {
            float extraDamage = this.getDamage() * 0.5f;
            target.hurt(target.level().damageSources().generic(), extraDamage);
        }
        
        // 给目标添加虚弱II效果，持续2秒（40tick）
        target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 1)); // 1表示等级II
        
        return result;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        
        // 检查冷却时间
        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.pass(itemstack);
        }
        
        // 播放末影珍珠投掷音效
        level.playSound(null, player.getX(), player.getY(), player.getZ(), 
                SoundEvents.ENDER_PEARL_THROW, SoundSource.PLAYERS, 0.5F, 0.4F);
        
        // 在服务器端创建并投掷末影珍珠实体
        if (!level.isClientSide) {
            ThrownEnderpearl pearl = new ThrownEnderpearl(level, player);
            pearl.setItem(new ItemStack(Items.ENDER_PEARL));
            pearl.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(pearl);
        }
        
        // 添加10秒冷却时间（200 ticks）
        player.getCooldowns().addCooldown(this, 200);
        
        // 记录物品使用统计
        player.awardStat(Stats.ITEM_USED.get(this));
        
        return InteractionResultHolder.success(itemstack);
    }
}