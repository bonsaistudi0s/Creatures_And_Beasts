package com.cgessinger.creaturesandbeasts.mixin;

import com.cgessinger.creaturesandbeasts.modules.CNBItemModule;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(AbstractFurnaceBlockEntity.class)
public class MixinAbstractFurnaceBlockEntity {

    @Inject(method = "getFuel", at = @At("RETURN"))
    private static void CNB_addCindershellShard(CallbackInfoReturnable<Map<Item, Integer>> cir) {
        Map<Item, Integer> fuelCache = cir.getReturnValue();
        fuelCache.put(CNBItemModule.CINDERSHELL_SHELL_SHARD.get(), 6400);
    }
}
