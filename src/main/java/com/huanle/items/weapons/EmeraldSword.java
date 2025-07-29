package com.huanle.items.weapons;

import com.huanle.materials.EmeraldTier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class EmeraldSword extends SwordItem {
    public EmeraldSword(Properties properties) {
        super(EmeraldTier.INSTANCE, 3, -2.4F, properties);
    }

}
