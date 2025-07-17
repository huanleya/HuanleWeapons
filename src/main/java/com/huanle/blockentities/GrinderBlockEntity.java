package com.huanle.blockentities;

import com.huanle.ModBlockEntities;
import com.huanle.recipes.GrinderRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class GrinderBlockEntity extends BlockEntity implements MenuProvider {

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;
    private static final int SLOT_COUNT = 2;
    private static final int MAX_PROGRESS = 200; // 10秒（200 ticks）
    private int progress = 0;
    private final ItemStackHandler itemHandler = new ItemStackHandler(SLOT_COUNT) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
        
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (slot == INPUT_SLOT) {
                return hasRecipe(stack);
            }
            return slot == OUTPUT_SLOT;
        }
    };
    
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    protected final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> GrinderBlockEntity.this.progress;
                case 1 -> GrinderBlockEntity.this.MAX_PROGRESS;
                default -> 0;
            };
        }
        
        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> GrinderBlockEntity.this.progress = value;
            }
        }
        
        @Override
        public int getCount() {
            return 2;
        }
    };
    
    public GrinderBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.GRINDER.get(), pos, blockState);
    }
    
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }
    
    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }
    
    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }
    
    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("grinder.progress", progress);
        super.saveAdditional(tag);
    }
    
    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        progress = tag.getInt("grinder.progress");
    }
    
    public void dropContents() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }
    
    @Override
    public Component getDisplayName() {
        return Component.translatable("block.huanle.grinder");
    }
    
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new com.huanle.menus.GrinderMenu(containerId, playerInventory, this, this.data);
    }
    
    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) {
            return;
        }
        
        if (hasRecipe()) {
            increaseCraftingProgress();
            setChanged(level, pos, state);
            
            if (hasProgressFinished()) {
                craftItem();
                resetProgress();
                setChanged(level, pos, state);
            }
        } else {
            if (progress > 0) {
                resetProgress();
                setChanged(level, pos, state);
            }
        }
    }
    
    private void resetProgress() {
        progress = 0;
    }
    
    private void craftItem() {
        Optional<GrinderRecipe> recipe = getCurrentRecipe();
        if (recipe.isPresent()) {
            ItemStack result = recipe.get().getResultItem(null);

            this.itemHandler.extractItem(INPUT_SLOT, 1, false);

            this.itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(result.getItem(),
                    this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + result.getCount()));
        }
    }
    
    private boolean hasRecipe() {
        Optional<GrinderRecipe> recipe = getCurrentRecipe();
        
        if (recipe.isEmpty()) {
            return false;
        }
        
        ItemStack result = recipe.get().getResultItem(null);
        return canInsertAmountIntoOutputSlot(result.getCount()) && canInsertItemIntoOutputSlot(result.getItem());
    }
    
    private boolean hasRecipe(ItemStack stack) {
        SimpleContainer inventory = new SimpleContainer(1);
        inventory.setItem(0, stack);
        
        Optional<GrinderRecipe> recipe = level.getRecipeManager()
                .getRecipeFor(GrinderRecipe.Type.INSTANCE, inventory, level);
        
        return recipe.isPresent();
    }
    
    private Optional<GrinderRecipe> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        
        return this.level.getRecipeManager().getRecipeFor(GrinderRecipe.Type.INSTANCE, inventory, level);
    }
    
    private boolean canInsertItemIntoOutputSlot(net.minecraft.world.item.Item item) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() || 
               this.itemHandler.getStackInSlot(OUTPUT_SLOT).is(item);
    }
    
    private boolean canInsertAmountIntoOutputSlot(int count) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + count <= 
               this.itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
    }
    
    private boolean hasProgressFinished() {
        return progress >= MAX_PROGRESS;
    }
    
    private void increaseCraftingProgress() {
        progress++;
    }
}