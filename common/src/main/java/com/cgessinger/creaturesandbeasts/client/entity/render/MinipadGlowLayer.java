package com.cgessinger.creaturesandbeasts.client.entity.render;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeastsConstants;
import com.cgessinger.creaturesandbeasts.entities.MinipadEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.Color;

public class MinipadGlowLayer extends GeoRenderLayer<MinipadEntity> {

    public MinipadGlowLayer(GeoEntityRenderer<MinipadEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, MinipadEntity animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        long time = animatable.level().getDayTime();

        if (animatable.isGlowing()) {
            RenderType eyesTexture = RenderType.entityTranslucent(ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "textures/entity/minipad/minipad_eyes_glow.png"));

            RenderType flowerGlow = RenderType.eyes(animatable.getMinipadType().getGlowTextureLocation());
            RenderType flowerTranslucent = RenderType.entityTranslucent(animatable.getMinipadType().getGlowTextureLocation());

            poseStack.pushPose();

            Color color = this.getRenderer().getRenderColor(animatable, partialTick, packedLight);

            if (!animatable.getSheared()) {
                this.getRenderer().reRender(this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(animatable)), poseStack, bufferSource, animatable, flowerGlow, bufferSource.getBuffer(flowerGlow), partialTick, packedLight, OverlayTexture.NO_OVERLAY, color.argbInt());
                this.getRenderer().reRender(this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(animatable)), poseStack, bufferSource, animatable, flowerTranslucent, bufferSource.getBuffer(flowerTranslucent), partialTick, packedLight, OverlayTexture.NO_OVERLAY, Color.ofARGB((float) Math.pow((time - 18000) / 5000f, 2), color.getRedFloat(), color.getGreenFloat(), color.getBlueFloat()).argbInt());
            }
            this.getRenderer().reRender(this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(animatable)), poseStack, bufferSource, animatable, eyesTexture, bufferSource.getBuffer(eyesTexture), partialTick, packedLight, OverlayTexture.NO_OVERLAY, Color.ofARGB((float) Math.pow((time - 18000) / 5000f, 2), color.getRedFloat(), color.getGreenFloat(), color.getBlueFloat()).argbInt());

            poseStack.popPose();
        }
    }
}
