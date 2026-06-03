package com.narylr.narylrmod.recipe;

import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.function.Function;

public record SteelFurnaceRecipe(
        Ingredient ingredient,
        int coalCount,
        ItemStack result,
        int cookingTime
) implements Recipe<SteelFurnaceRecipeInput> {

    @Override
    public boolean matches(SteelFurnaceRecipeInput input, Level level) {
        ItemStack inputStack = input.getItem(0);
        ItemStack coalStack = input.getItem(1);

        return ingredient.test(inputStack)
                && coalStack.is(Items.COAL)
                && coalStack.getCount() >= coalCount;
    }

    @Override
    public ItemStack assemble(SteelFurnaceRecipeInput input, HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.STEEL_FURNACING_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.STEEL_FURNACING_TYPE;
    }

    public static class Serializer implements RecipeSerializer<SteelFurnaceRecipe> {
        public static final MapCodec<SteelFurnaceRecipe> CODEC = RecordCodecBuilder.mapCodec(
                new Function<RecordCodecBuilder.Instance<SteelFurnaceRecipe>, App<RecordCodecBuilder.Mu<SteelFurnaceRecipe>, SteelFurnaceRecipe>>() {
                    @Override
                    public App<RecordCodecBuilder.Mu<SteelFurnaceRecipe>, SteelFurnaceRecipe> apply(RecordCodecBuilder.Instance<SteelFurnaceRecipe> instance) {
                        return instance.group(
                                        Ingredient.CODEC.fieldOf("ingredient").forGetter(SteelFurnaceRecipe::ingredient),
                                        Codec.INT.fieldOf("coal_count").forGetter(SteelFurnaceRecipe::coalCount),
                                        ItemStack.CODEC.fieldOf("result").forGetter(SteelFurnaceRecipe::result),
                                        Codec.INT.fieldOf("cookingtime").forGetter(SteelFurnaceRecipe::cookingTime)
                        ).apply(instance, SteelFurnaceRecipe::new);
                    }
                }
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, SteelFurnaceRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC,
                SteelFurnaceRecipe::ingredient,
                ByteBufCodecs.VAR_INT,
                SteelFurnaceRecipe::coalCount,
                ItemStack.STREAM_CODEC,
                SteelFurnaceRecipe::result,
                ByteBufCodecs.VAR_INT,
                SteelFurnaceRecipe::cookingTime,
                SteelFurnaceRecipe::new
        );

        @Override
        public MapCodec<SteelFurnaceRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SteelFurnaceRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
