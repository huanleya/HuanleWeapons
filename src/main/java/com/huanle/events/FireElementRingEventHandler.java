package com.huanle.events;

import com.huanle.HuanleMod;
import com.huanle.ModItems;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

@Mod.EventBusSubscriber(modid = HuanleMod.MOD_ID)
public class FireElementRingEventHandler {

    private static boolean hasFireElementRing(Player player) {
        return CuriosApi.getCuriosInventory(player)
                .map(handler -> handler.findFirstCurio(ModItems.FIRE_ELEMENT_RING.get()).isPresent())
                .orElse(false);
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (hasFireElementRing(player)) {
                if (event.getSource().is(DamageTypes.IN_FIRE) ||
                    event.getSource().is(DamageTypes.ON_FIRE) ||
                    event.getSource().is(DamageTypes.LAVA) ||
                    event.getSource().is(DamageTypes.HOT_FLOOR) ||
                    event.getSource().is(DamageTypes.FIREBALL) ||
                    event.getSource().is(DamageTypes.UNATTRIBUTED_FIREBALL)) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (event.getSource().getEntity() instanceof Player attacker) {
            if (hasFireElementRing(attacker)) {
                LivingEntity target = event.getEntity();
                target.setSecondsOnFire(5);
            }
        }
    }
}