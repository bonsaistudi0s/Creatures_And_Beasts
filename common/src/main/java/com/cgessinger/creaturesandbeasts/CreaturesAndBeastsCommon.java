package com.cgessinger.creaturesandbeasts;

import com.cgessinger.creaturesandbeasts.modules.*;
import com.cgessinger.creaturesandbeasts.world.gen.ModEntitySpawns;

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

        ModEntitySpawns.entitySpawnPlacementRegistry();
    }
}
