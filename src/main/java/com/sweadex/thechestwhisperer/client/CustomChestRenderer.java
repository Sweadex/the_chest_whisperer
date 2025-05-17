package com.sweadex.thechestwhisperer.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.nbt.CompoundTag;

public class CustomChestRenderer implements BlockEntityRenderer<ChestBlockEntity> {

    private static final ResourceLocation DEFAULT_CHEST_TEXTURE = new ResourceLocation("minecraft", "textures/entity/chest/normal.png");
    private static final ResourceLocation STOLEN_CHEST_TEXTURE = new ResourceLocation("thechestwhisperer", "textures/entity/chest/stolen.png");

    public CustomChestRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(ChestBlockEntity chest, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        //CompoundTag data = chest.getPersistentData();
        //System.out.println("DEBUG : " + chest.getPersistentData());
        //boolean isStolen = data.contains("lastThief");
        boolean stolen = ChestStolenData.isChestStolen(chest.getBlockPos());

        ResourceLocation texture = stolen ? STOLEN_CHEST_TEXTURE : DEFAULT_CHEST_TEXTURE;

        // Appelle le renderer vanilla avec une texture personnalisée
        VanillaChestRendererHelper.renderChestWithTexture(chest, texture, poseStack, bufferSource, combinedLight, combinedOverlay, partialTicks);
    }
}
