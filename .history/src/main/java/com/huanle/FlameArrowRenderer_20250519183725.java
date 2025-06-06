package com.huanle;

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

public class FlameArrowRenderer extends EntityRenderer<FlameArrowEntity> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HuanleMod.MOD_ID, "textures/entity/flame_arrow.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(TEXTURE);

    public FlameArrowRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(FlameArrowEntity entityIn, float entityYaw, float partialTicks, PoseStack poseStack, 
                       MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        
        // 根据箭的飞行方向旋转模型
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entityIn.yRotO, entityIn.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot())));
        
        // 获取顶点缓冲区
        VertexConsumer vertexConsumer = buffer.getBuffer(RENDER_TYPE);
        
        // 设置缩放 - 标准Minecraft箭的缩放
        float scale = 0.0625F;
        poseStack.scale(scale, scale, scale);
        
        // 添加幻影箭的抖动效果
        float f1 = 0.15625F; // 纹理U坐标起点
        float f2 = 0.3125F;  // 纹理V坐标起点
        float f3 = 0.15625F; // 纹理U坐标终点
        float f4 = 0.3125F;  // 纹理V坐标终点
        float f5 = 0.05625F; // Z轴偏移量
        float f6 = 0.125F;   // 箭杆宽度
        float f7 = 0.625F;   // 箭杆长度
        
        // 应用抖动效果
        float f9 = (float)(entityIn.getShakeTime() - partialTicks);
        if (f9 > 0.0F) {
            float f10 = -Mth.sin(f9 * 3.0F) * f9;
            poseStack.mulPose(Axis.ZP.rotationDegrees(f10));
        }
        
        // 渲染主箭杆 - 中间连接部分 - 使用幻影箭的渲染方式
        poseStack.pushPose();
        
        // 箭杆正面
        poseStack.pushPose();
        poseStack.scale(f7, f6, f6); // 使用定义的箭杆长度和宽度
        this.vertex(poseStack, vertexConsumer, -4, 0, -f5, f1, f2, 0, 1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, 0, -f5, f3, f2, 0, 1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, 1, -f5, f3, f4, 0, 1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, -4, 1, -f5, f1, f4, 0, 1, 0, packedLight);
        poseStack.popPose();
        
        // 箭杆背面
        poseStack.pushPose();
        poseStack.scale(f7, f6, f6);
        this.vertex(poseStack, vertexConsumer, -4, 1, f5, f1, f4, 0, -1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, 1, f5, f3, f4, 0, -1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, 0, f5, f3, f2, 0, -1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, -4, 0, f5, f1, f2, 0, -1, 0, packedLight);
        poseStack.popPose();
        
        // 箭杆顶部
        poseStack.pushPose();
        poseStack.scale(f7, f6, f6);
        this.vertex(poseStack, vertexConsumer, -4, 1, -f5, f1, f2, 0, 0, -1, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, 1, -f5, f3, f2, 0, 0, -1, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, 1, f5, f3, f4, 0, 0, -1, packedLight);
        this.vertex(poseStack, vertexConsumer, -4, 1, f5, f1, f4, 0, 0, -1, packedLight);
        poseStack.popPose();
        
        // 箭杆底部
        poseStack.pushPose();
        poseStack.scale(f7, f6, f6);
        this.vertex(poseStack, vertexConsumer, -4, 0, f5, f1, f4, 0, 0, 1, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, 0, f5, f3, f4, 0, 0, 1, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, 0, -f5, f3, f2, 0, 0, 1, packedLight);
        this.vertex(poseStack, vertexConsumer, -4, 0, -f5, f1, f2, 0, 0, 1, packedLight);
        poseStack.popPose();
        
        poseStack.popPose();
        
        // 渲染上箭杆 - 参考幻影箭的双箭设计
        poseStack.pushPose();
        poseStack.translate(0.0F, -4.0F * scale, 0.0F);
        
        // 上箭杆正面
        poseStack.pushPose();
        poseStack.scale(f7, f6, f6);
        this.vertex(poseStack, vertexConsumer, -4, 0, -f5, f1, f2, 0, 1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, 0, -f5, f3, f2, 0, 1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, 1, -f5, f3, f4, 0, 1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, -4, 1, -f5, f1, f4, 0, 1, 0, packedLight);
        poseStack.popPose();
        
        // 上箭杆背面
        poseStack.pushPose();
        poseStack.scale(f7, f6, f6);
        this.vertex(poseStack, vertexConsumer, -4, 1, f5, f1, f4, 0, -1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, 1, f5, f3, f4, 0, -1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, 0, f5, f3, f2, 0, -1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, -4, 0, f5, f1, f2, 0, -1, 0, packedLight);
        poseStack.popPose();
        
        // 上箭杆顶部
        poseStack.pushPose();
        poseStack.scale(f7, f6, f6);
        this.vertex(poseStack, vertexConsumer, -4, 1, -f5, f1, f2, 0, 0, -1, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, 1, -f5, f3, f2, 0, 0, -1, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, 1, f5, f3, f4, 0, 0, -1, packedLight);
        this.vertex(poseStack, vertexConsumer, -4, 1, f5, f1, f4, 0, 0, -1, packedLight);
        poseStack.popPose();
        
        // 上箭杆底部
        poseStack.pushPose();
        poseStack.scale(f7, f6, f6);
        this.vertex(poseStack, vertexConsumer, -4, 0, f5, f1, f4, 0, 0, 1, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, 0, f5, f3, f4, 0, 0, 1, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, 0, -f5, f3, f2, 0, 0, 1, packedLight);
        this.vertex(poseStack, vertexConsumer, -4, 0, -f5, f1, f2, 0, 0, 1, packedLight);
        poseStack.popPose();
        
        poseStack.popPose();
        
        // 渲染下箭杆 - 参考幻影箭的双箭设计
        poseStack.pushPose();
        poseStack.translate(0.0F, 4.0F * scale, 0.0F);
        
        // 下箭杆正面
        poseStack.pushPose();
        poseStack.scale(f7, f6, f6);
        this.vertex(poseStack, vertexConsumer, -4, 0, -f5, f1, f2, 0, 1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, 0, -f5, f3, f2, 0, 1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, 1, -f5, f3, f4, 0, 1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, -4, 1, -f5, f1, f4, 0, 1, 0, packedLight);
        poseStack.popPose();
        
        // 下箭杆背面
        poseStack.pushPose();
        poseStack.scale(f7, f6, f6);
        this.vertex(poseStack, vertexConsumer, -4, 1, f5, f1, f4, 0, -1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, 1, f5, f3, f4, 0, -1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, 0, f5, f3, f2, 0, -1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, -4, 0, f5, f1, f2, 0, -1, 0, packedLight);
        poseStack.popPose();
        
        // 下箭杆顶部
        poseStack.pushPose();
        poseStack.scale(f7, f6, f6);
        this.vertex(poseStack, vertexConsumer, -4, 1, -f5, f1, f2, 0, 0, -1, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, 1, -f5, f3, f2, 0, 0, -1, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, 1, f5, f3, f4, 0, 0, -1, packedLight);
        this.vertex(poseStack, vertexConsumer, -4, 1, f5, f1, f4, 0, 0, -1, packedLight);
        poseStack.popPose();
        
        // 下箭杆底部
        poseStack.pushPose();
        poseStack.scale(f7, f6, f6);
        this.vertex(poseStack, vertexConsumer, -4, 0, f5, f1, f4, 0, 0, 1, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, 0, f5, f3, f4, 0, 0, 1, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, 0, -f5, f3, f2, 0, 0, 1, packedLight);
        this.vertex(poseStack, vertexConsumer, -4, 0, -f5, f1, f2, 0, 0, 1, packedLight);
        poseStack.popPose();
        
        poseStack.popPose();
        
        // 渲染上箭头 - 使用幻影箭的渲染方式
        poseStack.pushPose();
        poseStack.translate(-16.0F * scale, -4.0F * scale, 0.0F);
        
        // 箭头正面
        poseStack.pushPose();
        poseStack.scale(0.4F, 0.4F, 0.2F);
        this.vertex(poseStack, vertexConsumer, 0, 0, -f5, 0.5F, 0.25F, 0, 1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, -3, -f5, 0.75F, 0.25F, 0, 1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, 3, -f5, 0.75F, 0.5F, 0, 1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 0, 0, -f5, 0.5F, 0.5F, 0, 1, 0, packedLight);
        poseStack.popPose();
        
        // 箭头背面
        poseStack.pushPose();
        poseStack.scale(0.4F, 0.4F, 0.2F);
        this.vertex(poseStack, vertexConsumer, 0, 0, f5, 0.5F, 0.5F, 0, -1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, 3, f5, 0.75F, 0.5F, 0, -1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, -3, f5, 0.75F, 0.25F, 0, -1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 0, 0, f5, 0.5F, 0.25F, 0, -1, 0, packedLight);
        poseStack.popPose();
        
        // 箭头上部
        poseStack.pushPose();
        poseStack.scale(0.4F, 0.4F, 0.2F);
        this.vertex(poseStack, vertexConsumer, 0, 0, -f5, 0.5F, 0.25F, 0, 0, -1, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, -3, -f5, 0.75F, 0.25F, 0, 0, -1, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, -3, f5, 0.75F, 0.5F, 0, 0, -1, packedLight);
        this.vertex(poseStack, vertexConsumer, 0, 0, f5, 0.5F, 0.5F, 0, 0, -1, packedLight);
        poseStack.popPose();
        
        // 箭头下部
        poseStack.pushPose();
        poseStack.scale(0.4F, 0.4F, 0.2F);
        this.vertex(poseStack, vertexConsumer, 0, 0, f5, 0.5F, 0.5F, 0, 0, 1, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, 3, f5, 0.75F, 0.5F, 0, 0, 1, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, 3, -f5, 0.75F, 0.25F, 0, 0, 1, packedLight);
        this.vertex(poseStack, vertexConsumer, 0, 0, -f5, 0.5F, 0.25F, 0, 0, 1, packedLight);
        poseStack.popPose();
        
        poseStack.popPose();
        
        // 渲染下箭头 - 使用幻影箭的渲染方式
        poseStack.pushPose();
        poseStack.translate(-16.0F * scale, 4.0F * scale, 0.0F);
        
        // 箭头正面
        poseStack.pushPose();
        poseStack.scale(0.4F, 0.4F, 0.2F);
        this.vertex(poseStack, vertexConsumer, 0, 0, -f5, 0.5F, 0.25F, 0, 1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, -3, -f5, 0.75F, 0.25F, 0, 1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, 3, -f5, 0.75F, 0.5F, 0, 1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 0, 0, -f5, 0.5F, 0.5F, 0, 1, 0, packedLight);
        poseStack.popPose();
        
        // 箭头背面
        poseStack.pushPose();
        poseStack.scale(0.4F, 0.4F, 0.2F);
        this.vertex(poseStack, vertexConsumer, 0, 0, f5, 0.5F, 0.5F, 0, -1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, 3, f5, 0.75F, 0.5F, 0, -1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, -3, f5, 0.75F, 0.25F, 0, -1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 0, 0, f5, 0.5F, 0.25F, 0, -1, 0, packedLight);
        poseStack.popPose();
        
        // 箭头上部
        poseStack.pushPose();
        poseStack.scale(0.4F, 0.4F, 0.2F);
        this.vertex(poseStack, vertexConsumer, 0, 0, -f5, 0.5F, 0.25F, 0, 0, -1, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, -3, -f5, 0.75F, 0.25F, 0, 0, -1, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, -3, f5, 0.75F, 0.5F, 0, 0, -1, packedLight);
        this.vertex(poseStack, vertexConsumer, 0, 0, f5, 0.5F, 0.5F, 0, 0, -1, packedLight);
        poseStack.popPose();
        
        // 箭头下部
        poseStack.pushPose();
        poseStack.scale(0.4F, 0.4F, 0.2F);
        this.vertex(poseStack, vertexConsumer, 0, 0, f5, 0.5F, 0.5F, 0, 0, 1, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, 3, f5, 0.75F, 0.5F, 0, 0, 1, packedLight);
        this.vertex(poseStack, vertexConsumer, 4, 3, -f5, 0.75F, 0.25F, 0, 0, 1, packedLight);
        this.vertex(poseStack, vertexConsumer, 0, 0, -f5, 0.5F, 0.25F, 0, 0, 1, packedLight);
        poseStack.popPose();
        
        poseStack.popPose();
        
        // 渲染上箭尾 - 左侧羽毛 - 使用幻影箭的渲染方式
        poseStack.pushPose();
        poseStack.translate(16.0F * scale, -4.0F * scale, 0.0F);
        
        // 左侧羽毛正面
        poseStack.pushPose();
        poseStack.scale(0.3F, 0.3F, 0.2F);
        this.vertex(poseStack, vertexConsumer, -3, -1, -f5, 0.75F, 0.5F, 0, 1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 3, -2, -f5 - 0.1F, 1.0F, 0.5F, 0, 1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 3, 0, -f5 - 0.1F, 1.0F, 0.75F, 0, 1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, -3, 1, -f5, 0.75F, 0.75F, 0, 1, 0, packedLight);
        poseStack.popPose();
        
        // 左侧羽毛背面
        poseStack.pushPose();
        poseStack.scale(0.3F, 0.3F, 0.2F);
        this.vertex(poseStack, vertexConsumer, -3, 1, f5, 0.75F, 0.75F, 0, -1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 3, 0, f5 + 0.1F, 1.0F, 0.75F, 0, -1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 3, -2, f5 + 0.1F, 1.0F, 0.5F, 0, -1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, -3, -1, f5, 0.75F, 0.5F, 0, -1, 0, packedLight);
        poseStack.popPose();
        
        // 左侧羽毛顶部
        poseStack.pushPose();
        poseStack.scale(0.3F, 0.3F, 0.2F);
        this.vertex(poseStack, vertexConsumer, -3, 1, -f5, 0.75F, 0.5F, 0, 0, -1, packedLight);
        this.vertex(poseStack, vertexConsumer, 3, 0, -f5 - 0.1F, 1.0F, 0.5F, 0, 0, -1, packedLight);
        this.vertex(poseStack, vertexConsumer, 3, 0, f5 + 0.1F, 1.0F, 0.75F, 0, 0, -1, packedLight);
        this.vertex(poseStack, vertexConsumer, -3, 1, f5, 0.75F, 0.75F, 0, 0, -1, packedLight);
        poseStack.popPose();
        
        // 左侧羽毛底部
        poseStack.pushPose();
        poseStack.scale(0.3F, 0.3F, 0.2F);
        this.vertex(poseStack, vertexConsumer, -3, -1, f5, 0.75F, 0.75F, 0, 0, 1, packedLight);
        this.vertex(poseStack, vertexConsumer, 3, -2, f5 + 0.1F, 1.0F, 0.75F, 0, 0, 1, packedLight);
        this.vertex(poseStack, vertexConsumer, 3, -2, -f5 - 0.1F, 1.0F, 0.5F, 0, 0, 1, packedLight);
        this.vertex(poseStack, vertexConsumer, -3, -1, -f5, 0.75F, 0.5F, 0, 0, 1, packedLight);
        poseStack.popPose();
        
        poseStack.popPose();
        
        // 渲染上箭尾 - 右侧羽毛 - 使用幻影箭的渲染方式
        poseStack.pushPose();
        poseStack.translate(16.0F * scale, -4.0F * scale, 0.0F);
        
        // 右侧羽毛正面
        poseStack.pushPose();
        poseStack.scale(0.3F, 0.3F, 0.2F);
        this.vertex(poseStack, vertexConsumer, -3, -1, -f5, 0.75F, 0.75F, 0, 1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 3, -2, -f5 + 0.1F, 1.0F, 0.75F, 0, 1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 3, 0, -f5 + 0.1F, 1.0F, 1.0F, 0, 1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, -3, 1, -f5, 0.75F, 1.0F, 0, 1, 0, packedLight);
        poseStack.popPose();
        
        // 右侧羽毛背面
        poseStack.pushPose();
        poseStack.scale(0.3F, 0.3F, 0.2F);
        this.vertex(poseStack, vertexConsumer, -3, 1, f5, 0.75F, 1.0F, 0, -1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 3, 0, f5 - 0.1F, 1.0F, 1.0F, 0, -1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 3, -2, f5 - 0.1F, 1.0F, 0.75F, 0, -1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, -3, -1, f5, 0.75F, 0.75F, 0, -1, 0, packedLight);
        poseStack.popPose();
        
        // 右侧羽毛顶部
        poseStack.pushPose();
        poseStack.scale(0.3F, 0.3F, 0.2F);
        this.vertex(poseStack, vertexConsumer, -3, 1, -f5, 0.75F, 0.75F, 0, 0, -1, packedLight);
        this.vertex(poseStack, vertexConsumer, 3, 0, -f5 + 0.1F, 1.0F, 0.75F, 0, 0, -1, packedLight);
        this.vertex(poseStack, vertexConsumer, 3, 0, f5 - 0.1F, 1.0F, 1.0F, 0, 0, -1, packedLight);
        this.vertex(poseStack, vertexConsumer, -3, 1, f5, 0.75F, 1.0F, 0, 0, -1, packedLight);
        poseStack.popPose();
        
        // 右侧羽毛底部
        poseStack.pushPose();
        poseStack.scale(0.3F, 0.3F, 0.2F);
        this.vertex(poseStack, vertexConsumer, -3, -1, f5, 0.75F, 1.0F, 0, 0, 1, packedLight);
        this.vertex(poseStack, vertexConsumer, 3, -2, f5 - 0.1F, 1.0F, 1.0F, 0, 0, 1, packedLight);
        this.vertex(poseStack, vertexConsumer, 3, -2, -f5 + 0.1F, 1.0F, 0.75F, 0, 0, 1, packedLight);
        this.vertex(poseStack, vertexConsumer, -3, -1, -f5, 0.75F, 0.75F, 0, 0, 1, packedLight);
        poseStack.popPose();
        
        poseStack.popPose();
        
        // 渲染下箭尾 - 左侧羽毛 - 使用幻影箭的渲染方式
        poseStack.pushPose();
        poseStack.translate(16.0F * scale, 4.0F * scale, 0.0F);
        
        // 左侧羽毛正面
        poseStack.pushPose();
        poseStack.scale(0.3F, 0.3F, 0.2F);
        this.vertex(poseStack, vertexConsumer, -3, -1, -f5, 0.75F, 0.5F, 0, 1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 3, -2, -f5 - 0.1F, 1.0F, 0.5F, 0, 1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 3, 0, -f5 - 0.1F, 1.0F, 0.75F, 0, 1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, -3, 1, -f5, 0.75F, 0.75F, 0, 1, 0, packedLight);
        poseStack.popPose();
        
        // 左侧羽毛背面
        poseStack.pushPose();
        poseStack.scale(0.3F, 0.3F, 0.2F);
        this.vertex(poseStack, vertexConsumer, -3, 1, f5, 0.75F, 0.75F, 0, -1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 3, 0, f5 + 0.1F, 1.0F, 0.75F, 0, -1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 3, -2, f5 + 0.1F, 1.0F, 0.5F, 0, -1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, -3, -1, f5, 0.75F, 0.5F, 0, -1, 0, packedLight);
        poseStack.popPose();
        
        // 左侧羽毛顶部
        poseStack.pushPose();
        poseStack.scale(0.3F, 0.3F, 0.2F);
        this.vertex(poseStack, vertexConsumer, -3, 1, -f5, 0.75F, 0.5F, 0, 0, -1, packedLight);
        this.vertex(poseStack, vertexConsumer, 3, 0, -f5 - 0.1F, 1.0F, 0.5F, 0, 0, -1, packedLight);
        this.vertex(poseStack, vertexConsumer, 3, 0, f5 + 0.1F, 1.0F, 0.75F, 0, 0, -1, packedLight);
        this.vertex(poseStack, vertexConsumer, -3, 1, f5, 0.75F, 0.75F, 0, 0, -1, packedLight);
        poseStack.popPose();
        
        // 左侧羽毛底部
        poseStack.pushPose();
        poseStack.scale(0.3F, 0.3F, 0.2F);
        this.vertex(poseStack, vertexConsumer, -3, -1, f5, 0.75F, 0.75F, 0, 0, 1, packedLight);
        this.vertex(poseStack, vertexConsumer, 3, -2, f5 + 0.1F, 1.0F, 0.75F, 0, 0, 1, packedLight);
        this.vertex(poseStack, vertexConsumer, 3, -2, -f5 - 0.1F, 1.0F, 0.5F, 0, 0, 1, packedLight);
        this.vertex(poseStack, vertexConsumer, -3, -1, -f5, 0.75F, 0.5F, 0, 0, 1, packedLight);
        poseStack.popPose();
        
        poseStack.popPose();
        
        // 渲染下箭尾 - 右侧羽毛 - 使用幻影箭的渲染方式
        poseStack.pushPose();
        poseStack.translate(16.0F * scale, 4.0F * scale, 0.0F);
        
        // 右侧羽毛正面
        poseStack.pushPose();
        poseStack.scale(0.3F, 0.3F, 0.2F);
        this.vertex(poseStack, vertexConsumer, -3, -1, -f5, 0.75F, 0.75F, 0, 1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 3, -2, -f5 + 0.1F, 1.0F, 0.75F, 0, 1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 3, 0, -f5 + 0.1F, 1.0F, 1.0F, 0, 1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, -3, 1, -f5, 0.75F, 1.0F, 0, 1, 0, packedLight);
        poseStack.popPose();
        
        // 右侧羽毛背面
        poseStack.pushPose();
        poseStack.scale(0.3F, 0.3F, 0.2F);
        this.vertex(poseStack, vertexConsumer, -3, 1, f5, 0.75F, 1.0F, 0, -1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 3, 0, f5 - 0.1F, 1.0F, 1.0F, 0, -1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, 3, -2, f5 - 0.1F, 1.0F, 0.75F, 0, -1, 0, packedLight);
        this.vertex(poseStack, vertexConsumer, -3, -1, f5, 0.75F, 0.75F, 0, -1, 0, packedLight);
        poseStack.popPose();
        
        // 右侧羽毛顶部
        poseStack.pushPose();
        poseStack.scale(0.3F, 0.3F, 0.2F);
        this.vertex(poseStack, vertexConsumer, -3, 1, -f5, 0.75F, 0.75F, 0, 0, -1, packedLight);
        this.vertex(poseStack, vertexConsumer, 3, 0, -f5 + 0.1F, 1.0F, 0.75F, 0, 0, -1, packedLight);
        this.vertex(poseStack, vertexConsumer, 3, 0, f5 - 0.1F, 1.0F, 1.0F, 0, 0, -1, packedLight);
        this.vertex(poseStack, vertexConsumer, -3, 1, f5, 0.75F, 1.0F, 0, 0, -1, packedLight);
        poseStack.popPose();
        
        // 右侧羽毛底部
        poseStack.pushPose();
        poseStack.scale(0.3F, 0.3F, 0.2F);
        this.vertex(poseStack, vertexConsumer, -3, -1, f5, 0.75F, 1.0F, 0, 0, 1, packedLight);
        this.vertex(poseStack, vertexConsumer, 3, -2, f5 - 0.1F, 1.0F, 1.0F, 0, 0, 1, packedLight);
        this.vertex(poseStack, vertexConsumer, 3, -2, -f5 + 0.1F, 1.0F, 0.75F, 0, 0, 1, packedLight);
        this.vertex(poseStack, vertexConsumer, -3, -1, -f5, 0.75F, 0.75F, 0, 0, 1, packedLight);
        poseStack.popPose();
        
        poseStack.popPose();
        
        // 添加火焰效果 - 使用半透明纹理
        // 中间火焰效果
        poseStack.pushPose();
        poseStack.scale(5.0F, 0.5F, 0.1F);
        this.vertex(poseStack, vertexConsumer, -3, -1, 0, 0.0F, 0.5F, 0, 0, 1, packedLight, 0.0F, 0.8F, 0.8F, 0.8F);
        this.vertex(poseStack, vertexConsumer, 3, -1, 0, 0.5F, 0.5F, 0, 0, 1, packedLight, 0.0F, 0.8F, 0.8F, 0.8F);
        this.vertex(poseStack, vertexConsumer, 3, 1, 0, 0.5F, 0.75F, 0, 0, 1, packedLight, 0.0F, 0.8F, 0.8F, 0.8F);
        this.vertex(poseStack, vertexConsumer, -3, 1, 0, 0.0F, 0.75F, 0, 0, 1, packedLight, 0.0F, 0.8F, 0.8F, 0.8F);
        poseStack.popPose();
        
        // 上部火焰效果
        poseStack.pushPose();
        poseStack.translate(0.0F, -4.0F * scale, 0.0F);
        poseStack.scale(5.0F, 0.5F, 0.1F);
        this.vertex(poseStack, vertexConsumer, -3, -1, 0, 0.0F, 0.5F, 0, 0, 1, packedLight, 0.0F, 0.8F, 0.8F, 0.8F);
        this.vertex(poseStack, vertexConsumer, 3, -1, 0, 0.5F, 0.5F, 0, 0, 1, packedLight, 0.0F, 0.8F, 0.8F, 0.8F);
        this.vertex(poseStack, vertexConsumer, 3, 1, 0, 0.5F, 0.75F, 0, 0, 1, packedLight, 0.0F, 0.8F, 0.8F, 0.8F);
        this.vertex(poseStack, vertexConsumer, -3, 1, 0, 0.0F, 0.75F, 0, 0, 1, packedLight, 0.0F, 0.8F, 0.8F, 0.8F);
        poseStack.popPose();
        
        // 下部火焰效果
        poseStack.pushPose();
        poseStack.translate(0.0F, 4.0F * scale, 0.0F);
        poseStack.scale(5.0F, 0.5F, 0.1F);
        this.vertex(poseStack, vertexConsumer, -3, -1, 0, 0.0F, 0.5F, 0, 0, 1, packedLight, 0.0F, 0.8F, 0.8F, 0.8F);
        this.vertex(poseStack, vertexConsumer, 3, -1, 0, 0.5F, 0.5F, 0, 0, 1, packedLight, 0.0F, 0.8F, 0.8F, 0.8F);
        this.vertex(poseStack, vertexConsumer, 3, 1, 0, 0.5F, 0.75F, 0, 0, 1, packedLight, 0.0F, 0.8F, 0.8F, 0.8F);
        this.vertex(poseStack, vertexConsumer, -3, 1, 0, 0.0F, 0.75F, 0, 0, 1, packedLight, 0.0F, 0.8F, 0.8F, 0.8F);
        poseStack.popPose();
        
        // 添加拖尾效果
        poseStack.pushPose();
        poseStack.scale(6.0F, 0.3F, 0.05F);
        this.vertex(poseStack, vertexConsumer, -2, -1, 0, 0.0F, 0.0F, 0, 0, 1, packedLight, 0.0F, 0.8F, 0.8F, 0.3F);
        this.vertex(poseStack, vertexConsumer, 2, -1, 0, 0.5F, 0.0F, 0, 0, 1, packedLight, 0.0F, 0.8F, 0.8F, 0.3F);
        this.vertex(poseStack, vertexConsumer, 2, 1, 0, 0.5F, 0.25F, 0, 0, 1, packedLight, 0.0F, 0.8F, 0.8F, 0.3F);
        this.vertex(poseStack, vertexConsumer, -2, 1, 0, 0.0F, 0.25F, 0, 0, 1, packedLight, 0.0F, 0.8F, 0.8F, 0.3F);
        poseStack.popPose();
        
        poseStack.popPose();
        super.render(entityIn, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
    
    private void vertex(PoseStack poseStack, VertexConsumer vertexConsumer, float x, float y, float z, float u, float v, int normalX, int normalY, int normalZ, int packedLight) {
        vertex(poseStack, vertexConsumer, x, y, z, u, v, normalX, normalY, normalZ, packedLight, 0.0F, 0.8F, 0.8F, 1.0F);
    }
    
    private void vertex(PoseStack poseStack, VertexConsumer vertexConsumer, float x, float y, float z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float red, float green, float blue, float alpha) {
        PoseStack.Pose pose = poseStack.last();
        vertexConsumer.vertex(pose.pose(), x, y, z)
            .color(red, green, blue, alpha)
            .uv(u, v)
            .overlayCoords(OverlayTexture.NO_OVERLAY)
            .uv2(packedLight)
            .normal(pose.normal(), normalX, normalY, normalZ)
            .endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(FlameArrowEntity entity) {
        return TEXTURE;
    }
}