package com.huanle.items.weapons;

import com.huanle.capabilities.ManaUtils;
import com.huanle.entities.FireballEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SunGodStaff extends BaseStaff {
    
    private static final float MANA_COST = 10.0F;
    private static final int COOLDOWN_TICKS = 20;
    private static final float FIREBALL_SPEED = 1.5F;
    private static final float FIREBALL_RAIN_MANA_COST = 30.0F;
    private static final int FIREBALL_RAIN_COOLDOWN_TICKS = 600;
    private static final int FIREBALL_RAIN_DURATION_TICKS = 200;
    private static final int FIREBALL_RAIN_INTERVAL = 10;
    private static final int FIREBALL_RAIN_RANGE = 16;
    private static final Map<UUID, FireballRainData> activeFireballRains = new HashMap<>();
    
    public SunGodStaff(Properties properties) {
        super(properties, MANA_COST, COOLDOWN_TICKS);
    }
    
    @Override
    public @Nonnull InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        
        if (!level.isClientSide) {
            if (player.isCrouching()) {
                return performFireballRain(level, player, itemStack, hand);
            } else {
                return super.use(level, player, hand);
            }
        }
        
        return InteractionResultHolder.fail(itemStack);
    }

    private InteractionResultHolder<ItemStack> performFireballRain(Level level, Player player, ItemStack itemStack, InteractionHand hand) {
        UUID playerId = player.getUUID();
        if (activeFireballRains.containsKey(playerId)) {
            return InteractionResultHolder.fail(itemStack);
        }
        if (isFireballRainOnCooldown(player)) {
            int remainingCooldown = getFireballRainRemainingCooldown(player);
            player.sendSystemMessage(Component.translatable("item.huanle.sun_god_staff.fireball_rain_cooldown", remainingCooldown / 20));
            return InteractionResultHolder.fail(itemStack);
        }
        if (!ManaUtils.hasMana(player, FIREBALL_RAIN_MANA_COST)) {
            player.sendSystemMessage(Component.translatable("item.huanle.sun_god_staff.not_enough_mana", (int)FIREBALL_RAIN_MANA_COST));
            return InteractionResultHolder.fail(itemStack);
        }
        if (!ManaUtils.consumeManaWithEnchantment(player, FIREBALL_RAIN_MANA_COST, itemStack)) {
            return InteractionResultHolder.fail(itemStack);
        }
        startFireballRain(level, player);
        itemStack.hurtAndBreak(3, player, (p) -> p.broadcastBreakEvent(hand));
        setFireballRainCooldown(player);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), 
                       SoundEvents.ENDER_DRAGON_GROWL, SoundSource.PLAYERS, 1.0F, 0.8F);
        
        return InteractionResultHolder.success(itemStack);
    }
    private void startFireballRain(Level level, Player player) {
        UUID playerId = player.getUUID();
        BlockPos centerPos = player.blockPosition();
        
        FireballRainData rainData = new FireballRainData(
            centerPos, 
            FIREBALL_RAIN_DURATION_TICKS, 
            0
        );
        
        activeFireballRains.put(playerId, rainData);
    }

    public static void tickFireballRains(ServerPlayer serverPlayer) {
        Level level = serverPlayer.level();
        if (level.isClientSide) return;
        
        activeFireballRains.entrySet().removeIf(entry -> {
            UUID playerId = entry.getKey();
            FireballRainData rainData = entry.getValue();
            
            Player player = level.getServer().getPlayerList().getPlayer(playerId);
            if (player == null) {
                return true;
            }
            
            rainData.ticksRemaining--;
            rainData.ticksSinceLastFireball++;
            if (rainData.ticksSinceLastFireball >= FIREBALL_RAIN_INTERVAL) {
                spawnRandomFireball(level, player, rainData.centerPos);
                rainData.ticksSinceLastFireball = 0;
            }
            if (rainData.ticksRemaining <= 0) {
                return true;
            }
            
            return false;
        });
    }

    private static void spawnRandomFireball(Level level, Player player, BlockPos centerPos) {
        double offsetX = (level.random.nextDouble() - 0.5) * FIREBALL_RAIN_RANGE * 2;
        double offsetZ = (level.random.nextDouble() - 0.5) * FIREBALL_RAIN_RANGE * 2;
        double startY = centerPos.getY() + 20 + level.random.nextInt(10);
        double targetX = centerPos.getX() + offsetX;
        double targetZ = centerPos.getZ() + offsetZ;
        Vec3 velocity = new Vec3(
            (level.random.nextDouble() - 0.5) * 0.2,
            -1.0 - level.random.nextDouble() * 0.5,
            (level.random.nextDouble() - 0.5) * 0.2
        );
        
        try {
            FireballEntity fireball = new FireballEntity(
                level,
                player,
                velocity.x,
                velocity.y,
                velocity.z
            );
            
            fireball.setPos(targetX, startY, targetZ);
            level.addFreshEntity(fireball);
            level.playSound(null, targetX, startY, targetZ, 
                           SoundEvents.GHAST_SHOOT, SoundSource.PLAYERS, 0.3F, 1.2F);
                           
        } catch (Exception e) {
            System.err.println("Error creating fireball rain: " + e.getMessage());
        }
    }

    private boolean isFireballRainOnCooldown(Player player) {
        return player.getPersistentData().contains("fireball_rain_cooldown") && 
               player.getPersistentData().getInt("fireball_rain_cooldown") > player.level().getGameTime();
    }
    private void setFireballRainCooldown(Player player) {
        player.getPersistentData().putInt("fireball_rain_cooldown", 
                                        (int)player.level().getGameTime() + FIREBALL_RAIN_COOLDOWN_TICKS);
    }
    private int getFireballRainRemainingCooldown(Player player) {
        if (player.getPersistentData().contains("fireball_rain_cooldown")) {
            int endTime = player.getPersistentData().getInt("fireball_rain_cooldown");
            long currentTime = player.level().getGameTime();
            return Math.max(0, endTime - (int)currentTime);
        }
        return 0;
    }
    
    @Override
    protected boolean performStaffAbility(Level level, Player player, ItemStack itemStack) {
        try {
            Vec3 lookDirection = player.getLookAngle();
            FireballEntity fireball = new FireballEntity(
                level,
                player,
                lookDirection.x * FIREBALL_SPEED,
                lookDirection.y * FIREBALL_SPEED,
                lookDirection.z * FIREBALL_SPEED
            );
            Vec3 startPos = player.getEyePosition().add(lookDirection.scale(1.0));
            fireball.setPos(startPos.x, startPos.y, startPos.z);
            level.addFreshEntity(fireball);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), 
                           SoundEvents.GHAST_SHOOT, SoundSource.PLAYERS, 0.5F, 1.0F);
            
            return true;
        } catch (Exception e) {
            System.err.println("Error creating fireball: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag flag) {
        tooltipComponents.add(Component.translatable("item.huanle.sun_god_staff.desc"));
        tooltipComponents.add(Component.translatable("item.huanle.sun_god_staff.desc2"));
        super.appendHoverText(stack, level, tooltipComponents, flag);
    }

    private static class FireballRainData {
        public final BlockPos centerPos;
        public int ticksRemaining;
        public int ticksSinceLastFireball;
        
        public FireballRainData(BlockPos centerPos, int duration, int ticksSinceLastFireball) {
            this.centerPos = centerPos;
            this.ticksRemaining = duration;
            this.ticksSinceLastFireball = ticksSinceLastFireball;
        }
    }
    
    @Override
    public boolean isFireResistant() {
        return true;
    }
}