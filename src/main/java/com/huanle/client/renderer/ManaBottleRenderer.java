package com.huanle.client.renderer;

import com.huanle.entities.ManaBottleEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class ManaBottleRenderer extends ThrownItemRenderer<ManaBottleEntity> {
    
    public ManaBottleRenderer(EntityRendererProvider.Context context) {
        super(context);
    }
}