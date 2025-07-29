package com.huanle.commands;

import com.huanle.commands.SpawnMysteriousMerchantCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "huanle", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommandHandler {
    
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        SpawnMysteriousMerchantCommand.register(event.getDispatcher());
    }
}