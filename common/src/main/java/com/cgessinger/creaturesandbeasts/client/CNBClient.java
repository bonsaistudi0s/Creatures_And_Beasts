package com.cgessinger.creaturesandbeasts.client;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeastsConstants;
import com.cgessinger.creaturesandbeasts.client.entity.model.CactemSpearModel;
import com.cgessinger.creaturesandbeasts.client.entity.render.CactemRenderer;
import com.cgessinger.creaturesandbeasts.client.entity.render.CindershellRenderer;
import com.cgessinger.creaturesandbeasts.client.entity.render.EndWhaleRenderer;
import com.cgessinger.creaturesandbeasts.client.entity.render.LilytadRenderer;
import com.cgessinger.creaturesandbeasts.client.entity.render.LittleGrebeRenderer;
import com.cgessinger.creaturesandbeasts.client.entity.render.LizardRenderer;
import com.cgessinger.creaturesandbeasts.client.entity.render.MinipadRenderer;
import com.cgessinger.creaturesandbeasts.client.entity.render.SporelingRenderer;
import com.cgessinger.creaturesandbeasts.client.entity.render.ThrownCactemSpearRenderer;
import com.cgessinger.creaturesandbeasts.client.entity.render.YetiRenderer;
import com.cgessinger.creaturesandbeasts.client.gui.screens.inventory.CinderFurnaceScreen;
import com.cgessinger.creaturesandbeasts.client.particle.CactemHealParticle;
import com.cgessinger.creaturesandbeasts.client.particle.MinipadFlowerParticle;
import com.cgessinger.creaturesandbeasts.mixin.accessor.ModelLayersAccessor;
import com.cgessinger.creaturesandbeasts.modules.CNBEntityModule;
import com.cgessinger.creaturesandbeasts.modules.CNBMenuModule;
import com.cgessinger.creaturesandbeasts.modules.CNBParticleTypeModule;
import com.helliongames.hellionsapi.client.HellionsAPICommonClient;
import com.helliongames.hellionsapi.registration.registries.client.HellionsAPIEntityRendererRegistry;
import com.helliongames.hellionsapi.registration.registries.client.HellionsAPIMenuScreenRegistry;
import com.helliongames.hellionsapi.registration.registries.client.HellionsAPIParticleFactoryRegistry;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class CNBClient {
    public static final HellionsAPIEntityRendererRegistry ENTITY_RENDERERS = new HellionsAPIEntityRendererRegistry(CreaturesAndBeastsConstants.MOD_ID);
    public static final HellionsAPIMenuScreenRegistry MENU_SCREENS = new HellionsAPIMenuScreenRegistry(CreaturesAndBeastsConstants.MOD_ID);
    public static final HellionsAPIParticleFactoryRegistry PARTICLE_FACTORIES = new HellionsAPIParticleFactoryRegistry(CreaturesAndBeastsConstants.MOD_ID);

    public static void init() {
        ENTITY_RENDERERS.register(CNBEntityModule.LITTLE_GREBE, LittleGrebeRenderer::new);
        ENTITY_RENDERERS.register(CNBEntityModule.LIZARD, LizardRenderer::new);
        ENTITY_RENDERERS.register(CNBEntityModule.CINDERSHELL, CindershellRenderer::new);
        ENTITY_RENDERERS.register(CNBEntityModule.LILYTAD, LilytadRenderer::new);
        ENTITY_RENDERERS.register(CNBEntityModule.SPORELING, SporelingRenderer::new);
        ENTITY_RENDERERS.register(CNBEntityModule.YETI, YetiRenderer::new);
        ENTITY_RENDERERS.register(CNBEntityModule.MINIPAD, MinipadRenderer::new);
        ENTITY_RENDERERS.register(CNBEntityModule.END_WHALE, EndWhaleRenderer::new);
        ENTITY_RENDERERS.register(CNBEntityModule.CACTEM, CactemRenderer::new);
        ENTITY_RENDERERS.register(CNBEntityModule.LIZARD_EGG, manager -> new ThrownItemRenderer<>(manager, 1.0F, true));
        ENTITY_RENDERERS.register(CNBEntityModule.THROWN_CACTEM_SPEAR, ThrownCactemSpearRenderer::new);

        MENU_SCREENS.register(CNBMenuModule.CINDER_FURNACE_MENU, CinderFurnaceScreen::new);

        PARTICLE_FACTORIES.register(CNBParticleTypeModule.PINK_MINIPAD_FLOWER, MinipadFlowerParticle.Factory::new);
        PARTICLE_FACTORIES.register(CNBParticleTypeModule.LIGHT_PINK_MINIPAD_FLOWER, MinipadFlowerParticle.Factory::new);
        PARTICLE_FACTORIES.register(CNBParticleTypeModule.YELLOW_MINIPAD_FLOWER, MinipadFlowerParticle.Factory::new);
        PARTICLE_FACTORIES.register(CNBParticleTypeModule.CACTEM_HEAL_PARTICLE, CactemHealParticle.Factory::new);

//        ItemPropertiesAccessor.invokeRegister(CNBItemModule.CACTEM_SPEAR.get(), ResourceLocation.withDefaultNamespace("throwing"), (item, resourceLocation, entity, itemPropertyFunction) -> entity != null && entity.isUsingItem() && entity.getUseItem() == item ? 1.0F : 0.0F);
        ModelLayersAccessor.getModelLayers().add(CactemSpearModel.LAYER_LOCATION);

        HellionsAPICommonClient.init(CreaturesAndBeastsConstants.MOD_ID);
    }
}
