/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  org.zeith.hammerlib.net.IPacket
 *  org.zeith.hammerlib.net.MainThreaded
 *  org.zeith.hammerlib.net.PacketContext
 *  org.zeith.hammerlib.util.XPUtil
 */
package org.zeith.improvableskills.net;

import java.math.BigInteger;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;
import org.zeith.hammerlib.util.XPUtil;
import org.zeith.improvableskills.data.PlayerDataManager;

@MainThreaded
public class PacketStoreXP
implements IPacket {
    public int xp;

    public PacketStoreXP(int xp) {
        this.xp = xp;
    }

    public PacketStoreXP() {
    }

    public void serverExecute(PacketContext net) {
        ServerPlayer player = net.getSender();
        PlayerDataManager.handleDataSafely((Player)player, data -> {
            if (!data.enableXPBank) {
                return;
            }
            int cxp = XPUtil.getXPTotal((Player)player);
            int xp = Math.min(this.xp, cxp);
            XPUtil.setPlayersExpTo((Player)player, (int)(cxp - xp));
            data.storageXp = data.storageXp.add(new BigInteger(Integer.toUnsignedString(xp)));
            data.sync();
        });
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.xp);
    }

    public void read(FriendlyByteBuf buf) {
        this.xp = buf.readInt();
    }
}

