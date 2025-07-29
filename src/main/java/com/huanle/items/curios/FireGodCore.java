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
import top.theillusivec4.curios.api.CuriosApi;
import net.minecraft.sounds.SoundEvents;
import com.google.common.collect.LinkedHashMultimap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class FireGodCore extends Item implements ICurioItem {
    private static final UUID FIRE_GOD_CORE_ATTACK_UUID = UUID.fromString("9c0d1e2f-5a6b-7c8d-9e0f-1a2b3c4d5e6f");
    private static final UUID FIRE_GOD_CORE_TOUGHNESS_UUID = UUID.fromString("0d1e2f3a-6b7c-8d9e-0f1a-2b3c4d5e6f7a");

    public FireGodCore(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag flag) {
        tooltipComponents.add(Component.translatable("item.huanle.fire_god_core.desc"));
        tooltipComponents.add(Component.translatable("item.huanle.fire_god_core.desc2"));
        tooltipComponents.add(Component.translatable("item.huanle.fire_god_core.desc3"));
        tooltipComponents.add(Component.translatable("item.huanle.fire_god_core.desc4"));
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
            player.displayClientMessage(Component.translatable("item.huanle.fire_god_core.equip"), true);
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        LivingEntity livingEntity = slotContext.entity();
        if (livingEntity instanceof Player player && !player.level().isClientSide) {
            player.displayClientMessage(Component.translatable("item.huanle.fire_god_core.unequip"), true);
        }
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity livingEntity = slotContext.entity();
        if (!livingEntity.level().isClientSide) {
            if (livingEntity.isOnFire()) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 300, 1, false, false));
            }
        }
    }

    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.ARMOR_EQUIP_GOLD, 1.0F, 1.0F);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> map = LinkedHashMultimap.create();
        map.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(
            FIRE_GOD_CORE_ATTACK_UUID,
            "Fire God Core attack bonus",
            0.15,
            AttributeModifier.Operation.MULTIPLY_BASE
        ));
        map.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(
            FIRE_GOD_CORE_TOUGHNESS_UUID,
            "Fire God Core toughness bonus",
            4.0,
            AttributeModifier.Operation.ADDITION
        ));
        UUID slotModifierUUID = UUID.fromString("1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d");
        CuriosApi.addSlotModifier(map, "charm", slotModifierUUID, 1.0, AttributeModifier.Operation.ADDITION);
        
        return map;
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return slotContext.identifier().equals("charm");
    }


}