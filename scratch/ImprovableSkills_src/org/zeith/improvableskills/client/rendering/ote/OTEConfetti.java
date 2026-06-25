/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.nbt.StringTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.level.ItemLike
 *  org.zeith.hammerlib.client.utils.FXUtils
 *  org.zeith.hammerlib.client.utils.RenderUtils
 *  org.zeith.hammerlib.util.colors.ColorHelper
 */
package org.zeith.improvableskills.client.rendering.ote;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Random;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.zeith.hammerlib.client.utils.FXUtils;
import org.zeith.hammerlib.client.utils.RenderUtils;
import org.zeith.hammerlib.util.colors.ColorHelper;
import org.zeith.improvableskills.client.rendering.OTEffect;
import org.zeith.improvableskills.client.rendering.OnTopEffects;

public class OTEConfetti
extends OTEffect {
    public static final Random random = new Random();
    public int color = 0xFF000000 | ColorHelper.packRGB((float)Math.max(0.5f, random.nextFloat()), (float)Math.max(0.5f, random.nextFloat()), (float)Math.max(0.5f, random.nextFloat()));
    public int ticksExisted;
    public float motionX;
    public float motionY;

    public static float sineF(float val) {
        return (float)Math.sin(Math.toRadians(val * 90.0f));
    }

    public static ItemStack getSkull(String player) {
        ItemStack stack = new ItemStack((ItemLike)Items.f_42680_, 1);
        stack.m_41700_("SkullOwner", (Tag)StringTag.m_129297_((String)player));
        return stack;
    }

    public OTEConfetti(double x, double y) {
        this.x = this.prevX = x;
        this.y = this.prevY = y;
        this.renderHud = false;
        OnTopEffects.effects.add(this);
    }

    @Override
    public void update() {
        super.update();
        ++this.ticksExisted;
        this.x += (double)this.motionX;
        this.y += (double)this.motionY;
        this.motionY = (float)((double)this.motionY + 0.05);
        this.motionX = (float)((double)this.motionX * 0.98535735);
        this.motionY = (float)((double)this.motionY * 0.98535735);
        int ma = 160 - Math.abs(this.hashCode()) % 40;
        if (this.ticksExisted >= ma || this.y < -8.0 || this.x < -8.0 || this.y > this.height || this.x > this.width) {
            this.setExpired();
        }
    }

    @Override
    public void render(GuiGraphics gfx, float partialTime) {
        PoseStack pose = gfx.m_280168_();
        float alpha = OTEConfetti.sineF((40.0f - (float)this.ticksExisted - partialTime) / 40.0f);
        int ma = 160 - Math.abs(this.hashCode()) % 40;
        double cx = this.prevX + (this.x - this.prevX) * (double)partialTime;
        double cy = this.prevY + (this.y - this.prevY) * (double)partialTime;
        float t = (float)this.ticksExisted + partialTime;
        float r = (float)(System.currentTimeMillis() % 2000L) / 2000.0f;
        r = r > 0.5f ? 1.0f - r : r;
        r += 0.45f;
        FXUtils.bindTexture((String)"improvableskills", (String)"textures/particles/sparkle.png");
        int tx = 64 * (int)((float)this.ticksExisted / (float)ma * 3.0f);
        RenderSystem.enableBlend();
        float scale = 0.125f;
        if (t < 5.0f) {
            scale *= t / 5.0f;
        }
        if (t >= (float)(ma - 5)) {
            scale *= 1.0f - (t - (float)ma + 5.0f) / 5.0f;
        }
        RenderSystem.setShaderColor((float)ColorHelper.getRed((int)this.color), (float)ColorHelper.getGreen((int)this.color), (float)ColorHelper.getBlue((int)this.color), (float)(0.9f * ColorHelper.getAlpha((int)this.color)));
        for (int i = 0; i < 3; ++i) {
            float ps = i == 0 ? scale : (i == 2 ? (float)((Math.sin((float)(this.hashCode() % 90) + t / 2.0f) + 1.0) / 2.5 * (double)scale) : scale / 2.0f);
            pose.m_85836_();
            RenderSystem.blendFunc((int)770, (int)(i == 0 ? 771 : 772));
            pose.m_85837_(cx - (double)(64.0f * ps / 2.0f), cy - (double)(64.0f * ps / 2.0f), 0.0);
            pose.m_85841_(ps, ps, ps);
            RenderUtils.drawTexturedModalRect((PoseStack)pose, (float)0.0f, (float)0.0f, (float)tx, (float)0.0f, (float)64.0f, (float)64.0f);
            pose.m_85849_();
        }
        RenderSystem.defaultBlendFunc();
        this.setWhiteColor();
    }
}

