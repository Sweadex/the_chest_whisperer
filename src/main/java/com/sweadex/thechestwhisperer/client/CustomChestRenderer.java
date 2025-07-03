package com.sweadex.thechestwhisperer.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;

public class CustomChestRenderer implements BlockEntityRenderer<ChestBlockEntity> {

    private static final ResourceLocation DEFAULT_CHEST_TEXTURE = new ResourceLocation("minecraft", "textures/entity/chest/normal.png");
    private static final ResourceLocation DEFAULT_CHEST_LEFT_TEXTURE = new ResourceLocation("minecraft", "textures/entity/chest/normal_left.png");
    private static final ResourceLocation DEFAULT_CHEST_RIGHT_TEXTURE = new ResourceLocation("minecraft", "textures/entity/chest/normal_right.png");
    private static final ResourceLocation STOLEN_CHEST_TEXTURE = new ResourceLocation("thechestwhisperer", "textures/entity/chest/stolen.png");
    private static final ResourceLocation STOLEN_CHEST_LEFT_TEXTURE = new ResourceLocation("thechestwhisperer", "textures/entity/chest/stolen_left.png");
    private static final ResourceLocation STOLEN_CHEST_RIGHT_TEXTURE = new ResourceLocation("thechestwhisperer", "textures/entity/chest/stolen_right.png");

    public CustomChestRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(ChestBlockEntity chest, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {

        BlockState state = chest.getBlockState();

        if (!(state.getBlock() instanceof ChestBlock)) return;

        ChestType type = state.getValue(ChestBlock.TYPE);


        boolean stolen = ChestStolenData.isChestStolen(chest.getBlockPos());

        ResourceLocation texture = switch (type) {
            case SINGLE -> stolen ? STOLEN_CHEST_TEXTURE : DEFAULT_CHEST_TEXTURE;
            case RIGHT -> stolen ? STOLEN_CHEST_RIGHT_TEXTURE : DEFAULT_CHEST_RIGHT_TEXTURE;
            case LEFT -> stolen ? STOLEN_CHEST_LEFT_TEXTURE : DEFAULT_CHEST_LEFT_TEXTURE;
            default -> DEFAULT_CHEST_TEXTURE;
        };

        VanillaChestRendererHelper.renderChestWithTexture(
                chest, texture, poseStack, bufferSource, combinedLight, combinedOverlay, partialTicks
        );
    }
}

