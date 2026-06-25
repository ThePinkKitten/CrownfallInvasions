/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.gameevent.GameEvent
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 *  org.zeith.hammerlib.net.IPacket
 *  org.zeith.hammerlib.net.MainThreaded
 *  org.zeith.hammerlib.net.Network
 *  org.zeith.hammerlib.net.PacketContext
 *  org.zeith.hammerlib.util.java.Threading
 */
package org.zeith.improvableskills.net;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.net.PacketContext;
import org.zeith.hammerlib.util.java.Threading;
import org.zeith.improvableskills.ImprovableSkills;
import org.zeith.improvableskills.SyncSkills;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.api.registry.PageletBase;
import org.zeith.improvableskills.client.gui.base.GuiTabbable;
import org.zeith.improvableskills.data.PlayerDataManager;

@MainThreaded
public class PacketOpenSkillsBook
implements IPacket {
    private CompoundTag nbt;

    public static void sync(ServerPlayer mp) {
        if (mp != null) {
            PlayerDataManager.handleDataSafely((Player)mp, data -> Network.sendTo((IPacket)new PacketOpenSkillsBook((PlayerSkillData)data), (ServerPlayer)mp));
            mp.m_9236_().m_220400_((Entity)mp, GameEvent.f_157811_, mp.m_20182_());
        }
    }

    PacketOpenSkillsBook(PlayerSkillData data) {
        this.nbt = data.serializeNBT();
    }

    public PacketOpenSkillsBook() {
    }

    public void serverExecute(PacketContext net) {
        PacketOpenSkillsBook.sync(net.getSender());
    }

    @OnlyIn(value=Dist.CLIENT)
    public void clientExecute(PacketContext net) {
        Minecraft mc = Minecraft.m_91087_();
        SyncSkills.handle((Player)mc.f_91074_, this);
        mc.m_91152_(GuiTabbable.lastPagelet.createTab(SyncSkills.getData()));
        Threading.createAndStart(() -> ImprovableSkills.PAGELETS().forEach(PageletBase::reload));
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

