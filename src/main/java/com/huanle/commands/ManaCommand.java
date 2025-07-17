package com.huanle.commands;

import com.huanle.capabilities.ManaUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

/**
 * 魔力系统测试命令
 */
public class ManaCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("mana")
            .requires(source -> source.hasPermission(2))
            .then(Commands.literal("get")
                .then(Commands.argument("player", EntityArgument.player())
                    .executes(context -> getMana(context, EntityArgument.getPlayer(context, "player")))
                )
                .executes(context -> getMana(context, context.getSource().getPlayerOrException()))
            )
            .then(Commands.literal("set")
                .then(Commands.argument("player", EntityArgument.player())
                    .then(Commands.argument("amount", FloatArgumentType.floatArg(0))
                        .executes(context -> setMana(context, EntityArgument.getPlayer(context, "player"), FloatArgumentType.getFloat(context, "amount")))
                    )
                )
            )
            .then(Commands.literal("add")
                .then(Commands.argument("player", EntityArgument.player())
                    .then(Commands.argument("amount", FloatArgumentType.floatArg(0))
                        .executes(context -> addMana(context, EntityArgument.getPlayer(context, "player"), FloatArgumentType.getFloat(context, "amount")))
                    )
                )
            )
            .then(Commands.literal("setmax")
                .then(Commands.argument("player", EntityArgument.player())
                    .then(Commands.argument("amount", FloatArgumentType.floatArg(1))
                        .executes(context -> setMaxMana(context, EntityArgument.getPlayer(context, "player"), FloatArgumentType.getFloat(context, "amount")))
                    )
                )
            )
        );
    }
    
    private static int getMana(CommandContext<CommandSourceStack> context, ServerPlayer player) {
        float currentMana = ManaUtils.getCurrentMana(player);
        float maxMana = ManaUtils.getMaxMana(player);
        
        context.getSource().sendSuccess(() -> Component.literal(
            Component.translatable("huanle.command.mana.get", player.getName().getString(), (int)currentMana, (int)maxMana).getString()
        ), false);
        
        return 1;
    }
    
    private static int setMana(CommandContext<CommandSourceStack> context, ServerPlayer player, float amount) {
        ManaUtils.setMana(player, amount);
        ManaUtils.syncManaToClient(player);
        
        context.getSource().sendSuccess(() -> Component.literal(
            Component.translatable("huanle.command.mana.set", player.getName().getString(), (int)amount).getString()
        ), true);
        
        return 1;
    }
    
    private static int addMana(CommandContext<CommandSourceStack> context, ServerPlayer player, float amount) {
        float oldMana = ManaUtils.getCurrentMana(player);
        ManaUtils.restoreMana(player, amount);
        float newMana = ManaUtils.getCurrentMana(player);
        
        context.getSource().sendSuccess(() -> Component.literal(
            Component.translatable("huanle.command.mana.add", 
                 player.getName().getString(), (int)amount, (int)oldMana, (int)newMana).getString()
        ), true);
        
        return 1;
    }
    
    private static int setMaxMana(CommandContext<CommandSourceStack> context, ServerPlayer player, float amount) {
        ManaUtils.setMaxMana(player, amount);
        ManaUtils.syncManaToClient(player);
        
        context.getSource().sendSuccess(() -> Component.literal(
            Component.translatable("huanle.command.mana.setmax", player.getName().getString(), (int)amount).getString()
        ), true);
        
        return 1;
    }
}