/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.RandomSource
 */
package org.zeith.improvableskills.api.loot;

import net.minecraft.util.RandomSource;

public class RandomBoolean {
    public RandomSource rand;
    public int n;

    public RandomBoolean(RandomSource rand) {
        this.rand = rand;
    }

    public RandomBoolean() {
        this(RandomSource.m_216327_());
    }

    public boolean get() {
        return this.rand.m_188503_(this.n) == 0;
    }

    public boolean get(RandomSource rand) {
        return rand.m_188503_(this.n) == 0;
    }
}

