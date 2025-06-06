package com.huanle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;

public class FlameArrowRenderer extends EntityRenderer<FlameArrowEntity> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HuanleMod.MOD_ID, "textures/entity/flame_arrow.png");
    private final FlameArrowModel model;

    public FlameArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
        model = new FlameArrowModel(context.bakeLayer(ModelLayers.FLAME_ARROW));
    }

    @Override
    public void render(FlameArrowEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, 
                       MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        
        // 根据箭的飞行方向旋转模型
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));
        
        // 调整模型大小和位置
        poseStack.scale(0.8F, 0.8F, 0.8F);
        poseStack.translate(0.0D, -0.25D, 0.0D);
        
        // 渲染模型
        model.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE)), 
                            packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(FlameArrowEntity entity) {
        return TEXTURE;
    }
}