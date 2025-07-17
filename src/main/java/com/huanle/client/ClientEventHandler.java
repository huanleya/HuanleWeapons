package com.huanle.client;

import com.huanle.ModItems;
import com.huanle.ModMenuTypes;
import com.huanle.client.gui.GrinderScreen;
import com.huanle.client.ModKeybind;
import com.huanle.network.NetworkHandler;
import com.huanle.network.OpenEnderChestMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

@Mod.EventBusSubscriber(modid = "huanle", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventHandler {
    
    @SubscribeEvent
    public static void onClientTick(net.minecraftforge.event.TickEvent.ClientTickEvent event) {
        if (event.phase != net.minecraftforge.event.TickEvent.Phase.END) {
            return;
        }
        
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        
        if (player == null || minecraft.screen != null) {
            return;
        }
        
        if (ModKeybind.ENDER_BELT_KEY.consumeClick()) {
            CuriosApi.getCuriosInventory(player).ifPresent(curiosInventory -> {
                ItemStack beltStack = curiosInventory.findFirstCurio(ModItems.ENDER_BELT.get()).map(slotResult -> slotResult.stack()).orElse(ItemStack.EMPTY);
                
                if (!beltStack.isEmpty()) {
                    NetworkHandler.sendToServer(new OpenEnderChestMessage());
                }
            });
        }
        

    }
    
    @Mod.EventBusSubscriber(modid = "huanle", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ModEventHandler {
        
        @SubscribeEvent
        public static void registerKeybinds(RegisterKeyMappingsEvent event) {
            event.register(ModKeybind.ENDER_BELT_KEY);
        }
        
        @SubscribeEvent
        public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
            event.registerAbove(net.minecraftforge.client.gui.overlay.VanillaGuiOverlay.DEBUG_TEXT.id(), "mana_hud", new ManaHUD());
        }
        
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                MenuScreens.register(ModMenuTypes.GRINDER_MENU.get(), GrinderScreen::new);
                net.minecraftforge.fml.ModLoadingContext.get().registerExtensionPoint(
                    net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory.class,
                    () -> new net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory(
                        (mc, screen) -> new ConfigScreen(screen)
                    )
                );
            });
        }
    }
}