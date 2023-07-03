package com.juaanp.perfectlybalanced.mixin;

import net.minecraft.entity.ExperienceOrbEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ExperienceOrbEntity.class)
public class ExperienceOrbEntityMixin {

    @Inject(method = "getMendingRepairCost", at = @At("RETURN"), cancellable = true)
    private void getMendingRepairCost(int repairAmount, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(repairAmount);
    }
}
