package com.narylr.narylrmod.client;

import com.narylr.narylrmod.client.screen.SteelFurnaceScreen;
import com.narylr.narylrmod.screen.ModMenus;
import com.narylr.narylrmod.screen.SteelFurnaceMenu;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class NarylrModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.

        MenuScreens.register(ModMenus.STEEL_FURNACE_MENU, new MenuScreens.ScreenConstructor<SteelFurnaceMenu, SteelFurnaceScreen>() {
            @Override
            public SteelFurnaceScreen create(SteelFurnaceMenu abstractContainerMenu, Inventory inventory, Component component) {
                return new SteelFurnaceScreen(abstractContainerMenu, inventory, component);
            }
        });
    }
}