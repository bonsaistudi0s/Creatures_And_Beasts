package com.cgessinger.creaturesandbeasts.mixin.client;

import com.cgessinger.creaturesandbeasts.entities.SporelingEntity;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class MixinMultiPlayerGameMode {

    @Inject(method = "performUseItemOn", at = @At("HEAD"), cancellable = true)
    private void CNB_manualDismountSporeling(LocalPlayer player, InteractionHand hand, BlockHitResult result, CallbackInfoReturnable<InteractionResult> cir) {
        if (player.isSecondaryUseActive() && player.getFirstPassenger() instanceof SporelingEntity sporelingEntity) {
            sporelingEntity.stopRiding();
            cir.setReturnValue(InteractionResult.CONSUME);
        }
    }
}
