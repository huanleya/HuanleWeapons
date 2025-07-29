package com.huanle;

import com.huanle.entities.*;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = HuanleMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(
            ForgeRegistries.ENTITY_TYPES, HuanleMod.MOD_ID);


    public static final RegistryObject<EntityType<ThrowingAxeEntity>> THROWING_AXE = ENTITIES.register(
            "throwing_axe",
            () -> EntityType.Builder.<ThrowingAxeEntity>of(ThrowingAxeEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("huanle:throwing_axe")
    );
    

    public static final RegistryObject<EntityType<FlameArrowEntity>> FLAME_ARROW = ENTITIES.register(
            "flame_arrow",
            () -> EntityType.Builder.<FlameArrowEntity>of(FlameArrowEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("huanle:flame_arrow")
    );
    

    public static final RegistryObject<EntityType<MoonlightArcEntity>> MOONLIGHT_ARC = ENTITIES.register(
            "moonlight_arc",
            () -> EntityType.Builder.<MoonlightArcEntity>of(MoonlightArcEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("huanle:moonlight_arc")
    );
    
    public static final RegistryObject<EntityType<FireballEntity>> FIREBALL = ENTITIES.register(
            "fireball",
            () -> EntityType.Builder.<FireballEntity>of(FireballEntity::new, MobCategory.MISC)
                    .sized(0.3125F, 0.3125F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("huanle:fireball")
    );
    
    // 僵尸矿工实体
    public static final RegistryObject<EntityType<ZombieMinerEntity>> ZOMBIE_MINER = ENTITIES.register(
            "zombie_miner",
            () -> EntityType.Builder.<ZombieMinerEntity>of(ZombieMinerEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .updateInterval(3)
                    .build("huanle:zombie_miner")
    );
    
    // 骷髅矿工实体
    public static final RegistryObject<EntityType<SkeletonMinerEntity>> SKELETON_MINER = ENTITIES.register(
            "skeleton_miner",
            () -> EntityType.Builder.<SkeletonMinerEntity>of(SkeletonMinerEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.99F)
                    .clientTrackingRange(8)
                    .updateInterval(3)
                    .build("huanle:skeleton_miner")
    );
    
    // 神秘商人实体
    public static final RegistryObject<EntityType<MysteriousMerchantEntity>> MYSTERIOUS_MERCHANT = ENTITIES.register(
            "mysterious_merchant",
            () -> EntityType.Builder.<MysteriousMerchantEntity>of(MysteriousMerchantEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(10)
                    .updateInterval(3)
                    .build("huanle:mysterious_merchant")
    );
    
    // 魔力瓶实体
    public static final RegistryObject<EntityType<ManaBottleEntity>> MANA_BOTTLE = ENTITIES.register(
            "mana_bottle",
            () -> EntityType.Builder.<ManaBottleEntity>of(ManaBottleEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("huanle:mana_bottle")
    );



    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
    

    
    public static void registerSpawnPlacements() {
        SpawnPlacements.register(ZOMBIE_MINER.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ZombieMinerEntity::checkZombieMinerSpawnRules);
        SpawnPlacements.register(SKELETON_MINER.get(), SpawnPlacements.Type.ON_GROUND, 
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SkeletonMinerEntity::checkSkeletonMinerSpawnRules);
    }
}