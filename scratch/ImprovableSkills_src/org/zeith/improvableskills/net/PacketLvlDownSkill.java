/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientboundSetExperiencePacket
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  org.zeith.hammerlib.net.IPacket
 *  org.zeith.hammerlib.net.MainThreaded
 *  org.zeith.hammerlib.net.PacketContext
 */
package org.zeith.improvableskills.net;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;
import org.zeith.improvableskills.ImprovableSkills;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;
import org.zeith.improvableskills.data.PlayerDataManager;
import org.zeith.improvableskills.net.PacketSyncSkillData;

@MainThreaded
public class PacketLvlDownSkill
implements IPacket {
    public ResourceLocation skill;

    public PacketLvlDownSkill(PlayerSkillBase skill) {
        this.skill = skill.getRegistryName();
    }

    public PacketLvlDownSkill() {
    }

    public void write(FriendlyByteBuf buf) {
        buf.m_130085_(this.skill);
    }

    public void read(FriendlyByteBuf buf) {
        this.skill = buf.m_130281_();
    }

    public void serverExecute(PacketContext ctx) {
        ServerPlayer player = ctx.getSender();
        PlayerDataManager.handleDataSafely((Player)player, data -> {
            PlayerSkillBase skill = (PlayerSkillBase)ImprovableSkills.SKILLS().getValue(this.skill);
            if (skill == null) {
                return;
            }
            short lvl = data.getSkillLevel(skill);
            if (lvl > 0 && skill.isDowngradable((PlayerSkillData)data)) {
                data.setSkillLevel(skill, lvl - 1);
                skill.onUpgrade(lvl, (short)(lvl - 1), (PlayerSkillData)data);
                skill.onDowngrade((PlayerSkillData)data, lvl);
                player.f_8906_.m_9829_((Packet)new ClientboundSetExperiencePacket(player.f_36080_, player.f_36079_, player.f_36078_));
                PacketSyncSkillData.sync(player);
            }
        });
    }
}

