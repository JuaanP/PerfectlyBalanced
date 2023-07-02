package com.juaanp.perfectlybalanced.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FrostedIceBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FrostedMagmaBlock extends FrostedIceBlock {

    BlockState lava = Blocks.LAVA.getDefaultState();

    public FrostedMagmaBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected void melt(BlockState state, World world, BlockPos pos) {
        super.melt(state, world, pos);
        world.setBlockState(pos, lava);
    }
}
