package com.huanle.items.food;

import com.huanle.ModEffects;
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

public class EnchantedEnderCrystalApple extends Item {
    public EnchantedEnderCrystalApple(Properties properties) {
        super(properties.food(new FoodProperties.Builder()
                .nutrition(4)
                .saturationMod(1.2F)
                .alwaysEat()
                .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 1800, 2), 1.0F)
                .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 3600, 4), 1.0F)
                .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1200, 2), 1.0F)
                .effect(() -> new MobEffectInstance(ModEffects.ENDER_RESISTANCE.get(), 4800, 1), 1.0F)
                .build()));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag flag) {
        tooltipComponents.add(Component.translatable("item.huanle.enchanted_ender_crystal_apple.desc"));
        super.appendHoverText(stack, level, tooltipComponents, flag);
    }
    
    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
