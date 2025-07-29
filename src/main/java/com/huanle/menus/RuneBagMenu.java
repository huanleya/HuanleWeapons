package com.huanle.menus;

import com.huanle.ModItems;
import com.huanle.ModMenuTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.Set;

public class RuneBagMenu extends AbstractContainerMenu {
    
    private final ItemStackHandler itemHandler;
    private final ItemStack runeBag;

    private static final Set<Item> RUNE_ITEMS = Set.of(
        ModItems.BLANK_RUNE.get(),
        ModItems.ICE_RUNE.get(),
        ModItems.EARTH_RUNE.get(),
        ModItems.ELECTRIC_RUNE.get(),
        ModItems.WIND_RUNE.get(),
        ModItems.FIRE_RUNE.get(),
        ModItems.LIFE_RUNE.get(),
        ModItems.WATER_RUNE.get(),
        ModItems.CRIMSON_RUNE.get()
    );

    public RuneBagMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory, playerInventory.player.getItemInHand(playerInventory.player.getUsedItemHand()));
    }

    public RuneBagMenu(int containerId, Inventory playerInventory, ItemStack runeBag) {
        super(ModMenuTypes.RUNE_BAG_MENU.get(), containerId);
        this.runeBag = runeBag;
        this.itemHandler = new ItemStackHandler(27) {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return RUNE_ITEMS.contains(stack.getItem());
            }
            
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                saveToItem();
            }
        };
        
        loadFromItem();

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new SlotItemHandler(itemHandler, row * 9 + col, 8 + col * 18, 18 + row * 18));
            }
        }

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }
    
    private void loadFromItem() {
        CompoundTag tag = runeBag.getOrCreateTag();
        if (tag.contains("Items")) {
            ListTag listTag = tag.getList("Items", 10);
            for (int i = 0; i < listTag.size() && i < itemHandler.getSlots(); i++) {
                CompoundTag itemTag = listTag.getCompound(i);
                int slot = itemTag.getByte("Slot") & 255;
                if (slot < itemHandler.getSlots()) {
                    itemHandler.setStackInSlot(slot, ItemStack.of(itemTag));
                }
            }
        }
    }
    
    private void saveToItem() {
        CompoundTag tag = runeBag.getOrCreateTag();
        ListTag listTag = new ListTag();
        
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            ItemStack stack = itemHandler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putByte("Slot", (byte) i);
                stack.save(itemTag);
                listTag.add(itemTag);
            }
        }
        
        tag.put("Items", listTag);
    }
    
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            
            if (index < 27) {
                if (!this.moveItemStackTo(itemstack1, 27, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (RUNE_ITEMS.contains(itemstack1.getItem())) {
                    if (!this.moveItemStackTo(itemstack1, 0, 27, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    return ItemStack.EMPTY;
                }
            }
            
            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        
        return itemstack;
    }
    
    @Override
    public boolean stillValid(Player player) {
        return player.getInventory().contains(runeBag);
    }
    
    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }
    
    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}