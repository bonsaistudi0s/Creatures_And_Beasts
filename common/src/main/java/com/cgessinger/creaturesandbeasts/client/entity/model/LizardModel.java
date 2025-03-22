package com.cgessinger.creaturesandbeasts.client.entity.model;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeastsConstants;
import com.cgessinger.creaturesandbeasts.entities.LizardEntity;
import com.cgessinger.creaturesandbeasts.modules.CNBLizardTypeModule;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class LizardModel extends GeoModel<LizardEntity> {
    private static final ResourceLocation LIZARD_MODEL = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "geo/entity/lizard/lizard.geo.json");
    private static final ResourceLocation MUSHROOM_LIZARD_MODEL = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "geo/entity/lizard/mushroom_lizard.geo.json");
    private static final ResourceLocation SAD_LIZARD_MODEL = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "geo/entity/lizard/sad_lizard.geo.json");
    private static final ResourceLocation SAD_MUSHROOM_LIZARD_MODEL = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "geo/entity/lizard/sad_mushroom_lizard.geo.json");

    private static final ResourceLocation LIZARD_ANIMATIONS = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "animations/lizard.json");

    @Override
    public ResourceLocation getModelResource(LizardEntity entity) {
        if (entity.getLizardType().equals(CNBLizardTypeModule.MUSHROOM)) {
            return entity.getSad() ? SAD_MUSHROOM_LIZARD_MODEL : MUSHROOM_LIZARD_MODEL;
        }
        
        return entity.getSad() ? SAD_LIZARD_MODEL : LIZARD_MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(LizardEntity entity) {
        return entity.getSad() ? entity.getLizardType().getSadTextureLocation() : entity.getLizardType().getTextureLocation();
    }

    @Override
    public ResourceLocation getAnimationResource(LizardEntity entity) {
        return LIZARD_ANIMATIONS;
    }

    @Override
    public void setCustomAnimations(LizardEntity animatable, long instanceId, AnimationState<LizardEntity> animationEvent) {
        super.setCustomAnimations(animatable, instanceId, animationEvent);

        GeoBone head_rotation = this.getAnimationProcessor().getBone("head_rotation");

        EntityModelData extraData = animationEvent.getData(DataTickets.ENTITY_MODEL_DATA);

        head_rotation.setRotX(extraData.headPitch() * ((float) Math.PI / 180F));
        head_rotation.setRotY(extraData.netHeadYaw() * ((float) Math.PI / 180F));
    }
}