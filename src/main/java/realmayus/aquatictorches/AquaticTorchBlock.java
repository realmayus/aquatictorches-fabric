package realmayus.aquatictorches;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TorchBlock;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AquaticTorchBlock extends TorchBlock implements Waterloggable {

    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final IntProperty FLOWING_WATER = IntProperty.of("water_level", 1, 8);

    public AquaticTorchBlock(Settings settings, ParticleEffect particle) {
        super(settings, particle);
        setDefaultState(this.stateManager.getDefaultState().with(WATERLOGGED, false));
    }


    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FLOWING_WATER, WATERLOGGED);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext placeContext) {
        FluidState fluidstate = placeContext.getWorld().getFluidState(placeContext.getBlockPos());
        boolean flag = fluidstate.getFluid() == Fluids.WATER || fluidstate.getFluid() == Fluids.FLOWING_WATER;
        boolean is_flowing = fluidstate.getFluid() == Fluids.FLOWING_WATER;
        return this.getDefaultState().with(WATERLOGGED, flag).with(FLOWING_WATER, is_flowing ? fluidstate.getLevel() : 8);
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        if (blockState.get(WATERLOGGED) && blockState.get(FLOWING_WATER) == 8) {
            return Fluids.WATER.getStill(false);
        } else if (blockState.get(WATERLOGGED) && blockState.get(FLOWING_WATER) != 8) {
            return Fluids.WATER.getFlowing(blockState.get(FLOWING_WATER), false);
        }
        return Fluids.EMPTY.getDefaultState();
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos blockPos, Random random) {
        double d0 = (double)blockPos.getX() + 0.5D;
        double d1 = (double)blockPos.getY() + 0.7D;
        double d2 = (double)blockPos.getZ() + 0.5D;
        world.addParticle(ParticleTypes.UNDERWATER, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        world.addParticle(ParticleTypes.GLOW_SQUID_INK, d0, d1, d2, 0.0D, 0.0D, 0.0D);
    }
}