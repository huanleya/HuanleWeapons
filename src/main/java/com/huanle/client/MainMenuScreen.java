package com.huanle.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class MainMenuScreen extends Screen {
    private final Screen parent;

    public MainMenuScreen(Screen parent) {
        super(Component.translatable("gui.huanle.config.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        this.addRenderableWidget(Button.builder(Component.translatable("gui.huanle.config.mana_config"), button -> {
            this.minecraft.setScreen(new ConfigScreen(this));
        }).bounds(centerX - 100, centerY - 30, 200, 20).build());

        this.addRenderableWidget(Button.builder(Component.translatable("gui.huanle.config.back"), button -> {
            this.minecraft.setScreen(parent);
        }).bounds(centerX - 50, centerY + 10, 100, 20).build());
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.drawCenteredString(this.font, Component.translatable("gui.huanle.config.title").getString(), this.width / 2, 50, 0xFFFFFF);
    }
    
    @Override
    public boolean isPauseScreen() {
        return false;
    }
    
    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(parent);
        }
    }
}