package com.narylr.narylrmod.item.material;

import com.narylr.narylrmod.item.HeavyItemAttributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class SteelIngotItem extends Item {
    public SteelIngotItem(Properties properties) {
        super(properties);
    }

    // 创建钢锭的属性修饰器，让钢锭拿在主手时变重但攻击更高
    public static ItemAttributeModifiers createSteelIngotAttributes() {
        return HeavyItemAttributes.createSteelIngotAttributes();
    }
}