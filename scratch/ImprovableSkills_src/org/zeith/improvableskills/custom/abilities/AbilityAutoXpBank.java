/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.world.entity.player.Player
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 *  org.zeith.hammerlib.net.IPacket
 *  org.zeith.hammerlib.net.Network
 *  org.zeith.hammerlib.util.XPUtil
 */
package org.zeith.improvableskills.custom.abilities;

import java.math.BigInteger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.util.XPUtil;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.api.registry.PlayerAbilityBase;
import org.zeith.improvableskills.client.gui.abil.GuiAutoXpBank;
import org.zeith.improvableskills.data.PlayerDataManager;
import org.zeith.improvableskills.net.PacketSetAutoXpBankData;

public class AbilityAutoXpBank
extends PlayerAbilityBase {
    public AbilityAutoXpBank() {
        this.setColor(0x33FF00);
    }

    @Override
    public void tick(PlayerSkillData data) {
        if (data.autoXpBank && !data.player.m_9236_().f_46443_ && data.atTickRate(2)) {
            int threshold = data.autoXpBankThreshold;
            int playerXP = XPUtil.getXPTotal((Player)data.player);
            if (playerXP > threshold) {
                int diff = Math.max(1, (int)Math.floor(Math.sqrt(playerXP - threshold)));
                XPUtil.takeXP((Player)data.player, (int)diff);
                data.storageXp = data.storageXp.add(BigInteger.valueOf(diff));
                data.sync();
            } else if (playerXP < threshold) {
                int diff = Math.max(1, (int)Math.floor(Math.sqrt(threshold - playerXP)));
                BigInteger diffBI = data.storageXp.min(BigInteger.valueOf(diff));
                XPUtil.giveXP((Player)data.player, (int)diffBI.intValue());
                data.storageXp = data.storageXp.subtract(diffBI);
                data.sync();
            }
        }
    }

    @Override
    public void onUnlocked(PlayerSkillData data) {
        data.autoXpBank = false;
        data.autoXpBankThreshold = XPUtil.getXPValueFromLevel((int)30);
    }

    @Override
    public boolean showDisabledIcon(PlayerSkillData data) {
        return !data.autoXpBank;
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void onClickClient(Player player, int mouseButton) {
        PlayerDataManager.handleDataSafely(player, data -> {
            if (mouseButton == 1) {
                data.autoXpBank = !data.autoXpBank;
                Network.sendToServer((IPacket)new PacketSetAutoXpBankData(data.autoXpBank));
            } else if (mouseButton == 0) {
                Minecraft.m_91087_().pushGuiLayer((Screen)new GuiAutoXpBank((PlayerSkillData)data));
            }
        });
    }
}

