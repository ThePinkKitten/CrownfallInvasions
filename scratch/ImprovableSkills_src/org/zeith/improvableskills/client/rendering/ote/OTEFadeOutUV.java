/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.Lighting
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  org.zeith.hammerlib.client.utils.UV
 */
package org.zeith.improvableskills.client.rendering.ote;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.zeith.hammerlib.client.utils.UV;
import org.zeith.improvableskills.client.rendering.OTEffect;
import org.zeith.improvableskills.client.rendering.OnTopEffects;
import org.zeith.improvableskills.init.ItemsIS;

public class OTEFadeOutUV
extends OTEffect {
    public ItemStack item = new ItemStack((ItemLike)ItemsIS.SKILLS_BOOK);
    private float w;
    private float h;
    private int totTime;
    private int prevTime;
    private int time;
    private UV uv;

    public OTEFadeOutUV(UV uv, float w, float h, double x, double y, int time) {
        this.renderHud = false;
        this.uv = uv;
        this.totTime = time;
        this.x = this.prevX = x;
        this.y = this.prevY = y;
        this.w = w;
        this.h = h;
        OnTopEffects.effects.add(this);
    }

    @Override
    public void update() {
        super.update();
        this.prevTime = this.time++;
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
        RenderSystem.enableBlend();
        Lighting.m_84930_();
        float scale = 1.0f + (float)Math.sqrt(t);
        RenderSystem.blendFunc((int)770, (int)1);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)((1.0f - t / (float)this.totTime) * 0.75f));
        this.uv.render(pose, this.x - (double)(scale / 2.0f), this.y - (double)(scale / 2.0f), this.w + scale, this.h + scale);
        this.setWhiteColor();
        RenderSystem.blendFunc((int)770, (int)771);
    }
}

