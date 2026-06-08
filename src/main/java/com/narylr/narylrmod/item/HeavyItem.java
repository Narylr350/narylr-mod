package com.narylr.narylrmod.item;

import net.minecraft.world.item.ItemStack;

// 表示这个物品拥有“沉重”属性，沉重值由具体物品自己决定
public interface HeavyItem {
    // 返回这个物品的基础移动速度惩罚，正数表示减速比例
    // 例如 0.15D 表示移动速度降低 15%，0.30D 表示移动速度降低 30%
    double getHeavyPenalty(ItemStack stack);
}