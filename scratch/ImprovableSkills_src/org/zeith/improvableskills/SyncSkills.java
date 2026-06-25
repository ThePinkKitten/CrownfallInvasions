/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.world.entity.player.Player
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 */
package org.zeith.improvableskills;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.improvableskills.ImprovableSkills;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.net.PacketOpenSkillsBook;
import org.zeith.improvableskills.net.PacketSyncSkillData;

public class SyncSkills {
    private static PlayerSkillData CLIENT_DATA;

    public static boolean is(PlayerSkillData data) {
        return data == CLIENT_DATA;
    }

    public static void doCheck(Player localPlayer) {
        if (localPlayer == null && CLIENT_DATA != null) {
            ImprovableSkills.LOG.info("Reset client skill data.");
            CLIENT_DATA = null;
        }
    }

    @OnlyIn(value=Dist.CLIENT)
    public static PlayerSkillData getData() {
        LocalPlayer mcp = Minecraft.m_91087_().f_91074_;
        if (CLIENT_DATA == null || SyncSkills.CLIENT_DATA.player != mcp) {
            CLIENT_DATA = new PlayerSkillData((Player)mcp);
            CLIENT_DATA.requestSync();
        }
        return CLIENT_DATA;
    }

    public static void handle(Player localPlayer, PacketOpenSkillsBook packet) {
        CLIENT_DATA = PlayerSkillData.deserialize(localPlayer, packet.getNbt());
    }

    public static void handle(Player localPlayer, PacketSyncSkillData packet) {
        CLIENT_DATA = PlayerSkillData.deserialize(localPlayer, packet.getNbt());
    }
}

