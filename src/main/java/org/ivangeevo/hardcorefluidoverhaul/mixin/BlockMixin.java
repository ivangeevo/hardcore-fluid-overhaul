package org.ivangeevo.hardcorefluidoverhaul.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.ivangeevo.hardcorefluidoverhaul.util.MiscUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.state.property.Properties.WATERLOGGED;

@Mixin(Block.class)
public abstract class BlockMixin
{

    // Make waterlogged blocks to not retain water source block on break.
    @Inject(method = "afterBreak", at = @At("HEAD"))
    private void onBreakWaterloggedBlock(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack tool, CallbackInfo ci)
    {
        this.handleCustomWaterlogging(world, pos, state, player);
    }

    @Unique
    public void handleCustomWaterlogging(World world, BlockPos pos, BlockState state, PlayerEntity player)
    {
        if ( state.contains(WATERLOGGED) && state.get(WATERLOGGED) && !world.isClient )
        {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            if (player.getAbilities().creativeMode)
            {
                MiscUtils.placeNonPersistentWater(world, pos);
            }
            else
            {
                MiscUtils.placeNonPersistentWater(world, pos);
            }
        }
    }

}
