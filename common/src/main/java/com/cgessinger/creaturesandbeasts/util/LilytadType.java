package com.cgessinger.creaturesandbeasts.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class LilytadType {
    private static final Map<ResourceLocation, LilytadType> LILYTAD_TYPES = new LinkedHashMap<>();

    private ResourceLocation id;
    private ResourceLocation texture;
    private Supplier<Item> shearItem;

    public LilytadType(@Nullable Item shearItem, ResourceLocation id, ResourceLocation texture) {
        this(() -> shearItem, id, texture);
    }

    public LilytadType(@Nullable Supplier<Item> shearItem, ResourceLocation id, ResourceLocation texture) {
        this.id = id;
        this.texture = texture;
        this.shearItem = shearItem;
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


    public ResourceLocation getId() {
        return id;
    }

    public void setId(ResourceLocation id) {
        this.id = id;
    }

    public ResourceLocation getTextureLocation() {
        return this.texture;
    }

    public void setTextureLocation(ResourceLocation textureLocation) {
        this.texture = textureLocation;
    }

    public static LilytadType registerLilytadType(LilytadType minipadType) {
        ResourceLocation id = minipadType.getId();
        if (LILYTAD_TYPES.containsKey(id)) {
            throw new IllegalStateException(String.format("%s already exists in the LilytadType registry.", id.toString()));
        }
        LILYTAD_TYPES.put(id, minipadType);
        return minipadType;
    }

    @Nullable
    public static LilytadType getById(@Nullable String id) {
        if (id == null) {
            return null;
        } else {
            return getById(ResourceLocation.tryParse(id));
        }
    }

    @Nullable
    public static LilytadType getById(@Nullable ResourceLocation id) {
        return LILYTAD_TYPES.get(id);
    }

    public boolean equals(Object obj) {
        if (obj instanceof LilytadType) {
            final LilytadType type = (LilytadType) obj;
            return type.getId().equals(this.getId()) &&
                    type.getTextureLocation().equals(this.getTextureLocation());
        } else {
            return false;
        }
    }
}
