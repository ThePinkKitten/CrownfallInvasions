/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity
 *  net.minecraftforge.event.entity.player.PlayerEvent
 */
package org.zeith.improvableskills.api.evt;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class CalculateAdditionalFurnaceExperienceMultiplier
extends PlayerEvent {
    private float multiplier = 0.0f;

    public CalculateAdditionalFurnaceExperienceMultiplier(Player player, AbstractFurnaceBlockEntity furnace) {
        super(player);
    }

    public float getMultiplier() {
        return this.multiplier;
    }

    public void addExtraPercent(float multiplier) {
        this.multiplier += multiplier;
    }
}

