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

    private static final boolean DISABLE_FOV_CHANGE = true;
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

        arrow.setSecondsOnFire(5);
        return arrow;
    }

    @Override
    public boolean isValidProjectile(ItemStack stack) {

        return stack.getItem() instanceof FlameArrowItem;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeLeft) {
        if (livingEntity instanceof Player player) {
            boolean hasInfiniteArrows = player.getAbilities().instabuild || stack.getEnchantmentLevel(net.minecraft.world.item.enchantment.Enchantments.INFINITY_ARROWS) > 0;
            ItemStack arrowStack = player.getProjectile(stack);


            if (arrowStack.isEmpty() && !hasInfiniteArrows) {

                return;
            }


            if (!arrowStack.isEmpty() && !(arrowStack.getItem() instanceof FlameArrowItem) && !hasInfiniteArrows) {

                ItemStack flameArrowStack = findFlameArrow(player);
                if (!flameArrowStack.isEmpty()) {

                    arrowStack = flameArrowStack;
                } else {

                    return;
                }
            }


            if (arrowStack.isEmpty() && hasInfiniteArrows) {
                arrowStack = new ItemStack(ModItems.FLAME_ARROW.get());
            }


            int i = this.getUseDuration(stack) - timeLeft;


            int quickDrawReduction = QuickDrawEnchantment.getDrawTimeReduction(stack);

            i += quickDrawReduction;


            int fullDrawBonus = 0;

            int maxDrawTime = 20;
            int actualDrawTime = Math.min(i, maxDrawTime);

            float f = getPowerForTime(actualDrawTime);

            if (f >= 0.1F) {
                if (!level.isClientSide) {

                    FlameArrowItem arrowItem = (FlameArrowItem) ModItems.FLAME_ARROW.get();
                    FlameArrowEntity flameArrow = new FlameArrowEntity(level, player, arrowStack);


                    float rangeBonus = 1.0F;

                    flameArrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 3.0F, 1.0F);

                    int powerLevel = stack.getEnchantmentLevel(net.minecraft.world.item.enchantment.Enchantments.POWER_ARROWS);
                    if (powerLevel > 0) {
                        flameArrow.setBaseDamage(flameArrow.getBaseDamage() + (double)powerLevel * 0.5D + 0.5D);
                    }

                    int punchLevel = stack.getEnchantmentLevel(net.minecraft.world.item.enchantment.Enchantments.PUNCH_ARROWS);
                    if (punchLevel > 0) {
                        flameArrow.setKnockback(punchLevel);
                    }


                    if (stack.getEnchantmentLevel(net.minecraft.world.item.enchantment.Enchantments.FLAMING_ARROWS) > 0) {
                        flameArrow.setSecondsOnFire(100);
                    }


                    stack.hurtAndBreak(1, player, (p) -> {
                        p.broadcastBreakEvent(player.getUsedItemHand());
                    });


                    if (hasInfiniteArrows || player.getAbilities().instabuild) {
                        flameArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                    }

                    level.addFreshEntity(flameArrow);
                }

                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        net.minecraft.sounds.SoundEvents.ARROW_SHOOT,
                        net.minecraft.sounds.SoundSource.PLAYERS,
                        1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);


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

            int totalUseDuration = this.getUseDuration(stack);


            int drawTime = totalUseDuration - remainingUseDuration;

            if (drawTime < 5) return;

            if (level.isClientSide) {

                int quickDrawLevel = stack.getEnchantmentLevel(ModEnchantments.QUICK_DRAW.get());

                int fullDrawLevel = 0;


                int standardDrawTime = 20;


                float animationProgress;


                int animationPhase = (drawTime / 20) % 5;
                int phaseTime = drawTime % 20;


                if (animationPhase % 2 == 0) {

                    animationProgress = (float)phaseTime / 20.0f;
                } else {

                    animationProgress = 1.0f - (float)phaseTime / 20.0f;
                }

                boolean isFullyDrawn = false;

                if (fullDrawLevel > 0) {

                    int fullDrawBonus = 0;
                    int requiredDrawTime = standardDrawTime - quickDrawLevel * 6;

                    isFullyDrawn = drawTime >= requiredDrawTime;

                    float fullDrawProgress = Math.min(1.0f, (float)drawTime / requiredDrawTime);

                    if (isFullyDrawn && fullDrawProgress > 0.5f && player.getRandom().nextInt(5) == 0) {

                        float yaw = player.getYRot();
                        float pitch = player.getXRot();
                        double arrowX = player.getX() - Math.sin(Math.toRadians(yaw)) * 0.8;
                        double arrowY = player.getY() + player.getEyeHeight() - Math.sin(Math.toRadians(pitch)) * 0.8;
                        double arrowZ = player.getZ() + Math.cos(Math.toRadians(yaw)) * 0.8;

                        for (int i = 0; i < 3; i++) {
                            double pOffset = (player.getRandom().nextDouble() - 0.5) * 0.3;
                            level.addParticle(
                                    player.getRandom().nextBoolean() ? ParticleTypes.FLAME : ParticleTypes.LAVA,
                                    arrowX + pOffset, arrowY + pOffset, arrowZ + pOffset,
                                    0, 0.05, 0
                            );
                        }
                    }
                } else {

                    int requiredDrawTime = standardDrawTime - quickDrawLevel * 6;
                    isFullyDrawn = drawTime >= requiredDrawTime;
                }


                int particleCount = 1 + (int)(animationProgress * 5);
                if (isFullyDrawn) {

                    particleCount += 3;
                }


                float yaw = player.getYRot();
                float pitch = player.getXRot();
                double posX = player.getX();
                double posY = player.getY() + player.getEyeHeight() - 0.2;
                double posZ = player.getZ();

                float offsetDistance = 0.5F;
                double bowX = posX - Math.sin(Math.toRadians(yaw)) * offsetDistance;
                double bowY = posY - Math.sin(Math.toRadians(pitch)) * offsetDistance;
                double bowZ = posZ + Math.cos(Math.toRadians(yaw)) * offsetDistance;

                for (int j = 0; j < particleCount; j++) {

                    double offsetX = (player.getRandom().nextDouble() - 0.5D) * 0.3D * animationProgress;
                    double offsetY = (player.getRandom().nextDouble() - 0.5D) * 0.3D * animationProgress;
                    double offsetZ = (player.getRandom().nextDouble() - 0.5D) * 0.3D * animationProgress;

                    double speedX = offsetX * 0.1D;
                    double speedY = 0.05D + offsetY * 0.1D;
                    double speedZ = offsetZ * 0.1D;


                    level.addParticle(ParticleTypes.FLAME,
                            bowX + offsetX,
                            bowY + offsetY,
                            bowZ + offsetZ,
                            speedX, speedY, speedZ);


                    if (animationProgress > 0.7F && player.getRandom().nextInt(3) == 0) {
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


    private ItemStack findFlameArrow(Player player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty() && stack.getItem() instanceof FlameArrowItem) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }


    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }
    

    @Override
    public boolean isFoil(ItemStack stack) {
        // 移除总是显示附魔效果的条件，只有在真正有附魔时才返回true
        return stack.isEnchanted();
    }
}
