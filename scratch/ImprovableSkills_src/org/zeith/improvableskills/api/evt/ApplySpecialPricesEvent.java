/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.npc.AbstractVillager
 *  net.minecraft.world.entity.player.Player
 *  net.minecraftforge.event.entity.living.LivingEvent
 */
package org.zeith.improvableskills.api.evt;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent;

public class ApplySpecialPricesEvent
extends LivingEvent {
    private final Player player;

    public ApplySpecialPricesEvent(AbstractVillager villager, Player player) {
        super((LivingEntity)villager);
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public AbstractVillager getEntity() {
        return (AbstractVillager)super.getEntity();
    }
}

