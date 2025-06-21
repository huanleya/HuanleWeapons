package com.huanle;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class AmethystApple extends Item {
    public AmethystApple(Properties properties) {
        super(properties.food(new FoodProperties.Builder()
                .nutrition(4)
                .saturationMod(1.2F)
                .alwaysEat()
                .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 3600, 4), 1.0F)  // 抗性提升V级3分钟
                .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 3600, 3), 1.0F)         // 伤害吸收IV级3分钟
                .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 600, 1), 1.0F)        // 生命恢复II级30秒
                .build()));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag flag) {
        tooltipComponents.add(Component.translatable("item.huanle.amethyst_apple.desc"));
        super.appendHoverText(stack, level, tooltipComponents, flag);
    }
}