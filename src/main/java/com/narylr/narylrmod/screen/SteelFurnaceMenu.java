package com.narylr.narylrmod.screen;

import com.narylr.narylrmod.block.entity.SteelFurnaceBlockEntity;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
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
        this(containerId, playerInventory, blockEntity.getInventory());
    }

    public SteelFurnaceMenu(int containerId, Inventory playerInventory, Container container) {
        super(ModMenus.STEEL_FURNACE_MENU, containerId);
        this.container = container;

        this.addSlot(new Slot(container, 0, 56, 17));   // 输入槽
        this.addSlot(new Slot(container, 1, 56, 53));   // 煤炭槽
        this.addSlot(new Slot(container, 2, 116, 35));  // 输出槽

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    //添加背包27格
    private void addPlayerInventory(Inventory playerInventory) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                this.addSlot(new Slot(
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
            this.addSlot(new Slot(
                    playerInventory,
                    column,
                    8 + column * 18,
                    142
            ));
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}