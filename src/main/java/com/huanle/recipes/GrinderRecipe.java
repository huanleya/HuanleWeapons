package com.huanle.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.huanle.HuanleMod;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/**
 * 磨粉机配方类
 * 定义磨粉机的配方系统
 */
public class GrinderRecipe implements Recipe<SimpleContainer> {
    
    private final NonNullList<Ingredient> inputItems;
    private final ItemStack output;
    private final ResourceLocation id;
    
    public GrinderRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> inputItems) {
        this.id = id;
        this.output = output;
        this.inputItems = inputItems;
    }
    
    @Override
    public boolean matches(SimpleContainer container, Level level) {
        if (level.isClientSide()) {
            return false;
        }
        
        return inputItems.get(0).test(container.getItem(0));
    }
    
    @Override
    public ItemStack assemble(SimpleContainer container, RegistryAccess registryAccess) {
        return output.copy();
    }
    
    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }
    
    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return output.copy();
    }
    
    @Override
    public ResourceLocation getId() {
        return id;
    }
    
    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }
    
    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }
    
    public static class Type implements RecipeType<GrinderRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "grinding";
    }
    
    public static class Serializer implements RecipeSerializer<GrinderRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(HuanleMod.MOD_ID, "grinding");
        
        @Override
        public GrinderRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            
            JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);
            
            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }
            
            return new GrinderRecipe(recipeId, output, inputs);
        }
        
        @Override
        public @Nullable GrinderRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buffer.readInt(), Ingredient.EMPTY);
            
            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(buffer));
            }
            
            ItemStack output = buffer.readItem();
            return new GrinderRecipe(recipeId, output, inputs);
        }
        
        @Override
        public void toNetwork(FriendlyByteBuf buffer, GrinderRecipe recipe) {
            buffer.writeInt(recipe.inputItems.size());
            
            for (Ingredient ingredient : recipe.inputItems) {
                ingredient.toNetwork(buffer);
            }
            
            buffer.writeItemStack(recipe.getResultItem(null), false);
        }
    }
}