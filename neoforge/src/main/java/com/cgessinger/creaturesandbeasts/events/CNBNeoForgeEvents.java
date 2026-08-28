package com.cgessinger.creaturesandbeasts.events;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeastsConstants;
import com.cgessinger.creaturesandbeasts.client.gui.screens.inventory.CinderFurnaceScreen;
import com.cgessinger.creaturesandbeasts.modules.CNBMenuModule;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = CreaturesAndBeastsConstants.MOD_ID)
public class CNBNeoForgeEvents {
    @SubscribeEvent // on the mod event bus only on the physical client
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(CNBMenuModule.CINDER_FURNACE_MENU.get(), CinderFurnaceScreen::new);
    }
}
