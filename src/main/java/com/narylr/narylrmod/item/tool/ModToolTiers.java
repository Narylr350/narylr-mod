package com.narylr.narylrmod.item.tool;

import com.narylr.narylrmod.item.ModItems;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

public class ModToolTiers {
    // 钢工具材料，定位为钻石的工业替代路线
    public static final Tier STEEL = new Tier() {
        // 钢工具不能正确掉落的方块标签，直接使用钻石级规则
        @Override
        public TagKey<Block> getIncorrectBlocksForDrops() {
            return BlockTags.INCORRECT_FOR_DIAMOND_TOOL;
        }

        // 钢工具耐久，高于钻石的 1561
        @Override
        public int getUses() {
            return 1800;
        }

        // 钢工具基础挖掘速度，略高于钻石，但持有时会因沉重而减速
        @Override
        public float getSpeed() {
            return 8.5F;
        }

        // 钢工具基础攻击加成，和钻石同级
        @Override
        public float getAttackDamageBonus() {
            return 3.0F;
        }

        // 钢工具附魔能力，低于钻石
        @Override
        public int getEnchantmentValue() {
            return 8;
        }

        // 钢工具修复材料
        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(ModItems.STEEL_INGOT);
        }
    };
}