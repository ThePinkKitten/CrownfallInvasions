/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent
 */
package org.zeith.improvableskills.client.rendering.tooltip;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import org.zeith.improvableskills.api.client.IClientSkillExtensions;
import org.zeith.improvableskills.api.tooltip.SkillTooltip;

public class SkillTooltipRenderer
implements ClientTooltipComponent {
    private final SkillTooltip tooltip;

    public SkillTooltipRenderer(SkillTooltip tooltip) {
        this.tooltip = tooltip;
    }

    public int m_142103_() {
        return 24;
    }

    public int m_142069_(Font font) {
        return 24;
    }

    public void m_183452_(Font font, int x, int y, GuiGraphics gfx) {
        if (IClientSkillExtensions.of(this.tooltip.skill()).slotRenderer().drawSlot(gfx, x, y - 1, 24.0f, 24.0f, 0.0f, 1.0f)) {
            return;
        }
        this.tooltip.skill().tex.toUV(false).render(gfx.m_280168_(), (double)x, (double)(y - 1), 24.0f, 24.0f);
    }
}

