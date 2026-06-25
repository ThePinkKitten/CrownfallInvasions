/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Registry
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.damagesource.DamageTypes
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraftforge.event.entity.living.LivingFallEvent
 *  net.minecraftforge.event.entity.living.LivingHurtEvent
 */
package org.zeith.improvableskills.custom.skills;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;
import org.zeith.improvableskills.data.PlayerDataManager;

public class SkillSoftLanding
extends PlayerSkillBase {
    public SkillSoftLanding() {
        super(10);
        this.xpCalculator.xpValue = 2;
        this.addListener(this::hook);
        this.addListener(this::damageHook);
    }

    private void damageHook(LivingHurtEvent e) {
        LivingEntity livingEntity;
        DamageSource ds = e.getSource();
        if (ds != null && (livingEntity = e.getEntity()) instanceof Player) {
            Player p = (Player)livingEntity;
            Registry dmgReg = e.getEntity().m_9236_().m_9598_().m_6632_(Registries.f_268580_).orElse(null);
            if (dmgReg == null) {
                return;
            }
            if (dmgReg.m_7981_((Object)ds.m_269415_()).equals((Object)DamageTypes.f_268671_.m_135782_())) {
                PlayerDataManager.handleDataSafely(p, data -> {
                    if (data.isSkillActive(this) && data.getSkillLevel(this) >= this.getMaxLevel() && e.getAmount() >= p.m_21223_()) {
                        e.setAmount(p.m_21223_() - 1.0f);
                    }
                });
            }
        }
    }

    private void hook(LivingFallEvent e) {
        LivingEntity livingEntity = e.getEntity();
        if (livingEntity instanceof Player) {
            Player p = (Player)livingEntity;
            PlayerDataManager.handleDataSafely(p, data -> {
                if (data == null || !data.isSkillActive(this)) {
                    return;
                }
                float softLandingStatLevel = data.getSkillProgress(this);
                float reduce = Math.min(0.5f, Math.max(0.25f, softLandingStatLevel));
                reduce = 1.0f - reduce;
                if (softLandingStatLevel > 0.0f) {
                    e.setDistance(e.getDistance() * reduce);
                    p.f_19789_ *= reduce;
                    e.setDamageMultiplier(e.getDamageMultiplier() * reduce);
                }
            });
        }
    }
}

