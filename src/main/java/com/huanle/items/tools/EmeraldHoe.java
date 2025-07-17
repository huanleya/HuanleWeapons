package com.huanle.items.tools;

import com.huanle.materials.EmeraldTier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class EmeraldHoe extends HoeItem {
    public EmeraldHoe(Properties properties) {
        super(EmeraldTier.INSTANCE, -3, 0.0F, properties);
    }

}
