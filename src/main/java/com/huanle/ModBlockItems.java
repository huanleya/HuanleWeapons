package com.huanle;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockItems {

    public static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, HuanleMod.MOD_ID);


    public static final RegistryObject<Item> MAGIC_CRYSTAL_ORE_ITEM = BLOCK_ITEMS.register("magic_crystal_ore",
            () -> new BlockItem(ModBlocks.MAGIC_CRYSTAL_ORE.get(), new Item.Properties()));
            

    public static final RegistryObject<Item> DEEPSLATE_MAGIC_CRYSTAL_ORE_ITEM = BLOCK_ITEMS.register("deepslate_magic_crystal_ore",
            () -> new BlockItem(ModBlocks.DEEPSLATE_MAGIC_CRYSTAL_ORE.get(), new Item.Properties()));
    

    public static final RegistryObject<Item> MAGIC_CRYSTAL_BLOCK_ITEM = BLOCK_ITEMS.register("magic_crystal_block",
            () -> new BlockItem(ModBlocks.MAGIC_CRYSTAL_BLOCK.get(), new Item.Properties()));
            

    public static void register(IEventBus eventBus) {
        BLOCK_ITEMS.register(eventBus);
    }
}
