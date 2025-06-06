package com.huanle;

import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;

public class SunGodSword extends SwordItem {
    public SunGodSword(Tier tier, int attackDamageModifier, float attackSpeedModifier, Item.Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (entity instanceof Player player && selected) {
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20, 0, false, false));
        }
    }

    @Override
    public boolean isFireResistant() {
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            AABB aabb = player.getBoundingBox().inflate(8.0D);
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, aabb);
            
            for (LivingEntity entity : entities) {
                if (entity != player) {
                    entity.hurt(level.damageSources().generic(), 5.0F);
                    entity.setSecondsOnFire(5);
                }
            }
        }
        player.getCooldowns().addCooldown(this, 100);
        return InteractionResultHolder.success(itemstack);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltipComponents, TooltipFlag flag) {
        tooltipComponents.add(Component.translatable("item.huanle.sun_god_sword.desc"));
        tooltipComponents.add(Component.translatable("item.huanle.sun_god_sword.desc2"));
        super.appendHoverText(stack, level, tooltipComponents, flag);
    }
}