package com.juaanp.perfectlybalanced.mixin;

import net.minecraft.enchantment.DamageEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DamageEnchantment.class)
public abstract class DamageEnchantmentMixin {

    @Shadow
    private int typeIndex;

    @Inject(method = "onTargetDamaged", at = @At("HEAD"), cancellable = true)
    private void onTargetDamaged(LivingEntity user, Entity target, int level, CallbackInfo ci) {
        ci.cancel();

        if (target instanceof LivingEntity livingEntity) {
            if (this.typeIndex == 2 && level > 0) {
                int i = 20 + user.getRandom().nextInt(20 * level);
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, i, 2));
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, i, 2));
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, i, 5));
            }
        }
    }
}
