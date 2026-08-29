package com.cgessinger.creaturesandbeasts;

import net.fabricmc.api.ModInitializer;

public class CreaturesAndBeasts implements ModInitializer {
    @Override
    public void onInitialize() {
        CreaturesAndBeastsCommon.init();
    }
}
