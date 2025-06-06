package com.huanle;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class HunleSword extends SwordItem {
    public HunleSword(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }

    @Override
    public boolean hurtEnemy(net.minecraft.world.item.ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!target.level().isClientSide) {
            // 获取目标实体的位置
            BlockPos pos = target.blockPosition();
            ServerLevel world = (ServerLevel) target.level();
            
            // 创建闪电实体
            LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(world);
            if (lightning != null) {
                lightning.moveTo(pos.getX(), pos.getY(), pos.getZ());
                world.addFreshEntity(lightning);
            }
        }
        return super.hurtEnemy(stack, target, attacker);
    }
}