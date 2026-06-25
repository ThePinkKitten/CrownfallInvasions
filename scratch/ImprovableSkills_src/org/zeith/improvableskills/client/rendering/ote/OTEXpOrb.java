/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.gui.GuiGraphics
 *  org.zeith.hammerlib.client.utils.FXUtils
 *  org.zeith.hammerlib.client.utils.RenderUtils
 */
package org.zeith.improvableskills.client.rendering.ote;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import org.zeith.hammerlib.client.utils.FXUtils;
import org.zeith.hammerlib.client.utils.RenderUtils;
import org.zeith.improvableskills.client.gui.GuiXPBank;
import org.zeith.improvableskills.client.rendering.OTEffect;
import org.zeith.improvableskills.utils.ScaledResolution;
import org.zeith.improvableskills.utils.Trajectory;

public class OTEXpOrb
extends OTEffect {
    private double tx;
    private double ty;
    private int totTime;
    private int prevTime;
    private int time;
    public double[] xPoints;
    public double[] yPoints;

    public OTEXpOrb(double x, double y, double tx, double ty, int time) {
        this.renderHud = false;
        this.totTime = time;
        this.x = this.prevX = x;
        this.y = this.prevY = y;
        this.tx = tx;
        this.ty = ty;
        double[][] path = Trajectory.makeBroken2DTrajectory(x, y, tx, ty, time, Math.abs((float)this.hashCode() / 25.0f));
        this.xPoints = path[0];
        this.yPoints = path[1];
    }

    @Override
    public void resize(ScaledResolution prev, ScaledResolution nev) {
        super.resize(prev, nev);
        this.tx = OTEXpOrb.handleResizeXd(this.tx, prev, nev);
        this.ty = OTEXpOrb.handleResizeYd(this.ty, prev, nev);
        this.xPoints = OTEXpOrb.handleResizeXdv(this.xPoints, prev, nev);
        this.yPoints = OTEXpOrb.handleResizeYdv(this.yPoints, prev, nev);
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
        if (!(this.currentGui instanceof GuiXPBank)) {
            return;
        }
        double cx = this.prevX + (this.x - this.prevX) * (double)partialTime;
        double cy = this.prevY + (this.y - this.prevY) * (double)partialTime;
        float t = (float)this.prevTime + partialTime;
        float r = (float)((System.currentTimeMillis() + (long)Math.abs(this.hashCode())) % 2000L) / 2000.0f;
        r = r > 0.5f ? 1.0f - r : r;
        r += 0.45f;
        FXUtils.bindTexture((String)"minecraft", (String)"textures/entity/experience_orb.png");
        int tx = 64 * (this.hashCode() % 3);
        float scale = 0.125f;
        if (t < 5.0f) {
            scale *= t / 5.0f;
        }
        if (t >= (float)(this.totTime - 5)) {
            scale *= 1.0f - (t - (float)this.totTime + 5.0f) / 5.0f;
        }
        RenderSystem.setShaderColor((float)r, (float)1.0f, (float)0.0f, (float)1.0f);
        pose.m_85836_();
        pose.m_85837_(cx - (double)(64.0f * scale / 2.0f), cy - (double)(64.0f * scale / 2.0f), 0.0);
        pose.m_85841_(scale, scale, scale);
        RenderUtils.drawTexturedModalRect((PoseStack)pose, (float)0.0f, (float)0.0f, (float)tx, (float)0.0f, (float)64.0f, (float)64.0f);
        pose.m_85849_();
        this.setWhiteColor();
    }
}

