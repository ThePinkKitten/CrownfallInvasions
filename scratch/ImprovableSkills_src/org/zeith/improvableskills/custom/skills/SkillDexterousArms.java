/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.storage.loot.BuiltInLootTables
 */
package org.zeith.improvableskills.custom.skills;

import java.util.Random;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import org.zeith.improvableskills.api.evt.DamageItemEvent;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;
import org.zeith.improvableskills.data.PlayerDataManager;

public class SkillDexterousArms
extends PlayerSkillBase {
    private final Random rng = new Random();

    public SkillDexterousArms() {
        super(15);
        this.setupScroll();
        this.getLoot().chance.n = 4;
        this.getLoot().setLootTable(BuiltInLootTables.f_78759_);
        this.setColor(16760881);
        this.xpCalculator.xpValue = 3;
        this.xpCalculator.setBaseFormula("((%lvl%+1)^%xpv%)/2");
        this.addListener(this::hook);
    }

    private void hook(DamageItemEvent e) {
        LivingEntity livingEntity = e.getEntity();
        if (livingEntity instanceof Player) {
            Player player = (Player)livingEntity;
            PlayerDataManager.handleDataSafely(player, data -> {
                if (!data.isSkillActive(this)) {
                    return;
                }
                float chanceToSaveDurability = Mth.m_14179_((float)data.getSkillProgress(this), (float)0.0f, (float)60.0f);
                for (int i = 0; i < e.getNewDamage(); ++i) {
                    if (!((float)(this.rng.nextInt(100) + 1) < chanceToSaveDurability)) continue;
                    e.setNewDamage(e.getNewDamage() - 1);
                }
            });
        }
    }
}

