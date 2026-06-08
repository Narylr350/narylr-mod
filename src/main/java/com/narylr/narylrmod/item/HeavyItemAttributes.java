package com.narylr.narylrmod.item;

import com.narylr.narylrmod.NarylrMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class HeavyItemAttributes {
    // 钢锭系沉重减速：主手持有时移动速度降低 15%
    public static final double STEEL_INGOT_HEAVY_PENALTY = 0.15D;

    // 钢块系沉重减速：主手持有时移动速度降低 30%
    public static final double STEEL_BLOCK_HEAVY_PENALTY = 0.30D;

    // 钢锭本体主手攻击加成
    public static final double STEEL_INGOT_ATTACK_DAMAGE_BONUS = 2.0D;

    // 钢块本体主手攻击加成，定位为比钢锭更重、更痛
    public static final double STEEL_BLOCK_ATTACK_DAMAGE_BONUS = 4.0D;

    // 创建钢锭本体属性：攻击 +2，移动速度 -15%
    public static ItemAttributeModifiers createSteelIngotAttributes() {
        return ItemAttributeModifiers.builder()
                .add(
                        Attributes.ATTACK_DAMAGE,
                        createModifier("steel_ingot_attack_damage", STEEL_INGOT_ATTACK_DAMAGE_BONUS, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND
                )
                .add(
                        Attributes.MOVEMENT_SPEED,
                        createModifier("steel_ingot_movement_speed", -STEEL_INGOT_HEAVY_PENALTY, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                        EquipmentSlotGroup.MAINHAND
                )
                .build();
    }

    // 创建钢块本体属性：攻击 +4，移动速度 -30%
    public static ItemAttributeModifiers createSteelBlockAttributes() {
        return ItemAttributeModifiers.builder()
                .add(
                        Attributes.ATTACK_DAMAGE,
                        createModifier("steel_block_attack_damage", STEEL_BLOCK_ATTACK_DAMAGE_BONUS, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND
                )
                .add(
                        Attributes.MOVEMENT_SPEED,
                        createModifier("steel_block_movement_speed", -STEEL_BLOCK_HEAVY_PENALTY, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                        EquipmentSlotGroup.MAINHAND
                )
                .build();
    }

    // 给钢锭系工具添加主手沉重属性
    public static ItemAttributeModifiers addSteelIngotToolModifier(ItemAttributeModifiers modifiers, String name) {
        return addHeavyMainHandModifier(modifiers, name, STEEL_INGOT_HEAVY_PENALTY);
    }

    // 给钢块系工具添加主手沉重属性，后续钢块狼牙棒 / 钢块工具使用
    public static ItemAttributeModifiers addSteelBlockToolModifier(ItemAttributeModifiers modifiers, String name) {
        return addHeavyMainHandModifier(modifiers, name, STEEL_BLOCK_HEAVY_PENALTY);
    }

    // 底层通用方法：给主手物品添加移动速度惩罚
    private static ItemAttributeModifiers addHeavyMainHandModifier(
            ItemAttributeModifiers modifiers,
            String name,
            double penalty
    ) {
        return modifiers.withModifierAdded(
                Attributes.MOVEMENT_SPEED,
                createModifier(name + "_movement_speed", -penalty, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                EquipmentSlotGroup.MAINHAND
        );
    }

    // 创建属性修饰器，统一生成命名空间 id，避免每个类重复写 ResourceLocation
    private static AttributeModifier createModifier(
            String name,
            double amount,
            AttributeModifier.Operation operation
    ) {
        return new AttributeModifier(
                ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, name),
                amount,
                operation
        );
    }
}