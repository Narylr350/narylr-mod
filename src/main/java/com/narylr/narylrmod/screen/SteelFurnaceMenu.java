package com.narylr.narylrmod.screen;

import com.narylr.narylrmod.block.entity.SteelFurnaceBlockEntity;
import com.narylr.narylrmod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class SteelFurnaceMenu extends AbstractContainerMenu {
    private final Container container;

    public SteelFurnaceMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(3));
    }

    public SteelFurnaceMenu(
            int containerId,
            Inventory playerInventory,
            SteelFurnaceBlockEntity blockEntity
    ) {
        this(containerId, playerInventory, (Container) blockEntity);
    }

    public SteelFurnaceMenu(
            int containerId,
            Inventory playerInventory,
            Container container
    ) {
        super(ModMenus.STEEL_FURNACE_MENU, containerId);
        this.container = container;

        addSlot(new Slot(container, 0, 56, 17) {
            @Override
            public boolean mayPlace(ItemStack itemStack) {
                //允许输入槽输入铁锭,生钢坯
                return itemStack.is(Items.IRON_INGOT) || itemStack.is(ModItems.RAW_STEEL);
            }
        });   // 输入槽
        addSlot(new Slot(container, 1, 56, 53) {
            @Override
            public boolean mayPlace(ItemStack itemStack) {
                //允许输入煤炭
                return itemStack.is(Items.COAL);
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
                if (stackInSlot.is(Items.IRON_INGOT) || stackInSlot.is(ModItems.RAW_STEEL)) {
                    if (!moveItemStackTo(stackInSlot, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                    //煤炭到煤炭槽
                } else if (stackInSlot.is(Items.COAL)) {
                    if (!moveItemStackTo(stackInSlot, 1, 2, true)) {
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
}