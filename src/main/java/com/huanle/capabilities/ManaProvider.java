package com.huanle.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ManaProvider implements ICapabilitySerializable<CompoundTag> {
    
    private final IMana mana = new Mana();
    private final LazyOptional<IMana> manaOptional = LazyOptional.of(() -> mana);
    
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ManaCapability.MANA_CAPABILITY) {
            return manaOptional.cast();
        }
        return LazyOptional.empty();
    }
    
    @Override
    public CompoundTag serializeNBT() {
        return ManaStorage.writeNBT(mana);
    }
    
    @Override
    public void deserializeNBT(CompoundTag nbt) {
        ManaStorage.readNBT(mana, nbt);
    }

    public void invalidate() {
        manaOptional.invalidate();
    }

    public IMana getMana() {
        return mana;
    }
}