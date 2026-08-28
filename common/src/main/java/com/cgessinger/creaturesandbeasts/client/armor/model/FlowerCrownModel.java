package com.cgessinger.creaturesandbeasts.client.armor.model;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeastsConstants;
import com.cgessinger.creaturesandbeasts.items.FlowerCrownItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class FlowerCrownModel extends GeoModel<FlowerCrownItem> {
    private final ResourceLocation FLOWER_CROWN_MODEL = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "geo/armor/flower_crown.geo.json");
    private final ResourceLocation FLOWER_CROWN_TEXTURE = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "textures/armor/flower_crown.png");
    private final ResourceLocation FLOWER_CROWN_ANIMATION = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "animations/flower_crown.json");

    @Override
    public ResourceLocation getModelResource(FlowerCrownItem object) {
        return FLOWER_CROWN_MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(FlowerCrownItem object) {
        return FLOWER_CROWN_TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(FlowerCrownItem animatable) {
        return FLOWER_CROWN_ANIMATION;
    }
}
