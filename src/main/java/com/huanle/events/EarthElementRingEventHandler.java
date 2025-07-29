package com.huanle.events;

import com.huanle.HuanleMod;
import com.huanle.ModAttributes;
import com.huanle.ModItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

@Mod.EventBusSubscriber(modid = HuanleMod.MOD_ID)
public class EarthElementRingEventHandler {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        
        if (entity instanceof Player player && hasEarthElementRing(player)) {
            double damageReduction = player.getAttributeValue(ModAttributes.DAMAGE_REDUCTION.get());
            if (damageReduction > 0) {
                float originalDamage = event.getAmount();
                float reducedDamage = originalDamage * (float)(1.0 - damageReduction);
                event.setAmount(reducedDamage);
            }
        }
    }
    
    private static boolean hasEarthElementRing(Player player) {
        return CuriosApi.getCuriosInventory(player)
                .map(handler -> handler.findFirstCurio(ModItems.EARTH_ELEMENT_RING.get()).isPresent())
                .orElse(false);
    }
}