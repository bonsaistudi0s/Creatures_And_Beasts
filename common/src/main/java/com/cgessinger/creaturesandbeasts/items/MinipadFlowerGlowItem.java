package com.cgessinger.creaturesandbeasts.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class MinipadFlowerGlowItem extends Item {
    public MinipadFlowerGlowItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
