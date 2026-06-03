package com.narylr.narylrmod.recipe;

import com.narylr.narylrmod.NarylrMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class ModRecipes {
    public static final RecipeType<SteelFurnaceRecipe> STEEL_FURNACING_TYPE =
            Registry.register(
                    BuiltInRegistries.RECIPE_TYPE,
                    ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "steel_furnacing"),
                    new RecipeType<>() {
                        @Override
                        public String toString() {
                            return NarylrMod.MOD_ID + ":steel_furnacing";
                        }
                    }
            );
    public static final RecipeSerializer<SteelFurnaceRecipe> STEEL_FURNACING_SERIALIZER =
            Registry.register(
                    BuiltInRegistries.RECIPE_SERIALIZER,
                    ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "steel_furnacing"),
                    new SteelFurnaceRecipe.Serializer()
            );

    public static void registerRecipes() {
        NarylrMod.LOGGER.info("注册钢熔炉配方类型");
    }
}
