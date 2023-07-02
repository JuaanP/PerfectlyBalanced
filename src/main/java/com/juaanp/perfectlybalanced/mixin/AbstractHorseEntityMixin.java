package com.juaanp.perfectlybalanced.mixin;

import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(AbstractHorseEntity.class)
public abstract class AbstractHorseEntityMixin {

    @Inject(method = "getChildHealthBonus", at = @At("RETURN"), cancellable = true)
    private void getChildHealthBonus(Random random, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(20.0F + (float) random.nextInt(20) + (float) random.nextInt(20));
    }

    @Inject(method = "getChildJumpStrengthBonus", at = @At("RETURN"), cancellable = true)
    private void getChildJumpStrengthBonus(Random random, CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(0.8F + random.nextDouble() * 0.2 + random.nextDouble() * 0.2 + random.nextDouble() * 0.2);
    }

    @Inject(method = "getChildMovementSpeedBonus", at = @At("RETURN"), cancellable = true)
    private void getChildMovementSpeedBonus(Random random, CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue((0.6F + random.nextDouble() * 0.2 + random.nextDouble() * 0.2 + random.nextDouble() * 0.2) * 0.5);
    }

    @ModifyArgs(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/AbstractHorseEntity;setVelocity(DDD)V"))
    private void travel(Args args) {
        double velocityX = args.get(0);
        double velocityY = args.get(1);
        double velocityZ = args.get(2);

        AbstractHorseEntity horse = (AbstractHorseEntity) (Object) this;
        PlayerEntity rider = (PlayerEntity) horse.getPrimaryPassenger();
        if (rider != null) {
            float yaw = rider.getYaw();
            float yawRadians = (float) Math.toRadians(yaw);
            double factor = 2 * horse.getJumpStrength();

            double modifiedVelocityX = velocityX + (-0.4 * MathHelper.sin(yawRadians) * factor);
            double modifiedVelocityZ = velocityZ + (0.4 * MathHelper.cos(yawRadians) * factor);

            args.set(0, modifiedVelocityX);
            args.set(1, velocityY + 0.3F);
            args.set(2, modifiedVelocityZ);
        }
    }

    @ModifyArg(method = "handleFallDamage", index= 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/AbstractHorseEntity;computeFallDamage(FF)I"))
    private float handleFallDamage(float fallDistance){
        return fallDistance * 0.5F;
    }
}