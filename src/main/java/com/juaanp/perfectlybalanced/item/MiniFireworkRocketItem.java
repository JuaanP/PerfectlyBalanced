package com.juaanp.perfectlybalanced.item;

import com.juaanp.perfectlybalanced.entity.projectile.MiniFireworkRocketEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class MiniFireworkRocketItem extends FireworkRocketItem {

    public MiniFireworkRocketItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user.isFallFlying()) {
            ItemStack itemStack = user.getStackInHand(hand);
            if (!world.isClient) {
                MiniFireworkRocketEntity entity = new MiniFireworkRocketEntity(world, itemStack, user);
                world.spawnEntity(entity);
                if (!user.getAbilities().creativeMode) {
                    itemStack.decrement(1);
                }

                user.incrementStat(Stats.USED.getOrCreateStat(this));
            }

            return TypedActionResult.success(user.getStackInHand(hand), world.isClient());
        } else {
            return TypedActionResult.pass(user.getStackInHand(hand));
        }
    }
}
