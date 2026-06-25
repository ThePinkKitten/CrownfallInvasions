/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.world.item.ItemStack
 *  org.zeith.hammerlib.client.utils.RenderUtils
 */
package org.zeith.improvableskills.client.rendering.ote;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import org.zeith.hammerlib.client.utils.RenderUtils;
import org.zeith.improvableskills.client.rendering.OTEffect;
import org.zeith.improvableskills.utils.ScaledResolution;
import org.zeith.improvableskills.utils.Trajectory;

public class OTEItemStack
extends OTEffect {
    public ItemStack item;
    private double tx;
    private double ty;
    private int totTime;
    private int prevTime;
    private int time;
    public double[] xPoints;
    public double[] yPoints;

    public OTEItemStack(double x, double y, double tx, double ty, int time, ItemStack item) {
        this.renderGui = false;
        this.totTime = time;
        this.x = this.prevX = x;
        this.y = this.prevY = y;
        this.tx = tx;
        this.ty = ty;
        this.item = item;
        double[][] path = Trajectory.makeBroken2DTrajectory(x, y, tx, ty, time, System.currentTimeMillis() % 1000000L);
        this.xPoints = path[0];
        this.yPoints = path[1];
    }

    @Override
    public void resize(ScaledResolution prev, ScaledResolution nev) {
        super.resize(prev, nev);
        this.tx = OTEItemStack.handleResizeXd(this.tx, prev, nev);
        this.ty = OTEItemStack.handleResizeYd(this.ty, prev, nev);
        this.xPoints = OTEItemStack.handleResizeXdv(this.xPoints, prev, nev);
        this.yPoints = OTEItemStack.handleResizeYdv(this.yPoints, prev, nev);
    }

    @Override
    public void update() {
        super.update();
        this.prevTime = this.time;
        int tt = this.xPoints.length;
        int cframe = Math.round((float)this.time / (float)this.totTime * (float)tt);
        this.x = this.xPoints[cframe];
        this.y = this.yPoints[cframe];
        ++this.time;
        if (this.time >= this.totTime) {
            this.setExpired();
        }
    }

    @Override
    public void render(GuiGraphics gfx, float partialTime) {
        PoseStack pose = gfx.m_280168_();
        double cx = this.prevX + (this.x - this.prevX) * (double)partialTime;
        double cy = this.prevY + (this.y - this.prevY) * (double)partialTime;
        float t = (float)this.prevTime + partialTime;
        int tx = 64 * (int)((float)this.time / (float)this.totTime * 3.0f);
        float scale = 1.0f;
        if (t < 5.0f) {
            scale *= t / 5.0f;
        }
        if (t >= (float)(this.totTime - 5)) {
            scale *= 1.0f - (t - (float)this.totTime + 5.0f) / 5.0f;
        }
        this.setWhiteColor();
        pose.m_85836_();
        pose.m_85837_(cx - (double)(16.0f * scale / 2.0f), cy - (double)(16.0f * scale / 2.0f), 0.0);
        pose.m_85841_(scale, scale, scale);
        RenderUtils.renderItemIntoGui((PoseStack)pose, (ItemStack)this.item, (int)0, (int)0);
        pose.m_85849_();
        this.setWhiteColor();
    }
}

