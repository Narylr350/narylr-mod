package com.narylr.narylrmod.item.weapon;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class SteelFryingPanItem extends SwordItem {
    // 钢平底锅未来的暴击概率，先预留为 100%
    private static final float CRITICAL_CHANCE = 1.0F;

    public SteelFryingPanItem(Tier tier, Item.Properties properties) {
        super(tier, properties);
    }

    // 预留：以后在攻击实体时调用，用来判断是否触发暴击
    public boolean shouldTriggerCriticalHit() {
        return CRITICAL_CHANCE >= 1.0F;
    }

    // 预留：以后实现钢平底锅的暴击效果，比如额外伤害、音效、粒子、击退等
    public void applyFryingPanCriticalEffect() {
        // TODO 后续实现 100% 暴击 buff
    }
}