package com.sweadex.thechestwhisperer.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;

public class VanillaChestRendererHelper {

    public static void renderChestWithTexture(ChestBlockEntity chest, ResourceLocation texture,
                                              PoseStack poseStack, MultiBufferSource bufferSource,
                                              int packedLight, int packedOverlay, float partialTicks) {

        Level level = chest.getLevel();
        boolean isInWorld = level != null;

        BlockState state = chest.getBlockState();
        if (!(state.getBlock() instanceof ChestBlock)) return;

        ChestType type = state.getValue(ChestBlock.TYPE);

        Direction facing;
        if (isInWorld) {
            facing = chest.getBlockState().getValue(ChestBlock.FACING);
        } else {
            facing = Direction.SOUTH;
        }

        ModelLayerLocation layer = switch (type) {
            case LEFT -> ChestModelLayers.LEFT;
            case RIGHT -> ChestModelLayers.RIGHT;
            default -> ChestModelLayers.SINGLE;
        };

        ModelPart model = Minecraft.getInstance().getEntityModels().bakeLayer(layer);

        ModelPart lid = model.getChild("lid");
        ModelPart base = model.getChild("bottom");
        ModelPart latch = model.getChild("lock");

        poseStack.pushPose();

        // Rotation de base selon la direction
        float rotation = switch (facing) {
            case NORTH -> 180.0F;
            case SOUTH -> 0.0F;
            case WEST -> -90.0F;
            case EAST -> 90.0F;
            default -> 180.0F;
        };

        poseStack.translate(0.5D, 0.0D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        poseStack.translate(-0.5D, 0.0D, -0.5D);

        // Animation
        float lidAngle = chest.getOpenNess(partialTicks);
        lidAngle = 1.0F - lidAngle;
        lidAngle = 1.0F - (lidAngle * lidAngle * lidAngle);
        lid.xRot = -(lidAngle * ((float) Math.PI / 2F));
        latch.xRot = lid.xRot;

        var vertexConsumer = bufferSource.getBuffer(RenderType.entityCutout(texture));
        lid.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        latch.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        base.render(poseStack, vertexConsumer, packedLight, packedOverlay);

        poseStack.popPose();
    }
}
