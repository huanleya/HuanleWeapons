package com.huanle.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigScreen extends Screen {
    private final Screen parent;

    public ConfigScreen(Screen parent) {
        super(Component.translatable("gui.huanle.config.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        
        int centerX = this.width / 2;
        int startY = 80;
        int spacing = 30;

        this.addRenderableWidget(Button.builder(
            Component.translatable("gui.huanle.config.mana_config"),
            button -> {
                this.minecraft.setScreen(new ManaConfigScreen(this));
            }
        ).bounds(centerX - 75, startY, 150, 20).build());

        this.addRenderableWidget(Button.builder(
            Component.translatable("gui.huanle.config.back"),
            button -> {
                this.minecraft.setScreen(parent);
            }
        ).bounds(centerX - 50, startY + spacing, 100, 20).build());
    }
    

    
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        int centerX = this.width / 2;
        guiGraphics.drawCenteredString(this.font, this.title, centerX, 30, 0xFFFFFF);
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