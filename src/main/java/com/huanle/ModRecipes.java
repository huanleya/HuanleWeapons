package com.huanle;

import com.huanle.recipes.GrinderRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * 配方注册类
 */
public class ModRecipes {
    
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, HuanleMod.MOD_ID);
    
    public static final RegistryObject<RecipeSerializer<GrinderRecipe>> GRINDING_SERIALIZER =
            SERIALIZERS.register("grinding", () -> GrinderRecipe.Serializer.INSTANCE);
    
    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}