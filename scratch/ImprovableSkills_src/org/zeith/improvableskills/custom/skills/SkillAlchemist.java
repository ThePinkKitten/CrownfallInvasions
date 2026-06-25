/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.particles.DustParticleOptions
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  org.joml.Vector3f
 */
package org.zeith.improvableskills.custom.skills;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.joml.Vector3f;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;
import org.zeith.improvableskills.mixins.BrewingStandBlockEntityAccessor;

public class SkillAlchemist
extends PlayerSkillBase {
    public static final DustParticleOptions BREWING_STAND_DUST = new DustParticleOptions(new Vector3f(1.0f, 1.0f, 0.0f), 1.0f);

    public SkillAlchemist() {
        super(15);
        this.setupScroll();
        this.setColor(14514238);
        this.getLoot().chance.n = 10;
        this.getLoot().setLootTable(EntityType.f_20495_.m_20677_());
        this.xpCalculator.xpValue = 2;
    }

    @Override
    public void tick(PlayerSkillData data, boolean isActive) {
        short lvl = data.getSkillLevel(this);
        boolean working = isActive && lvl > 0;
        Level level = data.player.m_9236_();
        if (!working || level.f_46443_) {
            return;
        }
        BlockPos center = data.player.m_20183_();
        int rad = 3;
        BlockPos.m_121940_((BlockPos)center.m_7918_(-rad, -rad, -rad), (BlockPos)center.m_7918_(rad, rad, rad)).forEach(pos -> {
            BlockEntity patt1384$temp = level.m_7702_(pos);
            if (patt1384$temp instanceof BrewingStandBlockEntityAccessor) {
                BrewingStandBlockEntityAccessor tef = (BrewingStandBlockEntityAccessor)patt1384$temp;
                int progress = tef.getBrewTime();
                if (progress > 0) {
                    tef.setBrewTime(Math.max(progress - 2 * (int)Math.sqrt(lvl * 2), 1));
                }
                if (level.f_46441_.m_188503_(9) == 0 && level instanceof ServerLevel) {
                    ServerLevel sl = (ServerLevel)level;
                    sl.m_8767_((ParticleOptions)BREWING_STAND_DUST, (double)pos.m_123341_() + 0.5, (double)pos.m_123342_() + 0.85, (double)pos.m_123343_() + 0.5, 1, 0.0, 0.0, 0.0, 0.0);
                }
            }
        });
    }
}

