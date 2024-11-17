package org.ivangeevo.hardcorefluidoverhaul.mixin;


import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FluidBlock.class)
public abstract class FluidBlockMixin
{
    @Shadow @Final protected FlowableFluid fluid;
    @Shadow @Final public static IntProperty LEVEL;

    // TODO:  Make it so that the bucket doesn't take source blocks when trying to replace(aka it shouldn't).
    // this right here for some reason when injected doesn't allow the bucket to change to the new itemstack (filled)
    // @Inject(method = "tryDrainFluid", at = @At("HEAD"), cancellable = true)
    private void onTryDrainFluid(WorldAccess world, BlockPos pos, BlockState state, CallbackInfoReturnable<ItemStack> cir)
    {
        if (state.get(LEVEL) == 0)
        {
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            cir.setReturnValue( new ItemStack(this.fluid.getBucketItem()) );
        }
        cir.setReturnValue( ItemStack.EMPTY );

    }
}
