package com.huanle;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FlameArrowItem extends ArrowItem {
    public FlameArrowItem(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack stack, LivingEntity shooter) {
        // 创建自定义的烈焰箭实体，而不是使用原版箭
        FlameArrowEntity arrow = new FlameArrowEntity(level, shooter, stack);
        return arrow;
    }

    @Override
    public boolean isFireResistant() {
        return true;
    }
}