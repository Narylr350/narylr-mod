package com.narylr.narylrmod.block.entity;

import com.narylr.narylrmod.block.SteelFurnaceBlock;
import com.narylr.narylrmod.item.ModItems;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class SteelFurnaceBlockEntity extends BlockEntity implements Container {
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
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> progress = value;
                case 1 -> maxProgress = value;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    private SteelFurnaceRecipe currentRecipe;
    //当前已经烧了多久
    private int progress = 0;
    //总共要烧多久
    private int maxProgress = 0;

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

    public static void serverTick(
            Level level,
            BlockPos pos,
            BlockState state,
            SteelFurnaceBlockEntity blockEntity
    ) {
        if (level.isClientSide) {
            return;
        }

        boolean working = blockEntity.hasRecipe();

        if (state.getValue(SteelFurnaceBlock.LIT) != working) {
            level.setBlock(pos, state.setValue(SteelFurnaceBlock.LIT, working), 3);
        }

        if (working) {
            blockEntity.progress++;

            if (blockEntity.progress >= blockEntity.maxProgress) {
                blockEntity.craftItem();
                blockEntity.progress = 0;
            }

            setChanged(level, pos, state);
        } else {
            if (blockEntity.progress != 0) {
                blockEntity.progress = 0;
                setChanged(level, pos, state);
            }
        }
    }

    private boolean hasRecipe() {
        if (level == null) {
            maxProgress = 0;
            currentRecipe = null;
            return false;
        }

        SteelFurnaceRecipeInput recipeInput = new SteelFurnaceRecipeInput(items.get(0), items.get(1));

        Optional<RecipeHolder<SteelFurnaceRecipe>> recipeHolder = level
                .getRecipeManager()
                .getRecipeFor(ModRecipes.STEEL_FURNACING_TYPE, recipeInput, level);

        if (recipeHolder.isEmpty()) {
            maxProgress = 0;
            currentRecipe = null;
            return false;
        }

        SteelFurnaceRecipe recipe = recipeHolder.get().value();
        ItemStack result = recipe.result();

        if (!canOutput(result)) {
            maxProgress = 0;
            currentRecipe = null;
            return false;
        }

        maxProgress = recipe.cookingTime();
        currentRecipe = recipe;
        return true;
    }

    private boolean canOutput(ItemStack result) {
        ItemStack output = items.get(2);

        if (output.isEmpty()) {
            return true;
        }

        return ItemStack.isSameItemSameComponents(output, result)
                && output.getCount() + result.getCount() <= output.getMaxStackSize();
    }

    private void craftItem() {
        if (currentRecipe == null) {
            return;
        }

        ItemStack input = items.get(0);
        ItemStack coal = items.get(1);
        ItemStack output = items.get(2);
        ItemStack result = currentRecipe.result().copy();

        input.shrink(1);
        coal.shrink(currentRecipe.coalCount());

        if (output.isEmpty()) {
            items.set(2, result);
        } else {
            output.grow(result.getCount());
        }

        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        ContainerHelper.saveAllItems(tag, items, registries);
        tag.putInt("progress", progress);
        tag.putInt("maxProgress", maxProgress);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        ContainerHelper.loadAllItems(tag, items, registries);
        progress = tag.getInt("progress");
        maxProgress = tag.getInt("maxProgress");
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
