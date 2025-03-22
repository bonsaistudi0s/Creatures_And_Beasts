package com.cgessinger.creaturesandbeasts.client.entity.model;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeastsConstants;
import com.cgessinger.creaturesandbeasts.entities.CactemEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CactemModel extends GeoModel<CactemEntity> {
    private static final ResourceLocation ELDER_CACTEM_MODEL = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "geo/entity/cactem/elder_cactem.geo.json");
    private static final ResourceLocation WARRIOR_CACTEM_MODEL = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "geo/entity/cactem/warrior_cactem.geo.json");
    private static final ResourceLocation BABY_CACTEM_MODEL = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "geo/entity/cactem/baby_cactem.geo.json");

    private static final ResourceLocation ELDER_CACTEM_TEXTURE = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "textures/entity/cactem/elder_cactem.png");
    private static final ResourceLocation WARRIOR_CACTEM_TEXTURE = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "textures/entity/cactem/warrior_cactem.png");
    private static final ResourceLocation BABY_CACTEM_TEXTURE = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "textures/entity/cactem/baby_cactem.png");

    private static final ResourceLocation CACTEM_ANIMATIONS = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "animations/cactem.json");

    @Override
    public ResourceLocation getModelResource(CactemEntity entity) {
        if (entity.isBaby()) {
            return BABY_CACTEM_MODEL;
        } else if (entity.isElder()) {
            return ELDER_CACTEM_MODEL;
        } else {
            return WARRIOR_CACTEM_MODEL;
        }
    }

    @Override
    public ResourceLocation getTextureResource(CactemEntity entity) {
        if (entity.isBaby()) {
            return BABY_CACTEM_TEXTURE;
        } else if (entity.isElder()) {
            return ELDER_CACTEM_TEXTURE;
        } else {
            return WARRIOR_CACTEM_TEXTURE;
        }
    }

    @Override
    public ResourceLocation getAnimationResource(CactemEntity entity) {
        return CACTEM_ANIMATIONS;
    }
}
