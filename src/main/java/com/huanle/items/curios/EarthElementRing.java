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
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.List;
import java.util.UUID;

public class EarthElementRing extends Item implements ICurioItem {
    
    public EarthElementRing(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.huanle.earth_element_ring.desc1").withStyle(ChatFormatting.AQUA));
        tooltip.add(Component.translatable("item.huanle.earth_element_ring.desc2").withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.translatable("item.huanle.earth_element_ring.desc3").withStyle(ChatFormatting.GREEN));
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

        builder.put(Attributes.ARMOR, 
            new AttributeModifier(UUID.fromString("2b3c4d5e-6f7a-8b9c-0d1e-2f3a4b5c6d7e"), "Earth Ring Armor", 4.0, AttributeModifier.Operation.ADDITION));

        builder.put(Attributes.ARMOR_TOUGHNESS, 
            new AttributeModifier(uuid, "Earth Ring Toughness", 6.0, AttributeModifier.Operation.ADDITION));

        builder.put(com.huanle.ModAttributes.DAMAGE_REDUCTION.get(), 
            new AttributeModifier(UUID.fromString("1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d"), "Earth Ring Damage Reduction", 0.15, AttributeModifier.Operation.MULTIPLY_BASE));
        
        return builder.build();
    }
}