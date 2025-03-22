package com.cgessinger.creaturesandbeasts.mixin;

import com.cgessinger.creaturesandbeasts.entities.SporelingEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class MixinEntity {

    @Inject(method = "positionRider(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity$MoveFunction;)V", at = @At("HEAD"), cancellable = true)
    private void CNB_positionSporelingRider(Entity entity, Entity.MoveFunction moveFunction, CallbackInfo ci) {
        if (entity instanceof SporelingEntity sporelingEntity && ((Entity) (Object)this) instanceof Player player) {
            if (player.hasPassenger(sporelingEntity)) {
                double d0 = player.getY() + 0.68D;
                float rotation = 0;

                float attackTime = player.getAttackAnim(0);
                if (attackTime > 0) {
                    rotation = Mth.sin(Mth.sqrt(attackTime) * ((float)Math.PI * 2F)) * 0.2F * Mth.RAD_TO_DEG;
                }

                if (!player.isCrouching()) {
                    moveFunction.accept(sporelingEntity, player.getX() + Mth.sin((player.yBodyRot + rotation) * Mth.DEG_TO_RAD) * 0.45D, d0, player.getZ() - Mth.cos((player.yBodyRot + rotation) * Mth.DEG_TO_RAD) * 0.45D);
                } else {
                    moveFunction.accept(sporelingEntity, player.getX() + Mth.sin((player.yBodyRot + rotation) * Mth.DEG_TO_RAD) * 0.7D, d0, player.getZ() - Mth.cos((player.yBodyRot + rotation) * Mth.DEG_TO_RAD) * 0.7D);
                }

                this.clampRotation(player, sporelingEntity);
                ci.cancel();
            }
        }
    }

    @Inject(method = "turn", at = @At("RETURN"))
    private void CNB_rotateSporelingRider(double xRot, double yRot, CallbackInfo ci) {
        if (((Entity) (Object)this) instanceof Player player && player.getFirstPassenger() instanceof SporelingEntity sporelingEntity) {
            this.clampRotation(player, sporelingEntity);
        }
    }

    private void clampRotation(Player vehicle, SporelingEntity rider) {
        rider.setYBodyRot(vehicle.yBodyRot + 180.0F);
        rider.setYRot(vehicle.yBodyRot + 180.0F);
        rider.setYHeadRot(rider.getYRot());
    }
}
