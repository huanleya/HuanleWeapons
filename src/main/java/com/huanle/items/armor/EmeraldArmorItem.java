package com.huanle.items.armor;

import com.huanle.HuanleMod;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeItem;

public class EmeraldArmorItem extends ArmorItem implements IForgeItem {

    public EmeraldArmorItem(ArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return slot == EquipmentSlot.LEGS
            ? HuanleMod.MOD_ID + ":textures/models/armor/emerald_layer_2.png"
            : HuanleMod.MOD_ID + ":textures/models/armor/emerald_layer_1.png";
    }
}
