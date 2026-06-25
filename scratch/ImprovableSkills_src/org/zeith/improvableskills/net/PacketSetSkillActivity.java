/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.player.Player
 *  org.zeith.hammerlib.net.IPacket
 *  org.zeith.hammerlib.net.MainThreaded
 *  org.zeith.hammerlib.net.PacketContext
 */
package org.zeith.improvableskills.net;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;
import org.zeith.improvableskills.ImprovableSkills;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;
import org.zeith.improvableskills.data.PlayerDataManager;

@MainThreaded
public class PacketSetSkillActivity
implements IPacket {
    private ResourceLocation skillId;
    private boolean enabled;

    public PacketSetSkillActivity(ResourceLocation skillId, boolean enabled) {
        this.skillId = skillId;
        this.enabled = enabled;
    }

    public PacketSetSkillActivity() {
    }

    public void write(FriendlyByteBuf buf) {
        buf.m_130085_(this.skillId);
        buf.writeBoolean(this.enabled);
    }

    public void read(FriendlyByteBuf buf) {
        this.skillId = buf.m_130281_();
        this.enabled = buf.readBoolean();
    }

    public void serverExecute(PacketContext ctx) {
        PlayerDataManager.handleDataSafely((Player)ctx.getSender(), data -> {
            PlayerSkillBase skill = (PlayerSkillBase)ImprovableSkills.SKILLS().getValue(this.skillId);
            if (skill != null) {
                data.setSkillState(skill, this.enabled);
            }
        });
    }
}

