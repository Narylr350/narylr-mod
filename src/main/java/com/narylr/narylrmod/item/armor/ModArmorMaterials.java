package com.narylr.narylrmod.item.armor;

import com.narylr.narylrmod.NarylrMod;
import com.narylr.narylrmod.item.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.Map;

public class ModArmorMaterials {
    // 钢盔甲材料，定位为钻石盔甲的工业替代路线
    public static final Holder<ArmorMaterial> STEEL = Holder.direct(new ArmorMaterial(
            Map.of(
                    ArmorItem.Type.BOOTS, 3,
                    ArmorItem.Type.LEGGINGS, 6,
                    ArmorItem.Type.CHESTPLATE, 8,
                    ArmorItem.Type.HELMET, 3,
                    ArmorItem.Type.BODY, 11
            ),
            8,
            SoundEvents.ARMOR_EQUIP_IRON,
            () -> Ingredient.of(ModItems.STEEL_INGOT),
            List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "steel"))),
            1.5F,
            0.0F
    ));
    // 下界合金钢盔甲材料，比普通钢甲更偏防御
    public static final Holder<ArmorMaterial> STEEL_NETHER = Holder.direct(new ArmorMaterial(
            Map.of(
                    ArmorItem.Type.BOOTS, 4,
                    ArmorItem.Type.LEGGINGS, 7,
                    ArmorItem.Type.CHESTPLATE, 9,
                    ArmorItem.Type.HELMET, 4,
                    ArmorItem.Type.BODY, 12
            ),
            8,
            SoundEvents.ARMOR_EQUIP_IRON,
            () -> Ingredient.of(ModItems.STEEL_NETHER_INGOT),
            List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "STEEL_NETHER"))),
            2.0F,
            0.0F
    ));
}