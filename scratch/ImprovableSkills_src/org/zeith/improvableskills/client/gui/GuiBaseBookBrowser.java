/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  org.zeith.hammerlib.client.utils.Scissors
 *  org.zeith.hammerlib.client.utils.TexturePixelGetter
 *  org.zeith.hammerlib.client.utils.UV
 *  org.zeith.hammerlib.util.java.tuples.Tuple2$Mutable2
 *  org.zeith.hammerlib.util.java.tuples.Tuples
 */
package org.zeith.improvableskills.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.zeith.hammerlib.client.utils.Scissors;
import org.zeith.hammerlib.client.utils.TexturePixelGetter;
import org.zeith.hammerlib.client.utils.UV;
import org.zeith.hammerlib.util.java.tuples.Tuple2;
import org.zeith.hammerlib.util.java.tuples.Tuples;
import org.zeith.improvableskills.ImprovableSkills;
import org.zeith.improvableskills.api.IGuiSkillDataConsumer;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.api.registry.PageletBase;
import org.zeith.improvableskills.client.gui.base.GuiTabbable;
import org.zeith.improvableskills.client.rendering.OnTopEffects;
import org.zeith.improvableskills.client.rendering.ote.OTEFadeOutUV;
import org.zeith.improvableskills.client.rendering.ote.OTESparkle;
import org.zeith.improvableskills.client.rendering.ote.OTETooltip;
import org.zeith.improvableskills.init.SoundsIS;

