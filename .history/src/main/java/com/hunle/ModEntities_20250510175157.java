package com.huanle;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(
            ForgeRegistries.ENTITY_TYPES, HunleMod.MOD_ID);

    // 注册投掷斧头实体
    public static final RegistryObject<EntityType<ThrowingAxeEntity>> THROWING_AXE = ENTITIES.register(
            "throwing_axe",
            () -> EntityType.Builder.<ThrowingAxeEntity>of(ThrowingAxeEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F) // 实体大小
                    .clientTrackingRange(4) // 客户端追踪范围
                    .updateInterval(20) // 更新间隔
                    .build(ResourceLocation.of(HunleMod.MOD_ID, "throwing_axe").toString())
    );

    // 在模组初始化时调用此方法来注册所有实体
    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}