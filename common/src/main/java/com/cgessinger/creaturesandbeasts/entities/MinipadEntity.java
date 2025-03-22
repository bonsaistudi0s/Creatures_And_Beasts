package com.cgessinger.creaturesandbeasts.entities;

import com.cgessinger.creaturesandbeasts.modules.CNBMinipadTypeModule;
import com.cgessinger.creaturesandbeasts.modules.CNBSoundModule;
import com.cgessinger.creaturesandbeasts.util.MinipadType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.List;

public class MinipadEntity extends Animal implements Shearable, GeoEntity {
    public static final EntityDataAccessor<String> TYPE = SynchedEntityData.defineId(MinipadEntity.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Boolean> SHEARED = SynchedEntityData.defineId(MinipadEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> GLOWING = SynchedEntityData.defineId(MinipadEntity.class, EntityDataSerializers.BOOLEAN);

    private static final RawAnimation SWIM = RawAnimation.begin().thenPlay("minipad_swim");
    private static final RawAnimation FLOAT = RawAnimation.begin().thenPlay("minipad_float");
    private static final RawAnimation WALK = RawAnimation.begin().thenPlay("minipad_walk");

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private int shearedTimer;

    public MinipadEntity(EntityType<MinipadEntity> type, Level worldIn) {
        super(type, worldIn);
        this.shearedTimer = 0;
        this.setPathfindingMalus(PathType.WATER, 0.0F);

        this.lookControl = new LookControl(this) {
            @Override
            public void tick() {
                MinipadEntity minipad = (MinipadEntity) this.mob;
                if (minipad.shouldLookAround()) {
                    super.tick();
                }
            }
        };
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(TYPE, CNBMinipadTypeModule.PINK.getId().toString());
        builder.define(SHEARED, false);
        builder.define(GLOWING, false);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        MinipadType type = MinipadType.getById(compound.getString("MinipadType"));
        if (type == null) {
            type = CNBMinipadTypeModule.PINK;
        }
        this.setMinipadType(type);
        this.shearedTimer = compound.getInt("ShearedTimer");
        this.setSheared(this.shearedTimer > 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("ShearedTimer", this.shearedTimer);
        compound.putString("MinipadType", this.getMinipadType().getId().toString());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 12.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new MinipadFloatGoal(this));
        this.goalSelector.addGoal(1, new MinipadPanicGoal(this, 1.25D));
        //this.goalSelector.addGoal(2, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(2, new MinipadTryFindWaterGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new MinipadRandomStrollGoal(this, 1.0D, 60, 240));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide() && --this.shearedTimer == 0) {
            this.setSheared(false);
        }

        if (!this.level().isClientSide()) {
            long time = this.level().getDayTime();
            this.setGlowing(time >= 13000 && time <= 23000);
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.floatMinipad();

        SimpleParticleType particle = this.getMinipadType().getParticle();
        if (particle != null && this.isGlowing() && !this.getSheared()) {
            if (this.random.nextDouble() < 0.1) {
                this.level().addParticle(particle, this.getX() + (this.random.nextDouble() * 0.5D - 0.25), this.getY() + 0.8 + (this.random.nextDouble() * 0.1D - 0.05), this.getZ() + (this.random.nextDouble() * 0.5D - 0.25), this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z);
            }
            if (this.random.nextDouble() < 0.07) {
                this.level().addParticle(particle, this.getX() + (this.random.nextDouble() * 24D - 12), this.getY() + this.random.nextDouble() * 7.5D, this.getZ() + (this.random.nextDouble() * 24D - 12), this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z);
            }
        }

    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        switch (this.random.nextInt(3)) {
            case 0:
                this.setMinipadType(CNBMinipadTypeModule.PINK);
            case 1:
                this.setMinipadType(CNBMinipadTypeModule.LIGHT_PINK);
                break;
            case 2:
            default:
                this.setMinipadType(CNBMinipadTypeModule.YELLOW);
                break;
        }

        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    public static boolean checkMinipadSpawnRules(EntityType<MinipadEntity> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
        return true;
    }

    @Override
    protected void pushEntities() {
        List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(0.2, 0, 0.2), EntitySelector.pushableBy(this));
        if (!list.isEmpty()) {
            int i = this.level().getGameRules().getInt(GameRules.RULE_MAX_ENTITY_CRAMMING);
            if (i > 0 && list.size() > i - 1 && this.random.nextInt(4) == 0) {
                int j = 0;

                for (Entity entity : list) {
                    if (!entity.isPassenger()) {
                        ++j;
                    }
                }

                if (j > i - 1) {
                    this.hurt(this.level().damageSources().cramming(), 6.0F);
                }
            }

            for (Entity entity : list) {
                this.doPush(entity);
            }
        }

    }

    @Override
    public boolean canBeCollidedWith() {
        return this.isAlive();
    }

    @Override
    public boolean canStandOnFluid(FluidState fluidState) {
        return fluidState.is(FluidTags.WATER);
    }

    private void floatMinipad() {
        if (this.isInWater()) {
            CollisionContext collisioncontext = CollisionContext.of(this);
            if (collisioncontext.isAbove(LiquidBlock.STABLE_SHAPE, this.blockPosition(), true) && !this.level().getFluidState(this.blockPosition().above()).is(FluidTags.WATER)) {
                this.setOnGround(true);
            } else {
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5D).add(0.0D, 0.1D, 0.0D));
            }
        }

    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob entity) {
        return null;
    }

