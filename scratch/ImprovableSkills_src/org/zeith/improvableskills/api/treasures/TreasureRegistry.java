/*
 * Decompiled with CFR 0.152.
 */
package org.zeith.improvableskills.api.treasures;

import java.util.ArrayList;
import org.zeith.improvableskills.api.treasures.TreasureDropBase;

public class TreasureRegistry {
    private static final ArrayList<TreasureDropBase> allDrops = new ArrayList();

    public static <T extends TreasureDropBase> T registerDrop(T drop) {
        if (drop == null) {
            return null;
        }
        allDrops.add(drop);
        return drop;
    }

    public static ArrayList<TreasureDropBase> allDrops() {
        return allDrops;
    }

    public static TreasureDropBase[] allDropsArray() {
        return allDrops.toArray(new TreasureDropBase[allDrops.size()]);
    }
}

