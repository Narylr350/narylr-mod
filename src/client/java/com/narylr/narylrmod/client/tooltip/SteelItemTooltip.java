package com.narylr.narylrmod.client.tooltip;

import com.narylr.narylrmod.item.HeavyItemHelper;
import com.narylr.narylrmod.item.ModItems;
import com.narylr.narylrmod.tag.ModTags;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class SteelItemTooltip {
    public static void register() {
        ItemTooltipCallback.EVENT.register((itemStack, tooltipContext, tooltipType, tooltip) -> {
            double heavyPenalty = HeavyItemHelper.getBaseHeavyPenalty(itemStack);

            if (heavyPenalty > 0.0D) {
                int percent = (int) Math.round(heavyPenalty * 100.0D);

                tooltip.add(Component.translatable("tooltip.narylr-mod.heavy_item.heavy", percent)
                        .withStyle(ChatFormatting.RED));
            }

            if (itemStack.is(ModTags.STEEL_TOOLS)) {
                tooltip.add(Component.translatable("tooltip.narylr-mod.steel_tool.durable")
                        .withStyle(ChatFormatting.GREEN));
            }

            if (itemStack.is(ModItems.STEEL_INGOT)
                    || itemStack.is(ModItems.STEEL_BLOCK_ITEM)) {
                tooltip.add(Component.translatable("tooltip.narylr-mod.steel_material.hard")
                        .withStyle(ChatFormatting.GREEN));
            }

            if (itemStack.is(ModItems.STEEL_HELMET)) {
                tooltip.add(Component.translatable("tooltip.narylr-mod.steel_armor.heavy", 2)
                        .withStyle(ChatFormatting.RED));
            }

            if (itemStack.is(ModItems.STEEL_CHESTPLATE)) {
                tooltip.add(Component.translatable("tooltip.narylr-mod.steel_armor.heavy", 5)
                        .withStyle(ChatFormatting.RED));
            }

            if (itemStack.is(ModItems.STEEL_LEGGINGS)) {
                tooltip.add(Component.translatable("tooltip.narylr-mod.steel_armor.heavy", 5)
                        .withStyle(ChatFormatting.RED));
            }

            if (itemStack.is(ModItems.STEEL_BOOTS)) {
                tooltip.add(Component.translatable("tooltip.narylr-mod.steel_armor.heavy", 3)
                        .withStyle(ChatFormatting.RED));
            }

            if (itemStack.is(ModTags.STEEL_ARMORS)) {
                tooltip.add(Component.translatable("tooltip.narylr-mod.steel_armor.durable")
                        .withStyle(ChatFormatting.GREEN));
                tooltip.add(Component.translatable("tooltip.narylr-mod.steel_armor.rule")
                        .withStyle(ChatFormatting.YELLOW));
            }
        });
    }

    private SteelItemTooltip() {
        // 工具类不需要创建对象
    }
}