package com.juaanp.perfectlybalanced.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = "net.minecraft.enchantment.$InfinityEnchantment")
public class InfinityEnchantmentMixin extends Enchantment {
    private InfinityEnchantmentMixin(Rarity weight, EquipmentSlot[] slotTypes) {
        super(weight, EnchantmentTarget.CROSSBOW, slotTypes);
    }
}
