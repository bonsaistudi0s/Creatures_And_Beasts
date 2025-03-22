package com.cgessinger.creaturesandbeasts.modules;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeastsCommon;
import com.cgessinger.creaturesandbeasts.CreaturesAndBeastsConstants;
import com.helliongames.hellionsapi.registration.holders.BiomeModifierDataHolder;
import com.helliongames.hellionsapi.registration.registries.HellionsAPIBiomeModifierRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobCategory;

public class CNBBiomeModifierModule {

    public static final HellionsAPIBiomeModifierRegistry BIOME_MODIFIERS = new HellionsAPIBiomeModifierRegistry(CreaturesAndBeastsConstants.MOD_ID);

    public static final BiomeModifierDataHolder CACTEM_SPAWN_ADDER = BIOME_MODIFIERS.register("cactem_spawns", BiomeModifierDataHolder.of(
            BiomeModifierDataHolder.BiomeTarget.Tag.of(ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsCommon.MOD_ID, "dry_biomes")),
            BiomeModifierDataHolder.SpawnData.of(
                    MobCategory.CREATURE,
                    ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsCommon.MOD_ID, "cactem"),
                    3,
                    6,
                    13
            )
    ));

    public static final BiomeModifierDataHolder CINDERSHELL_SPAWN_ADDER = BIOME_MODIFIERS.register("cindershell_spawns", BiomeModifierDataHolder.of(
            BiomeModifierDataHolder.BiomeTarget.Location.of(ResourceLocation.withDefaultNamespace("nether_wastes")),
            BiomeModifierDataHolder.SpawnData.of(
                    MobCategory.CREATURE,
                    ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsCommon.MOD_ID, "cindershell"),
                    400,
                    2,
                    8
            )
    ));

    public static final BiomeModifierDataHolder END_WHALE_SPAWN_ADDER = BIOME_MODIFIERS.register("end_whale_spawns", BiomeModifierDataHolder.of(
            BiomeModifierDataHolder.BiomeTarget.Tag.of(ResourceLocation.withDefaultNamespace("is_end")),
            BiomeModifierDataHolder.SpawnData.of(
                    MobCategory.CREATURE,
                    ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsCommon.MOD_ID, "end_whale"),
                    1,
                    1,
                    1,
                    400,
                    1
            )
    ));

    public static final BiomeModifierDataHolder LILYTAD_SPAWN_ADDER = BIOME_MODIFIERS.register("lilytad_spawns", BiomeModifierDataHolder.of(
            BiomeModifierDataHolder.BiomeTarget.Location.of(ResourceLocation.withDefaultNamespace("swamp")),
            BiomeModifierDataHolder.SpawnData.of(
                    MobCategory.CREATURE,
                    ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsCommon.MOD_ID, "lilytad"),
                    45,
                    1,
                    1
            )
    ));

    public static final BiomeModifierDataHolder LITTLE_GREBE_SPAWN_ADDER = BIOME_MODIFIERS.register("little_grebe_spawns", BiomeModifierDataHolder.of(
            BiomeModifierDataHolder.BiomeTarget.Location.of(ResourceLocation.withDefaultNamespace("river")),
            BiomeModifierDataHolder.SpawnData.of(
                    MobCategory.CREATURE,
                    ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsCommon.MOD_ID, "little_grebe"),
                    35,
                    2,
                    3
            )
    ));

    public static final BiomeModifierDataHolder MINIPAD_SPAWN_ADDER = BIOME_MODIFIERS.register("minipad_spawns", BiomeModifierDataHolder.of(
            BiomeModifierDataHolder.BiomeTarget.Location.of(ResourceLocation.withDefaultNamespace("swamp")),
            BiomeModifierDataHolder.SpawnData.of(
                    MobCategory.CREATURE,
                    ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsCommon.MOD_ID, "minipad"),
                    20,
                    3,
                    6
            )
    ));

    public static final BiomeModifierDataHolder YETI_PEAKS_SPAWN_ADDER = BIOME_MODIFIERS.register("yeti_peaks_spawns", BiomeModifierDataHolder.of(
            BiomeModifierDataHolder.BiomeTarget.Location.of(ResourceLocation.withDefaultNamespace("frozen_peaks")),
            BiomeModifierDataHolder.SpawnData.of(
                    MobCategory.CREATURE,
                    ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsCommon.MOD_ID, "yeti"),
                    1,
                    2,
                    3
            )
    ));

    public static final BiomeModifierDataHolder YETI_SPAWN_ADDER = BIOME_MODIFIERS.register("yeti_spawns", BiomeModifierDataHolder.of(
            BiomeModifierDataHolder.BiomeTarget.Tag.of(ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsCommon.MOD_ID, "yeti_biomes")),
            BiomeModifierDataHolder.SpawnData.of(
                    MobCategory.CREATURE,
                    ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsCommon.MOD_ID, "yeti"),
                    2,
                    2,
                    3
            )
    ));

    public static final BiomeModifierDataHolder LIZARD_DESERT_SPAWN_ADDER = BIOME_MODIFIERS.register("lizard_desert_spawns", BiomeModifierDataHolder.of(
            BiomeModifierDataHolder.BiomeTarget.Tag.of(ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsCommon.MOD_ID, "dry_biomes")),
            BiomeModifierDataHolder.SpawnData.of(
                    MobCategory.CREATURE,
                    ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsCommon.MOD_ID, "lizard"),
                    15,
                    1,
                    4
            )
    ));

    public static final BiomeModifierDataHolder LIZARD_JUNGLE_SPAWN_ADDER = BIOME_MODIFIERS.register("lizard_jungle_spawns", BiomeModifierDataHolder.of(
            BiomeModifierDataHolder.BiomeTarget.Tag.of(ResourceLocation.withDefaultNamespace("is_jungle")),
            BiomeModifierDataHolder.SpawnData.of(
                    MobCategory.CREATURE,
                    ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsCommon.MOD_ID, "lizard"),
                    100,
                    1,
                    4
            )
    ));

    public static final BiomeModifierDataHolder LIZARD_MUSHROOM_SPAWN_ADDER = BIOME_MODIFIERS.register("lizard_mushroom_spawns", BiomeModifierDataHolder.of(
            BiomeModifierDataHolder.BiomeTarget.Location.of(ResourceLocation.withDefaultNamespace("mushroom_fields")),
            BiomeModifierDataHolder.SpawnData.of(
                    MobCategory.CREATURE,
                    ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsCommon.MOD_ID, "lizard"),
                    10,
                    1,
                    4
            )
    ));

    public static final BiomeModifierDataHolder SPORELING_MUSHROOM_SPAWN_ADDER = BIOME_MODIFIERS.register("sporeling_mushroom_spawns", BiomeModifierDataHolder.of(
            BiomeModifierDataHolder.BiomeTarget.Location.of(ResourceLocation.withDefaultNamespace("mushroom_fields")),
            BiomeModifierDataHolder.SpawnData.of(
                    MobCategory.CREATURE,
                    ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsCommon.MOD_ID, "sporeling"),
                    20,
                    3,
                    5
            )
    ));

    public static final BiomeModifierDataHolder SPORELING_SWAMP_SPAWN_ADDER = BIOME_MODIFIERS.register("sporeling_swamp_spawns", BiomeModifierDataHolder.of(
            BiomeModifierDataHolder.BiomeTarget.Location.of(ResourceLocation.withDefaultNamespace("swamp")),
            BiomeModifierDataHolder.SpawnData.of(
                    MobCategory.CREATURE,
                    ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsCommon.MOD_ID, "sporeling"),
                    25,
                    3,
                    5
            )
    ));

    public static final BiomeModifierDataHolder SPORELING_LUSH_SPAWN_ADDER = BIOME_MODIFIERS.register("sporeling_lush_spawns", BiomeModifierDataHolder.of(
            BiomeModifierDataHolder.BiomeTarget.Location.of(ResourceLocation.withDefaultNamespace("lush_caves")),
            BiomeModifierDataHolder.SpawnData.of(
                    MobCategory.CREATURE,
                    ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsCommon.MOD_ID, "sporeling"),
                    60,
                    3,
                    5
            )
    ));

    public static final BiomeModifierDataHolder SPORELING_DARK_SPAWN_ADDER = BIOME_MODIFIERS.register("sporeling_dark_spawns", BiomeModifierDataHolder.of(
            BiomeModifierDataHolder.BiomeTarget.Location.of(ResourceLocation.withDefaultNamespace("dark_forest")),
            BiomeModifierDataHolder.SpawnData.of(
                    MobCategory.CREATURE,
                    ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsCommon.MOD_ID, "sporeling"),
                    70,
                    3,
                    5
            )
    ));

    public static final BiomeModifierDataHolder SPORELING_WASTES_SPAWN_ADDER = BIOME_MODIFIERS.register("sporeling_wastes_spawns", BiomeModifierDataHolder.of(
            BiomeModifierDataHolder.BiomeTarget.Location.of(ResourceLocation.withDefaultNamespace("nether_wastes")),
            BiomeModifierDataHolder.SpawnData.of(
                    MobCategory.MONSTER,
                    ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsCommon.MOD_ID, "sporeling"),
                    60,
                    2,
                    4
            )
    ));

    public static final BiomeModifierDataHolder SPORELING_WARPED_SPAWN_ADDER = BIOME_MODIFIERS.register("sporeling_warped_spawns", BiomeModifierDataHolder.of(
            BiomeModifierDataHolder.BiomeTarget.Location.of(ResourceLocation.withDefaultNamespace("warped_forest")),
            BiomeModifierDataHolder.SpawnData.of(
                    MobCategory.MONSTER,
                    ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsCommon.MOD_ID, "sporeling"),
                    2,
                    2,
                    4
            )
    ));

    public static final BiomeModifierDataHolder SPOORELING_CRIMSON_SPAWN_ADDER = BIOME_MODIFIERS.register("sporeling_crimson_spawns", BiomeModifierDataHolder.of(
            BiomeModifierDataHolder.BiomeTarget.Location.of(ResourceLocation.withDefaultNamespace("crimson_forest")),
            BiomeModifierDataHolder.SpawnData.of(
                    MobCategory.MONSTER,
                    ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsCommon.MOD_ID, "sporeling"),
                    120,
                    2,
                    4
            )
    ));

    // Called in the mod initializer / constructor in order to make sure that items are registered
    public static void load() {}
}
