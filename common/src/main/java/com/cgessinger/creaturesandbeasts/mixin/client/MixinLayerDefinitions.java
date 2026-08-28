package com.cgessinger.creaturesandbeasts.mixin.client;

import com.cgessinger.creaturesandbeasts.client.entity.model.CactemSpearModel;
import com.google.common.collect.ImmutableMap;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(LayerDefinitions.class)
public class MixinLayerDefinitions {

    @Inject(method = "createRoots", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMap$Builder;build()Lcom/google/common/collect/ImmutableMap;"), remap = false)
    private static void CNB_createCactemSpearLayerDefinition(CallbackInfoReturnable<Map<ModelLayerLocation, LayerDefinition>> cir, @Local ImmutableMap.Builder<ModelLayerLocation, LayerDefinition> builder) {
        builder.put(CactemSpearModel.LAYER_LOCATION, CactemSpearModel.createLayer());
    }
}
