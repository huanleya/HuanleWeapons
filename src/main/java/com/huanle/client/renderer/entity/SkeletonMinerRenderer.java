package com.huanle.client.renderer.entity;

import com.huanle.HuanleMod;
import com.huanle.entities.SkeletonMinerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.resources.ResourceLocation;

public class SkeletonMinerRenderer extends SkeletonRenderer {
    private static final ResourceLocation SKELETON_MINER_TEXTURE = ResourceLocation.fromNamespaceAndPath(HuanleMod.MOD_ID, "textures/entity/skeleton_miner.png");

    public SkeletonMinerRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(net.minecraft.world.entity.monster.AbstractSkeleton entity) {
        return SKELETON_MINER_TEXTURE;
    }
}