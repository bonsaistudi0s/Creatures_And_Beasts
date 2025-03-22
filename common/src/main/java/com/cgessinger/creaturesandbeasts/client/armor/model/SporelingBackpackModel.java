package com.cgessinger.creaturesandbeasts.client.armor.model;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeastsCommon;
import com.cgessinger.creaturesandbeasts.items.SporelingBackpackItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SporelingBackpackModel extends GeoModel<SporelingBackpackItem> {
    private final ResourceLocation SPORELING_BACKPACK_MODEL = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsCommon.MOD_ID, "geo/armor/sporeling_backpack.geo.json");
    private final ResourceLocation SPORELING_BACKPACK_TEXTURE = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsCommon.MOD_ID, "textures/armor/sporeling_backpack.png");
    private final ResourceLocation SPORELING_BACKPACK_ANIMATION = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsCommon.MOD_ID, "animations/sporeling_backpack.json");

    @Override
    public ResourceLocation getModelResource(SporelingBackpackItem object) {
        return SPORELING_BACKPACK_MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(SporelingBackpackItem object) {
        return SPORELING_BACKPACK_TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(SporelingBackpackItem animatable) {
        return null;
    }

}
