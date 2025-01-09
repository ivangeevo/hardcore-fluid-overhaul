package org.ivangeevo.hardcorefluidoverhaul.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.IceBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.ivangeevo.hardcorefluidoverhaul.util.MiscUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IceBlock.class)
public class IceBlockMixin
{

    @Unique private static final BlockState regularFlowingState = Fluids.FLOWING_WATER.getFlowing(7, false).getBlockState();


    //@Inject(method = "afterBreak", at = @At(value = "INVOKE",
    //target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"), cancellable = true)
    private void onAfterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack tool, CallbackInfo ci)
    {
        // TODO: FIX-NOT WORKING, ALWAYS BREAKS TO NON PERSISTENT WATER :(
        // Ensure this method executes only if broken by a player
        if (player instanceof PlayerEntity) {
            BlockState blockState = world.getBlockState(pos.down());
            if (blockState.blocksMovement() || blockState.isLiquid()) {
                world.setBlockState(pos, regularFlowingState);
                MiscUtils.placeNonPersistentWater(world, pos);
            }

            ci.cancel();
        }
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
