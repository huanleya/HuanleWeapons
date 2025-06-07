package com.huanle;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent;

@Mod.EventBusSubscriber(modid = HuanleMod.MOD_ID)
public class EventHandler {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingHurt(LivingHurtEvent event) {
        // 处理熔岩行者附魔的伤害保护
        if ((event.getSource().getMsgId().equals("lava") || event.getSource().getMsgId().equals("hot_floor")) && event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
            
            // 检查玩家是否穿着带有熔岩行者附魔的靴子
            int lavaWalkerLevel = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.LAVA_WALKER.get(), boots);
            System.out.println("LavaWalker enchantment level: " + lavaWalkerLevel + " for player: " + player.getName().getString());
            // 如果玩家有熔岩行者附魔，取消伤害
            if (lavaWalkerLevel > 0) {
                event.setCanceled(true);
                System.out.println("LavaWalker enchantment canceled damage for player: " + player.getName().getString());
                return;
            }
        }

        // 处理附雷附魔
        if (event.getSource().getEntity() instanceof LivingEntity) {
            LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
            
            ItemStack mainHandWeapon = attacker.getMainHandItem();
            ItemStack offHandWeapon = attacker.getOffhandItem();
            
            int mainHandLevel = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.LIGHTNING_STRIKE.get(), mainHandWeapon);
            int offHandLevel = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.LIGHTNING_STRIKE.get(), offHandWeapon);

            if (event.getEntity() instanceof LivingEntity) {
                LivingEntity target = (LivingEntity) event.getEntity();
                if (mainHandLevel > 0) {
                    LightningEnchantment.onHitEntity(target, attacker, mainHandWeapon, mainHandLevel);
                }
                if (offHandLevel > 0) {
                    LightningEnchantment.onHitEntity(target, attacker, offHandWeapon, offHandLevel);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingDamage(LivingDamageEvent event) {
        if ((event.getSource().getMsgId().equals("lava") || event.getSource().getMsgId().equals("hot_floor")) && event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
            int lavaWalkerLevel = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.LAVA_WALKER.get(), boots);
            System.out.println("LavaWalker (DamageEvent) enchantment level: " + lavaWalkerLevel + " for player: " + player.getName().getString());
            if (lavaWalkerLevel > 0) {
                event.setCanceled(true);
                System.out.println("LavaWalker (DamageEvent) canceled damage for player: " + player.getName().getString());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(LivingEvent.LivingTickEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
        int lavaWalkerLevel = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.LAVA_WALKER.get(), boots);
        if (lavaWalkerLevel > 0) {
            BlockPos below = player.blockPosition().below();
            if (player.level().getBlockState(below).is(Blocks.MAGMA_BLOCK)) {
                // 兜底：移除着火状态
                player.clearFire();
            }
        }
    }
}