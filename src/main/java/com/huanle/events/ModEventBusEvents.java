package com.huanle.events;

import com.huanle.HuanleMod;
import com.huanle.ModAttributes;
import com.huanle.ModEntities;
import com.huanle.entities.MysteriousMerchantEntity;
import com.huanle.entities.SkeletonMinerEntity;
import com.huanle.entities.ZombieMinerEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HuanleMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.ZOMBIE_MINER.get(), ZombieMinerEntity.createAttributes().build());
        event.put(ModEntities.SKELETON_MINER.get(), SkeletonMinerEntity.createAttributes().build());
        event.put(ModEntities.MYSTERIOUS_MERCHANT.get(), MysteriousMerchantEntity.createAttributes().build());
    }
}