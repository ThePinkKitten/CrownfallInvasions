package com.thepinkkitten.crownfallinvasions.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import com.thepinkkitten.crownfallinvasions.CrownfallWorldData;

public class CrownfallConfigCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("crownfall_config")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("get_kills")
                        .executes(context -> {
                            ServerLevel level = context.getSource().getLevel();
                            int kills = CrownfallWorldData.get(level).getGlobalKillCount();
                            context.getSource().sendSuccess(() -> net.minecraft.network.chat.Component.literal("Current Global Kills: " + kills), false);
                            return kills;
                        })
                )
                .then(Commands.literal("set_kills")
                        .then(Commands.argument("kills", IntegerArgumentType.integer(0))
                                .executes(context -> {
                                    int newKills = IntegerArgumentType.getInteger(context, "kills");
                                    ServerLevel level = context.getSource().getLevel();
                                    CrownfallWorldData data = CrownfallWorldData.get(level);
                                    data.setGlobalKillCount(newKills);
                                    context.getSource().sendSuccess(() -> net.minecraft.network.chat.Component.literal("Global Kills set to: " + newKills), true);
                                    return 1;
                                })
                        )
                )
        );
    }
}
