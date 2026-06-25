/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.item.ItemStack
 */
package org.zeith.improvableskills.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import org.zeith.improvableskills.api.PlayerSkillData;

public interface IDigSpeedAffectorSkill {
    public float getDigMultiplier(ItemStack var1, BlockPos var2, PlayerSkillData var3);
}

