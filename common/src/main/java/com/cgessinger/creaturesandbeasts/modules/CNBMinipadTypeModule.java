package com.cgessinger.creaturesandbeasts.modules;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeastsConstants;
import com.cgessinger.creaturesandbeasts.util.MinipadType;
import com.helliongames.hellionsapi.registration.holders.ParticleDataHolder;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CNBMinipadTypeModule {
    // Make sure to change the initial size of this ArrayList when adding new Lizard variants
    private static final List<MinipadType> MINIPAD_TYPES = new ArrayList<>(3);

    public static final MinipadType LIGHT_PINK = registerWithCNBDirectory(CreaturesAndBeastsConstants.MOD_ID, "light_pink", CNBParticleTypeModule.LIGHT_PINK_MINIPAD_FLOWER);
    public static final MinipadType PINK = registerWithCNBDirectory(CreaturesAndBeastsConstants.MOD_ID, "pink", CNBParticleTypeModule.PINK_MINIPAD_FLOWER);
    public static final MinipadType YELLOW = registerWithCNBDirectory(CreaturesAndBeastsConstants.MOD_ID, "yellow", CNBParticleTypeModule.YELLOW_MINIPAD_FLOWER);

    private static MinipadType registerWithCNBDirectory(String namespace, String name, ParticleDataHolder<SimpleParticleType> particle) {
        return registerWithCNBDirectory(() -> BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(namespace, name + "_minipad_flower")), () -> BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(namespace, name + "_minipad_flower_glow")), namespace, name, particle);
    }

    private static MinipadType registerWithCNBDirectory(@Nullable Item shearItem, @Nullable Item glowShearItem, String namespace, String name, ParticleDataHolder<SimpleParticleType> particle) {
        return registerWithCNBDirectory(() -> shearItem, () -> glowShearItem, namespace, name, particle);
    }

    private static MinipadType registerWithCNBDirectory(@Nullable Supplier<Item> shearItem, @Nullable Supplier<Item> glowShearItem, String namespace, String name, ParticleDataHolder<SimpleParticleType> particle) {
        return register(new MinipadType(shearItem, glowShearItem, ResourceLocation.fromNamespaceAndPath(namespace, name), Pair.of(ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "textures/entity/minipad/minipad_" + name + ".png"), ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "textures/entity/minipad/minipad_" + name + "_glow.png")), particle));
    }

    private static MinipadType register(MinipadType minipadType) {
        MINIPAD_TYPES.add(minipadType);
        return minipadType;
    }

    public static void registerAll() {
        for (MinipadType minipadType : MINIPAD_TYPES) {
            MinipadType.registerMinipadType(minipadType);
        }
    }
}
