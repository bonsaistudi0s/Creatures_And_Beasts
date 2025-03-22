package com.cgessinger.creaturesandbeasts.client.entity.render;

import com.cgessinger.creaturesandbeasts.client.entity.model.CindershellModel;
import com.cgessinger.creaturesandbeasts.entities.CindershellEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CindershellRenderer extends GeoEntityRenderer<CindershellEntity> {

    public CindershellRenderer(EntityRendererProvider.Context context) {
        super(context, new CindershellModel());
        this.addRenderLayer(new CindershellGlowLayer(this));
        this.shadowRadius = 0.4F;
    }

    @Override
    protected float getDeathMaxRotation(CindershellEntity entityLivingBaseIn) {
        return 0;
    }

    @Override
    public void renderRecursively(PoseStack poseStack, CindershellEntity animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        if (bone.getName().equals("itemHolder") && !animatable.isInvisible()) {
            poseStack.pushPose();
            poseStack.translate(0, 0.62D, -1.52D);
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            Minecraft.getInstance().getItemRenderer().renderStatic(animatable.getItemBySlot(EquipmentSlot.MAINHAND), ItemDisplayContext.THIRD_PERSON_LEFT_HAND, packedLight, packedOverlay, poseStack, bufferSource, animatable.level(), 0);
            poseStack.popPose();
        }
        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }
}
