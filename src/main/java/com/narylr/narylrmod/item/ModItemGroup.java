package com.narylr.narylrmod.item;

import com.narylr.narylrmod.NarylrMod;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class ModItemGroup {
    public static final CreativeModeTab STEEL_EXPANSION_GROUP = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "steel_craft"),
            FabricItemGroup.builder()
                    .icon(new Supplier<ItemStack>() {
                        @Override
                        public ItemStack get() {
                            return new ItemStack(ModItems.STEEL_INGOT);
                        }
                    })
                    .title(Component.translatable("itemGroup.narylr-mod.steel_craft"))
                    .displayItems(new CreativeModeTab.DisplayItemsGenerator() {
                        @Override
                        public void accept(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) {
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
                        }
                    })
                    .build()
    );

    public static void registerItemGroups() {
        NarylrMod.LOGGER.info("注册创造模式Tab栏");
    }
}
