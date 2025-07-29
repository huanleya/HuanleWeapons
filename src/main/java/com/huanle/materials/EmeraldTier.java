package com.huanle.materials;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tiers;

import java.util.function.Supplier;

public class EmeraldTier implements Tier {
    public static final EmeraldTier INSTANCE = new EmeraldTier();

    private final Supplier<Ingredient> repairIngredient = () ->
            Ingredient.of(Items.EMERALD);

    private EmeraldTier() {}

    @Override
    public int getUses() {
        return Tiers.DIAMOND.getUses() - 500;
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
        return Tiers.DIAMOND.getEnchantmentValue() + 5;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairIngredient.get();
    }
}
