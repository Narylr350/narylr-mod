package com.narylr.narylrmod.tag;

import com.narylr.narylrmod.NarylrMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {
    // 炼钢配方可使用的碳源材料
    public static final TagKey<Item> STEEL_CARBON_SOURCES = TagKey.create(
            Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "steel_carbon_sources")
    );
}
