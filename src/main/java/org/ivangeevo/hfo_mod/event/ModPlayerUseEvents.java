package org.ivangeevo.hfo_mod.event;

import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidDrainable;
import net.minecraft.block.FluidFillable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.ivangeevo.hfo_mod.mixin.accessor.BucketItemAccessor;

public class ModPlayerUseEvents {

    public static void register() {
        UseItemCallback.EVENT.register((PlayerEntity player, World world, Hand hand) -> {
            ItemStack stack = player.getStackInHand(hand);

            // Modify only bucket item usage
            if (!(stack.getItem() instanceof BucketItem bucketItem)) return TypedActionResult.pass(stack);
            BucketItemAccessor bucketItemAccessor = (BucketItemAccessor) bucketItem;

            // If spectator or in creative -> pass to default interaction behavior
            if (player.isSpectator() || player.isCreative()) {
                return TypedActionResult.pass(stack);
            }

            BlockHitResult blockHitResult = raycast(world, player, bucketItemAccessor.getFluid() == Fluids.EMPTY
                    ? RaycastContext.FluidHandling.SOURCE_ONLY : RaycastContext.FluidHandling.NONE);

            if (blockHitResult.getType() == HitResult.Type.MISS) {
                return TypedActionResult.pass(stack);
            }

            if (blockHitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos blockPos = blockHitResult.getBlockPos();
                Direction direction = blockHitResult.getSide();
                BlockPos blockPos2 = blockPos.offset(direction);

                if (!world.canPlayerModifyAt(player, blockPos) || !player.canPlaceOn(blockPos2, direction, stack)) {
                    return TypedActionResult.fail(stack);
                }

                if (bucketItemAccessor.getFluid() == Fluids.EMPTY) {
                    BlockState blockState = world.getBlockState(blockPos);
                    FluidState fluidState = blockState.getFluidState();

                    // Check if the block contains flowing water, and return fail if true
                    if (fluidState.isOf(Fluids.FLOWING_WATER)) {
                        return TypedActionResult.fail(stack);
                    }


                    // Check if the block contains flowing lava or lava, and handle accordingly
                    if (fluidState.isOf(Fluids.FLOWING_LAVA) || fluidState.isOf(Fluids.LAVA)) {
                        stack.decrement(1);
                        player.damage(player.getDamageSources().inFire(), 1.0f); // Using the IN_FIRE damage source for lava damage
                        player.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 0.8f, 1.0f);
                        return TypedActionResult.fail(stack);
                    }

                    // Continue with original logic if not flowing water
                    if (blockState.getBlock() instanceof FluidDrainable fluidDrainable) {

                        ItemStack drainedStack = fluidDrainable.tryDrainFluid(player, world, blockPos, blockState);

                        if (!drainedStack.isEmpty()) {
                            player.incrementStat(Stats.USED.getOrCreateStat(bucketItem));
                            fluidDrainable.getBucketFillSound().ifPresent(sound -> player.playSound(sound, 1.0f, 1.0f));
                            world.emitGameEvent(player, GameEvent.FLUID_PICKUP, blockPos);
                            ItemStack exchangedStack = ItemUsage.exchangeStack(stack, player, drainedStack);

                            if (!world.isClient) {
                                Criteria.FILLED_BUCKET.trigger((ServerPlayerEntity) player, drainedStack);
                            }

                            return TypedActionResult.success(exchangedStack, world.isClient());
                        }
                    }

                    return TypedActionResult.fail(stack);
                }

                // Handle placing fluid logic
                BlockState blockState = world.getBlockState(blockPos);
                BlockPos posToPlace = blockState.getBlock() instanceof FluidFillable && bucketItemAccessor.getFluid() == Fluids.WATER ? blockPos : blockPos2;

                if (bucketItem.placeFluid(player, world, posToPlace, blockHitResult)) {
                    bucketItem.onEmptied(player, world, stack, posToPlace);

                    if (player instanceof ServerPlayerEntity) {
                        Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity) player, posToPlace, stack);
                    }

                    player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
                    return TypedActionResult.success(BucketItem.getEmptiedStack(stack, player), world.isClient());
                }

                return TypedActionResult.fail(stack);
            }

            return TypedActionResult.pass(stack);
        });
    }

    protected static BlockHitResult raycast(World world, PlayerEntity player, RaycastContext.FluidHandling fluidHandling) {
        Vec3d vec3d = player.getEyePos();
        Vec3d vec3d2 = vec3d.add(player.getRotationVector(player.getPitch(), player.getYaw()).multiply(player.getBlockInteractionRange()));
        return world.raycast(new RaycastContext(vec3d, vec3d2, RaycastContext.ShapeType.OUTLINE, fluidHandling, player));
    }

}
