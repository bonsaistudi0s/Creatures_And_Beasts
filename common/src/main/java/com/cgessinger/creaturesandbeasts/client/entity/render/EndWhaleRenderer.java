package com.cgessinger.creaturesandbeasts.client.entity.render;

import com.cgessinger.creaturesandbeasts.client.entity.model.EndWhaleModel;
import com.cgessinger.creaturesandbeasts.entities.EndWhaleEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class EndWhaleRenderer extends GeoEntityRenderer<EndWhaleEntity> {

    public EndWhaleRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new EndWhaleModel());
        this.shadowRadius = 1.5F;
    }

    @Override
    public @Nullable RenderType getRenderType(EndWhaleEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(texture);
    }

    @Override
    protected void applyRotations(EndWhaleEntity endWhale, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        super.applyRotations(endWhale, poseStack, ageInTicks, rotationYaw, partialTick, nativeScale);
        float whaleRotY = endWhale.getViewYRot(partialTick);
        float wantedRotY;
        float whaleRotX = endWhale.getViewXRot(partialTick);
        float wantedRotX;
        Entity rider = endWhale.getFirstPassenger();

        if (rider != null) {
            wantedRotY = rider.getViewYRot(partialTick);
            wantedRotX = rider.getViewXRot(partialTick);
        } else {
            wantedRotY = endWhale.yBodyRot;
            wantedRotX = endWhale.getXRot();
        }

        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.wrapDegrees(whaleRotY - wantedRotY) / 2));
        poseStack.mulPose(Axis.XP.rotationDegrees(Mth.wrapDegrees(whaleRotX - wantedRotX)));
    }
}
