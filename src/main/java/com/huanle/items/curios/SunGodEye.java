package com.huanle.items.curios;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.huanle.ModAttributes;
import com.huanle.capabilities.ManaUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class SunGodEye extends Item implements ICurioItem {
    private int animationTick = 0;
    private int currentAnimation = 0;

    private static final UUID SUN_GOD_EYE_MANA_UUID = UUID.fromString("a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d");

    public SunGodEye(Properties properties) {
        super(properties);
    }


    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag flag) {
        tooltipComponents.add(Component.translatable("item.huanle.sun_god_eye.desc"));
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
            ManaUtils.syncManaToClient(player);
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        LivingEntity livingEntity = slotContext.entity();
        if (livingEntity instanceof Player player && !player.level().isClientSide) {
            float currentMana = ManaUtils.getCurrentMana(player);
            float newMaxMana = ManaUtils.getMaxManaWithAttributes(player);
            if (currentMana > newMaxMana) {
                ManaUtils.setMana(player, newMaxMana);
            }
            ManaUtils.syncManaToClient(player);
        }
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity livingEntity = slotContext.entity();
        if (!livingEntity.level().isClientSide) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 300, 0, false, false));
            livingEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 300, 0, false, false));
        }
    }
    
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> map = LinkedHashMultimap.create();
        map.put(ModAttributes.MAX_MANA.get(), new AttributeModifier(
            SUN_GOD_EYE_MANA_UUID,
            "Sun God Eye mana bonus",
            100.0,
            AttributeModifier.Operation.ADDITION
        ));
        
        return map;
    }
}
