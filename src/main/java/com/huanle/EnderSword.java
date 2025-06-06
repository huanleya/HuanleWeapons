package com.huanle;

import java.util.Random;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import java.util.List;

public class EnderSword extends SwordItem {
    private final Random random = new Random();
    
    public EnderSword(Tier tier, int attackDamageModifier, float attackSpeedModifier, Item.Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }
    
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {

        boolean result = super.hurtEnemy(stack, target, attacker);
        

        if (random.nextFloat() <= 0.2f) {
            float extraDamage = this.getDamage() * 0.5f;
            target.hurt(target.level().damageSources().generic(), extraDamage);
        }
        

        target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 1)); // 1表示等级II
        
        return result;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        

        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.pass(itemstack);
        }
        

        level.playSound(null, player.getX(), player.getY(), player.getZ(), 
                SoundEvents.ENDER_PEARL_THROW, SoundSource.PLAYERS, 0.5F, 0.4F);
        

        if (!level.isClientSide) {

            NoHurtEnderPearl pearl = new NoHurtEnderPearl(level, player);
            pearl.setItem(new ItemStack(Items.ENDER_PEARL));
            pearl.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(pearl);
        }
        

        player.getCooldowns().addCooldown(this, 200);
        

        player.awardStat(Stats.ITEM_USED.get(this));
        
        return InteractionResultHolder.success(itemstack);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltipComponents, TooltipFlag flag) {
        tooltipComponents.add(Component.translatable("item.huanle.ender_sword.desc"));
        tooltipComponents.add(Component.translatable("item.huanle.ender_sword.desc2"));
        super.appendHoverText(stack, level, tooltipComponents, flag);
    }
}