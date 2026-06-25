/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.player.Player
 *  org.zeith.hammerlib.net.INBTPacket
 *  org.zeith.hammerlib.net.MainThreaded
 *  org.zeith.hammerlib.net.PacketContext
 */
package org.zeith.improvableskills.net;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.zeith.hammerlib.net.INBTPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;
import org.zeith.improvableskills.data.PlayerDataManager;

@MainThreaded
public class PacketSetMagnetismData
implements INBTPacket {
    public Float range;
    public Boolean enabled;

    public PacketSetMagnetismData(Float range, Boolean enabled) {
        this.range = range;
        this.enabled = enabled;
    }

    public PacketSetMagnetismData(Float range) {
        this.range = range;
    }

    public PacketSetMagnetismData(Boolean enabled) {
        this.enabled = enabled;
    }

    public PacketSetMagnetismData() {
    }

    public void write(CompoundTag nbt) {
        if (this.range != null) {
            nbt.m_128350_("Range", this.range.floatValue());
        }
        if (this.enabled != null) {
            nbt.m_128379_("Enabled", this.enabled.booleanValue());
        }
    }

    public void read(CompoundTag nbt) {
        if (nbt.m_128441_("Range")) {
            this.range = Float.valueOf(nbt.m_128457_("Range"));
        }
        if (nbt.m_128441_("Enabled")) {
            this.enabled = nbt.m_128471_("Enabled");
        }
    }

    public void serverExecute(PacketContext ctx) {
        PlayerDataManager.handleDataSafely((Player)ctx.getSender(), data -> {
            if (this.enabled != null) {
                data.magnetism = this.enabled;
            }
            if (this.range != null) {
                data.magnetismRange = Mth.m_14036_((float)this.range.floatValue(), (float)0.0f, (float)8.0f);
            }
        });
    }
}

