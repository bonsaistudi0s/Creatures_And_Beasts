package com.cgessinger.creaturesandbeasts.mixin;

import com.cgessinger.creaturesandbeasts.entities.SporelingEntity;
import com.cgessinger.creaturesandbeasts.modules.CNBItemModule;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity {

    protected MixinPlayer(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow public abstract ItemStack getItemBySlot(EquipmentSlot slot);

    @Inject(method = "tick", at = @At("HEAD"))
    private void CNB_dismountSporeling(CallbackInfo ci) {
        if (this.getFirstPassenger() instanceof SporelingEntity sporelingEntity && !this.getItemBySlot(EquipmentSlot.CHEST).is(CNBItemModule.SPORELING_BACKPACK.get())) {
            sporelingEntity.stopRiding();
        }
    }
}
