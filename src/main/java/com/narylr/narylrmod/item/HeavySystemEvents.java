package com.narylr.narylrmod.item;

import com.narylr.narylrmod.NarylrMod;
import com.narylr.narylrmod.enchantment.ModEnchantments;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

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

    // 计算钢甲沉重减速
    // 钢甲内部按部位累加：头盔 2%，胸甲 5%，护腿 5%，靴子 3%，满套 15%
    private static double calculateFinalHeavyPenalty(ServerPlayer player) {
        ItemStack mainHandStack = player.getMainHandItem();

        double mainHandPenalty = HeavyItemHelper.getBaseHeavyPenalty(mainHandStack);
        mainHandPenalty = applyLightweightReduction(player, mainHandStack, mainHandPenalty);

        double armorPenalty = calculateSteelArmorPenalty(player);

        return Math.max(mainHandPenalty, armorPenalty);
    }

    // 计算钢甲沉重减速
    // 钢甲内部按部位累加：头盔 2%，胸甲 5%，护腿 5%，靴子 3%，满套 15%
    private static double calculateSteelArmorPenalty(ServerPlayer player) {
        double penalty = 0.0D;

        if (isItemInSlot(player, EquipmentSlot.HEAD, ModItems.STEEL_HELMET)) {
            penalty += HeavyItemAttributes.STEEL_HELMET_HEAVY_PENALTY;
        }

        if (isItemInSlot(player, EquipmentSlot.CHEST, ModItems.STEEL_CHESTPLATE)) {
            penalty += HeavyItemAttributes.STEEL_CHESTPLATE_HEAVY_PENALTY;
        }

        if (isItemInSlot(player, EquipmentSlot.LEGS, ModItems.STEEL_LEGGINGS)) {
            penalty += HeavyItemAttributes.STEEL_LEGGINGS_HEAVY_PENALTY;
        }

        if (isItemInSlot(player, EquipmentSlot.FEET, ModItems.STEEL_BOOTS)) {
            penalty += HeavyItemAttributes.STEEL_BOOTS_HEAVY_PENALTY;
        }

        return Math.min(penalty, HeavyItemAttributes.FULL_STEEL_ARMOR_HEAVY_PENALTY);
    }

    // 判断玩家指定装备槽是否穿着指定物品
    private static boolean isItemInSlot(ServerPlayer player, EquipmentSlot slot, Item item) {
        ItemStack stack = player.getItemBySlot(slot);
        return !stack.isEmpty() && stack.is(item);
    }

    // 轻盈附魔抵消沉重减速
    // 轻盈 I 抵消 1/3，轻盈 II 抵消 2/3，轻盈 III 完全抵消
    private static double applyLightweightReduction(ServerPlayer player, ItemStack stack, double penalty) {
        if (penalty <= 0.0D || stack.isEmpty()) {
            return penalty;
        }

        int lightweightLevel = getLightweightLevel(player, stack);

        if (lightweightLevel <= 0) {
            return penalty;
        }

        int clampedLevel = Math.min(lightweightLevel, 3);
        double reductionRate = clampedLevel / 3.0D;

        return penalty * (1.0D - reductionRate);
    }

    // 获取物品上的轻盈附魔等级
    private static int getLightweightLevel(ServerPlayer player, ItemStack stack) {
        Registry<Enchantment> enchantmentRegistry = player.registryAccess().registryOrThrow(Registries.ENCHANTMENT);

        Holder.Reference<Enchantment> lightweight = enchantmentRegistry.getHolderOrThrow(
                ModEnchantments.LIGHTWEIGHT
        );

        return EnchantmentHelper.getItemEnchantmentLevel(lightweight, stack);
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