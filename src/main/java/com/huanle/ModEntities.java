package com.huanle;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(
            ForgeRegistries.ENTITY_TYPES, HuanleMod.MOD_ID);


    public static final RegistryObject<EntityType<ThrowingAxeEntity>> THROWING_AXE = ENTITIES.register(
            "throwing_axe",
            () -> EntityType.Builder.<ThrowingAxeEntity>of(ThrowingAxeEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F) // 实体大小
                    .clientTrackingRange(4) // 客户端追踪范围
                    .updateInterval(20) // 更新间隔
                    .build("huanle:throwing_axe") // 使用字符串直接构造
    );
    

    public static final RegistryObject<EntityType<FlameArrowEntity>> FLAME_ARROW = ENTITIES.register(
            "flame_arrow",
            () -> EntityType.Builder.<FlameArrowEntity>of(FlameArrowEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F) // 实体大小
                    .clientTrackingRange(4) // 客户端追踪范围
                    .updateInterval(20) // 更新间隔
                    .build("huanle:flame_arrow")
    );
    

    public static final RegistryObject<EntityType<MoonlightArcEntity>> MOONLIGHT_ARC = ENTITIES.register(
            "moonlight_arc",
            () -> EntityType.Builder.<MoonlightArcEntity>of(MoonlightArcEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F) // 实体大小
                    .clientTrackingRange(4) // 客户端追踪范围
                    .updateInterval(20) // 更新间隔
                    .build("huanle:moonlight_arc")
    );



    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}