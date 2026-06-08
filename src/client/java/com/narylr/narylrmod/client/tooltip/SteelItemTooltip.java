package com.narylr.narylrmod.client.tooltip;

import com.narylr.narylrmod.item.ModItems;
import com.narylr.narylrmod.tag.ModTags;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class SteelItemTooltip {
    // 注册钢制物品提示文本
    public static void register() {
        ItemTooltipCallback.EVENT.register((itemStack, tooltipContext, tooltipType, tooltip) -> {
            if (itemStack.is(ModTags.STEEL_TOOLS)) {
                tooltip.add(Component.translatable("tooltip.narylr-mod.steel_tool.heavy")
                        .withStyle(ChatFormatting.RED));
                tooltip.add(Component.translatable("tooltip.narylr-mod.steel_tool.durable")
                        .withStyle(ChatFormatting.GREEN));
            }

            if (itemStack.is(ModItems.STEEL_INGOT)) {
                tooltip.add(Component.translatable("tooltip.narylr-mod.steel_ingot.heavy")
                        .withStyle(ChatFormatting.RED));
                tooltip.add(Component.translatable("tooltip.narylr-mod.steel_ingot.hard")
                        .withStyle(ChatFormatting.GREEN));
            }

            if (itemStack.is(ModItems.STEEL_BLOCK_ITEM)) {
                tooltip.add(Component.translatable("tooltip.narylr-mod.steel_ingot.heavy")
                        .withStyle(ChatFormatting.RED));
                tooltip.add(Component.translatable("tooltip.narylr-mod.steel_ingot.hard")
                        .withStyle(ChatFormatting.GREEN));
            }
        });
    }
}