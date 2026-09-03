package com.cgessinger.creaturesandbeasts.client;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeastsConstants;
import com.cgessinger.creaturesandbeasts.modules.CNBDataComponentTypeModule;
import com.cgessinger.creaturesandbeasts.modules.CNBItemModule;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;

public class CreaturesAndBeastsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CNBClient.init();

        ItemProperties.register(CNBItemModule.CACTEM_SPEAR.get(), ResourceLocation.withDefaultNamespace("throwing"), (item, resourceLocation, entity, itemPropertyFunction) -> entity != null && entity.isUsingItem() && entity.getUseItem() == item ? 1.0F : 0.0F);
        ItemProperties.register(CNBItemModule.CINDER_SWORD.get(), ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "imbue_level"), (item, resourcelocation, entity, itemPropertyFunction) -> item.getOrDefault(CNBDataComponentTypeModule.IMBUE_LEVEL, 0));
    }
}
