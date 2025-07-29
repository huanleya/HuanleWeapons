package com.huanle.items.curios;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.api.type.capability.ICurio;
import net.minecraftforge.common.ForgeMod;
import com.huanle.events.FiveElementRingEventHandler;

import java.util.List;
import java.util.UUID;

public class FiveElementRing extends Item implements ICurioItem {
    
    public FiveElementRing(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.huanle.five_element_ring.separator.gold").withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD));
        tooltip.add(Component.translatable("item.huanle.five_element_ring.gold1").withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.translatable("item.huanle.five_element_ring.gold2").withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.translatable("item.huanle.five_element_ring.gold3").withStyle(ChatFormatting.YELLOW));

        tooltip.add(Component.translatable("item.huanle.five_element_ring.separator.wood").withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD));
        tooltip.add(Component.translatable("item.huanle.five_element_ring.wood1").withStyle(ChatFormatting.GREEN));
        tooltip.add(Component.translatable("item.huanle.five_element_ring.wood2").withStyle(ChatFormatting.GREEN));
        tooltip.add(Component.translatable("item.huanle.five_element_ring.wood3").withStyle(ChatFormatting.GREEN));

        tooltip.add(Component.translatable("item.huanle.five_element_ring.separator.water").withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD));
        tooltip.add(Component.translatable("item.huanle.five_element_ring.water1").withStyle(ChatFormatting.AQUA));
        tooltip.add(Component.translatable("item.huanle.five_element_ring.water2").withStyle(ChatFormatting.AQUA));
        tooltip.add(Component.translatable("item.huanle.five_element_ring.water3").withStyle(ChatFormatting.AQUA));
        tooltip.add(Component.translatable("item.huanle.five_element_ring.water4").withStyle(ChatFormatting.AQUA));

        tooltip.add(Component.translatable("item.huanle.five_element_ring.separator.fire").withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
        tooltip.add(Component.translatable("item.huanle.five_element_ring.fire1").withStyle(ChatFormatting.RED));
        tooltip.add(Component.translatable("item.huanle.five_element_ring.fire2").withStyle(ChatFormatting.RED));
        tooltip.add(Component.translatable("item.huanle.five_element_ring.fire3").withStyle(ChatFormatting.RED));

        tooltip.add(Component.translatable("item.huanle.five_element_ring.separator.earth").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
        tooltip.add(Component.translatable("item.huanle.five_element_ring.earth1").withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.translatable("item.huanle.five_element_ring.earth2").withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.translatable("item.huanle.five_element_ring.earth3").withStyle(ChatFormatting.GOLD));

        tooltip.add(Component.translatable("item.huanle.five_element_ring.separator.ultimate").withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD));
        tooltip.add(Component.translatable("item.huanle.five_element_ring.ultimate1").withStyle(ChatFormatting.LIGHT_PURPLE));
        tooltip.add(Component.translatable("item.huanle.five_element_ring.ultimate2").withStyle(ChatFormatting.LIGHT_PURPLE));
        
        super.appendHoverText(stack, world, tooltip, flag);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.ARMOR_EQUIP_GOLD, 1.0F, 1.0F);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return slotContext.identifier().equals("ring");
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

        if (slotContext.entity() instanceof Player player && FiveElementRingEventHandler.isOnCooldown(player)) {
            return builder.build(); // 返回空的属性修饰符
        }

        builder.put(Attributes.ATTACK_DAMAGE, 
            new AttributeModifier(UUID.fromString("1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d"), "Five Element Ring Attack Damage", 0.20, AttributeModifier.Operation.MULTIPLY_BASE));

        builder.put(Attributes.MAX_HEALTH, 
            new AttributeModifier(UUID.fromString("2b3c4d5e-6f7a-8b9c-0d1e-2f3a4b5c6d7e"), "Five Element Ring Max Health", 20.0, AttributeModifier.Operation.ADDITION));

        builder.put(ForgeMod.SWIM_SPEED.get(), 
            new AttributeModifier(UUID.fromString("3c4d5e6f-7a8b-9c0d-1e2f-3a4b5c6d7e8f"), "Five Element Ring Swim Speed", 0.75, AttributeModifier.Operation.MULTIPLY_BASE));
        builder.put(Attributes.MOVEMENT_SPEED, 
            new AttributeModifier(UUID.fromString("4d5e6f7a-8b9c-0d1e-2f3a-4b5c6d7e8f9a"), "Five Element Ring Movement Speed", 0.10, AttributeModifier.Operation.MULTIPLY_BASE));

        builder.put(Attributes.ARMOR, 
            new AttributeModifier(UUID.fromString("5e6f7a8b-9c0d-1e2f-3a4b-5c6d7e8f9a0b"), "Five Element Ring Armor", 4.0, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ARMOR_TOUGHNESS, 
            new AttributeModifier(UUID.fromString("6f7a8b9c-0d1e-2f3a-4b5c-6d7e8f9a0b1c"), "Five Element Ring Armor Toughness", 6.0, AttributeModifier.Operation.ADDITION));
        builder.put(com.huanle.ModAttributes.DAMAGE_REDUCTION.get(), 
            new AttributeModifier(UUID.fromString("7a8b9c0d-1e2f-3a4b-5c6d-7e8f9a0b1c2d"), "Five Element Ring Damage Reduction", 0.15, AttributeModifier.Operation.MULTIPLY_BASE));
        
        return builder.build();
    }

    @Override
    public boolean isFireResistant() {
        return true;
    }
}