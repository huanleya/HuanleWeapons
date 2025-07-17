package com.huanle.materials;

import com.huanle.ModItems;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Tiers;

import java.util.function.Supplier;

public class EnderCrystalTier implements Tier {
    public static final EnderCrystalTier INSTANCE = new EnderCrystalTier();

    private final Supplier<Ingredient> repairIngredient = () ->
        Ingredient.of(ModItems.ENDER_CRYSTAL.get());

    private EnderCrystalTier() {}

    @Override
    public int getUses() {
        return Tiers.DIAMOND.getUses() + 500;
    }

    @Override
    public float getSpeed() {
        return Tiers.DIAMOND.getSpeed();
    }

    @Override
    public float getAttackDamageBonus() {
        return Tiers.DIAMOND.getAttackDamageBonus();
    }

    @Override
    public int getLevel() {
        return Tiers.DIAMOND.getLevel();
    }

    @Override
    public int getEnchantmentValue() {
        return Tiers.DIAMOND.getEnchantmentValue();
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairIngredient.get();
    }
}
