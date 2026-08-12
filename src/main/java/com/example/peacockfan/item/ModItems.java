package com.example.peacockfan.item;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class ModItems {
    public static Item register(ResourceKey<Item> key,
                                Function<Item.Properties, Item> factory,
                                Item.Properties settings) {
        Item item = factory.apply(settings.setId(key));
        Registry.register(BuiltInRegistries.ITEM, key, item);
        return item;
    }

    public static final Item PEACOCK_FEATHER_FAN = register(
            ModItemIds.PEACOCK_FEATHER_FAN,
            PeacockFeatherFanItem::new,
            new Item.Properties().stacksTo(1)
    );

    public static void initialize() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register(tab -> tab.accept(PEACOCK_FEATHER_FAN));
    }
}
