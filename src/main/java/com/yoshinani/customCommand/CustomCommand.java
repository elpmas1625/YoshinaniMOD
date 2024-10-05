package com.yoshinani.customCommand;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CustomCommand {
    private static final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("yoshinastop")
                .executes(CustomCommand::stopServer));
    }

    private static int stopServer(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(() -> {
            return Component.translatable("commands.stop.stopping");
        }, true);
        context.getSource().getServer().halt(false);
        return 1;
    }
}
