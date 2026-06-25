package com.thepinkkitten.crownfallinvasions.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import com.thepinkkitten.crownfallinvasions.event.CrownfallSpawnEvent;

public class SummonCrownfallCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("summon_crownfall_horde")
                .requires(source -> source.hasPermission(2))
                .executes(context -> {
                    ServerLevel level = context.getSource().getLevel();
                    BlockPos pos = BlockPos.containing(context.getSource().getPosition());
                    CrownfallSpawnEvent.spawnHorde(level, pos);
                    context.getSource().sendSuccess(() -> net.minecraft.network.chat.Component.literal("Crownfall Invasions summoned!"), true);
                    return 1;
                }));
    }
}
