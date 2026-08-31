package com.cgessinger.creaturesandbeasts;


import com.cgessinger.creaturesandbeasts.client.CNBClient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(value = CreaturesAndBeastsConstants.MOD_ID)
public class CreaturesAndBeasts {
    public CreaturesAndBeasts(IEventBus eventBus) {
        CreaturesAndBeastsCommon.init();

        if (FMLEnvironment.dist.isClient()) {
            CNBClient.init();
        }
    }
}