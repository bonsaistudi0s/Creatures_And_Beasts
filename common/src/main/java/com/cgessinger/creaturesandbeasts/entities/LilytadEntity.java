package com.cgessinger.creaturesandbeasts.entities;

import com.cgessinger.creaturesandbeasts.entities.ai.FindWaterOneDeepGoal;
import com.cgessinger.creaturesandbeasts.modules.CNBLilytadTypeModule;
import com.cgessinger.creaturesandbeasts.modules.CNBSoundModule;
import com.cgessinger.creaturesandbeasts.util.LilytadType;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
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
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.List;

public class LilytadEntity extends Animal implements Shearable, GeoEntity {
    public static final EntityDataAccessor<String> TYPE = SynchedEntityData.defineId(LilytadEntity.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Boolean> SHEARED = SynchedEntityData.defineId(LilytadEntity.class, EntityDataSerializers.BOOLEAN);
    private int shearedTimer;

    private final AnimatableInstanceCache animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);
    private static final RawAnimation WALK = RawAnimation.begin().thenPlay("lilytad.walk");

    public LilytadEntity(EntityType<LilytadEntity> type, Level worldIn) {
        super(type, worldIn);
        this.shearedTimer = 0;

        this.lookControl = new LookControl(this) {
            @Override
            public void tick() {
                LilytadEntity lilytad = (LilytadEntity) this.mob;
                if (lilytad.shouldLookAround()) {
                    super.tick();
                }
            }
        };
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(TYPE, CNBLilytadTypeModule.PINK.getId().toString());
        builder.define(SHEARED, false);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        LilytadType type = LilytadType.getById(compound.getString("LilytadType"));
        if (type == null) {
            type = CNBLilytadTypeModule.PINK;
        }
        this.setLilytadType(type);
        this.shearedTimer = compound.getInt("ShearedTimer");
        this.setSheared(this.shearedTimer > 0);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("ShearedTimer", this.shearedTimer);
        compound.putString("LilytadType", this.getLilytadType().getId().toString());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FindWaterOneDeepGoal(this));
        this.goalSelector.addGoal(2, new LilytadPanicGoal(this, 1.25D));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D) {
            @Override
            public boolean canUse() {
                return !this.mob.level().getFluidState(this.mob.blockPosition()).is(FluidTags.WATER) && super.canUse();
            }

            @Override
            public boolean canContinueToUse() {
                return !this.mob.level().getFluidState(this.mob.blockPosition()).is(FluidTags.WATER) && super.canContinueToUse();
            }
        });
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide() && --this.shearedTimer == 0) {
            this.setSheared(false);
        }
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        switch (this.random.nextInt(3)) {
            case 0:
            default:
                this.setLilytadType(CNBLilytadTypeModule.PINK);
                break;
            case 1:
                this.setLilytadType(CNBLilytadTypeModule.LIGHT_PINK);
                break;
            case 2:
                this.setLilytadType(CNBLilytadTypeModule.YELLOW);
                break;
        }

        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    public static boolean checkLilytadSpawnRules(EntityType<LilytadEntity> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
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

    public void setLilytadType(LilytadType lilytadType) {
        this.entityData.set(TYPE, lilytadType.getId().toString());
    }

    public LilytadType getLilytadType() {
        return LilytadType.getById(this.entityData.get(TYPE));
    }

    @Override
    public boolean readyForShearing() {
        return !this.getSheared();
    }

    @Override
    public void shear(SoundSource source) {
        this.level().playSound(null, this, SoundEvents.SHEEP_SHEAR, source, 1.0F, 1.0F);
        this.setSheared(true);

        if (this.getLilytadType().getShearItem() == null) return;

        ItemEntity itementity = this.spawnAtLocation(this.getLilytadType().getShearItem(), 1);
        if (itementity != null) {
            itementity.setDeltaMovement(itementity.getDeltaMovement().add(
                    (this.random.nextFloat() - this.random.nextFloat()) * 0.1F,
                    this.random.nextFloat() * 0.05F,
                    (this.random.nextFloat() - this.random.nextFloat()) * 0.1F)
            );
        }
    }

    public boolean shouldLookAround() {
        return !this.level().getFluidState(this.blockPosition()).is(FluidTags.WATER);
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return CNBSoundModule.LILYTAD_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return CNBSoundModule.LILYTAD_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return CNBSoundModule.LILYTAD_DEATH.get();
    }

    private PlayState animationPredicate(AnimationState<LilytadEntity> state) {
        if (state.isMoving()) {
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
        return this.animatableInstanceCache;
    }

    static class LilytadPanicGoal extends PanicGoal {
        private final LilytadEntity lilytad;

        public LilytadPanicGoal(LilytadEntity lilytad, double speedModifier) {
            super(lilytad, speedModifier);
            this.lilytad = lilytad;
        }

        @Override
        public void start() {
            this.lilytad.getNavigation().moveTo(this.posX, this.posY, this.posZ, this.speedModifier);
            this.isRunning = true;
        }

        @Override
        protected boolean findRandomPosition() {
            boolean flag = GoalUtils.mobRestricted(this.lilytad, 5);
            Vec3 vec3 = RandomPos.generateRandomPos(this.lilytad, () -> {
                BlockPos blockpos = RandomPos.generateRandomDirection(this.lilytad.getRandom(), 5, 4);
                return generateRandomPosTowardDirection(this.lilytad, 5, flag, blockpos);
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
        private static BlockPos generateRandomPosTowardDirection(LilytadEntity lilytad, int horizontalRange, boolean flag, BlockPos posTowards) {
            BlockPos blockpos = RandomPos.generateRandomPosTowardDirection(lilytad, horizontalRange, lilytad.getRandom(), posTowards);
            return !GoalUtils.isOutsideLimits(blockpos, lilytad) && !GoalUtils.isRestricted(flag, lilytad, blockpos) && !GoalUtils.hasMalus(lilytad, blockpos) && (!GoalUtils.isNotStable(lilytad.getNavigation(), blockpos) || (GoalUtils.isWater(lilytad, blockpos) && lilytad.level().getBlockState(blockpos.below()).canOcclude() && lilytad.level().getBlockState(blockpos.above()).isAir())) ? blockpos : null;
        }
    }
}
