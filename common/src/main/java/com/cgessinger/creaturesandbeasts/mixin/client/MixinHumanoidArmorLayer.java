package com.cgessinger.creaturesandbeasts.mixin.client;

import com.cgessinger.creaturesandbeasts.client.armor.render.FlowerCrownRenderer;
import com.cgessinger.creaturesandbeasts.modules.CNBItemModule;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(HumanoidArmorLayer.class)
public class MixinHumanoidArmorLayer<T extends LivingEntity, A extends HumanoidModel<T>> {

    @Inject(method = "renderModel", at = @At("HEAD"), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    private void CNB_renderGlowingFlowerCrown(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, A model, int dyeColor, ResourceLocation textureLocation, CallbackInfo ci) {
        if (model instanceof FlowerCrownRenderer flowerCrownRenderer && flowerCrownRenderer.getCurrentStack().is(CNBItemModule.GLOWING_FLOWER_CROWN.get())) {
            VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(textureLocation));
            model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, dyeColor);
            bufferSource.getBuffer(RenderType.eyes(textureLocation));
            model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, dyeColor);
            ci.cancel();
        }
    }
}
