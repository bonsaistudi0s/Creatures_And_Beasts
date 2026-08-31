package com.cgessinger.creaturesandbeasts.client;

import net.fabricmc.api.ClientModInitializer;

public class CreaturesAndBeastsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CNBClient.init();
    }
}
