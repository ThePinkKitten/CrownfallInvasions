/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 */
package org.zeith.improvableskills.client.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.improvableskills.utils.ScaledResolution;

@OnlyIn(value=Dist.CLIENT)
public class OTEffect {
    protected Screen currentGui;
    protected int mouseX;
    protected int mouseY;
    public boolean renderGui;
    public boolean renderHud;
    public double x;
    public double y;
    public double prevX;
    public double prevY;
    public double width;
    public double height;
    public boolean expired;

    public OTEffect() {
        this.currentGui = Minecraft.m_91087_().f_91080_;
        this.renderGui = true;
        this.renderHud = true;
        this.expired = false;
        this.width = Minecraft.m_91087_().m_91268_().m_85443_();
        this.height = Minecraft.m_91087_().m_91268_().m_85444_();
    }

    public void render(GuiGraphics gfx, float partialTime) {
    }

    public void update() {
        this.prevX = this.x;
        this.prevY = this.y;
    }

    public void setExpired() {
        this.expired = true;
    }

    public void resize(ScaledResolution prev, ScaledResolution nev) {
        this.x = OTEffect.handleResizeXd(this.x, prev, nev);
        this.prevX = OTEffect.handleResizeXd(this.prevX, prev, nev);
        this.y = OTEffect.handleResizeYd(this.y, prev, nev);
        this.prevY = OTEffect.handleResizeYd(this.prevY, prev, nev);
        this.width = Minecraft.m_91087_().m_91268_().m_85443_();
        this.height = Minecraft.m_91087_().m_91268_().m_85444_();
    }

    public void setWhiteColor() {
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    protected static double handleResizeXd(double x, ScaledResolution prev, ScaledResolution nev) {
        return x / prev.getScaledWidth_double() * nev.getScaledWidth_double();
    }

    protected static double handleResizeYd(double y, ScaledResolution prev, ScaledResolution nev) {
        return y / prev.getScaledHeight_double() * nev.getScaledHeight_double();
    }

    protected static int handleResizeXi(int x, ScaledResolution prev, ScaledResolution nev) {
        return x / prev.getScaledWidth() * nev.getScaledWidth();
    }

    protected static int handleResizeYi(int y, ScaledResolution prev, ScaledResolution nev) {
        return y / prev.getScaledHeight() * nev.getScaledHeight();
    }

    protected static int[] handleResizeXiv(int[] x, ScaledResolution prev, ScaledResolution nev) {
        int[] v = (int[])x.clone();
        for (int i = 0; i < v.length; ++i) {
            v[i] = OTEffect.handleResizeXi(x[i], prev, nev);
        }
        return v;
    }

    protected static int[] handleResizeYiv(int[] y, ScaledResolution prev, ScaledResolution nev) {
        int[] v = (int[])y.clone();
        for (int i = 0; i < v.length; ++i) {
            v[i] = OTEffect.handleResizeYi(y[i], prev, nev);
        }
        return v;
    }

    protected static double[] handleResizeXdv(double[] x, ScaledResolution prev, ScaledResolution nev) {
        double[] v = (double[])x.clone();
        for (int i = 0; i < v.length; ++i) {
            v[i] = OTEffect.handleResizeXd(x[i], prev, nev);
        }
        return v;
    }

    protected static double[] handleResizeYdv(double[] y, ScaledResolution prev, ScaledResolution nev) {
        double[] v = (double[])y.clone();
        for (int i = 0; i < v.length; ++i) {
            v[i] = OTEffect.handleResizeYd(y[i], prev, nev);
        }
        return v;
    }
}

