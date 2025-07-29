package com.huanle.items.tools;

import com.huanle.materials.EnderCrystalTier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class EnderCrystalPickaxe extends PickaxeItem {
    public EnderCrystalPickaxe(Properties properties) {
        super(EnderCrystalTier.INSTANCE, 1, -2.8F, properties);
    }
}
