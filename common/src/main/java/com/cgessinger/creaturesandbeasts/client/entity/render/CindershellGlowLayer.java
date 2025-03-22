package com.cgessinger.creaturesandbeasts.client.entity.render;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeastsConstants;
import com.cgessinger.creaturesandbeasts.entities.CindershellEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.texture.AutoGlowingTexture;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.ClientUtil;

public class CindershellGlowLayer extends GeoRenderLayer<CindershellEntity> {
    private static final ResourceLocation GLOW_LAYER = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "textures/entity/cindershell/cindershell_glow.png");

    public CindershellGlowLayer(GeoEntityRenderer<CindershellEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    protected @Nullable RenderType getRenderType(CindershellEntity animatable, @Nullable MultiBufferSource bufferSource) {
        if (animatable instanceof Entity entity) {
            boolean invisible = entity.isInvisible();
            ResourceLocation texture = AutoGlowingTexture.getEmissiveResource(GLOW_LAYER);
            if (invisible && !entity.isInvisibleTo(ClientUtil.getClientPlayer())) {
                return RenderType.itemEntityTranslucentCull(texture);
            } else if (Minecraft.getInstance().shouldEntityAppearGlowing(entity)) {
                return invisible ? RenderType.outline(texture) : AutoGlowingTexture.getOutlineRenderType(GLOW_LAYER);
            } else {
                return invisible ? null : AutoGlowingTexture.getRenderType(GLOW_LAYER);
            }
        } else {
            return AutoGlowingTexture.getRenderType(GLOW_LAYER);
        }
    }

    @Override
    public void render(PoseStack poseStack, CindershellEntity animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if (!animatable.isBaby()) {
            renderType = this.getRenderType(animatable, bufferSource);
            if (renderType != null) {
                this.getRenderer().reRender(this.getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, renderType, bufferSource.getBuffer(renderType), partialTick, 15728640, packedOverlay, this.getRenderer().getRenderColor(animatable, partialTick, packedLight).argbInt());
            }
        }
    }
}
