package com.cgessinger.creaturesandbeasts.entities.ai;

import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class SmoothSwimGoal extends Goal {
    private final PathfinderMob entity;

    public SmoothSwimGoal(PathfinderMob entityIn) {
        this.entity = entityIn;
        entityIn.getNavigation().setCanFloat(true);
    }

    @Override
    public boolean canUse() {
        return (this.entity.isInWater() && this.entity.getFluidHeight(FluidTags.WATER) + this.entity.getEyeHeight() / 2.2 > this.entity.getFluidJumpThreshold() || this.entity.isInLava());
    }

    @Override
    public void tick() {
        this.entity.setDeltaMovement(this.entity.getDeltaMovement().add(new Vec3(0, 0.01F, 0)));

        if (this.entity.horizontalCollision) {
            this.entity.setDeltaMovement(this.entity.getDeltaMovement().add(new Vec3(0, 0.5F, 0)));
        }
    }
}
