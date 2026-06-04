package com.narylr.narylrmod.client.compat.jade;

import com.narylr.narylrmod.block.SteelFurnaceBlock;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class SteelCraftJadePlugin implements IWailaPlugin {

    // 注册服务端数据同步
    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(
                SteelFurnaceJadeProvider.INSTANCE,
                SteelFurnaceBlock.class
        );
    }

    // 注册客户端方块提示组件
    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(
                SteelFurnaceJadeProvider.INSTANCE,
                SteelFurnaceBlock.class
        );
    }
}