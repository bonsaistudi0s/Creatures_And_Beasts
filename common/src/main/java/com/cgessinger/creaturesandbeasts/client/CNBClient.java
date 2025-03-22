package com.cgessinger.creaturesandbeasts.client;

import com.cgessinger.creaturesandbeasts.client.entity.render.*;
import com.cgessinger.creaturesandbeasts.client.gui.screens.inventory.CinderFurnaceScreen;
import com.cgessinger.creaturesandbeasts.mixin.accessor.ItemPropertiesAccessor;
import com.cgessinger.creaturesandbeasts.modules.CNBEntityModule;
import com.cgessinger.creaturesandbeasts.modules.CNBItemModule;
import com.cgessinger.creaturesandbeasts.modules.CNBMenuModule;
import com.helliongames.hellionsapi.registration.registries.client.HellionsAPIEntityRendererRegistry;
import com.helliongames.hellionsapi.registration.registries.client.HellionsAPIMenuScreenRegistry;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;

public class CNBClient {
    public static void init() {
        HellionsAPIMenuScreenRegistry.register(CNBMenuModule.CINDER_FURNACE_MENU, CinderFurnaceScreen::new);

        HellionsAPIEntityRendererRegistry.register(CNBEntityModule.LITTLE_GREBE, LittleGrebeRenderer::new);
        HellionsAPIEntityRendererRegistry.register(CNBEntityModule.LIZARD, LizardRenderer::new);
        HellionsAPIEntityRendererRegistry.register(CNBEntityModule.CINDERSHELL, CindershellRenderer::new);
        HellionsAPIEntityRendererRegistry.register(CNBEntityModule.LILYTAD, LilytadRenderer::new);
        HellionsAPIEntityRendererRegistry.register(CNBEntityModule.SPORELING, SporelingRenderer::new);
        HellionsAPIEntityRendererRegistry.register(CNBEntityModule.YETI, YetiRenderer::new);
        HellionsAPIEntityRendererRegistry.register(CNBEntityModule.MINIPAD, MinipadRenderer::new);
        HellionsAPIEntityRendererRegistry.register(CNBEntityModule.END_WHALE, EndWhaleRenderer::new);
        HellionsAPIEntityRendererRegistry.register(CNBEntityModule.CACTEM, CactemRenderer::new);
        HellionsAPIEntityRendererRegistry.register(CNBEntityModule.LIZARD_EGG, manager -> new ThrownItemRenderer<>(manager, 1.0F, true));
        HellionsAPIEntityRendererRegistry.register(CNBEntityModule.THROWN_CACTEM_SPEAR, ThrownCactemSpearRenderer::new);

        ItemPropertiesAccessor.invokeRegister(CNBItemModule.CACTEM_SPEAR.get(), ResourceLocation.withDefaultNamespace("throwing"), (item, resourceLocation, entity, itemPropertyFunction) -> entity != null && entity.isUsingItem() && entity.getUseItem() == item ? 1.0F : 0.0F);
    }
}
