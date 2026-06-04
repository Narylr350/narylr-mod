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
    // 当前已经烧了多久（单位：游戏刻）
    private int progress = 0;
    // 总共要烧多久才能完成当前的配方（单位：游戏刻）
    private int maxProgress = 0;
    // 当前燃料燃烧时间（单位：游戏刻）
    private int burnTime = 0;
    // 燃料总共可以燃烧的时间（单位：游戏刻）
    private int maxBurnTime = 0;
    // 模式常量 - 表示钢熔炉处于空闲状态
    private static final int MODE_IDLE = 0;
    // 模式常量 - 表示钢熔炉正在处理钢铁配方
    private static final int MODE_STEEL = 1;
    // 模式常量 - 表示钢熔炉正在处理熔炼配方
    private static final int MODE_SMELTING = 2;
    // 钢熔炉当前的工作模式
    private int mode = MODE_IDLE;
    // 当前使用的钢铁配方
    private SteelFurnaceRecipe currentSteelRecipe;
    // 当前使用的熔炼配方
    private RecipeHolder<SmeltingRecipe> currentSmeltingRecipe;
    // 钢熔炉中的物品槽位列表，包含3个槽位
    private final NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
    // 同步到客户端的数据容器，用于存储进度、最大进度、燃烧时间和最大燃烧时间
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

    public ContainerData getData() {
        return data;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, SteelFurnaceBlockEntity blockEntity) {
        if (level.isClientSide) {
            return;
        }

        boolean working = blockEntity.tickWork();

        if (state.getValue(SteelFurnaceBlock.LIT) != working) {
            level.setBlock(pos, state.setValue(SteelFurnaceBlock.LIT, working), 3);
        }

        setChanged(level, pos, state);
    }

    private boolean tickWork() {
        if (mode == MODE_IDLE) {
            return tryStartSteelRecipe() || tryStartSmeltingRecipe();
        }

        if (mode == MODE_STEEL) {
            return processSteelRecipe();
        }

        if (mode == MODE_SMELTING) {
            return processSmeltingRecipe();
        }

        stopWorking();
        return false;
    }

    private boolean tryStartSteelRecipe() {
        SteelFurnaceRecipe recipe = findSteelRecipeWithCoal();

        if (recipe == null || !canOutput(recipe.result())) {
            return false;
        }

        ItemStack coal = items.get(1);
        coal.shrink(recipe.coalCount());

        currentSteelRecipe = recipe;
        currentSmeltingRecipe = null;

        mode = MODE_STEEL;
        progress = 0;
        maxProgress = recipe.cookingTime();
        burnTime = recipe.cookingTime();
        maxBurnTime = recipe.cookingTime();

        setChanged();
        return true;
    }

    private SteelFurnaceRecipe findSteelRecipeWithCoal() {
        if (level == null) {
            return null;
        }

        SteelFurnaceRecipeInput recipeInput = new SteelFurnaceRecipeInput(items.get(0), items.get(1));

        Optional<RecipeHolder<SteelFurnaceRecipe>> recipeHolder =
                level.getRecipeManager().getRecipeFor(ModRecipes.STEEL_FURNACING_TYPE, recipeInput, level);

        return recipeHolder.map(RecipeHolder::value).orElse(null);
    }

    private SteelFurnaceRecipe findSteelRecipeByInputOnly() {
        if (level == null) {
            return null;
        }

        ItemStack input = items.get(0);

        return level.getRecipeManager()
                .getAllRecipesFor(ModRecipes.STEEL_FURNACING_TYPE)
                .stream()
                .map(RecipeHolder::value)
                .filter(recipe -> recipe.ingredient().test(input))
                .findFirst()
                .orElse(null);
    }

    private boolean processSteelRecipe() {
        if (burnTime <= 0) {
            stopWorking();
            return false;
        }

        SteelFurnaceRecipe recipe = currentSteelRecipe;

        if (recipe == null) {
            recipe = findSteelRecipeByInputOnly();
            currentSteelRecipe = recipe;
        }

        if (recipe == null || !canOutput(recipe.result())) {
            stopWorking();
            return false;
        }

        maxProgress = recipe.cookingTime();

        burnTime--;
        progress++;

        if (progress >= maxProgress) {
            finishSteelRecipe();
            stopWorking();
            return tryStartSteelRecipe();
        }

        setChanged();
        return true;
    }

    private void finishSteelRecipe() {
        if (currentSteelRecipe == null) {
            return;
        }

        ItemStack input = items.get(0);
        ItemStack output = items.get(2);
        ItemStack result = currentSteelRecipe.result().copy();

        input.shrink(1);

        if (output.isEmpty()) {
            items.set(2, result);
        } else {
            output.grow(result.getCount());
        }

        setChanged();
    }

    private RecipeHolder<SmeltingRecipe> findSmeltingRecipe() {
        if (level == null) {
            return null;
        }

        SingleRecipeInput recipeInput = new SingleRecipeInput(items.get(0));

        Optional<RecipeHolder<SmeltingRecipe>> recipeHolder =
                level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, recipeInput, level);

        return recipeHolder.orElse(null);
    }

    private boolean tryStartSmeltingRecipe() {
        RecipeHolder<SmeltingRecipe> recipe = findSmeltingRecipe();

        if (recipe == null) {
            return false;
        }

        ItemStack result = recipe.value().getResultItem(level.registryAccess());

        if (!canOutput(result)) {
            return false;
        }

        if (!consumeFuel()) {
            return false;
        }

        currentSteelRecipe = null;
        currentSmeltingRecipe = recipe;

        mode = MODE_SMELTING;
        progress = 0;
        maxProgress = Math.max(recipe.value().getCookingTime() / 2, 1);

        setChanged();
        return true;
    }

    // 处理普通熔炼模式：燃料会持续燃烧，没有可熔炼输入时只消耗火焰，不推进进度
    private boolean processSmeltingRecipe() {
        if (burnTime <= 0) {
            stopWorking();
            return false;
        }

        burnTime--;

        RecipeHolder<SmeltingRecipe> recipe = findSmeltingRecipe();

        if (recipe == null) {
            progress = 0;
            maxProgress = 0;

            if (burnTime <= 0) {
                stopWorking();
                return false;
            }

            setChanged();
            return true;
        }

        ItemStack result = recipe.value().getResultItem(level.registryAccess());

        if (!canOutput(result)) {
            progress = 0;
            maxProgress = Math.max(recipe.value().getCookingTime() / 2, 1);

            if (burnTime <= 0) {
                stopWorking();
                return false;
            }

            setChanged();
            return true;
        }

        currentSmeltingRecipe = recipe;
        maxProgress = Math.max(recipe.value().getCookingTime() / 2, 1);

        progress++;

        if (progress >= maxProgress) {
            finishSmeltingRecipe();
            progress = 0;
        }

        if (burnTime <= 0) {
            stopWorking();

            if (tryStartSmeltingRecipe()) {
                return true;
            }

            return false;
        }

        setChanged();
        return true;
    }

    private boolean consumeFuel() {
        ItemStack fuel = items.get(1);

        if (fuel.isEmpty()) {
            return false;
        }

        int fuelTime = AbstractFurnaceBlockEntity.getFuel().getOrDefault(fuel.getItem(), 0);

        if (fuelTime <= 0) {
            return false;
        }

        burnTime = fuelTime;
        maxBurnTime = fuelTime;

        fuel.shrink(1);
        return true;
    }

    private void finishSmeltingRecipe() {
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

    private void stopWorking() {
        mode = MODE_IDLE;

        progress = 0;
        maxProgress = 0;
        burnTime = 0;
        maxBurnTime = 0;

        currentSteelRecipe = null;
        currentSmeltingRecipe = null;

        setChanged();
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
            itemStack.setCount(getMaxStackSize());
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
