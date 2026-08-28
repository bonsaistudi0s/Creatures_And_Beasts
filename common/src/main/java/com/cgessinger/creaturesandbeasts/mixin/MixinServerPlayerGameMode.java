package com.cgessinger.creaturesandbeasts.mixin;

import com.cgessinger.creaturesandbeasts.entities.SporelingEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerGameMode.class)
public class MixinServerPlayerGameMode {

    @Shadow @Final protected ServerPlayer player;

    @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
    private void CNB_manualDismountSporeling(ServerPlayer player, Level level, ItemStack stack, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        BlockPos blockpos = hitResult.getBlockPos();
        BlockState blockstate = level.getBlockState(blockpos);
        if (!blockstate.getBlock().isEnabled(level.enabledFeatures())) {
            return;
        }

        if (player.isSecondaryUseActive() && player.getFirstPassenger() instanceof SporelingEntity sporelingEntity) {
            sporelingEntity.stopRiding();
            cir.setReturnValue(InteractionResult.CONSUME);
        }
    }
}
