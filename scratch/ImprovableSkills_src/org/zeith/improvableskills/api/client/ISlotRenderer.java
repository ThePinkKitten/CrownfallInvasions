/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiGraphics
 */
package org.zeith.improvableskills.api.client;

import net.minecraft.client.gui.GuiGraphics;

@FunctionalInterface
public interface ISlotRenderer {
    public static final ISlotRenderer NONE = (gfx, x, y, width, height, hoverProgress, partialTicks) -> false;

    public boolean drawSlot(GuiGraphics var1, float var2, float var3, float var4, float var5, float var6, float var7);
}

