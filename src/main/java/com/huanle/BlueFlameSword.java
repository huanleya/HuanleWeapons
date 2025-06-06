package com.huanle;

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

public class BlueFlameSword extends SwordItem {
    private final Random random = new Random();
    
    public BlueFlameSword(@Nonnull Tier tier, int attackDamageModifier, float attackSpeedModifier, @Nonnull Item.Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }
    
    @Override
    public boolean hurtEnemy(@Nonnull ItemStack stack, @Nonnull LivingEntity target, @Nonnull LivingEntity attacker) {

        boolean result = super.hurtEnemy(stack, target, attacker);
        

        if (target.isOnFire()) {
            float extraDamage = this.getDamage() * 3.0f;
            target.hurt(target.level().damageSources().generic(), extraDamage);
        }
        

        target.setSecondsOnFire(target.getRemainingFireTicks() / 20 + 5);
        
        return result;
    }
    
    @Override
    public void inventoryTick(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        

        if (entity instanceof Player player && (isSelected || player.getOffhandItem() == stack)) {
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 10, 0, false, false));
            

            if (level.isClientSide && random.nextFloat() < 0.3f) {
                double x = player.getX() + (random.nextDouble() - 0.5D) * 0.5D;
                double y = player.getY() + 0.8D + (random.nextDouble() - 0.5D) * 0.3D;
                double z = player.getZ() + (random.nextDouble() - 0.5D) * 0.5D;
                

                level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 0, 0.05D, 0);
            }
        }
    }
    
    @Override
    public boolean isFireResistant() {
        return true;
    }
    
    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag flag) {
        tooltipComponents.add(Component.translatable("item.huanle.blue_flame_sword.desc"));
        tooltipComponents.add(Component.translatable("item.huanle.blue_flame_sword.desc2"));
        tooltipComponents.add(Component.translatable("item.huanle.blue_flame_sword.desc3"));
        super.appendHoverText(stack, level, tooltipComponents, flag);
    }
}
