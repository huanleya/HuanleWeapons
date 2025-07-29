package com.huanle.items.weapons;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.huanle.entities.MoonlightArcEntity;
import com.huanle.capabilities.ManaUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

public class MoonlightSword extends SwordItem {
    private static final int COOLDOWN_TICKS = 40;
    private static final float MANA_COST = 10.0f;

    public MoonlightSword(@Nonnull Tier tier, int attackDamageModifier, float attackSpeedModifier, @Nonnull Item.Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }

    @Override
    public @Nonnull InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        
        if (!level.isClientSide) {
            // 检查魔力是否足够
            if (!ManaUtils.hasMana(player, MANA_COST)) {
                return InteractionResultHolder.fail(itemstack);
            }
            
            // 消耗魔力
            if (!ManaUtils.consumeManaWithEnchantment(player, MANA_COST, itemstack)) {
                return InteractionResultHolder.fail(itemstack);
            }

            MoonlightArcEntity arcEntity = new MoonlightArcEntity(level, player);
            arcEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(arcEntity);
            

            level.playSound(null, player.getX(), player.getY(), player.getZ(), 
                    SoundEvents.ENDER_PEARL_THROW, SoundSource.PLAYERS, 
                    0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
            

            player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);

            if (!player.getAbilities().instabuild) {
                itemstack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
            }
        }
        
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }

    @Override
    public boolean isFireResistant() {
        return true;
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag flag) {
        tooltipComponents.add(Component.translatable("item.huanle.moonlight_sword.desc"));
        tooltipComponents.add(Component.translatable("item.huanle.moonlight_sword.desc2"));
        super.appendHoverText(stack, level, tooltipComponents, flag);
    }
}
