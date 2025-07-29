package com.huanle.items.tools;

import com.huanle.materials.EnderCrystalTier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class EnderCrystalHoe extends HoeItem {
    public EnderCrystalHoe(Properties properties) {
        super(EnderCrystalTier.INSTANCE, -3, 0.0F, properties);
    }
}
