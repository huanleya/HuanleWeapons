package com.huanle;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * 处理FOV修改事件，阻止烈焰弓拉弓时视角缩放
 */
@Mod.EventBusSubscriber(modid = HuanleMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = net.minecraftforge.api.distmarker.Dist.CLIENT)
public class FovModifierHandler {

    @SubscribeEvent
    public static void onComputeFovModifier(ComputeFovModifierEvent event) {
        // 获取玩家实例
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        
        // 获取玩家正在使用的物品
        ItemStack itemStack = player.getUseItem();
        
        // 检查玩家是否正在拉弓
        if (!itemStack.isEmpty() && player.isUsingItem()) {
            // 检查使用的是否是弓（包括普通弓和烈焰弓）
            if (itemStack.getItem() instanceof net.minecraft.world.item.BowItem ||
                itemStack.getUseAnimation() == net.minecraft.world.item.UseAnim.BOW) {
                
                // 如果是烈焰弓，则挡止FOV变化
                if (itemStack.getItem() instanceof FlameBowItem) {
                    // 将FOV修改因子设置为1.0，这样就不会缩放
                    event.setNewFovModifier(1.0F);
                }
            }
        }
    }
}
