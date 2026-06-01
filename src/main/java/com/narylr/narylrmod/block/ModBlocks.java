package com.narylr.narylrmod.block;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import static com.narylr.narylrmod.NarylrMod.LOGGER;
import static com.narylr.narylrmod.NarylrMod.MOD_ID;

public class ModBlocks {
    public static final Block STEEL_BLOCK = Registry.register(
            BuiltInRegistries.BLOCK,
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "steel_block"),
            new Block(BlockBehaviour.Properties.of()
                    .strength(6.0F, 8.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)
            )
    );
    public static final Block STEEL_FURNACE = Registry.register(
            BuiltInRegistries.BLOCK,
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "steel_furnace"),
            new SteelFurnaceBlock(BlockBehaviour.Properties.of()
                    .strength(6.0F, 10.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)
            )
    );

    public static void registerModBlocks() {
        LOGGER.info("注册方块");
    }
}
