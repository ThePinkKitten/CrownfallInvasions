/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.Window
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.world.item.ItemStack
 *  org.zeith.hammerlib.client.utils.RenderUtils
 *  org.zeith.hammerlib.client.utils.TexturePixelGetter
 */
package org.zeith.improvableskills.client.rendering.ote;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import org.zeith.hammerlib.client.utils.RenderUtils;
import org.zeith.hammerlib.client.utils.TexturePixelGetter;
import org.zeith.improvableskills.api.registry.PlayerAbilityBase;
import org.zeith.improvableskills.client.rendering.OTEffect;
import org.zeith.improvableskills.client.rendering.OnTopEffects;
import org.zeith.improvableskills.client.rendering.ote.OTEAbility;
import org.zeith.improvableskills.client.rendering.ote.OTESkillSparkle;
import org.zeith.improvableskills.utils.ScaledResolution;
import org.zeith.improvableskills.utils.Trajectory;

public class OTEItemAbilityScroll
extends OTEffect {
    public ItemStack item;
    private double tx;
    private double ty;
    private int totTime;
    private int prevTime;
    private int time;
    public double[] xPoints;
    public double[] yPoints;
    public PlayerAbilityBase[] abilities;

    public OTEItemAbilityScroll(double x, double y, double tx, double ty, int time, ItemStack item, PlayerAbilityBase ... skills) {
        this.renderGui = false;
        this.abilities = skills;
        this.totTime = time;
        this.x = this.prevX = x;
        this.y = this.prevY = y;
        this.tx = tx;
        this.ty = ty;
        this.item = item;
        double[][] path = Trajectory.makeBroken2DTrajectory(x, y, tx, ty, time, (float)(System.currentTimeMillis() % 1000000L) / 90.0f);
        this.xPoints = path[0];
        this.yPoints = path[1];
    }

    @Override
    public void resize(ScaledResolution prev, ScaledResolution nev) {
        super.resize(prev, nev);
        this.tx = OTEItemAbilityScroll.handleResizeXd(this.tx, prev, nev);
        this.ty = OTEItemAbilityScroll.handleResizeYd(this.ty, prev, nev);
        this.xPoints = OTEItemAbilityScroll.handleResizeXdv(this.xPoints, prev, nev);
        this.yPoints = OTEItemAbilityScroll.handleResizeYdv(this.yPoints, prev, nev);
    }

    @Override
    public void update() {
        super.update();
        this.prevTime = this.time;
        int tt = this.xPoints.length;
        int cframe = Math.min(Math.round((float)this.time / (float)this.totTime * (float)tt), this.xPoints.length - 1);
        this.x = this.xPoints[cframe];
        this.y = this.yPoints[cframe];
        ++this.time;
        int spawnTime = 10 * this.abilities.length;
        if (this.time >= this.totTime) {
            int cur = (this.time - this.totTime) / 10;
            if ((this.time - this.totTime) % 10 == 0 && cur < this.abilities.length) {
                Minecraft mc = Minecraft.m_91087_();
                Window sr = mc.m_91268_();
                OnTopEffects.effects.add(new OTEAbility(this.x, this.y, sr.m_85445_() - 12, sr.m_85446_() - 12, 40, this.abilities[cur]));
                mc.m_91106_().m_120367_((SoundInstance)SimpleSoundInstance.m_119752_((SoundEvent)SoundEvents.f_11887_, (float)1.0f));
            }
        } else {
            int[] rgbs;
            int lcf = Math.max(cframe - 10, 0);
            Random r = new Random();
            if (r.nextBoolean() && (rgbs = TexturePixelGetter.getAllColors((ResourceLocation)this.abilities[r.nextInt((int)this.abilities.length)].tex.toUV((boolean)true).path)).length > 0) {
                int col = rgbs[r.nextInt(rgbs.length)];
                double tx = this.xPoints[lcf] + (double)((float)(r.nextInt(16) - r.nextInt(16)) / 2.0f);
                double ty = this.yPoints[cframe] + (double)((float)(r.nextInt(16) - r.nextInt(16)) / 2.0f);
                OnTopEffects.effects.add(new OTESkillSparkle(this.x - (double)r.nextInt(8) + (double)r.nextInt(8), this.y - (double)r.nextInt(8) + (double)r.nextInt(8), tx, ty, 20, col));
            }
        }
        if (this.time >= this.totTime + spawnTime) {
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
        if (t >= (float)(this.totTime + 10 * this.abilities.length - 5)) {
            scale *= 1.0f - (t - (float)this.totTime + 5.0f - (float)(10 * this.abilities.length)) / 5.0f;
        }
        this.setWhiteColor();
        pose.m_85836_();
        pose.m_85837_(cx - (double)(16.0f * scale / 2.0f), cy - (double)(16.0f * scale / 2.0f), 0.0);
        pose.m_252880_(scale, scale, scale);
        RenderUtils.renderItemIntoGui((PoseStack)pose, (ItemStack)this.item, (int)0, (int)0);
        pose.m_85849_();
        this.setWhiteColor();
    }
}

