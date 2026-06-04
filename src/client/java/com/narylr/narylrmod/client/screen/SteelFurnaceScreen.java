package com.narylr.narylrmod.client.screen;

import com.narylr.narylrmod.screen.SteelFurnaceMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SteelFurnaceScreen extends AbstractContainerScreen<SteelFurnaceMenu> {
    public static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("narylr-mod", "textures/gui/container/furnace.png");
    public static final ResourceLocation BURN_PROGRESS_SPRITE  =
            ResourceLocation.fromNamespaceAndPath("narylr-mod", "container/steel_furnace/burn_progress");
    private static final ResourceLocation LIT_PROGRESS_SPRITE =
            ResourceLocation.fromNamespaceAndPath("narylr-mod", "container/steel_furnace/lit_progress");

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

        int burnProgress = menu.getScaledBurnProgress();
        if (burnProgress > 0) {
            guiGraphics.blitSprite(
                    LIT_PROGRESS_SPRITE,
                    14, 14,
                    0, 14 - burnProgress,
                    x + 56, y + 36 + 14 - burnProgress,
                    14, burnProgress
            );
        }

        int progress = menu.getScaledProgress();
        if (progress > 0) {
            guiGraphics.blitSprite(
                    BURN_PROGRESS_SPRITE,
                    24, 16,
                    0, 0,
                    x + 79, y + 34,
                    progress, 16
            );
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
