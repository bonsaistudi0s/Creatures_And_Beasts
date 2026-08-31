package com.cgessinger.creaturesandbeasts;


import com.cgessinger.creaturesandbeasts.client.CNBClient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(value = CreaturesAndBeastsConstants.MOD_ID)
public class CreaturesAndBeasts {
    public CreaturesAndBeasts(IEventBus eventBus) {
        CreaturesAndBeastsCommon.init();
        CNBClient.init();
    }
}