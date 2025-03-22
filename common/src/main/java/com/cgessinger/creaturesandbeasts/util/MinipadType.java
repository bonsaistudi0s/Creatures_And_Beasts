package com.cgessinger.creaturesandbeasts.util;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MinipadType {
    private static final Map<ResourceLocation, MinipadType> MINIPAD_TYPES = new LinkedHashMap<>();

    private ResourceLocation id;
    private Pair<ResourceLocation, ResourceLocation> textures;
    private Supplier<Item> shearItem;
    private Supplier<Item> glowShearItem;
    private Supplier<SimpleParticleType> particle;

    public MinipadType(@Nullable Item shearItem, @Nullable Item glowShearItem, ResourceLocation id, ResourceLocation texture, ResourceLocation glowTexture, Supplier<SimpleParticleType> particle) {
        this(shearItem, glowShearItem, id, Pair.of(texture, glowTexture), particle);
    }

    public MinipadType(@Nullable Supplier<Item> shearItem, @Nullable Supplier<Item> glowShearItem, ResourceLocation id, ResourceLocation texture, ResourceLocation glowTexture, Supplier<SimpleParticleType> particle) {
        this(shearItem, glowShearItem, id, Pair.of(texture, glowTexture), particle);
    }

    public MinipadType(@Nullable Item shearItem, @Nullable Item glowShearItem, ResourceLocation id, Pair<ResourceLocation, ResourceLocation> textures, Supplier<SimpleParticleType> particle) {
        this(() -> shearItem, () -> glowShearItem, id, textures, particle);
    }

    public MinipadType(@Nullable Supplier<Item> shearItem, @Nullable Supplier<Item> glowShearItem, ResourceLocation id, Pair<ResourceLocation, ResourceLocation> textures, Supplier<SimpleParticleType> particle) {
        this.id = id;
        this.textures = textures;
        this.shearItem = shearItem;
        this.glowShearItem = glowShearItem;
        this.particle = particle;
    }

    @CheckForNull
    public Item getShearItem() {
        final Item item = this.shearItem.get();
        if (item == null || item.equals(Items.AIR)) {
            return null;
        } else {
            return item;
        }
    }

    public void setShearItem(@Nullable Item shearItem) {
        this.shearItem = () -> shearItem;
    }

    @CheckForNull
    public Item getGlowShearItem() {
        final Item item = this.glowShearItem.get();
        if (item == null || item.equals(Items.AIR)) {
            return null;
        } else {
            return item;
        }
    }

    public void setGlowShearItem(@Nullable Item glowShearItem) {
        this.glowShearItem = () -> glowShearItem;
    }

    public ResourceLocation getId() {
        return id;
    }

    public void setId(ResourceLocation id) {
        this.id = id;
    }

    public ResourceLocation getTextureLocation() {
        return this.textures.getFirst();
    }

    public void setTextureLocation(ResourceLocation textureLocation) {
        this.textures = Pair.of(textureLocation, this.textures.getSecond());
    }

    public ResourceLocation getGlowTextureLocation() {
        return this.textures.getSecond();
    }

    public void setParticle(SimpleParticleType particle) {
        this.particle = () -> particle;
    }

    @CheckForNull
    public SimpleParticleType getParticle() {
        return this.particle.get();
    }

    public void setGlowTextureLocation(ResourceLocation glowTextureLocation) {
        this.textures = Pair.of(this.textures.getFirst(), glowTextureLocation);
    }

    public static MinipadType registerMinipadType(MinipadType minipadType) {
        ResourceLocation id = minipadType.getId();
        if (MINIPAD_TYPES.containsKey(id)) {
            throw new IllegalStateException(String.format("%s already exists in the MinipadType registry.", id.toString()));
        }
        MINIPAD_TYPES.put(id, minipadType);
        return minipadType;
    }

    @Nullable
    public static MinipadType getById(@Nullable String id) {
        if (id == null) {
            return null;
        } else {
            return getById(ResourceLocation.tryParse(id));
        }
    }

    @Nullable
    public static MinipadType getById(@Nullable ResourceLocation id) {
        return MINIPAD_TYPES.get(id);
    }

    public boolean equals(Object obj) {
        if (obj instanceof MinipadType) {
            final MinipadType type = (MinipadType) obj;
            return type.getId().equals(this.getId()) &&
                    type.getTextureLocation().equals(this.getTextureLocation());
        } else {
            return false;
        }
    }
}
