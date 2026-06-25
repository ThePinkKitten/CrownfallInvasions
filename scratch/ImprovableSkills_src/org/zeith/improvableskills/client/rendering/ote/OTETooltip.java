/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.network.chat.Component
 *  org.zeith.hammerlib.client.utils.FXUtils
 */
package org.zeith.improvableskills.client.rendering.ote;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.zeith.hammerlib.client.utils.FXUtils;
import org.zeith.improvableskills.client.rendering.OTEffect;
import org.zeith.improvableskills.client.rendering.OnTopEffects;

public class OTETooltip
extends OTEffect {
    public final List<Component> tooltip = new ArrayList<Component>();
    private int time;
    private static OTETooltip cinst;

    public OTETooltip() {
        OnTopEffects.effects.add(this);
    }

    public static void showTooltip(Component ... tip) {
        OTETooltip.showTooltip(Arrays.asList(tip));
    }

    public static void showTooltip(List<Component> tip) {
        if (cinst == null) {
            cinst = new OTETooltip();
        }
        OTETooltip.cinst.tooltip.clear();
        OTETooltip.cinst.tooltip.addAll(tip);
        OTETooltip.cinst.time = 0;
        if (OnTopEffects.effects.indexOf(cinst) != OnTopEffects.effects.size() - 1) {
            OnTopEffects.effects.remove(cinst);
            OnTopEffects.effects.add(cinst);
        }
    }

    @Override
    public void update() {
        if (this.time++ >= 8) {
            this.setExpired();
        } else {
            cinst = this;
            if (OnTopEffects.effects.indexOf(this) != OnTopEffects.effects.size() - 1) {
                OnTopEffects.effects.remove(this);
                OnTopEffects.effects.add(this);
            }
        }
    }

    @Override
    public void setExpired() {
        super.setExpired();
        cinst = null;
    }

    @Override
    public void render(GuiGraphics gfx, float partialTime) {
        PoseStack pose = gfx.m_280168_();
        if (!this.tooltip.isEmpty()) {
            pose.m_85836_();
            pose.m_252880_(0.0f, 0.0f, 200.0f);
            FXUtils.setPositionTexShader();
            RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            gfx.m_280677_(Minecraft.m_91087_().f_91062_, this.tooltip, Optional.empty(), this.mouseX, this.mouseY);
            pose.m_85849_();
            this.tooltip.clear();
        }
    }
}

