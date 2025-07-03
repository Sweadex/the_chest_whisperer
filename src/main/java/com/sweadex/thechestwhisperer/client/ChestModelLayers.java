package com.sweadex.thechestwhisperer.client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ChestModelLayers {
    public static final ModelLayerLocation SINGLE = new ModelLayerLocation(new ResourceLocation("minecraft", "chest"), "main");
    public static final ModelLayerLocation LEFT   = new ModelLayerLocation(new ResourceLocation("minecraft", "chest"), "left");
    public static final ModelLayerLocation RIGHT  = new ModelLayerLocation(new ResourceLocation("minecraft", "chest"), "right");
}
