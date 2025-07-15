package com.huanle.items.weapons;

import com.huanle.materials.EnderCrystalTier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class EnderCrystalSword extends SwordItem {
    public EnderCrystalSword(Properties properties) {
        super(EnderCrystalTier.INSTANCE, 3, -2.4F, properties);
    }
}
