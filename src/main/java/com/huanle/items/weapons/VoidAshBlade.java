package com.huanle.items.weapons;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import com.huanle.ModEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.EquipmentSlot;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap;
import com.huanle.capabilities.ManaUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class VoidAshBlade extends SwordItem {

    private static final int COOLDOWN_TICKS = 600;
    private static final int EFFECT_RADIUS = 32;
    private static final UUID ATTACK_RANGE_MODIFIER_UUID = UUID.fromString("9d49e8e6-45a6-11ee-be56-0242ac120002");
    private static final int STUN_SKILL_COOLDOWN = 200;
    private static final int STUN_DURATION = 60;
    private static final int WEAKNESS_DURATION = 200;
    private static final int STUN_RADIUS = 16;

    public VoidAshBlade(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker.level().getRandom().nextFloat() < 0.2f) {
            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 1)); // 虚弱I，持续5秒
        }

        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isCrouching()) {
            if (player.getCooldowns().isOnCooldown(this)) {
                return InteractionResultHolder.pass(stack);
            }
            float currentMana = ManaUtils.getCurrentMana(player);
            if (currentMana < 20.0f) {
                if (!level.isClientSide) {
                    player.displayClientMessage(
                        Component.translatable("item.huanle.void_ash_blade.not_enough_mana", 20),
                        true
                    );
                }
                return InteractionResultHolder.pass(stack);
            }
            ManaUtils.consumeManaWithEnchantment(player, 20.0f, stack);
            applyStunEffect(level, player);
            player.getCooldowns().addCooldown(this, STUN_SKILL_COOLDOWN);
            
            return InteractionResultHolder.success(stack);
        } else {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(stack);
        }
    }
    private void applyStunEffect(Level level, Player player) {
        AABB aabb = new AABB(
                player.getX() - STUN_RADIUS,
                player.getY() - STUN_RADIUS,
                player.getZ() - STUN_RADIUS,
                player.getX() + STUN_RADIUS,
                player.getY() + STUN_RADIUS,
                player.getZ() + STUN_RADIUS);

        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, aabb,
                entity -> entity != player && !entity.isAlliedTo(player) && !(entity instanceof ArmorStand));
        for (LivingEntity entity : entities) {
            entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, WEAKNESS_DURATION, 1));
            entity.addEffect(new MobEffectInstance(ModEffects.STUN.get(), STUN_DURATION, 0));
        }
        if (!level.isClientSide) {
            ServerLevel serverLevel = (ServerLevel) level;
            double x = player.getX();
            double y = player.getY();
            double z = player.getZ();

            level.playSound(null, x, y, z, SoundEvents.WITHER_AMBIENT, SoundSource.PLAYERS, 1.0F, 0.5F);
            for (int i = 0; i < 100; i++) {
                double angle = level.getRandom().nextDouble() * Math.PI * 2;
                double distance = level.getRandom().nextDouble() * STUN_RADIUS;
                double offsetX = Math.sin(angle) * distance;
                double offsetZ = Math.cos(angle) * distance;

                serverLevel.sendParticles(
                        ParticleTypes.SOUL,
                        x + offsetX,
                        y + 0.5,
                        z + offsetZ,
                        1, 0, 0.2, 0, 0.02);
            }
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if (!(livingEntity instanceof Player player)) return;
        int totalUseDuration = getUseDuration(stack);
        int currentUseTick = totalUseDuration - remainingUseDuration;
        if (currentUseTick % 20 == 0 && currentUseTick > 0) {
            float currentMana = ManaUtils.getCurrentMana(player);
            if (currentMana < 1.0f) {
                if (!level.isClientSide) {
                    player.displayClientMessage(
                        Component.translatable("item.huanle.void_ash_blade.not_enough_mana", 1),
                        true
                    );
                }
                player.stopUsingItem();
                return;
            }
            ManaUtils.consumeManaWithEnchantment(player, 1.0f, stack);
        }
        AABB aabb = new AABB(
                player.getX() - 64,
                player.getY() - 64,
                player.getZ() - 64,
                player.getX() + 64,
                player.getY() + 64,
                player.getZ() + 64);

        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, aabb,
                entity -> entity != player && !entity.isAlliedTo(player));
        for (LivingEntity entity : entities) {
            double dx = player.getX() - entity.getX();
            double dy = player.getY() - entity.getY();
            double dz = player.getZ() - entity.getZ();

            double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (distance > 0.1) {
                entity.setDeltaMovement(entity.getDeltaMovement().add(
                    dx / distance * 0.1,
                    dy / distance * 0.1,
                    dz / distance * 0.1
                ));
            }
        }
        if (!level.isClientSide && remainingUseDuration % 5 == 0) {
            ServerLevel serverLevel = (ServerLevel) level;
            double x = player.getX();
            double y = player.getY();
            double z = player.getZ();

            level.playSound(null, x, y, z, SoundEvents.WITHER_SHOOT, SoundSource.PLAYERS, 0.3F, 1.0F);

            for (int i = 0; i < 20; i++) {
                double offsetX = level.getRandom().nextGaussian() * 64 * 0.2;
                double offsetY = level.getRandom().nextGaussian() * 64 * 0.2;
                double offsetZ = level.getRandom().nextGaussian() * 64 * 0.2;

                serverLevel.sendParticles(
                        ParticleTypes.SMOKE,
                        x + offsetX,
                        y + offsetY,
                        z + offsetZ,
                        1, 0, 0, 0, 0.05);
            }
        }
    }
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag flag) {
        tooltipComponents.add(Component.translatable("item.huanle.void_ash_blade.desc"));
        tooltipComponents.add(Component.translatable("tooltip.huanle.void_ash_blade.crouch_right_click"));
        tooltipComponents.add(Component.translatable("tooltip.huanle.void_ash_blade.hold_right_click"));
        super.appendHoverText(stack, level, tooltipComponents, flag);
    }
    
    @Override
    public Multimap<net.minecraft.world.entity.ai.attributes.Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<net.minecraft.world.entity.ai.attributes.Attribute, AttributeModifier> attributes = super.getAttributeModifiers(slot, stack);
        
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<net.minecraft.world.entity.ai.attributes.Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(attributes);
            builder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(ATTACK_RANGE_MODIFIER_UUID, 
                "Void Ash Blade attack range", 1.0, AttributeModifier.Operation.ADDITION));
                
            return builder.build();
        }
        
        return attributes;
    }
}
