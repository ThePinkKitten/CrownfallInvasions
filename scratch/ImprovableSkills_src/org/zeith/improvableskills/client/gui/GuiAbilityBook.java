/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.entity.player.Player
 *  org.zeith.hammerlib.client.utils.UV
 */
package org.zeith.improvableskills.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.zeith.hammerlib.client.utils.UV;
import org.zeith.improvableskills.ImprovableSkills;
import org.zeith.improvableskills.api.OwnedTexture;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.api.client.IClientAbilityExtensions;
import org.zeith.improvableskills.api.client.ISlotRenderer;
import org.zeith.improvableskills.api.registry.PlayerAbilityBase;
import org.zeith.improvableskills.client.gui.GuiBaseBookBrowser;
import org.zeith.improvableskills.client.gui.GuiSkillViewer;
import org.zeith.improvableskills.custom.pagelets.PageletAbilities;

public class GuiAbilityBook
extends GuiBaseBookBrowser<AbilityTxInstance, PageletAbilities> {
    public GuiAbilityBook(PageletAbilities pagelet, PlayerSkillData data) {
        super(pagelet, data);
    }

    @Override
    protected void provideElements(Consumer<AbilityTxInstance> handler) {
        ImprovableSkills.ABILITIES().getValues().stream().sorted(Comparator.comparing(t -> t.getLocalizedName(this.data).getString())).filter(this.data::hasAbility).forEach(ab -> handler.accept(new AbilityTxInstance(ab.tex)));
    }

    public class AbilityTxInstance
    implements GuiBaseBookBrowser.ITxInstance {
        final OwnedTexture<PlayerAbilityBase> tex;

        public AbilityTxInstance(OwnedTexture<PlayerAbilityBase> tex) {
            this.tex = tex;
        }

        @Override
        public UV getHoverUV() {
            return this.tex.toUV(true);
        }

        @Override
        public void drawUV(GuiGraphics gfx, float x, float y, float width, float height, float hoverProgress, float partialTicks) {
            ISlotRenderer sr = IClientAbilityExtensions.of((PlayerAbilityBase)this.tex.owner).slotRenderer();
            if (sr.drawSlot(gfx, x, y, width, height, hoverProgress, partialTicks)) {
                return;
            }
            PoseStack pose = gfx.m_280168_();
            UV norm = this.tex.toUV(false);
            norm.render(pose, (double)x, (double)y, width, height);
            if (hoverProgress > 0.0f) {
                UV hov = this.tex.toUV(true);
                gfx.m_280246_(1.0f, 1.0f, 1.0f, hoverProgress);
                hov.render(pose, (double)x, (double)y, width, height);
                gfx.m_280246_(1.0f, 1.0f, 1.0f, 1.0f);
            }
        }

        @Override
        public List<Component> getHoverTooltip() {
            return List.of(((PlayerAbilityBase)this.tex.owner).getLocalizedName());
        }

        @Override
        public GuiBaseBookBrowser.ClickFeedback onMouseClicked(int button) {
            ((PlayerAbilityBase)this.tex.owner).onClickClient((Player)((GuiAbilityBook)GuiAbilityBook.this).f_96541_.f_91074_, button);
            return new GuiBaseBookBrowser.ClickFeedback(true, true, true);
        }

        @Override
        public void renderDecorations(GuiGraphics gfx, float hoverProgress, double x, double y, float partialTicks) {
            PoseStack pose = gfx.m_280168_();
            if (((PlayerAbilityBase)this.tex.owner).showDisabledIcon(GuiAbilityBook.this.data)) {
                GuiSkillViewer.CROSS.render(pose, x + 9.5, y + 21.0, 5.0f, 5.0f);
            }
        }
    }
}

