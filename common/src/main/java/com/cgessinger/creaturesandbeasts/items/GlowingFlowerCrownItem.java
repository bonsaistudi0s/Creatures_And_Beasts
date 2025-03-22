package com.cgessinger.creaturesandbeasts.items;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class GlowingFlowerCrownItem extends FlowerCrownItem {
    public GlowingFlowerCrownItem(Holder<ArmorMaterial> material, Ingredient repairItems, ArmorItem.Type type, Properties properties) {
        super(material, repairItems, type, properties);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
