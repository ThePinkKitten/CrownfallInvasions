/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.ItemStack
 */
package org.zeith.improvableskills.api.treasures;

import java.util.List;
import net.minecraft.world.item.ItemStack;
import org.zeith.improvableskills.api.treasures.DropCondition;
import org.zeith.improvableskills.api.treasures.TreasureContext;

public abstract class TreasureDropBase
implements DropCondition {
    private float chance = 1.0f;

    public float getChance() {
        return this.chance;
    }

    public void setChance(float newChance) {
        this.chance = newChance;
    }

    public void drop(TreasureContext ctx, List<ItemStack> drops) {
    }

    public TreasureDropBase copy() {
        try {
            TreasureDropBase l = (TreasureDropBase)this.getClass().newInstance();
            l.chance = this.chance;
            return l;
        }
        catch (Throwable throwable) {
            return null;
        }
    }
}

