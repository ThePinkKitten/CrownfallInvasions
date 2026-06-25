/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.Lighting
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  org.zeith.hammerlib.client.utils.RenderUtils
 */
package org.zeith.improvableskills.client.rendering.ote;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.zeith.hammerlib.client.utils.RenderUtils;
import org.zeith.improvableskills.client.rendering.OTEffect;
import org.zeith.improvableskills.client.rendering.OnTopEffects;
import org.zeith.improvableskills.init.ItemsIS;
import org.zeith.improvableskills.utils.ScaledResolution;

public class OTEBook
extends OTEffect {
    public ItemStack item = new ItemStack((ItemLike)ItemsIS.SKILLS_BOOK);
    private double tx;
    private double ty;
    private int totTime;
    private int prevTime;
    private int time;
    private static OTEBook book;

    public static void show(int time) {
        if (time == 0) {
            if (book != null && !OTEBook.book.expired) {
                OTEBook.book.totTime = time + 8;
            }
            return;
        }
        if (book != null && !OTEBook.book.expired) {
            OTEBook.book.totTime = Math.max(OTEBook.book.totTime, time);
            OTEBook.book.prevTime = OTEBook.book.time = Math.min(5, OTEBook.book.time);
        } else {
            Minecraft mc = Minecraft.m_91087_();
            ScaledResolution scaledresolution = new ScaledResolution(mc);
            int w = scaledresolution.getScaledWidth();
            int h = scaledresolution.getScaledHeight();
            new OTEBook(w - 12, h - 12, time);
        }
    }

    public OTEBook(double x, double y, int time) {
        this.renderGui = false;
        this.totTime = time;
        this.x = this.prevX = x;
        this.y = this.prevY = y;
        OnTopEffects.effects.add(this);
    }

    @Override
    public void resize(ScaledResolution prev, ScaledResolution nev) {
        super.resize(prev, nev);
        this.tx = OTEBook.handleResizeXd(this.tx, prev, nev);
        this.ty = OTEBook.handleResizeYd(this.ty, prev, nev);
    }

    @Override
    public void update() {
        super.update();
        this.prevTime = this.time++;
        if (this.time >= this.totTime) {
            this.setExpired();
            book = null;
        } else {
            book = this;
        }
    }

    @Override
    public void render(GuiGraphics gfx, float partialTime) {
        PoseStack pose = gfx.m_280168_();
        double cx = this.prevX + (this.x - this.prevX) * (double)partialTime;
        double cy = this.prevY + (this.y - this.prevY) * (double)partialTime;
        float t = (float)this.prevTime + partialTime;
        Lighting.m_84930_();
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

