/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.renderer.GameRenderer
 *  org.zeith.hammerlib.client.utils.FXUtils
 *  org.zeith.hammerlib.client.utils.RenderUtils
 *  org.zeith.hammerlib.util.colors.ColorHelper
 */
package org.zeith.improvableskills.client.rendering.ote;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import org.zeith.hammerlib.client.utils.FXUtils;
import org.zeith.hammerlib.client.utils.RenderUtils;
import org.zeith.hammerlib.util.colors.ColorHelper;
import org.zeith.improvableskills.client.rendering.OTEffect;
import org.zeith.improvableskills.utils.ScaledResolution;
import org.zeith.improvableskills.utils.Trajectory;

public class OTESparkle
extends OTEffect {
    private int color;
    private double tx;
    private double ty;
    private int totTime;
    private int prevTime;
    private int time;
    public double[] xPoints;
    public double[] yPoints;
    Class<? extends Screen> screen;

    public OTESparkle(double x, double y, double tx, double ty, int time, int color) {
        this.renderHud = false;
        this.totTime = time;
        this.x = this.prevX = x;
        this.y = this.prevY = y;
        this.tx = tx;
        this.ty = ty;
        this.color = color;
        double[][] path = Trajectory.makeBroken2DTrajectory(x, y, tx, ty, time, Math.abs((float)this.hashCode() / 25.0f));
        this.xPoints = path[0];
        this.yPoints = path[1];
        this.screen = Minecraft.m_91087_().f_91080_ != null ? Minecraft.m_91087_().f_91080_.getClass() : null;
    }

    @Override
    public void resize(ScaledResolution prev, ScaledResolution nev) {
        super.resize(prev, nev);
        this.tx = OTESparkle.handleResizeXd(this.tx, prev, nev);
        this.ty = OTESparkle.handleResizeYd(this.ty, prev, nev);
        this.xPoints = OTESparkle.handleResizeXdv(this.xPoints, prev, nev);
        this.yPoints = OTESparkle.handleResizeYdv(this.yPoints, prev, nev);
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
        Screen gui = Minecraft.m_91087_().f_91080_;
        if (gui == null && this.screen == null || this.screen != null && this.screen.isInstance(gui)) {
            double cx = this.prevX + (this.x - this.prevX) * (double)partialTime;
            double cy = this.prevY + (this.y - this.prevY) * (double)partialTime;
            float t = (float)this.prevTime + partialTime;
            float r = (float)(System.currentTimeMillis() % 2000L) / 2000.0f;
            r = r > 0.5f ? 1.0f - r : r;
            r += 0.45f;
            RenderSystem.setShader(GameRenderer::m_172817_);
            FXUtils.bindTexture((String)"improvableskills", (String)"textures/particles/sparkle.png");
            int tx = 64 * (int)((float)this.time / (float)this.totTime * 3.0f);
            float scale = 0.125f;
            if (t < 5.0f) {
                scale *= t / 5.0f;
            }
            if (t >= (float)(this.totTime - 5)) {
                scale *= 1.0f - (t - (float)this.totTime + 5.0f) / 5.0f;
            }
            RenderSystem.setShaderColor((float)ColorHelper.getRed((int)this.color), (float)ColorHelper.getGreen((int)this.color), (float)ColorHelper.getBlue((int)this.color), (float)(0.9f * ColorHelper.getAlpha((int)this.color)));
            RenderSystem.enableBlend();
            RenderSystem.blendFunc((int)770, (int)772);
            for (int i = 0; i < 4; ++i) {
                float ps = scale / ((float)i + 1.0f);
                pose.m_85836_();
                pose.m_85837_(cx - (double)(64.0f * ps / 2.0f), cy - (double)(64.0f * ps / 2.0f), 5.0);
                pose.m_85841_(ps, ps, ps);
                RenderUtils.drawTexturedModalRect((PoseStack)pose, (float)0.0f, (float)0.0f, (float)tx, (float)0.0f, (float)64.0f, (float)64.0f);
                pose.m_85849_();
            }
            RenderSystem.defaultBlendFunc();
            this.setWhiteColor();
        }
    }
}

