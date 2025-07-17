package com.huanle.items.armor;

import com.huanle.HuanleMod;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeItem;

import java.util.List;
import java.util.UUID;

public class EnderCrystalArmorItem extends ArmorItem implements IForgeItem {
    private static final UUID ENDER_CRYSTAL_HEALTH_UUID = UUID.fromString("5eb9f2b7-8e7d-4716-9e27-4e13c8c3c23a");
    
    public EnderCrystalArmorItem(ArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return slot == EquipmentSlot.LEGS
            ? HuanleMod.MOD_ID + ":textures/models/armor/ender_crystal_layer_2.png"
            : HuanleMod.MOD_ID + ":textures/models/armor/ender_crystal_layer_1.png";
    }
    public static void tickArmorEffects(ItemStack stack, Level level, Player player) {
        if (!level.isClientSide) {
            boolean hasFullSet = hasFullEnderCrystalArmor(player);
            AttributeInstance maxHealthAttribute = player.getAttribute(Attributes.MAX_HEALTH);
            if (maxHealthAttribute != null) {
                boolean hasHealthModifier = maxHealthAttribute.getModifier(ENDER_CRYSTAL_HEALTH_UUID) != null;
                if (hasFullSet && !hasHealthModifier) {
                    AttributeModifier healthModifier = new AttributeModifier(
                        ENDER_CRYSTAL_HEALTH_UUID,
                        "Ender Crystal armor health bonus",
                        20.0,
                        AttributeModifier.Operation.ADDITION
                    );
                    maxHealthAttribute.addPermanentModifier(healthModifier);
                }
                else if (!hasFullSet && hasHealthModifier) {
                    maxHealthAttribute.removeModifier(ENDER_CRYSTAL_HEALTH_UUID);
                }
            }
        }
    }

    private static boolean hasFullEnderCrystalArmor(Player player) {
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack leggings = player.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
        return !helmet.isEmpty() && helmet.getItem() instanceof EnderCrystalArmorItem &&
               !chestplate.isEmpty() && chestplate.getItem() instanceof EnderCrystalArmorItem &&
               !leggings.isEmpty() && leggings.getItem() instanceof EnderCrystalArmorItem &&
               !boots.isEmpty() && boots.getItem() instanceof EnderCrystalArmorItem;
    }
    
    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltipComponents, TooltipFlag flag) {
        tooltipComponents.add(Component.translatable("item.huanle.ender_crystal_armor.desc"));
        super.appendHoverText(stack, level, tooltipComponents, flag);
    }
}
