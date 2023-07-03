package com.juaanp.perfectlybalanced;

import com.juaanp.perfectlybalanced.block.FrostedMagmaBlock;
import com.mojang.serialization.Lifecycle;
import net.minecraft.block.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public class AllBlocks {

    public static final Block FROSTED_MAGMA = new FrostedMagmaBlock(AbstractBlock.Settings.of(Material.STONE).sounds(BlockSoundGroup.DEEPSLATE).hardness(3f).resistance(6f).slipperiness(0.7f).velocityMultiplier(0.7f).jumpVelocityMultiplier(0.9f));

    public static void addBlock(String name, Block block){
        Registry.register(Registry.BLOCK, new Identifier(PerfectlyBalanced.MOD_ID, name), block);
    }

    public static void replaceBlock(Block replace, String name, Block block){
        Identifier id = new Identifier(PerfectlyBalanced.MOD_ID, name);
        int replacedId = Registry.BLOCK.getRawId(replace);
        RegistryKey<Block> key = RegistryKey.of(Registry.BLOCK_KEY, id);
        Registry.BLOCK.set(replacedId, key, block, Lifecycle.stable());
    }

    public static void registerAllBlocks() {
        addBlock("frosted_magma", FROSTED_MAGMA);
    }
}
