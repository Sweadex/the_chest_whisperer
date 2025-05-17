package com.sweadex.thechestwhisperer.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class VanillaChestRendererHelper {

    public static final ModelLayerLocation SINGLE_CHEST_LAYER = new ModelLayerLocation(new ResourceLocation("minecraft", "chest"), "main");

    public static void renderChestWithTexture(ChestBlockEntity chest, ResourceLocation texture,
                                              PoseStack poseStack, MultiBufferSource bufferSource,
                                              int packedLight, int packedOverlay, float partialTicks) {

        BlockState state = chest.getBlockState();
        if (!(state.getBlock() instanceof ChestBlock)) return;

        ModelPart model = Minecraft.getInstance().getEntityModels().bakeLayer(SINGLE_CHEST_LAYER);
        ModelPart lid = model.getChild("lid");
        ModelPart base = model.getChild("bottom");
        ModelPart latch = model.getChild("lock");

        poseStack.pushPose();

        Direction facing = state.getValue(ChestBlock.FACING);
        float rotation = switch (facing) {
            case NORTH -> 180.0F;
            case SOUTH -> 0.0F;
            case WEST -> -90.0F;
            case EAST -> 90.0F;
            default -> 0.0F;
        };

        poseStack.translate(0.5D, 0.0D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        poseStack.translate(-0.5D, 0.0D, -0.5D);

        // Utiliser partialTicks ici pour l’animation fluide
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
