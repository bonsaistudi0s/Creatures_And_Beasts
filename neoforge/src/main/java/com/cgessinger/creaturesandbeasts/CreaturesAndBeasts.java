package com.cgessinger.creaturesandbeasts;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(CreaturesAndBeastsConstants.MOD_ID)
public class CreaturesAndBeasts {

    public CreaturesAndBeasts(IEventBus eventBus) {
        CreaturesAndBeastsCommon.init();
    }
}