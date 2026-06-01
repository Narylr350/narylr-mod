package com.narylr.narylrmod.screen;

import com.narylr.narylrmod.NarylrMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

public class ModMenus {
    public static final MenuType<SteelFurnaceMenu> STEEL_FURNACE_MENU = Registry.register(
            BuiltInRegistries.MENU,
            ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "steel_furnace"),
            new MenuType<>(new MenuType.MenuSupplier<SteelFurnaceMenu>() {
                @Override
                public SteelFurnaceMenu create(int i, Inventory inventory) {
                    return new SteelFurnaceMenu(i, inventory);
                }
            }, null)
    );

    public static void registerMenus() {
        NarylrMod.LOGGER.info("注册钢熔炉菜单");
    }
}
