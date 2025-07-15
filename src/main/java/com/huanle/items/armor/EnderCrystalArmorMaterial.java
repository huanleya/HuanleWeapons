package com.huanle.items.armor;

import com.huanle.ModItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public class EnderCrystalArmorMaterial implements ArmorMaterial {
    public static final EnderCrystalArmorMaterial ENDER_CRYSTAL = new EnderCrystalArmorMaterial();

    private static final int[] DURABILITY_PER_SLOT = new int[]{1407, 1592, 1555, 1481};
    private static final int[] PROTECTION_PER_SLOT = new int[]{3, 8, 6, 3};

    private final Supplier<Ingredient> repairIngredient = () ->
        Ingredient.of(ModItems.ENDER_CRYSTAL.get());

    private EnderCrystalArmorMaterial() {}

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return DURABILITY_PER_SLOT[type.getSlot().getIndex()];
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return PROTECTION_PER_SLOT[type.getSlot().getIndex()];
    }

    @Override
    public int getEnchantmentValue() {
        return 15;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_DIAMOND;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairIngredient.get();
    }

    @Override
    public String getName() {
        return "ender_crystal";
    }

    @Override
    public float getToughness() {
        return 3.0F;
    }

    @Override
    public float getKnockbackResistance() {
        return 0.1F;
    }
}
