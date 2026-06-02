package com.narylr.narylrmod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SteelFurnaceBlockEntity extends BlockEntity {
    private final SimpleContainer inventory = new SimpleContainer(3);

    public SteelFurnaceBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.STEEL_FURNACE_BLOCK_ENTITY, pos, blockState);
    }

    public SimpleContainer getInventory() {
        return inventory;
    }
}
