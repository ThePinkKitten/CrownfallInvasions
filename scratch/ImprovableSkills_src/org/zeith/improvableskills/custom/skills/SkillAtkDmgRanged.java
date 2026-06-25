/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.player.Player
 *  net.minecraftforge.event.entity.living.LivingHurtEvent
 */
package org.zeith.improvableskills.custom.skills;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.zeith.improvableskills.api.DamageSourceProcessor;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;
import org.zeith.improvableskills.data.PlayerDataManager;

public class SkillAtkDmgRanged
extends PlayerSkillBase {
    public SkillAtkDmgRanged() {
        super(15);
        this.setupScroll();
        this.getLoot().chance.n = 40;
        this.getLoot().setLootTable(EntityType.f_20524_.m_20677_());
        this.xpCalculator.xpValue = 3;
        this.addListener(this::damageHook);
    }

    private void damageHook(LivingHurtEvent e) {
        DamageSource ds = e.getSource();
        if (ds != null && DamageSourceProcessor.getDamageType(ds) == DamageSourceProcessor.DamageType.RANGED) {
            Player p = DamageSourceProcessor.getRangedOwner(ds);
            PlayerDataManager.handleDataSafely(p, data -> {
                short melee = data.getSkillLevel(this);
                float pp = data.getSkillProgress(this);
                e.setAmount(e.getAmount() + e.getAmount() * pp + (float)melee / 2.0f);
            });
        }
    }
}

