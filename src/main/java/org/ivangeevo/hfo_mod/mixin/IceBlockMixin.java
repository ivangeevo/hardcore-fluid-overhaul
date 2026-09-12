package org.ivangeevo.hfo_mod.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.IceBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.ivangeevo.hfo_mod.HFOMod;
import org.ivangeevo.hfo_mod.config.HFOModConfig;
import org.ivangeevo.hfo_mod.util.MiscUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.block.IceBlock.getMeltedState;

@Mixin(IceBlock.class)
public abstract class IceBlockMixin
{

    @Unique private static final BlockState regularFlowingState = Fluids.FLOWING_WATER.getFlowing(7, false).getBlockState();

    @Inject(method = "afterBreak", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"), cancellable = true)
    private void onAfterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack tool, CallbackInfo ci)
    {
        BlockState blockState = world.getBlockState(pos.down());

        if (blockState.blocksMovement() || blockState.isLiquid()) {
            if (HFOModConfig.waterPersistentInOverworld.get()) {
                world.setBlockState(pos, regularFlowingState);
                MiscUtils.placeNonPersistentWater(world, pos);
            } else {
                world.setBlockState(pos, getMeltedState());
            }
        }

        ci.cancel();
    }




    //@Inject(method = "melt", at = @At(value = "INVOKE",
            //target = "Lnet/minecraft/block/IceBlock;getMeltedState()Lnet/minecraft/block/BlockState;", ordinal = 0), cancellable = true)
    private void onMelt(BlockState state, World world, BlockPos pos, CallbackInfo ci)
    {
        world.setBlockState(pos, regularFlowingState);
        MiscUtils.placeNonPersistentWater(world, pos);

        ci.cancel();
    }

}
