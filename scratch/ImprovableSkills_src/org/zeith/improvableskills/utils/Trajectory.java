/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Mth
 */
package org.zeith.improvableskills.utils;

import net.minecraft.util.Mth;

public class Trajectory {
    public static double[][] makeBroken2DTrajectory(double x, double y, double tx, double ty, int coords, float timeOffset) {
        return Trajectory.makeBroken2DTrajectory(x, y, tx, ty, coords, timeOffset, 5.0f);
    }

    public static double[][] makeBroken2DTrajectory(double x, double y, double tx, double ty, int coords, float timeOffset, float offset) {
        boolean hBoost;
        double hDel = x - tx;
        double vDel = y - ty;
        float dx = (float)(hDel / (double)coords);
        float dy = (float)(vDel / (double)coords);
        boolean bl = hBoost = Math.abs(hDel) > Math.abs(vDel);
        if (hBoost) {
            dx *= 2.0f;
        } else {
            dy *= 2.0f;
        }
        double[] xPoints = new double[coords + 1];
        double[] yPoints = new double[coords + 1];
        for (int a = 0; a <= coords; ++a) {
            float phase = (float)a / (float)coords;
            float mx = Mth.m_14031_((float)((timeOffset + (float)a) / 7.0f)) * offset * (1.0f - phase);
            float my = Mth.m_14031_((float)((timeOffset + (float)a) / 5.0f)) * offset * (1.0f - phase);
            xPoints[a] = x - (double)(dx * (float)a) + (double)mx;
            yPoints[a] = y - (double)(dy * (float)a) + (double)my;
            if (hBoost) {
                dx *= 1.0f - 1.0f / ((float)coords * 3.0f / 2.0f);
                continue;
            }
            dy *= 1.0f - 1.0f / ((float)coords * 3.0f / 2.0f);
        }
        return new double[][]{xPoints, yPoints};
    }
}

