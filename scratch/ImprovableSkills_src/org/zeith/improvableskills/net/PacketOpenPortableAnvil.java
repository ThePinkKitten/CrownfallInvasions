/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  org.zeith.hammerlib.net.IPacket
 *  org.zeith.hammerlib.net.PacketContext
 */
package org.zeith.improvableskills.net;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.PacketContext;
import org.zeith.improvableskills.data.PlayerDataManager;
import org.zeith.improvableskills.init.AbilitiesIS;
import org.zeith.improvableskills.init.GuiHooksIS;
import org.zeith.improvableskills.utils.GuiManager;

public class PacketOpenPortableAnvil
implements IPacket {
    private static final Component CONTAINER_TITLE = Component.m_237115_((String)"container.repair");

    public void serverExecute(PacketContext net) {
        ServerPlayer mp = net.getSender();
        PlayerDataManager.handleDataSafely((Player)mp, dat -> {
            if (AbilitiesIS.ANVIL.registered() && dat.hasAbility(AbilitiesIS.ANVIL)) {
                GuiManager.openGuiCallback(GuiHooksIS.REPAIR, (Player)mp, CONTAINER_TITLE);
            }
        });
    }
}

