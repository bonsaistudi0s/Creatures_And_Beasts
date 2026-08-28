package com.cgessinger.creaturesandbeasts.mixin.client;

import com.cgessinger.creaturesandbeasts.entities.EndWhaleEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class MixinLivingEntityRenderer<T extends LivingEntity> {

    @Inject(method = "setupRotations", at = @At("RETURN"))
    private void CNB_setupWhaleRidingRotations(T entity, PoseStack poseStack, float bob, float yBodyRot, float partialTick, float scale, CallbackInfo ci) {
        if (entity.getVehicle() instanceof EndWhaleEntity endWhale) {
            float whaleRotY = endWhale.getViewYRot(partialTick);
            float playerRotY = entity.getViewYRot(partialTick);
            float whaleRotX = endWhale.getViewXRot(partialTick);
            float playerRotX = entity.getViewXRot(partialTick);
            poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.wrapDegrees(whaleRotY - playerRotY) / 2));
            poseStack.mulPose(Axis.XP.rotationDegrees(Mth.wrapDegrees(whaleRotX - playerRotX)));
        }
    }


    @Redirect(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getVehicle()Lnet/minecraft/world/entity/Entity;"))
    private Entity CNB_redirectPlayerRotOnWhale(LivingEntity entity) {
        if (entity.getVehicle() instanceof EndWhaleEntity) {
            return null;
        } else {
            return entity.getVehicle();
        }
    }
}
