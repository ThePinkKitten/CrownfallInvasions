/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.gameevent.vibrations.VibrationInfo
 *  net.minecraft.world.level.gameevent.vibrations.VibrationSystem$Data
 *  net.minecraft.world.level.gameevent.vibrations.VibrationSystem$User
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.eventbus.api.Event
 */
package org.zeith.improvableskills.proxy;

import java.util.function.Consumer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.vibrations.VibrationInfo;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import org.zeith.improvableskills.api.evt.DamageItemEvent;
import org.zeith.improvableskills.api.evt.VibrationEvent;

public class ASMProxy {
    public static int hurtItem(ItemStack stack, int damageBy, LivingEntity entity, Consumer<LivingEntity> onBroken) {
        DamageItemEvent evt = new DamageItemEvent(stack, entity, damageBy);
        MinecraftForge.EVENT_BUS.post((Event)evt);
        return evt.getNewDamage();
    }

    public static boolean cancelVibrationReception(ServerLevel level, VibrationSystem.Data event, VibrationSystem.User context, VibrationInfo info) {
        if (MinecraftForge.EVENT_BUS.post((Event)new VibrationEvent(level, event, context, info))) {
            event.m_280036_(null);
            return true;
        }
        return false;
    }
}

