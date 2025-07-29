package com.huanle.enchantments;

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
    private static final EnchantmentCategory SWORD_TRIDENT_AXE = EnchantmentCategory.create(
            "sword_trident_axe", 
            item -> item instanceof SwordItem || item instanceof TridentItem || item instanceof AxeItem
    );

    public LightningEnchantment() {
        super(Rarity.RARE, SWORD_TRIDENT_AXE, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinCost(int level) {
        return 10 + (level - 1) * 10;
    }

    @Override
    public int getMaxCost(int level) {
        return getMinCost(level) + 15;
    }

    public static void onHitEntity(LivingEntity target, LivingEntity attacker, ItemStack stack, int level) {
        if (!target.level().isClientSide && level > 0) {

            BlockPos pos = target.blockPosition();
            ServerLevel world = (ServerLevel) target.level();

            for (int i = 0; i < level; i++) {
                LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(world);
                if (lightning != null) {
                    lightning.moveTo(pos.getX(), pos.getY(), pos.getZ());
                    world.addFreshEntity(lightning);
                }
            }
        }
    }
}
