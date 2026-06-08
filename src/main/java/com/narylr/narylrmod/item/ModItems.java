package com.narylr.narylrmod.item;

import com.narylr.narylrmod.NarylrMod;
import com.narylr.narylrmod.block.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;


//注册工具
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
            new SteelBlockItem(
                    ModBlocks.STEEL_BLOCK,
                    new Properties().attributes(SteelBlockItem.createSteelBlockAttributes())
            )
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
                            HeavyItemAttributes.addSteelIngotToolModifier(
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
                            HeavyItemAttributes.addSteelIngotToolModifier(
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
                            HeavyItemAttributes.addSteelIngotToolModifier(
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
                            HeavyItemAttributes.addSteelIngotToolModifier(
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
                            HeavyItemAttributes.addSteelIngotToolModifier(
                                    HoeItem.createAttributes(ModToolTiers.STEEL, -1.0F, 0.0F),
                                    "steel_hoe"
                            )
                    )
            )
    );
    public static final Item STEEL_HELMET = Registry.register(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "steel_helmet"),
            new ArmorItem(
                    ModArmorMaterials.STEEL,
                    ArmorItem.Type.HELMET,
                    new Properties().durability(ArmorItem.Type.HELMET.getDurability(37))
            )
    );
    public static final Item STEEL_CHESTPLATE = Registry.register(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "steel_chestplate"),
            new ArmorItem(
                    ModArmorMaterials.STEEL,
                    ArmorItem.Type.CHESTPLATE,
                    new Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(37))
            )
    );
    public static final Item STEEL_LEGGINGS = Registry.register(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "steel_leggings"),
            new ArmorItem(
                    ModArmorMaterials.STEEL,
                    ArmorItem.Type.LEGGINGS,
                    new Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(37))
            )
    );
    public static final Item STEEL_BOOTS = Registry.register(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "steel_boots"),
            new ArmorItem(
                    ModArmorMaterials.STEEL,
                    ArmorItem.Type.BOOTS,
                    new Properties().durability(ArmorItem.Type.BOOTS.getDurability(37))
            )
    );
    //下界合金钢剑
    public static final Item STEEL_NETHER_SWORD = Registry.register(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "steel_nether_sword"),
            new SwordItem(
                    ModToolTiers.STEEL,
                    new Properties().attributes(
                            HeavyItemAttributes.addSteelIngotToolModifier(
                                    SwordItem.createAttributes(ModToolTiers.STEEL, 4, -2.6F),
                                    "steel_nether_sword"
                            )
                    )
            )
    );
    //下界合金钢镐
    public static final Item STEEL_NETHER_PICKAXE = Registry.register(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "steel_nether_pickaxe"),
            new PickaxeItem(
                    ModToolTiers.STEEL,
                    new Properties().attributes(
                            HeavyItemAttributes.addSteelIngotToolModifier(
                                    PickaxeItem.createAttributes(ModToolTiers.STEEL, 1.0F, -3.0F),
                                    "steel_nether_pickaxe"
                            )
                    )
            )
    );
    // 钢平底锅：钢锭系重武器，伤害高于钢剑，但攻速更慢
    public static final Item STEEL_FRYING_PAN= Registry.register(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "steel_frying_pan"),
            new SteelFryingPanItem(
                    ModToolTiers.STEEL,
                    new Properties().attributes(
                            HeavyItemAttributes.addSteelIngotToolModifier(
                                    SwordItem.createAttributes(ModToolTiers.STEEL, 5, -2.8F),
                                    "steel_frying_pan"
                            )
                    )
            )
    );
    // 狼牙棒：钢块系重武器，伤害更高，但主手持有减速 30%，攻速也更慢
    public static final Item STEEL_MORNING_STAR = Registry.register(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "steel_morning_star"),
            new SteelMorningStarItem(
                    ModToolTiers.STEEL,
                    new Properties().attributes(
                            HeavyItemAttributes.addSteelBlockToolModifier(
                                    SwordItem.createAttributes(ModToolTiers.STEEL, 8, -3.4F),
                                    "steel_morning_star"
                            )
                    )
            )
    );

    public static void registerModItems() {
        NarylrMod.LOGGER.info("注册物品");
    }
}
