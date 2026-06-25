/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  org.zeith.hammerlib.net.IPacket
 *  org.zeith.hammerlib.net.MainThreaded
 *  org.zeith.hammerlib.net.PacketContext
 */
package org.zeith.improvableskills.net;

import net.minecraft.network.FriendlyByteBuf;
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
public class PacketLvlUpSkill
implements IPacket {
    public ResourceLocation skill;

    public PacketLvlUpSkill(PlayerSkillBase skill) {
        this.skill = skill.getRegistryName();
    }

    public PacketLvlUpSkill() {
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
            if (skill.canUpgrade((PlayerSkillData)data) && lvl < 32766) {
                data.setSkillLevel(skill, lvl + 1);
                skill.onUpgrade(lvl, (short)(lvl + 1), (PlayerSkillData)data);
                PacketSyncSkillData.sync(player);
            }
        });
    }
}

