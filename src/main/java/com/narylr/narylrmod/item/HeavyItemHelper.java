package com.narylr.narylrmod.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

// 沉重物品工具类：统一判断 ItemStack 的基础沉重减速
public class HeavyItemHelper {
    // 没有沉重效果时返回 0
    public static final double NO_HEAVY_PENALTY = 0.0D;

    // 获取物品的基础沉重减速
    // 返回正数，例如 0.15D 表示减速 15%，0.30D 表示减速 30%
    public static double getBaseHeavyPenalty(ItemStack stack) {
        if (stack.isEmpty()) {
            return NO_HEAVY_PENALTY;
        }

        Item item = stack.getItem();

        // 优先读取物品自己声明的沉重值
        if (item instanceof HeavyItem heavyItem) {
            return heavyItem.getHeavyPenalty(stack);
        }

        // 普通钢锭系工具暂时没有独立类，所以在这里统一识别
        if (isSteelIngotTool(item)) {
            return HeavyItemAttributes.STEEL_INGOT_HEAVY_PENALTY;
        }

        return NO_HEAVY_PENALTY;
    }

    // 判断是否是钢锭系普通工具
    private static boolean isSteelIngotTool(Item item) {
        return item == ModItems.STEEL_SWORD
                || item == ModItems.STEEL_PICKAXE
                || item == ModItems.STEEL_AXE
                || item == ModItems.STEEL_SHOVEL
                || item == ModItems.STEEL_HOE
                || item == ModItems.STEEL_NETHER_SWORD
                || item == ModItems.STEEL_NETHER_PICKAXE;
    }

    private HeavyItemHelper() {
        // 工具类不需要创建对象
    }
}