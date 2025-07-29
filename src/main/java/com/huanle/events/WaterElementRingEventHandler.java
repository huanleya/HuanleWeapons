package com.huanle.events;

import com.huanle.HuanleMod;
import com.huanle.ModItems;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

@Mod.EventBusSubscriber(modid = HuanleMod.MOD_ID)
public class WaterElementRingEventHandler {

    private static boolean hasWaterElementRing(Player player) {
        return CuriosApi.getCuriosInventory(player)
                .map(handler -> handler.findFirstCurio(ModItems.WATER_ELEMENT_RING.get()).isPresent())
                .orElse(false);
    }

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        if (hasWaterElementRing(player)) {
            boolean headInWater = player.isEyeInFluid(FluidTags.WATER);
            boolean notOnGround = !player.onGround();
            
            if (headInWater) {
                event.setNewSpeed(event.getNewSpeed() * 5.0F);

                if (notOnGround) {
                    event.setNewSpeed(event.getNewSpeed() * 5.0F);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (hasWaterElementRing(player)) {
                if (player.isInWater() && player.getAirSupply() < player.getMaxAirSupply()) {
                    player.setAirSupply(player.getMaxAirSupply());
                }
            }
        }
    }
}