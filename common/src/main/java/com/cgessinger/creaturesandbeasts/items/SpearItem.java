package com.cgessinger.creaturesandbeasts.items;

import com.cgessinger.creaturesandbeasts.entities.ThrownCactemSpearEntity;
import com.cgessinger.creaturesandbeasts.modules.CNBSoundModule;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class SpearItem extends Item implements ProjectileItem {
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public SpearItem(Properties properties) {
        super(properties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        this.defaultModifiers = builder.build();
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return !player.isCreative();
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int useTicks) {
        if (entity instanceof Player) {
            Player player = (Player)entity;
            int i = this.getUseDuration(stack, entity) - useTicks;
            if (i >= 10 && !level.isClientSide) {
                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(entity.getUsedItemHand()));

                spawnSpears(stack, player, level);

                if (!player.getAbilities().instabuild) {
                    player.getInventory().removeItem(stack);
                }
            }

            player.awardStat(Stats.ITEM_USED.get(this));
        }
    }


    private void spawnSpears(ItemStack stack, Player player, Level level) {
        int numberOfSpears = level instanceof ServerLevel serverLevel ? EnchantmentHelper.processProjectileCount(serverLevel, stack, player, 1) : 1;
        float[] afloat = getShotPitches(player.getRandom());

        ItemStack noLoyaltyStack = stack.copy();

        ItemEnchantments.Mutable enchantments = new ItemEnchantments.Mutable(noLoyaltyStack.getEnchantments());
        enchantments.removeIf(enchantmentHolder -> enchantmentHolder.is(Enchantments.LOYALTY));
        noLoyaltyStack.update(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY, itemEnchantments -> enchantments.toImmutable());

        if (level instanceof ServerLevel serverLevel) {
            float spread = EnchantmentHelper.processProjectileSpread(serverLevel, stack, player, 0.0F);
            float f1 = numberOfSpears == 1 ? 0.0F : 2.0F * spread / (float)(numberOfSpears - 1);
            float f2 = (float)((numberOfSpears - 1) % 2) * f1 / 2.0F;

            for (int i = 0; i < numberOfSpears; i++) {
                float angle = f2 + (float)((i + 1) / 2) * f1;

                if (i == 0) {
                    shootProjectile(level, player, stack, afloat[i], angle, 0.0F, true);
                } else if (i == 1) {
                    shootProjectile(level, player, noLoyaltyStack, afloat[i], angle, -10.0F, false);
                } else {
                    shootProjectile(level, player, noLoyaltyStack, afloat[i], angle, 10.0F, false);
                }
            }
        }
    }

    private void shootProjectile(Level level, Player player, ItemStack stack, float soundVariation, float angle, float randomization, boolean canPickup) {
        ThrownCactemSpearEntity thrownSpear = new ThrownCactemSpearEntity(level, player, stack);
        Vec3 vec31 = player.getUpVector(1.0F);
        Quaternionf quaternion = new Quaternionf().setAngleAxis((double)(angle * (float) (Math.PI / 180.0)), vec31.x, vec31.y, vec31.z);
        Vec3 viewVector = player.getViewVector(1.0F);
        Vector3f vector3f = viewVector.toVector3f().rotate(quaternion);
        thrownSpear.shoot(vector3f.x(), vector3f.y(), vector3f.z(), 1.6F, randomization);

        if (player.getAbilities().instabuild) {
            thrownSpear.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        } else {
            thrownSpear.pickup = canPickup ? AbstractArrow.Pickup.ALLOWED : AbstractArrow.Pickup.DISALLOWED;
        }

        level.addFreshEntity(thrownSpear);
        level.playSound(null, thrownSpear, CNBSoundModule.SPEAR_THROW.get(), SoundSource.PLAYERS, 1.0F, soundVariation);
    }

    private static float[] getShotPitches(RandomSource rand) {
        boolean flag = rand.nextBoolean();
        return new float[]{1.0F, getRandomShotPitch(flag, rand), getRandomShotPitch(!flag, rand)};
    }

    private static float getRandomShotPitch(boolean chance, RandomSource rand) {
        float f = chance ? 0.63F : 0.43F;
        return 1.0F / (rand.nextFloat() * 0.5F + 1.8F) + f;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.getDamageValue() >= itemstack.getMaxDamage() - 1) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity hurtEntity, LivingEntity owner) {
        return true;
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        if ((double)state.getDestroySpeed(level, pos) != 0.0D) {
            stack.hurtAndBreak(2, entity, LivingEntity.getSlotForHand(entity.getUsedItemHand()));
        }

        return true;
    }

    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(
                        Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 5.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND
                )
                .add(
                        Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -2.9F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND
                )
                .build();
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }

    @Override
    public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
        ThrownCactemSpearEntity thrownSpear = new ThrownCactemSpearEntity(level, pos.x(), pos.y(), pos.z(), stack.copyWithCount(1));
        thrownSpear.pickup = AbstractArrow.Pickup.ALLOWED;
        return thrownSpear;
    }
}
