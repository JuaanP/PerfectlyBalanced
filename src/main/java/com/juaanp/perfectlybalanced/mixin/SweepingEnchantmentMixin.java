package com.juaanp.perfectlybalanced.mixin;

import net.minecraft.enchantment.SweepingEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SweepingEnchantment.class)
public class SweepingEnchantmentMixin {

    @Inject(method = "getMaxLevel", at = @At("RETURN"), cancellable = true)
    private void getMaxLevel(CallbackInfoReturnable<Integer> cir){
        cir.setReturnValue(5);
    }
}
