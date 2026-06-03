package com.narylr.narylrmod.client.compat.jei;

import com.narylr.narylrmod.NarylrMod;
import com.narylr.narylrmod.block.ModBlocks;
import com.narylr.narylrmod.recipe.ModRecipes;
import com.narylr.narylrmod.recipe.SteelFurnaceRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.client.Minecraft;

import java.util.List;

public class SteelCraftJeiPlugin implements IModPlugin {
    private static final ResourceLocation PLUGIN_ID = ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new SteelFurnaceRecipeCategory(registration.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.level == null) {
            return;
        }

        List<SteelFurnaceRecipe> recipes = minecraft.level.getRecipeManager()
                .getAllRecipesFor(ModRecipes.STEEL_FURNACING_TYPE)
                .stream()
                .map(RecipeHolder::value)
                .toList();

        registration.addRecipes(SteelFurnaceRecipeCategory.RECIPE_TYPE, recipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(
                new ItemStack(ModBlocks.STEEL_FURNACE),
                SteelFurnaceRecipeCategory.RECIPE_TYPE
        );
    }
}
