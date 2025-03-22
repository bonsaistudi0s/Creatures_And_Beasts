package com.cgessinger.creaturesandbeasts.modules;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeastsConstants;
import com.cgessinger.creaturesandbeasts.blocks.CinderFurnaceBlock;
import com.cgessinger.creaturesandbeasts.blocks.LizardEggBlock;
import com.helliongames.hellionsapi.registration.holders.BlockDataHolder;
import com.helliongames.hellionsapi.registration.registries.HellionsAPIBlockRegistry;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class CNBBlockModule {
    public static final HellionsAPIBlockRegistry BLOCKS = new HellionsAPIBlockRegistry(CreaturesAndBeastsConstants.MOD_ID);

    public static final BlockDataHolder<?> PINK_WATERLILY_BLOCK = BLOCKS.register("pink_waterlily_block",
            BlockDataHolder.of(() -> new FlowerBlock(MobEffects.HEAL, 5.0F, BlockBehaviour.Properties.of().noCollission().instabreak().sound(SoundType.GRASS))));

    public static final BlockDataHolder<?> POTTED_PINK_WATERLILY = BLOCKS.register("potted_pink_waterlily",
            BlockDataHolder.of(() -> new FlowerPotBlock(PINK_WATERLILY_BLOCK.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT))));

    public static final BlockDataHolder<?> LIGHT_PINK_WATERLILY_BLOCK = BLOCKS.register("light_pink_waterlily_block",
            BlockDataHolder.of(() -> new FlowerBlock(MobEffects.HEAL, 5, BlockBehaviour.Properties.of().noCollission().instabreak().sound(SoundType.GRASS))));

    public static final BlockDataHolder<?> POTTED_LIGHT_PINK_WATERLILY = BLOCKS.register("potted_light_pink_waterlily",
            BlockDataHolder.of(() -> new FlowerPotBlock(LIGHT_PINK_WATERLILY_BLOCK.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT))));

    public static final BlockDataHolder<?> YELLOW_WATERLILY_BLOCK = BLOCKS.register("yellow_waterlily_block",
            BlockDataHolder.of(() -> new FlowerBlock(MobEffects.HEAL, 5, BlockBehaviour.Properties.of().noCollission().instabreak().sound(SoundType.GRASS))));

    public static final BlockDataHolder<?> POTTED_YELLOW_WATERLILY = BLOCKS.register("potted_yellow_waterlily",
            BlockDataHolder.of(() -> new FlowerPotBlock(YELLOW_WATERLILY_BLOCK.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT))));

    public static final BlockDataHolder<?> CINDER_FURNACE = BLOCKS.register("cinder_furnace",
            BlockDataHolder.of(() -> new CinderFurnaceBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.5F))));

    public static BlockDataHolder<?> LIZARD_EGGS = BLOCKS.register("lizard_egg_block",
            BlockDataHolder.of(LizardEggBlock::new));

    // Called in the mod initializer / constructor in order to make sure that items are registered
    public static void load() {}
}
