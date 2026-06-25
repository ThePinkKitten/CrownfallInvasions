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
public class PacketSetAutoXpBankData
implements INBTPacket {
    public Integer threshold;
    public Boolean enabled;

    public PacketSetAutoXpBankData(Integer threshold, Boolean enabled) {
        this.threshold = threshold;
        this.enabled = enabled;
    }

    public PacketSetAutoXpBankData(Integer threshold) {
        this.threshold = threshold;
    }

    public PacketSetAutoXpBankData(Boolean enabled) {
        this.enabled = enabled;
    }

    public PacketSetAutoXpBankData() {
    }

    public void write(CompoundTag nbt) {
        if (this.threshold != null) {
            nbt.m_128405_("Threshold", this.threshold.intValue());
        }
        if (this.enabled != null) {
            nbt.m_128379_("Enabled", this.enabled.booleanValue());
        }
    }

    public void read(CompoundTag nbt) {
        if (nbt.m_128441_("Threshold")) {
            this.threshold = nbt.m_128451_("Threshold");
        }
        if (nbt.m_128441_("Enabled")) {
            this.enabled = nbt.m_128471_("Enabled");
        }
    }

    public void serverExecute(PacketContext ctx) {
        PlayerDataManager.handleDataSafely((Player)ctx.getSender(), data -> {
            if (this.enabled != null) {
                data.autoXpBank = this.enabled;
            }
            if (this.threshold != null) {
                data.autoXpBankThreshold = Mth.m_14045_((int)this.threshold, (int)0, (int)Integer.MAX_VALUE);
            }
        });
    }
}

