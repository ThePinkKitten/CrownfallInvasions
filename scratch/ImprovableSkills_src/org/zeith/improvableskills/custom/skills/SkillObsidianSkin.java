/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.damagesource.DamageEffects
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.storage.loot.BuiltInLootTables
 *  net.minecraftforge.event.entity.living.LivingHurtEvent
 */
package org.zeith.improvableskills.custom.skills;

import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;
import org.zeith.improvableskills.data.PlayerDataManager;

public class SkillObsidianSkin
extends PlayerSkillBase {
    public SkillObsidianSkin() {
        super(20);
        this.setupScroll();
        this.getLoot().chance.n = 3;
        this.getLoot().setLootTable(BuiltInLootTables.f_78760_);
        this.setColor(10174153);
        this.xpCalculator.xpValue = 2;
        this.addListener(this::damageHook);
    }

    private void damageHook(LivingHurtEvent e) {
        LivingEntity livingEntity;
        DamageSource ds = e.getSource();
        if (ds != null && ds.m_269415_().f_268686_() == DamageEffects.BURNING && (livingEntity = e.getEntity()) instanceof Player) {
            Player p = (Player)livingEntity;
            PlayerDataManager.handleDataSafely(p, data -> {
                if (!data.isSkillActive(this)) {
                    return;
                }
                e.setAmount(e.getAmount() * (1.0f - data.getSkillProgress(this) * 0.5f));
            });
        }
    }
}

