package com.huanle.items.weapons;

import java.util.List;
import java.util.Random;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class FireGodSword extends SwordItem {
    private final Random random = new Random();
    
    public FireGodSword(@Nonnull Tier tier, int attackDamageModifier, float attackSpeedModifier, @Nonnull Item.Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }
    
    @Override
    public boolean hurtEnemy(@Nonnull ItemStack stack, @Nonnull LivingEntity target, @Nonnull LivingEntity attacker) {
        boolean result = super.hurtEnemy(stack, target, attacker);
        
        // 点燃目标8秒
        target.setSecondsOnFire(8);
        
        // 添加火焰粒子效果
        if (target.level().isClientSide) {
            for (int i = 0; i < 15; i++) {
                double x = target.getX() + (random.nextDouble() - 0.5D) * 1.2D;
                double y = target.getY() + target.getBbHeight() * 0.5D + (random.nextDouble() - 0.5D) * 1.2D;
                double z = target.getZ() + (random.nextDouble() - 0.5D) * 1.2D;
                
                target.level().addParticle(ParticleTypes.FLAME, x, y, z, 0, 0.1, 0);
                if (random.nextFloat() < 0.3f) {
                    target.level().addParticle(ParticleTypes.LAVA, x, y, z, 0, 0, 0);
                }
            }
        }
        
        return result;
    }
    
    @Override
    public void inventoryTick(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

        if (entity instanceof Player player && (isSelected || player.getOffhandItem() == stack)) {
            // 持有时给予火焰抗性
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20, 0, false, false));
            
            // 在岩浆中时给予力量效果
            if (player.isInLava()) {
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20, 1, false, false));
            }

            // 添加火焰粒子效果
            if (level.isClientSide && random.nextFloat() < 0.15f) {
                double x = player.getX() + (random.nextDouble() - 0.5D) * 0.6D;
                double y = player.getY() + 1.0D + (random.nextDouble() - 0.5D) * 0.4D;
                double z = player.getZ() + (random.nextDouble() - 0.5D) * 0.6D;
                
                level.addParticle(ParticleTypes.FLAME, x, y, z, 0, 0.05, 0);
            }
        }
    }
    
    @Override
    public boolean isFireResistant() {
        return true;
    }
    
    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag flag) {
        tooltipComponents.add(Component.translatable("item.huanle.fire_god_sword.desc"));
        tooltipComponents.add(Component.translatable("item.huanle.fire_god_sword.desc2"));
        tooltipComponents.add(Component.translatable("item.huanle.fire_god_sword.desc3"));
        super.appendHoverText(stack, level, tooltipComponents, flag);
    }
}