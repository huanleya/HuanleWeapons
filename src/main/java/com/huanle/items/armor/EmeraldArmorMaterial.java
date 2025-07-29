package com.huanle.items.armor;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public class EmeraldArmorMaterial implements ArmorMaterial {
    public static final EmeraldArmorMaterial EMERALD = new EmeraldArmorMaterial();
    private static final int[] DURABILITY_PER_SLOT = new int[]{164, 328, 295, 229};
    private static final int[] PROTECTION_PER_SLOT = new int[]{3, 8, 6, 3};

    private final Supplier<Ingredient> repairIngredient = () ->
        Ingredient.of(Items.EMERALD);

    private EmeraldArmorMaterial() {}

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
        return 10;
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
        return "emerald";
    }

    @Override
    public float getToughness() {
        return 2.0F;
    }

    @Override
    public float getKnockbackResistance() {
        return 0.0F;
    }
}
