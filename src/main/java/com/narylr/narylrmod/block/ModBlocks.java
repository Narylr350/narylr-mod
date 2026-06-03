package com.narylr.narylrmod.block;

import com.narylr.narylrmod.NarylrMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModBlocks {
    public static final Block STEEL_BLOCK = Registry.register(
            BuiltInRegistries.BLOCK,
            ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "steel_block"),
            new Block(BlockBehaviour.Properties.of()
                    .strength(6.0F, 8.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)
            )
    );
    public static final Block STEEL_FURNACE = Registry.register(
            BuiltInRegistries.BLOCK,
            ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "steel_furnace"),
            new SteelFurnaceBlock(BlockBehaviour.Properties.of()
                    .strength(6.0F, 10.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)
            )
    );

    public static void registerModBlocks() {
        NarylrMod.LOGGER.info("注册方块");
    }
}
