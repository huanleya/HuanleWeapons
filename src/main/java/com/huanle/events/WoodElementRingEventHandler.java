package com.huanle.events;

import com.huanle.HuanleMod;
import com.huanle.ModItems;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Random;

@Mod.EventBusSubscriber(modid = HuanleMod.MOD_ID)
public class WoodElementRingEventHandler {
    private static final Random RANDOM = new Random();
    private static final double LIFE_STEAL_CHANCE = 0.25;
    private static final float LIFE_STEAL_AMOUNT = 2.0F;

    private static boolean hasWoodElementRing(Player player) {
        return CuriosApi.getCuriosInventory(player)
                .map(handler -> handler.findFirstCurio(ModItems.WOOD_ELEMENT_RING.get()).isPresent())
                .orElse(false);
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (hasWoodElementRing(player)) {
                if (event.getSource().is(DamageTypes.MAGIC)) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onMobEffectApplicable(MobEffectEvent.Applicable event) {
        if (event.getEntity() instanceof Player player) {
            if (hasWoodElementRing(player)) {
                if (event.getEffectInstance().getEffect() == MobEffects.POISON) {
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            if (hasWoodElementRing(player)) {
                if (RANDOM.nextDouble() < LIFE_STEAL_CHANCE) {
                    float currentHealth = player.getHealth();
                    float maxHealth = player.getMaxHealth();
                    float newHealth = Math.min(currentHealth + LIFE_STEAL_AMOUNT, maxHealth);
                    player.setHealth(newHealth);
                }
            }
        }
    }
}