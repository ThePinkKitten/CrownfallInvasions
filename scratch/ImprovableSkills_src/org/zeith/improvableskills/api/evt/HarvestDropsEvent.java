/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.NonNullList
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraftforge.event.level.BlockEvent
 */
package org.zeith.improvableskills.api.evt;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;

public class HarvestDropsEvent
extends BlockEvent {
    private final NonNullList<ItemStack> drops = NonNullList.m_122779_();
    private final Player entity;

    public HarvestDropsEvent(LevelAccessor level, BlockPos pos, BlockState state, Player entity) {
        super(level, pos, state);
        this.entity = entity;
    }

    public Player getEntity() {
        return this.entity;
    }

    public NonNullList<ItemStack> getDrops() {
        return this.drops;
    }
}

