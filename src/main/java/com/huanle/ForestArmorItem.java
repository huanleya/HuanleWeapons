package com.huanle;

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
        // 根据盔甲部位返回不同的纹理路径
        // 头盔、胸甲和靴子使用 layer 1，护腿使用 layer 2
        return slot == EquipmentSlot.LEGS 
            ? HuanleMod.MOD_ID + ":textures/models/armor/forest_layer_2.png"
            : HuanleMod.MOD_ID + ":textures/models/armor/forest_layer_1.png";
    }

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        if (!level.isClientSide) {
            // 检查玩家是否穿戴了完整的森林套装
            boolean hasFullSet = hasFullForestArmor(player);
            // 检查玩家是否在水中
            boolean isInWater = player.isInWater();

            if (hasFullSet && isInWater) {
                // 给予生命恢复II效果（效果ID为10，等级1代表II级，持续时间20tick=1秒）
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20, 1, false, false));
            }
        }
    }

    private boolean hasFullForestArmor(Player player) {
        // 检查所有四个装备槽位
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack leggings = player.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
        
        // 检查每个槽位是否都装备了森林盔甲
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
