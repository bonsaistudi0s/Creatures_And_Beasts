package com.cgessinger.creaturesandbeasts.util;

import com.cgessinger.creaturesandbeasts.modules.CNBItemModule;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.Random;

public interface Netable {
    boolean fromNet();

    void setFromNet(boolean fromNet);

    void saveToNetTag(ItemStack stack);

    ItemStack getItemStack();

    SoundEvent getPickupSound();

    static <T extends LivingEntity & Netable> Optional<InteractionResult> netMobPickup(Player player, InteractionHand hand, T entity) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (itemstack.getItem() == CNBItemModule.ENTITY_NET.get() && entity.isAlive()) {
            ItemStack lizardItem = entity.getItemStack();

            if (lizardItem == null) {
                return Optional.empty();
            }

            entity.playSound(entity.getPickupSound(), 1.0F, 1.0F);
            entity.saveToNetTag(lizardItem);
            player.addItem(lizardItem);
            itemstack.hurtAndBreak(1, player, hand.equals(InteractionHand.MAIN_HAND) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            spawnParticles(ParticleTypes.HAPPY_VILLAGER, entity);
            Level level = entity.level();

            entity.discard();
            return Optional.of(InteractionResult.sidedSuccess(level.isClientSide));
        } else {
            return Optional.empty();
        }
    }

    static void spawnParticles(ParticleOptions data, LivingEntity entity) {
        Random random = new Random();
        for (int i = 0; i < 7; ++i) {
            double d0 = random.nextGaussian() * 0.02D;
            double d1 = random.nextGaussian() * 0.02D;
            double d2 = random.nextGaussian() * 0.02D;
            entity.level().addParticle(data, entity.getRandomX(1.0D), entity.getRandomY() + 0.5D, entity.getRandomZ(1.0D), d0, d1, d2);
        }
    }
}
