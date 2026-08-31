package com.cgessinger.creaturesandbeasts.modules;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeastsConstants;
import com.cgessinger.creaturesandbeasts.mixin.accessor.SimpleParticleTypeAccessor;
import com.helliongames.hellionsapi.registration.holders.ParticleDataHolder;
import com.helliongames.hellionsapi.registration.registries.HellionsAPIParticleRegistry;
import net.minecraft.core.particles.SimpleParticleType;

public class CNBParticleTypeModule {

    public static final HellionsAPIParticleRegistry PARTICLE_MODULE = new HellionsAPIParticleRegistry(CreaturesAndBeastsConstants.MOD_ID);

    public static final ParticleDataHolder<SimpleParticleType> PINK_MINIPAD_FLOWER = PARTICLE_MODULE.register("pink_minipad_flower", ParticleDataHolder.of(() -> SimpleParticleTypeAccessor.createSimpleParticleType(false)));
    public static final ParticleDataHolder<SimpleParticleType> LIGHT_PINK_MINIPAD_FLOWER = PARTICLE_MODULE.register("light_pink_minipad_flower", ParticleDataHolder.of(() -> SimpleParticleTypeAccessor.createSimpleParticleType(false)));
    public static final ParticleDataHolder<SimpleParticleType> YELLOW_MINIPAD_FLOWER = PARTICLE_MODULE.register("yellow_minipad_flower", ParticleDataHolder.of(() -> SimpleParticleTypeAccessor.createSimpleParticleType(false)));
    public static final ParticleDataHolder<SimpleParticleType> CACTEM_HEAL_PARTICLE = PARTICLE_MODULE.register("heal", ParticleDataHolder.of(() -> SimpleParticleTypeAccessor.createSimpleParticleType(false)));

    // Called in the mod initializer / constructor in order to make sure that items are registered
    public static void load() {}
}
