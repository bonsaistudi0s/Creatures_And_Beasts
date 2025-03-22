package com.cgessinger.creaturesandbeasts.entities;

import com.cgessinger.creaturesandbeasts.modules.CNBSoundModule;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.EnumSet;

import static com.cgessinger.creaturesandbeasts.modules.CNBItemTagModule.Items.END_WHALE_FOOD;

public class EndWhaleEntity extends TamableAnimal implements FlyingAnimal, Saddleable, GeoEntity {
    private static final EntityDataAccessor<Boolean> SADDLED = SynchedEntityData.defineId(EndWhaleEntity.class, EntityDataSerializers.BOOLEAN);

    private final AnimatableInstanceCache animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);
    private static final RawAnimation FLY = RawAnimation.begin().thenPlay("whale_fly");

    public EndWhaleEntity(EntityType<EndWhaleEntity> entityType, Level level) {
        super(entityType, level);
        this.setTame(false, false);
        this.moveControl = new FlyingMoveControl(this, 2, true);
        this.lookControl = new EndWhaleLookControl(this);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 160.0D)
                .add(Attributes.MOVEMENT_SPEED, 1.0D)
                .add(Attributes.FOLLOW_RANGE, 100.0D)
                .add(Attributes.FLYING_SPEED, 1.0D);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new EndWhaleTemptGoal(this, 1.25D, Ingredient.of(END_WHALE_FOOD)));
        this.goalSelector.addGoal(1, new EndWhaleWanderGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SADDLED, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Saddled", this.isSaddled());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        boolean isSaddled = tag.getBoolean("Saddled");
        if (isSaddled) {
            this.equipSaddle(ItemStack.EMPTY, SoundSource.PLAYERS);
        }
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new EndWhaleBodyRotationControl(this);
    }

    @Override
    public int getMaxHeadYRot() {
        return 0;
    }

    @Override
    public int getHeadRotSpeed() {
        return 30;
    }

    @Override
    public boolean isSaddleable() {
        return this.isTame();
    }

    @Override
    public void equipSaddle(ItemStack stack, @Nullable SoundSource soundSource) {
        this.entityData.set(SADDLED, true);
        this.playSound(SoundEvents.HORSE_SADDLE, 1.0F, 1.0F);
    }

    public void removeSaddle() {
        this.entityData.set(SADDLED, false);
        this.spawnAtLocation(Items.SADDLE);
        this.playSound(SoundEvents.HORSE_SADDLE, 0.8F, 1.0F);
    }

    @Override
    public boolean isSaddled() {
        return this.entityData.get(SADDLED);
    }

    private void mountWhale(Player player) {
        if (!this.level().isClientSide) {
            player.setYRot(this.getYRot());
            player.setXRot(this.getXRot());
            player.startRiding(this);
        }
    }

    @Override
    protected @NotNull Vec3 getPassengerAttachmentPoint(Entity rider, EntityDimensions dimensions, float partialTick) {
        double verticalOffset = super.getPassengerAttachmentPoint(rider, dimensions, partialTick).y;
        float whaleRoll = this.getWhaleRoll(rider) * Mth.PI/180;
        float whalePitch = this.getWhalePitch(rider) * Mth.PI/180;

        Vec3 pos = new Vec3(this.getX() + Mth.cos(this.getYRot() * Mth.PI/180) * verticalOffset * Mth.sin(whaleRoll) + Mth.sin(this.getYRot() * Mth.PI/180) * verticalOffset * Mth.sin(whalePitch),
                this.getY() + verticalOffset * Mth.cos(whaleRoll) * Mth.cos(whalePitch),
                this.getZ() + Mth.sin(this.getYRot() * Mth.PI/180) * verticalOffset * Mth.sin(whaleRoll) - Mth.cos(this.getYRot() * Mth.PI/180) * verticalOffset * Mth.sin(whalePitch));

        this.clampRotation(rider);

        return pos;
    }

    protected void clampRotation(Entity rider) {
        rider.setYBodyRot(this.getYRot());
        float f = Mth.wrapDegrees(rider.getYRot() - this.getYRot());
        float f1 = Mth.clamp(f, -90.0F, 90.0F);
        rider.yRotO += f1 - f;
        rider.setYRot(rider.getYRot() + f1 - f);
        rider.setYHeadRot(rider.getYRot());
    }

    private float getWhaleRoll(Entity rider) {
        float whaleRotY = this.getYRot();
        float riderRotY = rider.getYRot();

        return Mth.wrapDegrees(whaleRotY - riderRotY) / 2;
    }

    private float getWhalePitch(Entity rider) {
        float whaleRotY = this.getXRot();
        float riderRotY = rider.getXRot();

        return Mth.wrapDegrees(whaleRotY - riderRotY);
    }

    @Override
    public boolean isControlledByLocalInstance() {
        return this.getControllingPassenger() instanceof LivingEntity;
    }

    @Override
    public LivingEntity getControllingPassenger() {
        Entity entity = this.getFirstPassenger();

        if (entity instanceof Player player) {
            return player;
        }

        return null;
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isAlive()) {
            if (this.isVehicle() && this.isControlledByLocalInstance() && this.isSaddled()) {
                LivingEntity livingentity = this.getControllingPassenger();
                this.setYRot(Mth.rotLerp(0.05F, this.getYRot(), livingentity.getYRot()));
                this.yRotO = this.getYRot();
                this.setXRot(livingentity.getXRot() * 0.5F);
                this.setRot(this.getYRot(), this.getXRot());
                this.yBodyRot = this.getYRot();
                this.yHeadRot = this.yBodyRot;
                float forwardMovement = livingentity.zza;
                if (forwardMovement <= 0.0F) {
                    forwardMovement *= 0.25F;
                }

                float verticalMovement = 0;

                if (Mth.abs(livingentity.getXRot()) > 7.0F) {
                    verticalMovement = Mth.rotLerp(0.01F, this.getXRot(), livingentity.getXRot()) * -forwardMovement/50;
                }

                if (this.isControlledByLocalInstance()) {
                    this.setSpeed((float)this.getAttributeValue(Attributes.FLYING_SPEED));

                    Vec3 proposedMovement = new Vec3(0, verticalMovement, forwardMovement);

                    if (this.isInLava()) {
                        this.moveRelative(0.02F, proposedMovement);
                        this.move(MoverType.SELF, this.getDeltaMovement());
                        this.setDeltaMovement(this.getDeltaMovement().scale(0.5D));
                    } else {
                        BlockPos ground = new BlockPos((int) this.getX(), (int) (this.getY() - 1.0D), (int) this.getZ());
                        float f = 0.91F;
                        if (this.onGround()) {
                            f = this.level().getBlockState(ground).getBlock().getFriction() * 0.91F;
                        }

                        float f1 = 0.16277137F / (f * f * f);

                        this.moveRelative(this.onGround() ? 0.1F * f1 : 0.1F, proposedMovement);
                        this.move(MoverType.SELF, this.getDeltaMovement());
                        this.setDeltaMovement(this.getDeltaMovement().scale(f));
                    }
                } else if (livingentity instanceof Player) {
                    this.setDeltaMovement(Vec3.ZERO);
                }

                this.calculateEntityAnimation(false);
                this.tryCheckInsideBlocks();
            } else {
                if (this.isInLava()) {
                    this.moveRelative(0.02F, travelVector);
                    this.move(MoverType.SELF, this.getDeltaMovement());
                    this.setDeltaMovement(this.getDeltaMovement().scale(0.5D));
                } else {
                    BlockPos ground = new BlockPos((int) this.getX(), (int) (this.getY() - 1.0D), (int) this.getZ());
                    float f = 0.91F;
                    if (this.onGround()) {
                        f = this.level().getBlockState(ground).getBlock().getFriction() * 0.91F;
                    }

                    float f1 = 0.16277137F / (f * f * f);

                    this.moveRelative(this.onGround() ? 0.1F * f1 : 0.02F, travelVector);
                    this.move(MoverType.SELF, this.getDeltaMovement());
                    this.setDeltaMovement(this.getDeltaMovement().scale(f));
                }

                this.calculateEntityAnimation(false);
            }
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (this.level().isClientSide) {
            boolean flag = this.isOwnedBy(player) || this.isTame() || itemstack.is(END_WHALE_FOOD) && !this.isTame();
            return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
        } else if (this.isSaddled() && player.isSecondaryUseActive()) {
            this.removeSaddle();
            return InteractionResult.CONSUME;
        } else if (this.isSaddled()) {
            this.mountWhale(player);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else if (!this.isTame()) {
            if (itemstack.is(END_WHALE_FOOD)) {
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                if (this.random.nextInt(10) == 0) {
                    this.tame(player);
                    this.navigation.stop();
                    this.setTarget(null);
                    this.setOrderedToSit(true);
                    this.level().broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.level().broadcastEntityEvent(this, (byte) 6);
                }

                return InteractionResult.SUCCESS;
            }
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public void onPassengerTurned(Entity entity) {
        this.clampRotation(entity);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob endWhale) {
        return null;
    }

    @Override
    public int getBaseExperienceReward() {
        return 12 + this.level().random.nextInt(5);
    }

    public static boolean checkEndWhaleSpawnRules(EntityType<EndWhaleEntity> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
        return true;
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return 10.0F;
    }

    @Override
    public boolean isFlying() {
        return true;
    }

    @Override
    public boolean causeFallDamage(float p_148750_, float p_148751_, DamageSource p_148752_) {
        return false;
    }

    @Override
    protected void checkFallDamage(double p_27754_, boolean p_27755_, BlockState p_27756_, BlockPos p_27757_) {
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, level);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(false);
        flyingpathnavigation.setCanPassDoors(false);
        return flyingpathnavigation;
    }

    @Nullable
    @Override
    public SoundEvent getAmbientSound() {
        return CNBSoundModule.END_WHALE_AMBIENT.get();
    }

    @Override
    public int getAmbientSoundInterval() {
        return 800;
    }

    @Override
    protected float getSoundVolume() {
        return 5.0F;
    }

    private PlayState animationPredicate(AnimationState<EndWhaleEntity> event) {
        event.getController().setAnimation(FLY);
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::animationPredicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animatableInstanceCache;
    }

    static class EndWhaleLookControl extends LookControl {
        private final EndWhaleEntity endWhale;

        public EndWhaleLookControl(EndWhaleEntity endWhale) {
            super(endWhale);
            this.endWhale = endWhale;
        }

        @Override
        public void tick() {
            if (this.endWhale.yBodyRot != this.endWhale.getYHeadRot()) {
                this.endWhale.yHeadRot = Mth.rotLerp(0.05F, this.endWhale.getYHeadRot(), this.endWhale.yBodyRot);
            }
        }
    }

    static class EndWhaleBodyRotationControl extends BodyRotationControl {
        private final EndWhaleEntity endWhale;
        private int headStableTime;
        private float lastStableYHeadRot;


        public EndWhaleBodyRotationControl(EndWhaleEntity endWhale) {
            super(endWhale);
            this.endWhale = endWhale;
        }

        @Override
        public void clientTick() {
            if (this.isMoving()) {
                this.endWhale.yBodyRot = Mth.rotLerp(0.05F, this.endWhale.yBodyRot, this.endWhale.getYRot());
                this.rotateHeadIfNecessary();
                this.lastStableYHeadRot = this.endWhale.yHeadRot;
                this.headStableTime = 0;
            } else {
                if (this.notCarryingMobPassengers()) {
                    if (Math.abs(this.endWhale.yHeadRot - this.lastStableYHeadRot) > 15.0F) {
                        this.headStableTime = 0;
                        this.lastStableYHeadRot = this.endWhale.yHeadRot;
                        this.rotateHeadIfNecessary();
                    } else {
                        ++this.headStableTime;
                        if (this.headStableTime > 10) {
                            this.rotateHeadTowardsFront();
                        }
                    }
                }
            }
        }

        private void rotateHeadIfNecessary() {
            this.endWhale.yHeadRot = Mth.rotLerp(0.05F, this.endWhale.yHeadRot, this.endWhale.yBodyRot);
        }

        private void rotateHeadTowardsFront() {
            this.endWhale.yHeadRot = Mth.rotLerp(0.05F, this.endWhale.yHeadRot, this.endWhale.yBodyRot);
        }

        private boolean notCarryingMobPassengers() {
            return !(this.endWhale.getFirstPassenger() instanceof Mob);
        }


        private boolean isMoving() {
            double d0 = this.endWhale.getX() - this.endWhale.xo;
            double d1 = this.endWhale.getZ() - this.endWhale.zo;
            return d0 * d0 + d1 * d1 > (double)2.5000003E-7F;
        }
    }

    static class EndWhaleWanderGoal extends Goal {
        private final EndWhaleEntity endWhale;

        EndWhaleWanderGoal(EndWhaleEntity endWhale) {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
            this.endWhale = endWhale;
        }

        @Override
        public boolean canUse() {
            return this.endWhale.navigation.isDone() && this.endWhale.random.nextInt(3) == 0 && !this.endWhale.isVehicle() && !this.endWhale.isLeashed();
        }

        @Override
        public boolean canContinueToUse() {
            return this.endWhale.navigation.isInProgress() && !this.endWhale.isVehicle() && !this.endWhale.isLeashed();
        }

        @Override
        public void start() {
            Vec3 vec3 = this.findPos();
            if (vec3 != null) {
                BlockPos targetPos = new BlockPos((int) vec3.x(), (int) vec3.y(), (int) vec3.z());
                this.endWhale.navigation.moveTo(this.endWhale.navigation.createPath(targetPos, 3), 1.0D);
            }
        }

        @Override
        public void stop() {
            this.endWhale.navigation.stop();
        }

        @Nullable
        private Vec3 findPos() {
            Vec3 vec3 = this.endWhale.getViewVector(0.5F);

            Vec3 vec32 = HoverRandomPos.getPos(this.endWhale, 20, 20, vec3.x, vec3.z, (float)Math.PI, 50, 15);
            vec32 = vec32 != null ? vec32 : AirAndWaterRandomPos.getPos(this.endWhale, 20, 20, -2, vec3.x, vec3.z, ((float)Math.PI));

            if (this.endWhale.isSaddled() && vec32 != null && this.endWhale.getOwner() != null && vec32.distanceTo(this.endWhale.getOwner().position()) > 100.0D) {
                vec32 = null;
            }

            return vec32;
        }
    }

    static class EndWhaleTemptGoal extends Goal {
        private static final TargetingConditions TEMP_TARGETING = TargetingConditions.forNonCombat().range(100.0D).ignoreLineOfSight();
        private final TargetingConditions targetingConditions;
        protected final EndWhaleEntity endWhale;
        private final double speedModifier;
        @Nullable
        protected Player player;
        private int calmDown;
        private final Ingredient items;

        public EndWhaleTemptGoal(EndWhaleEntity endWhale, double speedModifier, Ingredient temptIngredient) {
            this.endWhale = endWhale;
            this.speedModifier = speedModifier;
            this.items = temptIngredient;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
            this.targetingConditions = TEMP_TARGETING.copy().selector(this::shouldFollow);
        }

        @Override
        public boolean canUse() {
            if (this.calmDown > 0) {
                --this.calmDown;
                return false;
            } else {
                this.player = this.endWhale.level().getNearestPlayer(this.targetingConditions, this.endWhale);
                return this.player != null && !this.endWhale.isVehicle();
            }
        }

        private boolean shouldFollow(LivingEntity entity) {
            return this.items.test(entity.getMainHandItem()) || this.items.test(entity.getOffhandItem());
        }

        @Override
        public boolean canContinueToUse() {
            return this.canUse();
        }

        @Override
        public void stop() {
            this.player = null;
            this.endWhale.getNavigation().stop();
            this.calmDown = reducedTickDelay(100);
        }

        @Override
        public void tick() {
            this.endWhale.getLookControl().setLookAt(this.player, (float)(this.endWhale.getMaxHeadYRot() + 20), (float)this.endWhale.getMaxHeadXRot());
            if (this.endWhale.distanceToSqr(this.player) < 6.25D) {
                this.endWhale.getNavigation().stop();
            } else {
                this.endWhale.getNavigation().moveTo(this.player, this.speedModifier);
            }
        }
    }
}
