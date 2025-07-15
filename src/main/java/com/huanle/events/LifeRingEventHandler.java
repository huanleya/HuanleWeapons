package com.huanle.events;

import com.huanle.ModItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

@Mod.EventBusSubscriber(modid = "huanle")
public class LifeRingEventHandler {

    private static boolean hasLifeRing(Player player) {
        return CuriosApi.getCuriosInventory(player)
                .map(handler -> handler.findFirstCurio(ModItems.LIFE_RING.get()).isPresent())
                .orElse(false);
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player != null && hasLifeRing(player)) {
            ItemStack tool = player.getMainHandItem();
            if (!tool.isEmpty()) {
                int currentFortune = tool.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);
                if (currentFortune == 0) {
                    tool.enchant(Enchantments.BLOCK_FORTUNE, 1);
                    player.level().getServer().execute(() -> {
                        if (tool.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE) == 1) {
                            tool.removeTagKey("Enchantments");
                        }
                    });
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if (event.getSource().getEntity() instanceof Player) {
            Player player = (Player) event.getSource().getEntity();
            if (hasLifeRing(player)) {
            ItemStack weapon = player.getMainHandItem();
            if (!weapon.isEmpty()) {
                int currentLooting = weapon.getEnchantmentLevel(Enchantments.MOB_LOOTING);
                if (currentLooting == 0) {
                    event.getDrops().forEach(itemEntity -> {
                        ItemStack stack = itemEntity.getItem();
                        if (stack.getCount() > 0 && player.level().random.nextFloat() < 0.5f) {
                            stack.setCount(stack.getCount() + 1);
                        }
                    });
                }
            }
            }
        }
    }
}