/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Vec3i
 *  net.minecraft.core.particles.DustParticleOptions
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.Container
 *  net.minecraft.world.item.crafting.Recipe
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.AbstractFurnaceBlock
 *  net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.phys.Vec3
 *  org.joml.Vector3f
 */
package org.zeith.improvableskills.custom.skills;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;
import org.zeith.improvableskills.mixins.AbstractFurnaceBlockEntityAccessor;

public class SkillAcceleratedFurnace
extends PlayerSkillBase {
    public static final DustParticleOptions FURNACE_DUST = new DustParticleOptions(new Vector3f(1.0f, 1.0f, 0.0f), 1.0f);

    public SkillAcceleratedFurnace() {
        super(15);
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
            AbstractFurnaceBlockEntity tef;
            BlockEntity patt1538$temp = level.m_7702_(pos);
            if (patt1538$temp instanceof AbstractFurnaceBlockEntity && (tef = (AbstractFurnaceBlockEntity)patt1538$temp) instanceof AbstractFurnaceBlockEntityAccessor) {
                BlockState state;
                AbstractFurnaceBlockEntityAccessor a = (AbstractFurnaceBlockEntityAccessor)tef;
                int burnTime = a.getLitTime();
                int progress = a.getCookingProgress();
                if (burnTime > 0 && level.f_46441_.m_188503_(this.maxLvl - lvl + 1) == 0) {
                    int add = 2 * (int)Math.round(Math.sqrt(lvl));
                    a.setCookingProgress(progress + add);
                    a.setLitTime((int)Math.max(0.0f, (float)burnTime - (float)add * 0.8f));
                    if (a.getCookingProgress() >= a.getCookingTotalTime()) {
                        Recipe recipe = level.m_7465_().m_44015_(a.getRecipeType(), (Container)tef, level).orElse(null);
                        if (a.callBurn(level.m_9598_(), recipe, a.getItems(), tef.m_6893_())) {
                            tef.m_6029_(recipe);
                        }
                        a.setCookingProgress(0);
                    }
                } else if (a.getCookingProgress() < 1 && ((Boolean)(state = level.m_8055_(pos)).m_61143_((Property)AbstractFurnaceBlock.f_48684_)).booleanValue()) {
                    state = (BlockState)state.m_61124_((Property)AbstractFurnaceBlock.f_48684_, (Comparable)Boolean.valueOf(false));
                    level.m_7731_(pos, state, 3);
                    level.m_151523_((BlockEntity)tef);
                }
                if (level.f_46441_.m_188503_(9) == 0 && level instanceof ServerLevel) {
                    ServerLevel sl = (ServerLevel)level;
                    Direction face = (Direction)tef.m_58900_().m_61143_((Property)AbstractFurnaceBlock.f_48683_);
                    Vec3 vec = Vec3.m_82528_((Vec3i)pos.m_121945_(face));
                    face = face.m_122424_();
                    vec = vec.m_82520_(0.5 + (double)face.m_122429_() * 0.5, 0.65 + (double)face.m_122430_() * 0.5, 0.5 + (double)face.m_122431_() * 0.5);
                    sl.m_8767_((ParticleOptions)FURNACE_DUST, vec.f_82479_, vec.f_82480_, vec.f_82481_, 1, 0.0, 0.0, 0.0, 0.0);
                }
            }
        });
    }
}

