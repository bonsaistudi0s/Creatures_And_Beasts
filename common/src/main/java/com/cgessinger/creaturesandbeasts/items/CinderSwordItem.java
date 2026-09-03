package com.cgessinger.creaturesandbeasts.items;

import com.cgessinger.creaturesandbeasts.modules.CNBDataComponentTypeModule;
import com.cgessinger.creaturesandbeasts.modules.CNBItemTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;

public class CinderSwordItem extends SwordItem {

    public CinderSwordItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity targetEntity, LivingEntity attackingEntity) {
        int imbueLevel = stack.getOrDefault(CNBDataComponentTypeModule.IMBUE_LEVEL, 0);

        if (imbueLevel > 0) {
            targetEntity.igniteForSeconds(2 * imbueLevel);
        }

        return super.hurtEnemy(stack, targetEntity, attackingEntity);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotNumber, boolean isSelected) {
        int imbuedTicks = stack.getOrDefault(CNBDataComponentTypeModule.IMBUE_TICKS, 0);
        int imbueLevel = stack.getOrDefault(CNBDataComponentTypeModule.IMBUE_LEVEL, 0);

        if (imbuedTicks > 0) {
            stack.update(CNBDataComponentTypeModule.IMBUE_TICKS, 0, comp -> imbuedTicks - 1);
        } else if (imbueLevel > 0 && entity instanceof Player player) {
            int newImbueLevel = Math.max(imbueLevel - 1, 0);
            if (newImbueLevel == 0) {
                player.playSound(SoundEvents.FIRE_EXTINGUISH, 1.0F, 1.0F);
            }

            stack.update(CNBDataComponentTypeModule.IMBUE_LEVEL, 0, comp -> newImbueLevel);

            stack.update(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY, comp -> comp.withModifierAdded(
                    Attributes.ATTACK_DAMAGE,
                    new AttributeModifier(
                            BASE_ATTACK_DAMAGE_ID, 3.0D + (double) newImbueLevel + (double) CNBItemTiers.CINDER.getAttackDamageBonus(), AttributeModifier.Operation.ADD_VALUE
                    ),
                    EquipmentSlotGroup.MAINHAND)
            );

            stack.update(CNBDataComponentTypeModule.IMBUE_TICKS, 0, comp -> 400);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        BlockPos pos = blockhitresult.getBlockPos();

        if (level.getFluidState(pos).is(Fluids.LAVA)) {
            itemstack.update(CNBDataComponentTypeModule.IMBUE_LEVEL, 0, comp -> 4);
            itemstack.update(CNBDataComponentTypeModule.IMBUE_TICKS, 0, comp -> 400);
            player.playSound(SoundEvents.BUCKET_FILL_LAVA, 1.0F, 1.0F);
            return InteractionResultHolder.success(itemstack);
        }

        return super.use(level, player, hand);
    }
}
