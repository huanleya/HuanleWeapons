package com.huanle.blocks;

import com.huanle.ModItems;
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

public class EnderCrystalOreBlock extends DropExperienceBlock {

    public EnderCrystalOreBlock(Properties properties) {
        super(properties);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        ItemStack tool = builder.getParameter(LootContextParams.TOOL);
        if (tool == null) {
            return Collections.singletonList(new ItemStack(ModItems.ENDER_CRYSTAL.get()));
        }

        int fortuneLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, tool);

        int count = 1;
        
        if (fortuneLevel > 0) {
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
        ItemStack dropStack = new ItemStack(ModItems.ENDER_CRYSTAL.get(), count);
        return Collections.singletonList(dropStack);
    }
}
