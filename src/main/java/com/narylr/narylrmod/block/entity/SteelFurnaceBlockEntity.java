package com.narylr.narylrmod.block.entity;

import com.narylr.narylrmod.block.SteelFurnaceBlock;
import com.narylr.narylrmod.recipe.ModRecipes;
import com.narylr.narylrmod.recipe.SteelFurnaceRecipe;
import com.narylr.narylrmod.recipe.SteelFurnaceRecipeInput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class SteelFurnaceBlockEntity extends BlockEntity implements Container {
    //当前已经烧了多久
    private int progress = 0;
    //总共要烧多久
    private int maxProgress = 0;
    //当前燃烧时间
    private int burnTime = 0;
    //总共燃烧时间
    private int maxBurnTime = 0;

    private SteelFurnaceRecipe currentSteelRecipe;
    private RecipeHolder<SmeltingRecipe> currentSmeltingRecipe;
    //临时存储在内存中
    private final SimpleContainer inventory = new SimpleContainer(3);
    //钢熔炉中的物品创建List集合存储
    private final NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
    //同步燃烧时间到客户端
    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> progress;
                case 1 -> maxProgress;
                case 2 -> burnTime;
                case 3 -> maxBurnTime;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> progress = value;
                case 1 -> maxProgress = value;
                case 2 -> burnTime = value;
                case 3 -> maxBurnTime = value;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    public SteelFurnaceBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.STEEL_FURNACE_BLOCK_ENTITY, pos, blockState);
    }

    @Deprecated
    public SimpleContainer getInventory() {
        return inventory;
    }

    public ContainerData getData() {
        return data;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, SteelFurnaceBlockEntity blockEntity) {
        if (level.isClientSide) {
            return;
        }

        boolean working = false;

        if (blockEntity.hasSteelRecipe()) {
            working = true;
            blockEntity.processSteelRecipe();
        } else if (blockEntity.hasSmeltingRecipe()) {
            working = blockEntity.processSmeltingRecipe();
        } else {
            blockEntity.resetProgress();
        }

        if (state.getValue(SteelFurnaceBlock.LIT) != working) {
            level.setBlock(pos, state.setValue(SteelFurnaceBlock.LIT, working), 3);
        }

        setChanged(level, pos, state);
    }

    private boolean hasSteelRecipe() {
        if (level == null) {
            maxProgress = 0;
            currentSteelRecipe = null;
            return false;
        }

        SteelFurnaceRecipeInput recipeInput = new SteelFurnaceRecipeInput(items.get(0), items.get(1));

        Optional<RecipeHolder<SteelFurnaceRecipe>> recipeHolder =
                level.getRecipeManager().getRecipeFor(ModRecipes.STEEL_FURNACING_TYPE, recipeInput, level);

        if (recipeHolder.isEmpty()) {
            currentSteelRecipe = null;
            return false;
        }

        SteelFurnaceRecipe recipe = recipeHolder.get().value();

        if (!canOutput(recipe.result())) {
            currentSteelRecipe = null;
            return false;
        }

        currentSteelRecipe = recipe;
        maxProgress = recipe.cookingTime();
        return true;
    }

    private void processSteelRecipe() {
        burnTime = 0;
        maxBurnTime = 0;

        progress++;

        if (progress >= maxProgress) {
            craftSteelRecipe();
            progress = 0;
        }
    }

    private void craftSteelRecipe() {
        if (currentSteelRecipe == null) {
            return;
        }

        ItemStack input = items.get(0);
        ItemStack coal = items.get(1);
        ItemStack output = items.get(2);
        ItemStack result = currentSteelRecipe.result().copy();

        input.shrink(1);
        coal.shrink(currentSteelRecipe.coalCount());

        if (output.isEmpty()) {
            items.set(2, result);
        } else {
            output.grow(result.getCount());
        }

        setChanged();
    }

    private boolean hasSmeltingRecipe() {
        if (level == null) {
            currentSmeltingRecipe = null;
            return false;
        }

        SingleRecipeInput recipeInput = new SingleRecipeInput(items.get(0));

        Optional<RecipeHolder<SmeltingRecipe>> recipeHolder =
                level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, recipeInput, level);

        if (recipeHolder.isEmpty()) {
            currentSmeltingRecipe = null;
            return false;
        }

        RecipeHolder<SmeltingRecipe> recipe = recipeHolder.get();
        ItemStack result = recipe.value().getResultItem(level.registryAccess());

        if (!canOutput(result)) {
            currentSmeltingRecipe = null;
            return false;
        }

        currentSmeltingRecipe = recipeHolder.get();
        maxProgress = Math.max(recipe.value().getCookingTime() / 2, 1);
        return true;
    }

    private boolean processSmeltingRecipe() {
        if (burnTime <= 0) {
            consumeFuel();
        }

        if (burnTime <= 0) {
            resetProgress();
            return false;
        }

        burnTime--;
        progress++;

        if (progress >= maxProgress) {
            craftSmeltingRecipe();
            progress = 0;
        }

        return true;
    }

    private void consumeFuel() {
        ItemStack fuel = items.get(1);

        if (fuel.isEmpty()) {
            return;
        }

        int fuelTime = AbstractFurnaceBlockEntity.getFuel().getOrDefault(fuel.getItem(), 0);

        if (fuelTime <= 0) {
            return;
        }

        burnTime = fuelTime;
        maxBurnTime = fuelTime;

        fuel.shrink(1);
    }

    private void craftSmeltingRecipe() {
        if (level == null || currentSmeltingRecipe == null) {
            return;
        }

        ItemStack input = items.get(0);
        ItemStack output = items.get(2);
        ItemStack result = currentSmeltingRecipe.value().getResultItem(level.registryAccess()).copy();

        input.shrink(1);

        if (output.isEmpty()) {
            items.set(2, result);
        } else {
            output.grow(result.getCount());
        }

        setChanged();
    }

    private void resetProgress() {
        if (progress != 0 || maxProgress != 0) {
            progress = 0;
            maxProgress = 0;
        }
    }

    private boolean canOutput(ItemStack result) {
        ItemStack output = items.get(2);

        if (result.isEmpty()) {
            return false;
        }

        if (output.isEmpty()) {
            return true;
        }

        return ItemStack.isSameItemSameComponents(output, result)
                && output.getCount() + result.getCount() <= output.getMaxStackSize();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        ContainerHelper.saveAllItems(tag, items, registries);
        tag.putInt("Progress", progress);
        tag.putInt("MaxProgress", maxProgress);
        tag.putInt("BurnTime", burnTime);
        tag.putInt("MaxBurnTime", maxBurnTime);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        ContainerHelper.loadAllItems(tag, items, registries);
        progress = tag.getInt("Progress");
        maxProgress = tag.getInt("MaxProgress");
        burnTime = tag.getInt("BurnTime");
        maxBurnTime = tag.getInt("MaxBurnTime");
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack item : items) {
            if (!item.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack itemStack = ContainerHelper.removeItem(items, slot, amount);
        if (!itemStack.isEmpty()) {
            setChanged();
        }
        return itemStack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        items.set(slot, itemStack);

        if (itemStack.getCount() > getMaxStackSize()) {
            itemStack.setCount(getContainerSize());
        }

        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        if (level == null) {
            return false;
        }

        if (level.getBlockEntity(worldPosition) != this) {
            return false;
        }
        return player.distanceToSqr(
                worldPosition.getX() + 0.5,
                worldPosition.getY() + 0.5,
                worldPosition.getZ() + 0.5
        ) <= 64;
    }

    @Override
    public void clearContent() {
        items.clear();
        setChanged();
    }
}
