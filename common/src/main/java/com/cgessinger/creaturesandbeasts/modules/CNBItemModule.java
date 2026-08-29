package com.cgessinger.creaturesandbeasts.modules;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeastsConstants;
import com.cgessinger.creaturesandbeasts.items.*;
import com.helliongames.hellionsapi.registration.holders.ItemDataHolder;
import com.helliongames.hellionsapi.registration.registries.HellionsAPIItemRegistry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;

public class CNBItemModule {
    public static final HellionsAPIItemRegistry ITEMS = new HellionsAPIItemRegistry(CreaturesAndBeastsConstants.MOD_ID);

    // Food
    public static final ItemDataHolder<?> APPLE_SLICE = ITEMS.register("apple_slice",
            ItemDataHolder.of(() -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.3F).build()))));

    public static final ItemDataHolder<?> PINK_WATERLILY = ITEMS.register("pink_waterlily",
            ItemDataHolder.of(() -> new WaterlilyBlockItem(CNBBlockModule.PINK_WATERLILY_BLOCK.get(), new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.5F).alwaysEdible()
                    .effect(new MobEffectInstance(MobEffects.HEAL, 1), 1.0F).build()))));

    public static final ItemDataHolder<?> LIGHT_PINK_WATERLILY = ITEMS.register("light_pink_waterlily",
            ItemDataHolder.of(() -> new WaterlilyBlockItem(CNBBlockModule.LIGHT_PINK_WATERLILY_BLOCK.get(), new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.5F).alwaysEdible()
                    .effect(new MobEffectInstance(MobEffects.HEAL, 1), 1.0F).build()))));

    public static final ItemDataHolder<?> YELLOW_WATERLILY = ITEMS.register("yellow_waterlily",
            ItemDataHolder.of(() -> new WaterlilyBlockItem(CNBBlockModule.YELLOW_WATERLILY_BLOCK.get(), new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.5F).alwaysEdible()
                    .effect(new MobEffectInstance(MobEffects.HEAL, 1), 1.0F).build()))));

    // Bucketed Mobs
    public static final ItemDataHolder<?> CINDERSHELL_BUCKET = ITEMS.register("cindershell_bucket",
            ItemDataHolder.of(() -> new CNBEntityBucketItem(CNBEntityModule.CINDERSHELL::get, Fluids.LAVA, () -> SoundEvents.BUCKET_EMPTY_LAVA, new Item.Properties().stacksTo(1))));

    // Misc. Items
    public static final ItemDataHolder<?> ENTITY_NET = ITEMS.register("entity_net",
            ItemDataHolder.of(() -> new Item(new Item.Properties().durability(64))));

    public static final ItemDataHolder<?> LIZARD_EGG = ITEMS.register("lizard_egg",
            ItemDataHolder.of(() -> new LizardEggItem(CNBBlockModule.LIZARD_EGGS.get())));

    public static final ItemDataHolder<?> CINDERSHELL_SHELL_SHARD = ITEMS.register("cindershell_shell_shard",
            ItemDataHolder.of(() -> new Item(new Item.Properties())));

    public static final ItemDataHolder<?> YETI_ANTLER = ITEMS.register("yeti_antler",
            ItemDataHolder.of(() -> new Item(new Item.Properties())));

    public static final ItemDataHolder<?> YETI_HIDE = ITEMS.register("yeti_hide",
            ItemDataHolder.of(() -> new Item(new Item.Properties())));


    public static final ItemDataHolder<?> PINK_MINIPAD_FLOWER = ITEMS.register("pink_minipad_flower",
            ItemDataHolder.of(() -> new Item(new Item.Properties())));

    public static final ItemDataHolder<?> LIGHT_PINK_MINIPAD_FLOWER = ITEMS.register("light_pink_minipad_flower",
            ItemDataHolder.of(() -> new Item(new Item.Properties())));

    public static final ItemDataHolder<?> YELLOW_MINIPAD_FLOWER = ITEMS.register("yellow_minipad_flower",
            ItemDataHolder.of(() -> new Item(new Item.Properties())));

    public static final ItemDataHolder<?> PINK_MINIPAD_FLOWER_GLOW = ITEMS.register("pink_minipad_flower_glow",
            ItemDataHolder.of(() -> new MinipadFlowerGlowItem(new Item.Properties())));

    public static final ItemDataHolder<?> LIGHT_PINK_MINIPAD_FLOWER_GLOW = ITEMS.register("light_pink_minipad_flower_glow",
            ItemDataHolder.of(() -> new MinipadFlowerGlowItem(new Item.Properties())));

    public static final ItemDataHolder<?> YELLOW_MINIPAD_FLOWER_GLOW = ITEMS.register("yellow_minipad_flower_glow",
            ItemDataHolder.of(() -> new MinipadFlowerGlowItem(new Item.Properties())));

    public static final ItemDataHolder<?> HEAL_SPELL_BOOK_1 = ITEMS.register("heal_spell_book_1",
            ItemDataHolder.of(() -> new HealSpellBookItem(new Item.Properties().stacksTo(1))));

    public static final ItemDataHolder<?> HEAL_SPELL_BOOK_2 = ITEMS.register("heal_spell_book_2",
            ItemDataHolder.of(() -> new HealSpellBookItem(new Item.Properties().stacksTo(1))));

    public static final ItemDataHolder<?> HEAL_SPELL_BOOK_3 = ITEMS.register("heal_spell_book_3",
            ItemDataHolder.of(() -> new HealSpellBookItem(new Item.Properties().stacksTo(1))));

    // Armor
    public static final ItemDataHolder<?> FLOWER_CROWN = ITEMS.register("flower_crown",
            ItemDataHolder.of(() -> new FlowerCrownItem(CNBArmorMaterialModule.FLOWER_CROWN.getHolder(), Ingredient.of(PINK_MINIPAD_FLOWER.get(), LIGHT_PINK_MINIPAD_FLOWER.get(), YELLOW_MINIPAD_FLOWER.get()), ArmorItem.Type.HELMET, new Item.Properties())));

    public static final ItemDataHolder<?> GLOWING_FLOWER_CROWN = ITEMS.register("glowing_flower_crown",
            ItemDataHolder.of(() -> new GlowingFlowerCrownItem(CNBArmorMaterialModule.FLOWER_CROWN.getHolder(), Ingredient.of(PINK_MINIPAD_FLOWER_GLOW.get(), LIGHT_PINK_MINIPAD_FLOWER_GLOW.get(), YELLOW_MINIPAD_FLOWER_GLOW.get()), ArmorItem.Type.HELMET, new Item.Properties())));

    public static final ItemDataHolder<?> SPORELING_BACKPACK = ITEMS.register("sporeling_backpack",
            ItemDataHolder.of(() -> new SporelingBackpackItem(CNBArmorMaterialModule.SPORELING_BACKPACK.getHolder(), ArmorItem.Type.CHESTPLATE, new Item.Properties())));

    // Tools
    public static final ItemDataHolder<?> CINDER_SWORD = ITEMS.register("cinder_sword",
            ItemDataHolder.of(() -> new CinderSwordItem(CNBItemTiers.CINDER, new Item.Properties().attributes(CinderSwordItem.createAttributes(CNBItemTiers.CINDER, 3, -2.4f)))));

    public static final ItemDataHolder<?> CACTEM_SPEAR = ITEMS.register("cactem_spear",
            ItemDataHolder.of(() -> new SpearItem(new Item.Properties().durability(100).attributes(SpearItem.createAttributes()))));

    // Spawn Eggs
    public static ItemDataHolder<?> GREBE_SPAWN_EGG = ITEMS.register("little_grebe_spawn_egg",
            ItemDataHolder.of(() -> new SpawnEggItem(CNBEntityModule.LITTLE_GREBE.get(), 0x00FFFFFF, 0x00FFFFFF, new Item.Properties())));

    public static ItemDataHolder<?> CINDERSHELL_SPAWN_EGG = ITEMS.register("cindershell_spawn_egg",
            ItemDataHolder.of(() -> new SpawnEggItem(CNBEntityModule.CINDERSHELL.get(), 0x0D0403, 0xC64500, new Item.Properties())));

    public static ItemDataHolder<?> LILYTAD_SPAWN_EGG = ITEMS.register("lilytad_spawn_egg",
            ItemDataHolder.of(() -> new SpawnEggItem(CNBEntityModule.LILYTAD.get(), 0x37702E, 0x102417, new Item.Properties())));

    public static ItemDataHolder<?> YETI_SPAWN_EGG = ITEMS.register("yeti_spawn_egg",
            ItemDataHolder.of(() -> new SpawnEggItem(CNBEntityModule.YETI.get(), 0xD7E1E7, 0x887E96, new Item.Properties())));

    public static ItemDataHolder<?> MINIPAD_SPAWN_EGG = ITEMS.register("minipad_spawn_egg",
            ItemDataHolder.of(() -> new SpawnEggItem(CNBEntityModule.MINIPAD.get(), 0x3EA62E, 0x194F28, new Item.Properties())));

    public static ItemDataHolder<?> LIZARD_SPAWN_EGG = ITEMS.register("lizard_spawn_egg",
            ItemDataHolder.of(() -> new SpawnEggItem(CNBEntityModule.LIZARD.get(), 0x00FFFFFF, 0x00FFFFFF, new Item.Properties())));

    public static ItemDataHolder<?> END_WHALE_SPAWN_EGG = ITEMS.register("end_whale_spawn_egg",
            ItemDataHolder.of(() -> new SpawnEggItem(CNBEntityModule.END_WHALE.get(), 0x5609AD, 0xD4AD5F, new Item.Properties())));

    public static ItemDataHolder<?> CACTEM_SPAWN_EGG = ITEMS.register("cactem_spawn_egg",
            ItemDataHolder.of(() -> new SpawnEggItem(CNBEntityModule.CACTEM.get(), 0x1A6E23, 0xDCEBAB, new Item.Properties())));

    public static ItemDataHolder<?> LIZARD_ITEM_DESERT = ITEMS.register("lizard_item_desert",
            ItemDataHolder.of(() -> new LizardItem(CNBEntityModule.LIZARD.get(), 0x00FFFFFF, 0x00FFFFFF, new Item.Properties().component(DataComponents.ENTITY_DATA, CustomData.EMPTY.update(comp -> comp.putString("LizardType", CNBLizardTypeModule.DESERT.getId().toString()))), CNBLizardTypeModule.DESERT)));

    public static ItemDataHolder<?> LIZARD_ITEM_DESERT_2 = ITEMS.register("lizard_item_desert_2",
            ItemDataHolder.of(() -> new LizardItem(CNBEntityModule.LIZARD.get(), 0x00FFFFFF, 0x00FFFFFF, new Item.Properties().component(DataComponents.ENTITY_DATA, CustomData.EMPTY.update(comp -> comp.putString("LizardType", CNBLizardTypeModule.DESERT_2.getId().toString()))), CNBLizardTypeModule.DESERT_2)));

    public static ItemDataHolder<?> LIZARD_ITEM_JUNGLE = ITEMS.register("lizard_item_jungle",
            ItemDataHolder.of(() -> new LizardItem(CNBEntityModule.LIZARD.get(), 0x00FFFFFF, 0x00FFFFFF, new Item.Properties().component(DataComponents.ENTITY_DATA, CustomData.EMPTY.update(comp -> comp.putString("LizardType", CNBLizardTypeModule.JUNGLE.getId().toString()))), CNBLizardTypeModule.JUNGLE)));

    public static ItemDataHolder<?> LIZARD_ITEM_JUNGLE_2 = ITEMS.register("lizard_item_jungle_2",
            ItemDataHolder.of(() -> new LizardItem(CNBEntityModule.LIZARD.get(), 0x00FFFFFF, 0x00FFFFFF, new Item.Properties().component(DataComponents.ENTITY_DATA, CustomData.EMPTY.update(comp -> comp.putString("LizardType", CNBLizardTypeModule.JUNGLE_2.getId().toString()))), CNBLizardTypeModule.JUNGLE_2)));

    public static ItemDataHolder<?> LIZARD_ITEM_MUSHROOM = ITEMS.register("lizard_item_mushroom",
            ItemDataHolder.of(() -> new LizardItem(CNBEntityModule.LIZARD.get(), 0x00FFFFFF, 0x00FFFFFF, new Item.Properties().component(DataComponents.ENTITY_DATA, CustomData.EMPTY.update(comp -> comp.putString("LizardType", CNBLizardTypeModule.MUSHROOM.getId().toString()))), CNBLizardTypeModule.MUSHROOM)));

    public static ItemDataHolder<?> SPORELING_OVERWORLD_EGG = ITEMS.register("sporeling_overworld_egg",
            ItemDataHolder.of(() -> new SporelingSpawnEggItem(CNBEntityModule.SPORELING.get(), 0xDE0942, 0xFFEBC4, new Item.Properties())));

    public static ItemDataHolder<?> SPORELING_NETHER_EGG = ITEMS.register("sporeling_nether_egg",
            ItemDataHolder.of(() -> new SporelingSpawnEggItem(CNBEntityModule.SPORELING.get(), 0xBF2828, 0xFF9245, new Item.Properties())));

    // Block Items
    public static ItemDataHolder<?> CINDERSHELL_FURNACE = ITEMS.register("cinder_furnace",
            ItemDataHolder.of(() -> new CinderFurnaceItem(CNBBlockModule.CINDER_FURNACE.get(), new Item.Properties())));

    // Called in the mod initializer / constructor in order to make sure that items are registered
    public static void load() {}
}
