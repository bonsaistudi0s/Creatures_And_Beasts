package com.cgessinger.creaturesandbeasts.entities.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.level.BlockGetter;

import javax.annotation.Nullable;


public class FindWaterOneDeepGoal extends TryFindWaterGoal {
    private final PathfinderMob creature;

    public FindWaterOneDeepGoal(PathfinderMob creature) {
        super(creature);
        this.creature = creature;
    }

    @Override
    public void start() {
        BlockPos blockpos = lookForWaterOneDeep(this.creature.level(), this.creature, 16);

        if (blockpos != null) {
            this.creature.getNavigation().moveTo(blockpos.getX(), blockpos.getY(), blockpos.getZ(), this.creature.isInWater() ? 1.5F : 1.0F);
        }
    }

    @Override
    public boolean canUse() {
        BlockPos pos = this.creature.blockPosition();
        boolean isValidPos = checkValidPos(pos);
        return this.creature.onGround() && !isValidPos;
    }

    private boolean checkValidPos(BlockPos pos) {
        return this.creature.level().getFluidState(pos).is(FluidTags.WATER) && this.creature.onGround() && this.creature.level().getBlockState(pos.above()).isAir();
    }

    @Nullable
    protected BlockPos lookForWaterOneDeep(BlockGetter blockGetter, Entity entity, int horizontalRange) {
        BlockPos blockpos = entity.blockPosition();
        return !blockGetter.getBlockState(blockpos).getCollisionShape(blockGetter, blockpos).isEmpty() ? null : BlockPos.findClosestMatch(entity.blockPosition(), horizontalRange, 10, (pos) -> blockGetter.getFluidState(pos).is(FluidTags.WATER) && blockGetter.getBlockState(pos.below()).isSolid() && blockGetter.getBlockState(pos.above()).isAir()).orElse(null);
    }
}
