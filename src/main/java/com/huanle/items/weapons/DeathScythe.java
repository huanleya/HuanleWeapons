package com.huanle.items.weapons;

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
import net.minecraft.world.phys.AABB;
import com.huanle.ModEffects;

public class DeathScythe extends SwordItem {
    private final Random random = new Random();
    
    public DeathScythe(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }
    
    @Override
    public boolean hurtEnemy(@Nonnull ItemStack stack, @Nonnull LivingEntity target, @Nonnull LivingEntity attacker) {
        // 基础伤害由SwordItem处理
        boolean result = super.hurtEnemy(stack, target, attacker);
        
        // 吸取敌人4点生命值
        if (attacker instanceof Player player) {
            player.heal(4.0f);
        }
        
        // 5%几率触发失血debuff
        if (random.nextFloat() <= 0.05f) {
            // 先扣除当前生命值的30%
            float healthReduction = target.getHealth() * 0.3f;
            target.hurt(target.level().damageSources().magic(), healthReduction);
            
            // 然后添加失血效果（持续扣血）
            target.addEffect(new MobEffectInstance(ModEffects.BLEEDING.get(), 100, 0)); // 5秒，等级I
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
            
            // 获取8格范围内的所有生物
            AABB aabb = player.getBoundingBox().inflate(8.0D);
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, aabb);
            
            // 对范围内的敌人施加凋零效果
            for (LivingEntity entity : entities) {
                if (entity != player) {
                    entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 160, 1)); // 8秒，等级II
                }
            }
            
            // 添加10秒冷却时间（200 ticks）
            player.getCooldowns().addCooldown(this, 200);
            
            return InteractionResultHolder.success(itemstack);
        }
        
        return InteractionResultHolder.pass(itemstack);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag flag) {
        tooltipComponents.add(Component.translatable("item.huanle.death_scythe.desc"));
        tooltipComponents.add(Component.translatable("item.huanle.death_scythe.desc2"));
        tooltipComponents.add(Component.translatable("item.huanle.death_scythe.desc3"));
        super.appendHoverText(stack, level, tooltipComponents, flag);
    }
}
