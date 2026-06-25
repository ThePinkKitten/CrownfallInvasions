/*
 * Decompiled with CFR 0.152.
 */
package org.zeith.improvableskills.api.treasures;

import org.zeith.improvableskills.api.treasures.TreasureContext;

@FunctionalInterface
public interface DropCondition {
    public boolean canDrop(TreasureContext var1);
}

