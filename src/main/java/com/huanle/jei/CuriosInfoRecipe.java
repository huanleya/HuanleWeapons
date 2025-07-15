package com.huanle.jei;

import net.minecraft.world.item.ItemStack;

public class CuriosInfoRecipe {
    
    private final ItemStack item;
    private final String slotType;
    private final String description;
    
    public CuriosInfoRecipe(ItemStack item, String slotType, String description) {
        this.item = item;
        this.slotType = slotType;
        this.description = description;
    }
    
    public ItemStack getItem() {
        return item;
    }
    
    public String getSlotType() {
        return slotType;
    }
    
    public String getDescription() {
        return description;
    }
}