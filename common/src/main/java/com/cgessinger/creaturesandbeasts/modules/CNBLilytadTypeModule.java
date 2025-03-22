package com.cgessinger.creaturesandbeasts.modules;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeastsConstants;
import com.cgessinger.creaturesandbeasts.util.LilytadType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CNBLilytadTypeModule {
    // Make sure to change the initial size of this ArrayList when adding new Lizard variants
    private static final List<LilytadType> LILYTAD_TYPES = new ArrayList<>(3);

    public static final LilytadType LIGHT_PINK = registerWithCNBDirectory(CreaturesAndBeastsConstants.MOD_ID, "light_pink");
    public static final LilytadType PINK = registerWithCNBDirectory(CreaturesAndBeastsConstants.MOD_ID, "pink");
    public static final LilytadType YELLOW = registerWithCNBDirectory(CreaturesAndBeastsConstants.MOD_ID, "yellow");


    private static LilytadType registerWithCNBDirectory(String namespace, String name) {
        return registerWithCNBDirectory(() -> BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(namespace, name + "_waterlily")), namespace, name);
    }

    private static LilytadType registerWithCNBDirectory(@Nullable Item shearItem, String namespace, String name) {
        return registerWithCNBDirectory(() -> shearItem, namespace, name);
    }

    private static LilytadType registerWithCNBDirectory(@Nullable Supplier<Item> shearItem, String namespace, String name) {
        return register(new LilytadType(shearItem, ResourceLocation.fromNamespaceAndPath(namespace, name), ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "textures/entity/lilytad/lilytad_" + name + ".png")));

    }

    private static LilytadType register(LilytadType lilytadType) {
        LILYTAD_TYPES.add(lilytadType);
        return lilytadType;
    }

    public static void registerAll() {
        for (LilytadType lilytadType : LILYTAD_TYPES) {
            LilytadType.registerLilytadType(lilytadType);
        }
    }
}
