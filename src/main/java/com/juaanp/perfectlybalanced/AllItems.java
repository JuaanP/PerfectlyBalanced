package com.juaanp.perfectlybalanced;

import com.juaanp.perfectlybalanced.item.MiniFireworkRocketItem;
import com.mojang.serialization.Lifecycle;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public class AllItems {

    public static final MiniFireworkRocketItem MINI_FIREWORK_ROCKET = new MiniFireworkRocketItem(new FabricItemSettings().group(ItemGroup.MISC));
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
    }
}
