package com.huanle.items.curios;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.api.type.capability.ICurio;
import net.minecraft.sounds.SoundEvents;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class LavaNecklace extends Item implements ICurioItem {
    private static final UUID LAVA_NECKLACE_HEALTH_UUID = UUID.fromString("7a8b9c2d-3e4f-5a6b-7c8d-9e0f1a2b3c4d");
    private static final UUID LAVA_NECKLACE_ATTACK_UUID = UUID.fromString("8b9c0d1e-4f5a-6b7c-8d9e-0f1a2b3c4d5e");

    public LavaNecklace(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag flag) {
        tooltipComponents.add(Component.translatable("item.huanle.lava_necklace.desc"));
        super.appendHoverText(stack, level, tooltipComponents, flag);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
    
    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        LivingEntity livingEntity = slotContext.entity();
        if (livingEntity instanceof Player player && !player.level().isClientSide) {
            player.displayClientMessage(Component.translatable("item.huanle.lava_necklace.equip"), true);
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        LivingEntity livingEntity = slotContext.entity();
        if (livingEntity instanceof Player player && !player.level().isClientSide) {
            player.displayClientMessage(Component.translatable("item.huanle.lava_necklace.unequip"), true);
        }
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity livingEntity = slotContext.entity();
        if (!livingEntity.level().isClientSide) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 300, 0, false, false));

            if (livingEntity.isInLava()) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 300, 0, false, false));
            }
        }
    }

    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.ARMOR_EQUIP_GOLD, 1.0F, 1.0F);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

        builder.put(Attributes.MAX_HEALTH, new AttributeModifier(
            LAVA_NECKLACE_HEALTH_UUID,
            "Lava Necklace health bonus",
            4.0,
            AttributeModifier.Operation.ADDITION
        ));

        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(
            LAVA_NECKLACE_ATTACK_UUID,
            "Lava Necklace attack bonus",
            0.1,
            AttributeModifier.Operation.MULTIPLY_BASE
        ));
        
        return builder.build();
    }
}