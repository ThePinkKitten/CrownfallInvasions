/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.Window
 *  net.minecraft.client.Minecraft
 */
package org.zeith.improvableskills.utils;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;

public class ScaledResolution {
    int scaledWidth;
    int scaledHeight;
    double scaleFactor;

    public ScaledResolution(Minecraft minecraft) {
        Window wnd = minecraft.m_91268_();
        this.scaledWidth = wnd.m_85445_();
        this.scaledHeight = wnd.m_85446_();
        this.scaleFactor = wnd.m_85449_();
    }

    public int getScaledWidth() {
        return this.scaledWidth;
    }

    public int getScaledHeight() {
        return this.scaledHeight;
    }

    public double getScaledWidth_double() {
        return this.scaledWidth;
    }

    public double getScaledHeight_double() {
        return this.scaledHeight;
    }

    public double getScaleFactor() {
        return this.scaleFactor;
    }
}

