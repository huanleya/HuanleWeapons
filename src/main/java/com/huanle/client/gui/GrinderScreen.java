package com.huanle.client.gui;

import com.huanle.HuanleMod;
import com.huanle.menus.GrinderMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GrinderScreen extends AbstractContainerScreen<GrinderMenu> {
    
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HuanleMod.MOD_ID, "textures/gui/grinder.png");
    
    public GrinderScreen(GrinderMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }
    
    @Override
    protected void init() {
        super.init();
        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;
    }
    
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        
        guiGraphics.blit(TEXTURE, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
        
        renderProgressArrow(guiGraphics, relX, relY);
    }
    
    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if (menu.isCrafting()) {
            int progress = menu.getScaledProgress();
            
            if (progress > 0) {
                guiGraphics.blit(TEXTURE, x + 93, y + 35, 176, 0, 16, progress);
            }
        }
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
    
    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderTooltip(guiGraphics, mouseX, mouseY);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        int arrowX = relX + 93;
        int arrowY = relY + 35;
        
        if (mouseX >= arrowX && mouseX <= arrowX + 16 && mouseY >= arrowY && mouseY <= arrowY + 16) {
            if (menu.isCrafting()) {
                int progress = menu.getRawProgress();
                int maxProgress = menu.getMaxProgress();
                Component progressText = Component.translatable("huanle.grinder.progress", progress, maxProgress);
                guiGraphics.renderTooltip(this.font, progressText, mouseX, mouseY);
            } else {
                if (!menu.getSlot(0).getItem().isEmpty()) {
                    Component recipeText = Component.translatable("gui.huanle.grinder.recipe_text");
                    guiGraphics.renderTooltip(this.font, recipeText, mouseX, mouseY);
                }
            }
        }
    }
}