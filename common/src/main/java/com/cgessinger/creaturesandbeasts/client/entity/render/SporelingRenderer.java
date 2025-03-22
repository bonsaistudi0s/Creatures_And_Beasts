package com.cgessinger.creaturesandbeasts.client.entity.render;

import com.cgessinger.creaturesandbeasts.client.entity.model.SporelingModel;
import com.cgessinger.creaturesandbeasts.entities.SporelingEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SporelingRenderer extends GeoEntityRenderer<SporelingEntity> {

    public SporelingRenderer(EntityRendererProvider.Context context) {
        super(context, new SporelingModel());
        this.shadowRadius = 0.4F;
    }

    @Override
    public @Nullable RenderType getRenderType(SporelingEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(texture);
    }

    @Override
    public void render(SporelingEntity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        boolean isRidingCrouch = false;
        boolean isRidingAttacking = false;

        if (entity.getVehicle() instanceof Player player && player.isCrouching()) {
            stack.pushPose();
            float yRotLerped = Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot());
            stack.mulPose(Axis.XP.rotationDegrees(-25.0F * Mth.cos(yRotLerped * Mth.DEG_TO_RAD)));
            stack.mulPose(Axis.ZP.rotationDegrees(-25.0F * Mth.sin(yRotLerped * Mth.DEG_TO_RAD)));
            isRidingCrouch = true;
        }

        if (entity.getVehicle() instanceof Player player && player.getAttackAnim(partialTicks) > 0) {
            stack.pushPose();
            float rotation = player.getAttackAnim(partialTicks);
            stack.mulPose(Axis.YP.rotationDegrees(-Mth.sin(Mth.sqrt(rotation) * ((float)Math.PI * 2F)) * 0.2F * Mth.RAD_TO_DEG));
            isRidingAttacking = true;
        }

        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);

        if (isRidingCrouch) {
            stack.popPose();
        }
        if (isRidingAttacking) {
            stack.popPose();
        }
    }

    @Override
    public void renderRecursively(PoseStack poseStack, SporelingEntity animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        if (bone.getName().equals("itemHolder")) {
            poseStack.pushPose();
            poseStack.scale(0.5F, 0.5F, 0.5F);
            poseStack.translate(0.6F, 0.1F, -0.1F);
            poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
            Minecraft.getInstance().getItemRenderer().renderStatic(this.animatable.getItemBySlot(EquipmentSlot.MAINHAND), ItemDisplayContext.THIRD_PERSON_LEFT_HAND, packedLight, packedOverlay, poseStack, bufferSource, animatable.level(), 0);
            poseStack.popPose();
        }

        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }
}
