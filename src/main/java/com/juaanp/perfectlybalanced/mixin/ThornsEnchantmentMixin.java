package com.juaanp.perfectlybalanced.mixin;

import net.minecraft.enchantment.ThornsEnchantment;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ThornsEnchantment.class)
public abstract class ThornsEnchantmentMixin {

    @Inject(
            method = "shouldDamageAttacker",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void shouldDamageAttacker(int level, Random random, CallbackInfoReturnable<Boolean> cir) {
        if (level <= 0) {
            cir.setReturnValue(Boolean.FALSE);
        } else {
            cir.setReturnValue(random.nextFloat() < 0.35F * (float)level);
        }
        cir.cancel();
    }

    @Inject(
            method = "getDamageAmount",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void getDamageAmount(int level, Random random, CallbackInfoReturnable<Integer> ci) {
        ci.setReturnValue((int) (level * 1.5f));
        ci.cancel();
    }
}
