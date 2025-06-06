package com.huanle;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class LightningEnchantment extends Enchantment {
    // 创建一个自定义的附魔类别，适用于剑、三叉戟和斧头
    private static final EnchantmentCategory SWORD_TRIDENT_AXE = EnchantmentCategory.create(
            "sword_trident_axe", 
            item -> item instanceof SwordItem || item instanceof TridentItem || item instanceof AxeItem
    );

    public LightningEnchantment() {
        super(Rarity.RARE, SWORD_TRIDENT_AXE, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMaxLevel() {
        return 3; // 最高等级为3
    }

    @Override
    public int getMinCost(int level) {
        return 10 + (level - 1) * 10; // 每级增加10点附魔消耗
    }

    @Override
    public int getMaxCost(int level) {
        return getMinCost(level) + 15;
    }

    // 当攻击实体时触发附魔效果
    public static void onHitEntity(LivingEntity target, LivingEntity attacker, ItemStack stack, int level) {
        if (!target.level().isClientSide && level > 0) {
            // 获取目标实体的位置
            BlockPos pos = target.blockPosition();
            ServerLevel world = (ServerLevel) target.level();
            
            // 根据附魔等级生成对应数量的闪电
            for (int i = 0; i < level; i++) {
                // 创建闪电实体
                LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(world);
                if (lightning != null) {
                    lightning.moveTo(pos.getX(), pos.getY(), pos.getZ());
                    world.addFreshEntity(lightning);
                }
            }
        }
    }
}