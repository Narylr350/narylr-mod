package com.narylr.narylrmod.client.tooltip;

import com.narylr.narylrmod.enchantment.ModEnchantments;
import com.narylr.narylrmod.item.HeavyItemHelper;
import com.narylr.narylrmod.item.ModItems;
import com.narylr.narylrmod.tag.ModTags;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class SteelItemTooltip {
    public static void register() {
        ItemTooltipCallback.EVENT.register((itemStack, tooltipContext, tooltipType, tooltip) -> {
            double baseHeavyPenalty = HeavyItemHelper.getBaseHeavyPenalty(itemStack);

            if (baseHeavyPenalty > 0.0D) {
                int lightweightLevel = getLightweightLevel(itemStack);
                double finalHeavyPenalty = applyLightweightReduction(baseHeavyPenalty, lightweightLevel);

                int basePercent = toPercent(baseHeavyPenalty);
                int finalPercent = toPercent(finalHeavyPenalty);

                if (lightweightLevel >= 3 || finalHeavyPenalty <= 0.0D) {
                    tooltip.add(Component.translatable("tooltip.narylr-mod.heavy_item.lightweight_full", basePercent)
                            .withStyle(ChatFormatting.AQUA));
                } else if (lightweightLevel > 0) {
                    tooltip.add(Component.translatable("tooltip.narylr-mod.heavy_item.heavy_reduced", finalPercent, basePercent)
                            .withStyle(ChatFormatting.AQUA));
                } else {
                    tooltip.add(Component.translatable("tooltip.narylr-mod.heavy_item.heavy", basePercent)
                            .withStyle(ChatFormatting.RED));
                }
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

    // 根据轻盈等级计算实际剩余沉重减速
    private static double applyLightweightReduction(double penalty, int lightweightLevel) {
        if (penalty <= 0.0D || lightweightLevel <= 0) {
            return penalty;
        }

        int clampedLevel = Math.min(lightweightLevel, 3);
        double reductionRate = clampedLevel / 3.0D;

        return penalty * (1.0D - reductionRate);
    }

    // 获取物品上的轻盈附魔等级
    private static int getLightweightLevel(ItemStack stack) {
        if (stack.isEmpty() || Minecraft.getInstance().level == null) {
            return 0;
        }

        Registry<Enchantment> enchantmentRegistry = Minecraft.getInstance()
                .level
                .registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT);

        Holder.Reference<Enchantment> lightweight = enchantmentRegistry.getHolderOrThrow(
                ModEnchantments.LIGHTWEIGHT
        );

        return EnchantmentHelper.getItemEnchantmentLevel(lightweight, stack);
    }

    // 小数转百分比
    private static int toPercent(double value) {
        return (int) Math.round(value * 100.0D);
    }
}