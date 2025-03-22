package com.cgessinger.creaturesandbeasts.client.entity.model;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeastsConstants;
import com.cgessinger.creaturesandbeasts.entities.LittleGrebeEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class LittleGrebeModel extends GeoModel<LittleGrebeEntity> {
    private static final ResourceLocation LITTLE_GREBE_MODEL = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "geo/entity/little_grebe/little_grebe.geo.json");
    private static final ResourceLocation LITTLE_GREBE_CHICK_MODEL = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "geo/entity/little_grebe/little_grebe_chick.geo.json");

    private static final ResourceLocation LITTLE_GREBE_TEXTURE = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "textures/entity/little_grebe/little_grebe.png");
    private static final ResourceLocation LITTLE_GREBE_CHICK_TEXTURE = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "textures/entity/little_grebe/little_grebe_chick.png");

    private static final ResourceLocation LITTLE_GREBE_ANIMATIONS = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "animations/little_grebe.json");

    @Override
    public ResourceLocation getModelResource(LittleGrebeEntity entity) {
        return entity.isBaby() ? LITTLE_GREBE_CHICK_MODEL : LITTLE_GREBE_MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(LittleGrebeEntity entity) {
        return entity.isBaby() ? LITTLE_GREBE_CHICK_TEXTURE : LITTLE_GREBE_TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(LittleGrebeEntity entity) {
        return LITTLE_GREBE_ANIMATIONS;
    }

    @Override
    public void setCustomAnimations(LittleGrebeEntity animatable, long instanceId, AnimationState<LittleGrebeEntity> animationEvent) {
        super.setCustomAnimations(animatable, instanceId, animationEvent);

        GeoBone head_rotation = this.getAnimationProcessor().getBone("head_rotation");

        EntityModelData extraData = animationEvent.getData(DataTickets.ENTITY_MODEL_DATA);

        head_rotation.setRotX(extraData.headPitch() * ((float) Math.PI / 180F));
        head_rotation.setRotY(extraData.netHeadYaw() * ((float) Math.PI / 180F));
    }
}