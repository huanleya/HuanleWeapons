package com.huanle;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * 注册恐惧药水相关的酿造配方
 */
public class ModBrewingRecipes {

    /**
     * 注册所有酿造配方
     */
    public static void register(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // 注册恐惧药水酿造配方
            // 水瓶 + 恐惧之泪 = 恐惧药水
            BrewingRecipeRegistry.addRecipe(
                Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER)),
                Ingredient.of(ModItems.FEAR_TEAR.get()),
                PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.FEAR_POTION.get())
            );

            // 恐惧药水 + 红石 = 长效恐惧药水
            BrewingRecipeRegistry.addRecipe(
                Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.FEAR_POTION.get())),
                Ingredient.of(Items.REDSTONE),
                PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.LONG_FEAR_POTION.get())
            );

            // 恐惧药水 + 荧石粉 = 强效恐惧药水
            BrewingRecipeRegistry.addRecipe(
                Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.FEAR_POTION.get())),
                Ingredient.of(Items.GLOWSTONE_DUST),
                PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.STRONG_FEAR_POTION.get())
            );
        });
    }
}
