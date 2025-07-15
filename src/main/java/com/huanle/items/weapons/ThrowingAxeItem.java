package com.huanle.items.weapons;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import java.util.List;
import com.huanle.ModEnchantments;
import com.huanle.enchantments.LightningEnchantment;
import com.huanle.entities.ThrowingAxeEntity;

public class ThrowingAxeItem extends AxeItem {
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public ThrowingAxeItem(Tier tier, float attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
        
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", attackSpeed, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(slot);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        
        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.pass(stack);
        }
        
        // 开始蓄力
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }
    
    @Override
    public void releaseUsing(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity livingEntity, int timeLeft) {
        if (livingEntity instanceof Player player) {
            // 计算蓄力时间
            int useDuration = this.getUseDuration(stack) - timeLeft;
            float powerFactor = getPowerForTime(useDuration);
            
            if (powerFactor < 0.1) {
                return;
            }

            level.playSound(null, player.getX(), player.getY(), player.getZ(), 
                    SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
            
            if (!level.isClientSide) {
                ThrowingAxeEntity axeEntity = new ThrowingAxeEntity(level, player, stack);
                float velocity = 1.0F + (powerFactor * 2.5F);
                axeEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, velocity, 1.0F);
                axeEntity.pickup = player.getAbilities().instabuild ? 
                    AbstractArrow.Pickup.CREATIVE_ONLY : AbstractArrow.Pickup.ALLOWED;
                
                level.addFreshEntity(axeEntity);
            }
            
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
                player.getCooldowns().addCooldown(this, 20);
            }
            
            player.awardStat(Stats.ITEM_USED.get(this));
        }
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.LIGHTNING_STRIKE.get(), stack);
        if (level > 0) {
            LightningEnchantment.onHitEntity(target, attacker, stack, level);
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    public static float getPowerForTime(int time) {
        float powerFactor = (float)time / 20.0F;
        powerFactor = (powerFactor * powerFactor + powerFactor * 2.0F) / 3.0F;
        
        if (powerFactor > 1.0F) {
            powerFactor = 1.0F;
        }
        
        return powerFactor;
    }
    
    @Override
    public int getUseDuration(@NotNull ItemStack stack) {
        return 72000;
    }
    
    @Override
    public UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.BOW;
    }
    
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag flag) {
        tooltipComponents.add(Component.translatable("item.huanle.throwing_axe.desc"));
        tooltipComponents.add(Component.translatable("item.huanle.throwing_axe.charge"));
        super.appendHoverText(stack, level, tooltipComponents, flag);
    }
}
