package com.juaanp.mixin;

import com.juaanp.PerfectlyBalanced;
import net.minecraft.block.*;
import net.minecraft.sound.BlockSoundGroup;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Blocks.class)
public abstract class BlocksMixin
{
    Logger logger = PerfectlyBalanced.LOGGER;

    @Redirect(
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args= {
                                    "stringValue=deepslate"
                            },
                            ordinal = 0
                    )
            ),
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/block/AbstractBlock$Settings;)Lnet/minecraft/block/PillarBlock;",
                    ordinal = 0
            ),
            method = "<clinit>")
    private static PillarBlock deepslate(AbstractBlock.Settings settings) {
        return new PillarBlock(AbstractBlock.Settings.of(Material.STONE, MapColor.DEEPSLATE_GRAY).requiresTool().strength(1.85F, 6.0F).sounds(BlockSoundGroup.DEEPSLATE));
    }
}
