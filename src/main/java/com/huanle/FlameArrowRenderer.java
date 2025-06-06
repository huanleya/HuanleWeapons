package com.huanle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

import javax.annotation.Nonnull;


public class FlameArrowRenderer extends ArrowRenderer<FlameArrowEntity> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("huanle", "textures/entity/arrow.png");

    private static final ResourceLocation FIRE_TEXTURE = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/particle/flame.png");

    private final RandomSource random = RandomSource.create();
    
    public FlameArrowRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }
    
    @Override
    public @Nonnull ResourceLocation getTextureLocation(@Nonnull FlameArrowEntity entity) {
        return TEXTURE;
    }
    
    @Override
    public void render(@Nonnull FlameArrowEntity entityIn, float entityYaw, float partialTicks, 
                     @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int packedLight) {

        super.render(entityIn, entityYaw, partialTicks, poseStack, buffer, packedLight);
        

        poseStack.pushPose();
        

        long gameTime = entityIn.level().getGameTime();
        float animationOffset = ((gameTime % 24) + partialTicks) / 24.0F;
        

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucentEmissive(FIRE_TEXTURE));
        

        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entityIn.yRotO, entityIn.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot())));

        renderFlameLayer(poseStack, vertexConsumer, -0.2F, 0.0F, 0.0F, 0.15F, 0.15F, animationOffset, 0.9F, packedLight);
        renderFlameLayer(poseStack, vertexConsumer, -0.15F, 0.05F, 0.02F, 0.12F, 0.12F, animationOffset + 0.25F, 0.8F, packedLight);
        

        renderFlameLayer(poseStack, vertexConsumer, 0.0F, 0.0F, 0.0F, 0.1F, 0.1F, animationOffset + 0.1F, 0.7F, packedLight);
        renderFlameLayer(poseStack, vertexConsumer, 0.1F, -0.05F, 0.01F, 0.08F, 0.08F, animationOffset + 0.4F, 0.6F, packedLight);
        

        float tailLength = 0.5F;
        int segments = 5;
        for (int i = 0; i < segments; i++) {
            float progress = (float)i / segments;
            float xOffset = 0.2F + progress * tailLength;
            float scale = 0.08F * (1.0F - progress * 0.8F);
            float alpha = 0.6F * (1.0F - progress);
            
            renderFlameLayer(poseStack, vertexConsumer, xOffset, 0.0F, 0.0F, 
                              scale, scale, animationOffset + progress * 0.5F, alpha, packedLight);
        }
        

        poseStack.popPose();
    }
    

    private void renderFlameLayer(PoseStack poseStack, VertexConsumer vertexConsumer,
                                 float xOffset, float yOffset, float zOffset,
                                 float xScale, float yScale, float animationOffset,
                                 float alpha, int packedLight) {
        poseStack.pushPose();
        

        float randomRotation = (random.nextFloat() - 0.5F) * 15.0F;
        poseStack.mulPose(Axis.XP.rotationDegrees(randomRotation));
        poseStack.mulPose(Axis.ZP.rotationDegrees(randomRotation));
        

        poseStack.translate(xOffset, yOffset, zOffset);
        poseStack.scale(xScale, yScale, 0.01F);

        float u1 = 0.0F;
        float u2 = 1.0F;
        float v1 = animationOffset;
        float v2 = v1 + 0.5F;

        PoseStack.Pose pose = poseStack.last();
        

        drawBillboardQuad(pose, vertexConsumer, u1, v1, u2, v2, 1.0F, 0.2F, 0.0F, alpha, packedLight);

        drawBillboardQuad(pose, vertexConsumer, u1, v1, u2, v2, 1.0F, 0.0F, 0.0F, alpha * 0.7F, packedLight);
        
        poseStack.popPose();
    }
    

    private void drawBillboardQuad(PoseStack.Pose pose, VertexConsumer vertexConsumer,
                                  float u1, float v1, float u2, float v2,
                                  float red, float green, float blue, float alpha,
                                  int packedLight) {

        vertexConsumer.vertex(pose.pose(), -0.5F, -0.5F, 0.0F)
            .color(red, green, blue, alpha)
            .uv(u1, v2)
            .overlayCoords(OverlayTexture.NO_OVERLAY)
            .uv2(packedLight)
            .normal(pose.normal(), 0.0F, 1.0F, 0.0F)
            .endVertex();

        vertexConsumer.vertex(pose.pose(), 0.5F, -0.5F, 0.0F)
            .color(red, green, blue, alpha)
            .uv(u2, v2)
            .overlayCoords(OverlayTexture.NO_OVERLAY)
            .uv2(packedLight)
            .normal(pose.normal(), 0.0F, 1.0F, 0.0F)
            .endVertex();

        vertexConsumer.vertex(pose.pose(), 0.5F, 0.5F, 0.0F)
            .color(red, green, blue, alpha)
            .uv(u2, v1)
            .overlayCoords(OverlayTexture.NO_OVERLAY)
            .uv2(packedLight)
            .normal(pose.normal(), 0.0F, 1.0F, 0.0F)
            .endVertex();

        vertexConsumer.vertex(pose.pose(), -0.5F, 0.5F, 0.0F)
            .color(red, green, blue, alpha)
            .uv(u1, v1)
            .overlayCoords(OverlayTexture.NO_OVERLAY)
            .uv2(packedLight)
            .normal(pose.normal(), 0.0F, 1.0F, 0.0F)
            .endVertex();
    }
}