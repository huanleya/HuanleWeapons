package com.huanle.events;

import com.huanle.HuanleMod;
import com.huanle.ModAttributes;

import com.huanle.ModItems;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

@Mod.EventBusSubscriber(modid = HuanleMod.MOD_ID)
public class LifeRingEventHandler {

    private static boolean hasLifeRing(Player player) {
        return CuriosApi.getCuriosInventory(player)
                .map(handler -> handler.findFirstCurio(ModItems.LIFE_RING.get()).isPresent())
                .orElse(false);
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player != null && hasLifeRing(player)) {
            double fortuneLevel = player.getAttributeValue(ModAttributes.FORTUNE_LEVEL.get());
            if (fortuneLevel > 0) {

            }
        }
    }

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if (event.getSource().getEntity() instanceof Player) {
            Player player = (Player) event.getSource().getEntity();
            if (hasLifeRing(player)) {
                double lootingLevel = player.getAttributeValue(ModAttributes.LOOTING_LEVEL.get());
                if (lootingLevel > 0) {

                }
            }
        }
    }
}