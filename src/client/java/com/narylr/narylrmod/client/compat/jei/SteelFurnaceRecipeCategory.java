package com.narylr.narylrmod.client.compat.jei;

import com.narylr.narylrmod.NarylrMod;
import com.narylr.narylrmod.block.ModBlocks;
import com.narylr.narylrmod.recipe.SteelFurnaceRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.placement.HorizontalAlignment;
import mezz.jei.api.gui.placement.VerticalAlignment;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public class SteelFurnaceRecipeCategory implements IRecipeCategory<SteelFurnaceRecipe> {

    public static final RecipeType<SteelFurnaceRecipe> RECIPE_TYPE =
            RecipeType.create(NarylrMod.MOD_ID, "steel_furnacing", SteelFurnaceRecipe.class);

    private final IDrawable icon;

    public SteelFurnaceRecipeCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableItemStack(new ItemStack(ModBlocks.STEEL_FURNACE));
    }

    @Override
    public RecipeType<SteelFurnaceRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.narylr-mod.category.steel_furnace");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public int getWidth() {
        return 82;
    }

    @Override
    public int getHeight() {
        return 54;
    }

    // 设置钢熔炉 JEI 配方的输入槽、碳源槽和输出槽，坐标抄 JEI 原版熔炉分类
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SteelFurnaceRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1)
                .setStandardSlotBackground()
                .addIngredients(recipe.ingredient());

        builder.addSlot(RecipeIngredientRole.INPUT, 1, 37)
                .setStandardSlotBackground()
                .addItemStack(new ItemStack(Items.COAL, recipe.coalCount()));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 19)
                .setOutputSlotBackground()
                .addItemStack(recipe.result().copy());
    }

    // 设置钢熔炉 JEI 配方的动画箭头、火焰、经验和时间，排版抄 JEI 原版熔炉分类
    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, SteelFurnaceRecipe recipe, IFocusGroup focuses) {
        builder.addAnimatedRecipeArrow(recipe.cookingTime())
                .setPosition(26, 17);

        builder.addAnimatedRecipeFlame(300)
                .setPosition(1, 20);

        if (recipe.experience() > 0) {
            Component experienceText = Component.translatable(
                    "gui.jei.category.smelting.experience",
                    recipe.experience()
            );

            builder.addText(experienceText, getWidth() - 20, 10)
                    .setPosition(0, 0, getWidth(), getHeight(), HorizontalAlignment.RIGHT, VerticalAlignment.TOP)
                    .setTextAlignment(HorizontalAlignment.RIGHT)
                    .setColor(0xFF808080);
        }

        int seconds = recipe.cookingTime() / 20;

        if (seconds > 0) {
            Component timeText = Component.translatable(
                    "gui.jei.category.smelting.time.seconds",
                    seconds
            );

            builder.addText(timeText, getWidth() - 20, 10)
                    .setPosition(0, 0, getWidth(), getHeight(), HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM)
                    .setTextAlignment(HorizontalAlignment.RIGHT)
                    .setTextAlignment(VerticalAlignment.BOTTOM)
                    .setColor(0xFF808080);
        }
    }
}