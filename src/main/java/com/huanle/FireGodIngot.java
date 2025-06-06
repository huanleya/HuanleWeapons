package com.huanle;

import net.minecraft.world.item.Item;

public class FireGodIngot extends Item {
    public FireGodIngot(Properties properties) {
        super(properties);
    }
    
    @Override
    public boolean isFireResistant() {
        return true;
    }
}