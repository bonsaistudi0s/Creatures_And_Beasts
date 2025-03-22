package com.cgessinger.creaturesandbeasts.modules;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeastsConstants;
import com.cgessinger.creaturesandbeasts.entities.*;
import com.helliongames.hellionsapi.registration.holders.EntityTypeDataHolder;
import com.helliongames.hellionsapi.registration.registries.HellionsAPIEntityRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class CNBEntityModule {
    public static final HellionsAPIEntityRegistry ENTITY_TYPES = new HellionsAPIEntityRegistry(CreaturesAndBeastsConstants.MOD_ID);

    /* CREATURES */
    public static final EntityTypeDataHolder<LittleGrebeEntity> LITTLE_GREBE = ENTITY_TYPES.register("little_grebe",
            EntityTypeDataHolder.of(() -> EntityType.Builder.of(LittleGrebeEntity::new, MobCategory.CREATURE).sized(0.5f, 0.6f).passengerAttachments(0.18f).build(ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "little_grebe").toString()))
                    .attributes(LittleGrebeEntity::createAttributes));

    public static final EntityTypeDataHolder<LizardEntity> LIZARD = ENTITY_TYPES.register("lizard",
            EntityTypeDataHolder.of(() -> EntityType.Builder.of(LizardEntity::new, MobCategory.CREATURE).sized(0.52f, 0.3f).build(ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "lizard").toString()))
                    .attributes(LizardEntity::createAttributes));

    public static final EntityTypeDataHolder<LilytadEntity> LILYTAD = ENTITY_TYPES.register("lilytad",
            EntityTypeDataHolder.of(() -> EntityType.Builder.of(LilytadEntity::new, MobCategory.CREATURE).sized(0.7f, 1.02f).build(ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "lilytad").toString()))
                    .attributes(LilytadEntity::createAttributes));

    public static final EntityTypeDataHolder<SporelingEntity> SPORELING = ENTITY_TYPES.register("sporeling",
            EntityTypeDataHolder.of(() -> EntityType.Builder.of(SporelingEntity::new, MobCategory.CREATURE).sized(0.6f, 0.85f).build(ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "sporeling").toString()))
                    .attributes(SporelingEntity::createAttributes));

    public static final EntityTypeDataHolder<MinipadEntity> MINIPAD = ENTITY_TYPES.register("minipad",
            EntityTypeDataHolder.of(() -> EntityType.Builder.of(MinipadEntity::new, MobCategory.CREATURE).sized(0.6f, 0.7f).build(ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "minipad").toString()))
                    .attributes(MinipadEntity::createAttributes));

    public static final EntityTypeDataHolder<EndWhaleEntity> END_WHALE = ENTITY_TYPES.register("end_whale",
            EntityTypeDataHolder.of(() -> EntityType.Builder.of(EndWhaleEntity::new, MobCategory.CREATURE).sized(3.0f, 1.5f).passengerAttachments(1.05f).build(ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "end_whale").toString()))
                    .attributes(EndWhaleEntity::createAttributes));

    public static final EntityTypeDataHolder<CactemEntity> CACTEM = ENTITY_TYPES.register("cactem",
            EntityTypeDataHolder.of(() -> EntityType.Builder.of(CactemEntity::new, MobCategory.CREATURE).sized(0.75F, 1.0F).eyeHeight(0.5f).build(ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "cactem").toString()))
                    .attributes(CactemEntity::createAttributes));

    public static final EntityTypeDataHolder<YetiEntity> YETI = ENTITY_TYPES.register("yeti",
            EntityTypeDataHolder.of(() -> EntityType.Builder.of(YetiEntity::new, MobCategory.CREATURE).sized(1.55f, 2.05f).build(ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "yeti").toString()))
                    .attributes(YetiEntity::createAttributes));

    public static final EntityTypeDataHolder<CindershellEntity> CINDERSHELL = ENTITY_TYPES.register("cindershell",
            EntityTypeDataHolder.of(() -> EntityType.Builder.of(CindershellEntity::new, MobCategory.CREATURE).sized(1.25f, 1.45f).eyeHeight(0.29f).fireImmune().build(ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "cindershell").toString()))
                    .attributes(CindershellEntity::createAttributes));

    /* PROJECTILES */
    public static final EntityTypeDataHolder<LizardEggEntity> LIZARD_EGG = ENTITY_TYPES.register("lizard_egg",
            EntityTypeDataHolder.of(() -> EntityType.Builder.<LizardEggEntity>of(LizardEggEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4).updateInterval(10).build(ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "lizard_egg").toString())));

    public static final EntityTypeDataHolder<ThrownCactemSpearEntity> THROWN_CACTEM_SPEAR = ENTITY_TYPES.register("thrown_cactem_spear",
            EntityTypeDataHolder.of(() -> EntityType.Builder.<ThrownCactemSpearEntity>of(ThrownCactemSpearEntity::new, MobCategory.MISC).sized(0.4F, 0.4F).clientTrackingRange(4).updateInterval(10).build(ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "cactem_spear").toString())));

    // Called in the mod initializer / constructor in order to make sure that items are registered
    public static void load() {}
}
