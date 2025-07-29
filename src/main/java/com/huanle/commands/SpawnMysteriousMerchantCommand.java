package com.huanle.commands;

import com.huanle.ModEntities;
import com.huanle.entities.MysteriousMerchantEntity;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.MobSpawnType;

public class SpawnMysteriousMerchantCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("spawnmerchant")
            .requires(source -> source.hasPermission(2))
            .executes(SpawnMysteriousMerchantCommand::execute));
    }

    private static int execute(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        
        if (source.getEntity() instanceof ServerPlayer player) {
            ServerLevel level = player.serverLevel();
            
            MysteriousMerchantEntity merchant = new MysteriousMerchantEntity(ModEntities.MYSTERIOUS_MERCHANT.get(), level);
            merchant.setPos(player.getX(), player.getY(), player.getZ());
            merchant.finalizeSpawn(level, level.getCurrentDifficultyAt(merchant.blockPosition()), MobSpawnType.COMMAND, null, null);
            
            level.addFreshEntity(merchant);
            
            source.sendSuccess(() -> Component.translatable("command.huanle.spawn_merchant.success"), true);
            return 1;
        }
        
        source.sendFailure(Component.translatable("command.huanle.spawn_merchant.player_only"));
        return 0;
    }
}