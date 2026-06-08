package com.narylr.narylrmod.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.block.Block;

public class SteelBlockItem extends BlockItem {
    public SteelBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    // 创建钢块的属性修饰器，让钢块拿在主手时更重，但攻击也更高
    public static ItemAttributeModifiers createSteelBlockAttributes() {
        return HeavyItemAttributes.createSteelBlockAttributes();
    }
}