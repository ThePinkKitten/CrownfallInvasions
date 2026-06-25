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
import org.zeith.improvableskills.api.OwnedTexture;
import org.zeith.improvableskills.api.client.IClientAbilityExtensions;
import org.zeith.improvableskills.api.registry.PlayerAbilityBase;
import org.zeith.improvableskills.api.tooltip.AbilityTooltip;

public class AbilityTooltipRenderer
implements ClientTooltipComponent {
    private final AbilityTooltip tooltip;

    public AbilityTooltipRenderer(AbilityTooltip tooltip) {
        this.tooltip = tooltip;
    }

    public int m_142103_() {
        return 24;
    }

    public int m_142069_(Font font) {
        return 24;
    }

    public void m_183452_(Font font, int x, int y, GuiGraphics gfx) {
        if (IClientAbilityExtensions.of(this.tooltip.ability()).slotRenderer().drawSlot(gfx, x, y - 1, 24.0f, 24.0f, 1.0f, 1.0f)) {
            return;
        }
        OwnedTexture<PlayerAbilityBase> tx = this.tooltip.ability().tex;
        tx.toUV(false).render(gfx.m_280168_(), (double)x, (double)(y - 1), 24.0f, 24.0f);
        tx.toUV(true).render(gfx.m_280168_(), (double)x, (double)(y - 1), 24.0f, 24.0f);
    }
}

