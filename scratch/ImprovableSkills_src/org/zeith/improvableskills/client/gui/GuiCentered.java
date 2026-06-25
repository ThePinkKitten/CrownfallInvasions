/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.InputConstants
 *  com.mojang.blaze3d.platform.InputConstants$Key
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 */
package org.zeith.improvableskills.client.gui;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class GuiCentered
extends Screen {
    protected int xSize = 176;
    protected int ySize = 166;
    protected int guiLeft;
    protected int guiTop;

    protected GuiCentered() {
        this((Component)Component.m_237113_((String)""));
    }

    protected GuiCentered(Component label) {
        super(label);
    }

    protected void setSize(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
    }

    protected void m_7856_() {
        super.m_7856_();
        this.guiLeft = (this.f_96543_ - this.xSize) / 2;
        this.guiTop = (this.f_96544_ - this.ySize) / 2;
    }

    public void m_88315_(GuiGraphics gfx, int mouseX, int mouseY, float partialTicks) {
        this.drawGuiContainerBackgroundLayer(gfx, partialTicks, mouseX, mouseY);
        PoseStack pose = gfx.m_280168_();
        pose.m_85836_();
        pose.m_252880_(0.0f, 0.0f, 100.0f);
        super.m_88315_(gfx, mouseX, mouseY, partialTicks);
        pose.m_85849_();
    }

    protected void drawGuiContainerBackgroundLayer(GuiGraphics pose, float partialTime, int mouseX, int mouseY) {
    }

    public boolean m_7933_(int p_97765_, int p_97766_, int p_97767_) {
        InputConstants.Key mouseKey = InputConstants.m_84827_((int)p_97765_, (int)p_97766_);
        if (super.m_7933_(p_97765_, p_97766_, p_97767_)) {
            return true;
        }
        if (this.f_96541_.f_91066_.f_92092_.isActiveAndMatches(mouseKey)) {
            this.m_7379_();
            return true;
        }
        return false;
    }

    public boolean m_7043_() {
        return false;
    }
}

