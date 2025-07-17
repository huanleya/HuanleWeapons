package com.huanle.client;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class ClientConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue MANA_BAR_ENABLED;
    public static final ForgeConfigSpec.IntValue MANA_BAR_WIDTH;
    public static final ForgeConfigSpec.IntValue MANA_BAR_HEIGHT;
    public static final ForgeConfigSpec.IntValue MANA_BAR_OFFSET_X;
    public static final ForgeConfigSpec.IntValue MANA_BAR_OFFSET_Y;

    public static final ForgeConfigSpec.BooleanValue SHOW_MANA_TEXT;
    public static final ForgeConfigSpec.DoubleValue TEXT_SCALE;
    public static final ForgeConfigSpec.EnumValue<ManaDisplayMode> MANA_DISPLAY_MODE;
    public static final ForgeConfigSpec.BooleanValue MANA_BAR_LARGE_SIZE;
    
    static {
        BUILDER.comment("Mana bar display settings").push("mana_bar");
        
        MANA_BAR_ENABLED = BUILDER
            .comment("Whether to enable mana bar display")
            .define("enabled", true);
            
        MANA_BAR_WIDTH = BUILDER
            .comment("Mana bar width")
            .defineInRange("width", 186, 50, 300);
            
        MANA_BAR_HEIGHT = BUILDER
            .comment("Mana bar height")
            .defineInRange("height", 9, 2, 20);
            
        MANA_BAR_OFFSET_X = BUILDER
            .comment("Mana bar X axis offset")
            .defineInRange("offset_x", 0, -500, 500);
            
        MANA_BAR_OFFSET_Y = BUILDER
            .comment("Mana bar Y axis offset")
            .defineInRange("offset_y", -50, -500, 500);
            

        SHOW_MANA_TEXT = BUILDER
            .comment("Whether to show mana value text")
            .define("show_text", true);
            
        TEXT_SCALE = BUILDER
            .comment("Text scale ratio")
            .defineInRange("text_scale", 0.8, 0.1, 2.0);
            
        MANA_DISPLAY_MODE = BUILDER
            .comment("Mana bar display mode: ALWAYS(always show), WEAPON_ONLY(show only when holding mana-consuming weapons)")
            .defineEnum("display_mode", ManaDisplayMode.WEAPON_ONLY);
            
        MANA_BAR_LARGE_SIZE = BUILDER
            .comment("Mana bar UI size: true(large), false(small)")
            .define("large_size", true);
            
        BUILDER.pop();
    }
    
    public static final ForgeConfigSpec SPEC = BUILDER.build();
    
    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, SPEC);
    }
    public static boolean isManaBarEnabled() {
        return MANA_BAR_ENABLED.get();
    }
    
    public static int getManaBarWidth() {
        return MANA_BAR_WIDTH.get();
    }
    
    public static int getManaBarHeight() {
        return MANA_BAR_HEIGHT.get();
    }
    
    public static int getManaBarOffsetX() {
        return MANA_BAR_OFFSET_X.get();
    }
    
    public static int getManaBarOffsetY() {
        return MANA_BAR_OFFSET_Y.get();
    }
    

    
    public static boolean shouldShowManaText() {
        return SHOW_MANA_TEXT.get();
    }
    
    public static float getTextScale() {
        return TEXT_SCALE.get().floatValue();
    }
    
    public static ManaDisplayMode getManaDisplayMode() {
        return MANA_DISPLAY_MODE.get();
    }
    
    public static boolean isManaBarLargeSize() {
        return MANA_BAR_LARGE_SIZE.get();
    }

    public static void setManaBarEnabled(boolean enabled) {
        MANA_BAR_ENABLED.set(enabled);
    }
    
    public static void setManaBarWidth(int width) {
        MANA_BAR_WIDTH.set(width);
    }
    
    public static void setManaBarHeight(int height) {
        MANA_BAR_HEIGHT.set(height);
    }
    
    public static void setManaBarOffsetX(int offsetX) {
        MANA_BAR_OFFSET_X.set(offsetX);
    }
    
    public static void setManaBarOffsetY(int offsetY) {
        MANA_BAR_OFFSET_Y.set(offsetY);
    }
    

    
    public static void setShowManaText(boolean show) {
        SHOW_MANA_TEXT.set(show);
    }
    
    public static void setTextScale(float scale) {
        TEXT_SCALE.set((double) scale);
    }
    
    public static void setManaDisplayMode(ManaDisplayMode mode) {
        MANA_DISPLAY_MODE.set(mode);
    }
    
    public static void setManaBarLargeSize(boolean largeSize) {
        MANA_BAR_LARGE_SIZE.set(largeSize);
    }
    
    public static void save() {
        SPEC.save();
    }
    

    public enum ManaDisplayMode {
        ALWAYS("always"),
        WEAPON_ONLY("weapon_only");
        
        private final String displayName;
        
        ManaDisplayMode(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}