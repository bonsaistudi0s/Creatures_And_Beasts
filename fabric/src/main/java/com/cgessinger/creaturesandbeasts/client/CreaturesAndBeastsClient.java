package com.cgessinger.creaturesandbeasts.client;

import com.cgessinger.creaturesandbeasts.client.gui.screens.inventory.CinderFurnaceScreen;
import com.cgessinger.creaturesandbeasts.modules.CNBMenuModule;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

public class CreaturesAndBeastsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MenuScreens.register(CNBMenuModule.CINDER_FURNACE_MENU.get(), CinderFurnaceScreen::new);

        CNBClient.init();
    }
}
