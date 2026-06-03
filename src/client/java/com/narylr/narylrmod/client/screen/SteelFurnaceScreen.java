package com.narylr.narylrmod.client.screen;

import com.narylr.narylrmod.screen.SteelFurnaceMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SteelFurnaceScreen extends AbstractContainerScreen<SteelFurnaceMenu> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/gui/container/furnace.png");

    public SteelFurnaceScreen(SteelFurnaceMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        imageWidth = 176;
        imageHeight = 166;
    }

    @Override
    protected void init() {
        super.init();

        titleLabelX = (imageWidth - font.width(title)) / 2;
        titleLabelY = 8;

        inventoryLabelX = 8;
        inventoryLabelY = 72;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = leftPos;
        int y = topPos;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
