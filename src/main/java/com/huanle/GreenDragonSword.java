package com.huanle;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class GreenDragonSword extends SwordItem {
    private final Random random = new Random();

    public GreenDragonSword(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }

    @Override
    public boolean hurtEnemy(@Nonnull ItemStack stack, @Nonnull LivingEntity target, @Nonnull LivingEntity attacker) {
        // 基础伤害由SwordItem处理
        boolean result = super.hurtEnemy(stack, target, attacker);
        
        // 命中时使目标中毒5秒 (100 ticks)
        target.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 0));
        
        // 20%的概率获得短暂加速效果 (3秒，等级I)
        if (random.nextFloat() <= 0.2f) {
            attacker.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 60, 0));
        }
        
        // 10%概率治疗使用者2点生命值
        if (random.nextFloat() <= 0.1f) {
            attacker.heal(2.0f);
        }
        
        return result;
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag flag) {
        tooltipComponents.add(Component.translatable("item.huanle.green_dragon_sword.desc"));
        tooltipComponents.add(Component.translatable("item.huanle.green_dragon_sword.desc2"));
        tooltipComponents.add(Component.translatable("item.huanle.green_dragon_sword.desc3"));
        super.appendHoverText(stack, level, tooltipComponents, flag);
    }
}
