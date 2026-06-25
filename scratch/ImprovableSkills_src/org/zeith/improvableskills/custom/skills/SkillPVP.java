/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraftforge.event.entity.living.LivingHurtEvent
 */
package org.zeith.improvableskills.custom.skills;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;
import org.zeith.improvableskills.data.PlayerDataManager;

public class SkillPVP
extends PlayerSkillBase {
    public SkillPVP() {
        super(20);
        this.xpCalculator.xpValue = 2;
        this.addListener(this::damageHook);
    }

    private void damageHook(LivingHurtEvent e) {
        Entity entity;
        DamageSource ds = e.getSource();
        if (ds != null && (entity = e.getSource().m_7639_()) instanceof Player) {
            Player attacker = (Player)entity;
            entity = e.getEntity();
            if (entity instanceof Player) {
                Player p = (Player)entity;
                PlayerDataManager.handleDataSafely(p, data -> {
                    if (!data.isSkillActive(this)) {
                        return;
                    }
                    float pp = 1.0f - data.getSkillProgress(this);
                    e.setAmount(e.getAmount() * Math.min(1.0f, 0.75f + pp / 4.0f));
                });
            }
        }
    }
}

