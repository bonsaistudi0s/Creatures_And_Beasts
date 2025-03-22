package com.cgessinger.creaturesandbeasts.blocks;

import com.cgessinger.creaturesandbeasts.entities.LizardEntity;
import com.cgessinger.creaturesandbeasts.modules.CNBEntityModule;
import com.cgessinger.creaturesandbeasts.modules.CNBLizardTypeModule;
import com.cgessinger.creaturesandbeasts.modules.CNBSoundModule;
import com.cgessinger.creaturesandbeasts.util.LizardType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class LizardEggBlock extends Block {
    public static final IntegerProperty EGGS = IntegerProperty.create("eggs", 1, 6);;
    private static final VoxelShape THREE_EGG_SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 2.0D, 11.0D);
    private LizardType parent1;
    private LizardType parent2;

    public LizardEggBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.SAND).strength(0.5F).sound(SoundType.METAL).randomTicks().noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(EGGS, 1));
        this.parent1 = CNBLizardTypeModule.DESERT;
        this.parent2 = CNBLizardTypeModule.JUNGLE;
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(EGGS);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
        if (this.canGrow(worldIn)) {
            this.removeOneEgg(worldIn, pos, state);
            worldIn.levelEvent(2001, pos, Block.getId(state));
            LizardEntity lizard = (LizardEntity) CNBEntityModule.LIZARD.get().create(worldIn);
            lizard.setAge(-24000);
            lizard.setLizardType(lizard.getRandom().nextBoolean() ? this.parent1 : this.parent2);
            lizard.moveTo(pos.getX() + 0.3D, pos.getY(), pos.getZ() + 0.3D, 0.0F, 0.0F);
            worldIn.addFreshEntity(lizard);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return THREE_EGG_SHAPE;
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        super.playerDestroy(level, player, pos, state, blockEntity, stack);
        this.removeOneEgg(level, pos, state);
    }

    private boolean canGrow(Level worldIn) {
        float f = worldIn.getTimeOfDay(1.0F);
        if ((double) f < 0.69D && (double) f > 0.65D) {
            return true;
        } else {
            return worldIn.random.nextInt(200) == 0;
        }
    }

    private void removeOneEgg(Level worldIn, BlockPos pos, BlockState state) {
        worldIn.playSound(null, pos, CNBSoundModule.LIZARD_EGG_HATCH.get(), SoundSource.BLOCKS, 1.0F, 0F);
        int i = state.getValue(EGGS);
        if (i <= 1) {
            worldIn.destroyBlock(pos, false);
        } else {
            worldIn.setBlock(pos, state.setValue(EGGS, i - 1), 2);
            worldIn.levelEvent(2001, pos, Block.getId(state));
        }
    }

    public void setParents(LizardType parent1, LizardType parent2) {
        this.parent1 = parent1;
        this.parent2 = parent2;
    }

    @Override
    public boolean canBeReplaced(BlockState blockState, BlockPlaceContext context) {
        return context.isSecondaryUseActive() && context.getItemInHand().is(this.asItem()) && blockState.getValue(EGGS) < 6 || super.canBeReplaced(blockState, context);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos());
        return blockstate.is(this) ? blockstate.setValue(EGGS, Math.min(6, blockstate.getValue(EGGS) + 1)) : super.getStateForPlacement(context);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState stateBelow = level.getBlockState(pos.below());
        return stateBelow.isFaceSturdy(level, pos.below(), Direction.UP, SupportType.FULL);
    }
}
