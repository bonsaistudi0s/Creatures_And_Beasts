package com.cgessinger.creaturesandbeasts.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class LizardType {
    private static final Map<ResourceLocation, LizardType> LIZARD_TYPES = new LinkedHashMap<>();

    private ResourceLocation id;
    private Pair<ResourceLocation, ResourceLocation> textures;
    private Supplier<Item> spawnItem;

    public LizardType(@Nullable Item spawnItem, ResourceLocation id, ResourceLocation texture, ResourceLocation sadTexture) {
        this(() -> spawnItem, id, texture, sadTexture);
    }

    public LizardType(@Nullable Supplier<Item> spawnItem, ResourceLocation id, ResourceLocation texture, ResourceLocation sadTexture) {
        this(spawnItem, id, Pair.of(texture, sadTexture));
    }

    public LizardType(@Nullable Supplier<Item> spawnItem, ResourceLocation id, Pair<ResourceLocation, ResourceLocation> textures) {
        this.id = id;
        this.textures = textures;
        this.spawnItem = spawnItem;
    }

    @CheckForNull
    public Item getSpawnItem() {
        final Item item = this.spawnItem.get();
        if (item == null || item.equals(Items.AIR)) {
            return null;
        } else {
            return item;
        }
    }

    public void setSpawnItem(@Nullable Item spawnItem) {
        this.spawnItem = () -> spawnItem;
    }


    public ResourceLocation getId() {
        return id;
    }

    public void setId(ResourceLocation id) {
        this.id = id;
    }

    public ResourceLocation getTextureLocation() {
        return this.textures.getLeft();
    }

    public void setTextureLocation(ResourceLocation textureLocation) {
        this.textures = Pair.of(textureLocation, this.textures.getRight());
    }

    public ResourceLocation getSadTextureLocation() {
        return this.textures.getRight();
    }

    public void setSadTextureLocation(ResourceLocation textureLocation) {
        this.textures = Pair.of(this.textures.getLeft(), textureLocation);
    }

    public static LizardType registerLizardType(LizardType lizardType) {
        ResourceLocation id = lizardType.getId();
        if (LIZARD_TYPES.containsKey(id)) {
            throw new IllegalStateException(String.format("%s already exists in the LizardType registry.", id.toString()));
        }
        LIZARD_TYPES.put(id, lizardType);
        return lizardType;
    }

    @Nullable
    public static LizardType getById(@Nullable String id) {
        if (id == null) {
            return null;
        } else {
            return getById(ResourceLocation.tryParse(id));
        }
    }

    @Nullable
    public static LizardType getById(@Nullable ResourceLocation id) {
        return LIZARD_TYPES.get(id);
    }

    public boolean equals(Object obj) {
        if (obj instanceof LizardType) {
            final LizardType type = (LizardType) obj;
            return type.getId().equals(this.getId()) &&
                    type.getTextureLocation().equals(this.getTextureLocation());
        } else {
            return false;
        }
    }
}
