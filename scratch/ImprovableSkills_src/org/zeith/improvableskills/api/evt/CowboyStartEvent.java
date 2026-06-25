/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraftforge.event.entity.player.PlayerEvent
 *  net.minecraftforge.eventbus.api.Event$HasResult
 */
package org.zeith.improvableskills.api.evt;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;

@Event.HasResult
public class CowboyStartEvent
extends PlayerEvent {
    private final LivingEntity target;

    public CowboyStartEvent(Player player, LivingEntity target) {
        super(player);
        this.target = target;
    }

    public LivingEntity target() {
        return this.target;
    }
}

