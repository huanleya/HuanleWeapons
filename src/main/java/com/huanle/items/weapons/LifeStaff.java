package com.huanle.items.weapons;

import com.huanle.ModItems;
import com.huanle.capabilities.ManaUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class LifeStaff extends Item {
    private static final int HEAL_AMOUNT = 6;
    private static final int COOLDOWN_TICKS = 1200;
    private static final float MANA_COST = 10.0f;
    private static final int LIFE_RUNE_COST = 5;

    public LifeStaff(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            if (player.getCooldowns().isOnCooldown(this)) {
                return InteractionResultHolder.fail(itemstack);
            }
            
            // 检查玩家生命值是否已满
            if (player.getHealth() >= player.getMaxHealth()) {
                return InteractionResultHolder.pass(itemstack);
            }
            
            // 检查是否有足够的魔力
            if (!ManaUtils.hasMana(player, MANA_COST)) {
                return InteractionResultHolder.fail(itemstack);
            }
            
            // 检查是否有足够的生命符文
            if (!hasEnoughLifeRunes(player, LIFE_RUNE_COST)) {
                return InteractionResultHolder.fail(itemstack);
            }
            
            // 消耗魔力
            if (!ManaUtils.consumeMana(player, MANA_COST)) {
                return InteractionResultHolder.fail(itemstack);
            }
            
            // 消耗生命符文
            if (!consumeLifeRunes(player, LIFE_RUNE_COST)) {
                return InteractionResultHolder.fail(itemstack);
            }
            
            // 治疗玩家
            player.heal(HEAL_AMOUNT);
            player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
            
            return InteractionResultHolder.success(itemstack);
        }

        return InteractionResultHolder.pass(itemstack);
    }
    
    /**
     * 检查玩家是否有足够的生命符文（包括背包和符文袋）
     */
    private boolean hasEnoughLifeRunes(Player player, int requiredAmount) {
        if (player.getAbilities().instabuild) {
            return true; // 创造模式不需要消耗
        }
        
        int count = 0;
        
        // 检查背包中的生命符文
        for (ItemStack stack : player.getInventory().items) {
            if (stack.is(ModItems.LIFE_RUNE.get())) {
                count += stack.getCount();
            }
        }
        
        // 检查符文袋中的生命符文
        count += countLifeRunesInRuneBags(player);
        
        return count >= requiredAmount;
    }
    
    /**
     * 消耗玩家背包和符文袋中的生命符文
     */
    private boolean consumeLifeRunes(Player player, int requiredAmount) {
        if (player.getAbilities().instabuild) {
            return true; // 创造模式不需要消耗
        }
        
        int remaining = requiredAmount;
        
        // 先消耗背包中的生命符文
        for (ItemStack stack : player.getInventory().items) {
            if (stack.is(ModItems.LIFE_RUNE.get()) && remaining > 0) {
                int toConsume = Math.min(remaining, stack.getCount());
                stack.shrink(toConsume);
                remaining -= toConsume;
                
                if (remaining <= 0) {
                    return true;
                }
            }
        }
        
        // 如果背包中不够，再消耗符文袋中的生命符文
        if (remaining > 0) {
            remaining = consumeLifeRunesFromRuneBags(player, remaining);
        }
        
        return remaining <= 0;
    }
    
    /**
     * 统计符文袋中的生命符文数量
     */
    private int countLifeRunesInRuneBags(Player player) {
        int count = 0;
        for (ItemStack stack : player.getInventory().items) {
            if (stack.is(ModItems.RUNE_BAG.get())) {
                count += countLifeRunesInRuneBag(stack);
            }
        }
        return count;
    }
    
    /**
     * 统计单个符文袋中的生命符文数量
     */
    private int countLifeRunesInRuneBag(ItemStack runeBag) {
        CompoundTag tag = runeBag.getTag();
        if (tag == null || !tag.contains("Items")) {
            return 0;
        }
        
        int count = 0;
        ListTag listTag = tag.getList("Items", 10);
        for (int i = 0; i < listTag.size(); i++) {
            CompoundTag itemTag = listTag.getCompound(i);
            ItemStack stack = ItemStack.of(itemTag);
            if (stack.is(ModItems.LIFE_RUNE.get())) {
                count += stack.getCount();
            }
        }
        return count;
    }
    
    /**
     * 从符文袋中消耗生命符文
     */
    private int consumeLifeRunesFromRuneBags(Player player, int requiredAmount) {
        int remaining = requiredAmount;
        
        for (ItemStack runeBagStack : player.getInventory().items) {
            if (runeBagStack.is(ModItems.RUNE_BAG.get()) && remaining > 0) {
                remaining = consumeLifeRunesFromRuneBag(runeBagStack, remaining);
                if (remaining <= 0) {
                    break;
                }
            }
        }
        
        return remaining;
    }
    
    /**
     * 从单个符文袋中消耗生命符文
     */
    private int consumeLifeRunesFromRuneBag(ItemStack runeBag, int requiredAmount) {
        CompoundTag tag = runeBag.getOrCreateTag();
        if (!tag.contains("Items")) {
            return requiredAmount;
        }
        
        int remaining = requiredAmount;
        ListTag listTag = tag.getList("Items", 10);
        
        for (int i = 0; i < listTag.size() && remaining > 0; i++) {
            CompoundTag itemTag = listTag.getCompound(i);
            ItemStack stack = ItemStack.of(itemTag);
            
            if (stack.is(ModItems.LIFE_RUNE.get())) {
                int toConsume = Math.min(remaining, stack.getCount());
                stack.shrink(toConsume);
                remaining -= toConsume;
                
                if (stack.isEmpty()) {
                    listTag.remove(i);
                    i--; // 调整索引，因为移除了一个元素
                } else {
                    // 更新物品数据
                    CompoundTag newItemTag = new CompoundTag();
                    newItemTag.putByte("Slot", itemTag.getByte("Slot"));
                    stack.save(newItemTag);
                    listTag.set(i, newItemTag);
                }
            }
        }
        
        // 更新符文袋的NBT数据
        tag.put("Items", listTag);
        
        return remaining;
    }
    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag flag) {
        tooltipComponents.add(Component.translatable("item.huanle.life_staff.desc"));
        tooltipComponents.add(Component.translatable("item.huanle.life_staff.desc2"));
        tooltipComponents.add(Component.translatable("tooltip.huanle.mana_cost", (int)MANA_COST));
        tooltipComponents.add(Component.translatable("tooltip.huanle.life_rune_cost", LIFE_RUNE_COST));
        super.appendHoverText(stack, level, tooltipComponents, flag);
    }
}
