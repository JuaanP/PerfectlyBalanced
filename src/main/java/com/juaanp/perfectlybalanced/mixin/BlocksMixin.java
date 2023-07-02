package com.juaanp.perfectlybalanced.mixin;

import net.minecraft.block.*;
import net.minecraft.sound.BlockSoundGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Blocks.class)
public abstract class BlocksMixin
{
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
        return new PillarBlock(AbstractBlock.Settings.of(Material.STONE, MapColor.DEEPSLATE_GRAY).requiresTool().strength(1.6F, 6.0F).sounds(BlockSoundGroup.DEEPSLATE));
    }
}
