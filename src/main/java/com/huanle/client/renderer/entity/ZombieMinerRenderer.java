package com.huanle.client.renderer.entity;

import com.huanle.HuanleMod;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.resources.ResourceLocation;

public class ZombieMinerRenderer extends ZombieRenderer {
    private static final ResourceLocation ZOMBIE_MINER_TEXTURE = ResourceLocation.fromNamespaceAndPath(HuanleMod.MOD_ID, "textures/entity/zombie_miner.png");

    public ZombieMinerRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(net.minecraft.world.entity.monster.Zombie entity) {
        return ZOMBIE_MINER_TEXTURE;
    }
}