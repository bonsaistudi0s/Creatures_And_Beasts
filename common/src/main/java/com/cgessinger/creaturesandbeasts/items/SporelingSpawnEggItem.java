package com.cgessinger.creaturesandbeasts.items;

import com.cgessinger.creaturesandbeasts.modules.CNBItemModule;
import com.cgessinger.creaturesandbeasts.modules.CNBSporelingTypeModule;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Spawner;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.Objects;

public class SporelingSpawnEggItem extends SpawnEggItem {

    public SporelingSpawnEggItem(EntityType<? extends Mob> entityTypeSupplier, final int primaryColor, final int secondaryColor, final Properties properties) {
        super(entityTypeSupplier, primaryColor, secondaryColor, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (!(level instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        } else {
            ItemStack itemstack = context.getItemInHand();
            BlockPos blockpos = context.getClickedPos();
            Direction direction = context.getClickedFace();
            BlockState blockstate = level.getBlockState(blockpos);
            if (blockstate.is(Blocks.SPAWNER)) {
                BlockEntity blockentity = level.getBlockEntity(blockpos);
                if (blockentity instanceof Spawner spawner) {
                    EntityType<?> entitytype1 = this.getType(itemstack);
                    spawner.setEntityId(entitytype1, level.getRandom());
                    blockentity.setChanged();
                    level.sendBlockUpdated(blockpos, blockstate, blockstate, 3);
                    itemstack.shrink(1);
                    return InteractionResult.CONSUME;
                }
            }

            BlockPos blockpos1;
            if (blockstate.getCollisionShape(level, blockpos).isEmpty()) {
                blockpos1 = blockpos;
            } else {
                blockpos1 = blockpos.relative(direction);
            }

            EntityType<?> entitytype = this.getType(itemstack);

            if (itemstack.is(CNBItemModule.SPORELING_OVERWORLD_EGG.get())) {
                itemstack.update(DataComponents.ENTITY_DATA, CustomData.EMPTY, comp -> comp.update(itemTag -> {
                    itemTag.putString("EggType", "Overworld");
                }));
            } else if (itemstack.is(CNBItemModule.SPORELING_NETHER_EGG.get())) {
                itemstack.update(DataComponents.ENTITY_DATA, CustomData.EMPTY, comp -> comp.update(itemTag -> {
                    itemTag.putString("EggType", "Nether");
                }));
            }

            if (entitytype.spawn((ServerLevel)level, itemstack, context.getPlayer(), blockpos1, MobSpawnType.SPAWN_EGG, true, !Objects.equals(blockpos, blockpos1) && direction == Direction.UP) != null) {
                itemstack.shrink(1);
                level.gameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, blockpos);
            }

            return InteractionResult.CONSUME;
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        HitResult hitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);

        if (hitresult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(itemstack);
        } else if (!(level instanceof ServerLevel)) {
            return InteractionResultHolder.success(itemstack);
        } else {
            BlockHitResult blockhitresult = (BlockHitResult)hitresult;
            BlockPos blockpos = blockhitresult.getBlockPos();
            if (!(level.getBlockState(blockpos).getBlock() instanceof LiquidBlock)) {
                return InteractionResultHolder.pass(itemstack);
            } else if (level.mayInteract(player, blockpos) && player.mayUseItemAt(blockpos, blockhitresult.getDirection(), itemstack)) {
                EntityType<?> entitytype = this.getType(itemstack);
                Holder<Biome> biome = level.getBiome(blockpos);
                String sporelingType;

                if (itemstack.is(CNBItemModule.SPORELING_OVERWORLD_EGG.get())) {
                    if (level.random.nextBoolean()) {
                        sporelingType = CNBSporelingTypeModule.RED_OVERWORLD.getId().toString();
                    } else {
                        sporelingType = CNBSporelingTypeModule.BROWN_OVERWORLD.getId().toString();
                    }

                    itemstack.update(DataComponents.ENTITY_DATA, CustomData.EMPTY, comp -> comp.update(itemTag -> {
                        itemTag.putString("SporelingType", sporelingType);
                    }));
                } else if (itemstack.is(CNBItemModule.SPORELING_NETHER_EGG.get())) {
                    if (biome.is(Biomes.CRIMSON_FOREST)) {
                        sporelingType = CNBSporelingTypeModule.CRIMSON_FUNGUS.getId().toString();
                    } else if (biome.is(Biomes.WARPED_FOREST)) {
                        sporelingType = CNBSporelingTypeModule.WARPED_FUNGUS.getId().toString();
                    } else {
                        if (level.random.nextBoolean()) {
                            sporelingType = CNBSporelingTypeModule.RED_NETHER.getId().toString();
                        } else {
                            sporelingType = CNBSporelingTypeModule.BROWN_NETHER.getId().toString();
                        }
                    }

                    itemstack.update(DataComponents.ENTITY_DATA, CustomData.EMPTY, comp -> comp.update(itemTag -> {
                        itemTag.putString("SporelingType", sporelingType);
                    }));
                }

                if (entitytype.spawn((ServerLevel)level, itemstack, player, blockpos, MobSpawnType.SPAWN_EGG, false, false) == null) {
                    return InteractionResultHolder.pass(itemstack);
                } else {
                    if (!player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                    level.gameEvent(player, GameEvent.ENTITY_PLACE, player.position());
                    return InteractionResultHolder.consume(itemstack);
                }
            } else {
                return InteractionResultHolder.fail(itemstack);
            }
        }
    }
}
