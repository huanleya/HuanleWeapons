package com.huanle.init;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class ModKeybind {
    
    public static final KeyMapping ENDER_BELT_KEY = new KeyMapping(
        "key.huanle.ender_belt", 
        InputConstants.KEY_G, 
        "key.categories.huanle"
    );
}