package com.cgessinger.creaturesandbeasts.world.gen;

import com.cgessinger.creaturesandbeasts.entities.*;
import com.cgessinger.creaturesandbeasts.mixin.accessor.SpawnPlacementsAccessor;
import com.cgessinger.creaturesandbeasts.modules.CNBEntityModule;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;

public class ModEntitySpawns {

    public static void entitySpawnPlacementRegistry() {
        SpawnPlacementsAccessor.register(CNBEntityModule.LITTLE_GREBE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, LittleGrebeEntity::checkGrebeSpawnRules);
        SpawnPlacementsAccessor.register(CNBEntityModule.LIZARD.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, LizardEntity::checkLizardSpawnRules);
        SpawnPlacementsAccessor.register(CNBEntityModule.CINDERSHELL.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CindershellEntity::checkCindershellSpawnRules);
        SpawnPlacementsAccessor.register(CNBEntityModule.SPORELING.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SporelingEntity::checkSporelingSpawnRules);
        SpawnPlacementsAccessor.register(CNBEntityModule.LILYTAD.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, LilytadEntity::checkLilytadSpawnRules);
        SpawnPlacementsAccessor.register(CNBEntityModule.YETI.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, YetiEntity::checkMobSpawnRules);
        SpawnPlacementsAccessor.register(CNBEntityModule.MINIPAD.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MinipadEntity::checkMinipadSpawnRules);
        SpawnPlacementsAccessor.register(CNBEntityModule.END_WHALE.get(), SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndWhaleEntity::checkEndWhaleSpawnRules);
    }

}
