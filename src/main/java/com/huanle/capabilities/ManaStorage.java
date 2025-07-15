package com.huanle.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class ManaStorage implements INBTSerializable<CompoundTag> {
    
    private static final String MANA_KEY = "mana";
    private static final String MAX_MANA_KEY = "maxMana";
    private static final String MANA_REGEN_RATE_KEY = "manaRegenRate";
    
    @Override
    public CompoundTag serializeNBT() {
        return new CompoundTag();
    }
    
    @Override
    public void deserializeNBT(CompoundTag nbt) {
    }

    public static CompoundTag writeNBT(IMana mana) {
        CompoundTag tag = new CompoundTag();
        tag.putFloat(MANA_KEY, mana.getMana());
        tag.putFloat(MAX_MANA_KEY, mana.getMaxMana());
        tag.putFloat(MANA_REGEN_RATE_KEY, mana.getManaRegenRate());
        return tag;
    }

    public static void readNBT(IMana mana, CompoundTag tag) {
        if (tag.contains(MAX_MANA_KEY)) {
            mana.setMaxMana(tag.getFloat(MAX_MANA_KEY));
        }
        if (tag.contains(MANA_REGEN_RATE_KEY)) {
            mana.setManaRegenRate(tag.getFloat(MANA_REGEN_RATE_KEY));
        }
        if (tag.contains(MANA_KEY)) {
            mana.setMana(tag.getFloat(MANA_KEY));
        }
    }
}