    public boolean getSheared() {
        return this.entityData.get(SHEARED);
    }

    public void setSheared(boolean sheared) {
        this.shearedTimer = sheared ? 18000 : 0;
        this.entityData.set(SHEARED, sheared);
    }

    public void setGlowing(boolean isGlowing) {
        this.entityData.set(GLOWING, isGlowing);
    }

    public boolean isGlowing() {
        return this.entityData.get(GLOWING);
    }

    public void setMinipadType(MinipadType minipadType) {
        this.entityData.set(TYPE, minipadType.getId().toString());
    }

    public MinipadType getMinipadType() {
        return MinipadType.getById(this.entityData.get(TYPE));
    }

    @Override
    public double getFluidJumpThreshold() {
        return 0.45D;
    }

    @Override
    public boolean readyForShearing() {
        return !this.getSheared();
    }

    @Override
    public void shear(SoundSource source) {
        this.level().playSound(null, this, SoundEvents.SHEEP_SHEAR, source, 1.0F, 1.0F);
        this.setSheared(true);

        ItemEntity itemEntity = null;
        
        if (this.level().getDayTime() > 13000 && this.getMinipadType().getGlowShearItem() != null) {
            itemEntity = this.spawnAtLocation(this.getMinipadType().getGlowShearItem(), 1);
        } else if (this.getMinipadType().getShearItem() != null) {
            itemEntity = this.spawnAtLocation(this.getMinipadType().getShearItem(), 1);
        }

        if (itemEntity != null) {
            itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().add(
                                    (this.random.nextFloat() - this.random.nextFloat()) * 0.1F,
                                   this.random.nextFloat() * 0.05F,
                                    (this.random.nextFloat() - this.random.nextFloat()) * 0.1F
                    )
            );
        }
    }

    public boolean shouldLookAround() {
        return !this.level().getFluidState(this.blockPosition()).is(FluidTags.WATER);
    }

    @Override
    protected int getBaseExperienceReward() {
        return 2 + this.level().random.nextInt(3);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        if (blockIn.getFluidState().isEmpty()) {
            this.playSound(CNBSoundModule.MINIPAD_STEP.get(), this.getSoundVolume() * 0.3F, this.getVoicePitch());
        }
    }

    @Override
    protected SoundEvent getSwimSound() {
        return CNBSoundModule.MINIPAD_SWIM.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return CNBSoundModule.MINIPAD_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return CNBSoundModule.MINIPAD_HURT.get();
    }

    private PlayState animationPredicate(AnimationState<MinipadEntity> state) {
        if (this.isInWater() && state.isMoving()) {
            state.getController().setAnimation(SWIM);
            return PlayState.CONTINUE;
        } else if (this.isInWater()) {
            state.getController().setAnimation(FLOAT);
            return PlayState.CONTINUE;
        } else if (state.isMoving()) {
            state.getController().setAnimation(WALK);
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::animationPredicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    static class MinipadFloatGoal extends FloatGoal {
        private final MinipadEntity minipad;

        public MinipadFloatGoal(MinipadEntity minipad) {
            super(minipad);
            this.minipad = minipad;
        }

        @Override
        public void tick() {
            if (this.minipad.getFluidHeight(FluidTags.WATER) > 0.5D) {
                this.minipad.getJumpControl().jump();
            } else {
                this.minipad.setDeltaMovement(this.minipad.getDeltaMovement().add(0.0D, 0.005D, 0.0D));
            }
        }
    }

    static class MinipadPanicGoal extends PanicGoal {
        private final MinipadEntity minipad;

        public MinipadPanicGoal(MinipadEntity minipad, double speedModifier) {
            super(minipad, speedModifier);
            this.minipad = minipad;
        }

        @Override
        public void start() {
            this.minipad.getNavigation().moveTo(this.posX, this.posY, this.posZ, this.speedModifier);
            this.isRunning = true;
        }

        @Override
        protected boolean findRandomPosition() {
            boolean flag = GoalUtils.mobRestricted(this.minipad, 5);
            Vec3 vec3 = RandomPos.generateRandomPos(this.minipad, () -> {
                BlockPos blockpos = RandomPos.generateRandomDirection(this.minipad.getRandom(), 5, 4);
                return generateRandomPosTowardDirection(this.minipad, 5, flag, blockpos);
            });
            if (vec3 == null) {
                return false;
            }

            this.posX = vec3.x;
            this.posY = vec3.y;
            this.posZ = vec3.z;
            return true;
        }

        @Nullable
        private static BlockPos generateRandomPosTowardDirection(MinipadEntity minipad, int horizontalRange, boolean flag, BlockPos posTowards) {
            BlockPos blockpos = RandomPos.generateRandomPosTowardDirection(minipad, horizontalRange, minipad.getRandom(), posTowards);
            return !GoalUtils.isOutsideLimits(blockpos, minipad) && !GoalUtils.isRestricted(flag, minipad, blockpos) && !GoalUtils.hasMalus(minipad, blockpos) && (!GoalUtils.isNotStable(minipad.getNavigation(), blockpos) || GoalUtils.isWater(minipad, blockpos)) ? blockpos : null;
        }
    }

    static class MinipadRandomStrollGoal extends RandomStrollGoal {
        private final MinipadEntity minipad;
        private final int intervalLand;
        private final int intervalWater;
        private final boolean checkNoActionTime;

        public MinipadRandomStrollGoal(MinipadEntity minipad, double speedModifier) {
            this(minipad, speedModifier, 60, 120);
        }

        public MinipadRandomStrollGoal(MinipadEntity minipad, double speedModifier, int intervalLand, int intervalWater) {
            this(minipad, speedModifier, intervalLand, intervalWater, true);
        }

        public MinipadRandomStrollGoal(MinipadEntity minipad, double speedModifier, int intervalLand, int intervalWater, boolean checkNoActionTime) {
            super(minipad, speedModifier, intervalLand, checkNoActionTime);
            this.minipad = minipad;
            this.intervalLand = intervalLand;
            this.intervalWater = intervalWater;
            this.checkNoActionTime = checkNoActionTime;
        }

        @Override
        public void start() {
            this.minipad.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
        }

        @Override
        public boolean canUse() {
            if (this.mob.isVehicle()) {
                return false;
            } else {
                if (!this.forceTrigger) {
                    if (this.checkNoActionTime && this.mob.getNoActionTime() >= 100) {
                        return false;
                    }

                    int i = this.minipad.isInWater() ? this.intervalWater : this.intervalLand;
                    if (this.mob.getRandom().nextInt(reducedTickDelay(i)) != 0) {
                        return false;
                    }
                }

                Vec3 vec3 = this.getPosition();
                if (vec3 == null) {
                    return false;
                } else {
                    this.wantedX = vec3.x;
                    this.wantedY = vec3.y;
                    this.wantedZ = vec3.z;
                    this.forceTrigger = false;
                    return true;
                }
            }
        }

        @Override
        protected Vec3 getPosition() {
            boolean flag = GoalUtils.mobRestricted(this.minipad, 10);
            Vec3 vec3 = RandomPos.generateRandomPos(this.minipad, () -> {
                BlockPos blockpos = RandomPos.generateRandomDirection(this.minipad.getRandom(), 10, 7);
                return generateRandomPosTowardDirection(this.minipad, 10, flag, blockpos);
            });

            return vec3;
        }

        @Nullable
        private static BlockPos generateRandomPosTowardDirection(MinipadEntity minipad, int horizontalRange, boolean flag, BlockPos posTowards) {
            BlockPos blockpos = RandomPos.generateRandomPosTowardDirection(minipad, horizontalRange, minipad.getRandom(), posTowards);
            return !GoalUtils.isOutsideLimits(blockpos, minipad) && !GoalUtils.isRestricted(flag, minipad, blockpos) && !GoalUtils.hasMalus(minipad, blockpos) && (!GoalUtils.isNotStable(minipad.getNavigation(), blockpos) || GoalUtils.isWater(minipad, blockpos)) ? blockpos : null;
        }
    }

    static class MinipadTryFindWaterGoal extends TryFindWaterGoal {
        private final MinipadEntity minipad;
        private final double speedModifier;

        public MinipadTryFindWaterGoal(MinipadEntity minipad, double speedModifier) {
            super(minipad);
            this.minipad = minipad;
            this.speedModifier = speedModifier;
        }

        @Override
        public void start() {
                BlockPos blockpos = this.minipad.blockPosition();
                BlockPos waterPos = this.minipad.level().getBlockState(blockpos).getCollisionShape(this.minipad.level(), blockpos).isEmpty() ? null : BlockPos.findClosestMatch(this.minipad.blockPosition(), 16, 5, (pos) -> this.minipad.level().getFluidState(pos).is(FluidTags.WATER)).orElse(null);
                if (waterPos != null) {
                    this.minipad.getNavigation().moveTo(waterPos.getX(), waterPos.getY(), waterPos.getZ(), this.speedModifier);
                }
        }
    }
}
