package com.narylr.narylrmod.enchantment;

import com.narylr.narylrmod.NarylrMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

// 模组附魔 key 统一放这里
// 1.21+ 的附魔主要由数据包 json 定义，这里只保存代码读取用的 ResourceKey
public class ModEnchantments {
    public static final ResourceKey<Enchantment> LIGHTWEIGHT = ResourceKey.create(
            Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "lightweight")
    );

    private ModEnchantments() {
        // 工具类不需要创建对象
    }
}