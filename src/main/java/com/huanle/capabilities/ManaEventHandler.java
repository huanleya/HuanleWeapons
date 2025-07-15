package com.huanle.capabilities;

import com.huanle.ModAttributes;
import com.huanle.items.armor.EnderCrystalArmorItem;
import com.huanle.items.armor.ForestArmorItem;
import com.huanle.items.armor.FireGodArmorItem;
import com.huanle.items.weapons.SunGodStaff;
import com.huanle.network.ManaUpdatePacket;
import com.huanle.network.NetworkHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = "huanle")
public class ManaEventHandler {
    
    private static final ResourceLocation MANA_CAPABILITY_ID = ResourceLocation.fromNamespaceAndPath("huanle", "mana");

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            ManaProvider provider = new ManaProvider();
            event.addCapability(MANA_CAPABILITY_ID, provider);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            AttributeInstance maxManaAttribute = serverPlayer.getAttribute(ModAttributes.MAX_MANA.get());
            if (maxManaAttribute != null) {
                serverPlayer.getCapability(ManaCapability.MANA_CAPABILITY).ifPresent(mana -> {
                    float attributeMaxMana = (float) maxManaAttribute.getValue();
                    mana.setMaxMana(attributeMaxMana);
                    NetworkHandler.INSTANCE.send(
                        PacketDistributor.PLAYER.with(() -> serverPlayer),
                        new ManaUpdatePacket(mana.getMana(), mana.getMaxMana(), mana.getManaRegenRate())
                    );
                });
            }
        }
    }
    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            serverPlayer.getCapability(ManaCapability.MANA_CAPABILITY).ifPresent(mana -> {
                mana.setMana(mana.getMaxMana());
                NetworkHandler.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> serverPlayer),
                    new ManaUpdatePacket(mana.getMana(), mana.getMaxMana(), mana.getManaRegenRate())
                );
            });
        }
    }
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (!event.player.level().isClientSide) {
                event.player.getCapability(ManaCapability.MANA_CAPABILITY).ifPresent(mana -> {
                    float oldMana = mana.getMana();
                    float oldMaxMana = mana.getMaxMana();
                    AttributeInstance maxManaAttribute = event.player.getAttribute(ModAttributes.MAX_MANA.get());
                    if (maxManaAttribute != null) {
                        float attributeMaxMana = (float) maxManaAttribute.getValue();
                        if (oldMaxMana != attributeMaxMana) {
                            mana.setMaxMana(attributeMaxMana);
                        }
                    }
                    
                    mana.tick();

                    if ((oldMana != mana.getMana() || oldMaxMana != mana.getMaxMana()) && event.player instanceof ServerPlayer serverPlayer) {
                        NetworkHandler.INSTANCE.send(
                            PacketDistributor.PLAYER.with(() -> serverPlayer),
                            new ManaUpdatePacket(mana.getMana(), mana.getMaxMana(), mana.getManaRegenRate())
                        );
                    }
                });

                if (event.player instanceof ServerPlayer serverPlayer) {
                    SunGodStaff.tickFireballRains(serverPlayer);
                }
                

                Player player = event.player;
                EnderCrystalArmorItem.tickArmorEffects(null, player.level(), player);
                ForestArmorItem.tickArmorEffects(null, player.level(), player);
                FireGodArmorItem.tickArmorEffects(null, player.level(), player);
            }
            else if (event.player.level().isClientSide) {
                event.player.getCapability(ManaCapability.MANA_CAPABILITY).ifPresent(mana -> {
                    AttributeInstance maxManaAttribute = event.player.getAttribute(ModAttributes.MAX_MANA.get());
                    if (maxManaAttribute != null) {
                        float attributeMaxMana = (float) maxManaAttribute.getValue();
                        if (mana.getMaxMana() != attributeMaxMana) {
                            mana.setMaxMana(attributeMaxMana);
                        }
                    }
                    mana.tick();
                });
            }
        }
    }
    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            serverPlayer.getCapability(ManaCapability.MANA_CAPABILITY).ifPresent(mana -> {
                NetworkHandler.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> serverPlayer),
                    new ManaUpdatePacket(mana.getMana(), mana.getMaxMana(), mana.getManaRegenRate())
                );
            });
        }
    }
}