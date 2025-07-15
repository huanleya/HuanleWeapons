package com.huanle.items.tools;

import com.huanle.materials.EmeraldTier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class EmeraldShovel extends ShovelItem {
    public EmeraldShovel(Properties properties) {
        super(EmeraldTier.INSTANCE, 1.5F, -3.0F, properties);
    }

}
