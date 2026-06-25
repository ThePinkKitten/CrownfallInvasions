/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.level.Level
 *  org.zeith.hammerlib.util.java.Cast
 */
package org.zeith.improvableskills.utils;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.zeith.hammerlib.util.java.Cast;

public class TileHelper {
    public static <T> List<T> collectTiles(Level world, BlockPos center, int rad, Class<T> type) {
        ArrayList<Object> al = new ArrayList<Object>();
        for (int x = -rad; x <= rad; ++x) {
            for (int y = -rad; y <= rad; ++y) {
                for (int z = -rad; z <= rad; ++z) {
                    Object t = Cast.cast((Object)world.m_7702_(center.m_7918_(x, y, z)), type);
                    if (t == null) continue;
                    al.add(t);
                }
            }
        }
        return al;
    }
}

