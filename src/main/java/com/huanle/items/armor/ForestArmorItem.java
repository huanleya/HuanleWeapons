package com.huanle.items.armor;

import com.huanle.HuanleMod;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeItem;

import java.util.List;

public class ForestArmorItem extends ArmorItem implements IForgeItem {

    public ForestArmorItem(Type type, Properties properties) {
        super(ForestArmorMaterial.INSTANCE, type, properties);
    }
    
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return slot == EquipmentSlot.LEGS 
            ? HuanleMod.MOD_ID + ":textures/models/armor/forest_layer_2.png"
            : HuanleMod.MOD_ID + ":textures/models/armor/forest_layer_1.png";
    }

    public static void tickArmorEffects(ItemStack stack, Level level, Player player) {
        if (!level.isClientSide) {
            boolean hasFullSet = hasFullForestArmor(player);
            boolean isInWater = player.isInWater();

            if (hasFullSet && isInWater) {
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20, 1, false, false));
            }
        }
    }

    private static boolean hasFullForestArmor(Player player) {
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack leggings = player.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
        return !helmet.isEmpty() && helmet.getItem() instanceof ForestArmorItem &&
               !chestplate.isEmpty() && chestplate.getItem() instanceof ForestArmorItem &&
               !leggings.isEmpty() && leggings.getItem() instanceof ForestArmorItem &&
               !boots.isEmpty() && boots.getItem() instanceof ForestArmorItem;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltipComponents, TooltipFlag flag) {
        tooltipComponents.add(Component.translatable(  "item.huanle.forest_armor.desc"));
        super.appendHoverText(stack, level, tooltipComponents, flag);
    }
}
