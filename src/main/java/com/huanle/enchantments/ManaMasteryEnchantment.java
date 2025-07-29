package com.huanle.enchantments;

import com.huanle.items.weapons.MagicSword;
import com.huanle.items.weapons.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ManaMasteryEnchantment extends Enchantment {

    // 创建只能附魔到消耗魔力武器的附魔类别
    private static final EnchantmentCategory MANA_WEAPONS = EnchantmentCategory.create(
            "mana_weapons", 
            item -> item instanceof MagicSword
                || item instanceof BaseStaff
                || item instanceof EarthSword
                || item instanceof DragonHunterSword
                || item instanceof VoidAshBlade
                || item instanceof SunGodSword
                || item instanceof MoonlightSword
    );

    public ManaMasteryEnchantment() {
        super(Rarity.RARE, MANA_WEAPONS, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
    }

    @Override
    public int getMaxLevel() {
        return 10;
    }

    @Override
    public int getMinCost(int level) {
        return 10 + (level - 1) * 5;
    }

    @Override
    public int getMaxCost(int level) {
        return getMinCost(level) + 15;
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        // 确保只能附魔到消耗魔力的武器
        return stack.getItem() instanceof MagicSword
            || stack.getItem() instanceof BaseStaff
            || stack.getItem() instanceof EarthSword
            || stack.getItem() instanceof DragonHunterSword
            || stack.getItem() instanceof VoidAshBlade
            || stack.getItem() instanceof SunGodSword
            || stack.getItem() instanceof MoonlightSword;
    }

    public static int getManaReduction(int level) {
        return level;
    }
}