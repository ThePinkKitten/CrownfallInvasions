/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 *  org.zeith.hammerlib.net.IPacket
 *  org.zeith.hammerlib.net.Network
 *  org.zeith.hammerlib.net.PacketContext
 *  org.zeith.hammerlib.util.java.Cast
 *  org.zeith.hammerlib.util.mcf.LogicalSidePredictor
 */
package org.zeith.improvableskills.net;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.net.PacketContext;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.mcf.LogicalSidePredictor;
import org.zeith.improvableskills.SyncSkills;
import org.zeith.improvableskills.api.IGuiSkillDataConsumer;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.data.PlayerDataManager;

public class PacketSyncSkillData
implements IPacket {
    public CompoundTag nbt;

    public static void sync(ServerPlayer mp) {
        try {
            if (mp != null) {
                PlayerDataManager.handleDataSafely((Player)mp, data -> Network.sendTo((IPacket)new PacketSyncSkillData((PlayerSkillData)data), (ServerPlayer)mp));
            }
        }
        catch (NullPointerException nullPointerException) {
            // empty catch block
        }
    }

    private PacketSyncSkillData(PlayerSkillData data) {
        this.nbt = data.serializeNBT();
        this.nbt.m_128405_("PlayerLocalXPLevel", data.player.f_36078_);
        this.nbt.m_128350_("PlayerLocalXPProgress", data.player.f_36080_);
        this.nbt.m_128350_("PlayerLocalHealth", data.player.m_21223_());
    }

    public PacketSyncSkillData() {
        this.nbt = new CompoundTag();
    }

    public void serverExecute(PacketContext ctx) {
        PlayerDataManager.handleDataSafely((Player)ctx.getSender(), data -> ctx.withReply((IPacket)new PacketSyncSkillData((PlayerSkillData)data)));
    }

    public boolean executeOnMainThread() {
        return LogicalSidePredictor.getCurrentLogicalSide().isClient();
    }

    @OnlyIn(value=Dist.CLIENT)
    public void clientExecute(PacketContext net) {
        LocalPlayer player = Minecraft.m_91087_().f_91074_;
        if (player == null) {
            return;
        }
        SyncSkills.handle((Player)player, this);
        Cast.optionally((Object)Minecraft.m_91087_().f_91080_, IGuiSkillDataConsumer.class).ifPresent(c -> c.applySkillData(SyncSkills.getData()));
        if (this.nbt.m_128441_("PlayerLocalXPLevel")) {
            player.f_36078_ = this.nbt.m_128451_("PlayerLocalXPLevel");
        }
        if (this.nbt.m_128441_("PlayerLocalXPProgress")) {
            player.f_36080_ = this.nbt.m_128457_("PlayerLocalXPProgress");
        }
        if (this.nbt.m_128441_("PlayerLocalHealth")) {
            player.m_21153_(this.nbt.m_128457_("PlayerLocalHealth"));
        }
        player.getPersistentData().m_128365_("ImprovableSkillsData", (Tag)this.nbt);
    }

    public CompoundTag getNbt() {
        return this.nbt;
    }

    public void write(FriendlyByteBuf buf) {
        buf.m_130079_(this.nbt);
    }

    public void read(FriendlyByteBuf buf) {
        this.nbt = buf.m_130260_();
    }
}

