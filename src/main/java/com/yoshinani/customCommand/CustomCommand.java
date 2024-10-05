package com.yoshinani.customCommand;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.yoshinani.money.Money;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;

public class CustomCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("yoshinastop")
                .executes(CustomCommand::stopServer));
    }

    private static int stopServer(CommandContext<CommandSourceStack> context) {
        Money.savePlayerMoney();
        MinecraftServer server = context.getSource().getServer();
        server.halt(true);
        return Command.SINGLE_SUCCESS;
    }
}