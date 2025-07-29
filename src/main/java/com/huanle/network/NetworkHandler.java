package com.huanle.network;

import com.huanle.HuanleMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(HuanleMod.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    
    public static void register() {
        int id = 0;
        INSTANCE.messageBuilder(OpenEnderChestMessage.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(OpenEnderChestMessage::new)
                .encoder(OpenEnderChestMessage::toBytes)
                .consumerMainThread(OpenEnderChestMessage::handle)
                .add();
        INSTANCE.messageBuilder(ManaUpdatePacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ManaUpdatePacket::new)
                .encoder(ManaUpdatePacket::toBytes)
                .consumerMainThread(ManaUpdatePacket::handle)
                .add();
    }
    
    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }
    
    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}