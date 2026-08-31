package com.cgessinger.creaturesandbeasts.modules;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeastsConstants;
import com.helliongames.hellionsapi.registration.registries.HellionsAPIDataComponentTypeRegistry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.util.ExtraCodecs;

public class CNBDataComponentTypeModule {
    public static final HellionsAPIDataComponentTypeRegistry DATA_COMPONENT_TYPES = new HellionsAPIDataComponentTypeRegistry(CreaturesAndBeastsConstants.MOD_ID);

    public static final DataComponentType<Integer> IMBUE_TICKS = DATA_COMPONENT_TYPES.register("imbue_ticks", DataComponentType.<Integer>builder().persistent(ExtraCodecs.POSITIVE_INT).build());
    public static final DataComponentType<Integer> IMBUE_LEVEL = DATA_COMPONENT_TYPES.register("imbue_level", DataComponentType.<Integer>builder().persistent(ExtraCodecs.POSITIVE_INT).build());
    public static final DataComponentType<Integer> HIDE_LEVEL = DATA_COMPONENT_TYPES.register("hide_level", DataComponentType.<Integer>builder().persistent(ExtraCodecs.POSITIVE_INT).build());

    // Called in the mod initializer / constructor in order to make sure that items are registered
    public static void load() {}
}
