package com.cgessinger.creaturesandbeasts.client.entity.model;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeastsConstants;
import com.cgessinger.creaturesandbeasts.entities.YetiEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class YetiModel extends GeoModel<YetiEntity> {
    private static final ResourceLocation YETI_MODEL = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "geo/entity/yeti/yeti.geo.json");
    private static final ResourceLocation BABY_YETI_MODEL = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "geo/entity/yeti/baby_yeti.geo.json");

    private static final ResourceLocation YETI_TEXTURE = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "textures/entity/yeti/yeti.png");
    private static final ResourceLocation BABY_YETI_TEXTURE = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "textures/entity/yeti/baby_yeti.png");

    private static final ResourceLocation YETI_ANIMATIONS = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "animations/yeti.json");

    @Override
    public ResourceLocation getModelResource(YetiEntity entity) {
        return entity.isBaby() ? BABY_YETI_MODEL : YETI_MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(YetiEntity entity) {
        return entity.isBaby() ? BABY_YETI_TEXTURE : YETI_TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(YetiEntity entity) {
        return YETI_ANIMATIONS;
    }

    @Override
    public void setCustomAnimations(YetiEntity animatable, long instanceId, AnimationState<YetiEntity> animationEvent) {
        super.setCustomAnimations(animatable, instanceId, animationEvent);

        GeoBone head_rotation = this.getAnimationProcessor().getBone("head_rotation");

        EntityModelData extraData = animationEvent.getData(DataTickets.ENTITY_MODEL_DATA);

        head_rotation.setRotX(extraData.headPitch() * ((float) Math.PI / 180F));
        if (animatable.isBaby()) {
            head_rotation.setRotZ(extraData.netHeadYaw() * ((float) Math.PI / 180F));
        } else {
            head_rotation.setRotY(extraData.netHeadYaw() * ((float) Math.PI / 180F));
        }
    }
}
