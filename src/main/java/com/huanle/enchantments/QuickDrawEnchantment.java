package com.huanle.enchantments;

import com.huanle.ModEnchantments;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class QuickDrawEnchantment extends Enchantment {

    private static final EnchantmentCategory BOW_ONLY = EnchantmentCategory.create(
            "bow_only", 
            item -> item instanceof BowItem
    );

    public QuickDrawEnchantment() {
        super(Rarity.UNCOMMON, BOW_ONLY, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinCost(int level) {
        return 5 + (level - 1) * 8;
    }

    @Override
    public int getMaxCost(int level) {
        return getMinCost(level) + 20;
    }
    

    public static int getDrawTimeReduction(ItemStack stack) {
        int level = stack.getEnchantmentLevel(ModEnchantments.QUICK_DRAW.get());
        if (level > 0) {
            return level * 6;
        }
        return 0;
    }
}
