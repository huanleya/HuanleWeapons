package com.huanle.items.weapons;

import com.huanle.capabilities.ManaUtils;
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

/**
 * 基础法杖类
 * 所有法杖都应该继承这个类
 */
public abstract class BaseStaff extends Item {
    
    protected final float manaCost;
    protected final int cooldownTicks;
    
    public BaseStaff(Properties properties, float manaCost, int cooldownTicks) {
        super(properties);
        this.manaCost = manaCost;
        this.cooldownTicks = cooldownTicks;
    }
    
    @Override
    public @Nonnull InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        
        if (!level.isClientSide) {
            try {
                // 检查是否有足够的魔力
                if (ManaUtils.hasMana(player, manaCost)) {
                    // 消耗魔力（考虑魔力精通附魔）
                    if (ManaUtils.consumeManaWithEnchantment(player, manaCost, itemStack)) {
                        // 执行法杖特殊能力
                        if (performStaffAbility(level, player, itemStack)) {
                            // 消耗耐久
                            itemStack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
                            
                            // 设置冷却时间
                            player.getCooldowns().addCooldown(this, cooldownTicks);
                            
                            return InteractionResultHolder.success(itemStack);
                        }
                    }
                } else {
                    // 魔力不足提示
                    player.sendSystemMessage(Component.translatable("message.huanle.insufficient_mana"));
                }
            } catch (Exception e) {
                // 记录错误
                System.err.println("Error using staff: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        return InteractionResultHolder.fail(itemStack);
    }
    
    /**
     * 执行法杖的特殊能力
     * 子类需要重写这个方法来实现具体的法杖效果
     * 
     * @param level 世界
     * @param player 玩家
     * @param itemStack 物品堆叠
     * @return 是否成功执行能力
     */
    protected abstract boolean performStaffAbility(Level level, Player player, ItemStack itemStack);
    
    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag flag) {
        // 添加魔力消耗信息
        tooltipComponents.add(Component.translatable("tooltip.huanle.mana_cost", (int)manaCost));
        super.appendHoverText(stack, level, tooltipComponents, flag);
    }
    
    /**
     * 获取魔力消耗
     */
    public float getManaCost() {
        return manaCost;
    }
    
    /**
     * 获取冷却时间
     */
    public int getCooldownTicks() {
        return cooldownTicks;
    }
}