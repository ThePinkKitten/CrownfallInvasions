/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Button
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  org.zeith.hammerlib.client.utils.UV
 */
package org.zeith.improvableskills.client.rendering.ote;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.zeith.hammerlib.client.utils.UV;
import org.zeith.improvableskills.client.gui.base.GuiCustomButton;
import org.zeith.improvableskills.client.rendering.OTEffect;
import org.zeith.improvableskills.client.rendering.OnTopEffects;
import org.zeith.improvableskills.init.ItemsIS;

public class OTEFadeOutButton
extends OTEffect {
    public ItemStack item = new ItemStack((ItemLike)ItemsIS.SKILLS_BOOK);
    private int totTime;
    private int prevTime;
    private int time;
    private Button uv;

    public OTEFadeOutButton(Button uv, int time) {
        this.renderHud = false;
        this.uv = uv;
        this.totTime = time;
        this.x = this.prevX = this.x;
        this.y = this.prevY = this.y;
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
        Minecraft mc = Minecraft.m_91087_();
        Font fontrenderer = mc.f_91062_;
        ResourceLocation rl = this.uv instanceof GuiCustomButton ? new ResourceLocation("improvableskills", "textures/gui/icons.png") : new ResourceLocation("minecraft", "textures/gui/widgets.png");
        float scale = 1.0f + (float)Math.sqrt(t);
        int a = (int)((1.0f - t / (float)this.totTime) * 0.75f * 255.0f);
        pose.m_85836_();
        RenderSystem.enableBlend();
        int i = !this.uv.f_93623_ ? 0 : (this.uv.m_5953_((double)this.mouseX, (double)this.mouseY) ? 2 : 1);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)((float)a / 255.0f));
        float yo = this.uv instanceof GuiCustomButton ? 0.0f : 46.0f;
        new UV(rl, 0.0f, yo + (float)(i * 20), (float)(this.uv.m_5711_() / 2) - scale / 2.0f, (float)this.uv.m_93694_()).render(pose, (double)((float)this.uv.m_252754_() - scale / 2.0f), (double)((float)this.uv.m_252907_() - scale / 2.0f), (float)(this.uv.m_5711_() / 2) + scale / 2.0f, (float)this.uv.m_93694_() + scale);
        new UV(rl, (float)(200 - this.uv.m_5711_() / 2) + scale / 2.0f, yo + (float)(i * 20), (float)(this.uv.m_5711_() / 2) - scale / 2.0f, (float)this.uv.m_93694_()).render(pose, (double)(this.uv.m_252754_() + this.uv.m_5711_() / 2), (double)((float)this.uv.m_252907_() - scale / 2.0f), (float)(this.uv.m_5711_() / 2) + scale / 2.0f, (float)this.uv.m_93694_() + scale);
        int j = 0xE0E0E0;
        int fg = this.uv.getFGColor();
        if (fg != 0) {
            j = fg;
        } else if (!this.uv.f_93623_) {
            j = 0xA0A0A0;
        } else if (this.uv.m_5953_((double)this.mouseX, (double)this.mouseY)) {
            j = 0xFFFFA0;
        }
        this.setWhiteColor();
        Component component = this.uv.m_6035_();
        int n = this.uv.m_252754_() + (this.uv.m_5711_() - fontrenderer.m_92852_((FormattedText)this.uv.m_6035_())) / 2;
        int n2 = this.uv.m_252907_();
        int n3 = this.uv.m_93694_();
        Objects.requireNonNull(fontrenderer);
        gfx.m_280614_(fontrenderer, component, n, n2 + (n3 - 9) / 2 + 1, a << 24 | j, true);
        this.setWhiteColor();
        pose.m_85849_();
    }
}

