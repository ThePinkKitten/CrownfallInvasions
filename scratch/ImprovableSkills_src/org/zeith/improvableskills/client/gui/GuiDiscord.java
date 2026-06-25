/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.renderer.texture.AbstractTexture
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.FormattedCharSequence
 *  org.zeith.hammerlib.client.texture.HttpTextureDownloader
 *  org.zeith.hammerlib.client.utils.RenderUtils
 *  org.zeith.hammerlib.client.utils.UV
 *  org.zeith.hammerlib.util.ZeithLinkRepository
 *  org.zeith.hammerlib.util.ZeithLinkRepository$PredefinedLink
 */
package org.zeith.improvableskills.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Optional;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.FormattedCharSequence;
import org.zeith.hammerlib.client.texture.HttpTextureDownloader;
import org.zeith.hammerlib.client.utils.RenderUtils;
import org.zeith.hammerlib.client.utils.UV;
import org.zeith.hammerlib.util.ZeithLinkRepository;
import org.zeith.improvableskills.client.gui.GuiNewsBook;
import org.zeith.improvableskills.client.gui.PageletDiscord;
import org.zeith.improvableskills.client.gui.base.GuiTabbable;
import org.zeith.improvableskills.client.rendering.ote.OTEConfetti;
import org.zeith.improvableskills.init.SoundsIS;
import org.zeith.improvableskills.utils.Sys;

public class GuiDiscord
extends GuiTabbable<PageletDiscord> {
    public static boolean texureLoaded = false;
    public final UV gui1;
    private static final ResourceLocation texture = new ResourceLocation("improvableskills", "textures/builtin/discord.png");
    public int hoverTime;
    public boolean hovered;

    public static AbstractTexture getDiscordServerIdTexture() {
        return HttpTextureDownloader.create((ResourceLocation)texture, (String)ZeithLinkRepository.getLink((ZeithLinkRepository.PredefinedLink)ZeithLinkRepository.PredefinedLink.DEV_DISCORD_CARD_IMAGE), () -> {
            texureLoaded = true;
        });
    }

    public GuiDiscord(PageletDiscord pagelet) {
        super(pagelet);
        this.gui1 = new UV(new ResourceLocation("improvableskills", "textures/gui/skills_gui_paper.png"), 0.0f, 0.0f, (float)this.xSize, (float)this.ySize);
        GuiDiscord.getDiscordServerIdTexture();
    }

    @Override
    public void m_86600_() {
        super.m_86600_();
        if (this.hovered && this.hoverTime < 10) {
            ++this.hoverTime;
        }
        if (!this.hovered && this.hoverTime > 0) {
            --this.hoverTime;
        }
    }

    @Override
    protected void drawBack(GuiGraphics gfx, float partialTicks, int mouseX, int mouseY) {
        PoseStack pose = gfx.m_280168_();
        this.setWhiteColor(gfx);
        this.gui1.render(pose, (float)this.guiLeft, (float)this.guiTop);
        this.hovered = (double)mouseX >= (double)this.guiLeft + ((double)this.xSize - (double)(3 * this.xSize) / 3.5) / 2.0 && (double)mouseY >= (double)this.guiTop + ((double)this.ySize - (double)this.xSize / 3.5) - 22.0 && (double)mouseX < (double)this.guiLeft + ((double)this.xSize - (double)(3 * this.xSize) / 3.5) / 2.0 + (double)(3 * this.xSize) / 3.5 && (double)mouseY < (double)this.guiTop + ((double)this.ySize - (double)this.xSize / 3.5) - 22.0 + (double)this.xSize / 3.5;
        boolean mouse = this.hovered;
        Optional.ofNullable(GuiDiscord.getDiscordServerIdTexture()).ifPresent(AbstractTexture::m_117966_);
        if (texureLoaded) {
            float m = 0.67f + 0.33f * OTEConfetti.sineF((float)this.hoverTime / 10.0f);
            RenderSystem.setShaderColor((float)m, (float)m, (float)m, (float)1.0f);
            RenderUtils.drawFullTexturedModalRect((GuiGraphics)gfx, (float)((float)this.guiLeft + ((float)this.xSize - (float)(3 * this.xSize) / 3.5f) / 2.0f), (float)((float)this.guiTop + ((float)this.ySize - (float)this.xSize / 3.5f) - 22.0f), (float)((float)(3 * this.xSize) / 3.5f), (float)((float)this.xSize / 3.5f));
            pose.m_85836_();
            for (FormattedCharSequence formattedcharsequence : this.f_96547_.m_92923_((FormattedText)Component.m_237115_((String)"pagelet.improvableskills:discord2"), this.xSize - 21)) {
                gfx.m_280649_(this.f_96547_, formattedcharsequence, this.guiLeft + 13, this.guiTop + 12, -16777216, false);
                pose.m_252880_(0.0f, 9.0f, 0.0f);
            }
            pose.m_85849_();
        } else {
            GuiNewsBook.spawnLoading(this.f_96543_, this.f_96544_);
        }
        this.setBlueColor(gfx);
        pose.m_85836_();
        pose.m_252880_(0.0f, 0.0f, 5.0f);
        this.gui2.render(pose, (float)this.guiLeft, (float)this.guiTop);
        pose.m_85849_();
    }

    @Override
    public boolean m_6375_(double mouseX, double mouseY, int mouseButton) {
        boolean mouse;
        boolean bl = mouse = mouseX >= (double)this.guiLeft + ((double)this.xSize - (double)(3 * this.xSize) / 3.5) / 2.0 && mouseY >= (double)this.guiTop + ((double)this.ySize - (double)this.xSize / 3.5) - 22.0 && mouseX < (double)this.guiLeft + ((double)this.xSize - (double)(3 * this.xSize) / 3.5) / 2.0 + (double)(3 * this.xSize) / 3.5 && mouseY < (double)this.guiTop + ((double)this.ySize - (double)this.xSize / 3.5) - 22.0 + (double)this.xSize / 3.5;
        if (mouse) {
            Sys.openURL(ZeithLinkRepository.getLink((ZeithLinkRepository.PredefinedLink)ZeithLinkRepository.PredefinedLink.DEV_DISCORD_INVITE));
            this.f_96541_.m_91106_().m_120367_((SoundInstance)SimpleSoundInstance.m_119752_((SoundEvent)SoundsIS.CONNECT, (float)1.0f));
            return true;
        }
        return super.m_6375_(mouseX, mouseY, mouseButton);
    }
}

