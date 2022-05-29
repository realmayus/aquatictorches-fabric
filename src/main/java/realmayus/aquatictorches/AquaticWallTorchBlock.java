package realmayus.aquatictorches;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Random;

public class AquaticWallTorchBlock extends WallTorchBlock implements Waterloggable {

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final IntProperty FLOWING_WATER = IntProperty.of("water_level", 1, 8);
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;


    public AquaticWallTorchBlock(Settings settings, ParticleEffect particle) {
        super(settings, particle);
        setDefaultState(this.stateManager.getDefaultState().with(WATERLOGGED, false).with(FLOWING_WATER, 8));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, FLOWING_WATER, WATERLOGGED);
    }

//    @Override
//    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
//        Direction dir = state.get(FACING);
//        switch (dir) {
//            case NORTH: return VoxelShapes.cuboid(5.5, 3.0, 11.0, 10.5, 13.0, 16.0);
//            case SOUTH : return  VoxelShapes.cuboid(5.5, 3.0, 0.0, 10.5, 13.0, 5.0);
//            case EAST : return  VoxelShapes.cuboid(0.0, 3.0, 5.5, 5.0, 13.0, 10.5);
//            case WEST : return  VoxelShapes.cuboid(11.0, 3.0, 5.5, 16.0, 13.0, 10.5);
//            default : AquaticTorches.LOGGER.error("Invalid direction detected at " + pos.toString()); return  VoxelShapes.fullCube();
//        }
//    }


//    @Override
//    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
//        Direction direction = state.get(FACING);
//        BlockPos blockpos = pos.offset(direction.getOpposite());  // block pos of block our torch is connected to
//        BlockState blockstate = world.getBlockState(blockpos);
//        return blockstate.isSideSolidFullSquare(world, blockpos, direction);
//    }
//
//    @Override
//    public BlockState rotate(BlockState state, BlockRotation rotation) {
//        return state.with(FACING, rotation.rotate(state.get(FACING)));
//    }
//
//    @Override
//    public BlockState mirror(BlockState state, BlockMirror mirror) {
//        return state.rotate(mirror.getRotation(state.get(FACING)));
//    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockstate = this.getDefaultState();
        WorldView worldView = ctx.getWorld();
        BlockPos blockpos = ctx.getBlockPos();
        Direction[] adirection = ctx.getPlacementDirections();

        for(Direction direction : adirection) {
            if (direction.getAxis().isHorizontal()) {
                Direction direction1 = direction.getOpposite();
                blockstate = blockstate.with(FACING, direction1);
                if (blockstate.canPlaceAt(worldView, blockpos)) {
                    break;
                }
            }
        }

        FluidState fluidstate = ctx.getWorld().getFluidState(ctx.getBlockPos());
        boolean flag = fluidstate.getFluid() == Fluids.WATER || fluidstate.getFluid() == Fluids.FLOWING_WATER;
        boolean is_flowing = fluidstate.getFluid() == Fluids.FLOWING_WATER;
        return blockstate.with(WATERLOGGED, flag).with(FLOWING_WATER, is_flowing ? fluidstate.getLevel() : 8);

    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        if (direction.getOpposite() == state.get(FACING) && !state.isFullCube(world, pos)) {
            return Blocks.AIR.getDefaultState();
        } else {
            return state;
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        if (state.get(WATERLOGGED) && state.get(FLOWING_WATER) == 8) {
            return Fluids.WATER.getStill(false);
        } else if (state.get(WATERLOGGED) && state.get(FLOWING_WATER) != 8) {
            return Fluids.WATER.getFlowing(state.get(FLOWING_WATER), false);
        }
        return Fluids.EMPTY.getDefaultState();
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos blockPos, Random random) {
        Direction direction = state.get(FACING);
        double d0 = (double)blockPos.getX() + 0.5D;
        double d1 = (double)blockPos.getY() + 0.7D;
        double d2 = (double)blockPos.getZ() + 0.5D;

        Direction direction1 = direction.getOpposite();
        world.addParticle(ParticleTypes.UNDERWATER, d0 + 0.27D * (double)direction1.getOffsetX(), d1 + 0.22D, d2 + 0.27D * (double)direction1.getOffsetZ(), 0.0D, 0.0D, 0.0D);
        world.addParticle(ParticleTypes.GLOW_SQUID_INK, d0 + 0.27D * (double)direction1.getOffsetX(), d1 + 0.22D, d2 + 0.27D * (double)direction1.getOffsetZ(), 0.0D, 0.0D, 0.0D);
    }
}
