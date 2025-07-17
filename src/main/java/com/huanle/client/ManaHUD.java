package com.huanle.client;

import com.huanle.capabilities.ManaCapability;
import com.huanle.items.weapons.MagicSword;
import com.huanle.items.weapons.BaseStaff;
import com.huanle.items.weapons.EarthSword;
import com.huanle.items.weapons.*;
import com.huanle.items.weapons.VoidAshBlade;
import com.huanle.items.weapons.SunGodSword;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;


/**
 * 魔力值HUD显示
 */
public class ManaHUD implements IGuiOverlay {
    private static final ResourceLocation MANA_BAR_BACKGROUND_TEXTURE_LARGE = ResourceLocation.fromNamespaceAndPath("huanle", "textures/gui/mana_bar_background.png");
    private static final ResourceLocation MANA_BAR_FILL_TEXTURE_LARGE = ResourceLocation.fromNamespaceAndPath("huanle", "textures/gui/mana_bar_fill.png");
    private static final ResourceLocation MANA_BAR_BACKGROUND_TEXTURE_SMALL = ResourceLocation.fromNamespaceAndPath("huanle", "textures/gui/mana_bar_background2.png");
    private static final ResourceLocation MANA_BAR_FILL_TEXTURE_SMALL = ResourceLocation.fromNamespaceAndPath("huanle", "textures/gui/mana_bar_fill2.png");
    private static String cachedManaText = "";
    private static int cachedTextWidth = 0;
    private static float lastMana = -1;
    private static float lastMaxMana = -1;
    private static boolean lastHoldingManaWeapon = false;
    private static long lastCheckTime = 0;
    private static final long CHECK_INTERVAL = 100;
    
    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        if (!ClientConfig.isManaBarEnabled()) {
            return;
        }
        
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        
        if (player == null || minecraft.options.hideGui) {
            return;
        }

        ClientConfig.ManaDisplayMode displayMode = ClientConfig.getManaDisplayMode();
        
        if (displayMode == ClientConfig.ManaDisplayMode.WEAPON_ONLY) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastCheckTime > CHECK_INTERVAL) {
                lastHoldingManaWeapon = isHoldingManaWeapon(player);
                lastCheckTime = currentTime;
            }

            if (!lastHoldingManaWeapon) {
                return;
            }
        }
        

        
        player.getCapability(ManaCapability.MANA_CAPABILITY).ifPresent(mana -> {
            if (mana.getMaxMana() > 0) {
                renderManaBar(guiGraphics, screenWidth, screenHeight, mana.getMana(), mana.getMaxMana());
            }
        });
    }

    private boolean isHoldingManaWeapon(Player player) {
        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();
        return isManaWeapon(mainHand) || isManaWeapon(offHand);
    }

    private boolean isManaWeapon(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return false;
        }

        return itemStack.getItem() instanceof MagicSword
            || itemStack.getItem() instanceof BaseStaff
            || itemStack.getItem() instanceof EarthSword
            || itemStack.getItem() instanceof DragonHunterSword
            || itemStack.getItem() instanceof VoidAshBlade
            || itemStack.getItem() instanceof SunGodSword;
    }

    private void renderManaBar(GuiGraphics guiGraphics, int screenWidth, int screenHeight, float currentMana, float maxMana) {
        boolean isLargeSize = ClientConfig.isManaBarLargeSize();
        ResourceLocation backgroundTexture = isLargeSize ? MANA_BAR_BACKGROUND_TEXTURE_LARGE : MANA_BAR_BACKGROUND_TEXTURE_SMALL;
        ResourceLocation fillTexture = isLargeSize ? MANA_BAR_FILL_TEXTURE_LARGE : MANA_BAR_FILL_TEXTURE_SMALL;
        int barWidth, barHeight;
        int bgTextureWidth, bgTextureHeight;
        
        if (isLargeSize) {
            barWidth = 186;
            barHeight = 9;
            bgTextureWidth = 186;
            bgTextureHeight = 9;
        } else {
            barWidth = 98;
            barHeight = 11;
            bgTextureWidth = 98;
            bgTextureHeight = 11;
        }
        int x = screenWidth / 2 - barWidth / 2 + ClientConfig.getManaBarOffsetX();
        int y = screenHeight - 50 + ClientConfig.getManaBarOffsetY();

        float manaPercentage = currentMana / maxMana;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        RenderSystem.setShaderTexture(0, backgroundTexture);
        guiGraphics.blit(backgroundTexture, x, y, 0, 0, barWidth, barHeight, bgTextureWidth, bgTextureHeight);

        if (manaPercentage > 0) {
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

            int actualFillWidth = (int) (fillTextureWidth * manaPercentage);
            
            guiGraphics.blit(fillTexture, x + fillOffsetX, y + fillOffsetY, 0, 0, 
                           Math.min(actualFillWidth, fillTextureWidth), fillTextureHeight, fillTextureWidth, fillTextureHeight);
        }

        if (ClientConfig.shouldShowManaText()) {
            if (lastMana != currentMana || lastMaxMana != maxMana) {
                cachedManaText = String.format("%d/%d", (int)currentMana, (int)maxMana);
                cachedTextWidth = Minecraft.getInstance().font.width(cachedManaText);
                lastMana = currentMana;
                lastMaxMana = maxMana;
            }
            
            int textX = x + (barWidth - cachedTextWidth) / 2;
            int textY = y - 10;
            float textScale = ClientConfig.getTextScale();
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(textScale, textScale, 1.0f);
            int scaledTextX = (int) ((textX + 1) / textScale);
            int scaledTextY = (int) ((textY + 1) / textScale);
            int scaledTextXMain = (int) (textX / textScale);
            int scaledTextYMain = (int) (textY / textScale);
            guiGraphics.drawString(Minecraft.getInstance().font, cachedManaText, scaledTextX, scaledTextY, 0xFF000000, false);
            guiGraphics.drawString(Minecraft.getInstance().font, cachedManaText, scaledTextXMain, scaledTextYMain, 0xFF00FFFF, false);
            guiGraphics.pose().popPose();
        }
        RenderSystem.disableBlend();
    }
}