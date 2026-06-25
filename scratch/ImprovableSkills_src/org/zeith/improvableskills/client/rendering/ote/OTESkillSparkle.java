/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.gui.GuiGraphics
 *  org.zeith.hammerlib.client.utils.FXUtils
 *  org.zeith.hammerlib.client.utils.RenderUtils
 *  org.zeith.hammerlib.util.colors.ColorHelper
 */
package org.zeith.improvableskills.client.rendering.ote;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import org.zeith.hammerlib.client.utils.FXUtils;
import org.zeith.hammerlib.client.utils.RenderUtils;
import org.zeith.hammerlib.util.colors.ColorHelper;
import org.zeith.improvableskills.client.rendering.OTEffect;
import org.zeith.improvableskills.utils.ScaledResolution;
import org.zeith.improvableskills.utils.Trajectory;

public class OTESkillSparkle
extends OTEffect {
    private int color;
    private double tx;
    private double ty;
    private int totTime;
    private int prevTime;
    private int time;
    public double[] xPoints;
    public double[] yPoints;

    public OTESkillSparkle(double x, double y, double tx, double ty, int time, int color) {
        this.totTime = time;
        this.x = this.prevX = x;
        this.y = this.prevY = y;
        this.tx = tx;
        this.ty = ty;
        this.color = color;
        double[][] path = Trajectory.makeBroken2DTrajectory(x, y, tx, ty, time, Math.abs((float)this.hashCode() / 25.0f));
        this.xPoints = path[0];
        this.yPoints = path[1];
    }

    @Override
    public void resize(ScaledResolution prev, ScaledResolution nev) {
        super.resize(prev, nev);
        this.tx = OTESkillSparkle.handleResizeXd(this.tx, prev, nev);
        this.ty = OTESkillSparkle.handleResizeYd(this.ty, prev, nev);
        this.xPoints = OTESkillSparkle.handleResizeXdv(this.xPoints, prev, nev);
        this.yPoints = OTESkillSparkle.handleResizeYdv(this.yPoints, prev, nev);
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
        float r = (float)(System.currentTimeMillis() % 2000L) / 2000.0f;
        r = r > 0.5f ? 1.0f - r : r;
        r += 0.45f;
        FXUtils.bindTexture((String)"improvableskills", (String)"textures/particles/sparkle.png");
        int tx = 64 * (int)((float)this.time / (float)this.totTime * 3.0f);
        float scale = 0.125f;
        if (t < 5.0f) {
            scale *= t / 5.0f;
        }
        if (t >= (float)(this.totTime - 5)) {
            scale *= 1.0f - (t - (float)this.totTime + 5.0f) / 5.0f;
        }
        RenderSystem.setShaderColor((float)ColorHelper.getRed((int)this.color), (float)ColorHelper.getGreen((int)this.color), (float)ColorHelper.getBlue((int)this.color), (float)1.0f);
        for (int i = 0; i < 3; ++i) {
            float ps = i == 0 ? scale : (i == 2 ? (float)((Math.sin((float)(this.hashCode() % 90) + t / 2.0f) + 1.0) / 2.5 * (double)scale) : scale / 2.0f);
            RenderSystem.blendFunc((int)770, (int)(i == 0 ? 771 : 772));
            pose.m_85836_();
            pose.m_85837_(cx - (double)(64.0f * ps / 2.0f), cy - (double)(64.0f * ps / 2.0f), 0.0);
            pose.m_85841_(ps, ps, ps);
            RenderUtils.drawTexturedModalRect((PoseStack)pose, (float)0.0f, (float)0.0f, (float)tx, (float)0.0f, (float)64.0f, (float)64.0f);
            pose.m_85849_();
        }
        RenderSystem.blendFunc((int)770, (int)771);
        this.setWhiteColor();
    }
}

