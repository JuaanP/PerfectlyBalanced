package com.juaanp.perfectlybalanced;

import com.juaanp.perfectlybalanced.item.InfernalNoodlesItem;
import com.juaanp.perfectlybalanced.item.InfernalStewItem;
import com.juaanp.perfectlybalanced.item.MiniFireworkRocketItem;
import com.juaanp.perfectlybalanced.item.PoisonousPotatoItem;
import com.mojang.serialization.Lifecycle;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public class AllItems {

    public static final MiniFireworkRocketItem MINI_FIREWORK_ROCKET = new MiniFireworkRocketItem(new FabricItemSettings().group(ItemGroup.MISC));
    public static final InfernalStewItem INFERNAL_STEW = new InfernalStewItem((new Item.Settings()).maxCount(1).group(ItemGroup.FOOD).food(FoodComponents.CHORUS_FRUIT));
    public static final InfernalNoodlesItem INFERNAL_NOODLES = new InfernalNoodlesItem((new Item.Settings()).maxCount(1).group(ItemGroup.FOOD).food(FoodComponents.CHORUS_FRUIT));
    public static final PoisonousPotatoItem POISONOUS_POTATO = new PoisonousPotatoItem((new Item.Settings()).group(ItemGroup.FOOD).food(FoodComponents.POISONOUS_POTATO));

    public static void addItem(String name, Item item){
        Registry.register(Registry.ITEM, new Identifier(PerfectlyBalanced.MOD_ID, name), item);
    }

    public static void replaceItem(Item replace, String name, Item item){
        Identifier id = new Identifier(PerfectlyBalanced.MOD_ID, name);
        int replacedId = Registry.ITEM.getRawId(replace);
        RegistryKey<Item> key = RegistryKey.of(Registry.ITEM_KEY, id);
        Registry.ITEM.set(replacedId, key, item, Lifecycle.stable());
    }

    public static void registerAllItems() {
        addItem("mini_firework", MINI_FIREWORK_ROCKET);
        addItem("infernal_stew", INFERNAL_STEW);
        addItem("infernal_noodles", INFERNAL_NOODLES);
        replaceItem(Items.POISONOUS_POTATO, "poisonous_potato", POISONOUS_POTATO);
    }
}
