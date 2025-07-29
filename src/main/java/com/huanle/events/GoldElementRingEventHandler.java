package com.huanle.events;

import com.huanle.HuanleMod;
import com.huanle.ModItems;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

@Mod.EventBusSubscriber(modid = HuanleMod.MOD_ID)
public class GoldElementRingEventHandler {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (hasGoldElementRing(player)) {
                player.invulnerableTime = Math.max(player.invulnerableTime, 20);
            }
        }
    }

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        if (hasGoldElementRing(player)) {
            float currentSpeed = event.getNewSpeed();
            event.setNewSpeed(currentSpeed * 1.1f);
        }
    }

    private static boolean hasGoldElementRing(Player player) {
        return CuriosApi.getCuriosInventory(player)
                .map(handler -> handler.findFirstCurio(ModItems.GOLD_ELEMENT_RING.get()).isPresent())
                .orElse(false);
    }
}