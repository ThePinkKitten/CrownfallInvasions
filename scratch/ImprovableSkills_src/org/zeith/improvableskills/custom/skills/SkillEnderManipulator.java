/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.player.Player
 *  net.minecraftforge.event.entity.EntityTeleportEvent$EnderPearl
 */
package org.zeith.improvableskills.custom.skills;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;
import org.zeith.improvableskills.data.PlayerDataManager;

public class SkillEnderManipulator
extends PlayerSkillBase {
    public SkillEnderManipulator() {
        super(5);
        this.setupScroll();
        this.getLoot().chance.n = 20;
        this.getLoot().setLootTable(EntityType.f_20566_.m_20677_());
        this.setColor(14015124);
        this.xpCalculator.xpValue = 3;
        this.addListener(this::hook);
    }

    private void hook(EntityTeleportEvent.EnderPearl e) {
        int lvl;
        ServerPlayer p = e.getPlayer();
        if (p != null && (lvl = ((Number)PlayerDataManager.handleDataSafely((Player)p, data -> data.isSkillActive(this) ? data.getSkillLevel(this) : (short)0, 0)).intValue()) > 0) {
            float prog = (float)lvl / (float)(this.getMaxLevel() - 1);
            if (prog > 1.0f) {
                e.setAttackDamage(e.getAttackDamage() / 10.0f);
                p.m_5634_(1.0f);
            } else {
                e.setAttackDamage(e.getAttackDamage() * (1.0f - prog * 0.8f));
            }
        }
    }
}

