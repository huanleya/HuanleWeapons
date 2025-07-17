package com.huanle.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 粉末物品类
 * 用于各种金属粉末
 */
public class PowderItem extends Item {
    
    private final String powderType;
    
    public PowderItem(Properties properties, String powderType) {
        super(properties);
        this.powderType = powderType;
    }
}