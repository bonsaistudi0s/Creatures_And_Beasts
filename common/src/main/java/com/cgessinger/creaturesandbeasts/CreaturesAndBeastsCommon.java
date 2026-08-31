package com.cgessinger.creaturesandbeasts;

import com.cgessinger.creaturesandbeasts.modules.CNBBiomeModifierModule;
import com.cgessinger.creaturesandbeasts.modules.CNBBlockModule;
import com.cgessinger.creaturesandbeasts.modules.CNBDataComponentTypeModule;
import com.cgessinger.creaturesandbeasts.modules.CNBEntityModule;
import com.cgessinger.creaturesandbeasts.modules.CNBItemModule;
import com.cgessinger.creaturesandbeasts.modules.CNBLilytadTypeModule;
import com.cgessinger.creaturesandbeasts.modules.CNBLizardTypeModule;
import com.cgessinger.creaturesandbeasts.modules.CNBLootModifierModule;
import com.cgessinger.creaturesandbeasts.modules.CNBMenuModule;
import com.cgessinger.creaturesandbeasts.modules.CNBMinipadTypeModule;
import com.cgessinger.creaturesandbeasts.modules.CNBParticleTypeModule;
import com.cgessinger.creaturesandbeasts.modules.CNBSoundModule;
import com.cgessinger.creaturesandbeasts.modules.CNBSpawnPlacementModule;
import com.cgessinger.creaturesandbeasts.modules.CNBSporelingTypeModule;
import com.helliongames.hellionsapi.HellionsAPICommon;

public class CreaturesAndBeastsCommon {

    public static void init() {
        CNBBlockModule.load();
        CNBItemModule.load();
        CNBEntityModule.load();
        CNBSoundModule.load();
        CNBParticleTypeModule.load();
        CNBMenuModule.load();
        CNBBiomeModifierModule.load();
        CNBLootModifierModule.load();
        CNBDataComponentTypeModule.load();

        CNBSporelingTypeModule.registerAll();
        CNBLizardTypeModule.registerAll();
        CNBLilytadTypeModule.registerAll();
        CNBMinipadTypeModule.registerAll();

        CNBSpawnPlacementModule.load();

        HellionsAPICommon.init(CreaturesAndBeastsConstants.MOD_ID);
    }
}
