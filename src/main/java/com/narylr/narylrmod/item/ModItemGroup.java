package com.narylr.narylrmod.item;

import com.narylr.narylrmod.NarylrMod;
import com.narylr.narylrmod.enchantment.ModEnchantments;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

import java.util.function.Supplier;

public class ModItemGroup {
    public static final CreativeModeTab STEEL_EXPANSION_GROUP = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "steel_craft"),
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(ModItems.STEEL_INGOT))
                    .title(Component.translatable("itemGroup.narylr-mod.steel_craft"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.STEEL_INGOT);//钢锭
                        output.accept(ModItems.STEEL_NUGGET);//钢粒
                        output.accept(ModItems.STEEL_BLOCK_ITEM);//钢块
                        output.accept(ModItems.RAW_STEEL);//生钢胚
                        output.accept(ModItems.STEEL_FURNACE_ITEM);//钢熔炉
                        output.accept(ModItems.STEEL_SWORD);//钢剑
                        output.accept(ModItems.STEEL_PICKAXE);//钢镐
                        output.accept(ModItems.STEEL_AXE);//钢斧
                        output.accept(ModItems.STEEL_SHOVEL);//钢铲
                        output.accept(ModItems.STEEL_HOE);//钢锄
                        output.accept(ModItems.STEEL_HELMET);//钢头盔
                        output.accept(ModItems.STEEL_CHESTPLATE);//钢胸甲
                        output.accept(ModItems.STEEL_LEGGINGS);//钢护腿
                        output.accept(ModItems.STEEL_BOOTS);//钢鞋子
                        output.accept(ModItems.STEEL_NETHER_SWORD);//下界合金钢剑
                        output.accept(ModItems.STEEL_NETHER_PICKAXE);//下界合金钢稿
                        output.accept(ModItems.STEEL_FRYING_PAN);//平底锅
                        output.accept(ModItems.STEEL_MORNING_STAR);//狼牙棒
                        output.accept(ModItems.STEEL_NETHER_HELMET);
                        output.accept(ModItems.STEEL_NETHER_CHESTPLATE);
                        output.accept(ModItems.STEEL_NETHER_LEGGINGS);
                        output.accept(ModItems.STEEL_NETHER_BOOTS);

                        itemDisplayParameters.holders()
                                .lookup(Registries.ENCHANTMENT)
                                .flatMap(enchantmentRegistry -> enchantmentRegistry.get(ModEnchantments.LIGHTWEIGHT))
                                .ifPresent(lightweight -> {
                                    output.accept(createEnchantedBook(lightweight, 1));
                                    output.accept(createEnchantedBook(lightweight, 2));
                                    output.accept(createEnchantedBook(lightweight, 3));
                                });
                    })
                    .build()
    );

    public static void registerItemGroups() {
        NarylrMod.LOGGER.info("注册创造模式Tab栏");
    }

    private static ItemStack createEnchantedBook(Holder<Enchantment> enchantment, int level) {
        return EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, level));
    }
}
