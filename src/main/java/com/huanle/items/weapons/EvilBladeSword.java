package com.huanle.items.weapons;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class EvilBladeSword extends SwordItem {
    
    public EvilBladeSword(@Nonnull Tier tier, int attackDamageModifier, float attackSpeedModifier, @Nonnull Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }
    
    @Override
    public boolean isFireResistant() {
        return true;
    }
}
