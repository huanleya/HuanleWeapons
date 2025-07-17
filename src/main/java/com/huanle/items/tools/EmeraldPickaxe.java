package com.huanle.items.tools;

import com.huanle.materials.EmeraldTier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class EmeraldPickaxe extends PickaxeItem {
    public EmeraldPickaxe(Properties properties) {
        super(EmeraldTier.INSTANCE, 1, -2.8F, properties);
    }
}
