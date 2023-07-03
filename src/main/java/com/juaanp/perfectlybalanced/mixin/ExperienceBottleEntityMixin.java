package com.juaanp.perfectlybalanced.mixin;

import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ExperienceBottleEntity.class)
public class ExperienceBottleEntityMixin {

    @ModifyArg(method = "onCollision",index = 2, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ExperienceOrbEntity;spawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/Vec3d;I)V"))
    private int onCollision(int amount) {
        return amount * 5;
    }
}