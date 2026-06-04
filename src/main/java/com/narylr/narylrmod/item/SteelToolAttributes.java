package com.narylr.narylrmod.item;

import com.narylr.narylrmod.NarylrMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class SteelToolAttributes {
    // 钢工具主手持有时的移动速度惩罚
    private static final double MOVEMENT_SPEED_PENALTY = -0.05D;

    // 给钢工具添加主手沉重属性
    public static ItemAttributeModifiers addHeavyMainHandModifier(ItemAttributeModifiers modifiers, String name) {
        return modifiers.withModifierAdded(
                Attributes.MOVEMENT_SPEED,
                new AttributeModifier(
                        ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, name + "_movement_speed"),
                        MOVEMENT_SPEED_PENALTY,
                        AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                ),
                EquipmentSlotGroup.MAINHAND
        );
    }
}