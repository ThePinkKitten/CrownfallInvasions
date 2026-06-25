/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 *  org.zeith.hammerlib.net.IPacket
 *  org.zeith.hammerlib.net.Network
 */
package org.zeith.improvableskills.custom.abilities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.Network;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.api.registry.PlayerAbilityBase;
import org.zeith.improvableskills.client.gui.abil.GuiMagnetism;
import org.zeith.improvableskills.data.PlayerDataManager;
import org.zeith.improvableskills.net.PacketSetMagnetismData;

public class AbilityMagnetism
extends PlayerAbilityBase {
    public AbilityMagnetism() {
        this.setColor(0xFF00FF);
    }

    @Override
    public void onUnlocked(PlayerSkillData data) {
        data.magnetism = true;
        data.magnetismRange = 4.0f;
    }

    @Override
    public void tick(PlayerSkillData data) {
        if (data.magnetism && data.magnetismRange > 1.0f) {
            Vec3 pos = data.player.m_20191_().m_82399_();
            for (ItemEntity ie : data.player.m_9236_().m_45976_(ItemEntity.class, new AABB(pos.f_82479_, pos.f_82480_, pos.f_82481_, pos.f_82479_, pos.f_82480_, pos.f_82481_).m_82400_((double)data.magnetismRange))) {
                if (ie.getPersistentData().m_128471_("PreventRemoteMovement")) continue;
                ie.m_20256_(ie.m_20184_().m_82490_((double)0.98f).m_82549_(data.player.m_20182_().m_82546_(ie.m_20182_()).m_82541_().m_82542_((double)0.1f, (double)0.2f, (double)0.1f)));
            }
        }
    }

    @Override
    public boolean showDisabledIcon(PlayerSkillData data) {
        return !data.magnetism;
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void onClickClient(Player player, int mouseButton) {
        PlayerDataManager.handleDataSafely(player, data -> {
            if (mouseButton == 1) {
                data.magnetism = !data.magnetism;
                Network.sendToServer((IPacket)new PacketSetMagnetismData(data.magnetism));
            } else if (mouseButton == 0) {
                Minecraft.m_91087_().pushGuiLayer((Screen)new GuiMagnetism((PlayerSkillData)data));
            }
        });
    }
}

