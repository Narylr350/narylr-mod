package com.narylr.narylrmod.screen;

import com.narylr.narylrmod.block.entity.SteelFurnaceBlockEntity;
import com.narylr.narylrmod.item.ModItems;
import com.narylr.narylrmod.recipe.ModRecipes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.jetbrains.annotations.NotNull;

public class SteelFurnaceMenu extends AbstractContainerMenu {
    private final Container container;
    private final ContainerData data;
    private final Level level;

    public SteelFurnaceMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(3), new SimpleContainerData(4));
    }

    public SteelFurnaceMenu(
            int containerId,
            Inventory playerInventory,
            SteelFurnaceBlockEntity blockEntity
    ) {
        this(containerId, playerInventory, blockEntity, blockEntity.getData());
    }

    public SteelFurnaceMenu(
            int containerId,
            Inventory playerInventory,
            Container container,
            ContainerData data
    ) {
        super(ModMenus.STEEL_FURNACE_MENU, containerId);
        this.container = container;
        this.data = data;
        level = playerInventory.player.level();

        addDataSlots(data);

        addSlot(new Slot(container, 0, 56, 17) {
            @Override
            public boolean mayPlace(ItemStack itemStack) {
                //允许输入槽输入铁锭,生钢坯
                return canPlaceInput(itemStack);
            }
        });   // 输入槽
        addSlot(new Slot(container, 1, 56, 53) {
            @Override
            public boolean mayPlace(ItemStack itemStack) {
                //允许输入煤炭
                return itemStack.is(Items.COAL) || AbstractFurnaceBlockEntity.isFuel(itemStack);
            }
        });   // 煤炭槽
        addSlot(new Slot(container, 2, 116, 35) {
            @Override
            public boolean mayPlace(ItemStack itemStack) {
                return false;
            }
        });  // 输出槽

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    private boolean canPlaceInput(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return false;
        }

        return hasSteelFurnaceRecipe(itemStack) || hasSmeltingRecipe(itemStack);
    }

    private boolean hasSteelFurnaceRecipe(ItemStack stack) {
        return level.getRecipeManager()
                .getAllRecipesFor(ModRecipes.STEEL_FURNACING_TYPE)
                .stream()
                .anyMatch(recipeHolder -> recipeHolder.value().ingredient().test(stack));
    }

    private boolean hasSmeltingRecipe(ItemStack stack) {
        return level.getRecipeManager()
                .getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(stack), level)
                .isPresent();
    }

    //添加背包27格
    private void addPlayerInventory(Inventory playerInventory) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(
                        playerInventory,
                        column + row * 9 + 9,
                        8 + column * 18,
                        84 + row * 18
                ));
            }
        }
    }

    //添加物品栏9格
    private void addPlayerHotbar(Inventory playerInventory) {
        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(
                    playerInventory,
                    column,
                    8 + column * 18,
                    142
            ));
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int index) {
        ItemStack originalStack = ItemStack.EMPTY;
        Slot slot = slots.get(index);

        if (slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            originalStack = stackInSlot.copy();

            //机器槽 0 1 2 其余为玩家背包
            //机器到玩家背包
            if (index < 3) {
                if (!moveItemStackTo(stackInSlot, 3, 39, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                //铁锭,生钢坯到输入槽
                if (canPlaceInput(stackInSlot)) {
                    if (!moveItemStackTo(stackInSlot, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                    //煤炭到煤炭槽
                } else if (stackInSlot.is(Items.COAL) || AbstractFurnaceBlockEntity.isFuel(stackInSlot)) {
                    if (!moveItemStackTo(stackInSlot, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                    //玩家背包到快捷栏
                } else if (index < 30) {
                    if (!moveItemStackTo(stackInSlot, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                    //快捷栏到玩家背包
                } else if (index < 39) {
                    if (!moveItemStackTo(stackInSlot, 3, 30, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (stackInSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stackInSlot.getCount() == originalStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stackInSlot);
        }
        return originalStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

    public int getProgress() {
        return data.get(0);
    }

    public int getMaxProgress() {
        return data.get(1);
    }

    public int getScaledProgress(){
        int progress = getProgress();
        int maxProgress = getMaxProgress();
        int arrowWidth = 24;

        if (maxProgress == 0 || progress ==0) {
            return 0;
        }

        return progress * arrowWidth / maxProgress;
    }

    public int getBurnTime() {
        return data.get(2);
    }

    public int getMaxBurnTime() {
        return data.get(3);
    }

    public int getScaledBurnProgress() {
        int burnTime = getBurnTime();
        int maxBurnTime = getMaxBurnTime();
        int flameHeight = 13;

        if (maxBurnTime == 0 || burnTime == 0) {
            return 0;
        }

        return burnTime * flameHeight / maxBurnTime;
    }
}