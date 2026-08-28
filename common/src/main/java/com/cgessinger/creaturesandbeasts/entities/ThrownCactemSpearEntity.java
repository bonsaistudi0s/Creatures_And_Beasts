package com.cgessinger.creaturesandbeasts.entities;

import com.cgessinger.creaturesandbeasts.modules.CNBEntityModule;
import com.cgessinger.creaturesandbeasts.modules.CNBItemModule;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class ThrownCactemSpearEntity extends AbstractArrow {
    private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(ThrownCactemSpearEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> IS_FOIL = SynchedEntityData.defineId(ThrownCactemSpearEntity.class, EntityDataSerializers.BOOLEAN);
    private boolean dealtDamage;
    public int clientSideReturnSpearTickCount;

    public ThrownCactemSpearEntity(EntityType<? extends ThrownCactemSpearEntity> entityType, Level level) {
        super(entityType, level);
    }

    public ThrownCactemSpearEntity(Level level, LivingEntity entity, ItemStack itemStack) {
        super(CNBEntityModule.THROWN_CACTEM_SPEAR.get(), entity, level, itemStack, null);
        this.entityData.set(IS_FOIL, itemStack.hasFoil());
        this.entityData.set(ID_LOYALTY, this.getLoyaltyFromItem(itemStack));
    }

    public ThrownCactemSpearEntity(Level level, double x, double y, double z, ItemStack pickupItemStack) {
        super(CNBEntityModule.THROWN_CACTEM_SPEAR.get(), x, y, z, level, pickupItemStack, null);
        this.entityData.set(ID_LOYALTY, this.getLoyaltyFromItem(pickupItemStack));
        this.entityData.set(IS_FOIL, pickupItemStack.hasFoil());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
       builder.define(ID_LOYALTY, (byte) 0);
       builder.define(IS_FOIL, false);
    }


    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        this.dealtDamage = tag.getBoolean("DealtDamage");
        this.entityData.set(ID_LOYALTY, this.getLoyaltyFromItem(this.getPickupItem()));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("DealtDamage", this.dealtDamage);
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        Entity entity = this.getOwner();
        int i = this.entityData.get(ID_LOYALTY);
        if (i > 0 && (this.dealtDamage || this.isNoPhysics()) && entity != null) {
            if (!this.isAcceptibleReturnOwner()) {
                if (!this.level().isClientSide && this.pickup == AbstractArrow.Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }

                this.discard();
            } else {
                this.setNoPhysics(true);
                Vec3 vec3 = entity.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.015D * (double)i, this.getZ());
                if (this.level().isClientSide) {
                    this.yOld = this.getY();
                }

                double d0 = 0.05D * (double)i;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vec3.normalize().scale(d0)));
                if (this.clientSideReturnSpearTickCount == 0) {
                    this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                }

                ++this.clientSideReturnSpearTickCount;
            }
        }
        super.tick();
    }

    public boolean isFoil() {
        return this.entityData.get(IS_FOIL);
    }

    @Nullable
    protected EntityHitResult findHitEntity(Vec3 vec1, Vec3 vec2) {
        return this.dealtDamage ? null : super.findHitEntity(vec1, vec2);
    }

    private boolean isAcceptibleReturnOwner() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayer) || !entity.isSpectator();
        } else {
            return false;
        }
    }

    private byte getLoyaltyFromItem(ItemStack stack) {
        return this.level() instanceof ServerLevel serverlevel
                ? (byte) Mth.clamp(EnchantmentHelper.getTridentReturnToOwnerAcceleration(serverlevel, stack, this), 0, 127)
                : 0;
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        Entity hitEntity = hitResult.getEntity();
        float f = 5.0F;
        Entity projectileThrower = this.getOwner();
        DamageSource damagesource = this.level().damageSources().thrown(this, (projectileThrower == null ? this : projectileThrower));

        if (this.level() instanceof ServerLevel serverLevel) {
            f = EnchantmentHelper.modifyDamage(serverLevel, this.getWeaponItem(), hitEntity, damagesource, f);
        }

        this.dealtDamage = true;
        if (hitEntity.hurt(damagesource, f)) {
            if (hitEntity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (this.level() instanceof ServerLevel serverLevel) {
                EnchantmentHelper.doPostAttackEffectsWithItemSource(serverLevel, hitEntity, damagesource, this.getWeaponItem());
            }

            if (hitEntity instanceof LivingEntity hitLivingEntity) {
                this.doKnockback(hitLivingEntity, damagesource);
                this.doPostHurtEffects(hitLivingEntity);
            }
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
        this.playSound(SoundEvents.TRIDENT_HIT, 1.0f, 1.0F);
    }

    @Override
    protected boolean tryPickup(Player player) {
        return super.tryPickup(player) || this.isNoPhysics() && this.ownedBy(player) && player.getInventory().add(this.getPickupItem());
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(CNBItemModule.CACTEM_SPEAR.get());
    }

    @Override
    public ItemStack getWeaponItem() {
        return this.getPickupItemStackOrigin();
    }

    @Override
    protected void doKnockback(LivingEntity entity, DamageSource damageSource) {
        if (this.level() instanceof ServerLevel serverLevel) {
            double knockback = EnchantmentHelper.modifyKnockback(serverLevel, this.getWeaponItem(), entity, damageSource, 0.0F);
            if (knockback > 0.0) {
                double d1 = Math.max(0.0, 1.0 - entity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                Vec3 vec3 = this.getDeltaMovement().multiply(1.0, 0.0, 1.0).normalize().scale(knockback * 0.6 * d1);
                if (vec3.lengthSqr() > 0.0) {
                    entity.push(vec3.x, 0.1, vec3.z);
                }
            }
        }
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    @Override
    public void playerTouch(Player player) {
        if (this.ownedBy(player) || this.getOwner() == null) {
            super.playerTouch(player);
        }
    }

    @Override
    public void tickDespawn() {
        int i = this.entityData.get(ID_LOYALTY);
        if (this.pickup != AbstractArrow.Pickup.ALLOWED || i <= 0) {
            super.tickDespawn();
        }
    }

    @Override
    public boolean shouldRender(double p_37588_, double p_37589_, double p_37590_) {
        return true;
    }
}
