package org.ivangeevo.hfo_mod.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.ivangeevo.hfo_mod.HFOMod;
import org.ivangeevo.hfo_mod.util.MiscUtils;
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
        this.handleCustomWaterlogging(world, pos, state);
    }

    @Unique
    public void handleCustomWaterlogging(World world, BlockPos pos, BlockState state) {
        if (state.contains(WATERLOGGED) && state.get(WATERLOGGED) && !world.isClient) {
            if (HFOMod.getInstance().settings.isWaterloggedBlocksDissipate()) {
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
                MiscUtils.placeNonPersistentWater(world, pos);
            } else {
                world.setBlockState(pos, Blocks.WATER.getDefaultState());
            }

        }
    }

}
