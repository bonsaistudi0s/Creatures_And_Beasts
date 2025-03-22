package com.cgessinger.creaturesandbeasts.events;

import com.cgessinger.creaturesandbeasts.config.CNBConfig;
import com.cgessinger.creaturesandbeasts.entities.SporelingEntity;
import com.cgessinger.creaturesandbeasts.items.HealSpellBookItem;
import com.cgessinger.creaturesandbeasts.modules.CNBItemModule;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class CNBEvents {

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        if (player.isSecondaryUseActive() && player.getFirstPassenger() instanceof SporelingEntity sporelingEntity) {
            sporelingEntity.stopRiding();
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onItemUnequip(LivingEvent.LivingTickEvent event) {
        if (event.getEntity() instanceof Player player && player.getFirstPassenger() instanceof SporelingEntity sporelingEntity && !player.getItemBySlot(EquipmentSlot.CHEST).is(CNBItemModule.SPORELING_BACKPACK.get())) {
            sporelingEntity.stopRiding();
        }
    }

	@SubscribeEvent
	public void onItemAttributeModifierCalculate(ItemAttributeModifierEvent event) {
		ItemStack input = event.getItemStack();
        CompoundTag tag = input.getTag();
        EquipmentSlot equipmentSlot = null;
        if (input.getItem() instanceof ArmorItem) {
            ArmorItem armorItem = (ArmorItem) input.getItem();
            equipmentSlot = armorItem.getSlot();
        }

		if (equipmentSlot != null && tag != null && event.getSlotType().equals(equipmentSlot) && tag.contains("HideAmount")) {
            int hideAmount = tag.getInt("HideAmount");

            if (equipmentSlot.equals(EquipmentSlot.HEAD)) {
                event.addModifier(Attributes.ARMOR, new AttributeModifier(UUID.fromString("96a6b318-81f1-475a-b4a4-b3da41d2711e"), "yeti_hide", CNBConfig.hideMultiplier * hideAmount, AttributeModifier.Operation.MULTIPLY_TOTAL));
            } else if (equipmentSlot.equals(EquipmentSlot.CHEST)) {
                event.addModifier(Attributes.ARMOR, new AttributeModifier(UUID.fromString("3f3136ff-4f04-4d62-a9cc-8d1f4175c1e2"), "yeti_hide", CNBConfig.hideMultiplier * hideAmount, AttributeModifier.Operation.MULTIPLY_TOTAL));
            } else if (equipmentSlot.equals(EquipmentSlot.LEGS)) {
                event.addModifier(Attributes.ARMOR, new AttributeModifier(UUID.fromString("f49d078c-2740-4283-8255-5d1f106efea0"), "yeti_hide", CNBConfig.hideMultiplier * hideAmount, AttributeModifier.Operation.MULTIPLY_TOTAL));
            } else {
                event.addModifier(Attributes.ARMOR, new AttributeModifier(UUID.fromString("b16e7c3f-508d-461d-8868-de6ee2a1314c"), "yeti_hide", CNBConfig.hideMultiplier * hideAmount, AttributeModifier.Operation.MULTIPLY_TOTAL));
            }
		}
	}

    @SubscribeEvent
    public void onAnvilChange(AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof ArmorItem && event.getRight().is(CNBItemModule.YETI_HIDE.get())) {
            ItemStack output = event.getLeft().copy();
            CompoundTag nbt = output.getOrCreateTag();
            int hideAmount = 1;

            if (nbt.contains("HideAmount")) {
                hideAmount += nbt.getInt("HideAmount");

                if (hideAmount > CNBConfig.hideAmount) {
                    return;
                }
            }

            nbt.putInt("HideAmount", hideAmount);
            event.setCost(CNBConfig.hideCost);
            event.setMaterialCost(1);
            event.setOutput(output);
        } else if (event.getLeft().getItem() instanceof HealSpellBookItem && event.getRight().getItem() instanceof HealSpellBookItem && event.getLeft().sameItem(event.getRight())) {
            ItemStack output;
            int cost;
            if (event.getLeft().is(CNBItemModule.HEAL_SPELL_BOOK_1.get())) {
                output = new ItemStack(CNBItemModule.HEAL_SPELL_BOOK_2.get());
                cost = 3;
            } else if (event.getLeft().is(CNBItemModule.HEAL_SPELL_BOOK_2.get())) {
                output = new ItemStack(CNBItemModule.HEAL_SPELL_BOOK_3.get());
                cost = 6;
            } else {
                return;
            }

            output.setTag(event.getLeft().getOrCreateTag());
            event.setCost(cost);
            event.setOutput(output);
            event.setMaterialCost(1);
        }
    }
}
