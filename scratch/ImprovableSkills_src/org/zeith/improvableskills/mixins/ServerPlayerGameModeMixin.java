/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.server.level.ServerPlayerGameMode
 *  net.minecraft.world.Containers
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.eventbus.api.Event
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package org.zeith.improvableskills.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.zeith.improvableskills.api.evt.HarvestDropsEvent;

@Mixin(value={ServerPlayerGameMode.class})
public class ServerPlayerGameModeMixin {
    @Shadow
    protected ServerLevel f_9244_;
    @Shadow
    @Final
    protected ServerPlayer f_9245_;

    @Inject(method={"removeBlock"}, at={@At(value="HEAD")}, remap=false)
    public void removeBlock_IS3(BlockPos p_180235_1_, boolean canHarvest, CallbackInfoReturnable<Boolean> cir) {
        HarvestDropsEvent drops = new HarvestDropsEvent((LevelAccessor)this.f_9244_, p_180235_1_, this.f_9244_.m_8055_(p_180235_1_), (Player)this.f_9245_);
        MinecraftForge.EVENT_BUS.post((Event)drops);
        Containers.m_19010_((Level)this.f_9244_, (BlockPos)p_180235_1_, drops.getDrops());
    }
}

