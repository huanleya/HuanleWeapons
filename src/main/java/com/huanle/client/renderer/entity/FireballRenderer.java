package com.huanle.client.renderer.entity;

import com.huanle.entities.FireballEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class FireballRenderer extends EntityRenderer<FireballEntity> {
    private static final ResourceLocation FIREBALL_TEXTURE = ResourceLocation.parse("huanle:textures/entity/fireball.png");
    
    public FireballRenderer(EntityRendererProvider.Context context) {
        super(context);
    }
    
    @Override
    public void render(@NotNull FireballEntity entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, 
                      @NotNull MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        float scale = 1.0F;
        poseStack.scale(scale, scale, scale);

        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(entity)));

        this.renderFireball(poseStack, vertexConsumer, packedLight);
        
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
    
    private void renderFireball(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight) {
        float size = 0.5F;
        var matrix = poseStack.last().pose();
        var normal = poseStack.last().normal();
        vertexConsumer.vertex(matrix, -size, -size, 0.0F).color(255, 255, 255, 255)
                .uv(0.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(packedLight).normal(normal, 0.0F, 0.0F, 1.0F).endVertex();
        vertexConsumer.vertex(matrix, size, -size, 0.0F).color(255, 255, 255, 255)
                .uv(1.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(packedLight).normal(normal, 0.0F, 0.0F, 1.0F).endVertex();
        vertexConsumer.vertex(matrix, size, size, 0.0F).color(255, 255, 255, 255)
                .uv(1.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(packedLight).normal(normal, 0.0F, 0.0F, 1.0F).endVertex();
        vertexConsumer.vertex(matrix, -size, size, 0.0F).color(255, 255, 255, 255)
                .uv(0.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(packedLight).normal(normal, 0.0F, 0.0F, 1.0F).endVertex();
    }
    
    @Override
    protected int getBlockLightLevel(@NotNull FireballEntity entity, @NotNull BlockPos pos) {
        return 15;
    }
    
    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull FireballEntity entity) {
        return FIREBALL_TEXTURE;
    }
}