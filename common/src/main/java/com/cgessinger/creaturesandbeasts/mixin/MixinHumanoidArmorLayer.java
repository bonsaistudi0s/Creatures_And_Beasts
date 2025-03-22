package com.cgessinger.creaturesandbeasts.mixin;

import com.cgessinger.creaturesandbeasts.client.armor.render.FlowerCrownRenderer;
import com.cgessinger.creaturesandbeasts.modules.CNBItemModule;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(HumanoidArmorLayer.class)
public class MixinHumanoidArmorLayer {

    @ModifyVariable(method = "renderModel(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IZLnet/minecraft/client/model/Model;FFFLnet/minecraft/resources/ResourceLocation;)V", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;getArmorFoilBuffer(Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/renderer/RenderType;ZZ)Lcom/mojang/blaze3d/vertex/VertexConsumer;"))
    private VertexConsumer CNB_renderGlowingFlowerCrown(VertexConsumer value, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn, boolean isFoil, Model model, float red, float green, float blue, ResourceLocation armorResource) {
        if (model instanceof FlowerCrownRenderer flowerCrownRenderer && flowerCrownRenderer.getCurrentItem().is(CNBItemModule.GLOWING_FLOWER_CROWN.get())) {
            VertexConsumer vertexConsumer = bufferIn.getBuffer(RenderType.entityTranslucent(armorResource));
            model.renderToBuffer(stack, vertexConsumer, packedLightIn, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
            return bufferIn.getBuffer(RenderType.eyes(armorResource));
        }
        return value;
    }
}
