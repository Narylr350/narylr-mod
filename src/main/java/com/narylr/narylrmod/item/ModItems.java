package com.narylr.narylrmod.item;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;

import static com.narylr.narylrmod.NarylrMod.LOGGER;
import static com.narylr.narylrmod.NarylrMod.MOD_ID;

public class ModItems {
    public static final Item STEEL_INGOT = Registry.register(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "steel_ingot"),
            new Item(new Properties())
    );
    public static final Item STEEL_NUGGET = Registry.register(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "steel_nugget"),
            new Item(new Properties())
    );
    public static final Item STEEL_BLOCK_ITEM = Registry.register(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "steel_block"),
            new BlockItem(ModBlocks.STEEL_BLOCK,new Properties())
    );
    public static final Item RAW_STEEL = Registry.register(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath(MOD_ID,"raw_steel"),
            new Item(new Properties())
    );
    public static final Item STEEL_FURNACE_ITEM = Registry.register(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "steel_furnace"),
            new BlockItem(ModBlocks.STEEL_FURNACE,new Properties())
    );
    public static void registerModItems() {
        LOGGER.info("物品注册成功");
    }
}
