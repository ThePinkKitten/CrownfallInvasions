/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.common.Tags$EntityTypes
 *  net.minecraftforge.event.entity.player.PlayerInteractEvent$EntityInteract
 *  net.minecraftforge.eventbus.api.Event
 *  net.minecraftforge.eventbus.api.Event$Result
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  org.zeith.hammerlib.net.IPacket
 *  org.zeith.hammerlib.net.Network
 */
package org.zeith.improvableskills.custom.abilities;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.Network;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.api.evt.CowboyStartEvent;
import org.zeith.improvableskills.api.registry.PlayerAbilityBase;
import org.zeith.improvableskills.data.PlayerDataManager;
import org.zeith.improvableskills.init.TagsIS3;
import org.zeith.improvableskills.net.PacketSetCowboyData;

public class AbilityCowboy
extends PlayerAbilityBase {
    public AbilityCowboy() {
        this.setColor(13734656);
        MinecraftForge.EVENT_BUS.addListener(this::entityClick);
    }

    @SubscribeEvent
    public void entityClick(PlayerInteractEvent.EntityInteract e) {
        LivingEntity le;
        Entity entity = e.getTarget();
        if (!(entity instanceof LivingEntity) || (le = (LivingEntity)entity).m_21224_()) {
            return;
        }
        PlayerDataManager.handleDataSafely(e.getEntity(), data -> {
            CowboyStartEvent evt;
            if (data.cowboy && !MinecraftForge.EVENT_BUS.post((Event)(evt = new CowboyStartEvent(data.player, le)))) {
                if (evt.getResult() == Event.Result.DENY) {
                    return;
                }
                if (evt.getResult() == Event.Result.DEFAULT) {
                    if (le.m_6095_().m_204039_(Tags.EntityTypes.BOSSES)) {
                        return;
                    }
                    if (le.m_6095_().m_204039_(TagsIS3.EntityTypes.PREVENT_COWBOY_INTERACTION)) {
                        return;
                    }
                }
                if (!data.player.m_9236_().m_5776_()) {
                    data.player.m_20329_((Entity)le);
                }
                e.setCanceled(true);
            }
        });
    }

    @Override
    public void onUnlocked(PlayerSkillData data) {
        data.cowboy = true;
    }

    @Override
    public boolean showDisabledIcon(PlayerSkillData data) {
        return !data.cowboy;
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void onClickClient(Player player, int mouseButton) {
        PlayerDataManager.handleDataSafely(player, data -> {
            data.cowboy = !data.cowboy;
            Network.sendToServer((IPacket)new PacketSetCowboyData(data.cowboy));
        });
    }
}

