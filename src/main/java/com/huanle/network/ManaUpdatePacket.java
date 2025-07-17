package com.huanle.network;

import com.huanle.capabilities.ManaCapability;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * 魔力更新数据包
 * 用于服务端向客户端同步魔力数据
 */
public class ManaUpdatePacket {
    
    private final float mana;
    private final float maxMana;
    private final float manaRegenRate;
    
    public ManaUpdatePacket(float mana, float maxMana, float manaRegenRate) {
        this.mana = mana;
        this.maxMana = maxMana;
        this.manaRegenRate = manaRegenRate;
    }

    public ManaUpdatePacket(FriendlyByteBuf buf) {
        this.mana = buf.readFloat();
        this.maxMana = buf.readFloat();
        this.manaRegenRate = buf.readFloat();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeFloat(this.mana);
        buf.writeFloat(this.maxMana);
        buf.writeFloat(this.manaRegenRate);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isClient()) {
                handleClientSide();
            }
        });
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private void handleClientSide() {
        Player player = net.minecraft.client.Minecraft.getInstance().player;
        if (player != null) {
            player.getCapability(ManaCapability.MANA_CAPABILITY).ifPresent(mana -> {
                mana.setMaxMana(this.maxMana);
                mana.setManaRegenRate(this.manaRegenRate);
                mana.setMana(this.mana);
            });
        }
    }
    public float getMana() {
        return mana;
    }
    
    public float getMaxMana() {
        return maxMana;
    }
    
    public float getManaRegenRate() {
        return manaRegenRate;
    }
}