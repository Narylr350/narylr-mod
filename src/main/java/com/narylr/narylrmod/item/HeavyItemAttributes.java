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

    // 创建钢锭本体属性：只保留攻击 +2
    // 移动速度减速交给 HeavySystemEvents 统一处理
    public static ItemAttributeModifiers createSteelIngotAttributes() {
        return ItemAttributeModifiers.builder()
                .add(
                        Attributes.ATTACK_DAMAGE,
                        createModifier("steel_ingot_attack_damage", STEEL_INGOT_ATTACK_DAMAGE_BONUS, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND
                )
                .build();
    }

    // 创建钢块本体属性：只保留攻击 +4
    // 移动速度减速交给 HeavySystemEvents 统一处理
    public static ItemAttributeModifiers createSteelBlockAttributes() {
        return ItemAttributeModifiers.builder()
                .add(
                        Attributes.ATTACK_DAMAGE,
                        createModifier("steel_block_attack_damage", STEEL_BLOCK_ATTACK_DAMAGE_BONUS, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND
                )
                .build();
    }

    // 钢锭系工具暂时不再往物品属性里直接添加移动速度 modifier
    // 工具是否沉重由 HeavyItemHelper 识别，实际减速由 HeavySystemEvents 统一处理
    public static ItemAttributeModifiers addSteelIngotToolModifier(ItemAttributeModifiers modifiers, String name) {
        return modifiers;
    }

    // 钢块系工具暂时不再往物品属性里直接添加移动速度 modifier
    // 工具是否沉重由 HeavyItemHelper 识别，实际减速由 HeavySystemEvents 统一处理
    public static ItemAttributeModifiers addSteelBlockToolModifier(ItemAttributeModifiers modifiers, String name) {
        return modifiers;
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

    private HeavyItemAttributes() {
        // 工具类不需要创建对象
    }
}