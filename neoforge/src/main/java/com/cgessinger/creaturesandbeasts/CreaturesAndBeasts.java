package com.cgessinger.creaturesandbeasts;


import com.cgessinger.creaturesandbeasts.client.CNBClient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(CreaturesAndBeastsConstants.MOD_ID)
public class CreaturesAndBeasts {

    public CreaturesAndBeasts(IEventBus eventBus) {
        CreaturesAndBeastsCommon.init();

        eventBus.addListener(this::clientSetup);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(CNBClient::init);
    }
}