package com.cgessinger.creaturesandbeasts;

import com.helliongames.hellionsapi.HellionsAPICommon;
import net.fabricmc.api.ModInitializer;

public class CreaturesAndBeasts implements ModInitializer {
    @Override
    public void onInitialize() {
        CreaturesAndBeastsCommon.init();

        HellionsAPICommon.init(CreaturesAndBeastsConstants.MOD_ID);
    }
}
