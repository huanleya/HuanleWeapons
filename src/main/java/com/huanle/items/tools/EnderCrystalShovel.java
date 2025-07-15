package com.huanle.items.tools;

import com.huanle.materials.EnderCrystalTier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class EnderCrystalShovel extends ShovelItem {
    public EnderCrystalShovel(Properties properties) {
        super(EnderCrystalTier.INSTANCE, 1.5F, -3.0F, properties);
    }
}
