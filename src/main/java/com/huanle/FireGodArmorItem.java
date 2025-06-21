package com.huanle;

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
        // 根据盔甲部位返回不同的纹理路径
        // 头盔、胸甲和靴子使用 layer 1，护腿使用 layer 2
        return slot == EquipmentSlot.LEGS
            ? HuanleMod.MOD_ID + ":textures/models/armor/fire_god_layer_2.png"
            : HuanleMod.MOD_ID + ":textures/models/armor/fire_god_layer_1.png";
    }

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        if (!level.isClientSide) {
            // 检查玩家是否穿戴了完整的炎神套装
            boolean hasFullSet = hasFullFireGodArmor(player);
            // 检查玩家是否在岩浆中
            boolean isInLava = player.isInLava();

            // 穿戴全套时提供防火效果，无论在哪里
            if (hasFullSet) {
                // 给予火焰抗性效果（持续时间20tick=1秒）
                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20, 0, false, false));
            }
            
            // 在熔岩中额外提供力量II效果
            if (hasFullSet && isInLava) {
                // 给予力量II效果（效果ID为5，等级1代表II级，持续时间20tick=1秒）
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20, 1, false, false));
            }
        }
    }

    private boolean hasFullFireGodArmor(Player player) {
        // 检查所有四个装备槽位
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack leggings = player.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);

        // 检查每个槽位是否都装备了炎神盔甲
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