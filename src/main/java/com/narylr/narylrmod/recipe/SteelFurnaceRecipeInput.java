package com.narylr.narylrmod.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record SteelFurnaceRecipeInput(ItemStack input, ItemStack coal) implements RecipeInput {
    @Override
    public ItemStack getItem(int index) {
        return switch (index){
            case 0 -> input;
            case 1 -> coal;
            default -> ItemStack.EMPTY;
        };
    }

    @Override
    public int size() {
        return 2;
    }
}
