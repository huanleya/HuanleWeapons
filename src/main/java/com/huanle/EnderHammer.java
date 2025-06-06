package com.huanle;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class EnderHammer extends DiggerItem {
    private final Random random = new Random();
    

    private static final TagKey<Block> MINEABLE_WITH_HAMMER = BlockTags.MINEABLE_WITH_PICKAXE;
    
    public EnderHammer(Tier tier, int attackDamageModifier, float attackSpeedModifier, Item.Properties properties) {
        super(attackDamageModifier, attackSpeedModifier, tier, MINEABLE_WITH_HAMMER, properties);
    }
    
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean result = super.hurtEnemy(stack, target, attacker);
        

        if (random.nextFloat() <= 0.2f) {
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 2)); // 缓慢III，3秒
            target.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 60, 0)); // 恶心I，3秒
            

            if (target.level() instanceof ServerLevel serverLevel) {
                for (int i = 0; i < 10; i++) {
                    double x = target.getX() + (random.nextDouble() - 0.5) * 2;
                    double y = target.getY() + random.nextDouble() * 2;
                    double z = target.getZ() + (random.nextDouble() - 0.5) * 2;
                    serverLevel.sendParticles(ParticleTypes.PORTAL, x, y, z, 1, 0, 0, 0, 0.1);
                }
            }
        }
        
        return result;
    }
    
    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        if (miningEntity instanceof Player player && !level.isClientSide) {

            mine3x3Area(stack, level, state, pos, player);
        }
        return super.mineBlock(stack, level, state, pos, miningEntity);
    }
    
    private void mine3x3Area(ItemStack stack, Level level, BlockState centerState, BlockPos centerPos, Player player) {

        Direction facing = player.getDirection();
        List<BlockPos> blocksToMine = new ArrayList<>();
        

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                BlockPos targetPos;
                
                switch (facing) {
                    case NORTH, SOUTH -> targetPos = centerPos.offset(x, y, 0);
                    case EAST, WEST -> targetPos = centerPos.offset(0, y, x);
                    case UP, DOWN -> targetPos = centerPos.offset(x, 0, y);
                    default -> targetPos = centerPos.offset(x, y, 0);
                }
                
                if (!targetPos.equals(centerPos)) {
                    blocksToMine.add(targetPos);
                }
            }
        }
        

        for (BlockPos pos : blocksToMine) {
            BlockState state = level.getBlockState(pos);
            

            if (state.is(MINEABLE_WITH_HAMMER) && 
                !state.isAir() && 
                state.getDestroySpeed(level, pos) >= 0 &&
                player.hasCorrectToolForDrops(state)) {
                

                level.destroyBlock(pos, true, player);
                

                stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                

                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.PORTAL, 
                        pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 
                        3, 0.3, 0.3, 0.3, 0.1);
                }
            }
        }
    }
    
    @Override
    public void inventoryTick(ItemStack stack, Level level, net.minecraft.world.entity.Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        

        if (isSelected && entity instanceof Player player && level.isClientSide) {

            if (level.getGameTime() % 5 == 0) {
                Vec3 playerPos = player.position();
                double x = playerPos.x + (random.nextDouble() - 0.5) * 1.5;
                double y = playerPos.y + 1 + random.nextDouble() * 0.5;
                double z = playerPos.z + (random.nextDouble() - 0.5) * 1.5;
                
                level.addParticle(ParticleTypes.PORTAL, x, y, z, 
                    (random.nextDouble() - 0.5) * 0.1, 
                    random.nextDouble() * 0.1, 
                    (random.nextDouble() - 0.5) * 0.1);
            }
        }
    }
    
    @Override
    public boolean isFireResistant() {
        return true;
    }
    
    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltipComponents, TooltipFlag flag) {
        tooltipComponents.add(Component.translatable("item.huanle.ender_hammer.desc"));
        tooltipComponents.add(Component.translatable("item.huanle.ender_hammer.desc2"));
        super.appendHoverText(stack, level, tooltipComponents, flag);
    }
}