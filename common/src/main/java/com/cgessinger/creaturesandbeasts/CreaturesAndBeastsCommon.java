package com.cgessinger.creaturesandbeasts;

import com.cgessinger.creaturesandbeasts.config.CNBConfig;
import com.cgessinger.creaturesandbeasts.events.CNBEvents;
import com.cgessinger.creaturesandbeasts.modules.*;
import com.cgessinger.creaturesandbeasts.world.gen.ModEntitySpawns;
import net.minecraft.world.item.CreativeModeTab;

import java.io.IOException;

public class CreaturesAndBeastsCommon {
    public static final CreativeModeTab TAB = new CreativeModeTab("cnb_tab");

    public static void init() {
        CNBBlockModule.load();
        CNBItemModule.load();
        CNBEntityModule.load();
        CNBSoundModule.load();
        CNBParticleTypeModule.load();
        CNBMenuModule.load();
        CNBBiomeModifierModule.load();
        CNBLootModifierModule.load();
        CNBDataComponentTypeModule.load();

        CNBSporelingTypeModule.registerAll();
        CNBLizardTypeModule.registerAll();
        CNBLilytadTypeModule.registerAll();
        CNBMinipadTypeModule.registerAll();

        ModEntitySpawns.entitySpawnPlacementRegistry();
    }

    public CreaturesAndBeastsCommon() {
        MinecraftForge.EVENT_BUS.register(new CNBEvents());

        try {
            CNBConfig.CONFIG = Config
                    .builder(FMLPaths.CONFIGDIR.get().resolve("creaturesandbeasts-common.toml"))
                    .loadClass(CNBConfig.class)
                    .build();
        } catch (IllegalStateException | IllegalArgumentException | IOException | ParsingException e) {
            throw new RuntimeException(
                    "Failed to load Creatures and Beasts config" +
                            (e instanceof ParsingException ? ", try fixing/deleting your config file" : ""), e);
        }

        CNBConfig.CONFIG.onReload(stage -> {
            if (stage == Config.ReloadStage.PRE) {
                CreaturesAndBeastsConstants.LOGGER.debug("Reloading Creatures and Beasts config");
            }
        });
    }
}
