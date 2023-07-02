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
        ci.cancel();

        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;

            if (!player.isCrawling()) {
                BlockState frosted_ice = Blocks.FROSTED_ICE.getDefaultState();
                BlockState frosted_magma = AllBlocks.FROSTED_MAGMA.getDefaultState();
                float f = (float) Math.min(16, 2 + level);
                BlockPos.Mutable mutable = new BlockPos.Mutable();
                Iterator var7 = BlockPos.iterate(blockPos.add((double) (-f), -1.0, (double) (-f)), blockPos.add((double) f, -1.0, (double) f)).iterator();

                while (var7.hasNext()) {
                    BlockPos blockPos2 = (BlockPos) var7.next();
                    if (blockPos2.isWithinDistance(entity.getPos(), (double) f)) {
                        mutable.set(blockPos2.getX(), blockPos2.getY() + 1, blockPos2.getZ());
                        BlockState blockState2 = world.getBlockState(mutable);
                        if (blockState2.isAir()) {
                            BlockState blockState3 = world.getBlockState(blockPos2);
                            if (blockState3.getMaterial() == Material.WATER && (Integer) blockState3.get(FluidBlock.LEVEL) == 0 && frosted_ice.canPlaceAt(world, blockPos2) && world.canPlace(frosted_ice, blockPos2, ShapeContext.absent())) {
                                world.setBlockState(blockPos2, frosted_ice);
                                world.createAndScheduleBlockTick(blockPos2, Blocks.FROSTED_ICE, MathHelper.nextInt(entity.getRandom(), 20, 40));
                            }
                            if (blockState3.getMaterial() == Material.LAVA && (Integer) blockState3.get(FluidBlock.LEVEL) == 0 && frosted_magma.canPlaceAt(world, blockPos2) && world.canPlace(frosted_magma, blockPos2, ShapeContext.absent())) {
                                world.setBlockState(blockPos2, frosted_magma);
                                world.createAndScheduleBlockTick(blockPos2, AllBlocks.FROSTED_MAGMA, MathHelper.nextInt(entity.getRandom(), 20, 40));
                            }
                        }
                    }
                }
            }
        }
    }
}
