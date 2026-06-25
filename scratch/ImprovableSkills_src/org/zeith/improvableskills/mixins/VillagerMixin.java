/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.npc.AbstractVillager
 *  net.minecraft.world.entity.npc.Villager
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.eventbus.api.Event
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package org.zeith.improvableskills.mixins;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zeith.improvableskills.api.evt.ApplySpecialPricesEvent;

@Mixin(value={Villager.class})
public abstract class VillagerMixin
extends AbstractVillager {
    public VillagerMixin(EntityType<? extends AbstractVillager> type, Level level) {
        super(type, level);
    }

    @Inject(method={"updateSpecialPrices"}, at={@At(value="HEAD")})
    private void updateSpecialPrices_IS3(Player forPlayer, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post((Event)new ApplySpecialPricesEvent(this, forPlayer));
    }
}

