package com.cgessinger.creaturesandbeasts.util;

import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;

public class SporelingType {
    private static final Map<ResourceLocation, SporelingType> SPORELING_TYPES = new LinkedHashMap<>();

    private ResourceLocation id;
    private Pair<ResourceLocation, ResourceLocation> modelAndTexture;
    private SporelingHostility hostility;

    public SporelingType(ResourceLocation id, ResourceLocation model, ResourceLocation texture, SporelingHostility hostility) {
        this(id, Pair.of(model, texture), hostility);
    }

    public SporelingType(ResourceLocation id, Pair<ResourceLocation, ResourceLocation> modelAndTexture, SporelingHostility hostility) {
        this.id = id;
        this.modelAndTexture = modelAndTexture;
        this.hostility = hostility;
    }

    public ResourceLocation getId() {
        return id;
    }

    public void setId(ResourceLocation id) {
        this.id = id;
    }

    public ResourceLocation getTextureLocation() {
        return this.modelAndTexture.getRight();
    }

    public void setTextureLocation(ResourceLocation textureLocation) {
        this.modelAndTexture = Pair.of(this.modelAndTexture.getLeft(), textureLocation);
    }

    public ResourceLocation getModelLocation() {
        return this.modelAndTexture.getLeft();
    }

    public void setModelLocation(ResourceLocation modelLocation) {
        this.modelAndTexture = Pair.of(modelLocation, this.modelAndTexture.getRight());
    }

    public SporelingHostility getHostility() {
        return hostility;
    }

    public void setHostility(SporelingHostility hostility) {
        this.hostility = hostility;
    }

    public static SporelingType registerSporelingType(SporelingType sporelingType) {
        ResourceLocation id = sporelingType.getId();
        if (SPORELING_TYPES.containsKey(id)) {
            throw new IllegalStateException(String.format("%s already exists in the SporelingType registry.", id.toString()));
        }
        SPORELING_TYPES.put(id, sporelingType);
        return sporelingType;
    }

    @Nullable
    public static SporelingType getById(@Nullable String id) {
        if (id == null) {
            return null;
        } else {
            return getById(ResourceLocation.tryParse(id));
        }
    }

    @Nullable
    public static SporelingType getById(@Nullable ResourceLocation id) {
        return SPORELING_TYPES.get(id);
    }

    public boolean equals(Object obj) {
        if (obj instanceof SporelingType) {
            final SporelingType type = (SporelingType) obj;
            return type.getId().equals(this.getId()) &&
                    type.getModelLocation().equals(this.getModelLocation()) &&
                    type.getTextureLocation().equals(this.getTextureLocation());
        } else {
            return false;
        }
    }

    public enum SporelingHostility {
        HOSTILE,
        NEUTRAL,
        FRIENDLY;
    }
}
