/*
 * Decompiled with CFR 0.152.
 */
package org.zeith.improvableskills.custom.skills;

import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;

public class SkillAttackSpeed
extends PlayerSkillBase {
    public SkillAttackSpeed() {
        super(25);
        this.xpCalculator.setBaseFormula("%lvl%^1.5");
    }

    @Override
    public void tick(PlayerSkillData data, boolean isActive) {
        if (isActive) {
            data.player.f_20922_ = (int)((double)data.player.f_20922_ + Math.sqrt(data.getSkillLevel(this)) / 2.0);
        }
    }
}

