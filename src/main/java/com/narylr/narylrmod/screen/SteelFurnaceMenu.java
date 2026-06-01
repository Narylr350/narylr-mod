package com.narylr.narylrmod.screen;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SteelFurnaceMenu extends AbstractContainerMenu {

    public SteelFurnaceMenu(int containerId, Inventory playerInventory) {
        super(ModMenus.STEEL_FURNACE_MENU, containerId);
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