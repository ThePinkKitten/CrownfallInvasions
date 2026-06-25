/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.resources.language.I18n
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.util.FormattedCharSequence
 *  org.zeith.hammerlib.client.utils.Scissors
 *  org.zeith.hammerlib.util.java.Hashers
 *  org.zeith.hammerlib.util.java.Threading
 */
package org.zeith.improvableskills.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import org.zeith.hammerlib.client.utils.Scissors;
import org.zeith.hammerlib.util.java.Hashers;
import org.zeith.hammerlib.util.java.Threading;
import org.zeith.improvableskills.client.gui.GuiNewsBook;
import org.zeith.improvableskills.client.gui.base.GuiTabbable;
import org.zeith.improvableskills.custom.pagelets.PageletUpdate;
import org.zeith.improvableskills.data.ClientData;
import org.zeith.improvableskills.utils.GoogleTranslate;
import org.zeith.improvableskills.utils.Sys;

public class GuiUpdateBook
extends GuiTabbable<PageletUpdate> {
    public int scroll;
    public MutableComponent changes;
    public MutableComponent translated;
    private static Map<String, String> cache = new HashMap<String, String>();

    public GuiUpdateBook(PageletUpdate pagelet) {
        super(pagelet);
        this.reload();
    }

    public String getOrTranslate(String changes) {
        if (cache.containsKey(changes)) {
            return cache.get(changes);
        }
        String sha256 = Hashers.SHA256.hashify(changes);
        String lng = Minecraft.m_91087_().getLocale().getLanguage();
        String lang = "update_" + lng + ".txt";
        String langSHA = "update_" + lng + ".sha";
        String stored = ClientData.readData(langSHA).orElse(null);
        String translatedCache = ClientData.readData(lang).orElse(null);
        if (!sha256.equalsIgnoreCase(stored) || translatedCache == null) {
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
            String ts = String.join((CharSequence)"\n", s);
            ClientData.writeData(langSHA, sha256);
            ClientData.writeData(lang, ts);
            return ts;
        }
        return translatedCache;
    }

    public void reload() {
        this.changes = null;
        this.translated = null;
        Threading.createAndStart(() -> {
            ((PageletUpdate)this.pagelet).reload();
            ((PageletUpdate)this.pagelet).joinReload();
            String ts = PageletUpdate.changes;
            this.changes = Component.m_237113_((String)ts);
            try {
                String c = "\u25ba ";
                ts = this.getOrTranslate(PageletUpdate.changes).replace("\n\n", "\n").replace("\n", "\n" + c);
            }
            catch (Throwable er) {
                er.printStackTrace();
            }
            this.translated = Component.m_237113_((String)ts);
        });
    }

    /*
     * Unable to fully structure code
     */
    @Override
    protected void drawBack(GuiGraphics gfx, float partialTicks, int mouseX, int mouseY) {
        pose = gfx.m_280168_();
        this.setWhiteColor(gfx);
        this.gui1.render(pose, (float)this.guiLeft, (float)this.guiTop);
        RenderSystem.enableBlend();
        Scissors.begin((int)this.guiLeft, (int)(this.guiTop + 5), (int)this.xSize, (int)(this.ySize - 10));
        upd = Component.m_237113_((String)(I18n.m_118938_((String)"gui.improvableskills:nver", (Object[])new Object[0]) + ": " + PageletUpdate.latest));
        if (mouseY < this.guiTop + 8 || mouseY >= this.guiTop + this.ySize - 11 || mouseX < this.guiLeft + 16 || mouseY < this.guiTop + 11 - this.scroll || mouseX >= this.guiLeft + 16 + this.f_96547_.m_92852_((FormattedText)upd)) ** GOTO lbl-1000
        Objects.requireNonNull(this.f_96547_);
        if (mouseY < this.guiTop + 11 - this.scroll + 9) {
            v0 = true;
        } else lbl-1000:
        // 2 sources

        {
            v0 = dwnHover = false;
        }
        if (this.translated != null) {
            pose.m_85836_();
            comp = upd.m_130944_(new ChatFormatting[]{dwnHover != false ? ChatFormatting.BLUE : ChatFormatting.RESET, ChatFormatting.UNDERLINE});
            for (FormattedCharSequence formattedcharsequence : this.f_96547_.m_92923_((FormattedText)comp, (int)this.gui1.width - 22)) {
                gfx.m_280649_(this.f_96547_, formattedcharsequence, this.guiLeft + 16, this.guiTop + 11 - this.scroll, -16777216, false);
                pose.m_252880_(0.0f, 9.0f, 0.0f);
            }
            comp = this.translated;
            for (FormattedCharSequence formattedcharsequence : this.f_96547_.m_92923_((FormattedText)comp, (int)this.gui1.width - 22)) {
                gfx.m_280649_(this.f_96547_, formattedcharsequence, this.guiLeft + 12, this.guiTop + 12 - this.scroll, -16777216, false);
                pose.m_252880_(0.0f, 9.0f, 0.0f);
            }
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

    /*
     * Unable to fully structure code
     */
    @Override
    public boolean m_6375_(double mouseX, double mouseY, int mouseButton) {
        upd = Component.m_237113_((String)(I18n.m_118938_((String)"gui.improvableskills:nver", (Object[])new Object[0]) + ": " + PageletUpdate.latest));
        if (!(mouseY >= (double)(this.guiTop + 8)) || !(mouseY < (double)(this.guiTop + this.ySize - 11)) || !(mouseX >= (double)(this.guiLeft + 16)) || !(mouseY >= (double)(this.guiTop + 11 - this.scroll)) || !(mouseX < (double)(this.guiLeft + 16 + this.f_96547_.m_92852_((FormattedText)upd)))) ** GOTO lbl-1000
        Objects.requireNonNull(this.f_96547_);
        if (mouseY < (double)(this.guiTop + 11 - this.scroll + 9)) {
            v0 = true;
        } else lbl-1000:
        // 2 sources

        {
            v0 = dwnHover = false;
        }
        if (dwnHover) {
            this.f_96541_.m_91106_().m_120367_((SoundInstance)SimpleSoundInstance.m_119752_((SoundEvent)SoundEvents.f_12470_, (float)1.0f));
            Sys.openURL(PageletUpdate.homepage + "/files");
            return true;
        }
        return super.m_6375_(mouseX, mouseY, mouseButton);
    }

    public boolean m_6050_(double x, double y, double dWheel) {
        int dw = (int)(dWheel * 120.0 / -30.0);
        if (dw != 0) {
            this.scroll += dw;
            int n = this.f_96547_.m_92923_((FormattedText)this.translated, (int)this.gui1.width - 22).size();
            Objects.requireNonNull(this.f_96547_);
            int totHe = Math.max(n * 9 - (this.ySize - 36), 0);
            this.scroll = Math.min(Math.max(0, this.scroll), totHe);
        }
        return true;
    }
}

