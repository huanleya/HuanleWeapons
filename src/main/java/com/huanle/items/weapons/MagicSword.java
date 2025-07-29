package com.huanle.items.weapons;

import com.huanle.capabilities.ManaUtils;
import com.huanle.ModEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import javax.annotation.Nullable;
import java.util.List;


public class MagicSword extends SwordItem {
    
    private static final float MAGIC_ATTACK_MANA_COST = 20.0f;
    private static final float MAGIC_ATTACK_DAMAGE = 10.0f;
    private static final double MAGIC_ATTACK_RANGE = 5.0;

    
    public MagicSword(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        
        if (!level.isClientSide) {
            if (player.getCooldowns().isOnCooldown(this)) {
                return InteractionResultHolder.fail(itemStack);
            }
            if (ManaUtils.hasMana(player, MAGIC_ATTACK_MANA_COST)) {
                if (ManaUtils.consumeManaWithEnchantment(player, MAGIC_ATTACK_MANA_COST, itemStack)) {
                    performFreezeAttack(player, level);
                    player.getCooldowns().addCooldown(this, 60);
                }
            } else {
                player.sendSystemMessage(Component.translatable("huanle.mana.insufficient", (int)MAGIC_ATTACK_MANA_COST).withStyle(ChatFormatting.RED));
            }
        }
        
        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }
    private void performFreezeAttack(Player player, Level level) {
        Vec3 playerPos = player.position();
        Vec3 lookVec = player.getLookAngle();
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1.0F, 1.2F);
        AABB searchArea = new AABB(
            playerPos.x - MAGIC_ATTACK_RANGE, playerPos.y - 2, playerPos.z - MAGIC_ATTACK_RANGE,
            playerPos.x + MAGIC_ATTACK_RANGE, playerPos.y + 3, playerPos.z + MAGIC_ATTACK_RANGE
        );
        
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, searchArea, 
            entity -> entity != player && entity.isAlive());
        
        for (LivingEntity entity : entities) {
            Vec3 entityPos = entity.position();
            Vec3 toEntity = entityPos.subtract(playerPos).normalize();
            if (lookVec.dot(toEntity) > 0.5) {
                entity.hurt(level.damageSources().magic(), MAGIC_ATTACK_DAMAGE);
                entity.addEffect(new MobEffectInstance(ModEffects.FROSTY.get(), 100, 0));
                if (level instanceof ServerLevel serverLevel) {
                    for (int i = 0; i < 20; i++) {
                        double offsetX = (level.random.nextDouble() - 0.5) * 2.0;
                        double offsetY = level.random.nextDouble() * 2.0;
                        double offsetZ = (level.random.nextDouble() - 0.5) * 2.0;
                        
                        serverLevel.sendParticles(ParticleTypes.SNOWFLAKE,
                                entityPos.x + offsetX, entityPos.y + offsetY, entityPos.z + offsetZ,
                                1, 0, 0, 0, 0.1);
                    }
                }
            }
        }
    }
    
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.translatable("item.huanle.magic_sword.desc").withStyle(ChatFormatting.BLUE));
        tooltip.add(Component.translatable("item.huanle.magic_sword.mana_cost", (int)MAGIC_ATTACK_MANA_COST).withStyle(ChatFormatting.AQUA));
        tooltip.add(Component.translatable("item.huanle.magic_sword.damage", (int)MAGIC_ATTACK_DAMAGE).withStyle(ChatFormatting.RED));
        tooltip.add(Component.translatable("item.huanle.magic_sword.freeze_duration").withStyle(ChatFormatting.LIGHT_PURPLE));
        tooltip.add(Component.translatable("item.huanle.magic_sword.range", (int)MAGIC_ATTACK_RANGE).withStyle(ChatFormatting.YELLOW));
    }
}