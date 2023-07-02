package com.juaanp.perfectlybalanced.mixin;

import net.minecraft.entity.passive.AnimalEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(AnimalEntity.class)
public abstract class AnimalEntityMixin {
    @ModifyArgs(method = "interactMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/AnimalEntity;growUp(IZ)V"))
    private void interactMob(Args args) {
        int age = args.get(0);
        args.set(0, age * 10);
    }
}