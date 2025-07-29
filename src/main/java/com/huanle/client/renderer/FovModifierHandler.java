package com.huanle.client.renderer;

import com.huanle.HuanleMod;
import com.huanle.items.weapons.FlameBowItem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HuanleMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = net.minecraftforge.api.distmarker.Dist.CLIENT)
public class FovModifierHandler {

    @SubscribeEvent
    public static void onComputeFovModifier(ComputeFovModifierEvent event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack itemStack = player.getUseItem();
        if (!itemStack.isEmpty() && player.isUsingItem()) {
            if (itemStack.getItem() instanceof net.minecraft.world.item.BowItem ||
                itemStack.getUseAnimation() == net.minecraft.world.item.UseAnim.BOW) {
                if (itemStack.getItem() instanceof FlameBowItem) {
                    event.setNewFovModifier(1.0F);
                }
            }
        }
    }
}
