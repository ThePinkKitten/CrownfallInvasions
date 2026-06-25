/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.MoverType
 *  net.minecraft.world.phys.Vec3
 */
package org.zeith.improvableskills.custom.skills;

import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;

public class SkillLadderKing
extends PlayerSkillBase {
    public SkillLadderKing() {
        super(15);
        this.xpCalculator.xpValue = 2;
    }

    @Override
    public void tick(PlayerSkillData data, boolean isActive) {
        if (isActive && data.player.m_6147_() && !data.player.m_6144_()) {
            float multiplier = (float)data.getSkillLevel(this) / (float)this.maxLvl;
            if (!data.player.f_19862_) {
                multiplier *= 0.5f;
            }
            data.player.m_6478_(MoverType.SELF, new Vec3(0.0, data.player.m_20184_().f_82480_ * (double)multiplier, 0.0));
        }
    }
}

