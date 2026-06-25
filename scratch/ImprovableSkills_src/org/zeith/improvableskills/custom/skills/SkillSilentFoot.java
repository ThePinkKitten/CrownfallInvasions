/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.storage.loot.BuiltInLootTables
 */
package org.zeith.improvableskills.custom.skills;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import org.zeith.improvableskills.api.evt.VibrationEvent;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;
import org.zeith.improvableskills.data.PlayerDataManager;

public class SkillSilentFoot
extends PlayerSkillBase {
    public SkillSilentFoot() {
        super(10);
        this.setupScroll();
        this.getLoot().chance.n = 1;
        this.getLoot().setLootTables(BuiltInLootTables.f_230876_, BuiltInLootTables.f_230877_, EntityType.f_217015_.m_20677_());
        this.setColor(162168);
        this.xpCalculator.xpValue = 4;
        this.xpCalculator.setBaseFormula("((%lvl%+1)^%xpv%)/3");
        this.addListener(this::hook);
    }

    private void hook(VibrationEvent e) {
        Entity entity = e.getInfo().f_244048_();
        if (entity instanceof ServerPlayer) {
            ServerPlayer mp = (ServerPlayer)entity;
            PlayerDataManager.handleDataSafely((Player)mp, data -> {
                if (!data.isSkillActive(this)) {
                    return;
                }
                double distance = e.getInfo().f_243776_();
                float radius = Mth.m_14179_((float)data.getSkillProgress(this), (float)e.getUser().m_280351_(), (float)1.0f);
                if ((double)radius < distance) {
                    e.setCanceled(true);
                }
            });
        }
    }
}

