package com.thepinkkitten.crownfallinvasions.event;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.thepinkkitten.crownfallinvasions.CrownfallMain;
import com.thepinkkitten.crownfallinvasions.command.SummonCrownfallCommand;

@Mod.EventBusSubscriber(modid = CrownfallMain.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CrownfallForgeEvents {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        SummonCrownfallCommand.register(event.getDispatcher());
        com.thepinkkitten.crownfallinvasions.command.CrownfallConfigCommand.register(event.getDispatcher());
    }
}
