package com.huanle.items.weapons;

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
    private static final int DURABILITY_COST = 5;

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
            if (player.getHealth() < player.getMaxHealth()) {
                player.heal(HEAL_AMOUNT);
                player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
                if (!player.getAbilities().instabuild) {
                    itemstack.hurtAndBreak(DURABILITY_COST, player, (p) -> {
                        p.broadcastBreakEvent(hand);
                    });
                }

                return InteractionResultHolder.success(itemstack);
            }
        }

        return InteractionResultHolder.pass(itemstack);
    }
    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag flag) {
        tooltipComponents.add(Component.translatable("item.huanle.life_staff.desc"));
        tooltipComponents.add(Component.translatable("item.huanle.life_staff.desc2"));
        tooltipComponents.add(Component.translatable("item.huanle.life_staff.desc3"));
        super.appendHoverText(stack, level, tooltipComponents, flag);
    }
}
