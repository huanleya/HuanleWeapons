package com.huanle;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.core.particles.ParticleTypes;

public class FlameBowItem extends BowItem implements FlameBowItem2 {
    public FlameBowItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (entity instanceof Player player && selected) {
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20, 0, false, false));
        }
    }

    @Override
    public boolean isFireResistant() {
        return true;
    }

    @Override
    public AbstractArrow customArrow(AbstractArrow arrow, ItemStack stack, Level level) {
        // 设置箭矢为燃烧状态
        arrow.setSecondsOnFire(5);
        return arrow;
    }
    
    @Override
    public boolean isValidProjectile(ItemStack stack) {
        // 只允许使用烈焰箭
        return stack.getItem() instanceof FlameArrowItem;
    }
    
    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeLeft) {
        if (livingEntity instanceof Player player) {
            boolean hasInfiniteArrows = player.getAbilities().instabuild || stack.getEnchantmentLevel(net.minecraft.world.item.enchantment.Enchantments.INFINITY_ARROWS) > 0;
            ItemStack arrowStack = player.getProjectile(stack);
            
            // 检查是否有箭
            if (arrowStack.isEmpty() && !hasInfiniteArrows) {
                // 没有箭
                return;
            }
            
            // 检查是否是烈焰箭
            if (!arrowStack.isEmpty() && !(arrowStack.getItem() instanceof FlameArrowItem) && !hasInfiniteArrows) {
                // 不是烈焰箭，尝试查找背包中的烈焰箭
                ItemStack flameArrowStack = findFlameArrow(player);
                if (!flameArrowStack.isEmpty()) {
                    // 找到了烈焰箭，使用它
                    arrowStack = flameArrowStack;
                } else {
                    // 没有找到烈焰箭，不允许射击
                    return;
                }
            }
            
            // 如果是创造模式或有无限附魔且没有箭，默认使用烈焰箭
            if (arrowStack.isEmpty() && hasInfiniteArrows) {
                arrowStack = new ItemStack(ModItems.FLAME_ARROW.get());
            }
            
            // 计算拉弓时间和力量
            int i = this.getUseDuration(stack) - timeLeft;
            float f = getPowerForTime(i);
            
            if (f >= 0.1F) {
                if (!level.isClientSide) {
                    // 直接创建并发射烈焰箭实体，而不是依赖原版弓的逻辑
                    FlameArrowItem arrowItem = (FlameArrowItem) ModItems.FLAME_ARROW.get();
                    FlameArrowEntity flameArrow = new FlameArrowEntity(level, player, arrowStack);
                    
                    // 设置箭的属性
                    flameArrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 3.0F, 1.0F);
                    
                    // 添加力量附魔效果
                    int powerLevel = stack.getEnchantmentLevel(net.minecraft.world.item.enchantment.Enchantments.POWER_ARROWS);
                    if (powerLevel > 0) {
                        flameArrow.setBaseDamage(flameArrow.getBaseDamage() + (double)powerLevel * 0.5D + 0.5D);
                    }
                    
                    // 添加冲击附魔效果
                    int punchLevel = stack.getEnchantmentLevel(net.minecraft.world.item.enchantment.Enchantments.PUNCH_ARROWS);
                    if (punchLevel > 0) {
                        flameArrow.setKnockback(punchLevel);
                    }
                    
                    // 添加火矢附魔效果
                    if (stack.getEnchantmentLevel(net.minecraft.world.item.enchantment.Enchantments.FLAMING_ARROWS) > 0) {
                        flameArrow.setSecondsOnFire(100);
                    }
                    
                    // 设置弓的耐久消耗
                    stack.hurtAndBreak(1, player, (p) -> {
                        p.broadcastBreakEvent(player.getUsedItemHand());
                    });
                    
                    // 设置箭的拾取模式
                    if (hasInfiniteArrows || player.getAbilities().instabuild) {
                        flameArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                    }
                    
                    // 将箭添加到世界中
                    level.addFreshEntity(flameArrow);
                }
                
                // 播放声音
                level.playSound(null, player.getX(), player.getY(), player.getZ(), 
                               net.minecraft.sounds.SoundEvents.ARROW_SHOOT, 
                               net.minecraft.sounds.SoundSource.PLAYERS, 
                               1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                
                // 如果不是创造模式且没有无限附魔，消耗一个箭
                if (!hasInfiniteArrows && !player.getAbilities().instabuild) {
                    arrowStack.shrink(1);
                    if (arrowStack.isEmpty()) {
                        player.getInventory().removeItem(arrowStack);
                    }
                }
            }
        }
    }

    @Override
    public void onUsingTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if (livingEntity instanceof Player player) {
            int drawTime = this.getUseDuration(stack) - remainingUseDuration;
            if (drawTime >= 5) { // 拉弓时间超过0.25秒就开始显示效果
                if (level.isClientSide) {
                    // 计算拉弓进度 (0.0 - 1.0)
                    float drawProgress = Math.min(1.0F, (float)drawTime / 20.0F);
                    
                    // 根据拉弓进度增加粒子数量
                    int particleCount = 2 + (int)(drawProgress * 8);
                    
                    // 获取玩家视角方向和位置
                    float yaw = player.getYRot();
                    float pitch = player.getXRot();
                    double posX = player.getX();
                    double posY = player.getY() + player.getEyeHeight() - 0.2;
                    double posZ = player.getZ();
                    
                    // 计算弓的位置（在玩家手前方）
                    float offsetDistance = 0.5F;
                    double bowX = posX - Math.sin(Math.toRadians(yaw)) * offsetDistance;
                    double bowY = posY - Math.sin(Math.toRadians(pitch)) * offsetDistance;
                    double bowZ = posZ + Math.cos(Math.toRadians(yaw)) * offsetDistance;
                    
                    // 生成火焰粒子
                    for (int j = 0; j < particleCount; j++) {
                        // 粒子在弓周围的随机偏移
                        double offsetX = (player.getRandom().nextDouble() - 0.5D) * 0.3D * drawProgress;
                        double offsetY = (player.getRandom().nextDouble() - 0.5D) * 0.3D * drawProgress;
                        double offsetZ = (player.getRandom().nextDouble() - 0.5D) * 0.3D * drawProgress;
                        
                        // 粒子的运动方向（从弓向外扩散）
                        double speedX = offsetX * 0.1D;
                        double speedY = 0.05D + offsetY * 0.1D; // 稍微向上飘
                        double speedZ = offsetZ * 0.1D;
                        
                        // 添加火焰粒子
                        level.addParticle(ParticleTypes.FLAME, 
                                         bowX + offsetX, 
                                         bowY + offsetY, 
                                         bowZ + offsetZ, 
                                         speedX, speedY, speedZ);
                        
                        // 在拉弓进度较高时添加额外的小火花粒子
                        if (drawProgress > 0.7F && player.getRandom().nextInt(3) == 0) {
                            level.addParticle(ParticleTypes.LAVA, 
                                             bowX + offsetX * 1.2, 
                                             bowY + offsetY * 1.2, 
                                             bowZ + offsetZ * 1.2, 
                                             speedX * 1.5, speedY * 1.5, speedZ * 1.5);
                        }
                    }
                }
            }
        }
    }

    /**
     * 在玩家背包中查找烈焰箭
     */
    private ItemStack findFlameArrow(Player player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty() && stack.getItem() instanceof FlameArrowItem) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }
}