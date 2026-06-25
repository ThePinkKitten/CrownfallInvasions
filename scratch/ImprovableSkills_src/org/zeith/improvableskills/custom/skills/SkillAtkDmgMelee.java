/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.player.Player
 *  net.minecraftforge.event.entity.living.LivingHurtEvent
 */
package org.zeith.improvableskills.custom.skills;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.zeith.improvableskills.api.DamageSourceProcessor;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;
import org.zeith.improvableskills.data.PlayerDataManager;

public class SkillAtkDmgMelee
extends PlayerSkillBase {
    public SkillAtkDmgMelee() {
        super(15);
        this.xpCalculator.xpValue = 3;
        this.addListener(this::damageHook);
    }

    private void damageHook(LivingHurtEvent e) {
        DamageSource ds = e.getSource();
        if (ds != null && DamageSourceProcessor.getDamageType(ds) == DamageSourceProcessor.DamageType.MELEE) {
            Player p = DamageSourceProcessor.getMeleeAttacker(ds);
            PlayerDataManager.handleDataSafely(p, data -> {
                if (!data.isSkillActive(this)) {
                    return;
                }
                float pp = data.getSkillProgress(this);
                e.setAmount(e.getAmount() + e.getAmount() * pp / 2.0f + pp * 7.0f);
            });
        }
    }
}

