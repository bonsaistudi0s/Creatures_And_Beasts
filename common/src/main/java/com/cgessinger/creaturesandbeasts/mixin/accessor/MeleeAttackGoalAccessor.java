package com.cgessinger.creaturesandbeasts.mixin.accessor;

import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MeleeAttackGoal.class)
public interface MeleeAttackGoalAccessor {

    @Accessor("ticksUntilNextAttack")
    void setTicksUntilNextAttack(int ticks);
}
