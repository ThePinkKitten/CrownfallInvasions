/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.RandomSource
 */
package org.zeith.improvableskills.api.treasures;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.RandomSource;
import org.zeith.improvableskills.api.treasures.TreasureContext;
import org.zeith.improvableskills.api.treasures.TreasureDropBase;
import org.zeith.improvableskills.api.treasures.TreasureRegistry;

public class DropUtil {
    public static RandomSource RANDOM = RandomSource.m_216327_();

    public static TreasureDropBase chooseDrop(TreasureContext ctx) {
        RANDOM = ctx.rand();
        return DropUtil.chooseDrop(TreasureRegistry.allDrops(), ctx);
    }

    public static TreasureDropBase chooseDrop(List<TreasureDropBase> allDrops, TreasureContext ctx) {
        ArrayList<TreasureDropBase> preDrops = new ArrayList<TreasureDropBase>();
        for (TreasureDropBase d : allDrops) {
            if (!d.canDrop(ctx)) continue;
            preDrops.add(d.copy());
        }
        float weightTotal = 0.0f;
        ArrayList<Float> weightPoints = new ArrayList<Float>();
        weightPoints.add(Float.valueOf(0.0f));
        for (TreasureDropBase drop : preDrops) {
            weightPoints.add(Float.valueOf(weightTotal += drop.getChance() * 100.0f));
        }
        float randomIndex = RANDOM.m_188501_() * weightTotal;
        return DropUtil.getDropByWeight(preDrops, weightPoints, randomIndex);
    }

    private static TreasureDropBase getDropByWeight(List<TreasureDropBase> drops, ArrayList<Float> weightPoints, float randomIndex) {
        for (int a = 0; a < drops.size(); ++a) {
            if (!(randomIndex >= weightPoints.get(a).floatValue()) || !(randomIndex < weightPoints.get(a + 1).floatValue())) continue;
            return drops.get(a);
        }
        return null;
    }
}

