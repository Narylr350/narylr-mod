package com.narylr.narylrmod.client.compat.jade;

import com.narylr.narylrmod.NarylrMod;
import com.narylr.narylrmod.block.entity.SteelFurnaceBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum SteelFurnaceJadeProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    // Jade 组件唯一标识
    private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(
            NarylrMod.MOD_ID,
            "steel_furnace"
    );

    // 给 Jade 客户端提示框添加钢熔炉状态文本
    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        CompoundTag data = accessor.getServerData();

        int mode = data.getInt("Mode");
        int progress = data.getInt("Progress");
        int maxProgress = data.getInt("MaxProgress");
        int burnTime = data.getInt("BurnTime");

        if (mode == 1) {
            tooltip.add(Component.literal("模式：炼钢"));
        } else if (mode == 2) {
            tooltip.add(Component.literal("模式：熔炼"));
        } else {
            tooltip.add(Component.literal("模式：空闲"));
        }

        if (maxProgress > 0) {
            int percent = progress * 100 / maxProgress;
            tooltip.add(Component.literal("进度：" + percent + "%"));
        }

        if (burnTime > 0) {
            int seconds = burnTime / 20;
            tooltip.add(Component.literal("燃烧剩余：" + seconds + "秒"));
        }
    }

    // 把服务端钢熔炉数据同步给 Jade 客户端显示
    @Override
    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        if (accessor.getBlockEntity() instanceof SteelFurnaceBlockEntity blockEntity) {
            data.putInt("Mode", blockEntity.getMode());
            data.putInt("Progress", blockEntity.getProgress());
            data.putInt("MaxProgress", blockEntity.getMaxProgress());
            data.putInt("BurnTime", blockEntity.getBurnTime());
            data.putInt("MaxBurnTime", blockEntity.getMaxBurnTime());
        }
    }

    // 获取 Jade 组件唯一标识
    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}