/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.SimpleMenuProvider
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.ContainerLevelAccess
 *  net.minecraft.world.level.Level
 *  org.zeith.hammerlib.net.IPacket
 *  org.zeith.hammerlib.net.MainThreaded
 *  org.zeith.hammerlib.net.PacketContext
 */
package org.zeith.improvableskills.net;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;
import org.zeith.improvableskills.client.gui.abil.crafting.CraftingMenuPortable;
import org.zeith.improvableskills.data.PlayerDataManager;
import org.zeith.improvableskills.init.AbilitiesIS;

@MainThreaded
public class PacketOpenPortableCraft
implements IPacket {
    private static final Component CONTAINER_TITLE = Component.m_237115_((String)"container.crafting");

    public void serverExecute(PacketContext net) {
        ServerPlayer mp = net.getSender();
        PlayerDataManager.handleDataSafely((Player)mp, dat -> {
            if (AbilitiesIS.CRAFTER.registered() && dat.hasAbility(AbilitiesIS.CRAFTER)) {
                mp.m_5893_((MenuProvider)new SimpleMenuProvider((windowId, inventory, player) -> new CraftingMenuPortable(windowId, inventory, ContainerLevelAccess.m_39289_((Level)player.m_9236_(), (BlockPos)player.m_20183_())), CONTAINER_TITLE));
            }
        });
    }
}

