package com.cgessinger.creaturesandbeasts.config;

import org.infernalstudios.config.Config;
import org.infernalstudios.config.annotation.Configurable;
import org.infernalstudios.config.annotation.DoubleRange;
import org.infernalstudios.config.annotation.IntegerRange;

public class CNBConfig {
    public static Config CONFIG;

    @Configurable(description = "Determines how many yeti hides can be used to reinforce an item.", category = "General")
    @IntegerRange(min = 0)
    public static int hideAmount = 5;

    @Configurable(description = "Determines the experience cost of applying yeti hide to an item.", category = "General")
    @IntegerRange(min = 0)
    public static int hideCost = 1;

    @Configurable(description = "Determines the multiplier used to add armor per yeti hide on an item.", category = "General")
    @DoubleRange(min = 0)
    public static double hideMultiplier = 0.01D;

}
