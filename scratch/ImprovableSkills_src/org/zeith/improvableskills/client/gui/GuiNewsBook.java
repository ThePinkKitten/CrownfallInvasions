/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Joiner
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.FormattedCharSequence
 *  net.minecraft.util.Mth
 *  org.zeith.hammerlib.client.utils.Scissors
 *  org.zeith.hammerlib.client.utils.UV
 *  org.zeith.hammerlib.util.colors.Rainbow
 *  org.zeith.hammerlib.util.java.Hashers
 *  org.zeith.hammerlib.util.java.Threading
 */
package org.zeith.improvableskills.client.gui;

import com.google.common.base.Joiner;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.zeith.hammerlib.client.utils.Scissors;
import org.zeith.hammerlib.client.utils.UV;
import org.zeith.hammerlib.util.colors.Rainbow;
import org.zeith.hammerlib.util.java.Hashers;
import org.zeith.hammerlib.util.java.Threading;
import org.zeith.improvableskills.client.gui.base.GuiTabbable;
import org.zeith.improvableskills.client.rendering.OnTopEffects;
import org.zeith.improvableskills.client.rendering.ote.OTESparkle;
import org.zeith.improvableskills.custom.pagelets.PageletNews;
import org.zeith.improvableskills.data.ClientData;
import org.zeith.improvableskills.utils.GoogleTranslate;

public class GuiNewsBook
extends GuiTabbable<PageletNews> {
    public final UV gui1;
    public Component changes;
    public Component translated;
    protected int prevScroll;
    protected int scroll;
    protected int maxScroll;
    protected double dScroll;

    public GuiNewsBook(PageletNews pagelet) {
        super(pagelet);
        this.gui1 = new UV(new ResourceLocation("improvableskills", "textures/gui/skills_gui_paper.png"), 0.0f, 0.0f, (float)this.xSize, (float)this.ySize);
        this.reload();
    }

    public String getOrTranslate(String changes) {
        if (changes == null) {
            changes = "";
        }
        String sha = Hashers.SHA256.hashify(changes);
        String lng = Minecraft.m_91087_().getLocale().getLanguage();
        String stored = ClientData.readData("news_" + lng + ".sha").orElse(null);
        String olng = ClientData.readData("news_" + lng + ".txt").orElse(null);
        if (!sha.equalsIgnoreCase(stored) || olng == null) {
            ArrayList<String> s = new ArrayList<String>();
            for (String ln : changes.split("\n")) {
                try {
                    if (!lng.equals("en")) {
                        ln = GoogleTranslate.translate(lng, ln);
                    }
                }
                catch (IOException iOException) {
                    // empty catch block
                }
                s.add(ln);
            }
            String ts = Joiner.on((String)"\n").join(s);
            ClientData.writeData("news.sha", sha);
            ClientData.writeData("news_" + lng + ".sha", sha);
            ClientData.writeData("news_" + lng + ".txt", ts);
            try {
                Field f = PageletNews.class.getDeclaredField("popping");
                f.setAccessible(true);
                f.setBoolean(this.pagelet, false);
            }
            catch (Exception exception) {
                // empty catch block
            }
            return ts;
        }
        return olng;
    }

    public void reload() {
        this.changes = null;
        this.translated = null;
        Threading.createAndStart(() -> {
            String ch = ((PageletNews)this.pagelet).getChanges();
            this.changes = ch == null ? Component.m_237113_((String)"") : Component.m_237113_((String)ch);
            this.translated = Component.m_237113_((String)this.getOrTranslate(((PageletNews)this.pagelet).getChanges()));
        });
    }

    @Override
    protected void drawBack(GuiGraphics gfx, float partialTicks, int mouseX, int mouseY) {
        String s;
        PoseStack pose = gfx.m_280168_();
        this.setWhiteColor(gfx);
        this.gui1.render(pose, (float)this.guiLeft, (float)this.guiTop);
        RenderSystem.enableBlend();
        Scissors.begin((int)this.guiLeft, (int)(this.guiTop + 5), (int)this.xSize, (int)(this.ySize - 10));
        Component translated = this.translated;
        if ((GuiNewsBook.m_96638_() || GuiNewsBook.m_96639_() || GuiNewsBook.m_96637_()) && (s = ((PageletNews)this.pagelet).getChanges()) != null) {
            translated = Component.m_237113_((String)s);
        }
        if (translated != null) {
            pose.m_85836_();
            pose.m_252880_(0.0f, Mth.m_14179_((float)this.f_96541_.getPartialTick(), (float)this.prevScroll, (float)this.scroll), 0.0f);
            this.maxScroll = 0;
            for (FormattedCharSequence formattedcharsequence : this.f_96547_.m_92923_((FormattedText)translated, (int)this.gui1.width - 22)) {
                gfx.m_280649_(this.f_96547_, formattedcharsequence, this.guiLeft + 12, this.guiTop + 12, -16777216, false);
                pose.m_252880_(0.0f, 9.0f, 0.0f);
                this.maxScroll += 9;
            }
            this.maxScroll = Math.max(0, this.maxScroll - (this.ySize - 36));
            pose.m_85849_();
        } else {
            GuiNewsBook.spawnLoading(this.f_96543_, this.f_96544_);
        }
        RenderSystem.enableDepthTest();
        Scissors.end();
        this.setBlueColor(gfx);
        pose.m_85836_();
        pose.m_252880_(0.0f, 0.0f, 5.0f);
        this.gui2.render(pose, (float)this.guiLeft, (float)this.guiTop);
        pose.m_85849_();
        this.setWhiteColor(gfx);
    }

    public static void spawnLoading(float width, float height) {
        Minecraft mc = Minecraft.m_91087_();
        float partialTicks = mc.getPartialTick();
        int dots = 3;
        float angle = 360 / dots;
        float degree = ((float)mc.f_91074_.f_19797_ + partialTicks) * 3.0f % 360.0f;
        float x = width / 2.0f;
        float y = height / 2.0f;
        float rad = 48.0f;
        for (int i = 0; i < dots; ++i) {
            double ax = (double)x + Math.sin(Math.toRadians(degree)) * (double)rad;
            double ay = (double)y + Math.cos(Math.toRadians(degree)) * (double)rad;
            double oax = (double)x + Math.sin(Math.toRadians(degree - 30.0f)) * (double)rad;
            double oay = (double)y + Math.cos(Math.toRadians(degree - 30.0f)) * (double)rad;
            if (Math.random() < 0.25) {
                OnTopEffects.effects.add(new OTESparkle(ax, ay, oax, oay, 20, 0xFF000000 | Rainbow.doIt((long)(i * 1000 / dots), (long)1000L)));
            }
            degree += angle;
        }
    }

    @Override
    public void m_86600_() {
        super.m_86600_();
        this.prevScroll = this.scroll;
        this.scroll = (int)((double)this.scroll + this.dScroll);
        this.scroll = Mth.m_14045_((int)this.scroll, (int)(-this.maxScroll), (int)0);
        this.dScroll = 0.0;
    }

    public boolean m_6050_(double i, double j, double k) {
        Objects.requireNonNull(this.f_96547_);
        this.dScroll += k * 9.0;
        return super.m_6050_(i, j, k);
    }
}

