package com.huanle.items.weapons;

import java.util.List;
import java.util.Random;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import java.util.Map;
import java.util.HashMap;

public class LootingDagger extends SwordItem {
    private final Random random = new Random();
    
    public LootingDagger(@Nonnull Tier tier, int attackDamageModifier, float attackSpeedModifier, @Nonnull Item.Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if (!hasLootingEnchantment(stack)) {
            addLootingEnchantment(stack);
        }
    }

    private boolean hasLootingEnchantment(ItemStack stack) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
        return enchantments.containsKey(Enchantments.MOB_LOOTING) && 
               enchantments.get(Enchantments.MOB_LOOTING) >= 5;
    }
    
    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
    
    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag flag) {
        tooltipComponents.add(Component.translatable("item.huanle.looting_dagger.desc"));
        super.appendHoverText(stack, level, tooltipComponents, flag);
    }
    
    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        super.onCraftedBy(stack, level, player);
        addLootingEnchantment(stack);
    }
    
    private void addLootingEnchantment(ItemStack stack) {
        stack.removeTagKey("Enchantments");
        stack.enchant(Enchantments.MOB_LOOTING, 5);
        if (!stack.hasTag()) {
            stack.setTag(new CompoundTag());
        }
        CompoundTag tag = stack.getTag();
        if (tag != null && !tag.contains("Enchantments")) {
            Map<net.minecraft.world.item.enchantment.Enchantment, Integer> enchantments = new HashMap<>();
            enchantments.put(Enchantments.MOB_LOOTING, 5);
            EnchantmentHelper.setEnchantments(enchantments, stack);
        }
    }
    
    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        addLootingEnchantment(stack);
        return stack;
    }
}
