package com.huanle;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.Collections;
import java.util.List;

/**
 * 末影水晶矿石方块
 * 挖掘时直接掉落末影水晶物品，受时运附魔影响
 */
public class EnderCrystalOreBlock extends DropExperienceBlock {

    public EnderCrystalOreBlock(Properties properties) {
        super(properties);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        // 获取挖掘工具
        ItemStack tool = builder.getParameter(LootContextParams.TOOL);
        if (tool == null) {
            // 如果没有工具，返回一个末影水晶
            return Collections.singletonList(new ItemStack(ModItems.ENDER_CRYSTAL.get()));
        }

        // 获取时运等级
        int fortuneLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, tool);
        
        // 根据时运等级计算掉落数量
        int count = 1; // 基础掉落一个
        
        if (fortuneLevel > 0) {
            // 时运I: 33%概率额外掉落1个
            // 时运II: 50%概率额外掉落1个，25%概率额外掉落2个
            // 时运III: 60%概率额外掉落1个，30%概率额外掉落2个，20%概率额外掉落3个
            RandomSource random = RandomSource.create();
            float chance = random.nextFloat();
            
            if (fortuneLevel == 1) {
                if (chance < 0.33f) {
                    count += 1;
                }
            } else if (fortuneLevel == 2) {
                if (chance < 0.5f) {
                    count += 1;
                } else if (chance < 0.75f) {
                    count += 2;
                }
            } else if (fortuneLevel >= 3) {
                if (chance < 0.6f) {
                    count += 1;
                } else if (chance < 0.9f) {
                    count += 2;
                } else {
                    count += 3;
                }
            }
        }
        
        // 创建对应数量的末影水晶物品堆叠
        ItemStack dropStack = new ItemStack(ModItems.ENDER_CRYSTAL.get(), count);
        return Collections.singletonList(dropStack);
    }
}