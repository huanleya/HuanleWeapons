package com.huanle;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface FlameBowItem2 {
    AbstractArrow customArrow(AbstractArrow arrow, ItemStack stack, Level level);

    boolean isValidProjectile(ItemStack stack);

    void onUsingTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration);
}
