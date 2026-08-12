package com.example.peacockfan.item;

import com.example.peacockfan.PeacockFan;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class ModItemIds {
    public static ResourceKey<Item> create(String name) {
        return ResourceKey.create(Registries.ITEM,
                Identifier.fromNamespaceAndPath(PeacockFan.MOD_ID, name));
    }

    public static final ResourceKey<Item> PEACOCK_FEATHER_FAN = create("peacock_feather_fan");
}