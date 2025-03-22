package com.cgessinger.creaturesandbeasts.modules;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeastsConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class CNBItemTagModule {
    public static class Items {
        public static final TagKey<Item> MINIPAD_FLOWERS = tag("minipad_flowers");
        public static final TagKey<Item> GLOWING_MINIPAD_FLOWERS = tag("glowing_minipad_flowers");
        public static final TagKey<Item> CINDERSHELL_FOOD = tag("cindershell_food");
        public static final TagKey<Item> END_WHALE_FOOD = tag("end_whale_food");
        public static final TagKey<Item> LITTLE_GREBE_FOOD = tag("little_grebe_food");
        public static final TagKey<Item> SPORELING_FOOD = tag("sporeling_food");

        private static TagKey<Item> tag(String name) {
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, name));
        }
    }

}
