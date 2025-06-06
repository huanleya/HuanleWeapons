package com.huanle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ThrowingAxeRenderer extends EntityRenderer<ThrowingAxeEntity> {
    private final ItemRenderer itemRenderer;
    private final float scale = 1.6F; // 增大投掷斧的渲染大小

    public ThrowingAxeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        

        System.out.println("ThrowingAxeRenderer created with default parameters");
    }

    @Override
    public void render(@NotNull ThrowingAxeEntity entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, 
                      @NotNull MultiBufferSource buffer, int packedLight) {

        ItemStack itemstack = entity.getItem();
        if (itemstack.isEmpty()) {

            itemstack = new ItemStack(ModItems.THROWING_AXE.get());
        }
        

        poseStack.pushPose();
        

        BakedModel bakedmodel = this.itemRenderer.getModel(itemstack, entity.level(), null, entity.getId());


        boolean isInGround = entity.isInGround();
        
        if (!isInGround) {

            float rotationSpeed = 8.0F;
            float rotation = (entity.tickCount + partialTicks) * rotationSpeed;

            poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
            poseStack.mulPose(Axis.ZP.rotationDegrees(rotation * 0.7F));
        } else {

            Direction hitDirection = entity.getHitDirection();

            switch (hitDirection) {
                case DOWN:

                    poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));

                    poseStack.translate(0.0F, -0.2F, 0.0F);
                    break;
                    
                case UP:

                    poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));

                    poseStack.translate(0.0F, -0.2F, 0.0F);
                    break;
                    
                case NORTH:

                    poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                    poseStack.mulPose(Axis.XP.rotationDegrees(45.0F));
                    poseStack.translate(0.0F, -0.1F, -0.15F);
                    break;
                    
                case SOUTH:
                    poseStack.mulPose(Axis.XP.rotationDegrees(45.0F));
                    poseStack.translate(0.0F, -0.1F, -0.15F);
                    break;
                    
                case WEST:
                    poseStack.mulPose(Axis.YP.rotationDegrees(270.0F));
                    poseStack.mulPose(Axis.XP.rotationDegrees(45.0F));
                    poseStack.translate(0.0F, -0.1F, -0.15F);
                    break;
                    
                case EAST:
                    poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
                    poseStack.mulPose(Axis.XP.rotationDegrees(45.0F));
                    poseStack.translate(0.0F, -0.1F, -0.15F);
                    break;
                    
                default:
                    poseStack.mulPose(Axis.XP.rotationDegrees(45.0F));
                    poseStack.translate(0.0F, -0.1F, -0.15F);
                    break;
            }
        }

        poseStack.scale(this.scale, this.scale, this.scale);
        

        int lightLevel = 15728880;

        this.itemRenderer.render(itemstack, ItemDisplayContext.GROUND, false, poseStack, buffer, 
                                lightLevel, OverlayTexture.NO_OVERLAY, bakedmodel);
        
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);

        if (entity.tickCount % 20 == 0) {
            System.out.println("Rendering throwing axe entity at: " + entity.getX() + ", " + entity.getY() + ", " + entity.getZ());
        }
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull ThrowingAxeEntity entity) {
        return ResourceLocation.parse(HuanleMod.MOD_ID + ":textures/item/throwing_axe.png");
    }
}
