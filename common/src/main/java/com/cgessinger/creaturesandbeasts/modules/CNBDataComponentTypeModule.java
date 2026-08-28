package com.cgessinger.creaturesandbeasts.modules;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeastsConstants;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;

public class CNBDataComponentTypeModule {
    public static final DataComponentType<Integer> IMBUE_TICKS = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "imbue_ticks"),
            DataComponentType.<Integer>builder().persistent(ExtraCodecs.POSITIVE_INT).build()
    );

    public static final DataComponentType<Integer> IMBUE_LEVEL = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "imbue_level"),
            DataComponentType.<Integer>builder().persistent(ExtraCodecs.POSITIVE_INT).build()
    );

    public static final DataComponentType<Integer> HIDE_LEVEL = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "hide_level"),
            DataComponentType.<Integer>builder().persistent(ExtraCodecs.POSITIVE_INT).build()
    );

    // Called in the mod initializer / constructor in order to make sure that items are registered
    public static void load() {}
}
