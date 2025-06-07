package com.huanle;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, HuanleMod.MOD_ID);

    // 魔法水晶矿石 - 基于铁矿石的属性
    public static final RegistryObject<Block> MAGIC_CRYSTAL_ORE = BLOCKS.register("magic_crystal_ore",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)
                    .strength(3.0f, 3.0f) // 设置挖掘难度和爆炸抗性
                    .requiresCorrectToolForDrops())); // 需要正确的工具才能掉落

    // 深层魔法水晶矿石 - 基于深层铁矿石的属性
    public static final RegistryObject<Block> DEEPSLATE_MAGIC_CRYSTAL_ORE = BLOCKS.register("deepslate_magic_crystal_ore",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE)
                    .strength(4.5f, 3.0f) // 深层矿石通常更难挖掘
                    .requiresCorrectToolForDrops())); // 需要正确的工具才能掉落

    // 魔法水晶块 - 基于钻石块的属性
    public static final RegistryObject<Block> MAGIC_CRYSTAL_BLOCK = BLOCKS.register("magic_crystal_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIAMOND_BLOCK)
                    .strength(5.0f, 6.0f) // 设置较高的强度和爆炸抗性
                    .requiresCorrectToolForDrops())); // 需要正确的工具才能掉落
}
