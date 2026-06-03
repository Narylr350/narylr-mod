package com.narylr.narylrmod.block.entity;

import com.narylr.narylrmod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SteelFurnaceBlockEntity extends BlockEntity implements Container {
    //临时存储在内存中
    private final SimpleContainer inventory = new SimpleContainer(3);
    //钢熔炉中的物品创建List集合存储
    private final NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);

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

    public static void serverTick(
            Level level,
            BlockPos pos,
            BlockState state,
            SteelFurnaceBlockEntity blockEntity
    ) {
        if (level.isClientSide) {
            return;
        }

        if (blockEntity.hasRecipe()) {
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
        var input = items.get(0);
        var coal = items.get(1);
        var output = items.get(2);

        if (!canOutputSteel(output)) {
            maxProgress = 0;
            return false;
        }

        if (input.is(Items.IRON_INGOT) && coal.is(Items.COAL) && coal.getCount() >= 4) {
            maxProgress = 160;
            return true;
        }

        if (input.is(ModItems.RAW_STEEL) && coal.is(Items.COAL) && coal.getCount() >= 2) {
            maxProgress = 80;
            return true;
        }

        maxProgress = 0;
        return false;
    }

    private boolean canOutputSteel(ItemStack output) {
        if (output.isEmpty()) {
            return true;
        }

        return output.is(ModItems.STEEL_INGOT) && output.getCount() < output.getMaxStackSize();
    }

    private void craftItem() {
        var input = items.get(0);
        var coal = items.get(1);
        var output = items.get(2);

        if (input.is(Items.IRON_INGOT)) {
            input.shrink(1);
            coal.shrink(4);
        } else if (input.is(ModItems.RAW_STEEL)) {
            input.shrink(1);
            coal.shrink(2);
        } else {
            return;
        }

        if (output.isEmpty()) {
            //输出为空,new ItemStack 1个钢锭
            items.set(2, new ItemStack(ModItems.STEEL_INGOT, 1));
        } else {
            //不为空就+1
            output.grow(1);
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
