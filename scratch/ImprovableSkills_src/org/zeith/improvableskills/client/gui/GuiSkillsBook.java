/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.item.ItemStack
 *  org.zeith.hammerlib.client.utils.RenderUtils
 *  org.zeith.hammerlib.client.utils.UV
 *  org.zeith.hammerlib.net.IPacket
 *  org.zeith.hammerlib.net.Network
 */
package org.zeith.improvableskills.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.zeith.hammerlib.client.utils.RenderUtils;
import org.zeith.hammerlib.client.utils.UV;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.Network;
import org.zeith.improvableskills.ImprovableSkills;
import org.zeith.improvableskills.api.OwnedTexture;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.api.client.IClientSkillExtensions;
import org.zeith.improvableskills.api.client.ISlotRenderer;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;
import org.zeith.improvableskills.client.gui.GuiBaseBookBrowser;
import org.zeith.improvableskills.client.gui.GuiSkillViewer;
import org.zeith.improvableskills.custom.items.ItemSkillScroll;
import org.zeith.improvableskills.custom.pagelets.PageletSkills;
import org.zeith.improvableskills.net.PacketSetSkillActivity;

public class GuiSkillsBook
extends GuiBaseBookBrowser<SkillTxInstance, PageletSkills> {
    public final UV medal;
    public final UV inactivity;

    public GuiSkillsBook(PageletSkills pagelet, PlayerSkillData data) {
        super(pagelet, data);
        this.medal = new UV(GuiSkillViewer.TEXTURE, (float)(this.xSize + 1), 0.0f, 10.0f, 10.0f);
        this.inactivity = GuiSkillViewer.CROSS;
    }

    @Override
    protected void provideElements(Consumer<SkillTxInstance> handler) {
        ImprovableSkills.SKILLS().getValues().stream().sorted(Comparator.comparing(t -> t.getLocalizedName(this.data).getString())).filter(skill -> skill.isVisible(this.data)).forEach(skill -> handler.accept(new SkillTxInstance(skill.tex)));
    }

    public class SkillTxInstance
    implements GuiBaseBookBrowser.ITxInstance {
        final OwnedTexture<PlayerSkillBase> tex;

        public SkillTxInstance(OwnedTexture<PlayerSkillBase> tex) {
            this.tex = tex;
        }

        @Override
        public UV getHoverUV() {
            return this.tex.toUV(true);
        }

        @Override
        public void drawUV(GuiGraphics gfx, float x, float y, float width, float height, float hoverProgress, float partialTicks) {
            ISlotRenderer sr = IClientSkillExtensions.of((PlayerSkillBase)this.tex.owner).slotRenderer();
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
            return List.of(((PlayerSkillBase)this.tex.owner).getLocalizedName());
        }

        @Override
        public GuiBaseBookBrowser.ClickFeedback onMouseClicked(int button) {
            if (button == 0) {
                GuiSkillsBook.this.f_96541_.pushGuiLayer((Screen)new GuiSkillViewer(GuiSkillsBook.this, (PlayerSkillBase)this.tex.owner));
            } else if (button == 1) {
                boolean newState = !GuiSkillsBook.this.data.isSkillActive((PlayerSkillBase)this.tex.owner);
                GuiSkillsBook.this.data.setSkillState((PlayerSkillBase)this.tex.owner, newState);
                Network.sendToServer((IPacket)new PacketSetSkillActivity(((PlayerSkillBase)this.tex.owner).getRegistryName(), newState));
            }
            return new GuiBaseBookBrowser.ClickFeedback(true, true, true);
        }

        @Override
        public void renderDecorations(GuiGraphics gfx, float hoverProgress, double x, double y, float partialTicks) {
            PoseStack pose = gfx.m_280168_();
            if (GuiSkillsBook.this.data.getSkillLevel((PlayerSkillBase)this.tex.owner) >= ((PlayerSkillBase)this.tex.owner).getMaxLevel()) {
                GuiSkillsBook.this.medal.render(pose, x + 15.0, y + 17.0, 10.0f, 10.0f);
            }
            if (!GuiSkillsBook.this.data.isSkillActive((PlayerSkillBase)this.tex.owner)) {
                GuiSkillsBook.this.inactivity.render(pose, x + 9.5, y + 21.0, 5.0f, 5.0f);
            }
            if (((PlayerSkillBase)this.tex.owner).getScrollState().hasScroll()) {
                pose.m_85836_();
                pose.m_85837_(x + 0.5, y + 19.5, 0.0);
                pose.m_85841_(0.5f, 0.5f, 1.0f);
                RenderSystem.enableDepthTest();
                RenderUtils.renderItemIntoGui((PoseStack)pose, (ItemStack)ItemSkillScroll.of((PlayerSkillBase)this.tex.owner), (int)0, (int)0);
                RenderSystem.disableDepthTest();
                pose.m_85849_();
            }
        }
    }
}

