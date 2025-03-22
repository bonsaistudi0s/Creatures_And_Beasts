package com.cgessinger.creaturesandbeasts.modules;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeastsConstants;
import com.helliongames.hellionsapi.registration.holders.SoundDataHolder;
import com.helliongames.hellionsapi.registration.registries.HellionsAPISoundRegistry;

public class CNBSoundModule {
    public static final HellionsAPISoundRegistry SOUND_MODULE = new HellionsAPISoundRegistry(CreaturesAndBeastsConstants.MOD_ID);

    public static final SoundDataHolder LITTLE_GREBE_AMBIENT = SOUND_MODULE.register("entity.little_grebe.ambient", SoundDataHolder.of());
    public static final SoundDataHolder LITTLE_GREBE_HURT = SOUND_MODULE.register("entity.little_grebe.hurt", SoundDataHolder.of());
    public static final SoundDataHolder LITTLE_GREBE_CHICK_AMBIENT = SOUND_MODULE.register("entity.little_grebe_chick.ambient", SoundDataHolder.of());

    public static final SoundDataHolder CINDERSHELL_AMBIENT = SOUND_MODULE.register("entity.cindershell.ambient", SoundDataHolder.of());
    public static final SoundDataHolder CINDERSHELL_HURT = SOUND_MODULE.register("entity.cindershell.hurt", SoundDataHolder.of());
    public static final SoundDataHolder CINDERSHELL_ADULT_EAT = SOUND_MODULE.register("entity.cindershell_adult.eat", SoundDataHolder.of());
    public static final SoundDataHolder CINDERSHELL_BABY_EAT = SOUND_MODULE.register("entity.cindershell_baby.eat", SoundDataHolder.of());

    public static final SoundDataHolder SPORELING_OVERWORLD_AMBIENT = SOUND_MODULE.register("entity.sporeling_overworld.ambient", SoundDataHolder.of());
    public static final SoundDataHolder SPORELING_OVERWORLD_HURT = SOUND_MODULE.register("entity.sporeling_overworld.hurt", SoundDataHolder.of());
    public static final SoundDataHolder SPORELING_NETHER_AMBIENT = SOUND_MODULE.register("entity.sporeling_nether.ambient", SoundDataHolder.of());
    public static final SoundDataHolder SPORELING_NETHER_HURT = SOUND_MODULE.register("entity.sporeling_nether.hurt", SoundDataHolder.of());
    public static final SoundDataHolder SPORELING_WARPED_AMBIENT = SOUND_MODULE.register("entity.sporeling_warped.ambient", SoundDataHolder.of());
    public static final SoundDataHolder SPORELING_WARPED_HURT = SOUND_MODULE.register("entity.sporeling_warped.hurt", SoundDataHolder.of());
    public static final SoundDataHolder SPORELING_BITE = SOUND_MODULE.register("entity.sporeling.bite", SoundDataHolder.of());

    public static final SoundDataHolder LILYTAD_AMBIENT = SOUND_MODULE.register("entity.lilytad.ambient", SoundDataHolder.of());
    public static final SoundDataHolder LILYTAD_HURT = SOUND_MODULE.register("entity.lilytad.hurt", SoundDataHolder.of());
    public static final SoundDataHolder LILYTAD_DEATH = SOUND_MODULE.register("entity.lilytad.death", SoundDataHolder.of());

    public static final SoundDataHolder YETI_AMBIENT = SOUND_MODULE.register("entity.yeti.ambient", SoundDataHolder.of());
    public static final SoundDataHolder YETI_HURT = SOUND_MODULE.register("entity.yeti.hurt", SoundDataHolder.of());
    public static final SoundDataHolder YETI_STEP = SOUND_MODULE.register("entity.yeti.step", SoundDataHolder.of());
    public static final SoundDataHolder YETI_HIT = SOUND_MODULE.register("entity.yeti.hit", SoundDataHolder.of());
    public static final SoundDataHolder YETI_ADULT_EAT = SOUND_MODULE.register("entity.yeti_adult.eat", SoundDataHolder.of());
    public static final SoundDataHolder YETI_BABY_EAT = SOUND_MODULE.register("entity.yeti_baby.eat", SoundDataHolder.of());

    public static final SoundDataHolder MINIPAD_HURT = SOUND_MODULE.register("entity.minipad.hurt", SoundDataHolder.of());
    public static final SoundDataHolder MINIPAD_STEP = SOUND_MODULE.register("entity.minipad.step", SoundDataHolder.of());
    public static final SoundDataHolder MINIPAD_SWIM = SOUND_MODULE.register("entity.minipad.swim", SoundDataHolder.of());

    public static final SoundDataHolder END_WHALE_AMBIENT = SOUND_MODULE.register("entity.end_whale.ambient", SoundDataHolder.of());

    public static final SoundDataHolder CACTEM_AMBIENT = SOUND_MODULE.register("entity.cactem.ambient", SoundDataHolder.of());
    public static final SoundDataHolder CACTEM_HURT = SOUND_MODULE.register("entity.cactem.hurt", SoundDataHolder.of());
    public static final SoundDataHolder CACTEM_HEAL = SOUND_MODULE.register("entity.cactem.heal", SoundDataHolder.of());

    public static final SoundDataHolder PLAYER_HEAL = SOUND_MODULE.register("item.heal_spell_book.player_heal", SoundDataHolder.of());

    public static final SoundDataHolder SPEAR_THROW = SOUND_MODULE.register("item.cactem_spear.throw", SoundDataHolder.of());

    public static final SoundDataHolder LIZARD_EGG_HATCH = SOUND_MODULE.register("entity.lizard.egg_hatch", SoundDataHolder.of());

    // Called in the mod initializer / constructor in order to make sure that items are registered
    public static void load() {}
}
