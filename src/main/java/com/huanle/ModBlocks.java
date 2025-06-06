package com.huanle;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, HuanleMod.MOD_ID);


    public static final RegistryObject<Block> MAGIC_CRYSTAL_ORE = BLOCKS.register("magic_crystal_ore", 
            () -> new DropExperienceBlock(
                BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.STONE),
                UniformInt.of(3, 7)  // 掉落经验值范围
            ));
            

    public static final RegistryObject<Block> DEEPSLATE_MAGIC_CRYSTAL_ORE = BLOCKS.register("deepslate_magic_crystal_ore", 
            () -> new DropExperienceBlock(
                BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DEEPSLATE)
                    .requiresCorrectToolForDrops()
                    .strength(6.0F, 7.0F)  // 比普通版本更硬
                    .sound(SoundType.DEEPSLATE),
                UniformInt.of(4, 8)  // 掉落经验值范围稍高
            ));

    public static final RegistryObject<Block> MAGIC_CRYSTAL_BLOCK = BLOCKS.register("magic_crystal_block", 
            () -> new Block(
                BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_BLUE)
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.AMETHYST)
                    .requiresCorrectToolForDrops()
                    .lightLevel(state -> 7)  // 发光效果
            ));
}
