package com.juaanp.perfectlybalanced.mixin;

import com.juaanp.perfectlybalanced.AllBlocks;
import net.minecraft.block.*;
import net.minecraft.enchantment.FrostWalkerEnchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;

@Mixin(FrostWalkerEnchantment.class)
public abstract class FrostWalkerEnchantmentMixin {

    @Inject(method = "freezeWater", at = @At("HEAD"), cancellable = true)
    private static void freezeWater(LivingEntity entity, World world, BlockPos blockPos, int level, CallbackInfo ci) {
        if (entity instanceof PlayerEntity) {

            if (!entity.isInSneakingPose())
            {
                BlockState frosted_iceDefaultState = Blocks.FROSTED_ICE.getDefaultState();
                BlockState frosted_magmaDefaultState = AllBlocks.FROSTED_MAGMA.getDefaultState();

                float f = (float) Math.min(16, 2 + level);
                BlockPos.Mutable mutable = new BlockPos.Mutable();

                for (BlockPos blockPos2 : BlockPos.iterate(blockPos.add((double) (-f), -1.0, (double) (-f)), blockPos.add((double) f, -1.0, (double) f))) {
                    if (blockPos2.isWithinDistance(entity.getPos(), (double) f)) {
                        mutable.set(blockPos2.getX(), blockPos2.getY() + 1, blockPos2.getZ());
                        BlockState blockState2 = world.getBlockState(mutable);
                        if (blockState2.isAir()) {
                            BlockState blockState3 = world.getBlockState(blockPos2);
                            if (blockState3.getMaterial() == Material.WATER && (Integer) blockState3.get(FluidBlock.LEVEL) == 0 && frosted_iceDefaultState.canPlaceAt(world, blockPos2) && world.canPlace(frosted_iceDefaultState, blockPos2, ShapeContext.absent())) {
                                world.setBlockState(blockPos2, frosted_iceDefaultState);
                                world.createAndScheduleBlockTick(blockPos2, Blocks.FROSTED_ICE, MathHelper.nextInt(entity.getRandom(), 20, 60));
                            } else if (blockState3.getMaterial() == Material.LAVA && (Integer) blockState3.get(FluidBlock.LEVEL) == 0 && frosted_magmaDefaultState.canPlaceAt(world, blockPos2) && world.canPlace(frosted_magmaDefaultState, blockPos2, ShapeContext.absent())) {
                                world.setBlockState(blockPos2, frosted_magmaDefaultState);
                                world.createAndScheduleBlockTick(blockPos2, AllBlocks.FROSTED_MAGMA, MathHelper.nextInt(entity.getRandom(), 20, 60));
                            } else if ((blockState3.getBlock() == Blocks.KELP || blockState3.getBlock() == Blocks.SEAGRASS || blockState3.getBlock() == Blocks.TALL_SEAGRASS) && frosted_iceDefaultState.canPlaceAt(world, blockPos2) && world.canPlace(frosted_iceDefaultState, blockPos2, ShapeContext.absent())) {
                                world.setBlockState(blockPos2, frosted_iceDefaultState);
                                world.createAndScheduleBlockTick(blockPos2, Blocks.FROSTED_ICE, MathHelper.nextInt(entity.getRandom(), 20, 60));
                            }
                        }
                    }
                }
            }
        }
        ci.cancel();
    }
}