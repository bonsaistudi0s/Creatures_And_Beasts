package com.cgessinger.creaturesandbeasts.events;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeastsCommon;
import com.cgessinger.creaturesandbeasts.client.armor.render.FlowerCrownRenderer;
import com.cgessinger.creaturesandbeasts.client.armor.render.SporelingBackpackRenderer;
import com.cgessinger.creaturesandbeasts.client.entity.model.CactemSpearModel;
import com.cgessinger.creaturesandbeasts.items.FlowerCrownItem;
import com.cgessinger.creaturesandbeasts.items.GlowingFlowerCrownItem;
import com.cgessinger.creaturesandbeasts.items.SporelingBackpackItem;

public class ClientEvents {

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CactemSpearModel.LAYER_LOCATION, CactemSpearModel::createLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.AddLayers event) {
        GeoArmorRenderer.registerArmorRenderer(FlowerCrownItem.class, FlowerCrownRenderer::new);
        GeoArmorRenderer.registerArmorRenderer(GlowingFlowerCrownItem.class, FlowerCrownRenderer::new);
        GeoArmorRenderer.registerArmorRenderer(SporelingBackpackItem.class, SporelingBackpackRenderer::new);
    }
}
