package com.huanle;

import com.huanle.blockentities.GrinderBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * 方块实体注册类
 */
public class ModBlockEntities {
    
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = 
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, HuanleMod.MOD_ID);
    
    public static final RegistryObject<BlockEntityType<GrinderBlockEntity>> GRINDER = 
            BLOCK_ENTITIES.register("grinder", () ->
                    BlockEntityType.Builder.of(GrinderBlockEntity::new, ModBlocks.GRINDER.get()).build(null));
    
    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}