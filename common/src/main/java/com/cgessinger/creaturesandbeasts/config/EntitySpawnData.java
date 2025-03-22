package com.cgessinger.creaturesandbeasts.config;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.toml.TomlFormat;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.HashMap;

public record EntitySpawnData(ResourceLocation entityType, ResourceLocation biome, MobCategory spawnCategory, int spawnWeight, int minCount, int maxCount, double mobCost, double energyBudget) {

    public ResourceLocation getEntityTypeLocation() {
        return entityType;
    }

    @Nullable
    public EntityType<? extends Entity> getEntityType() {
        return ForgeRegistries.ENTITY_TYPES.getValue(this.getEntityTypeLocation());
    }

    public ResourceLocation getBiomeLocation() {
        return biome;
    }

    @Nullable
    public ResourceKey<Biome> getBiome() {
        return ResourceKey.create(Registry.BIOME_REGISTRY, biome);
    }

    public String getCategoryName() {
        return spawnCategory.getName();
    }

    @Nullable
    public MobCategory getCategory() {
        return spawnCategory;
    }

    public int getMinCount() {
        return minCount;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public int getSpawnWeight() {
        return spawnWeight;
    }

    public double getMobCost() {
        return mobCost;
    }

    public double getEnergyBudget() {
        return energyBudget;
    }

    public Config toConfig() {
        CommentedConfig config = CommentedConfig.of(() -> new HashMap<>(6), TomlFormat.instance());

        config.set("entityType", this.getEntityTypeLocation().toString());
        config.set("biome", this.getBiomeLocation().toString());
        config.set("spawnCategory", this.getCategoryName());
        config.set("spawnWeight", this.getSpawnWeight());
        config.set("minCount", this.getMinCount());
        config.set("maxCount", this.getMaxCount());
        config.set("mobCost", this.getMobCost());
        config.set("energyBudget", this.getEnergyBudget());

        config.setComment("entityType", " Determines what entity this spawn is applied to.");
        config.setComment("biome", " Determines what biome this entity should spawn in.");
        config.setComment("spawnCategory", "Determines what spawn category this entity should use.");
        config.setComment("spawnWeight", "Determines the spawn weight of the entity.\n Range: [0, 1000000]");
        config.setComment("minCount", "Determines the minimum number of entities to spawn.\n Range: [0, 1000000]");
        config.setComment("maxCount", "Determines the maximum number of entities to spawn.\n Range: [0, 1000000]");
        config.setComment("mobCost", "Determines the spawn cost for the entity.");
        config.setComment("energyBudget", "Determines the maximum cost for the entity in a given area.");

        return config;
    }

    @Override
    public String toString() {
        return "SpawnData [entityType=" + entityType + ", biome=" + biome + ", spawnCategory=" + spawnCategory + ", spawnWeight=" + spawnWeight + ", minCount=" + minCount + ", maxCount=" + maxCount + "]";
    }

    public static EntitySpawnData fromConfig(Config config) {
        double mobCost = 0;
        double energyBudget = 0;
        if (config.get("mobCost") instanceof Number number) {
            mobCost = number.doubleValue();
        }
        if (config.get("energyBudget") instanceof Number number) {
            energyBudget = number.doubleValue();
        }


        return new EntitySpawnData(
                ResourceLocation.tryParse(config.get("entityType")),
                ResourceLocation.tryParse(config.get("biome")),
                config.getEnum("spawnCategory", MobCategory.class),
                config.getInt("spawnWeight"),
                config.getInt("minCount"),
                config.getInt("maxCount"),
                mobCost,
                energyBudget);
    }

    public static EntitySpawnData of(EntityType<? extends Entity> entityType, ResourceKey<Biome> biome, MobCategory category, int spawnWeight, int minCount, int maxCount, double mobCost, double energyBudget) {
        return new EntitySpawnData(ForgeRegistries.ENTITY_TYPES.getKey(entityType), biome.location(), category, spawnWeight, minCount, maxCount, mobCost, energyBudget);
    }

    public static EntitySpawnData of(ResourceLocation entityType, ResourceKey<Biome> biome, MobCategory category, int spawnWeight, int minCount, int maxCount, double mobCost, double energyBudget) {
        return new EntitySpawnData(entityType, biome.location(), category, spawnWeight, minCount, maxCount, mobCost, energyBudget);
    }

    public static EntitySpawnData of(ResourceLocation entityType, ResourceLocation biome, MobCategory category, int spawnWeight, int minCount, int maxCount, double mobCost, double energyBudget) {
        return new EntitySpawnData(entityType, biome, category, spawnWeight, minCount, maxCount, mobCost, energyBudget);
    }
}
