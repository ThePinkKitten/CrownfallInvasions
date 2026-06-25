/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  net.minecraftforge.eventbus.api.IEventBus
 *  org.zeith.hammerlib.api.proxy.IProxy
 */
package org.zeith.improvableskills.proxy;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import org.zeith.hammerlib.api.proxy.IProxy;

public class ISServer
implements IProxy {
    public void register(IEventBus modBus) {
    }

    public boolean hasShiftDown() {
        return false;
    }

    public Player getClientPlayer() {
        return null;
    }

    public void sparkle(Level level, double x, double y, double z, double xMove, double yMove, double zMove, int color, int maxAge) {
    }
}

