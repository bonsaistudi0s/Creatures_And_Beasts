package com.cgessinger.creaturesandbeasts.mixin;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeastsConstants;
import com.cgessinger.creaturesandbeasts.items.HealSpellBookItem;
import com.cgessinger.creaturesandbeasts.modules.CNBDataComponentTypeModule;
import com.cgessinger.creaturesandbeasts.modules.CNBItemModule;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public abstract class MixinAnvilMenu extends ItemCombinerMenu {

    @Shadow private int repairItemCountCost;

    @Shadow @Final private DataSlot cost;
    @Unique
    private static final ResourceLocation ARMOR_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "yeti_armor");

    public MixinAnvilMenu(@Nullable MenuType<?> type, int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(type, containerId, playerInventory, access);
    }

    @Inject(method = "createResult", at = @At("RETURN"))
    private void CNB_createYetiHideResult(CallbackInfo ci) {
        if (!this.resultSlots.getItem(0).isEmpty()) return;

        ItemStack input1 = this.inputSlots.getItem(0);
        if (input1.getItem() instanceof ArmorItem armorItem) {
            ItemStack input2 = this.inputSlots.getItem(1);

            if (input2.is(CNBItemModule.YETI_HIDE.get())) {
                ItemStack result = input1.copy();
                EquipmentSlot slot = armorItem.getEquipmentSlot();

                int currHide = result.getOrDefault(CNBDataComponentTypeModule.HIDE_LEVEL, 0);
                if (currHide >= 5) return;

                this.repairItemCountCost = 1;

                result.update(CNBDataComponentTypeModule.HIDE_LEVEL, 0, comp -> currHide + 1);
                result.update(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY, comp -> comp.withModifierAdded(
                        Attributes.ARMOR,
                        new AttributeModifier(
                                ARMOR_MODIFIER_ID,
                                (currHide + 1) * 0.01D,
                                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                                ),
                        EquipmentSlotGroup.bySlot(slot)
                        )
                );

                this.cost.set(1);
                this.resultSlots.setItem(0, result);
            }
        }
    }

    @Inject(method = "createResult", at =@At("RETURN"))
    private void CNB_createHealSpellBookResult(CallbackInfo ci) {
        if (!this.resultSlots.getItem(0).isEmpty()) return;

        ItemStack input1 = this.inputSlots.getItem(0);
        ItemStack input2 = this.inputSlots.getItem(1);
        if (input1.getItem() instanceof HealSpellBookItem && input2.getItem() instanceof HealSpellBookItem && ItemStack.isSameItem(input1, input2)) {
            ItemStack result;
            int cost;

            if (input1.is(CNBItemModule.HEAL_SPELL_BOOK_1.get())) {
                result = new ItemStack(CNBItemModule.HEAL_SPELL_BOOK_2.get());
                cost = 3;
            } else if (input1.is(CNBItemModule.HEAL_SPELL_BOOK_2.get())) {
                result = new ItemStack(CNBItemModule.HEAL_SPELL_BOOK_3.get());
                cost = 6;
            } else {
                return;
            }

            this.cost.set(cost);
            this.resultSlots.setItem(0, result);
            this.repairItemCountCost = 1;
        }
    }
}
