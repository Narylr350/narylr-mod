package com.narylr.narylrmod.item;

import com.narylr.narylrmod.NarylrMod;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

// 沉重系统事件：统一处理玩家身上的沉重减速
public class HeavySystemEvents {
    // 统一沉重减速 modifier 的 id
    // 后续所有沉重来源算完以后，都只通过这个 modifier 影响玩家移动速度
    private static final ResourceLocation HEAVY_MOVEMENT_SPEED_ID =
            ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "heavy_movement_speed");

    // 注册沉重系统事件
    public static void registerEvents() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                updatePlayerHeavyPenalty(player);
            }
        });
    }

    // 更新单个玩家的沉重减速
    private static void updatePlayerHeavyPenalty(ServerPlayer player) {
        double penalty = calculateFinalHeavyPenalty(player);
        applyMovementSpeedPenalty(player, penalty);
    }

    // 计算最终沉重减速
    // v1 只计算主手物品
    // 后续钢盔甲减速、多个来源取最大值、轻盈附魔抵消都可以从这里扩展
    private static double calculateFinalHeavyPenalty(ServerPlayer player) {
        ItemStack mainHandStack = player.getMainHandItem();

        double mainHandPenalty = HeavyItemHelper.getBaseHeavyPenalty(mainHandStack);
        mainHandPenalty = applyLightweightReduction(mainHandStack, mainHandPenalty);

        return mainHandPenalty;
    }

    // 预留：轻盈附魔抵消沉重减速
    // v1 暂时不实现轻盈，所以直接返回原始减速
    private static double applyLightweightReduction(ItemStack stack, double penalty) {
        return penalty;
    }

    // 给玩家添加或移除统一移动速度 modifier
    private static void applyMovementSpeedPenalty(ServerPlayer player, double penalty) {
        AttributeInstance movementSpeed = player.getAttribute(Attributes.MOVEMENT_SPEED);

        if (movementSpeed == null) {
            return;
        }

        // 先移除旧 modifier，避免重复添加
        movementSpeed.removeModifier(HEAVY_MOVEMENT_SPEED_ID);

        // 没有沉重效果就不添加新 modifier
        if (penalty <= 0.0D) {
            return;
        }

        AttributeModifier modifier = new AttributeModifier(
                HEAVY_MOVEMENT_SPEED_ID,
                -penalty,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );

        movementSpeed.addTransientModifier(modifier);
    }

    private HeavySystemEvents() {
        // 工具类不需要创建对象
    }
}