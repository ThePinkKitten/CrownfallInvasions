/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.math.Axis
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.renderer.GameRenderer
 *  net.minecraft.client.renderer.texture.AbstractTexture
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.core.Holder
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.world.item.ItemStack
 *  net.minecraftforge.registries.IForgeRegistry
 *  org.zeith.hammerlib.client.utils.FXUtils
 *  org.zeith.hammerlib.client.utils.RenderUtils
 *  org.zeith.hammerlib.client.utils.UV
 *  org.zeith.hammerlib.util.java.tuples.Tuple2$Mutable2
 *  org.zeith.hammerlib.util.shaded.json.JSONObject
 */
package org.zeith.improvableskills.client.gui.base;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistry;
import org.zeith.hammerlib.client.utils.FXUtils;
import org.zeith.hammerlib.client.utils.RenderUtils;
import org.zeith.hammerlib.client.utils.UV;
import org.zeith.hammerlib.util.java.tuples.Tuple2;
import org.zeith.hammerlib.util.shaded.json.JSONObject;
import org.zeith.improvableskills.ImprovableSkills;
import org.zeith.improvableskills.SyncSkills;
import org.zeith.improvableskills.api.registry.PageletBase;
import org.zeith.improvableskills.client.gui.GuiCentered;
import org.zeith.improvableskills.client.gui.base.GuiCustomButton;
import org.zeith.improvableskills.client.rendering.ote.OTEConfetti;
import org.zeith.improvableskills.client.rendering.ote.OTETooltip;
import org.zeith.improvableskills.custom.pagelets.PageletUpdate;
import org.zeith.improvableskills.init.PageletsIS;
import org.zeith.improvableskills.init.SoundsIS;
import org.zeith.improvableskills.utils.ScaledResolution;
import org.zeith.improvableskills.utils.Sys;

