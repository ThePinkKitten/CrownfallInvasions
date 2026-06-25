/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.player.Player
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 *  org.zeith.hammerlib.net.IPacket
 *  org.zeith.hammerlib.net.Network
 */
package org.zeith.improvableskills.custom.abilities;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.Network;
import org.zeith.improvableskills.api.registry.PlayerAbilityBase;
import org.zeith.improvableskills.net.PacketOpenPortableEnch;

public class AbilityEnchanting
extends PlayerAbilityBase {
    public AbilityEnchanting() {
        this.setColor(32255);
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void onClickClient(Player player, int mouseButton) {
        Network.sendToServer((IPacket)new PacketOpenPortableEnch());
    }
}

