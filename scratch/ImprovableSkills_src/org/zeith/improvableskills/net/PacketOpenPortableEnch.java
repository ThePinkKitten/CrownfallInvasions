/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  org.zeith.hammerlib.net.IPacket
 *  org.zeith.hammerlib.net.MainThreaded
 *  org.zeith.hammerlib.net.PacketContext
 */
package org.zeith.improvableskills.net;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;
import org.zeith.improvableskills.data.PlayerDataManager;
import org.zeith.improvableskills.init.AbilitiesIS;
import org.zeith.improvableskills.init.GuiHooksIS;
import org.zeith.improvableskills.utils.GuiManager;

@MainThreaded
public class PacketOpenPortableEnch
implements IPacket {
    public void serverExecute(PacketContext net) {
        ServerPlayer mp = net.getSender();
        PlayerDataManager.handleDataSafely((Player)mp, dat -> {
            if (AbilitiesIS.ENCHANTING.registered() && dat.hasAbility(AbilitiesIS.ENCHANTING)) {
                GuiManager.openGuiCallback(GuiHooksIS.ENCHANTMENT, (Player)mp, (Component)Component.m_237115_((String)"container.enchant"));
            }
        });
    }
}

