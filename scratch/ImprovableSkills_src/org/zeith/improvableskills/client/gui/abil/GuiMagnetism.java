/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.core.Holder
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.util.FormattedCharSequence
 *  net.minecraft.util.Mth
 *  org.zeith.hammerlib.client.utils.FXUtils
 *  org.zeith.hammerlib.client.utils.RenderUtils
 *  org.zeith.hammerlib.client.utils.UV
 *  org.zeith.hammerlib.net.IPacket
 *  org.zeith.hammerlib.net.Network
 */
package org.zeith.improvableskills.client.gui.abil;

import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.Rectangle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.zeith.hammerlib.client.utils.FXUtils;
import org.zeith.hammerlib.client.utils.RenderUtils;
import org.zeith.hammerlib.client.utils.UV;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.Network;
import org.zeith.improvableskills.api.IGuiSkillDataConsumer;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.client.gui.GuiCentered;
import org.zeith.improvableskills.client.gui.base.GuiCustomButton;
import org.zeith.improvableskills.client.rendering.ote.OTEFadeOutButton;
import org.zeith.improvableskills.client.rendering.ote.OTEFadeOutUV;
import org.zeith.improvableskills.net.PacketSetMagnetismData;

public class GuiMagnetism
extends GuiCentered
implements IGuiSkillDataConsumer {
    public static final ResourceLocation TEXTURE = new ResourceLocation("improvableskills", "textures/gui/magnetism.png");
    protected PlayerSkillData data;
    public final UV main;
    public final UV slider;
    protected float sliderValue;
    protected Rectangle sliderRect;
    protected boolean draggingSlider;
    protected GuiCustomButton toggleButton;

    public GuiMagnetism(PlayerSkillData data) {
        this.data = data;
        if (data != null) {
            this.sliderValue = data.magnetismRange / 8.0f;
        }
        this.setSize(176, 85);
        this.main = new UV(TEXTURE, 0.0f, 0.0f, (float)this.xSize, (float)this.ySize);
        this.slider = new UV(TEXTURE, 176.0f, 40.0f, 4.0f, 10.0f);
    }

    @Override
    protected void m_7856_() {
        super.m_7856_();
        this.sliderRect = new Rectangle(this.guiLeft + 25, this.guiTop + 53, 126, 10);
        this.draggingSlider = false;
        this.toggleButton = (GuiCustomButton)this.m_142416_((GuiEventListener)new GuiCustomButton(0, this.guiLeft + 25, this.guiTop + 25, 20, 20, (Component)Component.m_237113_((String)""), btn -> {
            this.data.magnetism = !this.data.magnetism;
            Network.sendToServer((IPacket)new PacketSetMagnetismData(this.data.magnetism));
            new OTEFadeOutButton(btn, 15);
            for (int i = 0; i < 3; ++i) {
                new OTEFadeOutUV(new UV(TEXTURE, 176.0f, this.data != null && this.data.magnetism ? 20.0f : 0.0f, 20.0f, 20.0f), 20.0f, 20.0f, btn.m_252754_(), btn.m_252907_(), 15 + i * 10);
            }
        }){

            @Override
            protected void renderBg(GuiGraphics pose, Minecraft mc, int x, int y) {
                super.renderBg(pose, mc, x, y);
                FXUtils.bindTexture((ResourceLocation)TEXTURE);
                RenderUtils.drawTexturedModalRect((GuiGraphics)pose, (float)this.m_252754_(), (float)this.m_252907_(), (float)176.0f, (float)(GuiMagnetism.this.data != null && GuiMagnetism.this.data.magnetism ? 0.0f : 20.0f), (float)20.0f, (float)20.0f);
            }
        });
    }

    public boolean m_6375_(double x, double y, int btn) {
        if (btn == 0 && this.sliderRect.contains(x, y)) {
            this.draggingSlider = true;
            return false;
        }
        return super.m_6375_(x, y, btn);
    }

    public boolean m_6348_(double x, double y, int btn) {
        if (this.draggingSlider && btn == 0) {
            float value = Mth.m_14036_((float)((float)(x - 2.0 - (double)this.sliderRect.x) / (float)(this.sliderRect.width - 4)), (float)0.0f, (float)1.0f) * 8.0f;
            Network.sendToServer((IPacket)new PacketSetMagnetismData(Float.valueOf(value)));
            if (this.data != null) {
                this.data.magnetismRange = value;
            }
            this.f_96541_.m_91106_().m_120367_((SoundInstance)SimpleSoundInstance.m_263171_((Holder)SoundEvents.f_12490_, (float)1.0f));
            this.draggingSlider = false;
        }
        return super.m_6348_(x, y, btn);
    }

    public void m_86600_() {
        super.m_86600_();
        if (!this.draggingSlider && this.data != null) {
            this.sliderValue = this.data.magnetismRange / 8.0f;
        }
    }

    public Component getInformation(float range) {
        boolean magnetic = this.data != null && this.data.magnetism;
        String rangeS = "%.01f".formatted(Float.valueOf(range));
        return !magnetic ? Component.m_237115_((String)"text.improvableskills.magnetism.off") : Component.m_237110_((String)"text.improvableskills.magnetism.on", (Object[])new Object[]{Component.m_237113_((String)rangeS)});
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics gfx, float partialTime, int mouseX, int mouseY) {
        PoseStack pose = gfx.m_280168_();
        float value = this.data != null ? this.data.magnetismRange : 0.0f;
        this.m_280273_(gfx);
        boolean bl = this.toggleButton.f_93623_ = this.data != null;
        if (this.draggingSlider) {
            value = Mth.m_14036_((float)((float)(mouseX - 2 - this.sliderRect.x) / (float)(this.sliderRect.width - 4)), (float)0.0f, (float)1.0f) * 8.0f;
            this.sliderValue = value / 8.0f;
        }
        this.main.render(pose, (float)this.guiLeft, (float)this.guiTop);
        this.slider.render(pose, (float)(this.guiLeft + 26) + 120.0f * this.sliderValue, (float)(this.guiTop + 53));
        pose.m_85836_();
        float scale = 0.75f;
        pose.m_252880_((float)(this.guiLeft + 48), (float)this.guiTop + 25.25f, 0.0f);
        pose.m_85841_(scale, scale, scale);
        int width = 102;
        width = (int)((float)width / scale);
        int y = 0;
        for (FormattedCharSequence comp : this.f_96547_.m_92923_((FormattedText)this.getInformation(value), width)) {
            gfx.m_280649_(this.f_96547_, comp, 0, y, 0xFFFFFF, true);
            y += 9;
        }
        pose.m_85849_();
    }

    @Override
    public void applySkillData(PlayerSkillData data) {
        this.data = data;
    }
}

