package com.huanle.client;

import com.huanle.capabilities.ManaCapability;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class ManaBarDragScreen extends Screen {
    private final Screen parent;
    private static final ResourceLocation MANA_BAR_BACKGROUND_TEXTURE_LARGE = ResourceLocation.fromNamespaceAndPath("huanle", "textures/gui/mana_bar_background.png");
    private static final ResourceLocation MANA_BAR_FILL_TEXTURE_LARGE = ResourceLocation.fromNamespaceAndPath("huanle", "textures/gui/mana_bar_fill.png");
    private static final ResourceLocation MANA_BAR_BACKGROUND_TEXTURE_SMALL = ResourceLocation.fromNamespaceAndPath("huanle", "textures/gui/mana_bar_background2.png");
    private static final ResourceLocation MANA_BAR_FILL_TEXTURE_SMALL = ResourceLocation.fromNamespaceAndPath("huanle", "textures/gui/mana_bar_fill2.png");
    private boolean isDragging = false;
    private int dragStartX = 0;
    private int dragStartY = 0;
    private int initialOffsetX = 0;
    private int initialOffsetY = 0;
    private int manaBarX = 0;
    private int manaBarY = 0;
    private int manaBarWidth = 0;
    private int manaBarHeight = 0;
    
    public ManaBarDragScreen(Screen parent) {
        super(Component.translatable("gui.huanle.mana_config.drag_position"));
        this.parent = parent;
    }
    
    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(Button.builder(Component.translatable("gui.huanle.mana_config.save"), button -> {
            ClientConfig.save();
            this.minecraft.setScreen(parent);
        }).bounds(this.width / 2 - 50, this.height - 40, 100, 20).build());

        this.addRenderableWidget(Button.builder(Component.translatable("gui.huanle.mana_config.reset_position"), button -> {
            ClientConfig.setManaBarOffsetX(0);
            ClientConfig.setManaBarOffsetY(-50);
            ClientConfig.save();
        }).bounds(this.width / 2 - 150, this.height - 40, 80, 20).build());

        this.addRenderableWidget(Button.builder(Component.translatable("gui.huanle.mana_config.cancel"), button -> {
            this.minecraft.setScreen(parent);
        }).bounds(this.width / 2 + 60, this.height - 40, 100, 20).build());
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);

        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
        guiGraphics.drawCenteredString(this.font, Component.translatable("gui.huanle.mana_config.drag_instruction").getString(), this.width / 2, 35, 0xAAAAAA);

        renderManaBarPreview(guiGraphics);
        
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        String positionInfo = String.format(I18n.get("huanle.mana_bar.position_info"), 
            ClientConfig.getManaBarOffsetX(), ClientConfig.getManaBarOffsetY());
        guiGraphics.drawCenteredString(this.font, positionInfo, this.width / 2, this.height - 60, 0xFFFFFF);
    }

    private void renderManaBarPreview(GuiGraphics guiGraphics) {
        boolean isLargeSize = ClientConfig.isManaBarLargeSize();
        ResourceLocation backgroundTexture = isLargeSize ? MANA_BAR_BACKGROUND_TEXTURE_LARGE : MANA_BAR_BACKGROUND_TEXTURE_SMALL;
        ResourceLocation fillTexture = isLargeSize ? MANA_BAR_FILL_TEXTURE_LARGE : MANA_BAR_FILL_TEXTURE_SMALL;
        if (isLargeSize) {
            manaBarWidth = 186;
            manaBarHeight = 9;
        } else {
            manaBarWidth = 98;
            manaBarHeight = 11;
        }
        manaBarX = this.width / 2 - manaBarWidth / 2 + ClientConfig.getManaBarOffsetX();
        manaBarY = this.height - 50 + ClientConfig.getManaBarOffsetY();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        if (isDragging) {
            guiGraphics.fill(manaBarX - 2, manaBarY - 2, manaBarX + manaBarWidth + 2, manaBarY + manaBarHeight + 2, 0x80FFFFFF);
        }

        RenderSystem.setShaderTexture(0, backgroundTexture);
        guiGraphics.blit(backgroundTexture, manaBarX, manaBarY, 0, 0, manaBarWidth, manaBarHeight, manaBarWidth, manaBarHeight);

        RenderSystem.setShaderTexture(0, fillTexture);
        int fillOffsetX, fillOffsetY;
        int fillTextureWidth, fillTextureHeight;
        
        if (isLargeSize) {
            fillOffsetX = 2;
            fillOffsetY = 2;
            fillTextureWidth = 182;
            fillTextureHeight = 5;
        } else {
            fillOffsetX = 3;
            fillOffsetY = 3;
            fillTextureWidth = 92;
            fillTextureHeight = 5;
        }
        
        int previewFillWidth = (int) (fillTextureWidth * 0.89f);
        
        guiGraphics.blit(fillTexture, manaBarX + fillOffsetX, manaBarY + fillOffsetY, 0, 0, 
                       Math.min(previewFillWidth, fillTextureWidth), fillTextureHeight, fillTextureWidth, fillTextureHeight);
        if (ClientConfig.shouldShowManaText()) {
            String manaText = "89/100";
            int textWidth = this.font.width(manaText);
            int textX = manaBarX + (manaBarWidth - textWidth) / 2;
            int textY = manaBarY - 10;
            
            float textScale = ClientConfig.getTextScale();
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(textScale, textScale, 1.0f);
            
            int scaledTextX = (int) ((textX + 1) / textScale);
            int scaledTextY = (int) ((textY + 1) / textScale);
            int scaledTextXMain = (int) (textX / textScale);
            int scaledTextYMain = (int) (textY / textScale);
            guiGraphics.drawString(this.font, manaText, scaledTextX, scaledTextY, 0xFF000000, false);
            guiGraphics.drawString(this.font, manaText, scaledTextXMain, scaledTextYMain, 0xFF00FFFF, false);
            
            guiGraphics.pose().popPose();
        }
        
        RenderSystem.disableBlend();
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            if (mouseX >= manaBarX && mouseX <= manaBarX + manaBarWidth &&
                mouseY >= manaBarY && mouseY <= manaBarY + manaBarHeight) {
                isDragging = true;
                dragStartX = (int) mouseX;
                dragStartY = (int) mouseY;
                initialOffsetX = ClientConfig.getManaBarOffsetX();
                initialOffsetY = ClientConfig.getManaBarOffsetY();
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0 && isDragging) {
            isDragging = false;
            ClientConfig.save();
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }
    
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (isDragging) {
            int deltaX = (int) mouseX - dragStartX;
            int deltaY = (int) mouseY - dragStartY;
            int newOffsetX = initialOffsetX + deltaX;
            int newOffsetY = initialOffsetY + deltaY;
            int maxOffsetX = this.width / 2;
            int minOffsetY = -(this.height - 50);
            int maxOffsetY = 100;
            newOffsetX = Math.max(-maxOffsetX, Math.min(maxOffsetX, newOffsetX));
            newOffsetY = Math.max(minOffsetY, Math.min(maxOffsetY, newOffsetY));
            
            ClientConfig.setManaBarOffsetX(newOffsetX);
            ClientConfig.setManaBarOffsetY(newOffsetY);
            
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }
    
    @Override
    public boolean isPauseScreen() {
        return false;
    }
    
    @Override
    public void onClose() {
        ClientConfig.save();
        if (this.minecraft != null) {
            this.minecraft.setScreen(parent);
        }
    }
}