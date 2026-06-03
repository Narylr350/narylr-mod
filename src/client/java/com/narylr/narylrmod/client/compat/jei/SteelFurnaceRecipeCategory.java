package com.narylr.narylrmod.client.compat.jei;

import com.narylr.narylrmod.NarylrMod;
import com.narylr.narylrmod.block.ModBlocks;
import com.narylr.narylrmod.recipe.SteelFurnaceRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class SteelFurnaceRecipeCategory implements IRecipeCategory<SteelFurnaceRecipe> {

    public static final RecipeType<SteelFurnaceRecipe> RECIPE_TYPE = RecipeType.create(NarylrMod.MOD_ID, "steel_furnacing", SteelFurnaceRecipe.class);

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/gui/container/furnace.png");

    private final IDrawableStatic background;
    private final IDrawable icon;

    public SteelFurnaceRecipeCategory(IGuiHelper guiHelper) {
        background = guiHelper.createDrawable(TEXTURE, 46, 13, 90, 60);
        icon = guiHelper.createDrawableItemStack(new ItemStack(ModBlocks.STEEL_FURNACE));
    }

    @Override
    public RecipeType<SteelFurnaceRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return  Component.translatable("jei.narylr-mod.category.steel_furnace");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SteelFurnaceRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 10, 5).addIngredients(recipe.ingredient());

        builder.addSlot(RecipeIngredientRole.INPUT, 10, 41).addItemStack(new ItemStack(Items.COAL, recipe.coalCount()));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 70, 23).addItemStack(recipe.result());
    }

    @Override
    public int getWidth() {
        return 90;
    }

    @Override
    public int getHeight() {
        return 60;
    }

    @Override
    public void draw(SteelFurnaceRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics);

        int seconds = recipe.cookingTime() / 20;

        guiGraphics.drawString(
                Minecraft.getInstance().font,
                Component.literal(seconds + "秒"),
                36,
                4,
                0xFF808080,
                false
        );

        guiGraphics.drawString(
                Minecraft.getInstance().font,
                Component.literal("经验 " + recipe.experience()),
                36,
                44,
                0xFF808080,
                false
        );
    }
}
