package com.juaanp.perfectlybalanced.mixin;

import net.minecraft.item.CrossbowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {

    @ModifyVariable(method = "shoot", index = 7, at = @At("HEAD"), argsOnly = true)
    private static float shoot(float value) {
        return value * 1.5F;
    }
}