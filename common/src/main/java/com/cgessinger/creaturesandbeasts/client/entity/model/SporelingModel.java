package com.cgessinger.creaturesandbeasts.client.entity.model;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeastsConstants;
import com.cgessinger.creaturesandbeasts.entities.SporelingEntity;
import com.cgessinger.creaturesandbeasts.modules.CNBSporelingTypeModule;
import com.cgessinger.creaturesandbeasts.util.SporelingType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SporelingModel extends GeoModel<SporelingEntity> {
    private static final ResourceLocation SPORELING_ANIMATIONS = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "animations/sporeling.json");

    private static final ResourceLocation GOOMY_MODEL = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "geo/entity/sporeling/sporeling_goomy.geo.json");
    private static final ResourceLocation SHRIMPSNAIL_MODEL = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "geo/entity/sporeling/sporeling_shrimpsnail.geo.json");

    private static final ResourceLocation BIT0_TEXTURE = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "textures/entity/sporeling/sporeling_bit0.png");
    private static final ResourceLocation LISTACALISTA_TEXTURE = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "textures/entity/sporeling/sporeling_listacalista.png");
    private static final ResourceLocation YUNGWILDER_TEXTURE = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "textures/entity/sporeling/sporeling_yungwilder.png");
    private static final ResourceLocation GOOMY_TEXTURE = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "textures/entity/sporeling/sporeling_goomy.png");
    private static final ResourceLocation SHRIMPSNAIL_TEXTURE = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "textures/entity/sporeling/sporeling_shrimpsnail.png");

    @Override
    public ResourceLocation getModelResource(SporelingEntity entity) {
        if (entity.hasCustomName() && entity.getSporelingType().getHostility().equals(SporelingType.SporelingHostility.FRIENDLY)) {
            String customName = entity.getCustomName().getString();
            if (customName.equals("Bit0") || customName.equals("ListaCalista") || customName.equals("yungwilder")) {
                return CNBSporelingTypeModule.RED_OVERWORLD.getModelLocation();
            } else if (customName.equals("Goomy")) {
                return GOOMY_MODEL;
            } else if (customName.equals("ShrimpSnail")) {
                return SHRIMPSNAIL_MODEL;
            }
        }
        return entity.getSporelingType().getModelLocation();
    }

    @Override
    public ResourceLocation getTextureResource(SporelingEntity entity) {
        if (entity.hasCustomName() && entity.getSporelingType().getHostility().equals(SporelingType.SporelingHostility.FRIENDLY)) {
            String customName = entity.getCustomName().getString();
            switch (customName) {
                case "Bit0":
                    return BIT0_TEXTURE;
                case "ListaCalista":
                    return LISTACALISTA_TEXTURE;
                case "yungwilder" :
                    return YUNGWILDER_TEXTURE;
                case "Goomy":
                    return GOOMY_TEXTURE;
                case "ShrimpSnail":
                    return SHRIMPSNAIL_TEXTURE;
            }
        }

        return entity.getSporelingType().getTextureLocation();
    }

    @Override
    public ResourceLocation getAnimationResource(SporelingEntity entity) {
        return SPORELING_ANIMATIONS;
    }
}
