package org.ivangeevo.hardcorefluidoverhaul.mixin;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidDrainable;
import net.minecraft.block.FluidFillable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import org.ivangeevo.hardcorefluidoverhaul.util.MiscUtils;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BucketItem.class)
public abstract class BucketItemMixin extends Item implements FluidModificationItem {
    @Shadow @Final private Fluid fluid;
    @Shadow protected abstract void playEmptyingSound(@Nullable PlayerEntity player, WorldAccess world, BlockPos pos);

    public BucketItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void onUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir)
    {
        ItemStack itemStack = user.getStackInHand(hand);
        BlockHitResult blockHitResult = BucketItem.raycast(world, user, this.fluid == Fluids.EMPTY
                ? RaycastContext.FluidHandling.SOURCE_ONLY : RaycastContext.FluidHandling.NONE);

        if (blockHitResult.getType() == HitResult.Type.MISS)
        {
            cir.setReturnValue(TypedActionResult.pass(itemStack));
        }

        if (blockHitResult.getType() == HitResult.Type.BLOCK)
        {
            BlockPos blockPos = blockHitResult.getBlockPos();
            Direction direction = blockHitResult.getSide();
            BlockPos blockPos2 = blockPos.offset(direction);

            if (!world.canPlayerModifyAt(user, blockPos) || !user.canPlaceOn(blockPos2, direction, itemStack))
            {
                cir.setReturnValue(TypedActionResult.fail(itemStack));
            }

            if (this.fluid == Fluids.EMPTY)
            {
                BlockState blockState = world.getBlockState(blockPos);
                FluidState fluidState = blockState.getFluidState();

                // Check if the block contains flowing water, and return fail if true
                if (fluidState.isOf(Fluids.FLOWING_WATER))
                {
                    cir.setReturnValue(TypedActionResult.fail(itemStack));
                }

                // Check if the block contains flowing lava or lava, and handle accordingly
                if (fluidState.isOf(Fluids.FLOWING_LAVA) || fluidState.isOf(Fluids.LAVA))
                {
                    cir.setReturnValue(TypedActionResult.fail(itemStack));
                    itemStack.decrement(1);
                    user.damage(user.getDamageSources().inFire(), 1.0f); // Using IN_FIRE damage source for lava damage
                    world.playSound(null, blockPos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, 0.8f, 1.0f);
                    return;
                }

                // Continue with original logic if not flowing water
                if (blockState.getBlock() instanceof FluidDrainable fluidDrainable)
                {
                    ItemStack drainedStack = fluidDrainable.tryDrainFluid(user, world, blockPos, blockState);

                    if (!drainedStack.isEmpty())
                    {
                        user.incrementStat(Stats.USED.getOrCreateStat(this));
                        fluidDrainable.getBucketFillSound().ifPresent(sound -> user.playSound((SoundEvent) sound, 1.0f, 1.0f));
                        world.emitGameEvent((Entity) user, GameEvent.FLUID_PICKUP, blockPos);
                        ItemStack exchangedStack = ItemUsage.exchangeStack(itemStack, user, drainedStack);

                        if (!world.isClient)
                        {
                            Criteria.FILLED_BUCKET.trigger((ServerPlayerEntity) user, drainedStack);
                        }

                        cir.setReturnValue(TypedActionResult.success(exchangedStack, world.isClient()));
                        return;
                    }
                }

                cir.setReturnValue(TypedActionResult.fail(itemStack));
                return;
            }

            // Handle placing fluid logic
            BlockState blockState = world.getBlockState(blockPos);
            BlockPos posToPlace = blockState.getBlock() instanceof FluidFillable && this.fluid == Fluids.WATER ? blockPos : blockPos2;

            if (this.placeFluid(user, world, posToPlace, blockHitResult)) {
                this.onEmptied(user, world, itemStack, posToPlace);

                if (user instanceof ServerPlayerEntity) {
                    Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity) user, posToPlace, itemStack);
                }

                user.incrementStat(Stats.USED.getOrCreateStat(this));
                cir.setReturnValue(TypedActionResult.success(BucketItem.getEmptiedStack(itemStack, user), world.isClient()));
                return;
            }

            cir.setReturnValue(TypedActionResult.fail(itemStack));
            return;
        }

        cir.setReturnValue(TypedActionResult.pass(itemStack));
    }

    @Inject(method = "placeFluid", at = @At("HEAD"), cancellable = true)
    private void modifyPlaceFluid(PlayerEntity player, World world, BlockPos pos, BlockHitResult hitResult, CallbackInfoReturnable<Boolean> cir) {

        boolean bl2;
        if (!(this.fluid instanceof FlowableFluid))
        {
            cir.setReturnValue(false);
        }
        BlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();
        boolean bl = blockState.canBucketPlace(this.fluid);
        boolean bl3 = bl2 = blockState.isAir() || bl || block instanceof FluidFillable && ((FluidFillable) block).canFillWithFluid(player, world, pos, blockState, this.fluid);
        if (!bl2)
        {
            cir.setReturnValue(hitResult != null && this.placeFluid(player, world, hitResult.getBlockPos().offset(hitResult.getSide()), null));
        }
        if (world.getDimension().ultrawarm() && this.fluid.isIn(FluidTags.WATER))
        {
            int i = pos.getX();
            int j = pos.getY();
            int k = pos.getZ();
            world.playSound(player, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5f, 2.6f + (world.random.nextFloat() - world.random.nextFloat()) * 0.8f);
            for (int l = 0; l < 8; ++l) {
                world.addParticle(ParticleTypes.LARGE_SMOKE, (double)i + Math.random(), (double)j + Math.random(), (double)k + Math.random(), 0.0, 0.0, 0.0);
            }
            cir.setReturnValue(true);
        }

        if (!world.isClient && bl && !blockState.isLiquid())
        {
            world.breakBlock(pos, true);
        }

        if (this.fluid == Fluids.WATER)
        {
            if ( !(block instanceof FluidFillable) )
            {
                MiscUtils.placeNonPersistentWater(world, pos);
            }
            else
            {
                ((FluidFillable)((Object)block)).tryFillWithFluid(world, pos, blockState, ((FlowableFluid)this.fluid).getStill(false));
            }
            this.playEmptyingSound(player, world, pos);
            cir.setReturnValue(true);
        }

        else // Rest of your conditions for other fluids
        {

            if (world.setBlockState(pos, this.fluid.getDefaultState().getBlockState(),
                    Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD) || blockState.getFluidState().isStill()) {
                this.playEmptyingSound(player, world, pos);
                cir.setReturnValue(true);
            }

        }

        cir.setReturnValue(cir.getReturnValue());
    }



}
