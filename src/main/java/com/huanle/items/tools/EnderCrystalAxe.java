package com.huanle.items.tools;

import com.huanle.materials.EnderCrystalTier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class EnderCrystalAxe extends AxeItem {
    public EnderCrystalAxe(Properties properties) {
        super(EnderCrystalTier.INSTANCE, 5.0F, -3.0F, properties);
    }
}
