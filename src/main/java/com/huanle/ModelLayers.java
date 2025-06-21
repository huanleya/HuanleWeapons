package com.huanle;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModelLayers {
    public static final ModelLayerLocation FLAME_ARROW = new ModelLayerLocation(
        ResourceLocation.fromNamespaceAndPath(HuanleMod.MOD_ID, "flame_arrow"), "main");
    
    public static final ModelLayerLocation FOREST_ARMOR_INNER = new ModelLayerLocation(
        ResourceLocation.fromNamespaceAndPath(HuanleMod.MOD_ID, "forest_armor"), "inner");
    
    public static final ModelLayerLocation FOREST_ARMOR_OUTER = new ModelLayerLocation(
        ResourceLocation.fromNamespaceAndPath(HuanleMod.MOD_ID, "forest_armor"), "outer");
}