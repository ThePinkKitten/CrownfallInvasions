/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.world.entity.player.Player
 *  org.zeith.hammerlib.net.INBTPacket
 *  org.zeith.hammerlib.net.MainThreaded
 *  org.zeith.hammerlib.net.PacketContext
 */
package org.zeith.improvableskills.net;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.zeith.hammerlib.net.INBTPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;
import org.zeith.improvableskills.data.PlayerDataManager;

@MainThreaded
public class PacketSetCowboyData
implements INBTPacket {
    public Boolean enabled;

    public PacketSetCowboyData(Boolean enabled) {
        this.enabled = enabled;
    }

    public PacketSetCowboyData() {
    }

    public void write(CompoundTag nbt) {
        if (this.enabled != null) {
            nbt.m_128379_("Enabled", this.enabled.booleanValue());
        }
    }

    public void read(CompoundTag nbt) {
        if (nbt.m_128441_("Enabled")) {
            this.enabled = nbt.m_128471_("Enabled");
        }
    }

    public void serverExecute(PacketContext ctx) {
        PlayerDataManager.handleDataSafely((Player)ctx.getSender(), data -> {
            if (this.enabled != null) {
                data.cowboy = this.enabled;
            }
        });
    }
}

