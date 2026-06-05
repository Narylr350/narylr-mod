package com.narylr.narylrmod.item;

import com.narylr.narylrmod.NarylrMod;
import com.narylr.narylrmod.block.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;

public class ModItems {
    public static final Item STEEL_INGOT = Registry.register(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "steel_ingot"),
            new SteelIngotItem(new Properties().attributes(SteelIngotItem.createSteelIngotAttributes()))
    );
    public static final Item STEEL_NUGGET = Registry.register(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "steel_nugget"),
            new Item(new Properties())
    );
    public static final Item STEEL_BLOCK_ITEM = Registry.register(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "steel_block"),
            new BlockItem(ModBlocks.STEEL_BLOCK,new Properties())
    );
    public static final Item RAW_STEEL = Registry.register(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID,"raw_steel"),
            new Item(new Properties())
    );
    public static final Item STEEL_FURNACE_ITEM = Registry.register(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "steel_furnace"),
            new BlockItem(ModBlocks.STEEL_FURNACE,new Properties())
    );
    public static final Item STEEL_SWORD = Registry.register(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "steel_sword"),
            new SwordItem(
                    ModToolTiers.STEEL,
                    new Properties().attributes(
                            SteelToolAttributes.addHeavyMainHandModifier(
                                    SwordItem.createAttributes(ModToolTiers.STEEL, 4, -2.6F),
                                    "steel_sword"
                            )
                    )
            )
    );
    public static final Item STEEL_PICKAXE = Registry.register(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "steel_pickaxe"),
            new PickaxeItem(
                    ModToolTiers.STEEL,
                    new Properties().attributes(
                            SteelToolAttributes.addHeavyMainHandModifier(
                                    PickaxeItem.createAttributes(ModToolTiers.STEEL, 1.0F, -3.0F),
                                    "steel_pickaxe"
                            )
                    )
            )
    );
    public static final Item STEEL_AXE = Registry.register(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "steel_axe"),
            new AxeItem(
                    ModToolTiers.STEEL,
                    new Properties().attributes(
                            SteelToolAttributes.addHeavyMainHandModifier(
                                    AxeItem.createAttributes(ModToolTiers.STEEL, 6.0F, -3.2F),
                                    "steel_axe"
                            )
                    )
            )
    );
    public static final Item STEEL_SHOVEL = Registry.register(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "steel_shovel"),
            new ShovelItem(
                    ModToolTiers.STEEL,
                    new Properties().attributes(
                            SteelToolAttributes.addHeavyMainHandModifier(
                                    ShovelItem.createAttributes(ModToolTiers.STEEL, 1.5F, -3.0F),
                                    "steel_shovel"
                            )
                    )
            )
    );
    public static final Item STEEL_HOE = Registry.register(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "steel_hoe"),
            new HoeItem(
                    ModToolTiers.STEEL,
                    new Properties().attributes(
                            SteelToolAttributes.addHeavyMainHandModifier(
                                    HoeItem.createAttributes(ModToolTiers.STEEL, -1.0F, 0.0F),
                                    "steel_hoe"
                            )
                    )
            )
    );

    public static void registerModItems() {
        NarylrMod.LOGGER.info("注册物品");
    }
}