public abstract class GuiBaseBookBrowser<TX extends ITxInstance, P extends PageletBase>
extends GuiTabbable<P>
implements IGuiSkillDataConsumer {
    public static final ResourceLocation PAPER_TEXTURE = ImprovableSkills.id("textures/gui/skills_gui_paper.png");
    protected final Rectangle scissorRect = new Rectangle();
    public float scrolledPixels;
    public float prevScrolledPixels;
    public int row = 6;
    public Map<TX, Tuple2.Mutable2<Integer, Integer>> hoverAnims = new HashMap<TX, Tuple2.Mutable2<Integer, Integer>>();
    public int currentHover;
    protected final List<TX> texes = new ArrayList<TX>();
    protected PlayerSkillData data;
    protected double dWheel;

    public GuiBaseBookBrowser(P pagelet, PlayerSkillData data) {
        super(pagelet);
        this.data = data;
        this.xSize = 195;
        this.ySize = 168;
        this.provideElements(this.texes::add);
    }

    @Override
    public void applySkillData(PlayerSkillData data) {
        this.data = data;
    }

    protected abstract void provideElements(Consumer<TX> var1);

    @Override
    protected void drawBack(GuiGraphics gfx, float partialTicks, int mouseX, int mouseY) {
        PoseStack pose = gfx.m_280168_();
        this.setWhiteColor(gfx);
        this.gui1.render(pose, (float)this.guiLeft, (float)this.guiTop);
        int co = this.texes.size();
        RenderSystem.enableBlend();
        this.scissorRect.setBounds(this.guiLeft, this.guiTop + 5, this.xSize, this.ySize - 10);
        Scissors.begin((int)this.guiLeft, (int)(this.guiTop + 5), (int)this.xSize, (int)(this.ySize - 10));
        int cht = 0;
        int chtni = 0;
        boolean singleHover = false;
        for (int i = 0; i < co; ++i) {
            boolean hover;
            int j = i % co;
            ITxInstance tex = (ITxInstance)this.texes.get(j);
            Tuple2.Mutable2 hovt = this.hoverAnims.get(tex);
            if (hovt == null) {
                hovt = new Tuple2.Mutable2((Object)0, (Object)0);
                this.hoverAnims.put(tex, (Tuple2.Mutable2<Integer, Integer>)hovt);
            }
            int cHoverTime = (Integer)hovt.a();
            int cHoverTimePrev = (Integer)hovt.b();
            float x = i % this.row * 28 + this.guiLeft + 16;
            float y = (float)(i / this.row * 28) - (this.prevScrolledPixels + (this.scrolledPixels - this.prevScrolledPixels) * partialTicks);
            if (y < -24.0f) continue;
            if (y > (float)(this.ySize - 14)) break;
            boolean bl = hover = (float)mouseX >= x && (float)mouseX < x + 24.0f && (float)mouseY >= (y += (float)(this.guiTop + 9)) && (float)mouseY < y + 24.0f;
            if (hover) {
                this.currentHover = i;
                singleHover = true;
                chtni = cHoverTime;
            }
            float hoverProgress = 0.0f;
            if (cHoverTime > 0) {
                cht = (int)((float)cHoverTimePrev + (float)(cHoverTime - cHoverTimePrev) * partialTicks);
                hoverProgress = (float)Math.sin(Math.toRadians((float)cht / 255.0f * 90.0f));
            }
            tex.drawUV(gfx, x, y, 24.0f, 24.0f, hoverProgress, partialTicks);
            tex.renderDecorations(gfx, hoverProgress, x, y, partialTicks);
        }
        if (!singleHover) {
            this.currentHover = -1;
        }
        Scissors.end();
        this.setBlueColor(gfx);
        this.gui2.render(pose, (double)this.guiLeft, (double)this.guiTop, (float)this.xSize, (float)this.ySize);
        this.setWhiteColor(gfx);
        this.setWhiteColor(gfx);
        if (this.currentHover >= 0 && chtni >= 200) {
            OTETooltip.showTooltip(((ITxInstance)this.texes.get(this.currentHover % co)).getHoverTooltip());
        }
    }

    public boolean m_6050_(double x, double y, double dWheel) {
        this.dWheel += dWheel;
        return true;
    }

    @Override
    public void m_86600_() {
        super.m_86600_();
        this.prevScrolledPixels = this.scrolledPixels;
        int co = this.texes.size();
        float maxPixels = 28 * (co / this.row) - 112;
        int dw = (int)this.dWheel * 100;
        if (dw != 0) {
            this.dWheel = 0.0;
            this.scrolledPixels -= (float)dw * 14.0f / 100.0f;
            this.scrolledPixels = Math.max(Math.min(this.scrolledPixels, maxPixels), 0.0f);
        }
        for (int i = 0; i < co; ++i) {
            int cHoverTime;
            int j = i % co;
            ITxInstance tex = (ITxInstance)this.texes.get(j);
            Tuple2.Mutable2 hovt = this.hoverAnims.computeIfAbsent(tex, k -> Tuples.mutable((Object)0, (Object)0));
            int pht = cHoverTime = ((Integer)hovt.a()).intValue();
            if (this.currentHover == i) {
                cHoverTime = Math.min(cHoverTime + 25, 255);
                double x = i % this.row * 28 + this.guiLeft + 16;
                double y = (float)(i / this.row * 28) - this.scrolledPixels;
                y += (double)(this.guiTop + 9);
                Random r = new Random();
                if (r.nextInt(3) == 0) {
                    int[] rgbs = tex.getAllColors();
                    int col = rgbs[r.nextInt(rgbs.length)];
                    double tx = x + 2.0 + (double)(r.nextFloat() * 20.0f);
                    double ty = y + 2.0 + (double)(r.nextFloat() * 20.0f);
                    if (this.scissorRect.contains(x, y) && this.scissorRect.contains(tx, ty)) {
                        OnTopEffects.effects.add(new OTESparkle(tx, ty, tx, ty, 11, col));
                    }
                }
            } else {
                cHoverTime = Math.max(cHoverTime - 10, 0);
            }
            hovt.setA((Object)cHoverTime);
            hovt.setB((Object)pht);
        }
    }

    @Override
    public boolean m_6375_(double mouseX, double mouseY, int mouseButton) {
        if (this.currentHover >= 0) {
            ITxInstance skill = (ITxInstance)this.texes.get(this.currentHover % this.texes.size());
            ClickFeedback feedback = skill.onMouseClicked(mouseButton);
            if (feedback.spawnFadeout) {
                int co = this.texes.size();
                for (int i = 0; i < co; ++i) {
                    int j = i % co;
                    ITxInstance tex = (ITxInstance)this.texes.get(j);
                    double x = i % this.row * 28 + this.guiLeft + 16;
                    double y = (float)(i / this.row * 28) - (this.prevScrolledPixels + (this.scrolledPixels - this.prevScrolledPixels) * this.f_96541_.getPartialTick());
                    if (tex != skill) continue;
                    new OTEFadeOutUV(tex.getHoverUV(), 24.0f, 24.0f, x, y + (double)this.guiTop + 9.0, 2);
                    break;
                }
            }
            if (feedback.playSound) {
                this.f_96541_.m_91106_().m_120367_((SoundInstance)SimpleSoundInstance.m_119752_((SoundEvent)SoundsIS.PAGE_TURNS, (float)1.0f));
            }
            if (feedback.consumeClick) {
                return true;
            }
        }
        return super.m_6375_(mouseX, mouseY, mouseButton);
    }

    public static interface ITxInstance {
        public UV getHoverUV();

        public void drawUV(GuiGraphics var1, float var2, float var3, float var4, float var5, float var6, float var7);

        public List<Component> getHoverTooltip();

        default public int[] getAllColors() {
            return TexturePixelGetter.getAllColors((ResourceLocation)this.getHoverUV().path);
        }

        public void renderDecorations(GuiGraphics var1, float var2, double var3, double var5, float var7);

        public ClickFeedback onMouseClicked(int var1);
    }

    public record ClickFeedback(boolean playSound, boolean spawnFadeout, boolean consumeClick) {
    }
}

