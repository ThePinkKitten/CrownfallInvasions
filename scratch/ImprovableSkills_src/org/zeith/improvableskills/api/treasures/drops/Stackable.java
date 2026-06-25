/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.item.ItemStack
 */
package org.zeith.improvableskills.api.treasures.drops;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface Stackable {
    public ItemStack transform(RandomSource var1);

    public static Stackable of(ItemStack instance) {
        return r -> instance.m_41777_();
    }

    public static Stackable of(ItemStack instance, int min, int max) {
        int mult = instance.m_41613_();
        return r -> {
            ItemStack ns = instance.m_41777_();
            ns.m_41764_(mult * min + r.m_188503_(max - min + 1));
            return ns;
        };
    }
}

