package com.cgessinger.creaturesandbeasts.entities;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeastsConstants;
import com.cgessinger.creaturesandbeasts.menus.CinderFurnaceMenu;
import com.cgessinger.creaturesandbeasts.modules.CNBBlockModule;
import com.cgessinger.creaturesandbeasts.modules.CNBEntityModule;
import com.cgessinger.creaturesandbeasts.modules.CNBItemModule;
import com.cgessinger.creaturesandbeasts.modules.CNBSoundModule;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.keyframe.event.SoundKeyframeEvent;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.cgessinger.creaturesandbeasts.modules.CNBItemTagModule.Items.CINDERSHELL_FOOD;

public class CindershellEntity extends Animal implements GeoEntity, Bucketable, ContainerListener, Container, RecipeCraftingHolder, StackedContentsCompatible, MenuProvider {
    private static final EntityDataAccessor<Boolean> EATING = SynchedEntityData.defineId(CindershellEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(CindershellEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> FURNACE = SynchedEntityData.defineId(CindershellEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Optional<UUID>> PLAYER = SynchedEntityData.defineId(CindershellEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    private final ResourceLocation healthReductionLocation = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "cindershell_health_reduction");
    private final AnimatableInstanceCache animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);
    private static final RawAnimation WALK = RawAnimation.begin().thenPlay("cindershell_walk");
    private static final RawAnimation BABY_WALK = RawAnimation.begin().thenPlay("baby_cindershell_walk");
    private static final RawAnimation IDLE_EAT = RawAnimation.begin().thenPlay("cindershell_idle_eat");
    private static final RawAnimation DEATH = RawAnimation.begin().thenPlay("cindershell_death");
    private static final RawAnimation IDLE = RawAnimation.begin().thenPlay("cindershell_idle");
    private static final RawAnimation EAT = RawAnimation.begin().thenPlay("cindershell_eat");

    protected CinderFurnaceMenu inventory;
    private Player playerInMenu;
    private int eatTimer;

    int cookingProgress;
    int cookingTotalTime;
    protected NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);
    protected final ContainerData dataAccess = new ContainerData() {
        public int get(int index) {
            switch(index) {
                case 0:
                    return CindershellEntity.this.cookingProgress;
                case 1:
                    return CindershellEntity.this.cookingTotalTime;
                default:
                    return 0;
            }
        }

        public void set(int index, int value) {
            switch(index) {
                case 0:
                    CindershellEntity.this.cookingProgress = value;
                    break;
                case 1:
                    CindershellEntity.this.cookingTotalTime = value;
            }

        }

        public int getCount() {
            return 2;
        }
    };
    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();

    public CindershellEntity(EntityType<CindershellEntity> type, Level worldIn) {
        super(type, worldIn);
        this.eatTimer = 0;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 80.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.1D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 100D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(EATING, false);
        builder.define(FROM_BUCKET, false);
        builder.define(FURNACE, false);
        builder.define(PLAYER, Optional.empty());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("FromBucket", this.fromBucket());
        tag.putBoolean("HasFurnace", this.hasFurnace());
        if (this.hasFurnace()) {
            ContainerHelper.saveAllItems(tag, this.items, this.registryAccess());

            if (this.entityData.get(PLAYER).isPresent()) {
                tag.putUUID("Player", this.entityData.get(PLAYER).get());
            }
            tag.putInt("CookTime", this.cookingProgress);
            tag.putInt("CookTimeTotal", this.cookingTotalTime);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        this.setFromBucket(tag.getBoolean("FromBucket"));
        UUID playerUUID = null;
        if (tag.contains("Player")) {
            playerUUID = tag.getUUID("Player");
        }
        this.setFurnace(tag.getBoolean("HasFurnace"), playerUUID);
        if (this.hasFurnace()) {
            if (tag.contains("Player") && this.level().getPlayerByUUID(tag.getUUID("Player")) != null) {
                this.inventory = this.createMenu(this.getId(), this.level().getPlayerByUUID(tag.getUUID("Player")).getInventory(), this.level().getPlayerByUUID(tag.getUUID("Player")));
            } else  {
                this.inventory = this.createMenu(this.getId(), new Inventory(null), null);
            }
            ContainerHelper.loadAllItems(tag, this.items, this.registryAccess());

            this.cookingProgress = tag.getInt("CookTime");
            this.cookingTotalTime = tag.getInt("CookTimeTotal");
        }

        super.readAdditionalSaveData(tag);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new CindershellFloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(2, new CindershellBreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.0D, Ingredient.of(CINDERSHELL_FOOD), false));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.getEating()) {
            this.navigation.stop();
            this.eatTimer--;
        }

        if (this.eatTimer == 10) {
            this.setHolding(ItemStack.EMPTY);
        } else if (this.eatTimer == 0) {
            this.setEating(false);
        }
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return 10.0F;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.hasFurnace() && this.random.nextDouble() <= 0.25) {
            this.level().addParticle(ParticleTypes.LARGE_SMOKE, this.getX() + (this.random.nextDouble() * 0.5D - 0.25), this.getY() + 2.5 + (this.random.nextDouble() * 0.1D - 0.05), this.getZ() + (this.random.nextDouble() * 0.5D - 0.25), this.getDeltaMovement().x, 0, this.getDeltaMovement().z);
        }

