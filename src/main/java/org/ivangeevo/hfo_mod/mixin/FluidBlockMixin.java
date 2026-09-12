package org.ivangeevo.hfo_mod.mixin;


import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.ivangeevo.hfo_mod.HFOMod;
import org.ivangeevo.hfo_mod.config.HFOModConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FluidBlock.class)
public abstract class FluidBlockMixin extends Block
{
    @Shadow @Final protected FlowableFluid fluid;

    public FluidBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "tryDrainFluid", at = @At("HEAD"), cancellable = true)
    private void onTryDrainFluid(PlayerEntity player, WorldAccess world, BlockPos pos, BlockState state, CallbackInfoReturnable<ItemStack> cir)
    {
        if (!HFOModConfig.sourceFluidPickupDisabled.get()) return;
        if (player != null) {
            if (player.isCreative()) return;
            cir.setReturnValue(new ItemStack(this.fluid.getBucketItem()));
        }

    }

}

