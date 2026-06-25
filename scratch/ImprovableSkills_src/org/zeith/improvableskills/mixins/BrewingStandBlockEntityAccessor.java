/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.level.block.entity.BrewingStandBlockEntity
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package org.zeith.improvableskills.mixins;

import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={BrewingStandBlockEntity.class})
public interface BrewingStandBlockEntityAccessor {
    @Accessor
    public int getBrewTime();

    @Accessor
    public void setBrewTime(int var1);
}