        if (!this.level().isClientSide && this.hasFurnace()) {
            if (this.inventory.getSlot(0).hasItem()) {
                RecipeHolder<?> recipe = this.level().getRecipeManager().getRecipeFor(this.inventory.getRecipeType(), new SingleRecipeInput(this.inventory.getSlot(0).getItem()), this.level()).orElse(null);

                if (this.canBurn(this.registryAccess(), recipe, this.inventory.getItems(), 64)) {
                    if (this.random.nextDouble() < 0.1D) {
                        this.playSound(SoundEvents.FURNACE_FIRE_CRACKLE, 1.0F, 1.0F);
                    }
                    ++this.cookingProgress;
                    if (this.cookingProgress >= this.cookingTotalTime) {
                        this.cookingProgress = 0;
                        this.cookingTotalTime = getTotalCookTime(this.level(), this.inventory.getRecipeType(), this);
                        if (this.burn(this.registryAccess(), recipe, this.items, 64)) {
                            this.setRecipeUsed(recipe);
                        }
                    }
                } else {
                    this.cookingProgress = 0;
                }
            }
        }
    }

    @Override
    protected void handlePortal() {
        if (!this.hasFurnace()) {
            super.handlePortal();
        }
    }

    @Override
    public @Nullable Entity changeDimension(DimensionTransition transition) {
        if (this.playerInMenu != null) {
            if (this.playerInMenu instanceof ServerPlayer serverPlayer) {
                serverPlayer.closeContainer();
            } else if (this.playerInMenu instanceof LocalPlayer localPlayer) {
                localPlayer.closeContainer();
            }
        }

        if (this.hasFurnace()) {
            this.cookingTotalTime = getTotalCookTime(transition.newLevel(), this.inventory.getRecipeType(), this);
        }

        return super.changeDimension(transition);
    }

    @Override
    public boolean isSensitiveToWater() {
        return true;
    }

    public static boolean checkCindershellSpawnRules(EntityType<CindershellEntity> entity, LevelAccessor level, MobSpawnType mobSpawnType, BlockPos pos, RandomSource random) {
        return pos.getY() <= 50;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return Ingredient.of(CINDERSHELL_FOOD).test(stack);
    }

    public InteractionResult tryStartEat(Player player, ItemStack stack) {
        if (stack.is(CINDERSHELL_FOOD)) {
            int i = this.getAge();
            if (!this.level().isClientSide && i == 0 && this.canFallInLove()) {
                this.usePlayerItem(player, player.getUsedItemHand(), stack);
                this.setEating(true);
                this.setInLove(player);
                this.playSound(CNBSoundModule.CINDERSHELL_ADULT_EAT.get(), 1.2F, 1F);
                this.setHolding(stack);
                return InteractionResult.SUCCESS;
            }

            if (this.isBaby()) {
                this.playSound(CNBSoundModule.CINDERSHELL_BABY_EAT.get(), 1.3F, 1F);
                this.usePlayerItem(player, player.getUsedItemHand(), stack);
                this.ageUp((int) (-i / 20F * 0.1F), true);
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }

            if (this.level().isClientSide) {
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand);

        if (item.is(Items.LAVA_BUCKET) && this.isAlive() && this.isBaby()) {
            this.playSound(this.getPickupSound(), 1.0F, 1.0F);
            ItemStack bucketItem = this.getBucketItemStack();
            this.saveToBucketTag(bucketItem);
            ItemStack bucketWithData = ItemUtils.createFilledResult(item, player, bucketItem, false);
            player.setItemInHand(hand, bucketWithData);
            Level level = this.level();

            if (!level.isClientSide) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)player, bucketItem);
            }

            this.discard();
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else if (!this.isBaby() && !this.hasFurnace() && item.is(CNBItemModule.CINDERSHELL_FURNACE.get())) {
            this.setFurnace(true, player.getUUID());

            this.inventory = this.createMenu(this.getId(), player.getInventory(), player);

            if (!player.getAbilities().instabuild) {
                item.shrink(1);
            }

            this.playSound(SoundEvents.HORSE_SADDLE, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else if (this.isFood(item) && !this.getEating()) {
            return this.tryStartEat(player, item);
        } else if (this.hasFurnace() && player.isSecondaryUseActive()) {
            this.dropEquipment();
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else if (this.hasFurnace()) {
            if (!this.level().isClientSide) {
                player.openMenu(this);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }

    public CinderFurnaceMenu createMenu(int id, Inventory playerInventory, Player player) {
        this.playerInMenu = player;
        return new CinderFurnaceMenu(id, playerInventory, this, this.dataAccess);
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        if (this.hasFurnace()) {
            this.playSound(SoundEvents.HORSE_SADDLE, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.8F);

            if (!this.level().isClientSide) {
                this.spawnAtLocation(CNBBlockModule.CINDER_FURNACE.get());
                for (int i = 0; i < this.inventory.getSize(); i++) {
                    this.spawnAtLocation(this.inventory.getSlot(i).getItem());
                }
                ((CinderFurnaceMenu.CinderFurnaceResultSlot)this.inventory.getSlot(1)).checkTakeAchievements(this.inventory.getSlot(1).getItem());
                this.clearContent();
            }

            this.setFurnace(false, null);
        }
    }

    @Override
    public int getContainerSize() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return ContainerHelper.removeItem(this.items, slot, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        ItemStack itemstack = this.getItem(slot);
        boolean flag = !stack.isEmpty() && stack.is(itemstack.getItem()) && ItemStack.isSameItemSameComponents(stack, itemstack);
        this.items.set(slot, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }

        if (slot == 0 && !flag) {
            this.dataAccess.set(1, getTotalCookTime(this.level(), this.inventory.getRecipeType(), this));
            this.dataAccess.set(0, 0);
            this.setChanged();
        }

    }

    @Override
    public void setChanged() {
    }

    @Override
    public void clearContent() {
        this.inventory.clearCraftingContent();
    }

    public boolean stillValid(Player player) {
        return true;
    }

    public static int getTotalCookTime(Level level, RecipeType<?> recipeType, CindershellEntity container) {
        ResourceKey<Level> dimensionKey = ResourceKey.create(Registries.DIMENSION, container.level().dimension().location());
        float cookTimeMultiplier = dimensionKey.equals(Level.NETHER) ? 1.0F : 1.667F;
        return (int) (level.getRecipeManager().getRecipeFor(container.inventory.getRecipeType(), new SingleRecipeInput(container.inventory.getSlot(0).getItem()), level).map(recipeHolder -> recipeHolder.value().getCookingTime()).orElse(200) * cookTimeMultiplier);
    }

    private boolean burn(RegistryAccess registryAccess, @Nullable RecipeHolder<?> recipe, NonNullList<ItemStack> inventory, int maxStackSize) {
        if (recipe != null && this.canBurn(registryAccess, recipe, inventory, maxStackSize)) {
            ItemStack itemstack = inventory.get(0);
            ItemStack itemstack1 = recipe.value().getResultItem(registryAccess);
            ItemStack itemstack2 = inventory.get(1);
            if (itemstack2.isEmpty()) {
                inventory.set(1, itemstack1.copy());
            } else if (itemstack2.is(itemstack1.getItem())) {
                itemstack2.grow(itemstack1.getCount());
            }

            itemstack.shrink(1);
            return true;
        } else {
            return false;
        }
    }

    private boolean canBurn(RegistryAccess registryAccess, @Nullable RecipeHolder<?> recipe, NonNullList<ItemStack> inventory, int maxStackSize) {
        if (!items.get(0).isEmpty() && recipe != null) {
            ItemStack itemstack = recipe.value().getResultItem(registryAccess);
            if (itemstack.isEmpty()) {
                return false;
            } else {
                ItemStack itemstack1 = items.get(1);
                if (itemstack1.isEmpty()) {
                    return true;
                } else if (!itemstack1.is(itemstack.getItem())) {
                    return false;
                } else if (itemstack1.getCount() + itemstack.getCount() <= maxStackSize && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize()) {
                    return true;
                } else {
                    return itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize();
                }
            }
        } else {
            return false;
        }
    }

    @Override
    public void setRecipeUsed(@Nullable RecipeHolder<?> recipe) {
        if (recipe != null) {
            ResourceLocation resourcelocation = recipe.id();
            this.recipesUsed.addTo(resourcelocation, 1);
        }
    }

    @Nullable
    @Override
    public RecipeHolder<?> getRecipeUsed() {
        return null;
    }

    public void awardUsedRecipesAndPopExperience(ServerPlayer player) {
        List<RecipeHolder<?>> list = this.getRecipesToAwardAndPopExperience(player.serverLevel(), player.position());
        player.awardRecipes(list);
        this.recipesUsed.clear();
    }

    public List<RecipeHolder<?>> getRecipesToAwardAndPopExperience(ServerLevel level, Vec3 vec3) {
        List<RecipeHolder<?>> list = Lists.newArrayList();

        for(Object2IntMap.Entry<ResourceLocation> entry : this.recipesUsed.object2IntEntrySet()) {
            level.getRecipeManager().byKey(entry.getKey()).ifPresent((recipe) -> {
                list.add(recipe);
                createExperience(level, vec3, entry.getIntValue(), ((AbstractCookingRecipe)recipe.value()).getExperience());
            });
        }

        return list;
    }

    private static void createExperience(ServerLevel level, Vec3 vec3, int value, float experience) {
        int i = Mth.floor((float)value * experience);
        float f = Mth.frac((float)value * experience);
        if (f != 0.0F && Math.random() < (double)f) {
            ++i;
        }

        ExperienceOrb.award(level, vec3, i);
    }

    @Override
    public void fillStackedContents(StackedContents stackedContents) {
        for(ItemStack itemstack : this.items) {
            stackedContents.accountStack(itemstack);
        }
    }

    @Override
    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
        this.entityData.set(FROM_BUCKET, fromBucket);
    }

    @Override
    public void saveToBucketTag(ItemStack stack) {
        Bucketable.saveDefaultDataToBucketTag(this, stack);

        stack.update(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY, comp -> comp.update(itemTag -> itemTag.putInt("Age", this.getAge())));
    }

    @Override
    public void loadFromBucketTag(CompoundTag compound) {
        Bucketable.loadDefaultDataFromBucketTag(this, compound);

        if (compound.contains("Age")) {
            this.setAge(compound.getInt("Age"));
        } else {
            this.setAge(-24000);
        }
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(CNBItemModule.CINDERSHELL_BUCKET.get());
    }

    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_LAVA;
    }

    @Override
    public void containerChanged(Container container) {

    }

    @Override
    public void setAge(int age) {
        super.setAge(age);
        double MAX_HEALTH = this.getAttribute(Attributes.MAX_HEALTH).getValue();
        float babyHealth = 10.0F;
        if (isBaby() && MAX_HEALTH > babyHealth) {
            Multimap<Holder<Attribute>, AttributeModifier> multimap = HashMultimap.create();
            multimap.put(Attributes.MAX_HEALTH, new AttributeModifier(this.healthReductionLocation, babyHealth - MAX_HEALTH, AttributeModifier.Operation.ADD_VALUE));
            this.getAttributes().addTransientAttributeModifiers(multimap);
            this.setHealth(babyHealth);
        }
    }

    @Override
    protected void ageBoundaryReached() {
        super.ageBoundaryReached();
        this.getAttribute(Attributes.MAX_HEALTH).removeModifier(this.healthReductionLocation);
        this.setHealth((float) this.getAttribute(Attributes.MAX_HEALTH).getValue());
    }

    @Override
    protected void tickDeath() {
        ++this.deathTime;
        if (this.deathTime == 23 && !this.level().isClientSide()) {
            this.level().broadcastEntityEvent(this, (byte)60);
            this.remove(Entity.RemovalReason.KILLED);
        }
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose pose) {
        return pose == Pose.SLEEPING ? SLEEPING_DIMENSIONS : super.getDimensions(pose).scale(this.getScale(), this.getHeightScale());
    }

    private float getHeightScale() {
        return this.isBaby() ? 0.35F : 1.0F;
    }

    @Override
    public float getScale() {
        return this.isBaby() ? 0.55F : 1.0F;
    }

    public ItemStack getHolding() {
        return this.getItemBySlot(EquipmentSlot.MAINHAND);
    }

    public void setHolding(ItemStack stack) {
        this.setItemSlot(EquipmentSlot.MAINHAND, stack);
    }

    public void setEating(boolean isEating) {
        this.eatTimer = isEating ? 40 : 0;
        this.entityData.set(EATING, isEating);
    }

    public boolean getEating() {
        return this.entityData.get(EATING);
    }

    public boolean hasFurnace() {
        return this.entityData.get(FURNACE);
    }

    public void setFurnace(boolean hasFurnace, @Nullable UUID playerUUID) {
        this.entityData.set(FURNACE, hasFurnace);
        if (playerUUID != null) {
            this.entityData.set(PLAYER, Optional.of(playerUUID));
        } else {
            this.entityData.set(PLAYER, Optional.empty());
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return CNBEntityModule.CINDERSHELL.get().create(level);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return CNBSoundModule.CINDERSHELL_AMBIENT.get();
    }

    @Override
    public int getAmbientSoundInterval() {
        return 120;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return CNBSoundModule.CINDERSHELL_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return CNBSoundModule.CINDERSHELL_HURT.get();
    }

    @Override
    protected float getSoundVolume() {
        return super.getSoundVolume() * 2;
    }

    @Override
    public int getMaxHeadYRot() {
        return 50;
    }

    @Override
    public int getMaxHeadXRot() {
        return 25;
    }

    private PlayState animationPredicate(AnimationState<CindershellEntity> state) {
        if (state.isMoving()) {
            state.getController().setAnimation(this.isBaby() ? BABY_WALK : WALK);
        } else if (this.getEating()) {
            state.getController().setAnimation(IDLE_EAT);
        } else if (this.isDeadOrDying()) {
            state.getController().setAnimation(DEATH);
        } else {
            state.getController().setAnimation(IDLE);
        }
        return PlayState.CONTINUE;
    }

    private PlayState eatAnimationPredicate(AnimationState<CindershellEntity> state) {
        if (this.getEating()) {
            state.getController().setAnimation(EAT);
            return PlayState.CONTINUE;
        }
        state.getController().forceAnimationReset();
        return PlayState.STOP;
    }

    private void soundListener(SoundKeyframeEvent<CindershellEntity> event) {
        LocalPlayer player = Minecraft.getInstance().player;
        player.playSound(this.isBaby() ? CNBSoundModule.CINDERSHELL_BABY_EAT.get() : CNBSoundModule.CINDERSHELL_ADULT_EAT.get(), 0.4F, 1F);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        AnimationController<CindershellEntity> controller = new AnimationController<>(this, "controller", 0, this::animationPredicate);
        AnimationController<CindershellEntity> eatController = new AnimationController<>(this, "eatController", 0, this::eatAnimationPredicate);

        eatController.setSoundKeyframeHandler(this::soundListener);

        controllerRegistrar.add(controller);
        controllerRegistrar.add(eatController);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animatableInstanceCache;
    }

    static class CindershellFloatGoal extends FloatGoal {
        private final CindershellEntity cindershell;

        public CindershellFloatGoal(CindershellEntity cindershell) {
            super(cindershell);
            this.cindershell = cindershell;
        }

        @Override
        public boolean canUse() {
            return this.cindershell.isInLava();
        }
    }

    static class CindershellBreedGoal extends BreedGoal {

        public CindershellBreedGoal(Animal cindershell, double speedModifier) {
            super(cindershell, speedModifier);
        }

        @Override
        protected void breed() {
            int range = this.animal.getRandom().nextInt(4) + 3;
            for (int i = 0; i <= range; i++) {
                super.breed();
            }
        }
    }
}
