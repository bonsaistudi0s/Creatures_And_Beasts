package com.cgessinger.creaturesandbeasts.entities;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeastsConstants;
import com.cgessinger.creaturesandbeasts.mixin.accessor.MeleeAttackGoalAccessor;
import com.cgessinger.creaturesandbeasts.modules.CNBEntityModule;
import com.cgessinger.creaturesandbeasts.modules.CNBSoundModule;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.keyframe.event.ParticleKeyframeEvent;
import software.bernie.geckolib.animation.keyframe.event.SoundKeyframeEvent;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class YetiEntity extends TamableAnimal implements GeoEntity, Enemy, NeutralMob {
    public static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(YetiEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> EATING = SynchedEntityData.defineId(YetiEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> PASSIVE = SynchedEntityData.defineId(YetiEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<ItemStack> HELD_ITEM = SynchedEntityData.defineId(YetiEntity.class, EntityDataSerializers.ITEM_STACK);

    private final AnimatableInstanceCache animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);
    private static final RawAnimation BABY_EAT = RawAnimation.begin().thenPlay("yeti_baby_eat");
    private static final RawAnimation ADULT_EAT = RawAnimation.begin().thenPlay("yeti_adult_eat");
    private static final RawAnimation BABY_WALK = RawAnimation.begin().thenPlay("yeti_baby_walk");
    private static final RawAnimation ADULT_WALK = RawAnimation.begin().thenPlay("yeti_adult_walk");
    private static final RawAnimation ATTACK = RawAnimation.begin().thenPlay("yeti_attack");

    private final ResourceLocation healthReductionLocation = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "yeti_health_reduction");
    private final float babyHealth = 20.0F;

    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private int remainingPersistentAngerTime;
    @Nullable
    private UUID persistentAngerTarget;

    private int eatTimer;
    private int attackTimer;

    public YetiEntity(EntityType<YetiEntity> type, Level worldIn) {
        super(type, worldIn);
        this.setTame(false, false);
        this.eatTimer = 0;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ATTACKING, false);
        builder.define(EATING, false);
        builder.define(PASSIVE, false);
        builder.define(HELD_ITEM, ItemStack.EMPTY);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Passive", this.isPassive());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Passive")) {
            this.setPassive(compound.getBoolean("Passive"));
        }
    }


    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 120.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 16.0D)
                .add(Attributes.ATTACK_SPEED, 0.1D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.7D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new YetiAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(3, new FollowParentGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 12.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D, 0.01F));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(4, new TargetPlayerGoal(this));
        this.targetSelector.addGoal(5, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.isEating()) {
            this.navigation.stop();
            this.eatTimer--;
        }

        if (this.isAttacking()) {
            this.navigation.stop();
            this.attackTimer--;
        }

        if (this.eatTimer == 40) {
            if (this.isBaby()) {
                this.ageUp((int) (-this.getAge() / 20F * 0.1F), true);
            }
            if (this.getHolding().is(Items.MELON_SLICE)) {
                this.setTarget(null);
                this.setPassive(true);
            }
            this.setHolding(ItemStack.EMPTY);
        } else if (this.eatTimer == 0) {
            this.setEating(false);
        }

        if (this.attackTimer == 10 && !this.isDeadOrDying()) {
            this.performAttack();
        } else if (this.attackTimer == 0) {
            this.setAttacking(false);
        }
    }

    @Override
    public boolean canBeLeashed() {
        return false;
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.remainingPersistentAngerTime;
    }

    @Override
    public void setRemainingPersistentAngerTime(int angerTime) {
        this.remainingPersistentAngerTime = angerTime;
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID uuid) {
        this.persistentAngerTarget = uuid;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return 10.0F;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, SpawnGroupData spawnDataIn) {
        if (spawnDataIn == null) {
            spawnDataIn = new AgeableMobGroupData(1.0F);
        }

        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand);

        if (!(this.isEating() || this.isAttacking())) {
            if (!this.level().isClientSide && item.getItem() == Items.MELON_SLICE && !this.isPassive()) {
                this.setOwnerUUID(player.getUUID());
                return this.startEat(player, item.copy());
            } else if (item.getItem() == Items.SWEET_BERRIES) {
                if (!this.level().isClientSide && this.getAge() == 0 && this.canFallInLove()) {
                    this.setInLove(player);
                    return this.startEat(player, item.copy());
                } else if (this.isBaby()) {
                    return this.startEat(player, item.copy());
                }
            }
        }

        if (this.level().isClientSide) {
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    /*
     * If a Yeti is a baby, apply the max health reduction to the yeti and set its health to the new max
     */
    @Override
    public void setAge(int age) {
        super.setAge(age);
        double MAX_HEALTH = this.getAttribute(Attributes.MAX_HEALTH).getValue();
        if (isBaby() && MAX_HEALTH > this.babyHealth) {
            Multimap<Holder<Attribute>, AttributeModifier> multimap = HashMultimap.create();
            multimap.put(Attributes.MAX_HEALTH, new AttributeModifier(this.healthReductionLocation, this.babyHealth - MAX_HEALTH, AttributeModifier.Operation.ADD_VALUE));
            this.getAttributes().addTransientAttributeModifiers(multimap);
            this.setHealth(this.babyHealth);
        }
    }

    /*
     * When a Yeti baby grows up, remove the max health debuff, maintain the same percentage of max health
     */
    @Override
    protected void ageBoundaryReached() {
        super.ageBoundaryReached();
        float percentHealth = this.getHealth() / this.babyHealth;
        this.getAttribute(Attributes.MAX_HEALTH).removeModifier(this.healthReductionLocation);
        this.setHealth(percentHealth * (float) this.getAttribute(Attributes.MAX_HEALTH).getValue());
        this.setEating(false);
        this.setHolding(ItemStack.EMPTY);

        if (!this.level().isClientSide && this.isPassive() && this.getOwner() != null && this.getOwner() instanceof ServerPlayer player) {
            this.tame(player);
            this.setPassive(false);
            this.navigation.stop();
            this.setTarget(null);
            this.level().broadcastEntityEvent(this, (byte)7);
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return CNBEntityModule.YETI.get().create(level);
    }

    public void setEating(boolean isEating) {
        this.eatTimer = isEating ? 60 : 0;
        this.entityData.set(EATING, isEating);
    }

    public boolean isEating() {
        return this.entityData.get(EATING);
    }

    public void setAttacking(boolean isAttacking) {
        this.entityData.set(ATTACKING, isAttacking);
        this.attackTimer = isAttacking ? 24 : 0;
    }

    public boolean isAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public boolean isPassive() {
        return this.entityData.get(PASSIVE);
    }

    public void setPassive(boolean isPassive) {
        this.entityData.set(PASSIVE, isPassive);
    }

    public ItemStack getHolding() {
        return this.entityData.get(HELD_ITEM);
    }

    public void setHolding(ItemStack stack) {
        this.entityData.set(HELD_ITEM, stack);
    }

    private InteractionResult startEat(Player player, ItemStack stack) {
        this.setHolding(stack);
        this.usePlayerItem(player, player.getUsedItemHand(), stack);
        this.setEating(true);
        this.gameEvent(GameEvent.ENTITY_INTERACT, player);
        SoundEvent sound = this.isBaby() ? CNBSoundModule.YETI_BABY_EAT.get() : CNBSoundModule.YETI_ADULT_EAT.get();
        this.playSound(sound, 1.1F, 1F);
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isInSittingPose() {
        return false;
    }

    @Override
    public void setInSittingPose(boolean p_21838_) {
    }

    @Override
    public boolean isOrderedToSit() {
        return false;
    }

    @Override
    public void setOrderedToSit(boolean p_21840_) {
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.isTame() && !this.hasCustomName();
    }

    private void performAttack() {
        List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(1.5D, 1.0D, 1.5D));

        for (LivingEntity entity : list) {
            if ((entity instanceof Player && entity.getUUID().equals(this.getOwnerUUID())) || (entity instanceof YetiEntity && Objects.equals(this.getOwnerUUID(), ((YetiEntity) entity).getOwnerUUID()))) {
                continue;
            }
            this.doHurtTarget(entity);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isBaby()) {
            List<YetiEntity> list = this.level().getEntitiesOfClass(YetiEntity.class, this.getBoundingBox().inflate(8.0D, 4.0D, 8.0D));

            for (YetiEntity yeti : list) {
                if (!yeti.isBaby() && !yeti.isTame()) {
                    yeti.setPassive(false);
                    yeti.setOwnerUUID(null);
                }
            }
        }

        if (!this.isTame()) {
            this.setPassive(false);
            this.setOwnerUUID(null);
        }
        return super.hurt(source, amount);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        if (blockIn.getFluidState().isEmpty()) {
            this.playSound(CNBSoundModule.YETI_STEP.get(), this.getSoundVolume() * 0.3F, this.getVoicePitch());
        }
    }

    @Override
    public float getVoicePitch() {
        float pitch = super.getVoicePitch();
        return this.isBaby() ? pitch * 1.5F : pitch;
    }

    @Override
    public int getMaxHeadYRot() {
        return 50;
    }

    @Override
    public int getMaxHeadXRot() {
        return 25;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isBaby() ? null : CNBSoundModule.YETI_AMBIENT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.isBaby() ? null : CNBSoundModule.YETI_HURT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return this.isBaby() ? null : CNBSoundModule.YETI_HURT.get();
    }

    private PlayState animationPredicate(AnimationState<YetiEntity> state) {
        if (this.isEating()) {
            state.getController().setAnimation(this.isBaby() ? BABY_EAT : ADULT_EAT);
        } else if (this.isAttacking()) {
            state.getController().setAnimation(ATTACK);
        } else if (state.isMoving()) {
            state.getController().setAnimation(this.isBaby() ? BABY_WALK : ADULT_WALK);
        } else {
            state.getController().forceAnimationReset();
            return PlayState.STOP;
        }

        return PlayState.CONTINUE;
    }

    private void soundListener(SoundKeyframeEvent<YetiEntity> event) {
        if (event.getKeyframeData().getSound().equals("hit.ground.sound")) {
            this.playSound(CNBSoundModule.YETI_HIT.get(), 0.4F, 1.0F);
        } else if (event.getKeyframeData().getSound().equals("yeti_ambient")) {
            this.playSound(CNBSoundModule.YETI_AMBIENT.get(), 1.0F, 1.0F);
        }
    }

    private void particleListener(ParticleKeyframeEvent<YetiEntity> event) {
        ParticleEngine manager = Minecraft.getInstance().particleEngine;
        BlockPos pos = this.blockPosition();

        if (event.getKeyframeData().getEffect().equals("hit.ground.particle")) {
            for (int x = pos.getX() - 1; x <= pos.getX() + 1; x++) {
                for (int z = pos.getZ() - 1; z <= pos.getZ() + 1; z++) {
                    BlockPos newPos = new BlockPos(x, pos.getY() - 1, z);
                    manager.destroy(newPos, this.level().getBlockState(newPos));
                }
            }
        } else if (event.getKeyframeData().getEffect().equals("eat.particle")) {
            spawnParticles(ParticleTypes.HAPPY_VILLAGER);
        }
    }

    public void spawnParticles(ParticleOptions data) {
        for (int i = 0; i < 7; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level().addParticle(data, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        AnimationController<YetiEntity> controller = new AnimationController<>(this, "controller", 0, this::animationPredicate);

        controller.setSoundKeyframeHandler(this::soundListener);
        controller.setParticleKeyframeHandler(this::particleListener);

        controllerRegistrar.add(controller);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animatableInstanceCache;
    }

    static class TargetPlayerGoal extends NearestAttackableTargetGoal<Player> {
        private final YetiEntity yeti;

        public TargetPlayerGoal(YetiEntity yeti) {
            super(yeti, Player.class, 20, true, true, null);
            this.yeti = yeti;
        }

        @Override
        public boolean canUse() {
            if (!this.yeti.isBaby() && !this.yeti.isPassive() && super.canUse()) {
                for (YetiEntity yeti : yeti.level().getEntitiesOfClass(YetiEntity.class, yeti.getBoundingBox().inflate(8.0D, 4.0D, 8.0D))) {
                    if (yeti.isBaby()) {
                        return true;
                    }
                }

            }
            return false;
        }

        @Override
        protected double getFollowDistance() {
            return super.getFollowDistance() * 0.5D;
        }
    }

    static class YetiAttackGoal extends MeleeAttackGoal {
        private final YetiEntity yeti;

        public YetiAttackGoal(YetiEntity yeti, double speedModifier, boolean requiresLineOfSight) {
            super(yeti, speedModifier, requiresLineOfSight);
            this.yeti = yeti;
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && !this.yeti.isBaby();
        }

        @Override
        public boolean canUse() {
            if (this.yeti.getTarget() instanceof TamableAnimal tamableAnimal && this.yeti.isTame() && this.yeti.getOwner() != null && this.yeti.getOwner().equals(tamableAnimal.getOwner())) {
                return false;
            }
            return super.canUse() && !this.yeti.isBaby() && this.yeti.getTarget() != this.yeti.getOwner();
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity target) {
            if (this.canPerformAttack(target)) {
                this.resetAttackCooldown();
            }
        }

        @Override
        public void stop() {
            super.stop();
            this.yeti.setAttacking(false);
        }

        @Override
        protected void resetAttackCooldown() {
            ((MeleeAttackGoalAccessor) this).setTicksUntilNextAttack(this.adjustedTickDelay(25));
            this.yeti.setAttacking(true);
        }
    }
}
