package com.huanle.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ManaConfigScreen extends Screen {
    private final Screen parent;
    private EditBox widthBox;
    private EditBox heightBox;
    private EditBox textScaleBox;
    private Checkbox enabledCheckbox;
    private Checkbox showTextCheckbox;
    private Button displayModeButton;
    private Button sizeToggleButton;

    public ManaConfigScreen(Screen parent) {
        super(Component.translatable("gui.huanle.mana_config.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        
        int centerX = this.width / 2;
        int startY = 50;
        int spacing = 25;

        enabledCheckbox = new Checkbox(centerX - 100, startY, 200, 20, 
            Component.translatable("gui.huanle.mana_config.enable_mana_bar"), ClientConfig.isManaBarEnabled());
        this.addRenderableWidget(enabledCheckbox);

        showTextCheckbox = new Checkbox(centerX - 100, startY + spacing, 200, 20, 
            Component.translatable("gui.huanle.mana_config.show_mana_text"), ClientConfig.shouldShowManaText());
        this.addRenderableWidget(showTextCheckbox);

        displayModeButton = Button.builder(
            Component.translatable("gui.huanle.mana_config.display_mode", getDisplayModeText(ClientConfig.getManaDisplayMode())),
            button -> {
                ClientConfig.ManaDisplayMode currentMode = ClientConfig.getManaDisplayMode();
                ClientConfig.ManaDisplayMode newMode = currentMode == ClientConfig.ManaDisplayMode.ALWAYS 
                    ? ClientConfig.ManaDisplayMode.WEAPON_ONLY 
                    : ClientConfig.ManaDisplayMode.ALWAYS;
                ClientConfig.setManaDisplayMode(newMode);
                ClientConfig.save();
                button.setMessage(Component.translatable("gui.huanle.mana_config.display_mode", getDisplayModeText(newMode)));
            }
        ).bounds(centerX - 100, startY + spacing * 2, 200, 20).build();
        this.addRenderableWidget(displayModeButton);

        sizeToggleButton = Button.builder(
            Component.translatable("gui.huanle.mana_config.ui_size", getSizeText(ClientConfig.isManaBarLargeSize())),
            button -> {
                boolean currentSize = ClientConfig.isManaBarLargeSize();
                boolean newSize = !currentSize;
                ClientConfig.setManaBarLargeSize(newSize);
                ClientConfig.save();
                button.setMessage(Component.translatable("gui.huanle.mana_config.ui_size", getSizeText(newSize)));
            }
        ).bounds(centerX - 100, startY + spacing * 3, 200, 20).build();
        this.addRenderableWidget(sizeToggleButton);

        widthBox = new EditBox(this.font, centerX - 50, startY + spacing * 4, 100, 20, Component.translatable("gui.huanle.mana_config.width"));
        widthBox.setValue(String.valueOf(ClientConfig.getManaBarWidth()));
        this.addRenderableWidget(widthBox);

        heightBox = new EditBox(this.font, centerX - 50, startY + spacing * 5, 100, 20, Component.translatable("gui.huanle.mana_config.height"));
        heightBox.setValue(String.valueOf(ClientConfig.getManaBarHeight()));
        this.addRenderableWidget(heightBox);

        textScaleBox = new EditBox(this.font, centerX - 50, startY + spacing * 6, 100, 20, Component.translatable("gui.huanle.mana_config.text_scale"));
        textScaleBox.setValue(String.valueOf(ClientConfig.getTextScale()));
        this.addRenderableWidget(textScaleBox);

        this.addRenderableWidget(Button.builder(Component.translatable("gui.huanle.mana_config.drag_position"), button -> {
            this.minecraft.setScreen(new ManaBarDragScreen(this));
        }).bounds(centerX - 85, startY + spacing * 7, 170, 20).build());

        this.addRenderableWidget(Button.builder(Component.translatable("gui.huanle.mana_config.save"), button -> {
            saveConfig();
            this.minecraft.setScreen(parent);
        }).bounds(centerX - 80, startY + spacing * 8 + 10, 70, 20).build());
        
        this.addRenderableWidget(Button.builder(Component.translatable("gui.huanle.mana_config.cancel"), button -> {
            this.minecraft.setScreen(parent);
        }).bounds(centerX + 10, startY + spacing * 8 + 10, 70, 20).build());
    }
    
    private void saveConfig() {
        try {
            ClientConfig.setManaBarEnabled(enabledCheckbox.selected());
            ClientConfig.setShowManaText(showTextCheckbox.selected());
            ClientConfig.setManaBarWidth(Integer.parseInt(widthBox.getValue()));
            ClientConfig.setManaBarHeight(Integer.parseInt(heightBox.getValue()));
            ClientConfig.setTextScale(Float.parseFloat(textScaleBox.getValue()));
            ClientConfig.save();
        } catch (NumberFormatException e) {
        }
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        int centerX = this.width / 2;
        int startY = 50;
        int spacing = 25;
        
        guiGraphics.drawCenteredString(this.font, this.title, centerX, 25, 0xFFFFFF);
        guiGraphics.drawString(this.font, Component.translatable("gui.huanle.mana_config.width").getString(), centerX - 100, startY + spacing * 4 + 5, 0xFFFFFF);
        guiGraphics.drawString(this.font, Component.translatable("gui.huanle.mana_config.height").getString(), centerX - 100, startY + spacing * 5 + 5, 0xFFFFFF);
        guiGraphics.drawString(this.font, Component.translatable("gui.huanle.mana_config.text_scale").getString(), centerX - 100, startY + spacing * 6 + 5, 0xFFFFFF);
    }
    
    @Override
    public boolean isPauseScreen() {
        return false;
    }
    
    @Override
    public void onClose() {
        saveConfig();
        if (this.minecraft != null) {
            this.minecraft.setScreen(parent);
        }
    }
    
    private String getDisplayModeText(ClientConfig.ManaDisplayMode mode) {
        return mode == ClientConfig.ManaDisplayMode.ALWAYS 
            ? Component.translatable("gui.huanle.mana_config.display_mode.always").getString()
            : Component.translatable("gui.huanle.mana_config.display_mode.weapon_only").getString();
    }
    
    private String getSizeText(boolean isLarge) {
        return isLarge 
            ? Component.translatable("gui.huanle.mana_config.ui_size.large").getString()
            : Component.translatable("gui.huanle.mana_config.ui_size.small").getString();
    }
}