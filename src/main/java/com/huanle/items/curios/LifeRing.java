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

public class LifeRing extends Item implements ICurioItem {
    private static final UUID LIFE_RING_SPEED_UUID = UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B");
    private static final UUID LIFE_RING_LOOTING_UUID = UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6C");
    private static final UUID LIFE_RING_FORTUNE_UUID = UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6D");

    public LifeRing(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag flag) {
        tooltipComponents.add(Component.translatable("item.huanle.life_ring.desc1"));
        tooltipComponents.add(Component.translatable("item.huanle.life_ring.desc2"));
        tooltipComponents.add(Component.translatable("item.huanle.life_ring.desc3"));
        tooltipComponents.add(Component.translatable("item.huanle.life_ring.desc4"));
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
            player.displayClientMessage(Component.translatable("item.huanle.life_ring.equip"), true);
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        LivingEntity livingEntity = slotContext.entity();
        if (livingEntity instanceof Player player && !player.level().isClientSide) {
            player.displayClientMessage(Component.translatable("item.huanle.life_ring.unequip"), true);
        }
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity livingEntity = slotContext.entity();
        if (!livingEntity.level().isClientSide) {
            if (livingEntity.tickCount % 200 == 0) { // 200 ticks = 10秒
                livingEntity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0, false, false));
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
        builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(
            LIFE_RING_SPEED_UUID,
            "Life Ring speed bonus",
            0.05,
            AttributeModifier.Operation.MULTIPLY_BASE
        ));
        
        // 添加抢夺等级
        builder.put(com.huanle.ModAttributes.LOOTING_LEVEL.get(), new AttributeModifier(
            LIFE_RING_LOOTING_UUID,
            "Life Ring looting bonus",
            1.0,
            AttributeModifier.Operation.ADDITION
        ));
        
        // 添加时运等级
        builder.put(com.huanle.ModAttributes.FORTUNE_LEVEL.get(), new AttributeModifier(
            LIFE_RING_FORTUNE_UUID,
            "Life Ring fortune bonus",
            1.0,
            AttributeModifier.Operation.ADDITION
        ));
        
        return builder.build();
    }
    
    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }
    
    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return slotContext.identifier().equals("ring");
    }
}