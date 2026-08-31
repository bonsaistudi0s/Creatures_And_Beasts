package com.cgessinger.creaturesandbeasts.modules;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeastsConstants;
import com.cgessinger.creaturesandbeasts.entities.CactemEntity;
import com.cgessinger.creaturesandbeasts.entities.CindershellEntity;
import com.cgessinger.creaturesandbeasts.entities.EndWhaleEntity;
import com.cgessinger.creaturesandbeasts.entities.LilytadEntity;
import com.cgessinger.creaturesandbeasts.entities.LittleGrebeEntity;
import com.cgessinger.creaturesandbeasts.entities.LizardEntity;
import com.cgessinger.creaturesandbeasts.entities.MinipadEntity;
import com.cgessinger.creaturesandbeasts.entities.SporelingEntity;
import com.cgessinger.creaturesandbeasts.entities.YetiEntity;
import com.helliongames.hellionsapi.registration.holders.SpawnPlacementDataHolder;
import com.helliongames.hellionsapi.registration.registries.HellionsAPISpawnPlacementRegistry;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;

public class CNBSpawnPlacementModule {
    public static final HellionsAPISpawnPlacementRegistry SPAWN_PLACEMENTS = new HellionsAPISpawnPlacementRegistry(CreaturesAndBeastsConstants.MOD_ID);

    static {
        SPAWN_PLACEMENTS.register(SpawnPlacementDataHolder.of(CNBEntityModule.CACTEM::get, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CactemEntity::checkMobSpawnRules));
        SPAWN_PLACEMENTS.register(SpawnPlacementDataHolder.of(CNBEntityModule.LITTLE_GREBE::get, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, LittleGrebeEntity::checkGrebeSpawnRules));
        SPAWN_PLACEMENTS.register(SpawnPlacementDataHolder.of(CNBEntityModule.LIZARD::get, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, LizardEntity::checkLizardSpawnRules));
        SPAWN_PLACEMENTS.register(SpawnPlacementDataHolder.of(CNBEntityModule.CINDERSHELL::get, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CindershellEntity::checkCindershellSpawnRules));
        SPAWN_PLACEMENTS.register(SpawnPlacementDataHolder.of(CNBEntityModule.SPORELING::get, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SporelingEntity::checkSporelingSpawnRules));
        SPAWN_PLACEMENTS.register(SpawnPlacementDataHolder.of(CNBEntityModule.LILYTAD::get, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, LilytadEntity::checkLilytadSpawnRules));
        SPAWN_PLACEMENTS.register(SpawnPlacementDataHolder.of(CNBEntityModule.YETI::get, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, YetiEntity::checkMobSpawnRules));
        SPAWN_PLACEMENTS.register(SpawnPlacementDataHolder.of(CNBEntityModule.MINIPAD::get, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MinipadEntity::checkMinipadSpawnRules));
        SPAWN_PLACEMENTS.register(SpawnPlacementDataHolder.of(CNBEntityModule.END_WHALE::get, SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndWhaleEntity::checkEndWhaleSpawnRules));
    }

    // Called in the mod initializer / constructor in order to make sure that items are registered
    public static void load() {}
}
