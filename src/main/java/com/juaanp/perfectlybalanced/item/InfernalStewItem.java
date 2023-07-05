package com.juaanp.perfectlybalanced.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.StewItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;


public class InfernalStewItem extends StewItem {

    public InfernalStewItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {

        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, 0.4F, 0.6F);
        ItemStack itemStack = super.finishUsing(stack, world, user);
        user.setStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 120), user);

        for (int i = 0; i < 10; i++) {
            double offsetX = (user.getRandom().nextFloat() - 0.5) * 0.04;
            double offsetY = (user.getRandom().nextFloat() - 0.5) * 0.04;
            double offsetZ = (user.getRandom().nextFloat() - 0.5) * 0.04;
            user.world.addParticle(ParticleTypes.FLAME,
                    user.getX() + user.getRandom().nextFloat() * user.getWidth() * 2.0F - user.getWidth(),
                    user.getY() + user.getRandom().nextFloat() * user.getHeight(),
                    user.getZ() + user.getRandom().nextFloat() * user.getWidth() * 2.0F - user.getWidth(),
                    offsetX, offsetY, offsetZ);
        }
        return user instanceof PlayerEntity && ((PlayerEntity) user).getAbilities().creativeMode ? itemStack : new ItemStack(Items.BOWL);
    }
}