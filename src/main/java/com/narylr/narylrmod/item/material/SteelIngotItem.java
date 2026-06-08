package com.narylr.narylrmod.item.material;

import com.narylr.narylrmod.item.HeavyItem;
import com.narylr.narylrmod.item.HeavyItemAttributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class SteelIngotItem extends Item implements HeavyItem {
    public SteelIngotItem(Properties properties) {
        super(properties);
    }

    // 钢锭本体属于钢锭系沉重物品，基础减速 15%
    @Override
    public double getHeavyPenalty(ItemStack stack) {
        return HeavyItemAttributes.STEEL_INGOT_HEAVY_PENALTY;
    }

    // 创建钢锭的属性修饰器，让钢锭拿在主手时变重但攻击更高
    public static ItemAttributeModifiers createSteelIngotAttributes() {
        return HeavyItemAttributes.createSteelIngotAttributes();
    }
}