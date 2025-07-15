package com.huanle;

import com.huanle.blocks.*;
import com.huanle.blocks.GrinderBlock;
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
                UniformInt.of(3, 7)
            ));
            

    public static final RegistryObject<Block> DEEPSLATE_MAGIC_CRYSTAL_ORE = BLOCKS.register("deepslate_magic_crystal_ore", 
            () -> new DropExperienceBlock(
                BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DEEPSLATE)
                    .requiresCorrectToolForDrops()
                    .strength(6.0F, 7.0F)
                    .sound(SoundType.DEEPSLATE),
                UniformInt.of(4, 8)
            ));

    public static final RegistryObject<Block> MAGIC_CRYSTAL_BLOCK = BLOCKS.register("magic_crystal_block", 
            () -> new Block(
                BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_BLUE)
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.AMETHYST)
                    .requiresCorrectToolForDrops()
                    .lightLevel(state -> 7)
            ));

    public static final RegistryObject<Block> ENDER_CRYSTAL_ORE = BLOCKS.register("ender_crystal_ore", 
            () -> new EnderCrystalOreBlock(
                BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.STONE)
                    .lightLevel(state -> 0)
            ));

    public static final RegistryObject<Block> ENDER_CRYSTAL_BLOCK = BLOCKS.register("ender_crystal_block", 
            () -> new Block(
                BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.AMETHYST)
                    .lightLevel(state -> 10)
            ));

    public static final RegistryObject<Block> GRINDER = BLOCKS.register("grinder",
            () -> new GrinderBlock(
                BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .requiresCorrectToolForDrops()
                    .strength(3.5F, 3.5F)
                    .sound(SoundType.METAL)
            ));
}
