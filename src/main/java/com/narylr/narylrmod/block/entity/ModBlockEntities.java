package com.narylr.narylrmod.block.entity;

import com.narylr.narylrmod.NarylrMod;
import com.narylr.narylrmod.block.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
    public static final BlockEntityType<SteelFurnaceBlockEntity> STEEL_FURNACE_BLOCK_ENTITY = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "steel_furnace"),
            BlockEntityType.Builder.of(SteelFurnaceBlockEntity::new, ModBlocks.STEEL_FURNACE).build()
    );

    public static void registerBlockEntities() {
        NarylrMod.LOGGER.info("注册钢熔炉方块实体");
    }
}