public class GuiTabbable<P extends PageletBase>
extends GuiCentered {
    public static final ResourceLocation ICONS = new ResourceLocation("improvableskills", "textures/gui/icons.png");
    public static PageletBase lastPagelet = PageletsIS.SKILLS;
    public static final Map<ResourceLocation, Tuple2.Mutable2<Float, Float>> EXTENSIONS = new HashMap<ResourceLocation, Tuple2.Mutable2<Float, Float>>();
    public final P pagelet;
    protected PageletBase selPgl;
    public Screen parent;
    public final UV gui1;
    public final UV gui2;
    List<Component> pageletTooltip = new ArrayList<Component>();
    protected int liveAnimationTime;
    protected boolean zeithBDay = false;
    protected final List<PageletBase> pagelets;
    int mouseX;
    int mouseY;

    public GuiTabbable(P pagelet) {
        this.pagelet = pagelet;
        lastPagelet = pagelet;
        IForgeRegistry<PageletBase> pgreg = ImprovableSkills.PAGELETS();
        this.pagelets = new ArrayList<PageletBase>(pgreg.getValues());
        this.pagelets.sort(Comparator.comparing(PageletBase::getRegistryName));
        this.xSize = 195;
        this.ySize = 168;
        this.gui1 = new UV(new ResourceLocation("improvableskills", "textures/gui/skills_gui_paper.png"), 0.0f, 0.0f, (float)this.xSize, (float)this.ySize);
        this.gui2 = new UV(new ResourceLocation("improvableskills", "textures/gui/skills_gui_overlay.png"), 0.0f, 0.0f, (float)this.xSize, (float)this.ySize);
    }

    protected void drawBack(GuiGraphics pose, float partialTicks, int mouseX, int mouseY) {
    }

    public void bindIcons() {
        FXUtils.bindTexture((ResourceLocation)ICONS);
    }

    public void m_86600_() {
        super.m_86600_();
        ScaledResolution sr = new ScaledResolution(this.f_96541_);
        int i1 = sr.getScaledWidth();
        int j1 = sr.getScaledHeight();
        boolean bl = this.zeithBDay = Calendar.getInstance().get(5) == 10 && Calendar.getInstance().get(2) == 10;
        if (this.zeithBDay) {
            int[] colors = new int[]{-65536, -39424, -256, -16711936, -16776961, -65281};
            int color = colors[colors.length - 1 - (int)(System.currentTimeMillis() % ((long)colors.length * 3000L) / 3000L) % colors.length];
            if (this.mouseX > this.f_96543_ / 2 - 16 && this.mouseY > this.guiTop - 36 && this.mouseX < this.f_96543_ / 2 + 16 && this.mouseY < this.guiTop - 4) {
                for (int i = 0; i < 4; ++i) {
                    OTEConfetti cft = new OTEConfetti(this.f_96543_ / 2, (float)(this.guiTop - 36) + OTEConfetti.random.nextFloat() * 32.0f);
                    cft.motionY = -1.25f;
                    cft.motionX = (OTEConfetti.random.nextFloat() - OTEConfetti.random.nextFloat()) * 6.0f;
                    cft.color = color;
                }
            }
        }
    }

    protected void setWhiteColor(GuiGraphics gfx) {
        gfx.m_280246_(1.0f, 1.0f, 1.0f, 1.0f);
    }

    protected void setBlueColor(GuiGraphics gfx) {
        gfx.m_280246_(0.0f, 0.53333336f, 1.0f, 1.0f);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics gfx, float partialTicks, int mouseX, int mouseY) {
        ItemStack stack;
        Object icon;
        PageletBase let;
        int j;
        ItemStack stack2;
        Object icon2;
        float dif;
        float progress;
        Tuple2.Mutable2 t;
        boolean mouseOver;
        int j2;
        AbstractTexture zeith;
        partialTicks = this.f_96541_.getPartialTick();
        PoseStack pose = gfx.m_280168_();
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.m_280273_(gfx);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        if (this.zeithBDay && (zeith = GuiCustomButton.getZeithAvatar()) != null) {
            RenderSystem.setShader(GameRenderer::m_172817_);
            zeith.m_117966_();
            pose.m_85836_();
            pose.m_252880_((float)(this.f_96543_ / 2 - 16), (float)(this.guiTop - 36), 350.0f);
            pose.m_252880_(16.0f, 16.0f, 0.0f);
            pose.m_252781_(Axis.f_252403_.m_252977_(6.0f * OTEConfetti.sineF((float)(System.currentTimeMillis() % 4000L) / 1000.0f)));
            pose.m_252880_(-16.0f, -16.0f, 0.0f);
            RenderUtils.drawFullTexturedModalRect((GuiGraphics)gfx, (float)0.0f, (float)0.0f, (float)32.0f, (float)32.0f);
            pose.m_85849_();
        }
        if (PageletUpdate.liveURL != null && (zeith = GuiCustomButton.getZeithAvatar()) != null) {
            RenderSystem.setShader(GameRenderer::m_172817_);
            zeith.m_117966_();
            Objects.requireNonNull(this.f_96547_);
            float s = 16.0f / 9.0f;
            float w = s * (float)this.f_96547_.m_92895_("LIVE");
            boolean hover = (float)mouseX >= ((float)this.f_96543_ - (w + 64.0f)) / 2.0f && (float)mouseX < ((float)this.f_96543_ - (w + 64.0f)) / 2.0f + w + 64.0f && mouseY >= this.guiTop - 36 && mouseY < this.guiTop - 4;
            RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            pose.m_85836_();
            pose.m_252880_(((float)this.f_96543_ - (w + 64.0f)) / 2.0f, (float)(this.guiTop - 36), 350.0f);
            RenderUtils.drawFullTexturedModalRect((GuiGraphics)gfx, (float)0.0f, (float)0.0f, (float)32.0f, (float)32.0f);
            pose.m_85836_();
            pose.m_252880_(32.0f, 4.0f, 0.0f);
            pose.m_85841_(s, s, 1.0f);
            gfx.m_280056_(this.f_96547_, "LIVE", 0, 3, hover ? 0xFFAAAA : 0xFFFFFF, false);
            RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            pose.m_85849_();
            FXUtils.bindTexture((String)"minecraft", (String)"textures/gui/stream_indicator.png");
            pose.m_85836_();
            pose.m_252880_(w + 32.0f, 0.0f, 0.0f);
            pose.m_85841_(0.25f, 1.0f, 1.0f);
            pose.m_85841_(0.53333336f, 0.53333336f, 1.0f);
            RenderUtils.drawTexturedModalRect((PoseStack)pose, (float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f, (float)240.0f, (float)60.0f);
            pose.m_85849_();
            pose.m_85849_();
        }
        this.selPgl = null;
        int i = 0;
        for (j2 = 0; j2 < this.pagelets.size(); ++j2) {
            PageletBase let2 = this.pagelets.get(j2);
            if (!let2.isVisible(SyncSkills.getData()) || !let2.isRight()) continue;
            boolean bl = mouseOver = mouseX >= this.guiLeft + 195 && mouseY >= this.guiTop + 10 + i * 25 && mouseX < this.guiLeft + 193 + 20 && mouseY < this.guiTop + 10 + i * 25 + 24;
            if (mouseOver) {
                this.selPgl = let2;
            }
            mouseOver |= this.pagelet == let2;
            this.gui2.bindTexture();
            t = EXTENSIONS.get(let2.getRegistryName());
            if (t == null) {
                t = new Tuple2.Mutable2((Object)Float.valueOf(0.0f), (Object)Float.valueOf(0.0f));
                EXTENSIONS.put(let2.getRegistryName(), (Tuple2.Mutable2<Float, Float>)t);
            }
            t.setA((Object)Float.valueOf(mouseOver ? 1.0f : 0.0f));
            progress = 5.0f * ((Float)t.b()).floatValue();
            dif = Math.max(-0.125f, Math.min(0.125f, ((Float)t.a()).floatValue() - ((Float)t.b()).floatValue()));
            progress += dif * partialTicks;
            progress = (float)(Math.sin(Math.toRadians((double)progress / 5.0 * 90.0)) * 5.0);
            if (progress >= 5.0f && let2 == this.pagelet) {
                ++i;
                continue;
            }
            pose.m_85836_();
            this.setBlueColor(gfx);
            pose.m_252880_((float)(this.guiLeft + 193) - 7.0f * ((5.0f - progress) / 5.0f), (float)(this.guiTop + 10 + i * 25), 0.0f);
            RenderUtils.drawTexturedModalRect((PoseStack)pose, (float)0.0f, (float)0.0f, (float)236.0f, (float)0.0f, (float)20.0f, (float)24.0f);
            pose.m_252880_(0.0f, 0.0f, -50.0f);
            icon2 = let2.getIcon();
            if (icon2 instanceof ItemStack) {
                stack2 = (ItemStack)icon2;
                RenderUtils.renderItemIntoGui((PoseStack)pose, (ItemStack)stack2, (int)2, (int)4);
            } else if (icon2 instanceof AbstractTexture) {
                AbstractTexture tex = (AbstractTexture)icon2;
                pose.m_252880_(0.0f, 0.0f, 150.0f);
                this.setWhiteColor(gfx);
                tex.m_117966_();
                RenderSystem.setShaderTexture((int)0, (int)tex.m_117963_());
                RenderUtils.drawFullTexturedModalRect((GuiGraphics)gfx, (float)2.0f, (float)4.0f, (float)16.0f, (float)16.0f);
            } else if (icon2 instanceof UV) {
                UV uv = (UV)icon2;
                pose.m_252880_(0.0f, 0.0f, 150.0f);
                this.setWhiteColor(gfx);
                uv.render(pose, 2.0, 4.0, 16.0f, 16.0f);
            }
            pose.m_85849_();
            ++i;
        }
        i = 0;
        for (j2 = 0; j2 < this.pagelets.size(); ++j2) {
            PageletBase let3 = this.pagelets.get(j2);
            if (!let3.isVisible(SyncSkills.getData()) || let3.isRight()) continue;
            boolean bl = mouseOver = mouseX >= this.guiLeft - 17 && mouseY >= this.guiTop + 10 + i * 25 && mouseX < this.guiLeft && mouseY < this.guiTop + 10 + i * 25 + 24;
            if (mouseOver) {
                this.selPgl = let3;
            }
            mouseOver |= this.pagelet == let3;
            this.gui2.bindTexture();
            t = EXTENSIONS.get(let3.getRegistryName());
            if (t == null) {
                t = new Tuple2.Mutable2((Object)Float.valueOf(0.0f), (Object)Float.valueOf(0.0f));
                EXTENSIONS.put(let3.getRegistryName(), (Tuple2.Mutable2<Float, Float>)t);
            }
            t.setA((Object)Float.valueOf(mouseOver ? 1.0f : 0.0f));
            progress = 5.0f * ((Float)t.b()).floatValue();
            dif = Math.max(-0.125f, Math.min(0.125f, ((Float)t.a()).floatValue() - ((Float)t.b()).floatValue()));
            progress += dif * partialTicks;
            progress = (float)(Math.sin(Math.toRadians((double)progress / 5.0 * 90.0)) * 5.0);
            if (progress >= 5.0f && let3 == this.pagelet) {
                ++i;
                continue;
            }
            pose.m_85836_();
            this.setBlueColor(gfx);
            pose.m_252880_((float)(this.guiLeft - 18) + 7.0f * ((5.0f - progress) / 5.0f), (float)(this.guiTop + 10 + i * 25), 0.0f);
            pose.m_85836_();
            pose.m_252880_(10.0f, 14.0f, 0.0f);
            pose.m_85841_(-1.0f, -1.0f, 1.0f);
            pose.m_252880_(-10.0f, -14.0f, 0.0f);
            RenderUtils.drawTexturedModalRect((PoseStack)pose, (float)0.0f, (float)4.0f, (float)236.0f, (float)0.0f, (float)20.0f, (float)24.0f);
            pose.m_85849_();
            pose.m_252880_(0.0f, 0.0f, -50.0f);
            icon2 = let3.getIcon();
            if (icon2 instanceof ItemStack) {
                stack2 = (ItemStack)icon2;
                RenderUtils.renderItemIntoGui((PoseStack)pose, (ItemStack)stack2, (int)2, (int)4);
            }
            if (icon2 instanceof AbstractTexture) {
                AbstractTexture tex = (AbstractTexture)icon2;
                pose.m_252880_(0.0f, 0.0f, 150.0f);
                this.setWhiteColor(gfx);
                tex.m_117966_();
                RenderSystem.setShaderTexture((int)0, (int)tex.m_117963_());
                RenderUtils.drawFullTexturedModalRect((GuiGraphics)gfx, (float)2.0f, (float)4.0f, (float)16.0f, (float)16.0f);
            } else if (icon2 instanceof UV) {
                UV uv = (UV)icon2;
                pose.m_252880_(0.0f, 0.0f, 150.0f);
                this.setWhiteColor(gfx);
                uv.render(pose, 0.0, 0.0, 16.0f, 16.0f);
            }
            pose.m_85849_();
            ++i;
        }
        this.setWhiteColor(gfx);
        pose.m_85836_();
        pose.m_252880_(0.0f, 0.0f, 100.0f);
        this.drawBack(gfx, partialTicks, mouseX, mouseY);
        pose.m_85849_();
        if (this.selPgl != null) {
            this.pageletTooltip.clear();
            this.selPgl.addTitle(this.pageletTooltip);
            OTETooltip.showTooltip(this.pageletTooltip);
        }
        Objects.requireNonNull(this.f_96547_);
        float s = 16.0f / 9.0f;
        float w = s * (float)this.f_96547_.m_92895_("LIVE");
        if (PageletUpdate.liveURL != null && (float)mouseX >= ((float)this.f_96543_ - (w + 64.0f)) / 2.0f && (float)mouseX < ((float)this.f_96543_ - (w + 64.0f)) / 2.0f + w + 64.0f && mouseY >= this.guiTop - 36 && mouseY < this.guiTop - 4) {
            OTETooltip.showTooltip(new Component[]{Component.m_237113_((String)"Zeitheron is LIVE!"), Component.m_237113_((String)JSONObject.quote((String)PageletUpdate.liveTitle)), Component.m_237113_((String)"Click to watch!")});
        } else if (this.zeithBDay && mouseX > this.f_96543_ / 2 - 16 && mouseY > this.guiTop - 36 && mouseX < this.f_96543_ / 2 + 16 && mouseY < this.guiTop - 4) {
            OTETooltip.showTooltip(new Component[]{Component.m_237113_((String)"Happy birthday, Zeitheron!")});
        }
        i = 0;
        for (j = 0; j < this.pagelets.size(); ++j) {
            boolean mouseOver2;
            let = this.pagelets.get(j);
            if (!let.isVisible(SyncSkills.getData()) || !let.isRight()) continue;
            boolean bl = mouseOver2 = mouseX >= this.guiLeft + 195 && mouseY >= this.guiTop + 10 + i * 25 && mouseX < this.guiLeft + 193 + 20 && mouseY < this.guiTop + 10 + i * 25 + 24;
            if (mouseOver2) {
                this.selPgl = let;
            }
            mouseOver2 |= this.pagelet == let;
            this.gui2.bindTexture();
            Tuple2.Mutable2 t2 = EXTENSIONS.get(let.getRegistryName());
            if (t2 == null) {
                t2 = new Tuple2.Mutable2((Object)Float.valueOf(0.0f), (Object)Float.valueOf(0.0f));
                EXTENSIONS.put(let.getRegistryName(), (Tuple2.Mutable2<Float, Float>)t2);
            }
            t2.setA((Object)Float.valueOf(mouseOver2 ? 1.0f : 0.0f));
            float progress2 = 5.0f * ((Float)t2.b()).floatValue();
            float dif2 = Math.max(-0.125f, Math.min(0.125f, ((Float)t2.a()).floatValue() - ((Float)t2.b()).floatValue()));
            progress2 += dif2 * partialTicks;
            progress2 = (float)(Math.sin(Math.toRadians((double)progress2 / 5.0 * 90.0)) * 5.0);
            if (progress2 < 5.0f || let != this.pagelet) {
                ++i;
                continue;
            }
            pose.m_85836_();
            this.setBlueColor(gfx);
            pose.m_252880_((float)(this.guiLeft + 193) - 7.0f * ((5.0f - progress2) / 5.0f), (float)(this.guiTop + 10 + i * 25), 200.0f);
            RenderUtils.drawTexturedModalRect((PoseStack)pose, (float)0.0f, (float)0.0f, (float)236.0f, (float)0.0f, (float)20.0f, (float)24.0f);
            pose.m_252880_(0.0f, 0.0f, -50.0f);
            icon = let.getIcon();
            if (icon instanceof ItemStack) {
                stack = (ItemStack)icon;
                RenderUtils.renderItemIntoGui((PoseStack)pose, (ItemStack)stack, (int)2, (int)4);
            } else if (icon instanceof AbstractTexture) {
                AbstractTexture tex = (AbstractTexture)icon;
                pose.m_252880_(0.0f, 0.0f, 150.0f);
                this.setWhiteColor(gfx);
                tex.m_117966_();
                RenderSystem.setShaderTexture((int)0, (int)tex.m_117963_());
                RenderUtils.drawFullTexturedModalRect((GuiGraphics)gfx, (float)2.0f, (float)4.0f, (float)16.0f, (float)16.0f);
            } else if (icon instanceof UV) {
                UV uv = (UV)icon;
                pose.m_252880_(0.0f, 0.0f, 150.0f);
                this.setWhiteColor(gfx);
                uv.render(pose, 2.0, 4.0, 16.0f, 16.0f);
            }
            pose.m_85849_();
            ++i;
        }
        i = 0;
        for (j = 0; j < this.pagelets.size(); ++j) {
            boolean mouseOver3;
            let = this.pagelets.get(j);
            if (!let.isVisible(SyncSkills.getData()) || let.isRight()) continue;
            boolean bl = mouseOver3 = mouseX >= this.guiLeft - 17 && mouseY >= this.guiTop + 10 + i * 25 && mouseX < this.guiLeft && mouseY < this.guiTop + 10 + i * 25 + 24;
            if (mouseOver3) {
                this.selPgl = let;
            }
            mouseOver3 |= this.pagelet == let;
            this.gui2.bindTexture();
            Tuple2.Mutable2 t3 = EXTENSIONS.get(let.getRegistryName());
            if (t3 == null) {
                t3 = new Tuple2.Mutable2((Object)Float.valueOf(0.0f), (Object)Float.valueOf(0.0f));
                EXTENSIONS.put(let.getRegistryName(), (Tuple2.Mutable2<Float, Float>)t3);
            }
            t3.setA((Object)Float.valueOf(mouseOver3 ? 1.0f : 0.0f));
            float progress3 = 5.0f * ((Float)t3.b()).floatValue();
            float dif3 = Math.max(-0.125f, Math.min(0.125f, ((Float)t3.a()).floatValue() - ((Float)t3.b()).floatValue()));
            progress3 += dif3 * partialTicks;
            progress3 = (float)(Math.sin(Math.toRadians((double)progress3 / 5.0 * 90.0)) * 5.0);
            if (progress3 < 5.0f || let != this.pagelet) {
                ++i;
                continue;
            }
            pose.m_85836_();
            this.setBlueColor(gfx);
            pose.m_252880_((float)(this.guiLeft - 18) + 7.0f * ((5.0f - progress3) / 5.0f), (float)(this.guiTop + 10 + i * 25), 200.0f);
            pose.m_85836_();
            pose.m_252880_(10.0f, 14.0f, 0.0f);
            pose.m_85841_(-1.0f, -1.0f, 1.0f);
            pose.m_252880_(-10.0f, -14.0f, 0.0f);
            RenderUtils.drawTexturedModalRect((PoseStack)pose, (float)0.0f, (float)4.0f, (float)236.0f, (float)0.0f, (float)20.0f, (float)24.0f);
            pose.m_85849_();
            pose.m_252880_(0.0f, 0.0f, -50.0f);
            icon = let.getIcon();
            if (icon instanceof ItemStack) {
                stack = (ItemStack)icon;
                RenderUtils.renderItemIntoGui((PoseStack)pose, (ItemStack)stack, (int)2, (int)4);
            }
            if (icon instanceof AbstractTexture) {
                AbstractTexture tex = (AbstractTexture)icon;
                pose.m_252880_(0.0f, 0.0f, 150.0f);
                this.setWhiteColor(gfx);
                tex.m_117966_();
                RenderSystem.setShaderTexture((int)0, (int)tex.m_117963_());
                RenderUtils.drawFullTexturedModalRect((GuiGraphics)gfx, (float)2.0f, (float)4.0f, (float)16.0f, (float)16.0f);
            } else if (icon instanceof UV) {
                UV uv = (UV)icon;
                pose.m_252880_(0.0f, 0.0f, 150.0f);
                this.setWhiteColor(gfx);
                uv.render(pose, 0.0, 0.0, 16.0f, 16.0f);
            }
            pose.m_85849_();
            ++i;
        }
    }

    public boolean m_6375_(double mouseX, double mouseY, int mouseButton) {
        if (this.selPgl != null) {
            if (this.selPgl.hasTab()) {
                if (this.pagelet != this.selPgl) {
                    this.f_96541_.m_91152_(this.selPgl.createTab(SyncSkills.getData()));
                }
            } else {
                this.selPgl.onClick();
            }
            this.f_96541_.m_91106_().m_120367_((SoundInstance)SimpleSoundInstance.m_119752_((SoundEvent)SoundsIS.PAGE_TURNS, (float)1.0f));
            return true;
        }
        Objects.requireNonNull(this.f_96547_);
        float s = 16.0f / 9.0f;
        float w = s * (float)this.f_96547_.m_92895_("LIVE");
        if (PageletUpdate.liveURL != null && mouseX >= (double)(((float)this.f_96543_ - (w + 64.0f)) / 2.0f) && mouseX < (double)(((float)this.f_96543_ - (w + 64.0f)) / 2.0f + w + 64.0f) && mouseY >= (double)(this.guiTop - 36) && mouseY < (double)(this.guiTop - 4)) {
            this.f_96541_.m_91106_().m_120367_((SoundInstance)SimpleSoundInstance.m_263171_((Holder)SoundEvents.f_12490_, (float)1.0f));
            Sys.openURL(PageletUpdate.liveURL);
            return true;
        }
        return super.m_6375_(mouseX, mouseY, mouseButton);
    }
}

