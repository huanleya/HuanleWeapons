package com.huanle.client.renderer;

import com.huanle.HuanleMod;
import com.huanle.ModEffects;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = HuanleMod.MOD_ID, value = Dist.CLIENT)
public class FreezeEffectRenderer {
    private static final ResourceLocation FREEZE_OVERLAY = ResourceLocation.fromNamespaceAndPath(HuanleMod.MOD_ID, "textures/gui/freeze_overlay.png");

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay() != VanillaGuiOverlay.HOTBAR.type()) return;

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null && minecraft.player.hasEffect(ModEffects.FREEZING.get())) {
            GuiGraphics guiGraphics = event.getGuiGraphics();
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.5F);
            RenderSystem.setShaderTexture(0, FREEZE_OVERLAY);

            int width = event.getWindow().getGuiScaledWidth();
            int height = event.getWindow().getGuiScaledHeight();
            guiGraphics.blit(FREEZE_OVERLAY, 0, 0, 0.0F, 0.0F, width, height, width, height);

            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
