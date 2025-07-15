package com.huanle.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenEnderChestMessage {
    
    public OpenEnderChestMessage() {
    }
    
    public OpenEnderChestMessage(FriendlyByteBuf buf) {

    }
    
    public void toBytes(FriendlyByteBuf buf) {

    }
    
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                player.openMenu(new net.minecraft.world.SimpleMenuProvider(
                    (id, inventory, p) -> net.minecraft.world.inventory.ChestMenu.threeRows(id, inventory, player.getEnderChestInventory()),
                    net.minecraft.network.chat.Component.translatable("container.enderchest")
                ));
            }
        });
        return true;
    }
}