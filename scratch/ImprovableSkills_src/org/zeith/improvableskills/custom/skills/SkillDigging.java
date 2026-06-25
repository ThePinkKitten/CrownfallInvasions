/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.tags.BlockTags
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.block.state.BlockState
 */
package org.zeith.improvableskills.custom.skills;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.zeith.improvableskills.api.IDigSpeedAffectorSkill;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;

public class SkillDigging
extends PlayerSkillBase
implements IDigSpeedAffectorSkill {
    public SkillDigging() {
        super(25);
        this.xpCalculator.setBaseFormula("%lvl%^1.5");
    }

    @Override
    public float getDigMultiplier(ItemStack stack, BlockPos pos, PlayerSkillData data) {
        if (pos == null) {
            return 0.0f;
        }
        BlockState b = data.player.m_9236_().m_8055_(pos);
        if (b.canHarvestBlock((BlockGetter)data.player.m_9236_(), pos, data.player) && b.m_204336_(BlockTags.f_144283_) && data.player.m_21205_().m_41691_(b) > 0.0f) {
            return (float)data.getSkillLevel(this) / 24.0f;
        }
        return 0.0f;
    }
}

