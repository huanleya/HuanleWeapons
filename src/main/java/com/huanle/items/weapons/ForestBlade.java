package com.huanle.items.weapons;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class ForestBlade extends SwordItem {
    private final Random random = new Random();
    
    public ForestBlade(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }
    
    @Override
    public void onCraftedBy(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull Player player) {

        super.onCraftedBy(stack, level, player);
    }
    
    @Override
    public void inventoryTick(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide && entity instanceof Player player) {

            if (isSelected && player.isInWater()) {

                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 0, false, true));
            }
        }
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }
    
    @Override
    public boolean hurtEnemy(@Nonnull ItemStack stack, @Nonnull LivingEntity target, @Nonnull LivingEntity attacker) {

        boolean result = super.hurtEnemy(stack, target, attacker);
        
        if (random.nextFloat() <= 0.3f) {
            target.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 1));
        }

        if (random.nextFloat() <= 0.03f && attacker instanceof Player) {
            Player player = (Player) attacker;
            player.setHealth(player.getMaxHealth());
        }
        
        return result;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.pass(itemstack);
        }

        if (player.isShiftKeyDown() && player.getHealth() > 10f) {
            player.hurt(player.damageSources().magic(), 10f);
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 1));
            player.getCooldowns().addCooldown(this, 200);
            
            return InteractionResultHolder.success(itemstack);
        }
        
        return InteractionResultHolder.pass(itemstack);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag flag) {
        tooltipComponents.add(Component.translatable("item.huanle.forest_blade.desc"));
        tooltipComponents.add(Component.translatable("item.huanle.forest_blade.desc2"));
        tooltipComponents.add(Component.translatable("item.huanle.forest_blade.desc3"));
        tooltipComponents.add(Component.translatable("item.huanle.forest_blade.desc4"));
        super.appendHoverText(stack, level, tooltipComponents, flag);
    }
}
