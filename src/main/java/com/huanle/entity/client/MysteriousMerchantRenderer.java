package com.huanle.entity.client;

import com.huanle.HuanleMod;
import com.huanle.entities.MysteriousMerchantEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.resources.ResourceLocation;

public class MysteriousMerchantRenderer extends MobRenderer<MysteriousMerchantEntity, VillagerModel<MysteriousMerchantEntity>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HuanleMod.MOD_ID, "textures/entity/villager.png");

    public MysteriousMerchantRenderer(EntityRendererProvider.Context context) {
        super(context, new VillagerModel<>(context.bakeLayer(ModelLayers.VILLAGER)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(MysteriousMerchantEntity entity) {
        return TEXTURE;
    }
}