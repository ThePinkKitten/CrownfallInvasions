/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.gui.GuiGraphics
 */
package org.zeith.improvableskills.client.rendering.ote;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import org.zeith.improvableskills.api.client.IClientAbilityExtensions;
import org.zeith.improvableskills.api.registry.PlayerAbilityBase;
import org.zeith.improvableskills.client.rendering.OTEffect;
import org.zeith.improvableskills.utils.ScaledResolution;
import org.zeith.improvableskills.utils.Trajectory;

public class OTEAbility
extends OTEffect {
    public PlayerAbilityBase item;
    private double tx;
    private double ty;
    private int totTime;
    private int prevTime;
    private int time;
    public double[] xPoints;
    public double[] yPoints;

    public OTEAbility(double x, double y, double tx, double ty, int time, PlayerAbilityBase item) {
        this.renderGui = false;
        this.totTime = time + 5;
        this.x = this.prevX = x;
        this.y = this.prevY = y;
        this.tx = tx;
        this.ty = ty;
        this.item = item;
        double[][] path = Trajectory.makeBroken2DTrajectory(x, y, tx, ty, time, (float)(System.currentTimeMillis() % 1000000L) / 100.0f, 5.0f);
        this.xPoints = path[0];
        this.yPoints = path[1];
        x = this.xPoints[0];
        y = this.yPoints[0];
    }

    @Override
    public void resize(ScaledResolution prev, ScaledResolution nev) {
        super.resize(prev, nev);
        this.tx = OTEAbility.handleResizeXd(this.tx, prev, nev);
        this.ty = OTEAbility.handleResizeYd(this.ty, prev, nev);
        this.xPoints = OTEAbility.handleResizeXdv(this.xPoints, prev, nev);
        this.yPoints = OTEAbility.handleResizeYdv(this.yPoints, prev, nev);
    }

    @Override
    public void update() {
        super.update();
        this.prevTime = this.time;
        int tt = this.xPoints.length;
        if (this.time > 5) {
            int cframe = Math.round((float)(this.time - 5) / (float)(this.totTime - 5) * (float)tt);
            this.x = this.xPoints[cframe];
            this.y = this.yPoints[cframe];
        }
        ++this.time;
        if (this.time >= this.totTime) {
            this.setExpired();
        }
    }

    @Override
    public void render(GuiGraphics gfx, float partialTime) {
        float cx = (float)(this.prevX + (this.x - this.prevX) * (double)partialTime);
        float cy = (float)(this.prevY + (this.y - this.prevY) * (double)partialTime);
        float t = (float)this.prevTime + partialTime;
        float scale = 1.0f;
        if (t < 5.0f) {
            scale *= t / 5.0f;
        }
        if (t >= (float)(this.totTime - 5)) {
            scale *= 1.0f - (t - (float)this.totTime + 5.0f) / 5.0f;
        }
        PoseStack pose = gfx.m_280168_();
        pose.m_85836_();
        this.setWhiteColor();
        if (!IClientAbilityExtensions.of(this.item).slotRenderer().drawSlot(gfx, cx - (scale *= 16.0f) / 2.0f, cy - scale / 2.0f, scale, scale, 1.0f, partialTime)) {
            this.item.tex.toUV(true).render(pose, (double)(cx - scale / 2.0f), (double)(cy - scale / 2.0f), scale, scale);
        }
        pose.m_85849_();
    }
}

