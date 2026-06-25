/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraftforge.event.entity.living.LivingEvent$LivingJumpEvent
 */
package org.zeith.improvableskills.custom.skills;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;
import org.zeith.improvableskills.data.PlayerDataManager;

public class SkillLeaper
extends PlayerSkillBase {
    public SkillLeaper() {
        super(15);
        this.xpCalculator.xpValue = 2;
        this.addListener(this::hook);
    }

    private void hook(LivingEvent.LivingJumpEvent e) {
        LivingEntity livingEntity = e.getEntity();
        if (livingEntity instanceof Player) {
            Player p = (Player)livingEntity;
            PlayerDataManager.handleDataSafely(p, data -> {
                if (!data.isSkillActive(this)) {
                    return;
                }
                float leaper = data.getSkillProgress(this);
                if (leaper > 0.0f) {
                    p.m_20256_(p.m_20184_().m_82542_(1.0, (double)(1.0f + leaper * 0.75f), 1.0));
                }
            });
        }
    }
}

