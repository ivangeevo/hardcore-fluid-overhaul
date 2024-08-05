package org.ivangeevo.hardcorefluidoverhaul.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class MiscUtils
{

    // Fluid states here.
    private static final FluidState regularFlowingState = Fluids.FLOWING_WATER.getFlowing(7, false);
    private static final FluidState lowFlowingState = Fluids.FLOWING_WATER.getFlowing(2, false);

    public static void placeNonPersistentWater(WorldAccess world, BlockPos pos)
    {
        if (canWaterDisplaceBlock(world, pos))
        {

            // Get the current state to drop it later
            BlockState currentState = world.getBlockState(pos);

            // Check if the current state is replaceable or should be dropped
            if (!currentState.isAir() && !currentState.isOf(Blocks.WATER))
            {
                // The current state is not air or water; drop it and then place water
                onFluidFlowIntoBlock(world, pos, currentState);
            }
            world.setBlockState(pos, regularFlowingState.getBlockState(), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            spreadWaterIfNeeded(world, pos);
        }
    }

    private static void spreadWaterIfNeeded(WorldAccess world, BlockPos pos) {
        flowWaterIntoBlockIfPossible(world, pos.add(1, 0, 0), lowFlowingState.getBlockState());
        flowWaterIntoBlockIfPossible(world, pos.add(-1, 0, 0), lowFlowingState.getBlockState());
        flowWaterIntoBlockIfPossible(world, pos.add(0, 0, 1), lowFlowingState.getBlockState());
        flowWaterIntoBlockIfPossible(world, pos.add(0, 0, -1), lowFlowingState.getBlockState());
    }


    static public void flowWaterIntoBlockIfPossible(WorldAccess world, BlockPos pos, BlockState state) {
        // Check if the block at pos can be replaced by water
        if (canWaterDisplaceBlock(world, pos)) {
            // Get the current state to drop it later
            BlockState currentState = world.getBlockState(pos);
            // Set the water block state
            world.setBlockState(pos, regularFlowingState.getBlockState(), 1);
            // Drop the current block state
            if (currentState.getBlock() != null) {
                onFluidFlowIntoBlock(world, pos, currentState);
            }
        }
    }

    static public boolean canWaterDisplaceBlock(WorldAccess world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);

        // Check if the block already contains water or is lava
        if (state.getFluidState().isOf(Fluids.WATER) || state.isOf(Blocks.LAVA)) {
            return false;
        }

        // Check specific blocks that prevent fluid flow
        return !state.isIn(BlockTags.PORTALS) && state.isReplaceable();
    }

    public static void onFluidFlowIntoBlock(WorldAccess world, BlockPos pos, BlockState state) {
        // Drop the block state if it exists
        ItemStack blockStack = state.getBlock().asItem().getDefaultStack();
        Block.dropStack((World) world, pos, blockStack);
    }
}
