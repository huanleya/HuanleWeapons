package com.huanle.items.armor;

import com.huanle.HuanleMod;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeItem;

import java.util.List;

public class FireGodArmorItem extends ArmorItem implements IForgeItem {

    public FireGodArmorItem(ArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return slot == EquipmentSlot.LEGS
            ? HuanleMod.MOD_ID + ":textures/models/armor/fire_god_layer_2.png"
            : HuanleMod.MOD_ID + ":textures/models/armor/fire_god_layer_1.png";
    }

    public static void tickArmorEffects(ItemStack stack, Level level, Player player) {
        if (!level.isClientSide) {
            boolean hasFullSet = hasFullFireGodArmor(player);
            boolean isInLava = player.isInLava();
            if (hasFullSet) {
                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20, 0, false, false));
            }
            if (hasFullSet && isInLava) {
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20, 1, false, false));
            }
        }
    }

    private static boolean hasFullFireGodArmor(Player player) {
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack leggings = player.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
        return !helmet.isEmpty() && helmet.getItem() instanceof FireGodArmorItem &&
               !chestplate.isEmpty() && chestplate.getItem() instanceof FireGodArmorItem &&
               !leggings.isEmpty() && leggings.getItem() instanceof FireGodArmorItem &&
               !boots.isEmpty() && boots.getItem() instanceof FireGodArmorItem;
    }
    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltipComponents, TooltipFlag flag) {
        tooltipComponents.add(Component.translatable(  "item.huanle.fire_god_armor.desc"));
        super.appendHoverText(stack, level, tooltipComponents, flag);
    }
}
