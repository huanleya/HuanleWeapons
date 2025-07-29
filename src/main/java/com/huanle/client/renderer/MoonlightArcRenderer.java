package com.huanle.client.renderer;

import com.huanle.HuanleMod;
import com.huanle.entities.MoonlightArcEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import javax.annotation.Nonnull;

public class MoonlightArcRenderer extends EntityRenderer<MoonlightArcEntity> {
    private static final ResourceLocation TEXTURE = ResourceLocation.parse(HuanleMod.MOD_ID + ":textures/entity/moonlight_arc.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutout(TEXTURE);

    public MoonlightArcRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @Nonnull ResourceLocation getTextureLocation(@Nonnull MoonlightArcEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(@Nonnull MoonlightArcEntity entity, float entityYaw, float partialTicks, @Nonnull PoseStack poseStack, 
                       @Nonnull MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        float alpha = 0.8f;
        if (entity.age > 30) {
            alpha = 0.8f - ((entity.age - 30) / 30f) * 0.8f;
        }

        int brightness = 15728880;

        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));

        float curve = Mth.sin(entity.age * 0.2f) * 0.3f;

        VertexConsumer vertexConsumer = buffer.getBuffer(RENDER_TYPE);

        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix4f = pose.pose();
        Matrix3f matrix3f = pose.normal();

        float width = 0.25f;
        float length = 0.7f;

        vertexConsumer.vertex(matrix4f, -width, curve, 0.0F)
                    .color(0.5f, 0.7f, 1.0f, alpha)
                    .uv(0, 0)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(brightness)
                    .normal(matrix3f, 0.0F, 1.0F, 0.0F)
                    .endVertex();

        vertexConsumer.vertex(matrix4f, width, curve, 0.0F)
                    .color(0.5f, 0.7f, 1.0f, alpha)
                    .uv(1, 0)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(brightness)
                    .normal(matrix3f, 0.0F, 1.0F, 0.0F)
                    .endVertex();

        vertexConsumer.vertex(matrix4f, width, -curve, length)
                    .color(0.3f, 0.5f, 1.0f, alpha)
                    .uv(1, 1)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(brightness)
                    .normal(matrix3f, 0.0F, 1.0F, 0.0F)
                    .endVertex();

        vertexConsumer.vertex(matrix4f, -width, -curve, length)
                    .color(0.3f, 0.5f, 1.0f, alpha)
                    .uv(0, 1)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(brightness)
                    .normal(matrix3f, 0.0F, 1.0F, 0.0F)
                    .endVertex();
        
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
