package com.cgessinger.creaturesandbeasts.modules;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeastsCommon;
import com.cgessinger.creaturesandbeasts.CreaturesAndBeastsConstants;
import com.helliongames.hellionsapi.registration.holders.ArmorMaterialDataHolder;
import com.helliongames.hellionsapi.registration.registries.HellionsAPIArmorMaterialRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.Map;

public class CNBArmorMaterialModule {
    public static final HellionsAPIArmorMaterialRegistry ARMOR_MATERIAL_MODULE = new HellionsAPIArmorMaterialRegistry(CreaturesAndBeastsConstants.MOD_ID);

    public static final ArmorMaterialDataHolder FLOWER_CROWN = ARMOR_MATERIAL_MODULE.register("flower_crown", ArmorMaterialDataHolder.of(() -> new ArmorMaterial(
            Map.of(
                    ArmorItem.Type.HELMET, 1,
                    ArmorItem.Type.CHESTPLATE, 3,
                    ArmorItem.Type.LEGGINGS, 2,
                    ArmorItem.Type.BOOTS, 3
            ),
            5,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            () -> Ingredient.EMPTY,
            List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsCommon.MOD_ID, "flower_crown"))),
            0.0f,
            0.0f))
    );

    public static final ArmorMaterialDataHolder SPORELING_BACKPACK = ARMOR_MATERIAL_MODULE.register("sporeling_backpack", ArmorMaterialDataHolder.of(() -> new ArmorMaterial(
            Map.of(
                    ArmorItem.Type.HELMET, 0,
                    ArmorItem.Type.CHESTPLATE, 1,
                    ArmorItem.Type.LEGGINGS, 0,
                    ArmorItem.Type.BOOTS, 0
            ),
            2,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            () -> Ingredient.of(Items.LEATHER),
            List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsCommon.MOD_ID, "sporeling_backpack"))),
            0.0F,
            0.0F))
    );

}
