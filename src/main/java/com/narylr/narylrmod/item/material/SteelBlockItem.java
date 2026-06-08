package com.narylr.narylrmod.item.material;

import com.narylr.narylrmod.item.HeavyItem;
import com.narylr.narylrmod.item.HeavyItemAttributes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.block.Block;

public class SteelBlockItem extends BlockItem implements HeavyItem {
    public SteelBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    // 钢块本体属于钢块系沉重物品，基础减速 30%
    @Override
    public double getHeavyPenalty(ItemStack stack) {
        return HeavyItemAttributes.STEEL_BLOCK_HEAVY_PENALTY;
    }

    // 创建钢块的属性修饰器，让钢块拿在主手时更重，但攻击也更高
    public static ItemAttributeModifiers createSteelBlockAttributes() {
        return HeavyItemAttributes.createSteelBlockAttributes();
    }
}