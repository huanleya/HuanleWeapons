package com.huanle.events;

import com.huanle.HuanleMod;
import com.huanle.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.stats.Stats;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = HuanleMod.MOD_ID)
public class FiveElementRingEventHandler {
    private static final Random RANDOM = new Random();
    private static final double LIFE_STEAL_CHANCE = 0.25;
    private static final float LIFE_STEAL_AMOUNT = 2.0F;
    private static final long COOLDOWN_TIME = 300000L;

    private static final Map<UUID, Long> playerCooldowns = new HashMap<>();

    private static boolean hasFiveElementRing(Player player) {
        return CuriosApi.getCuriosInventory(player)
                .map(handler -> handler.findFirstCurio(ModItems.FIVE_ELEMENT_RING.get()).isPresent())
                .orElse(false);
    }

    public static boolean isOnCooldown(Player player) {
        UUID playerId = player.getUUID();
        Long cooldownEnd = playerCooldowns.get(playerId);
        if (cooldownEnd == null) {
            return false;
        }
        
        long currentTime = System.currentTimeMillis();
        if (currentTime >= cooldownEnd) {
            playerCooldowns.remove(playerId);
            return false;
        }
        return true;
    }

    private static long getRemainingCooldown(Player player) {
        UUID playerId = player.getUUID();
        Long cooldownEnd = playerCooldowns.get(playerId);
        if (cooldownEnd == null) {
            return 0;
        }
        
        long currentTime = System.currentTimeMillis();
        return Math.max(0, (cooldownEnd - currentTime) / 1000);
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (hasFiveElementRing(player) && !isOnCooldown(player)) {
                event.setCanceled(true);

                player.level().playSound(null, player.getX(), player.getY(), player.getZ(), 
                    SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0F, 1.0F);

                player.setHealth(player.getMaxHealth());
                player.removeAllEffects();

                UUID playerId = player.getUUID();
                playerCooldowns.put(playerId, System.currentTimeMillis() + COOLDOWN_TIME);

                player.awardStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING));

                ItemStack fiveElementRing = CuriosApi.getCuriosInventory(player)
                    .map(handler -> handler.findFirstCurio(ModItems.FIVE_ELEMENT_RING.get())
                        .map(slotResult -> slotResult.stack())
                        .orElse(ItemStack.EMPTY))
                    .orElse(ItemStack.EMPTY);
                
                if (!fiveElementRing.isEmpty()) {
                    player.getCooldowns().addCooldown(fiveElementRing.getItem(), 6000); // 5分钟冷却显示
                }
                

            }
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (hasFiveElementRing(player) && !isOnCooldown(player)) {
                player.invulnerableTime = Math.max(player.invulnerableTime, 20);

                if (event.getSource().is(DamageTypes.IN_FIRE) ||
                    event.getSource().is(DamageTypes.ON_FIRE) ||
                    event.getSource().is(DamageTypes.LAVA) ||
                    event.getSource().is(DamageTypes.HOT_FLOOR) ||
                    event.getSource().is(DamageTypes.FIREBALL) ||
                    event.getSource().is(DamageTypes.UNATTRIBUTED_FIREBALL)) {
                    event.setCanceled(true);
                }

                double damageReduction = player.getAttributeValue(com.huanle.ModAttributes.DAMAGE_REDUCTION.get());
                if (damageReduction > 0) {
                    float originalDamage = event.getAmount();
                    float reducedDamage = originalDamage * (float)(1.0 - damageReduction);
                    event.setAmount(reducedDamage);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        if (hasFiveElementRing(player) && !isOnCooldown(player)) {
            event.setNewSpeed(event.getNewSpeed() * 1.1F);

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
    public static void onMobEffectApplicable(MobEffectEvent.Applicable event) {
        if (event.getEntity() instanceof Player player) {
            if (hasFiveElementRing(player) && !isOnCooldown(player)) {
                if (event.getEffectInstance().getEffect() == MobEffects.POISON) {
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (event.getSource().getEntity() instanceof Player attacker) {
            if (hasFiveElementRing(attacker) && !isOnCooldown(attacker)) {
                LivingEntity target = event.getEntity();

                if (RANDOM.nextDouble() < LIFE_STEAL_CHANCE) {
                    attacker.heal(LIFE_STEAL_AMOUNT);
                }

                target.setSecondsOnFire(5);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (hasFiveElementRing(player)) {
                if (isOnCooldown(player)) {
                    if (player.tickCount % 100 == 0) {
                        long remainingSeconds = getRemainingCooldown(player);
                        long minutes = remainingSeconds / 60;
                        long seconds = remainingSeconds % 60;
                        player.displayClientMessage(
                            Component.translatable("message.huanle.five_element_ring.cooldown", minutes, seconds), 
                            true
                        );
                    }
                    return;
                }

                if (player.isInWater() && player.getAirSupply() < player.getMaxAirSupply()) {
                    player.setAirSupply(player.getMaxAirSupply());
                }

                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 300, 0, false, false));

                if (player.isOnFire() || player.isInLava()) {
                    if (player.tickCount % 20 == 0) {
                        player.heal(1.0F);
                    }
                }
            }
        }
    }
}