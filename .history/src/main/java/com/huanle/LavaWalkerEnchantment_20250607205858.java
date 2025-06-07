package com.huanle;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LavaWalkerEnchantment extends Enchantment {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final EnchantmentCategory ARMOR_FEET = EnchantmentCategory.ARMOR_FEET;
    
    public LavaWalkerEnchantment() {
        super(Rarity.RARE, ARMOR_FEET, new EquipmentSlot[]{EquipmentSlot.FEET});
    }
    
    @Override
    public int getMaxLevel() {
        return 2;
    }
    
    @Override
    public int getMinCost(int level) {
        return level * 10;
    }
    
    @Override
    public int getMaxCost(int level) {
        return this.getMinCost(level) + 15;
    }
    
    @Override
    public boolean isTreasureOnly() {
        return true;
    }
    
    @Mod.EventBusSubscriber(modid = HuanleMod.MOD_ID)
    public static class LavaWalkerHandler {
        private static final Map<BlockPos, Integer> LAVA_BLOCKS_TO_REVERT = Collections.synchronizedMap(new HashMap<>());

        @SubscribeEvent
        public static void onLivingUpdate(LivingEvent.LivingTickEvent event) {
            LivingEntity entity = event.getEntity();
            Level level = entity.level();
            
            if (level.isClientSide) {
                return;
            }
            
            // 检查实体是否有熔岩行者附魔的靴子
            ItemStack boots = entity.getItemBySlot(EquipmentSlot.FEET);
            int enchantLevel = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.LAVA_WALKER.get(), boots);
            
            if (enchantLevel > 0) {
                try {
                    freezeLava(entity, level, entity.blockPosition(), enchantLevel);
                } catch (Exception e) {
                    LOGGER.error("Error in LavaWalker enchantment: " + e.getMessage());
                }
            }
        }
        
        @SubscribeEvent
        public static void onWorldTick(TickEvent.LevelTickEvent event) {
            if (event.phase == TickEvent.Phase.END && !event.level.isClientSide) {
                updateLavaBlocks((ServerLevel) event.level);
            }
        }
        
        /**
         * 将实体周围的熔岩转换为岩浆块
         */
        private static void freezeLava(LivingEntity entity, Level level, BlockPos pos, int enchantLevel) {
            if (!entity.onGround() || entity.isSpectator()) {
                return;
            }
            
            try {
                // 根据附魔等级确定转换范围
                int range = 1 + enchantLevel;
                BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
                
                for (BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-range, -1, -range), pos.offset(range, -1, range))) {
                    if (blockPos.closerToCenterThan(entity.position(), range)) {
                        mutablePos.set(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                        BlockState blockState = level.getBlockState(mutablePos);
                        
                        // 检查是否是静态熔岩
                        if (blockState.is(Blocks.LAVA) && blockState.getValue(LiquidBlock.LEVEL) == 0) {
                            // 检查上方是否有空间
                            BlockPos abovePos = mutablePos.above();
                            if (level.isEmptyBlock(abovePos) || level.getBlockState(abovePos).getCollisionShape(level, abovePos, CollisionContext.empty()).isEmpty()) {
                                // 再次检查方块状态，确保它仍然是熔岩
                                BlockState currentState = level.getBlockState(mutablePos);
                                if (currentState.is(Blocks.LAVA) && currentState.getValue(LiquidBlock.LEVEL) == 0) {
                                    // 将熔岩转换为岩浆块并更新周围方块
                                    BlockState magmaBlock = Blocks.MAGMA_BLOCK.defaultBlockState();
                                    level.setBlock(mutablePos, magmaBlock, 3);
                                    level.neighborChanged(mutablePos, Blocks.MAGMA_BLOCK, mutablePos);
                                    
                                    // 添加到恢复列表，设置恢复时间（10秒 = 200刻）
                                    LAVA_BLOCKS_TO_REVERT.put(mutablePos.immutable(), 200);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Error in LavaWalker freezeLava: " + e.getMessage());
            }
        }
        
        /**
         * 更新需要恢复的岩浆块
         */
        private static void updateLavaBlocks(ServerLevel level) {
            try {
                if (LAVA_BLOCKS_TO_REVERT.isEmpty()) {
                    return;
                }
                
                Iterator<Map.Entry<BlockPos, Integer>> iterator = LAVA_BLOCKS_TO_REVERT.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<BlockPos, Integer> entry = iterator.next();
                    BlockPos pos = entry.getKey();
                    int timeLeft = entry.getValue() - 1;
                    
                    if (timeLeft <= 0) {
                        // 恢复为熔岩
                        if (level.isLoaded(pos)) {
                            BlockState currentState = level.getBlockState(pos);
                            if (currentState.is(Blocks.MAGMA_BLOCK)) {
                                // 设置为熔岩并更新周围方块
                                BlockState lavaState = Blocks.LAVA.defaultBlockState();
                                level.setBlock(pos, lavaState, 3);
                                level.scheduleTick(pos, Blocks.LAVA, 0);
                                level.neighborChanged(pos, Blocks.LAVA, pos);
                            }
                        }
                        iterator.remove();
                    } else {
                        entry.setValue(timeLeft);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Error in LavaWalker updateLavaBlocks: " + e.getMessage());
            }
        }
    }
}