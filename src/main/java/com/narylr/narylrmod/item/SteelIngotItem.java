package com.narylr.narylrmod.item;

import com.narylr.narylrmod.NarylrMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class SteelIngotItem extends Item {
    // 钢锭额外提供的攻击伤害
    private static final double ATTACK_DAMAGE_BONUS = 2.0D;

    // 钢锭拿在主手时降低的移动速度比例
    private static final double MOVEMENT_SPEED_PENALTY = -0.15D;

    public SteelIngotItem(Properties properties) {
        super(properties);
    }

    // 创建钢锭的属性修饰器，让钢锭拿在主手时变重但攻击更高
    public static ItemAttributeModifiers createSteelIngotAttributes() {
        return ItemAttributeModifiers.builder()
                .add(
                        Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(
                                ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "steel_ingot_attack_damage"),
                                ATTACK_DAMAGE_BONUS,
                                AttributeModifier.Operation.ADD_VALUE
                        ),
                        EquipmentSlotGroup.MAINHAND
                )
                .add(
                        Attributes.MOVEMENT_SPEED,
                        new AttributeModifier(
                                ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "steel_ingot_movement_speed"),
                                MOVEMENT_SPEED_PENALTY,
                                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                        ),
                        EquipmentSlotGroup.MAINHAND
                )
                .build();
    }
}