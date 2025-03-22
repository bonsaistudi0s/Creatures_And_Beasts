package com.cgessinger.creaturesandbeasts.modules;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeastsConstants;
import com.helliongames.hellionsapi.registration.holders.LootModifierDataHolder;
import com.helliongames.hellionsapi.registration.registries.HellionsAPILootModifierRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class CNBLootModifierModule {

    public static final HellionsAPILootModifierRegistry LOOT_MODIFIERS = new HellionsAPILootModifierRegistry(CreaturesAndBeastsConstants.MOD_ID);

    public static final LootModifierDataHolder NETHER_BRIDGE_LOOT_MODIFIER = LOOT_MODIFIERS.register(
            "nether_bridge_loot_modifier",
            ResourceLocation.withDefaultNamespace("chests/nether_bridge"),
            () -> LootItem.lootTableItem(CNBItemModule.CINDERSHELL_SHELL_SHARD.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1,3))).when(LootItemRandomChanceCondition.randomChance(0.07f))
    );

    // Called in the mod initializer / constructor in order to make sure that items are registered
    public static void load() {}
}
