package com.narylr.narylrmod.item.weapon;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class SteelMorningStarItem extends SwordItem {
    // 钢狼牙棒未来的流血持续时间，单位可以先按 tick 理解，20 tick = 1 秒
    private static final int BLEED_DURATION_TICKS = 100;

    // 钢狼牙棒未来的流血伤害，先只预留字段，不真正扣血
    private static final float BLEED_DAMAGE = 1.0F;

    public SteelMorningStarItem(Tier tier, Item.Properties properties) {
        super(tier, properties);
    }

    // 预留：以后在攻击实体时调用，用来判断是否可以触发流血
    public boolean canApplyBleeding() {
        return true;
    }

    // 预留：以后实现流血效果，比如持续掉血、粒子、音效等
    public void applyBleedingEffect() {
        // TODO 后续实现流血 buff
    }
}