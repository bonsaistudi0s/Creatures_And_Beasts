package com.cgessinger.creaturesandbeasts.client.entity.model;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeastsConstants;
import com.cgessinger.creaturesandbeasts.entities.EndWhaleEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class EndWhaleModel extends GeoModel<EndWhaleEntity> {
    private static final ResourceLocation END_WHALE_MODEL = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "geo/entity/end_whale/end_whale.geo.json");

    private static final ResourceLocation END_WHALE_TEXTURE = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "textures/entity/end_whale/end_whale.png");
    private static final ResourceLocation END_WHALE_SADDLE_TEXTURE = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "textures/entity/end_whale/end_whale_saddle.png");

    private static final ResourceLocation END_WHALE_ANIMATIONS = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "animations/end_whale.json");


    @Override
    public ResourceLocation getModelResource(EndWhaleEntity entity) {
        return END_WHALE_MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(EndWhaleEntity entity) {
        return entity.isSaddled() ? END_WHALE_SADDLE_TEXTURE : END_WHALE_TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(EndWhaleEntity entity) {
        return END_WHALE_ANIMATIONS;
    }
}
